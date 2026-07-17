package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.multilookup.MultiSelectLookUp;
import com.google.gwt.core.client.GWT;

public class MultiBrigadaLookUp extends MultiSelectLookUp {

    public MultiBrigadaLookUp() {
        super();
        if (getBox() != null) {
            getBox().setStartFromTHLetter(1);
        }
    }

    @Override
    public void onLookUpService(final ListingFilterParameter filterParameters) {
        filterParameters.setLookUpBy(Constants.BY_NAME);
        filterParameters.setHRMS(true);
        AvailabilityService.App.get().getBrigadasForLookUp(filterParameters, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable throwable) {
                GWT.log(throwable.getMessage());
            }

            @Override
            public void success(SelectItem[] result) {
                clearOracleItems();
                MultiBrigadaLookUp.super.getSuggestBox().setLimit(result.length);
                setItems(filterParameters.getSearchKey(), result);
                String searchKey = "";
                if (filterParameters.getSearchKey() != null && filterParameters.getSearchKey().trim().contains(",")) {
                    searchKey = filterParameters.getSearchKey().replace(",", "").trim();
                } else {
                    if (filterParameters.getSearchKey() != null && filterParameters.getSearchKey().contains("<")) {
                        searchKey = filterParameters.getSearchKey().substring(filterParameters.getSearchKey().lastIndexOf("<") + 1).trim();
                    } else {
                        searchKey = filterParameters.getSearchKey() == null ? "" : filterParameters.getSearchKey();
                    }
                }
                MultiBrigadaLookUp.super.getSuggestBox().showSuggestions(searchKey);
                getBox().getOracle().setFullSearch(true);
            }
        });
    }


    @Override
    public boolean onCondition(String text) {
        return false;
    }
}
