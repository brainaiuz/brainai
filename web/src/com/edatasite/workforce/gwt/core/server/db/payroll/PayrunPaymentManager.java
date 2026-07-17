package com.edatasite.workforce.gwt.core.server.db.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsPayrunPayment;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.math.BigDecimal;
import java.util.List;

public interface PayrunPaymentManager extends Manager<EdsPayrunPayment> {
    List<EdsPayrunPayment> getPayrunPayments(Integer payslipTableId);

    BigDecimal getTotalPaymentByPayrunId(Integer groupPayrunId);
}
