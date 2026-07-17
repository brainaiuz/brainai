package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.scheduledcourse;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse.ScheduledCourseItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse.TimeSlotItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialPanel;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 01/09/12
 * Time: 01:33
 * To change this template use File | Settings | File Templates.
 */
public class CloneCourseScheduleView extends CustomForm2 {

    private static TCStrings tcStrings = TCStrings.App.get();
    private ScheduledCourseListView courseListView;

    private Integer courseScheduleId;
    private ScheduledCourseItem courseScheduledItem;
    //    private CourseScheduleRequringCalendar courseScheduleRequringCalendar;
    private Integer weekStart;
    private MaterialPanel cloneWidget;
    private DatePicker startDateClone;

    public CloneCourseScheduleView(Integer courseScheduleId) {
        super("cloneCourseSchedule", tcStrings.clonedCourseSchedule());
        this.courseScheduleId = courseScheduleId;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.CLONE_SCHEDULED_COURSE_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        TCService.App.get().getCourseSchedule(courseScheduleId, false, new AsyncCallback<ScheduledCourseItem>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ScheduledCourseItem scheduledCourseItem) {
                LoadingPanel.loading(false);
                courseScheduledItem = scheduledCourseItem;
//                courseScheduleRequringCalendar.drawRequiringCalendarWidget(courseScheduledItem);
            }
        });
    }

    @Override
    protected void addButtons() {
        customizeButton.setVisible(false);
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        return null;
    }

    @Override
    protected void registerFields() {

        startDateClone = new DatePicker();
        startDateClone.setDate(new Date());
        startDateClone.ensureDebugId("startDateClone");
        startDateClone.addChangeHandler(changeEvent -> {
            setCloneOfCourseSchedule(startDateClone.getDate());
        });


        cloneWidget = new MaterialPanel();
        cloneWidget.getElement().getStyle().setPadding(0, Style.Unit.PX);
        cloneWidget.getElement().getStyle().setMarginTop(15, Style.Unit.PX);
        cloneWidget.add(new GRow(new GColumn(GColumnEnum.COL_5, startDateClone)));

        weekStart = Integer.valueOf(Utils.userSettings.get(Constants.OVERALL_DATE_PICKER_WEEK_START)) - 1;

        addTitleField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.COURSE_SCHEDULE_REQUIRING_DATAILS, tcStrings.clonedCourseScheduleDetails());
        addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.COURSE_SCHEDULE_REQUIRING, cloneWidget, wfmStrings.clonE());
        show();

    }

    private void setCloneOfCourseSchedule(Date date) {

        courseScheduledItem.setStartDate(date);
        courseScheduledItem.setEndDate(getCalculatedEndDate(courseScheduledItem));
        LoadingPanel.loading(true);
        TCService.App.get().setCloneOfCourseSchedule(courseScheduledItem, new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Integer success) {
                LoadingPanel.loading(false);
                Info.show(" Cloned Course Schedule succesfull add");
                closeTab();
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SCHEDULED_COURSE_SAVED, null, null);

            }
        });
    }

    @Override
    protected void initPredefinedValues() {

    }

    /**
     * get calculated end date for check availability schedule items
     *
     * @return
     */
    private Date getCalculatedEndDate(ScheduledCourseItem scheduledCourseItem) {
        Date startDate = (Date) scheduledCourseItem.getStartDate().clone();
        startDate.setHours(0);
        startDate.setMinutes(0);
        startDate.setSeconds(0);

        Long startTime = (scheduledCourseItem.getStartDate().getTime() - startDate.getTime()) / 60000;
        Integer scStartTime = startTime.intValue();
        Integer cDuration = scheduledCourseItem.getDuration() * 60;

        Date date = (Date) startDate.clone();
        int duration = 0;
        int scheduleDay = 0;
        do {
            TimeSlotItem timeSlotItem = scheduledCourseItem.getTimeSlotItems().get(date.getDay());

            if (courseScheduledItem.isEnabledOvertime() && weekStart != null && (timeSlotItem.getStartTime() == 0 || timeSlotItem.getEndTime() == 0)) {
                timeSlotItem = scheduledCourseItem.getTimeSlotItems().get(weekStart);
            }

            if (timeSlotItem.getStartTime() != 0 && timeSlotItem.getEndTime() != 0) {
                int totalDurationInDay = 0; //total duration in day
                Integer scTime = scStartTime; //scheduled course start time

                //calculate lunch time from time slot
                int lunchTime = timeSlotItem.getLunchEnd() - timeSlotItem.getLunchStart();

                //calculate break time from time slot
                int breakTime = timeSlotItem.getCoffeeEnd() - timeSlotItem.getCoffeeStart();

                //calculate duration hour(s) in day
                if (scTime != 0) {
                    duration = scTime + cDuration;

                    //lunch time applying to schedule duration
                    if (scTime <= timeSlotItem.getLunchStart() && duration > timeSlotItem.getLunchStart()) {
                        totalDurationInDay += lunchTime;
                    }

                    //break time applying to schedule duration
                    if (scTime <= timeSlotItem.getCoffeeStart() && (duration + lunchTime) > timeSlotItem.getCoffeeStart()) {
                        totalDurationInDay += breakTime;
                    }

                    totalDurationInDay += duration;

                    scTime = 0;
                } else {
                    duration = timeSlotItem.getStartTime() + cDuration;

                    totalDurationInDay = duration + lunchTime + breakTime;
                }

                //split day(s) of course schedule duration
                if (timeSlotItem.getEndTime() < totalDurationInDay) {
                    cDuration = totalDurationInDay - timeSlotItem.getEndTime();
                    date.setDate(date.getDate() + 1);
                } else {
                    //apply lunch time to duration in day
                    if (scStartTime <= timeSlotItem.getLunchStart() && duration > timeSlotItem.getLunchStart()) {
                        duration += lunchTime;
                    }

                    //apply break time to duration in day
                    if (scStartTime <= timeSlotItem.getCoffeeStart() && (duration + lunchTime) > timeSlotItem.getCoffeeStart()) {
                        duration += breakTime;
                    }

                    date.setMinutes(duration);
                    cDuration = 0;
                }

                scheduleDay++;
            } else {
                date.setDate(date.getDate() + 1);
            }
        } while (cDuration > 0);

        scheduledCourseItem.setScheduleDuration(scheduleDay);
        return date;
    }

    private boolean isAvailableDateForSchedule(Date date) {
        TimeSlotItem timeSlotItem = courseScheduledItem.getTimeSlotItems().get(date.getDay());
        if (courseScheduledItem.isEnabledOvertime() && weekStart != null && (timeSlotItem.getStartTime() == 0 || timeSlotItem.getEndTime() == 0)) {
            timeSlotItem = courseScheduledItem.getTimeSlotItems().get(weekStart);
        }

        if (timeSlotItem.getStartTime() == 0 && timeSlotItem.getEndTime() == 0) {
            return false;
        }

        Date dayStart = (Date) date.clone();
        dayStart.setHours(0);
        dayStart.setMinutes(0);
        dayStart.setSeconds(0);

        int timeInDay = Long.valueOf((date.getTime() - dayStart.getTime()) / 60000).intValue();

        return timeInDay >= timeSlotItem.getStartTime()
                && timeInDay <= timeSlotItem.getEndTime();
    }

    @Override
    public String getIconStyle() {
        return "bgMark cloned-course-schedule-icon";
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
