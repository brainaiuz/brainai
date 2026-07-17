package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import java.util.Map;

public class NumericWidget {

    private String title;
    private String value;
    private String color;
    private int x;
    private int y;
    private int width;
    private int height;
    private Map<String, String> tableItems;
    private String type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Map<String, String> getTableItems() {
        return tableItems;
    }

    public void setTableItems(Map<String, String> tableItems) {
        this.tableItems = tableItems;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
