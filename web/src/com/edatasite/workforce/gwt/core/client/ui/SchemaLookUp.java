package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Nov 11, 2010
 * Time: 11:10:36 PM
 */
public class SchemaLookUp extends LookUp {
    private boolean isFromPertnerBackend = false;

    public SchemaLookUp() {
        getSuggestBox().setAutoSelectEnabled(false);
    }

    public SchemaLookUp(boolean isFromPertnerBackend) {
        getSuggestBox().setAutoSelectEnabled(false);
        this.isFromPertnerBackend = isFromPertnerBackend;
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {

    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
        filterParametrs.setFromPartnerBackend(isFromPertnerBackend);
        AllInOneService.App.get().getSchemasAsSelectItem(filterParametrs, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable caught) {
            }

            @Override
            public void success(SelectItem[] result) {
                oracle.clear();
                setItems(filterParametrs.getSearchKey(), result);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                SchemaLookUp.super.getSuggestBox().showSuggestions(searchKey);
            }
        });
    }
}
