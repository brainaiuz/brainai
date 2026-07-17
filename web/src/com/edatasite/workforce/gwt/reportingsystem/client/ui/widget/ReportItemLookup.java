package com.edatasite.workforce.gwt.reportingsystem.client.ui.widget;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.reportingsystem.client.service.ReportingService;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;

public class ReportItemLookup extends LookUp {
    @Override
    protected void onItemDeleteInsertUpdate(int type) {

    }

    @Override
    protected void lookUpService(ListingFilterParameter filter) {
        ReportingService.App.get().lookupForReportItems(filter, new AsyncCallback<ArrayList<SelectItem>>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ArrayList<SelectItem> result) {
                setItems(null, result.toArray(new SelectItem[0]));
            }
        });
    }
}
