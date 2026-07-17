package com.edatasite.workforce.gwt.core.client.ui.datePicker;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.hijri.HijriCalc;
import com.edatasite.workforce.gwt.core.client.ui.hijri.SimpleHijriDate;
import com.edatasite.workforce.gwt.core.client.ui.view.CalendarView;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PopupCalendar extends ModifiedPopupPanel implements Constants {

    private boolean leave;
    private String theme;
    private final DatePicker datePicker;
    private final DateTimeFormat dayNameFormat;
    private final DateTimeFormat monthFormat;
    private final DateTimeFormat monthOnlyFormat;
    private final DateTimeFormat monthMinFormat;
    private final DateTimeFormat dayNumberFormat;
    private final DateTimeFormat yearNumberFormat;
    private Label currentMonth;
    private Label currentHijriMonth;
    private final Grid daysGrid;
    private final Grid monthGrid;
    private final Grid yearsGrid;
    private Date displayedMonth;
    private final List<ChangeHandler> handlers = new ArrayList<>();
    private String useCustomWeekStart;
    private boolean showYearArrows;
    private boolean showMonthGrid;
    private boolean showYearGrid;
    private boolean isShowMonthGrid;
    private boolean isShowYearGrid;

    {
        this.leave = true;
        this.theme = "blue";
        this.dayNameFormat = Utils.isArabicLanguage() ? DateTimeFormat.getFormat("ccccc") : DateTimeFormat.getFormat("E");
        this.monthFormat = DateTimeFormat.getFormat("MMMM yyyy");
        this.monthOnlyFormat = DateTimeFormat.getFormat("MMM");
        this.monthMinFormat = DateTimeFormat.getFormat("MM");
        this.dayNumberFormat = DateTimeFormat.getFormat("d");
        this.yearNumberFormat = DateTimeFormat.getFormat("y");
        this.daysGrid = new Grid(7, 7);
        this.monthGrid = new Grid(3, 4);
        this.yearsGrid = new Grid(3, 4);
        this.useCustomWeekStart = OVERALL_DATE_PICKER_WEEK_START;
        this.showYearArrows = true;
    }

    /**
     * Create a calendar popup. You have to call the displayMonth method to
     * display the the popup.
     *
     * @param datePicker The date picker on which the popup is attached
     */
    public PopupCalendar(DatePicker datePicker) {
        this(datePicker, OVERALL_DATE_PICKER_WEEK_START, true);
    }

    public PopupCalendar(DatePicker datePicker, String useCustomWeekStart, boolean showYearArrows) {
        super(true);
//        observable = new Observable();
        this.datePicker = datePicker;
        this.setStyleName(theme + "-date-picker");
        this.useCustomWeekStart = useCustomWeekStart;
        this.showYearArrows = showYearArrows;
        VerticalPanel panel = new VerticalPanel();
        this.add(panel);

        sinkEvents(Event.ONBLUR);
        drawGrids(panel);
    }

    public PopupCalendar(DatePicker datePicker, String useCustomWeekStart, boolean showYearArrows, boolean isShowMonthGrid) {
        super(true);
        this.datePicker = datePicker;
        this.setStyleName(theme + "-date-picker");
        this.useCustomWeekStart = useCustomWeekStart;
        this.showYearArrows = showYearArrows;
        this.isShowMonthGrid = isShowMonthGrid;

        VerticalPanel panel = new VerticalPanel();
        this.add(panel);
        sinkEvents(Event.ONBLUR);
        drawGrids(panel);
    }

    public PopupCalendar(DatePicker datePicker, String useCustomWeekStart, boolean showYearArrows, boolean isShowMonthGrid, boolean isShowYearGrid) {
        super(true);
        this.datePicker = datePicker;
        this.setStyleName(theme + "-date-picker");
        this.useCustomWeekStart = useCustomWeekStart;
        this.showYearArrows = showYearArrows;
        this.isShowMonthGrid = isShowMonthGrid;
        this.isShowYearGrid = isShowYearGrid;

        VerticalPanel panel = new VerticalPanel();
        this.add(panel);
        sinkEvents(Event.ONBLUR);
        drawGrids(panel);
    }

    private void drawGrids(VerticalPanel panel) {
        panel.clear();
        if (isShowYearGrid) {
            drawYearOnlyGrids(panel);
        } else if (isShowMonthGrid) {
            drawMonthGrids(panel);
        } else {
            drawMonthLine(panel);

            if (showYearGrid) {
                drawYearGrid(panel);
                drawLabelMoisAnnee();
            } else if (showMonthGrid) {
                drawMonthGrid(panel);
                drawLabelMoisAnnee();
            } else {
                if (Utils.isAlternativeCalendar()) {
                    drawHijriyMonthLineCalendar(panel);
                }
                drawWeekLine(panel);
                if (Utils.isAlternativeCalendar()) {
                    drawHijriyWeekLineCalendar(panel);
                }
                drawDayGrid(panel);
            }
        }

    }

    private void drawYearOnlyGrids(VerticalPanel panel) {
        panel.clear();
        Grid yearLine = new Grid(1, 3);
        yearLine.setStyleName(theme + "-" + "month-line");
        Label previousYears = new Label("«");
        previousYears.addClickHandler(sender -> {
            leave = false;
            PopupCalendar.this.changeMonth(-144);
        });
        yearLine.setWidget(0, 0, previousYears);
        Label nextYears = new Label("»");
        nextYears.addClickHandler(sender -> {
            leave = false;
            PopupCalendar.this.changeMonth(144);
        });
        yearLine.setWidget(0, 2, nextYears);
        currentMonth = new Label();
        yearLine.setWidget(0, 1, currentMonth);
        panel.add(yearLine);

        yearsGrid.addTableListener((sender, row, cell) -> {
            Date selectedYear = DateUtil.addYears(getYearsGridOrigin(displayedMonth), row * 4 + cell);
            setDisplayedMonth(selectedYear);
            datePicker.setDate(selectedYear);
            datePicker.synchronizeFromDate();
            PopupCalendar.this.hide();
            leave = true;
            for (ChangeHandler handler : handlers) {
                handler.onChange(null);
            }
        });
        yearsGrid.setStyleName(theme + "-" + "day-grid");
        panel.add(yearsGrid);
        // content is populated by displayMonth() when the popup opens
    }

    private void drawMonthGrids(VerticalPanel panel) {
        panel.clear();

        drawOnlyMonthLine(panel);

        if (showYearGrid) {
            drawYearGrid(panel);
            drawLabelMoisAnnee();
        } else if (showMonthGrid) {
            drawOnlyMonthGrid(panel);
            drawLabelMoisAnnee();
        } else {
            drawMonth(panel);
        }
    }

    /**
     * Return the month displayed by the PopupCalendar.
     *
     * @return a Date pointing to the month
     */
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
        if (this.displayedMonth == null) {
            if (datePicker.getDate() != null) {
                this.displayedMonth = datePicker.getDate();
            } else {
                this.displayedMonth = new Date();
            }
        }
        this.drawLabelMoisAnnee();
        this.drawDaysGridContent(this.displayedMonth);
        this.drawMonthsGridContent(this.displayedMonth);
        this.drawYearsGridContent(getYearsGridOrigin(this.displayedMonth));
        show();
    }

    /**
     * This method is destined to be used by the DatePicker in case of focus lost.
     * It creates a delay before the popup hides to allows the popup to catch
     * a click and eventually update the Date of the DatePicker.
     */
    public void hidePopupCalendar() {
        Scheduler.get().scheduleDeferred(() -> {
            Timer t = new Timer() {
                public void run() {
                    if (leave) {
                        hide();
                        showMonthGrid = false;
                        showYearGrid = false;
                        for (ChangeHandler handler : handlers) {
                            handler.onChange(null);
                        }
                    } else {
                        leave = true;
                    }
                }
            };
            t.schedule(300);
        });
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
        if (showYearArrows) {
            Label previousYear = new Label("«");
            previousYear.addClickHandler(sender -> {
                leave = false;
                PopupCalendar.this.changeMonth(showYearGrid ? -144 : -12);
            });
            monthLine.setWidget(0, 0, previousYear);
            Label nextYear = new Label("»");
            nextYear.addClickHandler(sender -> {
                leave = false;
                PopupCalendar.this.changeMonth(showYearGrid ? 144 : 12);
            });
            monthLine.setWidget(0, 4, nextYear);
        }
        if (!showYearGrid && !showMonthGrid) {
            Label previousMonth = new Label("‹");
            previousMonth.addClickListener(sender -> {
                leave = false;
                PopupCalendar.this.changeMonth(-1);
            });
            monthLine.setWidget(0, 1, previousMonth);
            Label nextMonth = new Label("›");
            nextMonth.addClickHandler(sender -> {
                leave = false;
                PopupCalendar.this.changeMonth(1);
            });
            monthLine.setWidget(0, 3, nextMonth);
        }
        monthCellFormatter.setWidth(0, 2, "60%");
        currentMonth = new Label();
        currentMonth.addClickHandler(sender -> {
            leave = false;
            showYearGrid = showMonthGrid;
            showMonthGrid = !showYearGrid;
            drawGrids((VerticalPanel) panel);
        });
        monthLine.setWidget(0, 2, currentMonth);
        panel.add(monthLine);
    }

    private void drawOnlyMonthLine(Panel panel) {
        Grid monthLine = new Grid(1, 5);
        monthLine.setStyleName(theme + "-" + "month-line");
        CellFormatter monthCellFormatter = monthLine.getCellFormatter();
        if (showYearArrows) {
            Label previousYear = new Label("«");
            previousYear.addClickHandler(sender -> {
                leave = false;
                PopupCalendar.this.changeMonth(showYearGrid ? -144 : -12);
            });
            monthLine.setWidget(0, 0, previousYear);
            Label nextYear = new Label("»");
            nextYear.addClickHandler(sender -> {
                leave = false;
                PopupCalendar.this.changeMonth(showYearGrid ? 144 : 12);
            });
            monthLine.setWidget(0, 4, nextYear);
        }
        monthCellFormatter.setWidth(0, 2, "60%");
        currentMonth = new Label();
        currentMonth.addClickHandler(sender -> {
            leave = false;
            showMonthGrid = showYearGrid;
            showYearGrid = !showMonthGrid;
            drawMonthGrids((VerticalPanel) panel);
        });
        monthLine.setWidget(0, 2, currentMonth);
        panel.add(monthLine);
    }


    private void drawHijriyMonthLineCalendar(VerticalPanel panel) {
        currentHijriMonth = new Label();
        currentHijriMonth.getElement().getStyle().setFontWeight(Style.FontWeight.BOLD);
        currentHijriMonth.getElement().getStyle().setColor("black");
        currentHijriMonth.getElement().getStyle().setMarginTop(5, Style.Unit.PX);

        panel.add(currentHijriMonth);
        panel.setCellHorizontalAlignment(currentHijriMonth, HasHorizontalAlignment.ALIGN_CENTER);
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
        Date weekFirstday;
        String overallDatePickerWeekStartT = useCustomWeekStart.equals(TIMESHEET_WEEK_START) ? Utils.userSettings.get(TIMESHEET_WEEK_START) : Utils.userSettings.get(OVERALL_DATE_PICKER_WEEK_START);
        if (overallDatePickerWeekStartT == null || "null".equals(overallDatePickerWeekStartT) || "".equals(overallDatePickerWeekStartT)) {
            overallDatePickerWeekStartT = "1";
        }
        if (overallDatePickerWeekStartT != null) {
            weekFirstday = DateUtil.getWeekFirstDay(new Date(), Integer.valueOf(overallDatePickerWeekStartT) - 1);
        } else {
            weekFirstday = DateUtil.getWeekFirstDay();
        }
        String lang = LocaleInfo.getCurrentLocale().getLocaleName();
        for (int i = 0; i < 7; i++) {
            if (i % 3 == 0 && "uz".equals(lang)) {
                weekLine.setText(0, i, dayNameFormat.format(DateUtil.addDays(weekFirstday, i)).substring(0, 2).toUpperCase());
            } else {
                weekLine.setText(0, i, dayNameFormat.format(DateUtil.addDays(weekFirstday, i)).substring(0, 1).toUpperCase());
            }
        }
        panel.add(weekLine);
    }


    private void drawHijriyWeekLineCalendar(VerticalPanel panel) {
        //To change body of created methods use File | Settings | File Templates.
    }

    /**
     * Display the grid which contains the days. When a day is clicked, it
     * updates the Date contained in the DatePicker.
     *
     * @param panel The panel contained in the popup
     */
    private void drawDayGrid(Panel panel) {
        this.daysGrid.addTableListener((sender, row, cell) -> {
            Date selectedDay = DateUtil.addDays(getDaysGridOrigin(displayedMonth), row * 7 + cell);
            datePicker.setDate(selectedDay);
            datePicker.synchronizeFromDate();
            PopupCalendar.this.hide();
            leave = true;
            for (ChangeHandler handler : handlers) {
                handler.onChange(null);
            }
        });
        daysGrid.setStyleName(theme + "-" + "day-grid");
        panel.add(daysGrid);
    }

    private void drawMonth(Panel panel) {
        this.monthGrid.addTableListener((sender, row, cell) -> {
            Date selectedDay = DateUtil.addMonths(getMonthsGridOrigin(displayedMonth), row * 4 + cell);

            setDisplayedMonth(selectedDay);
            datePicker.setDate(selectedDay);
            datePicker.synchronizeFromDate();
            PopupCalendar.this.hide();
            leave = true;
            drawLabelMoisAnnee();
            for (ChangeHandler handler : handlers) {
                handler.onChange(null);
            }
        });
        monthGrid.setStyleName(theme + "-" + "day-grid");
        panel.add(monthGrid);
    }
    /**
     * Display the grid which contains the months. When a month is clicked, it
     * updates the value of displayedMonth's month
     *
     * @param panel The panel contained in the popup
     */
    private void drawMonthGrid(Panel panel) {
        this.monthGrid.addTableListener((sender, row, cell) -> {
            Date selectedDay = DateUtil.addMonths(getMonthsGridOrigin(displayedMonth), row * 4 + cell);
            setDisplayedMonth(selectedDay);
            leave = false;
            showMonthGrid = false;
            drawGrids((VerticalPanel) panel);
            drawLabelMoisAnnee();
            drawDaysGridContent(displayedMonth);
            for (ChangeHandler handler : handlers) {
                handler.onChange(null);
            }
        });
        monthGrid.setStyleName(theme + "-" + "day-grid");
        panel.add(monthGrid);
        drawMonthsGridContent(getMonthsGridOrigin(displayedMonth));
    }

    private void drawOnlyMonthGrid(Panel panel) {
        monthGrid.addTableListener((sender, row, cell) -> {
            Date selectedDay = DateUtil.addMonths(getMonthsGridOrigin(displayedMonth), row * 4 + cell);
            setDisplayedMonth(selectedDay);
            datePicker.setDate(selectedDay);
            datePicker.synchronizeFromDate();
            PopupCalendar.this.hide();
            leave = true;
            showMonthGrid = false;
            drawMonthGrids((VerticalPanel) panel);
            drawLabelMoisAnnee();
            for (ChangeHandler handler : handlers) {
                handler.onChange(null);
            }
        });
        monthGrid.setStyleName(theme + "-" + "day-grid");
        panel.add(monthGrid);
        drawMonthsGridContent(getMonthsGridOrigin(displayedMonth));

    }

    /**
     * Display the grid which contains the years. When a year is clicked, it
     * updates the value of displayedMonth's year
     *
     * @param panel The panel contained in the popup
     */
    private void drawYearGrid(Panel panel) {
        this.yearsGrid.addTableListener((sender, row, cell) -> {
            Date selectedDay = DateUtil.addYears(getYearsGridOrigin(displayedMonth), row * 4 + cell);
            setDisplayedMonth(selectedDay);
            leave = false;
            showYearGrid = false;
            showMonthGrid = true;
            if (isShowMonthGrid) {
                drawMonthGrids((VerticalPanel) panel);
            } else {
                drawGrids((VerticalPanel) panel);
            }
            for (ChangeHandler handler : handlers) {
                handler.onChange(null);
            }
        });
        yearsGrid.setStyleName(theme + "-" + "day-grid");
        panel.add(yearsGrid);
        drawYearsGridContent(getYearsGridOrigin(displayedMonth));
    }

    public void addChangeHandler(ChangeHandler handler) {
        handlers.add(handler);
    }

    /**
     * Update the Label which shows the displayed month (in the month line).
     */
    private void drawLabelMoisAnnee() {
        if (showYearGrid || isShowYearGrid) {
            Date origin = getYearsGridOrigin(this.displayedMonth);
            int selectedYear = Integer.parseInt(yearNumberFormat.format(origin));
            currentMonth.setText(selectedYear + "-" + (selectedYear + 11));
        } else if (showMonthGrid) {
            currentMonth.setText(yearNumberFormat.format(this.displayedMonth));
        } else {
            currentMonth.setText(CalendarView.replaceMonth(monthMinFormat.format(this.displayedMonth)) +
                    " " + DateUtils.getYear(displayedMonth));
        }
        if (Utils.isAlternativeCalendar() && displayedMonth != null) {
            SimpleHijriDate simpleHijriDate = HijriCalc.toHijri(this.displayedMonth);
            currentHijriMonth.setText(simpleHijriDate.getDatePickerCurrentMonthFormat());
        }
    }

    /**
     * Draw the days into the days grid. Days drawn are the days of the displayed month
     * and few days after and before the displayed month.
     *
     * @param displayedMonth Date of the displayed month
     */
    private void drawDaysGridContent(Date displayedMonth) {
        CellFormatter cfJours = daysGrid.getCellFormatter();
        Date cursor = this.getDaysGridOrigin(displayedMonth);
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                daysGrid.setText(i, j, dayNumberFormat.format(cursor));
                cfJours.removeStyleName(i, j, theme + "-" + "selected");
                cfJours.removeStyleName(i, j, theme + "-"
                        + "current-month-selected");
                cfJours.removeStyleName(i, j, theme + "-" + "other-day");
                cfJours.removeStyleName(i, j, theme + "-"
                        + "current-month-other-day");
                cfJours.removeStyleName(i, j, theme + "-" + "week-end");
                cfJours.removeStyleName(i, j, theme + "-"
                        + "current-month-week-end");
                cfJours.removeStyleName(i, j, theme + "-" + "current-day");

                if (datePicker.getDate() != null
                        && DateUtil.areOnTheSameDay(datePicker.getDate(), cursor)) {
                    if (displayedMonth.getMonth() == cursor.getMonth()) {


                        cfJours.addStyleName(i, j, theme + "-" + "current-month-selected");
                    } else {
                        cfJours.addStyleName(i, j, theme + "-" + "selected");
                    }
                } else if (/*useCustomWeekStart ? */DateUtil.isInWeekEndCustom(cursor, useCustomWeekStart)/* : DateUtil.isInWeekEnd(cursor)*/) {
                    if (displayedMonth.getMonth() == cursor.getMonth()) {
                        cfJours.addStyleName(i, j, theme + "-"
                                + "current-month-week-end");
                    } else {
                        cfJours.addStyleName(i, j, theme + "-" + "week-end");
                    }
                } else if (displayedMonth.getMonth() == cursor.getMonth()) {
                    cfJours.addStyleName(i, j, theme + "-"
                            + "current-month-other-day");
                } else {
                    cfJours.addStyleName(i, j, theme + "-" + "other-day");
                }
                if (DateUtil.areOnTheSameDay(datePicker.getCurrentDate(), cursor)) {
                    cfJours.addStyleName(i, j, theme + "-" + "current-day");
                    cfJours.getElement(i, j).setAttribute("id", "datePicker-currentDay");
                }

                cursor = DateUtil.addDays(cursor, 1);
            }
        }
    }

    /**
     * Draw 12 months into the month grid
     *
     * @param selectedMonth months of displayed year
     */
    private void drawMonthsGridContent(Date selectedMonth) {
        CellFormatter cellFormatter = monthGrid.getCellFormatter();
        Date cursor = getMonthsGridOrigin(selectedMonth);

        Date month = new Date(selectedMonth.getDate());
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                String format = monthOnlyFormat.format(month);
                monthGrid.setText(i, j,
                        LocaleInfo.getCurrentLocale() != null && LocaleInfo.getCurrentLocale().getLocaleName() != null
                                && LocaleInfo.getCurrentLocale().getLocaleName().equalsIgnoreCase("ru")
                                ? format.substring(0, Math.min(3, format.length())) + "."
                                : format + ".");

                cellFormatter.removeStyleName(i, j, theme + "-" + "selected");
                cellFormatter.removeStyleName(i, j, theme + "-"
                        + "current-month-selected");
                cellFormatter.removeStyleName(i, j, theme + "-" + "other-month");
                cellFormatter.removeStyleName(i, j, theme + "-"
                        + "current-month-other-day");
                cellFormatter.removeStyleName(i, j, theme + "-" + "current-day");
                if (DateUtil.areOnTheSameDay(datePicker.getCurrentDate(), cursor)) {
                    cellFormatter.addStyleName(i, j, theme + "-" + "current-day");
                    cellFormatter.getElement(i, j).setAttribute("id", "datePicker-currentDay");
                }

                cursor = DateUtil.addMonths(cursor, 1);
                month = DateUtil.addMonths(month, 1);

            }
        }
    }

    /**
     * Draw 12 years starting with 2000 + 12n
     *
     * @param selectedYear year orienting on which grid is drawn
     */
    private void drawYearsGridContent(Date selectedYear) {
        CellFormatter cellFormatter = yearsGrid.getCellFormatter();
        Date year = new Date(selectedYear.getTime());
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                yearsGrid.setText(i, j, yearNumberFormat.format(year));
                cellFormatter.getElement(i, j).getStyle().setWidth(62, Style.Unit.PX);
                year.setYear(year.getYear() + 1);
            }
        }
    }

    /**
     * Change the displayed month.
     *
     * @param i Number of month to add to the displayed month
     */
    protected void changeMonth(int i) {
        this.displayedMonth = DateUtil.addMonths(this.displayedMonth, i);
        this.displayMonth();
    }

    /**
     * Return the first day to display. If the month first day is after the 5th
     * day of the week, it return the first day of the week. Else, it returns
     * the first day of the week before.
     *
     * @param displayedMonth - d
     * @return The first day to display in the grid
     */
    private Date getDaysGridOrigin(Date displayedMonth) {
        int currentYear = displayedMonth.getYear();
        int currentMonth = displayedMonth.getMonth();
        CellFormatter cfJours = daysGrid.getCellFormatter();
        Date monthFirstDay = new Date(currentYear, currentMonth, 1);
        int indice = DateUtil.getWeekDayIndex(monthFirstDay);
        Date origineTableau;
        String overallDatePickerWeekStartT = TIMESHEET_WEEK_START.equals(useCustomWeekStart) ? Utils.userSettings.get(TIMESHEET_WEEK_START) : Utils.userSettings.get(OVERALL_DATE_PICKER_WEEK_START);
        if (overallDatePickerWeekStartT == null || "null".equals(overallDatePickerWeekStartT) || "".equals(overallDatePickerWeekStartT)) {
            overallDatePickerWeekStartT = "1";
        }
        if (indice > 4) {
            origineTableau = DateUtil.getWeekFirstDay(monthFirstDay, Integer.valueOf(overallDatePickerWeekStartT) - 1);
        } else {
            origineTableau = DateUtil.getWeekFirstDay(DateUtil.addDays(monthFirstDay, -7), Integer.valueOf(overallDatePickerWeekStartT) - 1);
        }
        return origineTableau;
    }

    private Date getMonthsGridOrigin(Date displayedMonth) {
        Date date = new Date(displayedMonth.getTime());
        date.setMonth(0);
        return date;
    }

    private Date getYearsGridOrigin(Date displayedMonth) {
        int currentYear = displayedMonth.getYear();
        for (int i = 0; i < 12; i++) {
            if ((currentYear - i + 8) % 12 == 0) {
                currentYear -= i;
                break;
            }
        }
        Date result = new Date(displayedMonth.getTime());
        result.setYear(currentYear);
        return result;
    }
}
