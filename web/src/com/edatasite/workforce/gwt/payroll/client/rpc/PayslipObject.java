package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Frequency;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.newemployee.client.rpc.EmployeeViewItem;
import com.edatasite.workforce.gwt.payroll.client.rpc.statutorypay.EmployeeSMPSettings;
import com.edatasite.workforce.gwt.payroll.client.rpc.statutorypay.EmployeeSPPASettings;
import com.edatasite.workforce.gwt.payroll.client.rpc.statutorypay.EmployeeSPPSettings;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 01.03.2009
 * Time: 17:03:22
 * To change this template use File | Settings | File Templates.
 */
public class PayslipObject implements IsSerializable {

    /*Payslip Add/Edit PreInit Data*/
    private SelectItem[] employees;

    /*Payslip Data*/
    private Integer id;

    private Date date;

    private Integer employeeID;

    private EmployeeViewItem employee;

    private String employeeName;

    private String payperiod;

    private String paymentmethod;

    private BigDecimal niEmployer;

    private BigDecimal niEmployee;

    private BigDecimal incomeTax;

    private BigDecimal payAdjustment;

    private BigDecimal totalPay;

    private BigDecimal totalTax;

    private BigDecimal netPay;

    private BigDecimal grossPay;

    private BigDecimal grossDeduction;

    private BigDecimal taxablePayInPeriod;

//    private BigDecimal totalPaymentsAdditionalRates;

//    private BigDecimal totalDeductionsAdditionalRates;

    private BigDecimal totalBonus;

    private LinkedHashMap<String, PaymentDeductionObject> payments;

    private LinkedHashMap<String, PaymentDeductionObject> deductions;

    private HashMap<Date, HashMap<String, PaymentDeductionObject>> advanceDeductions;

    private ArrayList<PaymentDeductionObject> cashAdvanceList;

    private IncomeTaxObject incomeTaxObject;

    private NITaxObject niObject;

    private String statusName;

    private String paymentPolicy;

    private boolean editable;

    private EmployeeSMPSettings employeeSMPSettings;
    private HashMap<String, Date> mppNotKitDates = new HashMap<>();
    private EmployeeSPPSettings employeeSPPSettings;
    private EmployeeSPPASettings employeeSPPASettings;
    private HashMap<String, Date> pppWorkDates = new HashMap<>();
    private HashMap<String, Date> p32Params = new HashMap<>();

    private boolean sendCopyToEmployee;
    private boolean isUkCompany;

    private boolean isOnIndustrialAction;

    private boolean isFromEndOfService;

    public PayslipObject() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public EmployeeViewItem getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeViewItem employee) {
        this.employee = employee;
    }

    public String getPayperiod() {
        return payperiod;
    }

    public void setPayperiod(String payperiod) {
        this.payperiod = payperiod;
    }

    public String getPaymentmethod() {
        return paymentmethod;
    }

    public void setPaymentmethod(String paymentmethod) {
        this.paymentmethod = paymentmethod;
    }

    public BigDecimal getNiEmployer() {
        return niEmployer;
    }

    public void setNiEmployer(BigDecimal niEmployer) {
        this.niEmployer = niEmployer;
    }

    public BigDecimal getNiEmployee() {
        return niEmployee;
    }

    public void setNiEmployee(BigDecimal niEmployee) {
        this.niEmployee = niEmployee;
    }

    public BigDecimal getIncomeTax() {
        return incomeTax;
    }

    public void setIncomeTax(BigDecimal incomeTax) {
        this.incomeTax = incomeTax;
    }

    public BigDecimal getTotalPay() {
        return totalPay;
    }

    public void setTotalPay(BigDecimal totalPay) {
        this.totalPay = totalPay;
    }

    public BigDecimal getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(BigDecimal totalTax) {
        this.totalTax = totalTax;
    }

    public BigDecimal getNetPay() {
        return netPay;
    }

    public void setNetPay(BigDecimal netPay) {
        this.netPay = netPay;
    }

    public LinkedHashMap<String, PaymentDeductionObject> getPayments() {
        return payments;
    }

    public void setPayments(LinkedHashMap<String, PaymentDeductionObject> payments) {
        this.payments = payments;
    }

    public LinkedHashMap<String, PaymentDeductionObject> getDeductions() {
        return deductions;
    }

    public void setDeductions(LinkedHashMap<String, PaymentDeductionObject> deductions) {
        this.deductions = deductions;
    }

    public ArrayList<PaymentDeductionObject> getCashAdvanceList() {
        if (cashAdvanceList == null) {
            cashAdvanceList = new ArrayList<>();
        }
        return cashAdvanceList;
    }

    public void setCashAdvanceList(ArrayList<PaymentDeductionObject> cashAdvanceList) {
        this.cashAdvanceList = cashAdvanceList;
    }

    public BigDecimal getGrossPay() {
        return grossPay;
    }

    public void setGrossPay(BigDecimal grossPay) {
        this.grossPay = grossPay;
    }

    public BigDecimal getGrossDeduction() {
        return grossDeduction;
    }

    public void setGrossDeduction(BigDecimal grossDeduction) {
        this.grossDeduction = grossDeduction;
    }

    public BigDecimal getTaxablePayInPeriod() {
        return taxablePayInPeriod;
    }

    public void setTaxablePayInPeriod(BigDecimal taxablePayInPeriod) {
        this.taxablePayInPeriod = taxablePayInPeriod;
    }

