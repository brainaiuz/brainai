package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting.bankTransfer;

import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;

public class CashReceiptsListView extends BankTransferListView {
    public CashReceiptsListView() {
        super(CASH_RECEIPT);
        setDescription(property.getPlural(wfmStrings.cashReceipt()));
    }

    @Override
    protected ListPanelType getPanelType() {
        return ListPanelType.CashReceiptListPanel;
    }

    @Override
    public String getPropertyCode() {
        return AccountingConstants.CASH_RECEIPT_STR;
    }
}
