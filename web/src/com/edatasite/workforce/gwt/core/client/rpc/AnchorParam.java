package com.edatasite.workforce.gwt.core.client.rpc;

public class AnchorParam {
    private String section;
    private String[] tokens;

    public AnchorParam(String section, String[] tokens) {
        this.section = section;
        this.tokens = tokens;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String[] getTokens() {
        return tokens;
    }

    public void setTokens(String[] tokens) {
        this.tokens = tokens;
    }

}
