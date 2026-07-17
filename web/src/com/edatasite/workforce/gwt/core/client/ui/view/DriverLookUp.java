package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.finnetlimited.reportservice.core.client.gwtrpc.CoreService;
import com.google.gwt.user.client.Command;

/**
 * User: Dilsh0d Madrahimov
 * Date: 15/11/16
 * Time: 5:44 PM
 */
public class DriverLookUp extends LookUp {
    private Command listener;
    private boolean showResignedEmployees;


    public DriverLookUp() {
        super(true);
    }

    public DriverLookUp(boolean showResignedEmployees) {
        super(true);
        this.showResignedEmployees = showResignedEmployees;
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {

    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
//        LoadingPanel.get().show("Searching...");
        filterParametrs.setResignedEmployeesIncluded(showResignedEmployees);
        CoreService.App.get().getDriversForLookUp(filterParametrs, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable caught) {
//                LoadingPanel.loading(false);
            }

            @Override
            public void success(SelectItem[] result) {
                setItems(filterParametrs.getSearchKey(), result);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                DriverLookUp.super.getOracle().setFullSearch(true);
                DriverLookUp.super.getSuggestBox().showSuggestions(searchKey);
//                LoadingPanel.loading(false);
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
