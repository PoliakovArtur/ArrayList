package example;

import java.util.*;
/**
 * Данный класс представляет собой имплементацию интерфейса {@code List}. Реализует
 * динамический массив. Элементы хранятся в обычном массиве, а динамичность
 * достигается за счет создания нового массива, большего по размеру, в который
 * копируются все элементы из старого, когда массив заполнен. Увеличение массива
 * происходит в 1.5 раза. Однозначным плюсом такой реализации является доступ к элементу
 * по индексу за О(1), как и в обычном массиве.
 * Кроме методов, предоставляемых {@code List}, здесь также реализованы методы манипуляций
 * с размером внутреннего массива (далее capacity), такие, как trimToSize и ensureCapacity,
 * позволяющие уменьшать его размер до количества элементов, и увеличивать до нужного значения,
 * метод clone, а также реализована возможность сортировать список, даже если он параметризован
 * типом, не имплементирующим Comparable.
 *
 * <p>Методы {@code size}, {@code isEmpty}, {@code get}, {@code set},
 * {@code iterator}, and {@code listIterator} работают за константное время.
 * Методы {@code add} и {@code remove} работают за амортизированное константное
 * время - вставка в конец, если capacity больше size выполняется за О(1),
 * в середину за О(n), где n - количество элементов, стоящих после удаляемого,
 * аналогично и с удалением элемента, только без надобности увеличивать массив.
 *
 * <p><strong>Данный класс не является потокобезапасным.</strong>
 * Если несколько потоков обращаются к {@code ArrayList} и хотя бы один поток
 * структурно изменяет список(в случае одного такого потока будет критично, если он
 * будет уменьшать список), список должен быть синхронизирован извне.
 *
 * <p>Итераторы, возвращаемые методами {@link #iterator() iterator} и
 * {@link #listIterator(int) listIterator} являются реализациями вложенного класса
 * ArrayListIterator, разница лишь в том, что итератор, полученный с помощью
 * {@link #iterator() iterator}, обернут в интерфейс {@link Iterator}, а
 * тот что был получен с помощью {@link #iterator() iterator} - в интерфейс {@link ListIterator}.
 * При вызове итератора, в нем сохраняется количество элементов в списке, на момент
 * его вызова. Если список был именем не через итератор, то при вызове методов
 * {@link ArrayListIterator#next() next}, {@link ArrayListIterator#previous() previous},
 * {@link ArrayListIterator#remove() remove},{@link ArrayListIterator#add(Object) add}
 * будет выброшено {@link ConcurrentModificationException}.
 *
 * @param <E> Тип, которым параметризован список
 *
 * @author  Поляков Артур
 */
public class ArrayList<E> implements List<E>, Cloneable {
    /**
     * Стандартный размер внутреннего массива.
     */
    private final static int DEFAULT_CAPACITY = 10;
    /**
     * Массив, в котором хранятся элементы.
     */
    private Object[] elements;
    /**
     * Количество элементов в списке.
     */
    private int size;
    /**
     * Размер внутреннего массива.
     */
    private int capacity;
    /**
     * Количество раз, которое список изменялся
     */
    private int modCount;
    /**
     * Конструктор, позволяющий указать размер
     * внутреннего массива
     * @param  initialCapacity размер внутреннего массива
     * @throws IllegalArgumentException если передано отрицательное значение
     */
    public ArrayList(int initialCapacity) {
        if(initialCapacity < 0) throw new IllegalArgumentException("capacity can't be lower than zero");
        this.elements = new Object[initialCapacity];
    }

