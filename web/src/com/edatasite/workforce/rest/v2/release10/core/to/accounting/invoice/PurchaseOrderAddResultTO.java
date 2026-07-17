package com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/*
    Created by Akhror Gulomov on 11/6/2020
 */
public class PurchaseOrderAddResultTO extends ResponseData {
    private Integer id;
    private String number;

    public PurchaseOrderAddResultTO() {
    }

    public PurchaseOrderAddResultTO(Integer id, String number) {
        this.id = id;
        this.number = number;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
