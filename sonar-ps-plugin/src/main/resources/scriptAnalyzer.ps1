param( 
    [Parameter(Mandatory = $true)][string]$inputDir,
    [Parameter(Mandatory = $true)][string]$output,
    [Parameter(Mandatory = $true)][string]$minAnalyzerVersion,
    [Parameter(Mandatory = $true)][string]$autoInstall,
    [Parameter(Mandatory = $true)][string]$suffixes
)

$moduleName = "PSScriptAnalyzer"
$isAutoInstall = [System.Convert]::ToBoolean($autoInstall)

function Install-RequiredModule {
    param([string]$Name)
    try {
        [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
        
        $provider = Get-PackageProvider -Name NuGet -ListAvailable -ErrorAction SilentlyContinue | Select-Object -First 1
        if ($null -eq $provider) {
            Write-Host "NuGet provider not found. Installing NuGet provider..."
            $providerParams = @{
                Name           = 'NuGet'
                MinimumVersion = '2.8.5.201'
                Force          = $true
                Scope          = 'CurrentUser'
                Confirm        = $false
            }
            
            $cmdProvider = Get-Command Install-PackageProvider -ErrorAction SilentlyContinue
            if ($cmdProvider -and $cmdProvider.Parameters.ContainsKey('AcceptLicense')) {
                $providerParams['AcceptLicense'] = $true
            }

            Install-PackageProvider @providerParams | Out-Null
        }

        Write-Host "Installing ${Name} module..."
        $installParams = @{
            Name         = $Name
            Scope        = 'CurrentUser'
            Force        = $true
            AllowClobber = $true
            Confirm      = $false
            ErrorAction  = 'Stop'
        }
        
        $cmd = Get-Command Install-Module -ErrorAction SilentlyContinue
        if ($cmd -and $cmd.Parameters.ContainsKey('AcceptLicense')) {
            $installParams['AcceptLicense'] = $true
        }

        Install-Module @installParams
        Write-Host "Successfully installed ${Name}."
    }
    catch {
        Write-Error "Failed to install ${Name}: $($_.Exception.Message)"
        exit 1
    }
}

# Check if any compatible version is available (>= min version)
$module = Get-Module -ListAvailable $moduleName | Where-Object { [version]$_.Version -ge [version]$minAnalyzerVersion } | Sort-Object Version -Descending | Select-Object -First 1

if (-not $module) {
    if ($isAutoInstall) {
        Write-Host "${moduleName} (>= $minAnalyzerVersion) not found. Attempting to install the latest version..."
        Install-RequiredModule -Name $moduleName
        $module = Get-Module -ListAvailable $moduleName | Sort-Object Version -Descending | Select-Object -First 1
    }
    else {
        # Check if any version at all is available
        $anyModule = Get-Module -ListAvailable $moduleName | Sort-Object Version -Descending | Select-Object -First 1
        if (-not $anyModule -or ([version]$anyModule.Version -lt [version]$minAnalyzerVersion)) {
            $versionFound = if ($anyModule) { $anyModule.Version } else { "none" }
            Write-Error "PSScriptAnalyzer version ${minAnalyzerVersion} or newer is required. Found: ${versionFound}. Please install a compatible version or set 'sonar.ps.psscriptanalyzer.autoinstall=true'."
            exit 1
        }
        $module = $anyModule
    }
}

try {
    Import-Module $moduleName -RequiredVersion $module.Version -ErrorAction Stop
    
    $include = $suffixes.Split(",") | ForEach-Object { 
        $s = $_.Trim(); 
        if (-not $s.StartsWith("*.")) { "*$s" } else { $s } 
    }

    $files = Get-ChildItem -Path $inputDir -Include $include -Recurse
    $totalFiles = $files.Count
    if ($totalFiles -eq 0) {
        Write-Warning "No files found matching suffixes ($suffixes) in $inputDir."
        exit 0
    }

    $groups = $files | Group-Object { $_.DirectoryName }
    $totalDirs = $groups.Count
    Write-Host "Found $totalFiles files in $totalDirs directories to analyze."
    
    # Check if Parallel processing is available (PS 7+)
    $canParallel = (Get-Command ForEach-Object).Parameters.ContainsKey('Parallel')

    $cpuCores = if ($env:NUMBER_OF_PROCESSORS) { [int]$env:NUMBER_OF_PROCESSORS } else { 2 }
    $dynamicThrottle = [Math]::Max(5, $cpuCores)

    if ($canParallel) {
        Write-Host "Parallel processing enabled (ThrottleLimit: $dynamicThrottle)."
    }

    $allIssues = foreach ($group in $groups) {
        $relativePath = $group.Name.Replace($inputDir, "")
        if ([string]::IsNullOrWhiteSpace($relativePath)) { $relativePath = "." }
        Write-Host "Analyzing directory: $relativePath ($($group.Count) files)"
        
        if ($canParallel) {
            # Run files in parallel within the directory
            $group.Group | ForEach-Object -Parallel {
                Invoke-ScriptAnalyzer -Path $_.FullName
            } -ThrottleLimit $dynamicThrottle
        }
        else {
            $group.Group | ForEach-Object {
                Invoke-ScriptAnalyzer -Path $_.FullName
            }
        }
    }

    Write-Host "Analysis complete. Saving results..."
    ($allIssues | Select-Object RuleName, Message, Line, Column, Severity, @{Name = 'File'; Expression = { $_.Extent.File } } | ConvertTo-Xml).Save("$output")
}
catch {
    Write-Error "Analysis failed: $($_.Exception.Message)"
    exit 1
}