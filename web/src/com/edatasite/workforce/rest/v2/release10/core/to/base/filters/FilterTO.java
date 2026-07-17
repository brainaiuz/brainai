package com.edatasite.workforce.rest.v2.release10.core.to.base.filters;

import java.util.ArrayList;

/**
 * Created by Anvar Akramov on 12/18/2017.
 */
public class FilterTO extends BaseFilterTO {

    private Integer filter_id;

    public FilterTO() {
    }

    public FilterTO(Integer filter_id, String name, Boolean is_active, DatePeriodTO date_period, ArrayList<FilterCategoryTO> filter_categories) {
        super(name, is_active, date_period, filter_categories);
        this.filter_id = filter_id;
    }

    public Integer getFilter_id() {
        return filter_id;
    }

    public void setFilter_id(Integer filter_id) {
        this.filter_id = filter_id;
    }

}
