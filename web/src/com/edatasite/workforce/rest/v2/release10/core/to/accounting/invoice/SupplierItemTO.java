package com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Anvar Akramov on 3/16/18.
 */
public class SupplierItemTO extends ResponseData {

    private Integer supplier_id;
    private String supplier_name;
    private AddressTO bill_to_address;
    private AddressTO ship_to_address;

    public SupplierItemTO() {
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

    public AddressTO getBill_to_address() {
        return bill_to_address;
    }

    public void setBill_to_address(AddressTO bill_to_address) {
        this.bill_to_address = bill_to_address;
    }

    public AddressTO getShip_to_address() {
        return ship_to_address;
    }

    public void setShip_to_address(AddressTO ship_to_address) {
        this.ship_to_address = ship_to_address;
    }
}
