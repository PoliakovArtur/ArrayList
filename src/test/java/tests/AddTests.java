package tests;

import example.ArrayList;
import org.junit.jupiter.api.*;

public class AddTests extends ArrayListTester {

    public AddTests() {
        super(true, true);
    }

    @Test
    public void testAdd() {}

    @Test
    public void testInsert() {
        int insertCount = randomSmallValue();
        for (int i = 0; i < insertCount; i++) {
            int element = randomValue();
            int index = randomValue(0, expectedSize + 1);
            testingList.add(index, element);
            if(expectedSize >= expectedElements.length) increaseArray();
            shiftArray(index, index + 1, expectedSize - index);
            expectedElements[index] = element;
            expectedSize++;
        }
    }

    @Test
    public void testAddAll() {
        ArrayList<Integer> randomList = randomList();
        testingList.addAll(randomList);
        addAll(expectedSize, randomList);
    }

    @Test
    public void testInsertAll() {
        int index = randomValue(0, expectedSize);
        ArrayList<Integer> randomList = randomList();
        testingList.addAll(index, randomList);
        addAll(index, randomList);
    }
    @Test
    public void testAddCollectionInConstructor() {
        testingList = new ArrayList<>(testingList);
    }

    private void addAll(int index, ArrayList<Integer> randomList) {
        int rndListSize = randomList.size();
        int resultSize = rndListSize + expectedSize;
        int shift = resultSize / expectedElements.length;
        increaseArray(shift);
        shiftArray(index, index + rndListSize, expectedSize - index);
        for (int i = 0; i < rndListSize; i++) {
            expectedElements[index++] = randomList.get(i);
        }
        expectedSize += rndListSize;
    }
}
