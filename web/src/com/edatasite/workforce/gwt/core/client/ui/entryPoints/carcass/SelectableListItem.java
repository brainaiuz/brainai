package com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass;

import gwt.material.design.client.ui.html.ListItem;

public class SelectableListItem extends ListItem {
    private boolean currentModule = false;

    public SelectableListItem(String... initialClasses) {
        super(initialClasses);
    }

    public boolean isCurrentModule() {
        return currentModule;
    }

    public void setCurrentModule(boolean currentModule) {
        this.currentModule = currentModule;
    }
}