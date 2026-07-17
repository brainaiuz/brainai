package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.LogHistoryItem;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CandidateStatusHistoryGrid extends TaskStatusHistoryGrid {

    public CandidateStatusHistoryGrid(Integer objectId) {
        super(objectId);
    }

    @Override
    public void refresher() {
        AllInOneService.App.get().getAllStatusHistories(objectId, new AsyncCallback<LogHistoryItem[]>() {

            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(LogHistoryItem[] result) {
                if (result.length > 0) {
                    supplyProvider(result);
                    reDrawItems();
                }
            }
        });
    }
}
