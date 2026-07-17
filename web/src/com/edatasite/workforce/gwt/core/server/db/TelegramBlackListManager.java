package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsTelegramBlackList;

/**
 * User: Abror Abdukadirov
 * Date: 03.06.2017 1:17
 */
public interface TelegramBlackListManager extends Manager<EdsTelegramBlackList> {

    EdsTelegramBlackList getByChatId(Long chatId);
}
