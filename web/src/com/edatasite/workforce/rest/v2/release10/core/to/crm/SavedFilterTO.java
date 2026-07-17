package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Anvar Akramov on 13/10/2017.
 */
public class SavedFilterTO extends ResponseData {

    private Integer category_id;
    private String category_name;
    private Boolean category_is_active;

    public SavedFilterTO() {
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public Boolean getCategory_is_active() {
        return category_is_active;
    }

    public void setCategory_is_active(Boolean category_is_active) {
        this.category_is_active = category_is_active;
    }
}
