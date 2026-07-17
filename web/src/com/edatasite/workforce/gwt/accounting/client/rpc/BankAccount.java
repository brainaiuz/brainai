package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 26.02.2009
 * Time: 18:32:13
 * To change this template use File | Settings | File Templates.
 */
public class BankAccount implements IsSerializable, ListingCustomFields {

    public static String ACTION = "action";
    public static String CODE_COLUMN = "code_column";
    public static String NUMBER_COLUMN = "number_column";
    public static String CURRENCY_COLUMN = "currency_column";
    public static String NAME_COLUMN = "name_column";
    public static String AMOUNT_COLUMN = "amount_column";
    private Integer objectId;
    private String code;
    private String name;
    private String accountNumber;
    private Boolean active;
    private String accauntName;
    private String bankBranch;
    private String bankAddress;
    private String swiftCode;
    private String ibanCode;
    private String sortCode;
    private String abaCode;
    private String agentID;
    private String big;

    private String streetAddress;
    private String city;
    private Integer countryId;
    private String countryName;
    private Integer stateId;
    private String stateName;
    private String postCode;
    private String phoneNumber;

    private BigDecimal balance;
    private Integer accountId;

    private CurrencyItem currency;
    private BigDecimal exchangeRate;

    private BigDecimal openingAmount;
    private DateNonConvertable openingDate;

    private boolean usedInSystem;

    private boolean isReconcilationReportEnabled;

    private Map<String, SelectItem[]> countryAndRegionItems;
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private HashMap<String, Object> customFieldsMap;
    private FileItem[] attachments;

    private ArrayList<SelectItem> selectedOwners;
    private SelectItem[] ownerItems;
    private String ownerNames;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
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

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccauntName() {
        return accauntName;
    }

    public void setAccauntName(String accauntName) {
        this.accauntName = accauntName;
    }

    public String getBankBranch() {
        return bankBranch;
    }

    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getIbanCode() {
        return ibanCode;
    }

    public void setIbanCode(String ibanCode) {
        this.ibanCode = ibanCode;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getAbaCode() {
        return abaCode;
    }

    public void setAbaCode(String abaCode) {
        this.abaCode = abaCode;
    }

    public String getAgentID() {
        return agentID;
    }

    public void setAgentID(String agentID) {
        this.agentID = agentID;
    }

    public String getBig() {
        return big;
    }

    public void setBig(String big) {
        this.big = big;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public CurrencyItem getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyItem currency) {
        this.currency = currency;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getOpeningAmount() {
        return openingAmount;
    }

    public void setOpeningAmount(BigDecimal openingAmount) {
        this.openingAmount = openingAmount;
    }

    public DateNonConvertable getOpeningDate() {
        return openingDate;
    }

    public void setOpeningDate(DateNonConvertable openingDate) {
        this.openingDate = openingDate;
    }

    public boolean isUsedInSystem() {
        return usedInSystem;
    }

    public void setUsedInSystem(boolean usedInSystem) {
        this.usedInSystem = usedInSystem;
    }

    public boolean isReconcilationReportEnabled() {
        return isReconcilationReportEnabled;
    }

    public void setReconcilationReportEnabled(boolean reconcilationReportEnabled) {
        isReconcilationReportEnabled = reconcilationReportEnabled;
    }

    public Map<String, SelectItem[]> getCountryAndRegionItems() {
        return countryAndRegionItems;
    }

    public void setCountryAndRegionItems(Map<String, SelectItem[]> countryAndRegionItems) {
        this.countryAndRegionItems = countryAndRegionItems;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
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

    public void setCustomFieldsMap(HashMap<String, Object> customFieldsMap) {
        this.customFieldsMap = customFieldsMap;
    }

    public Boolean isActive() {
        return active != null ? active : true;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public ArrayList<SelectItem> getSelectedOwners() {
        return this.selectedOwners;
    }

    public void setSelectedOwners(ArrayList<SelectItem> selectedOwners) {
        this.selectedOwners = selectedOwners;
    }

    public SelectItem[] getOwnerItems() {
        return this.ownerItems;
    }

    public void setOwnerItems(final SelectItem[] ownerItems) {
        this.ownerItems = ownerItems;
    }

    public String getOwnerNames() {
        return this.ownerNames;
    }

    public void setOwnerNames(final String ownerNames) {
        this.ownerNames = ownerNames;
    }
}
