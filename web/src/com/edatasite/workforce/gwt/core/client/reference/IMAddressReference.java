package com.edatasite.workforce.gwt.core.client.reference;

import com.edatasite.workforce.gwt.core.client.ui.Constants;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 05-Jul-2010
 * Time: 22:15:56
 */
public enum IMAddressReference {
    GTALK(Constants.G_GOOGLE_TALK),
    AIM(Constants.G_AIM),
    YAHOO(Constants.G_YAHOO),
    SKYPE(Constants.G_SKYPE),
    QQ(Constants.G_QQ),
    MSN(Constants.G_MSN),
    ICQ(Constants.G_ICQ),
    JABBER(Constants.G_JABBER);

    IMAddressReference(int id) {
        this.id = id;
    }

    private int id;

    public int getId() {
        return id;
    }

    public int getParentId() {
        return CRMContactRelatedReference.IMADDRESSES.getId();
    }
}
