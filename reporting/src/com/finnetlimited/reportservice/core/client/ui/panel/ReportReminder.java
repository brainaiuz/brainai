package com.finnetlimited.reportservice.core.client.ui.panel;

import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.FlexPanel;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.client.ui.TimeBox;
import com.edatasite.workforce.gwt.core.client.ui.UiSettings;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.Date;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_WIDTH;

/**
 * User: Sanjar
 * Date: Apr 20, 2011
 * Time: 5:42:09 PM
 */
public class ReportReminder extends Composite implements SchedulerConstant {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private CommonServiceAsync commonService = CommonService.App.get();
    private final DateTimeFormat timeFormat = DateTimeFormat.getFormat("HH:mm");
    private final String htmlSpace = "&nbsp; &nbsp; &nbsp;";


    private WfmForm table;
    private WfmForm.Field recurrenceTypeField;
    private WfmForm.Field everyDayField;      // for daily
    private WfmForm.Field everyWeekDayField;  // for daily
    private WfmForm.Field repeatsNDayField;
    private WfmForm.Field monthlyField;
    private WfmForm.Field yearlyField;
    private WfmForm.Field checkBoxesField;
    private WfmForm.Field startDateField;
    private WfmForm.Field endDateField;

    private KpiCheckBox enableEmailReminder;
    private RadioButton daily;
    private RadioButton weekly;
    private RadioButton monthly;
    private RadioButton yearly;
    private RadioButton everyDay;             // for daily
    private RadioButton everyYear;            // for yearly
    private RadioButton day;                  // for monthly
    private RadioButton theMonth;             // for monthly
    private RadioButton theYear;              // for yearly
    private RadioButton everyWeekDay;
    private TextBox repeats;
    private DataListBox repeatsNMonth1;
    private DataListBox repeatsNMonth2;
    private DataListBox dayPositionForMonth;
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
    private RadioButton until;
    private RadioButton after;
    private TextBox afterBox;
    private HorizontalPanel allComponents;
    private RecurrenceJobItem recurrenceItem;
    private String timeType = "Day(s)";
    private Integer recurrenceType;
    private Integer checkBoxCounter = 0;
    private TimeBox timeBox;
    private FlowPanel content;
    private HTML hhMM;
    private Date date;
    private FlexPanel mainPanel;

    public ReportReminder() {
        super();
        mainPanel = new FlexPanel();
        mainPanel.setStyleName("viewTemplate");
    }

    public void drawForm(RecurrenceJobItem recItem) {
        this.recurrenceItem = (recItem != null ? recItem : new RecurrenceJobItem());
        if (enableEmailReminder == null) {
            enableEmailReminder = new KpiCheckBox();
            enableEmailReminder.addClickHandler(clickEvent -> {
                if (enableEmailReminder.getValue()) {
                    content.setVisible(true);
                } else {
                    content.setVisible(false);
                }
            });
        }
        enableEmailReminder.setValue(true);
        drawAllComponents(recurrenceItem);
    }

