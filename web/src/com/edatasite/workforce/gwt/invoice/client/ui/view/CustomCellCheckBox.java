package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;

public class CustomCellCheckBox extends KpiCheckBox implements CustomCellInterface {
    @Override
    public String getDisplayValue() {
        return String.valueOf(getValue());
    }

    @Override
    public void setItemValue(Object value) {
        setValue((Boolean) value);
    }

    @Override
    public void setItemFocus(boolean focused) {
        setFocus(focused);
    }
}
