package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion;

import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.task.TaskSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.*;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.event.*;
import com.google.gwt.event.logical.shared.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.Date;

/**
 * <code>CalendarWidget</code> is an {@link InteractiveWidget}
 * that maintains a calendar model (a set of {@link Appointment} objects)
 * managed through an {@link AppointmentManager}.
 * <p/>
 * LayoutStrategy - CHECK
 * ??? or is this same as DragDrop
 * that if the Appointment object is updated, need to refersh widget
 *
 * @see InteractiveWidget
 */
public class CalendarWidget extends InteractiveWidget implements
        HasSelectionHandlers<Appointment>, HasDeleteHandlers<Appointment>,
        HasOpenHandlers<Appointment>, HasTimeBlockClickHandlers<Date>,
        HasSaveHandlers<Appointment>, HasUpdateHandlers<Appointment> {

    /**
     * Set to <code>true</code> if the calendar layout is suspended and cannot
     * be triggered.
     */
    private boolean layoutSuspended = false;

    /**
     * Set to <code>true</code> if the calendar is pending the layout of its
     * appointments.
     */
    private boolean layoutPending = false;

    /**
     * The date currently displayed by the calendar. Set to current system date
     * by default.
     */
    private Date date;

    /**
     * Public access to appointments (View/Book)
     */
    protected boolean isPublic = false;

    /**
     * is bookable
     */
    protected boolean isBookable = true;

    /**
     * Calendar settings, set to default.
     */
    private CalendarSettings settings = CalendarSettings.DEFAULT_SETTINGS;

    /**
     * The component to manage the set of appointments displayed by this
     * <code>CalendarWidget</code>.
     */
    private AppointmentManager appointmentManager = null;

    private CalendarView view = null;

    /**
     * Creates a <code>CalendarWidget</code> with an empty set of appointments
     * and the current system date as the date currently displayed by the
     * calendar.
     */
    public CalendarWidget() {
        this(new Date());
    }

    public CalendarWidget(Date date) {
        appointmentManager = new AppointmentManager();
        this.date = DateUtil.resetTime(date);
    }

    /**
     * Changes the current view of this calendar widget to the specified
     * <code>view</code>. By setting this widget's current view the whole widget
     * panel is cleared.
     *
     * @param view The {@link CalendarView} implementation to render this widget's underlying calendar
     */
    public final void setView(CalendarView view) {
        this.getRootPanel().clear();
        this.view = view;
        this.view.attach(this);
        this.setStyleName(this.view.getStyleName());
        this.refresh();

        if (view.getHeight() != null) {
            this.setHeight(view.getHeight());
        }
    }

    public Date getDate() {
        return (Date) date.clone();
    }

    public void setDate(Date date, int days) {
        this.date = DateUtil.resetTime(date);
        view.setDisplayedDays(days);
        refresh();
    }

    public void setDate(Date date) {
        setDate(date, getDays());
    }

    /**
     * Moves this calendar widget current <code>date</code> as many days as
     * specified by the <code>numOfDays</code> parameter.
     *
     * @param numOfDays The number of days to change the calendar date forward (positive number) or backwards.
     */
    @SuppressWarnings("deprecation")
    public void addDaysToDate(int numOfDays) {
        this.date.setDate(this.date.getDate() + numOfDays);
    }

    public int getDays() {
        return view == null ? 3 : view.getDisplayedDays();
    }

    public void setDays(int days) {
        view.setDisplayedDays(days);
        refresh();
    }

    /**
     * Returns the collection of appointments in the underlying in-memory model
     * of this calendar widget. <strong>Warning</strong>: the returned
     * collection of apointments can be modified by client code, possibly
     * breaking the system model invariants.
     *
     * @return The set of appointments to be displayed by this calendar widget
     * @see AppointmentManager#getAppointments()
     */
    public ArrayList<Appointment> getAppointments() {
        return appointmentManager.getAppointments();
    }

    public ArrayList<Appointment> getAppointmentsByRecurring(Appointment recurringAppointment) {
        return appointmentManager.getAppointmentsByRecurring(recurringAppointment);
    }

    /**
     * Removes an appointment from the calendar.
     *
     * @param appointment the item to be removed.
     */
    public void removeAppointment(Appointment appointment) {
        removeAppointment(appointment, false);
    }

    public void removeAppointmentWithoutRefresh(Appointment appointment) {
        appointmentManager.removeAppointment(appointment);
    }

    /**
     * Removes the currently selected appointment from the model, if such appointment is set.
     */
    public void removeCurrentlySelectedAppointment() {
        appointmentManager.removeCurrentlySelectedAppointment();
    }

    /**
     * Removes an appointment from the calendar.
     *
     * @param appointment the item to be removed.
     * @param fireEvents  <code>true</code> to allow deletion events to be fired
     */
    public void removeAppointment(Appointment appointment, boolean fireEvents) {
        boolean commitChange = true;

        if (fireEvents) {
            commitChange = DeleteEvent.fire(this, getSelectedAppointment());
        }

        if (commitChange) {
            appointmentManager.removeAppointment(appointment);
            refresh();
        }
    }

    public void removeAppointmentsWithoutRefresh(ArrayList<Appointment> appointments) {
        for (Appointment appointment : appointments) {
            appointmentManager.removeAppointment(appointment);
        }
    }

    /**
     * Resets the &quot;currently selected&quot; appointment of this calendar.
     *
     * @see AppointmentManager
     */
    public void resetSelectedAppointment() {
        appointmentManager.resetSelectedAppointment();
    }

    /**
     * Adds an appointment to the calendar.
     *
     * @param appointment item to be added
     */
    public void addAppointment(Appointment appointment) {
        appointmentManager.addAppointment(appointment);
        refresh();
    }

    public void addAppointmentWithoutRefresh(Appointment appointment) {
        appointmentManager.addAppointment(appointment);
    }

    /**
     * Adds each appointment in the list to the calendar.
     *
     * @param appointments items to be added.
     */
    public void addAppointments(ArrayList<Appointment> appointments) {
        appointmentManager.addAppointments(appointments);
        refresh();
    }

    public void addAppointmentsWithoutRefresh(ArrayList<Appointment> appointments) {
        appointmentManager.addAppointments(appointments);
    }

    /**
     * Clears all appointment items.
     */
    public void clearAppointments() {
        appointmentManager.clearAppointments();
        refresh();
    }

    /**
     * Sets the currently selected item.
     *
     * @param appointment the item to be selected, or <code>null</code> to de-select all items.
     */
    public void setSelectedAppointment(Appointment appointment) {
        setSelectedAppointment(appointment, true);
    }

    public void setSelectedAppointment(Appointment appointment, boolean fireEvents) {
        appointmentManager.setSelectedAppointment(appointment);

        if (fireEvents) {
            fireSelectionEvent(appointment);
        }
    }

    /**
     * Indicates whether there is a &quot;currently selected&quot; appointment
     * at the moment.
     *
     * @return <code>true</code> if there is an appointment currently selected, <code>false</code> if it is <code>null</code>.
     * @see AppointmentManager#hasAppointmentSelected()
     */
    public boolean hasAppointmentSelected() {
        return appointmentManager.hasAppointmentSelected();
    }

    /**
     * Gets the currently selected item.
     *
     * @return the selected item.
     */
    public Appointment getSelectedAppointment() {
        return appointmentManager.getSelectedAppointment();
    }

    /**
     * Tells whether the passed <code>appointment</code> is the currently
     * selected appointment.
     *
     * @param appointment The appointment to test to be the currently selected
     * @return <code>true</code> if there is a currently selected appointment
     *         and happens to be equal to the passed <code>appointment</code>
     * @see AppointmentManager#isTheSelectedAppointment(Appointment)
     */
    public boolean isTheSelectedAppointment(Appointment appointment) {
        return appointmentManager.isTheSelectedAppointment(appointment);
    }

    /**
     * Performs all layout calculations for the list of appointments and resizes
     * the Calendar View appropriately.
     */
    public void refresh() {
        if (layoutSuspended) {
            layoutPending = true;
            return;
        }
        appointmentManager.sortAppointments();

        doLayout();
        doSizing();
    }

    public void refresh(boolean wideScreen) {
        view.setWideScreen(wideScreen);
        refresh();
    }

    public void doLayout() {
        view.doLayout();
    }

    public void doSizing() {
        view.doSizing();
    }

    public void onLoad() {
        DeferredCommand.addCommand(() -> doSizing());
    }

    /**
     * Suspends the calendar from performing a layout. This can be useful when
     * adding a large number of appointments at a time, since a layout is
     * performed each time an appointment is added.
     */
    public void suspendLayout() {
        layoutSuspended = true;
    }

    /**
     * Allows the calendar to perform a layout, sizing the component and placing
     * all appointments. If a layout is pending it will get executed when this
     * method is called.
     */
    public void resumeLayout() {
        layoutSuspended = false;

        if (layoutPending) {
            refresh();
        }
    }

    public CalendarSettings getSettings() {
        return this.settings;
    }

    public void setSettings(CalendarSettings settings) {
        this.settings = settings;
    }

    public void scrollToHour(int hour) {
        view.scrollToHour(hour);
    }

    public void scrollToHour() {
        scrollToHour(settings.getScrollToHour());
    }

    public boolean selectPreviousAppointment() {
        boolean selected = appointmentManager.selectPreviousAppointment();
        if (selected) {
            fireSelectionEvent(getSelectedAppointment());
        }
        return selected;
    }

    public boolean selectNextAppointment() {
        boolean selected = appointmentManager.selectNextAppointment();
        if (selected) {
            fireSelectionEvent(getSelectedAppointment());
        }
        return selected;
    }

    @Override
    public void onDeleteKeyPressed() {
        view.onDeleteKeyPressed();
    }

    @Override
    public void onDoubleClick(Element element, Event event) {
        view.onDoubleClick(element, event);
    }

    @Override
    public void onDownArrowKeyPressed() {
        view.onDownArrowKeyPressed();
    }

    @Override
    public void onLeftArrowKeyPressed() {
        view.onLeftArrowKeyPressed();
    }

    @Override
    public void onMouseDown(Element element, Event event) {
        view.onSingleClick(element, event);
    }

    @Override
    public void onRightArrowKeyPressed() {
        view.onRightArrowKeyPressed();
    }

    @Override
    public void onUpArrowKeyPressed() {
        view.onUpArrowKeyPressed();
    }

    public void fireOpenEvent(Appointment appointment) {
        OpenEvent.fire(this, appointment);
    }

    public void fireDeleteEvent(Appointment appointment) {
        //fire the event to notify the client
        boolean allow = DeleteEvent.fire(this, appointment);

        if (allow) {
            appointmentManager.removeAppointment(appointment);
            refresh();
        }
    }

    public void fireSelectionEvent(Appointment appointment) {
        view.onAppointmentSelected(appointment);
        ShortAppointmentView appointmentView = initShortAppointmentView(appointment);
        appointmentView.center();
        SelectionEvent.fire(this, appointment);
    }

    public void fireTimeBlockClickEvent(final Command onClosePopup, Date date, Appointment appointment, final int left, final int top) {
        final ShortAppointmentView appointmentView = initShortAppointmentView(appointment);
            appointmentView.setPopupPositionAndShow((offsetWidth, offsetHeight) -> {
                int leftPosition = left + 20;
                int topPosition = top + 2 * settings.getPixelsPerInterval();

                int relativeX = leftPosition + offsetWidth;
                int relativeY = topPosition + offsetHeight;

                if (relativeX > Window.getClientWidth()) {
                    leftPosition = left - offsetWidth - 3;
                }

                if (view.getDisplayedDays() == 1) {
                    leftPosition = Window.getClientWidth() / 2;
                }

                if (relativeY > Window.getClientHeight()) {
                    topPosition = top - offsetHeight - 3;
                }

                appointmentView.setPosition(leftPosition, topPosition);
            });
        appointmentView.onClosePopup(popupPanelCloseEvent -> {
            if (onClosePopup != null) {
                onClosePopup.execute();
            }
        });

        TimeBlockClickEvent.fire(this, date);
    }

    public ShortAppointmentView initShortAppointmentView(Appointment appointment) {
        ShortAppointmentView appointmentView = new ShortAppointmentView(appointment);
        appointmentView.onSaveOrUpdateAppointment(new SaveAppointmentHandler() {
            public void onSaveOrUpdate(Appointment appointment) {
                fireSaveEvent(appointment);
            }

            public void onSaveOrUpdateTask(Appointment appointment) {
                fireSaveEvent(appointment);

            }
        });
        appointmentView.onDeleteAppointment(new DeleteAppointmentHandler() {
            public void onDelete(Appointment appointment) {
                fireDeleteEvent(appointment);
            }

            public void onDeleteTask(TaskSingleItem taskSingleItem) {

            }
        });
        return appointmentView;
    }

    public PublicShortAppointmentView initPublicShortAppointmentView(Appointment appointment, boolean isBookable) {
        return new PublicShortAppointmentView(appointment, isBookable);
    }

    public void fireSaveEvent(Appointment appointment) {
        SaveEvent.fire(this, appointment);
    }

    public void fireUpdateEvent(Appointment appointment) {
        //refresh the appointment
        refresh();
        //fire the event to notify the client
        boolean allow = UpdateEvent.fire(this, appointment);

        if (!allow) {
            appointmentManager.rollback();
            refresh();
        }

        /**
         * Below we are doing temporary solution, so later we MUST handle this.
         * On Drag End besides of updating we are saving it also, because at data base
         * we are checking to existance of appointmentID, if appointment has its id,
         * instead of saving, it automatically updates the appointment. Therefore, right
         * now we are simply calling saveEvent too.
         */
        fireSaveEvent(appointment);
    }

    public HandlerRegistration addSelectionHandler(SelectionHandler<Appointment> handler) {
        return addHandler(handler, SelectionEvent.getType());
    }

    public HandlerRegistration addDeleteHandler(DeleteHandler<Appointment> handler) {
        return addHandler(handler, DeleteEvent.getType());
    }

    public HandlerRegistration addTimeBlockClickHandler(TimeBlockClickHandler<Date> handler) {
        return addHandler(handler, TimeBlockClickEvent.getType());
    }

    public HandlerRegistration addUpdateHandler(UpdateHandler<Appointment> handler) {
        return addHandler(handler, UpdateEvent.getType());
    }

    public HandlerRegistration addOpenHandler(OpenHandler<Appointment> handler) {
        return addHandler(handler, OpenEvent.getType());
    }

    public HandlerRegistration addSaveHandler(SaveHandler<Appointment> handler) {
        return addHandler(handler, SaveEvent.getType());
    }

    public void addToRootPanel(Widget widget) {
        getRootPanel().add(widget);
    }

    public void setRollbackAppointment(Appointment appointment) {
        appointmentManager.setRollbackAppointment(appointment);
    }

    public void setCommittedAppointment(Appointment appointment) {
        appointmentManager.setCommittedAppointment(appointment);
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public boolean isBookable() {
        return isBookable;
    }

    public void setBookable(boolean bookable) {
        isBookable = bookable;
    }
}
