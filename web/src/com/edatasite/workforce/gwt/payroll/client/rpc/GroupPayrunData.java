package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 13.03.14
 * Time: 15:02
 * To change this template use File | Settings | File Templates.
 */
public class GroupPayrunData implements IsSerializable {

    public static final String PERIOD = "period";
    public static final String APPROVER = "approver";
    public static final String PREPARER = "preparer";
    public static final String STATUS = "status";
    public static final String BATCH = "batch";
    public static final String TOTAL_AMOUNT = "totalAmount";
    public static final String TOTAL_IN_BASE = "totalInBase";
    public static final String CURRENCY_NAME = "currencyName";
    public static final String PAYMENT_METHOD = "paymentMethod";
    public static final String PROCESS_DATE = "processDate";
    public static final String BASIC_SALARY = "basicSalary";
    public static final String ALLOWANCE = "allowance";
    public static final String PENSION = "pension";
    public static final String EMPLOYER_CONTRIBUTION = "employerContribution";
    public static final String DEDUCTION = "deduction";
    public static final String EXPENSE = "expense";

    private Integer objectID;
    private String status;
    private String statusCode;
    private String status2;
    private SelectItem creator;
    private SelectItem approver;
    private SelectItem approver2;
    private SelectItem payrollBatchItem;
    private SelectItem projectItem;
    private SelectItem locationItem;
    private Integer monthID;
    private Integer year;
    private Integer frequency;
    private String month;
    private String returnMessage;
    private BigDecimal totalAmount;
    private BigDecimal totalInBase;
    private SinglePayrunItem[] tableItems;
    private DateNonConvertable createdDate;
    private DateNonConvertable approveDate;
    private DateNonConvertable processDate;
    private SelectItem[] existingPayslips;
    private String currencyName;
    private String companyWpsNumber;
    private String companyBankAccountCode;
    private HashSet<PaymentDeductionSelectItem> allPaymentCategories;
    private HashSet<PaymentDeductionSelectItem> allDeductionCategories;
    private HashSet<PaymentDeductionSelectItem> allTaxCategories;
    private HashSet<PaymentDeductionSelectItem> allEmployerContributionCategories;
    private Boolean fromTaxi;
    private Boolean calculatePension;
    private Boolean atsCustomization;
    private Boolean doubleApprovedEnabled;
    private Boolean enabledMultiCurrency;
    private Integer pensionType;
    private Integer pensionValueType;
    private Integer companyPensionType;
    private BigDecimal companyPensionValue;
    private BigDecimal companyNonLocalPensionValue;
    private BigDecimal pensionValue;
    private BigDecimal nonLocalPensionValue;
    private BigDecimal empMaxTaxableAmount = BigDecimal.ZERO;
    private BigDecimal compMaxtaxableAmount = BigDecimal.ZERO;
    private ArrayList<PaymentDeductionSelectItem> pensionAllowances;
    private Boolean doubleConfirmationEnabled;
    private boolean sendNotification;
    private CurrencyItem currency;
    private BigDecimal exchangeRate;
    private SelectItem payMethod;
    private String payMethodName;
    private ArrayList<SelectItem> paymentMethods = new ArrayList<>();
    private ArrayList<Integer> pendingItemIds;
    private ArrayList<PayrunPayment> payments;
    private HashMap<Integer, SinglePayrunItem> changedItems;
    private HashMap<Integer, Boolean> deletedItems;
    private BigDecimal basicSalary;
    private BigDecimal allowance;
    private BigDecimal pension;
    private BigDecimal deduction;
    private BigDecimal expense;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatus2() {
        return status2;
    }

    public void setStatus2(String status2) {
        this.status2 = status2;
    }

    public SelectItem getApprover2() {
        return approver2;
    }

    public void setApprover2(SelectItem approver2) {
        this.approver2 = approver2;
    }

    public SelectItem getApprover() {
        return approver;
    }

    public void setApprover(SelectItem approver) {
        this.approver = approver;
    }

    public SelectItem getPayrollBatchItem() {
        return payrollBatchItem;
    }

    public void setPayrollBatchItem(SelectItem payrollBatchItem) {
        this.payrollBatchItem = payrollBatchItem;
    }

    public SelectItem getProjectItem() {
        return projectItem;
    }

    public void setProjectItem(SelectItem projectItem) {
        this.projectItem = projectItem;
    }

    public SelectItem getLocationItem() {
        return this.locationItem;
    }

    public void setLocationItem(final SelectItem locationItem) {
        this.locationItem = locationItem;
    }

