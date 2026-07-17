package com.edatasite.workforce.gwt.core.server.zatca.service.dto;

import java.math.BigDecimal;

public class TaxCategory {
    private String categoryID;
    private BigDecimal percent;

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public BigDecimal getPercent() {
        return percent;
    }

    public void setPercent(BigDecimal percent) {
        this.percent = percent;
    }
}
