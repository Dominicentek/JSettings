package com.settingslib.elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class Category extends Element implements Iterable<Element> {
    private final ArrayList<Element> elements = new ArrayList<>();
    public Category(String name, int id, boolean locked, Element... elements) {
        super(name, id, locked);
        this.elements.addAll(Arrays.asList(elements));
    }
    public int amount() {
        return elements.size();
    }
    public Element[] elements() {
        return elements.toArray(new Element[0]);
    }
    public void add(Element element) {
        elements.add(element);
    }
    public void remove(Element element) {
        elements.remove(element);
    }
    public void clear() {
        elements.clear();
    }
    public boolean contains(Element element) {
        return elements.contains(element);
    }
    public Iterator<Element> iterator() {
        return elements.iterator();
    }
    public Element getElementByID(int id) {
        for (Element element : elements) {
            if (element.getID() == id) return element;
            if (element instanceof Category) {
                Element el = ((Category)element).getElementByID(id);
                if (el == null) continue;
                return el;
            }
        }
        return null;
    }
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.toString());
        builder.append(",els=[");
        for (Element element : elements) {
            builder.append(element).append(";");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append("]");
        return builder.toString();
    }
}
