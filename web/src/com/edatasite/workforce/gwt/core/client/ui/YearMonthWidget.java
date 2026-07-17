package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.panel.HorizontalPanelDiv;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;

import java.util.Date;

/**
 * User: Ilhom
 * Date: 6/13/13
 * Time: 4:35 PM
 */
public class YearMonthWidget extends Composite {

    private final DateTimeFormat format_month = DateTimeFormat.getFormat("MMMM");
    private final DateTimeFormat format_year = DateTimeFormat.getFormat("yyyy");

    private DataListBox box_year;
    private DataListBox box_month;
    private Command box_changeHandler;
    private HorizontalPanelDiv generalPanelDiv;

    public YearMonthWidget() {
        generalPanelDiv = new HorizontalPanelDiv();
        initWidget(generalPanelDiv);
        init();
    }

    public Date getStartYearMonthDate() {
        if (box_month.getSelectedItem() != null && box_year.getSelectedItem() != null) {
            Integer s_year = box_year.getSelectedItem().getId();
            Integer s_month = box_month.getSelectedItem().getId();
            return DateUtil.getMonthFirstDay(new Date(s_year, s_month, 1));
        }
        return null;
    }

    public Date getEndYearMonthDate() {
        if (box_month.getSelectedItem() != null && box_year.getSelectedItem() != null) {
            Integer s_year = box_year.getSelectedItem().getId();
            Integer s_month = box_month.getSelectedItem().getId();
            return DateUtil.getMonthLastDate(new Date(s_year, s_month, 1));
        }
        return null;
    }

    public void setBox_changeHandler(Command box_changeHandler) {
        this.box_changeHandler = box_changeHandler;
    }

    public void setSelected(int month, int year) {
        box_month.setSelected(month);
        box_year.setSelected(year);
    }

    public void setSpacing(int spacing) {
        box_month.getElement().getStyle().setMarginRight(spacing, Style.Unit.PX);
    }

    public void setWidth(String width_month, String width_year) {
        box_month.setWidth(width_month);
        box_year.setWidth(width_year);
    }

    private void init() {
        //initialize
        //box year
        box_year = new DataListBox();
        box_year.setWithoutNullLabel(true);
        box_year.setWidth("60px");
        //box month
        box_month = new DataListBox();
        box_month.setWithoutNullLabel(true);
        box_month.setWidth("95px");

        generalPanelDiv.add(3, box_year, box_month);
        //drawing
        setBoxYearItems();
        setBoxMonthItems();
    }

    private void setBoxMonthItems() {
        SelectItem[] monthItems = new SelectItem[12];
        Date currentDate = new Date();
        int currentMonth = currentDate.getMonth();
        Date date = DateUtil.getYearFirstDay(currentDate);

        int k = 0;
        for (int i = 0; i <= 12; i++) {
            monthItems[k] = new SelectItem(i, format_month.format(date));
            date = DateUtil.addMonths(date, 1);
            k++;
        }
        box_month.setItems(monthItems);
        box_month.setSelected(currentMonth);

        //month box change listener
        box_month.addValueChangeHandler(event -> {
            //year box change logic
            if (box_changeHandler != null) {
                box_changeHandler.execute();
            }
        });
    }

    private void setBoxYearItems() {
        Date date = new Date();
        int currentYear = date.getYear();
        date.setYear(date.getYear() - 2);
        int startYear = date.getYear();
        SelectItem[] yearItems = new SelectItem[currentYear - startYear + 1];
        int k = 0;
        for (int i = startYear; i <= currentYear; i++) {
            yearItems[k] = new SelectItem(date.getYear(), format_year.format(date));
            date.setYear(date.getYear() + 1);
            k++;
        }
        box_year.setItems(yearItems);
        box_year.setSelected(currentYear);

        //year box change listener
        box_year.addValueChangeHandler(event -> {
            //year box change logic
            if (box_changeHandler != null) {
                box_changeHandler.execute();
            }
        });
    }
}