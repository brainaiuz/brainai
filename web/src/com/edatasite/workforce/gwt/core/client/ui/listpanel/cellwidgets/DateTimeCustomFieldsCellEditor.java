package com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets;

import com.edatasite.workforce.gwt.core.client.ui.DateTimeWidget;
import com.google.gwt.gen2.table.client.InlineCellEditor;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Widget;

import java.util.Arrays;
import java.util.Date;

public abstract class DateTimeCustomFieldsCellEditor<E> extends InlineCellEditor<E> {
    public DateTimeWidget dateTimeCustomFields;
    private static final DateTimeFormat format = DateTimeFormat.getFormat("HH:mm");
    private boolean isDefault = false;
    private Date prevValue;


    protected DateTimeCustomFieldsCellEditor() {
        super(new DateTimeWidget());
        this.dateTimeCustomFields = (DateTimeWidget) getContentWidget();
        if (this.getElement() != null && this.getElement().getParentElement() != null) {}
        Widget accept = this.getAcceptWidget();
        dateTimeCustomFields.getTime().setWidth("28%");
        accept.getParent().addStyleName("btn btn--icon btn--default ficon--check");
        dateTimeCustomFields.addStyleName("gwt-InlineCellEditor__datepicker");
        this.getElement().addClassName("dateTimeCell file--DateTimeCustomFiledsCellEditor");
    }

    @Override
    public void editCell(CellEditInfo cellEditInfo, E cellValue, Callback<E> eCallback) {
        super.editCell(cellEditInfo, cellValue, eCallback);
        dateTimeCustomFields.getDateField().setFocus(true);
    }

    public Date getDate() {
        return dateTimeCustomFields.getDateTime();
    }

    public void setDateTime(Date date) {
        dateTimeCustomFields.getDateField().setDate(date);

        String str = format.format(date);

        String[] arr = str.split(":");
        dateTimeCustomFields.getTime().setValue(Arrays.stream(arr).mapToInt(Integer::parseInt).toArray());
    }

    public void setDefaultValue(boolean isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    protected boolean onAccept() {
        return true;
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
