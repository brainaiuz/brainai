package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseResource;

/**
 * Created by umakarimov on 9/30/15.
 */
public class Office365ResponseStatus extends Office365BaseResource {
    private String time;
    private String response;

    /**
     * @see https://graph.microsoft.io/GraphDocuments/api-reference/v1.0/resources/responsestatus.htm
     */
    public Office365ResponseStatus() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
