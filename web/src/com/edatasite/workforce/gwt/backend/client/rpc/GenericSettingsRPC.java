package com.edatasite.workforce.gwt.backend.client.rpc;

import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * Created by User on 7/13/2016.
 */
public class GenericSettingsRPC implements IsSerializable {
    public static final String KEY = "key";
    public static final String DESCRIPTION = "description";
    public static final String VALUE = "value";

    private GenericSettingsEnum key;
    private String description;
    private boolean enabled;

    public GenericSettingsEnum getKey() {
        return key;
    }

    public void setKey(GenericSettingsEnum key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
