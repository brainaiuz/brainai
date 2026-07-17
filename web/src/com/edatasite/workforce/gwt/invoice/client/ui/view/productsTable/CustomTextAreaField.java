package com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable;

import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldInterface;

public class CustomTextAreaField extends TextArea2 implements CustomCellInterface, CustomFieldInterface {
    private CompanyCustomFieldItem customFieldItem;

    public CustomTextAreaField(CompanyCustomFieldItem customFieldItem) {
        super(AREA_LENGTH_3);
        this.customFieldItem = customFieldItem;
        setEnabled(!customFieldItem.isDisabled());
    }

    @Override
    public String getDisplayValue() {
        return getText();
    }

    @Override
    public void setItemValue(Object value) {

    }

    @Override
    public void setItemFocus(boolean focused) {
        getTextArea().setFocus(focused);
    }

    @Override
    public CompanyCustomFieldItem getFieldItem() {
        if (customFieldItem != null) {
            customFieldItem.setFieldStringValue(getText());
        }
        return customFieldItem;
    }

    @Override
    public void setFieldItem(CompanyCustomFieldItem fieldItem) {
        if (customFieldItem != null && fieldItem != null) {
            customFieldItem.setObjectId(fieldItem.getObjectId());
            setText(fieldItem.getFieldStringValue() != null ? fieldItem.getFieldStringValue() : "");
        }
    }
}
