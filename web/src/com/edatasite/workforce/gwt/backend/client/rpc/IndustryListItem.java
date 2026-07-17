package com.edatasite.workforce.gwt.backend.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class IndustryListItem implements IsSerializable {
    private String industry;
    private String systemUsedCount;
    private String systemUsedCountInPercentage;

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getSystemUsedCount() {
        return systemUsedCount;
    }

    public void setSystemUsedCount(String systemUsedCount) {
        this.systemUsedCount = systemUsedCount;
    }

    public String getSystemUsedCountInPercentage() {
        return systemUsedCountInPercentage;
    }

    public void setSystemUsedCountInPercentage(String systemUsedCountInPercentage) {
        this.systemUsedCountInPercentage = systemUsedCountInPercentage;
    }
}
