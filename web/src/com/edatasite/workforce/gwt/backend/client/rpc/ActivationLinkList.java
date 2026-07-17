package com.edatasite.workforce.gwt.backend.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ActivationLinkList implements IsSerializable {
    private ActivationLinkListItem[] results;

    public ActivationLinkList() {

    }

    public ActivationLinkList(ActivationLinkListItem[] results) {
        this.results = results;
    }

    public ActivationLinkListItem[] getResults() {
        return results;
    }

    public void setResults(ActivationLinkListItem[] results) {
        this.results = results;
    }


}
