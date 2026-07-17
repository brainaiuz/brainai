package com.edatasite.workforce.gwt.contact.client.ui;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Composite;

import java.util.Date;

/**
 * User: Ilhombek
 * Date: 1/26/12
 * Time: 12:44 PM
 */
public class DOBWidget extends Composite {

    private DataListBox box_day;
    private DataListBox box_month;
    private DataListBox box_year;
    private Command box_changeHandler;
    private final DateTimeFormat format_month = DateTimeFormat.getFormat("MMMM");
    private final DateTimeFormat format_year = DateTimeFormat.getFormat("yyyy");
    private boolean isHireDOBWidget = false;
    private boolean isDescendingDOBWidget = false;

    public DOBWidget() {
        this(false, false);
    }

    public DOBWidget(boolean isHireDOBWidget, boolean isDescendingDOBWidget) {
        this.isHireDOBWidget = isHireDOBWidget;
        this.isDescendingDOBWidget = isDescendingDOBWidget;
        init();
    }

    /**
     * generate date - date/month/year
     *
     * @return convertable date
     */
    public Date getDOBDate() {
        if (box_day.getSelectedIndex() != 0 && box_month.getSelectedIndex() != 0 && box_year.getSelectedIndex() != 0) {
            Integer s_year = box_year.getSelectedItem().getId();
            Integer s_month = box_month.getSelectedItem().getId();
            Integer s_date = box_day.getSelectedItem().getId();
            return new Date(s_year, s_month, s_date, 0, 0, 0);
        }
        return null;
    }

    /**
     * generate date - date/month/year
     *
     * @return date
     */
    public DateNonConvertable getConvertableDOBDate() {
        if (box_day.getSelectedIndex() != 0 && box_month.getSelectedIndex() != 0 && box_year.getSelectedIndex() != 0) {
            Integer s_year = box_year.getSelectedItem().getId();
            Integer s_month = box_month.getSelectedItem().getId();
            Integer s_date = box_day.getSelectedItem().getId();
            Date dob = new Date(s_year, s_month, s_date, 0, 0, 0);
            return new DateNonConvertable(dob);
        }
        return null;
    }

    public void setBox_changeHandler(Command box_changeHandler) {
        this.box_changeHandler = box_changeHandler;
    }

    public void setSelected(int id) {//default null label select
        box_day.setSelected(id);
        box_month.setSelectedIndex(id);
        box_year.setSelected(id);
    }

    public void setSelected(int day, int month, int year) {
        box_day.setSelected(day);
        box_month.setSelected(month);
        box_year.setSelected(year);
        setBoxDateItems();
    }

    public void setSpacing(int spacing) {
    }

    public void setWidth(String width_day, String width_month, String width_year) {
        box_day.setWidth(width_day);
        box_month.setWidth(width_month);
        box_year.setWidth(width_year);
    }

    public boolean box_validate(Boolean... required) {
        boolean isRequired = required != null && required.length > 0 ? required[0] : false;
        this.removeStyleName("x-form-invalid");
        box_day.removeStyleName("x-form-invalid");
        box_month.removeStyleName("x-form-invalid");
        box_year.removeStyleName("x-form-invalid");
        //validate box fields
        int dateFill = 0;
        if (box_day.getSelectedItem() == null || box_day.getSelectedIndex() == 0) {
            dateFill++;
        }
        if (box_month.getSelectedItem() == null || box_month.getSelectedIndex() == 0) {
            dateFill++;
        }
        if (box_year.getSelectedItem() == null || box_year.getSelectedIndex() == 0) {
            dateFill++;
        }

        if ((dateFill > 0 && dateFill < 3) || (isRequired && dateFill != 0)) {
            this.addStyleName(Constants.ERROR_FORM_STYLE);
            if (box_day.getSelectedItem() == null || (box_day.getSelectedItem() != null && "".equals(box_day.getSelectedItem().getName()))) {
                box_day.setStyleName("x-form-invalid");
            }
            if (box_month.getSelectedItem() == null || (box_month.getSelectedItem() != null && "".equals(box_month.getSelectedItem().getName()))) {
                box_month.setStyleName("x-form-invalid");
            }
            if (box_year.getSelectedItem() == null || (box_year.getSelectedItem() != null && "".equals(box_year.getSelectedItem().getName()))) {
                box_year.setStyleName("x-form-invalid");
            }
        }

        return isRequired ? dateFill != 0 : dateFill > 0 && dateFill < 3;
    }

