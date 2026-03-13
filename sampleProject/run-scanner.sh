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

HTTP_RESPONSE=$(curl -sS -u admin:AdminPassword123! -w "\n%{http_code}" \
    -X POST "$SONAR_URL/api/user_tokens/generate" \
    -d "name=$TOKEN_NAME")

HTTP_STATUS=$(echo "$HTTP_RESPONSE" | tail -n 1)
TOKEN_RESPONSE=$(echo "$HTTP_RESPONSE" | sed '$d')

TOKEN=$(echo "$TOKEN_RESPONSE" | jq -r '.token // empty' 2>/dev/null || echo "")

if [ "$HTTP_STATUS" != "200" ] || [ -z "$TOKEN" ]; then
    echo "Error: Failed to generate analysis token."
    echo "HTTP Status Code: ${HTTP_STATUS:-UNKNOWN}"
    echo "API Response Body: $TOKEN_RESPONSE"
    
    if [ "$HTTP_STATUS" = "401" ]; then
        echo "Help: Authentication failed. Verify if the admin password was correctly updated in setup-sonarqube.sh."
    fi
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
