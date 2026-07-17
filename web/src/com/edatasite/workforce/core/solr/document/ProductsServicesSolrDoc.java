package com.edatasite.workforce.core.solr.document;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.ChildDocument;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:30.
 */
@SolrDocument(collection = "productsServicesCore")
public class ProductsServicesSolrDoc extends BaseSolrDoc {

    @Id
    @Indexed(name = "oid", type = "string", required = true)
    private String oid;

    @Field("companyId")
    @Indexed(name = "companyId", type = "pint", required = true)
    private Integer companyId;

    @Field("productId")
    private Integer productId;

    @Field("docType")
    private String doctype;

    @Field("productParentId")
    private Integer productParentId;

    @Field("productNumber")
    private String productNumber;

    @Field("partNumber")
    private String partNumber;

    @Field("skuNumber")
    private String skuNumber;

    @Field("upsNumber")
    private String upsNumber;

    @Field("subsidiaryProductUniqNum")
    private String subsidiaryProductUniqNum;

    @Field("productName")
    private String productName;

    @Field("barcode")
    private String barcode;

    @Field("productTypeId")
    private Integer productTypeId;

    @Field("productRentalItemId")
    private Integer productRentalItemId;

    @Field("productActive")
    private Boolean productActive;

    @Field("rentStatus")
    private String rentStatus;

    @Field("rentStatusIdName")
    @Indexed(name = "rentStatusIdName", type = "string", stored = false)
    private String rentStatusIdName;

    @Field("rentStatusId")
    private Integer rentStatusId;

    @Field("rentStatusCode")
    private String rentStatusCode;

    @Field("productStorefrontEnable")
    private Boolean productStorefrontEnable;

    @Field("inventoryTrackingEnabled")
    private Boolean inventoryTrackingEnabled;

    @Field("trackBatchesEnabled")
    private Boolean trackBatchesEnabled;

    @Field("productTypeName")
    private String productTypeName;

    @Field("productTypeIdName")
    @Indexed(name = "productTypeIdName", type = "string", stored = false)
    private String productTypeIdName;

    @Field("accountId")
    private Integer accountId;

    @Field("accountName")
    private String accountName;

    @Field("cogsAccountId")
    private Integer cogsAccountId;

    @Field("cogsAccountName")
    private String cogsAccountName;

    @Field("cogsAccountIdName")
    @Indexed(name = "cogsAccountIdName", type = "string", stored = false)
    private String cogsAccountIdName;

    @Field("assetAccountId")
    private Integer assetAccountId;

    @Field("assetAccountName")
    private String assetAccountName;

    @Field("assetAccountIdName")
    @Indexed(name = "assetAccountIdName", type = "string", stored = false)
    private String assetAccountIdName;

    @Field("accountIdName")
    @Indexed(name = "accountIdName", type = "string", stored = false)
    private String accountIdName;

    @Field("description")
    private String description;

    @Field("taxrateId")
    private Integer taxrateId;

    @Field("taxrate")
    private String taxrate;

    @Field("taxEffectiveRate")
    private Double taxEffectiveRate;

    @Field("vendor")
    private String vendor;

    @Field("manufacturer")
    private String manufacturer;

    @Field("category")
    private String category;

    @Field("categoryId")
    private Integer categoryId;

    @Field("parentCategory")
    private String parentCategory;

    @Field("parentCategoryId")
    private Integer parentCategoryId;

    @Field("unitprice")
    private Double unitprice;

    @Field("costprice")
    private Double costprice;

    @Field("averageCost")
    private String averageCost;

    @Field("quantityOnHand")
    private Double quantityOnHand;

    @Field("multiSupplierId")
    @Indexed(name = "multiSupplierId", type = "pints")
    private List<Integer> multiSupplierId = new ArrayList<>();

    @Field("multiSupplierName")
    @Indexed(name = "multiSupplierName", type = "string")
    private List<String> multiSupplierName = new ArrayList<>();

    @Field("multiSupplierIdName")
    @Indexed(name = "multiSupplierIdName", type = "string", stored = false)
    private List<String> multiSupplierIdName = new ArrayList<>();

    @Field("multiLocationId")
    @Indexed(name = "multiLocationId", type = "pints")
    private List<Integer> multiLocationId = new ArrayList<>();

