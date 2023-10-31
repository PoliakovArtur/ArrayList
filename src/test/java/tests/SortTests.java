package tests;


import example.ArrayList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class SortTests extends ArrayListTester {

    @Test
    public void testSortWithComparable() {
        for (int i = 0; i < TESTS_COUNT; i++) {
            generateRandomTestingList();
            Arrays.sort(expectedElements);
            testingList.sort();
        }
    }

    @Test
    public void testSortIncomparableElements() {
        enabledBaseTests = false;
        ArrayList<Object> objects = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            objects.add(new Object());
        }
        assertThrows(ClassCastException.class, objects::sort);
    }

    @Test
    public void testSortWithComparator() {
        generateRandomTestingList();
        sortWithComparator(Collections.reverseOrder());
    }
    private void sortWithComparator(Comparator<Integer> comparator) {
        Arrays.sort(expectedElements, comparator);
        testingList.sort(comparator);
    }
}
