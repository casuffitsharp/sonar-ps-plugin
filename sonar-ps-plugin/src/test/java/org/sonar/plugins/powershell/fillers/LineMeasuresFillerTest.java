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

public class LineMeasuresFillerTest {

  private SensorContext context;
  private InputFile inputFile;
  private NewMeasure<Integer> nclocMeasure;
  private NewMeasure<Integer> commentMeasure;
  private ContextWriteGuard writeGuard;
  private LineMeasuresFiller sut;

  @Before
  @SuppressWarnings("unchecked")
  public void setUp() {
    context = mock(SensorContext.class);
    inputFile = mock(InputFile.class);
    nclocMeasure = mock(NewMeasure.class);
    commentMeasure = mock(NewMeasure.class);
    writeGuard = new ContextWriteGuard();
    sut = new LineMeasuresFiller();

    when(inputFile.lines()).thenReturn(10);
    when(context.<Integer>newMeasure()).thenReturn(commentMeasure, nclocMeasure);
    when(commentMeasure.on(any())).thenReturn(commentMeasure);
    when(commentMeasure.forMetric(any())).thenReturn(commentMeasure);
    when(commentMeasure.withValue(any())).thenReturn(commentMeasure);
    when(nclocMeasure.on(any())).thenReturn(nclocMeasure);
    when(nclocMeasure.forMetric(any())).thenReturn(nclocMeasure);
    when(nclocMeasure.withValue(any())).thenReturn(nclocMeasure);
  }

  @Test
  public void shouldCountNclocAndComments() {
    Tokens tokens = new Tokens();
    // Line 1: Code
    tokens.getTokens().add(createToken("Write-Host", "Generic", 1, 1));
    // Line 2: Comment
    tokens.getTokens().add(createToken("# some comment", "Comment", 2, 2));
    // Line 3: Both (counts as code)
    tokens.getTokens().add(createToken("Get-Item", "Generic", 3, 3));
    tokens.getTokens().add(createToken("# trailing", "Comment", 3, 3));

    sut.fill(context, inputFile, tokens, writeGuard);

    verify(commentMeasure).forMetric(CoreMetrics.COMMENT_LINES);
    verify(commentMeasure).withValue(1); // Only line 2 is pure comment
    verify(nclocMeasure).forMetric(CoreMetrics.NCLOC);
    verify(nclocMeasure).withValue(2); // Lines 1 and 3 have code
  }

  @Test
  public void shouldHandleEmptyTokens() {
    Tokens tokens = new Tokens();
    sut.fill(context, inputFile, tokens, writeGuard);

    verify(commentMeasure).withValue(0);
    verify(nclocMeasure).withValue(0);
  }

  private Tokens.Token createToken(String text, String kind, int startLine, int endLine) {
    return new Tokens.Token(text, "", "", kind, "", startLine, endLine, 1, 1, 0, 0);
  }
}
