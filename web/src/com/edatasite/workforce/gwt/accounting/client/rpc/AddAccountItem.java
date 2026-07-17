package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: administrator
 * Date: 24.02.2009
 * Time: 12:51:33
 * To change this template use File | Settings | File Templates.
 */
public class AddAccountItem implements Serializable, ListingCustomFields {
    private Integer objectId;
    private Integer countryId;
    private Integer accountTypeId;
    private Integer accountKey;
    private String accountTypeCode;
    private TaxItem taxItem;
    private String name;
    private String description;
    private String code;
    private boolean showInExpense;
    private boolean active;
    private boolean enablePayments;
    private boolean showInLookUp;
    private String qbAccountID; //quickbook account list id
    private String saasuAccountID; // account from Saasu
    private Date sasuuLastUpdatedDate;
    private boolean fromSaasu;
    private AccountItem parentAccount;
    private AccountItem[] parentAccountList;
    private Integer currencyID;
    private boolean hasChilds;
    private boolean usedInSystem;
    private boolean fromQuickbooks;
	private Boolean isEditable;
    private String qbEditSequence;
    private Boolean isDefaultAccount;
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private HashMap<String, Object> customFieldValues;

    private BigDecimal openingAmount;
    private Date openingDate;

    public BigDecimal getOpeningAmount() {
        return openingAmount;
    }

    public void setOpeningAmount(BigDecimal openingAmount) {
        this.openingAmount = openingAmount;
    }

    public Date getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(Date openingDate) {
        this.openingDate = openingDate;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public Integer getAccountTypeId() {
        return accountTypeId;
    }

    public void setAccountTypeId(Integer accountTypeId) {
        this.accountTypeId = accountTypeId;
    }

    public Integer getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(Integer accountKey) {
        this.accountKey = accountKey;
    }

    public TaxItem getTaxItem() {
        return taxItem;
    }

    public void setTaxItem(TaxItem taxItem) {
        this.taxItem = taxItem;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isShowInExpense() {
        return showInExpense;
    }

    public void setShowInExpense(boolean showInExpense) {
        this.showInExpense = showInExpense;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isEnablePayments() {
        return enablePayments;
    }

    public void setEnablePayments(boolean enablePayments) {
        this.enablePayments = enablePayments;
    }

    public String getQBAccountID() {
        return qbAccountID;
    }

    public void setQBAccountID(String qbAccountID) {
        this.qbAccountID = qbAccountID;
    }

    public String getAccountTypeCode() {
        return accountTypeCode;
    }

    public void setAccountTypeCode(String accountTypeCode) {
        this.accountTypeCode = accountTypeCode;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getSaasuAccountID() {
        return saasuAccountID;
    }

    public void setSaasuAccountID(String saasuAccountID) {
        this.saasuAccountID = saasuAccountID;
    }

    public Date getSasuuLastUpdatedDate() {
        return sasuuLastUpdatedDate;
    }

    public void setSasuuLastUpdatedDate(Date sasuuLastUpdatedDate) {
        this.sasuuLastUpdatedDate = sasuuLastUpdatedDate;
    }

    public boolean isFromSaasu() {
        return fromSaasu;
    }

    public void setFromSaasu(boolean fromSaasu) {
        this.fromSaasu = fromSaasu;
    }

    public AccountItem getParentAccount() {
        return parentAccount;
    }

    public void setParentAccount(AccountItem parentAccount) {
        this.parentAccount = parentAccount;
    }

    public Integer getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(Integer currencyID) {
        this.currencyID = currencyID;
    }

    public boolean isHasChilds() {
        return hasChilds;
    }

    public void setHasChilds(boolean hasChilds) {
        this.hasChilds = hasChilds;
    }

    public boolean isUsedInSystem() {
        return usedInSystem;
    }

    public void setUsedInSystem(boolean usedInSystem) {
        this.usedInSystem = usedInSystem;
    }

    public boolean isFromQuickbooks() {
        return fromQuickbooks;
    }

    public void setFromQuickbooks(boolean fromQuickbooks) {
        this.fromQuickbooks = fromQuickbooks;
    }

	public Boolean getIsEditable() {
		return isEditable != null ? isEditable : false;
	}

	public void setIsEditable(Boolean isEditable) {
		this.isEditable = isEditable;
	}

	public String getQbEditSequence() {
        return qbEditSequence;
    }

    public void setQbEditSequence(String qbEditSequence) {
        this.qbEditSequence = qbEditSequence;
    }

    public boolean isShowInLookUp() {
        return showInLookUp;
    }

    public void setShowInLookUp(boolean showInLookUp) {
        this.showInLookUp = showInLookUp;
    }

    public Boolean getIsDefaultAccount() {
        return isDefaultAccount != null ? isDefaultAccount : false;
    }

    public void setIsDefaultAccount(Boolean isDefaultAccount) {
        this.isDefaultAccount = isDefaultAccount;
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

    public AccountItem[] getParentAccountList() {
        return parentAccountList;
    }

    public void setParentAccountList(AccountItem[] parentAccountList) {
        this.parentAccountList = parentAccountList;
    }

    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return customFieldValues.get(columnCodeKey);
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {
        customFieldValues.put(columnCodeKey, cellValue);
    }
}
