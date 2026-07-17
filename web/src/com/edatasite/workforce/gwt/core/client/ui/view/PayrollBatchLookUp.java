package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 10/23/15
 * Time: 8:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class PayrollBatchLookUp extends LookUp {


    public PayrollBatchLookUp() {
        super();
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {

    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
//        LoadingPanel.get().show("Searching...");
        AllInOneService.App.get().getPayrollBatchesForLookUp(filterParametrs, new AbstractAsyncCallback<ArrayList<SelectItem>>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(ArrayList<SelectItem> result) {
                setItems(filterParametrs.getSearchKey(), result.toArray(new SelectItem[]{}));
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                PayrollBatchLookUp.super.getOracle().setFullSearch(true);
                PayrollBatchLookUp.super.getSuggestBox().showSuggestions(searchKey);
                LoadingPanel.loading(false);
            }
        });
    }
}
