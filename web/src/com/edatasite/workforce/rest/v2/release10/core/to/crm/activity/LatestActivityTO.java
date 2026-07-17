package com.edatasite.workforce.rest.v2.release10.core.to.crm.activity;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.status.FlowSettingsTO;

/**
 * Created by Dilsh0d Madrahimov on 03/03/2018.
 */
public class LatestActivityTO extends ResponseData {

    private Integer item_id;
    private Integer entity_id;
    private String type;
    private String description;
    private String start_date;
    private String end_date;
    private String priority;
    private FlowSettingsTO status;


    public LatestActivityTO() {
    }

    public Integer getItem_id() {
        return item_id;
    }

    public void setItem_id(Integer item_id) {
        this.item_id = item_id;
    }

    public Integer getEntity_id() {
        return entity_id;
    }

    public void setEntity_id(Integer entity_id) {
        this.entity_id = entity_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public FlowSettingsTO getStatus() {
        return status;
    }

    public void setStatus(FlowSettingsTO status) {
        this.status = status;
    }
}
