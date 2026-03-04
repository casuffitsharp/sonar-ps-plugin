package org.sonar.plugins.powershell.fillers;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.plugins.powershell.ast.Tokens;
import org.sonar.plugins.powershell.ast.Tokens.Token;

public class HalsteadComplexityFiller implements IFiller {
    private static final Logger LOGGER = LoggerFactory.getLogger(HalsteadComplexityFiller.class);

    private static final List<String> skipTypes = Arrays.asList("EndOfInput", "NewLine");

    private static final List<String> operandTypes = Arrays.asList("StringExpandable", "Variable", "SplattedVariable",
            "StringLiteral", "HereStringExpandable", "HereStringLiteral");

    @Override
    public void fill(final SensorContext context, final InputFile f, final Tokens tokens, ContextWriteGuard writeGuard) {
        try {
            final List<String> uniqueOperands = new LinkedList<>();
            final List<String> uniqueOperators = new LinkedList<>();
            int totalOperands = 0;

            for (final Token token : tokens.getTokens()) {
                if (skipTypes.contains(token.getKind()) || token.getText() == null) {
                    continue;
                }

                final String text = token.getText().toLowerCase();
                if (operandTypes.contains(token.getKind())) {
                    totalOperands++;
                    if (!uniqueOperands.contains(text)) {
                        uniqueOperands.add(text);
                    }
                    continue;
                }
                if (!uniqueOperators.contains(text)) {
                    uniqueOperators.add(text);
                }

            }
            int difficulty = (int) ((int) Math.ceil(uniqueOperators.size() / 2.0)
                    * ((totalOperands * 1.0) / uniqueOperands.size()));
            writeGuard.write(() -> context.<Integer>newMeasure().on(f).forMetric(CoreMetrics.COGNITIVE_COMPLEXITY).withValue(difficulty)
                    .save());

        } catch (final Throwable e) {
            LOGGER.warn("Exception while saving cognitive complexity metric", e);
        }

    }

}
