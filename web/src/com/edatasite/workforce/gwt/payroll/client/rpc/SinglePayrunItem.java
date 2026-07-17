package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.view.PayrollPdfTemplateList;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 04.03.14
 * Time: 10:38
 * To change this template use File | Settings | File Templates.
 */
public class SinglePayrunItem implements IsSerializable, ListingCustomFields {

    public static final String ACTION = "action";
    public static final String EMPLOYEE_CODE = "employee_code";
    public static final String EMPLOYEE = "employee";
    public static final String PROJECTS = "projects";
    public static final String PERIOD = "period";
    public static final String APPROVER = "approver";
    public static final String PREPARER = "preparer";
    public static final String TOTAL = "total";
    public static final String STATUS = "status";
    public static final String CURRENCY = "currency";
    public static final String DRIVER_ID = "driverID";
    public static final String PROCESS_DATE = "processDate";
    public static final String PAYMENT_METHOD = "paymentMethod";
    private Integer objectID;
    private Integer employeeID;
    private Integer groupPayrunID;
    private BigDecimal daysWorked;
    private Integer pensionValueType;
    private Integer pensionType;
    private Integer monthDaysCount;
    private Integer companyPensionType;
    private Integer pdfTemplateID;
    private String employeeCode;
    private Long employeeNumber;
    private String employee;
    private String employeeTemplateStatus;
    private String firstName;
    private String lastName;
    private String rejectionNote;
    private String wpsNumber;
    private String bankName;
    private String accountName;
    private String statusName;
    private String accountNumber;
    private String bankCode;
    private String bankIBAN;
    private String description;
    private String currencyName;
    private String returnMessage;
    private DateNonConvertable creationDate;
    private DateNonConvertable approvedDate;
    private BigDecimal salary;
    private BigDecimal basicSalary;
    private BigDecimal dailyRate;
    private BigDecimal actualMonthPay;
    private BigDecimal allowance;
    private BigDecimal additionalPay;
    private BigDecimal deduction;
    private BigDecimal tax;
    private BigDecimal expense;
    private BigDecimal total;
    private BigDecimal totalInBase;
    private BigDecimal monthlyCollection;
    private BigDecimal flueAmount;
    private BigDecimal spentFlueAmount;
    private BigDecimal comission;
    private BigDecimal pensionRate;
    private BigDecimal nonLocalPensionRate;
    private BigDecimal pensionAmount;
    private BigDecimal companyPensionAmount;
    private BigDecimal companyPensionRate;
    private BigDecimal companyNonLocalPensionRate;
    private BigDecimal empMaxTaxableAmount = BigDecimal.ZERO;
    private BigDecimal compMaxTaxableAmount = BigDecimal.ZERO;
    private DateNonConvertable fromDate;
    private DateNonConvertable toDate;
    private DateNonConvertable processDate;
    private Boolean approved;
    private Boolean sendEmail;
    private Boolean calculatePension;
    private Boolean isLocalEmployee;
    private Boolean isFromEndOfService;
    private Boolean atsCustomization;
    private Boolean doubleApprovedEnabled;
    private Boolean doubleConfirmationEnabled;
    private Boolean sendNotification;
    private Boolean enabledMultiCurrency;
    private Boolean enabledAccounting;
    private ArrayList<PaymentDeductionObject> paymentCategories = new ArrayList<>();
    private ArrayList<PaymentDeductionObject> deductionCategories = new ArrayList<>();
    private ArrayList<PaymentDeductionObject> taxCategories = new ArrayList<>();
    private ArrayList<PaymentDeductionObject> employerContributionCategories = new ArrayList<>();
    private PaymentDeductionObject employeeExpenses;
    private PaymentDeductionObject petrolLimitExcess;
    private Long driverID;
    private String approvalRejectionStatus;
    /* for validation multiple payslipTables for one month */
    private ArrayList<String> payedPayslipDataList;

