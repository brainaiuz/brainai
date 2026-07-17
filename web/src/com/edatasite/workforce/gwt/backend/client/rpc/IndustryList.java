package com.edatasite.workforce.gwt.backend.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class IndustryList implements IsSerializable {
    private IndustryListItem[] results;

    public IndustryList() {

    }

    public IndustryList(IndustryListItem[] results) {
        this.results = results;
    }

    public IndustryListItem[] getResults() {
        return results;
    }

    public void setResults(IndustryListItem[] results) {
        this.results = results;
    }
}