    public Integer getMonthID() {
        return monthID;
    }

    public void setMonthID(Integer monthID) {
        this.monthID = monthID;
    }

    public String getMonth() {
        return month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public SinglePayrunItem[] getTableItems() {
        return tableItems;
    }

    public void setTableItems(SinglePayrunItem[] tableItems) {
        this.tableItems = tableItems;
    }

    public DateNonConvertable getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateNonConvertable createdDate) {
        this.createdDate = createdDate;
    }

    public DateNonConvertable getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(DateNonConvertable approveDate) {
        this.approveDate = approveDate;
    }

    public DateNonConvertable getProcessDate() {
        return processDate;
    }

    public void setProcessDate(DateNonConvertable processDate) {
        this.processDate = processDate;
    }

    public SelectItem getCreator() {
        return creator;
    }

    public void setCreator(SelectItem creator) {
        this.creator = creator;
    }

    public SelectItem[] getExistingPayslips() {
        return existingPayslips;
    }

    public void setExistingPayslips(SelectItem[] existingPayslips) {
        this.existingPayslips = existingPayslips;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCompanyWpsNumber() {
        return companyWpsNumber;
    }

    public void setCompanyWpsNumber(String companyWpsNumber) {
        this.companyWpsNumber = companyWpsNumber;
    }

    public String getCompanyBankAccountCode() {
        return companyBankAccountCode;
    }

    public void setCompanyBankAccountCode(String companyBankAccountCode) {
        this.companyBankAccountCode = companyBankAccountCode;
    }

    public HashSet<PaymentDeductionSelectItem> getAllPaymentCategories() {
        if (allPaymentCategories == null) {
            allPaymentCategories = new HashSet<>();
        }

        return allPaymentCategories;
    }

    public void setAllPaymentCategories(HashSet<PaymentDeductionSelectItem> allPaymentCategories) {
        this.allPaymentCategories = allPaymentCategories;
    }

    public HashSet<PaymentDeductionSelectItem> getAllDeductionCategories() {
        if (allDeductionCategories == null) {
            allDeductionCategories = new HashSet<>();
        }
        return allDeductionCategories;
    }

    public void setAllDeductionCategories(HashSet<PaymentDeductionSelectItem> allDeductionCategories) {
        this.allDeductionCategories = allDeductionCategories;
    }

    public HashSet<PaymentDeductionSelectItem> getAllTaxCategories() {
        if (allTaxCategories == null) {
            allTaxCategories = new HashSet<>();
        }
        return allTaxCategories;
    }

    public void setAllTaxCategories(HashSet<PaymentDeductionSelectItem> allTaxCategories) {
        this.allTaxCategories = allTaxCategories;
    }

    public HashSet<PaymentDeductionSelectItem> getAllEmployerContributionCategories() {
        return allEmployerContributionCategories;
    }

    public void setAllEmployerContributionCategories(HashSet<PaymentDeductionSelectItem> allEmployerContributionCategories) {
        this.allEmployerContributionCategories = allEmployerContributionCategories;
    }

    public Boolean isFromTaxi() {
        return fromTaxi != null && fromTaxi;
    }

    public void setFromTaxi(Boolean fromTaxi) {
        this.fromTaxi = fromTaxi;
    }

    public Boolean getCalculatePension() {
        return calculatePension;
    }

    public void setCalculatePension(Boolean calculatePension) {
        this.calculatePension = calculatePension;
    }

    public Boolean isAtsCustomizationEnabled() {
        return atsCustomization != null ? atsCustomization : Boolean.FALSE;
    }

    public void setAtsCustomization(Boolean atsCustomization) {
        this.atsCustomization = atsCustomization;
    }

    public Boolean isDoubleApprovedEnabled() {
        return Optional.ofNullable(doubleApprovedEnabled).orElse(false);
    }

    public void setDoubleApprovedEnabled(Boolean doubleApprovedEnabled) {
        this.doubleApprovedEnabled = doubleApprovedEnabled;
    }

    public Integer getPensionType() {
        return pensionType;
    }

    public void setPensionType(Integer pensionType) {
        this.pensionType = pensionType;
    }

    public Integer getPensionValueType() {
        return pensionValueType;
    }

    public void setPensionValueType(Integer pensionValueType) {
        this.pensionValueType = pensionValueType;
    }

    public Integer getCompanyPensionType() {
        return companyPensionType;
    }

    public void setCompanyPensionType(Integer companyPensionType) {
        this.companyPensionType = companyPensionType;
    }

    public BigDecimal getCompanyPensionValue() {
        return companyPensionValue;
    }

    public void setCompanyPensionValue(BigDecimal companyPensionValue) {
        this.companyPensionValue = companyPensionValue;
    }

    public BigDecimal getPensionValue() {
        return pensionValue;
    }

    public void setPensionValue(BigDecimal pensionValue) {
        this.pensionValue = pensionValue;
    }

    public BigDecimal getNonLocalPensionValue() {
        return nonLocalPensionValue;
    }

    public void setNonLocalPensionValue(BigDecimal nonLocalPensionValue) {
        this.nonLocalPensionValue = nonLocalPensionValue;
    }

    public BigDecimal getEmpMaxTaxableAmount() {
        return empMaxTaxableAmount;
    }

    public void setEmpMaxTaxableAmount(BigDecimal empMaxTaxableAmount) {
        this.empMaxTaxableAmount = empMaxTaxableAmount;
    }

    public BigDecimal getCompMaxtaxableAmount() {
        return compMaxtaxableAmount;
    }

    public void setCompMaxtaxableAmount(BigDecimal compMaxtaxableAmount) {
        this.compMaxtaxableAmount = compMaxtaxableAmount;
    }

    public ArrayList<PaymentDeductionSelectItem> getPensionAllowances() {
        if (pensionAllowances == null) {
            pensionAllowances = new ArrayList<>();
        }
        return pensionAllowances;
    }

    public void setPensionAllowances(ArrayList<PaymentDeductionSelectItem> pensionAllowances) {
        this.pensionAllowances = pensionAllowances;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public BigDecimal getCompanyNonLocalPensionValue() {
        return companyNonLocalPensionValue;
    }

    public void setCompanyNonLocalPensionValue(BigDecimal companyNonLocalPensionValue) {
        this.companyNonLocalPensionValue = companyNonLocalPensionValue;
    }

    public boolean isSendNotification() {
        return sendNotification;
    }

    public void setSendNotification(boolean sendNotification) {
        this.sendNotification = sendNotification;
    }

    public Boolean isDoubleConfirmationEnabled() {
        return doubleConfirmationEnabled != null && doubleConfirmationEnabled;
    }

    public void setDoubleConfirmationEnabled(Boolean doubleConfirmationEnabled) {
        this.doubleConfirmationEnabled = doubleConfirmationEnabled;
    }

    public BigDecimal getTotalInBase() {
        return totalInBase;
    }

    public void setTotalInBase(BigDecimal totalInBase) {
        this.totalInBase = totalInBase;
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

    public SelectItem getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(SelectItem payMethod) {
        this.payMethod = payMethod;
    }

    public String getPayMethodName() {
        return payMethodName;
    }

    public void setPayMethodName(String payMethodName) {
        this.payMethodName = payMethodName;
    }

    public ArrayList<SelectItem> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(ArrayList<SelectItem> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public ArrayList<Integer> getPendingItemIds() {
        return pendingItemIds;
    }

    public void setPendingItemIds(ArrayList<Integer> pendingItemIds) {
        this.pendingItemIds = pendingItemIds;
    }

    public ArrayList<PayrunPayment> getPayments() {
        return payments;
    }

    public void setPayments(ArrayList<PayrunPayment> payments) {
        this.payments = payments;
    }

    public HashMap<Integer, SinglePayrunItem> getChangedItems() {
        return changedItems;
    }

    public void setChangedItems(HashMap<Integer, SinglePayrunItem> changedItems) {
        this.changedItems = changedItems;
    }

    public HashMap<Integer, Boolean> getDeletedItems() {
        return deletedItems;
    }

    public void setDeletedItems(HashMap<Integer, Boolean> deletedItems) {
        this.deletedItems = deletedItems;
    }

    public BigDecimal getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(BigDecimal basicSalary) {
        this.basicSalary = basicSalary;
    }

    public BigDecimal getAllowance() {
        return allowance;
    }

    public void setAllowance(BigDecimal allowance) {
        this.allowance = allowance;
    }

    public BigDecimal getPension() {
        return pension;
    }

    public void setPension(BigDecimal pension) {
        this.pension = pension;
    }

    public BigDecimal getDeduction() {
        return deduction;
    }

    public void setDeduction(BigDecimal deduction) {
        this.deduction = deduction;
    }

    public BigDecimal getExpense() {
        return expense;
    }

    public void setExpense(BigDecimal expense) {
        this.expense = expense;
    }
}
