package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * User: Abror Abdukadirov
 * Date: 18.05.2017 4:04
 */
public interface TelegramChatService extends RemoteService {

    ListResult<TelegramChatListItem> getChatList(ListingFilterParameter fp);

    SelectItem[] getChatListAsSelectItem(ListingFilterParameter fp);

    TelegramChatListItem getChat(Integer objectId);

    void createChat(Long chatId, String chatName);

    void saveTelegramChat(TelegramChatListItem telegramChatListItem);

    Integer updateChat(TelegramChatListItem item);

    Boolean deleteChat(Integer objectId);

    void sendMessage(Long chatId, String chatName, String text);

    void sendCaseCreateMessage(Integer entityId, String messageType, Integer userId);

    ListResult<TelegramSettingsItem> getTelegramSettingsList(ListingFilterParameter fp);

    SelectItem[] getTelegramSettingsAsSelectItems();

    Boolean deleteTelegramSettingsItem(Integer id);

    Integer saveTelegramSettingsItem(TelegramSettingsItem telegramSettingsItem);

    TelegramSettingsItem getTelegramSettingsItem(Integer id);

    class App {
        public static TelegramChatServiceAsync get() {
            ServiceDefTarget target = GWT.create(CoreGenericService.class);
            target.setServiceEntryPoint(Utils.getHostNameURL() + "rpc/telegramChat");
            return (TelegramChatServiceAsync) target;
        }
    }
}
