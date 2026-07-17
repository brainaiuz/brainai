package com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Dilsh0d on 11/3/2017.
 */
public class AddressTO extends ResponseData {
    private Integer address_id;
    private String address_name;

    public AddressTO() {
    }

    public AddressTO(Integer address_id, String address_name) {
        this.address_id = address_id;
        this.address_name = address_name;
    }

    public Integer getAddress_id() {
        return address_id;
    }

    public void setAddress_id(Integer address_id) {
        this.address_id = address_id;
    }

    public String getAddress_name() {
        return address_name;
    }

    public void setAddress_name(String address_name) {
        this.address_name = address_name;
    }
}
