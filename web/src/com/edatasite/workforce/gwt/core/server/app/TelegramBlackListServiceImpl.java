package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.core.domain.EdsTelegramBlackList;
import com.edatasite.workforce.gwt.core.server.db.TelegramBlackListManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * User: Abror Abdukadirov
 * Date: 03.06.2017 1:09
 */
@Service("telegramBlackListService")
public class TelegramBlackListServiceImpl implements TelegramBlackListService {

    @Autowired
    private TelegramBlackListManager telegramBlackListManager;

    @Override
    @Transactional
    public void saveChat(Long chatId, String chatName) {
        if (chatId != null) {
            EdsTelegramBlackList edsBlackList = telegramBlackListManager.getByChatId(chatId);
            if (edsBlackList == null) {
                edsBlackList = new EdsTelegramBlackList();
            }
            edsBlackList.setChatId(chatId);
            if (chatName == null) {
                EdsTelegramBlackList edsBlackChat = telegramBlackListManager.getByChatId(chatId);
                if (edsBlackChat != null && edsBlackChat.getChatName() != null) {
                    chatName = edsBlackChat.getChatName();
                }
            }
            edsBlackList.setChatName(chatName);
            edsBlackList.setCreationDate(new Date());
            telegramBlackListManager.createOrUpdate(edsBlackList);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validate(Long chatId) {
        if (chatId != null) {
            return telegramBlackListManager.getByChatId(chatId) != null;
        }
        return false;
    }

    @Override
    @Transactional
    public void deleteByChatId(Long chatId) {
        if (chatId != null) {
            EdsTelegramBlackList edsBlackList = telegramBlackListManager.getByChatId(chatId);
            if (edsBlackList != null) {
                telegramBlackListManager.delete(edsBlackList);
            }
        }
    }
}
