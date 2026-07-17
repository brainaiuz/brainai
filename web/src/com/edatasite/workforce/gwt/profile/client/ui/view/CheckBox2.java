package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.google.gwt.dom.client.Style;

/**
 * Created by Virus on 3/26/2014.
 */
public class CheckBox2 extends KpiCheckBox implements CustomCellInterface {
    public CheckBox2() {
    }

    @Override
    public String getDisplayValue() {
       return getValue() ? "<img width=\"20px\" height=\"20px\" src=\"customisation/workforcetrack/images/tick.png\"/>" : "<img width=\"20px\" height=\"20px\" src=\"customisation/workforcetrack/images/circle.png\"/>" ;
    }

    @Override
    public void setItemValue(Object value) {
        setValue((Boolean) value);
    }

    @Override
    public void setItemFocus(boolean focused) {
        getElement().getFirstChildElement().getStyle().setWidth(100, Style.Unit.PCT);
    }
}
