package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.core.client.rpc.ReportService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * User: Jamshid on 18.12.21
 */

public class PositionLookUp extends LookUp {

    private Integer objectId;

    public PositionLookUp() {
    }

    public PositionLookUp(Integer objectId) {
        this.objectId = objectId;
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    protected void lookUpService(final ListingFilterParameter filter) {
        filter.setLookUp(true);
        ReportService.App.get().getPositionsList(filter, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable throwable) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void onSuccess(SelectItem[] selectItems) {
                setItems(filter.getSearchKey(), selectItems);
                String searchKey = filter.getSearchKey() == null ? "" : filter.getSearchKey();
                PositionLookUp.super.getSuggestBox().showSuggestions(searchKey);
            }
        });
    }

    @Override
    public void clear() {
        super.clear();
        oracle.clearItems();
        refreshOracle(true);
        getTextBox().setText(wfmStrings.searchTypeMessage());
    }
}
