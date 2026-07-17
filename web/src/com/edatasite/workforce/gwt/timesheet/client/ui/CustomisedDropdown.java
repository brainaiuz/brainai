package com.edatasite.workforce.gwt.timesheet.client.ui;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.google.gwt.user.client.Command;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Apr 29, 2009
 * Time: 7:33:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class CustomisedDropdown extends DataListBox {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private final Integer customised = 0;
    private final Integer week = 1;
    private final Integer month = 2;
    private final DatePicker from;
    private final DatePicker to;
    private final Command listener;

    public CustomisedDropdown(DatePicker from, DatePicker to, Command listener) {
        this.from = from;
        this.to = to;
        this.listener = listener;
        setItems();
        addListeners();
    }

    private void setItems() {
        SelectItem[] items = new SelectItem[3];
        items[0] = new SelectItem(customised, wfmStrings.customisedDateRange());
        items[1] = new SelectItem(week, wfmStrings.thisWeek());
        items[2] = new SelectItem(month, wfmStrings.thisMonth());
        setWithoutNullLabel(true);
        setItems(items);
    }

    private void addListeners() {
        addValueChangeHandler(sender -> {
            if (getSelectedItem() != null) {
                if (getSelectedItem().getId().equals(customised)) {
                    setPickersEnabled(true);
                } else if (getSelectedItem().getId().equals(week)) {
                    setPickersEnabled(false);
                    Date weekStart = DateUtil.getWeekFirstDay();
                    weekStart = DateUtil.addDays(weekStart, 1);
                    from.setDate(weekStart);
                    Date weekEnd = DateUtil.getWeekLastDay();
                    weekEnd = DateUtil.addDays(weekEnd, 1);
                    to.setDate(weekEnd);
                } else if (getSelectedItem().getId().equals(month)) {
                    setPickersEnabled(false);
                    from.setDate(DateUtil.getMonthFirstDay(new Date()));
                    to.setDate(DateUtil.getMonthLastDate(new Date()));
                }
                listener.execute();
            }

        });
    }

    private void setPickersEnabled(boolean enabled) {
        from.setEnabled(enabled);
        to.setEnabled(enabled);
    }
}
