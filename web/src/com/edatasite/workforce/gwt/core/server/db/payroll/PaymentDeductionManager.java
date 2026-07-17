package com.edatasite.workforce.gwt.core.server.db.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsPaymentDeduction;
import com.edatasite.workforce.gwt.core.client.enums.EPPaymentType;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.server.db.Manager;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayslipFilter;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: 23.02.2009
 * Time: 18:05:55
 * To change this template use File | Settings | File Templates.
 */
public interface PaymentDeductionManager extends Manager<EdsPaymentDeduction> {

    List<EdsPaymentDeduction> getPayslipPaymentDeductions(Integer payslipID, String type);

    List<EdsPaymentDeduction> getSinglePayrunCashAdvanceDeductions(Integer singlePayrunID);

    List<EdsPaymentDeduction> getPayslipAdvancePayments(Integer payslipID);

    BigDecimal getTotalPaymentByCategories(Integer employeeID, String categories);

    void deletePaymentOrDeduction(Integer EdsPaymentDeductionID);

    void deletePaymentOrDeductionsByAdditionalPaymentId(Integer EdsPaymentDeductionID);

    List<EdsPaymentDeduction> getPaymentDeductionByCatogoryID(Integer categoryID);

    EdsPaymentDeduction getDeductionOrLoanByCashAdvanceID(Integer objectID);

    HashMap<Integer, BigDecimal> getEmployeeCategoriesTotal(String employeeIds, Integer categoryType);

    EdsPaymentDeduction getEmployeeRecurringPaymentDeductionByCategory(Integer employeeId, Integer categoryId);

    Map<String, PaymentDeductionObject> getRecurringPaymentDeductionByCategoryMap(Collection<Integer> employeeIds);

    List<EdsPaymentDeduction> getPayslipAdditionalPayments(Integer paymentID);

    Integer getPaymentDeductionIdByCashAdvance(Integer cashAdvanceId);

    List<PaymentDeductionObject> getEmployeesPaymentDeduction(Collection<Integer> employeeIds, PayslipFilter filter);

    Integer getAdditionalPaymentCountByFilter(ListingFilterParameter fp);

    List<EdsPaymentDeduction> getAdditionalPaymentItemListByFilter(ListingFilterParameter fp);

    EdsPaymentDeduction getPredefinedPaymentDeduction(Integer employeeId, Integer categoryId, EPPaymentType paymentType);

    default Multimap<Integer, PaymentDeductionObject> getEmployeesPaymentDeductionMap(Collection<Integer> employeeIds,
                                                                                      PayslipFilter filter) {
        final List<PaymentDeductionObject> list = this.getEmployeesPaymentDeduction(employeeIds, filter);
        final Multimap<Integer, PaymentDeductionObject> resultMap = ArrayListMultimap.create();

        for (PaymentDeductionObject paymentDeduction : list) {
            final SelectItem employee = paymentDeduction.getEmployee();

            if (employee == null) {
                continue;
            }
            resultMap.put(employee.getId(), paymentDeduction);
        }
        return resultMap;
    }

    List<EdsPaymentDeduction> getEmpployeePaymentDeductions(Integer employeeId, boolean payment);

    EdsPaymentDeduction getByRecurringPayDeductionID(Integer objectID);

    EdsPaymentDeduction getPreviousPaymentDeductionByEffectiveDate(Integer employeeId, Integer categoryId, Date effectiveDate);

    EdsPaymentDeduction getNextPaymentDeductionByEffectiveDate(Integer employeeId, Integer categoryId, Date effectiveDate);

    BigDecimal getPayslipMaterialAidTotalPayments(Date startDate, Date endDate, Integer employeeID, String systemCode);

    List<EdsPaymentDeduction> getPaymentsByEffectiveDate(Integer employeeId, Date effectiveDate);

    List<EdsPaymentDeduction> getBackupEmployeeAdditionalPaymentsUsedInPayslips(Integer backupsEmployeeID, Integer monthId, Integer year);

    List<Object> getAdditionalPaymentTotalAmount(Integer additionalPaymentId);
}
