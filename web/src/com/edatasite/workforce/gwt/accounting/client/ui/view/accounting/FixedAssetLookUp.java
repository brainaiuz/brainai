package com.edatasite.workforce.gwt.accounting.client.ui.view.accounting;

import com.edatasite.workforce.gwt.accounting.client.rpc.fixedAsset.FixedAssetService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 8/7/12
 * Time: 11:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class FixedAssetLookUp extends LookUp {
    @Override
    protected void onItemDeleteInsertUpdate(int type) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
//        LoadingPanel.get().show(wfmStrings.searching());
        FixedAssetService.App.get().getFixedAssetsForLookUp(filterParametrs, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
//                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(SelectItem[] result) {
//                LoadingPanel.loading(false);
                setItems(filterParametrs.getSearchKey(), result);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                FixedAssetLookUp.super.getSuggestBox().showSuggestions(searchKey);
            }
        });
    }
}
