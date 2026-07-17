package com.edatasite.workforce.gwt.core.server.app;

/**
 * User: Abror Abdukadirov
 * Date: 03.06.2017 1:09
 */
public interface TelegramBlackListService {

    void saveChat(Long chatId, String chatName);

    boolean validate(Long chatId);

    void deleteByChatId(Long chatId);
}
