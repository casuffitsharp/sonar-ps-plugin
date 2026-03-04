package org.sonar.plugins.powershell.fillers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.plugins.powershell.ast.Tokens;

public class CComplexityFiller implements IFiller {
    private static final Logger LOGGER = LoggerFactory.getLogger(CComplexityFiller.class);

    @Override
    public void fill(final SensorContext context, final InputFile f, final Tokens tokens) {
        try {
            synchronized (context) {
                context.<Integer>newMeasure().on(f).forMetric(CoreMetrics.COMPLEXITY).withValue(tokens.getComplexity())
                        .save();
            }
        } catch (final Throwable e) {
            LOGGER.warn("Exception while saving tokens", e);
        }

    }

}
