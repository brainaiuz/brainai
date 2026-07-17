package com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset;

import com.edatasite.workforce.gwt.accounting.client.rpc.DailyDepreciationRateItem;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/5/11
 * Time: 12:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class FixedAssetItem implements ListingCustomFields, IsSerializable {
    public static final String ACTION = "action";
    public static final String CATEGORY = "category";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String DATE = "date";
    public static final String COST = "cost";
    public static final String RESIDUALVALUE = "residualValue";
    public static final String ASSETLIFE = "assetLife";
    public static final String LOCATION = "location";
    public static final String ACCOUNT = "account";
    public static final String OWNER = "owner";
    public static final String DEPARTMENT = "department";
    public static final String NUMBER = "number";
    public static final String CALCULATE_DEPRECIATION = "calculateDepreciation";
    public static final String STATUS = "status";

    private Integer objectID;
    private SelectItem owner;
    private AccountItem account;
    private String code;
    private String name;
    private String description;
    private BigDecimal cost;
    private BigDecimal quantity;
    private DateNonConvertable currentDate;
    private DateNonConvertable creationDate;
    private BigDecimal usefulLife;
    private BigDecimal residualValue;
    private boolean isEditable;
    private AccountItem financedByAccount;
    private NumberData numberData;
    private Boolean calculateDepreciation;
    private Integer purchaseInvoiceID;
    private Integer purchaseOrderID;
    private String convertedItemNumber;
    private String imageLink;
    private Integer imageID;
    private String existingCodeStatus;//YES, NO
    private String status;
    private Integer journalId;
    private Integer pdfTemplateId;
    private SelectItem[] templates;


    private Integer locationID;
    private String locationName;
    private SelectItem[] locations;

    private SelectItem department;

    private Integer disposeType;
    private Integer disposeAccountID;
    private BigDecimal disposeTaxAmount;
    private BigDecimal disposeAmount;
    private TaxItem taxItem;
    private BigDecimal taxAmount;
    private Integer taxCalculationType;
    private Boolean disposed;

    private DateNonConvertable disposedDate;

    private Boolean showDescInBarcode;
    private ArrayList<CompanyCustomFieldItem> customFields;
    private HashMap<String, Object> customFieldsMap;

    private SelectItem fixedAssetAccount;
    private SelectItem expenseAccount;
    private DailyDepreciationRateItem[] dailyDepreciationRateItems;

    public FixedAssetItem() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Boolean getDisposed() {
        return disposed;
    }

    public void setDisposed(Boolean disposed) {
        this.disposed = disposed;
    }

    public AccountItem getAccount() {
        return account;
    }

    public void setAccount(AccountItem account) {
        this.account = account;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public DateNonConvertable getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(DateNonConvertable currentDate) {
        this.currentDate = currentDate;
    }

    public DateNonConvertable getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(DateNonConvertable creationDate) {
        this.creationDate = creationDate;
    }

    public BigDecimal getUsefulLife() {
        return usefulLife;
    }

    public void setUsefulLife(BigDecimal usefulLife) {
        this.usefulLife = usefulLife;
    }

    public BigDecimal getResidualValue() {
        return residualValue;
    }

    public void setResidualValue(BigDecimal residualValue) {
        this.residualValue = residualValue;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    public AccountItem getFinancedByAccount() {
        return financedByAccount;
    }

    public void setFinancedByAccount(AccountItem financedByAccount) {
        this.financedByAccount = financedByAccount;
    }

    public NumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
    }

    public Boolean isCalculateDepreciation() {
        return calculateDepreciation != null ? calculateDepreciation : false;
    }

    public void setCalculateDepreciation(Boolean calculateDepreciation) {
        this.calculateDepreciation = calculateDepreciation;
    }

    public Integer getPurchaseInvoiceID() {
        return purchaseInvoiceID;
    }

    public void setPurchaseInvoiceID(Integer purchaseInvoiceID) {
        this.purchaseInvoiceID = purchaseInvoiceID;
    }

    public Integer getPurchaseOrderID() {
        return purchaseOrderID;
    }

    public void setPurchaseOrderID(Integer purchaseOrderID) {
        this.purchaseOrderID = purchaseOrderID;
    }

    public String getConvertedItemNumber() {
        return convertedItemNumber;
    }

    public void setConvertedItemNumber(String convertedItemNumber) {
        this.convertedItemNumber = convertedItemNumber;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public Integer getImageID() {
        return imageID;
    }

    public void setImageID(Integer imageID) {
        this.imageID = imageID;
    }

    public String getExistingCodeStatus() {
        return existingCodeStatus;
    }

    public void setExistingCodeStatus(String status) {
        this.existingCodeStatus = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getLocationID() {
        return locationID;
    }

    public void setLocationID(Integer locationID) {
        this.locationID = locationID;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public SelectItem[] getLocations() {
        return locations;
    }

    public void setLocations(SelectItem[] locations) {
        this.locations = locations;
    }

    public SelectItem getDepartment() {
        return department;
    }

    public void setDepartment(SelectItem department) {
        this.department = department;
    }

    public Integer getDisposeType() {
        return disposeType;
    }

    public void setDisposeType(Integer disposeType) {
        this.disposeType = disposeType;
    }

    public Integer getDisposeAccountID() {
        return disposeAccountID;
    }

    public void setDisposeAccountID(Integer disposeAccountID) {
        this.disposeAccountID = disposeAccountID;
    }

    public BigDecimal getDisposeTaxAmount() {
        return disposeTaxAmount;
    }

    public void setDisposeTaxAmount(BigDecimal disposeTaxAmount) {
        this.disposeTaxAmount = disposeTaxAmount;
    }

    public BigDecimal getDisposeAmount() {
        return disposeAmount;
    }

    public void setDisposeAmount(BigDecimal disposeAmount) {
        this.disposeAmount = disposeAmount;
    }

    public Boolean getShowDescInBarcode() {
        return showDescInBarcode;
    }

    public void setShowDescInBarcode(Boolean showDescInBarcode) {
        this.showDescInBarcode = showDescInBarcode;
    }

    public void setCustomFields(ArrayList<CompanyCustomFieldItem> customFields) {
        this.customFields = customFields;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFields() {
        return customFields;
    }

    public SelectItem getOwner() {
        return owner;
    }

    public void setOwner(SelectItem owner) {
        this.owner = owner;
    }

    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return getCustomFieldsMap() != null ? getCustomFieldsMap().get(columnCodeKey) : null;
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {
        getCustomFieldsMap().put(columnCodeKey, cellValue);
    }

    public HashMap<String, Object> getCustomFieldsMap() {
        if (customFieldsMap == null) {
            customFieldsMap = new HashMap<>();
        }
        return customFieldsMap;
    }

    public Integer getJournalId() {
        return this.journalId;
    }

    public void setJournalId(final Integer journalId) {
        this.journalId = journalId;
    }

    public Integer getPdfTemplateId() {
        return pdfTemplateId;
    }

    public void setPdfTemplateId(Integer pdfTemplateId) {
        this.pdfTemplateId = pdfTemplateId;
    }

    public SelectItem[] getTemplates() {
        return templates;
    }

    public void setTemplates(SelectItem[] templates) {
        this.templates = templates;
    }

    public void setCustomFieldsMap(HashMap<String, Object> customFieldsMap) {
        this.customFieldsMap = customFieldsMap;
    }

    public DateNonConvertable getDisposedDate() {
        return disposedDate;
    }

    public void setDisposedDate(DateNonConvertable disposedDate) {
        this.disposedDate = disposedDate;
    }

    public SelectItem getFixedAssetAccount() {
        return fixedAssetAccount;
    }

    public void setFixedAssetAccount(SelectItem fixedAssetAccount) {
        this.fixedAssetAccount = fixedAssetAccount;
    }

    public SelectItem getExpenseAccount() {
        return expenseAccount;
    }

    public void setExpenseAccount(SelectItem expenseAccount) {
        this.expenseAccount = expenseAccount;
    }

    public DailyDepreciationRateItem[] getDailyDepreciationRateItems() {
        return dailyDepreciationRateItems;
    }

    public void setDailyDepreciationRateItems(DailyDepreciationRateItem[] dailyDepreciationRateItems) {
        this.dailyDepreciationRateItems = dailyDepreciationRateItems;
    }

    public void setTaxItem(TaxItem taxItem) {
        this.taxItem = taxItem;
    }

    public TaxItem getTaxItem() {
        return taxItem;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Integer getTaxCalculationType() {
        return taxCalculationType;
    }

    public void setTaxCalculationType(Integer taxCalculationType) {
        this.taxCalculationType = taxCalculationType;
    }

    public String getBarcodeGenerateText(String companyID, String dateAsString) {
        return account.getName() + " | " +
                code + " | " +
                name + " | " +
                cost.setScale(2, BigDecimal.ROUND_HALF_UP).toString() + " | " +
                dateAsString + " | " +
                usefulLife + "year(s) | " +
                residualValue.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    public String getBarcodeGenerateText(boolean withDescription, String dateAsString, boolean hasFullBarcodeData) {
        if (withDescription) {
            if (hasFullBarcodeData) {
                return account.getName() + " | " +
                        code + " | " +
                        name + " | " +
                        description + " | " +
                        cost.setScale(2, BigDecimal.ROUND_HALF_UP).toString() + " | " +
                        dateAsString + " | " +
                        usefulLife + "year(s) | " +
                        residualValue.setScale(2, BigDecimal.ROUND_HALF_UP).toString() +
                        (locationName != null ? " | " + locationName : "");
            } else {
                return name;
            }
        } else {
            if (hasFullBarcodeData) {
                return account.getName() + " | " +
                        code + " | " +
                        name + " | " +
                        cost.setScale(2, BigDecimal.ROUND_HALF_UP).toString() + " | " +
                        dateAsString + " | " +
                        usefulLife + "year(s) | " +
                        residualValue.setScale(2, BigDecimal.ROUND_HALF_UP).toString() +
                        (locationName != null ? " | " + locationName : "");
            } else {
                return name;
            }
        }
    }
}
