package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsTelegramBlackList;
import com.edatasite.workforce.gwt.core.server.db.TelegramBlackListManager;
import org.springframework.stereotype.Repository;

/**
 * User: Abror Abdukadirov
 * Date: 03.06.2017 1:18
 */
@Repository("telegramBlackListManager")
public class TelegramBlackListManagerImpl extends BaseManager<EdsTelegramBlackList> implements TelegramBlackListManager {

    public TelegramBlackListManagerImpl() {
        super(EdsTelegramBlackList.class);
    }

    @Override
    public EdsTelegramBlackList getByChatId(Long chatId) {
        return (EdsTelegramBlackList) findSingle("select bl from EdsTelegramBlackList bl where bl.chatId=?", chatId);
    }
}
