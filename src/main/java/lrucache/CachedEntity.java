package main.java.lrucache;

public class CachedEntity<K,V> {
    private final K key;
    private final V value;

    public CachedEntity(K key, V value) {
        this.value = value;
        this.key = key;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
}
