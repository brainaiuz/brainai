package com.edatasite.workforce.gwt.googlecalendar.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Aug 13, 2009
 * Time: 6:09:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class WorkforceEvents implements IsSerializable {

    private ArrayList<Appointment> events;
    private ArrayList<Appointment> calls;
    private ArrayList<Appointment> projects;
    private ArrayList<Appointment> tasks;
    private ArrayList<Appointment> issues;
    private ArrayList<Appointment> leaveRequests;
    private ArrayList<Appointment> performanceAppraisals;
    private ArrayList<Appointment> holidays;
    private ArrayList<Appointment> courses;

    /**
     * Working time of employee.
     */
    private int workingHourStart;
    private int workingHourEnd;
    private UsersCalendarSettingsItem calendarSettingsItem;
    private Date startDate;
    private Date endDate;
    private ArrayList<SelectItem> selectedUsers;
    private Integer scrollToHour;

    public ArrayList<Appointment> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Appointment> events) {
        this.events = events;
    }

    public ArrayList<Appointment> getCalls() {
        return this.calls;
    }

    public void setCalls(final ArrayList<Appointment> calls) {
        this.calls = calls;
    }

    public ArrayList<Appointment> getProjects() {
        return projects;
    }

    public void setProjects(ArrayList<Appointment> projects) {
        this.projects = projects;
    }

    public ArrayList<Appointment> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Appointment> tasks) {
        this.tasks = tasks;
    }

    public ArrayList<Appointment> getIssues() {
        return issues;
    }

    public void setIssues(ArrayList<Appointment> issues) {
        this.issues = issues;
    }

    public ArrayList<Appointment> getLeaveRequests() {
        return leaveRequests;
    }

    public void setLeaveRequests(ArrayList<Appointment> leaveRequests) {
        this.leaveRequests = leaveRequests;
    }

    public ArrayList<Appointment> getPerformanceAppraisals() {
        return performanceAppraisals;
    }

    public void setPerformanceAppraisals(ArrayList<Appointment> performanceAppraisals) {
        this.performanceAppraisals = performanceAppraisals;
    }

    public ArrayList<Appointment> getHolidays() {
        return holidays;
    }

    public void setHolidays(ArrayList<Appointment> holidays) {
        this.holidays = holidays;
    }

    public int getWorkingHourStart() {
        return workingHourStart;
    }

    public void setWorkingHourStart(int workingHourStart) {
        this.workingHourStart = workingHourStart;
    }

    public int getWorkingHourEnd() {
        return workingHourEnd;
    }

    public void setWorkingHourEnd(int workingHourEnd) {
        this.workingHourEnd = workingHourEnd;
    }

    public UsersCalendarSettingsItem getCalendarSettings() {
        return calendarSettingsItem;
    }

    public void setCalendarSettings(UsersCalendarSettingsItem calendarSettingsItem) {
        this.calendarSettingsItem = calendarSettingsItem;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setSelectedUsers(ArrayList<SelectItem> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }

    public ArrayList<SelectItem> getSelectedUsers() {
        return selectedUsers;
    }

    public Integer getScrollToHour() {
        return scrollToHour;
    }

    public void setScrollToHour(Integer scrollToHour) {
        this.scrollToHour = scrollToHour;
    }

    public ArrayList<Appointment> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Appointment> courses) {
        this.courses = courses;
    }
}
