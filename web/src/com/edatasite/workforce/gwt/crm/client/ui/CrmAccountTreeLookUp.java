package com.edatasite.workforce.gwt.crm.client.ui;

import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 6/7/12
 * Time: 6:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrmAccountTreeLookUp extends LookUp implements CrmConstants {

    private String treeLevel;
    private CrmAccountTreeLookUp parent;

    public CrmAccountTreeLookUp(String treeLevel, CrmAccountTreeLookUp parent) {
        this.treeLevel = treeLevel;
        this.parent = parent;
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {
        addListener(CrmAccountTreeLookUp.this, WfmUiEventType.ON_ACCOUNT_DELETED, WfmUiEventType.ON_ACCOUNTS_ADD_EDIT);
    }

    @Override
    protected void lookUpService(final ListingFilterParameter fp) {
//        LoadingPanel.get().show("Searching...");
        fp.setLookUp(true);
        fp.setParentID(getParentID());

        AllInOneService.App.get().getAccountTreeLookUpItems(fp, treeLevel, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
//                LoadingPanel.loading(false);
                GWT.log(caught.getMessage());
            }

            @Override
            public void onSuccess(SelectItem[] result) {
//                LoadingPanel.loading(false);
                setItems(fp.getSearchKey(), result);
                String searchKey = fp.getSearchKey() == null ? "" : fp.getSearchKey();
                CrmAccountTreeLookUp.super.getSuggestBox().showSuggestions(searchKey);
            }
        });
    }

    private Integer getParentID() {
        if (parent != null && parent.getSelectedItemID() != null && parent.getSelectedItemID() != 0) {
            return parent.getSelectedItemID();
        }

        return null;
    }
}
