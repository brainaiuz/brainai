package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsBotActivation;
import com.edatasite.workforce.gwt.core.server.db.BotActivationManager;
import org.springframework.stereotype.Repository;

@Repository("botActivationKeyManager")
public class BotActivationManagerImpl extends BaseManager<EdsBotActivation> implements BotActivationManager {

    public BotActivationManagerImpl() {
        super(EdsBotActivation.class);
    }

    @Override
    public EdsBotActivation getByKey(String key) {
        return (EdsBotActivation) findSingle("from EdsBotActivation ba where ba.key=?", key);
    }

    @Override
    public EdsBotActivation getByEmail(String email) {
        return (EdsBotActivation) findSingle("from EdsBotActivation ba where ba.username=?", email);
    }
}