    private ArrayList<PaymentDeductionSelectItem> pensionAllowances;
    private Integer statusID;
    private String status;
    private String statusCode;
    private String status2;
    private SelectItem creator;
    private SelectItem approver;
    private SelectItem approver2;
    private ArrayList<SelectItem> projects;
    private Integer monthID;
    private Integer year;
    private Integer frequency;
    private String month;
    private String paymentPolicy;
    private String payMethodName;
    private Integer payMethodId;
    // from company payroll settings
    private BigDecimal numberOfWorkDay;
    // for pdf templates
    private PayrollPdfTemplateList pdfTemplateList;
    private HashMap<String, String> customFields;
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private CurrencyItem[] currencies;
    private CurrencyItem currency;
    private BigDecimal exchangeRate;
    private BigDecimal recurringsTotal;
    private Integer nonPaidLeaveDays;
    private BigDecimal monthlySalik;
    private boolean isEditable;
    private SelectItem[] payMethods;
    private String position;
    private String nationality;
    private Date startDate;
    private Double regularOvertimeRate;
    private Double weekendOvertimeRate;
    private Double holidayOvertimeRate;
    private SinglePayrunItem prevMonthItem;
    private BigDecimal gross;
    private HashMap<String, Object> customFieldMap;
    private PayrollTotalTO totalTO;
    private BigDecimal employerContribution;
    private SelectItem projectItem;
    private Integer projectId;

    private List<PayrunPaymentItem> paymentItems;

    private Integer processDateId;
    private Integer basicSalaryId;
    private Integer journalId;

    public SinglePayrunItem() {
    }

