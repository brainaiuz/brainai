package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * User: Abror Abdukadirov
 * Date: 31.07.2017 21:14
 */
public interface TelegramChatServiceAsync {

    void getChatList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<TelegramChatListItem>> callback);

    void getChatListAsSelectItem(ListingFilterParameter filterParameter, AsyncCallback<SelectItem[]> callback);

    void getChat(Integer objectId, AsyncCallback<TelegramChatListItem> callback);

    void createChat(Long chatId, String chatName, AsyncCallback<Void> callback);

    void updateChat(TelegramChatListItem item, AsyncCallback<Integer> callback);

    void deleteChat(Integer objectId, AsyncCallback<Boolean> callback);

    void sendMessage(Long chatId, String chatName, String text, AsyncCallback<Void> callback);

    void sendCaseCreateMessage(Integer entityId, String messageType, Integer userId, AsyncCallback<Void> callback);

    void getTelegramSettingsList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<TelegramSettingsItem>> callback);

    void deleteTelegramSettingsItem(Integer id, AsyncCallback<Boolean> callback);

    void saveTelegramSettingsItem(TelegramSettingsItem telegramSettingsItem, AsyncCallback<Integer> callback);

    void getTelegramSettingsItem(Integer id, AsyncCallback<TelegramSettingsItem> callback);

    void saveTelegramChat(TelegramChatListItem telegramChatListItem, AsyncCallback<Void> callback);

    void getTelegramSettingsAsSelectItems(AsyncCallback<SelectItem[]> async);
}
