package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.payroll.DailyOvertimeData;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.MonthlyOvertimeDataWithRates;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 07.04.14
 * Time: 20:07
 * To change this template use File | Settings | File Templates.
 */
public class PayslipTableRequestObject extends RequestObject {

    private String creator;
    private String approver;
    private String month;
    private Date createdDate;
    private Date approveDate;
    private Date fromDate;
    private Date toDate;
    private Date processDate;
    private String rejectionNote;
    private String description;
    private String payMethod;
    private LinkedList<PaymentDeductionObject> allPaymentCategories;
    private LinkedList<PaymentDeductionObject> allDeductionCategories;
    private LinkedHashMap<Integer, PaymentDeductionObject> allPaymentCategoriesYTD;
    private LinkedHashMap<Integer, PaymentDeductionObject> allDeductionCategoriesYTD;
    private PaymentDeductionObject employeeExpenses;
    private HashMap<Date, DailyOvertimeData> dailyOvertimeData;
    private MonthlyOvertimeDataWithRates overtimeDataWithRates;

    private Integer employeeId;
    private Integer monthId;
    private Integer year;
    private Integer pdfTemplateID;

    private String employeeName;
    private String employeeCode;
    private String employeeDepartment;
    private String employeePosition;
    private String employeePaymentType;
    private String iBanCode;
    private String bankAccountNumber;
    private String workedDays;
    private BigDecimal workDays;
    private String employeeLocation;
    private String employeeAddress;
    private Date employeeHireDate;
    private Date resignationDate;
    private BigDecimal basicSalary;
    private BigDecimal dailyRate;
    private BigDecimal actualMonthPay;
    private BigDecimal allowance;
    private BigDecimal additionalPay;
    private BigDecimal deduction;
    private BigDecimal tax;
    private BigDecimal expense;
    private String paymentPolicy;
    private BigDecimal total;
    private BigDecimal totalInBase;
    private BigDecimal totalPayToDate;
    private BigDecimal pensionAmount;
    private String currency;
    private BigDecimal exchangeRate;
    private HashMap<String, String> customFields;
    private BigDecimal comission;
    private BigDecimal monthlyCollection;
    private BigDecimal spentFlueAmount;
    private BigDecimal monthlySalik;
    private Long driverNumber;
    private BigDecimal allowanceAndBasicSalaryTotal;

    public PayslipTableRequestObject() {
        super();
    }

    public PayslipTableRequestObject(Integer objectID) {
        super(objectID);
    }