    public SinglePayrunItem(Integer objectID, Integer employeeID, String employee, String employeeCode, Date fromDate, Date toDate, Date processDate, BigDecimal daysWorked,
                            BigDecimal basicSalary, BigDecimal dailyRate, BigDecimal actualMonthPay, BigDecimal allowance, BigDecimal additionalPay, BigDecimal deduction, BigDecimal tax, BigDecimal employerContribution,
                            BigDecimal expense, BigDecimal comission, BigDecimal monthlyCollection, BigDecimal spentFlueAmount, BigDecimal monthlySalik, String description,
                            BigDecimal total, BigDecimal totalInBase, Integer currencyId, String currencyName, BigDecimal exchangeRate, String rejectionNote,
                            Boolean sendEmail, BigDecimal pensionAmount, BigDecimal companyPensionAmount, Integer monthID, Integer year, String month,
                            Integer frequency, Integer statusID, String statusName, String statusCode, Integer creatorId, String creatorname,
                            Integer approverId, String approverName, Boolean isFromEndOfService, String paymentPolicy, Integer pdfTemplateID, String payMethodName, Integer projectId) {
        this.objectID = objectID;
        this.employeeID = employeeID;
        this.employeeCode = employeeCode;
        this.employee = employee;
        this.fromDate = fromDate != null ? new DateNonConvertable(fromDate) : null;
        this.toDate = toDate != null ? new DateNonConvertable(toDate) : null;
        this.processDate = processDate != null ? new DateNonConvertable(processDate) : null;
        this.daysWorked = daysWorked;
        this.basicSalary = basicSalary;
        this.dailyRate = dailyRate;
        this.actualMonthPay = actualMonthPay;
        this.allowance = allowance;
        this.additionalPay = additionalPay;
        this.deduction = deduction;
        this.tax = tax;
        this.employerContribution = employerContribution;
        this.expense = expense;
        this.comission = comission;
        this.monthlyCollection = monthlyCollection;
        this.spentFlueAmount = spentFlueAmount;
        this.monthlySalik = monthlySalik;
        this.description = description;
        this.total = total;
        this.totalInBase = totalInBase;
        this.currency = currencyId != null ? new CurrencyItem(currencyId, currencyName) : null;
        this.exchangeRate = exchangeRate;
        this.rejectionNote = rejectionNote;
        this.sendEmail = sendEmail;
        this.pensionAmount = pensionAmount;
        this.companyPensionAmount = companyPensionAmount;
        this.monthID = monthID;
        this.year = year;
        this.month = month;
        this.frequency = frequency;
        this.statusID = statusID;
        this.statusName = statusName;
        this.statusCode = statusCode;
        this.status = status;
        this.creator = creatorId != null ? new SelectItem(creatorId, creatorname) : null;
        this.approver = approverId != null ? new SelectItem(approverId, approverName) : null;
        this.isFromEndOfService = isFromEndOfService;
        this.paymentPolicy = paymentPolicy;
        this.pdfTemplateID = pdfTemplateID;
        this.payMethodName = payMethodName;
        this.projectId = projectId;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public Integer getGroupPayrunID() {
        return groupPayrunID;
    }

    public void setGroupPayrunID(Integer groupPayrunID) {
        this.groupPayrunID = groupPayrunID;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public Long getSortEmployeeNumber() {
        return employeeNumber != null ? employeeNumber : 0L;
    }

    public Long getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(Long employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getEmployeeTemplateStatus() {
        return employeeTemplateStatus;
    }

    public void setEmployeeTemplateStatus(String employeeTemplateStatus) {
        this.employeeTemplateStatus = employeeTemplateStatus;
    }

    public DateNonConvertable getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(DateNonConvertable creationDate) {
        this.creationDate = creationDate;
    }

    public DateNonConvertable getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(DateNonConvertable approvedDate) {
        this.approvedDate = approvedDate;
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

    public DateNonConvertable getProcessDate() {
        return processDate;
    }

    public void setProcessDate(DateNonConvertable processDate) {
        this.processDate = processDate;
    }

    public BigDecimal getDaysWorked() {
        return daysWorked;
    }

    public void setDaysWorked(BigDecimal daysWorked) {
        this.daysWorked = daysWorked;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public BigDecimal getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(BigDecimal basicSalary) {
        this.basicSalary = basicSalary;
    }

    public BigDecimal getActualMonthPay() {
        return actualMonthPay;
    }

    public void setActualMonthPay(BigDecimal actualMonthPay) {
        this.actualMonthPay = actualMonthPay;
    }

    public BigDecimal getAllowance() {
        return allowance;
    }

    public void setAllowance(BigDecimal allowance) {
        this.allowance = allowance;
    }

    public BigDecimal getAdditionalPay() {
        return additionalPay;
    }

    public void setAdditionalPay(BigDecimal additionalPay) {
        this.additionalPay = additionalPay;
    }

    public BigDecimal getDeduction() {
        return deduction;
    }

    public void setDeduction(BigDecimal deduction) {
        this.deduction = deduction;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTotal() {
        return total != null ? total : new BigDecimal(0);
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(BigDecimal dailyRate) {
        this.dailyRate = dailyRate;
    }

    public BigDecimal getExpense() {
        return expense;
    }

    public BigDecimal getMonthlyCollection() {
        return monthlyCollection;
    }

    public void setMonthlyCollection(BigDecimal monthlyCollection) {
        this.monthlyCollection = monthlyCollection;
    }

    public BigDecimal getFlueAmount() {
        return flueAmount;
    }

    public void setFlueAmount(BigDecimal flueAmount) {
        this.flueAmount = flueAmount;
    }

    public BigDecimal getSpentFlueAmount() {
        return spentFlueAmount;
    }

    public void setSpentFlueAmount(BigDecimal spentFlueAmount) {
        this.spentFlueAmount = spentFlueAmount;
    }

    public BigDecimal getComission() {
        return comission;
    }

    public BigDecimal getPensionRate() {
        return pensionRate;
    }

    public void setPensionRate(BigDecimal pensionRate) {
        this.pensionRate = pensionRate;
    }

    public BigDecimal getNonLocalPensionRate() {
        return nonLocalPensionRate;
    }

    public void setNonLocalPensionRate(BigDecimal nonLocalPensionRate) {
        this.nonLocalPensionRate = nonLocalPensionRate;
    }

    public Integer getCompanyPensionType() {
        return companyPensionType;
    }

    public void setCompanyPensionType(Integer companyPensionType) {
        this.companyPensionType = companyPensionType;
    }

    public Integer getPdfTemplateID() {
        return pdfTemplateID;
    }

    public void setPdfTemplateID(Integer pdfTemplateID) {
        this.pdfTemplateID = pdfTemplateID;
    }

    public Integer getPensionValueType() {
        return pensionValueType;
    }

    public void setPensionValueType(Integer pensionValueType) {
        this.pensionValueType = pensionValueType;
    }

    public Integer getPensionType() {
        return pensionType;
    }

    public void setPensionType(Integer pensionType) {
        this.pensionType = pensionType;
    }

    public BigDecimal getPensionAmount() {
        return pensionAmount;
    }

    public void setPensionAmount(BigDecimal pensionAmount) {
        this.pensionAmount = pensionAmount;
    }

    public BigDecimal getCompanyPensionAmount() {
        return companyPensionAmount;
    }

    public void setCompanyPensionAmount(BigDecimal companyPensionAmount) {
        this.companyPensionAmount = companyPensionAmount;
    }

    public BigDecimal getCompanyPensionRate() {
        return companyPensionRate;
    }

    public void setCompanyPensionRate(BigDecimal companyPensionRate) {
        this.companyPensionRate = companyPensionRate;
    }

    public BigDecimal getCompanyNonLocalPensionRate() {
        return companyNonLocalPensionRate;
    }

    public void setCompanyNonLocalPensionRate(BigDecimal companyNonLocalPensionRate) {
        this.companyNonLocalPensionRate = companyNonLocalPensionRate;
    }

    public BigDecimal getEmpMaxTaxableAmount() {
        return empMaxTaxableAmount;
    }

    public void setEmpMaxTaxableAmount(BigDecimal empMaxTaxableAmount) {
        this.empMaxTaxableAmount = empMaxTaxableAmount;
    }

    public BigDecimal getCompMaxTaxableAmount() {
        return compMaxTaxableAmount;
    }

    public void setCompMaxTaxableAmount(BigDecimal compMaxTaxableAmount) {
        this.compMaxTaxableAmount = compMaxTaxableAmount;
    }

    public void setComission(BigDecimal comission) {
        this.comission = comission;
    }

    public void setExpense(BigDecimal expense) {
        this.expense = expense;
    }

    public Boolean isApproved() {
        return approved != null ? approved : Boolean.FALSE;
    }


    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public Boolean isSendEmail() {
        return sendEmail != null ? sendEmail : Boolean.FALSE;
    }

    public Boolean sendNotification() {
        return sendNotification != null ? sendNotification : Boolean.FALSE;
    }

    public void setSendNotification(Boolean sendNotification) {
        this.sendNotification = sendNotification;
    }

    public Boolean isAtsCustomization() {
        return atsCustomization;
    }

    public void setAtsCustomization(Boolean atsCustomization) {
        this.atsCustomization = atsCustomization;
    }

    public Boolean isDoubleApprovedEnabled() {
        return doubleApprovedEnabled;
    }

    public void setDoubleApprovedEnabled(Boolean doubleApprovedEnabled) {
        this.doubleApprovedEnabled = doubleApprovedEnabled;
    }

    public Boolean isDoubleConfirmationEnabled() {
        return doubleConfirmationEnabled != null && doubleConfirmationEnabled;
    }

    public void setDoubleConfirmationEnabled(Boolean doubleConfirmationEnabled) {
        this.doubleConfirmationEnabled = doubleConfirmationEnabled;
    }

    public void setSendEmail(Boolean sendEmail) {
        this.sendEmail = sendEmail;
    }

    public String getRejectionNote() {
        return rejectionNote;
    }

    public void setRejectionNote(String rejectionNote) {
        this.rejectionNote = rejectionNote;
    }

    public String getWpsNumber() {
        return wpsNumber;
    }

    public void setWpsNumber(String wpsNumber) {
        this.wpsNumber = wpsNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankIBAN() {
        return bankIBAN;
    }

    public void setBankIBAN(String bankIBAN) {
        this.bankIBAN = bankIBAN;
    }

    public ArrayList<PaymentDeductionObject> getPaymentCategories() {
        return paymentCategories;
    }

    public void setPaymentCategories(ArrayList<PaymentDeductionObject> paymentCategories) {
        this.paymentCategories = paymentCategories;
    }

    public ArrayList<PaymentDeductionObject> getDeductionCategories() {
        return deductionCategories;
    }

    public void setDeductionCategories(ArrayList<PaymentDeductionObject> deductionCategories) {
        this.deductionCategories = deductionCategories;
    }

    public ArrayList<PaymentDeductionObject> getTaxCategories() {
        return taxCategories;
    }

    public void setTaxCategories(ArrayList<PaymentDeductionObject> taxCategories) {
        this.taxCategories = taxCategories;
    }

    public ArrayList<PaymentDeductionObject> getEmployerContributionCategories() {
        return employerContributionCategories;
    }

    public void setEmployerContributionCategories(ArrayList<PaymentDeductionObject> employerContributionCategories) {
        this.employerContributionCategories = employerContributionCategories;
    }

    public Integer getMonthDaysCount() {
        return monthDaysCount;
    }

    public void setMonthDaysCount(Integer monthDaysCount) {
        this.monthDaysCount = monthDaysCount;
    }

    public void setDriverID(Long driverID) {
        this.driverID = driverID;
    }

    public Long getDriverID() {
        return driverID;
    }

    public Boolean isCalculatePension() {
        return calculatePension != null ? calculatePension : Boolean.FALSE;
    }

    public void setCalculatePension(Boolean calculatePension) {
        this.calculatePension = calculatePension;
    }

    public Boolean isLocalEmployee() {
        return isLocalEmployee != null ? isLocalEmployee : Boolean.FALSE;
    }

    public void setLocalEmployee(Boolean localEmployee) {
        isLocalEmployee = localEmployee;
    }

    public Boolean isFromEndOfService() {
        return isFromEndOfService != null ? isFromEndOfService : Boolean.FALSE;
    }

    public void setFromEndOfService(Boolean fromEndOfService) {
        isFromEndOfService = fromEndOfService;
    }

    public PaymentDeductionObject getEmployeeExpenses() {
        return employeeExpenses;
    }

    public void setEmployeeExpenses(PaymentDeductionObject employeeExpenses) {
        this.employeeExpenses = employeeExpenses;
    }

    public PaymentDeductionObject getPetrolLimitExcess() {
        return petrolLimitExcess;
    }

    public void setPetrolLimitExcess(PaymentDeductionObject petrolLimitExcess) {
        this.petrolLimitExcess = petrolLimitExcess;
    }
    public SelectItem getProjectItem() {
        return projectItem;
    }

    public void setProjectItem(SelectItem projectItem) {
        this.projectItem = projectItem;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getStatusID() {
        return statusID;
    }

    public void setStatusID(Integer statusID) {
        this.statusID = statusID;
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

    public SelectItem getCreator() {
        return creator;
    }

    public void setCreator(SelectItem creator) {
        this.creator = creator;
    }

    public SelectItem getApprover() {
        return approver;
    }

    public void setApprover(SelectItem approver) {
        this.approver = approver;
    }

    public SelectItem getApprover2() {
        return approver2;
    }

    public void setApprover2(SelectItem approver2) {
        this.approver2 = approver2;
    }

    public ArrayList<SelectItem> getProjects() {
        return projects;
    }

    public void setProjects(ArrayList<SelectItem> projects) {
        this.projects = projects;
    }

    public Integer getMonthID() {
        return monthID;
    }

    public void setMonthID(Integer monthID) {
        this.monthID = monthID;
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

    public BigDecimal getNumberOfWorkDay() {
        return numberOfWorkDay;
    }

    public void setNumberOfWorkDay(BigDecimal numberOfWorkDay) {
        this.numberOfWorkDay = numberOfWorkDay;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getPaymentPolicy() {
        return paymentPolicy;
    }

    public void setPaymentPolicy(String paymentPolicy) {
        this.paymentPolicy = paymentPolicy;
    }

    public String getPayMethodName() {
        return payMethodName;
    }

    public void setPayMethodName(String payMethodName) {
        this.payMethodName = payMethodName;
    }

    public Integer getPayMethodId() {
        return payMethodId;
    }

    public void setPayMethodId(Integer payMethodId) {
        this.payMethodId = payMethodId;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public PayrollPdfTemplateList getPdfTemplateList() {
        return pdfTemplateList;
    }

    public void setPdfTemplateList(PayrollPdfTemplateList pdfTemplateList) {
        this.pdfTemplateList = pdfTemplateList;
    }

    public ArrayList<String> getPayedPayslipDataList() {
        return payedPayslipDataList;
    }

    public void setPayedPayslipDataList(ArrayList<String> payedPayslipDataList) {
        this.payedPayslipDataList = payedPayslipDataList;
    }

    public HashMap<String, String> getCustomFields() {
        if (customFields == null) {
            customFields = new HashMap<>();
        }
        return customFields;
    }

    public void setCustomFields(HashMap<String, String> customFields) {
        this.customFields = customFields;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }

    public void addCustomField(String key, String value) {
        getCustomFields().put(key, value);
    }

    public String getApprovalRejectionStatus() {
        return approvalRejectionStatus;
    }

    public void setApprovalRejectionStatus(String approvalRejectionStatus) {
        this.approvalRejectionStatus = approvalRejectionStatus;
    }

    public CurrencyItem[] getCurrencies() {
        return currencies;
    }

    public void setCurrencies(CurrencyItem[] currencies) {
        this.currencies = currencies;
    }

    public CurrencyItem getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyItem currency) {
        this.currency = currency;
    }

    public BigDecimal getTotalInBase() {
        return totalInBase;
    }

    public void setTotalInBase(BigDecimal totalInBase) {
        this.totalInBase = totalInBase;
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

    public Boolean getEnabledAccounting() {
        return enabledAccounting != null ? enabledAccounting : false;
    }

    public void setEnabledAccounting(Boolean enabledAccounting) {
        this.enabledAccounting = enabledAccounting;
    }

    public BigDecimal getRecurringsTotal() {
        return recurringsTotal;
    }

    public void setRecurringsTotal(BigDecimal recurringsTotal) {
        this.recurringsTotal = recurringsTotal;
    }

    public Integer getNonPaidLeaveDays() {
        return nonPaidLeaveDays != null ? nonPaidLeaveDays : 0;
    }

    public void setNonPaidLeaveDays(Integer nonPaidLeaveDays) {
        this.nonPaidLeaveDays = nonPaidLeaveDays;
    }

    public BigDecimal getMonthlySalik() {
        return monthlySalik;
    }

    public void setMonthlySalik(BigDecimal monthlySalik) {
        this.monthlySalik = monthlySalik;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    public SelectItem[] getPayMethods() {
        return payMethods;
    }

    public void setPayMethods(SelectItem[] payMethods) {
        this.payMethods = payMethods;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Double getRegularOvertimeRate() {
        return regularOvertimeRate;
    }

    public void setRegularOvertimeRate(Double regularOvertimeRate) {
        this.regularOvertimeRate = regularOvertimeRate;
    }

    public Double getWeekendOvertimeRate() {
        return weekendOvertimeRate;
    }

    public void setWeekendOvertimeRate(Double weekendOvertimeRate) {
        this.weekendOvertimeRate = weekendOvertimeRate;
    }

    public Double getHolidayOvertimeRate() {
        return holidayOvertimeRate;
    }

    public void setHolidayOvertimeRate(Double holidayOvertimeRate) {
        this.holidayOvertimeRate = holidayOvertimeRate;
    }

    public SinglePayrunItem getPrevMonthItem() {
        return prevMonthItem;
    }

    public void setPrevMonthItem(SinglePayrunItem prevMonthItem) {
        this.prevMonthItem = prevMonthItem;
    }

    public BigDecimal getGross() {
        return gross;
    }

    public void setGross(BigDecimal gross) {
        this.gross = gross;
    }

    public void setCustomFieldMap(HashMap<String, Object> customFieldMap) {
        this.customFieldMap = customFieldMap;
    }

    public HashMap<String, Object> getCustomFieldMap() {
        if (customFieldMap == null) {
            customFieldMap = new HashMap<>();
        }
        return customFieldMap;
    }

    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return getCustomFieldMap().get(columnCodeKey);
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {
        getCustomFieldMap().put(columnCodeKey, cellValue);
    }

    public PayrollTotalTO getTotalTO() {
        return totalTO;
    }

    public void setTotalTO(PayrollTotalTO totalTO) {
        this.totalTO = totalTO;
    }

    public BigDecimal getEmployerContribution() {
        return employerContribution;
    }

    public void setEmployerContribution(BigDecimal employerContribution) {
        this.employerContribution = employerContribution;
    }

    public List<PayrunPaymentItem> getPaymentItems() {
        return paymentItems;
    }

    public void setPaymentItems(List<PayrunPaymentItem> paymentItems) {
        this.paymentItems = paymentItems;
    }

    public Integer getProcessDateId() {
        return this.processDateId;
    }

    public void setProcessDateId(final Integer processDateId) {
        this.processDateId = processDateId;
    }

    public Integer getBasicSalaryId() {
        return this.basicSalaryId;
    }

    public void setBasicSalaryId(final Integer basicSalaryId) {
        this.basicSalaryId = basicSalaryId;
    }

    public Integer getJournalId() {
        return journalId;
    }

    public void setJournalId(Integer journalId) {
        this.journalId = journalId;
    }
}
