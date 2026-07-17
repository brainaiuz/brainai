package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.monthview;

import com.allen_sauer.gwt.dnd.client.*;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.hijri.HijriUtils;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.CalendarSettings;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.CalendarView;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.CalendarWidget;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.*;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.util.FormattingUtil;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

/**
 * <p>A CalendarView that displays appointments for a given month. The Month is
 * displayed in a grid-style view where cells represents days, columns
 * represents days of the week (i.e. Monday, Tuesday, etc.) and rows represent a
 * full week (Sunday through Saturday).<p/>
 * <p/>
 * <h3>CSS Style Rules</h3>
 * <ul class='css'>
 * <li>.gwt-cal-MonthView { }</li>
 * <li>.dayCell { cell that represents a day }</li>
 * <li>.dayCell-today { cell that represents today }</li>
 * <li>.dayCell-disabled { cell's day falls outside the month }</li>
 * <li>.dayCell-today-disabled { cell represents today, falls outside the month }</li> <li>.dayCellLabel { header for the cell }</li>
 * <li>.dayCellLabel-today { cell represents today }</li>
 * <li>.dayCellLabel-disabled { cell's day falls outside the month }</li>
 * <li>.dayCellLabel-today-disabled { cell represents today, falls outside the month }</li>
 * <li>.weekDayLabel { label for the days of the week }</li>
 * </ul>
 */
public class MonthView extends CalendarView implements Constants {

    public static final Comparator<Appointment> APPOINTMENT_COMPARATOR = (a1, a2) -> {
        int compare = Boolean.compare(a2.isMultiDay(), a1.isMultiDay());
        if (compare == 0) {
            compare = a1.getStartDate().compareTo(a2.getStartDate());
        }

        if (compare == 0) {
            compare = a2.getEndDate().compareTo(a1.getEndDate());
        }

        return compare;
    };

    private final int DAYS_IN_A_WEEK = 7;
    private final String MONTH_VIEW = "gwt-cal-MonthView";
    private final String CANVAS_STYLE = "canvas";
    private final String GRID_STYLE = "grid";
    private final String CELL_STYLE = "dayCell";
    private final String MORE_LABEL_STYLE = "moreAppointments";
    private final String CELL_HEADER_STYLE = "dayCellLabel";
    private final String WEEKDAY_LABEL_STYLE = "weekDayLabel";

    private ShortAppointmentView appointmentView;

    /**
     * List of appointment panels drawn on the month view canvas.
     */
    private ArrayList<AppointmentWidget> appointmentsWidgets = new ArrayList<>();

    /**
     * All appointments are placed on this canvas and arranged.
     */
    private AbsolutePanel appointmentCanvas = new AbsolutePanel();

    /**
     * All appointments of each day of month are stored and each date gets appointment list related to that day.
     */
    private HashMap<Date, ArrayList<Appointment>> appointmentsPerDay = new HashMap<>();

    /**
     * The first date displayed on the MonthView (1st cell.) This date is not
     * necessarily the first date of the month as the month view will sometimes
     * display days from the adjacent months because of the number of days
     * fitting in the visible grid.
     */
    private Date firstDateDisplayed;

    private int height = 2;

    private int width = 20;

    /**
     * Grid that makes up the days and weeks of the MonthView.
     */
    private FlexTable monthCalendarGrid = new FlexTable();

    /**
     * The number of rows required to display the entire month in grid format.
     * Although most months span a total of five weeks, there are some months
     * that span six weeks.
     */
    private int monthViewRequiredRows = 5;

    //HERE ARE A BUNCH OF CALCULATED VALUES THAT ARE USED DURING LAYOUT
    // NOT SURE IF THE VARIABLES SHOULD BE KEPT AT THE CLASS LEVEL
    // OR AT THE METHOD LEVEL, WE WILL SEE LATER.

    private int calculatedWeekDayHeaderHeight;
    private int calculatedDayHeaderHeight;

    /**
     * Maximum appointments per cell (day).
     */
    private int calculatedCellAppointments;

    /**
     * Height of each Cell (day), including the day's header.
     */
    private float calculatedCellOffsetHeight;

    /**
     * Height of each Cell (day), excluding the day's header.
     */
    private float calculatedCellHeight;

    /**
     * List of AppointmentAdapter objects that represent the currently selected
     * appointment.
     */
    private ArrayList<AppointmentWidget> selectedAppointmentAdapters = new ArrayList<>();

