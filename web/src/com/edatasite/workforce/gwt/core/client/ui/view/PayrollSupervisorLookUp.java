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
 * Date: 03/03/22
 * Time: 5:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class PayrollSupervisorLookUp extends LookUp {
    private Command listener;

    public PayrollSupervisorLookUp() {

    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {

    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
        filterParametrs.setLookUp(true);
        AllInOneService.App.get().getPayrollSupervisorForLookUp(filterParametrs, new AbstractAsyncCallback<SelectItem>() {
            @Override
            public void failure(Throwable caught) {
            }

            @Override
            public void success(SelectItem result) {
                setSelected(result.getId(), result.getName());
                PayrollSupervisorLookUp.super.getOracle().setFullSearch(true);
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
