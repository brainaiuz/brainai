package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.google.gwt.user.client.ui.TextArea;

/**
 * Created by Dilshod Madrahimov on 7/23/15 2:52 PM
 */
public class ExtendedTextArea extends TextArea implements CustomCellInterface {

    public ExtendedTextArea() {
        super();
    }

    @Override
    public String getDisplayValue() {
        return getText();
    }

    @Override
    public void setItemValue(Object value) {
        setText(String.valueOf(value));
    }

    @Override
    public void setItemFocus(boolean focused) {
        setFocus(focused);
    }
}