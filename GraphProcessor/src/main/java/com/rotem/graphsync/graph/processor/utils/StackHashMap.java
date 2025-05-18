package com.rotem.graphsync.graph.processor.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class StackHashMap<K, V> {

    private final Map<K, V> map = new HashMap<>();
    private final Stack<K> keys = new Stack<>();

    public V pop() {
        K key = keys.pop();
        V value = map.remove(key);

        return value;
    }

    public void push(K key, V value) {
        if(!map.containsKey(key)) {
            keys.push(key);
        }

        map.put(key, value);
    }

    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public boolean isEmpty() {
        return keys.isEmpty();
    }
}
