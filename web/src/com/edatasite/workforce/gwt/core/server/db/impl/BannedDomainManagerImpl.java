package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsBannedDomain;
import com.edatasite.workforce.gwt.core.server.db.BannedDomainManager;
import org.springframework.stereotype.Repository;

@Repository("bannedDomainManager")
public class BannedDomainManagerImpl extends BaseManager<EdsBannedDomain> implements BannedDomainManager {

    public BannedDomainManagerImpl() {
        super(EdsBannedDomain.class);
    }


    @Override
    public boolean areEmailAndDomainBanned(String email) {
        return (boolean) findNativeSingle("select count(id) > 0 from " + getPublic() + ".banneddomain bd where lower(bd.domainname) = ? or lower(bd.domainname) = ?", email.toLowerCase(), email.split("@")[1].toLowerCase());
    }
}
