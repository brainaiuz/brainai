package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseResource;

/**
 * Created by umakarimov on 9/30/15.
 */
public class Office365SearchResult extends Office365BaseResource {
    private String onClickTelemetryUrl;

    /**
     * @param data
     * @see http://graph.microsoft.io/GraphDocuments/api-reference/v1.0/resources/searchresult.htm
     */
    public Office365SearchResult() {
    }

    public String getOnClickTelemetryUrl() {
        return onClickTelemetryUrl;
    }

    public void setOnClickTelemetryUrl(String onClickTelemetryUrl) {
        this.onClickTelemetryUrl = onClickTelemetryUrl;
    }
}
