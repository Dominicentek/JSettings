package com.settingslib;

public interface ButtonHandler {
    void handle(int id);
    static void setGlobalHandler(ButtonHandler handler) {

    }
}
