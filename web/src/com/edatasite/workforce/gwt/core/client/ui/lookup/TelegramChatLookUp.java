package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramChatService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.multilookup.MultiSelectLookUp;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;

public class TelegramChatLookUp extends MultiSelectLookUp {
    private String accessToken;
    private HTMLPanel customPanel;

    public TelegramChatLookUp() {
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
    public void onActionPerformed(int type) {
        super.onActionPerformed(type);
    }

    @Override
    public void onLookUpService(ListingFilterParameter filterParametrs) {
        if (accessToken != null) {
            if (customPanel != null) {
                LoadingPanel.loading(true, customPanel);
            } else {
                LoadingPanel.loading(true);
            }
            filterParametrs.setAccessToken(accessToken);
            TelegramChatService.App.get().getChatListAsSelectItem(filterParametrs, new AsyncCallback<SelectItem[]>() {
                @Override
                public void onFailure(Throwable throwable) {
                    if (customPanel != null) {
                        LoadingPanel.loading(false, customPanel);
                    } else {
                        LoadingPanel.loading(false);
                    }
                    GWT.log(throwable.getMessage());
                }

                @Override
                public void onSuccess(SelectItem[] selectItems) {
                    setItems(filterParametrs.getSearchKey(), selectItems);
                    String searchKey = "";
                    if (filterParametrs.getSearchKey() != null && filterParametrs.getSearchKey().trim().contains(",")) {
                        searchKey = filterParametrs.getSearchKey().replace(",", "").trim();
                    } else {
                        if (filterParametrs.getSearchKey() != null && filterParametrs.getSearchKey().contains("<")) {
                            searchKey = filterParametrs.getSearchKey().substring(filterParametrs.getSearchKey().lastIndexOf("<")).trim();
                        } else {
                            searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                        }
                    }
                    TelegramChatLookUp.super.getSuggestBox().showSuggestions(searchKey);
                    getBox().getOracle().setFullSearch(true);
                    if (customPanel != null) {
                        LoadingPanel.loading(false, customPanel);
                    } else {
                        LoadingPanel.loading(false);
                    }
                }
            });
        }
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
