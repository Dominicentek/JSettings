package com.settingslib;

import com.settingslib.elements.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.zip.CRC32;

public class Settings {
    private boolean readOnly;
    private Category root;
    private String name;
    public Settings(String name) {
        this(name, false);
    }
    public Settings(String name, boolean readOnly) {
        root = new Category(name, 0, false);
        this.readOnly = readOnly;
        this.name = name;
    }
    public static Settings get(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = in.read(buf)) > 0) {
            out.write(buf, 0, bytesRead);
        }
        byte[] data = out.toByteArray();
        ByteBuffer buffer = ByteBuffer.wrap(data);
        if (buffer.get() != 'S' || buffer.get() != 'T' || buffer.get() != 'G' || buffer.get() != 'S') throw new SettingsException("Invalid magic bytes");
        int endianness = Short.toUnsignedInt(buffer.getShort());
        if (endianness == 0xFFFE) buffer.order(ByteOrder.LITTLE_ENDIAN);
        int crc32 = buffer.getInt();
        buffer.position(buffer.position() + 4);
        boolean readOnly = buffer.get() != 0;
        int nameLength = Short.toUnsignedInt(buffer.getShort());
        String name = "";
        for (int i = 0; i < nameLength; i++) {
            name += (char)buffer.get();
        }
        byte[] content = new byte[buffer.capacity() - buffer.position()];
        int headerEnd = buffer.position();
        buffer.get(content, 0, content.length);
        buffer.position(headerEnd);
        int calculatedCrc32 = crc32(content);
        if (crc32 != calculatedCrc32) System.err.println("CRC32 doesn't match, the file may be corrupted");
        Settings settings = new Settings(name, readOnly);
        Element element = getElement(buffer);
        if (element instanceof Category) settings.root = (Category)element;
        else throw new SettingsException("Category expected as first element");
        return settings;
    }
    public byte[] save() {
        ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024);
        buffer.putInt(('S' << 24) | ('T' << 16) | ('G' << 8) | 'S');
        buffer.putShort((short)0xFEFF);
        buffer.putLong(0);
        buffer.put(readOnly ? (byte)1 : 0);
        buffer.putShort((short)name.length());
        for (char character : name.toCharArray()) {
            buffer.put((byte)character);
        }
        int headerEnd = buffer.position();
        saveElement(buffer, root);
        int length = buffer.position();
        byte[] content = new byte[length - headerEnd];
        buffer.position(headerEnd);
        buffer.get(content);
        buffer.position(6);
        buffer.putInt(crc32(content));
        buffer.putInt(length);
        buffer.position(0);
        byte[] data = new byte[length];
        buffer.get(data);
        return data;
    }
    private static Element getElement(ByteBuffer buffer) {
        int type = buffer.get();
        boolean locked = buffer.get() != 0;
        int id = buffer.getInt();
        int nameLength = Short.toUnsignedInt(buffer.getShort());
        String name = "";
        for (int i = 0; i < nameLength; i++) {
            name += (char)buffer.get();
        }
        if (type == 0) {
            Category category = new Category(name, id, locked);
            int options = buffer.getInt();
            for (int i = 0; i < options; i++) {
                category.add(getElement(buffer));
            }
            return category;
        }
        else if (type == 1) {
            boolean value = buffer.get() != 0;
            boolean defaultValue = buffer.get() != 0;
            return new Checkbox(name, id, locked, value, defaultValue);
        }
        else if (type == 2) {
            float min = buffer.getFloat();
            float max = buffer.getFloat();
            float step = buffer.getFloat();
            float value = buffer.getFloat();
            float defaultValue = buffer.getFloat();
            return new Slider(name, id, locked, min, max, step, value, defaultValue);
        }
        else if (type == 3) {
            int optionAmount = buffer.getInt();
            ArrayList<String> options = new ArrayList<>();
            for (int i = 0; i < optionAmount; i++) {
                int length = Short.toUnsignedInt(buffer.getShort());
                String option = "";
                for (int j = 0; j < length; j++) {
                    option += (char)buffer.get();
                }
                options.add(option);
            }
            int value = buffer.getInt();
            int defaultValue = buffer.getInt();
            return new DropdownBox(name, id, locked, value, defaultValue, options.toArray(new String[0]));
        }
        else if (type == 4) {
            return new Button(name, id, locked);
        }
        else if (type == 5) {
            return new Link(name, id, locked, buffer.getInt());
        }
        return null;
    }
    private static void saveElement(ByteBuffer buffer, Element element) {
        if (element instanceof Category) buffer.put((byte)0);
        if (element instanceof Checkbox) buffer.put((byte)1);
        if (element instanceof Slider) buffer.put((byte)2);
        if (element instanceof DropdownBox) buffer.put((byte)3);
        if (element instanceof Button) buffer.put((byte)4);
        if (element instanceof Link) buffer.put((byte)5);
        buffer.put(element.isLocked() ? (byte)1 : 0);
        buffer.putInt(element.getID());
        buffer.putShort((short)element.getName().length());
        for (char character : element.getName().toCharArray()) {
            buffer.put((byte)character);
        }
        if (element instanceof Category) {
            Category category = (Category)element;
            buffer.putInt(category.amount());
            for (Element el : category) {
                saveElement(buffer, el);
            }
        }
        if (element instanceof Checkbox) {
            Checkbox checkbox = (Checkbox)element;
            buffer.put((byte)(checkbox.getCurrentValue() ? 1 : 0));
            buffer.put((byte)(checkbox.getDefaultValue() ? 1 : 0));
        }
        if (element instanceof Slider) {
            Slider slider = (Slider)element;
            buffer.putFloat(slider.getMin());
            buffer.putFloat(slider.getMax());
            buffer.putFloat(slider.getStep());
            buffer.putFloat(slider.getCurrentValue());
            buffer.putFloat(slider.getDefaultValue());
        }
        if (element instanceof DropdownBox) {
            DropdownBox box = (DropdownBox)element;
            buffer.putInt(box.amount());
            for (int i = 0; i < box.amount(); i++) {
                String option = box.options()[i];
                buffer.putShort((short)option.length());
                for (char character : option.toCharArray()) {
                    buffer.put((byte)character);
                }
            }
            buffer.putInt(box.getCurrentValue());
            buffer.putInt(box.getDefaultValue());
        }
        if (element instanceof Link) {
            buffer.putInt(((Link)element).getDestination());
        }
    }
    private static int crc32(byte[] data) {
        CRC32 crc32 = new CRC32();
        crc32.update(data);
        return (int)crc32.getValue();
    }
    public boolean isReadOnly() {
        return readOnly;
    }
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }
    public Category getRootCategory() {
        return root;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Element getElementByID(int id) {
        return root.getElementByID(id);
    }
    public String toString() {
        return "readonly=" + readOnly + ",name='" + name + "',root=" + root;
    }
}
