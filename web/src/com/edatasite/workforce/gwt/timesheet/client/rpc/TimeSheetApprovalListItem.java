package com.edatasite.workforce.gwt.timesheet.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: Abdulaziz
 * Date: 04.05.2009
 * Time: 16:49:40
 */
public class TimeSheetApprovalListItem implements IsSerializable {
    public static String ACTION = "action";
    public static String EMPLOYEENAME = "employeeName";
    public static String STATUS = "status";
    public static String PROJECTNAME = "projectName";
    public static String FROMDATE = "fromDate";
    public static String ENDDATE = "endDate";
    public static String APPROVER = "approver";
    public static String SUBMITTED_DATE = "submittedDate";
    public static String APPROVAL_DATE = "approvalDate";
    public static String TIMESPENT = "timeSpent";
    public static String APPROVED = "approvedHours";

    public static final String WAITING = "_WAITING";
    public static final String APPROVEDS = "_APPROVED";
    public static final String REJECTED = "_REJECTED";

    private Integer id;
    private String employeeName;
    private String projectName;
    private DateNonConvertable fromDate;
    private DateNonConvertable endDate;
    private String timeSpent;
    private String status;
    private String statusCode;
    private String approver;
    private String approvedHours;
    private DateNonConvertable submittedDate;
    private DateNonConvertable approvalDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public DateNonConvertable getFromDate() {
        return fromDate;
    }

    public void setFromDate(DateNonConvertable fromDate) {
        this.fromDate = fromDate;
    }

    public DateNonConvertable getEndDate() {
        return endDate;
    }

    public void setEndDate(DateNonConvertable endDate) {
        this.endDate = endDate;
    }

    public String getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(String timeSpent) {
        this.timeSpent = timeSpent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getApprovedHours() {
        return approvedHours;
    }

    public void setApprovedHours(String approvedHours) {
        this.approvedHours = approvedHours;
    }

    public DateNonConvertable getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(DateNonConvertable submittedDate) {
        this.submittedDate = submittedDate;
    }

    public DateNonConvertable getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(DateNonConvertable approvalDate) {
        this.approvalDate = approvalDate;
    }
}