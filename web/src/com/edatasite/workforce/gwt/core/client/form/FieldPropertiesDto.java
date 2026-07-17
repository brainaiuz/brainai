package com.edatasite.workforce.gwt.core.client.form;

import java.util.List;

public class FieldPropertiesDto {
    private String section;
    private boolean expanded;
    private List<CustomizeFormItem> fields;

    public FieldPropertiesDto() {
    }

    public FieldPropertiesDto(String section, List<CustomizeFormItem> fields) {
        this.section = section;
        this.fields = fields;
    }

    public FieldPropertiesDto(String section, boolean expanded, List<CustomizeFormItem> fields) {
        this.section = section;
        this.expanded = expanded;
        this.fields = fields;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public List<CustomizeFormItem> getFields() {
        return fields;
    }

    public void setFields(List<CustomizeFormItem> fields) {
        this.fields = fields;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
