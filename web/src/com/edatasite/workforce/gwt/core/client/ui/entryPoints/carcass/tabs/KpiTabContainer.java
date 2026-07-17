package com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.tabs;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.google.gwt.core.client.GWT;
import gwt.material.design.client.ui.MaterialTab;

import java.util.HashMap;
import java.util.LinkedList;

public class KpiTabContainer extends MaterialTab {

    /**
     * This tab always will render the current system container
     */
    private KpiTabItem mainTab;

    /**
     * Contains the selected one
     */
    private KpiTabItem selectedTab;

    /**
     * Contains tab list
     */
    private LinkedList<KpiTabItem> tabItems;

    /**
     * Contains list of tab items by SinksContainer
     */
    private HashMap<String, KpiTabItem> tabsMap;

    public KpiTabContainer() {
        super();

        initialize();
    }

    private void initialize() {
        //initialize tab list container
        tabItems = new LinkedList<>();

        //initialize main tab
        mainTab = new KpiTabItem();
        tabItems.add(mainTab);

        add(mainTab);

        //it will serve to contains all tab items
        tabsMap = new HashMap<>();

        initializeListeners();
    }

    private void initializeListeners() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ADD_TAB, KpiTabContainer.this, (sender, args) -> {
            SinksContainer container = (SinksContainer) args;

            if (container.isDynamic()) {
                addItem(container);
            } else {
                setMainTab(container);
            }
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_SELECT_TAB, KpiTabContainer.this, (sender, args) -> {
            SinksContainer container = (SinksContainer) args;

            if (tabsMap.get(container.getName()) != null) {
                KpiTabItem activeTab = tabsMap.get(container.getName());
                activeTab.onSelect();
            }
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.UPDATE_TAB_TITLE, KpiTabContainer.this, (sender, args) -> {
            SinksContainer container = (SinksContainer) args;

            if (tabsMap.get(container.getName()) != null) {
                KpiTabItem activeTab = tabsMap.get(container.getName());

                if (!container.isDynamic() && container.getWorkarea().getCurrentView() != null) {
                    activeTab.setTabName(container.getWorkarea().getCurrentView().getDescription());
                } else {
                    activeTab.setTabName(container.getDescription(), container.getTitle());
                }
            }
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_REMOVE_TAB, KpiTabContainer.this, (sender, args) -> {
            SinksContainer container = (SinksContainer) args;
            GWT.log("CONTAINER VIEW NAME: " + container.getPreparedView());
            GWT.log("CONTAINER NAME: " + container.getName());
            if (tabsMap.get(container.getName()) != null) {
                KpiTabItem closableTab = tabsMap.get(container.getName());

                closableTab.onClose();
                removeItem(closableTab);
            }
        });
    }


    public void addItem(SinksContainer container) {
        KpiTabItem tabItem = new KpiTabItem(container);
        tabItems.add(tabItem);
        tabsMap.put(container.getName(), tabItem);

        add(tabItem);

        setSelection(tabItem);
    }

    public void removeItem(KpiTabItem tabItem) {
        tabItems.remove(tabItem);
        tabsMap.remove(tabItem.getTabId());
        remove(tabItem);

        if (tabItem == selectedTab) {
            selectedTab = null;

            if (tabItems.size() > 0) {
                selectedTab = tabItems.get(tabItems.size() - 1);
                selectTab(selectedTab.getTabId());

                selectedTab.onSelect();
            }
        }
    }

    /**
     * @param container
     */
    public void setMainTab(SinksContainer container) {

        if (!Utils.isNullOrEmpty(mainTab.getTabId())) {
            tabsMap.remove(mainTab.getTabId());
        }
        mainTab.setContainer(container);
        tabsMap.put(container.getName(), mainTab);

        if (container.getWorkarea().getCurrentView() != null) {
            mainTab.setTabName(container.getWorkarea().getCurrentView().getDescription());
        }

        setSelection(mainTab);
    }

    public void setSelection(KpiTabItem tabItem) {
        selectedTab = tabItem;
        selectTab(tabItem.getTabId());
    }

    public KpiTabItem getSelectedTab() {
        return selectedTab;
    }

    public LinkedList<KpiTabItem> getTabItems() {
        return this.tabItems;
    }
}
