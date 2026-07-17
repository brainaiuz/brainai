package com.edatasite.workforce.gwt.core.client.ui.listpanel;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.HashMap;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 23-Aug-2010
 * Time: 14:42:22
 */
public class ColumnTool implements IsSerializable {
    private int columnWidth;
    HashMap<String, ColumnColor> colors = new HashMap<>();

    public int getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(int columnWidth) {
        this.columnWidth = columnWidth;
    }

    public boolean hasColor(String value) {
        return colors.containsKey(value);
    }

    public boolean hasColor() {
        return colors.size() > 0;
    }

    public void clearColors() {
        colors.clear();
    }

    public boolean isRowColor(String value) {
        return hasColor(value) && colors.get(value).isRowColor();
    }

    public boolean isColumnColor(String value) {
        return hasColor(value) && colors.get(value).isColumnColor();
    }

    public boolean isWordColor(String value) {
        return hasColor(value) && colors.get(value).isWordColor();
    }

    public String getColor(String value) {
        if (hasColor(value)) {
            return colors.get(value).getColor();
        }
        return null;
    }

    public HashMap<String, ColumnColor> getColors() {
        return colors;
    }

    public void addColor(ColumnColor color) {
        getColors().put(color.getCondition(), color);
    }
}
