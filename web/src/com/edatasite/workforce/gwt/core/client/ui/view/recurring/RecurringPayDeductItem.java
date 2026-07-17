package com.edatasite.workforce.gwt.core.client.ui.view.recurring;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.HasApprovers;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RecurringPayDeductItem extends HasApprovers implements IsSerializable {

    public static final String ACTION = "action";
    public static final String EMPLOYEE_CODE = "employeeCode";
    public static final String EMPLOYEE_NAME = "employeeName";
    public static final String FROM_DATE = "fromDate";
    public static final String TO_DATE = "toDate";
    public static final String APPROVER = "approver";
    public static final String PAY_TYPE = "payType";
    public static final String TOTAL = "total";
    public static final String REMAINING_AMOUNT = "remainingAmount";
    public static final String STATUS = "status";

    private Integer objectID;
    private Integer currentUserId;
    private Integer type;
    private PayType payType;
    private BigDecimal paymentAmount;
    private BigDecimal percentage;
    private BigDecimal totalLimit;
    private BigDecimal remainingAmount;
    private SelectItem status;
    private SelectItem approver;
    private SelectItem employee;
    private String employeeCode;
    private String employeeName;

    private CurrencyItem currency;
    private BigDecimal exchangeRate;

    private SelectItem categoryItem;
    private DateNonConvertable fromDate;
    private DateNonConvertable toDate;

    private DateNonConvertable approvedDate;

    private List<PaymentDeductionSelectItem> linkedCategories = new ArrayList<>();

    private boolean fromAllAllowances;

    private BigDecimal minimumWage;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(Integer currentUserId) {
        this.currentUserId = currentUserId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public PayType getPayType() {
        return payType;
    }

    public void setPayType(PayType payType) {
        this.payType = payType;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public BigDecimal getTotalLimit() {
        return totalLimit;
    }

    public void setTotalLimit(BigDecimal totalLimit) {
        this.totalLimit = totalLimit;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public SelectItem getStatus() {
        return status;
    }

    public void setStatus(SelectItem status) {
        this.status = status;
    }

    public SelectItem getApprover() {
        return approver;
    }

    public void setApprover(SelectItem approver) {
        this.approver = approver;
    }

    public SelectItem getEmployee() {
        return employee;
    }

    public void setEmployee(SelectItem employee) {
        this.employee = employee;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
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

    public SelectItem getCategoryItem() {
        return categoryItem;
    }

    public void setCategoryItem(SelectItem categoryItem) {
        this.categoryItem = categoryItem;
    }

    public DateNonConvertable getFromDate() {
        return fromDate;
    }

    public void setFromDate(DateNonConvertable fromDate) {
        this.fromDate = fromDate;
    }

    public DateNonConvertable getToDate() {
        return toDate;
    }

    public void setToDate(DateNonConvertable toDate) {
        this.toDate = toDate;
    }

    public DateNonConvertable getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(DateNonConvertable approvedDate) {
        this.approvedDate = approvedDate;
    }

    public List<PaymentDeductionSelectItem> getLinkedCategories() {
        return linkedCategories != null ? linkedCategories : new ArrayList<>();
    }

    public void setLinkedCategories(List<PaymentDeductionSelectItem> linkedCategories) {
        this.linkedCategories = linkedCategories;
    }

    public boolean isFromAllAllowances() {
        return fromAllAllowances;
    }

    public void setFromAllAllowances(boolean fromAllAllowances) {
        this.fromAllAllowances = fromAllAllowances;
    }

    public BigDecimal getMinimumWage() {
        return minimumWage;
    }

    public void setMinimumWage(BigDecimal minimumWage) {
        this.minimumWage = minimumWage;
    }
}
