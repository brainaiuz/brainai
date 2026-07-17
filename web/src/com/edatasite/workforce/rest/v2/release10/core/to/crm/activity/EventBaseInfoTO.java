package com.edatasite.workforce.rest.v2.release10.core.to.crm.activity;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Abdurakhmonov Farrukh on 03/17/2018.
 */
public class EventBaseInfoTO extends ResponseData {
    private Integer item_id;
    private String name;
    private String description;
    private String start_date;
    private String end_date;
    private Boolean all_day;

    public EventBaseInfoTO() {
    }

    public Integer getItem_id() {
        return item_id;
    }

    public void setItem_id(Integer item_id) {
        this.item_id = item_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Boolean getAll_day() {
        return all_day;
    }

    public void setAll_day(Boolean all_day) {
        this.all_day = all_day;
    }
}
