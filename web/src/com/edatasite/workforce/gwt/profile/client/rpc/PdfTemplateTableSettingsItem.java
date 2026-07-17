package com.edatasite.workforce.gwt.profile.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: Abror Abdukadirov
 * Date: 11.01.2019 13:48
 */
public class PdfTemplateTableSettingsItem implements IsSerializable {
    private String columnCode;
    private String columnTitle;
    private String columnDefaultTitle;
    private int width;
    private int alignment;  //0 - left, 1 -center, 2 - right
    private boolean selected;
    private Integer sorder = 0;
    private boolean isCustomField = false;

    public PdfTemplateTableSettingsItem() {
    }

    public PdfTemplateTableSettingsItem(String columnCode, String columnTitle, String columnDefaultTitle, boolean selected, int width, int alignment) {
        this(columnCode, columnTitle, columnDefaultTitle, selected, width, alignment, false);
    }

    public PdfTemplateTableSettingsItem(String columnCode, String columnTitle, String columnDefaultTitle, boolean selected, int width, int alignment, boolean isCustomField) {
        this.columnCode = columnCode;
        this.columnTitle = columnTitle;
        this.columnDefaultTitle = columnDefaultTitle;
        this.selected = selected;
        this.width = width;
        this.alignment = alignment;
        this.isCustomField = isCustomField;
    }

    public String getColumnCode() {
        return columnCode;
    }

    public void setColumnCode(String columnCode) {
        this.columnCode = columnCode;
    }

    public String getColumnTitle() {
        return columnTitle;
    }

    public void setColumnTitle(String columnTitle) {
        this.columnTitle = columnTitle;
    }

    public String getColumnDefaultTitle() {
        return columnDefaultTitle;
    }

    public void setColumnDefaultTitle(String columnDefaultTitle) {
        this.columnDefaultTitle = columnDefaultTitle;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getAlignment() {
        return alignment;
    }

    public void setAlignment(int alignment) {
        this.alignment = alignment;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Integer getSorder() {
        return sorder;
    }

    public void setSorder(Integer sorder) {
        this.sorder = sorder;
    }

    public boolean isCustomField() {
        return isCustomField;
    }

    public void setCustomField(boolean customField) {
        isCustomField = customField;
    }
}
