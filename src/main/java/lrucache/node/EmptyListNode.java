package main.java.lrucache.node;

import main.java.lrucache.DLinkedList;
import main.java.lrucache.ListNode;

public class EmptyListNode<T> implements ListNode<T> {
    private final DLinkedList<T> list;

    public EmptyListNode(DLinkedList<T> list) {
        this.list = list;
    }

    @Override
    public void quit() {
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public T getValue() throws NullPointerException {
        throw new NullPointerException();
    }

    @Override
    public DLinkedList<T> getList() {
        return list;
    }

    @Override
    public ListNode<T> getPrev() {
        return this;
    }

    @Override
    public ListNode<T> getNext() {
        return this;
    }

    @Override
    public void setPrev(ListNode<T> next) {
    }

    @Override
    public void setNext(ListNode<T> prev) {
            }

    @Override
    public ListNode<T> search(T value) {
        return this;
    }
}
