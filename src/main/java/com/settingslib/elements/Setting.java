package com.settingslib.elements;

public abstract class Setting<T> extends Element {
    private T defaultValue;
    private T currentValue;
    public Setting(String name, int id, boolean locked, T currentValue, T defaultValue) {
        super(name, id, locked);
        this.currentValue = currentValue;
        this.defaultValue = defaultValue;
    }
    public T getDefaultValue() {
        return defaultValue;
    }
    public T getCurrentValue() {
        return currentValue;
    }
    public void setCurrentValue(T value) {
        this.currentValue = value;
    }
    public void restoreDefault() {
        currentValue = defaultValue;
    }
    public boolean isDefault() {
        return defaultValue.equals(currentValue);
    }
}
