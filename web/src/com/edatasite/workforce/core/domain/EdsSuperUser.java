package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class EdsSuperUser extends EdsObject {

    @Column(name = "isSuperUser", columnDefinition = "boolean default false")
    private boolean isSuperUser = false;

    public boolean isSuperUser() {
        return isSuperUser;
    }

    public void setSuperUser(boolean superUser) {
        isSuperUser = superUser;
    }
}
