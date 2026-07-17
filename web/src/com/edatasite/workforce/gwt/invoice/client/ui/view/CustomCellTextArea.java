package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.google.gwt.user.client.ui.TextArea;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Aug 11, 2009
 * Time: 3:26:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class CustomCellTextArea extends TextArea implements CustomCellInterface{
    private Integer[] entryIds;

    public Integer[] getEntryIds() {
        return entryIds;
    }

    public void setEntryIds(Integer[] entryIds) {
        this.entryIds = entryIds;
    }

    @Override
    public String getDisplayValue() {
        return super.getText();
    }

    @Override
    public void setItemValue(Object value) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setItemFocus(boolean focused) {
        super.setFocus(focused);
    }
}
