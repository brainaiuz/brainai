package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.View;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.client.ui.TimeBox;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTable;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Apr 15, 2010
 * Time: 12:26:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class ReminderView extends View implements SchedulerConstant {
    private final CommonServiceAsync commonService = CommonService.App.get();
    private final String htmlSpace = "&nbsp; &nbsp; &nbsp;";
//    private final String REMINDER_DEFAULT_WIDTH = "80px"; //https://prnt.sc/rril4p changed to class "short-field"
    private ArrayList<SelectItem> rolesList;

    private WfmForm table;
    private WfmForm.Field recurrenceTypeField;
    private WfmForm.Field everyDayField;
    private WfmForm.Field monthlyField;
    private WfmForm.Field yearlyField;
    private WfmForm.Field checkBoxesField;
    private WfmForm.Field startDateField;
    private WfmForm.Field endDateField;

    private KpiSwitcher enableEmailReminder;
    private RadioButton hourly;
    private RadioButton daily;
    private RadioButton weekly;
    private RadioButton monthly;
    private RadioButton yearly;
    private RadioButton everyDay;
    private RadioButton everyYear;
    private RadioButton day;
    private RadioButton theYear;
    private RadioButton everyWeekDay;
    private TextBox repeats;
    private TextBox interval;
    private DataListBox repeatsNMonth1;
    private DataListBox repeatsNMonth2;
    private DataListBox dayPositionForYear;
    private DataListBox daysNameForMonth;
    private DataListBox daysNameForYear;
    private DataListBox monthsName1;
    private DataListBox monthsName2;
    private Label typeLabel;
    private KpiCheckBox sunday;
    private KpiCheckBox monday;
    private KpiCheckBox tuesday;
    private KpiCheckBox wednesday;
    private KpiCheckBox thursday;
    private KpiCheckBox friday;
    private KpiCheckBox saturday;
    private DatePicker startDate;
    private DatePicker endDate;
    private RadioButton never;
    private RadioButton until;
    private RadioButton after;
    private TextBox afterBox;
    private WfmButton2 saveButton;
    private RecurrenceJobItem recurrenceItem;
    private String timeType = "Day(s)";
    private Integer recurrenceType;
    private Integer checkBoxCounter = 0;
    private TimeBox timeBox;
    private FlowPanel content;
    private MultiTable roleMultiTable;
    private KpiCheckBox toClientCheckBox;
    private FlexTable chbTable;
    private HTML hhMM;
    private Date date;
    private FlexTable endDateTable;

    private boolean FROM_OVERDUE_INVOICE_FORM = false;
    private boolean FROM_INVOICEADDFORM = false;
    private boolean FROM_RECURRINGBILLFORM = false;
    private boolean FROM_RECURRINGMANUALJOURNALFORM = false;
    private boolean FROM_ADDTASK_FORM = false;
    private boolean FROM_ADD_COURSE_SCHEDULE_FORM = false;
    private boolean FROM_ADDEVENT_FORM = false;
    private boolean FROM_REPORTING_FORM = false;
    private boolean FROM_WORKFLOW_FORM = false;
    private boolean FROM_MAGENTO_FORM = false;

    public ReminderView(Integer fromView) {
        super("invoiceReminder", wfmStrings.overdueInvoiceReminder());
        if (fromView != null) {
            if (fromView == OVERDUE_INVOICE_FORM) {
                this.FROM_OVERDUE_INVOICE_FORM = true;
            } else if (fromView == INVOICE_ADD_FORM) {
                this.FROM_INVOICEADDFORM = true;
            } else if (fromView == RECURRING_BILL_FORM) {
                this.FROM_RECURRINGBILLFORM = true;
            } else if (fromView == RECURRING_MANUAL_JOURNAL_FORM) {
                this.FROM_RECURRINGMANUALJOURNALFORM = true;
            } else if (fromView == RECURRING_TASK_FORM) {
                this.FROM_ADDTASK_FORM = true;
            } else if (fromView == RECURRING_COURSE_SCHEDULE) {
                this.FROM_ADD_COURSE_SCHEDULE_FORM = true;
            } else if (fromView == RECURRING_EVENT_FORM) {
                this.FROM_ADDEVENT_FORM = true;
            } else if (fromView == RECURRING_REPORT_FORM) {
                this.FROM_REPORTING_FORM = true;
            } else if (fromView == RECURRING_WORKFLOW_FORM) {
                this.FROM_WORKFLOW_FORM = true;
            } else if (fromView == RECURRING_MAGENTO_FORM) {
                this.FROM_MAGENTO_FORM = true;
            }
        }
    }

    @Override
    protected Widget onInitialize() {
        commonService.getJob(OVERDUE_INVOICE_REMINDER, new AbstractAsyncCallback<RecurrenceJobItem>() {
            public void failure(Throwable throwable) {
            }

            public void success(RecurrenceJobItem recurrenceJobItem) {
                if (recurrenceJobItem != null) {
                    recurrenceItem = recurrenceJobItem;
                }
                rolesList = recurrenceJobItem.getRoles();
                drawForm(recurrenceItem);
            }
        });
        return null;
    }

    public void drawForm(RecurrenceJobItem recItem) {
        this.recurrenceItem = (recItem != null ? recItem : new RecurrenceJobItem());
        if (saveButton == null) {
            saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
            saveButton.addClickHandler(clickEvent -> save());
        }
        if (enableEmailReminder == null) {
            enableEmailReminder = new KpiSwitcher();
            enableEmailReminder.addClickHandler(clickEvent -> {
                content.setVisible(enableEmailReminder.getValue());
            });
        }
        enableEmailReminder.setValue(true);
        drawAllComponents(recurrenceItem);
    }

    private void drawTitleComponents() {
        if (FROM_OVERDUE_INVOICE_FORM) {
            add(new HTML("<div style='margin:10px 0 0 15px;'><b class=customTitle><font size=+1>" + wfmStrings.overdueInvoiceReminder() + "</font></b></div>"));
            FlowPanel space1 = new FlowPanel();
            space1.setHeight("5px");
            add(space1);
        }
        HorizontalPanel hp = new HorizontalPanel();
        hp.add(new HTML(htmlSpace));
        hp.add(new HTML(wfmStrings.enableReminder()));
        hp.add(new HTML(htmlSpace));
        hp.add(enableEmailReminder);
        add(hp);
    }

    public void drawAllComponents(final RecurrenceJobItem item) {
        if (enableEmailReminder == null) {
            enableEmailReminder = new KpiSwitcher(wfmStrings.enableReminder(), wfmStrings.enableReminder(), true);
            enableEmailReminder.addClickHandler(clickEvent -> {
                content.setVisible(enableEmailReminder.getValue());
            });
        }
        String[] column = {"25%", "75%"};
        table = new WfmForm(column, "100%");
        table.addStyleName("reminder");

        daily = new KpiRadioButton("rb", " " + wfmStrings.daily());
        daily.getElement().setId("Daily_checkbox");
        daily.ensureDebugId("daily-radioButton");
        daily.addStyleName("marginLeft5");
        if (item.getObjectId() == null) {
            daily.setValue(true);
        }
        timeType = wfmStrings.days();
        daily.addClickHandler(clickEvent -> {
            typeLabel.setText(wfmStrings.days());
            timeType = wfmStrings.days();
            recurrenceType = RECURRENCE_TYPE_DAILY;
            clear();
            drawTableBegin();
            drawTableForDaily();
            drawTableEnd(item);
            add(content);
            drawSaveButton();
            recurrenceTypeField.setErrorMessage(null, "");
        });
        hourly = new KpiRadioButton("rb", " " + wfmStrings.hourly());
        hourly.getElement().setId("Hourly_checkbox");
        hourly.addStyleName("marginLeft5");
        hourly.addClickHandler(clickEvent -> {
            typeLabel.setText(wfmStrings.hours());
            timeType = wfmStrings.hours();
            recurrenceType = RECURRENCE_TYPE_HOURLY;
            clear();
            drawTableBegin();
            drawTableForHourly();
            drawTableEnd(item);
            add(content);
            drawSaveButton();
            recurrenceTypeField.setErrorMessage(null, "");
        });
        weekly = new KpiRadioButton("rb", " " + wfmStrings.weekly());
        weekly.getElement().setId("Weekly_checkbox");
        weekly.ensureDebugId("weekly-radioButton");
        weekly.addClickHandler(clickEvent -> {
            typeLabel.setText(wfmStrings.weeks());
            timeType = wfmStrings.weeks();
            recurrenceType = RECURRENCE_TYPE_WEEKLY;
            clear();
            drawTableBegin();
            drawTableForWeekly();
            drawTableEnd(item);
            add(content);
            drawSaveButton();
            recurrenceTypeField.setErrorMessage(null, "");
            if (FROM_ADDEVENT_FORM || FROM_ADDTASK_FORM || FROM_ADD_COURSE_SCHEDULE_FORM) {
                if (date != null) {
                    switch (DateUtil.getWeekDayIndex(date)) {
                        case 0:
                            sunday.setValue(true);
                            break;
                        case 1:
                            monday.setValue(true);
                            break;
                        case 2:
                            tuesday.setValue(true);
                            break;
                        case 3:
                            wednesday.setValue(true);
                            break;
                        case 4:
                            thursday.setValue(true);
                            break;
                        case 5:
                            friday.setValue(true);
                            break;
                        case 6:
                            saturday.setValue(true);
                            break;
                    }
                    checkBoxCounter++;
                }
            }
        });
        monthly = new KpiRadioButton("rb", " " + wfmStrings.monthly());
        monthly.getElement().setId("monthly_checkbox");
        monthly.ensureDebugId("montthly-radioButton");
        monthly.addClickHandler(clickEvent -> {
            typeLabel.setText(wfmStrings.months());
            timeType = wfmStrings.months();
            recurrenceType = RECURRENCE_TYPE_MONTHLY;
            clear();
            drawTableBegin();
            drawTableForMonthly();
            drawTableEnd(item);
            add(content);
            drawSaveButton();
            recurrenceTypeField.setErrorMessage(null, "");
        });
        yearly = new KpiRadioButton("rb", " " + wfmStrings.yearly());
        yearly.getElement().setId("yearly_checkbox");
        yearly.ensureDebugId("yearly-raddioButton");
        yearly.addClickHandler(clickEvent -> {
            recurrenceType = RECURRENCE_TYPE_YEARLY;
            clear();
            drawTableBegin();
            drawTableForYearly();
            drawTableEnd(item);
            add(content);
            drawSaveButton();
            recurrenceTypeField.setErrorMessage(null, "");
        });

        if (item != null && item.getType() != null) {
            switch (item.getType()) {
                case RECURRENCE_TYPE_HOURLY:
                    hourly.setValue(true);
                    break;
                case RECURRENCE_TYPE_DAILY:
                    daily.setValue(true);
                    break;
                case RECURRENCE_TYPE_WEEKLY:
                    weekly.setValue(true);
                    break;
                case RECURRENCE_TYPE_MONTHLY:
                    monthly.setValue(true);
                    break;
                case RECURRENCE_TYPE_YEARLY:
                    yearly.setValue(true);
                    break;
            }
        }

        everyDay = new KpiRadioButton("evd", " " + wfmStrings.every());
        everyDay.ensureDebugId("repeats-every");
        everyDay.getElement().setId("everyDay_checkbox");
        everyDay.addStyleName("marginLeft5");
        if (item.getObjectId() == null || item.getDailyPatternOptions() != null && item.getDailyPatternOptions().equals(DAILY_PATTERN_OPTION_INTERVAL)) {
            everyDay.setValue(true);
        }
        everyWeekDay = new KpiRadioButton("evd", " " + wfmStrings.everyWeekday());
        everyWeekDay.getElement().setId("everyDay_checkbox");
        everyWeekDay.ensureDebugId("everyWeekdy-radioButton");
        if (item.getDailyPatternOptions() != null && item.getDailyPatternOptions().equals(DAILY_PATTERN_OPTION_WEEKDAYS)) {
            everyWeekDay.setValue(true);
        }

        typeLabel = new Label(wfmStrings.days());
        typeLabel.getElement().setId("days_label");
        sunday = new KpiCheckBox(" " + wfmStrings.sunday());
        sunday.getElement().setId("sunday_checkbox");
        sunday.addClickHandler(clickEvent -> {
            if (sunday.getValue()) {
                checkBoxCounter++;
            } else {
                checkBoxCounter--;
            }
        });
        if (item != null && item.isSunday() != null && item.isSunday()) {
            sunday.setValue(true);
            checkBoxCounter++;
        }
        monday = new KpiCheckBox(" " + wfmStrings.monday());
        monday.getElement().setId("monday_checkbox");
        monday.addClickHandler(clickEvent -> {
            if (monday.getValue()) {
                checkBoxCounter++;
            } else {
                checkBoxCounter--;
            }
        });
        if (item != null && item.isMonday() != null && item.isMonday()) {
            monday.setValue(true);
            checkBoxCounter++;
        }
        tuesday = new KpiCheckBox(" " + wfmStrings.tuesday());
        tuesday.getElement().setId("tuesday_checkbox");
        tuesday.addClickHandler(clickEvent -> {
            if (tuesday.getValue()) {
                checkBoxCounter++;
            } else {
                checkBoxCounter--;
            }
        });
        if (item != null && item.isTuesday() != null && item.isTuesday()) {
            tuesday.setValue(true);
            checkBoxCounter++;
        }
        wednesday = new KpiCheckBox(" " + wfmStrings.wednesday());
        wednesday.getElement().setId("wednesday_checkbox");
        wednesday.addClickHandler(clickEvent -> {
            if (wednesday.getValue()) {
                checkBoxCounter++;
            } else {
                checkBoxCounter--;
            }
        });
        if (item != null && item.isWednesday() != null && item.isWednesday()) {
            wednesday.setValue(true);
            checkBoxCounter++;
        }
        thursday = new KpiCheckBox(" " + wfmStrings.thursday());
        thursday.getElement().setId("thursday_checkbox");
        thursday.addClickHandler(clickEvent -> {
            if (thursday.getValue()) {
                checkBoxCounter++;
            } else {
                checkBoxCounter--;
            }
        });
        if (item != null && item.isThursday() != null && item.isThursday()) {
            thursday.setValue(true);
            checkBoxCounter++;
        }
        friday = new KpiCheckBox(" " + wfmStrings.friday());
        friday.getElement().setId("friday_checkbox");
        friday.addClickHandler(clickEvent -> {
            if (friday.getValue()) {
                checkBoxCounter++;
            } else {
                checkBoxCounter--;
            }
        });
        if (item != null && item.isFriday() != null && item.isFriday()) {
            friday.setValue(true);
            checkBoxCounter++;
        }
        saturday = new KpiCheckBox(" " + wfmStrings.saturday());
        saturday.getElement().setId("saturday_checkbox");
        saturday.addClickHandler(clickEvent -> {
            if (saturday.getValue()) {
                checkBoxCounter++;
            } else {
                checkBoxCounter--;
            }
        });
        if (item != null && item.isSaturday() != null && item.isSaturday()) {
            saturday.setValue(true);
            checkBoxCounter++;
        }

        dayPositionForYear = new DataListBox();
        dayPositionForYear.addStyleName("short-select");
        setDayPositionForYear(dayPositionForYear);
        dayPositionForYear.addFocusHandler(event -> theYear.setValue(true));

        daysNameForMonth = new DataListBox();
        setDaysNameForMonth(daysNameForMonth);

        daysNameForYear = new DataListBox();
        daysNameForYear.setItems(daysNameForMonth.getItems());
        daysNameForYear.addFocusHandler(event -> theYear.setValue(true));
        daysNameForYear.getElement().addClassName("short-select");

        startDate = new DatePicker();
        startDate.addStyleName("short-field test--startDate");
        startDate.ensureDebugId("startDate");
        startDate.setDate(new Date());
        startDate.addChangeHandler(valueChangeEvent -> validateRecurrenceStartDateTime());

        endDate = new DatePicker();
        endDate.addStyleName("short-field test--endDate");
        endDate.ensureDebugId("endDate");
        endDate.addChangeHandler(dateValueChangeEvent -> until.setValue(true, true));
        if (item != null && item.getStartDate() != null) {
            startDate.setDate(item.getStartDate());
        }
        never = new KpiRadioButton("rbEndDate", " " + "   " + wfmStrings.never());
        setNoWrap(never);

        until = new KpiRadioButton("rbEndDate", "   " + wfmStrings.until());
        setNoWrap(until);

        after = new KpiRadioButton("rbEndDate", "   " + wfmStrings.after());
        setNoWrap(after);

        afterBox = new TextBox();
        afterBox.setWidth("30px");
        Validation.addNumericKeyboardListener(afterBox);
        afterBox.addFocusHandler(event -> after.setValue(true));
        afterBox.addKeyUpHandler(event -> {
            if (afterBox.getText() == null || "".equals(afterBox.getText())) {
                endDateField.setErrorMessage(wfmStrings.pleaseEnterValue(), "");
            } else {
                endDateField.setErrorMessage(null, "");
            }
        });

        if (item != null && item.getEndType() != null && item.getEndType().equals(NO_END_DATE)) {
            never.setValue(true);
        } else if (item != null && item.getEndType() != null && item.getEndType().equals(END_BY_DATE)) {
            until.setValue(true);
            endDate.setDate(item.getEndDate());
        } else if (item != null && item.getEndType() != null && item.getEndType().equals(END_AFTER_OCCURRENCES)) {
            after.setValue(true);
            afterBox.setText(item.getOccurrence().toString());
        }

        repeatsNMonth1 = new DataListBox();
        repeatsNMonth1.addFocusHandler(event -> day.setValue(true));
        repeatsNMonth1.addStyleName("short-field test--repeatsNMonth1");
        repeatsNMonth2 = new DataListBox();
        repeatsNMonth2.addFocusHandler(event -> {
        });
        repeatsNMonth2.addStyleName("short-field test--repeatsNMonth2");
        for (Integer i = 1; i <= 12; i++) {
            repeatsNMonth1.addListItem(new SelectItem(i, i.toString()));
            repeatsNMonth2.addListItem(new SelectItem(i, i.toString()));
        }

        day = new KpiRadioButton("monthly", " " + wfmStrings.day());
        day.addStyleName("test--day");

        monthsName1 = new DataListBox();
        monthsName1.addFocusHandler(event -> everyYear.setValue(true));
        monthsName2 = new DataListBox();
        monthsName2.addFocusHandler(event -> theYear.setValue(true));
        monthsName1.addStyleName("short-select");
        monthsName2.addStyleName("short-select");
        initializeMonthName(monthsName1);
        initializeMonthName(monthsName2);
        everyYear = new KpiRadioButton("yearly", " " + wfmStrings.every());
//        everyYear.addStyleName("marginLeft5");
        if (item.getType() != null && item.getType().equals(RECURRENCE_TYPE_YEARLY) && item.getCustomPatternDay() == null) {
            monthsName1.setSelectedIndex(item.getYearlyMonth());
            everyYear.setValue(true);
        }

        theYear = new KpiRadioButton("yearly", wfmStrings.the());
//        theYear.addStyleName("marginLeft5");
        setNoWrap(theYear);
        if (item.getType() != null && item.getType().equals(RECURRENCE_TYPE_YEARLY) && item.getCustomPatternDay() != null) {
            monthsName2.setSelectedIndex(item.getYearlyMonth());
            daysNameForYear.setSelectedIndex(item.getMonthlyOrYearlyDay());
            dayPositionForYear.setSelectedIndex(item.getCustomPatternDay());
            theYear.setValue(true);
        }

        timeBox = new TimeBox("00:00");
        timeBox.addStyleName("short-field test--timeBox");
        initializeTimeOfStartDate(item.getStartDate());
        timeBox.onClick(() -> validateRecurrenceStartDateTime());

        drawTableBegin();
        if (item != null && item.getType() != null) {
            switch (item.getType()) {
                case RECURRENCE_TYPE_HOURLY:
                    drawTableForHourly();
                    break;
                case RECURRENCE_TYPE_DAILY:
                    drawTableForDaily();
                    break;
                case RECURRENCE_TYPE_WEEKLY:
                    drawTableForWeekly();
                    break;
                case RECURRENCE_TYPE_MONTHLY:
                    drawTableForMonthly();
                    break;
                case RECURRENCE_TYPE_YEARLY:
                    drawTableForYearly();
                    break;
            }
        } else {
            drawTableForDaily();
        }
        drawTableEnd(item);
        add(content);
        if (FROM_OVERDUE_INVOICE_FORM) {
            if (item != null && item.getObjectId() != null) {
                content.setVisible(true);
                enableEmailReminder.setValue(true);
            } else {
                content.setVisible(false);
                enableEmailReminder.setValue(false);
            }
            HorizontalPanel hp1 = new HorizontalPanel();
            hp1.add(new HTML(htmlSpace));
            hp1.add(saveButton);
            add(hp1);
        } else {
            content.setVisible(true);
            enableEmailReminder.setValue(true);
        }
    }

    private void setNoWrap(Widget noWrap) {
        noWrap.getElement().getStyle().setProperty("whiteSpace", "noWrap");
    }

    private TextBox createRepeatsNDayBox() {
        repeats = new TextBox();
        repeats.addStyleName("short-field test--repeats");
        Validation.addNumericKeyboardListener(repeats);
        return repeats;
    }

    private TextBox createIntervalBox() {
        interval = new TextBox();
        interval.setValue("1");
        interval.addStyleName("short-field test--interval");
        Validation.addNumericKeyboardListener(interval);
        return interval;
    }

    public static void initializeMonthName(DataListBox monthsName) {
        monthsName.addListItem(new SelectItem(1, wfmStrings.january()));
        monthsName.addListItem(new SelectItem(2, wfmStrings.february()));
        monthsName.addListItem(new SelectItem(3, wfmStrings.march()));
        monthsName.addListItem(new SelectItem(4, wfmStrings.april()));
        monthsName.addListItem(new SelectItem(5, wfmStrings.may()));
        monthsName.addListItem(new SelectItem(6, wfmStrings.june()));
        monthsName.addListItem(new SelectItem(7, wfmStrings.july()));
        monthsName.addListItem(new SelectItem(8, wfmStrings.august()));
        monthsName.addListItem(new SelectItem(9, wfmStrings.september()));
        monthsName.addListItem(new SelectItem(10, wfmStrings.october()));
        monthsName.addListItem(new SelectItem(11, wfmStrings.november()));
        monthsName.addListItem(new SelectItem(12, wfmStrings.december()));
    }

    private void drawTableBegin() {
        content = new FlowPanel();
        table = new WfmForm();
        table.addStyleName("reccurringPanel-table file--reminderView");
        if (FROM_OVERDUE_INVOICE_FORM) {
            drawTitleComponents();
            table.addHorizontalLine();
        }
        recurrenceTypeField = table.addField(wfmStrings.recurrenceType(), new Widget[]{daily, new HTML(htmlSpace), weekly, new HTML(htmlSpace), monthly, new HTML(htmlSpace), yearly, new HTML(htmlSpace), FROM_WORKFLOW_FORM ? hourly : new HTML(htmlSpace)}, true);
        recurrenceTypeField.getWidgets()[0].getParent().addStyleName(DEFAULT_WIDTH);
    }

    private void drawTableEnd(RecurrenceJobItem item) {
        HTML occur = new HTML(wfmStrings.occurences());
        endDateTable = new FlexTable();
        endDateTable.addStyleName(DEFAULT_WIDTH + " " + "cellSpace-x reccurringPanel-endDate");
        endDateTable.setWidget(0, 0, never);
        endDateTable.setWidget(0, 1, until);
        endDateTable.setWidget(0, 2, endDate);
        endDateTable.setWidget(0, 3, after);
        endDateTable.setWidget(0, 4, afterBox);
        endDateTable.setWidget(0, 5, occur);
        endDateField = table.addField(" " + wfmStrings.endDate(), endDateTable, true);
        never.ensureDebugId("never-radioButton");
        until.ensureDebugId("until-radioButton");
        endDate.ensureDebugId("endDate-datePicker");
        after.ensureDebugId("after-raddioButton");
        afterBox.ensureDebugId("afterBox-occurences");

        content.add(table);
        if (!(FROM_ADDEVENT_FORM || FROM_ADDTASK_FORM || FROM_ADD_COURSE_SCHEDULE_FORM)) {
            hhMM = new HTML(htmlSpace + "hh:mm");
            startDateField = table.addField(" " + wfmStrings.startDate(), new Widget[]{startDate, new HTML(htmlSpace), timeBox, timeBox.isVisible() ? hhMM : new Label()}, true);
        }
        if (FROM_OVERDUE_INVOICE_FORM) {
            roleMultiTable = createSendMultiTable(item.getSelectedRoles());
            toClientCheckBox = new KpiCheckBox();
            toClientCheckBox.setValue(item != null && item.getToClient() != null ? item.getToClient() : Boolean.FALSE);
            chbTable = new FlexTable();
            HTML html = new HTML("<b class=customTitle> &nbsp " + wfmStrings.sendNotification() + "&nbsp</b>");
            html.getElement().getStyle().setMarginBottom(10, Style.Unit.PX);
            html.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
            chbTable.setWidget(0, 0, html);
            chbTable.getCellFormatter().getElement(0, 0).setAttribute("valign", "top");
            chbTable.getColumnFormatter().setWidth(0, "150px");
            chbTable.setWidget(0, 1, roleMultiTable);
            chbTable.setWidget(1, 0, new HTML("<b class=customTitle> &nbsp " + wfmStrings.toClient() + "&nbsp</b>"));
            chbTable.setWidget(1, 1, toClientCheckBox);
            chbTable.setWidget(2, 0, new HTML(""));
            chbTable.getFlexCellFormatter().setHeight(0, 0, "80px");
            content.add(chbTable);
        }
        drawSaveButton();
    }

    private void drawSaveButton() {
        if (FROM_OVERDUE_INVOICE_FORM) {
            FlowPanel space2 = new FlowPanel();
            space2.setHeight("5px");
            add(space2);
            FlowPanel hp1 = new FlowPanel();
            hp1.add(new HTML(htmlSpace));
            hp1.add(saveButton);
            add(hp1);
        }
    }

    private void drawTableForHourly() {
        recurrenceType = RECURRENCE_TYPE_HOURLY;
        repeats = createRepeatsNDayBox();
        if (recurrenceItem.getType() != null && recurrenceItem.getType().equals(RECURRENCE_TYPE_HOURLY) && recurrenceItem.getInterval() != null) {
            repeats.setText(recurrenceItem.getInterval().toString());
        } else {
            repeats.setText(null);
        }
        typeLabel.setText(wfmStrings.hours());
        timeType = wfmStrings.hours();
        FlowPanel hpCheckBoxes1 = new FlowPanel();
        hpCheckBoxes1.add(everyDay);
        everyDay.addStyleName("test--everyDay");
        hpCheckBoxes1.add(repeats);
        repeats.addStyleName("repeats-textBox");
        repeats.ensureDebugId("repeats-textBox");
        HTML type = new HTML("&nbsp " + timeType);
        type.addStyleName("test--drawTableForHourly-type");
        hpCheckBoxes1.add(type);
        everyDayField = table.addField(" " + wfmStrings.repeats(), hpCheckBoxes1, true);

    }

    private void drawTableForDaily() {
        recurrenceType = RECURRENCE_TYPE_DAILY;
        repeats = createRepeatsNDayBox();
        if (recurrenceItem.getType() != null && recurrenceItem.getType().equals(RECURRENCE_TYPE_DAILY) && recurrenceItem.getInterval() != null) {
            repeats.setText(recurrenceItem.getInterval().toString());
        } else {
            repeats.setText(null);
        }
        FlowPanel hpCheckBoxes1 = new FlowPanel();
        hpCheckBoxes1.add(everyDay);
        everyDay.addStyleName("test--everyDay");
        hpCheckBoxes1.add(repeats);
        repeats.addStyleName("test--repeats");
        HTML type = new HTML("&nbsp " + timeType);
        type.addStyleName("test--type");
        hpCheckBoxes1.add(type);
        if (!FROM_ADDEVENT_FORM && !FROM_ADDTASK_FORM && !FROM_WORKFLOW_FORM && !FROM_ADD_COURSE_SCHEDULE_FORM) {
            hpCheckBoxes1.add(everyWeekDay);
            everyWeekDay.addStyleName("left marginLeft5");
        }
        everyDayField = table.addField(" " + wfmStrings.repeats(), hpCheckBoxes1, true);
        everyDayField.getControl().addStyleName("reccurringPanel-everyDay");
    }

    private void drawTableForWeekly() {
        recurrenceType = RECURRENCE_TYPE_WEEKLY;
        VerticalPanel vpCheckBoxes = new VerticalPanel();
        vpCheckBoxes.addStyleName(DEFAULT_WIDTH + " " + "reccurringPanel-forWeekly");
        HorizontalPanel hpCheckBoxes1 = new HorizontalPanel();
        hpCheckBoxes1.add(sunday);
        hpCheckBoxes1.add(new HTML(htmlSpace));
        hpCheckBoxes1.add(monday);
        hpCheckBoxes1.add(new HTML(htmlSpace));
        hpCheckBoxes1.add(tuesday);
        hpCheckBoxes1.add(new HTML(htmlSpace));
        hpCheckBoxes1.add(wednesday);
        HorizontalPanel hpCheckBoxes2 = new HorizontalPanel();
        hpCheckBoxes2.add(thursday);
        hpCheckBoxes2.add(new HTML(htmlSpace));
        hpCheckBoxes2.add(friday);
        hpCheckBoxes2.add(new HTML(htmlSpace));
        hpCheckBoxes2.add(saturday);
        vpCheckBoxes.add(hpCheckBoxes1);
        HorizontalPanel space2 = new HorizontalPanel();
        space2.setHeight("5px");
        vpCheckBoxes.add(space2);
        vpCheckBoxes.add(hpCheckBoxes2);
        checkBoxesField = table.addField(" " + wfmStrings.repeatsOn(), vpCheckBoxes, true);
    }

    private void drawTableForMonthly() {
        recurrenceType = RECURRENCE_TYPE_MONTHLY;
        repeats = createRepeatsNDayBox();
        repeats.addFocusHandler(event -> day.setValue(true));
        repeats.setVisible(true);
        day.setVisible(true);
        if (recurrenceItem.getType() != null && recurrenceItem.getType().equals(RECURRENCE_TYPE_MONTHLY) && recurrenceItem.getMonthlyOrYearlyDay() != null) {
            if (recurrenceItem.getMonthlyOrYearlyPatternOption() == MONTHLY_OR_YEARLY_PATTERN_CUSTOM) {
                repeats.setText(String.valueOf(recurrenceItem.getMonthlyOrYearlyDay()));
            } else {
                repeats.setText(null);
            }
            if (recurrenceItem.getCustomPatternDay() == null) {
                day.setValue(true);
                if (recurrenceItem.getInterval() != null) {
                    repeatsNMonth1.setSelectedIndex(recurrenceItem.getInterval());
                }
            }
        } else {
            if ((FROM_ADDEVENT_FORM || FROM_ADDTASK_FORM || FROM_ADD_COURSE_SCHEDULE_FORM)) {
                repeats.setText("0");
                repeats.setVisible(false);
                day.setVisible(false);
            }
        }

        VerticalPanel vp = new VerticalPanel();
        HorizontalPanel hp1 = new HorizontalPanel();
        hp1.add(day);
        hp1.add(new HTML(htmlSpace));
        hp1.add(repeats);
        if ((FROM_ADDEVENT_FORM || FROM_ADDTASK_FORM || FROM_ADD_COURSE_SCHEDULE_FORM)) {
            hp1.add(new HTML(wfmStrings.every() + " &nbsp"));
        } else {
            hp1.add(new HTML("&nbsp " + wfmStrings.ofEvery() + " &nbsp"));
        }
        hp1.add(repeatsNMonth1);
        hp1.add(new HTML("&nbsp " + wfmStrings.months()));
        vp.add(hp1);
        HorizontalPanel space2 = new HorizontalPanel();
        space2.setHeight("5px");
        vp.add(space2);
        monthlyField = table.addField(" " + wfmStrings.repeats(), vp, true);
        monthlyField.getControl().addStyleName("reccurringPanel-everyMonth");
    }

    private void drawTableForYearly() {
        recurrenceType = RECURRENCE_TYPE_YEARLY;
        repeats = createRepeatsNDayBox();
        repeats.addFocusHandler(event -> everyYear.setValue(true));
        interval = createIntervalBox();
        interval.addFocusHandler(focusEvent -> everyYear.setValue(true));
        if (recurrenceItem.getType() != null && recurrenceItem.getType().equals(RECURRENCE_TYPE_YEARLY) && recurrenceItem.getMonthlyOrYearlyDay() != null && recurrenceItem.getMonthlyOrYearlyPatternOption() == MONTHLY_OR_YEARLY_PATTERN_CUSTOM) {
            repeats.setText(recurrenceItem.getMonthlyOrYearlyDay().toString());
        } else {
            repeats.setText(null);
        }

        if (recurrenceItem.getType() != null && recurrenceItem.getType().equals(RECURRENCE_TYPE_YEARLY) && recurrenceItem.getInterval() != null) {
            interval.setValue(recurrenceItem.getInterval().toString());
        }

        VerticalPanel vp = new VerticalPanel();
        HorizontalPanel hp1 = new HorizontalPanel();
        hp1.add(everyYear);
        hp1.add(new HTML(htmlSpace));
        hp1.add(interval);
        hp1.add(new HTML("&nbsp;" + wfmStrings.years() + "&nbsp;&nbsp;&nbsp;"));
        hp1.add(monthsName1);
        hp1.add(new HTML(htmlSpace));
        hp1.add(repeats);
        HorizontalPanel hp2 = new HorizontalPanel();
        hp2.addStyleName("reccurringPanel-everyYear__row-2");
        hp2.add(theYear);
        hp2.add(new HTML(htmlSpace));
        hp2.add(dayPositionForYear);
        hp2.add(new HTML(htmlSpace));
        hp2.add(daysNameForYear);
        hp2.add(new HTML("&nbsp;" + wfmStrings.of() + "&nbsp;"));
        hp2.add(monthsName2);
        vp.add(hp1);
        HorizontalPanel space2 = new HorizontalPanel();
        space2.setHeight("10px");
        vp.add(space2);
        vp.add(hp2);
        yearlyField = table.addField(" " + wfmStrings.repeats(), vp, true);
        yearlyField.getControl().addStyleName("reccurringPanel-everyYear");
    }

    private void save() {
        if (!validate()) {
            return;
        }
        RecurrenceJobItem item = getData();
        LoadingPanel.loading(true);
        commonService.saveRecurrenceJob(item, new AbstractAsyncCallback<Void>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Void aVoid) {
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.recurrence()), Info.Type.INFO);
                if (FROM_OVERDUE_INVOICE_FORM) {
                    closeTab();
                }
            }
        });
    }

    public RecurrenceJobItem getData() {
        RecurrenceJobItem item = new RecurrenceJobItem();
        item.setObjectId(recurrenceItem.getObjectId());
        if (enableEmailReminder.getValue()) {
            if (FROM_OVERDUE_INVOICE_FORM) {
                item.setJobType(OVERDUE_INVOICE_REMINDER);
                getRoleData(item, roleMultiTable);
                item.setToClient(toClientCheckBox.getValue());
            } else if (FROM_ADDTASK_FORM) {
                item.setJobType(RECURRING_TASK);
            } else if (FROM_ADD_COURSE_SCHEDULE_FORM) {
                item.setJobType(RECURRING_COURSE_SCHEDULE);
            } else if (FROM_ADDEVENT_FORM) {
                item.setJobType(RECURRING_EVENT);
            } else if (FROM_INVOICEADDFORM) {
                item.setJobType(RECURRING_INVOICE_REMINDER);
            } else if (FROM_RECURRINGBILLFORM) {
                item.setJobType(RECURRING_BILL_REMINDER);
            } else if (FROM_RECURRINGMANUALJOURNALFORM) {
                item.setJobType(RECURRING_MANUAL_JOURNAL_REMINDER);
            } else if (FROM_REPORTING_FORM) {
                item.setJobType(RECURRING_REPORT);
            } else if (FROM_WORKFLOW_FORM) {
                item.setJobType(RECURRING_WORKFLOW);
            } else if (FROM_MAGENTO_FORM) {
                item.setJobType(SYNCHRONIZE_MAGENTO_CATALOG);
            } else {
                item.setJobType(OVERDUE_INVOICE_REMINDER);
            }
            item.setBusObjectId(recurrenceItem.getBusObjectId());
            item.setEnabled(enableEmailReminder.getValue());
            item.setType(recurrenceType);                                              // daily, ..., yearly
            date = startDate.getDate();
            date.setHours(Integer.parseInt(timeBox.getText().substring(0, 2)));
            date.setMinutes(Integer.parseInt(timeBox.getText().substring(3, 5)));
            item.setStartDate(date);
            if (until.getValue()) {
                item.setEndType(END_BY_DATE);
                Date endDate = (Date) date.clone();
                endDate.setYear(this.endDate.getDate().getYear());
                endDate.setMonth(this.endDate.getDate().getMonth());
                endDate.setDate(this.endDate.getDate().getDate());
                endDate.setMinutes(date.getMinutes());
                item.setEndDate(endDate);
            } else if (after.getValue()) {
                item.setEndType(END_AFTER_OCCURRENCES);
                item.setOccurrence(Integer.valueOf(afterBox.getText()).equals(0) ? 1 : Integer.valueOf(afterBox.getText()));
            } else {
                item.setEndType(NO_END_DATE);
            }

            if (recurrenceType == null || recurrenceType == RECURRENCE_TYPE_DAILY || recurrenceType == RECURRENCE_TYPE_HOURLY) {
                if (everyDay.getValue()) {
                    item.setInterval(Integer.parseInt(repeats.getText()));
                    item.setDailyPatternOptions(DAILY_PATTERN_OPTION_INTERVAL);
                } else if (!FROM_ADDEVENT_FORM && !FROM_ADDTASK_FORM && !FROM_ADD_COURSE_SCHEDULE_FORM && everyWeekDay.getValue()) {
                    item.setDailyPatternOptions(DAILY_PATTERN_OPTION_WEEKDAYS);
                }
            } else if (recurrenceType == RECURRENCE_TYPE_WEEKLY) {
                item.setInterval(1);
                item.setSunday(sunday.getValue());
                item.setMonday(monday.getValue());
                item.setTuesday(tuesday.getValue());
                item.setWednesday(wednesday.getValue());
                item.setThursday(thursday.getValue());
                item.setFriday(friday.getValue());
                item.setSaturday(saturday.getValue());
            } else if (recurrenceType == RECURRENCE_TYPE_MONTHLY) {
                if (day.getValue()) {
                    item.setMonthlyOrYearlyDay(Integer.parseInt(repeats.getText())); // 15 of 31 (or 30 or 28-29) day of month
                    item.setInterval(repeatsNMonth1.getSelectedIndex());          // interval with month
                    item.setMonthlyOrYearlyPatternOption(MONTHLY_OR_YEARLY_PATTERN_CUSTOM);
                    Date itemStartDate = item.getStartDate();

                    if (DateUtil.getMonthLastDate(startDate.getDate()).getDate() < item.getMonthlyOrYearlyDay()) {
                        int monthLastDay = DateUtil.getMonthLastDate(startDate.getDate()).getDate();
                        itemStartDate.setDate(monthLastDay);
                        item.setStartDate(itemStartDate);
                        item.setMonthlyOrYearlyDay(monthLastDay);
                    } else {
                        itemStartDate.setDate(item.getMonthlyOrYearlyDay());
                        item.setStartDate(itemStartDate);
                    }
                }
            } else if (recurrenceType == RECURRENCE_TYPE_YEARLY) {
                if (everyYear.getValue()) {
                    if (interval.getText() != null) {
                        item.setInterval(Integer.parseInt(interval.getText()));
                    } else {
                        item.setInterval(1);
                    }
                    item.setMonthlyOrYearlyDay(Integer.parseInt(repeats.getText())); // 15 of 31 (or 30 or 28-29) day of month
                    item.setYearlyMonth(monthsName1.getSelectedIndex());
                    item.setMonthlyOrYearlyPatternOption(MONTHLY_OR_YEARLY_PATTERN_CUSTOM);
                } else if (theYear.getValue()) {
                    item.setCustomPatternDay(dayPositionForYear.getSelectedIndex());     // first, ..., last
                    item.setMonthlyOrYearlyDay(daysNameForYear.getSelectedIndex());      // Monday, ..., Saturday
                    item.setYearlyMonth(monthsName2.getSelectedIndex());           // January, ..., December
                    item.setMonthlyOrYearlyPatternOption(MONTHLY_OR_YEARLY_PATTERN_SIMPLE);
                }
            }
            item.setUserTimeZone(getTimeZone(item.getStartDate()));
        } else {
            recurrenceItem.setEnabled(false);
        }
        return item;
    }

    private void getRoleData(RecurrenceJobItem remindersTO, MultiTable reminderTable) {
        int widgetSize = reminderTable.getWidgetsMaps().size();
        if (widgetSize > 0) {
            Map<Integer, SelectItem> roleMap = new HashMap<>();
            for (int i = 0; i < widgetSize; i++) {
                if (((DataListBox) reminderTable.getWidgetMapByRowID(i).getWidgets()[0]).getSelectedItem() != null) {
                    SelectItem roleItem = new SelectItem();
                    roleItem.setId(((DataListBox) reminderTable.getWidgetMapByRowID(i).getWidgets()[0]).getSelectedItem().getId());
                    roleItem.setName(((DataListBox) reminderTable.getWidgetMapByRowID(i).getWidgets()[0]).getSelectedItem().getName());
                    roleMap.put(roleItem.getId(), roleItem);
                }
            }
            if (!roleMap.isEmpty()) {
                remindersTO.setSelectedRoles(new ArrayList<>(roleMap.values()));
            }
        }
    }

    private String getTimeZone(Date date) {
        Integer minutes = date.getTimezoneOffset();
        String gmt = "GMT-";
        String timeZone = gmt + Utils.formatMinutes(minutes);
        if (minutes < 0) {
            gmt = "GMT+";
            minutes = (-1) * minutes;
            timeZone = gmt + Utils.formatMinutes(minutes);
        } else if (minutes == 0) {
            timeZone = "GMT";
        }
        return timeZone;
    }

    public boolean validate() {
        int errors = 0;
        Integer endType = 0;
        if (table != null) {
            table.cleanupErrors();
        }
        if (enableEmailReminder.getValue()) {
            // recurrence type
            if (!Validation.validateRadioButtonRequired(daily, recurrenceTypeField) && !Validation.validateRadioButtonRequired(weekly, recurrenceTypeField) &&
                    !Validation.validateRadioButtonRequired(monthly, recurrenceTypeField) && !Validation.validateRadioButtonRequired(yearly, recurrenceTypeField)) {
                errors++;
            } else {
                recurrenceTypeField.setErrorMessage(null, "");
            }
            // start Date
            if (!Validation.validateDate(startDate, startDateField, true)) {
                errors++;
            }

            if (!never.getValue()) {
                endType++;
            }
            if (!until.getValue() || until.getValue() && endDate.getDate() == null) {
                endType++;
            }

            if (!after.getValue() || after.getValue() && (afterBox.getText() == null || "".equals(afterBox.getText()))) {
                endType++;
            }

            if (endDateField != null && endType == 3) {
                errors++;
                endDateField.setErrorMessage(wfmStrings.pleaseEnterValue(), "");
            }

            // daily
            if (recurrenceType != null && recurrenceType == RECURRENCE_TYPE_DAILY) {
                if (everyDay.getValue()) {
                    if (!Validation.validateTextBoxRequired(repeats, everyDayField)) {
                        errors++;
                    }
                } else if (!Validation.validateRadioButtonRequired(everyWeekDay, everyDayField)) {
                    errors++;
                }
            } else
                // weekly
                if (recurrenceType != null && recurrenceType == RECURRENCE_TYPE_WEEKLY) {
                    TextBox checks = new TextBox();
                    if (checkBoxCounter > 0) {
                        checks.setText(checkBoxCounter.toString());
                    }
                    if (!Validation.validateTextBoxRequired(checks, checkBoxesField)) {
                        errors++;
                    } else {
                        checkBoxesField.setErrorMessage(null, "");
                    }
                } else
                    // monthly
                    if (recurrenceType != null && recurrenceType == RECURRENCE_TYPE_MONTHLY) {
                        if (day.getValue()) {
                            if (!Validation.validateTextBoxRequired(repeats, monthlyField)) {
                                errors++;
                            }
                            try {
                                int monthDay = Integer.parseInt(repeats.getText());
                                if (!(FROM_ADDEVENT_FORM || FROM_ADDTASK_FORM || FROM_ADD_COURSE_SCHEDULE_FORM) && (monthDay < 1 || monthDay > 31)) {
                                    addValidMonthDayHandler();
                                    errors++;
                                }
                            } catch (NumberFormatException e) {
                                addValidMonthDayHandler();
                                errors++;
                            }
                            if (!Validation.validateListBoxRequired(repeatsNMonth1, monthlyField, wfmStrings.pleaseSelect())) {
                                errors++;
                            }
                        } else {
                            TextBox checkRepeats = new TextBox();
                            if (!Validation.validateTextBoxRequired(checkRepeats, monthlyField)) {
                                errors++;
                            }
                        }
                    } else
                        // yearly
                        if (recurrenceType != null && recurrenceType == RECURRENCE_TYPE_YEARLY) {
                            if (everyYear.getValue()) {
                                if (!Validation.validateListBoxRequired(monthsName1, yearlyField, wfmStrings.pleaseSelect())) {
                                    errors++;
                                }
                                if (!Validation.validateTextBoxRequired(repeats, yearlyField)) {
                                    errors++;
                                }
                            } else if (theYear.getValue()) {
                                if (!Validation.validateListBoxRequired(dayPositionForYear, yearlyField, wfmStrings.pleaseSelect())) {
                                    errors++;
                                }
                                if (!Validation.validateListBoxRequired(daysNameForYear, yearlyField, wfmStrings.pleaseSelect())) {
                                    errors++;
                                }
                                if (!Validation.validateListBoxRequired(monthsName2, yearlyField, wfmStrings.pleaseSelect())) {
                                    errors++;
                                }
                            } else if (!Validation.validateRadioButtonRequired(everyYear, yearlyField)) {
                                errors++;
                            }
                        }

            if (errors > 0) {
                if (!FROM_ADDTASK_FORM && !FROM_ADDEVENT_FORM && !FROM_ADD_COURSE_SCHEDULE_FORM) {
                    Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
                }
                return false;
            }
        }
        return true;
    }

    public boolean validateRecurrenceStartDateTime() {
        if (startDateField.isVisible() && (FROM_OVERDUE_INVOICE_FORM || FROM_INVOICEADDFORM || FROM_RECURRINGBILLFORM || FROM_RECURRINGMANUALJOURNALFORM)) {
            final long MIN_MILLIS = 60 * 1000;
            Date currentDate = new Date();
            Date date = startDate.getDate();
            date.setHours(Integer.parseInt(timeBox.getText().substring(0, 2)));
            date.setMinutes(Integer.parseInt(timeBox.getText().substring(3, 5)));

            long currentMinute = currentDate.getTime() / MIN_MILLIS;
            long startMinute = date.getTime() / MIN_MILLIS;
            if (startMinute - currentMinute < 0) {
                Info.show(wfmStrings.recurringStartDateValidationMessage(), Info.Type.WARNING);
                startDate.setDate(currentDate);
                initializeTimeOfStartDate(null);
                return false;
            }
        }
        return true;
    }

    public boolean validateRecurrenceEndDate(Date endDate, String errorMessage) {
        if (until.getValue()) {
            Date recEndDateWithTime = new Date();
            recEndDateWithTime.setYear(this.endDate.getDate().getYear());
            recEndDateWithTime.setMonth(this.endDate.getDate().getMonth());
            recEndDateWithTime.setDate(this.endDate.getDate().getDate());
            recEndDateWithTime.setHours(endDate.getHours());
            recEndDateWithTime.setMinutes(endDate.getMinutes());
            if (!Validation.validateDateOrder(endDate, recEndDateWithTime, errorMessage, false)) {
                endDateField.setErrorMessage(errorMessage, "", 2);
                return false;
            } else {
                endDateField.setErrorMessage(null, "");
            }
        }
        return true;
    }

    public boolean validateIntervalGreaterThanZero(String errorMessage) {
        if (daily.getValue() && everyDay.getValue()) {
            if (!repeats.getText().equals("") && Integer.valueOf(repeats.getText()).equals(0)) {
                everyDayField.setErrorMessage(errorMessage, "");
                return false;
            } else {
                everyDayField.setErrorMessage(null, "");
            }
        }
        return true;
    }

    private void addValidMonthDayHandler() {
        monthlyField.setErrorMessage(wfmStrings.pleaseEnterValidMonthDay(), "");
        repeats.addKeyDownHandler(event -> {
            if (((TextBox) event.getSource()).getText().length() < 1) {
                monthlyField.setErrorMessage(wfmStrings.pleaseEnterValue(), "");
            } else {
                monthlyField.setErrorMessage(null, "");
            }
        });
    }

    public String getIconStyle() {
        return "icon-settings-overdue-invoice";
    }

    public FlowPanel getContent() {
        return content;
    }

    public void setStartDate(Date value) {
        startDate.setDate(value);
    }

    public Integer getRecurrenceType() {
        return recurrenceType;
    }

    public void setStart(Date start) {
        date = start;
    }

    public void hideNeverRadioButton() {
        never.setVisible(false);
        never.getElement().getParentElement().addClassName("empty-cell");
    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    private void initializeTimeOfStartDate(Date startDate) {
        Date date = startDate;
        if (date == null) {
            date = new Date();
            if (date.getMinutes() < 15) {
                date.setMinutes(15);
            } else if (date.getMinutes() < 30) {
                date.setMinutes(30);
            } else if (date.getMinutes() < 45) {
                date.setMinutes(45);
            } else if (date.getMinutes() <= 59) {
                date.setMinutes(0);
                date.setHours(date.getHours() + 1);
            }
        }
        String hour = date.getHours() > 9 ? Integer.toString(date.getHours()) : "0" + (Integer.valueOf(date.getHours()));
        String minut = date.getMinutes() > 9 ? Integer.toString(date.getMinutes()) : "0" + (Integer.valueOf(date.getMinutes()));
        timeBox.setText(hour + ":" + minut);
    }

    public static void setDaysNameForMonth(DataListBox daysNameForMonth) {
        daysNameForMonth.addListItem(new SelectItem(1, wfmStrings.sunday()));
        daysNameForMonth.addListItem(new SelectItem(2, wfmStrings.monday()));
        daysNameForMonth.addListItem(new SelectItem(3, wfmStrings.tuesday()));
        daysNameForMonth.addListItem(new SelectItem(4, wfmStrings.wednesday()));
        daysNameForMonth.addListItem(new SelectItem(5, wfmStrings.thursday()));
        daysNameForMonth.addListItem(new SelectItem(6, wfmStrings.friday()));
        daysNameForMonth.addListItem(new SelectItem(7, wfmStrings.saturday()));
    }

    public static void setDayPositionForYear(DataListBox dayPositionForYear) {
        dayPositionForYear.addListItem(new SelectItem(1, wfmStrings.first()));
        dayPositionForYear.addListItem(new SelectItem(2, wfmStrings.second()));
        dayPositionForYear.addListItem(new SelectItem(3, wfmStrings.third()));
        dayPositionForYear.addListItem(new SelectItem(4, wfmStrings.fourth()));
        dayPositionForYear.addListItem(new SelectItem(5, wfmStrings.last()));
    }

    public static void initializeDayOfMonth(DataListBox dayofMonth) {
        SelectItem[] items = new SelectItem[31];
        for (int i = 0; i < 31; i++) {
            items[i] = new SelectItem(i, i + "");
        }
        dayofMonth.setItems(items);
    }

    private MultiTable createSendMultiTable(ArrayList<SelectItem> roleItems) {
        final MultiTable roleWidget = new MultiTable(5, getMultiTableWidgets());
        roleWidget.setOnLinesAdded(() -> setRoleItemsToListBox(roleWidget, null));
        roleWidget.setSpacing(5);
        if (roleItems == null || roleItems.size() < 1) {
            setRoleItemsToListBox(roleWidget, null);
            return roleWidget;
        }
        for (SelectItem role : roleItems) {
            setRoleItemsToListBox(roleWidget, role);
            roleWidget.cloneWidgetsRow();
        }
        roleWidget.removeRowTable(roleItems.size());
        return roleWidget;
    }

    private MultiTableWidgets getMultiTableWidgets() {
        return new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                WidgetsMap widgetsMap = new WidgetsMap();
                DataListBox rolesListBox = new DataListBox();
                rolesListBox.setWidth("150px");
                widgetsMap.addWidgets(rolesListBox);
                return widgetsMap;
            }

            @Override
            public boolean isFilled() {
                return false;
            }
        };
    }

    private void setRoleItemsToListBox(MultiTable multiTable, SelectItem selectRole) {
        int i = multiTable.getWidgetsMaps().size() - 1;
        for (Widget widget : multiTable.getWidgetsMaps().get(i).getWidgets()) {
            DataListBox listBox = (DataListBox) widget;
            listBox.clear();
            listBox.setItems(rolesList.toArray(new SelectItem[]{}));
            if (selectRole != null) {
                listBox.setSelected(selectRole);
            }
        }
    }
}