    private PickupDragController dragController;

    private MonthViewDropController monthViewDropController = null;

    /**
     * This method is called when the MonthView is attached to the Calendar and
     * displayed. This is where all components are configured and added to the
     * RootPanel.
     */
    public void attach(CalendarWidget widget) {
        super.attach(widget);

        calendarWidget.addToRootPanel(monthCalendarGrid);
        monthCalendarGrid.setCellPadding(0);
        monthCalendarGrid.setBorderWidth(0);
        monthCalendarGrid.setCellSpacing(0);
        monthCalendarGrid.setStyleName(GRID_STYLE);

        calendarWidget.addToRootPanel(appointmentCanvas);
        appointmentCanvas.setStyleName(CANVAS_STYLE);

        selectedAppointmentAdapters.clear();

        if (dragController == null) {
            dragController = new PickupDragController(appointmentCanvas, true);
            dragController.addDragHandler(new DragHandler() {
                public void onDragEnd(DragEndEvent event) {
                    RootPanel.getBodyElement().removeClassName("has-dnd-drag");
                    Integer userId = Integer.valueOf(Cookies.getCookie(CommandConstants.GOOGLE_CALENDAR_OWNER_ID_COOKIE));
                    if (Utils.getUserID().equals(userId)) {
                        Appointment appointment = ((AppointmentWidget) event.getContext().draggable).getAppointment();
                        calendarWidget.setCommittedAppointment(appointment);
                        calendarWidget.fireUpdateEvent(appointment);
                    }
                }

                public void onDragStart(DragStartEvent event) {
                    RootPanel.getBodyElement().addClassName("has-dnd-drag");
                    Integer userId = Integer.valueOf(Cookies.getCookie(CommandConstants.GOOGLE_CALENDAR_OWNER_ID_COOKIE));
                    if (Utils.getUserID().equals(userId)) {
                        calendarWidget.setRollbackAppointment(((AppointmentWidget) event.getContext().draggable).getAppointment().clone());
                    }
                }

                public void onPreviewDragEnd(DragEndEvent event) throws VetoDragException {
                    //do nothing
                }

                public void onPreviewDragStart(DragStartEvent event) throws VetoDragException {
                    //do nothing
                }
            });
        }

        /*
         * Need to re-set appointmentCanvas to position:absolute because gwt-dnd
         * will set it to relative, but then the layout gets f***ed up
         */
        DOM.setStyleAttribute(appointmentCanvas.getElement(), "position", "absolute");

        dragController.setBehaviorDragStartSensitivity(5);
        dragController.setBehaviorDragProxy(true);

        // instantiate our drop controller
        monthViewDropController = new MonthViewDropController(appointmentCanvas, monthCalendarGrid);
        dragController.registerDropController(monthViewDropController);
    }

    /**
     * Performs a Layout and arranges all appointments on the MonthView's
     * appointment canvas.
     */
    @Override
    public void doLayout() {
        //Clear all existing appointments
        appointmentCanvas.clear();
        monthCalendarGrid.clear();
        appointmentsPerDay.clear();
        appointmentsWidgets.clear();
        selectedAppointmentAdapters.clear();

        while (monthCalendarGrid.getRowCount() > 0) {
            monthCalendarGrid.removeRow(0);
        }
//        if (!isWideScreen()) {
//            setHeight((Window.getClientHeight()) + "px");
//        }
        //Rebuild the month grid
        buildCalendarGrid();

        //(Re)calculate some variables
        calculateCellHeight();
        calculateCellAppointments();

        //set variables needed by the drop controller
        //monthViewDropController.setDayHeaderHeight(calculatedDayHeaderHeight);
        monthViewDropController.setDaysPerWeek(DAYS_IN_A_WEEK);
        //monthViewDropController.setWeekdayHeaderHeight(calculatedWeekDayHeaderHeight);
        monthViewDropController.setWeeksPerMonth(monthViewRequiredRows);
        monthViewDropController.setFirstDateDisplayed(firstDateDisplayed);

        //Sort the appointments
        calendarWidget.getAppointments().sort(APPOINTMENT_COMPARATOR);

        // Send appointments to layout manager
        MonthLayoutDescription monthLayoutDescription = new MonthLayoutDescription(firstDateDisplayed, calendarWidget.getAppointments());

        // Get the layouts for each week in the month
        WeekLayoutDescription[] weeks = monthLayoutDescription.getWeekDescriptions();
        if (isWideScreen()) {
            int h = height * (DAYS_IN_A_WEEK) * (Appointment.DEFAULT_HEIGHT + 5) + 200;
            if ((Window.getClientHeight() - 176) < h) {
                setHeight(h + "px");
            }
            if (width > 20) {
                setWidthCalendar(width * DAYS_IN_A_WEEK * 7.5 + 30 + "px");
            }
            calculateCellHeight();
            calculateCellAppointments();
        }
        for (int weekOfMonth = 0; weekOfMonth < weeks.length && weekOfMonth < monthViewRequiredRows; weekOfMonth++) {
            WeekLayoutDescription weekDescription = weeks[weekOfMonth];

            if (weekDescription != null) {
                AppointmentStackingManager topAppointmentManager = weekDescription.getTopAppointmentsManager();
                layOnTopOfTheWeekHangingAppointments(topAppointmentManager, weekOfMonth);
                layOnWeekDaysAppointments(weekDescription, weekOfMonth);
            }
        }
    }

