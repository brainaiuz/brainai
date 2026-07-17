package com.edatasite.workforce.rest.v3.release10.accounting.dto;

import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.edatasite.workforce.rest.v3.release10.core.to.IdName;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.fasterxml.jackson.annotation.JsonAlias;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by Normurod Buriev.
 * Date: 2/24/2021 5:11 PM
 */
public class AdjustmentItemDto {
    @NotNull(message = "Product is required.")
    private ItemDto product;
    private ItemDto department;
    private IdName warehouse;
    @JsonAlias({"project", "relatedProject", "related_project"})
    private IdCode project;
    @NotNull(message = "Quantity on hand is required.")
    @Min(value = 0, message = "Quantity on hand cannot be negative.")
    private BigDecimal quantityOnHand;

    public AdjustmentItemDto() {
    }

    public ItemDto getProduct() {
        return product;
    }

    public void setProduct(ItemDto product) {
        this.product = product;
    }

    public ItemDto getDepartment() {
        return department;
    }

    public void setDepartment(ItemDto department) {
        this.department = department;
    }

    public IdName getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(IdName warehouse) {
        this.warehouse = warehouse;
    }

    public IdCode getProject() {
        return project;
    }

    public void setProject(IdCode project) {
        this.project = project;
    }

    public BigDecimal getQuantityOnHand() {
        return quantityOnHand;
    }

    public void setQuantityOnHand(BigDecimal quantityOnHand) {
        this.quantityOnHand = quantityOnHand;
    }
}
