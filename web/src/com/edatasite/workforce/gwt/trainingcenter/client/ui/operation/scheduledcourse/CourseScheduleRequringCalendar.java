package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.scheduledcourse;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.resourceUtil.MonthDay;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse.ScheduledCourseItem;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 30/08/12
 * Time: 17:56
 * To change this template use File | Settings | File Templates.
 */
public class CourseScheduleRequringCalendar extends FlowPanel {

    private static TCStrings tcStrings = TCStrings.App.get();
    private static final String[] weekDayShortName = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

    private int hour;
    private int minute;
    private MonthDay monthDay;
    private RequiringDate requiringDateInterface;
    private Map<String, Date> clonedDateListMap = new HashMap<>();
    private ScheduledCourseItem scheduledCourseItem;

    private Date courseScheduleDate;
    private FlexTable calendarTabel;

    public CourseScheduleRequringCalendar() {
        super();
    }

    public void drawRequiringCalendarWidget(ScheduledCourseItem scheduledCourseItem) {
        this.scheduledCourseItem = scheduledCourseItem;
        this.courseScheduleDate = scheduledCourseItem.getStartDate();
        if (scheduledCourseItem.getClonedDateList() != null) {
            for (Date clonedDate : scheduledCourseItem.getClonedDateList()) {
                clonedDateListMap.put(clonedDate.getYear() + "_" + clonedDate.getMonth() + "_" + clonedDate.getDate(), clonedDate);
            }
        }
        clear();
        calendarTabel = new FlexTable();
        calendarTabel.setStyleName("recuring-calendar");
        calendarTabel.setCellPadding(0);
        calendarTabel.setCellSpacing(0);
        add(calendarTabel);
        initCalendar();
    }

    private void initCalendar() {
        this.hour = courseScheduleDate.getHours();
        this.minute = courseScheduleDate.getMinutes();
        monthDay = new MonthDay(new Date(courseScheduleDate.getYear(), courseScheduleDate.getMonth(), 1, hour, minute));
        drawRequiringCalendar();
    }

    private void drawRequiringCalendar() {
        int row = 0;
        for (int i = 0; i < 12; i++) {
            calendarTabel.setHTML(row, 0, DateUtils.getYearMonthFormat(monthDay.getDate()));
            calendarTabel.getFlexCellFormatter().setWidth(row, 0, "170px");
            calendarTabel.getFlexCellFormatter().setStyleName(row, 0, "month-name");
            int rowspan = drawRequiringCalendarDays(row, i == 0);
            calendarTabel.getFlexCellFormatter().setRowSpan(row, 0, rowspan);
            monthDay.dateGenerate(1);
            row += rowspan;
            setEmptyRow(row++);
        }
    }

    private void setEmptyRow(int row) {
        for (int i = 0; i <= 7; i++) {
            calendarTabel.setHTML(row, i, "&nbsp;");
        }
    }

    private int drawRequiringCalendarDays(int row, boolean enable) {
        drawRequiringDaysName(row++);
        int rowspan = 1, cell = monthDay.getDate().getDay();
        for (int day = 1; day <= monthDay.getMaxMonthDay(); day++) {
            KpiCheckBox checkBox = getCalendarCheckBox(day, monthDay.getYear(), monthDay.getMonth());
            Date date = new Date(monthDay.getYear(), monthDay.getMonth(), day);
            if (cell == 0) {
                calendarTabel.setWidget(row, 6, checkBox);
                calendarTabel.getFlexCellFormatter().setStyleName(row++, 6, "sunday");
                rowspan++;
                cell = 1;
                if ((enable && courseScheduleDate.getDate() >= day) || clonedDateListMap.containsKey(monthDay.getYear() + "_" + monthDay.getMonth() + "_" + day)) {
                    checkBox.setEnabled(false);
                    if (courseScheduleDate.getDate() == day || clonedDateListMap.containsKey(monthDay.getYear() + "_" + monthDay.getMonth() + "_" + day)) {
                        checkBox.setValue(true);
                    }
                }
                if (scheduledCourseItem.getTimeSlotItems().get(date.getDay()).getStartTime() == 0 && scheduledCourseItem.getTimeSlotItems().get(date.getDay()).getEndTime() == 0) {
                    checkBox.setEnabled(false);
                }
                continue;
            }
            if ((enable && courseScheduleDate.getDate() >= day) || clonedDateListMap.containsKey(monthDay.getYear() + "_" + monthDay.getMonth() + "_" + day)) {
                checkBox.setEnabled(false);
                if (courseScheduleDate.getDate() == day || clonedDateListMap.containsKey(monthDay.getYear() + "_" + monthDay.getMonth() + "_" + day)) {
                    checkBox.setValue(true);
                }
            }
            if (!scheduledCourseItem.isEnabledOvertime() && scheduledCourseItem.getTimeSlotItems().get(date.getDay()).getStartTime() == 0 && scheduledCourseItem.getTimeSlotItems().get(date.getDay()).getEndTime() == 0) {
                checkBox.setEnabled(false);
            }
            calendarTabel.setWidget(row, cell - 1, checkBox);
            cell++;
            if (cell == 7) {
                cell = 0;
            }
        }
        return rowspan + 1;
    }

