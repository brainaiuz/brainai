package com.edatasite.workforce.gwt.core.server.db.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollPayment;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.math.BigDecimal;
import java.util.List;

public interface PayrollPaymentManager extends Manager<EdsPayrollPayment> {

    List<EdsPayrollPayment> getPayrollPayments(Integer additionalPaymentId);

    BigDecimal getTotalPaymentByAdditionalPaymentId(Integer additionalPaymentId);
}
