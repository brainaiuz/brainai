package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.multilookup.MultiSelectLookUp;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTMLPanel;

public class MultiSelectVacancyLookUp extends MultiSelectLookUp {
    private HTMLPanel customPanel;

    public MultiSelectVacancyLookUp() {
        super();
        if (getBox() != null) {
            getBox().setStartFromTHLetter(1);
        }
    }

    @Override
    public void onActionPerformed(int type) {
        super.onActionPerformed(type);
    }

    @Override
    public boolean onCondition(String text) {
        return false;
    }

    @Override
    public void onLookUpService(final ListingFilterParameter filterParametrs) {
        if (customPanel != null) {
            LoadingPanel.loading(true, customPanel);
        } else {
            LoadingPanel.loading(true);
        }
        filterParametrs.setLookUpBy(Constants.BY_NAME);
        filterParametrs.setType(LookUpConstants.VACANCIES);
        AllInOneService.App.get().getLookUpItems(filterParametrs, LookUpConstants.VACANCIES,null, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable throwable) {
                if (customPanel != null) {
                    LoadingPanel.loading(false, customPanel);
                } else {
                    LoadingPanel.loading(false);
                }
                GWT.log(throwable.getMessage());
            }

            @Override
            public void success(SelectItem[] result) {
                setItems(filterParametrs.getSearchKey(), result);
                String searchKey = "";
                if (filterParametrs.getSearchKey() != null && filterParametrs.getSearchKey().trim().contains(",")) {
                    searchKey = filterParametrs.getSearchKey().replaceAll(",", "").trim();
                } else {
                    if (filterParametrs.getSearchKey() != null && filterParametrs.getSearchKey().contains("<")) {
                        searchKey = filterParametrs.getSearchKey().substring(filterParametrs.getSearchKey().lastIndexOf("<") + 1).trim();
                    } else {
                        searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                    }
                }
                MultiSelectVacancyLookUp.super.getSuggestBox().showSuggestions(searchKey);
                getBox().getOracle().setFullSearch(true);
                if (customPanel != null) {
                    LoadingPanel.loading(false, customPanel);
                } else {
                    LoadingPanel.loading(false);
                }
            }
        });
    }

    public HTMLPanel getCustomPanel() {
        return customPanel;
    }

    public void setCustomPanel(HTMLPanel customPanel) {
        this.customPanel = customPanel;
    }
}
