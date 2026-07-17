package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseResource;

/**
 * Created by umakarimov on 9/30/15.
 */
public class Office365IdentitySet extends Office365BaseResource {

    private Office365Identity user;
    private Office365Identity device;
    private Office365Identity application;

    /**
     * @see http://graph.microsoft.io/GraphDocuments/api-reference/v1.0/resources/identityset.htm
     */
    public Office365IdentitySet() {
    }

    public Office365Identity getUser() {
        return user;
    }

    public void setUser(Office365Identity user) {
        this.user = user;
    }

    public Office365Identity getDevice() {
        return device;
    }

    public void setDevice(Office365Identity device) {
        this.device = device;
    }

    public Office365Identity getApplication() {
        return application;
    }

    public void setApplication(Office365Identity application) {
        this.application = application;
    }
}
