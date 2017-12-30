package org.neuroph.core.data;

import org.junit.Test;

import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DataSetTest {

    @Test
    public void takeSubset_takeFirstTenRows_returnsCorrectSubset() {
        DataSet dataSet = DataSet.createFromFile(getResourceFilePath("iris_normalized.txt"), 4, 3, ",");

        DataSet subset = dataSet.takeSubset(0, 10);

        assertEquals(10, subset.size());
        IntStream.range(0, 10)
                .forEach(value -> {
                    DataSetRow dataSetRow = dataSet.get(value);
                    DataSetRow subsetRow = subset.get(value);

                    assertEqualRows(dataSetRow, subsetRow);
                });
    }

    @Test
    public void takeSubset_takeMoreThanAvailable_returnsOnlyAvailableRows() {
        DataSet dataSet = DataSet.createFromFile(getResourceFilePath("iris_normalized.txt"), 4, 3, ",");

        DataSet subset = dataSet.takeSubset(145, 155);

        assertEquals(5, subset.size());
    }

    @Test
    public void takeSubset_emptyDataset_returnsEmptySubset() {
        DataSet dataSet = new DataSet(10);

        DataSet subset = dataSet.takeSubset(0, 10);

        assertTrue(subset.isEmpty());
    }

    @Test
    public void takeSubset_splitIntoEightSubsets_expectCorrectSplit() {
        DataSet dataSet = DataSet.createFromFile(getResourceFilePath("iris_normalized.txt"), 4, 3, ",");
        List<String> rowsFromDataset = dataSet.stream()
                .map(DataSetRow::toString)
                .collect(toList());

        List<DataSet> subsets = IntStream.range(0, 8)
                .mapToObj(value -> dataSet.takeSubset(value * 20, value * 20 + 20))
                .collect(toList());
        List<String> rowsFromSubsets = subsets.stream()
                .flatMap(Collection::stream)
                .map(DataSetRow::toString)
                .collect(toList());


        assertEquals(8, subsets.size());
        assertEquals(rowsFromDataset.size(), rowsFromSubsets.size());
        assertTrue(rowsFromDataset.containsAll(rowsFromSubsets));
    }

    private void assertEqualRows(DataSetRow dataSetRow, DataSetRow dataSetRow1) {
        assertEquals(dataSetRow.toString(), dataSetRow1.toString());
    }

    private String getResourceFilePath(String resourceName) {
        URL resource = getClass().getResource(resourceName);

        return resource.getPath();
    }
}