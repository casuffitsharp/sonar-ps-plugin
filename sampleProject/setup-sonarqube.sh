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

echo "Disabling force password change for admin..."
curl -s -u admin:admin -X POST "$SONAR_URL/api/users/change_password" \
     -d "login=admin" \
     -d "previousPassword=admin" \
     -d "password=AdminPassword123!" || true # Ignore error if already changed

echo "Creating project '$PROJECT_KEY'..."
curl -s -u admin:AdminPassword123! -X POST "$SONAR_URL/api/projects/create" \
     -d "name=$PROJECT_NAME" \
     -d "project=$PROJECT_KEY" \
     -d "mainBranch=main" || true # Ignore error if already exists

echo "SonarQube configuration finished."