    @Field("multiLocationName")
    @Indexed(name = "multiLocationName", type = "string")
    private List<String> multiLocationName = new ArrayList<>();

    @Field("multiLocationIdName")
    @Indexed(name = "multiLocationIdName", type = "string", stored = false)
    private List<String> multiLocationIdName = new ArrayList<>();

    @Field("unitMeasureMentId")
    private Integer unitMeasureMentId;

    @Field("unitMeasureMentName")
    private String unitMeasureMentName;

    @Field("unitMeasurementIdName")
    @Indexed(name = "unitMeasurementIdName", type = "string", stored = false)
    private String unitMeasurementIdName;

    @Field("createdDate")
    private Date createdDate;

    @Field("updatedDate")
    private Date updatedDate;

    @Field("brandId")
    private Integer brandId;

    @Field("brandName")
    private String brandName;

    @Field("brandIdName")
    @Indexed(name = "brandIdName", type = "string", stored = false)
    private String brandIdName;

    @Field("warehouseId")
    private Integer warehouseId;

    @Field("warehouseName")
    private String warehouseName;

    @Field("multiSupplierNumber")
    @Indexed(name = "multiSupplierNumber", type = "string")
    private List<String> multiSupplierNumber = new ArrayList<>();

    @Field("warehouseStock")
    private Double warehouseStock;

    @Field("creatorId")
    private Integer creatorId;

    @Field("creatorName")
    private String creatorName;

    @Field("creatorIdName")
    @Indexed(name = "creatorIdName", type = "string", stored = false)
    private String creatorIdName;

    @Field("updaterId")
    private Integer updaterId;

    @Field("updaterName")
    private String updaterName;

    @Field("updaterIdName")
    @Indexed(name = "updaterIdName", type = "string", stored = false)
    private String updaterIdName;

    @Field("productDiscountTypeId")
    @Indexed(name = "productDiscountTypeId")
    private Integer productDiscountTypeId;

    @Field("productDiscountTypeName")
    @Indexed(name = "productDiscountTypeName", type = "string", stored = false)
    private String productDiscountTypeName;

    @Field("productDiscountAmount")
    @Indexed(name = "productDiscountAmount", type = "pdouble", stored = false)
    private Double productDiscountAmount;
    @Field("newProductCustomDescription")
    private List<NewProductCustomDescription> newProductCustomDescriptions;

    @ChildDocument
    private List<ProductsServicesSolrDoc> warehouses = new ArrayList<>();


    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getDoctype() {
        return doctype;
    }

    public void setDoctype(String doctype) {
        this.doctype = doctype;
    }

    public Integer getProductParentId() {
        return productParentId;
    }

