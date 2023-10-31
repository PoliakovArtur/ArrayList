package tests;

import org.junit.jupiter.api.*;

public class SetTests extends ArrayListTester {

    @Test
    public void testSet() {
        int setCount = RANDOMIZER.nextInt(expectedSize >> 1, expectedSize);
        for (int i = 0; i < setCount; i++) {
            int index = RANDOMIZER.nextInt(0, expectedSize);
            int replacement = RANDOMIZER.nextInt();
            testingList.set(index, replacement);
            expectedElements[index] = replacement;
        }
    }

}
