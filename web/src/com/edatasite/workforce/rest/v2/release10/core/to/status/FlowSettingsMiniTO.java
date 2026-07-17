package com.edatasite.workforce.rest.v2.release10.core.to.status;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilsh0d on 9/25/2017.
 */
public class FlowSettingsMiniTO extends ResponseData {

    private String type_flow;

    public FlowSettingsMiniTO() {
    }

    public String getType_flow() {
        return type_flow;
    }

    public void setType_flow(String type_flow) {
        this.type_flow = type_flow;
    }
}
