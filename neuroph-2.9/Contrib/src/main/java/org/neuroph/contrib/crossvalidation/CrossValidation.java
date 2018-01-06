package org.neuroph.contrib.crossvalidation;

import org.neuroph.contrib.crossvalidation.results.CrossValidationResult;
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
        this.dataSet = dataSet;
    }

    public Collection<CrossValidationResult> run() throws InterruptedException, ExecutionException {
        dataSet.shuffle();

        List<CrossValidationWorker> workerTasks = IntStream.range(0, numberOfFolds)
                .mapToObj(this::createWorker)
                .collect(Collectors.toList());

        ExecutorService executor = Executors.newFixedThreadPool(4);
        List<Future<CrossValidationResult>> evaluationResults = executor.invokeAll(workerTasks);
        executor.shutdown();

        List<CrossValidationResult> results = new ArrayList<>();

        for (Future<CrossValidationResult> evaluationResult : evaluationResults) {
            results.add(evaluationResult.get());
        }

        return results;
    }

    private CrossValidationWorker createWorker(int foldIndex) {
        return new CrossValidationWorker(neuralNetwork, dataSet.splitIntoSubsets(numberOfFolds), foldIndex);
    }
}
