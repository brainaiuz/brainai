package com.workforcetrack.mobile.rpc.accounting;

import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductLocationItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.workforcetrack.mobile.rpc.client.MSelectItem;
import com.workforcetrack.mobile.rpc.expense.MAccountItem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 5/23/11
 * Time: 12:52 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "productListItem")
public class MProductListItem {

    private String itemName;
    private Integer objectID;
    private Integer accountID;
    private Integer cogsAccountID;
    private String cogsAccount;
    private Integer assetAccountID;
    private String assetAccount;
    private Integer vatID;
    private Integer type;
    private String typeName;
    private String internalSKUNumber;
    private String upcNumber;
    private String description;
    private BigDecimal sellingPrice;
    private BigDecimal unitPrice;
    private BigDecimal quantity;
    private BigDecimal totalQtyOnHand;
    private BigDecimal totalValue;
    private Boolean trackInventory;
    private Integer[] taxIDs;
    private MTaxList taxList = new MTaxList();
    private MAccountsByCategory accounts = new MAccountsByCategory();
    private String productNumber;
    private MAccountItem accountItem;

    //from ProductItem
    private BigDecimal itemsInStock;
    private String account;
    private String taxRate;
    private Boolean inventoryItem;
    private String warehouseName;
    private String locationName;
    private String skuNumber;
    private BigDecimal minReorderPoint;
    private Boolean showInStoreFront;

    public List<MDiscountItem> discountItems;

    //for WFP ProductItem
    private Double rating;
    private BigDecimal rentalRate;
    private String vendorName;
    private Boolean freeShipping;
    private Integer categoryID;
    private String categoryName;
    private Integer rentalPeriod;
    private List<MProductPicture> pictures;
    private String customName;
    private String asOf;
    private List<MCompanyCustomField> customFieldItems;
    public List<MSelectItem> productLocations;
    private Integer warehouseID;
    public List<MSelectItem> productCategories;
    private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    public MProductListItem() {
    }

    public MProductListItem(NewProduct newProduct) {
        if (newProduct != null) {
            this.objectID = newProduct.getObjectId();
            this.itemName = newProduct.getItemName();
            this.accountID = newProduct.getAccountId();
            if (newProduct.getAccountItem() != null) {
                this.account = newProduct.getAccountItem().getName();
                //this.accountItem = new MAccountItem(newProduct.getAccountItem());
            }

            this.vatID = newProduct.getTaxItem() != null ? newProduct.getTaxItem().getId() : null;
            this.type = newProduct.getType();
            this.internalSKUNumber = newProduct.getInternalSKUNumber();
            this.upcNumber = newProduct.getUpcNumber();
            this.description = newProduct.getDescription();
            this.sellingPrice = newProduct.getSellingPrice();
            this.unitPrice = newProduct.getUnitPrice();
            this.quantity = newProduct.getQuantity();
            this.trackInventory = newProduct.getTrackInventory();
            this.taxList = new MTaxList(newProduct.getTaxList());
            this.accounts = new MAccountsByCategory(newProduct.getAccounts());
            this.productNumber = newProduct.getNumberData() != null ? newProduct.getNumberData().getNumberString() : "";
            //this.accountItem = new MAccountItem(newProduct.getAccountItem());
            this.taxIDs = newProduct.getTaxIDs();
            this.totalQtyOnHand = newProduct.getTotalQtyOnHand();
            if (newProduct.getWarehouse() != null)
            this.warehouseName = newProduct.getWarehouse().getName();
            this.quantity = newProduct.getQuantity();

            if (newProduct.getProductLocations() != null && newProduct.getProductLocations().length > 0) {
                this.productLocations = new ArrayList<>();
                for (ProductLocationItem item : newProduct.getProductLocations()){
                    SelectItem selectItem = new SelectItem(item.getWarehouseID(), item.getWarehouseName(), item.getQty().toString());
                    this.productLocations.add(new MSelectItem(selectItem));
                }
            }
            if (newProduct.getDiscountItems() != null && newProduct.getDiscountItems().length > 0) {
                this.discountItems = new ArrayList<>();
                for (DiscountItem discountItem : newProduct.getDiscountItems()) {
                    this.discountItems.add(new MDiscountItem(discountItem));
                }
            }
            if (newProduct.getProductCategories() != null && newProduct.getProductCategories().length > 0) {
                this.productCategories = new ArrayList<>();
                for (SelectItem item : newProduct.getProductCategories()) {
                    this.productCategories.add(new MSelectItem(item.getId(), item.getName()));
                }
            }
        }
    }

