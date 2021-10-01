package main.java.lrucache;

public interface ListNode<V> {
    boolean isEmpty();
    void quit();
    V getValue() throws NullPointerException;
    DLinkedList<V> getList();
    ListNode<V> getPrev();
    ListNode<V> getNext();
    void setPrev(ListNode<V> prev);
    void setNext(ListNode<V> next);
    ListNode<V> search(V value);
}
