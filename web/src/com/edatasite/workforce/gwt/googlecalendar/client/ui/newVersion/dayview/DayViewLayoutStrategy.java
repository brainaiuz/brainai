package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.dayview;

import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.HasSettings;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.util.AppointmentUtil;
import com.google.gwt.user.client.ui.Label;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Responsible for arranging all Appointments, visually, on a screen in a manner
 * similar to the Microsoft Outlook / Windows Vista calendar.
 * See: <img src='http://www.microsoft.com/library/media/1033/athome/images/moredone/calendar.gif'/>
 * <p/>
 * Note how overlapping appointments are displayed in the provided image
 */
public class DayViewLayoutStrategy {

    private HasSettings settings = null;

    public DayViewLayoutStrategy(HasSettings settings) {
        this.settings = settings;
    }

    public ArrayList<AppointmentAdapter> doLayout(List<Appointment> appointments, int dayIndex, int dayCount) {
        int intervalsPerHour = settings.getSettings().getIntervalsPerHour(); //15 minute intervals
        float intervalSize = settings.getSettings().getPixelsPerInterval(); //15 pixels per interval

        /*
         * Note: it is important that all appointments are sorted by Start date
         * (asc) and Duration (desc) for this algorithm to work. If that is not
         * the case, it won't work, at all!! Maybe this is a problem that needs
         * to be addressed
         */

        // set to 30 minutes. this means there will be 48 cells. 60min / 30min
        // interval * 24
        // int minutesPerInterval = 30;
        // interval size, set to 100px
        // float sizeOfInterval = 25f;

        // a calendar can view multiple days at a time. sets number of visible
        // days
        int minutesPerInterval = DateUtil.MINUTES_PER_HOUR / intervalsPerHour;

        // get number of cells (time blocks)
        int numberOfTimeBlocks = DateUtil.MINUTES_PER_HOUR / minutesPerInterval * DateUtil.HOURS_PER_DAY;
        TimeBlock[] timeBlocks = new TimeBlock[numberOfTimeBlocks];

        for (int i = 0; i < numberOfTimeBlocks; i++) {
            TimeBlock t = new TimeBlock();
            t.setStart(i * minutesPerInterval);
            t.setEnd(t.getStart() + minutesPerInterval);
            t.setOrder(i);
            t.setTop((float) i * intervalSize);
            t.setBottom(t.getTop() + intervalSize);
            timeBlocks[i] = t;
        }

        // each appointment will get "wrapped" in an appoinetment cell object,
        // so that we can assign it a location in the grid, row and
        // column span, etc.
        ArrayList<AppointmentAdapter> appointmentCells = new ArrayList<>();
        int groupMaxColumn = 0; // track total columns here! this will reset
        // when a group completes
        int groupStartIndex = -1;
        int groupEndIndex = -2;

        // Question: how to distinguish start / finish of a new group?
        // Answer: when endCell of previous appointments < startCell of new
        // appointment

        // for each appointments, we need to see if it intersects with each time
        // block
        for (Appointment appointment : appointments) {
            if (!appointment.isNoTask() || dayCount == 1) {
                TimeBlock startBlock = null;
                TimeBlock endBlock = null;

                // if(blockGroupEndCell)

                // wrap appointment with AppointmentInterface Cell and add to list
                AppointmentAdapter apptCell = new AppointmentAdapter(appointment);
                appointmentCells.add(apptCell);

                // get the first time block in which the appointment should appear
                // re-evaluate a time block that had zero matches...
                // store the index of the currently evaluated time block, if no
                // match, increment
                // that will prevent the same block from ever being re-evaluated
                // after no match found
                for (TimeBlock block : timeBlocks) {
                    // does the appointment intersect w/ the block???
                    if (block.intersectsWith(apptCell)) {
                        // we found one! set as start block and exit loop
                        startBlock = block;
                        // blockGroup.put(block, block);

                        if (groupEndIndex < startBlock.getOrder()) {
                            for (int i = groupStartIndex; i <= groupEndIndex; i++) {
                                TimeBlock tb = timeBlocks[i];
                                tb.setTotalColumns(groupMaxColumn + 1);
                            }
                            groupStartIndex = startBlock.getOrder();
                            groupMaxColumn = 0;
                        }

                        break;
                    } else {
                        // here is where I would increment, as per above to-do
                    }
                }

                // add the appointment to the start block
                startBlock.getAppointments().add(apptCell);
                // add block to appointment
                apptCell.getIntersectingBlocks().add(startBlock);

                // set the appointments column, if it has not already been set
                // if it has been set, we need to get it for reference later on in
                // this method
                int column = startBlock.getFirstAvailableColumn();
                apptCell.setColumnStart(column);
                apptCell.setColumnSpan(1); // hard-code to 1, for now

                // add column to block's list of occupied columns, so that the
                // column cannot be given to another appointment
                startBlock.getOccupiedColumns().put(column, column);

                // sets the start cell of the appt to the current block
                // we can do this since the blocks are ordered ascending
                apptCell.setCellStart(startBlock.getOrder());

                // go through all subsequent blocks...
                // find intersections
                for (int i = startBlock.getOrder() + 1; i < timeBlocks.length; i++) {
                    // get the nextTimeBlock
                    TimeBlock nextBlock = timeBlocks[i];
                    if (nextBlock.intersectsWith(apptCell)) {
                        // yes! add appointment to the block
                        // register start column
                        nextBlock.getAppointments().add(apptCell);
                        nextBlock.getOccupiedColumns().put(column, column);
                        endBlock = nextBlock; // this may change if intersects with
                        // next block

                        // add block to appointments list of intersecting blocks
                        apptCell.getIntersectingBlocks().add(nextBlock);
                    }
                }

                // if end block was never set, use the start block
                endBlock = (endBlock == null) ? startBlock : endBlock;
                // maybe here is the "end" of a group, where we then evaluate max
                // column

                if (column > groupMaxColumn) {
                    groupMaxColumn = column;
                }

                if (groupEndIndex < endBlock.getOrder()) {
                    groupEndIndex = endBlock.getOrder();
                }

                // set the appointments cell span (top to bottom)
                apptCell.setCellSpan(endBlock.getOrder() - startBlock.getOrder() + 1);

            }
            for (int i = groupStartIndex; i <= groupEndIndex; i++) {
                TimeBlock tb = timeBlocks[i];
                tb.setTotalColumns(groupMaxColumn + 1);
            }
            // we need to know the MAX number of cells for each time block.
            // so unfortunately we have to go back through the list to find this out

            //last stage is to calculate the adjustment reuired for 'multi-day' / multi-column
            float leftAdj = dayIndex / dayCount; //  0/3  or 2/3
            float widthAdj = 1f / dayCount;

            float paddingLeft = .5f;
            float paddingRight = .5f;
            float paddingBottom = 2;

            // now that everything has been assigned a cell, column and spans
            // we can calculate layout
            // Note: this can only be done after every single appointment has
            // been assigned a position in the grid
            for (AppointmentAdapter apptCell : appointmentCells) {
                float width = 1f / (float) apptCell.getIntersectingBlocks().get(0).getTotalColumns() * 100;
                float left = (float) apptCell.getColumnStart() / (float) apptCell.getIntersectingBlocks().get(0).getTotalColumns() * 100;

                //AppointmentInterface appt = apptCell.getAppointment();
                apptCell.setTop(apptCell.getCellStart() * intervalSize); // ok!
                apptCell.setLeft((widthAdj * 100 * dayIndex) + (left * widthAdj) + paddingLeft); // ok
                apptCell.setWidth(width * widthAdj - paddingLeft - paddingRight); // ok!
                apptCell.setHeight((float) apptCell.getIntersectingBlocks().size() * intervalSize - paddingBottom); // ok!

                float apptStart = apptCell.getAppointmentStart();
                float apptEnd = apptCell.getAppointmentEnd();
                float blockStart = timeBlocks[apptCell.getCellStart()].getStart();
                float blockEnd = timeBlocks[apptCell.getCellStart() + apptCell.getCellSpan() - 1].getEnd();
                float blockDuration = blockEnd - blockStart;
                float apptDuration = apptEnd - apptStart;
                float timeFillHeight = apptDuration / blockDuration * 100f;
                float timeFillStart = (apptStart - blockStart) / blockDuration * 100f;
                apptCell.setCellPercentFill(timeFillHeight);
                apptCell.setCellPercentStart(timeFillStart);
            }
        }
        return appointmentCells;
    }

