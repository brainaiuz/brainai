package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Jonibek
 * Date: Mar 30, 2009
 * Time: 7:19:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class AccountListItem extends WfmTreeItem {

    public static final String ACTION = "action";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String TYPE = "type";
    public static final String TAX_CODE = "tax_code";
    public static final String DESCRIPTION = "description";
    public static final String CURRENCY = "currency";
    public static final String BALANCE = "balance";
    public static final String FROM = "from";
    public static final String TO = "to";
    public static final String TAKEN_FROM_VACTION_ALLOWANCE = "takenFromVacationAllowance";
    public static final String TAKEN_FROM_ANNUAL_LEAVE_ALLOWANCE = "takenFromAnnualLeaveAllowance";
    public static final String LOCATION = "location";
    public static final String PARENT = "parent";
    public static final String LAST_UPDATED_DATE = "lastUpdatedDate";
    public static final String STATUS = "status";
    public static final String SHOW_IN_EXPENSE_CLAIM = "showInExpenseClaim";
    public static final String ACTIVE = "active";
    public static final String ENABLE_PAYMENTS_TO_THIS_ACCOUNT = "enablePaymentsToThisAccount";
    public static final String CHART_OF_ACCOUNT_KEY = "key";

    private Integer objectID;
    private String codeString;
    private String accountType;
    private String accountTypeCode;
    private String taxRate;
    private String ytd;
    private Integer chartOfAccountkey;
    private Boolean isEditable;
    private String currency;
    private BigDecimal balance;
    private BigDecimal foreignBalance;
    private String saasuGUID;
    private Date lastUpdatedDate;
    private Date saasuLastUpdatedDate;
    private String saasuLastUpdatedUid;
    private Integer bankAccountId;
    private String parentName;
    private Boolean active;
    private Boolean showInExpense;
    private Boolean enablePayments;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getAccountTypeCode() {
        return accountTypeCode;
    }

    public void setAccountTypeCode(String accountTypeCode) {
        this.accountTypeCode = accountTypeCode;
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

    public Boolean isEditable() {
        return isEditable != null ? isEditable : false;
    }

    public void setEditable(Boolean editable) {
        this.isEditable = editable;
    }

    public void setIsEditable(Boolean editable) {
        this.isEditable = editable;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getForeignBalance() {
        return foreignBalance;
    }

    public void setForeignBalance(BigDecimal foreignBalance) {
        this.foreignBalance = foreignBalance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getSaasuGUID() {
        return saasuGUID;
    }

    public void setSaasuGUID(String saasuGUID) {
        this.saasuGUID = saasuGUID;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Date getSaasuLastUpdatedDate() {
        return saasuLastUpdatedDate;
    }

    public void setSaasuLastUpdatedDate(Date saasuLastUpdatedDate) {
        this.saasuLastUpdatedDate = saasuLastUpdatedDate;
    }

    public String getSaasuLastUpdatedUid() {
        return saasuLastUpdatedUid;
    }

    public void setSaasuLastUpdatedUid(String saasuLastUpdatedUid) {
        this.saasuLastUpdatedUid = saasuLastUpdatedUid;
    }

    public Integer getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(Integer bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Boolean isActive() {
        return active != null ? active : true;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getShowInExpense() {
        return showInExpense;
    }

    public void setShowInExpense(Boolean showInExpense) {
        this.showInExpense = showInExpense;
    }

    public Boolean getEnablePayments() {
        return enablePayments;
    }

    public void setEnablePayments(Boolean enablePayments) {
        this.enablePayments = enablePayments;
    }

    public Integer getChartOfAccountkey() {
        return chartOfAccountkey;
    }

    public void setChartOfAccountkey(Integer chartOfAccountkey) {
        this.chartOfAccountkey = chartOfAccountkey;
    }
}
