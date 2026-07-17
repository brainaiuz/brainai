package com.edatasite.workforce.rest.v2.release10.core.to.accounting.product;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Anvar Akramov on 15/03/2018.
 */
public class SupplierTO extends ResponseData {
    private Integer supplier_id;
    private String supplier_name;

    public SupplierTO() {
    }

    public SupplierTO(Integer supplier_id, String supplier_name) {
        this.supplier_id = supplier_id;
        this.supplier_name = supplier_name;
    }

    public Integer getSupplier_id() {
        return supplier_id;
    }

    public void setSupplier_id(Integer supplier_id) {
        this.supplier_id = supplier_id;
    }

    public String getSupplier_name() {
        return supplier_name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }
}
