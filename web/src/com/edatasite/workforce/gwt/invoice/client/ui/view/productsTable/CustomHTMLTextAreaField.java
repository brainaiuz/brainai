package com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable;

import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.ui.KpiEditor;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldInterface;

/**
 * User: Azamjon
 * Date: 10-Nov-2019
 */

public class CustomHTMLTextAreaField extends KpiEditor implements CustomCellInterface, CustomFieldInterface {
    private CompanyCustomFieldItem customFieldItem;

    public CustomHTMLTextAreaField(CompanyCustomFieldItem customFieldItem) {
        super();
        this.customFieldItem = customFieldItem;
    }

    @Override
    public String getDisplayValue() {
        return getData();
    }

    @Override
    public void setItemValue(Object value) {

    }

    @Override
    public void setItemFocus(boolean focused) {
    }

    @Override
    public CompanyCustomFieldItem getFieldItem() {
        if (customFieldItem != null) {
            customFieldItem.setFieldStringValue(getData());
        }
        return customFieldItem;
    }

    @Override
    public void setFieldItem(CompanyCustomFieldItem fieldItem) {
        if (customFieldItem != null && fieldItem != null) {
            customFieldItem.setObjectId(fieldItem.getObjectId());
            setData(fieldItem.getFieldStringValue() != null ? fieldItem.getFieldStringValue() : "");
        }
    }
}