package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

public class VatReturnItem implements IsSerializable {

    private Integer objectID;
    private SelectItem status;
    private DateNonConvertable fromDate;
    private DateNonConvertable toDate;
    private DateNonConvertable filedOn;
    private DateNonConvertable due;
    private BigDecimal payableTaxTotal;
    private BigDecimal reclaimableTaxTotal;
    private boolean removable;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public SelectItem getStatus() {
        return status;
    }

    public void setStatus(SelectItem status) {
        this.status = status;
    }

    public DateNonConvertable getFromDate() {
        return fromDate;
    }

    public void setFromDate(DateNonConvertable fromDate) {
        this.fromDate = fromDate;
    }

    public DateNonConvertable getToDate() {
        return toDate;
    }

    public void setToDate(DateNonConvertable toDate) {
        this.toDate = toDate;
    }

    public DateNonConvertable getFiledOn() {
        return filedOn;
    }

    public void setFiledOn(DateNonConvertable filedOn) {
        this.filedOn = filedOn;
    }

    public BigDecimal getPayableTaxTotal() {
        return payableTaxTotal;
    }

    public void setPayableTaxTotal(BigDecimal payableTaxTotal) {
        this.payableTaxTotal = payableTaxTotal;
    }

    public BigDecimal getReclaimableTaxTotal() {
        return reclaimableTaxTotal;
    }

    public void setReclaimableTaxTotal(BigDecimal reclaimableTaxTotal) {
        this.reclaimableTaxTotal = reclaimableTaxTotal;
    }

    public boolean isRemovable() {
        return removable;
    }

    public void setRemovable(boolean removable) {
        this.removable = removable;
    }

    public DateNonConvertable getDue() {
        return due;
    }

    public void setDue(DateNonConvertable due) {
        this.due = due;
    }
}
