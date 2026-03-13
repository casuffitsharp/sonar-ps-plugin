#!/bin/bash
set -e

SONAR_URL="http://sonarqube:9000"
PROJECT_KEY="sample-project-ps"
PROJECT_NAME="Sample PowerShell Project"

echo "Waiting for SonarQube to be fully ready..."
# The healthcheck in compose file ensures the container is up, but let's be safe
while ! curl -s -f "$SONAR_URL/api/system/status" | grep -q '"status":"UP"'; do
    sleep 2
done

echo "SonarQube is UP!"

echo "Setting up admin password..."
MAX_RETRIES=5
RETRY_COUNT=0
PASSWORD_CHANGED=false

while [ $RETRY_COUNT -lt $MAX_RETRIES ]; do
  # Check if we can already authenticate with the NEW password
  HTTP_STATUS_AUTH=$(curl -s -o /dev/null -w "%{http_code}" -u admin:AdminPassword123! "$SONAR_URL/api/users/current")
  if [ "$HTTP_STATUS_AUTH" = "200" ]; then
    echo "Admin password already set correctly."
    PASSWORD_CHANGED=true
    break
  fi

  echo "Attempting to change admin password (attempt $((RETRY_COUNT+1))/$MAX_RETRIES)..."
  # Try to change password from default 'admin'
  HTTP_STATUS=$(curl -s -o /dev/null -w "%{http_code}" -u admin:admin -X POST "$SONAR_URL/api/users/change_password" \
       -d "login=admin" \
       -d "previousPassword=admin" \
       -d "password=AdminPassword123!")
  
  if [ "$HTTP_STATUS" = "204" ] || [ "$HTTP_STATUS" = "200" ]; then
    echo "Password changed successfully."
    PASSWORD_CHANGED=true
    break
  fi
  
  echo "Failed to change password (HTTP $HTTP_STATUS). Retrying in 5s..."
  RETRY_COUNT=$((RETRY_COUNT+1))
  sleep 5
done

if [ "$PASSWORD_CHANGED" = "false" ]; then
  echo "Error: Failed to set admin password after $MAX_RETRIES attempts."
  exit 1
fi

echo "Creating project '$PROJECT_KEY'..."
curl -s -u admin:AdminPassword123! -X POST "$SONAR_URL/api/projects/create" \
     -d "name=$PROJECT_NAME" \
     -d "project=$PROJECT_KEY" \
     -d "mainBranch=main" || true # Ignore error if already exists

echo "SonarQube configuration finished."
