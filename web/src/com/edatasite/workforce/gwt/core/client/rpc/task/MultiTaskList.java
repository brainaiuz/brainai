package com.edatasite.workforce.gwt.core.client.rpc.task;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: Apr 18, 2009
 * Time: 12:21:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class MultiTaskList implements IsSerializable {

    public static final String FROM_TODO_LIST = "FROM_TODO_LIST";

    private String createdFrom;
    private Integer projectID;
    private Integer workstreamID;
    private String workstreamName;
    private TaskSingleItem[] taskSingleItems;

    public String getCreatedFrom() {
        return createdFrom;
    }

    public void setCreatedFrom(String createdFrom) {
        this.createdFrom = createdFrom;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
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

    public TaskSingleItem[] getTaskSingleItems() {
        return taskSingleItems;
    }

    public void setTaskSingleItems(TaskSingleItem[] taskSingleItems) {
        this.taskSingleItems = taskSingleItems;
    }
}
