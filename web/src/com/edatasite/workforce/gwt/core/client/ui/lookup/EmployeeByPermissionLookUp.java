package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;


/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 23.03.14
 * Time: 22:25
 * To change this template use File | Settings | File Templates.
 */
public class EmployeeByPermissionLookUp extends EmployeeLookUp {

    String permissionCode;

    public EmployeeByPermissionLookUp() {
        super(true, false, true);
    }


    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
//        LoadingPanel.get().show("Searching...");
        filterParametrs.setParams(permissionCode);
        filterParametrs.setResignedEmployeesIncluded(true);
        AllInOneService.App.get().getEmployeesByPermessionCode(filterParametrs, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(SelectItem[] result) {
                setItems(filterParametrs.getSearchKey(), result);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                EmployeeByPermissionLookUp.super.getSuggestBox().showSuggestions(searchKey);
                LoadingPanel.loading(false);
            }
        });
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    @Override
    public void clear() {
        super.clear();
        oracle.clearItems();
        refreshOracle(true);
        getTextBox().setText(wfmStrings.searchTypeMessage());
        getTextBox().getElement().getStyle().setColor("#999999");
    }

}

