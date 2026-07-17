package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.google.gwt.user.client.Command;

/**
 * Created with IntelliJ IDEA.
 * User: Azam Ahmadjonov
 * Date: 01/27/22
 * Time: 5:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class PayrollLocationLookUp extends LookUp {
    private Command listener;

    public PayrollLocationLookUp() {

    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {

    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
        filterParametrs.setLookUp(true);
        AllInOneService.App.get().getPayrollLocationForLookUp(filterParametrs, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable caught) {
            }

            @Override
            public void success(SelectItem[] result) {
                setItems(filterParametrs.getSearchKey(), result);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                PayrollLocationLookUp.super.getOracle().setFullSearch(true);
                PayrollLocationLookUp.super.getSuggestBox().showSuggestions(searchKey);
            }
        });
    }

    public void setOnSelectListener(Command onSelectListener) {
        this.listener = onSelectListener;
    }

    public Command getOnSelectListener() {
        return listener;
    }
}
