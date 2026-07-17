package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.google.gwt.user.client.Command;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 2/5/15
 * Time: 5:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class PayrollEmployeeLookUp extends LookUp {
    private Command listener;
    private boolean showResignedEmployees;
    private boolean showEmployeesWithResignationDate;
    private boolean showAllEmployees;

    public PayrollEmployeeLookUp(boolean showResignedEmployees) {
        this.showResignedEmployees = showResignedEmployees;
    }

    public PayrollEmployeeLookUp(boolean showResignedEmployees, boolean showEmployeesWithResignationDate) {
        this.showResignedEmployees = showResignedEmployees;
        this.showEmployeesWithResignationDate = showEmployeesWithResignationDate;
    }

    public PayrollEmployeeLookUp(boolean showResignedEmployees, boolean showEmployeesWithResignationDate, boolean showAllEmployees) {
        this.showResignedEmployees = showResignedEmployees;
        this.showEmployeesWithResignationDate = showEmployeesWithResignationDate;
        this.showAllEmployees = showAllEmployees;
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {

    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
//        LoadingPanel.get().show("Searching...");
        filterParametrs.setResignedEmployeesIncluded(showResignedEmployees);
        filterParametrs.setShowEmployeesWithResignationDate(showEmployeesWithResignationDate);
        filterParametrs.setAllEmployees(showAllEmployees);
        AllInOneService.App.get().getEmployeesForLookUp(filterParametrs, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable caught) {
//                LoadingPanel.loading(false);
            }

            @Override
            public void success(SelectItem[] result) {
                setItems(filterParametrs.getSearchKey(), result);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                PayrollEmployeeLookUp.super.getOracle().setFullSearch(true);
                PayrollEmployeeLookUp.super.getSuggestBox().showSuggestions(searchKey);
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
