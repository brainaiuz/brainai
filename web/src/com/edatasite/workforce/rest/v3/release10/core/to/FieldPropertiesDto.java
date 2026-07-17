package com.edatasite.workforce.rest.v3.release10.core.to;

import com.edatasite.workforce.gwt.core.client.form.CustomizeFormItem;

import java.util.List;

public class FieldPropertiesDto {
    private String section;
    private List<CustomizeFormItem> fields;

    public FieldPropertiesDto() {
    }

    public FieldPropertiesDto(String section, List<CustomizeFormItem> fields) {
        this.section = section;
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
}
