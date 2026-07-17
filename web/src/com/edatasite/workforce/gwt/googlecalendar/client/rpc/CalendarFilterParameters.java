package com.edatasite.workforce.gwt.googlecalendar.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Mar 24, 2010
 * Time: 3:21:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class CalendarFilterParameters implements IsSerializable {

    private int days;
    private long dueDate;
    private boolean showEvent;
    private boolean showCall;
    private boolean showProject;
    private boolean showTasks;
    private boolean showIssues;
    private boolean showLeaveRequest;
    private boolean showPA;
    private boolean showHolidays;

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public long getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isShowEvent() {
        return showEvent;
    }

    public void setShowEvent(boolean showEvent) {
        this.showEvent = showEvent;
    }

    public boolean isShowCall() {
        return this.showCall;
    }

    public void setShowCall(final boolean showCall) {
        this.showCall = showCall;
    }

    public boolean isShowProject() {
        return showProject;
    }

    public void setShowProject(boolean showProject) {
        this.showProject = showProject;
    }

    public boolean isShowTasks() {
        return showTasks;
    }

    public void setShowTasks(boolean showTasks) {
        this.showTasks = showTasks;
    }

    public boolean isShowIssues() {
        return showIssues;
    }

    public void setShowIssues(boolean showIssues) {
        this.showIssues = showIssues;
    }

    public boolean isShowLeaveRequest() {
        return showLeaveRequest;
    }

    public void setShowLeaveRequest(boolean showLeaveRequest) {
        this.showLeaveRequest = showLeaveRequest;
    }

    public boolean isShowPA() {
        return showPA;
    }

    public void setShowPA(boolean showPA) {
        this.showPA = showPA;
    }

    public boolean isShowHolidays() {
        return showHolidays;
    }

    public void setShowHolidays(boolean showHolidays) {
        this.showHolidays = showHolidays;
    }

    public HashMap<String, String> getRequestParameters() {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("dueDate", getAsString(dueDate));
        parameters.put("days", getAsString(days));
        parameters.put("showProject", getAsString(showProject));
        parameters.put("showEvent", getAsString(showEvent));
        parameters.put("showTasks", getAsString(showTasks));
        parameters.put("showIssues", getAsString(showIssues));
        parameters.put("showLeaveRequest", getAsString(showLeaveRequest));
        parameters.put("showPA", getAsString(showPA));
        parameters.put("showHolidays", getAsString(showHolidays));
        return parameters;
    }

    private String getAsString(Object obj) {
        return obj == null ? "" : String.valueOf(obj);
    }
}
