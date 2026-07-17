package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseResource;

/**
 * Created by umakarimov on 9/30/15.
 */
public class Office365Attendee extends Office365BaseResource {
    private String type;
    private Office365ResponseStatus status;

    /**
     * @see https://graph.microsoft.io/GraphDocuments/api-reference/v1.0/resources/attendee.htm
     */
    public Office365Attendee() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Office365ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(Office365ResponseStatus status) {
        this.status = status;
    }
}
