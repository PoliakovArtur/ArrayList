package tests;

import example.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.*;

public class CapacityTests extends ArrayListTester {
    public CapacityTests() {
        super(false, false);
    }

    @Test
    public void testDefaultCapacity() throws IllegalAccessException {
        int capacity = getCapacity(new ArrayList<>());
        assertEquals(10, capacity);
    }
    @Test
    public void testCapacityConstructor() throws IllegalAccessException {
        int testCount = randomSmallValue();
        for (int i = 0; i < testCount; i++) {
            int expected = randomValue(0, MAX_RANDOM_SMALL_VALUE);
            ArrayList<Integer> list = new ArrayList<>(expected);
            int actual = getCapacity(list);
            assertEquals(expected, actual);
        }
    }
    @Test
    public void testZeroCapacity() {
        generateRandomTestingList();
        ArrayList<Integer> list = new ArrayList<>(0);
        Executable addElements = () -> list.addAll(testingList);
        assertDoesNotThrow(addElements);
        testingList = list;
    }
    @Test
    public void testTrimToSize() throws IllegalAccessException {
        generateRandomTestingList();
        int removeCount = randomValue(expectedSize >> 2, expectedSize >> 1);
        for (int i = 0; i < removeCount; i++) {
            testingList.remove(--expectedSize);
        }
        testingList.trimToSize();
        int expected = expectedSize;
        int actual = getCapacity(testingList);
        assertEquals(expected, actual);
    }
    @Test
    public void testEnsureCapacity() throws IllegalAccessException {
        generateRandomTestingList();
        int expected = 1000;
        assertTrue(testingList.ensureCapacity(expected));
        int actual = getCapacity(testingList);
        assertEquals(expected, actual);
        assertThrows(IllegalArgumentException.class, () -> testingList.ensureCapacity(-1));
        assertFalse(testingList.ensureCapacity(randomValue(0, expected)));
    }
    private int getCapacity(ArrayList<Integer> list) throws IllegalAccessException {
        Class<?> clazz = list.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields) {
            if(field.getName().equals("elements")) {
                field.setAccessible(true);
                Object[] elements = (Object[]) field.get(list);
                return elements.length;
            }
        }
        return -1;
    }
}
