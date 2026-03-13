#!/bin/bash
set -e

SONAR_URL="http://sonarqube:9000"
PROJECT_KEY="sample-project-ps"

echo "Waiting for SonarQube API to be ready..."
until curl -s -u admin:AdminPassword123! "$SONAR_URL/api/system/status" | grep -q '"status":"UP"'; do
  echo "SonarQube is not ready yet. Waiting..."
  sleep 5
done

echo "Generating analysis token..."
TOKEN_NAME="scanner-token-$(date +%s)"
TOKEN_RESPONSE=$(curl -s -u admin:AdminPassword123! -X POST "$SONAR_URL/api/user_tokens/generate" -d "name=$TOKEN_NAME")
TOKEN=$(echo "$TOKEN_RESPONSE" | jq -r '.token // empty')

if [ -z "$TOKEN" ]; then
    echo "Failed to generate token."
    echo "Response: $TOKEN_RESPONSE"
    exit 1
fi

echo "Running Sonar Scanner..."
# Uncomment the line below to enable scanner debugging (port 5006)
# export SONAR_SCANNER_OPTS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=*:5006"

cd /usr/src/sampleProject && sonar-scanner \
  -Dsonar.projectKey=$PROJECT_KEY \
  -Dsonar.sources=. \
  -Dsonar.host.url=$SONAR_URL \
  -Dsonar.token=$TOKEN

echo "Scan complete!"
