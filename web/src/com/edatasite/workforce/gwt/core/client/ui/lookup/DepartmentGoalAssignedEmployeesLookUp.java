package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;

public class DepartmentGoalAssignedEmployeesLookUp extends LookUp {
    private SelectItem employee = null;
    private final Integer departmentGoalId;

    public DepartmentGoalAssignedEmployeesLookUp(Integer departmentGoalId) {
        super(true);
        this.departmentGoalId = departmentGoalId;
        fetchInitData();
    }

    private void fetchInitData() {
        HrmsService.App.get().getDepartmentGoalAssignedEmployees(this.departmentGoalId, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(SelectItem[] result) {
                DepartmentGoalAssignedEmployeesLookUp.super.getOracle().setFullSearch(true);
                setItemsandSelect(result);
            }
        });

    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {
    }

    @Override
    protected void lookUpService(ListingFilterParameter filterParametrs) {
        if (filterParametrs != null) {
            filterParametrs.setObjectId(this.departmentGoalId);
            HrmsService.App.get().getDepartmentGoalAssignedEmployees(filterParametrs.getObjectId(), new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void failure(Throwable throwable) {
                }

                @Override
                public void success(SelectItem[] result) {
                    DepartmentGoalAssignedEmployeesLookUp.super.getOracle().setFullSearch(true);
                    setItems(result, filterParametrs.getSearchKey());
                    String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                    DepartmentGoalAssignedEmployeesLookUp.super.getSuggestBox().showSuggestions(searchKey);
                }
            });
        }
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