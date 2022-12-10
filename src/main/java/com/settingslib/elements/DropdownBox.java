package com.settingslib.elements;

public class DropdownBox extends Setting<Integer> {
    private final String[] options;
    public DropdownBox(String name, int id, boolean locked, int currentValue, int defaultValue, String... options) {
        super(name, id, locked, currentValue, defaultValue);
        this.options = options;
    }
    public int amount() {
        return options.length;
    }
    public String[] options() {
        return options;
    }
    public String getOption(int index) {
        return options[index];
    }
    public String getSelectedOption() {
        return options[getCurrentValue()];
    }
    public String toString() {
        String opts = "";
        for (String option : options) {
            opts += "'" + option + "',";
        }
        if (!opts.isEmpty()) opts = opts.substring(0, opts.length() - 1);
        return super.toString() + ",opts=[" + opts + "],val=" + getCurrentValue() + ",def=" + getDefaultValue();
    }
}
