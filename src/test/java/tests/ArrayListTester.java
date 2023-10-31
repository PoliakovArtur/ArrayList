package tests;

import org.junit.jupiter.api.*;
import example.ArrayList;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArrayListTester {
    protected static final Random RANDOMIZER = new Random();
    protected static final int MIN_RANDOM_SMALL_VALUE = 20;
    protected static final int MAX_RANDOM_SMALL_VALUE = 101;
    protected static final int TESTS_COUNT = 10;
    protected boolean enabledBaseTests;

    protected int expectedSize;

    protected ArrayList<Integer> testingList;
    protected Integer[] expectedElements;

    public ArrayListTester() {
        enabledBaseTests = true;
        generateRandomTestingList();
    }

    public ArrayListTester(boolean enabledBaseTests, boolean generateRandomTestingList) {
        this.enabledBaseTests = enabledBaseTests;
        if(generateRandomTestingList) generateRandomTestingList();
    }
    protected void generateRandomTestingList() {
        testingList = new ArrayList<>();
        expectedSize = randomSmallValue();
        expectedElements = new Integer[expectedSize];
        for (int i = 0; i < expectedSize; i++) {
            int element = randomValue();
            testingList.add(element);
            expectedElements[i] = element;
        }
    }

    protected ArrayList<Integer> randomList() {
        return randomList(randomSmallValue());
    }

    protected ArrayList<Integer> randomList(int size) {
        ArrayList<Integer> randomList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            int element = randomValue();
            randomList.add(element);
        }
        return randomList;
    }
    protected void increaseArray() {
        increaseArray(1);
    }

    protected void increaseArray(int shift) {
        Integer[] newArray = new Integer[expectedElements.length << shift];
        System.arraycopy(expectedElements, 0, newArray, 0, expectedElements.length);
        expectedElements = newArray;
    }

    protected void shiftArray(int from, int to, int count) {
        System.arraycopy(expectedElements, from, expectedElements, to, count);
    }

    protected int randomSmallValue() {
        return randomValue(MIN_RANDOM_SMALL_VALUE, MAX_RANDOM_SMALL_VALUE);
    }

    protected int randomValue(int origin, int bound) {
        return RANDOMIZER.nextInt(origin, bound);
    }

    protected int randomValue() {
        return RANDOMIZER.nextInt();
    }

    protected boolean randomBoolean() {
        return RANDOMIZER.nextBoolean();
    }
    @AfterEach
    @Order(1)
    public void testSize() {
        if(enabledBaseTests) {
            assertEquals(expectedSize, testingList.size());
        }
    }

    @AfterEach
    @Order(2)
    public void testElements() {
        if (enabledBaseTests) {
            for (int i = 0; i < expectedSize; i++) {
                Integer expected = expectedElements[i];
                Integer actual = testingList.get(i);
                assertEquals(expected, actual);
            }
        }
    }
}
