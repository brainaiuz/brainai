package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class InvoiceDynamicBankAccountListBox extends DynamicBankAccountListBox {
    public InvoiceDynamicBankAccountListBox(Integer objectId, SelectItem bankAccountItem, String viewType) {
        super(bankAccountItem, bank -> InvoiceService.App.get().changeBankAccount(objectId, viewType, bank == null ? null : bank.getId(), new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {}

            @Override
            public void onSuccess(Void aVoid) {}
        }));
    }
}