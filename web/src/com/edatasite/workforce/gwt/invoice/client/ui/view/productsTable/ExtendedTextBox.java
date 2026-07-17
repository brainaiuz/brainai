package com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable;

import com.edatasite.workforce.gwt.accounting.client.AccountingUtils;
import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.google.gwt.user.client.ui.TextBox;

public class ExtendedTextBox extends TextBox implements CustomCellInterface {

    private Integer objectId;

    public ExtendedTextBox() {
        super();
    }

    @Override
    public String getDisplayValue() {
        return AccountingUtils.get().getPriceValue(getText());
    }

    @Override
    public void setItemValue(Object value) {

    }

    @Override
    public void setItemFocus(boolean focused) {
        setFocus(focused);
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }
}
