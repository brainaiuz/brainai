package com.edatasite.workforce.rest.v2.release10.core.to.status;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Anvar Akramov on 9/25/2017.
 */
public class StatusAddTO extends ResponseData {

    private String type_flow;
    private FlowSettingsAddTO status;

    public StatusAddTO() {
    }

    public String getType_flow() {
        return type_flow;
    }

    public void setType_flow(String type_flow) {
        this.type_flow = type_flow;
    }

    public FlowSettingsAddTO getStatus() {
        return status;
    }

    public void setStatus(FlowSettingsAddTO status) {
        this.status = status;
    }
}