    /**
     * This method is responsible only for multi day appointments.
     *
     * @param weekTopElements
     * @param weekOfMonth
     */
    private void layOnTopOfTheWeekHangingAppointments(AppointmentStackingManager weekTopElements, int weekOfMonth) {
        int appointmentsCount = weekTopElements.getDescriptions().size();

        for (int layer = 0; layer < appointmentsCount; layer++) {
            ArrayList<AppointmentLayoutDescription> descriptionsInLayer = weekTopElements.getDescriptionsInLayer(layer);

            if (descriptionsInLayer == null) {
                return;
            }

            for (AppointmentLayoutDescription weekTopElement : descriptionsInLayer) {
                Appointment appointment = weekTopElement.getAppointment();

                int weekStartDay = weekTopElement.getWeekStartDay();
                int weekEndDay = weekTopElement.getWeekEndDay();

                if (weekStartDay < 0) {
                    weekStartDay = 0;
                }

                /**
                 * We are storing all day related appointments according the day index that gets us the date.
                 */
                for (int dayOfWeek = weekStartDay; dayOfWeek <= weekEndDay; dayOfWeek++) {
                    ArrayList<Appointment> appointments = getDailyAppointments(weekOfMonth, dayOfWeek);
                    if (!appointments.contains(appointment)) {
                        appointments.add(appointment);
                    }
                }

                /**
                 * If it exceeds allowed number of appointments, we shouldn't draw it.
                 */

                if (isWideScreen() || !exceedMaxAppointmentsPerCell(0, layer)) {
                    layOnAppointment(appointment, weekStartDay, weekEndDay, weekOfMonth, layer);
                }
            }
        }
    }

    private ArrayList<Appointment> getDailyAppointments(int weekOfMonth, int dayOfWeek) {
        Date date = determineDate(weekOfMonth, dayOfWeek);

        if (!appointmentsPerDay.containsKey(date)) {
            appointmentsPerDay.put(date, new ArrayList<>());
        }

        return appointmentsPerDay.get(date);
    }

    private Date determineDate(int weekOfMonth, int dayOfWeek) {
        int days = weekOfMonth * DAYS_IN_A_WEEK + dayOfWeek;
        return DateUtil.addDays(firstDateDisplayed, days);
    }

    private boolean exceedMaxAppointmentsPerCell(int maxMultiDay, int dayIndex) {
        return maxMultiDay + dayIndex >= calculatedCellAppointments;
    }

