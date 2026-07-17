package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.google.gwt.i18n.client.DateTimeFormat;

import java.util.Date;

/**
 * Created by Dilshod Madrahimov on 7/23/15 3:50 PM
 */
public class CustomDatePickerCell extends DatePicker implements CustomCellInterface {
    private static DateTimeFormat dateFormatter = DateUtils.getFormat();
    private Date date;

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
                date = dateFormatter.parse(getText());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            return date;
        }
        return null;
    }

    @Override
    public void setItemFocus(boolean focused) {
        setFocus(focused);
    }
}