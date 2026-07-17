package com.edatasite.workforce.rest.v2.release10.core.to.base.filters;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Anvar Akramov on 12/18/2017.
 */
public class DatePeriodTO extends ResponseData {

    private String date_from;
    private String date_to;
    private String filter_by;//one of [ DATE_CREATED, DATE_UPDATED, NOT_SELECTED ]

    public DatePeriodTO() {
    }

    public DatePeriodTO(String date_from, String date_to, String filter_by) {
        this.date_from = date_from;
        this.date_to = date_to;
        this.filter_by = filter_by;
    }

    public String getDate_from() {
        return date_from;
    }

    public void setDate_from(String date_from) {
        this.date_from = date_from;
    }

    public String getDate_to() {
        return date_to;
    }

    public void setDate_to(String date_to) {
        this.date_to = date_to;
    }

    public String getFilter_by() {
        return filter_by;
    }

    public void setFilter_by(String filter_by) {
        this.filter_by = filter_by;
    }
}