    private void init() {
        //initialize
        box_day = new DataListBox();
//        box_day.setWidth("60px");
        box_month = new DataListBox();
        box_year = new DataListBox();
//        box_year.setWidth("75px");
        box_month.ensureDebugId("dateOfBirth-forMonth");
        box_day.ensureDebugId("dateOfBirth-forDay");
        box_year.ensureDebugId("dateOfBirth-fromYear");
        box_day.setWithoutNullLabel(true);
        box_month.setWithoutNullLabel(true);
        box_year.setWithoutNullLabel(true);

        setBoxYearItems();
        setBoxMonthItems();
        setBoxDateItems();

        InputGroup inputGroup = new InputGroup(box_day, box_month, box_year);
        initWidget(inputGroup);
    }

    private void setBoxDateItems() {
        SelectItem dateItems[] = new SelectItem[31 + 1];
        dateItems[0] = new SelectItem(0, "");

        SelectItem selectedBox_day = box_day.getSelectedItem();
        boolean selectedBox_dayExistInDropDown = false;
        int k = 0;
        if (box_year.getSelectedItem() != null && box_year.getSelectedItem().getId() != 0 &&
                box_month.getSelectedItem() != null && box_month.getSelectedItem().getId() != -1) {
            int year = box_year.getSelectedItem().getId();
            int month = box_month.getSelectedItem().getId();
            Date date = new Date();
            date.setYear(year);
            date.setMonth(month);
            int lastDate = DateUtil.getMonthLastDate(date).getDate();
            dateItems = new SelectItem[lastDate + 1];
            dateItems[0] = new SelectItem(0, "");
            k = 1;
            for (int i = 1; i <= lastDate; i++) {
                dateItems[k] = new SelectItem(i, String.valueOf(i));
                k++;
                if (selectedBox_day != null && i == selectedBox_day.getId()) {
                    selectedBox_dayExistInDropDown = true;
                }
            }
            box_day.setItems(dateItems);
            box_day.setSelected(selectedBox_dayExistInDropDown ? selectedBox_day.getId() : 1);
        } else {
            k = 1;
            for (int i = 1; i <= 31; i++) {
                dateItems[k] = new SelectItem(i, String.valueOf(i));
                k++;
                if (selectedBox_day != null && i == selectedBox_day.getId()) {
                    selectedBox_dayExistInDropDown = true;
                }
            }
            box_day.setItems(dateItems);
            box_day.setSelected(selectedBox_dayExistInDropDown ? selectedBox_day.getId() : 0);
        }
        box_day.addValueChangeHandler(event -> {
            box_day.removeStyleName("x-form-invalid");
            if (box_day.getSelectedItem() != null && box_month.getSelectedItem() != null && box_year.getSelectedItem() != null) {
                if ("".equals(box_day.getSelectedItem().getName()) && "".equals(box_month.getSelectedItem().getName()) && "".equals(box_year.getSelectedItem().getName())) {
                    box_month.removeStyleName("x-form-invalid");
                    box_year.removeStyleName("x-form-invalid");
                    if (box_changeHandler != null) {
                        box_changeHandler.execute();
                    }
                }
                if (!"".equals(box_day.getSelectedItem().getName()) && !"".equals(box_month.getSelectedItem().getName()) && !"".equals(box_year.getSelectedItem().getName())) {
                    box_month.removeStyleName("x-form-invalid");
                    box_year.removeStyleName("x-form-invalid");
                    if (box_changeHandler != null) {
                        box_changeHandler.execute();
                    }
                }
            }
        });
    }

