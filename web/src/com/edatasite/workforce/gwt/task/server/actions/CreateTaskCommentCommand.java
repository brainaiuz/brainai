package com.edatasite.workforce.gwt.task.server.actions;

import com.edatasite.workforce.gwt.core.server.servlets.WfmCommand;

public class CreateTaskCommentCommand extends WfmCommand {

    private Integer taskUID;
    private String text;

    public Integer getTaskUID() {
        return taskUID;
    }

    public void setTaskUID(Integer taskUID) {
        this.taskUID = taskUID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
