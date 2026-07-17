package com.edatasite.workforce.gwt.profile.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by Aziz on 08.09.14.
 */
public class BenefitItem implements IsSerializable {

    public static final String NAME = "NAME";
    public static final String TYPE = "TYPE";
    public static final String QTYTYPE = "QTYTYPE";
    public static final String CURRENCY = "CURRENCY";
    public static final String EXPIRE_DATE = "EXPIRE_DATE";
    public static final String TRANSFERRABLE = "TRANSFERRABLE";
    public static final String STATUS = "STATUS";

    private Integer objectId;
    private String name;
    private String code;
    private String type;
    private Integer typeID;
    private String qtytype;
    private Integer qtytypeID;
    private String currency;
    private Integer currencyID;
    private DateNonConvertable expireDate;
    private Boolean transferrable = false;
    private Boolean qtyRestriction = false;
    private SelectItem[] types;
    private SelectItem[] qtyTypes;
    private SelectItem[] currencys;
    private ArrayList<SelectItem> employees = new ArrayList<>();
    private String description;
    private boolean isActive;
    private double allowance;
    private boolean applyAll;
    private SelectItem debitToAccount;
    private SelectItem creditToAccount;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
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

    public String getQtytype() {
        return qtytype;
    }

    public void setQtytype(String qtytype) {
        this.qtytype = qtytype;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public DateNonConvertable getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(DateNonConvertable expireDate) {
        this.expireDate = expireDate;
    }

    public Boolean getTransferrable() {
        return transferrable;
    }

    public void setTransferrable(Boolean transferrable) {
        this.transferrable = transferrable;
    }

    public Boolean getQtyRestriction() {
        return qtyRestriction;
    }

    public void setQtyRestriction(Boolean qtyRestriction) {
        this.qtyRestriction = qtyRestriction;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public Integer getQtytypeID() {
        return qtytypeID;
    }

    public void setQtytypeID(Integer qtytypeID) {
        this.qtytypeID = qtytypeID;
    }

    public Integer getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(Integer currencyID) {
        this.currencyID = currencyID;
    }

    public SelectItem[] getTypes() {
        return types;
    }

    public void setTypes(SelectItem[] types) {
        this.types = types;
    }

    public SelectItem[] getQtyTypes() {
        return qtyTypes;
    }

    public void setQtyTypes(SelectItem[] qtyTypes) {
        this.qtyTypes = qtyTypes;
    }

    public SelectItem[] getCurrencys() {
        return currencys;
    }

    public void setCurrencys(SelectItem[] currencys) {
        this.currencys = currencys;
    }

    public void setEmployees(ArrayList<SelectItem> employees) {
        this.employees = employees;
    }

    public ArrayList<SelectItem> getEmployees() {
        return employees;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAllowance() {
        return allowance;
    }

    public void setAllowance(double allowance) {
        this.allowance = allowance;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isApplyAll() {
        return applyAll;
    }

    public void setApplyAll(boolean applyAll) {
        this.applyAll = applyAll;
    }

    public SelectItem getDebitToAccount() {
        return debitToAccount;
    }

    public void setDebitToAccount(SelectItem debitToAccount) {
        this.debitToAccount = debitToAccount;
    }

    public SelectItem getCreditToAccount() {
        return creditToAccount;
    }

    public void setCreditToAccount(SelectItem creditToAccount) {
        this.creditToAccount = creditToAccount;
    }
}
