package com.edatasite.workforce.rest.v3.release10.accounting.dto;

import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;

public class PriceLevelBBDto {
    private Integer id;
    private ItemDto brand;
    private Integer effectType;
    private Double percent;

    public PriceLevelBBDto() {
    }

    public PriceLevelBBDto(Integer id, ItemDto brand, Integer effectType, Double percent) {
        this.id = id;
        this.brand = brand;
        this.effectType = effectType;
        this.percent = percent;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ItemDto getBrand() {
        return brand;
    }

    public void setBrand(ItemDto brand) {
        this.brand = brand;
    }

    public Integer getEffectType() {
        return effectType;
    }

    public void setEffectType(Integer effectType) {
        this.effectType = effectType;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PriceLevelBBDto)) return false;

        PriceLevelBBDto that = (PriceLevelBBDto) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (brand != null ? !brand.equals(that.brand) : that.brand != null) return false;
        if (effectType != null ? !effectType.equals(that.effectType) : that.effectType != null) return false;
        if (percent != null ? !percent.equals(that.percent) : that.percent != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (brand != null ? brand.hashCode() : 0);
        result = 31 * result + (effectType != null ? effectType.hashCode() : 0);
        result = 31 * result + (percent != null ? percent.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PriceLevelBBDto{" +
                "id=" + id +
                ", brand=" + brand +
                ", effectType=" + effectType +
                ", percent=" + percent +
                '}';
    }
}
