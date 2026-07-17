package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.InvoiceTermsItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.LinkedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/11/12
 * Time: 7:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class InvoiceTermsLookUp extends LookUp {

    private LinkedHashMap<Integer, InvoiceTermsItem> dataMap = new LinkedHashMap<>();

    public InvoiceTermsLookUp() {
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
//        LoadingPanel.loading(true);
        AllInOneService.App.get().getInvoiceTermsForLookUp(filterParametrs, new AsyncCallback<InvoiceTermsItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
//                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(InvoiceTermsItem[] result) {
//                LoadingPanel.loading(false);

                setItems(filterParametrs.getSearchKey(), result);
                initTermsItems(result);

                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                InvoiceTermsLookUp.super.getSuggestBox().showSuggestions(searchKey);
                InvoiceTermsLookUp.super.getOracle().setFullSearch(true);
            }
        });
    }

    private void initTermsItems(InvoiceTermsItem[] termsItems) {
        if (termsItems != null) {
            for (InvoiceTermsItem ti : termsItems) {
                dataMap.put(ti.getId(), ti);
            }
        }
    }

    public void addTermsItem(InvoiceTermsItem termsItem) {
        if (termsItem != null) {
            addItem(termsItem);
            dataMap.put(termsItem.getId(), termsItem);
        }
    }

    public InvoiceTermsItem getSelectedData() {
        if (getSelectedItemID() != null && dataMap.containsKey(getSelectedItemID())) {
            return dataMap.get(getSelectedItemID());
        }
        return null;
    }
}
