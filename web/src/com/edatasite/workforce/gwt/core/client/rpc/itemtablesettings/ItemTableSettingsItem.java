package com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Normurod on 3/23/2017.
 */
public class ItemTableSettingsItem implements IsSerializable {
    private ColumnConfigs[] shownColumns;
    private ColumnConfigs[] allColumns;

    public ColumnConfigs[] getShownColumns() {
        return shownColumns;
    }

    public void setShownColumns(ColumnConfigs[] shownColumns) {
        this.shownColumns = shownColumns;
    }

    public ColumnConfigs[] getAllColumns() {
        return allColumns;
    }

    public void setAllColumns(ColumnConfigs[] allColumns) {
        this.allColumns = allColumns;
    }
}
