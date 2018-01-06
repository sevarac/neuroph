package org.neuroph.contrib.crossvalidation;

import org.neuroph.contrib.crossvalidation.evaluation.MeanSquaredErrorEvaluation;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;

public class CrossValidationResult {
    private final double meanSquareError;
    private final DataSet testSet;
    private final NeuralNetwork neuralNetwork;

    public CrossValidationResult(DataSet testSet, NeuralNetwork neuralNetwork) {
        //    TODO: do we need to evaluate here? Why not get results and then evaluate separately
        this.meanSquareError = new MeanSquaredErrorEvaluation().evaluateDataSet(neuralNetwork, testSet);
        this.testSet = testSet;
        this.neuralNetwork = neuralNetwork;
    }

    public double getMeanSquareError() {
        return meanSquareError;
    }

    public DataSet getTestSet() {
        return testSet;
    }

    public NeuralNetwork getNeuralNetwork() {
        return neuralNetwork;
    }

    @Override
    public String toString() {
//        TODO: string.format
        return "EvaluationResult{" + "dataSet=" + testSet + ", meanSquareError=" + meanSquareError + "\r\n";
    }
}
