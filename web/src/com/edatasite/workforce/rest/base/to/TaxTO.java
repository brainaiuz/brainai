package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;

import java.math.BigDecimal;

/**
 * Created by Dilshod Madrahimov on 03.02.2015.
 */
public class TaxTO extends SelectItemTO {

    BigDecimal taxPercent;
    BigDecimal effectiveTaxPercent;

    public TaxTO() {
    }

    public TaxTO(TaxItem taxItem) {
        this.id = taxItem.getId();
        this.name = taxItem.getName();
        this.description = taxItem.getDescription();
        this.effectiveTaxPercent = taxItem.getEffectiveTaxPercent();
        this.taxPercent = taxItem.getTaxPercent();
    }


    public TaxTO(Integer id, String vatName, BigDecimal taxPercent, BigDecimal effectiveTaxPercent) {
        super(id, vatName);
        this.taxPercent = taxPercent;
        this.effectiveTaxPercent = effectiveTaxPercent;
    }

    public BigDecimal getTaxPercent() {
        return taxPercent;
    }

    public void setTaxPercent(BigDecimal taxPercent) {
        this.taxPercent = taxPercent;
    }

    public BigDecimal getEffectiveTaxPercent() {
        return effectiveTaxPercent;
    }

    public void setEffectiveTaxPercent(BigDecimal effectiveTaxPercent) {
        this.effectiveTaxPercent = effectiveTaxPercent;
    }

}