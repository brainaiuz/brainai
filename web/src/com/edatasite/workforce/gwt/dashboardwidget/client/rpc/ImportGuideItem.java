package com.edatasite.workforce.gwt.dashboardwidget.client.rpc;

import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.google.gwt.user.client.rpc.IsSerializable;

public class ImportGuideItem implements IsSerializable {
    private String name;
    private ImportTypeEnum type;
    private String url;
    private Boolean enabled;

    public ImportGuideItem() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImportTypeEnum getType() {
        return type;
    }

    public void setType(ImportTypeEnum type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
