package com.edatasite.workforce.rest.v2.release10.core.to.crm.activity;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Abdurakhmonov Farrukh on 03/17/2018.
 */
public class RecurrenceRepeatsTO extends ResponseData {
    private String type;
    private Integer count;
    private ArrayList<String> selected_days;
    private String yearly_date;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public ArrayList<String> getSelected_days() {
        return selected_days;
    }

    public void setSelected_days(ArrayList<String> selected_days) {
        this.selected_days = selected_days;
    }

    public String getYearly_date() {
        return yearly_date;
    }

    public void setYearly_date(String yearly_date) {
        this.yearly_date = yearly_date;
    }
}
