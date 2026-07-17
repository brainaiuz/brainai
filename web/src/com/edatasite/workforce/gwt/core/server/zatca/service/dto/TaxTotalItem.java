package com.edatasite.workforce.gwt.core.server.zatca.service.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TaxTotalItem {
    private BigDecimal taxAmount;

    private List<TaxSubTotalItem> subTotalItems;

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public List<TaxSubTotalItem> getSubTotalItems() {
        if (subTotalItems == null) {
            subTotalItems = new ArrayList<>();
        }
        return subTotalItems;
    }

    public void setSubTotalItems(List<TaxSubTotalItem> subTotalItems) {
        this.subTotalItems = subTotalItems;
    }
}
