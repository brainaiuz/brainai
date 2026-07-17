package com.edatasite.workforce.gwt.accounting.client.ui.view.balancesheet;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Normurod on 2/20/2017.
 */
public class BalancesheetSettingsItem implements IsSerializable {

    private String code;
    private String title;

    private BalancesheetSettingsItem[] items;

    public BalancesheetSettingsItem() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BalancesheetSettingsItem[] getItems() {
        return items;
    }

    public void setItems(BalancesheetSettingsItem[] items) {
        this.items = items;
    }
}
