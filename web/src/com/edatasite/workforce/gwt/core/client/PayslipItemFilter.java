package com.edatasite.workforce.gwt.core.client;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.SalaryHistory;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayslipFilter;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 19.08.14
 * Time: 0:17
 * To change this template use File | Settings | File Templates.
 */
public class PayslipItemFilter implements IsSerializable, Serializable {

    protected Integer objectID;
    protected Integer employeeID;
    protected String employeeName;

    protected Integer payslipTableId;
    protected String status;
    protected String periodChecker;
    protected DateNonConvertable fromDate;
    protected DateNonConvertable toDate;
    protected DateNonConvertable processDate;
    protected Integer daysOfMonth;
    protected Integer month;
    protected Integer year;
    protected Integer projectId;
    protected boolean fromChangeHandler;
    protected boolean fromView;
    protected boolean calculateBasicSalaryFromProject;
    protected boolean fromNexMonth;
    protected boolean empCodeAdjoined = true;
    protected boolean calculatePension = false;

    protected Integer countryId;
    protected String countryCode;

    protected Integer baseCurrencyId;
    protected boolean isEmployeeCodeInteger;

    protected boolean isLeaveSettingsCalculationEnabled;
    protected PaymentDeductionSelectItem leaveMTCategoryItem;
    protected List<PaymentDeductionObject> leaveDeductionLinkedCategories;
    protected List<PaymentDeductionObject> leaveDailyTypeLinkedCategories;
    protected List<PaymentDeductionObject> leaveMoneyTypeLinkedCategories;

    protected List<PaymentDeductionObject> paymentDeductions;
    protected Map<String, String> employeeSettingsMap;
    protected Integer lastYearMinutes;

    private Double[] spentMinutes;
    private Map<String, PaymentDeductionSelectItem> categoryMap;
    private Map<String, String> companyPayrollSettingsMap;
    private List<SalaryHistory> salaryHistories;

    private BigDecimal mrotValue;