    public int doMultiDayLayout(ArrayList<Appointment> appointments, ArrayList<AppointmentAdapter> adapters, Date start, int days) {
        //for a particular day need to track all used rows
        HashMap<Integer, HashMap<Integer, Integer>> daySlotMap = new HashMap<>();

        int minHeight = 30;
        int maxRow = 0;

        //convert appointment to adapter
        for (Appointment appointment : appointments) {
            adapters.add(new AppointmentAdapter(appointment));
        }

        /**
         * If the number of appointments exceeds allowed we need to put the widget
         * that stores the information about those events.
         */
        ArrayList<Label> links = new ArrayList<>();

        //create array of dates
        ArrayList<Date> dateList = new ArrayList<>();
        Date tempStartDate = (Date) start.clone();

        for (int i = 0; i < days; i++) {
            Date d = DateUtil.resetTime((Date) tempStartDate.clone());
            daySlotMap.put(i, new HashMap<>());
            dateList.add(d);
            links.add(new Label());
            tempStartDate = DateUtil.addDays(tempStartDate, 1);
        }

        //add appointments to each day
        for (AppointmentAdapter adapter : adapters) {
            int columnSpan = 0; //number of columns spanned
            boolean isStart = true; //indicates if current column is appointment start column

            //set column & span
            for (int i = 0; i < dateList.size(); i++) {
                Date date = dateList.get(i);
                boolean isWithinRange = AppointmentUtil.rangeContains(adapter.getAppointment(), date);

                //while we are at it, we can set the adapters start column and column span
                if (isWithinRange) {
                    if (isStart) {
                        adapter.setColumnStart(i);
                        isStart = false;
                    }

                    adapter.setColumnSpan(columnSpan);
                    columnSpan++;
                }
            }

            //now we set the row, which cannot be more than total # of appointments
            for (int x = 0; x < adapters.size(); x++) {
                boolean isRowOccupied = false;
                for (int y = adapter.getColumnStart(); y <= adapter.getColumnStart() + adapter.getColumnSpan(); y++) {
                    try {
                        HashMap<Integer, Integer> rowMap = daySlotMap.get(y);
                        if (rowMap != null && rowMap.containsKey(x)) {
                            isRowOccupied = true;
                        } else {
                            break; //break out of loop, nothing found in row slot
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                if (!isRowOccupied) {
                    //add row to maps
                    for (int y = adapter.getColumnStart(); y <= adapter.getColumnStart() + adapter.getColumnSpan(); y++) {
                        HashMap<Integer, Integer> rowMap = daySlotMap.get(y);
                        if (rowMap != null) {
                            rowMap.put(x, x);
                        }

                        if (x > maxRow) {
                            maxRow = x;
                        }
                    }
                    //set the row (also named cell)
                    adapter.setCellStart(x);
                    //break loop

                    //now we set the appointment's location
                    float top = adapter.getCellStart() * 25f + 5f;
                    float width = ((float) adapter.getColumnSpan() + 1f) / days * 100f - 1f;//10f = padding
                    float left = ((float) adapter.getColumnStart()) / days * 100f + .5f;//10f = padding
                    adapter.setWidth(width);
                    adapter.setLeft(left);
                    adapter.setTop(top);
                    adapter.setHeight(Appointment.DEFAULT_HEIGHT);
                    break;
                }
            }
        }

        int height = (maxRow + 1) * 25 + 5;
        return Math.max(height, minHeight);
    }
}