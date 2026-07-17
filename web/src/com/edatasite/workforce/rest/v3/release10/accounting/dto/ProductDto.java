package com.edatasite.workforce.rest.v3.release10.accounting.dto;

import com.edatasite.workforce.gwt.accounting.client.ui.view.inventory.product.ProductPicture;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.rest.v3.release10.core.request.CustomFieldRequest;
import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ProductDto {

    //product
    private Integer id;
    private String objectKey;
    @NotNull(message = "name is required")
    @NotBlank(message = "name cannot be blank")
    private String name;
    private String number;
    @NotNull(message = "type is required")
    @Pattern(regexp = "INVENTORY_ITEM|NON_INVENTORY_ITEM|SERVICE|ASSEMBLY_ITEM|OTHER_CHARGE|PRODUCT_KIT",
            message = "type must be one of INVENTORY_ITEM/NON_INVENTORY_ITEM/SERVICE/ASSEMBLY_ITEM/OTHER_CHARGE/PRODUCT_KIT")
    private String type;
    private String description;
    private boolean active = true;

    //financial information
    @NotNull(message = "sellingPrice is required")
    private BigDecimal sellingPrice;
    private BigDecimal purchasePrice;
    @NotNull(message = "salesAccount is required")
    private IdCode salesAccount;
    private IdCode purchaseAccount;
    private boolean purchasedFromSupplier = false;

    //Inventory Stock Information
    @Valid
    private InventoryStockInformationDto inventoryStockInformation;
    private ItemDto defaultWarehouse;

    //Parameters
    private IdCode category;
    private IdCode brand;
    private List<IdCode> discounts;
    private Integer taxId;
    private IdCode taxData;
    private List<PriceLevelDto> priceLevel;
    private ProductPicture[] images;
    private FileItem[] attachments;

    //More Options
    private String skuNumber;
    private IdCode unitMeasurement;
    private String partNumber;
    private String upcNumber;
    private String manufacturer;
    private List<IdCode> suppliers;
    private Long barcode;
    @Valid
    private List<? extends CustomFieldRequest> customFields;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonDeserialize(using = DateDeserializer.class)
    private Date createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonDeserialize(using = DateDeserializer.class)
    private Date updatedAt;
    private Boolean soldToCustomer;
    private ItemDto assetAccount;

    public ProductDto() {
    }

    public ProductDto(Integer id, String objectKey, String name, String number, String type, String description, boolean active, BigDecimal sellingPrice, BigDecimal purchasePrice, IdCode salesAccount, IdCode purchaseAccount, boolean purchasedFromSupplier, InventoryStockInformationDto inventoryStockInformation, IdCode category, IdCode brand, List<IdCode> discounts, Integer taxId, List<PriceLevelDto> priceLevel, ProductPicture[] images, FileItem[] attachments, String skuNumber, IdCode unitMeasurement, String partNumber, String upcNumber, String manufacturer, List<IdCode> suppliers, Long barcode, List<? extends CustomFieldRequest> customFields, Date createdAt, Date updatedAt) {
        this.id = id;
        this.objectKey = objectKey;
        this.name = name;
        this.number = number;
        this.type = type;
        this.description = description;
        this.active = active;
        this.sellingPrice = sellingPrice;
        this.purchasePrice = purchasePrice;
        this.salesAccount = salesAccount;
        this.purchaseAccount = purchaseAccount;
        this.purchasedFromSupplier = purchasedFromSupplier;
        this.inventoryStockInformation = inventoryStockInformation;
        this.category = category;
        this.brand = brand;
        this.discounts = discounts;
        this.taxId = taxId;
        this.priceLevel = priceLevel;
        this.images = images;
        this.attachments = attachments;
        this.skuNumber = skuNumber;
        this.unitMeasurement = unitMeasurement;
        this.partNumber = partNumber;
        this.upcNumber = upcNumber;
        this.manufacturer = manufacturer;
        this.suppliers = suppliers;
        this.barcode = barcode;
        this.customFields = customFields;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @JsonIgnore
    public boolean isExistingObject() {
        return StringUtils.isNotBlank(getObjectKey()) || getId() != null || StringUtils.isNotBlank(getNumber());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public BigDecimal getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public IdCode getSalesAccount() {
        return salesAccount;
    }

    public void setSalesAccount(IdCode salesAccount) {
        this.salesAccount = salesAccount;
    }

    public IdCode getPurchaseAccount() {
        return purchaseAccount;
    }

    public void setPurchaseAccount(IdCode purchaseAccount) {
        this.purchaseAccount = purchaseAccount;
    }

    public boolean isPurchasedFromSupplier() {
        return purchasedFromSupplier;
    }

    public void setPurchasedFromSupplier(boolean purchasedFromSupplier) {
        this.purchasedFromSupplier = purchasedFromSupplier;
    }

    public InventoryStockInformationDto getInventoryStockInformation() {
        return inventoryStockInformation;
    }

    public void setInventoryStockInformation(InventoryStockInformationDto inventoryStockInformation) {
        this.inventoryStockInformation = inventoryStockInformation;
    }

    public IdCode getCategory() {
        return category;
    }

    public void setCategory(IdCode category) {
        this.category = category;
    }

    public IdCode getBrand() {
        return brand;
    }

    public void setBrand(IdCode brand) {
        this.brand = brand;
    }

    public List<IdCode> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(List<IdCode> discounts) {
        this.discounts = discounts;
    }

    public Integer getTaxId() {
        return taxId;
    }

    public void setTaxId(Integer taxId) {
        this.taxId = taxId;
    }

    public void setTaxData(IdCode taxData) {
        this.taxData = taxData;
    }

    public IdCode getTaxData() {
        return taxData;
    }

    public List<PriceLevelDto> getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(List<PriceLevelDto> priceLevel) {
        this.priceLevel = priceLevel;
    }

    public ProductPicture[] getImages() {
        return images;
    }

    public void setImages(ProductPicture[] images) {
        this.images = images;
    }

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public String getSkuNumber() {
        return skuNumber;
    }

    public void setSkuNumber(String skuNumber) {
        this.skuNumber = skuNumber;
    }

    public IdCode getUnitMeasurement() {
        return unitMeasurement;
    }

    public void setUnitMeasurement(IdCode unitMeasurement) {
        this.unitMeasurement = unitMeasurement;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getUpcNumber() {
        return upcNumber;
    }

    public void setUpcNumber(String upcNumber) {
        this.upcNumber = upcNumber;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public List<IdCode> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<IdCode> suppliers) {
        this.suppliers = suppliers;
    }

    public Long getBarcode() {
        return barcode;
    }

    public void setBarcode(Long barcode) {
        this.barcode = barcode;
    }

    public List<? extends CustomFieldRequest> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<? extends CustomFieldRequest> customFields) {
        this.customFields = customFields;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductDto)) return false;

        ProductDto that = (ProductDto) o;

        if (isActive() != that.isActive()) return false;
        if (isPurchasedFromSupplier() != that.isPurchasedFromSupplier()) return false;
        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (getObjectKey() != null ? !getObjectKey().equals(that.getObjectKey()) : that.getObjectKey() != null)
            return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        if (getNumber() != null ? !getNumber().equals(that.getNumber()) : that.getNumber() != null) return false;
        if (getType() != null ? !getType().equals(that.getType()) : that.getType() != null) return false;
        if (getDescription() != null ? !getDescription().equals(that.getDescription()) : that.getDescription() != null)
            return false;
        if (getSellingPrice() != null ? !getSellingPrice().equals(that.getSellingPrice()) : that.getSellingPrice() != null)
            return false;
        if (getPurchasePrice() != null ? !getPurchasePrice().equals(that.getPurchasePrice()) : that.getPurchasePrice() != null)
            return false;
        if (getSalesAccount() != null ? !getSalesAccount().equals(that.getSalesAccount()) : that.getSalesAccount() != null)
            return false;
        if (getPurchaseAccount() != null ? !getPurchaseAccount().equals(that.getPurchaseAccount()) : that.getPurchaseAccount() != null)
            return false;
        if (getInventoryStockInformation() != null ? !getInventoryStockInformation().equals(that.getInventoryStockInformation()) : that.getInventoryStockInformation() != null)
            return false;
        if (getCategory() != null ? !getCategory().equals(that.getCategory()) : that.getCategory() != null)
            return false;
        if (getBrand() != null ? !getBrand().equals(that.getBrand()) : that.getBrand() != null) return false;
        if (getDiscounts() != null ? !getDiscounts().equals(that.getDiscounts()) : that.getDiscounts() != null)
            return false;
        if (getTaxId() != null ? !getTaxId().equals(that.getTaxId()) : that.getTaxId() != null) return false;

        if (getTaxData() != null ? !getTaxData().equals(that.getTaxData()) : that.getTaxData() != null)

            if (getPriceLevel() != null ? !getPriceLevel().equals(that.getPriceLevel()) : that.getPriceLevel() != null)
            return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(getImages(), that.getImages())) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(getAttachments(), that.getAttachments())) return false;
        if (getSkuNumber() != null ? !getSkuNumber().equals(that.getSkuNumber()) : that.getSkuNumber() != null)
            return false;
        if (getUnitMeasurement() != null ? !getUnitMeasurement().equals(that.getUnitMeasurement()) : that.getUnitMeasurement() != null)
            return false;
        if (getPartNumber() != null ? !getPartNumber().equals(that.getPartNumber()) : that.getPartNumber() != null)
            return false;
        if (getUpcNumber() != null ? !getUpcNumber().equals(that.getUpcNumber()) : that.getUpcNumber() != null)
            return false;
        if (getManufacturer() != null ? !getManufacturer().equals(that.getManufacturer()) : that.getManufacturer() != null)
            return false;
        if (getSuppliers() != null ? !getSuppliers().equals(that.getSuppliers()) : that.getSuppliers() != null)
            return false;
        if (getBarcode() != null ? !getBarcode().equals(that.getBarcode()) : that.getBarcode() != null) return false;
        if (getCustomFields() != null ? !getCustomFields().equals(that.getCustomFields()) : that.getCustomFields() != null)
            return false;
        if (getCreatedAt() != null ? !getCreatedAt().equals(that.getCreatedAt()) : that.getCreatedAt() != null)
            return false;
        if (getUpdatedAt() != null ? !getUpdatedAt().equals(that.getUpdatedAt()) : that.getUpdatedAt() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getObjectKey() != null ? getObjectKey().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getNumber() != null ? getNumber().hashCode() : 0);
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (isActive() ? 1 : 0);
        result = 31 * result + (getSellingPrice() != null ? getSellingPrice().hashCode() : 0);
        result = 31 * result + (getPurchasePrice() != null ? getPurchasePrice().hashCode() : 0);
        result = 31 * result + (getSalesAccount() != null ? getSalesAccount().hashCode() : 0);
        result = 31 * result + (getPurchaseAccount() != null ? getPurchaseAccount().hashCode() : 0);
        result = 31 * result + (isPurchasedFromSupplier() ? 1 : 0);
        result = 31 * result + (getInventoryStockInformation() != null ? getInventoryStockInformation().hashCode() : 0);
        result = 31 * result + (getCategory() != null ? getCategory().hashCode() : 0);
        result = 31 * result + (getBrand() != null ? getBrand().hashCode() : 0);
        result = 31 * result + (getDiscounts() != null ? getDiscounts().hashCode() : 0);
        result = 31 * result + (getTaxId() != null ? getTaxId().hashCode() : 0);
        result = 31 * result + (getTaxData() != null ? getTaxData().hashCode() : 0);
        result = 31 * result + (getPriceLevel() != null ? getPriceLevel().hashCode() : 0);
        result = 31 * result + Arrays.hashCode(getImages());
        result = 31 * result + Arrays.hashCode(getAttachments());
        result = 31 * result + (getSkuNumber() != null ? getSkuNumber().hashCode() : 0);
        result = 31 * result + (getUnitMeasurement() != null ? getUnitMeasurement().hashCode() : 0);
        result = 31 * result + (getPartNumber() != null ? getPartNumber().hashCode() : 0);
        result = 31 * result + (getUpcNumber() != null ? getUpcNumber().hashCode() : 0);
        result = 31 * result + (getManufacturer() != null ? getManufacturer().hashCode() : 0);
        result = 31 * result + (getSuppliers() != null ? getSuppliers().hashCode() : 0);
        result = 31 * result + (getBarcode() != null ? getBarcode().hashCode() : 0);
        result = 31 * result + (getCustomFields() != null ? getCustomFields().hashCode() : 0);
        result = 31 * result + (getCreatedAt() != null ? getCreatedAt().hashCode() : 0);
        result = 31 * result + (getUpdatedAt() != null ? getUpdatedAt().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProductDto{" +
                "id=" + id +
                ", objectKey='" + objectKey + '\'' +
                ", name='" + name + '\'' +
                ", number='" + number + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", active=" + active +
                ", sellingPrice=" + sellingPrice +
                ", purchasePrice=" + purchasePrice +
                ", salesAccount=" + salesAccount +
                ", purchaseAccount=" + purchaseAccount +
                ", purchasedFromSupplier=" + purchasedFromSupplier +
                ", inventoryStockInformation=" + inventoryStockInformation +
                ", category=" + category +
                ", brand=" + brand +
                ", discounts=" + discounts +
                ", taxId=" + taxId +
                ", priceLevel=" + priceLevel +
                ", images=" + Arrays.toString(images) +
                ", attachments=" + Arrays.toString(attachments) +
                ", skuNumber='" + skuNumber + '\'' +
                ", unitMeasurement=" + unitMeasurement +
                ", partNumber='" + partNumber + '\'' +
                ", upcNumber='" + upcNumber + '\'' +
                ", manufacturer='" + manufacturer + '\'' +
                ", suppliers=" + suppliers +
                ", barcode=" + barcode +
                ", customFields=" + customFields +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    public ItemDto getDefaultWarehouse() {
        return defaultWarehouse;
    }

    public void setDefaultWarehouse(ItemDto defaultWarehouse) {
        this.defaultWarehouse = defaultWarehouse;
    }

    public Boolean isSoldToCustomer() {
        return soldToCustomer;
    }

    public void setSoldToCustomer(Boolean soldToCustomer) {
        this.soldToCustomer = soldToCustomer != null && soldToCustomer;
    }

    public ItemDto getAssetAccount() {
        return assetAccount;
    }

    public void setAssetAccount(ItemDto assetAccount) {
        this.assetAccount = assetAccount;
    }
}
