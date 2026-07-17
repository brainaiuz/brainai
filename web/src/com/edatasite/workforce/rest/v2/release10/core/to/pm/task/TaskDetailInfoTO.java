package com.edatasite.workforce.rest.v2.release10.core.to.pm.task;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.status.FlowSettingsTO;

/**
 * Created by Dilshod Madrahimov on 12/26/2017.
 */
public class TaskDetailInfoTO extends ResponseData {
    private TaskBaseInfoTO base_info;
    private FlowSettingsTO status;

    public TaskDetailInfoTO() {
    }

    public TaskBaseInfoTO getBase_info() {
        return base_info;
    }

    public void setBase_info(TaskBaseInfoTO base_info) {
        this.base_info = base_info;
    }

    public FlowSettingsTO getStatus() {
        return status;
    }

    public void setStatus(FlowSettingsTO status) {
        this.status = status;
    }
}
