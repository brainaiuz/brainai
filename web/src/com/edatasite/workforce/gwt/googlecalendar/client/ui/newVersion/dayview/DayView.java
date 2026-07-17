package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.dayview;

import com.allen_sauer.gwt.dnd.client.DragEndEvent;
import com.allen_sauer.gwt.dnd.client.DragHandler;
import com.allen_sauer.gwt.dnd.client.DragStartEvent;
import com.allen_sauer.gwt.dnd.client.PickupDragController;
import com.allen_sauer.gwt.dnd.client.VetoDragException;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.CalendarSettings;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.CalendarView;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.CalendarWidget;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.HasSettings;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.AppointmentListView;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.DeleteAppointmentHandler;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.PublicShortAppointmentView;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.SaveAppointmentHandler;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.ShortAppointmentView;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.util.AppointmentUtil;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DayView extends CalendarView implements HasSettings {
    private final WfmStrings wfmStrings = WfmStrings.App.get();
    private DayViewHeader dayViewHeader;
    private DayViewBody dayViewBody;
    private DayViewMultiDayBody multiViewBody;
    private DayViewLayoutStrategy layoutStrategy;
    private PickupDragController dragController;

//    private List<AppointmentWidget> appointmentWidgets = new ArrayList<AppointmentWidget>();
    /**
     * All appointments are placed on this canvas and arranged.
     */
    private AbsolutePanel absolutePanel = new AbsolutePanel();


    private List<AppointmentDragWidget> appointmentWidgets = new ArrayList<>();

//    private AbsolutePositionDropController dayViewDropController = null;

    private DayViewDropController dayViewDropController = null;
    /**
     * List of AppointmentAdapter objects that represent the currently selected
     * appointment.
     */
    private ArrayList<AppointmentDragWidget> selectedAppointmentWidgets = new ArrayList<>();

    public DayView() {

    }

    public void doLayout() {
        // PERFORM APPOINTMENT LAYOUT NOW
        Date d = (Date) calendarWidget.getDate().clone();

        multiViewBody.setDays(calendarWidget.getDays());
        dayViewHeader.setDays((Date) d.clone(), calendarWidget.getDays());
        dayViewHeader.setYear((Date) d.clone());
        dayViewBody.setDays((Date) d.clone(), calendarWidget.getDays());
        dayViewBody.getTimeline().prepare();

        // LAYOUT THE VIEW NOW
        // view.doSizing(this);
        // doSizing(widget);

        this.selectedAppointmentWidgets.clear();
        appointmentWidgets.clear();
        // widgetAppointmentIndex.clear();
        // appointmentWidgetIndex.clear();

        // PROBLEMS ARE
        // 1) Month View may actually show different dates,
        // such as days before the 1st and after the last
        // day of month. Push this to the "VIEW"?????
        // 2) This works great for Absolute Positioning, such as the
        // DayView and MonthView, but not so well for the
        // ListView which is relative positioning... how to handle???
        // 3) How to handle scenarios where multiple appointments are "grouped"
        // such as a MS Outlook Resource / Booking view?????
        // Or is this a separate widget all together???
        // 4) How to handle scenarios where appointments need to be grouped by
        // the calendar??

        // HERE IS WHERE WE DO THE LAYOUT
        Date tmpDate = (Date) calendarWidget.getDate().clone();

        for (int i = 0; i < calendarWidget.getDays(); i++) {
            ArrayList<Appointment> filteredList = AppointmentUtil.filterListByDate(calendarWidget.getAppointments(), tmpDate);
            // perform layout
            ArrayList<AppointmentAdapter> appointmentAdapters = layoutStrategy.doLayout(filteredList, i, calendarWidget.getDays());

            // add all appointments back to the grid
            // CHANGE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            for (AppointmentAdapter appt : appointmentAdapters) {
                if (!appt.getAppointment().isNoTask() || calendarWidget.getDays() == 1) {
                    AppointmentDragWidget panel = new AppointmentDragWidget(appt.getAppointment());
                    if (appt.getAppointment().getStyle().contains("#")) {
                        panel.getElement().getStyle().setBackgroundColor(appt.getAppointment().getStyle());
                        panel.getElement().getStyle().setBorderColor(appt.getAppointment().getBorder());
                        panel.getElement().getStyle().setBorderWidth(1, Style.Unit.PX);
                        panel.getElement().getStyle().setBorderStyle(Style.BorderStyle.SOLID);
                    } else if (appt.getAppointment().isNoTask()) {
                        panel.getElement().getStyle().setBorderColor(appt.getAppointment().getBorder());
                        panel.getElement().getStyle().setBorderWidth(1, Style.Unit.PX);
                        panel.getElement().getStyle().setBorderStyle(Style.BorderStyle.SOLID);
                    } else {
                        panel.addStyleName(appt.getAppointment().getStyle());
                    }
                    DOM.setStyleAttribute(panel.getElement(), "position", "absolute");
                    panel.setWidth(appt.getWidth());
                    panel.setHeight(appt.getHeight());
                    panel.setTop(appt.getTop());
                    panel.setLeft(appt.getLeft());

                    dayViewBody.getGrid().grid.add(panel);
                    if (!appt.getAppointment().isNoTask()) {
                        dragController.makeDraggable(panel);
                    }

                    if (calendarWidget.isTheSelectedAppointment(panel.getAppointment())) {
                        panel.addStyleName("selected");
                        selectedAppointmentWidgets.add(panel);
                    }
                    appointmentWidgets.add(panel);
                }
            }

            tmpDate = DateUtil.addDays(tmpDate, 1);
        }

        ArrayList<Appointment> filteredList = AppointmentUtil.filterListByDateRange(calendarWidget.getAppointments(), calendarWidget.getDate(), calendarWidget.getDays());

        ArrayList<AppointmentAdapter> adapterList = new ArrayList<>();
        int desiredHeight = layoutStrategy.doMultiDayLayout(filteredList, adapterList, calendarWidget.getDate(), calendarWidget.getDays());

        multiViewBody.grid.setHeight(desiredHeight + "px");
        multiViewBody.determineScrollBarWidth(desiredHeight);

        for (AppointmentAdapter appt : adapterList) {
            if (!appt.getAppointment().isNoTask() || calendarWidget.getDays() == 1) {
                AppointmentDragWidget panel = new AppointmentDragWidget(appt.getAppointment());
                if (appt.getAppointment().getStyle().contains("#")) {
                    panel.getElement().getStyle().setBackgroundColor(appt.getAppointment().getStyle());
                    panel.getElement().getStyle().setBorderColor(appt.getAppointment().getBorder());
                    panel.getElement().getStyle().setBorderWidth(1, Style.Unit.PX);
                    panel.getElement().getStyle().setBorderStyle(Style.BorderStyle.SOLID);
                } else if (appt.getAppointment().isNoTask()) {
                    panel.getElement().getStyle().setBorderColor(appt.getAppointment().getBorder());
                    panel.getElement().getStyle().setBorderWidth(1, Style.Unit.PX);
                    panel.getElement().getStyle().setBorderStyle(Style.BorderStyle.SOLID);
                } else {
                    panel.addStyleName(appt.getAppointment().getStyle());
                }
                DOM.setStyleAttribute(panel.getElement(), "position", "absolute");
                panel.setWidth(appt.getWidth());
                panel.setHeight(appt.getHeight());
                panel.setTop(appt.getTop());
                panel.setLeft(appt.getLeft());
//            panel.setAppointment(appt.getAppointment());
//            panel.setMultiDay(true);

//			dayViewBody.getGrid().grid.add(panel);

                if (calendarWidget.isTheSelectedAppointment(panel.getAppointment())) {
                    panel.addStyleName("gwt-appointment-selected");
                    selectedAppointmentWidgets.add(panel);
                }

                appointmentWidgets.add(panel);
                multiViewBody.grid.add(panel);
            }
        }
    }

    @Override
    public void scrollToHour(int hour) {
        dayViewBody.getScrollPanel().setScrollPosition(hour * getSettings().getIntervalsPerHour() * getSettings().getPixelsPerInterval());
    }

    public void doSizing() {
    }

    public void onDeleteKeyPressed() {
        if (calendarWidget.getSelectedAppointment() != null) {
            calendarWidget.fireDeleteEvent(calendarWidget.getSelectedAppointment());
        }
    }

    public void onDoubleClick(Element element, Event event) {
        /*ArrayList<AppointmentWidget> list = findAppointmentWidgetsByElement(element);
        if (!list.isEmpty()) {
            Appointment appt = list.get(0).getAppointment();
            calendarWidget.fireOpenEvent(appt);
        } else if (getSettings().getTimeBlockClickNumber() == CalendarSettings.Click.Double
                && element == dayViewBody.getGrid().gridOverlay.getElement()) {
            int x = DOM.eventGetClientX(event);
            int y = DOM.eventGetClientY(event);
            timeBlockClick(x, y);
        }*/

        ArrayList<AppointmentDragWidget> list = findAppointmentWidgetsByElement(element);
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

    public void onSingleClick(Element element, Event event) {
        Appointment appointment = findAppointmentByElement(element);
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
                } else if ((link = findLinkByElement(element)) != null) {
                    onClickLink(link);
                } else if (calendarWidget.getSettings().getTimeBlockClickNumber() == CalendarSettings.Click.Single && !isOutOfCell(event)) {
                    int x = (Utils.isIE() || Utils.getUserAgent().contains("chrome")) ? (DOM.eventGetClientX(event) + Window.getScrollLeft()) : DOM.eventGetClientX(event);
                    int y = (Utils.isIE() || Utils.getUserAgent().contains("chrome")) ? (DOM.eventGetClientY(event) + Window.getScrollTop()) : DOM.eventGetClientY(event);
                    timeBlockClick(x, y);
                }
            }
        }
    }

    private Link findLinkByElement(Element element) {
        for (int i = 0; i < absolutePanel.getWidgetCount(); i++) {
            Widget widget = absolutePanel.getWidget(i);
            if (DOM.isOrHasChild(widget.getElement(), element) && widget instanceof Link) {
                return (Link) widget;
            }
        }
        return null;
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
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
    }

    private boolean isOutOfCell(Event event) {
        int positionY = DOM.eventGetClientY(event);
        int calculatedWeekDayHeaderHeight = dayViewBody.getOffsetHeight();
        return positionY < dayViewBody.getAbsoluteLeft() - calculatedWeekDayHeaderHeight;
    }

    @Override
    public void onAppointmentSelected(Appointment appt) {
        ArrayList<AppointmentDragWidget> clickedAppointmentAdapters = findAppointmentWidget(appt);

        if (!clickedAppointmentAdapters.isEmpty()) {
            for (AppointmentDragWidget adapter : selectedAppointmentWidgets) {
                adapter.removeStyleName("gwt-appointment-selected");
            }

            for (AppointmentDragWidget adapter : clickedAppointmentAdapters) {
                adapter.addStyleName("gwt-appointment-selected");
            }

            selectedAppointmentWidgets.clear();
            selectedAppointmentWidgets = clickedAppointmentAdapters;

            DOM.scrollIntoView(clickedAppointmentAdapters.get(0).getElement());
        }
    }

    public void onRightArrowKeyPressed() {
        calendarWidget.selectNextAppointment();
    }

    public void onUpArrowKeyPressed() {
        calendarWidget.selectPreviousAppointment();
    }

    public void onDownArrowKeyPressed() {
        calendarWidget.selectNextAppointment();
    }

    public void onLeftArrowKeyPressed() {
        calendarWidget.selectPreviousAppointment();
    }

    public CalendarSettings getSettings() {
        // REMOVE THIS, this should not be here
        return super.calendarWidget.getSettings();
    }

    public void setSettings(CalendarSettings settings) {
        // REMOVE THIS, this should not be here
    }

    @Override
    public String getStyleName() {
        return "gwt-cal";
    }

    @Override
    public void attach(CalendarWidget widget) {
        super.attach(widget);

        if (dayViewBody == null) {
            dayViewBody = new DayViewBody(this, absolutePanel);
            dayViewHeader = new DayViewHeader(this);
            layoutStrategy = new DayViewLayoutStrategy(this);
            multiViewBody = new DayViewMultiDayBody();
        }

        calendarWidget.getRootPanel().add(dayViewHeader);
        calendarWidget.getRootPanel().add(multiViewBody);
        calendarWidget.getRootPanel().add(dayViewBody);

        if (dragController == null) {
            dragController = new PickupDragController(absolutePanel, true);
            dragController.addDragHandler(new DragHandler() {
                public void onDragEnd(DragEndEvent event) {
                    RootPanel.getBodyElement().removeClassName("has-dnd-drag");
                    Integer userId = Integer.valueOf(Cookies.getCookie(CommandConstants.GOOGLE_CALENDAR_OWNER_ID_COOKIE));
                    if (Utils.getUserID().equals(userId)) {
                        Appointment appointment = ((AppointmentDragWidget) event.getContext().draggable).getAppointment();
                        calendarWidget.setCommittedAppointment(appointment);
                        calendarWidget.fireUpdateEvent(appointment);
                    }
                }

                public void onDragStart(DragStartEvent event) {
                    RootPanel.getBodyElement().addClassName("has-dnd-drag");
                    Integer userId = Integer.valueOf(Cookies.getCookie(CommandConstants.GOOGLE_CALENDAR_OWNER_ID_COOKIE));
                    if (Utils.getUserID().equals(userId)) {
                        calendarWidget.setRollbackAppointment(((AppointmentDragWidget) event.getContext().draggable).getAppointment().clone());
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
         * Need to re-set absolutePanel to position:absolute because gwt-dnd
         * will set it to relative, but then the layout gets f***ed up
         */
        dragController.setBehaviorDragStartSensitivity(5);
        dragController.setBehaviorDragProxy(true);

        // instantiate our drop controller
        dayViewDropController = new DayViewDropController(absolutePanel, dayViewBody, calendarWidget);
        dragController.registerDropController(dayViewDropController);
    }

    private void timeBlockClick(int x, int y) {
        int windowWidth = Window.getClientWidth();
        if (x < windowWidth - 55) {
            int left = dayViewBody.getGrid().gridOverlay.getAbsoluteLeft();
            int top = dayViewBody.getScrollPanel().getAbsoluteTop();
            int width = dayViewBody.getGrid().gridOverlay.getOffsetWidth();
            int scrollOffset = dayViewBody.getScrollPanel().getScrollPosition();

            // x & y are based on screen position,need to get x/y relative to
            // component
            int relativeY = y - top + scrollOffset;
            int relativeX = x - left;

            // find the interval clicked and day clicked
            double interval = Math.floor(relativeY / (double) getSettings().getPixelsPerInterval());
            double day = Math.floor((double) relativeX / ((double) width / (double) calendarWidget.getDays()));

            // create new appointment date based on click
            Date newStartDate = DateUtil.resetTime(calendarWidget.getDate());
            newStartDate = DateUtil.addTime(newStartDate, 0, (int) interval * (60 / getSettings().getIntervalsPerHour()));
            newStartDate = DateUtil.addDays(newStartDate, (int) day);

            DateTimeFormat time = DateTimeFormat.getFormat("HH:mm");
            Date end = DateUtil.addTime(newStartDate, 1, 0);
            if (!DateUtil.areOnTheSameDay(newStartDate, end)) {
                int minutes = DateUtil.MINUTES_PER_HOUR * DateUtil.HOURS_PER_DAY - DateUtil.calculateDateInMinutes(newStartDate);
                end = DateUtil.addTime(newStartDate, 0, minutes);
            }
            Appointment appointment = new Appointment();
            appointment.setSubject(time.format(newStartDate) + " - " + time.format(end));
            appointment.setDescription(Property.get(Constants.EVENT_LIST, wfmStrings.newEvent(), wfmStrings.event()));
            appointment.setStartDate(newStartDate);
            appointment.setEndDate(end);

            ArrayList<Appointment> appointments = new ArrayList<>();
            appointments.add(appointment);

            ArrayList<AppointmentAdapter> appointmentAdapters = layoutStrategy.doLayout(appointments, (int) day, calendarWidget.getDays());

            final AppointmentWidget panel = new AppointmentWidget();
            for (AppointmentAdapter appt : appointmentAdapters) {
                panel.addStyleName(appt.getAppointment().getStyle());
                panel.setWidth(appt.getWidth());
                panel.setHeight(appt.getHeight());
                panel.setTitle(appt.getAppointment().getSubject());
                panel.setTop(appt.getTop());
                panel.setLeft(appt.getLeft());
                panel.setAppointment(appt.getAppointment());
                panel.setDescription(appt.getAppointment().getDescription());

                dayViewBody.getGrid().grid.add(panel);
            }

            Command closePopup = () -> dayViewBody.getGrid().grid.remove(panel);

            DOM.scrollIntoView(panel.getElement());

            appointment.setSubject(null);//If the title is not null, it will draw existing appointment view because of the title name.
            appointment.setDescription(null);
            appointment.setAllDay(false);
            calendarWidget.fireTimeBlockClickEvent(closePopup, newStartDate, appointment, panel.getAbsoluteLeft(), panel.getAbsoluteTop());
        }
    }

    private void timeBlockClick(int x) {
        int windowWidth = Window.getClientWidth();
        if (x < windowWidth - 55) {
            int left = dayViewBody.getGrid().gridOverlay.getAbsoluteLeft();
            int width = dayViewBody.getGrid().gridOverlay.getOffsetWidth();

            int relativeX = x - left;

            // find the interval clicked and day clicked
            double day = Math.floor((double) relativeX / ((double) width / (double) calendarWidget.getDays()));

            // create new appointment date based on click
            Date newStartDate = DateUtil.resetTime(calendarWidget.getDate());
//        newStartDate = DateUtils.addTime(newStartDate, 0, 0);
            newStartDate = DateUtil.addDays(newStartDate, (int) day);

            DateTimeFormat time = DateTimeFormat.getFormat("HH:mm");
            Date end = DateUtil.addTime(newStartDate, 24, 0);

            Appointment appointment = new Appointment();
            appointment.setSubject(time.format(newStartDate) + " - " + time.format(end));
            appointment.setDescription(Property.get(Constants.EVENT_LIST, wfmStrings.newEvent(), wfmStrings.event()));
            appointment.setStartDate(newStartDate);
            appointment.setEndDate(end);

            ArrayList<Appointment> appointments = new ArrayList<>();
            appointments.add(appointment);

            ArrayList<AppointmentAdapter> appointmentAdapters = layoutStrategy.doLayout(appointments, (int) day, calendarWidget.getDays());

            final AppointmentWidget panel = new AppointmentWidget();
            for (AppointmentAdapter appt : appointmentAdapters) {
                panel.addStyleName(appt.getAppointment().getStyle());
                panel.setWidth(appt.getWidth());
                panel.setHeight(appt.getHeight());
                panel.setTitle(appt.getAppointment().getSubject());
                panel.setTop(appt.getTop());
                panel.setLeft(appt.getLeft());
                panel.setAppointment(appt.getAppointment());
                panel.setDescription(appt.getAppointment().getDescription());
                panel.setMultiDay(appointment.isMultiDayAppointment());

                dayViewBody.getGrid().grid.add(panel);
            }

            Command closePopup = () -> dayViewBody.getGrid().grid.remove(panel);

            DOM.scrollIntoView(panel.getElement());

            appointment.setSubject(null);//If the title is not null, it will draw existing appointment view because of the title name.
            appointment.setDescription(null);

            calendarWidget.fireTimeBlockClickEvent(closePopup, newStartDate, appointment, panel.getAbsoluteLeft(), 136);
        }
    }

    private ArrayList<AppointmentDragWidget> findAppointmentWidgetsByElement(Element element) {
        return findAppointmentWidget(findAppointmentByElement(element));
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
        for (AppointmentDragWidget widget : appointmentWidgets) {
            if (DOM.isOrHasChild(widget.getElement(), element)) {
                appointmentAtElement = widget.getAppointment();
                break;
            }
        }
        return appointmentAtElement;
    }

    /**
     * Finds any related adapters that match the given Appointment.
     *
     * @param appt Appointment to match.
     * @return List of related AppointmentWidget objects.
     */
    private ArrayList<AppointmentDragWidget> findAppointmentWidget(Appointment appt) {
        ArrayList<AppointmentDragWidget> appointmentAdapters = new ArrayList<>();
        if (appt != null) {
            for (AppointmentDragWidget widget : appointmentWidgets) {
                if (widget.getAppointment().equals(appt)) {
                    appointmentAdapters.add(widget);
                }
            }
        }
        return appointmentAdapters;
    }

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
