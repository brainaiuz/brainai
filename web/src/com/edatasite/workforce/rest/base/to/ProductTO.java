package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductLocationItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.MultiPriceItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Dilshod Madrahimov.
 */
public class ProductTO implements IsSerializable {
    Integer id;
    String name;
    String number;
    String description;
    SelectItemTO type;
    SelectItemTO category;
    ArrayList<DiscountTO> discounts;
    SelectItemTO brand;
    TaxTO tax;
    TaxTO doubleTax;
    BigDecimal costPrice;
    BigDecimal sellingPrice;
    BigDecimal averageCost;
    SelectItemTO cogsAccount;
    SelectItemTO account;
    BigDecimal comission;
    SelectItemTO unitMeasurement;
    ArrayList<SelectItemTO> suppliers;
    String barcode;
    String manufacturer;
    String skuNumber;
    String upcNumber;
    ArrayList<InventoryStockTO> inventoryStocks;
    ArrayList<MultiPriceItemTO> multiPriceItems;
    SelectItemTO assetAccount;
    BigDecimal totalValue;
    Long asOfDate;

    public ProductTO() {

    }

    public ProductTO(ProductItem product) {
        this.id = product.getObjectId();
        this.name = product.getName();
        this.number = product.getProductNumber();
        this.type = new SelectItemTO(product.getType(), product.getTypeName());
    }

