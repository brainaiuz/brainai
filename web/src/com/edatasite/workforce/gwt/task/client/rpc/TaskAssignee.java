package com.edatasite.workforce.gwt.task.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.IdTime;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: iabdullo
 * Date: 15.09.14 17:31
 */
public class TaskAssignee implements IsSerializable{
    private Integer taskId;
    private IdTime[] assignees;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public IdTime[] getAssignees() {
        return assignees;
    }

    public void setAssignees(IdTime[] assignees) {
        this.assignees = assignees;
    }
}
