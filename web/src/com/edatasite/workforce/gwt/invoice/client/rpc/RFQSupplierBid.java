package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/27/12
 * Time: 7:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class RFQSupplierBid implements IsSerializable {
    private Integer objectID;
    private Integer rfqItemID;
    private SelectItem supplier;
    private BigDecimal amount;

    public RFQSupplierBid() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getRfqItemID() {
        return rfqItemID;
    }

    public void setRfqItemID(Integer rfqItemID) {
        this.rfqItemID = rfqItemID;
    }

    public SelectItem getSupplier() {
        return supplier;
    }

    public void setSupplier(SelectItem supplier) {
        this.supplier = supplier;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
