package com.edatasite.workforce.rest.v2.release10.core.to.pm.task;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Anvar Akramov on 4/10/2018.
 */
public class TaskAddTO extends ResponseData {

    private Boolean ignore_warning;
    private CreateTaskDetailsTO task;

    public TaskAddTO() {
    }

    public Boolean getIgnore_warning() {
        return ignore_warning;
    }

    public void setIgnore_warning(Boolean ignore_warning) {
        this.ignore_warning = ignore_warning;
    }

    public CreateTaskDetailsTO getTask() {
        return task;
    }

    public void setTask(CreateTaskDetailsTO task) {
        this.task = task;
    }
}
