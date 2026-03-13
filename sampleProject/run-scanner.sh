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

set +e
HTTP_RESPONSE=$(curl -sS -u admin:AdminPassword123! -w "\n%{http_code}" \
    -X POST "$SONAR_URL/api/user_tokens/generate" \
    -d "name=$TOKEN_NAME")
CURL_EXIT_CODE=$?
set -e

HTTP_STATUS=$(echo "$HTTP_RESPONSE" | tail -n 1)
TOKEN_RESPONSE=$(echo "$HTTP_RESPONSE" | sed '$d')

TOKEN=$(echo "$TOKEN_RESPONSE" | jq -r '.token // empty' 2>/dev/null || echo "")

if [ "$CURL_EXIT_CODE" -ne 0 ] || [ "$HTTP_STATUS" != "200" ] || [ -z "$TOKEN" ]; then
    echo "Error: Failed to generate analysis token."
    [ "$CURL_EXIT_CODE" -ne 0 ] && echo "Curl execution failed with exit code: $CURL_EXIT_CODE"
    echo "HTTP Status Code: ${HTTP_STATUS:-UNKNOWN}"
    
    if [ "$HTTP_STATUS" != "200" ] && [ -n "$TOKEN_RESPONSE" ]; then
        echo "API Response Body: $TOKEN_RESPONSE"
    elif [ "$HTTP_STATUS" == "200" ] && [ -z "$TOKEN" ]; then
        echo "API Response: Received 200 OK but could not parse token."
    fi
    
    if [ "$HTTP_STATUS" = "401" ]; then
        echo "Help: Authentication failed. Verify if the admin password was correctly updated."
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
