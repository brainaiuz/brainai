package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;

public class RequestForQuoteLookUp extends LookUp {

    public RequestForQuoteLookUp() {
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {

    }

    @Override
    protected void lookUpService(ListingFilterParameter filterParametrs) {
        filterParametrs.setLookUp(true);
        AllInOneService.App.get().getRFQList(filterParametrs, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(SelectItem[] result) {
                setItems(filterParametrs.getSearchKey(), result);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                RequestForQuoteLookUp.super.getOracle().setFullSearch(true);
                RequestForQuoteLookUp.super.getSuggestBox().showSuggestions(searchKey);
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
