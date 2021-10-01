package main.java.lrucache.singlethread;

import main.java.lrucache.DLinkedList;
import main.java.lrucache.ListNode;
import main.java.lrucache.node.EmptyListNode;
import main.java.lrucache.node.MyListNode;

public class SingleThreadDLinkedList<T> implements DLinkedList<T> {
    private final EmptyListNode<T> emptyListNode;
    private ListNode<T> head;
    private ListNode<T> tail;
    private int size;

    public SingleThreadDLinkedList() {
        this.emptyListNode = new EmptyListNode<>(this);
        clear();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        head = emptyListNode;
        tail = emptyListNode;
        size = 0;
    }

    @Override
    public void remove(ListNode<T> node) {
        if (node == tail) {
            removeFromTail();
        } else{
            node.quit();
            if (node == head) {
                head = head.getNext();
            }
            size--;
        }
    }

    @Override
    public ListNode<T> removeFromTail() {
        ListNode<T> oldTail = tail;
        if (oldTail == head) {
            tail = head = emptyListNode;
        } else {
            tail = tail.getPrev();
            oldTail.quit();
        }
        if (!oldTail.isEmpty()) {
            size--;
        }
        return oldTail;
    }

    @Override
    public ListNode<T> addToFront(T value) {
        head = new MyListNode<>(value, head, this);
        if (tail.isEmpty()) {
            tail = head;
        }
        size++;
        return head;
    }

    @Override
    public ListNode<T> shiftToFront(ListNode<T> node) {
        return node.isEmpty() ? emptyListNode : updateAndShiftToFront(node, node.getValue());
    }

    @Override
    public ListNode<T> updateAndShiftToFront(ListNode<T> node, T newValue) {
        if (node.isEmpty() || (this != node.getList())) {
            // if node is empty or not contained in this list
            return emptyListNode;
        }
        remove(node);
        addToFront(newValue);
        return head;
    }
}
