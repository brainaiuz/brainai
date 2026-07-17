package com.edatasite.workforce.rest.v2.release10.core.to.hrms;

import com.edatasite.workforce.gwt.core.client.rpc.PositionItem;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

public class PositionTO extends ResponseData {
    private Integer position_id;
    private String position_name;

    public PositionTO() {
    }

    public PositionTO(PositionItem item) {
        setPosition_id(item.getObjectID());
        setPosition_name(item.getName());
    }

    public Integer getPosition_id() {
        return position_id;
    }

    public void setPosition_id(Integer position_id) {
        this.position_id = position_id;
    }

    public String getPosition_name() {
        return position_name;
    }

    public void setPosition_name(String position_name) {
        this.position_name = position_name;
    }
}
