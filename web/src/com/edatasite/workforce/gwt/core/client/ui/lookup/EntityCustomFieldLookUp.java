package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * User: Dilshod Madrahimov
 * Date: July 26, 2016
 */
public class EntityCustomFieldLookUp extends LookUp {

    private String query;

    public EntityCustomFieldLookUp() {
    }

    public EntityCustomFieldLookUp(String query) {
        this.query = query;
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
//        LoadingPanel.get().show(wfmStrings.searching());
        String searchKey = filterParametrs.getSearchKey();
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
                    EntityCustomFieldLookUp.super.getSuggestBox().showSuggestions(searchKey);
                }
            });
        }
//        LoadingPanel.loading(false);
    }

    public void addItem(SelectItem item) {
        super.addItem(item);
    }

    public void addItemOnly(SelectItem item) {
        super.addItem(item);
    }


}