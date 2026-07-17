package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseResource;

/**
 * Created by umakarimov on 9/30/15.
 */
public class Office365Recipient extends Office365BaseResource {
    private Office365EmailAddress emailAddress;

    /**
     * @see https://graph.microsoft.io/GraphDocuments/api-reference/v1.0/resources/recipient.htm
     */
    public Office365Recipient() {
    }

    public Office365EmailAddress getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(Office365EmailAddress emailAddress) {
        this.emailAddress = emailAddress;
    }
}
