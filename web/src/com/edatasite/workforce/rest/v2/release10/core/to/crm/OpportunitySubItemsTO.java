package com.edatasite.workforce.rest.v2.release10.core.to.crm;

import com.edatasite.workforce.rest.v2.release10.core.to.accounting.product.ProductBrandTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CurrencyValueTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Abdurakhmonov Farrukh on 03/05/2017.
 */
public class OpportunitySubItemsTO extends ResponseData {
    private Integer id;
    private String name;
    private BigDecimal count;
    private String description;
    private String supplier;
    private CurrencyValueTO price;
    private Integer category_id;
    private List<Object> custom_fields;
    private ProductBrandTO brand;
    private BigDecimal discount;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getCount() {
        return count;
    }

    public void setCount(BigDecimal count) {
        this.count = count;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public CurrencyValueTO getPrice() {
        return price;
    }

    public void setPrice(CurrencyValueTO price) {
        this.price = price;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public List<Object> getCustom_fields() {
        return custom_fields;
    }

    public void setCustom_fields(List<Object> custom_fields) {
        this.custom_fields = custom_fields;
    }

    public ProductBrandTO getBrand() {
        return brand;
    }

    public void setBrand(ProductBrandTO brand) {
        this.brand = brand;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }
}
