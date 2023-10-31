package tests;

import example.ArrayList;
import org.junit.jupiter.api.*;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertThrows;
public class RemoveTests extends ArrayListTester {

    @Test
    public void testRemove() {
        int removeCount = randomValue(expectedSize >> 1, expectedSize);
        for (int i = 0; i < removeCount; i++) {
            int index = randomValue(0, expectedSize);
            if (index < expectedSize - 1) {
                System.arraycopy(expectedElements, index + 1, expectedElements, index, expectedSize - (index + 1));
            }
            expectedSize--;
            testingList.remove(index);
        }
    }

    @Test
    public void testClear() {
        testingList.clear();
        expectedSize = 0;
        assertThrows(IndexOutOfBoundsException.class, () -> testingList.get(0));
    }

    @Test
    public void testRemoveByValue() {
        testingList = new ArrayList<>();
        expectedSize = randomSmallValue();
        expectedElements = new Integer[expectedSize];
        for (int i = 0; i < expectedSize; i++) {
            testingList.add(1);
            expectedElements[i] = 1;
        }

        Set<Integer> uniqueElements = new HashSet<>();
        uniqueElements.add(1);

        int setCount = randomValue(expectedSize >> 2, expectedSize);
        for (int i = 0; i < setCount; i++) {
            int value = 1;
            while (uniqueElements.contains(value)) value = randomValue();
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
            testingList.remove(Integer.valueOf(value));
            int minIndex = Integer.min(index1, index2);
            shiftArray(minIndex + 1, minIndex, expectedSize - (minIndex + 1));
            expectedSize--;
        }
    }

    @Test
    public void testRemoveAll() {
        Set<Integer> nonRemoval = new HashSet<>(testingList);
        int valuesCount = randomSmallValue();
        ArrayList<Object> objects = new ArrayList<>();
        int objectsSize = randomSmallValue();
        for (int i = 0; i < objectsSize; i++) {
            objects.add(new Object());
        }
        for (int i = 0; i < valuesCount; i++) {
            int times = randomValue(1, 5);
            int value;
            do {
                value = randomValue();
            } while (nonRemoval.contains(value));
            for (int j = 0; j < times; j++) {
                int index = randomValue(0, testingList.size() + 1);
                testingList.add(index, value);
            }
            int index = randomValue(0, objects.size() + 1);
            objects.add(index, value);
        }
        testingList.removeAll(objects);
    }
    @Test
    public void testRetainAll() {
        Set<Integer> retainedElements = new HashSet<>(randomList());
        int retainedElementsCount = randomSmallValue();
        for (int i = 0; i < retainedElementsCount; i++) {
            int times = randomValue(1, 5);
            int value = randomValue();
            for (int j = 0; j < times; j++) {
                int index = randomValue(0, testingList.size() + 1);
                testingList.add(index, value);
                if(expectedElements.length <= expectedSize) increaseArray();
                shiftArray(index, index + 1, expectedSize - index);
                expectedElements[index] = value;
                expectedSize++;
            }
            retainedElements.add(value);
        }
        int retainedElementsSize = 0;
        Integer[] retainedListCopy = new Integer[expectedSize];
        for(Integer i : expectedElements) {
            if(retainedElements.contains(i)) {
                retainedListCopy[retainedElementsSize++] = i;
            }
        }
        expectedElements = retainedListCopy;
        expectedSize = retainedElementsSize;
        testingList.retainAll(retainedElements);
    }

    @Test
    public void NullPointerExceptionTest() {
        assertThrows(NullPointerException.class, () -> testingList.removeAll(null));
        assertThrows(NullPointerException.class, () -> testingList.retainAll(null));
    }
}