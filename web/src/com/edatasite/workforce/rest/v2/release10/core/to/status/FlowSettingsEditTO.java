package com.edatasite.workforce.rest.v2.release10.core.to.status;

/**
 * Created by Anvar Akramov on 9/25/2017.
 */
public class FlowSettingsEditTO extends FlowSettingsAddTO {

    private Integer status_id;
    private Integer order_id;

    public FlowSettingsEditTO() {
    }

    public Integer getStatus_id() {
        return status_id;
    }

    public void setStatus_id(Integer status_id) {
        this.status_id = status_id;
    }

    public Integer getOrder_id() {
        return order_id;
    }

    public void setOrder_id(Integer order_id) {
        this.order_id = order_id;
    }
}
