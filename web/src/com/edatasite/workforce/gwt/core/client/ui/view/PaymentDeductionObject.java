package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollCategory;
import com.edatasite.workforce.gwt.core.client.enums.EPPaymentType;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.ExpenseData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: 22.02.2009
 * Time: 19:51:42
 * To change this template use File | Settings | File Templates.
 */
public class PaymentDeductionObject implements IsSerializable, Serializable {

    public static final String OBJECT_ID = "objectId";
    public static final String EMPLOYEE_ID = "employeeId";
    public static final String EMPLOYEE_NAME = "employeeName";
    public static final String EMPLOYEE_CODE = "employeeCode";
    public static final String AMOUNT = "amount";
    public static final String PERCENTAGE = "percentage";
    public static final String CATEGORY = "category";
    public static final String ADDITIONAL_PAYMENT_DATE = "paymentDate";
    public static final String EMPLOYEE_CONTRIBUTION = "contribution";
    public static final String TAX = "tax";
    public static final String DEDUCTION = "deduction";
    public static final String TOTAL_AMOUNT = "taxAmount";
    private HashMap<String, String> valuesMap;

    private Integer id;
    private int year;
    private int payPeriod;
    private int frequency;
    private int numberOfPeriods;
    private Integer categoryid;
    private String categoryname;
    private String categorycode;
    private String categorytype;
    private SelectItem employee;
    private Integer employeeTemplateID;
    private Integer cashadvanceid;
    private Integer psdId;
    private Integer type;
    private Integer leaveType;
    private Integer[] relatedIDs;
    private BigDecimal percentage;
    private BigDecimal commission;
    private BigDecimal amount;
    private BigDecimal paymentAmount;
    private BigDecimal basicSalaryPartAmount;
    private BigDecimal additionalRateAmount;
    private BigDecimal totalAmount;
    private BigDecimal remainingAmount;
    private BigDecimal numberOfWorkDays;
    private BigDecimal leaveDaysCount;
    private DateNonConvertable starttDate;
    private DateNonConvertable enddDate;
    private Date paymentdate;
    private DateNonConvertable additionalPaymentDate;
    private Date startdate;
    private Date enddate;
    private boolean paymentCategory;
    private boolean deductionCategory;
    private boolean taxCategory;
    private boolean employerContributionCategory;
    private boolean materialAidCategory;
    private boolean loan;
    private boolean isUsed;
    private boolean issalaryobject;
    private boolean isDeleted;
    private boolean isNotEditable;
    private boolean isfromallallowances;
    private boolean isCashAdvance;
    private String statusName;
    private ExpenseData[] expenses;
    private PaymentDeductionSelectItem categoryItem;
    private LeavePaymentItem leavePaymentItem;
    private List<PaymentDeductionObject> linkedCategories;
    private String remarks;
    private ArrayList<Integer> sickRequestids;
    private boolean additionalPayment = false;
    private Integer payslipCategoryDays;
    private Integer payslipCategoryMinutes;
    private EPPaymentType paymentType;
    private String reference;
    private BigDecimal employeeBasicSalary;
    private BigDecimal basicPlusAllowance;
    private ArrayList<CompanyCustomFieldItem> itemCustomFields;

    private Integer settingItemId;
    private Integer countIncident;
    private BigDecimal empMode;
    private BigDecimal employerContribution;
    private BigDecimal tax;
    private BigDecimal deduction;
    private List<PaymentDeductionObject> taxCategories = new ArrayList<>();
    private List<PaymentDeductionObject> allTaxCategories = new ArrayList<>();
    private List<PaymentDeductionObject> employerContributionCategories = new ArrayList<>();
    private List<PaymentDeductionObject> allEmployerContributionCategories = new ArrayList<>();
    private List<PaymentDeductionObject> deductionCategories = new ArrayList<>();
    private List<PaymentDeductionObject> allDeductionCategories = new ArrayList<>();

    private HashMap<String, BigDecimal> lgotaBalanceMap = new HashMap<>();

    public Integer getCountIncident() {
        return countIncident;
    }