    public MProductListItem(ProductItem productItem) {
        if (productItem != null) {
            this.objectID = productItem.getObjectId();
            this.itemName = productItem.getName();
            this.description = productItem.getDescription();
            this.unitPrice = productItem.getUnitpPrice();
            this.totalValue = productItem.getTotalValue();
            this.sellingPrice = productItem.getCostPrice();
            this.type = productItem.getType();
            this.typeName = productItem.getTypeName();
            this.itemsInStock = productItem.getItemsInStock();
            this.account = productItem.getAccount();
            this.taxRate = productItem.getTaxRate();
            this.inventoryItem = productItem.isInventoryItem();
            this.warehouseName = productItem.getWarehouseName();
//            this.locationName = productItem.getLocationName();
            this.skuNumber = productItem.getSkuNumber();
            this.minReorderPoint = productItem.getMinReorderPoint();
            this.showInStoreFront = productItem.isStorefrontEnable();
            this.productNumber = productItem.getProductNumber();
            this.accountID = productItem.getAccountID();
            this.cogsAccount = productItem.getCogsAccount();
            this.cogsAccountID = productItem.getCogsAccountID();
            this.assetAccount = productItem.getAssetAccount();
            this.assetAccountID = productItem.getAssetAccountID();
            this.vatID = productItem.getTaxRateID();
            this.warehouseID = productItem.getWarehouseId();
            if(productItem.getProductLocations() != null) {
                for (ProductLocationItem item : productItem.getProductLocations()) {
                    this.quantity = item.getQty();
                    this.totalQtyOnHand = item.getQty();
                    break;
                }
            }
            this.categoryID = productItem.getCategoryId();
            this.categoryName = productItem.getCategory();
            this.asOf = productItem.getAsOf() != null ? productItem.getAsOf().getDate().toString() : "";
        }

    }

