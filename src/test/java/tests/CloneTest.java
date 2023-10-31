package tests;

import example.ArrayList;
import org.junit.jupiter.api.Test;

public class CloneTest extends ArrayListTester {

    @Test
    public void testDeepClone() {
        ArrayList<Integer> clone = (ArrayList<Integer>) testingList.clone();
        for (int i = 0; i < 10; i++) {
            int index = randomValue(0, testingList.size());
            int value = randomValue();
            testingList.set(index, value);
        }
        testingList = clone;
    }
}
