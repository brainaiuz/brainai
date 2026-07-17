package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.task.client.ui.TaskLogHistoryListView;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

public class CandidateLogHistoryListView extends TaskLogHistoryListView implements Constants {

    public CandidateLogHistoryListView(Integer entityID) {
        super(CANDIDATE_UPDATES_LIST, entityID);
        this.entityID = entityID;
    }

    @Override
    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.CandidateHistoryListView, getColumns(), getRequestProvider(), getPanelDesigner());

        add(list);
        return null;
    }

    protected ListingRequestProvider<HistoryItem> getRequestProvider() {
        return (listingFilterParameter, listingCallback) -> {
            listingFilterParameter.setEntityID(entityID);
            AllInOneService.App.get().getCandidateUpdatesList(listingFilterParameter, new AsyncCallback<ListResult<HistoryItem>>() {
                @Override
                public void onFailure(Throwable throwable) {
                }

                @Override
                public void onSuccess(ListResult<HistoryItem> result) {
                    listingCallback.onSuccess(result);
                }
            });
        };
    }
}