    public static boolean convert(NewProduct newProduct, MProductListItem mProductListItem, boolean fromNewProduct) {

        if (newProduct == null || mProductListItem == null) {
            return false;
        }

        try {
            if (fromNewProduct) {

            } else {
                newProduct.setObjectId(mProductListItem.getObjectID());
                newProduct.setItemName(mProductListItem.getItemName());
                newProduct.setAccountId(mProductListItem.getAccountID());
                if (mProductListItem.getVatID() != null)
                    newProduct.setTaxItem(new TaxItem(mProductListItem.getVatID(), null));
                newProduct.setType(mProductListItem.getType());
                newProduct.setInternalSKUNumber(mProductListItem.getInternalSKUNumber());
                newProduct.setUpcNumber(mProductListItem.getUpcNumber());
                newProduct.setDescription(mProductListItem.getDescription());
                newProduct.setSellingPrice(mProductListItem.getSellingPrice());
                newProduct.setUnitPrice(mProductListItem.getUnitPrice());
                newProduct.setQuantity(mProductListItem.getQuantity());
                newProduct.setTrackInventory(mProductListItem.isTrackInventory());
                newProduct.setCategoryID(mProductListItem.getCategoryID());
                newProduct.setCogsAccountID(mProductListItem.getCogsAccountID());
                newProduct.setAssetAccountID(mProductListItem.getAssetAccountID());
                if (mProductListItem.getAsOf() != null) {
                    try {
                        newProduct.setAsOf(new DateNonConvertable(df.parse(mProductListItem.getAsOf())));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                newProduct.setTotalQtyOnHand(mProductListItem.getTotalQtyOnHand());
                newProduct.setTotalValue(newProduct.getUnitPrice().multiply(newProduct.getQuantity()));

                NumberData numberData = new NumberData();
                numberData.setNumberString(mProductListItem.getProductNumber());
                //newProduct.setTaxIDs(new Integer[]{mProductListItem.getVatID()});
                newProduct.setVatId(mProductListItem.getVatID());

                // newProduct.setTaxList(MTaxList.convert(mProductListItem.getTaxList()));
                // newProduct.setAccounts(MAccountsByCategory.convert(mProductListItem.getAccounts()));
            }

            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void getTaxRateByTaxIDs() {
        if (this.taxList != null && this.taxList.getTaxItems() != null && this.getTaxIDs() != null && this.getTaxIDs().length > 0 && this.getTaxIDs()[0] != null) {
            for (MTaxItem taxItem : this.taxList.getTaxItems()) {
                if (this.getTaxIDs()[0].equals(taxItem.getObjectID())) {
                    this.taxRate = taxItem.getName();
                    break;
                }
            }
        }
    }

    public MAccountItem getAccountItem() {
        return accountItem;
    }

    public void setAccountItem(MAccountItem accountItem) {
        this.accountItem = accountItem;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getAccountID() {
        return accountID;
    }

    public void setAccountID(Integer accountID) {
        this.accountID = accountID;
    }

    public Integer getVatID() {
        return vatID;
    }

    public void setVatID(Integer vatID) {
        this.vatID = vatID;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getInternalSKUNumber() {
        return internalSKUNumber;
    }

    public void setInternalSKUNumber(String internalSKUNumber) {
        this.internalSKUNumber = internalSKUNumber;
    }

    public String getUpcNumber() {
        return upcNumber;
    }

    public void setUpcNumber(String upcNumber) {
        this.upcNumber = upcNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Boolean isTrackInventory() {
        return trackInventory;
    }

    public void setTrackInventory(Boolean trackInventory) {
        this.trackInventory = trackInventory;
    }

    public Integer[] getTaxIDs() {
        return taxIDs;
    }

    public void setTaxIDs(Integer[] taxIDs) {
        this.taxIDs = taxIDs;
    }

    public MTaxList getTaxList() {
        return taxList;
    }

    public void setTaxList(MTaxList taxList) {
        this.taxList = taxList;
    }

    public MAccountsByCategory getAccounts() {
        return accounts;
    }

    public void setAccounts(MAccountsByCategory accounts) {
        this.accounts = accounts;
    }

    public BigDecimal getItemsInStock() {
        return itemsInStock;
    }

    public void setItemsInStock(BigDecimal itemsInStock) {
        this.itemsInStock = itemsInStock;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public Boolean isInventoryItem() {
        return inventoryItem;
    }

    public void setInventoryItem(Boolean inventoryItem) {
        this.inventoryItem = inventoryItem;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getSkuNumber() {
        return skuNumber;
    }

    public void setSkuNumber(String skuNumber) {
        this.skuNumber = skuNumber;
    }

    public BigDecimal getMinReorderPoint() {
        return minReorderPoint;
    }

    public void setMinReorderPoint(BigDecimal minReorderPoint) {
        this.minReorderPoint = minReorderPoint;
    }

    public Boolean isShowInStoreFront() {
        return showInStoreFront;
    }

    public void setShowInStoreFront(Boolean showInStoreFront) {
        this.showInStoreFront = showInStoreFront;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public BigDecimal getRentalRate() {
        return rentalRate;
    }

    public void setRentalRate(BigDecimal rentalRate) {
        this.rentalRate = rentalRate;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public Boolean getFreeShipping() {
        return freeShipping;
    }

    public void setFreeShipping(Boolean freeShipping) {
        this.freeShipping = freeShipping;
    }

    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getRentalPeriod() {
        return rentalPeriod;
    }

    public void setRentalPeriod(Integer rentalPeriod) {
        this.rentalPeriod = rentalPeriod;
    }

    public List<MProductPicture> getPictures() {
        return pictures;
    }

    public void setPictures(List<MProductPicture> pictures) {
        this.pictures = pictures;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public List<MCompanyCustomField> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setCustomFieldItems(List<MCompanyCustomField> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }

    public Integer getCogsAccountID() {
        return cogsAccountID;
    }

    public void setCogsAccountID(Integer cogsAccountID) {
        this.cogsAccountID = cogsAccountID;
    }

    public Integer getAssetAccountID() {
        return assetAccountID;
    }

    public void setAssetAccountID(Integer assetAccountID) {
        this.assetAccountID = assetAccountID;
    }

    public String getAsOf() {
        return asOf;
    }

    public void setAsOf(String asOf) {
        this.asOf = asOf;
    }

    public BigDecimal getTotalQtyOnHand() {
        return totalQtyOnHand;
    }

    public void setTotalQtyOnHand(BigDecimal totalQtyOnHand) {
        this.totalQtyOnHand = totalQtyOnHand;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public List<MSelectItem> getProductLocations() {
        return productLocations;
    }

    public void setProductLocations(List<MSelectItem> productLocations) {
        this.productLocations = productLocations;
    }

}
