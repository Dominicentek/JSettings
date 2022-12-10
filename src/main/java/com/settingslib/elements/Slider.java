package com.settingslib.elements;

public class Slider extends Setting<Float> {
    private float min;
    private float max;
    private float step;
    public Slider(String name, int id, boolean locked, float min, float max, float step, float currentValue, float defaultValue) {
        super(name, id, locked, currentValue, defaultValue);
        this.min = min;
        this.max = max;
        this.step = step;
        correct();
    }
    public float getMax() {
        return max;
    }
    public float getMin() {
        return min;
    }
    public float getStep() {
        return step;
    }
    public void increment() {
        setCurrentValue(getCurrentValue() + step);
    }
    public void decrement() {
        setCurrentValue(getCurrentValue() - step);
    }
    public void setCurrentValue(Float value) {
        super.setCurrentValue(value);
        correct();
    }
    public void restoreDefault() {
        super.restoreDefault();
        correct();
    }
    public void correct() {
        if (getCurrentValue() < min) super.setCurrentValue(min);
        if (getCurrentValue() > max) super.setCurrentValue(max);
    }
    public String toString() {
        return super.toString() + ",min=" + min + ",max=" + max + ",step=" + step + ",val=" + getCurrentValue() + ",def=" + getDefaultValue();
    }
}
