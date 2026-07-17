package com.edatasite.workforce.gwt.core.server.db.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsPaymentDeduction;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipPayments;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 12.06.14
 * Time: 0:25
 * To change this template use File | Settings | File Templates.
 */
public interface PayslipPaymentsManager extends Manager<EdsPayslipPayments> {

    EdsPayslipPayments getPayslipPayment(Integer paymentDeductionID, Integer payslipItemID);

    EdsPayslipPayments getOldPayslipPayment(Integer paymentDeductionID, Integer payslipID);

    BigDecimal getPayedAmountByCategory(Integer paymentDeductionID);

    BigDecimal getPaymentAmount(Integer paymentDeductionID, Integer payslipItemID);

    BigDecimal getPaymentAmountForOldPayslip(Integer paymentDeductionID, Integer payslipID);

    List<EdsPaymentDeduction> getCategoriesForTransaction(Integer objectID);

    List<Object[]> getEosDataFromOldPaylip(Integer employeeID);

    List<Object[]> getEosDataFromPaylipTableItem(Integer employeeID);

    List<Object[]> getEosDataFromPaylipTableItemByCategory(Integer employeeID, String categories);

    List<EdsPayslipPayments> getByPayslipItemID(Integer payslipItemID);

    void deleteByPayslipItemID(Integer payslipItemID);

    void deleteByPayslipID(Integer payslipID);

    Boolean checkPaymentDeductionForUsed(Integer paymentDeductionID);

    LinkedHashMap<String,BigDecimal> getPayrollChartData(ListingFilterParameter fp);

    LinkedHashMap<String, BigDecimal> getTotalSalaryChartData(ListingFilterParameter fp);

    LinkedHashMap<String, BigDecimal> getTotalIncetivesByDepartmentChartData(ListingFilterParameter fp);

    LinkedHashMap<String, BigDecimal> getTotalSalaryRatioChartData(ListingFilterParameter fp);

    List<EdsPayslipPayments> getCashAdvancePayments(ListingFilterParameter filter);

    Integer getCashAdvancePaymentAmount(ListingFilterParameter filter);

    LinkedHashMap<Integer, BigDecimal> getPayrollYTDChartData(ListingFilterParameter fp, boolean isPayment);

    LinkedHashMap<String, BigDecimal> getEmployeePayrollYTDChartData(ListingFilterParameter fp, boolean isPayment);

    LinkedHashMap<String, ArrayList<String>> getPayrollYTDChartDataCategories(ListingFilterParameter fp);

    LinkedHashMap<Integer, BigDecimal> getPayrollYTDChartDataExpenses(ListingFilterParameter fp);

    LinkedHashMap<Integer, BigDecimal> getEmployeePayrollYTDChartDataExpenses(ListingFilterParameter fp);

    HashMap<Integer, BigDecimal> getEmployeeSalaryForPeriod(ListingFilterParameter fp);

    Map<Integer, BigDecimal> getPaymentAmounts(String paymentDeductionIds, Integer payslipItemID);

}
