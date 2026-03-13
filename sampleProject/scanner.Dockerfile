# 1) Get the Scanner CLI with bundled JRE
FROM sonarsource/sonar-scanner-cli:12.0 AS scanner

# 2) Base image: Ubuntu 24.04 with PowerShell pre-installed
FROM mcr.microsoft.com/powershell:ubuntu-24.04

# Copy Scanner CLI from the official image
COPY --from=scanner /opt/sonar-scanner /opt/sonar-scanner

# Copy the JRE from scanner image
COPY --from=scanner /usr/lib/jvm/java-21-amazon-corretto.x86_64 /opt/java
ENV JAVA_HOME="/opt/java"
ENV PATH="${JAVA_HOME}/bin:${PATH}"

# Install required packages (curl, jq) for run-scanner.sh
RUN apt-get update && apt-get install -y --no-install-recommends \
    curl \
    jq \
    && rm -rf /var/lib/apt/lists/* \
    && ln -s /opt/sonar-scanner/bin/sonar-scanner /usr/local/bin/sonar-scanner \
    && pwsh -NoProfile -Command '$ErrorActionPreference = "Stop"; \
        Install-Module -Name PSScriptAnalyzer -RequiredVersion 1.24.0 \
        -Scope AllUsers -Force -ErrorAction Stop'
