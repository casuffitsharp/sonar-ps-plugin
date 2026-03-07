package org.sonar.plugins.powershell.fillers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.measure.NewMeasure;
import org.sonar.plugins.powershell.ast.Tokens;
import org.sonar.plugins.powershell.metrics.PowershellMetrics;
import org.sonar.plugins.powershell.utils.ContextWriteGuard;

public class HalsteadComplexityFillerTest {

  private SensorContext context;
  private InputFile inputFile;

  @SuppressWarnings("rawtypes")
  private NewMeasure measure;

  private ContextWriteGuard writeGuard;
  private HalsteadComplexityFiller sut;

  @Before
  @SuppressWarnings({"unchecked", "rawtypes"})
  public void setUp() {
    context = mock(SensorContext.class);
    inputFile = mock(InputFile.class);
    measure = mock(NewMeasure.class);
    writeGuard = new ContextWriteGuard();
    sut = new HalsteadComplexityFiller();

    when(context.newMeasure()).thenReturn(measure);

    when(measure.on(any())).thenReturn(measure);
    when(measure.forMetric(any())).thenReturn(measure);
    when(measure.withValue(any())).thenReturn(measure);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void shouldCalculateComplexity() {
    Tokens tokens = new Tokens();
    // Operators
    tokens.getTokens().add(createToken("+", "Operator"));
    tokens.getTokens().add(createToken("-", "Operator"));
    // Operands
    tokens.getTokens().add(createToken("$var", "Variable"));
    tokens.getTokens().add(createToken("'string'", "StringLiteral"));

    sut.fill(context, inputFile, tokens, writeGuard);

    verify(measure).forMetric(PowershellMetrics.HALSTEAD_DIFFICULTY);
    verify(measure).forMetric(PowershellMetrics.HALSTEAD_VOLUME);
    verify(measure).forMetric(PowershellMetrics.HALSTEAD_EFFORT);

    // difficulty = 1
    // volume = 8.0
    // effort = 8.0
    verify(measure).withValue(1);
    verify(measure, org.mockito.Mockito.times(2)).withValue(8.0);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void shouldHandleEmptyOperands() {
    Tokens tokens = new Tokens();
    sut.fill(context, inputFile, tokens, writeGuard);

    verify(measure).withValue(0);
    verify(measure, org.mockito.Mockito.times(2)).withValue(0.0);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void shouldSkipComments() {
    Tokens tokens = new Tokens();
    tokens.getTokens().add(createToken("# comment", "Comment"));
    sut.fill(context, inputFile, tokens, writeGuard);

    verify(measure).withValue(0);
    verify(measure, org.mockito.Mockito.times(2)).withValue(0.0);
  }

  private Tokens.Token createToken(String text, String kind) {
    return new Tokens.Token(text, "", "", kind, "", 1, 1, 1, 1, 0, 0);
  }
}
