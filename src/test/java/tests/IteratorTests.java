package tests;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class IteratorTests extends ArrayListTester {

    public IteratorTests() {
        super(false, true);
    }
    @Test
    public void testClassicIterator() {
        Iterator<Integer> iterator = testingList.iterator();
        Integer[] actual = new Integer[expectedSize];
        int index = 0;
        while (iterator.hasNext()) {
            actual[index++] = iterator.next();
        }
        assertArrayEquals(expectedElements, actual);
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    public void testConcurrentModification() {
        Iterator<Integer> iterator = testingList.iterator();
        int value = randomValue();
        testingList.add(value);
        increaseArray();
        expectedElements[expectedSize++] = value;
        assertThrows(ConcurrentModificationException.class, iterator::next);
    }

    @Test
    public void testForEach() {
        Integer[] actual = new Integer[expectedSize];
        int index = 0;
        for(Integer i : testingList) {
            actual[index++] = i;
        }
        assertArrayEquals(expectedElements, actual);
    }

    @Test
    public void walkInIteratorTest() {
        ListIterator<Integer> listIterator = testingList.listIterator();
        Integer[] expected = new Integer[expectedSize << 2];
        System.arraycopy(expectedElements, 0, expected, 0, expectedSize);
        System.arraycopy(expectedElements, 0, expected, expectedSize << 1, expectedSize);
        for (int i = expectedSize; i < expected.length; i+= expectedSize << 1) {
            int index = i;
            for (int j = expectedSize - 1; j >=0; j--) {
                expected[index++] = expectedElements[j];
            }
        }
        Integer[] actual = new Integer[expectedSize << 2];
        int actualIndex = 0;
        for (int i = 0; i < 2; i++) {
            while (listIterator.hasNext()) {
                actual[actualIndex++] = listIterator.next();
            }
            while (listIterator.hasPrevious()) {
                actual[actualIndex++] = listIterator.previous();
            }
        }
        assertArrayEquals(expected, actual);
    }

    @Test
    public void addToHeadTest() {
        ListIterator<Integer> iterator = testingList.listIterator();
        for (int i = 0; i < 10; i++) {
            iterator.add(1);
        }
        increaseArray();
        shiftArray(0, 10, expectedSize);
        Arrays.fill(expectedElements, 0, 10, 1);
        expectedSize += 10;
        enabledBaseTests = true;
    }

    @Test
    public void addToTailTest() {
        ListIterator<Integer> iterator = testingList.listIterator(testingList.size());
        for (int i = 0; i < 10; i++) {
            iterator.add(1);
        }
        increaseArray();
        Arrays.fill(expectedElements, expectedSize, expectedSize + 10, 1);
        expectedSize += 10;
        enabledBaseTests = true;
    }
    @Test
    public void testRemove() {
        ListIterator<Integer> iterator = testingList.listIterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
        assertTrue(testingList.isEmpty());
        assertThrows(IndexOutOfBoundsException.class, () -> testingList.get(0));
    }

    @Test
    public void testBackwardsRemove() {
        ListIterator<Integer> iterator = testingList.listIterator(testingList.size());
        while (iterator.hasPrevious()) {
            iterator.previous();
            iterator.remove();
        }
        assertTrue(testingList.isEmpty());
        assertThrows(IndexOutOfBoundsException.class, () -> testingList.get(0));
    }
    @Test
    public void testIllegalStateException() {
        ListIterator<Integer> iterator = testingList.listIterator(testingList.size() >> 1);
        assertThrows(IllegalStateException.class, iterator::remove);
        assertThrows(IllegalStateException.class, () -> iterator.set(randomValue()));

        iterator.next();
        assertDoesNotThrow(iterator::remove);
        assertThrows(IllegalStateException.class, iterator::remove);

        iterator.previous();
        assertDoesNotThrow(() -> iterator.set(randomValue()));
        assertThrows(IllegalStateException.class, () -> iterator.set(randomValue()));
    }

    @Test
    public void testSet() {
        ListIterator<Integer> iterator = testingList.listIterator();
        int index = 0;
        while (iterator.hasNext()) {
            iterator.next();
            int value = randomValue();
            iterator.set(value);
            expectedElements[index++] = value;
        }
        enabledBaseTests = true;
    }

    @Test
    public void testBackwardsSet() {
        ListIterator<Integer> iterator = testingList.listIterator(testingList.size());
        int index = expectedSize;
        while (iterator.hasPrevious()) {
            iterator.previous();
            int value = randomValue();
            iterator.set(value);
            expectedElements[--index] = value;
        }
        enabledBaseTests = true;
    }
}
