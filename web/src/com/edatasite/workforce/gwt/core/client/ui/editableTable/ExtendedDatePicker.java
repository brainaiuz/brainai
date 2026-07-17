package com.edatasite.workforce.gwt.core.client.ui.editableTable;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.interfaces.CustomCellInterface;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.google.gwt.i18n.client.DateTimeFormat;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 16.05.14
 * Time: 10:27
 * To change this template use File | Settings | File Templates.
 */
public class ExtendedDatePicker extends DatePicker implements CustomCellInterface {

    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    private DateTimeFormat dateFormatter;
    private Date date;

    public ExtendedDatePicker() {
        super();
    }

    @Override
    public String getDisplayValue() {
        return getDate() != null ? DateUtils.format(getDate()) : wfmStrings.pleaseSelect();
    }

    @Override
    public void setItemValue(Object value) {
        setDate((Date) value);
    }

    @Override
    public Date getDate() {
        if (getText() != null && !getText().isEmpty() && !wfmStrings.pleaseSelect().equals(getText())) {
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
