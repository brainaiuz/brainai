package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.bankTransfer;

import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;

public class BankReceiptListView extends BankTransferListView {
    public BankReceiptListView() {
        super(RECEIVE_MONEY);
        setDescription(property.getPlural(accountingStrings.bankReceipts()));
    }

    @Override
    protected ListPanelType getPanelType() {
        return ListPanelType.BankReceiptListPanel;
    }

    @Override
    public String getPropertyCode() {
        return AccountingConstants.RECEIVE_MONEY_STR;
    }

}
