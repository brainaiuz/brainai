package com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice;

/**
 * Created by Dilsh0d on 11/2/2017.
 */
public class InvoiceCustomerTO extends SupplierTO {

    private AddressTO bill_to_address;
    private AddressTO ship_to_address;

    public InvoiceCustomerTO() {
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
