package com.edatasite.workforce.gwt.invoice.client.ui.view.rentalorder;

import com.edatasite.workforce.gwt.accounting.client.rpc.RentalOrderItem;

import java.io.Serializable;


public class RentalOrderPriceItem implements Serializable {

    private Integer rowID;
    private RentalOrderItem item;

    public Integer getRowID() {
        return this.rowID;
    }

    public void setRowID(final Integer rowID) {
        this.rowID = rowID;
    }

    public RentalOrderItem getItem() {
        return this.item;
    }

    public void setItem(final RentalOrderItem item) {
        this.item = item;
    }
}