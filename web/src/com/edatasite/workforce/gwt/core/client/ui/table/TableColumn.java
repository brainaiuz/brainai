package com.edatasite.workforce.gwt.core.client.ui.table;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: May 19, 2010
 * Time: 5:13:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class TableColumn<D> {

    private static final int minWidth = 50;
    private static final int maxWidth = 500;

    private String name;
    private String text;
    private TableItemValue<D> tableCellValue;
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
