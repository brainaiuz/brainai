package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ReportResult implements IsSerializable {

    private String clientName;
    private String projectName;
    private Integer projectID;
    private String departmentName;
    private String employeeName;
    private Integer employeeID;
    private int/*Double*/ sum;
    private Integer workstreamID;
    private String workstreamName;
    private String taskName;
    private Integer taskID;
    private Integer timesheetID;
    private boolean isBillable = false;
    private boolean isApproved = false;
    private boolean isRejected = false;
    private DateNonConvertable creatDate;
    private String description;
    private String percentCompleted;
    private Integer groupId;
    private String comment;
    private String status;
    private String timesheetStatus;
    private Integer estimatedTime;
    private String approvedHours;
    private String managerComment;
    private String hourType;
    private Integer approveReject;

    public ReportResult(int/*Double*/ sum, String clientName, String projectName,
                        String departmentName, String employeeName, String taskName,
                        DateNonConvertable creatDate, String description, String comment, String percentCompleted, Integer groupId) {
        this.sum = sum;
        this.clientName = clientName;
        this.projectName = projectName;
        this.departmentName = departmentName;
        this.employeeName = employeeName;
        this.taskName = taskName;
        this.creatDate = creatDate;
        this.description = description;
        this.percentCompleted = percentCompleted;
        this.groupId = groupId;
        this.comment = comment;
    }

    public ReportResult() {

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimesheetStatus() {
        return timesheetStatus;
    }

    public void setTimesheetStatus(String timesheetStatus) {
        this.timesheetStatus = timesheetStatus;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int/*Double*/ getSum() {
        return sum;
    }

    public void setSum(int/*Double*/ sum) {
        this.sum = sum;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Integer getWorkstreamID() {
        return workstreamID;
    }

    public void setWorkstreamID(Integer workstreamID) {
        this.workstreamID = workstreamID;
    }

    public String getWorkstreamName() {
        return workstreamName;
    }

    public void setWorkstreamName(String workstreamName) {
        this.workstreamName = workstreamName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public DateNonConvertable getCreatDate() {
        return creatDate;
    }

    public void setCreatDate(DateNonConvertable creatDate) {
        this.creatDate = creatDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPercentCompleted() {
        return percentCompleted;
    }

    public void setPercentCompleted(String percentCompleted) {
        this.percentCompleted = percentCompleted;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Integer getTaskID() {
        return taskID;
    }

    public void setTaskID(Integer taskID) {
        this.taskID = taskID;
    }

    public Integer getTimesheetID() {
        return timesheetID;
    }

    public void setTimesheetID(Integer timesheetID) {
        this.timesheetID = timesheetID;
    }

    public boolean isBillable() {
        return isBillable;
    }

    public void setBillable(boolean billable) {
        isBillable = billable;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public boolean isRejected() {
        return isRejected;
    }

    public void setRejected(boolean rejected) {
        isRejected = rejected;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public Integer getEstimatedTime() {
        return estimatedTime;
    }

    public void setEstimatedTime(Integer estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public String getApprovedHours() {
        return approvedHours != null ? approvedHours : "";
    }

    public void setApprovedHours(String approvedHours) {
        this.approvedHours = approvedHours;
    }

    public String getManagerComment() {
        return managerComment;
    }

    public void setManagerComment(String managerComment) {
        this.managerComment = managerComment;
    }

    public String getHourType() {
        return hourType;
    }

    public void setHourType(String hourType) {
        this.hourType = hourType;
    }

    public Integer getApproveReject() {
        return approveReject;
    }

    public void setApproveReject(Integer approveReject) {
        this.approveReject = approveReject;
    }
}
