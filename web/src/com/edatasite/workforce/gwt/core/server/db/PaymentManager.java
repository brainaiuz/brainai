package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsPayment;

import java.util.Date;
import java.util.List;

public interface PaymentManager extends Manager<EdsPayment> {

    List<EdsPayment> getPayments();

    List<EdsPayment> getExpenses(Integer employeeID);

    List<EdsPayment> getPayments(Date from, Date to, Integer employeeID);

    List<EdsPayment> getDeductions(Date from, Date to, Integer employeeID);

    List<EdsPayment> getNotAssignedPayments(Integer employeeID);

    List<EdsPayment> getNotAssignedExpenses(Integer employeeID);

    List<EdsPayment> getNotAssignedDeductions(Integer employeeID);

    List<EdsPayment> getPaymentsByPayslip(Integer payslipID);

    List<EdsPayment> getDeductionsByPayslip(Integer payslipID);

    List<EdsEmployee> getEmployeesWithExpenses();

    EdsPayment getPayment(Integer paymentId);
}
