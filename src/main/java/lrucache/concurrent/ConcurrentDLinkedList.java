package main.java.lrucache.concurrent;

import main.java.lrucache.DLinkedList;
import main.java.lrucache.node.EmptyListNode;
import main.java.lrucache.ListNode;
import main.java.lrucache.node.MyListNode;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConcurrentDLinkedList<T> implements DLinkedList<T> {
    private final EmptyListNode<T> emptyListNode;
    private ListNode<T> head;
    private ListNode<T> tail;
    private AtomicInteger size;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public ConcurrentDLinkedList() {
        emptyListNode = new EmptyListNode<T>(this);
        clear();
    }

    @Override
    public int size() {
        lock.readLock().lock();
        try {
            return size.get();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void clear() {
        lock.writeLock().lock();
        try {
            head = emptyListNode;
            tail = emptyListNode;
            size = new AtomicInteger(0);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void remove(ListNode<T> node) {
        if (node != tail) {
            node.quit();
            if (node == head) {
                head = head.getNext();
            }
            size.decrementAndGet();
        } else {
            removeFromTail();
        }
    }

    @Override
    public ListNode<T> removeFromTail() {
        lock.writeLock().lock();
        try {
            ListNode<T> oldTail = tail;
            if (oldTail == head) {
                head = emptyListNode;
                tail = emptyListNode;
            } else {
                tail = tail.getPrev();
                oldTail.quit();
            }
            if (!oldTail.isEmpty()) {
                size.decrementAndGet();
            }
            return oldTail;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public ListNode<T> addToFront(T value) {
        lock.writeLock().lock();
        try {
            head = new MyListNode<>(value, head, this);
            if (tail.isEmpty()) {
                tail = head;
            }
            size.incrementAndGet();
            return head;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public ListNode<T> shiftToFront(ListNode<T> node) {
        return node.isEmpty() ? emptyListNode : updateAndShiftToFront(node, node.getValue());
    }

    @Override
    public ListNode<T> updateAndShiftToFront(ListNode<T> node, T newValue) {
        lock.writeLock().lock();
        try {
            if (node.isEmpty() || (this != (node.getList()))) {
                return emptyListNode;
            }
            remove(node);
            addToFront(newValue);
            return head;
        } finally {
            lock.writeLock().unlock();
        }
    }
}
