package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.google.gwt.user.client.rpc.AsyncCallback;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DRAFT;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.PAYABLE;


public class PurchaseInvoiceLookUp extends LookUp {

    @Override
    protected void onItemDeleteInsertUpdate(int type) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void lookUpService(final ListingFilterParameter fp) {

        fp.setCreditNote(false);
        fp.setInvoiceType(PAYABLE);
        fp.setStatusCode(DRAFT);
        InvoiceService.App.get().getPILookUp(fp, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(SelectItem[] items) {
                setItems(fp.getSearchKey(), items);

                String searchKey = fp.getSearchKey() == null ? "" : fp.getSearchKey();
                PurchaseInvoiceLookUp.super.getSuggestBox().showSuggestions(searchKey);
                PurchaseInvoiceLookUp.super.getOracle().setFullSearch(true);
            }
        });
    }
}
