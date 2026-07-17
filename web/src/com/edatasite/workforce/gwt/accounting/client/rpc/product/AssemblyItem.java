package com.edatasite.workforce.gwt.accounting.client.rpc.product;

import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.HasApprovers;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.edatasite.workforce.gwt.invoice.client.rpc.QuantityItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 1/15/12
 * Time: 7:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class AssemblyItem extends HasApprovers implements ListingCustomFields, IsSerializable {

    private Integer id;
    private Integer assemblyItemId;
    private SelectItem product;
    private String category;
    private String description;
    private Integer productType;
    private BigDecimal quantity;
    private BigDecimal itemQuantity;
    private BigDecimal costPrice;
    private BigDecimal productPrice;
    private BigDecimal productSellingPrice;
    private BigDecimal total;
    private BigDecimal itemsInStock;
    private Boolean active;
    private Integer warehouseId;
    private ArrayList<Integer> locationId;
    private Integer productId;
    private ArrayList<MultiPriceItem> multiPriceItems;
    private QuantityItem[] items;
    private SelectItem account;
    private BigDecimal sellingPrice;
    private SelectItem productDefaultWarehouse;
    private SelectItem status;
    private String statusCode;
    private NumberData numberData;
    private Boolean isBuilt;
    private DateNonConvertable date;
    private DateNonConvertable createdDate;
    private DateNonConvertable updatedDate;
    private SelectItem creator;
    private SelectItem updater;
    private SelectItem assemblyItem;
    private SelectItem wareHouseItem;
    private NewProduct newProduct;
    private SelectItem approver;
    private boolean approveProcessEnabled;
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private HashMap<String, Object> customFieldValues;
    private SelectItem[] templates;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAssemblyItemId() {
        return assemblyItemId;
    }

    public void setAssemblyItemId(Integer assemblyItemId) {
        this.assemblyItemId = assemblyItemId;
    }

    public SelectItem getProduct() {
        return product;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setProduct(SelectItem product) {
        this.product = product;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getProductType() {
        return productType;
    }

    public void setProductType(Integer productType) {
        this.productType = productType;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(BigDecimal itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getItemsInStock() {
        return itemsInStock;
    }

    public void setItemsInStock(BigDecimal itemsInStock) {
        this.itemsInStock = itemsInStock;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public ArrayList<MultiPriceItem> getMultiPriceItems() {
        if (multiPriceItems == null) {
            multiPriceItems = new ArrayList<>();
        }
        return multiPriceItems;
    }

    public void setMultiPriceItems(ArrayList<MultiPriceItem> multiPriceItems) {
        this.multiPriceItems = multiPriceItems;
    }

    public QuantityItem[] getItems() {
        return items;
    }

    public void setItems(QuantityItem[] items) {
        this.items = items;
    }

    public SelectItem getAccount() {
        return account;
    }

    public void setAccount(SelectItem account) {
        this.account = account;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public BigDecimal getProductSellingPrice() {
        return this.productSellingPrice;
    }

    public void setProductSellingPrice(final BigDecimal productSellingPrice) {
        this.productSellingPrice = productSellingPrice;
    }

    public void setProductDefaultWarehouse(SelectItem productDefaultWarehouse) {
        this.productDefaultWarehouse = productDefaultWarehouse;
    }

    public SelectItem getProductDefaultWarehouse() {
        return productDefaultWarehouse;
    }

    public ArrayList<Integer> getLocationIds() {
        return locationId;
    }

    public void setLocationIds(ArrayList<Integer> locationId) {
        this.locationId = locationId;
    }

    public SelectItem getStatus() {
        return this.status;
    }

    public void setStatus(final SelectItem status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public NumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
    }

    public Boolean isBuilt() {
        return isBuilt;
    }

    public void setBuilt(Boolean isBuilt) {
        this.isBuilt = isBuilt;
    }

    public DateNonConvertable getDate() {
        return date;
    }
    public void setDate(DateNonConvertable date) {
        this.date = date;
    }

    public DateNonConvertable getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateNonConvertable createdDate) {
        this.createdDate = createdDate;
    }

    public DateNonConvertable getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(DateNonConvertable updatedDate) {
        this.updatedDate = updatedDate;
    }

    public SelectItem getCreator() {
        return creator;
    }

    public void setCreator(SelectItem creator) {
        this.creator = creator;
    }

    public SelectItem getUpdater() {
        return updater;
    }

    public void setUpdater(SelectItem updater) {
        this.updater = updater;
    }

    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return null;
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {

    }

    public SelectItem getAssemblyItem() {
        return assemblyItem;
    }

    public void setAssemblyItem(SelectItem assemblyItem) {
        this.assemblyItem = assemblyItem;
    }

    public SelectItem getWareHouseItem() {
        return wareHouseItem;
    }

    public void setWareHouseItem(SelectItem wareHouseItem) {
        this.wareHouseItem = wareHouseItem;
    }

    public NewProduct getNewProduct() {
        return newProduct;
    }

    public void setNewProduct(NewProduct newProduct) {
        this.newProduct = newProduct;
    }

    public SelectItem getApprover() {
        return this.approver;
    }

    public void setApprover(final SelectItem approver) {
        this.approver = approver;
    }

    public boolean isApproveProcessEnabled() {
        return this.approveProcessEnabled;
    }

    public void setApproveProcessEnabled(final boolean approveProcessEnabled) {
        this.approveProcessEnabled = approveProcessEnabled;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }

    public HashMap<String, Object> getCustomFieldValuesItems() {
        return customFieldValues;
    }

    public void setCustomFieldValuesItems(HashMap<String, Object> customFieldValues) {
        this.customFieldValues = customFieldValues;
    }

    public SelectItem[] getTemplates() {
        return this.templates;
    }

    public void setTemplates(final SelectItem[] templates) {
        this.templates = templates;
    }
}
