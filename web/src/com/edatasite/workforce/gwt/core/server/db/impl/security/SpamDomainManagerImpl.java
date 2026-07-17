package com.edatasite.workforce.gwt.core.server.db.impl.security;

import com.edatasite.workforce.core.domain.security.EdsSpamDomain;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.security.SpamDomainManager;
import org.springframework.stereotype.Repository;

@Repository
public class SpamDomainManagerImpl extends BaseManager<EdsSpamDomain> implements SpamDomainManager {

    public SpamDomainManagerImpl() {
        super(EdsSpamDomain.class);
    }

    @Override
    public EdsSpamDomain findByHost(String host) {
        return (EdsSpamDomain) findSingle("from EdsSpamDomain where lower(host) = ? ", host);
    }
}
