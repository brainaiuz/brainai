package com.edatasite.workforce.gwt.core.client.form;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: Abror Abdukadirov
 * Date: 11.10.2019 17:30
 */
public class CustomFormAttributeItem implements IsSerializable {
    private Integer id;
    private String formId;
    private String fieldId;
    private String fieldType;
    private String label;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getFieldType() {
        return fieldType;
    }

    public void setFieldType(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
