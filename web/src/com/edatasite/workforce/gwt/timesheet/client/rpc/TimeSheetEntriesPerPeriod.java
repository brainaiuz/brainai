package com.edatasite.workforce.gwt.timesheet.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Abdulaziz
 * Date: 30.04.2009
 * Time: 18:29:28
 * To change this template use File | Settings | File Templates.
 */
public class TimeSheetEntriesPerPeriod implements IsSerializable {

    private Integer projectId;
//    private List<Integer> projectIds;
    private DateNonConvertable fromDate;
    private DateNonConvertable toDate;
    private TaskTimeSheetEntry[] entries;
//    private int entriesTotalCount;
    private TimesheetSettings settings;
    private Integer employeeID;
    private Integer approverID;

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

/*
    public List<Integer> getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(List<Integer> projectIds) {
        this.projectIds = projectIds;
    }
*/

    public TaskTimeSheetEntry[] getEntries() {
        return entries;
    }

    public void setEntries(TaskTimeSheetEntry[] entries) {
        this.entries = entries;
    }

    public DateNonConvertable getFromDate() {
        return fromDate;
    }

    public void setFromDate(DateNonConvertable fromDate) {
        this.fromDate = fromDate;
    }

    public DateNonConvertable getToDate() {
        return toDate;
    }

    public void setToDate(DateNonConvertable toDate) {
        this.toDate = toDate;
    }

/*
    public int getEntriesTotalCount() {
        return entriesTotalCount;
    }

    public void setEntriesTotalCount(int entriesTotalCount) {
        this.entriesTotalCount = entriesTotalCount;
    }
*/

    public TimesheetSettings getSettings() {
        return settings;
    }

    public void setSettings(TimesheetSettings settings) {
        this.settings = settings;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public Integer getApproverID() {
        return approverID;
    }

    public void setApproverID(Integer approverID) {
        this.approverID = approverID;
    }
}
