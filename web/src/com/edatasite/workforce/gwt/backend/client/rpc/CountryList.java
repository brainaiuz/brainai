package com.edatasite.workforce.gwt.backend.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CountryList implements IsSerializable {
    private CountryListItem[] results;

    public CountryList() {

    }

    public CountryList(CountryListItem[] results) {
        this.results = results;
    }

    public CountryListItem[] getResults() {
        return results;
    }

    public void setResults(CountryListItem[] results) {
        this.results = results;
    }


}
