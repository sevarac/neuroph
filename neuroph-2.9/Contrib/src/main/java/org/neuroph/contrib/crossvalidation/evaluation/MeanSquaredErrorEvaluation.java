package org.neuroph.contrib.crossvalidation.evaluation;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.learning.error.MeanSquaredError;

public final class MeanSquaredErrorEvaluation {

    public double evaluateDataSet(NeuralNetwork neuralNetwork, DataSet dataSet) {
        ErrorEvaluator errorEvaluator = new ErrorEvaluator(new MeanSquaredError());

        dataSet.forEach(dataSetRow -> {
            neuralNetwork.setInput(dataSetRow.getInput());

            neuralNetwork.calculate();

            errorEvaluator.processNetworkResult(neuralNetwork.getOutput(), dataSetRow.getDesiredOutput());
        });

        return errorEvaluator.getResult();
    }
}
