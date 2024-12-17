package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K , V> {

    private BSTNode root;
    public int size() {
        return size(root);
    }

    private int size(BSTNode node) {
        if (node == null) {
            return 0;
        }
        return node.size;
    }

    public void put(K key, V val) {
        if (key == null ) {
            throw new IllegalArgumentException("calls put() with a null key || val");
        }
        root = put(root, key, val);
    }
    private BSTNode put(BSTNode node, K key, V val) {
        if (node == null) {
            return new BSTNode(key, val, 1);
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = put(node.left, key, val);
        } else if (cmp > 0) {
            node.right = put(node.right, key, val);
        } else {
            node.val = val;
        }
        node.size = 1 + size(node.left) + size(node.right);
        return node;
    }

    public V get(K key) {
        if (key == null ) {
            throw new IllegalArgumentException("calls put() with a null key");
        }
        return get(root, key);
    }

    public V get(BSTNode node, K key) {
        if (node == null) {
            return null;
        }
        int cmp =  key.compareTo(node.key);
        if (cmp < 0) {
            return get(node.left, key);
        } else if (cmp > 0) {
            return get(node.right, key);
        } else {
            return node.val;
        }
    }

    public boolean containsKey(K key) {
        return containsKey(root, key);
    }
    public boolean containsKey(BSTNode node, K key) {
        if (node == null) {
            return false;
        }
        int cmp =  key.compareTo(node.key);
        if (cmp < 0) {
            return containsKey(node.left, key);
        } else if (cmp > 0) {
            return containsKey(node.right, key);
        } else {
            return true;
        }
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException("不支持的操作");
    }

    private class BSTNode {
        private final K key;         // sorted by key
        private V val;         // associated data
        private BSTNode left, right;  // left and right subtrees
        private int size;          // number of nodes in subtree
        public BSTNode(K key, V val, int size) {
            this.key = key;
            this.val = val;
            this.size = size;
        }
    }


    public void clear() {
        clear(root);
        root = null;
    }

    public void clear(BSTNode node) {
        if (node == null) {
            return;
        } else {
            clear(node.left);
            clear(node.right);
        }
        node.left = null;
        node.right = null;
    }

    public void printInOrder() {
        printInOrder(root);
    }
    private void printInOrder(BSTNode node) {
        if (node == null) return;
        printInOrder(node.left);
        System.out.println("key: " + node.key + ", value: " + node.val);
        printInOrder(node.right);
    }

    public Set<K> keySet() {
        throw new UnsupportedOperationException("dont support keySet() method!");
    }

    public V remove(K key) {
        throw new UnsupportedOperationException("dont support removeK(K key) method!");
    }

    public V remove(K key, V value) {
        throw new UnsupportedOperationException("dont support remove(K key, V value) method!");
    }


}
