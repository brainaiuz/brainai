package com.edatasite.workforce.rest.v2.release10.core.to.base;

/**
 * Created by Abdurakhmonov Farrukh on 14/03/2018.
 */
public class FieldStateTO extends ResponseData {
    private String field;
    private Boolean required;
    private boolean canEdit;

    public FieldStateTO() {
    }

    public FieldStateTO(String field, Boolean required) {
        this.field = field;
        this.required = required;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }
}
