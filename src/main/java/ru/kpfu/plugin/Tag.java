package ru.kpfu.plugin;

import java.util.ArrayList;
import java.util.HashMap;

public class Tag {

    private String name;
    private HashMap<String, String> values;
    private int level;

    public Tag(String name, HashMap<String, String> values, int level) {
        this.name = name;
        this.values = values;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, String> getValues() {
        return values;
    }

    public void setValues(HashMap<String, String> values) {
        this.values = values;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        for (String key : values.keySet()) {
            buffer.append(key).append("=").append(values.get(key)).append("\n");
        }

        return name + ": " + buffer.toString() + " at level=" + level;
    }
}
