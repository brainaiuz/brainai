package com.edatasite.workforce.gwt.crm.client.ui;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.LookUp;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.MassMailService;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Feb 26, 2011
 * Time: 7:43:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class FromEmailLookUp extends LookUp {

    public static CrmStrings strings = CrmStrings.App.get();
    private SelectItem[] resultItems;

    public FromEmailLookUp(final TextBox fullName) {
        getSuggestBox().setAutoSelectEnabled(false);
        getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            for (SelectItem s : resultItems) {
                if (getSelectedItem() != null && getSelectedItem().getId() != null && getSelectedItem().getId().equals(s.getId())) {
                    fullName.setValue(s.getDescription() != null ? s.getDescription() : "");
                }
            }
        });
    }

    @Override
    protected void onItemDeleteInsertUpdate(int type) {

    }

    @Override
    protected void lookUpService(final ListingFilterParameter filterParametrs) {
//        LoadingPanel.get().show(strings.searching());
        MassMailService.App.get().getFromEmailsAsSelectItem(filterParametrs.getSearchKey(), new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable caught) {
//                LoadingPanel.loading(false);
            }

            @Override
            public void success(SelectItem[] result) {
                resultItems = result;
                setItems(filterParametrs.getSearchKey(), result);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                FromEmailLookUp.super.getSuggestBox().showSuggestions(searchKey);
//                LoadingPanel.loading(false);
            }
        });


    }

}
