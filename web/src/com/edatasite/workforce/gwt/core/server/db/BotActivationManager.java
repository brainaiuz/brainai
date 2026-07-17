package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsBotActivation;

public interface BotActivationManager extends Manager<EdsBotActivation> {
    EdsBotActivation getByKey(String key);

    EdsBotActivation getByEmail(String email);
}
