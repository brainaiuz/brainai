package com.edatasite.workforce.rest.v2.release10.core.to.base.filters;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Anvar Akramov on 12/18/2017.
 */
public class FilterDataTO extends ResponseData {

    private DatePeriodTO date_period;
    private ArrayList<FilterCategoryTO> filter_categories;

    public FilterDataTO() {
    }

    public FilterDataTO(DatePeriodTO date_period, ArrayList<FilterCategoryTO> filter_categories) {
        this.date_period = date_period;
        this.filter_categories = filter_categories;
    }

    public DatePeriodTO getDate_period() {
        return date_period;
    }

    public void setDate_period(DatePeriodTO date_period) {
        this.date_period = date_period;
    }

    public ArrayList<FilterCategoryTO> getFilter_categories() {
        if(filter_categories==null) {
            filter_categories = new ArrayList<>();
        }
        return filter_categories;
    }

    public void setFilter_categories(ArrayList<FilterCategoryTO> filter_categories) {
        this.filter_categories = filter_categories;
    }
}
