package com.edatasite.workforce.gwt.crm.client.ui;

import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 6/7/12
 * Time: 7:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class CrmAccountTree extends Composite implements CrmConstants, Constants {

    private VerticalPanel pnlContainer;

    private CrmAccountTreeLookUp level1;
    private CrmAccountTreeLookUp level2;
    private CrmAccountTreeLookUp level3;

    public CrmAccountTree() {
        initialize();
    }

    public SelectItem getSelectedItem() {
        if (level3.getSelectedItemID() != null && level3.getSelectedItemID() != 0) {
            return level3.getSelectedItem();
        }

        if (level2.getSelectedItemID() != null && level2.getSelectedItemID() != 0) {
            return level2.getSelectedItem();
        }

        if (level1.getSelectedItemID() != null && level1.getSelectedItemID() != 0) {
            return level1.getSelectedItem();
        }

        return null;
    }

    public Integer getSelectedItemID() {
        if (level3.getSelectedItemID() != null && level3.getSelectedItemID() != 0) {
            return level3.getSelectedItemID();
        }

        if (level2.getSelectedItemID() != null && level2.getSelectedItemID() != 0) {
            return level2.getSelectedItemID();
        }

        if (level1.getSelectedItemID() != null && level1.getSelectedItemID() != 0) {
            return level1.getSelectedItemID();
        }

        return null;
    }

    public void setSelectedItems(SelectItem[] items) {
        if (items != null && items.length > 0) {
            if (items.length > 0) {
                level1.addItem(items[0]);
            }

            if (items.length > 1) {
                level2.addItem(items[1]);
            }

            if (items.length > 2) {
                level3.addItem(items[items.length-1]);
            }
        }
    }

    private void initialize() {
        pnlContainer = new VerticalPanel();
        pnlContainer.setSpacing(5);

        level1 = new CrmAccountTreeLookUp(TREE_LEVEL_1, null);
        level1.getSuggestBox().addStyleName(DEFAULT_WIDTH);
        level1.getSuggestBox().setStyleName("file--CrmAccountTree");
        level1.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            level2.clear();
            level3.clear();
        });

        level2 = new CrmAccountTreeLookUp(TREE_LEVEL_2, level1);
        level2.getSuggestBox().addStyleName(DEFAULT_WIDTH);
        level2.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            if (level2.getSelectedItemID() != null && level2.getSelectedItemID() != 0) {
                level3.clear();

                initAccountTree(level2.getSelectedItemID());
            }
        });


        level3 = new CrmAccountTreeLookUp(TREE_LEVEL_3, level2);
        level3.getSuggestBox().addStyleName(DEFAULT_WIDTH);
        level3.getSuggestBox().addSelectionHandler(suggestionSelectionEvent -> {
            if (level3.getSelectedItemID() != null && level3.getSelectedItemID() != 0) {
                initAccountTree(level3.getSelectedItemID());
            }
        });

        pnlContainer.add(level1);
        pnlContainer.add(level2);
        pnlContainer.add(level3);
        initWidget(pnlContainer);
    }

    private void initAccountTree(Integer selectionItemID) {
        LoadingPanel.loading(true);
        AllInOneService.App.get().getParentAccountsTreeList(selectionItemID, new AsyncCallback<SelectItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                GWT.log(caught.getMessage());
            }

            @Override
            public void onSuccess(SelectItem[] items) {
                LoadingPanel.loading(false);
                if (items != null && items.length > 0) {
                    if (items.length > 0) {
                        level1.addItem(items[0]);
                    }

                    if (items.length > 1) {
                        level2.addItem(items[1]);
                    }
                }
            }
        });

    }
}
