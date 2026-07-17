package com.edatasite.workforce.gwt.core.client.ui.editableTable;

import com.google.gwt.user.client.ui.Widget;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 2/11/12
 * Time: 3:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class ColumnConfig {

    /**
     * Column Name for key(Column Id)
     */
    private String name;

    /**
     * Display Column Name
     */
    private String title;

    /**
     * column width [PX]
     */
    private int width;

    /**
     * column has pixel or percentage
     */
    private boolean isPixel;
    /**
     * column has changed
     */
    private boolean changed;
    /**
     * column has disabled
     */
    private boolean disabled;

    /**
     * Column Cell Type
     * here will be TextBoxCell, DatePackerCell, ComboBoxCell ...
     */
    private Class cell;

    /*
    * column valid type
    * */
    private Boolean isRequired;

    private boolean forceWidthInPercent;


    private String customStyleName;
    private BigDecimal minValue;

    public ColumnConfig(Class cell, String name, int width) {
        this(cell, name, width, false);
    }

    public ColumnConfig(Class cell, String name, int width, Boolean isRequired) {
        this(cell, name, "", width, isRequired);
    }

    public ColumnConfig(Class cell, String name, int width, Boolean isRequired, String customStyleName) {
        this(cell, name, "", width, isRequired, customStyleName);
    }

    public ColumnConfig(Class cell, String name, String title, int width) {
        this(cell, name, title, width, false);
    }

    public ColumnConfig(Class cell, String name, String title, int width, Boolean isRequired) {
        this(cell, name, title, width, isRequired, false);
    }

    public ColumnConfig(Class cell, String name, String title, int width, Boolean isRequired, boolean isPixel) {
        this.cell = cell;
        this.name = name;
        this.title = title;
        this.width = width;
        this.isPixel = isPixel;
        this.isRequired = isRequired;
    }

    public ColumnConfig(Class cell, String name, String title, int width, Boolean isRequired, String customStyleName) {
        this(cell, name, title, width, isRequired, customStyleName, false);
    }

    public ColumnConfig(Class cell, String name, String title, int width, Boolean isRequired, String customStyleName, boolean isPixel) {
        this.cell = cell;
        this.name = name;
        this.title = title;
        this.width = width;
        this.isRequired = isRequired;
        this.customStyleName = customStyleName;
        this.isPixel = isPixel;
    }

    public Class getCell() {
        return cell;
    }

    public void setCell(Class cell) {
        this.cell = cell;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public boolean isPixel() {
        return isPixel;
    }

    public void setPixel(boolean pixel) {
        isPixel = pixel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isRequired() {
        return isRequired;
    }

    public void setRequired(Boolean required) {
        isRequired = required;
    }

    public String getCustomStyleName() {
        return customStyleName;
    }

    public void setCustomStyleName(String customStyleName) {
        this.customStyleName = customStyleName;
    }

    public boolean isForceWidthInPercent() {
        return forceWidthInPercent;
    }

    public void setForceWidthInPercent(boolean forceWidthInPercent) {
        this.forceWidthInPercent = forceWidthInPercent;
    }

    public boolean isChanged() {
        return this.changed;
    }

    public void setChanged(final boolean changed) {
        this.changed = changed;
    }

    public boolean isDisabled() {
        return this.disabled;
    }

    public void setDisabled(final boolean disabled) {
        this.disabled = disabled;
    }

    public BigDecimal getMinValue() {
        return minValue;
    }

    public void setMinValue(final BigDecimal minValue) {
        this.minValue = minValue;
    }
}
