package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsTelegramChat;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * User: Abror Abdukadirov
 * Date: 18.05.2017 3:48
 */
public interface TelegramChatManager extends Manager<EdsTelegramChat> {

    Integer getListCount(ListingFilterParameter fp);

    List<EdsTelegramChat> getList(ListingFilterParameter fp);

    EdsTelegramChat getByChatId(Long chatId);

    EdsTelegramChat getById(Integer id);

    EdsTelegramChat getByChatIdAndBotToken(Long chatId, String botToken);

    List<EdsTelegramChat> getActiveChatsByType(String messageType);

    void deleteTelegramChatsRuleIds(Integer objectID);
}
