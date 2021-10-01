package main.java.lrucache.node;

import main.java.lrucache.DLinkedList;
import main.java.lrucache.ListNode;

public class MyListNode<T> implements ListNode<T> {
    private final T value;
    private final DLinkedList<T> list;
    private ListNode<T> next;
    private ListNode<T> prev;

    public MyListNode(T value, ListNode<T> next, DLinkedList<T> list) {
        this.value = value;
        this.list = list;
        this.next = next;
        this.setPrev(next.getPrev());
        this.prev.setNext(this);
        this.next.setPrev(this);
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void quit() {
        this.prev.setNext(this.getNext());
        this.next.setPrev(this.getPrev());
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public DLinkedList<T> getList() {
        return this.list;
    }

    @Override
    public ListNode<T> getPrev() {
        return this.prev;
    }

    @Override
    public ListNode<T> getNext() {
        return this.next;
    }

    @Override
    public void setPrev(ListNode<T> prev) {
        this.prev = prev;
    }

    @Override
    public void setNext(ListNode<T> next) {
        this.next = next;
    }

    @Override
    public ListNode<T> search(T value) {
        return this.getValue() == value ? this : this.getNext().search(value);
    }
}
