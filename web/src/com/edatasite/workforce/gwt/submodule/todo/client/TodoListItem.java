package com.edatasite.workforce.gwt.submodule.todo.client;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 5/19/11
 * Time: 2:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class TodoListItem implements IsSerializable {

    public static final String PUBLIC = "PUBLIC";
    public static final String PRIVATE = "PRIVATE";

    public static final String HIGH = "_TASK_PRIORITY_HIGH";
    public static final String MEDIUM = "_TASK_PRIORITY_MEDIUM";
    public static final String LOW = "_TASK_PRIORITY_LOW";

    private String reporterName;
    private String priorityName;
    private String priorityCode;
    private Integer statusID;
    private String taskName;
    private Integer taskID;

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getPriorityName() {
        return priorityName;
    }

    public void setPriorityName(String priorityName) {
        this.priorityName = priorityName;
    }

    public String getPriorityCode() {
        return priorityCode;
    }

    public void setPriorityCode(String priorityCode) {
        this.priorityCode = priorityCode;
    }

    public Integer getStatusID() {
        return statusID;
    }

    public void setStatusID(Integer statusID) {
        this.statusID = statusID;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Integer getTaskID() {
        return taskID;
    }

    public void setTaskID(Integer taskID) {
        this.taskID = taskID;
    }
}