    /**
     * This method is responsible only for daily or all day appointments.
     *
     * @param week
     * @param weekOfMonth
     */
    private void layOnWeekDaysAppointments(WeekLayoutDescription week, int weekOfMonth) {
        AppointmentStackingManager topAppointmentManager = week.getTopAppointmentsManager();

        for (int dayOfWeek = 0; dayOfWeek < DAYS_IN_A_WEEK; dayOfWeek++) {
            DayLayoutDescription dayAppointments = week.getDayLayoutDescription(dayOfWeek);

            if (dayAppointments != null) {
                int maxMultiDay = topAppointmentManager.singleDayLowestOrder(dayOfWeek);
                int count = dayAppointments.getAppointments().size();
                int allCount = count + maxMultiDay;

                if (allCount > height) {
                    height = allCount;
                }

                for (int i = 0; i < count; i++) {
                    if (!isWideScreen() && exceedMaxAppointmentsPerCell(maxMultiDay, i)) {
                        ArrayList<Appointment> appointments = getDailyAppointments(weekOfMonth, dayOfWeek);

                        for (Appointment dayAppointment : dayAppointments.getAppointments()) {
                            if (!appointments.contains(dayAppointment)) {
                                appointments.add(dayAppointment);
                            }
                        }

                        putMoreLink(appointments, weekOfMonth, dayOfWeek);
                        break;
                    }

                    Appointment appointment = dayAppointments.getAppointments().get(i);

                    if (appointment != null && appointment.getSubject() != null && appointment.getSubject().length() > width) {
                        width = appointment.getSubject().length();
                        if (isWideScreen()) {
                            setWidthCalendar(width * DAYS_IN_A_WEEK * 7.5 + 30 + "px");
                        }
                    }

                    layOnAppointment(appointment, dayOfWeek, dayOfWeek, weekOfMonth, i + maxMultiDay);
                }
            } else {
                ArrayList<Appointment> appointments = getDailyAppointments(weekOfMonth, dayOfWeek);
                if (appointments.size() > height) {
                    height = appointments.size();
                }

                if (!isWideScreen() && exceedMaxAppointmentsPerCell(appointments.size(), -1)) {
                    putMoreLink(appointments, weekOfMonth, dayOfWeek);
                }
            }
        }

    }

    private void putMoreLink(ArrayList<Appointment> appointments, int weekOfMonth, int dayOfWeek) {
        final Link more = new Link("+" + (appointments.size() - calculatedCellAppointments) + " more");
        more.setStyleName(MORE_LABEL_STYLE);
        more.setAppointments(appointments);
        more.setDate(determineDate(weekOfMonth, dayOfWeek));
        /*more.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                onClickLink(more);
            }
        });*/

        placeItemInGrid(more, dayOfWeek, dayOfWeek, weekOfMonth, calculatedCellAppointments);
        appointmentCanvas.add(more);
    }

    private void layOnAppointment(final Appointment appointment, int colStart, int colEnd, int row, int cellPosition) {
        AppointmentWidget panel = new AppointmentWidget(appointment);
        placeItemInGrid(panel, colStart, colEnd, row, cellPosition);

        if (appointment.isMultiDay() || appointment.isAllDay()) {
            panel.setStyleName("multiDayAppointment");
        } else {
            panel.setStyleName("appointment");
        }
        panel.addStyleName(appointment.getStyle());

        if (appointment.isEditable() && !isPublic()) {
            dragController.makeDraggable(panel);
        }

        if (calendarWidget.isTheSelectedAppointment(appointment)) {
            panel.addStyleName("selected");
            selectedAppointmentAdapters.add(panel);
        }
        appointmentsWidgets.add(panel);
        appointmentCanvas.add(panel);
    }

    /**
     * Gets the Month View's primary style name.
     */
    public String getStyleName() {
        return MONTH_VIEW;
    }

    /**
     * Handles the DoubleClick event to determine if an Appointment has been
     * selected. If an appointment has been double clicked the OpenEvent will
     * get fired for that appointment.
     */
    public void onDoubleClick(Element clickedElement, Event event) {
        ArrayList<AppointmentWidget> list = findAppointmentWidgetsByElement(clickedElement);
        if (!list.isEmpty()) {
            Appointment appointment = list.get(0).getAppointment();
            //If an appointment is editable, we are opening it on double click.
            if (appointment.isEditable() && !isPublic()) {
                ShortAppointmentView appointmentView = calendarWidget.initShortAppointmentView(appointment);
                appointmentView.center();
            }
            calendarWidget.fireOpenEvent(appointment);
        }
    }

