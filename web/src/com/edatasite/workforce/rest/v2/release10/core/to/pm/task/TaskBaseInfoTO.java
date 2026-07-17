package com.edatasite.workforce.rest.v2.release10.core.to.pm.task;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilshod Madrahimov on 12/26/2017.
 */
public class TaskBaseInfoTO extends ResponseData {

    private String name;
    private String description;
    private Integer status_id;
    private Integer item_id;
    private String due_date;
    private String priority;
    private String tasks_presence;

    public TaskBaseInfoTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus_id() {
        return status_id;
    }

    public void setStatus_id(Integer status_id) {
        this.status_id = status_id;
    }

    public Integer getItem_id() {
        return item_id;
    }

    public void setItem_id(Integer item_id) {
        this.item_id = item_id;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getTasks_presence() {
        return tasks_presence;
    }

    public void setTasks_presence(String tasks_presence) {
        this.tasks_presence = tasks_presence;
    }
}
