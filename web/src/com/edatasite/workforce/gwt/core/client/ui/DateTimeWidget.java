package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiTimePicker;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;

import java.util.Arrays;
import java.util.Date;

public class DateTimeWidget extends Composite {
    private KpiDatePicker dateField;
    private KpiTimePicker time;
    private Date date;
    private Integer widthTime;
    private static final DateTimeFormat format = DateTimeFormat.getFormat("HH:mm");

    public DateTimeWidget() {
        init();
    }

    public DateTimeWidget(Integer widthTime) {
        this.widthTime = widthTime;
        init();
    }

    private void init() {
        String pattern = DateUtils.getFormatInternal().getPattern();
        String dateFormat = null;
        if (pattern.contains("hh") || pattern.contains("HH")) {
            String format = null;
            if (pattern.contains("hh")) {
                format = pattern.split("hh")[0];
            } else {
                format = pattern.split("HH")[0];
            }

            if (format != null && format.length() > 0) {
                dateFormat = format.substring(0, format.length() - 1);
            }
        }

        dateField = new KpiDatePicker(dateFormat != null ? DateTimeFormat.getFormat(dateFormat) : null);
        dateField.addStyleName("width250");

        time = new KpiTimePicker(true);
        time.setMarginTop(0);
        time.setPaddingLeft(8);
        if (widthTime != null) {
            time.setWidth(widthTime + "%");
        }
        time.setStyleName("timepicker form-control");

        initWidget(new InputGroup(dateField, time));
    }

    public Date getDateTime() {
        if (dateField.getDate() != null) {
            date = dateField.getDate();
            date.setHours(time.getValue()[0]);
            date.setMinutes(time.getValue()[1]);

            return date;
        }
        return null;
    }

    public void setDateTime(Date date) {
        dateField.setDate(date);

        String str = format.format(date);

        String[] arr = str.split(":");
        this.time.setValue(Arrays.stream(arr).mapToInt(Integer::parseInt).toArray());
    }

    public KpiDatePicker getDateField() {
        return dateField;
    }

    public KpiTimePicker getTime() {
        return time;
    }

    public void setEnabled(boolean enable) {
        dateField.setEnabled(enable);
        time.setEnabled(enable);
    }
}
