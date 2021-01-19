package temp;

import java.util.*;

public class MyHashMap<K, V> {
    private class Node {
        private V value;
        private K key;

        public Node(K key, V value) {
            this.value = value;
            this.key = key;
        }
    }

    private final int listLength = 20000;
    private ArrayList<Node> list = new ArrayList<>(listLength);

    public MyHashMap() {
        for (int i = 0; i < listLength; i++) {
            list.add(null);
        }
    }

    public Set<K> keySet() {
        Set<K> set = new TreeSet<>();
        for (Node node : list) {
            if (node != null) {
                set.add(node.key);
            }
        }
        return set;
    }

    public V get(K key) {
        int hash = Objects.hashCode(key);
        if (list.get(hash & (listLength - 1)) != null) {
            int currentIndex = hash & (listLength - 1);
            if(list.get(currentIndex).key.equals(key)){
                return list.get(currentIndex).value;
            }
            currentIndex++;
            while (list.get(currentIndex) != null) {
                if (!list.get(currentIndex).key.equals(key)) {
                    currentIndex++;
                }else{
                    return list.get(currentIndex).value;
                }
                if (currentIndex == list.size()) {
                    return null;
                }
            }
            return null;
        } else {
            return null;
        }
    }

    public void put(K key, V value) {
        int hash = Objects.hashCode(key);
        if (list.get(hash & (listLength - 1)) != null) {
            int currentIndex = hash & (listLength - 1);
            while (list.get(currentIndex) != null) {
                if (list.get(currentIndex).key.equals(key)) {
                    list.get(currentIndex).value = value;
                    return;
                }
                currentIndex++;
                if(currentIndex == listLength) {
                    list.add(null);
                }
            }
            list.set(currentIndex, new Node(key, value));
        } else {
            list.set(hash & (listLength - 1), new Node(key, value));
        }
    }
}
