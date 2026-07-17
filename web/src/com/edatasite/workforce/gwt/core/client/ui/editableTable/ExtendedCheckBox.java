package com.edatasite.workforce.gwt.core.client.ui.editableTable;

import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 23/08/12
 * Time: 22:04
 * To change this template use File | Settings | File Templates.
 */
public class ExtendedCheckBox extends KpiCheckBox implements CustomCellInterface {


    public ExtendedCheckBox() {
        super();
    }


    @Override
    public String getDisplayValue() {
        return "      ";
    }

    @Override
    public void setItemValue(Object value) {
        setValue(Boolean.parseBoolean(value.toString()));
    }

    @Override
    public void setItemFocus(boolean focused) {
        setFocus(focused);
    }
}
