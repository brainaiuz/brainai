package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;

/**
 * Created by Dilshod Madrahimov on 03/15/17 1:35 PM
 */
public class TransactionJournalLookUp extends LookUp {

    public TransactionJournalLookUp() {
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
//        LoadingPanel.get().show(wfmStrings.searching());
        AccountingService.App.get().getTransactionJournals(filterParametrs, new AsyncCallback<ArrayList<SelectItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onSuccess(ArrayList<SelectItem> result) {
                setItems(filterParametrs.getSearchKey(), result != null ? result.toArray(new SelectItem[result.size()]) : null);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                TransactionJournalLookUp.super.getSuggestBox().showSuggestions(searchKey);
//                LoadingPanel.loading(false);
            }
        });
    }

    @Override
    public void clear() {
        super.clear();
        oracle.clearItems();
        refreshOracle(true);
        getTextBox().setText(wfmStrings.searchTypeMessage());
        getTextBox().getElement().getStyle().setColor("#999999");
    }

}
