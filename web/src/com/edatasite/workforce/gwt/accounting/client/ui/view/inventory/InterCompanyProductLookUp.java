package com.edatasite.workforce.gwt.accounting.client.ui.view.inventory;

import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 11/26/12
 * Time: 3:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class InterCompanyProductLookUp extends LookUp{
    @Override
    protected void onItemDeleteInsertUpdate(int type) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
//        LoadingPanel.get().show(wfmStrings.searching());
        ProductService.App.get().getInterCompanyProducts(filterParametrs, new AsyncCallback<SelectItem[]>(){
            @Override
            public void onFailure(Throwable caught) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onSuccess(SelectItem[] result) {
//                LoadingPanel.loading(false);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                setItems(searchKey, result);
                InterCompanyProductLookUp.super.getSuggestBox().showSuggestions(searchKey);
            }
        });
    }
}
