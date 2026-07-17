package com.edatasite.workforce.gwt.core.server.db.payroll;

import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollPaymentItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.math.BigDecimal;
import java.util.List;

public interface PayrollPaymentItemManager extends Manager<EdsPayrollPaymentItem> {

    List<EdsPayrollPaymentItem> getListByFilter(ListingFilterParameter fp);

    List<EdsPayrollPaymentItem> getPayrollPaymentItems(Integer singleAddPaymentId);

    BigDecimal getTotalPaymentBySingleAddPaymentId(Integer singleAddPaymentId);

    BigDecimal getTotalSinglePaymentsByAdditionalPaymentId(Integer additionalPaymentId);

    boolean isLastItemInPayrollPayment(Integer paymentId);
}
