package main.java.lrucache.concurrent;

import main.java.lrucache.CachedEntity;
import main.java.lrucache.LRUCache;
import main.java.lrucache.ListNode;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConcurrentLRUCache<K, V> implements LRUCache<K, V> {
    private final Map<K, ListNode<CachedEntity<K, V>>> linkedListNodeMap;
    private final ConcurrentDLinkedList<CachedEntity<K, V>> doublyLinkedList;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final int size;

    public ConcurrentLRUCache(int maxSize) {
        size = maxSize;
        linkedListNodeMap = new ConcurrentHashMap<>(size);
        doublyLinkedList = new ConcurrentDLinkedList<>();
    }

    @Override
    public Optional<V> get(K key) {
        lock.readLock().lock();
        try {
            ListNode<CachedEntity<K, V>> node = this.linkedListNodeMap.get(key);
            if (node != null && !node.isEmpty()) {
                // if good node
                linkedListNodeMap.put(key, this.doublyLinkedList.shiftToFront(node));
                return Optional.of(node.getValue().getValue());
            }
            return Optional.empty();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public int getListSize() {
        lock.readLock().lock();
        try {
            return doublyLinkedList.size();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void put(K key, V value) {
        lock.writeLock().lock();
        try {
            CachedEntity<K, V> entity = new CachedEntity<>(key, value);
            ListNode<CachedEntity<K, V>> newNode;
            if (linkedListNodeMap.containsKey(key)) {
                // already cached -> update it's position
                ListNode<CachedEntity<K, V>> node = linkedListNodeMap.get(key);
                newNode = doublyLinkedList.updateAndShiftToFront(node, entity);
            } else {
                // fresh one
                if (getListSize() >= size) {
                    // delete the oldest node
                    lock.writeLock().lock();
                    try {
                        ListNode<CachedEntity<K, V>> node = doublyLinkedList.removeFromTail();
                        if (!node.isEmpty()) {
                            linkedListNodeMap.remove(node.getValue().getKey());
                        }
                    } finally {
                        lock.writeLock().unlock();
                    }
                }
                newNode = doublyLinkedList.addToFront(entity);
            }
            if (newNode.isEmpty()) {
                return;
            }
            linkedListNodeMap.put(key, newNode);
        } finally {
            lock.writeLock().unlock();
        }
    }
}
