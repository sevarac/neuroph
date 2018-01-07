package org.neuroph.samples.crossval;

import org.neuroph.contrib.crossvalidation.CrossValidation;
import org.neuroph.contrib.crossvalidation.CrossValidationResult;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.MultiLayerPerceptron;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class NewIrisCrossValSample {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        MultiLayerPerceptron neuralNet = new MultiLayerPerceptron(4, 16, 3);
        DataSet dataSet = DataSet.createFromFile("data_sets/iris_data_normalised.txt", 4, 3, ",");

        List<CrossValidationResult> run = new CrossValidation(neuralNet, dataSet, 10).run();

        run.forEach(System.out::println);
    }
}
