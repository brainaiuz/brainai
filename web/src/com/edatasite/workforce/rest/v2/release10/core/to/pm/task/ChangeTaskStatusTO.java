package com.edatasite.workforce.rest.v2.release10.core.to.pm.task;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilsh0d Madrahimov on 12/27/2017.
 */
public class ChangeTaskStatusTO extends ResponseData {

    private Integer task_id;
    private Integer status_id;

    public ChangeTaskStatusTO() {
    }

    public Integer getTask_id() {
        return task_id;
    }

    public void setTask_id(Integer task_id) {
        this.task_id = task_id;
    }

    public Integer getStatus_id() {
        return status_id;
    }

    public void setStatus_id(Integer status_id) {
        this.status_id = status_id;
    }
}
