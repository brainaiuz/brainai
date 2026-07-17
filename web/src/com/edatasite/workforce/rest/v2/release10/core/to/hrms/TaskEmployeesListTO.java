package com.edatasite.workforce.rest.v2.release10.core.to.hrms;

import com.edatasite.workforce.gwt.core.client.rpc.task.TaskInvolvedMember;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

public class TaskEmployeesListTO extends ResponseData {

    private Integer taskId;
    private String taskName;
    private TaskInvolvedMember[] assignees;

    public TaskEmployeesListTO() {
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public TaskInvolvedMember[] getAssignees() {
        return assignees;
    }

    public void setAssignees(TaskInvolvedMember[] assignees) {
        this.assignees = assignees;
    }
}
