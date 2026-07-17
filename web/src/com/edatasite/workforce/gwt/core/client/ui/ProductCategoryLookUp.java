package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;

/**
 * Created with IntelliJ IDEA.
 * User: Farruh Atabayev
 * Date: 05/12/18
 * Time: 2:40 AM
 * To change this template use File | Settings | File Templates.
 */
public class ProductCategoryLookUp extends LookUp {

    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();


    @Override
    protected void onItemDeleteInsertUpdate(int type) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
        LoadingPanel.loading(true);
        filterParametrs.setLookUp(true);
        AccountingService.App.get().getProductCategoriesAsSelectItem(filterParametrs, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(SelectItem[] result) {
                LoadingPanel.loading(false);
                setItems(filterParametrs.getSearchKey(), result);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                ProductCategoryLookUp.super.getOracle().setFullSearch(true);
                ProductCategoryLookUp.super.getSuggestBox().showSuggestions(searchKey);
            }
        });
    }
}
