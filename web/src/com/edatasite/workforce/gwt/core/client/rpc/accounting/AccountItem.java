package com.edatasite.workforce.gwt.core.client.rpc.accounting;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 23.02.2009
 * Time: 12:19:17
 * To change this template use File | Settings | File Templates.
 */
public class AccountItem extends SelectItem implements ListingCustomFields {

    private String code;
    private Integer accountKey;
    private Integer currencyID;
    private String currencyCode;
    private Integer accountTypeID;
    private String accountTypeCode;
    private String accountTypeCategory;
    private boolean isCheckedForExpense;
    private String parentCode;
    private Integer parentId;
    private String parentName;
    private SelectItem parent;
    private SelectItem currency;
    private SelectItem accountType;
    private Integer level;
    private boolean isDefault;
    private boolean active;
    private boolean bankAccountActive;
    private Date lastUpdatedDate;
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private HashMap<String, Object> customFieldValues;

    public AccountItem() {

    }

    public AccountItem(Integer id, String name) {
        super(id, name);
    }

    public AccountItem(Integer id, String code, String name) {
        super(id, name);
        this.code = code;
    }

    public AccountItem(Integer id, String code, String name, Boolean isCheckedForExpense) {
        super(id, name);
        this.isCheckedForExpense = isCheckedForExpense;
        this.code = code;
    }

    public AccountItem(Integer id, String code, String name, Integer accountTypeID, String accountTypeCode, String accountTypeCategory, Integer key, Integer currencyID, String currencyCode) {
        super(id, name);
        this.code = code;
        this.accountTypeID = accountTypeID;
        this.accountTypeCode = accountTypeCode;
        this.accountTypeCategory = accountTypeCategory;
        this.accountKey = key;
        this.currencyID = currencyID;
        this.currencyCode = currencyCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getAccountKey() {
        return accountKey;
    }

    public void setAccountKey(Integer accountKey) {
        this.accountKey = accountKey;
    }

    public Integer getAccountTypeID() {
        return accountTypeID;
    }

    public void setAccountTypeID(Integer accountTypeID) {
        this.accountTypeID = accountTypeID;
    }

    public String getAccountTypeCode() {
        return accountTypeCode;
    }

    public void setAccountTypeCode(String accountTypeCode) {
        this.accountTypeCode = accountTypeCode;
    }

    public String getAccountTypeCategory() {
        return accountTypeCategory;
    }

    public void setAccountTypeCategory(String accountTypeCategory) {
        this.accountTypeCategory = accountTypeCategory;
    }

    public Integer getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(Integer currencyID) {
        this.currencyID = currencyID;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public boolean isCheckedForExpense() {
        return isCheckedForExpense;
    }

    public void setCheckedForExpense(boolean checkedForExpense) {
        isCheckedForExpense = checkedForExpense;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public boolean isDefault() {
        return this.isDefault;
    }

    public void setDefault(final boolean aDefault) {
        this.isDefault = aDefault;
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


    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return customFieldValues.get(columnCodeKey);
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {
        customFieldValues.put(columnCodeKey, cellValue);
    }

    public SelectItem getParent() {
        return parent;
    }

    public void setParent(SelectItem parent) {
        this.parent = parent;
    }

    public SelectItem getCurrency() {
        return currency;
    }

    public void setCurrency(SelectItem currency) {
        this.currency = currency;
    }

    public SelectItem getAccountType() {
        return accountType;
    }

    public void setAccountType(SelectItem accountType) {
        this.accountType = accountType;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public boolean isBankAccountActive() {
        return bankAccountActive;
    }

    public void setBankAccountActive(boolean bankAccountActive) {
        this.bankAccountActive = bankAccountActive;
    }
}
