package com.edatasite.workforce.rest.v2.release10.core.to.accounting.product;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.math.BigDecimal;

/**
 * Created by Anvar Akramov on 8/8/2019.
 */
public class ZapierProductVariantTO extends ResponseData {
    private Integer parent_id;
    private String parent_name;
    private String parent_description;
    private String parent_vendor;
    private BigDecimal parent_quantity;
    private String parent_inventory_policy;
    private String parent_image_url;
    private String parent_part_number;
    private String parent_number;
    private ProductTypeTO parent_product_type;
    private ProductCategoryTO parent_category;
    private ProductCategoryTO parent_category_parent;
    private String parent_sku_number;
    private String parent_barcode;
    private BigDecimal parent_unit_price;
    private BigDecimal parent_cost_price;
    private BigDecimal parent_rate;

    private Integer id;
    private String name;
    private BigDecimal unit_price;
    private BigDecimal compare_price;
    private String sku_number;
    private BigDecimal quantity;
    private String inventory_policy;

    public ZapierProductVariantTO() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParent_id() {
        return parent_id;
    }

    public void setParent_id(Integer parent_id) {
        this.parent_id = parent_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSku_number() {
        return sku_number;
    }

    public void setSku_number(String sku_number) {
        this.sku_number = sku_number;
    }

    public BigDecimal getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(BigDecimal unit_price) {
        this.unit_price = unit_price;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public String getInventory_policy() {
        return inventory_policy;
    }

    public void setInventory_policy(String inventory_policy) {
        this.inventory_policy = inventory_policy;
    }

    public String getParent_name() {
        return parent_name;
    }

    public void setParent_name(String parent_name) {
        this.parent_name = parent_name;
    }

    public String getParent_description() {
        return parent_description;
    }

    public void setParent_description(String parent_description) {
        this.parent_description = parent_description;
    }

    public String getParent_vendor() {
        return parent_vendor;
    }

    public void setParent_vendor(String parent_vendor) {
        this.parent_vendor = parent_vendor;
    }

    public BigDecimal getParent_quantity() {
        return parent_quantity;
    }

    public void setParent_quantity(BigDecimal parent_quantity) {
        this.parent_quantity = parent_quantity;
    }

    public String getParent_inventory_policy() {
        return parent_inventory_policy;
    }

    public void setParent_inventory_policy(String parent_inventory_policy) {
        this.parent_inventory_policy = parent_inventory_policy;
    }

    public String getParent_image_url() {
        return parent_image_url;
    }

    public void setParent_image_url(String parent_image_url) {
        this.parent_image_url = parent_image_url;
    }

    public String getParent_part_number() {
        return parent_part_number;
    }

    public void setParent_part_number(String parent_part_number) {
        this.parent_part_number = parent_part_number;
    }

    public String getParent_number() {
        return parent_number;
    }

    public void setParent_number(String parent_number) {
        this.parent_number = parent_number;
    }

    public ProductTypeTO getParent_product_type() {
        return parent_product_type;
    }

    public void setParent_product_type(ProductTypeTO parent_product_type) {
        this.parent_product_type = parent_product_type;
    }

    public ProductCategoryTO getParent_category() {
        return parent_category;
    }

    public void setParent_category(ProductCategoryTO parent_category) {
        this.parent_category = parent_category;
    }

    public ProductCategoryTO getParent_category_parent() {
        return parent_category_parent;
    }

    public void setParent_category_parent(ProductCategoryTO parent_category_parent) {
        this.parent_category_parent = parent_category_parent;
    }

    public String getParent_sku_number() {
        return parent_sku_number;
    }

    public void setParent_sku_number(String parent_sku_number) {
        this.parent_sku_number = parent_sku_number;
    }

    public String getParent_barcode() {
        return parent_barcode;
    }

    public void setParent_barcode(String parent_barcode) {
        this.parent_barcode = parent_barcode;
    }

    public BigDecimal getParent_unit_price() {
        return parent_unit_price;
    }

    public void setParent_unit_price(BigDecimal parent_unit_price) {
        this.parent_unit_price = parent_unit_price;
    }

    public BigDecimal getParent_cost_price() {
        return parent_cost_price;
    }

    public void setParent_cost_price(BigDecimal parent_cost_price) {
        this.parent_cost_price = parent_cost_price;
    }

    public BigDecimal getParent_rate() {
        return parent_rate;
    }

    public void setParent_rate(BigDecimal parent_rate) {
        this.parent_rate = parent_rate;
    }

    public BigDecimal getCompare_price() {
        return compare_price;
    }

    public void setCompare_price(BigDecimal compare_price) {
        this.compare_price = compare_price;
    }
}
