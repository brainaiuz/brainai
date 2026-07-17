package com.edatasite.workforce.gwt.expenses.client.ui.view.report;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 1/17/12
 * Time: 3:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class PurchaseOrderLookUp extends LookUp{
    @Override
    protected void onItemDeleteInsertUpdate(int type) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void lookUpService(final ListingFilterParameter fp) {
//        LoadingPanel.get().show(wfmStrings.searching());
        QuoteService.App.get().getPurchaseOrders(fp, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }
            @Override
            public void onSuccess(SelectItem[] items) {
//                LoadingPanel.loading(false);

                setItems(fp.getSearchKey(), items);

                String searchKey = fp.getSearchKey() == null ? "" : fp.getSearchKey();
                PurchaseOrderLookUp.super.getSuggestBox().showSuggestions(searchKey);
                PurchaseOrderLookUp.super.getOracle().setFullSearch(true);
            }
        });
    }
}
