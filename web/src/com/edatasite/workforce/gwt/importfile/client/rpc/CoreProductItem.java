package com.edatasite.workforce.gwt.importfile.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Sep 21, 2010
 * Time: 4:19:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class CoreProductItem implements IsSerializable {
    private Integer objectId;
    private Integer itemId;
    private Integer numberId;
    private Integer nameId;
    private Integer spNameId;
    private Integer prNameId;
    private Integer frNameId;
    private Integer descriptionId;
    private Integer spDescriptionId;
    private Integer prDescriptionId;
    private Integer frDescriptionId;
    private CustomisedImportData category;

    private CustomisedImportData productType;
    private Integer skuNumberId;
    private Integer barcodeId;
    private Integer upcNumberId;
    private CustomisedImportData unitMeasurement;
    private CustomisedImportData vendor;
    private CustomisedImportData brand;
    private CustomisedImportData condition;

    private Integer costPriceId;
    private Integer sellingPriceId;
    private CustomisedImportData account;
    private CustomisedImportData cogsAccount;
    //    private CustomisedImportData incomeAccount;
    private CustomisedImportData taxRate;
//    private CustomisedImportData priceLevel;
//    private CustomisedImportData customPrice;

    private Integer warehouseNameId;
    private Integer locationNameId;
    private Integer sysWarehouseId;
    //    private Integer sysLocationId;
    private Integer quantityId;
//    private Integer minReorderPointId;

    private Integer showInStoreFrontId;
    private Integer featuredId;
    private Integer virtualId;
    private Integer freeShippingId;
    private Integer specialOfferId;
    private Integer showOnHomepageId;

    //    private CustomisedImportData kitProduct;
//    private Integer kitProductQuantityId;
    private CustomisedImportData assetAccount;
    private Integer globalReorderPointId;
    //    private Integer totalValueId;
    private Integer asOfDateId;
    private CustomisedImportData rentalPeriod;
    private Integer overdueRateId;
    private Integer cancellationPeriodId;
    private Integer cancellationFeeId;
    private Integer manufacturerId;
    private Integer partNumberId;
    private Integer openingBalanceDate;
    private Integer brandId;
    private boolean multiWarehouseEnabled;

    private ArrayList<CompanyCustomFieldItem> customFields;

    private HashMap<Integer, Integer> multiPrices;
    private HashMap<Integer, Integer> multiWarehouses;//warehouseID,columnID
    private HashMap<Integer, Integer> multiPriceLevels;//priceLevelID,columnID

    public CoreProductItem() {
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getNumberId() {
        return numberId;
    }

    public void setNumberId(Integer numberId) {
        this.numberId = numberId;
    }

    public Integer getNameId() {
        return nameId;
    }

    public void setNameId(Integer nameId) {
        this.nameId = nameId;
    }

    public Integer getDescriptionId() {
        return descriptionId;
    }

    public void setDescriptionId(Integer descriptionId) {
        this.descriptionId = descriptionId;
    }

    public Integer getSkuNumberId() {
        return skuNumberId;
    }

    public void setSkuNumberId(Integer skuNumberId) {
        this.skuNumberId = skuNumberId;
    }

    public Integer getBarcodeId() {
        return barcodeId;
    }

    public void setBarcodeId(Integer barcodeId) {
        this.barcodeId = barcodeId;
    }

    public Integer getUpcNumberId() {
        return upcNumberId;
    }

    public void setUpcNumberId(Integer upcNumberId) {
        this.upcNumberId = upcNumberId;
    }

    public Integer getCostPriceId() {
        return costPriceId;
    }

    public void setCostPriceId(Integer costPriceId) {
        this.costPriceId = costPriceId;
    }

    public Integer getSellingPriceId() {
        return sellingPriceId;
    }

    public void setSellingPriceId(Integer sellingPriceId) {
        this.sellingPriceId = sellingPriceId;
    }

    public Integer getShowInStoreFrontId() {
        return showInStoreFrontId;
    }

    public void setShowInStoreFrontId(Integer showInStoreFrontId) {
        this.showInStoreFrontId = showInStoreFrontId;
    }

    public Integer getFeaturedId() {
        return featuredId;
    }

    public void setFeaturedId(Integer featuredId) {
        this.featuredId = featuredId;
    }

    public Integer getVirtualId() {
        return virtualId;
    }

    public void setVirtualId(Integer virtualId) {
        this.virtualId = virtualId;
    }

    public Integer getFreeShippingId() {
        return freeShippingId;
    }

    public void setFreeShippingId(Integer freeShippingId) {
        this.freeShippingId = freeShippingId;
    }

    public Integer getSpecialOfferId() {
        return specialOfferId;
    }

    public void setSpecialOfferId(Integer specialOfferId) {
        this.specialOfferId = specialOfferId;
    }

    public Integer getShowOnHomepageId() {
        return showOnHomepageId;
    }

    public void setShowOnHomepageId(Integer showOnHomepageId) {
        this.showOnHomepageId = showOnHomepageId;
    }

    public Integer getWarehouseNameId() {
        return warehouseNameId;
    }

    public void setWarehouseNameId(Integer warehouseNameId) {
        this.warehouseNameId = warehouseNameId;
    }

    public Integer getLocationNameId() {
        return locationNameId;
    }

    public void setLocationNameId(Integer locationNameId) {
        this.locationNameId = locationNameId;
    }

    public Integer getSysWarehouseId() {
        return sysWarehouseId;
    }

    public void setSysWarehouseId(Integer sysWarehouseId) {
        this.sysWarehouseId = sysWarehouseId;
    }

//    public Integer getSysLocationId() {
//        return sysLocationId;
//    }
//
//    public void setSysLocationId(Integer sysLocationId) {
//        this.sysLocationId = sysLocationId;
//    }

    public Integer getQuantityId() {
        return quantityId;
    }

    public void setQuantityId(Integer quantityId) {
        this.quantityId = quantityId;
    }

//    public Integer getMinReorderPointId() {
//        return minReorderPointId;
//    }
//
//    public void setMinReorderPointId(Integer minReorderPointId) {
//        this.minReorderPointId = minReorderPointId;
//    }

    public CustomisedImportData getCategory() {
        return category;
    }

    public void setCategory(CustomisedImportData category) {
        this.category = category;
    }

    public CustomisedImportData getProductType() {
        return productType;
    }

    public void setProductType(CustomisedImportData productType) {
        this.productType = productType;
    }

    public CustomisedImportData getUnitMeasurement() {
        return unitMeasurement;
    }

    public void setUnitMeasurement(CustomisedImportData unitMeasurement) {
        this.unitMeasurement = unitMeasurement;
    }

    public CustomisedImportData getVendor() {
        return vendor;
    }

    public void setVendor(CustomisedImportData vendor) {
        this.vendor = vendor;
    }

    public CustomisedImportData getBrand() {
        return brand;
    }

    public void setBrand(CustomisedImportData brand) {
        this.brand = brand;
    }

    public CustomisedImportData getCondition() {
        return condition;
    }

    public void setCondition(CustomisedImportData condition) {
        this.condition = condition;
    }

    public CustomisedImportData getAccount() {
        return account;
    }

    public void setAccount(CustomisedImportData account) {
        this.account = account;
    }

    public CustomisedImportData getCogsAccount() {
        return cogsAccount;
    }

    public void setCogsAccount(CustomisedImportData cogsAccount) {
        this.cogsAccount = cogsAccount;
    }

//    public CustomisedImportData getIncomeAccount() {
//        return incomeAccount;
//    }
//
//    public void setIncomeAccount(CustomisedImportData incomeAccount) {
//        this.incomeAccount = incomeAccount;
//    }

    public CustomisedImportData getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(CustomisedImportData taxRate) {
        this.taxRate = taxRate;
    }

//    public CustomisedImportData getKitProduct() {
//        return kitProduct;
//    }
//
//    public void setKitProduct(CustomisedImportData kitProduct) {
//        this.kitProduct = kitProduct;
//    }
//
//    public Integer getKitProductQuantityId() {
//        return kitProductQuantityId;
//    }
//
//    public void setKitProductQuantityId(Integer kitProductQuantityId) {
//        this.kitProductQuantityId = kitProductQuantityId;
//    }

    public CustomisedImportData getAssetAccount() {
        return assetAccount;
    }

    public void setAssetAccount(CustomisedImportData assetAccount) {
        this.assetAccount = assetAccount;
    }

    public Integer getGlobalReorderPointId() {
        return globalReorderPointId;
    }

    public void setGlobalReorderPointId(Integer globalReorderPointId) {
        this.globalReorderPointId = globalReorderPointId;
    }

//    public Integer getTotalValueId() {
//        return totalValueId;
//    }
//
//    public void setTotalValueId(Integer totalValueId) {
//        this.totalValueId = totalValueId;
//    }

    public Integer getAsOfDateId() {
        return asOfDateId;
    }

    public void setAsOfDateId(Integer asOfDateId) {
        this.asOfDateId = asOfDateId;
    }

    public CustomisedImportData getRentalPeriod() {
        return rentalPeriod;
    }

    public void setRentalPeriod(CustomisedImportData rentalPeriod) {
        this.rentalPeriod = rentalPeriod;
    }

    public Integer getOverdueRateId() {
        return overdueRateId;
    }

    public void setOverdueRateId(Integer overdueRateId) {
        this.overdueRateId = overdueRateId;
    }

    public Integer getCancellationPeriodId() {
        return cancellationPeriodId;
    }

    public void setCancellationPeriodId(Integer cancellationPeriodId) {
        this.cancellationPeriodId = cancellationPeriodId;
    }

    public Integer getCancellationFeeId() {
        return cancellationFeeId;
    }

    public void setCancellationFeeId(Integer cancellationFeeId) {
        this.cancellationFeeId = cancellationFeeId;
    }

    public Integer getManufacturerId() {
        return manufacturerId;
    }

    public void setManufacturerId(Integer manufacturerId) {
        this.manufacturerId = manufacturerId;
    }

    public Integer getPartNumberId() {
        return partNumberId;
    }

    public void setPartNumberId(Integer partNumberId) {
        this.partNumberId = partNumberId;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getSpNameId() {
        return spNameId;
    }

    public void setSpNameId(Integer spNameId) {
        this.spNameId = spNameId;
    }

    public Integer getPrNameId() {
        return prNameId;
    }

    public void setPrNameId(Integer prNameId) {
        this.prNameId = prNameId;
    }

    public Integer getFrNameId() {
        return frNameId;
    }

    public void setFrNameId(Integer frNameId) {
        this.frNameId = frNameId;
    }

    public Integer getSpDescriptionId() {
        return spDescriptionId;
    }

    public void setSpDescriptionId(Integer spDescriptionId) {
        this.spDescriptionId = spDescriptionId;
    }

    public Integer getPrDescriptionId() {
        return prDescriptionId;
    }

    public void setPrDescriptionId(Integer prDescriptionId) {
        this.prDescriptionId = prDescriptionId;
    }

    public Integer getFrDescriptionId() {
        return frDescriptionId;
    }

    public void setFrDescriptionId(Integer frDescriptionId) {
        this.frDescriptionId = frDescriptionId;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(ArrayList<CompanyCustomFieldItem> customFields) {
        this.customFields = customFields;
    }

    public HashMap<Integer, Integer> getMultiPrices() {
        if(multiPrices == null){
            multiPrices = new HashMap<>();
        }
        return multiPrices;
    }

    public void setMultiPrices(HashMap<Integer, Integer> multiPrices) {
        this.multiPrices = multiPrices;
    }

    public Integer getOpeningBalanceDate() {
        return openingBalanceDate;
    }

    public void setOpeningBalanceDate(Integer openingBalanceDate) {
        this.openingBalanceDate = openingBalanceDate;
    }

    public boolean isMultiWarehouseEnabled() {
        return multiWarehouseEnabled;
    }

    public void setMultiWarehouseEnabled(boolean multiWarehouseEnabled) {
        this.multiWarehouseEnabled = multiWarehouseEnabled;
    }

    public HashMap<Integer, Integer> getMultiWarehouses() {
        if (multiWarehouses == null) {
            multiWarehouses = new HashMap<>();
        }
        return multiWarehouses;
    }

    public HashMap<Integer, Integer> getMultiPriceLevels() {
        if (multiPriceLevels == null) {
            multiPriceLevels = new HashMap<>();
        }
        return multiPriceLevels;
    }

    public ImportFile getImportFile() {
        ImportFile importFile = createColumns(this);
        importFile.setFileID(getObjectId());
        return importFile;
    }

    private ImportFile createColumns(CoreProductItem item) {
        ImportFile importFile = new ImportFile();
        if (item != null) {
            importFile.addColumn(ImportField.ProductFields.FIELD_ITEM_ID, item.getItemId());
            importFile.addColumn(ImportField.ProductFields.FIELD_NUMBER, item.getNumberId());
            importFile.addColumn(ImportField.ProductFields.FIELD_NAME, item.getNameId());
            importFile.addColumn(ImportField.ProductFields.FIELD_DESCRIPTION, item.getDescriptionId());
            importFile.addColumn(ImportField.ProductFields.FIELD_CATEGORY, item.getCategory().getCsvColumnId());
            importFile.addColumn(ImportField.ProductFields.FIELD_BRAND, item.getBrandId());

            importFile.addColumn(ImportField.ProductFields.FIELD_PRODUCT_TYPE, item.getProductType().getCsvColumnId());
            importFile.addColumn(ImportField.ProductFields.FIELD_SKU_NUMBER, item.getSkuNumberId());
            importFile.addColumn(ImportField.ProductFields.FIELD_UPC_NUMBER, item.getUpcNumberId());
            importFile.addColumn(ImportField.ProductFields.FIELD_MANUFACTURER, item.getManufacturerId());
            importFile.addColumn(ImportField.ProductFields.FIELD_PART_NUMBER, item.getPartNumberId());

            importFile.addColumn(ImportField.ProductFields.FIELD_UNIT_MEASUREMENT, item.getUnitMeasurement().getCsvColumnId());
            importFile.addColumn(ImportField.ProductFields.FIELD_VENDOR, item.getVendor().getCsvColumnId());

            importFile.addColumn(ImportField.ProductFields.FIELD_COST_PRICE, item.getCostPriceId());
            importFile.addColumn(ImportField.ProductFields.FIELD_SELLING_PRICE, item.getSellingPriceId());
            importFile.addColumn(ImportField.ProductFields.FIELD_ACCOUNT, item.getAccount().getCsvColumnId());
            importFile.addColumn(ImportField.ProductFields.FIELD_COGS_ACCOUNT, item.getCogsAccount().getCsvColumnId());
            importFile.addColumn(ImportField.ProductFields.FIELD_ACCOUNT, item.getAccount().getCsvColumnId());
            importFile.addColumn(ImportField.ProductFields.FIELD_TAX_RATE, item.getTaxRate().getCsvColumnId());
            importFile.addColumn(ImportField.ProductFields.FIELD_LOCATION, item.getLocationNameId());
            if (item.isMultiWarehouseEnabled()) {
                if (item.getMultiWarehouses().size() > 0) {
                    for (Integer columnId : item.getMultiWarehouses().keySet()) {
                        importFile.addColumn(columnId, item.getMultiWarehouses().get(columnId));
                    }
                }
            }
            if (item.getMultiPriceLevels().size() > 0) {
                for (Integer columnId : item.getMultiPriceLevels().keySet()) {
                    importFile.addColumn(columnId, item.getMultiPriceLevels().get(columnId));
                }
            }
            importFile.addColumn(ImportField.ProductFields.FIELD_WAREHOUSE, item.getWarehouseNameId());
            importFile.addColumn(ImportField.ProductFields.FIELD_QUANTITY, item.getQuantityId());
            importFile.addColumn(ImportField.ProductFields.FIELD_BARCODE, item.getBarcodeId());

            /*importFile.addColumn(ImportField.ProductFields.FIELD_WEB_ENABLED, item.getShowInStoreFrontId());
            importFile.addColumn(ImportField.ProductFields.FIELD_FEATURED, item.getFeaturedId());
            importFile.addColumn(ImportField.ProductFields.FIELD_VIRTUAL, item.getVirtualId());
            importFile.addColumn(ImportField.ProductFields.FIELD_FREE_SHIPPING, item.getFreeShippingId());
            importFile.addColumn(ImportField.ProductFields.FIELD_SPECIAL_OFFER, item.getSpecialOfferId());
            importFile.addColumn(ImportField.ProductFields.FIELD_SHOW_ON_HOMEPAGE, item.getShowOnHomepageId());*/

            importFile.addColumn(ImportField.ProductFields.FIELD_ASSET_ACCOUNT, item.getAssetAccount().getCsvColumnId());
            importFile.addColumn(ImportField.ProductFields.FIELD_GLOBAL_REORDER_POINT, item.getGlobalReorderPointId());
            importFile.addColumn(ImportField.ProductFields.FIELD_OPENING_BALANCE_DATE, item.getOpeningBalanceDate());

            importFile.addColumn(ImportField.ProductFields.SYSTEM_CATEGORY_ID, item.getCategory().getSystemSelectedId());
            importFile.addColumn(ImportField.ProductFields.SYSTEM_PRODUCT_TYPE_ID, item.getProductType().getSystemSelectedId());
            importFile.addColumn(ImportField.ProductFields.SYSTEM_UNIT_MEASUREMENT_ID, item.getUnitMeasurement().getSystemSelectedId());
            importFile.addColumn(ImportField.ProductFields.SYSTEM_VENDOR_ID, item.getVendor().getSystemSelectedId());
            importFile.addColumn(ImportField.ProductFields.SYSTEM_ACCOUNT_ID, item.getAccount().getSystemSelectedId());
            if (item.getCogsAccount() != null && item.getCogsAccount().getSystemSelectedId() != null) {
                importFile.addColumn(ImportField.ProductFields.SYSTEM_COGS_ACCOUNT_ID, item.getCogsAccount().getSystemSelectedId());
            }
            importFile.addColumn(ImportField.ProductFields.SYSTEM_TAX_RATE_ID, item.getTaxRate().getSystemSelectedId());
            importFile.addColumn(ImportField.ProductFields.SYSTEM_WAREHOUSE_ID, item.getSysWarehouseId());
            importFile.addColumn(ImportField.ProductFields.SYSTEM_ASSET_ACCOUNT_ID, item.getAssetAccount().getSystemSelectedId());

            if (item.getMultiPrices().size() > 0) {
                for (Integer columnId : item.getMultiPrices().keySet()) {
                    importFile.addColumn(columnId, item.getMultiPrices().get(columnId));
                }
            }
            if (item.getCustomFields() != null && item.getCustomFields().size() > 0) {
                int s = ImportField.ProductFields.FIELD_CUSTOM_FIELD_START_NUMBER;
                for (CompanyCustomFieldItem customField : item.getCustomFields()) {

                    if (customField != null && customField.getFieldStringValue() != null && !"".equals(customField.getFieldStringValue()) && customField.getFieldStringValue().matches(Constants.REGEX_INTEGER)) {
                        Integer columnID = Integer.parseInt(customField.getFieldStringValue());
                        importFile.addExtraColumn(false,
                                s++,
                                columnID,
                                customField.getDataType(),
                                customField.getColumnCode(),
                                customField.getCustomFieldSettingID() != null ? customField.getCustomFieldSettingID().toString() : "-1",
                                customField.getUiType(),
                                customField.getPredefinedValues() != null ? String.join("-:-", customField.getPredefinedValues()) : null,
                                customField.getFieldName());
                    } else {
                        importFile.addExtraColumn(false, s++, null);
                    }
                }
            }
        }
        return importFile;
    }
}
