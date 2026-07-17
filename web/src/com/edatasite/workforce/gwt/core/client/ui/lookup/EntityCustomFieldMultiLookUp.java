package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.multilookup.MultiSelectLookUp;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class EntityCustomFieldMultiLookUp extends MultiSelectLookUp {

    private String query;

    public EntityCustomFieldMultiLookUp() {
    }

    @Override
    public boolean onCondition(String text) {
        return false;
    }

    public EntityCustomFieldMultiLookUp(String query) {
        this.query = query;
    }

    @Override
    public void onLookUpService(ListingFilterParameter filterParametrs) {
        if (query != null) {
            AllInOneService.App.get().getEntityCustomFieldLookUpData(query,null,  new AsyncCallback<SelectItem[]>() {
                @Override
                public void onFailure(Throwable caught) {
//                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(SelectItem[] result) {
                    setItems(filterParametrs.getSearchKey(), result);
                    String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                    EntityCustomFieldMultiLookUp.super.getSuggestBox().showSuggestions(searchKey);
                }
            });
        }
    }

}