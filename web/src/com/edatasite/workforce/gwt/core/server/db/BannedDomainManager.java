package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsBannedDomain;

public interface BannedDomainManager extends Manager<EdsBannedDomain> {

    boolean areEmailAndDomainBanned(String domainName);

}
