package com.edatasite.workforce.gwt.core.client.ui.components;

import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.user.client.ui.TextArea;

/**
 * Created by: Azazello
 * Date: 1/12/2018
 * Time: 1:11 PM
 */
public class KpiTextArea extends TextArea implements Constants, CustomCellInterface {
    private Integer[] entryIds;

    public KpiTextArea() {
        addStyleName("form-control");
    }

    public void setPlaceholder(String placeholder) {
        if (placeholder != null && !"".equals(placeholder)) {
            getElement().setAttribute("placeholder", placeholder);
        }
    }

    public Integer[] getEntryIds() {
        return entryIds;
    }

    public void setEntryIds(Integer[] entryIds) {
        this.entryIds = entryIds;
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
