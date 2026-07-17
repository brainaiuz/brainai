package com.edatasite.workforce.gwt.payroll.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.RequestObject;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Oct 2, 2009
 * Time: 11:05:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class PayslipRequestObject extends RequestObject {

    public static final String ROW_SPLITTER = "<rs>";
    public static final String CELL_SPLITTER = "<cs>";

    public static final String FROM_GENERATE_PAYSLIP = "GENERATE_PAYSLIP";
    public static final String FROM_PAYSLIP_LIST = "PAYSLIP_LIST";

    private Integer employeeId;
    private String weekOrMonthNo;
    private Integer year;
    private Integer frequency;
    private BigDecimal amountNIable;
    private BigDecimal amountTaxable;
    private Long date;

    private String payments;
    private String deductions;
    private String advancePayments;

    private String niNumber;
    private String taxCode;

    private String paymentPeriod;
    private String paymentMethod;

    private String totalGrossPay;
    private String totalDeductions;
    private String totalTaxablePay;
    private String totalPayToDate;
    private String totalBonus;
    private String netPay;
    private String payAdjustment;


    private String fromView;

    private String fromArabic;
    private String fromCompanyUK;

    public PayslipRequestObject() {
    }

    public HashMap<String, String> getRequestParams() {
        final HashMap<String, String> parametersMap = new HashMap<>();
        parametersMap.put("objectID", getObjectID() == null ? "" : getObjectID().toString());
        parametersMap.put("employeeId", employeeId == null ? "" : employeeId.toString());
        parametersMap.put("weekOrMonthNo", weekOrMonthNo == null ? "" : weekOrMonthNo.toLowerCase());
        parametersMap.put("year", year == null ? "" : year.toString());
        parametersMap.put("frequency", frequency == null ? "" : frequency.toString());
        parametersMap.put("amountNIable", amountNIable == null ? "" : amountNIable.toString());
        parametersMap.put("amountTaxable", amountTaxable == null ? "" : amountTaxable.toString());
        parametersMap.put("date", date == null ? "" : date.toString());
        parametersMap.put("payments", payments);
        parametersMap.put("deductions", deductions);
        parametersMap.put("niNumber", niNumber);
        parametersMap.put("taxCode", taxCode);
        parametersMap.put("paymentPeriod", paymentPeriod);
        parametersMap.put("paymentMethod", paymentMethod);
        parametersMap.put("totalGrossPay", totalGrossPay);
        parametersMap.put("totalDeductions", totalDeductions);
        parametersMap.put("totalTaxablePay", totalTaxablePay);
        parametersMap.put("totalPayToDate", totalPayToDate);
        parametersMap.put("totalBonus", totalBonus);
        parametersMap.put("netPay", netPay);
        parametersMap.put("payAdjustment", payAdjustment);
        parametersMap.put("advancePayments", advancePayments);
        parametersMap.put("fromView", fromView);
        parametersMap.put("fromArabic", fromArabic);

        return parametersMap;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getWeekOrMonthNo() {
        return weekOrMonthNo;
    }

    public void setWeekOrMonthNo(String weekOrMonthNo) {
        this.weekOrMonthNo = weekOrMonthNo;
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

    public BigDecimal getAmountNIable() {
        return amountNIable;
    }

    public void setAmountNIable(BigDecimal amountNIable) {
        this.amountNIable = amountNIable;
    }

    public BigDecimal getAmountTaxable() {
        return amountTaxable;
    }

    public void setAmountTaxable(BigDecimal amountTaxable) {
        this.amountTaxable = amountTaxable;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getPayments() {
        return payments;
    }

    public void setPayments(String payments) {
        this.payments = payments;
    }

    public String getDeductions() {
        return deductions;
    }

    public void setDeductions(String deductions) {
        this.deductions = deductions;
    }

    public String getNiNumber() {
        return niNumber;
    }

    public void setNiNumber(String niNumber) {
        this.niNumber = niNumber;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getPaymentPeriod() {
        return paymentPeriod;
    }

    public void setPaymentPeriod(String paymentPeriod) {
        this.paymentPeriod = paymentPeriod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getTotalGrossPay() {
        return totalGrossPay;
    }

    public void setTotalGrossPay(String totalGrossPay) {
        this.totalGrossPay = totalGrossPay;
    }

    public String getTotalDeductions() {
        return totalDeductions;
    }

    public void setTotalDeductions(String totalDeductions) {
        this.totalDeductions = totalDeductions;
    }

    public String getTotalTaxablePay() {
        return totalTaxablePay;
    }

    public void setTotalTaxablePay(String totalTaxablePay) {
        this.totalTaxablePay = totalTaxablePay;
    }

    public String getTotalPayToDate() {
        return totalPayToDate;
    }

    public void setTotalPayToDate(String totalPayToDate) {
        this.totalPayToDate = totalPayToDate;
    }

    public String getNetPay() {
        return netPay;
    }

    public void setNetPay(String netPay) {
        this.netPay = netPay;
    }

    public String getPayAdjustment() {
        return payAdjustment;
    }

    public void setPayAdjustment(String payAdjustment) {
        this.payAdjustment = payAdjustment;
    }

    public String getAdvancePayments() {
        return advancePayments;
    }

    public void setAdvancePayments(String advancePayments) {
        this.advancePayments = advancePayments;
    }

    public String getFromView() {
        return fromView;
    }

    public void setFromView(String fromView) {
        this.fromView = fromView;
    }

    public String getFromArabic() {
        return fromArabic;
    }

    public void setFromArabic(String fromArabic) {
        this.fromArabic = fromArabic;
    }

    public String getTotalBonus() {
        return totalBonus;
    }

    public void setTotalBonus(String totalBonus) {
        this.totalBonus = totalBonus;
    }

    public String getFromCompanyUK() {
        return fromCompanyUK;
    }

    public void setFromCompanyUK(String fromCompanyUK) {
        this.fromCompanyUK = fromCompanyUK;
    }
}
