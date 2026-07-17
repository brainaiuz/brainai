package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseResource;

/**
 * Created by umakarimov on 9/30/15.
 */
public class Office365Deleted extends Office365BaseResource {
    private String state;

    /**
     * @see http://graph.microsoft.io/GraphDocuments/api-reference/v1.0/resources/deleted.htm
     */
    public Office365Deleted() {
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
