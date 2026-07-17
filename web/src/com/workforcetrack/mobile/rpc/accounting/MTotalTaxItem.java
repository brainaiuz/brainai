package com.workforcetrack.mobile.rpc.accounting;

import com.edatasite.workforce.gwt.invoice.client.rpc.TotalTaxItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;

@XmlRootElement(name = "totalTaxItem")
public class MTotalTaxItem {

    private MTaxItem taxItem;
    private BigDecimal taxAmount;

    public MTotalTaxItem() {
    }

    public MTotalTaxItem(TotalTaxItem totalTaxItem) {
        this.taxItem = new MTaxItem(totalTaxItem.getTaxItem());
        this.taxAmount = totalTaxItem.getTaxAmount();
    }

    public TotalTaxItem convertToTotalTaxItem(TotalTaxItem totalTaxItem) {
        if (totalTaxItem == null) {
            totalTaxItem = new TotalTaxItem();
        }
        totalTaxItem.setTaxItem(this.taxItem.convertToTaxItem(totalTaxItem.getTaxItem()));
        totalTaxItem.setTaxAmount(this.taxAmount);

        return totalTaxItem;
    }

    public MTaxItem getTaxItem() {
        return taxItem;
    }

    public void setTaxItem(MTaxItem taxItem) {
        this.taxItem = taxItem;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }
}