package com.edatasite.workforce.rest.v3.release10.accounting.dto;

import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;

import java.math.BigDecimal;

public class PriceLevelDto {

    private IdCode priceLevel;
    private BigDecimal customPrice;

    public PriceLevelDto() {
    }

    public PriceLevelDto(IdCode priceLevel, BigDecimal customPrice) {
        this.priceLevel = priceLevel;
        this.customPrice = customPrice;
    }

    public IdCode getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(IdCode priceLevel) {
        this.priceLevel = priceLevel;
    }

    public BigDecimal getCustomPrice() {
        return customPrice;
    }

    public void setCustomPrice(BigDecimal customPrice) {
        this.customPrice = customPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PriceLevelDto)) return false;

        PriceLevelDto that = (PriceLevelDto) o;

        if (getPriceLevel() != null ? !getPriceLevel().equals(that.getPriceLevel()) : that.getPriceLevel() != null)
            return false;
        if (getCustomPrice() != null ? !getCustomPrice().equals(that.getCustomPrice()) : that.getCustomPrice() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getPriceLevel() != null ? getPriceLevel().hashCode() : 0;
        result = 31 * result + (getCustomPrice() != null ? getCustomPrice().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PriceLevelDto{" +
                "priceLevel=" + priceLevel +
                ", customPrice=" + customPrice +
                '}';
    }
}
