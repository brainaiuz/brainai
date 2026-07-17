package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.HasApprovers;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 03.08.14
 * Time: 0:13
 * To change this template use File | Settings | File Templates.
 */
public class CashAdvanceItem extends HasApprovers implements IsSerializable, ListingCustomFields {

    public static final String ACTION = "action";
    public static final String EMPLOYEE_CODE = "employeeCode";
    public static final String EMPLOYEE_NAME = "employeeName";
    public static final String DATE = "date";
    public static final String APPROVER = "approver";
    public static final String AMOUNT = "amount";
    public static final String STATUS = "status";
    public static final String NUMBER = "number";
    public static final String REMAINING_AMOUNT = "remainingAmount";

    public static final String NUMBER_EXISTS = "NUMBER_EXISTS";
    public static final String NOT_SUFFICIENT_AMOUNT = "NOT_SUFFICIENT_AMOUNT";
    private Integer objectID;
    private SelectItem approver;
    private SelectItem employee;
    private String employeeCode;
    private String employeeName;
    private SelectItem driverNumber;
    private String driverId;
    private String purpose;
    private String type;
    private BigDecimal paymentAmount;
    private BigDecimal totalAmount;
    private BigDecimal totalInBaseAmount;
    private DateNonConvertable date;
    private DateNonConvertable transactionDate;
    private DateNonConvertable approvedDate;
    private DateNonConvertable creationDate;
    private SelectItem paymentMethod;
    private SelectItem[] paymentMethods;
    private SelectItem status;
    private Double percent;
    private SelectItem paidFromAccount;
    private SelectItem cashAdvanceAccount;
    private Boolean doubleConfirmationEnabled;
    private PaymentDeductionSelectItem categoryItem;
    private boolean canApprove;
    private FileItem[] attachments;
    private CurrencyItem currency;
    private BigDecimal exchangeRate;
    private Boolean enabledMultiCurrency;
    private PayrollPdfTemplateList pdfTemplateList;
    private List<PaymentDeductionObject> loanCategories;
    private Integer currentUserId;
    private String reference;
    private String number;
    private Integer intNumber;
    private Integer multiCashAdvanceId;
    private BankTransferNumberData bankTransferNumberData;
    private BigDecimal remainingAmount;
    private BigDecimal basicSalary;
    private BigDecimal percentage;
    private boolean isApproveForAll;
    private boolean isUsedInPayslip;
    private SelectItem[] templates;
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private HashMap<String, Object> customFieldValues;
    private Integer leaveRequestId;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
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

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public DateNonConvertable getDate() {
        return date;
    }

    public void setDate(DateNonConvertable date) {
        this.date = date;
    }

    public DateNonConvertable getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(DateNonConvertable approvedDate) {
        this.approvedDate = approvedDate;
    }

    public DateNonConvertable getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(DateNonConvertable creationDate) {
        this.creationDate = creationDate;
    }

    public DateNonConvertable getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(DateNonConvertable transactionDate) {
        this.transactionDate = transactionDate;
    }


    public SelectItem getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(SelectItem paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public SelectItem[] getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(SelectItem[] paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public String getOverAllStatus() {
        return getOverallStatus() != null ? getOverallStatus().getName() : null;
    }

    public SelectItem getStatus() {
        return status;
    }

    public void setStatus(SelectItem status) {
        this.status = status;
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }

    public SelectItem getPaidFromAccount() {
        return paidFromAccount;
    }

    public void setPaidFromAccount(SelectItem paidFromAccount) {
        this.paidFromAccount = paidFromAccount;
    }

    public SelectItem getCashAdvanceAccount() {
        return cashAdvanceAccount;
    }

    public void setCashAdvanceAccount(SelectItem cashAdvanceAccount) {
        this.cashAdvanceAccount = cashAdvanceAccount;
    }

    public Boolean getDoubleConfirmationEnabled() {
        return doubleConfirmationEnabled != null ? doubleConfirmationEnabled : false;
    }

    public void setDoubleConfirmationEnabled(Boolean doubleConfirmationEnabled) {
        this.doubleConfirmationEnabled = doubleConfirmationEnabled;
    }

    public PaymentDeductionSelectItem getCategoryItem() {
        return categoryItem;
    }

    public void setCategoryItem(PaymentDeductionSelectItem categoryItem) {
        this.categoryItem = categoryItem;
    }

    public boolean isCanApprove() {
        return canApprove;
    }

    public void setCanApprove(boolean canApprove) {
        this.canApprove = canApprove;
    }

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public BigDecimal getTotalInBaseAmount() {
        return totalInBaseAmount;
    }

    public void setTotalInBaseAmount(BigDecimal totalInBaseAmount) {
        this.totalInBaseAmount = totalInBaseAmount;
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

    public Boolean isEnabledMultiCurrency() {
        return enabledMultiCurrency != null && enabledMultiCurrency;
    }

    public void setEnabledMultiCurrency(Boolean enabledMultiCurrency) {
        this.enabledMultiCurrency = enabledMultiCurrency;
    }

    public SelectItem getDriverNumber() {
        return driverNumber;
    }

    public void setDriverNumber(SelectItem driverNumber) {
        this.driverNumber = driverNumber;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public PayrollPdfTemplateList getPdfTemplateList() {
        return pdfTemplateList;
    }

    public void setPdfTemplateList(PayrollPdfTemplateList pdfTemplateList) {
        this.pdfTemplateList = pdfTemplateList;
    }

    public List<PaymentDeductionObject> getLoanCategories() {
        if (loanCategories == null) {
            loanCategories = new ArrayList<>();
        }
        return loanCategories;
    }

    public Integer getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(Integer currentUserId) {
        this.currentUserId = currentUserId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public Integer getMultiCashAdvanceId() {
        return this.multiCashAdvanceId;
    }

    public void setMultiCashAdvanceId(final Integer multiCashAdvanceId) {
        this.multiCashAdvanceId = multiCashAdvanceId;
    }

    public BankTransferNumberData getBankTransferNumberData() {
        return bankTransferNumberData;
    }

    public void setBankTransferNumberData(BankTransferNumberData bankTransferNumberData) {
        this.bankTransferNumberData = bankTransferNumberData;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public BigDecimal getBasicSalary() {
        return this.basicSalary;
    }

    public void setBasicSalary(final BigDecimal basicSalary) {
        this.basicSalary = basicSalary;
    }

    public BigDecimal getPercentage() {
        return this.percentage;
    }

    public void setPercentage(final BigDecimal percentage) {
        this.percentage = percentage;
    }

    public boolean isApproveForAll() {
        return isApproveForAll;
    }

    public void setApproveForAll(boolean approveForAll) {
        isApproveForAll = approveForAll;
    }

    public boolean isUsedInPayslip() {
        return isUsedInPayslip;
    }

    public SelectItem[] getTemplates() {
        return templates;
    }

    public void setTemplates(SelectItem[] templates) {
        this.templates = templates;
    }

    public void setUsedInPayslip(boolean usedInPayslip) {
        isUsedInPayslip = usedInPayslip;
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

    public Integer getLeaveRequestId() {
        return leaveRequestId;
    }

    public void setLeaveRequestId(Integer leaveRequestId) {
        this.leaveRequestId = leaveRequestId;
    }
}