    private void setBoxMonthItems() {
        SelectItem monthItems[] = new SelectItem[12 + 1];
        monthItems[0] = new SelectItem(12, "");
        Date date = DateUtil.getYearFirstDay(new Date());
        int k = 1;
        for (int i = 0; i < 12; i++) {
            monthItems[k] = new SelectItem(i, format_month.format(date));
            date = DateUtil.addMonths(date, 1);
            k++;
        }
        box_month.setItems(monthItems);
        box_month.setSelected(12);

        box_month.addValueChangeHandler(event -> {
            setBoxDateItems();
            box_month.removeStyleName("x-form-invalid");
            if (box_day.getSelectedItem() != null && box_month.getSelectedItem() != null && box_year.getSelectedItem() != null) {
                if ("".equals(box_day.getSelectedItem().getName()) && "".equals(box_month.getSelectedItem().getName()) && "".equals(box_year.getSelectedItem().getName())) {
                    box_day.removeStyleName("x-form-invalid");
                    box_year.removeStyleName("x-form-invalid");
                    if (box_changeHandler != null) {
                        box_changeHandler.execute();
                    }
                }
                if (!"".equals(box_day.getSelectedItem().getName()) && !"".equals(box_month.getSelectedItem().getName()) && !"".equals(box_year.getSelectedItem().getName())) {
                    box_day.removeStyleName("x-form-invalid");
                    box_year.removeStyleName("x-form-invalid");
                    if (box_changeHandler != null) {
                        box_changeHandler.execute();
                    }
                }
            }
        });
    }

    private void setBoxYearItems() {
        Date date = new Date();
        int currentYear = date.getYear();
        date.setYear(date.getYear() - 100);
        int startYear = date.getYear();
        if (isHireDOBWidget) {
            currentYear += 1;
        }
        SelectItem yearItems[] = new SelectItem[currentYear - startYear + 2];
        yearItems[0] = new SelectItem(0, "");
        int k = 1;

        int addOrSubtract = 1;
        if (isDescendingDOBWidget) {
            date.setYear(currentYear);
            addOrSubtract = -1;
        }
        for (int i = startYear; i <= currentYear; i++) {
            yearItems[k] = new SelectItem(date.getYear(), format_year.format(date));
            date.setYear(date.getYear() + addOrSubtract);
            k++;
        }
        box_year.setItems(yearItems);
        box_year.setSelected(0);

        box_year.addValueChangeHandler(event -> {
            setBoxDateItems();
            box_year.removeStyleName("x-form-invalid");
            if (box_day.getSelectedItem() != null && box_month.getSelectedItem() != null && box_year.getSelectedItem() != null) {
                if ("".equals(box_day.getSelectedItem().getName()) && "".equals(box_month.getSelectedItem().getName()) && "".equals(box_year.getSelectedItem().getName())) {
                    box_month.removeStyleName("x-form-invalid");
                    box_day.removeStyleName("x-form-invalid");
                    if (box_changeHandler != null) {
                        box_changeHandler.execute();
                    }
                }
                if (!"".equals(box_day.getSelectedItem().getName()) && !"".equals(box_month.getSelectedItem().getName()) && !"".equals(box_year.getSelectedItem().getName())) {
                    box_month.removeStyleName("x-form-invalid");
                    box_day.removeStyleName("x-form-invalid");
                    if (box_changeHandler != null) {
                        box_changeHandler.execute();
                    }
                }
            }
        });
    }

    public void addBlurHandler(BlurHandler blurHandler) {
        if (blurHandler != null) {
            box_day.addBlurHandler(blurHandler);
            box_month.addBlurHandler(blurHandler);
            box_year.addBlurHandler(blurHandler);
        }
    }
}