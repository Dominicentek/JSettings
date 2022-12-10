package com.settingslib.elements;

public class Checkbox extends Setting<Boolean> {
    public Checkbox(String name, int id, boolean locked, boolean currentValue, boolean defaultValue) {
        super(name, id, locked, currentValue, defaultValue);
    }
    public String toString() {
        return super.toString() + ",val=" + getCurrentValue() + ",def=" + getDefaultValue();
    }
}
