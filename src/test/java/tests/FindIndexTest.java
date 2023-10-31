package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import example.ArrayList;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;

public class FindIndexTest extends ArrayListTester {

    public FindIndexTest() {
        super(false, false);
    }
    @Test
    public void testIndexOf() {
        testingList = new ArrayList<>();
        expectedSize = randomSmallValue();
        expectedElements = new Integer[expectedSize];

        for (int i = 0; i < expectedSize; i++) {
            testingList.add(1);
            expectedElements[i] = 1;
        }

        Set<Integer> uniqueElements = new HashSet<>();
        uniqueElements.add(1);

        int setCount = randomValue(expectedSize >> 1, expectedSize);

        for (int i = 0; i < setCount; i++) {
            int value = 1;
            while (uniqueElements.contains(value)) {
                value = randomValue();
            }
            uniqueElements.add(value);
            int index1 = 0, index2 = 0;
            while (index1 == index2) {
                index1 = randomValue(0, expectedSize);
                index2 = randomValue(0, expectedSize);
            }
            expectedElements[index1] = value;
            expectedElements[index2] = value;
            testingList.set(index1, value);
            testingList.set(index2, value);
            testIndexOf(value, Integer.min(index1, index2), Integer.max(index1, index2));
        }
    }

    private void testIndexOf(int value, int minIndex, int maxIndex) {
        assertEquals(minIndex, testingList.indexOf(value));
        assertEquals(maxIndex, testingList.lastIndexOf(value));
    }
}
