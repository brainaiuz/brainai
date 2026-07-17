package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Dilsh0d Madrahimov on 13/10/2017.
 */
public class FilteredStatusesRequestTO extends ResponseData {

    private ArrayList<Integer> people_filter_id;
    private ArrayList<Integer> categories_filter_id;

    public FilteredStatusesRequestTO() {
    }

    public ArrayList<Integer> getPeople_filter_id() {
        return people_filter_id;
    }

    public void setPeople_filter_id(ArrayList<Integer> people_filter_id) {
        this.people_filter_id = people_filter_id;
    }

    public ArrayList<Integer> getCategories_filter_id() {
        return categories_filter_id;
    }

    public void setCategories_filter_id(ArrayList<Integer> categories_filter_id) {
        this.categories_filter_id = categories_filter_id;
    }
}

