package com.edatasite.workforce.gwt.invoice.client.ui.view.productsTable;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.DateTimeWidget;
import com.edatasite.workforce.gwt.core.client.ui.customfields.CustomFieldInterface;
import com.google.gwt.i18n.client.DateTimeFormat;

import java.util.Date;

public class CustomDateTime extends DateTimeWidget implements CustomCellInterface, CustomFieldInterface {
    private Date date;
    private CompanyCustomFieldItem customFieldItem;

    public CustomDateTime(CompanyCustomFieldItem customFieldItem) {
        super();
        getDateField().removeStyleName("width250 file--CustomDateTime");
        getTime().setMarginTop(-5);
        getTime().setPaddingLeft(10);

        this.customFieldItem = customFieldItem.cloneObject();
    }

    @Override
    public String getDisplayValue() {

        return getDateTime() != null ? DateTimeFormat.getFormat(Utils.getLongDateFormat()).format(getDateTime()) : "Please Select";

    }

    @Override
    public void setItemValue(Object value) {
        setDateTime((Date) value);
    }


    @Override
    public void setItemFocus(boolean focused) {
        getDateField().setItemFocus(focused);
    }

    @Override
    public CompanyCustomFieldItem getFieldItem() {

        if (getDateField().getText() != null && !getDateField().getText().isEmpty() && !"MMM dd, yyyy".equals(getDateField().getText())) {
            customFieldItem.setFieldDateNonConvertedValue(new DateNonConvertable(getDateTime()));
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
}
