package com.edatasite.workforce.rest.v2.release10.core.to.status;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Anvar Akramov on 9/25/2017.
 */
public class FlowSettingsDeleteTO extends ResponseData {

    private Integer status_id;
    private String type_flow;

    public FlowSettingsDeleteTO() {
    }

    public Integer getStatus_id() {
        return status_id;
    }

    public void setStatus_id(Integer status_id) {
        this.status_id = status_id;
    }

    public String getType_flow() {
        return type_flow;
    }

    public void setType_flow(String type_flow) {
        this.type_flow = type_flow;
    }
}
