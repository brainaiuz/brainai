package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 11/2/11
 * Time: 8:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class CurrencyLookUp extends LookUp{

    private boolean withoutBaseCurrency;

    public CurrencyLookUp(boolean withoutBaseCurrency){
        this.withoutBaseCurrency = withoutBaseCurrency;
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
//        LoadingPanel.get().show("Searching...");
        filterParametrs.setLookUp(true);
        filterParametrs.setFiltirize(withoutBaseCurrency);

        InvoiceService.App.get().getCurrencies(filterParametrs, new AsyncCallback<CurrencyItem[]> (){
            @Override
            public void onFailure(Throwable throwable) {
//                LoadingPanel.loading(false);
            }
            @Override
            public void onSuccess(CurrencyItem[] result) {
//                LoadingPanel.loading(false);
                setItems(filterParametrs.getSearchKey(), result);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                CurrencyLookUp.super.getOracle().setFullSearch(true);
                CurrencyLookUp.super.getSuggestBox().showSuggestions(searchKey);
            }
        });
    }
}
