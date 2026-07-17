package com.edatasite.workforce.rest.v3.release10.accounting.dto;

import com.edatasite.workforce.rest.v3.release10.accounting.constants.AccountingAPIFieldConstants;
import com.edatasite.workforce.rest.v3.release10.core.request.CustomFieldRequest;
import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"product", "description", "quantity", "unitPrice", "discount", "accountCode", "taxItem"})
@JsonIgnoreProperties(ignoreUnknown = true, value = {"properties"})
public class LineItemDto implements AccountingAPIFieldConstants, Serializable {
    @JsonIgnore
    public static final String PRICE_LEVEL = "PRICE_LEVEL";

    @NotNull(message = "Product is required.")
    private ItemDto product;
    private String description;
    @NotNull(message = "Quantity is required.")
    private BigDecimal quantity;
    @JsonAlias({"unitPrice", "unitprice"})
    private BigDecimal unitPrice;
    private BigDecimal discount;//discount in %(percent)
    private String accountCode;
    private ItemDto taxItem;
    private ItemDto department;
    private Integer warehouseId;
    @JsonAlias({"project", "relatedProject", "related_project"})
    private IdCode project;
    private IdCode unitMeasurement;
    @JsonAlias({"customFields", "customfields"})
    private List<? extends CustomFieldRequest> customFields;

    /**
     * This field is for "handling Custom Field" purpose
     */
    @JsonIgnore
    private Map<String, Object> properties;

    public LineItemDto() {
    }

    @JsonAnySetter
    public void addProperty(String key, Object value) {
        if (properties == null) {
            properties = new HashMap<>();
        }
        properties.put(key, value);
    }

    @JsonAnyGetter
    public Map<String, Object> getProperties() {
        if (properties == null) {
            properties = new HashMap<>();
        }
        return properties;
    }

    @JsonIgnore
    public void setAccount(ItemDto account) {
        addProperty(ACCOUNT, account);
    }

    @JsonIgnore
    public void setDiscountAmount(BigDecimal discountAmount) {
        addProperty(DISCOUNT_AMOUNT, discountAmount);
    }

    public String getStringValue(String key) {
        Object value = getProperties().get(key);

        if (value != null && value instanceof String) {
            return (String) value;
        }
        return value != null ? value.toString() : null;
    }

    public ItemDto getProduct() {
        return product;
    }

    public void setProduct(ItemDto product) {
        this.product = product;
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

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public ItemDto getTaxItem() {
        return taxItem;
    }

    public void setTaxItem(ItemDto taxItem) {
        this.taxItem = taxItem;
    }

    public ItemDto getDepartment() {
        return department;
    }

    public void setDepartment(ItemDto department) {
        this.department = department;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public IdCode getProject() {
        return project;
    }

    public void setProject(IdCode project) {
        this.project = project;
    }

    public List<? extends CustomFieldRequest> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<? extends CustomFieldRequest> customFields) {
        this.customFields = customFields;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }

    public IdCode getUnitMeasurement() {
        return unitMeasurement;
    }

    public void setUnitMeasurement(IdCode unitMeasurement) {
        this.unitMeasurement = unitMeasurement;
    }

}