    /**
     * Handles the a single click to determine if an appointment has been
     * selected. If an appointment is clicked it's selected status will be set
     * to true and a SelectionEvent will be fired.
     */
    @Override
    public void onSingleClick(Element clickedElement, Event event) {
        Appointment appointment = findAppointmentByElement(clickedElement);
        if (isPublic()) {
            if (appointment != null) {
                if (appointment.isEditable()) {
                    PublicShortAppointmentView appointmentView = calendarWidget.initPublicShortAppointmentView(appointment, isBookable());
                    appointmentView.center();
                    calendarWidget.fireOpenEvent(appointment);
                }
            }
        } else {
            Integer userId = Integer.valueOf(Cookies.getCookie(CommandConstants.GOOGLE_CALENDAR_OWNER_ID_COOKIE));
            if (Utils.getUserID().equals(userId) || Utils.hasRole(Constants.CALENDAR_EDITOR)) {
                Link link;
                //For monthly editable events it will be drawn on double click on them, therefore only not editable events will be shown on single click.
                if (appointment != null) {
                    if (!appointment.isEditable()) {
                        selectAppointment(appointment);
                    }
                } else if ((link = findLinkByElement(clickedElement)) != null) {
                    onClickLink(link);
                } else if (calendarWidget.getSettings().getTimeBlockClickNumber() == CalendarSettings.Click.Single && !isOutOfCell(event)) {
                    int x = (Utils.isIE() || Utils.getUserAgent().contains("chrome")) ? (DOM.eventGetClientX(event) + Window.getScrollLeft()) : DOM.eventGetClientX(event);
                    int y = (Utils.isIE() || Utils.getUserAgent().contains("chrome")) ? (DOM.eventGetClientY(event) + Window.getScrollTop()) : DOM.eventGetClientY(event);
                    timeBlockClick(x, y);
                }
            }
        }
    }

    private void timeBlockClick(int x, int y) {
        int left = monthCalendarGrid.getAbsoluteLeft();
        int top = monthCalendarGrid.getAbsoluteTop() + calculatedWeekDayHeaderHeight;
        double width = (double) monthCalendarGrid.getOffsetWidth() / (double) DAYS_IN_A_WEEK;

        // x & y are based on screen position, need to get x / y relative to component
        int relativeX = x - left;
        int relativeY = y - top;

        // find the interval clicked and day clicked
        double dayOfWeek = Math.floor((double) relativeX / width);
        double weekOfMonth = Math.floor((double) relativeY / (double) calculatedCellOffsetHeight);

        // create new appointment date based on click
        Date newStartDate = DateUtil.addDays(firstDateDisplayed, (int) (dayOfWeek + weekOfMonth * DAYS_IN_A_WEEK));

        float leftPosition = (float) left + (float) dayOfWeek * (float) width;
        float topPosition = (float) top + (float) weekOfMonth * calculatedCellOffsetHeight;

        final AppointmentWidget panel = new AppointmentWidget(leftPosition, topPosition, (float) width, calculatedCellOffsetHeight);
        panel.addStyleName("monthly");
        appointmentCanvas.add(panel);

        Appointment appointment = new Appointment();
        appointment.setAllDay(true);
        appointment.setStartDate(newStartDate);
        Date endDate = DateUtil.addMinutes(newStartDate, 30);
        appointment.setEndDate(endDate);
        appointment.setAllDay(true);

        calendarWidget.fireTimeBlockClickEvent(null, newStartDate, appointment, (int) leftPosition, (int) topPosition);
    }

    private void onClickLink(Link link) {
        int left = link.getAbsoluteLeft();
        int top = link.getAbsoluteTop();
        AppointmentListView apptListView = new AppointmentListView(link.getAppointments(), link.getDate(), left, top);
        apptListView.onUpdateAppointment(new SaveAppointmentHandler() {
            public void onSaveOrUpdate(Appointment appointment) {
                calendarWidget.fireSaveEvent(appointment);
            }

            public void onSaveOrUpdateTask(Appointment appointment) {
                calendarWidget.fireSaveEvent(appointment);
            }
        });
        apptListView.onDeleteAppointment(new DeleteAppointmentHandler() {
            public void onDelete(Appointment appointment) {
                calendarWidget.fireDeleteEvent(appointment);
            }

            public void onDeleteTask(TaskSingleItem taskSingleItem) {

            }
        });
    }

    private boolean isOutOfCell(Event event) {
        int positionY = DOM.eventGetClientY(event);
        return positionY < monthCalendarGrid.getAbsoluteTop() + calculatedWeekDayHeaderHeight;
    }

    private ArrayList<AppointmentWidget> findAppointmentWidgetsByElement(Element element) {
        return findAppointmentWidgets(findAppointmentByElement(element));
    }

