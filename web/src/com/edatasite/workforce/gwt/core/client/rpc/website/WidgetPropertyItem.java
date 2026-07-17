package com.edatasite.workforce.gwt.core.client.rpc.website;

import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sanjar
 * Date: Nov 6, 2010
 * Time: 3:15:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class WidgetPropertyItem extends SelectItem {
    private String guid;
    private String label;
    private Integer type;
    private String value;
    private String text;
    private String options;
    private String dropdown;
    private SelectItem[] items;
    private Integer[] values;
    private FilterItem[] filters;
    private String defaultValue;

    private FileItem[] files;
    private LinkedHashMap<String, ArrayList<SelectItem>> rolePermissionMap;
    private LinkedHashMap<String, SelectItem> permissionNameMap;

    public WidgetPropertyItem() {
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    public SelectItem[] getItems() {
        return items;
    }

    public void setItems(SelectItem[] items) {
        this.items = items;
    }

    public Integer[] getValues() {
        return values;
    }

    public void setValues(Integer[] values) {
        this.values = values;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public FileItem[] getFiles() {
        return files;
    }

    public void setFiles(FileItem[] files) {
        this.files = files;
    }

    public FilterItem[] getFilters() {
        return filters;
    }

    public void setFilters(FilterItem[] filters) {
        this.filters = filters;
    }

    public String getDropdown() {
        return dropdown;
    }

    public void setDropdown(String dropdown) {
        this.dropdown = dropdown;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setRolePermissionMap(LinkedHashMap<String, ArrayList<SelectItem>> rolePermissionMap) {
        this.rolePermissionMap = rolePermissionMap;
    }

    public LinkedHashMap<String, ArrayList<SelectItem>> getRolePermissionMap() {
        return rolePermissionMap;
    }

    public LinkedHashMap<String, SelectItem> getPermissionNameMap() {
        return permissionNameMap;
    }

    public void setPermissionNameMap(LinkedHashMap<String, SelectItem> permissionNameMap) {
        this.permissionNameMap = permissionNameMap;
    }
}
