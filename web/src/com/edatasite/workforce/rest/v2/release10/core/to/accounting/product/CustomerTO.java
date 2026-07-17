package com.edatasite.workforce.rest.v2.release10.core.to.accounting.product;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Anvar Akramov on 15/03/2018.
 */
public class CustomerTO extends ResponseData {
    private Integer customer_id;
    private String customer_name;
    private String customer_email;

    public CustomerTO() {
    }

    public CustomerTO(Integer customer_id, String customer_name, String customer_email) {
        this(customer_id, customer_name);
        this.customer_email = customer_email;
    }

    public CustomerTO(Integer customer_id, String customer_name) {
        this.customer_id = customer_id;
        this.customer_name = customer_name;
    }

    public Integer getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(Integer customer_id) {
        this.customer_id = customer_id;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }
}
