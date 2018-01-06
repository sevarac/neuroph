package org.neuroph.contrib.crossvalidation;

import org.apache.commons.lang3.SerializationUtils;
import org.neuroph.contrib.crossvalidation.results.CrossValidationResult;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;

import java.util.List;
import java.util.concurrent.Callable;

class CrossValidationWorker implements Callable<CrossValidationResult> {

    private final NeuralNetwork neuralNetwork;
    private final List<DataSet> datasetSubsets;
    private final int foldIndex;

    CrossValidationWorker(NeuralNetwork neuralNetwork, List<DataSet> datasetSubsets, int foldIndex) {
        this.neuralNetwork = neuralNetwork;
        this.datasetSubsets = datasetSubsets;
        this.foldIndex = foldIndex;
    }

    @Override
    public CrossValidationResult call() {
        NeuralNetwork neuralNet = SerializationUtils.clone(this.neuralNetwork);

        DataSet testSet = datasetSubsets.get(foldIndex);
        DataSet trainingSet = datasetSubsets.stream()
                .filter(subset -> subset != testSet)
                .reduce(new DataSet(neuralNet.getInputsCount()), (previous, next) -> {
                    previous.addAll(next);

                    return previous;
                });

        neuralNet.learn(trainingSet);

        return new CrossValidationResult(testSet, neuralNet);
    }
}
