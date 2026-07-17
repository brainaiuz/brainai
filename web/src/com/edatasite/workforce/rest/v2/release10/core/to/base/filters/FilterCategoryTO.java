package com.edatasite.workforce.rest.v2.release10.core.to.base.filters;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Anvar Akramov on 12/18/2017.
 */
public class FilterCategoryTO extends ResponseData {

    private String category_code;
    private String category_name;
    private Boolean is_active;
    private ArrayList<SubCategoryTO> sub_categories;

    public FilterCategoryTO() {
    }

    public FilterCategoryTO(String category_code, String category_name) {
        this.category_code = category_code;
        this.category_name = category_name;
    }

    public FilterCategoryTO(String category_name, Boolean is_active) {
        this.category_name = category_name;
        this.is_active = is_active;
    }

    public FilterCategoryTO(String category_name, Boolean is_active, ArrayList<SubCategoryTO> sub_categories) {
        this.category_name = category_name;
        this.is_active = is_active;
        this.sub_categories = sub_categories;
    }

    public String getCategory_code() {
        return category_code;
    }

    public void setCategory_code(String category_code) {
        this.category_code = category_code;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }

    public ArrayList<SubCategoryTO> getSub_categories() {
        return sub_categories;
    }

    public void setSub_categories(ArrayList<SubCategoryTO> sub_categories) {
        this.sub_categories = sub_categories;
    }
}