    public void drawAllComponents(final RecurrenceJobItem item) {
        if (enableEmailReminder == null) {
            enableEmailReminder = new KpiCheckBox(htmlSpace + wfmStrings.enableReminder(), true);
            enableEmailReminder.addClickHandler(clickEvent -> {
                if (enableEmailReminder.getValue()) {
                    content.setVisible(true);
                } else {
                    content.setVisible(false);
                }
            });
        }
        String[] column = {"25%", "75%"};
        table = new WfmForm(column, "100%");
        table.addStyleName("reminder");

        daily = new KpiRadioButton("rb", " " + wfmStrings.daily());

        if (item.getObjectId() == null) {
            daily.setValue(true);
        }
        daily.addClickHandler(clickEvent -> {
            typeLabel.setText(wfmStrings.days());
            timeType = wfmStrings.days();
            recurrenceType = RECURRENCE_TYPE_DAILY;
            mainPanel.removeAll();
            drawTableBegin();
            drawTableForDaily();
            drawTableEnd();
            mainPanel.add(content);
            recurrenceTypeField.setErrorMessage(null, "");
        });
        weekly = new KpiRadioButton("rb", " " + wfmStrings.weekly());
        weekly.addClickHandler(clickEvent -> {
            typeLabel.setText(wfmStrings.weeks());
            timeType = wfmStrings.weeks();
            recurrenceType = RECURRENCE_TYPE_WEEKLY;
            mainPanel.removeAll();
            drawTableBegin();
            drawTableForWeekly();
            drawTableEnd();
            mainPanel.add(content);
            recurrenceTypeField.setErrorMessage(null, "");
        });
        monthly = new KpiRadioButton("rb", " " + wfmStrings.monthly());
        monthly.addClickHandler(clickEvent -> {
            typeLabel.setText(wfmStrings.months());
            timeType = wfmStrings.months();
            recurrenceType = RECURRENCE_TYPE_MONTHLY;
            mainPanel.removeAll();
            drawTableBegin();
            drawTableForMonthly();
            drawTableEnd();
            mainPanel.add(content);
            recurrenceTypeField.setErrorMessage(null, "");
        });
        yearly = new KpiRadioButton("rb", " " + wfmStrings.yearly());
        yearly.addClickHandler(clickEvent -> {
            recurrenceType = RECURRENCE_TYPE_YEARLY;
            mainPanel.removeAll();
            drawTableBegin();
            drawTableForYearly();
            drawTableEnd();
            mainPanel.add(content);
            recurrenceTypeField.setErrorMessage(null, "");
        });

        if (item != null && item.getType() != null) {
            switch (item.getType()) {
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

        if (item.getObjectId() == null || item.getDailyPatternOptions() != null && item.getDailyPatternOptions().equals(DAILY_PATTERN_OPTION_INTERVAL)) {
            everyDay.setValue(true);
        }
        everyWeekDay = new KpiRadioButton("evd", " " + wfmStrings.everyWeekday());
        if (item.getDailyPatternOptions() != null && item.getDailyPatternOptions().equals(DAILY_PATTERN_OPTION_WEEKDAYS)) {
            everyWeekDay.setValue(true);
        }

        typeLabel = new Label(wfmStrings.days());
        sunday = new KpiCheckBox(" " + wfmStrings.sunday());
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

        dayPositionForMonth = new DataListBox();
//        dayPositionForMonth.addStyleName(DEFAULT_WIDTH); //unusible
        dayPositionForMonth.addStyleName("file--ReportReminder");
        dayPositionForMonth.addListItem(new SelectItem(1, wfmStrings.first()));
        dayPositionForMonth.addListItem(new SelectItem(2, wfmStrings.second()));
        dayPositionForMonth.addListItem(new SelectItem(3, wfmStrings.third()));
        dayPositionForMonth.addListItem(new SelectItem(4, wfmStrings.fourth()));
        dayPositionForMonth.addListItem(new SelectItem(5, wfmStrings.last()));
        dayPositionForMonth.addFocusHandler(event -> theMonth.setValue(true));

        dayPositionForYear = new DataListBox();
        dayPositionForYear.addStyleName(DEFAULT_WIDTH);
        dayPositionForYear.addListItem(new SelectItem(1, wfmStrings.first()));
        dayPositionForYear.addListItem(new SelectItem(2, wfmStrings.second()));
        dayPositionForYear.addListItem(new SelectItem(3, wfmStrings.third()));
        dayPositionForYear.addListItem(new SelectItem(4, wfmStrings.fourth()));
        dayPositionForYear.addListItem(new SelectItem(5, wfmStrings.last()));
        dayPositionForYear.addFocusHandler(event -> theYear.setValue(true));

        daysNameForMonth = new DataListBox();
        daysNameForMonth.addListItem(new SelectItem(1, wfmStrings.sunday()));
        daysNameForMonth.addListItem(new SelectItem(2, wfmStrings.monday()));
        daysNameForMonth.addListItem(new SelectItem(3, wfmStrings.tuesday()));
        daysNameForMonth.addListItem(new SelectItem(4, wfmStrings.wednesday()));
        daysNameForMonth.addListItem(new SelectItem(5, wfmStrings.thursday()));
        daysNameForMonth.addListItem(new SelectItem(6, wfmStrings.friday()));
        daysNameForMonth.addListItem(new SelectItem(7, wfmStrings.saturday()));
        daysNameForMonth.addFocusHandler(event -> theMonth.setValue(true));

        daysNameForYear = new DataListBox();
        daysNameForYear.setItems(daysNameForMonth.getItems());
        daysNameForYear.addFocusHandler(event -> theYear.setValue(true));

        startDate = new DatePicker();
        startDate.addStyleName(DEFAULT_WIDTH);
        startDate.setDate(new Date());

        endDate = new DatePicker();
        endDate.addStyleName(DEFAULT_WIDTH);

        if (item != null && item.getStartDate() != null) {
            startDate.setDate(item.getStartDate());
        }

        until = new KpiRadioButton("rbEndDate", wfmStrings.until());
        setNoWrap(until);
        endDate.getPopup().addPopupListener((sender, autoClosed) -> until.setValue(true, true));
        after = new KpiRadioButton("rbEndDate", wfmStrings.after());
        setNoWrap(after);

        afterBox = new TextBox();
        afterBox.setWidth("30px");
        afterBox.addFocusHandler(event -> after.setValue(true));

        if (item != null && item.getEndType() != null && item.getEndType().equals(END_BY_DATE)) {
            until.setValue(true);
            endDate.setDate(item.getEndDate());
        } else if (item != null && item.getEndType() != null && item.getEndType().equals(END_AFTER_OCCURRENCES)) {
            after.setValue(true);
            afterBox.setText(item.getOccurrence().toString());
        }

        repeatsNMonth1 = new DataListBox();
        repeatsNMonth1.addFocusHandler(event -> day.setValue(true));
        repeatsNMonth1.addStyleName(DEFAULT_WIDTH);
        repeatsNMonth2 = new DataListBox();
        repeatsNMonth2.addFocusHandler(event -> theMonth.setValue(true));
        repeatsNMonth2.addStyleName(DEFAULT_WIDTH);
        for (Integer i = 1; i <= 12; i++) {
            repeatsNMonth1.addListItem(new SelectItem(i, i.toString()));
            repeatsNMonth2.addListItem(new SelectItem(i, i.toString()));
        }

        day = new KpiRadioButton("monthly", " " + wfmStrings.day());

        theMonth = new KpiRadioButton("monthly", " The");
        setNoWrap(theMonth);

        monthsName1 = new DataListBox();
        monthsName1.addFocusHandler(event -> everyYear.setValue(true));
        monthsName2 = new DataListBox();
        monthsName2.addFocusHandler(event -> theYear.setValue(true));
        monthsName2.getElement().getStyle().setMarginLeft(14, Style.Unit.PX);
        monthsName1.addStyleName(DEFAULT_WIDTH);
        monthsName2.addStyleName(DEFAULT_WIDTH);
        initializeMonthName(monthsName1);
        initializeMonthName(monthsName2);
        everyYear = new KpiRadioButton("yearly", " " + wfmStrings.every());
        if (item.getType() != null && item.getType().equals(RECURRENCE_TYPE_YEARLY) && item.getCustomPatternDay() == null) {
            monthsName1.setSelectedIndex(item.getYearlyMonth());
            everyYear.setValue(true);
        }

        theYear = new KpiRadioButton("yearly", " The");
        setNoWrap(theYear);
        if (item.getType() != null && item.getType().equals(RECURRENCE_TYPE_YEARLY) && item.getCustomPatternDay() != null) {
            monthsName2.setSelectedIndex(item.getYearlyMonth());
            daysNameForYear.setSelectedIndex(item.getMonthlyOrYearlyDay());
            dayPositionForYear.setSelectedIndex(item.getCustomPatternDay());
            theYear.setValue(true);
        }

        timeBox = new TimeBox("00:00", UiSettings.BLUE_THEME);
        timeBox.addStyleName(DEFAULT_WIDTH);

        Date startDate;
        if (item.getStartDate() != null) {
            startDate = item.getStartDate();
        } else {
            Date date = new Date();
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
            startDate = date;
        }

        String hour = startDate.getHours() > 9 ? (Integer.valueOf(startDate.getHours())).toString() : "0" + (Integer.valueOf(startDate.getHours())).toString();
        String minut = startDate.getMinutes() > 9 ? (Integer.valueOf(startDate.getMinutes())).toString() : "0" + (Integer.valueOf(startDate.getMinutes())).toString();
        timeBox.setText(hour + ":" + minut);

        drawTableBegin();
        if (item != null && item.getType() != null) {
            switch (item.getType()) {
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
        drawTableEnd();

        mainPanel.add(content);

        content.setVisible(true);
        enableEmailReminder.setValue(true);

        initWidget(mainPanel);
    }

    private void setNoWrap(Widget noWrap) {
        noWrap.getElement().getStyle().setProperty("whiteSpace", "noWrap");
    }

    private TextBox createRepeatsNDayBox() {
        repeats = new TextBox();
        repeats.addStyleName(DEFAULT_WIDTH);
        return repeats;
    }

    private void initializeMonthName(DataListBox monthsName) {
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
        recurrenceTypeField = table.addField(wfmStrings.recurrenceType(), new Widget[]{daily, new HTML(htmlSpace), weekly, new HTML(htmlSpace), monthly, new HTML(htmlSpace), yearly}, true);
    }

    private void drawTableEnd() {
        HTML occur = new HTML(/*"&nbsp; " + */wfmStrings.occurences());
        endDateField = table.addField(" " + wfmStrings.endDate(), new Widget[]{/*never,*/ until, endDate, after, afterBox, occur}, true, 1);
        content.add(table);

        hhMM = new HTML(htmlSpace + "hh:mm");
        startDateField = table.addField(" " + wfmStrings.startDate(), new Widget[]{startDate, new HTML(htmlSpace), timeBox, timeBox.isVisible() ? hhMM : new Label()}, true);
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
        everyDay.addStyleName("left marginRight5");
        hpCheckBoxes1.add(repeats);
        repeats.addStyleName("left marginRight5 marginLeft5");
        HTML type = new HTML("&nbsp " + timeType);
        type.addStyleName("left marginRight5");
        hpCheckBoxes1.add(type);
        everyDayField = table.addField(" " + wfmStrings.repeats(), hpCheckBoxes1, true);
    }

    private void drawTableForWeekly() {
        recurrenceType = RECURRENCE_TYPE_WEEKLY;
/*repeats = createRepeatsNDayBox();
if(recurrenceItem.getType()!=null && recurrenceItem.getType().equals(RECURRENCE_TYPE_WEEKLY) && recurrenceItem.getInterval()!=null)
repeats.setText(recurrenceItem.getInterval().toString());
else repeats.setText(null);*/
        FlexTable table = new FlexTable();
        table.setWidget(0, 0, sunday);
        table.setWidget(0, 1, monday);
        table.setWidget(0, 2, tuesday);
        table.setWidget(0, 3, wednesday);
        table.setWidget(1, 0, thursday);
        table.setWidget(1, 1, friday);
        table.setWidget(1, 2, saturday);

        checkBoxesField = this.table.addField(" " + wfmStrings.repeatsOn(), table, true);
    }

    private void drawTableForMonthly() {
        recurrenceType = RECURRENCE_TYPE_MONTHLY;
        repeats = createRepeatsNDayBox();
        repeats.addFocusHandler(event -> day.setValue(true));
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
            if (recurrenceItem.getCustomPatternDay() != null) {
                dayPositionForMonth.setSelectedIndex(recurrenceItem.getCustomPatternDay());
                daysNameForMonth.setSelectedIndex(recurrenceItem.getMonthlyOrYearlyDay());
                theMonth.setValue(true);
                repeatsNMonth2.setSelectedIndex(recurrenceItem.getInterval());
            }
        }

        VerticalPanel vp = new VerticalPanel();
        HorizontalPanel hp1 = new HorizontalPanel();
        hp1.add(day);
        hp1.add(new HTML(htmlSpace));
        hp1.add(repeats);
        hp1.add(new HTML("&nbsp;" + wfmStrings.ofEvery() + "&nbsp;"));
        hp1.add(repeatsNMonth1);
        hp1.add(new HTML("&nbsp;" + wfmStrings.months()));
        vp.add(hp1);

        hp1 = new HorizontalPanel();
        hp1.add(theMonth);
        hp1.add(new HTML(htmlSpace));
        hp1.add(dayPositionForMonth);
        daysNameForMonth.addStyleName(DEFAULT_WIDTH);
        hp1.add(daysNameForMonth);
        hp1.add(new HTML("&nbsp;" + wfmStrings.ofEvery() + "&nbsp;"));
        hp1.add(repeatsNMonth2);
        hp1.add(new HTML("&nbsp;" + wfmStrings.months()));
        HorizontalPanel space2 = new HorizontalPanel();
        space2.setHeight("5px");
        vp.add(space2);
        vp.add(hp1);
        monthlyField = table.addField(" " + wfmStrings.repeats(), vp, true);
    }

    private void drawTableForYearly() {
        recurrenceType = RECURRENCE_TYPE_YEARLY;
        repeats = createRepeatsNDayBox();
        repeats.addFocusHandler(event -> everyYear.setValue(true));
        if (recurrenceItem.getType() != null && recurrenceItem.getType().equals(RECURRENCE_TYPE_YEARLY) && recurrenceItem.getMonthlyOrYearlyDay() != null && recurrenceItem.getMonthlyOrYearlyPatternOption() == MONTHLY_OR_YEARLY_PATTERN_CUSTOM) {
            repeats.setText(recurrenceItem.getMonthlyOrYearlyDay().toString());
        } else {
            repeats.setText(null);
        }
        VerticalPanel vp = new VerticalPanel();
        HorizontalPanel hp1 = new HorizontalPanel();
        hp1.add(everyYear);
        hp1.add(new HTML("&nbsp;&nbsp;"));
        hp1.add(monthsName1);
        hp1.add(new HTML(htmlSpace));
        hp1.add(repeats);
        vp.add(hp1);

        hp1 = new HorizontalPanel();
        hp1.add(theYear);
        hp1.add(new HTML(htmlSpace));
        hp1.add(dayPositionForYear);
        hp1.add(daysNameForYear);
        hp1.add(new HTML("&nbsp;" + wfmStrings.of()));
        hp1.add(monthsName2);
        vp.add(hp1);

        HorizontalPanel space2 = new HorizontalPanel();
        space2.setHeight("5px");
        vp.add(space2);
        yearlyField = table.addField(" " + wfmStrings.repeats(), vp, true);
    }

    public RecurrenceJobItem getData() {
        RecurrenceJobItem item = new RecurrenceJobItem();
        item.setObjectId(recurrenceItem.getObjectId());
        if (enableEmailReminder.getValue()) {
            item.setJobType(RECURRING_REPORT);
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
                item.setOccurrence(Integer.valueOf(afterBox.getText()));
            }

            if (recurrenceType == null || recurrenceType == RECURRENCE_TYPE_DAILY) {
                if (everyDay.getValue()) {
                    item.setInterval(Integer.parseInt(repeats.getText()));
                    item.setDailyPatternOptions(DAILY_PATTERN_OPTION_INTERVAL);
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
                    Date newDate = (Date) date.clone();
                    newDate = DateUtil.getMonthLastDate(newDate);
                    newDate.setDate(Integer.valueOf(repeats.getText()) <= newDate.getDate() ? Integer.parseInt(repeats.getText()) : newDate.getDate());
                    item.setStartDate(newDate);
                    item.setMonthlyOrYearlyDay(newDate.getDate()/*Integer.parseInt(repeats.getText())*/); // 15 of 31 (or 30 or 28-29) day of month
                    item.setInterval(repeatsNMonth1.getSelectedIndex());          // interval with month
                    item.setMonthlyOrYearlyPatternOption(MONTHLY_OR_YEARLY_PATTERN_CUSTOM);
                } else if (theMonth.getValue()) {
                    item.setCustomPatternDay(dayPositionForMonth.getSelectedIndex());     // first, ..., last
                    item.setMonthlyOrYearlyDay(daysNameForMonth.getSelectedIndex());      // Monday, ..., Saturday
                    item.setInterval(repeatsNMonth2.getSelectedIndex());          // interval with month
                    item.setMonthlyOrYearlyPatternOption(MONTHLY_OR_YEARLY_PATTERN_SIMPLE);
                }
            } else if (recurrenceType == RECURRENCE_TYPE_YEARLY) {
                if (everyYear.getValue()) {
                    item.setInterval(1);
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
        } else {
            recurrenceItem.setEnabled(false);
        }
        return item;
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
            if (!until.getValue() || until.getValue() && endDate.getDate() == null) {
                endType++;
            }

            if (!after.getValue() || after.getValue() && (afterBox.getText() == null || "".equals(afterBox.getText()))) {
                endType++;
            }

            TextBox checkEndType = new TextBox();
            if (endType == 3) {
                checkEndType.setText(endType.toString());
                if (startDateField != null && Validation.validateTextBoxRequired(checkEndType, startDateField)) {
                    errors++;
                }
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
                return false;
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
}
