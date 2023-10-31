package tests;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class ToArrayTests extends ArrayListTester {

    @Test
    public void testToArrayWithoutArgs() {
        enabledBaseTests = false;
        Object[] array = testingList.toArray();
        assertArrayEquals(expectedElements, array);
    }

    @Test
    public void testToArrayLengthLowerThanCapacity() {
        Integer[] array = new Integer[0];
        array = testingList.toArray(array);
        assertArrayEquals(expectedElements, array);
    }

    @Test
    public void testToArrayLengthGreaterThanCapacity() {
        Integer[] expected = new Integer[expectedSize << 1];
        for (int i = expectedSize; i < expected.length; i++) {
            expected[i] = randomValue();
        }
        System.arraycopy(expectedElements, 0, expected, 0, expectedSize);
        Integer[] actual = expected.clone();
        testingList.toArray(actual);
        assertArrayEquals(expected, actual);
    }
}
