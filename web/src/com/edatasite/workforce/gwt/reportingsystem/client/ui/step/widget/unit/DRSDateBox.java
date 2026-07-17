package com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;
import com.google.gwt.user.datepicker.client.DefaultMonthSelector;
import com.google.gwt.user.datepicker.client.MonthSelector;

import java.util.Date;

/**
 * User: ${Dilsh0d}
 * Date: 17-Mar-2010
 * Time: 18:20:35
 */
public class DRSDateBox extends HorizontalPanel {

    private DateBox.Format format;
    public DateBox dateBox;
    private boolean p = true;

    public DRSDateBox() {
        setStyleName("drs-datepicker");
        getElement().getStyle().setPadding(0, Style.Unit.PX);
        format = new DateBox.DefaultFormat(DateTimeFormat.getFormat(Utils.getShortDateFormat()));
        dateBox = new DateBox();
        dateBox.getDatePicker().setYearArrowsVisible(true);
        dateBox.setFormat(format);
        dateBox.addStyleName("form-control");

//        dateBox.getTextBox().addKeyPressHandler(new KeyPressHandler() {
//            public void onKeyPress(KeyPressEvent event) {
//                dateBox.getTextBox().cancelKey();
//            }
//        });

        add(dateBox);
        setCellVerticalAlignment(dateBox, VerticalPanel.ALIGN_MIDDLE);
    }

    public void setPlaceholder(String placeholder) {
        dateBox.getElement().setAttribute("placeholder", placeholder);
    }

    public String getText() {
        return dateBox.getTextBox().getText();
    }

    public void setText(String text) {
        dateBox.getTextBox().setText(text);
    }

    public Date getSelectedDate() {
        return dateBox.getValue();
    }

    public void setSelectedDate(Date date) {
        dateBox.setValue(date);
    }

    public void setSelectedDate(String date) {
        try {
            this.setSelectedDate(DateUtils.parse(date));
        } catch (Exception e) {

        }
    }

    public void setEnabled(boolean p) {
        this.p = p;
        dateBox.setEnabled(p);
    }
}
