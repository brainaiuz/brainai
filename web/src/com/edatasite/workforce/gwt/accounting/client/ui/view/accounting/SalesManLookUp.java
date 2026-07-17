package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 12/19/12
 * Time: 4:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class SalesManLookUp extends LookUp {

    @Override
    protected void onItemDeleteInsertUpdate(int type) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
//        LoadingPanel.get().show(wfmStrings.searching());
        AccountingService.App.get().getSalesMansAsSelectItem(filterParametrs, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable throwable) {
//                LoadingPanel.loading(false);
            }

            @Override
            public void success(SelectItem[] result) {
//                LoadingPanel.loading(false);
                setItems(filterParametrs.getSearchKey(), result);
                //initItems(result);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                SalesManLookUp.super.getSuggestBox().showSuggestions(searchKey);
                SalesManLookUp.super.getOracle().setFullSearch(true);
            }
        });
    }
}
