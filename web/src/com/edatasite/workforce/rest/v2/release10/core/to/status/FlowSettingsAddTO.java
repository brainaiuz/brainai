package com.edatasite.workforce.rest.v2.release10.core.to.status;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Anvar Akramov on 9/25/2017.
 */
public class FlowSettingsAddTO extends ResponseData {

    private String status_name;
    private ColorTO status_color;

    public FlowSettingsAddTO() {
    }

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public ColorTO getStatus_color() {
        return status_color;
    }

    public void setStatus_color(ColorTO status_color) {
        this.status_color = status_color;
    }
}
