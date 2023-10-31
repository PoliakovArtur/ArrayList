package tests;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
public class SubListTest extends ArrayListTester {
    @Test
    public void testSubList() {
        int testCount = randomSmallValue();
        for (int i = 0; i < testCount; i++) {
            int from = randomValue(0, expectedSize >> 1);
            int to = randomValue(from + 1, expectedSize);
            Integer[] expected = Arrays.copyOfRange(expectedElements, from, to);
            List<Integer> subList = testingList.subList(from, to);
            Integer[] actual = new Integer[subList.size()];
            subList.toArray(actual);
            assertArrayEquals(expected, actual);
        }
    }
}
