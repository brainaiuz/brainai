package com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.uk;

import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VatReturnBox;
import com.edatasite.workforce.gwt.accounting.client.rpc.vatreturn.VatReturnData;

import java.math.BigDecimal;
import java.util.HashMap;

public class UKVatReturn extends VatReturnData {
    private BigDecimal vatOnSales;
    private BigDecimal totalSales;
    private BigDecimal vatOnPurchase;
    private BigDecimal totalPurchase;


    public BigDecimal getVatOnSales() {
        return vatOnSales;
    }

    public void setVatOnSales(BigDecimal vatOnSales) {
        this.vatOnSales = vatOnSales;
    }

    public BigDecimal getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(BigDecimal totalSales) {
        this.totalSales = totalSales;
    }

    public BigDecimal getVatOnPurchase() {
        return vatOnPurchase;
    }

    public void setVatOnPurchase(BigDecimal vatOnPurchase) {
        this.vatOnPurchase = vatOnPurchase;
    }

    public BigDecimal getTotalPurchase() {
        return totalPurchase;
    }

    public void setTotalPurchase(BigDecimal totalPurchase) {
        this.totalPurchase = totalPurchase;
    }

    public BigDecimal getPayableOrReclaimableTax(){
        return getPayableTaxTotal().compareTo(BigDecimal.ZERO) > 0 ? getPayableTaxTotal() : getReclaimableTaxTotal();
    }

    public HashMap<VatReturnBox, BigDecimal> getValuesMap() {
        HashMap<VatReturnBox, BigDecimal> valuesMap = new HashMap<>();
        valuesMap.put(VatReturnBox.BOX_1, getVatOnSales());
        valuesMap.put(VatReturnBox.BOX_2, BigDecimal.ZERO);
        valuesMap.put(VatReturnBox.BOX_3, getVatOnSales());
        valuesMap.put(VatReturnBox.BOX_4, getVatOnPurchase());
        valuesMap.put(VatReturnBox.BOX_5, getPayableOrReclaimableTax());
        valuesMap.put(VatReturnBox.BOX_6, getTotalSales());
        valuesMap.put(VatReturnBox.BOX_7, getTotalPurchase());
        valuesMap.put(VatReturnBox.BOX_8, BigDecimal.ZERO);
        valuesMap.put(VatReturnBox.BOX_9, BigDecimal.ZERO);
        return valuesMap;
    }
}
