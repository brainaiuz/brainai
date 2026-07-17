package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Aug 19, 2009
 * Time: 7:59:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class PayrollSettings implements IsSerializable {
    private Integer employeeId;
    private String employeeName;
    private String employeeFirstName;
    private String employeeLastName;
    private String employeeEmail;
    private Integer employeePeriodMinuts;
    private String gender;
    private NumberData numberData;
    private DateNonConvertable dob;
    private DateNonConvertable startDate;
    private DateNonConvertable resignationDate;
    private DateNonConvertable prevEndDate;
    private DateNonConvertable startDateForOnlyPayroll;
    private ArrayList<PaymentDeductionObject> paymentCategories;
    private ArrayList<PaymentDeductionObject> deductionCategories;
    private ArrayList<PaymentDeductionObject> taxCategories;
    private ArrayList<PaymentDeductionObject> loanCategories;
    private ArrayList<PaymentDeductionObject> employerContributions;
    private PaymentDeductionSelectItem salaryCategory;
    private CurrencyItem[] currencies;
    private CurrencyItem salaryCurrency;
    private Boolean enabledMultiCurrency;
    private SelectItem[] payMethods;
    private SelectItem payMethod;
    private String paymentMethod;
    private SelectItem[] countries;
    private SelectItem citizenship;

    private String statusCode;
    private Integer statusId;
    private SelectItem[] roleList;
    private Integer[] roleId;
    private Boolean noAccess;
    private Boolean ess;
    private Integer[] userLimit;
    private SelectItem[] statusList;
    private String status;
    private String salaryMode;


    public SelectItem[] getStatusList() {
        return statusList;
    }

    public void setStatusList(SelectItem[] statusList) {
        this.statusList = statusList;
    }

    private HashMap<String, String> payrollSettings;

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeFirstName() {
        return employeeFirstName;
    }

    public void setEmployeeFirstName(String employeeFirstName) {
        this.employeeFirstName = employeeFirstName;
    }

    public String getEmployeeLastName() {
        return employeeLastName;
    }

    public void setEmployeeLastName(String employeeLastName) {
        this.employeeLastName = employeeLastName;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public DateNonConvertable getStartDate() {
        return startDate;
    }

    public void setStartDate(DateNonConvertable startDate) {
        this.startDate = startDate;
    }

    public DateNonConvertable getDob() {
        return dob;
    }

    public void setDob(DateNonConvertable dob) {
        this.dob = dob;
    }

    public NumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
    }

    public DateNonConvertable getResignationDate() {
        return resignationDate;
    }

    public void setResignationDate(DateNonConvertable resignationDate) {
        this.resignationDate = resignationDate;
    }

    public DateNonConvertable getPrevEndDate() {
        return prevEndDate;
    }

    public void setPrevEndDate(DateNonConvertable prevEndDate) {
        this.prevEndDate = prevEndDate;
    }

    public DateNonConvertable getStartDateForOnlyPayroll() {
        return startDateForOnlyPayroll;
    }

    public void setStartDateForOnlyPayroll(DateNonConvertable startDateForOnlyPayroll) {
        this.startDateForOnlyPayroll = startDateForOnlyPayroll;
    }

    public HashMap<String, String> getPayrollSettings() {
        if (payrollSettings == null) {
            payrollSettings = new HashMap<>();
        }
        return payrollSettings;
    }

    public void setPayrollSettings(HashMap<String, String> payrollSettings) {
        this.payrollSettings = payrollSettings;
    }

    public Integer getEmployeePeriodMinuts() {
        return employeePeriodMinuts;
    }

    public void setEmployeePeriodMinuts(Integer employeePeriodMinuts) {
        this.employeePeriodMinuts = employeePeriodMinuts;
    }

    public ArrayList<PaymentDeductionObject> getPaymentCategories() {
        if (paymentCategories == null) {
            paymentCategories = new ArrayList<>();
        }
        return paymentCategories;
    }

    public void setPaymentCategories(ArrayList<PaymentDeductionObject> paymentCategories) {
        this.paymentCategories = paymentCategories;
    }

    public ArrayList<PaymentDeductionObject> getDeductionCategories() {
        if (deductionCategories == null) {
            deductionCategories = new ArrayList<>();
        }
        return deductionCategories;
    }

    public void setDeductionCategories(ArrayList<PaymentDeductionObject> deductionCategories) {
        this.deductionCategories = deductionCategories;
    }

    public ArrayList<PaymentDeductionObject> getTaxCategories() {
        if (taxCategories == null) {
            taxCategories = new ArrayList<>();
        }
        return taxCategories;
    }

    public void setTaxCategories(ArrayList<PaymentDeductionObject> taxCategories) {
        this.taxCategories = taxCategories;
    }

    public PaymentDeductionSelectItem getSalaryCategory() {
        return salaryCategory;
    }

    public void setSalaryCategory(PaymentDeductionSelectItem salaryCategory) {
        this.salaryCategory = salaryCategory;
    }

    public ArrayList<PaymentDeductionObject> getLoanCategories() {
        if (loanCategories == null) {
            loanCategories = new ArrayList<>();
        }
        return loanCategories;
    }

    public void setLoanCategories(ArrayList<PaymentDeductionObject> loanCategories) {
        this.loanCategories = loanCategories;
    }

    public CurrencyItem[] getCurrencies() {
        return currencies;
    }

    public void setCurrencies(CurrencyItem[] currencies) {
        this.currencies = currencies;
    }

    public CurrencyItem getSalaryCurrency() {
        return salaryCurrency;
    }

    public void setSalaryCurrency(CurrencyItem salaryCurrency) {
        this.salaryCurrency = salaryCurrency;
    }

    public Boolean isEnabledMultiCurrency() {
        return enabledMultiCurrency != null && enabledMultiCurrency;
    }

    public void setEnabledMultiCurrency(Boolean enabledMultiCurrency) {
        this.enabledMultiCurrency = enabledMultiCurrency;
    }

    public SelectItem[] getPayMethods() {
        return payMethods;
    }

    public void setPayMethods(SelectItem[] payMethods) {
        this.payMethods = payMethods;
    }

    public SelectItem getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(SelectItem payMethod) {
        this.payMethod = payMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public SelectItem[] getCountries() {
        return countries;
    }

    public void setCountries(SelectItem[] countries) {
        this.countries = countries;
    }

    public SelectItem getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(SelectItem citizenship) {
        this.citizenship = citizenship;
    }

    public String getStatusCode(){
        return statusCode;
    }

    public void setStatusCode(String statusCode){
        this.statusCode = statusCode;
    }
    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }
    public SelectItem[] getRoleList() {
        return roleList;
    }

    public void setRoleList(SelectItem[] roleList) {
        this.roleList = roleList;
    }
    public Integer[] getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer[] roleId) {
        this.roleId = roleId;
    }

    public Boolean getNoAccess() {
        if (noAccess == null) {
            noAccess = Boolean.FALSE;
        }
        return noAccess;
    }

    public void setNoAccess(Boolean noAccess) {
        this.noAccess = noAccess;
    }


    public Integer[] getUserLimit() {
        return userLimit;
    }

    public void setUserLimit(Integer[] userLimit) {
        this.userLimit = userLimit;
    }

    public Boolean getEss() {
        if (ess == null) {
            return Boolean.FALSE;
        }
        return ess;
    }

    public void setEss(Boolean ess) {
        this.ess = ess;
    }

    public void setStatus(String s) {
        this.status = s;
    }
    public String getStatus(){
        return status;
    }

    public ArrayList<PaymentDeductionObject> getEmployerContributions() {
        return employerContributions;
    }

    public void setEmployerContributions(ArrayList<PaymentDeductionObject> employerContributions) {
        this.employerContributions = employerContributions;
    }

    public String getSalaryMode() {
        return salaryMode;
    }

    public void setSalaryMode(String salaryMode) {
        this.salaryMode = salaryMode;
    }
}
