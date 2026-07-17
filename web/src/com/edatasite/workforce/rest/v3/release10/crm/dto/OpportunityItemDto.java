package com.edatasite.workforce.rest.v3.release10.crm.dto;

import com.edatasite.workforce.rest.v3.release10.accounting.dto.LineItemDto;
import com.edatasite.workforce.rest.v3.release10.core.to.IdName;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by Normurod Buriev.
 * Date: 3/24/2021 5:37 PM
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OpportunityItemDto extends LineItemDto {
    private ItemDto supplier;
    private IdName category;
    private IdName brand;

    public OpportunityItemDto() {
    }

    public ItemDto getSupplier() {
        return supplier;
    }

    public void setSupplier(ItemDto supplier) {
        this.supplier = supplier;
    }

    public IdName getCategory() {
        return category;
    }

    public void setCategory(IdName category) {
        this.category = category;
    }

    public IdName getBrand() {
        return brand;
    }

    public void setBrand(IdName brand) {
        this.brand = brand;
    }
}
