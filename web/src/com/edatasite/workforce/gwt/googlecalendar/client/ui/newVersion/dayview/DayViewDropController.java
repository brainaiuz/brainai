package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.dayview;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.CalendarWidget;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.AbsolutePanel;

import java.util.Date;

/**
 * Created by KHasan on 04.11.15.
 */
public class DayViewDropController extends AbsolutePositionDropController {

    private DayViewBody dayViewBody;
    private CalendarWidget calendarWidget;

    /**
     * Basic constructor.
     *
     * @param dropTarget     the absolute panel drop target
     * @param dayViewBody
     * @param calendarWidget
     */
    public DayViewDropController(AbsolutePanel dropTarget, DayViewBody dayViewBody, CalendarWidget calendarWidget) {
        super(dropTarget);
        this.dayViewBody = dayViewBody;
        this.calendarWidget = calendarWidget;
    }

    /**
     * Callback method executed once the drag has completed.
     * We need to reset the background color of all previously highlighted
     * cells. Also need to actually change the appointment's start / end date
     * here (code doesn't exist yet).
     */
    @Override
    @SuppressWarnings("deprecation")
    public void onDrop(DragContext context) {
        Integer userId = Integer.valueOf(Cookies.getCookie(CommandConstants.GOOGLE_CALENDAR_OWNER_ID_COOKIE));
        if (Utils.getUserID().equals(userId)) {
            super.onDrop(context);

            int left = dayViewBody.getGrid().gridOverlay.getAbsoluteLeft();
            int top = dayViewBody.getScrollPanel().getAbsoluteTop();
            int width = dayViewBody.getGrid().gridOverlay.getOffsetWidth();
            int scrollOffset = dayViewBody.getScrollPanel().getScrollPosition();

            // x & y are based on screen position,need to get x/y relative to
            // component

            Draggable draggable = draggableList.get(0);
            int relativeY = context.desiredDraggableY - top + scrollOffset;
            int relativeX = context.desiredDraggableX - left;

            // find the interval clicked and day clicked
            double interval = Math.floor(relativeY / (double) calendarWidget.getSettings().getPixelsPerInterval());
            double day = Math.floor((double) relativeX / ((double) width / (double) calendarWidget.getDays()));

            // create new appointment date based on click
            Date newStartDate = DateUtil.resetTime(calendarWidget.getDate());
            newStartDate = DateUtil.addTime(newStartDate, 0, (int) interval * (60 / calendarWidget.getSettings().getIntervalsPerHour()));
            newStartDate = DateUtil.addDays(newStartDate, (int) day);

            //get the appointment
            Appointment appointment = null;
            try {
                appointment = ((AppointmentDragWidget) context.draggable).getAppointment();
            } catch (Exception e) {
                e.printStackTrace();
            }

            long diff = appointment.getEndDate().getTime() - appointment.getStartDate().getTime();
            long diffHours = diff / (60 * 60 * 1000);
            long diffMinutes = (diff - diffHours * 60 * 60 * 1000) / (60 * 1000);
            DateTimeFormat time = DateTimeFormat.getFormat("HH:mm");

            Date end = DateUtil.addTime(newStartDate, (int) diffHours, (int) diffMinutes);
            if (!DateUtil.areOnTheSameDay(newStartDate, end)) {
                int minutes = DateUtil.MINUTES_PER_HOUR * DateUtil.HOURS_PER_DAY - DateUtil.calculateDateInMinutes(newStartDate);
                end = DateUtil.addTime(newStartDate, 0, minutes);
            }
            appointment.setStartDate(newStartDate);
            appointment.setEndDate(end);
        }
    }

}
