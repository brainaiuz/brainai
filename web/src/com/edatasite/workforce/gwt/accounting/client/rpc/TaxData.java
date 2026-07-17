package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.enums.TaxKeyEnum;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TaxData implements Serializable {

    private Integer objectId;
    private String taxName;
    private BigDecimal taxRate;
    private Integer taxTypeId;
    private Integer countryId;
    private boolean isVatReturnEnabled;
    private boolean groupTax;
    private TaxComponentData[] components;
    private LinkedList<TaxItem> groupItems;
    private Integer permissionType;

    private TaxKeyEnum key;

    private boolean selectedByDefault;
    private Integer faiId;
    private ReferenceItem[] faiVats;
    private Boolean status;
    private List<Integer> faiCategoryIds;
    private SelectItem[] faiCategoryOptions;
    private Integer faiPurchaseId;
    private ReferenceItem[] faiPurchaseVats;
    private List<Integer> faiPurchaseCategoryIds;
    private SelectItem[] faiPurchaseCategoryOptions;

    public boolean isSelectedByDefault() {
        return selectedByDefault;
    }

    public void setSelectedByDefault(boolean selectedByDefault) {
        this.selectedByDefault = selectedByDefault;
    }

    public TaxData() {
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public Integer getTaxTypeId() {
        return taxTypeId;
    }

    public void setTaxTypeId(Integer taxTypeId) {
        this.taxTypeId = taxTypeId;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public TaxComponentData[] getComponents() {
        return components;
    }

    public void setComponents(TaxComponentData[] components) {
        this.components = components;
    }

    public boolean isVatReturnEnabled() {
        return isVatReturnEnabled;
    }

    public void setVatReturnEnabled(boolean vatReturnEnabled) {
        isVatReturnEnabled = vatReturnEnabled;
    }

    public boolean isGroupTax() {
        return groupTax;
    }

    public void setGroupTax(boolean groupTax) {
        this.groupTax = groupTax;
    }

    public LinkedList<TaxItem> getGroupItems() {
        return groupItems;
    }

    public void setGroupItems(LinkedList<TaxItem> groupItems) {
        this.groupItems = groupItems;
    }

    public Integer getPermissionType() {
        return permissionType;
    }

    public void setPermissionType(Integer permissionType) {
        this.permissionType = permissionType;
    }

    public TaxKeyEnum getKey() {
        return key;
    }

    public void setKey(TaxKeyEnum key) {
        this.key = key;
    }

    public Integer getFaiId() {
        return faiId;
    }

    public void setFaiId(Integer faiId) {
        this.faiId = faiId;
    }

    public ReferenceItem[] getFaiVats() {
        return faiVats;
    }

    public void setFaiVats(ReferenceItem[] faiVats) {
        this.faiVats = faiVats;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Boolean getStatus() {
        return status;
    }

    public List<Integer> getFaiCategoryIds() {
        return faiCategoryIds != null ? faiCategoryIds : new ArrayList<>();
    }

    public void setFaiCategoryIds(List<Integer> faiCategoryIds) {
        this.faiCategoryIds = faiCategoryIds;
    }

    public void setFaiCategoryOptions(SelectItem[] faiCategoryOptions) {
        this.faiCategoryOptions = faiCategoryOptions;
    }

    public SelectItem[] getFaiCategoryOptions() {
        return faiCategoryOptions;
    }

    public Integer getFaiPurchaseId() {
        return faiPurchaseId;
    }

    public void setFaiPurchaseId(Integer faiPurchaseId) {
        this.faiPurchaseId = faiPurchaseId;
    }

    public ReferenceItem[] getFaiPurchaseVats() {
        return faiPurchaseVats;
    }

    public void setFaiPurchaseVats(ReferenceItem[] faiPurchaseVats) {
        this.faiPurchaseVats = faiPurchaseVats;
    }

    public List<Integer> getFaiPurchaseCategoryIds() {
        return faiPurchaseCategoryIds != null ?  faiPurchaseCategoryIds : new ArrayList<>();
    }

    public void setFaiPurchaseCategoryIds(List<Integer> faiPurchaseCategoryIds) {
        this.faiPurchaseCategoryIds = faiPurchaseCategoryIds;
    }

    public SelectItem[] getFaiPurchaseCategoryOptions() {
        return faiPurchaseCategoryOptions;
    }

    public void setFaiPurchaseCategoryOptions(SelectItem[] faiPurchaseCategoryOptions) {
        this.faiPurchaseCategoryOptions = faiPurchaseCategoryOptions;
    }
}
