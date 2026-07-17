package com.edatasite.workforce.rest.v2.release10.core.to.accounting.product;

import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductTrackBatchItem;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.pm.task.CustomFieldsTO;
import com.edatasite.workforce.rest.v3.release10.core.request.CustomFieldRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Dilsh0d on 11/2/2017.
 */
public class ProductListItemTO extends ResponseData {
    private Integer id;
    private String name;
    private String description; //added as part of zapier integration
    private String vendor; //added as part of zapier integration
    private BigDecimal quantity; //added as part of zapier integration
    private String inventory_policy; //added as part of zapier integration
    private String image_url; //added as part of zapier integration
    private String categories; //added as part of zapier integration
    private String part_number;
    private String number;
    private ProductTypeTO product_type;
    private ProductCategoryTO category;
    private ProductCategoryTO category_parent;
    private ProductBrandTO brand;
    private String sku_number;
    private String barcode;
    private BigDecimal unit_price;
    private BigDecimal cost_price;
    private BigDecimal rate;
    private String variants;
    private ArrayList<CustomFieldsTO> custom_fields;
    private boolean trackBatchesEnabled = false;
    private List<ProductTrackBatchItem> batchItems;//For Javlon's apteka

    private Integer tax_id;
    private String tax_name;
    private BigDecimal tax_effective_rate;

    private ArrayList<InventoryStockItemTO> inventory_stock_item_list;

    //Added for Javlon's Apteka
    private Integer account_id;
    private String account_name;
    private List<ProductPicturesTo> product_pictures;
    private String created_date;
    private String updated_date;

    private List<DiscountItem> discounts;
    private LinkedHashMap<String, BigDecimal> multiPrices;
    private Integer taxCalculationType;

    private List<? extends CustomFieldRequest> categoryCustomFields;

    public ProductListItemTO() {
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
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

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getPart_number() {
        return part_number;
    }

    public void setPart_number(String part_number) {
        this.part_number = part_number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public ProductTypeTO getProduct_type() {
        return product_type;
    }

    public void setProduct_type(ProductTypeTO product_type) {
        this.product_type = product_type;
    }

    public ProductCategoryTO getCategory() {
        return category;
    }

    public void setCategory(ProductCategoryTO category) {
        this.category = category;
    }

    public ProductCategoryTO getCategory_parent() {
        return category_parent;
    }

    public void setCategory_parent(ProductCategoryTO category_parent) {
        this.category_parent = category_parent;
    }

    public ProductBrandTO getBrand() {
        return brand;
    }

    public void setBrand(ProductBrandTO brand) {
        this.brand = brand;
    }

    public String getSku_number() {
        return sku_number;
    }

    public void setSku_number(String sku_number) {
        this.sku_number = sku_number;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public BigDecimal getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(BigDecimal unit_price) {
        this.unit_price = unit_price;
    }

    public BigDecimal getCost_price() {
        return cost_price;
    }

    public void setCost_price(BigDecimal cost_price) {
        this.cost_price = cost_price;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getVariants() {
        return variants;
    }

    public void setVariants(String variants) {
        this.variants = variants;
    }

    public ArrayList<CustomFieldsTO> getCustom_fields() {
        return custom_fields;
    }

    public void setCustom_fields(ArrayList<CustomFieldsTO> custom_fields) {
        this.custom_fields = custom_fields;
    }

    public boolean isTrackBatchesEnabled() {
        return trackBatchesEnabled;
    }

    public void setTrackBatchesEnabled(boolean trackBatchesEnabled) {
        this.trackBatchesEnabled = trackBatchesEnabled;
    }

    public List<ProductTrackBatchItem> getBatchItems() {
        return batchItems;
    }

    public void setBatchItems(List<ProductTrackBatchItem> batchItems) {
        this.batchItems = batchItems;
    }

    public Integer getTax_id() {
        return tax_id;
    }

    public void setTax_id(Integer tax_id) {
        this.tax_id = tax_id;
    }

    public String getTax_name() {
        return tax_name;
    }

    public void setTax_name(String tax_name) {
        this.tax_name = tax_name;
    }

    public BigDecimal getTax_effective_rate() {
        return tax_effective_rate;
    }

    public void setTax_effective_rate(BigDecimal tax_effective_rate) {
        this.tax_effective_rate = tax_effective_rate;
    }

    public ArrayList<InventoryStockItemTO> getInventory_stock_item_list() {
        return inventory_stock_item_list;
    }

    public void setInventory_stock_item_list(ArrayList<InventoryStockItemTO> inventory_stock_item_list) {
        this.inventory_stock_item_list = inventory_stock_item_list;
    }

    public Integer getAccount_id() {
        return account_id;
    }

    public void setAccount_id(Integer account_id) {
        this.account_id = account_id;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public List<ProductPicturesTo> getProduct_pictures() {
        return product_pictures;
    }

    public void setProduct_pictures(List<ProductPicturesTo> product_pictures) {
        this.product_pictures = product_pictures;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }

    public List<DiscountItem> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(List<DiscountItem> discounts) {
        this.discounts = discounts;
    }

    public LinkedHashMap<String, BigDecimal> getMultiPrices() {
        return multiPrices;
    }

    public void setMultiPrices(LinkedHashMap<String, BigDecimal> multiPrices) {
        this.multiPrices = multiPrices;
    }

    public Integer getTaxCalculationType() {
        return taxCalculationType;
    }

    public void setTaxCalculationType(Integer taxCalculationType) {
        this.taxCalculationType = taxCalculationType;
    }

    public List<? extends CustomFieldRequest> getCategoryCustomFields() {
        return categoryCustomFields;
    }

    public void setCategoryCustomFields(List<? extends CustomFieldRequest> categoryCustomFields) {
        this.categoryCustomFields = categoryCustomFields;
    }
}
