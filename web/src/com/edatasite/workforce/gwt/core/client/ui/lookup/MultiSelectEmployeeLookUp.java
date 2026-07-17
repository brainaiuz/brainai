package com.edatasite.workforce.gwt.core.client.ui.lookup;

import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.multilookup.MultiSelectLookUp;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.HTMLPanel;

/**
 * Created with IntelliJ IDEA.
 * User: developer
 * Date: 5/2/12
 * Time: 5:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class MultiSelectEmployeeLookUp extends MultiSelectLookUp {
    private SelectItem employee = null;
    private HTMLPanel customPanel;
    private Command removeActionCommand;
    private Integer removedItemID;

    public MultiSelectEmployeeLookUp() {
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
        AllInOneService.App.get().getLookUpItems(filterParametrs, LookUpConstants.EMPLOYEE_ID, null, new AbstractAsyncCallback<SelectItem[]>() {
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
                    searchKey = filterParametrs.getSearchKey().replace(",", "").trim();
                } else {
                    if (filterParametrs.getSearchKey() != null && filterParametrs.getSearchKey().contains("<")) {
                        searchKey = filterParametrs.getSearchKey().substring(filterParametrs.getSearchKey().lastIndexOf("<") + 1).trim();
                    } else {
                        searchKey = filterParametrs.getSearchKey() == null ? "" : filterParametrs.getSearchKey();
                    }
                }
                MultiSelectEmployeeLookUp.super.getSuggestBox().showSuggestions(searchKey);
                getBox().getOracle().setFullSearch(true);
                if (customPanel != null) {
                    LoadingPanel.loading(false, customPanel);
                } else {
                    LoadingPanel.loading(false);
                }
            }
        });
    }

    public SelectItem selectCurrentUser() {
        if (employee == null) {
            AllInOneService.App.get().getCurrentUser(new AbstractAsyncCallback<SelectItem>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(SelectItem result) {
                    employee = result;
                    setSelectedItems(employee);
                    LoadingPanel.loading(false);
                }
            });
        } else {
            setSelectedItems(employee);
            return employee;
        }
        return employee;
    }

    public HTMLPanel getCustomPanel() {
        return customPanel;
    }

    public void setCustomPanel(HTMLPanel customPanel) {
        this.customPanel = customPanel;
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
