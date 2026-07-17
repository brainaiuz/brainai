package com.edatasite.workforce.rest.v2.release10.core.to.accounting.product;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Farrukh Abdurakhmonov on 11/21/2017.
 */

public class ProductParentTO extends ResponseData {

    private Integer parent_id;
    private String parent_name;

    public ProductParentTO() {
    }

    public ProductParentTO(Integer parent_id, String parent_name) {
        this.parent_id = parent_id;
        this.parent_name = parent_name;
    }

    public Integer getParent_id() {
        return parent_id;
    }

    public void setParent_id(Integer parent_id) {
        this.parent_id = parent_id;
    }

    public String getParent_name() {
        return parent_name;
    }

    public void setParent_name(String parent_name) {
        this.parent_name = parent_name;
    }
}
