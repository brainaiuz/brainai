package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsBotImage;
import com.edatasite.workforce.gwt.core.server.db.BotManager;
import org.springframework.stereotype.Repository;

@Repository
public class BotManagerImpl extends BaseManager<EdsBotImage> implements BotManager {
    public BotManagerImpl() {
        super(EdsBotImage.class);
    }
}

