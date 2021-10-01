package main.java.lrucache.singlethread;

import main.java.lrucache.CachedEntity;
import main.java.lrucache.LRUCache;
import main.java.lrucache.ListNode;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class SingleThreadLRUCache<K, V> implements LRUCache<K, V> {
    private final Map<K, ListNode<CachedEntity<K, V>>> map;
    private final SingleThreadDLinkedList<CachedEntity<K, V>> list;
    private final int size;

    public SingleThreadLRUCache(int maxSize) {
        map = new ConcurrentHashMap<>(maxSize);
        list = new SingleThreadDLinkedList<>();
        size = maxSize;
    }

    @Override
    public Optional<V> get(K key) {
        ListNode<CachedEntity<K, V>> node = map.get(key);
        if (node != null && !node.isEmpty()) {
            // if good node
            map.put(key, list.shiftToFront(node));
            return Optional.of(node.getValue().getValue());
        }
        return Optional.empty();
    }

    @Override
    public int getListSize() {
        return list.size();
    }

    @Override
    public void put(K key, V value) {
        CachedEntity<K, V> entity = new CachedEntity<>(key, value);
        ListNode<CachedEntity<K, V>> newNode;
        if (map.containsKey(key)) {
            // already cached -> update it's position
            ListNode<CachedEntity<K, V>> node = map.get(key);
            newNode = list.updateAndShiftToFront(node, entity);
        } else {
            // fresh one
            if (getListSize() >= size) {
                // delete the oldest node
                ListNode<CachedEntity<K, V>> node = list.removeFromTail();
                if (node.isEmpty()) {
                    return;
                }
                map.remove(node.getValue().getKey());
            }
            newNode = list.addToFront(entity);
        }
        if (newNode.isEmpty()) {
            return;
        }
        map.put(key, newNode);
    }
}
