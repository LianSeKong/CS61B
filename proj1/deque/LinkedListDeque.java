package deque;
import java.util.Iterator;
public class LinkedListDeque<T> implements Iterable<T>, Deque<T> {
    private final LinkedListNode<T> sentinelHead;
    private final LinkedListNode<T> sentinelTail;
    private int size;
    public LinkedListDeque() {
        size = 0;
        sentinelHead = new LinkedListNode<T>((T) new Object());
        sentinelTail = new LinkedListNode<T>((T) new Object());
        sentinelHead.first = sentinelTail;
        sentinelHead.rest = sentinelTail;
        sentinelTail.first = sentinelHead;
        sentinelTail.rest = sentinelHead;
    }
    public void addFirst(T item) {
        LinkedListNode<T> first = sentinelHead.rest;
        LinkedListNode<T> node = new LinkedListNode<T>(item, sentinelHead, first);
        first.first = node;
        sentinelHead.rest = node;
        size += 1;
    }
    public void addLast(T item) {
        LinkedListNode<T> rest = sentinelTail.first;
        LinkedListNode<T> node = new LinkedListNode<T>(item, rest, sentinelTail);
        rest.rest = node;
        sentinelTail.first = node;
        size += 1;
    }
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        LinkedListNode<T> first = sentinelHead.rest;
        sentinelHead.rest = first.rest;
        first.rest.first = sentinelHead;
        size -= 1;
        return first.item;
    }
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        LinkedListNode<T> last = sentinelTail.first;
        sentinelTail.first = last.first;
        last.first.rest = sentinelTail;
        size -= 1;
        return last.item;
    }

    public int size() {
        return size;
    }
    @Override
    public void printDeque() {
        LinkedListNode<T> p = sentinelHead.rest;
        StringBuffer bf = new StringBuffer();
        while (p != sentinelTail) {
            bf.append(p.item).append(" ");
            p = p.rest;
        }
        System.out.println(bf);
    }
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        LinkedListNode<T> p = sentinelHead.rest;
        while (index > 0) {
            p = p.rest;
            index -= 1;
        }
        return p.item;
    }
    private T getRecursiveHelper(LinkedListNode<T> p, int index) {
        if (index == 0) {
            return p.item;
        }
        return getRecursiveHelper(p.rest, index - 1);
    }
    public T getRecursive(int index) {
        if (isEmpty()) {
            return null;
        }
        return getRecursiveHelper(sentinelHead.rest, index);
    }
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Deque)) {
            return false;
        }
        Deque<T> lld =  (Deque<T>) o;
        if (lld.size() != size()) {
            return false;
        }
        for (int i = 0; i < size(); i++) {
            if (!lld.get(i).equals(get(i))) {
                return false;
            }
        }
        return true;
    }
    public Iterator<T> iterator() {
        return new LinkedListDequeIterator();
    }
    private class LinkedListDequeIterator implements Iterator<T> {
        private LinkedListNode<T> p;
        LinkedListDequeIterator() {
            p = sentinelHead.rest;
        }
        public boolean hasNext() {
            return p != sentinelTail;
        }
        public T next() {
            T t = p.item;
            p = p.rest;
            return t;
        }
    }

    public static class LinkedListNode<T> {
        private T item;
        private LinkedListNode<T> first;
        private LinkedListNode<T> rest;
        public LinkedListNode(T i) {
            item = i;
            first = null;
            rest = null;
        }
        public LinkedListNode(T i, LinkedListNode<T> f, LinkedListNode<T> r) {
            item = i;
            first = f;
            rest = r;
        }
    }
}
