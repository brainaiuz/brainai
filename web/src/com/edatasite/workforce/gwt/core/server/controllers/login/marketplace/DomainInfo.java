package com.edatasite.workforce.gwt.core.server.controllers.login.marketplace;

import java.io.Serializable;

/**
 * User: Anvarbek
 * Date: May 12, 2010
 * Time: 11:09:05 AM
 */
public class DomainInfo implements Serializable {

    public DomainInfo() {
    }

    public DomainInfo(boolean domainExists, boolean userExists) {
        this.domainExists = domainExists;
        this.userExists = userExists;
    }

    private boolean domainExists;
    private boolean userExists;

    public boolean isDomainExists() {
        return domainExists;
    }

    public void setDomainExists(boolean domainExists) {
        this.domainExists = domainExists;
    }

    public boolean isUserExists() {
        return userExists;
    }

    public void setUserExists(boolean userExists) {
        this.userExists = userExists;
    }
}
