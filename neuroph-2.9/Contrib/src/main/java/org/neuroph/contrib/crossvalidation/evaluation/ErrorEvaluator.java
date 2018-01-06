package org.neuroph.contrib.crossvalidation.evaluation;

import org.neuroph.core.learning.error.ErrorFunction;

/**
 * Calculates scalar evaluation result using ErrorFunction
 */
class ErrorEvaluator implements Evaluator<Double> {

    private final ErrorFunction errorFunction;

    ErrorEvaluator(final ErrorFunction errorFunction) {
        this.errorFunction = errorFunction;
    }

    @Override
    public void processNetworkResult(final double[] networkOutput, final double[] desiredOutput) {
        errorFunction.addPatternError(networkOutput, desiredOutput);
    }

    @Override
    public Double getResult() {
        return errorFunction.getTotalError();
    }

    @Override
    public void reset() {
        errorFunction.reset();
    }
}

