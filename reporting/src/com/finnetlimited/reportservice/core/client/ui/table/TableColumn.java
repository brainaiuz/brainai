package com.finnetlimited.reportservice.core.client.ui.table;

/**
 * Created by IntelliJ IDEA.
 * User: nodir
 * Date: 05.08.2010
 * Time: 21:21:12
 * To change this template use File | Settings | File Templates.
 */
public final class TableColumn {

    private static final int minWidth = 50;
    private static final int maxWidth = 500;

    private String name;
    private String text;

    private int width;

    public TableColumn(String name) {
        this(name, name);
    }

    public TableColumn(String name, String text) {
        this(name, text, minWidth);
    }

    public TableColumn(String name, String text, int width) {
        this.name = name;
        this.text = text;
        this.width = width;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}
