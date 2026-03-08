param (
    [Parameter(Mandatory=$true)]
    [string]$Password # Trigger PSAvoidUsingPlainTextForPassword
)

# Trigger PSAvoidUsingInvokeExpression
$command = "echo " + $Password
Invoke-Expression $command

# Additional code smell
$user = "admin"
Write-Host "Executing task for $user" # Trigger PSAvoidUsingWriteHost
