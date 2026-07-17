package com.edatasite.workforce.gwt.timesheet.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TimeslotItem;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.timesheet.client.ui.spreadsheet.TimesheetModelCallback;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FastTimesheetModel {

    private final TimesheetServiceAsync timesheetService = TimesheetService.App.get();

    private DateNonConvertable[] dates;
    private DateNonConvertable today;

    private int[] dailyStatistics;
    private SelectItem[] tasks;
    private SelectItem[] projects;
    private SelectItem[] clients;
    private SelectItem[] workstreams;
    private TaskStatus[] statuses;
    private FastTaskTransfer[] transferTasks;
    private TimesheetReport[] weeklyStatistics;
    private TimesheetReport[] monthlyStatistics;
    private TimeslotItem timeslotItem;
    private TimesheetSettings settings;

    private HashMap<Integer, Float> percentByTaskId = new HashMap<>();
    private HashMap<Integer, Map<Date, TimesheetDataItem>> itemByTaskId = new HashMap<>();

    public FastTimesheetModel(TimesheetFilterData data) {
        projects = data.getProjects();
        workstreams = data.getWorkstreams();
        clients = data.getClients();
    }

    public FastTimesheetModel(FastTimesheetData data) {
        transferTasks = data.getTransferTasks();
        dates = data.getDates();
        today = data.getToday();
        projects = data.getProjects();
        clients = data.getClients();
        workstreams = data.getWorkstream();
        statuses = new TaskStatus[transferTasks.length];
        dailyStatistics = data.getDailyStatistics();
        weeklyStatistics = data.getWeeklyStatistics();
        monthlyStatistics = data.getMonthlyStatistices();
        timeslotItem = data.getTimeslotItem();
    }

    public SelectItem[] getTasks() {
        return tasks;
    }

    public void setTasks(SelectItem[] tasks) {
        this.tasks = tasks;
    }

    public DateNonConvertable[] getDates() {
        return dates;
    }

    public void setDates(DateNonConvertable[] dates) {
        this.dates = dates;
    }

    public DateNonConvertable getDate(int index) {
        return dates[index];
    }

    public TimesheetDataItem getItem(int employeeTaskId, Date date) {
        Map itemByDate = itemByTaskId.get(employeeTaskId);
        TimesheetDataItem item = itemByDate != null ? (TimesheetDataItem) itemByDate.get(date) : null;
        if (item == null) {
            item = new TimesheetDataItem();
            item.setEmployeeTaskID(employeeTaskId);
            item.setDate(date);
        }
        return item;
    }

    public Float getPercent(Integer employeeTaskId) {
        return percentByTaskId.get(employeeTaskId) == null ? Float.valueOf("0.0") : percentByTaskId.get(employeeTaskId);
    }

    public int getMinutes(Integer employeeTaskId, Date date) {
        TimesheetDataItem item = getItem(employeeTaskId, date);
        if (item == null) {
            return 0;
        }

        return item.getMinutes();
    }

    public void update(TimesheetDataItem item) {
        for (FastTaskTransfer transferTask : transferTasks) {
            if (transferTask.getEmplTaskId() == item.getEmployeeTaskID()) {
                if (transferTask.getTaskStatus().getStatus() == 2) { //Not Started
                    TaskStatus taskStatus = new TaskStatus();
                    taskStatus.setEmployeeTaskId(transferTask.getEmplTaskId());
                    taskStatus.setTaskId(transferTask.getTaskId());
                    taskStatus.setStatus(3);

                    timesheetService.updateStatus(taskStatus, new AbstractAsyncCallback<Void>() {
                        public void success(Void result) {
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TIMESHEET_TASK_STATUS_CHANGED, result, null);
                        }
                    });
                }
            }
        }
        indexItem(item);
    }

    private void indexItem(TimesheetDataItem item) {
        Map<Date, TimesheetDataItem> itemByDate = itemByTaskId.computeIfAbsent(item.getEmployeeTaskID(), k -> new HashMap<>());

        itemByDate.put(item.getDate(), item);
    }

    public void applyUpdates(final TimesheetDataItem item, final TimesheetModelCallback callback) {
        if (item != null) {
            timesheetService.applyUpdates(item, null, new AbstractAsyncCallback<Integer>() {
                public void success(Integer itemId) {
                    update(item);
                    item.setId(itemId);

                    indexItem(item);
                    callback.onSuccess();
                }

                public void failure(Throwable result) {
                    callback.onFailure();
                }
            });
        }
    }

    public TaskStatus[] getStatuses() {
        return statuses;
    }

    public void setStatuses(TaskStatus[] statuses) {
        this.statuses = statuses;
    }

    public FastTaskTransfer[] getTransferTasks() {
        return transferTasks;
    }

    public void setTransferTasks(FastTaskTransfer[] transferTasks) {
        this.transferTasks = transferTasks;
    }

    public DateNonConvertable getToday() {
        return today;
    }

    public void setToday(DateNonConvertable today) {
        this.today = today;
    }

    public int[] getDailyStatistics() {
        return dailyStatistics;
    }

    public TimesheetReport[] getWeeklyStatistics() {
        return weeklyStatistics;
    }

    public TimesheetReport[] getMonthlyStatistics() {
        return monthlyStatistics;
    }

    public TimeslotItem getTimeslotItem() {
        return timeslotItem;
    }

    public SelectItem[] getProjects() {
        return projects;
    }

    public SelectItem[] getWorkstreams() {
        return workstreams;
    }

    public SelectItem[] getClients() {
        return clients;
    }

    public void setClients(SelectItem[] clients) {
        this.clients = clients;
    }

    public TimesheetSettings getSettings() {
        return settings;
    }

    public void setSettings(TimesheetSettings settings) {
        this.settings = settings;
    }
}
