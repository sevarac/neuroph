package org.neuroph.contrib.crossvalidation;

import org.junit.Test;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CrossValidationTest {

    @Test
    public void run_dataSetSplitIntoTenFolds_runsCrossValidationForEach() throws ExecutionException, InterruptedException {
        MultiLayerPerceptron neuralNet = new MultiLayerPerceptron(4, 16, 3);
        DataSet dataSet = DataSet.createFromFile(getResourceFilePath("iris_normalized.txt"), 4, 3, ",");
        List<String> datasetRows = dataSet.stream().map(DataSetRow::toString).collect(toList());
        int foldCount = 10;

        CrossValidation testee = new CrossValidation(neuralNet, dataSet, foldCount);
        List<CrossValidationResult> results = testee.run();

        List<String> rowsFromResults = results.stream()
                .map(CrossValidationResult::getTestSet)
                .flatMap(Collection::stream)
                .map(DataSetRow::toString)
                .collect(toList());

        assertEquals(foldCount, results.size());
        assertTrue(rowsFromResults.containsAll(datasetRows));
    }

    private String getResourceFilePath(String resourceName) {
        URL resource = getClass().getResource(resourceName);

        return resource.getPath();
    }
}