package com.settingslib.elements;

import com.settingslib.ButtonHandler;

public class Button extends Element {
    private static ButtonHandler handler = id -> {};
    public Button(String name, int id, boolean locked) {
        super(name, id, locked);
    }
    public void click() {
        handler.handle(getID());
    }
    public static void setHandler(ButtonHandler handler) {
        Button.handler = handler;
    }
}
