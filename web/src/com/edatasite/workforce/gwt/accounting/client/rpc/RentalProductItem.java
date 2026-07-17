package com.edatasite.workforce.gwt.accounting.client.rpc;

import java.io.Serializable;
import java.math.BigDecimal;

public class RentalProductItem implements Serializable {
    private Integer objectId;
    private String unitCode;
    private BigDecimal price;
    private String description;

    public RentalProductItem() {
    }

    public RentalProductItem(final String unitCode) {
        this.unitCode = unitCode;
    }

    public Integer getObjectId() {
        return this.objectId;
    }

    public void setObjectId(final Integer objectId) {
        this.objectId = objectId;
    }

    public String getUnitCode() {
        return this.unitCode;
    }

    public void setUnitCode(final String unitCode) {
        this.unitCode = unitCode;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }
}
