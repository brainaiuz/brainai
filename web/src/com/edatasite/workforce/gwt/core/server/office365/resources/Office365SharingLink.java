package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseResource;

/**
 * Created by umakarimov on 9/30/15.
 */
public class Office365SharingLink extends Office365BaseResource {
    private static final String NONE = "none";
    private static final String SAME = "same";
    private static final String OTHER = "other";

    private String email;
    private String redeemedBy;
    private Boolean signInRequired;

    /**
     * @param data
     * @see http://graph.microsoft.io/GraphDocuments/api-reference/v1.0/resources/sharinginvitation.htm
     */
    public Office365SharingLink() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRedeemedBy() {
        return redeemedBy;
    }

    public void setRedeemedBy(String redeemedBy) {
        this.redeemedBy = redeemedBy;
    }

    public Boolean getSignInRequired() {
        return signInRequired;
    }

    public void setSignInRequired(Boolean signInRequired) {
        this.signInRequired = signInRequired;
    }
}
