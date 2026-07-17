package com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;

public class VatReturnData implements IsSerializable {
    private BigDecimal payableTaxTotal;
    private BigDecimal reclaimableTaxTotal;

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
}
