package com.edatasite.workforce.rest.v2.release10.core.to.pm.task;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

public class TaskDetailsInfoResultTO extends ResponseData {
    private TaskDetailsItemTO item;

    public TaskDetailsInfoResultTO(TaskDetailsItemTO item) {
        this.item = item;
    }

    public TaskDetailsItemTO getItem() {
        return item;
    }

    public void setItem(TaskDetailsItemTO item) {
        this.item = item;
    }
}
