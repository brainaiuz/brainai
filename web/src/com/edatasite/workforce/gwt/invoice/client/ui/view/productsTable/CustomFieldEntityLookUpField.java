package com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable;

import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EntityCustomFieldLookUp;

public class CustomFieldEntityLookUpField extends EntityCustomFieldLookUp implements CustomCellInterface, CustomFieldInterface {

    private CompanyCustomFieldItem customFieldItem;

    public CustomFieldEntityLookUpField(CompanyCustomFieldItem customFieldItem) {
        super(customFieldItem.getQuery());
        this.customFieldItem = customFieldItem.cloneObject();
    }

    @Override
    public String getDisplayValue() {
        return getText();
    }

    @Override
    public void setItemValue(Object value) {
        Integer id = null;
        try {
            id = (Integer) value;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (id != null && customFieldItem.getQueryItems() != null) {
            for (SelectItem selectItem : customFieldItem.getQueryItems()) {
                if (selectItem.getId().equals(id)) {
                    setSelected(new SelectItem(id, selectItem.getName()));
                    break;
                }
            }
        }
    }

    @Override
    public void setItemFocus(boolean focused) {
        getSuggestBox().setFocus(focused);
    }

    @Override
    public CompanyCustomFieldItem getFieldItem() {
        if (getSelectedItem() != null) {
            customFieldItem.setFieldStringValue(getSelectedItem().getName());
            customFieldItem.setSelectedId(getSelectedItem().getId());
        } else {
            customFieldItem.setSelectedId(null);
            customFieldItem.setFieldStringValue("");
        }
        return customFieldItem;
    }

    @Override
    public void setFieldItem(CompanyCustomFieldItem fieldItem) {
        customFieldItem.setObjectId(fieldItem.getObjectId());
        if (fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue())) {
            if (fieldItem.getSelectedId() != null) {
                addItem(new SelectItem(fieldItem.getSelectedId(), fieldItem.getFieldStringValue()));
            }
        }
    }
}