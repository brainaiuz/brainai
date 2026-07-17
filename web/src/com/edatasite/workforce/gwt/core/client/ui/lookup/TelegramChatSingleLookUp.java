package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramChatService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TelegramChatSingleLookUp extends LookUp {
    private String accessToken;

    public TelegramChatSingleLookUp() {
        super();
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {
    }


    @Override
    public void lookUpService(ListingFilterParameter filterParametrs) {
        if (accessToken != null) {
            LoadingPanel.loading(true);
            filterParametrs.setAccessToken(accessToken);
            TelegramChatService.App.get().getChatListAsSelectItem(filterParametrs, new AsyncCallback<SelectItem[]>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
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
                    TelegramChatSingleLookUp.super.getSuggestBox().showSuggestions(searchKey);
                    LoadingPanel.loading(false);
                }
            });
        }
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
