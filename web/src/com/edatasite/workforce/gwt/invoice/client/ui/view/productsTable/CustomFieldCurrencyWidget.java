package com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable;

import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.ui.CurrencyWidget;

public class CustomFieldCurrencyWidget extends CurrencyWidget implements CustomCellInterface {

    private CompanyCustomFieldItem customFieldItem;

    public CustomFieldCurrencyWidget(CompanyCustomFieldItem customFieldItem, String customForm) {
        super(customForm, true);
        this.customFieldItem = customFieldItem.cloneObject();
    }

    @Override
    public String getDisplayValue() {
        return getCurrencyListBox() != null && getCurrencyListBox().getSelectedItem() != null && getCurrencyName() != null ? getCurrencyName() : "";
    }

    @Override
    public void setItemValue(Object value) {
        setCurrency((Integer) value);
    }

    @Override
    public void setItemFocus(boolean focused) {

    }
}
