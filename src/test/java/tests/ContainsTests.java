package tests;

import example.ArrayList;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ContainsTests extends ArrayListTester {

    public ContainsTests() {
        super(false, false);
    }
    @Test
    public void testContains() {
        expectedSize = randomSmallValue();
        expectedElements = new Integer[expectedSize];
        testingList = new ArrayList<>();
        for (int i = 0; i < expectedSize; i++) {
            int value = randomValue(0, 1000);
            testingList.add(value);
            expectedElements[i] = value;
        }
        Arrays.sort(expectedElements);
        for (int i = 0; i < 1000; i++) {
            boolean expected = Arrays.binarySearch(expectedElements, i) > -1;
            boolean actual = testingList.contains(i);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testContainsAll() {
        generateRandomTestingList();
        int testCount = randomSmallValue();
        for (int i = 0; i < testCount; i++) {
            ArrayList<Integer> list = new ArrayList<>();
            int listSize = randomValue(MIN_RANDOM_SMALL_VALUE >> 1, expectedSize);
            for (int j = 0; j < listSize; j++) {
                int index = randomValue(0, expectedSize);
                list.add(testingList.get(index));
            }
            boolean expected = randomBoolean();
            if(!expected) {
                int index = randomValue(0, list.size());
                list.add(index, 1001);
            }

            boolean actual = testingList.containsAll(list);
            assertEquals(expected, actual);
        }
    }
}
