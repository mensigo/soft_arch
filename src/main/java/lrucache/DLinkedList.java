package main.java.lrucache;

public interface DLinkedList<T> {
    int size();
    void clear();
    void remove(ListNode<T> node);
    ListNode<T> removeFromTail();
    ListNode<T> addToFront(T value);
    ListNode<T> shiftToFront(ListNode<T> node);
    ListNode<T> updateAndShiftToFront(ListNode<T> node, T newValue);
}
