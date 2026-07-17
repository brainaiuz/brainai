package com.edatasite.workforce.gwt.payroll.client.ui.view.payslip.widgets;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.google.gwt.i18n.client.DateTimeFormat;

import java.util.Date;
import java.util.Objects;

/**
 * User: satimov
 * Date: 2/5/18 8:31 PM
 */
public class CustomDatePickerCell extends DatePicker implements CustomCellInterface {
    private Date date;
    private DateTimeFormat dateFormatter;

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
        if (this.getText() == null ||
            this.getText().isEmpty() ||
            Objects.equals("Please Select", this.getText())) {
            return null;
        }
        try {
            if (dateFormatter == null) {
                dateFormatter = DateUtils.getFormat();
            }
            if (!dateFormatter.getPattern().equals(this.getText())) {
                date = dateFormatter.parse(this.getText());
            }
        } catch (IllegalArgumentException ignored) {
        }
        return date;
    }

    @Override
    public void setItemFocus(boolean focused) {
        setFocus(focused);
    }
}
