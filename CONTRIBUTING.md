# Contributing to sonar-ps-plugin

Thank you for your interest in contributing! This project is a fork of [gretard/sonar-ps-plugin](https://github.com/gretard/sonar-ps-plugin) and aims to keep the PowerShell plugin for SonarQube alive and up to date.

## How to contribute

### Reporting bugs

Please [open an issue](https://github.com/casuffitsharp/sonar-ps-plugin/issues) describing:
- What you expected to happen
- What actually happened
- Steps to reproduce
- Plugin version, SonarQube version, and OS

### Suggesting features

Open an issue with the `enhancement` label describing the feature and its motivation.

### Submitting pull requests

1. Fork the repository and create a branch from `main`.
2. Make your changes with appropriate tests.
3. Ensure the build passes: `mvn -f sonar-ps-plugin/pom.xml verify`.
4. Submit a pull request and follow our integrity standards below.

## Pull Request Standards

To maintain a high-quality project history and automate our release process, we enforce the following standards via automated checks:

### 1. Title Convention (Conventional Commits)
Pull Request titles must follow the [Conventional Commits](https://www.conventionalcommits.org) specification:

`<type>[optional scope]: <description>`

- **Types**: `feat`, `fix`, `chore`, `docs`, `refactor`, `perf`, `test`, `style`, `build`, `ci`.
- **Breaking Changes**: Add a `!` after the type (e.g., `feat!: change return type`).
- **Scopes (Optional)**: `core`, `ci`, `deps`, `rules`, `analyzer`, `maven`.

**Examples**:
- `feat(rules): add new rule for pipeline security`
- `fix: resolve NPE in IssuesFiller`
- `chore(deps): bump org.mockito to 5.23.0`

### 2. Labels
Every Pull Request must have at least one valid label from:
`feature`, `enhancement`, `bug`, `fix`, `chore`, `refactor`, `dependencies`, `documentation`, `major`, `skip-changelog`.

> [!IMPORTANT]
> Use the `major` label and the `!` suffix in the title for breaking changes to trigger a major version release.

## Development setup

**Requirements:** JDK 21+, Maven 3.x, PowerShell with PSScriptAnalyzer

```bash
# Install PSScriptAnalyzer (required for tests)
pwsh -Command "Install-Module -Name PSScriptAnalyzer -RequiredVersion 1.24.0 -Scope CurrentUser -Force"

# Build and run tests
mvn -f sonar-ps-plugin/pom.xml verify
```

## License

By contributing, you agree that your contributions will be licensed under the [GNU Lesser General Public License v3.0](LICENSE).
