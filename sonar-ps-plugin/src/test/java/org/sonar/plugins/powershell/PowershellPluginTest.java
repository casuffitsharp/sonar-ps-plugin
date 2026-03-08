package org.sonar.plugins.powershell;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.sonar.api.Plugin;

public class PowershellPluginTest {

  // Framework types that the SonarQube DI container manages natively and do NOT
  // need @ScannerSide.
  private static final List<Class<?>> FRAMEWORK_INTERFACES =
      List.of(
          org.sonar.api.batch.sensor.Sensor.class,
          org.sonar.api.resources.Language.class,
          org.sonar.api.config.PropertyDefinition.class,
          org.sonar.api.server.rule.RulesDefinition.class,
          org.sonar.api.server.profile.BuiltInQualityProfilesDefinition.class,
          org.sonar.api.measures.Metrics.class,
          org.sonar.api.ce.measure.MeasureComputer.class);

  @Test
  public void testAllRegisteredClassesImplementExtensionPoints() {
    PowershellPlugin plugin = new PowershellPlugin();
    Plugin.Context context = mock(Plugin.Context.class);

    List<Class<?>> registeredClasses = new ArrayList<>();

    // Capture single-extension calls
    doAnswer(
            invocation -> {
              Object arg = invocation.getArgument(0);
              if (arg instanceof Class) {
                registeredClasses.add((Class<?>) arg);
              }
              return null;
            })
        .when(context)
        .addExtension(any());

    // Capture varargs extension calls by intercepting the known methods
    doAnswer(
            invocation -> {
              for (Object arg : invocation.getArguments()) {
                if (arg instanceof Class) {
                  registeredClasses.add((Class<?>) arg);
                } else if (arg instanceof Object[]) {
                  for (Object innerArg : (Object[]) arg) {
                    if (innerArg instanceof Class) {
                      registeredClasses.add((Class<?>) innerArg);
                    }
                  }
                }
              }
              return null;
            })
        .when(context)
        .addExtensions(any(), any());

    doAnswer(
            invocation -> {
              for (Object arg : invocation.getArguments()) {
                if (arg instanceof Class) {
                  registeredClasses.add((Class<?>) arg);
                } else if (arg instanceof Object[]) {
                  for (Object innerArg : (Object[]) arg) {
                    if (innerArg instanceof Class) {
                      registeredClasses.add((Class<?>) innerArg);
                    }
                  }
                }
              }
              return null;
            })
        .when(context)
        .addExtensions(any(), any(), any());

    plugin.define(context);

    List<String> missing = new ArrayList<>();
    for (Class<?> clazz : registeredClasses) {
      // Check if the registered class implements a well-known SonarQube framework
      // interface
      boolean isFrameworkType =
          FRAMEWORK_INTERFACES.stream().anyMatch(iface -> iface.isAssignableFrom(clazz));
      if (!isFrameworkType) {
        missing.add(clazz.getName());
      }
    }

    if (!missing.isEmpty()) {
      fail(
          "The following classes are registered as extensions but do not implement a "
              + "known SonarQube extension point like Sensor or RulesDefinition:\n  - "
              + String.join("\n  - ", missing)
              + "\n\nThis will cause Spring DI NoSuchBeanDefinitionException in newer SonarQube versions. "
              + "Instead of registering them, instantiate them manually in your Sensors.");
    }
  }
}
