package com.edatasite.workforce.rest.v2.release10.core.to.base.filters;

import java.util.ArrayList;

/**
 * Created by Anvar Akramov on 12/18/2017.
 */
public class BaseFilterTO extends FilterDataTO {

    private String name;
    private Boolean is_active;
    private Boolean is_default;
    private Boolean is_public;

    public BaseFilterTO() {
    }

    public BaseFilterTO(String name, Boolean is_active, DatePeriodTO date_period, ArrayList<FilterCategoryTO> filter_categories) {
        super(date_period, filter_categories);
        this.name = name;
        this.is_active = is_active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }

    public Boolean getIs_default() {
        return is_default;
    }

    public void setIs_default(Boolean is_default) {
        this.is_default = is_default;
    }

    public Boolean getIs_public() {
        return is_public;
    }

    public void setIs_public(Boolean is_public) {
        this.is_public = is_public;
    }

}
