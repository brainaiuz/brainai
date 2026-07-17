package com.edatasite.workforce.gwt.task.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class TaskTimeSheetSuggestItem implements IsSerializable {
  private   Integer id;
    private String comment;
    private String entryDate;
    private Integer taskId;
    private Integer timeSpent;
    private String number;
    private String name;
    private String description;
    private String startDate;
    private String dueDate;

    public TaskTimeSheetSuggestItem() {
    }

    public TaskTimeSheetSuggestItem(Integer id, String comment, String entryDate, Integer taskId, Integer timeSpent, String number, String name, String description, String startDate, String dueDate) {
        this.id = id;
        this.comment = comment;
        this.entryDate = entryDate;
        this.taskId = taskId;
        this.timeSpent = timeSpent;
        this.number = number;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.dueDate = dueDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getTimeSpent() {
        return timeSpent;
    }

    public void setTimeSpent(Integer timeSpent) {
        this.timeSpent = timeSpent;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}

