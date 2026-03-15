# sonar-ps-plugin

[![Build Status](https://dev.azure.com/casuffitsharp/sonar-ps-plugin/_apis/build/status/5?branchName=main)](https://dev.azure.com/casuffitsharp/sonar-ps-plugin/_build/latest?definitionId=5&branchName=main)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=casuffitsharp_sonar-ps-plugin&metric=coverage)](https://sonarcloud.io/summary/new_code?id=casuffitsharp_sonar-ps-plugin)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=casuffitsharp_sonar-ps-plugin&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=casuffitsharp_sonar-ps-plugin)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=casuffitsharp_sonar-ps-plugin&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=casuffitsharp_sonar-ps-plugin)
[![GitHub release](https://img.shields.io/github/v/release/casuffitsharp/sonar-ps-plugin)](https://github.com/casuffitsharp/sonar-ps-plugin/releases)
[![License](https://img.shields.io/github/license/casuffitsharp/sonar-ps-plugin)](https://github.com/casuffitsharp/sonar-ps-plugin/blob/main/LICENSE)

Repository for Powershell language plugin for SonarQube.

> **Fork notice:** This project is a fork of [gretard/sonar-ps-plugin](https://github.com/gretard/sonar-ps-plugin), which is no longer actively maintained. This fork aims to continue development and keep the plugin compatible with newer versions of SonarQube.

## Description

Currently the plugin supports:

- Reporting of issues found by [PSScriptAnalyzer](https://github.com/PowerShell/PSScriptAnalyzer)
- Cyclomatic, Cognitive, and Halstead complexity metrics
- Reporting number of lines of code and comment lines metrics

## Complexity Metrics

This plugin calculates several complexity metrics for PowerShell scripts:

- **Cyclomatic Complexity**: Measures the number of linearly independent paths through a program's source code.
- **Cognitive Complexity**: A measure of how hard the control flow of a script or file is to understand. It increments for conditionals (`if`, `switch`), loops (`for`, `foreach`, `while`), `catch` blocks, and logical operators (`-and`, `-or`, `-xor`). It also adds a nesting penalty for nested structures.
- **Halstead Metrics**: Measures structural complexity based on the number of operators and operands. It includes custom metrics for **Halstead Difficulty**, **Halstead Volume**, and **Halstead Effort**.

## Usage

1. Download and install SonarQube
2. Download the plugin from the [releases](https://github.com/casuffitsharp/sonar-ps-plugin/releases) and copy it to SonarQube's `extensions/plugins` directory
3. Start SonarQube and enable rules
4. Prepare build agent machines:

The plugin requires **PSScriptAnalyzer 1.24.0**. You can either install it manually or let the plugin handle it.

### Option A: Automatic Installation (Default)
The plugin handles the installation of the latest **PSScriptAnalyzer** (minimum 1.24.0) automatically.

### Option B: Manual Management
If you prefer to manage the module yourself, disable auto-installation and ensure the module (>= 1.24.0) is present:
- **Scanner Command**: `-Dsonar.ps.psscriptanalyzer.autoinstall=false`
- **WINDOWS**: `Install-Module -Name PSScriptAnalyzer -RequiredVersion 1.24.0 -Scope "CurrentUser" -Force`
- **LINUX**: `pwsh -Command "Install-Module -Name PSScriptAnalyzer -RequiredVersion 1.24.0 -Scope CurrentUser -Force"`

## Configuration

The following options can be overridden either in SonarQube Administration or in project configuration files:

| Property | Description | Default |
|---|---|---|
| `sonar.ps.file.suffixes` | File extensions detected as PowerShell files | `.ps1,.psm1,.psd1` |
| `sonar.ps.executable` | PowerShell executable path | `powershell.exe` |
| `sonar.ps.tokenizer.skip` | Skip tokenizer (may be time consuming) | `false` |
| `sonar.ps.tokenizer.timeout` | Max seconds to wait for tokenizer results | `3600` |
| `sonar.ps.plugin.skip` | Skip plugin entirely (no sensors run) | `false` |
| `sonar.ps.psscriptanalyzer.autoinstall` | Automatically install PSScriptAnalyzer (min 1.24.0) if missing | `true` |
| `sonar.ps.external.rules.skip` | Comma-separated `repo:ruleId` pairs to skip reporting | _(none)_ |

## Requirements

| Plugin version | SonarQube | PSScriptAnalyzer | Java |
|---|---|---|---|
| [1.0.2](https://github.com/casuffitsharp/sonar-ps-plugin/releases/tag/v1.0.2) | 26.1+ | 1.24.0+ | 21+ |
| [0.5.3](https://github.com/gretard/sonar-ps-plugin/releases/tag/0.5.3) | 8.9.2+ | 1.20+ | 17+ |
| [0.5.1](https://github.com/gretard/sonar-ps-plugin/releases/tag/0.5.1) | 8.9.2+ | 1.20+ | 11+ |
| [0.5.0](https://github.com/gretard/sonar-ps-plugin/releases/tag/0.5.0) | 6.7.7+ | 1.18.1 | 8+ |

## Building

Requirements: JDK 21+, Maven, PSScriptAnalyzer

```bash
# Build the plugin
mvn -f sonar-ps-plugin/pom.xml package
```

The built JAR will be located in `sonar-ps-plugin/target/`.

## Contributing

Contributions are welcome! Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details.

## License

This project is licensed under the GNU Lesser General Public License v3.0 — see [LICENSE](LICENSE) for details.

Original work by [gretard](https://github.com/gretard).
