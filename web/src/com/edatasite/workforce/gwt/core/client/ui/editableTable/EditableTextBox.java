package com.edatasite.workforce.gwt.core.client.ui.editableTable;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 13/08/12
 * Time: 13:33
 * To change this template use File | Settings | File Templates.
 */
public class EditableTextBox extends TextBox implements CustomCellInterface {

    private static NumberFormat numberFormat = Utils.getCalculationNumberFormat();

    private String additionalField;
    private String emptyValue;
    private boolean numericField;
    private Integer additionalId;

    public EditableTextBox() {
        super();
    }

    public EditableTextBox(String emptyValue, boolean numericField) {
        super();
        this.emptyValue = emptyValue;
        this.numericField = numericField;
    }

    @Override
    public String getDisplayValue() {
        String value = "";
        if (numericField) {
            value = "".equals(getText()) ? emptyValue : numberFormat.format(numberFormat.parse(getText()));
        } else {
            value = getText();
        }
        return value;
    }

    @Override
    public void setItemValue(Object value) {
        setText((String) value);
    }

    @Override
    public void setItemFocus(boolean focused) {
        setFocus(focused);
    }

    public String getAdditionalField() {
        return additionalField;
    }

    public void setAdditionalField(String additionalField) {
        this.additionalField = additionalField;
    }

    public Integer getAdditionalId() {
        return additionalId;
    }

    public void setAdditionalId(Integer additionalId) {
        this.additionalId = additionalId;
    }
}
