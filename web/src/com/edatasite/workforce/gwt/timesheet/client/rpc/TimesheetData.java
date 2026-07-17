package com.edatasite.workforce.gwt.timesheet.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TimeslotItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

public class TimesheetData implements IsSerializable, Serializable {

    private static final long serialVersionUID = 676600886877703637L;

    private int year;
    private int week;
    private boolean lastWeek;
    private int employeeId;
    private int[] dailyStatistics;

    private DateNonConvertable clientsToday;
    private DateNonConvertable today;
    private DateNonConvertable yesterday;
    private DateNonConvertable[] dates;

    private TaskTransfer[] transferTasks;
    private TimesheetDataItem[] items;
    private TimesheetReport[] weeklyStatistics;
    private TimesheetReport[] monthlyStatistices;
    private TimeslotItem timeslotItem;
    private SelectItem[] projects;
    private SelectItem[] workstream;
    private SelectItem[] clients;
    private SelectItem[] employees;
    private SelectItem[] approvers;

    public DateNonConvertable getClientsToday() {
        return clientsToday;
    }

    public void setClientsToday(DateNonConvertable clientsToday) {
        this.clientsToday = clientsToday;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public boolean isLastWeek() {
        return lastWeek;
    }

    public void setLastWeek(boolean lastWeek) {
        this.lastWeek = lastWeek;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int[] getDailyStatistics() {
        return dailyStatistics;
    }

    public void setDailyStatistics(int[] dailyStatistics) {
        this.dailyStatistics = dailyStatistics;
    }

    public DateNonConvertable getToday() {
        return today;
    }

    public void setToday(DateNonConvertable today) {
        this.today = today;
    }

    public DateNonConvertable getYesterday() {
        return yesterday;
    }

    public void setYesterday(DateNonConvertable yesterday) {
        this.yesterday = yesterday;
    }

    public DateNonConvertable[] getDates() {
        return dates;
    }

    public void setDates(DateNonConvertable[] dates) {
        this.dates = dates;
    }

    public TaskTransfer[] getTransferTasks() {
        return transferTasks;
    }

    public void setTransferTasks(TaskTransfer[] transferTasks) {
        this.transferTasks = transferTasks;
    }

    public TimesheetDataItem[] getItems() {
        return items;
    }

    public void setItems(TimesheetDataItem[] items) {
        this.items = items;
    }

    public TimesheetReport[] getWeeklyStatistics() {
        return weeklyStatistics;
    }

    public void setWeeklyStatistics(TimesheetReport[] weeklyStatistics) {
        this.weeklyStatistics = weeklyStatistics;
    }

    public TimesheetReport[] getMonthlyStatistices() {
        return monthlyStatistices;
    }

    public void setMonthlyStatistices(TimesheetReport[] monthlyStatistices) {
        this.monthlyStatistices = monthlyStatistices;
    }

    public TimeslotItem getTimeslotItem() {
        return timeslotItem;
    }

    public void setTimeslotItem(TimeslotItem timeslotItem) {
        this.timeslotItem = timeslotItem;
    }

    public SelectItem[] getProjects() {
        return projects;
    }

    public void setProjects(SelectItem[] projects) {
        this.projects = projects;
    }

    public SelectItem[] getWorkstream() {
        return workstream;
    }

    public void setWorkstream(SelectItem[] workstream) {
        this.workstream = workstream;
    }

    public SelectItem[] getClients() {
        return clients;
    }

    public void setClients(SelectItem[] clients) {
        this.clients = clients;
    }

    public SelectItem[] getEmployees() {
        return employees;
    }

    public void setEmployees(SelectItem[] employees) {
        this.employees = employees;
    }

    public SelectItem[] getApprovers() {
        return approvers;
    }

    public void setApprovers(SelectItem[] approvers) {
        this.approvers = approvers;
    }
}
