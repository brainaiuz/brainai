package com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets;

import com.edatasite.workforce.gwt.core.client.ui.DateTimePicker;
import com.google.gwt.gen2.table.client.InlineCellEditor;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

import java.util.Date;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 01-Dec-2010
 * Time: 17:14:12
 * <p/>
 * <E> Column Type In Listing Panel
 */
public abstract class DateTimePickerCellEditor<E> extends InlineCellEditor<E> {


    private Date prevValue;
    private boolean isDefault = false;
    private DateTimePicker dateTimePicker;

    public DateTimePickerCellEditor() {
        super(new DateTimePicker(true, false));
        this.dateTimePicker = (DateTimePicker) getContentWidget();
        Widget accept = this.getAcceptWidget();
        accept.getParent().addStyleName("btn btn--icon btn--default ficon--check");
        dateTimePicker.addStyleName("gwt-InlineCellEditor__datepicker");
        this.getElement().addClassName("dateTimeCell file--DateTimePickerCellEditor-1");
    }

    public DateTimePickerCellEditor(boolean showOnlyDatepicker) {
        super(new DateTimePicker(true, false, showOnlyDatepicker));
        this.dateTimePicker = (DateTimePicker) getContentWidget();
        //Task Number: T1000
        this.setWidth("210px");
        //this.getDateTimePicker().getStartDatePicker().setWidth("100%");
        Widget accept = this.getAcceptWidget();
        accept.getParent().addStyleName("btn btn--icon btn--default ficon--check");
        dateTimePicker.addStyleName("gwt-InlineCellEditor__datepicker");
        this.getElement().addClassName("dateTimeCell file--DateTimePickerCellEditor-2");
    }

    public DateTimePicker getDateTimePicker() {
        return dateTimePicker;
    }

    @Override
    public void editCell(CellEditInfo cellEditInfo, E cellValue, Callback<E> eCallback) {
        super.editCell(cellEditInfo, cellValue, eCallback);
        dateTimePicker.getStartDatePicker().setFocus(true);
    }

    public void setDate(Date date, boolean allDay) {
        dateTimePicker.setStartDate(date);
        if (allDay) {
            int hour = date.getHours();
            int minut = date.getMinutes();
            String pm_am = hour < 12 ? " AM" : " PM";
            String time = "";
            if (hour == 0) {
                hour = 12;
            } else if (hour > 12) {
                hour -= 12;
            }
            if (minut < 10) {
                time = time + hour + ":0" + minut + pm_am;
            } else {
                time = time + hour + ":" + minut + pm_am;
            }
            dateTimePicker.setAllDay(false);
            dateTimePicker.setVisableAllCheck(true);
            dateTimePicker.setStartTime(time);
        } else {
            dateTimePicker.setAllDay(true);
            dateTimePicker.setVisableAllCheck(false);
        }
    }

    public Date getDate() {
        return dateTimePicker.getStartDate();
    }

    public void setDefaultValue(boolean isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    protected boolean onAccept() {
        if ((prevValue != null && getDate() != null && !prevValue.equals(getDate()))
                || (prevValue == null && getDate() != null)
                || (prevValue != null && getDate() == null)) {
            return true;
        }
        super.cancel();
        return false;
    }

    @Override
    public void show() {
        if (isDefault) {
            prevValue = null;
        } else {
            prevValue = getDate();
        }
        super.show();
        int offset = Window.getClientWidth() - getWidget().getOffsetWidth() - 32;
        if ((getPopupLeft() + getWidget().getOffsetWidth()) > Window.getClientWidth()) {
            setPopupPosition(offset, getPopupTop());
        }
    }
}
