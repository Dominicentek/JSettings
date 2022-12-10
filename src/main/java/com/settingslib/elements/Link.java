package com.settingslib.elements;

public class Link extends Element {
    private int dest;
    public Link(String name, int id, boolean locked, int dest) {
        super(name, id, locked);
    }
    public int getDestination() {
        return dest;
    }
    public String toString() {
        return super.toString() + ",dst=" + dest;
    }
}
