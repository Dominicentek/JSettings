package com.settingslib.elements;

public abstract class Element {
    private String name;
    private int id;
    private boolean locked;
    private Category parent;
    public Element(String name, int id, boolean locked) {
        this.name = name;
        this.id = id;
        this.locked = locked;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getID() {
        return id;
    }
    public boolean isLocked() {
        return locked;
    }
    public void setLocked(boolean locked) {
        this.locked = locked;
    }
    public Category getParent() {
        return parent;
    }
    void setParent(Category parent) {
        this.parent = parent;
    }
    public String toString() {
        return getClass().getSimpleName() + " locked=" + locked + ",id=" + id + ",name='" + name + "'";
    }
}
