package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.bankTransfer;

import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;

public class BankPaymentListView extends BankTransferListView {
    public BankPaymentListView() {
        super(SPEND_MONEY);
        setDescription(property.getPlural(accountingStrings.bankPayments()));
    }

    @Override
    protected ListPanelType getPanelType() {
        return ListPanelType.BankPaymentListPanel;
    }


    @Override
    public String getPropertyCode() {
        return AccountingConstants.SPEND_MONEY_STR;
    }
}
