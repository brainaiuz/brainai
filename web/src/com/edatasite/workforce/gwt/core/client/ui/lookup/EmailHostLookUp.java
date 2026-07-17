package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;

public class EmailHostLookUp extends LookUp {

    @Override
    protected void onItemDeleteInsertUpdate(int type) {

    }

    @Override
    protected void lookUpService(ListingFilterParameter filterParametrs) {
        //BillboardPanel.get().show(accountingStrings.searching());
        filterParametrs.setLookUp(true);
        AllInOneService.App.get().getImapHostAndSmptHost(filterParametrs, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable throwable) {
                //BillboardPanel.get().hide();
            }

            @Override
            public void success(SelectItem[] result) {
                //BillboardPanel.get().hide();
                setItems(filterParametrs.getSearchKey(), result);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                EmailHostLookUp.super.getOracle().setFullSearch(true);
                EmailHostLookUp.super.getSuggestBox().showSuggestions(searchKey);
            }
        });
    }
}