    /**
     * Создает ArrayList с размером
     * внутреннего массива {@link #DEFAULT_CAPACITY}
     */
    public ArrayList() {
        this(DEFAULT_CAPACITY);
    }
    /**
     * Создает ArrayList на основе другой коллекции.
     * @param c коллекция, на основе которой нужно создать ArrayList
     * @throws NullPointerException если передан {@code null}
     */
    public ArrayList(Collection<? extends E> c) {
        if(c == null) throw new NullPointerException();
        Object[] array = c.toArray();
        int colSize = array.length;
        capacity = colSize < DEFAULT_CAPACITY ? DEFAULT_CAPACITY : (colSize >> 1) + colSize;
        elements = new Object[capacity];
        System.arraycopy(array, 0, elements, 0, colSize);
        size = colSize;
    }
    /**
     * Вставляет элемент в список в указанную позицию. Если передать индекс
     * равный size, то элемент добавится в конец
     * @param index позиция, куда нужно вставить элемент
     * @param element вставляемый элемент
     * @throws IndexOutOfBoundsException если передан индекс меньше нуля либо больше size
     */
    @Override
    public void add(int index, E element) {
        if(index != size) checkIndex(index);
        if(size == capacity) {
            increaseArray();
        }
        shiftArray(index, index + 1, size - index);
        size++;
        elements[index] = element;
        modCount++;
    }
    /**
     * Добавляет элемент в конец списка
     * @return {@code true} если элемент был добавлен
     */
    @Override
    public boolean add(E e) {
        add(size, e);
        return true;
    }
    /**
     * Добавляет все элементы из переданной коллекции в конец списка.
     * @param c коллекция, из которой будут браться элементы
     * @throws NullPointerException если передан {@code null}
     * @return {@code true} если был добавлен хотя бы один элемент, в противном случае {@code false}
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        return addAll(size, c);
    }
    /**
     * Вставляет в список все элементы из переданной коллекции в указанную позицию.
     * @param index позиция, куда нужно вставить элементы
     * @param c коллекция, из которой будут браться элементы
     * @throws NullPointerException если передан {@code null}
     * @throws IndexOutOfBoundsException если передан индекс меньше нуля либо больше size
     * @return {@code true} если был добавлен хотя бы один элемент, в противном случае {@code false}
     */
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if(c == null) throw new NullPointerException();
        int colSize = c.size();
        if(colSize == 0) return false;
        if(index != size) checkIndex(index);
        int resultSize = colSize + size;
        if (resultSize > capacity) {
            increaseArray((resultSize >> 1) + resultSize);
        }
        shiftArray(index, index + colSize, size - index);
        for(E e : c) {
            elements[index++] = e;
        }
        size = resultSize;
        modCount++;
        return true;
    }
    /**
     * Возвращает элемент по индексу
     * @param index индекс, по которому нужно найти элемент
     * @throws IndexOutOfBoundsException если передан индекс меньше нуля либо больше size - 1
     * @return элемент, который был найден по переданному индексу
     */

    @Override
    public E get(int index) {
        checkIndex(index);
        @SuppressWarnings("unchecked")
        E element = (E) elements[index];
        return element;
    }
    /**
     * Заменяет элемент по индексу
     * @param index индекс, по которому нужно заменить
     * @throws IndexOutOfBoundsException если передан индекс меньше нуля либо больше size - 1
     * @return элемент, который был заменен
     */
    @Override
    public E set(int index, E element) {
        checkIndex(index);
        @SuppressWarnings("unchecked")
        E replaced = (E) elements[index];
        elements[index] = element;
        return replaced;
    }
    /**
     * Удаляет элемент из списка по значению. Поиск элемента производится с начала списка, поэтому, если
     * таких элементов несколько, то удаляется тот, что ближе к началу.
     * @param o элемент, который нужно удалить.
     * @throws IndexOutOfBoundsException если передан индекс меньше нуля либо больше size - 1
     * @return {@code true}, если элемент был найден и удален. {@code false} - если элемент не был найден
     */
    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if(index > -1) {
            remove(index);
            return true;
        }
        return false;
    }
    /**
     * Удаляет элемент из списка по индексу, сдвигая все элементы справа влево на одну позицию.
     * @param index индекс, по которому нужно удалить элемент
     * @throws IndexOutOfBoundsException если передан индекс меньше нуля либо больше size - 1
     * @return элемент, который был удален
     */
    @Override
    public E remove(int index) {
        checkIndex(index);
        @SuppressWarnings("unchecked")
        E removed = (E) elements[index];
        shiftArray(index + 1, index, size - (index + 1));
        size--;
        elements[size] = null;
        modCount++;
        return removed;
    }

    /**
     * Удаляет все элементы из списка, равные элементам из переданной коллекции. В отличие от
     * метода {@link #remove(Object o)}, данный метод удалит все одинаковые элементы из списка,
     * которые равны одному из элементов из переданной коллекции.
     * @param c коллекция, содержащая элементы, которые необходимо удалить из списка
     * @throws NullPointerException если передан {@code null}
     * @return {@code true}, если был удален хотя бы один элемент, {@code false} в противном случае
     */

    @Override
    public boolean removeAll(Collection<?> c) {
        if(c == null) throw new NullPointerException();
        boolean isRemoveOne = false;
        Object present = new Object();
        Set<Object> removingElements = new HashSet<>();
        for(Object o : c) {
            if(!removingElements.contains(o)) {
                removingElements.add(o);
                for (int i = 0; i < size; i++) {
                    if(elements[i].equals(o)) {
                        elements[i] = present;
                        isRemoveOne = true;
                    }
                }
            }
        }
        if(isRemoveOne) {
            for (int i = size - 1; i >= 0 ; i--) {
                if(elements[i] == present) remove(i);
            }
            modCount++;
            return true;
        }
        return false;
    }

    /**
     * Удаляет все элементы из списка, не содержащиеся переданной коллекции.
     * @param c коллекция, содержащая элементы, которые необходимо удалить из списка
     * @throws NullPointerException если передан {@code null}
     * @return {@code true}, если был удален хотя бы один элемент, {@code false} в противном случае
     */

    @Override
    public boolean retainAll(Collection<?> c) {
        if(c == null) throw new NullPointerException();
        Set<Object> retainedElementsSet = new HashSet<>(c);
        Object[] retainedElements = new Object[capacity];
        int newSize = 0;
        for(Object e : elements) {
            if(retainedElementsSet.contains(e)) {
                retainedElements[newSize++] = e;
            }
        }
        if(newSize == size) {
            return false;
        }
        size = newSize;
        elements = retainedElements;
        modCount++;
        return true;
    }
    /**
     * Удаляет все элементы из списка.
     */
    @Override
    public void clear() {
        Arrays.fill(elements, null);
        size = 0;
        modCount++;
    }
    /**
     * Ищет переданный элемент в списке, начиная с начала списка
     * @param o элемент, который нужно найти
     * @return индекс, по которому находится элемент. -1 - если элемент не был найден
     */

    @Override
    public int indexOf(Object o) {
        for (int i = 0; i < size; i++) {
            if(elements[i].equals(o)) return i;
        }
        return -1;
    }
    /**
     * Ищет переданный элемент в списке, начиная с конца списка
     * @param o элемент, который нужно найти
     * @return индекс, по которому находится элемент. -1 - если элемент не был найден
     */
    @Override
    public int lastIndexOf(Object o) {
        for (int i = size - 1; i >= 0; i--) {
            if(elements[i].equals(o)) return i;
        }
        return -1;
    }
    /**
     * Метод для проверки валидности индекса. Используется в методах, в которых производится работа с элементами по индексу.
     * Индекс считается валидным, если он лежит в диапазоне [0, size).
     * @param index индекс, валидность которого нужно проверить
     * @throws IndexOutOfBoundsException если по данному индексу нельзя получить элемент
     */
    private void checkIndex(int index) {
        if(index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(String.format("Index %d out of bounds for length %d", index, size));
        }
    }
    /**
     * Возвращает размер списка
     * @return размер списка
     */

    @Override
    public int size() {
        return size;
    }
    /**
     * Проверяет, является ли список пустым
     * @return {@code true}, если список пуст, {@code false} если в списке есть хотя бы один элемент
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    /**
     * Проверяет, есть ли в списке переданный элемент.
     * @param o элемент, который нужно найти
     * @return {@code true}, если элемент был найден, {@code false} в противном случае
     */
    @Override
    public boolean contains(Object o) {
        return indexOf(o) > -1;
    }
    /**
     * Проверяет наличие всех элементов в списке из переданной коллекции
     * @param c коллекция, содержащая элементы, проверить наличие которых требуется
     * @throws NullPointerException если передан {@code null}
     * @return {@code true}, если все элементы хотя бы в одном экземпляре содержатся в списке,
     * {@code false} если хотя бы один элемент из коллекции не был найден в списке
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        if(c == null) throw new NullPointerException();
        if(c.size() == 0) return true;
        if(size == 0) return false;
        for(Object o : c) {
            if(!contains(o)) return false;
        }
        return true;
    }

    /**
     * Преобразует список в массив. Размер массива будет равен количеству элементов
     * @return массив, содержащий все элементы списка
     */
    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elements, size);
    }
    /**
     * Копирует все элементы из списка в переданный массив. Если размер массива меньше, чем
     * количество элементов, то будет возвращен новый массив, содержащий все элементы из списка.
     * В отличие от метода {@link #toArray()}, позволяет получить массив нужного типа
     * @param a массив, в который нужно скопировать элементы.
     * @throws NullPointerException если передан {@code null}
     * @return массив, содержащий все элементы списка
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if(a == null) throw new NullPointerException();
        if(a.length < size) {
            return (T[]) Arrays.copyOf(elements, size, a.getClass());
        }
        System.arraycopy(elements, 0, a, 0, size);
        return a;
    }
    /**
     * Увеличивает внутренний массив до переданного значения. Если
     * переданное значение меньше размера массива и положительно, то ничего не произойдет.
     * @param capacity новый размер внутреннего массива
     * @throws NullPointerException если capacity меньше нуля.
     * @return {@code true}, если массив был увеличен, {@code false} в противном случае
     */
    public boolean ensureCapacity(int capacity) {
        if(capacity < 0) throw new IllegalArgumentException("capacity can't be lower than zero");
        if(capacity <= this.capacity) return false;
        this.capacity = capacity;
        Object[] ensuredArray = new Object[capacity];
        System.arraycopy(elements, 0, ensuredArray, 0, size);
        elements = ensuredArray;
        return true;
    }
    /**
     * Удаляет все неиспользуемые ячейки внутреннего массива путем создания нового массива,
     * содержащего все элементы списка.
     */
    public void trimToSize() {
        elements = Arrays.copyOf(elements, size);
    }
    /**
     * Возвращает новый список, содержащий элементы в переданном диапазоне индексов, не
     * включаю последний, что означает, что если нужно, чтобы в новом списке был последний элемент
     * текущего списка, toIndex должен быть равен {@link #size}
     * @param fromIndex начало диапазона
     * @param toIndex конец диапазона(не будет включен)
     * @throws IllegalArgumentException если fromIndex меньше чем toIndex, либо если передан
     * неверный индекс.
     * @return Новый список, содержащий все элементы в диапазоне
     */
    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        if(fromIndex >= toIndex) throw  new IllegalArgumentException("toIndex must be greater than fromIndex");
        checkIndex(fromIndex);
        checkIndex(toIndex - 1);
        ArrayList<E> subList = new ArrayList<>();
        subList.elements = Arrays.copyOfRange(elements, fromIndex, toIndex);
        subList.size = toIndex - fromIndex;
        return subList;
    }
    /**
     * Возвращает итератор списка
     * @return итератор данного списка, являющийся объектом класса {@link ArrayListIterator},
     * обернутый в интерфейс {@link Iterator}
     */
    @Override
    public Iterator<E> iterator() {
        return new ArrayListIterator();
    }
    /**
     * Возвращает итератор списка
     * @return итератор данного списка, являющийся объектом класса {@link ArrayListIterator},
     * обернутый в интерфейс {@link ListIterator}
     */
    @Override
    public ListIterator<E> listIterator() {
        return new ArrayListIterator();
    }
    /**
     * Позволяет получить итератор, указатель которого находится на переданном индексе
     * @param index индекс, на котором будет находиться указатель итератора.
     * Здесь индекс может быть равен size, что означает что указатель будет находиться в конце списка.
     * @return итератор данного списка, являющийся объектом класса {@link ArrayListIterator},
     * обернутый в интерфейс {@link ListIterator}
     */

    @Override
    public ListIterator<E> listIterator(int index) {
        if(index != size) checkIndex(index);
        return new ArrayListIterator(index);
    }

    /**
     * Класс, реализующий интерфейс {@link ListIterator} в самом базовом варианте.
     * Данный итератор позволяет двигаться по списку в любом направлении, добавлять,
     * удалять элементы, менять их значение. Он запоминает сколько раз менялся список
     * на момент его создания и при вызове методов {@link ArrayListIterator#next() next},
     * {@link ArrayListIterator#previous() previous}, {@link ArrayListIterator#remove remove},
     * {@link ArrayListIterator#add add}, сравнивает данное значение с аналогичным, которое хранится в самом списке.
     * Если они не равны, то выбрасывается {@link ConcurrentModificationException}.
     * <p>Указатель списка может принимать значения от 0 до size. Когда он принимает крайние значения, это означает,
     * что итератор дошел до начала либо конца списка.
     * <p>Удалить элемент или поменять значение элементам можно только после вызова одного из методов:
     * {@link ArrayListIterator#next() next}, {@link ArrayListIterator#previous() previous}, при чем
     * единожды - вызвать два раза подряд метод {@link ArrayListIterator#remove remove} или
     * {@link ArrayListIterator#set set}, или вызвать эти методы подряд в любой комбинации не получится,
     * будет выброшено {@link IllegalStateException}. При вызове этих методов они применяются к
     * предыдущему элементу, на котором стоял указатель, т.е. если был вызван метод {@link ArrayListIterator#next() next},
     * то будет изменен элемент, стоящий раньше по списку относительно указателя, если {@link ArrayListIterator#previous() previous},
     * то наоборот.
     * <p>Добавление элементов с помощью итератора работает аналогичным образом, что и удаление, за исключением ограничения на
     * обязательный вызов перед добавлением {@link ArrayListIterator#next() next} или {@link ArrayListIterator#previous() previous}.
     * Добавлять элементы можно сколько угодно раз подряд.
     */
    private class ArrayListIterator implements ListIterator<E> {
        /**
         * Курсор, указывающий на текущий элемент
         */
        int cursor;
        /**
         * Индекс элемента, на котором был указатель до вызова методов {@link ArrayListIterator#next() next} или
         * {@link ArrayListIterator#previous() previous}.
         */
        int last;
        /**
         * Количество раз, которое изменялся список на момент создания итератора
         */
        int expectedModCount = modCount;

        ArrayListIterator() {}
        ArrayListIterator(int cursor) {
            this.cursor = cursor;
            last = -1;
        }

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @Override
        public E next() {
            if(cursor == size) throw new NoSuchElementException();
            checkForCoModification();
            @SuppressWarnings("unchecked")
            E element = (E) elements[cursor];
            last = cursor++;
            return element;
        }

        @Override
        public boolean hasPrevious() {
            return cursor > 0;
        }

        @Override
        public E previous() {
            if(cursor == 0) throw new NoSuchElementException();
            checkForCoModification();
            last = --cursor;
            @SuppressWarnings("unchecked")
            E element = (E) elements[cursor];
            return element;
        }

        @Override
        public int nextIndex() {
            return cursor + 1;
        }

        @Override
        public int previousIndex() {
            return cursor;
        }

        @Override
        public void remove() {
            if(last < 0) throw new IllegalStateException();
            checkForCoModification();
            if(last < cursor) cursor = last;
            ArrayList.this.remove(last);
            last = -1;
            expectedModCount++;
        }

        @Override
        public void set(E e) {
            if(last < 0) throw new IllegalStateException();
            checkForCoModification();
            ArrayList.this.set(last, e);
            last = -1;
        }

        @Override
        public void add(E e) {
            checkForCoModification();
            ArrayList.this.add(cursor++, e);
            expectedModCount++;
        }
        /**
         * Проверяет, менялся ли список не через данный итератор
         * @throws {@link ConcurrentModificationException} если список был структурно изменен не через данный итератор
         */
        void checkForCoModification() {
            if(expectedModCount != ArrayList.this.modCount) {
                throw new ConcurrentModificationException();
            }
        }
    }

    /**
     * Сдвигает часть внутреннего массива. Данный метод является универсальным,
     * он позволяет переместить часть массива назад либо вперед. Используется
     * при вставке элементов в список, при удалении элементов из середины списка.
     * @param from начальный индекс той части массива, которую нужно переместить
     * @param to индекс, куда нужно переместить часть массива
     * @param count размер перемещаемой части массива
     */

    private void shiftArray(int from, int to, int count) {
        System.arraycopy(elements, from, elements, to, count);
    }

    /**
     * Увеличивает массив в полтора раза путем создания нового массива и копирования в него элементов из старого.
     */
    private void increaseArray() {
        increaseArray((capacity >> 1) + capacity + 1);
    }

    /**
     * Увеличивает массив до переданного значения. Используется в некоторых методах, например,
     * в добавлении коллекции элементов список, чтобы не увеличивать массив несколько раз,
     * если коллекция слишком велика.
     * @param newCapacity размер, до которого нужно увеличить массив
     */
    private void increaseArray(int newCapacity) {
        capacity = newCapacity;
        Object[] newArray = new Object[capacity];
        System.arraycopy(elements, 0, newArray, 0, size);
        elements = newArray;
    }

    /**
     * Компаратор, используемый при сортировке списка.
     */
    private transient Comparator<? super E> comparator;
    /**
     * Сортирует список, с помощью переданного компаратора.
     * Здесь реализована классическая сортировка Хоара или
     * быстрая сортировка. Средняя алгоритмическая сложность
     * равна O(n log n), где n - количество элементов, основание
     * логарифма равно 2, т.к. рекурсивно массив разбивается на
     * два подмассива, которые сортируются, до тех пор, пока
     * массив не будет состоять из одного элемента. Разбивка на массивы
     * происходит не буквально, а за счет передачи индексов, за пределами
     * которых массив не будет сортироваться
     * @param comparator компаратор, с помощью которого будет сортироваться массив
     */

    public void sort(Comparator<? super E> comparator) {
        this.comparator = comparator;
        separate(0, size - 1);
    }

    /**
     * Сортирует список, пытаясь приводить сортируемые элементы к
     * Comparable. Ответственность за то, имплементирует ли параметризованный
     * тип Comparable, ложится на того, кто вызывает этот метод. Ради сокращения
     * количества кода, приведение к Comparable и сравнение элементов завернуто
     * в Comparator.
     */
    @SuppressWarnings("unchecked")
    public void sort() {
        sort((e1, e2) -> ((Comparable<E>) e1).compareTo(e2));
    }

    /**
     * Часть алгоритма быстрой сортировки. Данный метод рекурсивно разбивает массив
     * на подмассивы, до тех пор, пока массив не будет состоять из одного элемента.
     * @param left левая граница массива включительно
     * @param right правая граница массива включительно
     */

    private void separate(int left, int right) {
        if(left < right) {
            int middle = sort(left, right);
            separate(left, middle - 1);
            separate(middle, right);
        }
    }

    /**
     * Часть алгоритма быстрой сортировки. Данный метод сортирует массив
     * относительно опорного элемента. Здесь выбирается элемент посередине,
     * хотя на самом деле неважно какой элемент будет выбран, т.к. заранее
     * неизвестно, какой массив будет передан. В результате выполнения данного
     * метода массив будет отсортирован относительно опорного элемента в том плане,
     * что все элементы, которые меньше либо равны опорному будут стоять
     * левее всех элементов, которые больше либо равны опорному.
     * @param left левая граница массива включительно
     * @param right правая граница массива включительно
     * @return начало правого подмассива, в котором все элементы больше либо равны опорному.
     */
    @SuppressWarnings("unchecked")
    private int sort(int left, int right) {
        @SuppressWarnings("unchecked")
        E pivot = (E) elements[left + right >> 1];
        while (left <= right) {
            while (comparator.compare((E) elements[left], pivot) < 0) left++;
            while (comparator.compare((E) elements[right], pivot) > 0) right--;
            if(left <= right) {
                @SuppressWarnings("unchecked")
                E temp = (E) elements[left];
                elements[left] = elements[right];
                elements[right] = temp;
                left++; right--;
            }
        }
        return left;
    }
    /**
     * Клонирует список. Т.к. при клонировании копируются ссылки, то в склонированном списке
     * создается новый внутренний массив, аналогичный по содержанию массиву данного списка.
     * @return клон данного списка
     */
    @Override
    public Object clone() {
        try {
            @SuppressWarnings("unchecked")
            ArrayList<E> clone = (ArrayList<E>) super.clone();
            clone.elements = Arrays.copyOf(elements, size);
            return clone;
        } catch (CloneNotSupportedException ex) {
            throw new InternalError();
        }
    }

    /**
     * Строковое представление списка.
     * @return строковое представление списка
     */

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            builder.append(elements[i]);
            if(i != size - 1) {
                builder.append(", ");
            }
        }
        builder.append("]");
        return builder.toString();
    }
}
