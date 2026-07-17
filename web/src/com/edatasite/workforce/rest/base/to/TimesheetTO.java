package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.timesheet.client.rpc.FastTaskTransfer;
import com.edatasite.workforce.gwt.timesheet.client.rpc.FastTimesheetData;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetDataItem;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetReport;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Umidbek.
 */
public class TimesheetTO implements IsSerializable {

    private Long today;
    private Long selectedDate;

    private UserTO user;
    private TimesheetTotalsTO totals;
    private ArrayList<TimesheetDayTO> days = new ArrayList<>();
    private ArrayList<TimesheetProjectTO> projects = new ArrayList<>();

    public TimesheetTO() {
    }


    public TimesheetTO(FastTimesheetData item, Boolean isDailyData, Long selectedDate) {
        initialize(item, isDailyData, selectedDate);
    }

    private void initialize(FastTimesheetData item, Boolean isDailyData, Long selectedDate) {
        HashMap<String, Integer> monthlyReport = new HashMap<>();
        HashMap<String, Integer> weeklyReport = new HashMap<>();

        this.selectedDate = selectedDate;
        this.today = item.getToday().getDateLong();
        this.user = new UserTO(item.getEmployeeId(), null);

        this.totals = new TimesheetTotalsTO();

        this.totals.setActualWeekly(0);
        this.totals.setPlannedWeekly(0);
        this.totals.setActualMonthly(0);
        this.totals.setPlannedMonthly(item.getTimeslotItem().getMonthlyPlannedTime());

        for (TimesheetReport report : item.getWeeklyStatistics()) {
            weeklyReport.put(report.getProjectName(), report.getSum());
        }
        for (TimesheetReport report : item.getMonthlyStatistices()) {
            monthlyReport.put(report.getProjectName(), report.getSum());
            this.totals.setActualMonthly(this.totals.getActualMonthly() + report.getSum());
        }

        for (int i = 0; i < item.getDates().length; i++) {
            TimesheetDayTO dayTO = new TimesheetDayTO();
            dayTO.setDate(item.getDates()[i].getDateLong());
            dayTO.setPlannedTime(item.getTimeslotItem().getWeekDaysPlannedTime()[i]);
            dayTO.setIsHoliday(item.getTimeslotItem().getHoliday()[i]);
            dayTO.setIsLeaveRequested(item.getTimeslotItem().getLR()[i]);

            this.totals.setPlannedWeekly(this.totals.getPlannedWeekly() + dayTO.getPlannedTime());

            this.totals.getActualDaily().add(item.getDailyStatistics()[i]);
            this.totals.getPlannedDaily().add(item.getTimeslotItem().getWeekDaysPlannedTime()[i]);

            this.days.add(dayTO);
        }

        HashMap<Integer, TimesheetProjectTO> toHashMap = new HashMap<>();
        HashMap<Integer, Integer> projectDailyHourTotalMap = new HashMap<>();

        for (FastTaskTransfer task : item.getTransferTasks()) {
            TimesheetProjectTO projectTO;
            Integer projectDailyHourTotal = 0;

            if (toHashMap.containsKey(task.getProjectId())) {
                projectTO = toHashMap.get(task.getProjectId());
                projectDailyHourTotal = projectDailyHourTotalMap.get(task.getProjectId());
            } else {
                projectTO = new TimesheetProjectTO();
                projectTO.setProject(new ProjectTO(task.getProjectId(), task.getProjectName()));
                projectTO.setWeeklyTotal(weeklyReport.get(projectTO.getProject().getName()));
                projectTO.setMonthlyTotal(monthlyReport.get(projectTO.getProject().getName()));
                toHashMap.put(task.getProjectId(), projectTO);
                projectDailyHourTotalMap.put(task.getProjectId(), 0);
            }

            TimesheetTaskTO timesheetTaskTO = new TimesheetTaskTO();
            TaskTO taskTO = new TaskTO();

            taskTO.setId(task.getTaskId());
            taskTO.setNumber(task.getTaskNumber());
            taskTO.setName(task.getEmplTaskName());
            taskTO.setStatus(new SelectItemTO(task.getTaskStatus().getStatus(), task.getTaskStatus().getStatusName()));
            taskTO.setStartDate(WrapUtils.dateToLong(task.getStartDate()));
            taskTO.setEndDate(WrapUtils.dateToLong(task.getEndDate()));

            taskTO.setPercentComplete((double) task.getPercentCompleted());
            taskTO.setHoursSpent(task.getTotalMinutes());
            taskTO.setEstimatedTime(task.getEstimatedTime());

            timesheetTaskTO.setTask(taskTO);

            if (task.getStatData() == null) {
                timesheetTaskTO.setWeeklyTotal(0);
                timesheetTaskTO.setMonthlyTotal(0);
            } else {
                timesheetTaskTO.setWeeklyTotal(task.getStatData().getWeekly());
                timesheetTaskTO.setMonthlyTotal(task.getStatData().getMonthly());
            }

            for (TimesheetDataItem dataItem : task.getDataItems()) {
                if (dataItem != null) {
                    TimesheetTaskEntryTO taskEntryTO = new TimesheetTaskEntryTO();
                    taskEntryTO.setComment(dataItem.getComment());
                    taskEntryTO.setMinutes(dataItem.getMinutes());
                    taskEntryTO.setStatus(dataItem.getStatus());
                    timesheetTaskTO.getEntries().add(taskEntryTO);
                    if (Boolean.TRUE.equals(isDailyData) && selectedDate != null && selectedDate.equals(dataItem.getDate().getTime())) {
                        taskTO.setDailyTotal(dataItem.getMinutes());
                    }
                }
            }

            if (timesheetTaskTO.getWeeklyTotal() != null) {
                this.totals.setActualWeekly(this.totals.getActualWeekly() + timesheetTaskTO.getWeeklyTotal());
            }
            if (Boolean.TRUE.equals(isDailyData)) {
                projectDailyHourTotal = projectDailyHourTotal + (taskTO.getDailyTotal() != null ? taskTO.getDailyTotal() : 0);
                projectTO.getProject().setDailyTotal(projectDailyHourTotal);
                projectDailyHourTotalMap.put(task.getProjectId(), projectDailyHourTotal);
            }
            projectTO.getTasks().add(timesheetTaskTO);
        }

        this.projects = new ArrayList<TimesheetProjectTO>(toHashMap.values());
    }

    public Long getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Long selectedDate) {
        this.selectedDate = selectedDate;
    }

    public UserTO getUser() {
        return user;
    }

    public void setUser(UserTO user) {
        this.user = user;
    }

    public TimesheetTotalsTO getTotals() {
        return totals;
    }

    public void setTotals(TimesheetTotalsTO totals) {
        this.totals = totals;
    }

    public ArrayList<TimesheetDayTO> getDays() {
        return days;
    }

    public void setDays(ArrayList<TimesheetDayTO> days) {
        this.days = days;
    }

    public ArrayList<TimesheetProjectTO> getProjects() {
        return projects;
    }

    public void setProjects(ArrayList<TimesheetProjectTO> projects) {
        this.projects = projects;
    }

    public Long getToday() {
        return today;
    }

    public void setToday(Long today) {
        this.today = today;
    }
}
