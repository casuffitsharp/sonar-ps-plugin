# sonar-ps-plugin

[![Build Status](https://dev.azure.com/casuffitsharp/sonar-ps-plugin/_apis/build/status/5?branchName=main)](https://dev.azure.com/casuffitsharp/sonar-ps-plugin/_build/latest?definitionId=5&branchName=main)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=casuffitsharp_sonar-ps-plugin&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=casuffitsharp_sonar-ps-plugin)
[![GitHub release](https://img.shields.io/github/v/release/casuffitsharp/sonar-ps-plugin)](https://github.com/casuffitsharp/sonar-ps-plugin/releases)
[![License](https://img.shields.io/github/license/casuffitsharp/sonar-ps-plugin)](https://github.com/casuffitsharp/sonar-ps-plugin/blob/main/LICENSE)

Repository for Powershell language plugin for SonarQube.

> **Fork notice:** This project is a fork of [gretard/sonar-ps-plugin](https://github.com/gretard/sonar-ps-plugin), which is no longer actively maintained. This fork aims to continue development and keep the plugin compatible with newer versions of SonarQube.

## Description

Currently the plugin supports:

- Reporting of issues found by [PSScriptAnalyzer](https://github.com/PowerShell/PSScriptAnalyzer)
- Cyclomatic and cognitive complexity metrics
- Reporting number of lines of code and comment lines metrics

## Usage

1. Download and install SonarQube
2. Download the plugin from the [releases](https://github.com/casuffitsharp/sonar-ps-plugin/releases) and copy it to SonarQube's `extensions/plugins` directory
3. Start SonarQube and enable rules
4. Prepare build agent machines:
   - **WINDOWS**:
     - Install [PSScriptAnalyzer](https://github.com/PowerShell/PSScriptAnalyzer) on your build machine where you plan to run sonar scanner:
     - In a PowerShell terminal run (more [info](https://learn.microsoft.com/en-us/powershell/utility-modules/psscriptanalyzer/overview?view=ps-modules#installing-psscriptanalyzer)): `Install-Module -Name PSScriptAnalyzer -RequiredVersion 1.24.0 -Scope "CurrentUser" -Force`
     - Verify the module installed successfully: `Invoke-ScriptAnalyzer -ScriptDefinition '"b" = "b"; function eliminate-file () { }'`
     - You can check the [sample project](https://github.com/casuffitsharp/sonar-ps-plugin/tree/master/sampleProject) to test the plugin and verify configuration
   - **LINUX**:
     - Install PowerShell on Linux (for example Ubuntu: https://learn.microsoft.com/en-us/powershell/scripting/install/install-ubuntu?view=powershell-7.4)
     - Install PSScriptAnalyzer: `pwsh -Command "Install-Module -Name PSScriptAnalyzer -RequiredVersion 1.24.0 -Scope CurrentUser -Force"`
     - Test the module: `pwsh -Command "Invoke-ScriptAnalyzer -ScriptDefinition '\"b\" = \"b\"; function eliminate-file () { }'"`
     - Specify the `sonar.ps.executable` property to point to the PowerShell executable on Linux (find it with `whereis pwsh`): `sonar.ps.executable="/usr/bin/pwsh"`

## Configuration

The following options can be overridden either in SonarQube Administration or in project configuration files:

| Property | Description | Default |
|---|---|---|
| `sonar.ps.file.suffixes` | File extensions detected as PowerShell files | `.ps1,.psm1,.psd1` |
| `sonar.ps.executable` | PowerShell executable path | `powershell.exe` |
| `sonar.ps.tokenizer.skip` | Skip tokenizer (may be time consuming) | `false` |
| `sonar.ps.tokenizer.timeout` | Max seconds to wait for tokenizer results | `3600` |
| `sonar.ps.plugin.skip` | Skip plugin entirely (no sensors run) | `false` |
| `sonar.ps.external.rules.skip` | Comma-separated `repo:ruleId` pairs to skip reporting | _(none)_ |

## Requirements

| Plugin version | SonarQube | PSScriptAnalyzer | Java |
|---|---|---|---|
| [0.5.3](https://github.com/gretard/sonar-ps-plugin/releases/tag/0.5.3) | 8.9.2+ | 1.20+ | 17+ |
| [0.5.1](https://github.com/gretard/sonar-ps-plugin/releases/tag/0.5.1) | 8.9.2+ | 1.20+ | 11+ |
| [0.5.0](https://github.com/gretard/sonar-ps-plugin/releases/tag/0.5.0) | 6.7.7+ | 1.18.1 | 8+ |
| [0.3.0](https://github.com/gretard/sonar-ps-plugin/releases/tag/0.3.0) | 6.3+ | 1.17.1 | 8+ |

## Building

Requirements: JDK 21+, Maven, PSScriptAnalyzer

```bash
# Install PSScriptAnalyzer (required for tests)
pwsh -Command "Install-Module -Name PSScriptAnalyzer -RequiredVersion 1.24.0 -Scope CurrentUser -Force"

# Build the plugin
mvn -f sonar-ps-plugin/pom.xml package
```

The built JAR will be located in `sonar-ps-plugin/target/`.

## Contributing

Contributions are welcome! Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details.

## License

This project is licensed under the GNU Lesser General Public License v3.0 — see [LICENSE](LICENSE) for details.

Original work by [gretard](https://github.com/gretard).
