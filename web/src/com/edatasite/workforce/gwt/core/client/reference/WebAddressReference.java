package com.edatasite.workforce.gwt.core.client.reference;

import com.edatasite.workforce.gwt.core.client.ui.Constants;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 05-Jul-2010
 * Time: 22:12:35
 */
public enum WebAddressReference {
    HOME(Constants.G_HOME),
    WORK(Constants.G_WORK),
    HOMEPAGE(Constants.G_HOME_PAGE),
    FTP(Constants.G_FTP),
    BLOG(Constants.G_BLOG),
    PROFILE(Constants.G_PROFILE),
    OTHER(Constants.G_OTHER),
    LINKEDIN(Constants.G_LINKEDIN),
    FACEBOOK(Constants.G_FACEBOOK),
    TWITTER(Constants.G_TWITTER),
    INSTAGRAM(Constants.G_INSTAGRAM);


    WebAddressReference(int id) {
        this.id = id;
    }

    private int id;

    public int getId() {
        return id;
    }

    public int getParentId() {
        return CRMContactRelatedReference.WEBSITES.getId();
    }
}
