package com.edatasite.workforce.rest.v3.release10.accounting.dto;

import com.edatasite.workforce.rest.v3.release10.core.request.CustomFieldRequest;
import com.edatasite.workforce.rest.v3.release10.core.to.DynamicDto;
import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.edatasite.workforce.rest.v3.release10.core.to.IdName;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class ExpenseItemDto extends DynamicDto {
    private Integer id;
    @NotNull(message = "category is required.")
    private IdCode category;
    private String description;
    @NotNull(message = "Quantity is required.")
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private IdName tax;
    private IdName department;
    @Valid
    private List<? extends CustomFieldRequest> customFields;

    public ExpenseItemDto() {
    }

    public ExpenseItemDto(Integer id, IdCode category, String description, BigDecimal quantity, BigDecimal unitPrice, IdName tax, IdName department, List<? extends CustomFieldRequest> customFields) {
        this.id = id;
        this.category = category;
        this.description = description;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.tax = tax;
        this.department = department;
        this.customFields = customFields;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public IdCode getCategory() {
        return category;
    }

    public void setCategory(IdCode category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public IdName getTax() {
        return tax;
    }

    public void setTax(IdName tax) {
        this.tax = tax;
    }

    public IdName getDepartment() {
        return department;
    }

    public void setDepartment(IdName department) {
        this.department = department;
    }

    public List<? extends CustomFieldRequest> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<? extends CustomFieldRequest> customFields) {
        this.customFields = customFields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ExpenseItemDto)) return false;

        ExpenseItemDto that = (ExpenseItemDto) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (category != null ? !category.equals(that.category) : that.category != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (quantity != null ? !quantity.equals(that.quantity) : that.quantity != null) return false;
        if (unitPrice != null ? !unitPrice.equals(that.unitPrice) : that.unitPrice != null) return false;
        if (tax != null ? !tax.equals(that.tax) : that.tax != null) return false;
        if (department != null ? !department.equals(that.department) : that.department != null) return false;
        if (customFields != null ? !customFields.equals(that.customFields) : that.customFields != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (quantity != null ? quantity.hashCode() : 0);
        result = 31 * result + (unitPrice != null ? unitPrice.hashCode() : 0);
        result = 31 * result + (tax != null ? tax.hashCode() : 0);
        result = 31 * result + (department != null ? department.hashCode() : 0);
        result = 31 * result + (customFields != null ? customFields.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ExpenseItemDto{" +
                "id=" + id +
                ", category=" + category +
                ", description='" + description + '\'' +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", tax=" + tax +
                ", department=" + department +
                ", customFields=" + customFields +
                '}';
    }
}