    /**
     * Builds and formats the Calendar Grid. No appointments are included when
     * building the grid.
     */
    @SuppressWarnings("deprecation")
    private void buildCalendarGrid() {
        int weekFirstDay = Integer.valueOf(Utils.userSettings.get(OVERALL_DATE_PICKER_WEEK_START));
        Date date = calendarWidget.getDate();
        date.setDate(1);

        int month = date.getMonth();

        //get the month's first date to display
        date = DateUtil.getWeekFirstDay(date, weekFirstDay - 1);

        firstDateDisplayed = DateUtil.safeDate((Date) date.clone());

        Date today = new Date();
        DateUtil.resetTime(today);

        String[] weekHeaders = CalendarSettings.DAY_LIST_SUNDAY;
        switch (weekFirstDay) {
            case 2:
                weekHeaders = CalendarSettings.DAY_LIST_MONDAY;
                break;
            case 7:
                weekHeaders = CalendarSettings.DAY_LIST_SATURDAY;
                break;
        }
        /* Add the calendar weekday heading */
        for (int i = 0; i < DAYS_IN_A_WEEK; i++) {
            monthCalendarGrid.setText(0, i, weekHeaders[i]);
            monthCalendarGrid.getCellFormatter().setVerticalAlignment(0, i, HasVerticalAlignment.ALIGN_TOP);
            monthCalendarGrid.getCellFormatter().setStyleName(0, i, WEEKDAY_LABEL_STYLE);//its height is '20px' and it is set in css.
        }

        monthViewRequiredRows = MonthViewHelper.monthViewRequiredRows(calendarWidget.getDate(), weekFirstDay);
        for (int monthGridRowIndex = 1; monthGridRowIndex <= monthViewRequiredRows; monthGridRowIndex++) {
            for (int dayOfWeekIndex = 0; dayOfWeekIndex < DAYS_IN_A_WEEK; dayOfWeekIndex++) {
                if (monthGridRowIndex != 1 || dayOfWeekIndex != 0) {
                    date.setDate(date.getDate() + 1);
                }
                configureDayInGrid(monthGridRowIndex, dayOfWeekIndex, String.valueOf(date.getDate()) + HijriUtils.getCalendarHijriDate(date), date.equals(today), date.getMonth() != month);
            }
        }
    }

    /**
     * Configures a single day in the month grid of this
     * <code>MonthView</code>.
     *
     * @param row               The row in the grid on which the day will be set
     * @param col               The col in the grid on which the day will be set
     * @param text              The heading in the day cell, i.e. the day number
     * @param isToday           Indicates whether the day corresponds to today in the month view
     * @param notInCurrentMonth Indicates whether the day is in the current visualized month or belongs
     *                          to any of the two adjacent months of the current month
     */
    private void configureDayInGrid(int row, int col, String text, boolean isToday, boolean notInCurrentMonth) {
        Label label = new Label(text);

        StringBuilder headerStyle = new StringBuilder(CELL_HEADER_STYLE);
        StringBuilder cellStyle = new StringBuilder(CELL_STYLE);
        if (isToday) {
            cellStyle.append(" dayCell-today");
        }

        if (notInCurrentMonth) {
            cellStyle.append(" dayCell-disabled");
        }

        label.setStyleName(headerStyle.toString());

        monthCalendarGrid.setWidget(row, col, label);
        monthCalendarGrid.getCellFormatter().setVerticalAlignment(row, col, HasVerticalAlignment.ALIGN_TOP);
        monthCalendarGrid.getCellFormatter().setStyleName(row, col, cellStyle.toString());
    }

    /**
     * Returns the {@link Appointment} indirectly associated to the passed
     * <code>element</code>. Each Appointment drawn on the CalendarView maps to
     * a Widget and therefore an Element. This method attempts to find an
     * Appointment based on the provided Element. If no match is found a null
     * value is returned.
     *
     * @param element Element to look up.
     * @return Appointment matching the element.
     */
    private Appointment findAppointmentByElement(Element element) {
        Appointment appointmentAtElement = null;
        for (AppointmentWidget widget : appointmentsWidgets) {
            if (DOM.isOrHasChild(widget.getElement(), element)) {
                appointmentAtElement = widget.getAppointment();
                break;
            }
        }
        return appointmentAtElement;
    }

    private Link findLinkByElement(Element element) {
        for (int i = 0; i < appointmentCanvas.getWidgetCount(); i++) {
            Widget widget = appointmentCanvas.getWidget(i);
            if (DOM.isOrHasChild(widget.getElement(), element) && widget instanceof Link) {
                return (Link) widget;
            }
        }
        return null;
    }

