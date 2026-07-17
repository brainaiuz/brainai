package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.multilookup.MultiSelectLookUp;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HTMLPanel;

public class LocationMultiLookUp extends MultiSelectLookUp {
    private HTMLPanel customPanel;

    private Command removeActionCommand;


    public LocationMultiLookUp() {
        super();
        setDisableBackSpaceRemove(true);
        if (getBox() != null) {
            getBox().setStartFromTHLetter(1);
        }
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
        filterParametrs.setLookUp(true);
        AllInOneService.App.get().getLocationsWithCodeAsSelectItem(filterParametrs, new AbstractAsyncCallback<SelectItem[]>() {
            @Override
            public void failure(Throwable caught) {
            }

            @Override
            public void success(SelectItem[] result) {
                setItems(filterParametrs.getSearchKey(), result);
                String searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                LocationMultiLookUp.super.getSuggestBox().showSuggestions(searchKey);
                getBox().getOracle().setFullSearch(true);
                if (customPanel != null) {
                    LoadingPanel.loading(false, customPanel);
                } else {
                    LoadingPanel.loading(false);
                }
            }
        });
    }
    public void setRemoveActionCommand(Command removeActionCommand) {
        this.removeActionCommand = removeActionCommand;
    }

    @Override
    public void runRemoveAction() {
        if (removeActionCommand != null) {
            removeActionCommand.execute();
        }
    }
}
