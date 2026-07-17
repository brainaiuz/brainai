package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalendarView extends Composite implements ClickHandler, HasChangeHandlers {

    @Override
    public void onClick(ClickEvent event) {
    }

    @Override
    public HandlerRegistration addChangeHandler(ChangeHandler handler) {
        return null;
    }

    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final Integer employeeID;
    private String theme;
    private String reasonCode;
    private final DateTimeFormat dayNameFormat;
    private final DateTimeFormat monthFormat;
    private final DateTimeFormat yearFormat;
    private final DateTimeFormat dayNumberFormat;
    private Label currentMonth;
    private final Grid daysGrid;
    private Date displayedMonth;
    private final VerticalPanel panel = new VerticalPanel();
    private CalendarItemRpc[] offDays;
    private CalendarItemRpc[] unAvaDays;
    private CalendarItemRpc[] defaultHolidayDays;
    private CalendarItemRpc[] leaveReqByMoneyDays;
    private CalendarItemRpc[] pendingLeave;
    private CalendarItemRpc[] countAsLeave;
    private Integer year;
    private final ICommand command;
    private Date lrPeriod1 = null;
    private Date lrPeriod2 = null;
    private boolean includeDaysOff;
    private boolean isExceptionalTimeSlot;
    private List<Integer> weekDaysToCountAsLeave;

    private ArrayList<String> reasonColours;
    private ArrayList<String> reasonNames;

    public void CalendarView() {
    }

    {
        this.theme = "blue";
        this.dayNameFormat = DateTimeFormat.getFormat("E");
        this.yearFormat = DateTimeFormat.getFormat("yyyy");
        this.monthFormat = DateTimeFormat.getFormat("MM");
        this.dayNumberFormat = DateTimeFormat.getFormat("d");
        this.daysGrid = new Grid(7, 7);
    }

    public CalendarView(Integer employeeID, String reasonCode, Integer year, ICommand command) {
        this(employeeID, reasonCode, year, command, null);

    }

    public CalendarView(Integer employeeID, String reasonCode, Integer year, ICommand command, Date displayDate) {
        this.employeeID = employeeID;
        this.year = year;
        this.command = command;
        this.reasonCode = reasonCode;
        if (displayDate == null) {
            DatePicker datePicker = new DatePicker();
            displayDate = datePicker.getCurrentDate();
        }
        if (getYear() != null) {
            displayDate.setYear(getYear() - 1900);
        }
        setDisplayedMonth(displayDate);
        panel.setStyleName(theme + "-date-picker");
//        panel.getElement().getStyle().setWidth(214, Style.Unit.PX);
        drawMonthLine(panel);
        drawWeekLine(panel);
        drawDayGrid(panel);
        getCalendarItems();
        initWidget(panel);
    }

    private void getCalendarItems() {
        Date displayFirstDay = this.getDaysGridOrigin(displayedMonth);  //display shown first date
        Date displayLastDay = DateUtil.addDays(displayFirstDay, 7 * 7); //display shown last date
        AllInOneService.App.get().getCalendarItems(employeeID, reasonCode, new DateNonConvertable(displayFirstDay), new DateNonConvertable(displayLastDay), new AbstractAsyncCallback<CalendarItems>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(CalendarItems calendarItems) {
                CalendarView.this.offDays = calendarItems.getOffDays();
                CalendarView.this.unAvaDays = calendarItems.getUnAva();
                CalendarView.this.defaultHolidayDays = calendarItems.getDefaultHolidayDays();
                CalendarView.this.leaveReqByMoneyDays = calendarItems.getLeaveReqByMoney();
                CalendarView.this.weekDaysToCountAsLeave = calendarItems.getDaysToCountAsLeave();
                CalendarView.this.isExceptionalTimeSlot = calendarItems.isExceptionalTimeSlot();

                List<CalendarItemRpc> pendings = new ArrayList<>();
                List<CalendarItemRpc> countAsLeaves = new ArrayList<>();
                if (lrPeriod1 != null && (lrPeriod2 == null || Validation.validateDateOrder(lrPeriod1, lrPeriod2))) {
                    Date period = lrPeriod1;
                    while (period.before(displayLastDay)) {
                        DateTimeFormat format = DateTimeFormat.getFormat("c");
                        Integer dayOfWeek = Integer.parseInt(format.format(period)) == 7 ? 0 : Integer.parseInt(format.format(period));
                        boolean sameDay = lrPeriod2 == null || DateUtils.areOnTheSameDay(period, lrPeriod2);
                        pendings.add(new CalendarItemRpc(new DateNonConvertable(period)));
                        if (weekDaysToCountAsLeave != null && weekDaysToCountAsLeave.size() > 0) {
                            for (Integer day : weekDaysToCountAsLeave) {
                                if (dayOfWeek.equals(day)) {
                                    countAsLeaves.add(new CalendarItemRpc(new DateNonConvertable(period)));
                                }
                            }
                        }
                        if (includeDaysOff) {
                            for (CalendarItemRpc offDay : offDays) {
                                if (DateUtils.areOnTheSameDay(offDay.getNonConvertable().getDate(), period) && (weekDaysToCountAsLeave == null || !weekDaysToCountAsLeave.contains(dayOfWeek))) {
                                    countAsLeaves.add(new CalendarItemRpc(new DateNonConvertable(period)));
                                }
                            }
                        }
                        period = DateUtils.addDays(period, 1);
                        if (sameDay) {
                            break;
                        }
                    }
                }
                pendingLeave = pendings.toArray(new CalendarItemRpc[]{});
                countAsLeave = countAsLeaves.toArray(new CalendarItemRpc[]{});

                //set display month
                displayMonth();
            }
        });
    }

    public Date getDisplayedMonth() {
        return displayedMonth;
    }

    /**
     * Set the month which is display by the PopupCalendar.
     *
     * @param displayedMonth The Date to display
     */
    public void setDisplayedMonth(Date displayedMonth) {
        this.displayedMonth = displayedMonth;
    }

    /**
     * Return the theme used by the PopupCalendar.
     *
     * @return Name of the theme
     */
    public String getTheme() {
        return this.theme;
    }

    /**
     * Set the theme used by the PopupCalendar.
     *
     * @param theme Name of the theme
     */
    public void setTheme(String theme) {
        this.theme = theme;
        this.setStyleName(theme + "-date-picker");
    }

    /**
     * Refresh the PopupCalendar and show it.
     */
    public void displayMonth() {
        this.drawLabelMoisAnnee();
        this.drawDaysGridContent(this.displayedMonth);
    }

    /**
     * Draw the monthLine with contains navigations buttons (change the month
     * and the year) and displayed the displayed month.
     *
     * @param panel The panel contained in the popup
     */
    private void drawMonthLine(Panel panel) {
        Grid monthLine = new Grid(1, 5);
        monthLine.setStyleName(theme + "-" + "month-line");
        CellFormatter monthCellFormatter = monthLine.getCellFormatter();

        Label previousYear = new Label("«");
        previousYear.addClickHandler(sender -> CalendarView.this.changeMonth(-12));
        monthLine.setWidget(0, 0, previousYear);
        Label previousMonth = new Label("‹");
        previousMonth.addClickHandler(sender -> CalendarView.this.changeMonth(-1));
        monthLine.setWidget(0, 1, previousMonth);
        monthCellFormatter.setWidth(0, 2, "60%");
        currentMonth = new Label();
        monthLine.setWidget(0, 2, currentMonth);
        Label nextMonth = new Label("›");
        nextMonth.addClickHandler(sender -> CalendarView.this.changeMonth(1));
        monthLine.setWidget(0, 3, nextMonth);
        Label nextYear = new Label("»");
        nextYear.addClickHandler(sender -> CalendarView.this.changeMonth(12));
        monthLine.setWidget(0, 4, nextYear);
        panel.add(monthLine);
    }

    /**
     * Draw the week line which displays first letter of week days. example : S
     * M T ....etc
     *
     * @param panel The panel contained in the popup
     */
    private void drawWeekLine(Panel panel) {
        Grid weekLine = new Grid(1, 7);
        weekLine.setStyleName(theme + "-" + "week-line");
        weekLine.getElement().getStyle().setWidth(214, Style.Unit.PX);
        Date /*weekFirstday = DateUtil.getWeekFirstDay();*/
                weekFirstday = DateUtil.getWeekFirstDay(new Date(), Integer.valueOf(Utils.userSettings.get(Constants.OVERALL_DATE_PICKER_WEEK_START)) - 1);
        for (int i = 0; i < 7; i++) {
            weekLine.setText(0, i, dayNameFormat.format(DateUtil.addDays(weekFirstday, i)).substring(0, 1).toUpperCase());
        }
        panel.add(weekLine);
    }

    /**
     * Display the grid which contains the days. When a day is clicked, it
     * updates the Date contained in the DatePicker.
     *
     * @param panel The panel contained in the popup
     */
    private void drawDayGrid(Panel panel) {
        daysGrid.setStyleName(theme + "-" + "day-grid-not-pointer");
        daysGrid.getElement().getStyle().setWidth(214, Style.Unit.PX);
        panel.add(daysGrid);
    }

    /**
     * Update the Label which shows the displayed month (in the month line).
     */
    private void drawLabelMoisAnnee() {
        currentMonth.setText(replaceMonth(monthFormat.format(this.displayedMonth)) + " " + yearFormat.format(this.displayedMonth));
    }

    public static String replaceMonth(String format) {
        String s = "";
        switch (format) {
            case "01":
                s = wfmStrings.january();
                break;
            case "02":
                s = wfmStrings.february();
                break;
            case "03":
                s = wfmStrings.march();
                break;
            case "04":
                s = wfmStrings.april();
                break;
            case "05":
                s = wfmStrings.may();
                break;
            case "06":
                s = wfmStrings.june();
                break;
            case "07":
                s = wfmStrings.july();
                break;
            case "08":
                s = wfmStrings.august();
                break;
            case "09":
                s = wfmStrings.september();
                break;

            case "10":
                s = wfmStrings.october();
                break;
            case "11":
                s = wfmStrings.november();
                break;
            case "12":
                s = wfmStrings.december();
                break;
        }
        return s;
    }

    /**
     * Draw the days into the days grid. Days drawn are the days of the
     * displayed month and few days after and before the displayed month.
     *
     * @param displayedMonth Date of the displayed month
     */
    private void drawDaysGridContent(Date displayedMonth) {
        CellFormatter cfJours = daysGrid.getCellFormatter();
        Date cursor = this.getDaysGridOrigin(displayedMonth);
        Date d = new Date();

        setReasonColours(new ArrayList<String>());
        setReasonNames(new ArrayList<String>());

        ArrayList<String> visibleColours = new ArrayList<String>();
        ArrayList<String> visibleReasons = new ArrayList<String>();

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                daysGrid.setHTML(i, j, "<span>" + dayNumberFormat.format(cursor) + "</span>");
                cfJours.setStyleName(i, j, "");

                if (DateUtil.areOnTheSameDay(d, cursor)) {
                    if (displayedMonth.getMonth() == cursor.getMonth()) {
                        cfJours.addStyleName(i, j, theme + "-" + "current-month-selected");
                    } else {
                        cfJours.addStyleName(i, j, theme + "-" + "selected");
                    }
                } else {
                    if (displayedMonth.getMonth() == cursor.getMonth()) {
                        cfJours.addStyleName(i, j, theme + "-" + "current-month-other-day");
                    } else {
                        cfJours.addStyleName(i, j, theme + "-" + "other-day");
                    }
                }
                if (pendingLeave != null && pendingLeave.length > 0) {
                    for (CalendarItemRpc pending : pendingLeave) {
                        if (DateUtil.areOnTheSameDay(pending.getNonConvertable().getNonConvertedDate(), cursor)) {
                            cfJours.addStyleName(i, j, "highlighted-day");
                            if (isIncludeDaysOff()) {
                                cfJours.addStyleName(i, j, "highlighted-off-day");
                            }
                        }
                    }
                }
                if (defaultHolidayDays != null) {
                    for (CalendarItemRpc defaultHolidayDay : defaultHolidayDays) {
                        if (DateUtil.areOnTheSameDay(defaultHolidayDay.getNonConvertable().getNonConvertedDate(), cursor)) {
                            boolean shouldRemove = true;
                            for (CalendarItemRpc pending : pendingLeave) {
                                if (DateUtil.areOnTheSameDay(pending.getNonConvertable().getNonConvertedDate(), cursor)) {
                                    shouldRemove = !defaultHolidayDay.isSelected();
                                }
                            }
                            if (shouldRemove) {
                                cfJours.removeStyleName(i, j, "highlighted-day");
                                cfJours.removeStyleName(i, j, "highlighted-off-day");
                            }
                            cfJours.addStyleName(i, j, theme + "-" + "red-day");

                            if (!visibleReasons.contains(wfmStrings.holiday())) {
                                visibleColours.add("FD5050");
                                visibleReasons.add(wfmStrings.holiday());
                            }
                        }
                    }
                }

                if (unAvaDays != null) {
                    for (CalendarItemRpc unAvaDay : unAvaDays) {
                        if (DateUtil.areOnTheSameDay(unAvaDay.getNonConvertable().getNonConvertedDate(), cursor)) {
                            cfJours.addStyleName(i, j, theme + "-" + "blue-day");
                            cfJours.addStyleName(i, j, "leave-" + unAvaDay.getColorHex());

                            if (!visibleReasons.contains(unAvaDay.getName())) {
                                visibleColours.add(unAvaDay.getColorHex());
                                visibleReasons.add(unAvaDay.getName());
                            }
                        }
                    }
                }

                if (offDays != null) {
                    for (CalendarItemRpc offDay : offDays) {
                        DateTimeFormat offDayFormat = DateTimeFormat.getFormat("EEE, MMM d, yyyy");
                        String offDayStringFormat = offDayFormat.format(offDay.getNonConvertable().getDate());
                        /*if (j == Integer.parseInt(offDay.getName())) {*/
                        if (DateUtil.areOnTheSameDay(offDay.getNonConvertable().getNonConvertedDate(), cursor)) {
                            cfJours.addStyleName(i, j, theme + "-" + "off-day");
                            if (isExceptionalTimeSlot || isIncludeDaysOff()) {
                                for (CalendarItemRpc asLeave : countAsLeave) {
                                    DateTimeFormat leaveDayFormat = DateTimeFormat.getFormat("EEE, MMM d, yyyy");
                                    String leaveDayStringFormat = leaveDayFormat.format(asLeave.getNonConvertable().getDate());
                                    if (offDayStringFormat.equals(leaveDayStringFormat)) {
                                        cfJours.removeStyleName(i, j, "highlighted-day");
                                        cfJours.addStyleName(i, j, theme + "-" + "count-as-leave-day");
                                    }
                                }
                            }
                        }
                    }
                }
                if (leaveReqByMoneyDays != null) {
                    for (CalendarItemRpc leaveReqByMoneyDay : leaveReqByMoneyDays) {
                        if (DateUtil.areOnTheSameDay(leaveReqByMoneyDay.getNonConvertable().getNonConvertedDate(), cursor)) {
                            cfJours.addStyleName(i, j, theme + "-" + "green-day");
                        }
                    }
                }
                cursor = DateUtil.addDays(cursor, 1);
            }
        }

        setReasonNames(visibleReasons);
        setReasonColours(visibleColours);

        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CALENDAR_DRAW, null, CalendarView.this);
    }

    /**
     * Change the displayed month.
     *
     * @param i Number of month to add to the displayed month
     */
    public void changeMonth(int i) {
        changeMonth(DateUtil.addMonths(this.displayedMonth, i));
    }

    public void changeMonth(Date date) {
        this.displayedMonth = date;
        this.getCalendarItems();
        if (command != null) {
            command.execute(this.displayedMonth);
        }
    }

    /**
     * Return the first day to display. If the month first day is after the 5th
     * day of the week, it return the first day of the week. Else, it returns
     * the first day of the week before.
     *
     * @param displayedMonth - displayed Month
     * @return The first day to display in the grid
     */
    private Date getDaysGridOrigin(Date displayedMonth) {
        int currentYear = displayedMonth.getYear();
        int currentMonth = displayedMonth.getMonth();
        CellFormatter cfJours = daysGrid.getCellFormatter();
        Date monthFirstDay = new Date(currentYear, currentMonth, 1);
        int indice = DateUtil.getWeekDayIndex(monthFirstDay);
        Date origineTableau;
        int firstDay = Integer.valueOf(Utils.userSettings.get(Constants.OVERALL_DATE_PICKER_WEEK_START)) - 1;
        if (indice > 4) {
            origineTableau = DateUtil.getWeekFirstDay(monthFirstDay, firstDay);
        } else {
            origineTableau = DateUtil.getWeekFirstDay(DateUtil.addDays(monthFirstDay, -7), firstDay);
        }
        return origineTableau;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public void setLrPeriod1(Date lrPeriod1) {
        this.lrPeriod1 = lrPeriod1;
    }

    public void setLrPeriod2(Date lrPeriod2) {
        this.lrPeriod2 = lrPeriod2;
    }

    public boolean isIncludeDaysOff() {
        return includeDaysOff;
    }

    public void setIncludeDaysOff(boolean includeDaysOff) {
        this.includeDaysOff = includeDaysOff;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public ArrayList<String> getReasonColours() {
        return this.reasonColours;
    }

    public void setReasonColours(ArrayList<String> reasonColours) {
        this.reasonColours = reasonColours;
    }

    public ArrayList<String> getReasonNames() {
        return this.reasonNames;
    }

    public void setReasonNames(ArrayList<String> reasonNames) {
        this.reasonNames = reasonNames;
    }
}
