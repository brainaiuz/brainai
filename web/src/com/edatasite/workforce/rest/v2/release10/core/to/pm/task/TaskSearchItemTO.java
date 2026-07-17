package com.edatasite.workforce.rest.v2.release10.core.to.pm.task;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.status.FlowSettingsTO;

import java.util.ArrayList;

/**
 * Created by Dilshod Madrahimov on 12/02/2018.
 */
public class TaskSearchItemTO extends ResponseData {

    private String name;
    private String number;
    private Integer status_id;
    private Integer item_id;
    private String due_date;
    private String start_date;
    private String end_date;
    private String priority;
    private FlowSettingsTO status;
    private ArrayList<CustomFieldsTO> custom_fields;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public TaskSearchItemTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus_id() {
        return status_id;
    }

    public void setStatus_id(Integer status_id) {
        this.status_id = status_id;
    }

    public Integer getItem_id() {
        return item_id;
    }

    public void setItem_id(Integer item_id) {
        this.item_id = item_id;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
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

    public ArrayList<CustomFieldsTO> getCustom_fields() {
        return custom_fields;
    }

    public void setCustom_fields(ArrayList<CustomFieldsTO> custom_fields) {
        this.custom_fields = custom_fields;
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
}
