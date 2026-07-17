package com.edatasite.workforce.gwt.core.server.zatca.service.dto;

public class LineItemTo {
    private String itemName;

    private TaxCategory classifiedTaxCategory;
    private String priceAmount;

    public String getPriceAmount() {
        return priceAmount;
    }

    public void setPriceAmount(String priceAmount) {
        this.priceAmount = priceAmount;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public TaxCategory getClassifiedTaxCategory() {
        return classifiedTaxCategory;
    }

    public void setClassifiedTaxCategory(TaxCategory classifiedTaxCategory) {
        this.classifiedTaxCategory = classifiedTaxCategory;
    }
}
