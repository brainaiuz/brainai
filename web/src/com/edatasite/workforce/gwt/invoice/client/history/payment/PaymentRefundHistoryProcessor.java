package com.edatasite.workforce.gwt.invoice.client.history.payment;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.history.HistoryProcessor;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.payment.PaymentRefundAddSinksContainer;
import com.edatasite.workforce.gwt.invoice.client.container.payment.PaymentRefundViewSinksContainer;

public class PaymentRefundHistoryProcessor implements HistoryProcessor {
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();

    private boolean isReceivable;

    public PaymentRefundHistoryProcessor(boolean isReceivable) {
        this.isReceivable = isReceivable;
    }

    @Override
    public SinksContainer process(String containerName, String[] strings) {
        return new PaymentRefundViewSinksContainer(containerName + strings[0], (isReceivable ? accountingStrings.customerRefund() : accountingStrings.supplierRefund()), strings);
    }

    @Override
    public SinksContainer processAdd(String[] params) {
        return new PaymentRefundAddSinksContainer((isReceivable ? "customerRefundadd" : "supplierRefundadd"), (isReceivable ? accountingStrings.customerRefund() : accountingStrings.supplierRefund()), params);
    }
}