    public KpiCheckBox getCalendarCheckBox(int day, final int year, final int month) {
        final KpiCheckBox checkBox = new KpiCheckBox(String.valueOf(day));
        checkBox.addClickHandler(clickEvent -> {
            final Date date = new Date(year, month, Integer.parseInt(checkBox.getText()), hour, minute);
            if (checkBox.getValue()) {
                requiringDateInterface.checkValidationRequiringDate(date, (result, courseScheduledItem) -> {
                    if (result != null && result == -2) {
                        checkBox.setValue(false);
                        Info.warn("Sorry can not clone schedule due to resource constraint");
                    } else if (result != null && result == -1) {
                        checkBox.setValue(false);
                        String courseName = courseScheduledItem.getCourseName() != null ? courseScheduledItem.getCourseName() : "";
                        String locationName = courseScheduledItem.getLocationName() != null ? courseScheduledItem.getLocationName() : "";
                        Info.warn("Sorry, can't save the schedule. Please set price for " + courseName + " course for " + locationName + " location");
                    } else if (result != null && result == -3) {
                        checkBox.setValue(false);
                        Info.show(tcStrings.availableDateForSchedule(), Info.Type.WARNING);
                    } else {
                        checkBox.setEnabled(false);
                        Info.show(" Cloned Course Schedule succesfull add");
                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SCHEDULED_COURSE_SAVED, null, null);
                    }
                });
            } else {
                requiringDateInterface.unCheckValidationRequiringDate(date);
            }
        });
        return checkBox;
    }

    private void drawRequiringDaysName(int row) {
        calendarTabel.setHTML(row, 1, weekDayShortName[1]);
        calendarTabel.setHTML(row, 2, weekDayShortName[2]);
        calendarTabel.setHTML(row, 3, weekDayShortName[3]);
        calendarTabel.setHTML(row, 4, weekDayShortName[4]);
        calendarTabel.setHTML(row, 5, weekDayShortName[5]);
        calendarTabel.setHTML(row, 6, weekDayShortName[6]);
        calendarTabel.setHTML(row, 7, weekDayShortName[0]);

        calendarTabel.getFlexCellFormatter().setStyleName(row, 1, "weekday");
        calendarTabel.getFlexCellFormatter().setWidth(row, 1, "40px");
        calendarTabel.getFlexCellFormatter().setStyleName(row, 2, "weekday");
        calendarTabel.getFlexCellFormatter().setWidth(row, 2, "40px");
        calendarTabel.getFlexCellFormatter().setStyleName(row, 3, "weekday");
        calendarTabel.getFlexCellFormatter().setWidth(row, 3, "40px");
        calendarTabel.getFlexCellFormatter().setStyleName(row, 4, "weekday");
        calendarTabel.getFlexCellFormatter().setWidth(row, 4, "40px");
        calendarTabel.getFlexCellFormatter().setStyleName(row, 5, "weekday");
        calendarTabel.getFlexCellFormatter().setWidth(row, 5, "40px");
        calendarTabel.getFlexCellFormatter().setStyleName(row, 6, "weekday");
        calendarTabel.getFlexCellFormatter().setWidth(row, 6, "40px");
        calendarTabel.getFlexCellFormatter().setStyleName(row, 7, "weekday");
        calendarTabel.getFlexCellFormatter().setWidth(row, 7, "40px");
    }

    public void addRequiringDate(RequiringDate requiringDate) {
        this.requiringDateInterface = requiringDate;
    }


    public interface RequiringDate {
        void checkValidationRequiringDate(Date date, Command command);

        void unCheckValidationRequiringDate(Date date);
    }

    public interface Command {
        void execute(Integer result, ScheduledCourseItem courseScheduledItem);
    }
}
