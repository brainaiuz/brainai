package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.enums.TaxKeyEnum;

import java.math.BigDecimal;

public class TaxItem extends SelectItem implements Cloneable{

    private BigDecimal taxPercent;
    private BigDecimal effectiveTaxPercent;

    //This is for UK companies only
    private Integer taxType;
    private TaxKeyEnum taxKey;
    private SelectItem[] faiCategories;
    private SelectItem[] faiPurchaseCategories;

    public TaxItem() {

    }

    public TaxItem(Integer id, String name) {
        super(id, name);
    }

    public TaxItem(Integer id, String vatName, BigDecimal taxPercent) {
        super(id, vatName);
        this.taxPercent = taxPercent;
    }

    public TaxItem(Integer id, String vatName, BigDecimal taxPercent, BigDecimal effectiveTaxPercent) {
        super(id, vatName);
        this.taxPercent = taxPercent;
        this.effectiveTaxPercent = effectiveTaxPercent;
    }

    public TaxItem(Integer id, String vatName, BigDecimal taxPercent, BigDecimal effectiveTaxPercent, Integer taxType) {
        super(id, vatName);
        this.taxPercent = taxPercent;
        this.effectiveTaxPercent = effectiveTaxPercent;
        this.taxType = taxType;
    }

    public BigDecimal getTaxPercent() {
        return taxPercent;
    }

    public BigDecimal getEffectiveTaxPercent() {
        return effectiveTaxPercent;
    }

    public Integer getTaxType() {
        return taxType;
    }

    public void setTaxType(Integer taxType) {
        this.taxType = taxType;
    }

    public void setTaxPercent(BigDecimal taxPercent) {
        this.taxPercent = taxPercent;
    }

    public void setEffectiveTaxPercent(BigDecimal effectiveTaxPercent) {
        this.effectiveTaxPercent = effectiveTaxPercent;
    }

    public TaxKeyEnum getTaxKey() {
        return taxKey;
    }

    public void setTaxKey(TaxKeyEnum taxKey) {
        this.taxKey = taxKey;
    }

    public void setFaiCategories(SelectItem[] faiCategories) {
        this.faiCategories = faiCategories;
    }

    public SelectItem[] getFaiCategories() {
        return faiCategories;
    }

    public void setFaiPurchaseCategories(SelectItem[] faiPurchaseCategories) {
        this.faiPurchaseCategories = faiPurchaseCategories;
    }

    public SelectItem[] getFaiPurchaseCategories() {
        return faiPurchaseCategories;
    }
}
