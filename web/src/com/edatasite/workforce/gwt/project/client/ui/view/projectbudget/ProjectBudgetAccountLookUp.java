package com.edatasite.workforce.gwt.project.client.ui.view.projectbudget;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/17/12
 * Time: 5:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProjectBudgetAccountLookUp extends LookUp {
    private String accountType;

    public ProjectBudgetAccountLookUp(String accountType) {
        this.accountType = accountType;
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
//        LoadingPanel.get().show(wfmStrings.searching());
        filterParametrs.setAccountType(accountType);
        ProjectService.App.get().getAccountsForProjectBudget(filterParametrs, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onSuccess(SelectItem[] result) {
                LoadingPanel.loading(false);
                setItems(filterParametrs.getSearchKey(), result);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                ProjectBudgetAccountLookUp.super.getSuggestBox().showSuggestions(searchKey);
            }
        });
    }
}
