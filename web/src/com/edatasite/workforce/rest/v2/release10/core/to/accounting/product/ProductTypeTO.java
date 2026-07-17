package com.edatasite.workforce.rest.v2.release10.core.to.accounting.product;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilsh0d on 11/2/2017.
 */
public class ProductTypeTO extends ResponseData {
    private Integer type_id;
    private String type_name;

    public ProductTypeTO() {
    }

    public ProductTypeTO(Integer type_id, String type_name) {
        this.type_id = type_id;
        this.type_name = type_name;
    }

    public Integer getType_id() {
        return type_id;
    }

    public void setType_id(Integer type_id) {
        this.type_id = type_id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }
}
