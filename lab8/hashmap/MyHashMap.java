package hashmap;

import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!
    private double loadFactor;
    private Collection<Node> items;

    /** Constructors */
    public MyHashMap() {
        buckets = createTable(16);
        loadFactor = 0.75;
        items = createBucket();
    }

    public MyHashMap(int initialSize) {
        buckets = createTable(initialSize);
        loadFactor = 0.75;
        items = createBucket();
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        buckets = createTable(initialSize);
        loadFactor = maxLoad;
        items = createBucket();
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] c = new Collection[tableSize];
        for (int i = 0; i < tableSize; i++) {
            c[i] = createBucket();
        }
        return c;
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!
    @Override
    public void clear() {
        items.clear();
        for (Collection<Node> c : buckets) {
            c.clear();
        }
    }

    /** Helper Function
     *  position in buckets
     */
    private int reduceIndex(K key, int len) {
        return Math.floorMod(key.hashCode(), len);
    }

    @Override
    public boolean containsKey(K key) {
        int index = reduceIndex(key, buckets.length);
        for (Node n: buckets[index]) {
            if (n.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V get(K key) {
        int index = reduceIndex(key, buckets.length);
        for (Node n: buckets[index]) {
            if (n.key.equals(key)) {
               return n.value;
            }
        }
        return null;
    }
    public int size() {
        return items.size();
    }

    @Override
    public void put(K key, V value) {
        if (containsKey(key)) {
            for (Node n: items) {
                if (n.key.equals(key)) {
                    n.value = value;
                }
            }
            int index = reduceIndex(key, buckets.length);
            for (Node n: buckets[index]) {
                if (n.key.equals(key)) {
                    n.value = value;
                }
            }
        } else {
            Node node = createNode(key, value);
            double currentFactor = (double) size() / (double) buckets.length;
            if (currentFactor >= loadFactor) {
                Collection<Node>[] expandBuckets = createTable(buckets.length * 2);
                for (Node n: items) {
                    int index = reduceIndex(n.key, expandBuckets.length);
                    expandBuckets[index].add(n);
                }
                buckets = expandBuckets;
            }
            items.add(node);
            int index = reduceIndex(node.key, buckets.length);
            buckets[index].add(node);
        }
    }

    @Override
    public Set<K> keySet() {
        Set<K> s = new HashSet<>();
        for (Node n: items) {
            s.add(n.key);
        }
        return s;
    }

    @Override
    public V remove(K key) {
          return remove(key,get(key));
    }

    @Override
    public V remove(K key, V value) {
        if (containsKey(key)) {
            for (Node n: items) {
                if (n.key.equals(key) || n.value.equals(value)) {
                    items.remove(n);
                    break;
                }
            }
            int index = reduceIndex(key, buckets.length);
            for (Node n: buckets[index]) {
                if (n.key.equals(key) || n.value.equals(value)) {
                    buckets[index].remove(n);
                    break;
                }
            }
            return value;
        }
        return null;
    }

    @Override
    public Iterator<K> iterator() {
        return new MyIterator();
    }

    private class MyIterator implements Iterator<K> {
        private LinkedList<K> s;
        private int i;
        MyIterator() {
            s = new LinkedList<>();
            for (Node n: items) {
                s.add(n.key);
            }
            i = 0;
        }

        @Override
        public boolean hasNext() {
            return i < size();
        }

        public K next() {
            K k = s.get(i);
            i += 1;
            return k;
        }

    }
}
