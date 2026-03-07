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
import org.sonar.plugins.powershell.utils.ContextWriteGuard;

public class CognitiveComplexityFillerTest {

  private SensorContext context;
  private InputFile inputFile;
  private NewMeasure<Integer> measure;
  private ContextWriteGuard writeGuard;
  private CognitiveComplexityFiller sut;

  @Before
  @SuppressWarnings("unchecked")
  public void setUp() {
    context = mock(SensorContext.class);
    inputFile = mock(InputFile.class);
    measure = mock(NewMeasure.class);
    writeGuard = new ContextWriteGuard();
    sut = new CognitiveComplexityFiller();

    when(context.<Integer>newMeasure()).thenReturn(measure);
    when(measure.on(any())).thenReturn(measure);
    when(measure.forMetric(any())).thenReturn(measure);
    when(measure.withValue(any())).thenReturn(measure);
  }

  @Test
  public void shouldSaveCognitiveComplexity() {
    Tokens tokens = new Tokens();
    tokens.setCognitiveComplexity(42);

    sut.fill(context, inputFile, tokens, writeGuard);

    verify(measure).forMetric(CoreMetrics.COGNITIVE_COMPLEXITY);
    verify(measure).withValue(42);
  }
}
