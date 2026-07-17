package com.edatasite.workforce.gwt.invoice.client.ui.view;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ProjectLookUp;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseService;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 6/22/11
 * Time: 2:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class AccountingEmployeeLookUp extends EmployeeLookUp {

    private ProjectLookUp projectLookUp;
    private String formType;

    public AccountingEmployeeLookUp() {
        super(true, false, true);
    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
        if (projectLookUp != null) {
            filterParametrs.setProjectId(projectLookUp.getSelectedItemID());
        }
        filterParametrs.setInvoiceType(formType);
        ExpenseService.App.get().getApproversForLookUp(filterParametrs, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(SelectItem[] result) {
                setItems(filterParametrs.getSearchKey(), result);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                AccountingEmployeeLookUp.super.getSuggestBox().showSuggestions(searchKey);
//                LoadingPanel.loading(false);
            }
        });
    }

    public void setProjectLookUp(ProjectLookUp projectLookUp) {
        this.projectLookUp = projectLookUp;
    }

    public void setFormType(String formType) {
        this.formType = formType;
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
