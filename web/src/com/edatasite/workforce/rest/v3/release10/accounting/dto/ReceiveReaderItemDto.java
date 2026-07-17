package com.edatasite.workforce.rest.v3.release10.accounting.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class ReceiveReaderItemDto {
    private String name;
    @JsonProperty("price_per_unit")
    private BigDecimal pricePerUnit;
    private BigDecimal quantity;
    private String category;

    public ReceiveReaderItemDto() {
    }

    public ReceiveReaderItemDto(String name, BigDecimal price, BigDecimal quantity, String category) {
        this.name = name;
        this.pricePerUnit = price;
        this.quantity = quantity;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return pricePerUnit;
    }

    public void setPrice(BigDecimal price) {
        this.pricePerUnit = price;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
