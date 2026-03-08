Sample scripts from: https://adamtheautomator.com/powershell-script-examples/

# Getting started
- Install sonar-ps plugin into SonarQube by copying the jar from [releases](https://github.com/casuffitsharp/sonar-ps-plugin/releases) into the SonarQube `./extensions/plugins` folder
- Restart the SonarQube server
- Download scanner from https://docs.sonarsource.com/sonarqube-server/analyzing-source-code/scanners/sonarscanner/
- Execute: `sonar-scanner.bat -D"sonar.login=admin" -D"sonar.password=<<PASSWORD>>"`

# Local Development using Docker Compose
To test changes to the plugin during development:
1. Build the plugin locally using Maven:
   ```bash
   mvn -f sonar-ps-plugin/pom.xml clean package
   ```
2. Start the SonarQube server and Scanner using Docker Compose from the `sampleProject` folder:
   ```bash
   docker compose up -d
   ```
   > **Note:** The `setup-sonarqube.sh` script runs automatically on container creation (via the entrypoint in `compose.yml`), handling initial SonarQube configuration such as changing the default admin password.
3. Whenever you make changes to the plugin code, repeat step 1 to build the new JAR and restart the SonarQube container to load it:
   ```bash
   docker compose restart sonarqube
   ```
4. Run the scanner container to analyze the `src` folder:
   ```bash
   docker exec sampleproject-scanner bash /usr/src/run-scanner.sh
   ```

# Accessing SonarQube
You can access the local SonarQube web UI at:
- **URL:** [http://localhost:9000](http://localhost:9000)
- **User:** `admin`
- **Password:** `AdminPassword123!` *(Note: The `setup-sonarqube.sh` script automatically changes the default `admin` password to this value on first run)*.