    public ProductTO(NewProduct product) {
        this.id = product.getObjectId();
        this.name = product.getItemName();
        this.number = product.getNumberData() != null ? product.getNumberData().getNumberString() : null;
        this.description = product.getDescription();
        if (product.getType() != null) {
            this.type = new SelectItemTO(product.getType(), AccountingConstants.PRODUCT_TYPES[product.getType() - 1].getName());
        }
        if (product.getCategoryID() != null) {
            this.category = new SelectItemTO(product.getCategoryID(), product.getCategoryName());
        }
        if (product.getBrandID() != null) {
            this.brand = new SelectItemTO(product.getBrandID(), product.getBrandName());
        }
        if (product.getDiscountItems() != null && product.getDiscountItems().length > 0) {
            ArrayList<DiscountTO> discountTOs = new ArrayList<>();
            for (DiscountItem discountItem : product.getDiscountItems()) {
                discountTOs.add(new DiscountTO(discountItem));
            }
            this.discounts = discountTOs;
        }
        if (product.getTaxItem() != null) {
            this.tax = new TaxTO(product.getTaxItem());
        }
        if (product.getDoubleTaxItem() != null) {
            this.doubleTax = new TaxTO(product.getDoubleTaxItem());
        }

        this.sellingPrice = product.getSellingPrice();
        this.costPrice = product.getUnitPrice();
        this.averageCost = product.getAverageCost();
        this.comission = product.getComission();

        if (product.getCogsAccount() != null) {
            this.cogsAccount = new SelectItemTO(product.getCogsAccount().getId(), product.getCogsAccount().getName());
        }
        if (product.getAccountItem() != null) {
            this.account = new SelectItemTO(product.getAccountItem().getId(), product.getAccountItem().getName());
        }
        if (product.getProductLocations() != null && product.getProductLocations().length > 0) {
            ArrayList<InventoryStockTO> inventoryStockTOs = new ArrayList<>();
            for (ProductLocationItem item : product.getProductLocations()) {
                inventoryStockTOs.add(new InventoryStockTO(item));
            }
            this.inventoryStocks = inventoryStockTOs;
        }
        if (product.getMultiPrices().size() > 0) {
            ArrayList<MultiPriceItemTO> multiPriceItemTOs = new ArrayList<>();
            for (MultiPriceItem item : product.getMultiPrices()) {
                multiPriceItemTOs.add(new MultiPriceItemTO(item));
            }
            this.multiPriceItems = multiPriceItemTOs;
        }
        if (product.getSuppliers() != null && product.getSuppliers().length > 0) {
            ArrayList<SelectItemTO> supplierTOs = new ArrayList<>();
            for (SelectItem item : product.getSuppliers()) {
                supplierTOs.add(new SelectItemTO(item));
            }
            this.suppliers = supplierTOs;
        }

        this.manufacturer = product.getManufacturer();
        this.skuNumber = product.getInternalSKUNumber();
        this.barcode = product.getBarCodeText();
        this.upcNumber = product.getUpcNumber();
        this.asOfDate = WrapUtils.dateToLong(product.getAsOf());
        this.totalValue = product.getTotalValue();
        if (product.getAssetAccount() != null) {
            this.assetAccount = new SelectItemTO(product.getAssetAccount());
        }

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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SelectItemTO getType() {
        return type;
    }

    public void setType(SelectItemTO type) {
        this.type = type;
    }

    public SelectItemTO getCategory() {
        return category;
    }

    public void setCategory(SelectItemTO category) {
        this.category = category;
    }

    public ArrayList<DiscountTO> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(ArrayList<DiscountTO> discounts) {
        this.discounts = discounts;
    }

    public SelectItemTO getBrand() {
        return brand;
    }

    public void setBrand(SelectItemTO brand) {
        this.brand = brand;
    }

    public TaxTO getTax() {
        return tax;
    }

    public void setTax(TaxTO tax) {
        this.tax = tax;
    }

    public TaxTO getDoubleTax() {
        return doubleTax;
    }

    public void setDoubleTax(TaxTO doubleTax) {
        this.doubleTax = doubleTax;
    }

    public BigDecimal getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(BigDecimal costPrice) {
        this.costPrice = costPrice;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public SelectItemTO getCogsAccount() {
        return cogsAccount;
    }

    public void setCogsAccount(SelectItemTO cogsAccount) {
        this.cogsAccount = cogsAccount;
    }

    public SelectItemTO getAccount() {
        return account;
    }

    public void setAccount(SelectItemTO account) {
        this.account = account;
    }

    public BigDecimal getComission() {
        return comission;
    }

    public void setComission(BigDecimal comission) {
        this.comission = comission;
    }

    public BigDecimal getAverageCost() {
        return averageCost;
    }

    public void setAverageCost(BigDecimal averageCost) {
        this.averageCost = averageCost;
    }

    public ArrayList<InventoryStockTO> getInventoryStocks() {
        return inventoryStocks;
    }

    public void setInventoryStocks(ArrayList<InventoryStockTO> inventoryStocks) {
        this.inventoryStocks = inventoryStocks;
    }

    public SelectItemTO getUnitMeasurement() {
        return unitMeasurement;
    }

    public void setUnitMeasurement(SelectItemTO unitMeasurement) {
        this.unitMeasurement = unitMeasurement;
    }

    public ArrayList<SelectItemTO> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(ArrayList<SelectItemTO> suppliers) {
        this.suppliers = suppliers;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public ArrayList<MultiPriceItemTO> getMultiPriceItems() {
        return multiPriceItems;
    }

    public void setMultiPriceItems(ArrayList<MultiPriceItemTO> multiPriceItems) {
        this.multiPriceItems = multiPriceItems;
    }

    public String getSkuNumber() {
        return skuNumber;
    }

    public void setSkuNumber(String skuNumber) {
        this.skuNumber = skuNumber;
    }

    public String getUpcNumber() {
        return upcNumber;
    }

    public void setUpcNumber(String upcNumber) {
        this.upcNumber = upcNumber;
    }

    public SelectItemTO getAssetAccount() {
        return assetAccount;
    }

    public void setAssetAccount(SelectItemTO assetAccount) {
        this.assetAccount = assetAccount;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public Long getAsOfDate() {
        return asOfDate;
    }

    public void setAsOfDate(Long asOfDate) {
        this.asOfDate = asOfDate;
    }

}
