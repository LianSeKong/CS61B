package deque;
import java.util.Iterator;
public class ArrayDeque<T> implements Iterable<T>, Deque<T> {
    private static final int CAPACITY = 2;
    private static final double ZOOM_FACTOR = 0.25;
    private static final int INITIAL_SIZE = 8;
    private T[] items;
    private int size;
    private int beginIndex;
    private int endIndex;

    public ArrayDeque() {
        size = 0;
        items = (T[]) new Object[INITIAL_SIZE];
        beginIndex = items.length / 2;
        endIndex = beginIndex + 1;
    }
    // get first element's index
    private int firstElementIndex() {
        if (beginIndex == items.length - 1) {
            return 0;
        } else {
            return beginIndex + 1;
        }
    }
    // get last element's index
    private int lastElementIndex() {
        if (endIndex == 0) {
            return items.length - 1;
        } else {
            return endIndex - 1;
        }
    }

    private void resize(int capacity) {
        // Create a new size items.
        T[] newItems = (T[]) new Object[capacity];
        // new items's begin index
        int newBeginIndex = (newItems.length) / 4;
        // Copy elements of old items
        for (int i = 0; i < size(); i++) {
            newItems[newBeginIndex + i] = get(i);
        }
        items = newItems;
        beginIndex = newBeginIndex - 1;
        endIndex = newBeginIndex + size;
    }
    public void addFirst(T item) {
        if (size == items.length) {
            resize(size * CAPACITY);
        }
        size += 1;
        items[beginIndex] = item;
        beginIndex = indexFromAddFirst(beginIndex);
    }
    private int indexFromAddFirst(int index) {
        if (index == 0) {
            return items.length - 1;
        } else {
            return index - 1;
        }
    }
    public void addLast(T item) {
        // 扩容
        if (size == items.length) {
            resize(size * CAPACITY);
        }
        size += 1;
        items[endIndex] = item;
        endIndex = indexFromAddLast(endIndex);
    }
    private int indexFromAddLast(int index) {
        if (index == items.length - 1) {
            return 0;
        } else {
            return index + 1;
        }
    }
    private boolean isReducedCapacity() {
        int cap = (int) (items.length * ZOOM_FACTOR);
        return items.length >= 16 && size <= cap;
    }
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        if (isReducedCapacity()) {
            resize(size * CAPACITY);
        }
        int firstIndex = firstElementIndex();
        T returnVal = items[firstIndex];
        items[firstIndex] = null;
        beginIndex = firstIndex;
        size -= 1;
        return returnVal;
    }

    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        if (isReducedCapacity()) {
            resize(size * CAPACITY);
        }
        int lastIndex = lastElementIndex();
        T returnVal = items[lastIndex];
        items[lastIndex] = null;
        endIndex = lastIndex;
        size -= 1;
        return returnVal;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        int currentIndex = (firstElementIndex() + index) % items.length;
        return items[currentIndex];
    }
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    private class ArrayDequeIterator implements Iterator<T> {
        private int i;
        ArrayDequeIterator() {
            i = 0;
        }

        @Override
        public boolean hasNext() {
            return i < size();
        }

        public T next() {
            T t = get(i);
            i += 1;
            return t;
        }

    }

    public boolean equals(Object o) {
        if (!(o instanceof Deque)) {
            return false;
        }
        Deque<T> ad = (Deque<T>) o;
        if (ad.size() != size()) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!ad.get(i).equals(get(i))) {
                return false;
            }
        }
        return true;
    }
    @Override
    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(get(i) + " ");
        }
        System.out.println();
    }

    public int size() {
        return size;
    }
}
