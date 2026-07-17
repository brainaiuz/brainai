package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;

/**
 * Created by Djuraev on 8/15/15.
 */
public class EmployeeLookUpWithCode extends LookUp {
    private String permissionCode;
    private String contextCode;
    private boolean showResignedEmployees;
    private SelectItem employee = null;
    private boolean isCRM;

    public EmployeeLookUpWithCode() {
        this(true, false, false);
    }

    public EmployeeLookUpWithCode(boolean listsEmployee, boolean isCRM, boolean showResignedEmployees) {
        super(true);
        this.isCRM = isCRM;
        this.showResignedEmployees = showResignedEmployees;
    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
//        LoadingPanel.get().show("Searching...");
        filterParametrs.setParams(permissionCode);
        filterParametrs.setCRM(isCRM);
        filterParametrs.setResignedEmployeesIncluded(showResignedEmployees);
        if (getContextCode() != null) {
            filterParametrs.setModule(getContextCode());
        }
        AllInOneService.App.get().getEmployeesAsSelectItem(filterParametrs, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(SelectItem[] result) {
                EmployeeLookUpWithCode.super.getOracle().setFullSearch(true);
                setItems(filterParametrs.getSearchKey(), result);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                EmployeeLookUpWithCode.super.getSuggestBox().showSuggestions(searchKey);
            }
        });
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }

    public String getContextCode() {
        return contextCode;
    }

    public void setContextCode(String contextCode) {
        this.contextCode = contextCode;
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {

    }

    public void selectCurrentUser() {
        if (employee == null) {
            AllInOneService.App.get().getCurrentUser(new AbstractAsyncCallback<SelectItem>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(SelectItem result) {
                    employee = result;
                    setSelected(employee);
                    LoadingPanel.loading(false);
                }
            });
        } else {
            setSelected(employee);
        }
    }
}
