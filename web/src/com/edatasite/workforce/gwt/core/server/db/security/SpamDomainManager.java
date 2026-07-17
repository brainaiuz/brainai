package com.edatasite.workforce.gwt.core.server.db.security;

import com.edatasite.workforce.core.domain.security.EdsSpamDomain;
import com.edatasite.workforce.gwt.core.server.db.Manager;

public interface SpamDomainManager extends Manager<EdsSpamDomain> {

    EdsSpamDomain findByHost(String host);
}
