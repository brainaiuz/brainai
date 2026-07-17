package com.edatasite.workforce.gwt.project.client.rpc;

import java.math.BigDecimal;

/**
 * Created by Shohruh on 20 Jun 2016.
 */
public class WageTaxItem {
    private BigDecimal tax;
    private BigDecimal total;
    private boolean withTax = false;

    public WageTaxItem(BigDecimal tax, BigDecimal total) {
        this.tax = tax;
        this.total = total;
    }

    public BigDecimal getTotal() {
        return withTax ? total.add(tax) : total;
    }

    public void setWithTax(boolean withTax) {
        this.withTax = withTax;
    }
}
