package com.edatasite.workforce.gwt.timesheet.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TaskStatus implements IsSerializable {

    private int taskId;
    private int employeeTaskId;
    private int status;
    private String statusName;
    private String priority;
    private boolean forceChangeStatus;

    public TaskStatus() {

    }

    public TaskStatus(int taskId, int status) {
        super();
        this.taskId = taskId;
        this.status = status;
    }

    public boolean isForceChangeStatus() {
        return forceChangeStatus;
    }

    public void setForceChangeStatus(boolean forceChangeStatus) {
        this.forceChangeStatus = forceChangeStatus;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public int getEmployeeTaskId() {
        return employeeTaskId;
    }

    public void setEmployeeTaskId(int employeeTaskId) {
        this.employeeTaskId = employeeTaskId;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
