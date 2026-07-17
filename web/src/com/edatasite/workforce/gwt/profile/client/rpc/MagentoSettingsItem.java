package com.edatasite.workforce.gwt.profile.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.io.Serializable;

/**
 * Created by Shohruh on 09 Jan 2017.
 */
public class MagentoSettingsItem implements Serializable {
    private String apiUrl;
    private String apiUser;
    private String apiKey;
    private SelectItem user;
    private RecurrenceJobItem recurrenceJobItem;

    public MagentoSettingsItem() {
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getApiUser() {
        return apiUser;
    }

    public void setApiUser(String apiUser) {
        this.apiUser = apiUser;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public SelectItem getUser() {
        return user;
    }

    public void setUser(SelectItem user) {
        this.user = user;
    }

    public RecurrenceJobItem getRecurrenceJobItem() {
        return recurrenceJobItem;
    }

    public void setRecurrenceJobItem(RecurrenceJobItem recurrenceJobItem) {
        this.recurrenceJobItem = recurrenceJobItem;
    }
}
