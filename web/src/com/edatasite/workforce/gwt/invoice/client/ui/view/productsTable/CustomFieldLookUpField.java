package com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable;

import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CustomFieldLookUp;

/**
 * User: Abror Abdukadirov
 * Date: 01.08.2019 20:47
 */
public class CustomFieldLookUpField extends CustomFieldLookUp implements CustomCellInterface {


    public CustomFieldLookUpField(CompanyCustomFieldItem customFieldItem) {
        super(customFieldItem);
    }

    @Override
    public String getDisplayValue() {
        return getText();
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
