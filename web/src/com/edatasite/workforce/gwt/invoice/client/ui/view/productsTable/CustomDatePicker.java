package com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldInterface;
import com.google.gwt.i18n.client.DateTimeFormat;

import java.util.Date;

public class CustomDatePicker extends DatePicker implements CustomCellInterface, CustomFieldInterface {
    private DateTimeFormat dateFormatter = DateUtils.getFormat();
    private Date date;
    private CompanyCustomFieldItem customFieldItem;

    public CustomDatePicker(CompanyCustomFieldItem customFieldItem) {
        super();
        this.customFieldItem = customFieldItem.cloneObject();
    }

    @Override
    public String getDisplayValue() {
        return getDate() != null ? DateUtils.format(getDate()) : "Please Select";
    }

    @Override
    public void setItemValue(Object value) {
        setDate((Date) value);
    }

    @Override
    public Date getDate() {
        if (getText() != null && !getText().isEmpty() && !"Please Select".equals(getText())) {
            try {
                if (dateFormatter == null) {
                    dateFormatter = DateUtils.getFormat();
                }
                if (!dateFormatter.getPattern().equals(getText())) {
                    date = dateFormatter.parse(getText());
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            return date;
        }
        return null;
    }

    @Override
    public CompanyCustomFieldItem getFieldItem() {
        if (getText() != null && !getText().isEmpty() && !"MMM dd, yyyy".equals(getText())) {
            customFieldItem.setFieldDateNonConvertedValue(new DateNonConvertable(getDate()));
        }
        return customFieldItem;
    }

    @Override
    public void setFieldItem(CompanyCustomFieldItem fieldItem) {
        if (customFieldItem != null && fieldItem != null) {
            customFieldItem.setObjectId(fieldItem.getObjectId());
            if (fieldItem.getFieldDateNonConvertedValue() != null) {
                setItemValue(fieldItem.getFieldDateNonConvertedValue().getNonConvertedDate());
            }
        }
    }


    @Override
    public void setItemFocus(boolean focused) {
        setFocus(focused);
    }
}
