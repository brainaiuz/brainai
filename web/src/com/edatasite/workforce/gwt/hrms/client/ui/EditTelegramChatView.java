package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramChatListItem;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramChatService;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramChatServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TelegramConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.finnetlimited.reportservice.core.client.gwtrpc.AbstractAsyncCallback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 * User: Abror Abdukadirov
 * Date: 01.08.2017 2:13
 */
public class EditTelegramChatView extends CustomForm implements Colapse, TelegramConstants {

    protected static final TelegramChatServiceAsync telegramChatService = TelegramChatService.App.get();
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    private HTML chatId;
    private HTML chatType;
    private HTML creator;
    private TextBox chatName;
    private DataListBox status;
    private KpiCheckBox sendCaseCreate;

    private String chat_edit_view = "chat_edit_view_";
    private static final String WIDTH = "180px";
    public TelegramChatListItem item;
    public Integer objectId;

    public EditTelegramChatView(Integer objectId) {
        super("addTelegramChat", "Edit Chat");
        this.objectId = objectId;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();

        chatId = new HTML("");
        chatId.ensureDebugId(chat_edit_view + "chatId");

        chatType = new HTML("");
        chatType.ensureDebugId(chat_edit_view + "chatType");

        creator = new HTML("");
        creator.ensureDebugId(chat_edit_view + "creator");

        chatName = new TextBox();
        chatName.setWidth(WIDTH);
        chatName.ensureDebugId(chat_edit_view + "chatName");

        status = new DataListBox();
        status.setWidth(WIDTH);
        status.ensureDebugId(chat_edit_view + "status");
        status.setItems(getStatuses());

        sendCaseCreate = new KpiCheckBox();
        sendCaseCreate.ensureDebugId(chat_edit_view + "sendCaseCreate");

        addTitleField(HRMS.TELEGRAM_CHAT.DETAILS, wfmStrings.details());
        addField(HRMS.TELEGRAM_CHAT.CHAT_ID, chatId, getTitle(hrmsStrings.chatId()));
        addField(HRMS.TELEGRAM_CHAT.CHAT_TYPE, chatType, getTitle(hrmsStrings.chatType()));
        addField(HRMS.TELEGRAM_CHAT.CHAT_NAME, chatName, getTitle(hrmsStrings.chatName(), true));
        addField(HRMS.TELEGRAM_CHAT.STATUS, status, getTitle(wfmStrings.status()));
        addField(HRMS.TELEGRAM_CHAT.CREATOR, creator, getTitle(wfmStrings.createdBy()));
        addTitleField(HRMS.TELEGRAM_CHAT.SEND_NOTIFICATION, wfmStrings.sendNotification());
        addField(HRMS.TELEGRAM_CHAT.SEND_CASE_CREATE, sendCaseCreate, getTitle(Property.get(Constants.CASE_LIST, wfmStrings.createCase(), wfmStrings.crmCase())));

        show();
        return null;
    }

    @Override
    protected void addButtons() {
        if (objectId != null) {
            addButton(wfmStrings.update(), null, (chat_edit_view + "update_button"), (ClickHandler) event -> updateData());
        }
    }

    @Override
    protected void getDataToFillFields() {
        telegramChatService.getChat(objectId, new AbstractAsyncCallback<TelegramChatListItem>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(TelegramChatListItem result) {
                item = result;
                if (item != null) {
                    setValuesToWidgets();
                }
            }
        });
    }

    private void setValuesToWidgets() {
        chatId.setHTML(String.valueOf(item.getChatId()));
        chatType.setHTML(getChatType(item.getChatType()));
        chatName.setText(item.getChatName());
        if (item.isActive()) {
            status.setSelected(1);
        } else {
            status.setSelected(0);
        }
        if (item.getCreator() != null) {
            creator.setHTML(item.getCreator().getName());
        } else {
            creator.setHTML("N/A");
        }
        sendCaseCreate.setValue(item.isSendCaseCreate());
    }

    private void updateData() {
        if (validation()) {
            enableButton(false);
            if (item == null) {
                item = new TelegramChatListItem();
            }
            item.setObjectId(objectId);
            item.setChatName(chatName.getText());
            if (status.getSelectedId() != null) {
                item.setActive(status.getSelectedId() != 0);
            }
            if (sendCaseCreate.getValue() != null) {
                item.setSendCaseCreate(sendCaseCreate.getValue());
            }
            LoadingPanel.loading(true);
            telegramChatService.updateChat(item, new AbstractAsyncCallback<Integer>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(true);
                }

                @Override
                public void success(Integer result) {
                    enableButton(true);
                    LoadingPanel.loading(true);
                    if (result != null) {
                        Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.telegram()), Info.Type.INFO);
                    }
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TELEGRAM_CHAT_EDIT, result, EditTelegramChatView.this);
                    closeTab();
                }
            });
        }
    }

    private boolean validation() {
        int error = 0;
        clearErrorStyle();
        error += markAsError(HRMS.TELEGRAM_CHAT.CHAT_NAME, chatName, chatName.getText() == null || "".equals(chatName.getText()));
        if (error > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private String getChatType(String chatType) {
        if (chatType != null) {
            if (GROUP_TYPE.equals(chatType)) {
                return "Group";
            } else if (MEMBER_TYPE.equals(chatType)) {
                return "Member";
            }
        }
        return "N/A";
    }

    public static SelectItem[] getStatuses() {
        SelectItem[] items = new SelectItem[2];
        items[0] = new SelectItem(0, wfmStrings.inactive());
        items[1] = new SelectItem(1, wfmStrings.active());
        return items;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.TELEGRAM_CHAT_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.EDIT;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}
