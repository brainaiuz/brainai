package com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Created by Dilsh0d on 11/2/2017.
 */
public class SupplierTO extends ResponseData {
    @Schema(required = true)
    private Integer customer_id;
    private String customer_name;

    public SupplierTO() {
    }

    public SupplierTO(Integer supplierObjectID, String supplierName) {
        this.customer_id = supplierObjectID;
        this.customer_name = supplierName;
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

}
