package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.monthview;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.AbsolutePositionDropController;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlexTable;

import java.util.Date;

public class MonthViewDropController extends AbsolutePositionDropController {

    private int daysPerWeek;
    private int weeksPerMonth;
    private Date firstDateDisplayed;

    /**
     * Flextable that displays a Month in grid format.
     */
    private FlexTable monthGrid;

    /**
     * List of all cells currently highlighted as an appointment
     * is being dragged.
     */
    private Element[] highlightedCells;

    public MonthViewDropController(AbsolutePanel dropTarget, FlexTable monthGrid) {
        super(dropTarget);
        this.monthGrid = monthGrid;
    }

    public void setDaysPerWeek(int daysPerWeek) {
        this.daysPerWeek = daysPerWeek;
    }

    public void setWeeksPerMonth(int weeksPerMonth) {
        this.weeksPerMonth = weeksPerMonth;
    }

    public Date getFirstDateDisplayed() {
        return firstDateDisplayed;
    }

    public void setFirstDateDisplayed(Date firstDateDisplayed) {
        this.firstDateDisplayed = firstDateDisplayed;
    }

    @Override
    public void onMove(DragContext context) {
        Integer userId = Integer.valueOf(Cookies.getCookie(CommandConstants.GOOGLE_CALENDAR_OWNER_ID_COOKIE));
        if (Utils.getUserID().equals(userId)) {
            super.onMove(context);

            //get the draggable object
            Draggable draggable = draggableList.get(0);

            //make sure it isn't null (shouldn't ever be)
            if (draggable == null) {
                return;
            }

            //get the mouse/drag coordinates
            int x = context.desiredDraggableX - dropTargetOffsetX + draggable.relativeX;
            int y = context.desiredDraggableY - dropTargetOffsetY + draggable.relativeY;

            //Now we need to figure out which cell to highlight based
            // on the X,Y coordinates
            int col = (int) Math.floor(x / (monthGrid.getOffsetWidth() / daysPerWeek));
            int row = (int) Math.floor(y / (monthGrid.getOffsetHeight() / weeksPerMonth)) + 1;

            //Get element for cell
            Element currHoveredCell = monthGrid.getFlexCellFormatter().getElement(row, col);

            //If this cell isn't already highlighted, we need to highlight
            if (highlightedCells == null || highlightedCells.length < 0 || !currHoveredCell.equals(highlightedCells[0])) {
                if (highlightedCells != null) {
                    for (Element elem : highlightedCells) {
                        if (elem != null) {
                            DOM.setStyleAttribute(elem, "backgroundColor", "#FFFFFF");
                        }
                    }
                }

                // here I hard-code 5 as the number of cells an appointment
                // should span. This, however, should be calculated.
                // Beware, we need to be very careful about memory here.
                // I tried to do a date diff calculation and got
                // out of memory exceptions in the JVM AND in the chrome browser
                Appointment appointment = ((AppointmentWidget) draggable.widget).getAppointment();

                int dateDiff = DateUtil.differenceInDays(appointment.getStartDate(), appointment.getEndDate());
                dateDiff = (dateDiff <= 0) ? 1 : dateDiff;
                highlightedCells = getCells(row, col, dateDiff);

                //alter its style as "highlighted"
                for (Element elem : highlightedCells) {
                    if (elem != null) {
                        DOM.setStyleAttribute(elem, "backgroundColor", "#C3D9FF");
                    }
                }
            }
        }
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

            for (Element elem : highlightedCells) {
                if (elem != null) {
                    DOM.setStyleAttribute(elem, "backgroundColor", "#FFFFFF");
                }
            }

            //reset highlighted cells to null
            highlightedCells = null;

            //get the draggable item
            Draggable draggable = draggableList.get(0);

            //get the appointment
            Appointment appointment = ((AppointmentWidget) context.draggable).getAppointment();

            //get the date difference for the appointment
            int dateDiff = DateUtil.differenceInDays(appointment.getEndDate(), appointment.getStartDate());//getDateDiff(appointment.isAllDay() || !appointment.isMultiDay(), appointment.getStart(), appointment.getEnd()) - 1;
            dateDiff = (dateDiff <= 0) ? 0 : dateDiff;

            //get the column and row for the draggable widget
            int row = getRow(context, draggable) - 1;
            int col = getColumn(context, draggable);
            int cell = row * daysPerWeek + col;

            //calculate the new start & end dates
            Date newStart = DateUtil.addDays(firstDateDisplayed, cell);
            newStart.setHours(appointment.getStartDate().getHours());
            newStart.setMinutes(appointment.getStartDate().getMinutes());
            newStart.setSeconds(appointment.getStartDate().getSeconds());

            Date newEnd = DateUtil.addDays(newStart, dateDiff);
            newEnd.setHours(appointment.getEndDate().getHours());
            newEnd.setMinutes(appointment.getEndDate().getMinutes());
            newEnd.setSeconds(appointment.getEndDate().getSeconds());

            appointment.setStartDate(newStart);
            appointment.setEndDate(newEnd);
        }
    }

    /**
     * Gets all the cells (as DOM Elements) that an appointment spans.
     * Note: It only includes cells in the table. If an appointment
     * ends in the following month the last cell in the list will
     * be the last cell in the table.
     *
     * @param row  Appointment's starting row
     * @param col  Appointment's starting column
     * @param days Number of days an appointment spans
     * @return Cell elements that an appointment spans
     */
    protected Element[] getCells(int row, int col, int days) {
        Element[] elems = new Element[days];

        for (int i = 0; i < days; i++) {
            if (col > daysPerWeek - 1) {
                col = 0;
                row++;
            }

            //Cheap code here. If the row / cell throw an out of index exception
            // we just break. THis kind of sucks because we have to
            // now account for null items in the Element[] array.
            try {
                elems[i] = monthGrid.getFlexCellFormatter().getElement(row, col);
            } catch (Exception ex) {
                break;
            }

            col++;
        }

        return elems;
    }

    /**
     * Gets the difference in days between two Dates.
     *
     * @param startDate
     * @param endDate
     * @return
     */
    @SuppressWarnings("deprecation")
    public int getDateDiff(boolean isAllDay, Date startDate, Date endDate) {
        if (isAllDay) {
            return 1;
        }

        if (startDate.getMonth() == endDate.getMonth()) {
            return endDate.getDate() - startDate.getDate() + 1;
        } else {
            return (int) Math.ceil(((endDate.getTime() - startDate.getTime()) / DateUtil.MILLIS_IN_A_DAY)) + 1;
        }
    }

    public int getRow(DragContext context, Draggable draggable) {
        //get the mouse/drag coordinates
        int y = context.desiredDraggableY - dropTargetOffsetY + draggable.relativeY;

        //Now we need to figure out which cell to highlight based
        // on the X,Y coordinates
        return (int) Math.floor(y / (monthGrid.getOffsetHeight() / weeksPerMonth)) + 1;
    }

    public int getColumn(DragContext context, Draggable draggable) {
        //get the mouse/drag coordinates
        int x = context.desiredDraggableX - dropTargetOffsetX + draggable.relativeX;

        //Now we need to figure out which cell to highlight based
        // on the X,Y coordinates
        return (int) Math.floor(x / (monthGrid.getOffsetWidth() / daysPerWeek));
    }
}