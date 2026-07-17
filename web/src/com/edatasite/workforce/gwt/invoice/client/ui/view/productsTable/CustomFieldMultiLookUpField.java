package com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable;

import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MultiSelectCustomFieldLookUp;

public class CustomFieldMultiLookUpField extends MultiSelectCustomFieldLookUp implements CustomCellInterface {


    public CustomFieldMultiLookUpField(CompanyCustomFieldItem customFieldItem) {
        super(customFieldItem);
    }

    @Override
    public String getDisplayValue() {
        String value = "";
        if (getSelectedItems() != null && getSelectedItems().size() > 0) {
            for (SelectItem items : getSelectedItems()) {
                value += items.getName() + "; ";
            }
        }
        return value;
    }

    @Override
    public void setItemValue(Object value) {
        setSelected((Integer) value);
    }

    @Override
    public void setItemFocus(boolean focused) {
        getSuggestBox().setFocus(focused);
    }


    @Override
    public CompanyCustomFieldItem getFieldItem() {
        return super.getFieldItem();
    }

    @Override
    public void setFieldItem(CompanyCustomFieldItem fieldItem) {
        super.setFieldItem(fieldItem);
    }
}