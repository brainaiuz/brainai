package com.edatasite.workforce.gwt.core.client.reference;

import com.edatasite.workforce.gwt.core.client.ui.Constants;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 05-Jul-2010
 * Time: 22:12:09
 */
public enum PhoneReference {
    HOME(Constants.G_HOME),
    WORK(Constants.G_WORK),
    MOBILE(Constants.G_MOBILE),
    FAX(Constants.G_FAX),
    HOMEFAX(Constants.G_HOME_FAX),
    WORKFAX(Constants.G_WORK_FAX),
    PAGER(Constants.G_PAGER),
    OTHER(Constants.G_OTHER),
    WHATS_APP(Constants.G_WHATS_APP),
    TELEGRAM(Constants.G_TELEGRAM),
    VIBER(Constants.G_VIBER),
    EXTENSION(Constants.G_EXTENSION);

    PhoneReference(int id) {
        this.id = id;
    }

    private int id;

    public int getId() {
        return id;
    }

    public int getParentId() {
        return CRMContactRelatedReference.PHONES.getId();
    }
}
