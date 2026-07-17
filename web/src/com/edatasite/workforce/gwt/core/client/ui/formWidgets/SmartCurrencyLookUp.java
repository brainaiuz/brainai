package com.edatasite.workforce.gwt.core.client.ui.formWidgets;

import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created with IntelliJ IDEA.
 * User: Shohruh
 * Date: 22.01.16
 * Time: 17:30:56
 * To change this template use File | Settings | File Templates.
 */
public class SmartCurrencyLookUp extends LookUp{

    private boolean showUsed;
    private boolean withoutBaseCurrency = true;

    public SmartCurrencyLookUp(boolean showUsed) {
        this.showUsed = showUsed;
    }

    public SmartCurrencyLookUp(boolean showUsed, boolean withoutBaseCurrency) {
        this.showUsed = showUsed;
        this.withoutBaseCurrency = withoutBaseCurrency;
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {

    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
//        LoadingPanel.get().show("Searching...");
        filterParametrs.setLookUp(true);

        CurrencyService.App.get().getCurrencies(showUsed, withoutBaseCurrency, new AsyncCallback<CurrencyItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {
//                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(CurrencyItem[] result) {
//                LoadingPanel.loading(false);
                SmartCurrencyLookUp.super.getSuggestBox().setLimit(result.length);
                setItems(filterParametrs.getSearchKey(), result);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                SmartCurrencyLookUp.super.getSuggestBox().showSuggestions(searchKey);
            }
        });
    }
}
