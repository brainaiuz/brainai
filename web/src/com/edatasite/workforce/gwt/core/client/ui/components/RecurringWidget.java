package com.edatasite.workforce.gwt.core.client.ui.components;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.client.ui.TimeBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxRow;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.addins.client.combobox.MaterialComboBox;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecurringWidget extends Div implements SchedulerConstant {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private GBoxRow row;
    private GBoxRow row2;
    private RecurringCronWidget typeBox;
    private RecurringEndDateWidget endDateBox;
    private RecurringStartDateWidget startDateBox;

    private RecurrenceJobItem jobItem;
    private boolean twoLines;

    public RecurringWidget(Integer type) {
        this(type, "");
    }

    public RecurringWidget(Integer type, String additionalClasses) {
        super(GBox.STYLE_GBOX, "recurringWidget file--RecurringWidget", additionalClasses); //NOTE https://prnt.sc/s2ntji, https://prnt.sc/ru4tep
        jobItem = new RecurrenceJobItem();
        jobItem.setJobType(getJobType(type));
        onInitialize();
    }

    @UiConstructor
    public RecurringWidget(Integer type, boolean twoLines) {
        super(GBox.STYLE_GBOX, "recurringWidget");
        jobItem = new RecurrenceJobItem();
        jobItem.setJobType(getJobType(type));
        this.twoLines = twoLines;
        onInitialize();
    }

    private void onInitialize() {
        row = new GBoxRow();
        if (twoLines) {
            row2 = new GBoxRow();
        }

        typeBox = new RecurringCronWidget();
        endDateBox = new RecurringEndDateWidget();
        startDateBox = new RecurringStartDateWidget();

        row.add(typeBox);
        if (twoLines) {
            row2.add(endDateBox);
            row2.add(startDateBox);
        } else {
            row.add(endDateBox);
            row.add(startDateBox);
        }

        add(row);
        if (twoLines) {
            add(row2);
        }
    }

    public void setData(RecurrenceJobItem data) {
        jobItem = data;
        typeBox.setValue(data);
        endDateBox.setValue(data);
        startDateBox.setValue(data);
    }

    public RecurrenceJobItem getData() {
        RecurrenceJobItem item = new RecurrenceJobItem();
        item.setObjectId(jobItem.getObjectId());
        item.setJobType(jobItem.getJobType());
        item.setBusObjectId(jobItem.getBusObjectId());
        item.setEnabled(true);

        endDateBox.getValue(item);
        startDateBox.getValue(item);
        typeBox.getValue(item);

        return item;
    }

    public boolean validate() {
        int errors = 0;
        if (!typeBox.validate()) {
            errors++;
        }
        if (!endDateBox.validate()) {
            errors++;
        }
        if (!startDateBox.validate()) {
            errors++;
        }
        return errors == 0;
    }

    private int getJobType(Integer type) {
        if (OVERDUE_INVOICE_FORM == type) {
            return OVERDUE_INVOICE_REMINDER;
        } else if (INVOICE_ADD_FORM == type) {
            return RECURRING_INVOICE_REMINDER;
        } else if (RECURRING_BILL_FORM == type) {
            return RECURRING_BILL_REMINDER;
        } else if (RECURRING_MANUAL_JOURNAL_FORM == type) {
            return RECURRING_MANUAL_JOURNAL_REMINDER;
        } else if (RECURRING_TASK_FORM == type) {
            return RECURRING_TASK;
        } else if (RECURRING_EVENT_FORM == type) {
            return RECURRING_EVENT;
        } else if (RECURRING_REPORT_FORM == type) {
            return RECURRING_REPORT;
        } else if (RECURRING_WORKFLOW_FORM == type) {
            return RECURRING_WORKFLOW;
        } else if (RECURRING_MAGENTO_FORM == type) {
            return SYNCHRONIZE_MAGENTO_CATALOG;
        } else if (RECURRING_TIMESHEET_FORM == type) {
            return TIMESHEET_REMINDER;
        } else {
            return OVERDUE_INVOICE_REMINDER;
        }
    }

    private class RecurringCronWidget extends GBoxItem {
        private HorizontalPanel div;

        private DataListBox type;
        //daily
        private TextBox dayBox;
        private Span newSpan;
        //weekly
        private MaterialComboBox<SelectItem> weekBox;
        //monthly
        private TextBox mDayBox;
        private DataListBox mRepeatBox;
        //yearly
        private DataListBox yMonthListBox;
        private DataListBox yTypeBox;
        private Div yValueBox;
        private DataListBox yWeekBox;
        private TextBox yTextBox;

        public RecurringCronWidget() {
            super();
            RecurringCronWidget.this.addStyleName("recurringCronWidget");
            initialize();
        }

        private void initialize() {
            div = new HorizontalPanel();
            div.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

            initTypes();
            initDailyCron();
            initWeeklyCron();
            initMonthlyCron();
            initYearlyCron();

            add(type);

            addComponent(div);

            onChangeEvent();
        }

        private void initTypes() {
            type = new DataListBox();
            type.setWithoutNullLabel(true);

            type.addListItem(new SelectItem(RECURRENCE_TYPE_DAILY, wfmStrings.daily()));
            type.addListItem(new SelectItem(RECURRENCE_TYPE_WEEKLY, wfmStrings.weekly()));
            type.addListItem(new SelectItem(RECURRENCE_TYPE_MONTHLY, wfmStrings.monthly()));
            type.addListItem(new SelectItem(RECURRENCE_TYPE_YEARLY, wfmStrings.yearly()));
            type.addListItem(new SelectItem(RECURRENCE_TYPE_EVEN_DAILY, wfmStrings.evenDays()));
            type.addListItem(new SelectItem(RECURRENCE_TYPE_ODD_DAILY, wfmStrings.oddDays()));
            type.addListItem(new SelectItem(RECURRENCE_TYPE_HOURLY, wfmStrings.hourly()));

            type.addValueChangeHandler(c -> onChangeEvent());
            type.setSelected(RECURRENCE_TYPE_DAILY);
        }

        private void initDailyCron() {
            dayBox = new TextBox();
            dayBox.setMaxLength(3);
            dayBox.addStyleName("emptyTextbox");
            dayBox.addChangeHandler(changeEvent -> dayBox.removeStyleName("x-form-invalid"));
            Validation.addNumericKeyboardListener(dayBox);
            newSpan = new Span();
            newSpan.addStyleName("emptyTextbox__sign");
        }

        private void initWeeklyCron() {
            weekBox = new MaterialComboBox<>();

            weekBox.setMultiple(true);
            weekBox.setCloseOnSelect(false);
//            weekBox.setPlaceholder(wfmStrings.pleaseSelect());
            weekBox.setStylePrimaryName("wfm-dropdown");

            weekBox.addItem(wfmStrings.sundayShort(), new SelectItem(SUNDAY, wfmStrings.sundayShort()));
            weekBox.addItem(wfmStrings.mondayShort(), new SelectItem(MONDAY, wfmStrings.mondayShort()));
            weekBox.addItem(wfmStrings.tuesdayShort(), new SelectItem(TUESDAY, wfmStrings.tuesdayShort()));
            weekBox.addItem(wfmStrings.wednesdayShort(), new SelectItem(WEDNESDAY, wfmStrings.wednesdayShort()));
            weekBox.addItem(wfmStrings.thursdayShort(), new SelectItem(THURSDAY, wfmStrings.thursdayShort()));
            weekBox.addItem(wfmStrings.fridayShort(), new SelectItem(FRIDAY, wfmStrings.fridayShort()));
            weekBox.addItem(wfmStrings.saturdayShort(), new SelectItem(SATURDAY, wfmStrings.saturdayShort()));
        }

        private void initMonthlyCron() {
            mDayBox = new TextBox();
            mDayBox.setMaxLength(2);
            Validation.addNumericKeyboardListener(mDayBox);

            mRepeatBox = new DataListBox();
            mRepeatBox.setWithoutNullLabel(true);
            for (Integer i = 1; i <= 12; i++) {
                mRepeatBox.addListItem(new SelectItem(i, i.toString()));
            }
            mRepeatBox.setSelected(1);
        }

        private void initYearlyCron() {
            yMonthListBox = new DataListBox();
            yMonthListBox.addListItem(new SelectItem(1, wfmStrings.january()));
            yMonthListBox.addListItem(new SelectItem(2, wfmStrings.february()));
            yMonthListBox.addListItem(new SelectItem(3, wfmStrings.march()));
            yMonthListBox.addListItem(new SelectItem(4, wfmStrings.april()));
            yMonthListBox.addListItem(new SelectItem(5, wfmStrings.may()));
            yMonthListBox.addListItem(new SelectItem(6, wfmStrings.june()));
            yMonthListBox.addListItem(new SelectItem(7, wfmStrings.july()));
            yMonthListBox.addListItem(new SelectItem(8, wfmStrings.august()));
            yMonthListBox.addListItem(new SelectItem(9, wfmStrings.september()));
            yMonthListBox.addListItem(new SelectItem(10, wfmStrings.october()));
            yMonthListBox.addListItem(new SelectItem(11, wfmStrings.november()));
            yMonthListBox.addListItem(new SelectItem(12, wfmStrings.december()));

            yTypeBox = new DataListBox();
            yTypeBox.setWithoutNullLabel(true);

            yTypeBox.addListItem(new SelectItem(0, wfmStrings.day()));
            yTypeBox.addListItem(new SelectItem(FIRST, wfmStrings.first()));
            yTypeBox.addListItem(new SelectItem(SECOND, wfmStrings.second()));
            yTypeBox.addListItem(new SelectItem(THIRD, wfmStrings.third()));
            yTypeBox.addListItem(new SelectItem(FOURTH, wfmStrings.fourth()));
            yTypeBox.addListItem(new SelectItem(LAST, wfmStrings.last()));
            yTypeBox.setSelected(0);

            yTypeBox.addValueChangeHandler(c -> onTypeChangeEvent());

            yTextBox = new TextBox();
            yTextBox.setMaxLength(2);
            Validation.addNumericKeyboardListener(yTextBox);

            yWeekBox = new DataListBox();
            yWeekBox.addListItem(new SelectItem(SUNDAY, wfmStrings.sundayShort()));
            yWeekBox.addListItem(new SelectItem(MONDAY, wfmStrings.mondayShort()));
            yWeekBox.addListItem(new SelectItem(TUESDAY, wfmStrings.tuesdayShort()));
            yWeekBox.addListItem(new SelectItem(WEDNESDAY, wfmStrings.wednesdayShort()));
            yWeekBox.addListItem(new SelectItem(THURSDAY, wfmStrings.thursdayShort()));
            yWeekBox.addListItem(new SelectItem(FRIDAY, wfmStrings.fridayShort()));
            yWeekBox.addListItem(new SelectItem(SATURDAY, wfmStrings.saturdayShort()));

            yValueBox = new Div();
            yValueBox.add(yTextBox);
        }

        public void setValue(RecurrenceJobItem item) {
            if (item == null) return;

            type.setSelected(item.getType());
            onChangeEvent();

            if (item.getType() != null) {
                switch (item.getType()) {
                    case RECURRENCE_TYPE_DAILY:
                    case RECURRENCE_TYPE_HOURLY:
                        if (item.getInterval() != null) {
                            dayBox.setText(item.getInterval().toString());
                        }
                        break;
                    case RECURRENCE_TYPE_WEEKLY:
                        List<SelectItem> items = new ArrayList<>();
                        if (item.isSunday()) {
                            items.add(new SelectItem(SUNDAY, wfmStrings.sundayShort()));
                        }
                        if (item.isMonday()) {
                            items.add(new SelectItem(MONDAY, wfmStrings.mondayShort()));
                        }
                        if (item.isTuesday()) {
                            items.add(new SelectItem(TUESDAY, wfmStrings.tuesdayShort()));
                        }
                        if (item.isWednesday()) {
                            items.add(new SelectItem(WEDNESDAY, wfmStrings.wednesdayShort()));
                        }
                        if (item.isThursday()) {
                            items.add(new SelectItem(THURSDAY, wfmStrings.thursdayShort()));
                        }
                        if (item.isFriday()) {
                            items.add(new SelectItem(FRIDAY, wfmStrings.fridayShort()));
                        }
                        if (item.isSaturday()) {
                            items.add(new SelectItem(SATURDAY, wfmStrings.saturdayShort()));
                        }
                        weekBox.setValues(items);
                        break;
                    case RECURRENCE_TYPE_MONTHLY:
                        if (item.getMonthlyOrYearlyDay() != null) {
                            mDayBox.setText(item.getMonthlyOrYearlyDay().toString());
                        }
                        if (item.getInterval() != null) {
                            mRepeatBox.setSelected(item.getInterval());
                        }
                        break;
                    case RECURRENCE_TYPE_YEARLY:
                        if (item.getYearlyMonth() != null) {
                            yMonthListBox.setSelected(item.getYearlyMonth());
                        }
                        if (item.getCustomPatternDay() != null) {
                            yTypeBox.setSelected(item.getCustomPatternDay());
                            yWeekBox.setSelected(item.getMonthlyOrYearlyDay());
                        } else {
                            yWeekBox.setSelected(0);
                            yTextBox.setText(item.getMonthlyOrYearlyDay().toString());
                        }
                        break;
                }
            }
        }

        public void getValue(RecurrenceJobItem item) {

            item.setType(type.getSelectedId());
            switch (type.getSelectedId()) {
                case RECURRENCE_TYPE_DAILY:
                case RECURRENCE_TYPE_HOURLY:
                    try {
                        item.setInterval(Integer.parseInt(dayBox.getText()));
                        item.setDailyPatternOptions(DAILY_PATTERN_OPTION_INTERVAL);
                    } catch (Exception e) {
                        GWT.log("Reccuring Days error.");
                    }
                    break;
                case RECURRENCE_TYPE_WEEKLY:
                    item.setInterval(1);
                    for (SelectItem selected : weekBox.getSelectedValues()) {
                        switch (selected.getId()) {
                            case SUNDAY:
                                item.setSunday(true);
                                break;
                            case MONDAY:
                                item.setMonday(true);
                                break;
                            case TUESDAY:
                                item.setTuesday(true);
                                break;
                            case WEDNESDAY:
                                item.setWednesday(true);
                                break;
                            case THURSDAY:
                                item.setThursday(true);
                                break;
                            case FRIDAY:
                                item.setFriday(true);
                                break;
                            case SATURDAY:
                                item.setSaturday(true);
                                break;
                        }
                    }
                    break;
                case RECURRENCE_TYPE_MONTHLY:
                    try {
                        item.setMonthlyOrYearlyDay(Integer.parseInt(mDayBox.getText()));
                    } catch (Exception e) {
                        GWT.log("Reccuring Months error.");
                    }
                    item.setInterval(mRepeatBox.getSelectedId());
                    item.setMonthlyOrYearlyPatternOption(MONTHLY_OR_YEARLY_PATTERN_CUSTOM);
                    Date itemStartDate = item.getStartDate();

                    if (DateUtil.getMonthLastDate(item.getStartDate()).getDate() < item.getMonthlyOrYearlyDay()) {
                        int monthLastDay = DateUtil.getMonthLastDate(item.getStartDate()).getDate();
                        itemStartDate.setDate(monthLastDay);
                        item.setStartDate(itemStartDate);
                        item.setMonthlyOrYearlyDay(monthLastDay);
                    } else {
                        itemStartDate.setDate(item.getMonthlyOrYearlyDay());
                        item.setStartDate(itemStartDate);
                    }
                    break;
                case RECURRENCE_TYPE_YEARLY:
                    item.setInterval(1);
                    item.setYearlyMonth(yMonthListBox.getSelectedId());
                    if (yTypeBox.getSelectedId() == 0) {
                        try {
                            item.setMonthlyOrYearlyDay(Integer.parseInt(yTextBox.getText()));
                        } catch (Exception e) {
                            GWT.log("Reccuring Years error.");
                        }
                        item.setMonthlyOrYearlyPatternOption(MONTHLY_OR_YEARLY_PATTERN_CUSTOM);
                    } else {
                        item.setCustomPatternDay(yTypeBox.getSelectedId());
                        item.setMonthlyOrYearlyDay(yWeekBox.getSelectedId());
                        item.setMonthlyOrYearlyPatternOption(MONTHLY_OR_YEARLY_PATTERN_SIMPLE);
                    }
                    break;

            }
        }

        public boolean validate() {
            int errors = 0;
            switch (type.getSelectedId()) {
                case RECURRENCE_TYPE_HOURLY:
                case RECURRENCE_TYPE_DAILY:
                    if (!Validation.validateTextBoxRequired(dayBox)) {
                        errors++;
                    }
                    break;
                case RECURRENCE_TYPE_WEEKLY:
                    if (weekBox.getSelectedValues().isEmpty()) {
                        errors++;
                        weekBox.setStyleName("x-form-invalid");
                    } else {
                        weekBox.setStylePrimaryName("wfm-dropdown");
                    }
                    break;
                case RECURRENCE_TYPE_MONTHLY:
                    if (!Validation.validateTextBoxRequired(mDayBox)) {
                        errors++;
                    } else if (Integer.parseInt(mDayBox.getText()) < 1 || Integer.parseInt(mDayBox.getText()) > 31) {
                        mDayBox.setStyleName("x-form-invalid");
                        errors++;
                    }
                    break;
                case RECURRENCE_TYPE_YEARLY:
                    if (!Validation.validateListBoxRequired(yMonthListBox)) {
                        errors++;
                    }
                    if (yTypeBox.getSelectedId() == 0) {
                        if (!Validation.validateTextBoxRequired(yTextBox)) {
                            errors++;
                        }
                    } else {
                        if (!Validation.validateListBoxRequired(yWeekBox)) {
                            errors++;
                        }
                    }
                    break;
            }
            return errors == 0;
        }

        private Element getWidgetTd(Widget w) {
            return w.getParent() != this ? null : DOM.getParent(w.getElement());
        }

        private void onChangeEvent() {
            div.clear();
            div.add(type);
            RecurringCronWidget.this.removeStyleName("recurringCronWidget--daily");
            RecurringCronWidget.this.removeStyleName("recurringCronWidget--weekly");
            RecurringCronWidget.this.removeStyleName("recurringCronWidget--monthly");
            RecurringCronWidget.this.removeStyleName("recurringCronWidget--yearly");
//            Element td = getWidgetTd(w);
//            if (td != null) {
//                this.setCellVerticalAlignment(td, align);
//            }
            switch (type.getSelectedId()) {
                case RECURRENCE_TYPE_DAILY:
                case RECURRENCE_TYPE_HOURLY:
                    div.add(new HTML(wfmStrings.every()));
                    Div wrapper = new Div();
                    wrapper.addStyleName("emptyTextbox__wrapper");
                    wrapper.add(dayBox);
                    wrapper.add(newSpan);
                    div.add(wrapper);
                    div.add(new HTML(RECURRENCE_TYPE_DAILY == type.getSelectedId() ? wfmStrings.days() : wfmStrings.hours()));
                    RecurringCronWidget.this.addStyleName("recurringCronWidget--daily");
                    break;
                case RECURRENCE_TYPE_WEEKLY:
                    div.add(weekBox);
                    RecurringCronWidget.this.addStyleName("recurringCronWidget--weekly");
                    break;
                case RECURRENCE_TYPE_MONTHLY:
                    div.add(new HTML(wfmStrings.day()));
                    Div wrapper2 = new Div();
                    wrapper2.addStyleName("emptyTextbox__wrapper");
                    wrapper2.add(mDayBox);
                    wrapper2.add(newSpan);
                    div.add(wrapper2);
                    div.add(new HTML(wfmStrings.ofEvery()));
                    div.add(mRepeatBox);
                    div.add(new HTML(wfmStrings.months()));
                    RecurringCronWidget.this.addStyleName("recurringCronWidget--monthly");
                    break;
                case RECURRENCE_TYPE_YEARLY:
                    div.add(yMonthListBox);
                    div.add(yTypeBox);
                    Div wrapper3 = new Div();
                    wrapper3.addStyleName("emptyTextbox__wrapper");
                    wrapper3.add(yValueBox);
                    wrapper3.add(newSpan);
                    div.add(wrapper3);
                    RecurringCronWidget.this.addStyleName("recurringCronWidget--yearly");
                    break;
            }
        }

        private void onTypeChangeEvent() {
            switch (yTypeBox.getSelectedId()) {
                case 0:
                    yWeekBox.removeFromParent();
                    yValueBox.add(yTextBox);
                    break;
                default:
                    yTextBox.removeFromParent();
                    yValueBox.add(yWeekBox);

            }
        }
    }

    private class RecurringEndDateWidget extends GBoxItem {
        private DataListBox type;
        private DatePicker datePicker;
        private TextBox textBox;
        private HTML html;

        private HorizontalPanel div;

        public RecurringEndDateWidget() {
            super();
            if (!twoLines) {
                RecurringEndDateWidget.this.addStyleName("recurringEndDateWidget");
            }
            initialize();
        }

        private void initialize() {
            div = new HorizontalPanel();
            div.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

            type = new DataListBox();
            type.setWithoutNullLabel(true);

            type.addListItem(new SelectItem(NO_END_DATE, wfmStrings.never()));
            type.addListItem(new SelectItem(END_AFTER_OCCURRENCES, wfmStrings.after()));
            type.addListItem(new SelectItem(END_BY_DATE, wfmStrings.until()));

            datePicker = new DatePicker();
            textBox = new TextBox();
            textBox.setMaxLength(3);
            Validation.addNumericKeyboardListener(textBox);

            html = new HTML(wfmStrings.repeats());

            div.add(type);
            type.addValueChangeHandler(c -> onChangeEvent());
            type.setSelected(NO_END_DATE);
            onChangeEvent();

            setLabel(wfmStrings.endDate());
            setComponent(div);
        }

        public void setValue(RecurrenceJobItem item) {
            if (item == null || item.getEndType() == null) return;

            switch (item.getEndType()) {
                case NO_END_DATE:
                    type.setSelected(NO_END_DATE);
                    break;
                case END_AFTER_OCCURRENCES:
                    type.setSelected(END_AFTER_OCCURRENCES);
                    textBox.setText(String.valueOf(item.getOccurrence()));
                    break;
                case END_BY_DATE:
                    type.setSelected(END_BY_DATE);
                    datePicker.setDate(item.getEndDate());
                    break;
            }
            onChangeEventWithItem(item);
        }

        public void getValue(RecurrenceJobItem item) {
            switch (type.getSelectedId()) {
                case NO_END_DATE:
                    item.setEndType(NO_END_DATE);
                    break;
                case END_AFTER_OCCURRENCES:
                    item.setEndType(END_AFTER_OCCURRENCES);
                    if (textBox.getText() != null && textBox.getText().trim().length() > 0) {
                        item.setOccurrence(Integer.valueOf(textBox.getText()));
                    } else {
                        item.setOccurrence(1);
                    }
                    break;
                case END_BY_DATE:
                    item.setEndType(END_BY_DATE);
                    item.setEndDate(datePicker.getDate());
                    break;
            }
        }

        public boolean validate() {
            int errors = 0;
            switch (type.getSelectedId()) {
                case END_AFTER_OCCURRENCES:
                    if (!Validation.validateTextBoxRequired(textBox)) {
                        errors++;
                    }
                    break;
                case END_BY_DATE:
                    if (!Validation.validateDate(datePicker)) {
                        errors++;
                    }
                    break;
            }
            return errors == 0;
        }

        private void onChangeEvent() {
            RecurringEndDateWidget.this.removeStyleName("recurringEndDateWidget--never");
            RecurringEndDateWidget.this.removeStyleName("recurringEndDateWidget--after");
            RecurringEndDateWidget.this.removeStyleName("recurringEndDateWidget--until");
            switch (type.getSelectedId()) {
                case NO_END_DATE:
                    datePicker.removeFromParent();
                    textBox.removeFromParent();
                    html.removeFromParent();
                    if (!twoLines) {
                        RecurringEndDateWidget.this.addStyleName("recurringEndDateWidget--never");
                    }
                    break;
                case END_AFTER_OCCURRENCES:
                    datePicker.removeFromParent();
                    div.add(textBox);
                    textBox.setText("1");
                    div.add(html);
                    if (!twoLines) {
                        RecurringEndDateWidget.this.addStyleName("recurringEndDateWidget--after");
                    }
                    break;
                case END_BY_DATE:
                    textBox.removeFromParent();
                    html.removeFromParent();
                    div.add(datePicker);
                    if (!twoLines) {
                        RecurringEndDateWidget.this.addStyleName("recurringEndDateWidget--until");
                    }
                    break;
            }
        }

        private void onChangeEventWithItem(RecurrenceJobItem item) {
            RecurringEndDateWidget.this.removeStyleName("recurringEndDateWidget--never");
            RecurringEndDateWidget.this.removeStyleName("recurringEndDateWidget--after");
            RecurringEndDateWidget.this.removeStyleName("recurringEndDateWidget--until");
            switch (type.getSelectedId()) {
                case NO_END_DATE:
                    datePicker.removeFromParent();
                    textBox.removeFromParent();
                    html.removeFromParent();
                    if (!twoLines) {
                        RecurringEndDateWidget.this.addStyleName("recurringEndDateWidget--never");
                    }
                    break;
                case END_AFTER_OCCURRENCES:
                    datePicker.removeFromParent();
                    div.add(textBox);
                    textBox.setText(String.valueOf(item.getOccurrence()));
                    div.add(html);
                    if (!twoLines) {
                        RecurringEndDateWidget.this.addStyleName("recurringEndDateWidget--after");
                    }
                    break;
                case END_BY_DATE:
                    textBox.removeFromParent();
                    html.removeFromParent();
                    div.add(datePicker);
                    if (!twoLines) {
                        RecurringEndDateWidget.this.addStyleName("recurringEndDateWidget--until");
                    }
                    break;
            }
        }
    }

    private class RecurringStartDateWidget extends GBoxItem {
        private DatePicker datePicker;
        private TimeBox timeBox;

        private HorizontalPanel div;

        public RecurringStartDateWidget() {
            super();
            if (!twoLines) {
                RecurringStartDateWidget.this.addStyleName("recurringStartDateWidget");
            }
            initialize();
        }

        private void initialize() {
            div = new HorizontalPanel();
            div.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

            datePicker = new DatePicker(new Date());
            datePicker.addChangeHandler(changeEvent -> datePicker.removeStyleName("x-form-invalid"));
            timeBox = new TimeBox();

            div.add(datePicker);
            div.add(timeBox);

            setLabel(wfmStrings.startDate());
            addComponent(div);
        }

        public void setValue(RecurrenceJobItem item) {
            if (item != null && item.getStartDate() != null) {
                Date date = item.getStartDate();
                datePicker.setDate(date);

                String hour = date.getHours() > 9 ? (Integer.valueOf(date.getHours())).toString() : "0" + (Integer.valueOf(date.getHours())).toString();
                String minut = date.getMinutes() > 9 ? (Integer.valueOf(date.getMinutes())).toString() : "0" + (Integer.valueOf(date.getMinutes())).toString();
                timeBox.setText(hour + ":" + minut);
            }
        }

        public void getValue(RecurrenceJobItem item) {
            String[] time = timeBox.getText().split(":");
            Date date = datePicker.getDate();
            date.setHours(Integer.parseInt(time[0]));
            date.setMinutes(Integer.parseInt(time[1]));
            item.setStartDate(date);
        }

        public boolean validate() {
            final long MIN_MILLIS = 60 * 1000;
            Date currentDate = new Date();
            Date date = datePicker.getDate();
            date.setHours(Integer.parseInt(timeBox.getText().substring(0, 2)));
            date.setMinutes(Integer.parseInt(timeBox.getText().substring(3, 5)));

            long currentMinute = currentDate.getTime() / MIN_MILLIS;
            long startMinute = date.getTime() / MIN_MILLIS;
            if (startMinute - currentMinute < 0) {
                datePicker.addStyleName("x-form-invalid");
                Utils.scrollIntoView(datePicker.getElement());
                Info.show(wfmStrings.recurringStartDateValidationMessage(), Info.Type.WARNING);
                datePicker.setDate(currentDate);
                return false;
            }
            return true;
        }
    }
}
