package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.crm.EdsCustomCrmAccount;

public interface CustomCrmAccountManager extends Manager<EdsCustomCrmAccount> {

    EdsCustomCrmAccount getCustomCrmAccountByEntityTypeAndEntityId(Integer entityId, String entityType);
}
