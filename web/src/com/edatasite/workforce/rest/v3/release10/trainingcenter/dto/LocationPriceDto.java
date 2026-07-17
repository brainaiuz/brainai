package com.edatasite.workforce.rest.v3.release10.trainingcenter.dto;

import java.math.BigDecimal;

public class LocationPriceDto {
    private Integer locationId;
    private BigDecimal price;
    private BigDecimal stopFee;

    public Integer getLocationId() {
        return locationId;
    }
    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public BigDecimal getStopFee() {
        return stopFee;
    }
    public void setStopFee(BigDecimal stopFee) {
        this.stopFee = stopFee;
    }
}
