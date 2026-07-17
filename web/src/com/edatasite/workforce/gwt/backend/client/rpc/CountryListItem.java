package com.edatasite.workforce.gwt.backend.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CountryListItem implements IsSerializable {
    private String country;
    private String systemUsedCount;
    private String systemUsedCountInPercentage;


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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