    public void setCountIncident(Integer countIncident) {
        this.countIncident = countIncident;
    }

    public PaymentDeductionObject() {
    }

    public PaymentDeductionObject(PaymentDeductionSelectItem categoryItem) {
        this.categoryItem = categoryItem;
    }

    public PaymentDeductionObject(PaymentDeductionSelectItem categoryItem, BigDecimal paymentAmount) {
        this.categoryItem = categoryItem;
        this.paymentAmount = paymentAmount;
    }

    public PaymentDeductionObject(Integer id, Integer employeeId, BigDecimal paymentAmount, Integer payType, BigDecimal percentage, BigDecimal totalAmount, DateNonConvertable startDate, DateNonConvertable endDate, PaymentDeductionSelectItem categoryItem) {
        this.id = id;
        if (employeeId != null) {
            this.employee = new SelectItem(employeeId);
        }
        this.paymentAmount = paymentAmount;
        this.percentage = percentage;
        this.type = payType;
        this.totalAmount = totalAmount;
        this.starttDate = startDate;
        this.enddDate = endDate;
        if (categoryItem != null) {
            this.paymentCategory = EdsPayrollCategory.PAYMENT.equals(categoryItem.getType());
            this.deductionCategory = EdsPayrollCategory.DEDUCTION.equals(categoryItem.getType());
            this.taxCategory = EdsPayrollCategory.TAX.equals(categoryItem.getType());
            this.employerContributionCategory = EdsPayrollCategory.EMPLOYER_CONTRIBUTION.equals(categoryItem.getType());
        }
        this.categoryItem = categoryItem;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCategoryid(Integer categoryid) {
        this.categoryid = categoryid;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategorycode(String categorycode) {
        this.categorycode = categorycode;
    }

    public void setCategorytype(String categorytype) {
        this.categorytype = categorytype;
    }

    public void setCashadvanceid(Integer cashadvanceid) {
        this.cashadvanceid = cashadvanceid;
    }

    public Integer getPsdId() {
        return this.psdId;
    }

    public void setPsdId(final Integer psdId) {
        this.psdId = psdId;
    }

    public void setPaymentdate(Date paymentdate) {
        this.paymentdate = paymentdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public void setIssalaryobject(boolean issalaryobject) {
        this.issalaryobject = issalaryobject;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public void setIsfromallallowances(boolean isfromallallowances) {
        this.isfromallallowances = isfromallallowances;
    }

    public boolean isCashAdvance() {
        return getCashAdvanceID() != null;
    }

    public PaymentDeductionSelectItem getCategoryItem() {
        return categoryItem;
    }

    public void setCategoryItem(PaymentDeductionSelectItem categoryItem) {
        this.categoryItem = categoryItem;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public BigDecimal getBasicSalaryPartAmount() {
        return this.basicSalaryPartAmount;
    }

    public void setBasicSalaryPartAmount(final BigDecimal basicSalaryPartAmount) {
        this.basicSalaryPartAmount = basicSalaryPartAmount;
    }

    public BigDecimal getAdditionalRateAmount() {
        return additionalRateAmount;
    }

    public void setAdditionalRateAmount(BigDecimal additionalRateAmount) {
        this.additionalRateAmount = additionalRateAmount;
    }

    public Date getPaymentDate() {
        return paymentdate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentdate = paymentDate;
    }

    public DateNonConvertable getAdditionalPaymentDate() {
        return this.additionalPaymentDate;
    }

    public void setAdditionalPaymentDate(final DateNonConvertable additionalPaymentDate) {
        this.additionalPaymentDate = additionalPaymentDate;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public SelectItem getEmployee() {
        return employee;
    }

    public void setEmployee(SelectItem employee) {
        this.employee = employee;
    }

    public BigDecimal getNumberOfWorkDays() {
        return numberOfWorkDays;
    }

    public void setNumberOfWorkDays(BigDecimal numberOfWorkDays) {
        this.numberOfWorkDays = numberOfWorkDays;
    }

    public BigDecimal getLeaveDaysCount() {
        return leaveDaysCount;
    }

    public void setLeaveDaysCount(BigDecimal leaveDaysCount) {
        this.leaveDaysCount = leaveDaysCount;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getPayPeriod() {
        return payPeriod;
    }

    public void setPayPeriod(int payPeriod) {
        this.payPeriod = payPeriod;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(Integer leaveType) {
        this.leaveType = leaveType;
    }

    public ExpenseData[] getExpenses() {
        return expenses;
    }

    public void setExpenses(ExpenseData[] expenses) {
        this.expenses = expenses;
    }

    public Integer[] getRelatedIDs() {
        return relatedIDs;
    }

    public void setRelatedIDs(Integer[] relatedIDs) {
        this.relatedIDs = relatedIDs;
    }

    public DateNonConvertable getStarttDate() {
        return starttDate;
    }

    public void setStarttDate(DateNonConvertable startDate) {
        this.starttDate = startDate;
    }

    public DateNonConvertable getEnddDate() {
        return enddDate;
    }

    public void setEnddDate(DateNonConvertable endDate) {
        this.enddDate = endDate;
    }

    public int getNumberOfPeriods() {
        return numberOfPeriods;
    }

    public void setNumberOfPeriods(int numberOfPeriods) {
        this.numberOfPeriods = numberOfPeriods;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public boolean isPaymentCategory() {
        return paymentCategory;
    }

    public void setPaymentCategory(boolean paymentCategory) {
        this.paymentCategory = paymentCategory;
    }

    public boolean isLoan() {
        return loan;
    }

    public void setLoan(boolean loan) {
        this.loan = loan;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public boolean isSalaryObject() {
        return issalaryobject;
    }

    public void setSalaryObject(boolean salaryObject) {
        issalaryobject = salaryObject;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public boolean isNotEditable() {
        return isNotEditable;
    }

    public void setNotEditable(boolean notEditable) {
        isNotEditable = notEditable;
    }

    public boolean isFromAllAllowances() {
        return isfromallallowances;
    }

    public void setFromAllAllowances(boolean fromAllAllowances) {
        isfromallallowances = fromAllAllowances;
    }

    public Integer getCashAdvanceID() {
        return cashadvanceid;
    }

    public void setCashAdvanceID(Integer cashAdvanceID) {
        this.cashadvanceid = cashAdvanceID;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LeavePaymentItem getLeavePaymentItem() {
        return leavePaymentItem;
    }

    public void setLeavePaymentItem(LeavePaymentItem leavePaymentItem) {
        this.leavePaymentItem = leavePaymentItem;
    }

    public ArrayList<Integer> getSickRequestids() {
        if (sickRequestids == null) {
            sickRequestids = new ArrayList<>();
        }
        return sickRequestids;
    }

    public void setSickRequestids(ArrayList<Integer> sickRequestids) {
        this.sickRequestids = sickRequestids;
    }

    public List<PaymentDeductionObject> getLinkedCategories() {
        if (linkedCategories == null) {
            linkedCategories = new ArrayList<>();
        }
        return linkedCategories;
    }

    public void setLinkedCategories(List<PaymentDeductionObject> linkedCategories) {
        this.linkedCategories = linkedCategories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PaymentDeductionObject that = (PaymentDeductionObject) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return employee != null ? employee.equals(that.employee) : that.employee == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (employee != null ? employee.hashCode() : 0);
        return result;
    }

    public void setAdditionalPayment(boolean additionalPayment) {
        this.additionalPayment = additionalPayment;
    }

    public boolean isAdditionalPayment() {
        return additionalPayment;
    }

    public Integer getPayslipCategoryDays() {
        return payslipCategoryDays;
    }

    public void setPayslipCategoryDays(Integer payslipCategoryDays) {
        this.payslipCategoryDays = payslipCategoryDays;
    }

    public Integer getPayslipCategoryMinutes() {
        return payslipCategoryMinutes;
    }

    public void setPayslipCategoryMinutes(Integer payslipCategoryMinutes) {
        this.payslipCategoryMinutes = payslipCategoryMinutes;
    }

    public EPPaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(EPPaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public boolean isDeductionCategory() {
        return deductionCategory;
    }

    public void setDeductionCategory(boolean deductionCategory) {
        this.deductionCategory = deductionCategory;
    }

    public boolean isTaxCategory() {
        return taxCategory;
    }

    public void setTaxCategory(boolean taxCategory) {
        this.taxCategory = taxCategory;
    }

    public boolean isEmployerContributionCategory() {
        return employerContributionCategory;
    }

    public void setEmployerContributionCategory(boolean employerContributionCategory) {
        this.employerContributionCategory = employerContributionCategory;
    }

    public boolean isMaterialAidCategory() {
        return materialAidCategory;
    }

    public void setMaterialAidCategory(boolean materialAidCategory) {
        this.materialAidCategory = materialAidCategory;
    }

    public void setEmployeeBasicSalary(BigDecimal salaryAmount) {
        this.employeeBasicSalary = salaryAmount;
    }

    public BigDecimal getEmployeeBasicSalary() {
        return employeeBasicSalary;
    }

    public BigDecimal getBasicPlusAllowance() {
        return basicPlusAllowance;
    }

    public void setBasicPlusAllowance(BigDecimal basicPlusAllowance) {
        this.basicPlusAllowance = basicPlusAllowance;
    }

    public Map<String, CompanyCustomFieldItem> getCustomFieldValuesAsMap() {
        if (getItemCustomFields() != null) {
            Map<String, CompanyCustomFieldItem> map = new LinkedHashMap<>();
            for (CompanyCustomFieldItem item : getItemCustomFields()) {
                map.put(item.getColumnCode(), item);
            }
            return map;
        }
        return null;
    }

    public ArrayList<CompanyCustomFieldItem> getItemCustomFields() {
        return itemCustomFields;
    }

    public void setItemCustomFields(ArrayList<CompanyCustomFieldItem> itemCustomFields) {
        this.itemCustomFields = itemCustomFields;
    }

    public Integer getSettingItemId() {
        return settingItemId;
    }

    public void setSettingItemId(Integer settingItemId) {
        this.settingItemId = settingItemId;
    }

    public BigDecimal getEmpMode() {
        return this.empMode;
    }

    public void setEmpMode(final BigDecimal empMode) {
        this.empMode = empMode;
    }

    public BigDecimal getEmployerContribution() {
        return this.employerContribution;
    }

    public void setEmployerContribution(final BigDecimal employerContribution) {
        this.employerContribution = employerContribution;
    }

    public BigDecimal getTax() {
        return this.tax;
    }

    public void setTax(final BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getDeduction() {
        return deduction;
    }

    public void setDeduction(BigDecimal deduction) {
        this.deduction = deduction;
    }

    public List<PaymentDeductionObject> getTaxCategories() {
        return this.taxCategories;
    }

    public void setTaxCategories(final List<PaymentDeductionObject> taxCategories) {
        this.taxCategories = taxCategories;
    }

    public List<PaymentDeductionObject> getAllTaxCategories() {
        return this.allTaxCategories;
    }

    public void setAllTaxCategories(final List<PaymentDeductionObject> allTaxCategories) {
        this.allTaxCategories = allTaxCategories;
    }

    public List<PaymentDeductionObject> getAllEmployerContributionCategories() {
        return this.allEmployerContributionCategories;
    }

    public void setAllEmployerContributionCategories(final List<PaymentDeductionObject> allEmployerContributionCategories) {
        this.allEmployerContributionCategories = allEmployerContributionCategories;
    }

    public List<PaymentDeductionObject> getEmployerContributionCategories() {
        return this.employerContributionCategories;
    }

    public void setEmployerContributionCategories(final List<PaymentDeductionObject> employerContributionCategories) {
        this.employerContributionCategories = employerContributionCategories;
    }

    public List<PaymentDeductionObject> getDeductionCategories() {
        return deductionCategories;
    }

    public void setDeductionCategories(List<PaymentDeductionObject> deductionCategories) {
        this.deductionCategories = deductionCategories;
    }

    public List<PaymentDeductionObject> getAllDeductionCategories() {
        return allDeductionCategories;
    }

    public void setAllDeductionCategories(List<PaymentDeductionObject> allDeductionCategories) {
        this.allDeductionCategories = allDeductionCategories;
    }

    public HashMap<String, BigDecimal> getLgotaBalanceMap() {
        return lgotaBalanceMap;
    }

    public void setLgotaBalanceMap(HashMap<String, BigDecimal> lgotaBalanceMap) {
        this.lgotaBalanceMap = lgotaBalanceMap;
    }

    public HashMap<String, String> getExcelValuesAsMap() {
        valuesMap = new HashMap<>();
        valuesMap.put(EMPLOYEE_NAME, getEmployee() != null ? getEmployee().getName() : "");
        valuesMap.put(EMPLOYEE_CODE, getEmployee() != null ? getEmployee().getDescription() : "");
        valuesMap.put(AMOUNT, getPaymentAmount() != null ? getPaymentAmount().toString() : BigDecimal.ZERO.toString());
        valuesMap.put(PERCENTAGE, getPercentage() != null ? getPercentage().toString() : BigDecimal.ZERO.toString());
        valuesMap.put(CATEGORY, getCategoryItem() != null ? getCategoryItem().getName() : "");
        valuesMap.put(ADDITIONAL_PAYMENT_DATE, getAdditionalPaymentDate() != null ? String.valueOf(getAdditionalPaymentDate().getDateLong()) : "");
        valuesMap.put(EMPLOYEE_CONTRIBUTION, getEmployerContribution() != null ? getEmployerContribution().toString() : BigDecimal.ZERO.toString());
        valuesMap.put(TAX, getTax() != null ? getTax().toString() : BigDecimal.ZERO.toString());
        valuesMap.put(DEDUCTION, getDeduction() != null ? getDeduction().toString() : BigDecimal.ZERO.toString());
        valuesMap.put(TOTAL_AMOUNT, getTotalAmount() != null ? getTotalAmount().toString() : BigDecimal.ZERO.toString());
        valuesMap.put(OBJECT_ID, String.valueOf(getId()));
        valuesMap.put(EMPLOYEE_ID, String.valueOf(getEmployee() != null ? getEmployee().getId() : null));
        return valuesMap;
    }

    public void setMapValuesToObject(HashMap<String, String> valueMap) {
        this.valuesMap = valueMap;
        setId(getIntegerValueFromString(OBJECT_ID));
        setEmployee(new SelectItem(getIntegerValueFromString(EMPLOYEE_ID), valuesMap.get(EMPLOYEE_NAME), valuesMap.get(EMPLOYEE_CODE)));
        setPaymentAmount(getBigDecimalValueFromString(AMOUNT));
        setAmount(getBigDecimalValueFromString(AMOUNT));
        setPercentage(getBigDecimalValueFromString(PERCENTAGE));
        setCategoryItem(new PaymentDeductionSelectItem(1, valuesMap.get(CATEGORY), "", ""));
        setAdditionalPaymentDate(valuesMap.get(ADDITIONAL_PAYMENT_DATE).isEmpty() ? null : new DateNonConvertable(new Date(Long.valueOf(valuesMap.get(ADDITIONAL_PAYMENT_DATE)))));
        setEmployerContribution(getBigDecimalValueFromString(EMPLOYEE_CONTRIBUTION));
        setTax(getBigDecimalValueFromString(TAX));
        setDeduction(getBigDecimalValueFromString(DEDUCTION));
        setTotalAmount(getBigDecimalValueFromString(TOTAL_AMOUNT));
    }

    private Integer getIntegerValueFromString(String code) {
        return valuesMap.get(code) != null && ("null".equals(valuesMap.get(code)) || valuesMap.get(code).isEmpty()) ? null : Integer.valueOf(valuesMap.get(code));
    }

    private BigDecimal getBigDecimalValueFromString(String code) {
        return valuesMap.get(code) != null ? new BigDecimal(valuesMap.get(code)) : BigDecimal.ZERO;
    }
}
