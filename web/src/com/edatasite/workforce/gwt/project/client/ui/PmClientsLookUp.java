package com.edatasite.workforce.gwt.project.client.ui;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;

/**
 * Created by IntelliJ IDEA.
 * User: Jamshid's
 * Date: 04-Oct-2010
 * Time: 06:24:35
 */
public class PmClientsLookUp extends LookUp {
    private Integer projectID;
    private boolean isFirst = true;
    public static WfmStrings wfmStrings = WfmStrings.App.get();

    public PmClientsLookUp() {
        getSuggestBox().setAutoSelectEnabled(true);
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {
        addListener(PmClientsLookUp.this, WfmUiEventType.ON_CLIENT_EDIT, WfmUiEventType.ON_CLIENT_DELETED);
        onClientAdd();
    }

    private void onClientAdd() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CLIENT_ADD, PmClientsLookUp.this, (sender, args) -> {
            if (args != null && args instanceof Integer) {
                ProjectService.App.get().getClient((Integer) args, new AbstractAsyncCallback<SelectItem>() {
                    @Override
                    public void success(SelectItem result) {
                        addItem(result);
                    }
                });
            }
        });
    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
        filterParametrs.setProjectId(projectID);
//        LoadingPanel.get().show(wfmStrings.searching());
        ProjectService.App.get().searchClientsByProjectId(projectID, filterParametrs.getSearchKey(), new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(SelectItem[] result) {
                PmClientsLookUp.super.getOracle().setFullSearch(true);
                setItems(filterParametrs.getSearchKey(), result);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                PmClientsLookUp.super.getSuggestBox().showSuggestions(searchKey);
                LoadingPanel.loading(false);

            }
        });


    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }
}
