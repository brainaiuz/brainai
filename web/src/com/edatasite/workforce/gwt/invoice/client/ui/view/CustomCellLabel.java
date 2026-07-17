package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.google.gwt.user.client.ui.Label;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 12/20/12
 * Time: 12:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class CustomCellLabel extends Label implements CustomCellInterface {


    public CustomCellLabel() {
        super();
    }

    public CustomCellLabel(String str) {
        super(str);
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
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
