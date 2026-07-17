package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.crm.EdsCustomCrmAccount;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CustomCrmAccountManager;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository("customCrmAccountManager")
public class CustomCrmAccountManagerImpl extends BaseManager<EdsCustomCrmAccount> implements CustomCrmAccountManager {

    public CustomCrmAccountManagerImpl() {
        super(EdsCustomCrmAccount.class);
    }

    @Override
    public EdsCustomCrmAccount getCustomCrmAccountByEntityTypeAndEntityId(Integer entityId, String entityType) {
        Map<String, Object> map = new HashMap<>();
        map.put("entityType", entityType);
        return (EdsCustomCrmAccount) findSingleByNamedParams("select a from EdsCustomCrmAccount a where " + (entityId != null ? "a.entityId = " + entityId + " and " : "") + " a.entityType = :entityType and " + ServerUtils.checkForDeleted("a.deleted"), map);
    }
}
