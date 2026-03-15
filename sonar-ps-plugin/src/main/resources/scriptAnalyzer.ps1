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
            Write-Output "NuGet provider not found. Installing NuGet provider..."
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

        Write-Output "Installing ${Name} module..."
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
        Write-Output "Successfully installed ${Name}."
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
        Write-Output "${moduleName} (>= $minAnalyzerVersion) not found. Attempting to install the latest version..."
        Install-RequiredModule -Name $moduleName
        $module = Get-Module -ListAvailable $moduleName | Where-Object { [version]$_.Version -ge [version]$minAnalyzerVersion } | Sort-Object Version -Descending | Select-Object -First 1
        if (-not $module) {
            Write-Error "${moduleName} installation completed but no version >= ${minAnalyzerVersion} was found."
            exit 1
        }
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
        Write-Output "No files found matching suffixes ($suffixes) in $inputDir. Generating empty output."
        $emptyResults = @() | Select-Object RuleName, Message, Line, Column, Severity, File
        ($emptyResults | ConvertTo-Xml).Save("$output")
        exit 0
    }

    Write-Output "Found $totalFiles files to analyze. Starting analysis..."
    $allIssues = @($files | Invoke-ScriptAnalyzer -ErrorAction SilentlyContinue | Where-Object { $null -ne $_ })

    Write-Output "Analysis complete. Saving results..."
    ($allIssues | Select-Object RuleName, Message, Line, Column, Severity, @{Name = 'File'; Expression = { $_.Extent.File } } | ConvertTo-Xml).Save("$output")
}
catch {
    Write-Error "Analysis failed: $($_.Exception.Message)"
    exit 1
}