    /**
     * Finds any related adapters that match the given Appointment.
     *
     * @param appt Appointment to match.
     * @return List of related AppointmentWidget objects.
     */
    private ArrayList<AppointmentWidget> findAppointmentWidgets(Appointment appt) {
        ArrayList<AppointmentWidget> appointmentAdapters = new ArrayList<>();
        if (appt != null) {
            for (AppointmentWidget widget : appointmentsWidgets) {
                if (widget.getAppointment().equals(appt)) {
                    appointmentAdapters.add(widget);
                }
            }
        }
        return appointmentAdapters;
    }

    public void onDeleteKeyPressed() {
        if (calendarWidget.getSelectedAppointment() != null) {
            calendarWidget.fireDeleteEvent(calendarWidget.getSelectedAppointment());
        }
    }

    @Override
    public void onAppointmentSelected(Appointment appointment) {
        ArrayList<AppointmentWidget> clickedAppointmentAdapters = findAppointmentWidgets(appointment);

        if (!clickedAppointmentAdapters.isEmpty()) {
            for (AppointmentWidget adapter : selectedAppointmentAdapters) {
                adapter.removeStyleName("selected");
            }

            for (AppointmentWidget adapter : clickedAppointmentAdapters) {
                adapter.addStyleName("selected");
            }

            selectedAppointmentAdapters.clear();
            selectedAppointmentAdapters = clickedAppointmentAdapters;
        }
    }

    /**
     * Calculates the height of each day cell in the Month grid. It excludes the
     * height of each day's header, as well as the overall header that shows the
     * weekday labels.
     *
     * @return
     */
    private void calculateCellHeight() {
        int gridHeight = monthCalendarGrid.getOffsetHeight();
        int weekdayRowHeight = monthCalendarGrid.getRowFormatter().getElement(0).getOffsetHeight();
        int dayHeaderHeight = monthCalendarGrid.getFlexCellFormatter().getElement(1, 0).getFirstChildElement().getOffsetHeight();

        calculatedCellOffsetHeight = (float) (gridHeight - weekdayRowHeight) / monthViewRequiredRows;
        calculatedCellHeight = calculatedCellOffsetHeight - dayHeaderHeight;
        calculatedWeekDayHeaderHeight = weekdayRowHeight;
        calculatedDayHeaderHeight = dayHeaderHeight;
    }

    /**
     * Calculates the maximum number of appointments that can be displayed in a
     * given "day cell".
     */
    private void calculateCellAppointments() {
        int apptPaddingTop = 1 + (Math.abs(FormattingUtil.getBorderOffset()) * 3);
        int apptHeight = Appointment.DEFAULT_HEIGHT;

        calculatedCellAppointments = (int) Math.floor((calculatedCellHeight - apptPaddingTop) / (float) (apptHeight + apptPaddingTop)) - 1;
    }

    private void placeItemInGrid(Widget panel, int colStart, int colEnd, int row, int cellPosition) {
        int apptPaddingTop = 1 + (Math.abs(FormattingUtil.getBorderOffset()) * 3);
        int apptHeight = Appointment.DEFAULT_HEIGHT;

        float left = (float) colStart / (float) DAYS_IN_A_WEEK * 100f + .5f;

        float width = ((float) (colEnd - colStart + 1) / (float) DAYS_IN_A_WEEK) * 100f - 1f;

        float top = calculatedWeekDayHeaderHeight + (row * calculatedCellOffsetHeight) +
                calculatedDayHeaderHeight + apptPaddingTop + (cellPosition * (apptHeight + apptPaddingTop));

        DOM.setStyleAttribute(panel.getElement(), "position", "absolute");
        DOM.setStyleAttribute(panel.getElement(), "top", top + "px");
        DOM.setStyleAttribute(panel.getElement(), "left", left + "%");
        DOM.setStyleAttribute(panel.getElement(), "width", width + "%");
    }

    /**
     * We need extra class that stores all appointments with date.
     * Date allows us to find out those appointments related to date.
     */
    private class Link extends Label {

        private ArrayList<Appointment> appointments;

        private Date date;

        public Link(String text) {
            super(text);
        }

        public ArrayList<Appointment> getAppointments() {
            return appointments;
        }

        public void setAppointments(ArrayList<Appointment> appointments) {
            this.appointments = appointments;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }
}
