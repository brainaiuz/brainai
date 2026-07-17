package com.edatasite.workforce.gwt.core.client.rpc.quickAddSettings;

import com.google.gwt.user.client.rpc.IsSerializable;

public class QuickAddColumnConfigs implements IsSerializable {
    private String code;
    private String name;
    private boolean required;
    private boolean selected;
    private int order;

    public QuickAddColumnConfigs() {
    }

    public QuickAddColumnConfigs(String code, String name, boolean required, boolean selected, int order) {
        this.code = code;
        this.name = name;
        this.required = required;
        this.selected = selected;
        this.order = order;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
