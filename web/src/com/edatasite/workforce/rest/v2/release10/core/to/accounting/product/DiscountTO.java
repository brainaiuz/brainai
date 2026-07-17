package com.edatasite.workforce.rest.v2.release10.core.to.accounting.product;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilsh0d on 11/2/2017.
 */
public class DiscountTO extends ResponseData {
    private Integer discount_id;
    private String discount_name;

    public DiscountTO() {
    }

    public DiscountTO(Integer discount_id, String discount_name) {
        this.discount_id = discount_id;
        this.discount_name = discount_name;
    }

    public Integer getDiscount_id() {
        return discount_id;
    }

    public void setDiscount_id(Integer discount_id) {
        this.discount_id = discount_id;
    }

    public String getDiscount_name() {
        return discount_name;
    }

    public void setDiscount_name(String discount_name) {
        this.discount_name = discount_name;
    }
}
