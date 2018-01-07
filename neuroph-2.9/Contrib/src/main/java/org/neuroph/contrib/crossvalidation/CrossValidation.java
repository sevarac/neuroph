package org.neuroph.contrib.crossvalidation;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CrossValidation {

    private NeuralNetwork neuralNetwork;
    private DataSet dataSet;
    private int numberOfFolds;

    public CrossValidation(NeuralNetwork neuralNetwork, DataSet dataSet, int foldCount) {
        this.neuralNetwork = neuralNetwork;
        this.numberOfFolds = foldCount;
        dataSet.shuffle();
        this.dataSet = dataSet;
    }

    public List<CrossValidationResult> run() throws InterruptedException, ExecutionException {
        List<CrossValidationWorker> workerTasks = createWorkers();

        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<Future<CrossValidationResult>> evaluationResults = executor.invokeAll(workerTasks);
        executor.shutdown();

        List<CrossValidationResult> results = new ArrayList<>();
        for (Future<CrossValidationResult> evaluationResult : evaluationResults) {
            results.add(evaluationResult.get());
        }

        return results;
    }

    private List<CrossValidationWorker> createWorkers() {
        return IntStream.range(0, numberOfFolds)
                .mapToObj((foldIndex) -> new CrossValidationWorker(neuralNetwork, dataSet.splitIntoSubsets(numberOfFolds), foldIndex))
                .collect(Collectors.toList());
    }
}
