package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseResource;

/**
 * Created by umakarimov on 9/30/15.
 */
public class Office365SharingInvitation extends Office365BaseResource {
    private String type;
    private String webUrl;
    private Office365Identity application;

    /**
     * @param data
     * @see http://graph.microsoft.io/GraphDocuments/api-reference/v1.0/resources/sharinglink.htm
     */
    public Office365SharingInvitation() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public Office365Identity getApplication() {
        return application;
    }

    public void setApplication(Office365Identity application) {
        this.application = application;
    }
}
