package com.edatasite.workforce.gwt.core.client.reference;

import com.edatasite.workforce.gwt.core.client.ui.Constants;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 05-Jul-2010
 * Time: 22:13:01
 */
public enum AddressReference {
    HOME(Constants.G_HOME),
    WORK(Constants.G_WORK),
    OTHER(Constants.G_OTHER);

    AddressReference(int id) {
        this.id = id;
    }

    private int id;

    public int getId() {
        return id;
    }

    public int getParentId() {
        return CRMContactRelatedReference.ADDRESSES.getId();
    }

    public boolean equals(Integer id) {
        return id != null && this.getId() == id;
    }
}
