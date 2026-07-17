package com.edatasite.workforce.gwt.payroll.server.app;

import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTableItem;
import com.edatasite.workforce.gwt.core.client.ui.view.CashAdvanceItem;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.payroll.client.rpc.SinglePayrunItem;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * User: Murad Satimov
 * Date: 3/29/18 3:49 AM
 */
public interface PayrollAsyncService {

    <T> T getInNewTransaction(Supplier<T> supplier);

    Integer saveSinglePayrunItem(SinglePayrunItem item, BiConsumer<SinglePayrunItem, EdsPayslipTableItem> expenseAndPaymentFunction);

    void applyGroupPayrunTotal(Integer groupPayrunId);

    Integer createCashAdvance(CashAdvanceItem cashAdvanceItem);

    Integer createPaymentDeduction(PaymentDeductionObject item);
}
