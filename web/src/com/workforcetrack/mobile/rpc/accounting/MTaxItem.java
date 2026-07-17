package com.workforcetrack.mobile.rpc.accounting;

import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.workforcetrack.mobile.rpc.client.MSelectItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement(name = "taxItem")
public class MTaxItem extends MSelectItem {

    private double taxPercent;
    private double effectiveTaxPercent;

    //This is for UK companies only
    private Integer taxType;

    public MTaxItem() {

    }

    public MTaxItem(Integer id, String vatName, double taxPercent) {
        super(id, vatName);
        this.taxPercent = taxPercent;
    }

    public MTaxItem(Integer id, String vatName, double taxPercent, double effectiveTaxPercent, Integer taxType) {
        super(id, vatName);
        this.taxPercent = taxPercent;
        this.effectiveTaxPercent = effectiveTaxPercent;
        this.taxType = taxType;
    }

    public MTaxItem(TaxItem taxItem) {
        this.objectID = taxItem.getId();
        this.name = taxItem.getName();
        this.description = taxItem.getDescription();
        this.taxPercent = taxItem.getTaxPercent() != null ? taxItem.getTaxPercent().doubleValue() : 0;
        this.effectiveTaxPercent = taxItem.getEffectiveTaxPercent() != null ? taxItem.getEffectiveTaxPercent().doubleValue() : 0;
        this.taxType = taxItem.getTaxType();
    }

    public TaxItem convertToTaxItem(TaxItem taxItem) {
        if (taxItem == null) {
            taxItem = new TaxItem();
        }
        taxItem.setId(this.getObjectID());
        taxItem.setName(this.getName());
        taxItem.setTaxPercent(new BigDecimal(this.getTaxPercent()));
        taxItem.setEffectiveTaxPercent(new BigDecimal(this.getEffectiveTaxPercent()));
        taxItem.setTaxType(this.getTaxType());
        return taxItem;
    }

    public double getTaxPercent() {
        return taxPercent;
    }

    public double getEffectiveTaxPercent() {
        return effectiveTaxPercent;
    }

    public Integer getTaxType() {
        return taxType;
    }

    public void setTaxType(Integer taxType) {
        this.taxType = taxType;
    }

    public void setTaxPercent(double taxPercent) {
        this.taxPercent = taxPercent;
    }

    public void setEffectiveTaxPercent(double effectiveTaxPercent) {
        this.effectiveTaxPercent = effectiveTaxPercent;
    }
}