    @Override
    public HashMap<String, String> getRequestParams() {
        final HashMap<String, String> parametersMap = new HashMap<>();
        parametersMap.put("objectID", getObjectID() == null ? "" : getObjectID().toString());
        parametersMap.put("employeeName", getEmployeeName());
        parametersMap.put("employeeCode", getEmployeeCode());
        parametersMap.put("year", getYear() != null ? getYear().toString() : "");
        parametersMap.put("month", getMonth());
        parametersMap.put("pdfTemplateID", getPdfTemplateID() != null ? getPdfTemplateID().toString() : "");

        return parametersMap;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Integer getMonthId() {
        return monthId;
    }

    public void setMonthId(Integer monthId) {
        this.monthId = monthId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getProcessDate() {
        return processDate;
    }

    public void setProcessDate(Date processDate) {
        this.processDate = processDate;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmployeeDepartment() {
        return employeeDepartment;
    }

    public void setEmployeeDepartment(String employeeDepartment) {
        this.employeeDepartment = employeeDepartment;
    }

    public String getEmployeePosition() {
        return employeePosition;
    }

    public void setEmployeePosition(String employeePosition) {
        this.employeePosition = employeePosition;
    }

    public String getEmployeePaymentType() {
        return employeePaymentType;
    }

    public void setEmployeePaymentType(String employeePaymentType) {
        this.employeePaymentType = employeePaymentType;
    }

    public String getWorkedDays() {
        return workedDays;
    }

    public void setWorkedDays(String workedDays) {
        this.workedDays = workedDays;
    }

    public BigDecimal getWorkDays() {
        return workDays;
    }

    public void setWorkDays(BigDecimal workDays) {
        this.workDays = workDays;
    }

    public BigDecimal getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(BigDecimal basicSalary) {
        this.basicSalary = basicSalary;
    }

    public BigDecimal getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(BigDecimal dailyRate) {
        this.dailyRate = dailyRate;
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

    public BigDecimal getExpense() {
        return expense;
    }

    public void setExpense(BigDecimal expense) {
        this.expense = expense;
    }

    public String getPaymentPolicy() {
        return paymentPolicy;
    }

    public void setPaymentPolicy(String paymentPolicy) {
        this.paymentPolicy = paymentPolicy;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getRejectionNote() {
        return rejectionNote;
    }

    public void setRejectionNote(String rejectionNote) {
        this.rejectionNote = rejectionNote;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public void addCustomField(String key, String value) {
        getCustomFields().put(key, value);
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public LinkedList<PaymentDeductionObject> getAllPaymentCategories() {
        if (allPaymentCategories == null) {
            allPaymentCategories = new LinkedList<>();
        }
        return allPaymentCategories;
    }

    public void setAllPaymentCategories(LinkedList<PaymentDeductionObject> allPaymentCategories) {
        this.allPaymentCategories = allPaymentCategories;
    }

    public LinkedList<PaymentDeductionObject> getAllDeductionCategories() {
        if (allDeductionCategories == null) {
            allDeductionCategories = new LinkedList<>();
        }
        return allDeductionCategories;
    }

    public void setAllDeductionCategories(LinkedList<PaymentDeductionObject> allDeductionCategories) {
        this.allDeductionCategories = allDeductionCategories;
    }

    public LinkedHashMap<Integer, PaymentDeductionObject> getAllPaymentCategoriesYTD() {
        if (allPaymentCategoriesYTD == null) {
            allPaymentCategoriesYTD = new LinkedHashMap<>();
        }
        return allPaymentCategoriesYTD;
    }

    public void setAllPaymentCategoriesYTD(LinkedHashMap<Integer, PaymentDeductionObject> allPaymentCategoriesYTD) {
        this.allPaymentCategoriesYTD = allPaymentCategoriesYTD;
    }

    public LinkedHashMap<Integer, PaymentDeductionObject> getAllDeductionCategoriesYTD() {
        if (allDeductionCategoriesYTD == null) {
            allDeductionCategoriesYTD = new LinkedHashMap<>();
        }
        return allDeductionCategoriesYTD;
    }

    public void setAllDeductionCategoriesYTD(LinkedHashMap<Integer, PaymentDeductionObject> allDeductionCategoriesYTD) {
        this.allDeductionCategoriesYTD = allDeductionCategoriesYTD;
    }

    public PaymentDeductionObject getEmployeeExpenses() {
        return employeeExpenses;
    }

    public void setEmployeeExpenses(PaymentDeductionObject employeeExpenses) {
        this.employeeExpenses = employeeExpenses;
    }

    public HashMap<Date, DailyOvertimeData> getDailyOvertimeData() {
        if (dailyOvertimeData == null) {
            dailyOvertimeData = new HashMap<>();
        }
        return dailyOvertimeData;
    }

    public void setDailyOvertimeData(HashMap<Date, DailyOvertimeData> dailyOvertimeData) {
        this.dailyOvertimeData = dailyOvertimeData;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getPeriod() {
        return getYear() != null ? getMonth().concat(", ").concat(getYear().toString()) : getMonth();
    }

    public Integer getPdfTemplateID() {
        return pdfTemplateID;
    }

    public void setPdfTemplateID(Integer pdfTemplateID) {
        this.pdfTemplateID = pdfTemplateID;
    }

    public BigDecimal getTotalPayToDate() {
        return totalPayToDate;
    }

    public void setTotalPayToDate(BigDecimal totalPayToDate) {
        this.totalPayToDate = totalPayToDate;
    }

    public BigDecimal getPensionAmount() {
        return pensionAmount;
    }

    public void setPensionAmount(BigDecimal pensionAmount) {
        this.pensionAmount = pensionAmount;
    }

    public String getEmployeeLocation() {
        return employeeLocation;
    }

    public void setEmployeeLocation(String employeeLocation) {
        this.employeeLocation = employeeLocation;
    }

    public Date getEmployeeHireDate() {
        return employeeHireDate;
    }

    public void setEmployeeHireDate(Date employeeHireDate) {
        this.employeeHireDate = employeeHireDate;
    }

    public Date getResignationDate() {
        return resignationDate;
    }

    public void setResignationDate(Date resignationDate) {
        this.resignationDate = resignationDate;
    }

    public MonthlyOvertimeDataWithRates getOvertimeDataWithRates() {
        return overtimeDataWithRates;
    }

    public void setOvertimeDataWithRates(MonthlyOvertimeDataWithRates overtimeDataWithRates) {
        this.overtimeDataWithRates = overtimeDataWithRates;
    }

    public String getiBanCode() {
        return iBanCode;
    }

    public void setiBanCode(String iBanCode) {
        this.iBanCode = iBanCode;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getEmployeeAddress() {
        return employeeAddress;
    }

    public void setEmployeeAddress(String employeeAddress) {
        this.employeeAddress = employeeAddress;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
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

    public void setComission(BigDecimal comission) {
        this.comission = comission;
    }

    public BigDecimal getComission() {
        return comission;
    }

    public void setMonthlyCollection(BigDecimal monthlyCollection) {
        this.monthlyCollection = monthlyCollection;
    }

    public BigDecimal getMonthlyCollection() {
        return monthlyCollection;
    }

    public void setSpentFlueAmount(BigDecimal spentFlueAmount) {
        this.spentFlueAmount = spentFlueAmount;
    }

    public BigDecimal getSpentFlueAmount() {
        return spentFlueAmount;
    }

    public void setMonthlySalik(BigDecimal monthlySalik) {
        this.monthlySalik = monthlySalik;
    }

    public BigDecimal getMonthlySalik() {
        return monthlySalik;
    }

    public Long getDriverNumber() {
        return driverNumber;
    }

    public void setDriverNumber(Long driverNumber) {
        this.driverNumber = driverNumber;
    }

    public BigDecimal getAllowanceAndBasicSalaryTotal() {
        return allowanceAndBasicSalaryTotal;
    }

    public void setAllowanceAndBasicSalaryTotal(BigDecimal allowanceAndBasicSalaryTotal) {
        this.allowanceAndBasicSalaryTotal = allowanceAndBasicSalaryTotal;
    }
}
