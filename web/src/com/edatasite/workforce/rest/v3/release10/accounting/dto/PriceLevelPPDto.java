package com.edatasite.workforce.rest.v3.release10.accounting.dto;

import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;

public class PriceLevelPPDto {
    private Integer id;
    private Double customPrice;
    private ItemDto product;

    public PriceLevelPPDto() {
    }

    public PriceLevelPPDto(Integer id, Double customPrice, ItemDto product) {
        this.id = id;
        this.customPrice = customPrice;
        this.product = product;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getCustomPrice() {
        return customPrice;
    }

    public void setCustomPrice(Double customPrice) {
        this.customPrice = customPrice;
    }

    public ItemDto getProduct() {
        return product;
    }

    public void setProduct(ItemDto product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PriceLevelPPDto)) return false;

        PriceLevelPPDto that = (PriceLevelPPDto) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (customPrice != null ? !customPrice.equals(that.customPrice) : that.customPrice != null) return false;
        if (product != null ? !product.equals(that.product) : that.product != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (customPrice != null ? customPrice.hashCode() : 0);
        result = 31 * result + (product != null ? product.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PriceLevelPPDto{" +
                "id=" + id +
                ", customPrice=" + customPrice +
                ", product=" + product +
                '}';
    }
}
