package main.java.lrucache;

import java.util.Optional;

public interface LRUCache<K, V> {
    int getListSize();
    Optional<V> get(K key);
    void put(K key, V value);
}
