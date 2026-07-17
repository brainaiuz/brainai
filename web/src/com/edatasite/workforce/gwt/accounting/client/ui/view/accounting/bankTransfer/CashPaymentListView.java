package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.bankTransfer;

import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;

public class CashPaymentListView extends BankTransferListView {
    public CashPaymentListView() {
        super(CASH_PAYMENT);
        setDescription(property.getPlural(wfmStrings.cashPayment()));
    }

    @Override
    protected ListPanelType getPanelType() {
        return ListPanelType.CashPaymentListPanel;
    }

    @Override
    public String getPropertyCode() {
        return AccountingConstants.CASH_PAYMENT_STR;
    }
}
