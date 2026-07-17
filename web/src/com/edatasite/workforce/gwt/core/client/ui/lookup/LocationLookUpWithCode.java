package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.google.gwt.user.client.Command;

/**
 * Created by Akhmadov Bekhruz on 03/02/23.
 */

public class LocationLookUpWithCode extends LookUp {
    private Command listener;
    private Integer parentId;

    public LocationLookUpWithCode() {
    }

    public LocationLookUpWithCode(Integer parentId) {
        this.parentId = parentId;
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {

    }

    @Override
    protected void lookUpService(ListingFilterParameter filterParametrs) {
        filterParametrs.setLookUp(true);
        filterParametrs.setParentID(this.parentId);
        AllInOneService.App.get().getLocationsWithCodeAsSelectItem(filterParametrs, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable caught) {
            }

            @Override
            public void success(SelectItem[] result) {
                LocationLookUpWithCode.super.getOracle().setFullSearch(true);
                setItems(filterParametrs.getSearchKey(), result);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                LocationLookUpWithCode.super.getSuggestBox().showSuggestions(searchKey);
            }
        });
    }

    public Command getOnSelectListener() {
        return listener;
    }

    public void setOnSelectListener(Command onSelectListener) {
        this.listener = onSelectListener;
    }
}