//    public BigDecimal getTotalPaymentsAdditionalRates() {
//        return totalPaymentsAdditionalRates;
//    }
//
//    public void setTotalPaymentsAdditionalRates(BigDecimal totalPaymentsAdditionalRates) {
//        this.totalPaymentsAdditionalRates = totalPaymentsAdditionalRates;
//    }
//
//    public BigDecimal getTotalDeductionsAdditionalRates() {
//        return totalDeductionsAdditionalRates;
//    }
//
//    public void setTotalDeductionsAdditionalRates(BigDecimal totalDeductionsAdditionalRates) {
//        this.totalDeductionsAdditionalRates = totalDeductionsAdditionalRates;
//    }

    public BigDecimal getTotalBonus() {
        return totalBonus;
    }

    public void setTotalBonus(BigDecimal totalBonus) {
        this.totalBonus = totalBonus;
    }

    public BigDecimal getPayAdjustment() {
        return payAdjustment;
    }

    public void setPayAdjustment(BigDecimal payAdjustment) {
        this.payAdjustment = payAdjustment;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public IncomeTaxObject getIncomeTaxObject() {
        return incomeTaxObject;
    }

    public void setIncomeTaxObject(IncomeTaxObject incomeTaxObject) {
        this.incomeTaxObject = incomeTaxObject;
    }

    public NITaxObject getNiObject() {
        return niObject;
    }

    public void setNiObject(NITaxObject niObject) {
        this.niObject = niObject;
    }

    public HashMap<Date, HashMap<String, PaymentDeductionObject>> getAdvanceDeductions() {
        return advanceDeductions;
    }

    public void setAdvanceDeductions(HashMap<Date, HashMap<String, PaymentDeductionObject>> advanceDeductions) {
        this.advanceDeductions = advanceDeductions;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getPaymentPolicy() {
        return paymentPolicy;
    }

    public void setPaymentPolicy(String paymentPolicy) {
        this.paymentPolicy = paymentPolicy;
    }

    public Integer getFrequency() {
        if (payperiod != null) {
            char freq = payperiod.charAt(0);
            if (freq == 'W') {
                return Frequency.WEEKLY.getId();
            } else {
                return Frequency.MONTHLY.getId();
            }
        }
        return null;
    }

    public Integer getPayPeriodNo() {
        return payperiod != null ? Integer.valueOf(payperiod.substring(1, payperiod.length())) : null;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public HashMap<String, Date> getP32Params() {
        return p32Params;
    }

    public void setP32Params(HashMap<String, Date> p32Params) {
        this.p32Params = p32Params;
    }

    public void putP32Param(String key, Date value) {
        this.p32Params.put(key, value);
    }

    public EmployeeSMPSettings getEmployeeSMPSettings() {
        return employeeSMPSettings;
    }

    public void setEmployeeSMPSettings(EmployeeSMPSettings employeeSMPSettings) {
        this.employeeSMPSettings = employeeSMPSettings;
    }

    public EmployeeSPPASettings getEmployeeSPPASettings() {
        return employeeSPPASettings;
    }

    public void setEmployeeSPPASettings(EmployeeSPPASettings employeeSPPASettings) {
        this.employeeSPPASettings = employeeSPPASettings;
    }

    public EmployeeSPPSettings getEmployeeSPPSettings() {
        return employeeSPPSettings;
    }

    public void setEmployeeSPPSettings(EmployeeSPPSettings employeeSPPSettings) {
        this.employeeSPPSettings = employeeSPPSettings;
    }

    public HashMap<String, Date> getMppNotKitDates() {
        return mppNotKitDates;
    }

    public void setMppNotKitDates(HashMap<String, Date> mppNotKitDates) {
        this.mppNotKitDates = mppNotKitDates;
    }

    public HashMap<String, Date> getPppWorkDates() {
        return pppWorkDates;
    }

    public void setPppWorkDates(HashMap<String, Date> pppWorkDates) {
        this.pppWorkDates = pppWorkDates;
    }

    public boolean isSendCopyToEmployee() {
        return sendCopyToEmployee;
    }

    public void setSendCopyToEmployee(boolean sendCopyToEmployee) {
        this.sendCopyToEmployee = sendCopyToEmployee;
    }

    public boolean isOnIndustrialAction() {
        return isOnIndustrialAction;
    }

    public void setOnIndustrialAction(boolean onIndustrialAction) {
        isOnIndustrialAction = onIndustrialAction;
    }

    public boolean isUkCompany() {
        return isUkCompany;
    }

    public void setUkCompany(boolean ukCompany) {
        isUkCompany = ukCompany;
    }

    public SelectItem[] getEmployees() {
        return employees;
    }

    public void setEmployees(SelectItem[] employees) {
        this.employees = employees;
    }

    public boolean isFromEndOfService() {
        return isFromEndOfService;
    }

    public void setFromEndOfService(boolean fromEndOfService) {
        isFromEndOfService = fromEndOfService;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }
}
