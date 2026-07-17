package com.edatasite.workforce.gwt.invoice.client.rpc;


import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 12/20/12
 * Time: 12:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class AllocateComissionItem implements Serializable {
    Integer quoteId;
    SelectItem salesMan;
    Double allocatePercent;
    BigDecimal allocateTotal;

    public AllocateComissionItem() {

    }

    public Integer getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Integer quoteId) {
        this.quoteId = quoteId;
    }

    public SelectItem getSalesMan() {
        return salesMan;
    }

    public void setSalesMan(SelectItem salesMan) {
        this.salesMan = salesMan;
    }

    public Double getAllocatePercent() {
        return allocatePercent;
    }

    public void setAllocatePercent(Double allocatePercent) {
        this.allocatePercent = allocatePercent;
    }

    public BigDecimal getAllocateTotal() {
        return allocateTotal;
    }

    public void setAllocateTotal(BigDecimal allocateTotal) {
        this.allocateTotal = allocateTotal;
    }
}