    public void setProductParentId(Integer productParentId) {
        this.productParentId = productParentId;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getSkuNumber() {
        return skuNumber;
    }

    public void setSkuNumber(String skuNumber) {
        this.skuNumber = skuNumber;
    }

    public String getUpsNumber() {
        return upsNumber;
    }

    public void setUpsNumber(String upsNumber) {
        this.upsNumber = upsNumber;
    }

    public String getSubsidiaryProductUniqNum() {
        return subsidiaryProductUniqNum;
    }

    public void setSubsidiaryProductUniqNum(String subsidiaryProductUniqNum) {
        this.subsidiaryProductUniqNum = subsidiaryProductUniqNum;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Integer getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Integer productTypeId) {
        this.productTypeId = productTypeId;
    }

    public Integer getProductRentalItemId() {
        return productRentalItemId;
    }

    public void setProductRentalItemId(Integer productRentalItemId) {
        this.productRentalItemId = productRentalItemId;
    }

    public Boolean getProductActive() {
        return productActive != null && productActive;
    }

    public void setProductActive(Boolean productActive) {
        this.productActive = productActive;
    }

    public Boolean getProductStorefrontEnable() {
        return productStorefrontEnable != null && productStorefrontEnable;
    }

    public void setProductStorefrontEnable(Boolean productStorefrontEnable) {
        this.productStorefrontEnable = productStorefrontEnable;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }

    public String getProductTypeIdName() {
        return productTypeIdName;
    }

    public void setProductTypeIdName(String productTypeIdName) {
        this.productTypeIdName = productTypeIdName;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Integer getCogsAccountId() {
        return cogsAccountId;
    }

    public void setCogsAccountId(Integer cogsAccountId) {
        this.cogsAccountId = cogsAccountId;
    }

    public String getCogsAccountName() {
        return cogsAccountName;
    }

    public void setCogsAccountName(String cogsAccountName) {
        this.cogsAccountName = cogsAccountName;
    }

    public Integer getAssetAccountId() {
        return assetAccountId;
    }

    public void setAssetAccountId(Integer assetAccountId) {
        this.assetAccountId = assetAccountId;
    }

    public String getAssetAccountName() {
        return assetAccountName;
    }

    public void setAssetAccountName(String assetAccountName) {
        this.assetAccountName = assetAccountName;
    }

    public String getAccountIdName() {
        return accountIdName;
    }

    public void setAccountIdName(String accountIdName) {
        this.accountIdName = accountIdName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTaxrateId() {
        return taxrateId;
    }

    public void setTaxrateId(Integer taxrateId) {
        this.taxrateId = taxrateId;
    }

    public String getTaxrate() {
        return taxrate;
    }

    public void setTaxrate(String taxrate) {
        this.taxrate = taxrate;
    }

    public Double getTaxEffectiveRate() {
        return taxEffectiveRate;
    }

    public void setTaxEffectiveRate(Double taxEffectiveRate) {
        this.taxEffectiveRate = taxEffectiveRate;
    }

    public String getVendor() {
        return vendor;
    }

    public void setVendor(String vendor) {
        this.vendor = vendor;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Double getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(Double unitprice) {
        this.unitprice = unitprice;
    }

    public Double getCostprice() {
        return costprice;
    }

    public void setCostprice(Double costprice) {
        this.costprice = costprice;
    }

    public String getAverageCost() {
        return averageCost;
    }

    public void setAverageCost(String averageCost) {
        this.averageCost = averageCost;
    }

    public Double getQuantityOnHand() {
        return quantityOnHand;
    }

    public void setQuantityOnHand(Double quantityOnHand) {
        this.quantityOnHand = quantityOnHand;
    }

    public List<Integer> getMultiSupplierId() {
        return multiSupplierId;
    }

    public void setMultiSupplierId(List<Integer> multiSupplierId) {
        this.multiSupplierId = multiSupplierId;
    }

    public List<String> getMultiSupplierName() {
        return multiSupplierName;
    }

    public void setMultiSupplierName(List<String> multiSupplierName) {
        this.multiSupplierName = multiSupplierName;
    }

    public List<String> getMultiSupplierIdName() {
        return multiSupplierIdName;
    }

    public void setMultiSupplierIdName(List<String> multiSupplierIdName) {
        this.multiSupplierIdName = multiSupplierIdName;
    }

    public Integer getUnitMeasureMentId() {
        return unitMeasureMentId;
    }

    public void setUnitMeasureMentId(Integer unitMeasureMentId) {
        this.unitMeasureMentId = unitMeasureMentId;
    }

    public String getUnitMeasureMentName() {
        return unitMeasureMentName;
    }

    public void setUnitMeasureMentName(String unitMeasureMentName) {
        this.unitMeasureMentName = unitMeasureMentName;
    }

    public String getUnitMeasurementIdName() {
        return unitMeasurementIdName;
    }

    public void setUnitMeasurementIdName(String unitMeasurementIdName) {
        this.unitMeasurementIdName = unitMeasurementIdName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getBrandIdName() {
        return brandIdName;
    }

    public void setBrandIdName(String brandIdName) {
        this.brandIdName = brandIdName;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    public Double getWarehouseStock() {
        return warehouseStock;
    }

    public void setWarehouseStock(Double warehouseStock) {
        this.warehouseStock = warehouseStock;
    }

    public String getCogsAccountIdName() {
        return cogsAccountIdName;
    }

    public void setCogsAccountIdName(String cogsAccountIdName) {
        this.cogsAccountIdName = cogsAccountIdName;
    }

    public String getAssetAccountIdName() {
        return assetAccountIdName;
    }

    public void setAssetAccountIdName(String assetAccountIdName) {
        this.assetAccountIdName = assetAccountIdName;
    }

    public List<String> getMultiSupplierNumber() {
        return multiSupplierNumber;
    }

    public void setMultiSupplierNumber(List<String> multiSupplierNumber) {
        this.multiSupplierNumber = multiSupplierNumber;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorIdName() {
        return creatorIdName;
    }

    public void setCreatorIdName(String creatorIdName) {
        this.creatorIdName = creatorIdName;
    }

    public Integer getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(Integer updaterId) {
        this.updaterId = updaterId;
    }

    public String getUpdaterName() {
        return updaterName;
    }

    public void setUpdaterName(String updaterName) {
        this.updaterName = updaterName;
    }

    public String getUpdaterIdName() {
        return updaterIdName;
    }

    public void setUpdaterIdName(String updaterIdName) {
        this.updaterIdName = updaterIdName;
    }

    public Integer getProductDiscountTypeId() {
        return productDiscountTypeId;
    }

    public void setProductDiscountTypeId(Integer productDiscountTypeId) {
        this.productDiscountTypeId = productDiscountTypeId;
    }

    public String getProductDiscountTypeName() {
        return productDiscountTypeName;
    }

    public void setProductDiscountTypeName(String productDiscountTypeIdName) {
        this.productDiscountTypeName = productDiscountTypeIdName;
    }

    public Double getProductDiscountAmount() {
        return productDiscountAmount;
    }

    public void setProductDiscountAmount(Double productDiscountAmount) {
        this.productDiscountAmount = productDiscountAmount;
    }

    public String getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(String parentCategory) {
        this.parentCategory = parentCategory;
    }

    public Integer getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(Integer parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public Boolean getInventoryTrackingEnabled() {
        return inventoryTrackingEnabled;
    }

    public void setInventoryTrackingEnabled(Boolean inventoryTrackingEnabled) {
        this.inventoryTrackingEnabled = inventoryTrackingEnabled;
    }

    public Boolean getTrackBatchesEnabled() {
        return trackBatchesEnabled;
    }

    public void setTrackBatchesEnabled(Boolean trackBatchesEnabled) {
        this.trackBatchesEnabled = trackBatchesEnabled;
    }

    public List<NewProductCustomDescription> getNewProductCustomDescriptions() {
        return newProductCustomDescriptions;
    }

    public void setNewProductCustomDescriptions(List<NewProductCustomDescription> newProductCustomDescriptions) {
        this.newProductCustomDescriptions = newProductCustomDescriptions;
    }

    public List<Integer> getMultiLocationId() {
        return multiLocationId;
    }

    public void setMultiLocationId(List<Integer> multiLocationId) {
        this.multiLocationId = multiLocationId;
    }

    public List<String> getMultiLocationName() {
        return multiLocationName;
    }

    public void setMultiLocationName(List<String> multiLocationName) {
        this.multiLocationName = multiLocationName;
    }

    public List<String> getMultiLocationIdName() {
        return multiLocationIdName;
    }

    public void setMultiLocationIdName(List<String> multiLocationIdName) {
        this.multiLocationIdName = multiLocationIdName;
    }

    public static class NewProductCustomDescription {
        @Id
        @Field("productId")
        @Indexed(name = "productId", type = "pint", required = true)
        private Integer id;
        @Field("productName")
        private String productName;
        @Field("quantity")
        @Indexed(name = "quantity", type = "pdouble", stored = false)
        private Double quantity;
        @Field("price")
        @Indexed(name = "price", type = "pdouble", stored = false)
        private Double price;


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public Double getQuantity() {
            return quantity;
        }

        public void setQuantity(Double quantity) {
            this.quantity = quantity;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }
    }

    public List<ProductsServicesSolrDoc> getWarehouses() {
        return warehouses;
    }

    public void setWarehouses(List<ProductsServicesSolrDoc> warehouses) {
        this.warehouses = warehouses;
    }

    public String getRentStatus() {
        return rentStatus;
    }

    public void setRentStatus(String rentStatus) {
        this.rentStatus = rentStatus;
    }

    public String getRentStatusIdName() {
        return rentStatusIdName;
    }

    public void setRentStatusIdName(String rentStatusIdName) {
        this.rentStatusIdName = rentStatusIdName;
    }

    public Integer getRentStatusId() {
        return rentStatusId;
    }

    public void setRentStatusId(Integer rentStatusId) {
        this.rentStatusId = rentStatusId;
    }

    public String getRentStatusCode() {
        return rentStatusCode;
    }

    public void setRentStatusCode(String rentStatusCode) {
        this.rentStatusCode = rentStatusCode;
    }
}