    public static PayslipItemFilter fromPayslipFilter(PayslipFilter payslipFilter) {
        PayslipItemFilter filter = new PayslipItemFilter();
        filter.setFromDate(payslipFilter.getFromDate());
        filter.setToDate(payslipFilter.getToDate());
        filter.setDaysOfMonth(payslipFilter.getDaysOfMonth());
        filter.setMonth(payslipFilter.getMonth());
        filter.setYear(payslipFilter.getYear());
        filter.setEmpCodeAdjoined(payslipFilter.isEmpCodeAdjoined());
        filter.setCalculatePension(payslipFilter.isCalculatePension());
        filter.setProjectId(payslipFilter.getProjectId());

        return filter;
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

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Integer getPayslipTableId() {
        return payslipTableId;
    }

    public void setPayslipTableId(Integer payslipTableId) {
        this.payslipTableId = payslipTableId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public Integer getDaysOfMonth() {
        return daysOfMonth;
    }

    public void setDaysOfMonth(Integer daysOfMonth) {
        this.daysOfMonth = daysOfMonth;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getPeriodChecker() {
        return periodChecker;
    }

    public void setPeriodChecker(String periodChecker) {
        this.periodChecker = periodChecker;
    }

    public boolean isFromChangeHandler() {
        return fromChangeHandler;
    }

    public void setFromChangeHandler(boolean fromChangeHandler) {
        this.fromChangeHandler = fromChangeHandler;
    }

    public boolean isFromView() {
        return fromView;
    }

    public void setFromView(boolean fromView) {
        this.fromView = fromView;
    }

    public boolean isCalculateBasicSalaryFromProject() {
        return calculateBasicSalaryFromProject;
    }

    public void setCalculateBasicSalaryFromProject(boolean calculateBasicSalaryFromProject) {
        this.calculateBasicSalaryFromProject = calculateBasicSalaryFromProject;
    }

    public boolean isFromNexMonth() {
        return fromNexMonth;
    }

    public void setFromNexMonth(boolean fromNexMonth) {
        this.fromNexMonth = fromNexMonth;
    }

    public boolean isEmpCodeAdjoined() {
        return empCodeAdjoined;
    }

    public void setEmpCodeAdjoined(boolean empCodeAdjoined) {
        this.empCodeAdjoined = empCodeAdjoined;
    }

    public boolean isCalculatePension() {
        return calculatePension;
    }

    public void setCalculatePension(boolean calculatePension) {
        this.calculatePension = calculatePension;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public Integer getBaseCurrencyId() {
        return baseCurrencyId;
    }

    public void setBaseCurrencyId(Integer baseCurrencyId) {
        this.baseCurrencyId = baseCurrencyId;
    }

    public boolean isEmployeeCodeInteger() {
        return isEmployeeCodeInteger;
    }

    public void setEmployeeCodeInteger(boolean employeeCodeInteger) {
        isEmployeeCodeInteger = employeeCodeInteger;
    }

    public boolean isLeaveSettingsCalculationEnabled() {
        return isLeaveSettingsCalculationEnabled;
    }

    public void setLeaveSettingsCalculationEnabled(boolean leaveSettingsCalculationEnabled) {
        isLeaveSettingsCalculationEnabled = leaveSettingsCalculationEnabled;
    }

    public PaymentDeductionSelectItem getLeaveMTCategoryItem() {
        return leaveMTCategoryItem;
    }

    public void setLeaveMTCategoryItem(PaymentDeductionSelectItem leaveMTCategoryItem) {
        this.leaveMTCategoryItem = leaveMTCategoryItem;
    }

    public List<PaymentDeductionObject> getLeaveDeductionLinkedCategories() {
        return leaveDeductionLinkedCategories;
    }

    public void setLeaveDeductionLinkedCategories(List<PaymentDeductionObject> leaveDeductionLinkedCategories) {
        this.leaveDeductionLinkedCategories = leaveDeductionLinkedCategories;
    }

    public List<PaymentDeductionObject> getLeaveDailyTypeLinkedCategories() {
        return leaveDailyTypeLinkedCategories;
    }

    public void setLeaveDailyTypeLinkedCategories(List<PaymentDeductionObject> leaveDailyTypeLinkedCategories) {
        this.leaveDailyTypeLinkedCategories = leaveDailyTypeLinkedCategories;
    }

    public List<PaymentDeductionObject> getLeaveMoneyTypeLinkedCategories() {
        return leaveMoneyTypeLinkedCategories;
    }

    public void setLeaveMoneyTypeLinkedCategories(List<PaymentDeductionObject> leaveMoneyTypeLinkedCategories) {
        this.leaveMoneyTypeLinkedCategories = leaveMoneyTypeLinkedCategories;
    }

    public List<PaymentDeductionObject> getPaymentDeductions() {
        return paymentDeductions;
    }

    public void setPaymentDeductions(List<PaymentDeductionObject> paymentDeductions) {
        this.paymentDeductions = paymentDeductions;
    }

    public Map<String, String> getEmployeeSettingsMap() {
        return employeeSettingsMap;
    }

    public void setEmployeeSettingsMap(Map<String, String> employeeSettingsMap) {
        this.employeeSettingsMap = employeeSettingsMap;
    }

    public Integer getLastYearMinutes() {
        return lastYearMinutes;
    }

    public void setLastYearMinutes(Integer lastYearMinutes) {
        this.lastYearMinutes = lastYearMinutes;
    }

    public Double[] getSpentMinutes() {
        return spentMinutes;
    }

    public void setSpentMinutes(Double[] spentMinutes) {
        this.spentMinutes = spentMinutes;
    }

    public PaymentDeductionSelectItem getCategory(String code) {
        return categoryMap.get(code);
    }

    public void setCategoryMap(Map<String, PaymentDeductionSelectItem> categoryMap) {
        this.categoryMap = categoryMap;
    }

    public String getCompanyPayrollSettings(String settingsCode) {
        return companyPayrollSettingsMap.get(settingsCode);
    }

    public String getCompanyPayrollSettingsOrDefault(String settingsCode, String defaultValue) {
        String value =  companyPayrollSettingsMap.get(settingsCode);
        return value != null && !value.isEmpty() ? value : defaultValue;
    }

    public void setCompanyPayrollSettingsMap(Map<String, String> companyPayrollSettingsMap) {
        this.companyPayrollSettingsMap = companyPayrollSettingsMap;
    }

    public List<SalaryHistory> getSalaryHistories() {
        return salaryHistories;
    }

    public void setSalaryHistories(List<SalaryHistory> salaryHistories) {
        this.salaryHistories = salaryHistories;
    }

    public BigDecimal getMrotValue() {
        return mrotValue;
    }

    public void setMrotValue(BigDecimal mrotValue) {
        this.mrotValue = mrotValue;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }
}
