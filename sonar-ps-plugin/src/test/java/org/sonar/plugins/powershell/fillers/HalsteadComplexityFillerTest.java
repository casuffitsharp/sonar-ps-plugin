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
import org.sonar.api.measures.CoreMetrics;
import org.sonar.plugins.powershell.ast.Tokens;

public class HalsteadComplexityFillerTest {

  private SensorContext context;
  private InputFile inputFile;
  private NewMeasure<Integer> measure;
  private ContextWriteGuard writeGuard;
  private HalsteadComplexityFiller sut;

  @Before
  @SuppressWarnings("unchecked")
  public void setUp() {
    context = mock(SensorContext.class);
    inputFile = mock(InputFile.class);
    measure = mock(NewMeasure.class);
    writeGuard = new ContextWriteGuard();
    sut = new HalsteadComplexityFiller();

    when(context.<Integer>newMeasure()).thenReturn(measure);
    when(measure.on(any())).thenReturn(measure);
    when(measure.forMetric(any())).thenReturn(measure);
    when(measure.withValue(any())).thenReturn(measure);
  }

  @Test
  public void shouldCalculateComplexity() {
    Tokens tokens = new Tokens();
    // Operators
    tokens.getTokens().add(createToken("+", "Operator"));
    tokens.getTokens().add(createToken("-", "Operator"));
    // Operands
    tokens.getTokens().add(createToken("$var", "Variable"));
    tokens.getTokens().add(createToken("'string'", "StringLiteral"));

    sut.fill(context, inputFile, tokens, writeGuard);

    verify(measure).forMetric(CoreMetrics.COGNITIVE_COMPLEXITY);
    // n1 = 2 (unique operators: +, -)
    // n2 = 2 (unique operands: $var, 'string')
    // N2 = 2 (total operands)
    // difficulty = ceil(n1/2.0) * (N2/n2) = ceil(2/2.0) * (2/2) = 1 * 1 = 1
    verify(measure).withValue(1);
  }

  @Test
  public void shouldHandleEmptyOperands() {
    Tokens tokens = new Tokens();
    sut.fill(context, inputFile, tokens, writeGuard);

    verify(measure).withValue(0); // Guarded against divide-by-zero
  }

  @Test
  public void shouldSkipComments() {
    Tokens tokens = new Tokens();
    tokens.getTokens().add(createToken("# comment", "Comment"));
    sut.fill(context, inputFile, tokens, writeGuard);

    verify(measure).withValue(0);
  }

  private Tokens.Token createToken(String text, String kind) {
    return new Tokens.Token(text, "", "", kind, "", 1, 1, 1, 1, 0, 0);
  }
}
