package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GRNLookUp extends LookUp {

    @Override
    protected void onItemDeleteInsertUpdate(int type) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void lookUpService(final ListingFilterParameter fp) {
        QuoteService.App.get().getGrnItems(fp, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(SelectItem[] items) {
                setItems(fp.getSearchKey(), items);

                String searchKey = fp.getSearchKey() == null ? "" : fp.getSearchKey();
                GRNLookUp.super.getSuggestBox().showSuggestions(searchKey);
                GRNLookUp.super.getOracle().setFullSearch(true);
            }
        });
    }
}
