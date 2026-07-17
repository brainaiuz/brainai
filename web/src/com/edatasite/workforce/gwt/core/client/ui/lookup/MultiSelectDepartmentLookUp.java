package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.multilookup.MultiSelectLookUp;
import com.google.gwt.core.client.GWT;

public class MultiSelectDepartmentLookUp extends MultiSelectLookUp {

    public MultiSelectDepartmentLookUp() {
        super();
        if (getBox() != null) {
            getBox().setStartFromTHLetter(1);
        }
    }

    @Override
    public boolean onCondition(String text) {
        return false;
    }

    @Override
    public void onLookUpService(final ListingFilterParameter filterParameters) {
        filterParameters.setLookUpBy(Constants.BY_NAME);
        filterParameters.setHRMS(true);
        AllInOneService.App.get().getLookUpItems(filterParameters, LookUpConstants.HR_DEPARTMENT_ID,null, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable throwable) {
                GWT.log(throwable.getMessage());
            }

            @Override
            public void success(SelectItem[] result) {
                clearOracleItems();
                MultiSelectDepartmentLookUp.super.getSuggestBox().setLimit(result.length);
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
                MultiSelectDepartmentLookUp.super.getSuggestBox().showSuggestions(searchKey);
                getBox().getOracle().setFullSearch(true);
            }
        });
    }
}
