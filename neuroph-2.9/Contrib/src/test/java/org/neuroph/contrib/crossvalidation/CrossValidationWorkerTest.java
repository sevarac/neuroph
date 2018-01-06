package org.neuroph.contrib.crossvalidation;

import org.junit.Test;
import org.neuroph.contrib.crossvalidation.results.CrossValidationResult;
import org.neuroph.core.data.DataSet;
import org.neuroph.nnet.MultiLayerPerceptron;

import java.net.URL;
import java.util.concurrent.ExecutionException;

public class CrossValidationWorkerTest {
    @Test
    public void test() throws ExecutionException, InterruptedException {
        MultiLayerPerceptron neuralNet = new MultiLayerPerceptron(4, 16, 3);
        DataSet dataSet = DataSet.createFromFile(getResourceFilePath("iris_normalized.txt"), 4, 3, ",");

        CrossValidationWorker testee = new CrossValidationWorker(neuralNet, dataSet.splitIntoSubsets(15), 1);
        CrossValidationResult call = testee.call();

        System.out.println(call);
    }

    private String getResourceFilePath(String resourceName) {
        URL resource = getClass().getResource(resourceName);

        return resource.getPath();
    }
}