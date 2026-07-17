package com.edatasite.workforce.rest.v2.release10.core.to.accounting.product;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Dilsh0d on 11/2/2017.
 */
public class ProductItemTO extends ProductListItemTO {
    private String description;
    private String as_of_date;
    private ProductBrandTO brand;
    private CogsAccountTO cogs_account;
    private IncomeAccountTO income_account;
    private ArrayList<DiscountTO> discount_list;
    private TaxTO tax;
    private BigDecimal commission;
    private String manufacturer;
    private String upc_number;
    private ProductParentTO product_parent;
    private Boolean active;
    private Integer order;
    private UnitMeasurementTO unit_measurement;
    private String weight_per_unit;
    private AssetAccountTO asset_account;
    private BigDecimal total_value;

    private ArrayList<AssemblyItemTO> assembly_items;
    private ArrayList<ProductKitItemTO> product_kit_items;

    public ProductItemTO() {
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAs_of_date() {
        return as_of_date;
    }

    public void setAs_of_date(String as_of_date) {
        this.as_of_date = as_of_date;
    }

    public ProductBrandTO getBrand() {
        return brand;
    }

    public void setBrand(ProductBrandTO brand) {
        this.brand = brand;
    }

    public CogsAccountTO getCogs_account() {
        return cogs_account;
    }

    public void setCogs_account(CogsAccountTO cogs_account) {
        this.cogs_account = cogs_account;
    }

    public IncomeAccountTO getIncome_account() {
        return income_account;
    }

    public void setIncome_account(IncomeAccountTO income_account) {
        this.income_account = income_account;
    }

    public ArrayList<DiscountTO> getDiscount_list() {
        return discount_list;
    }

    public void setDiscount_list(ArrayList<DiscountTO> discount_list) {
        this.discount_list = discount_list;
    }

    public TaxTO getTax() {
        return tax;
    }

    public void setTax(TaxTO tax) {
        this.tax = tax;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getUpc_number() {
        return upc_number;
    }

    public void setUpc_number(String upc_number) {
        this.upc_number = upc_number;
    }

    public ProductParentTO getProduct_parent() {
        return product_parent;
    }

    public void setProduct_parent(ProductParentTO product_parent) {
        this.product_parent = product_parent;
    }


    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }


    public UnitMeasurementTO getUnit_measurement() {
        return unit_measurement;
    }

    public void setUnit_measurement(UnitMeasurementTO unit_measurement) {
        this.unit_measurement = unit_measurement;
    }

    public String getWeight_per_unit() {
        return weight_per_unit;
    }

    public void setWeight_per_unit(String weight_per_unit) {
        this.weight_per_unit = weight_per_unit;
    }

    public AssetAccountTO getAsset_account() {
        return asset_account;
    }

    public void setAsset_account(AssetAccountTO asset_account) {
        this.asset_account = asset_account;
    }

    public BigDecimal getTotal_value() {
        return total_value;
    }

    public void setTotal_value(BigDecimal total_value) {
        this.total_value = total_value;
    }

    public ArrayList<AssemblyItemTO> getAssembly_items() {
        return assembly_items;
    }

    public void setAssembly_items(ArrayList<AssemblyItemTO> assembly_items) {
        this.assembly_items = assembly_items;
    }

    public ArrayList<ProductKitItemTO> getProduct_kit_items() {
        return product_kit_items;
    }

    public void setProduct_kit_items(ArrayList<ProductKitItemTO> product_kit_items) {
        this.product_kit_items = product_kit_items;
    }
}
