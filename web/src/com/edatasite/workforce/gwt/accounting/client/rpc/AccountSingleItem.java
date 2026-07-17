package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Jonibek
 * Date: Apr 3, 2009
 * Time: 5:56:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class AccountSingleItem implements IsSerializable, ListingCustomFields {
    private Integer objectID;
    private String code;
    private String name;
    private String description;
    private String type;
    private String taxRate;
    private String ytd;
    private String parentAccount;
    private String showInExpense;
    private String active;
    private String enablePayments;
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private HashMap<String, Object> customFieldValues;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public String getYtd() {
        return ytd;
    }

    public void setYtd(String ytd) {
        this.ytd = ytd;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParentAccount() {
        return parentAccount;
    }

    public void setParentAccount(String parentAccount) {
        this.parentAccount = parentAccount;
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

    public String getShowInExpense() {
        return showInExpense;
    }

    public void setShowInExpense(String showInExpense) {
        this.showInExpense = showInExpense;
    }

    public String getEnablePayments() {
        return enablePayments;
    }

    public void setEnablePayments(String enablePayments) {
        this.enablePayments = enablePayments;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
