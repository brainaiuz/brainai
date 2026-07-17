package com.edatasite.workforce.gwt.core.server.db.impl;


import com.edatasite.workforce.core.domain.EdsWebHookHistory;
import com.edatasite.workforce.gwt.core.server.db.WebHookHistoryManager;
import org.springframework.stereotype.Repository;

@Repository("webHookHistoryManagerImpl")
public class WebHookHistoryManagerImpl extends BaseManager<EdsWebHookHistory> implements WebHookHistoryManager {
    public WebHookHistoryManagerImpl() {
        super(EdsWebHookHistory.class);

    }

}