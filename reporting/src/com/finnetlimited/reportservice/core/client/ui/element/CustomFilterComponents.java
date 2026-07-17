package com.finnetlimited.reportservice.core.client.ui.element;

import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Marat
 * Date: 18.04.12
 * Time: 14:56
 * */

/**
 * @field field Can be Textbox, Date, Lookup.
 * @field fieldName Title for field.
 * @field fieldDate Used only when 2 date fields available.
 * @field First value has every widget (field).
 * @field Type of widget.
 */
public class CustomFilterComponents {
    private Widget field;
    private String fieldName;
    private String value;
    private String type;

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Widget getField() {
        return field;
    }

    public void setField(Widget field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
