package com.edatasite.workforce.gwt.profile.client.rpc;

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.HashMap;

/**
 * Created by Shohruh on 02-Feb-17.
 */
public class IntegrationSettingsItem implements IsSerializable {
    private HashMap<String, String> settings;

    public IntegrationSettingsItem() {
    }

    public HashMap<String, String> getSettings() {
        if (settings == null) settings = new HashMap<>();
        return settings;
    }

    public void setSettings(HashMap<String, String> settings) {
        this.settings = settings;
    }

    public void addSetting(String key, String value) {
        getSettings().put(key, value);
    }

    public String getSetting(String key) {
        String val = getSettings().get(key);
        return val != null ? val : "";
    }

    public void setTgUrl(String value) {
        addSetting(Constants.TARGET_URL, value);
    }

    public String getTgUrl() {
        return getSetting(Constants.TARGET_URL);
    }

    public void setTgUsername(String value) {
        addSetting(Constants.TARGET_USERNAME, value);
    }

    public String getTgUsername() {
        return getSetting(Constants.TARGET_USERNAME);
    }

    public void setTgPassword(String value) {
        addSetting(Constants.TARGET_PASSWORD, value);
    }

    public String getTgPassword() {
        return getSetting(Constants.TARGET_PASSWORD);
    }

    public void setTgController(String value) {
        addSetting(Constants.TARGET_CONTROLLER, value);
    }

    public String getTgController() {
        return getSetting(Constants.TARGET_CONTROLLER);
    }
}
