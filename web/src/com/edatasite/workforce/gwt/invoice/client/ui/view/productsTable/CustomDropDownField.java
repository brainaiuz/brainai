package com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldInterface;

import java.util.ArrayList;

public class CustomDropDownField extends DataListBox implements CustomFieldInterface {
    CompanyCustomFieldItem customFieldItem;

    public CustomDropDownField(CompanyCustomFieldItem customFieldItem) {
        super();
        this.customFieldItem = customFieldItem.cloneObject();
        parseDropDownValues(customFieldItem.getPredefinedValues());
    }

    private void parseDropDownValues(String[] predefinedValues) {
        if (predefinedValues != null) {
            ArrayList<SelectItem> items = new ArrayList<>();
            for (String value : predefinedValues) {
                items.add(new SelectItem(value.hashCode(), value));
            }

            setItems(items.toArray(new SelectItem[]{}));
        }
    }

    @Override
    public CompanyCustomFieldItem getFieldItem() {
        if (getSelectedItem() != null) {
            customFieldItem.setFieldStringValue(getSelectedItem().getName());
        } else {
            customFieldItem.setFieldStringValue("");
        }
        return customFieldItem;
    }

    @Override
    public void setFieldItem(CompanyCustomFieldItem fieldItem) {
        if (fieldItem != null) {
            customFieldItem.setObjectId(fieldItem.getObjectId());
            if (fieldItem.getFieldStringValue() != null && !"".equals(fieldItem.getFieldStringValue())) {
                if (Constants.DATA_TYPE_NUMBER.equals(customFieldItem.getDataType())) {
                    Double dv = Double.valueOf(fieldItem.getFieldStringValue());
                    for (SelectItem item : getItems()) {
                        if (dv.equals(Double.valueOf(item.getName()))) {
                            setSelected(item.getId());
                            break;
                        }
                    }
                } else {
                    setSelected(fieldItem.getFieldStringValue().hashCode());
                }
            }
        }
    }
}
