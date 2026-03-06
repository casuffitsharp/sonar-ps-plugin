package org.sonar.plugins.powershell.sensors;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.utils.TempFolder;
import org.sonar.plugins.powershell.Constants;
import org.sonar.plugins.powershell.PluginConfiguration;
import org.sonar.plugins.powershell.PowershellLanguage;
import org.sonar.plugins.powershell.utils.PowershellScriptExecutor;

public abstract class BaseSensor implements org.sonar.api.batch.sensor.Sensor {

  private static final Logger LOGGER = LoggerFactory.getLogger(BaseSensor.class);
  protected final PluginConfiguration config;

  protected BaseSensor(PluginConfiguration config) {
    this.config = config;
  }

  @Override
  public void describe(final SensorDescriptor descriptor) {
    descriptor.onlyOnLanguage(PowershellLanguage.KEY).name(this.getClass().getSimpleName());
  }

  @Override
  public void execute(final SensorContext context) {
    if (config.isPluginSkipped()) {
      LOGGER.debug("Skipping sensor as skip plugin flag is set: {}", Constants.SKIP_PLUGIN);
      return;
    }

    innerExecute(context);
  }

  protected abstract void innerExecute(final SensorContext context);

  protected File prepareScript(TempFolder folder, String resourcePath, String fileName)
      throws IOException {
    java.net.URL resource = getClass().getResource(resourcePath);
    if (resource == null) {
      throw new IOException("Resource not found: " + resourcePath);
    }
    final File scriptFile = folder.newFile("ps", fileName);
    FileUtils.copyURLToFile(resource, scriptFile);
    return scriptFile;
  }

  protected PowershellScriptExecutor.Builder createExecutor(File scriptFile) {
    String executable = config.getPowershellExecutable();

    PowershellScriptExecutor.Builder builder =
        PowershellScriptExecutor.builder().withScriptFile(scriptFile);

    if (executable != null) {
      builder.withExecutable(executable);
    }

    return builder;
  }

  protected static String read(Process process) throws IOException {
    return "input: " + read(process.getInputStream()) + " error: " + read(process.getErrorStream());
  }

  protected static String read(InputStream stream) throws IOException {
    try (final BufferedReader reader =
        new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
      final StringBuilder builder = new StringBuilder();
      String line;
      while ((line = reader.readLine()) != null) {
        builder.append(line);
        builder.append(System.lineSeparator());
      }
      return builder.toString();
    }
  }
}
