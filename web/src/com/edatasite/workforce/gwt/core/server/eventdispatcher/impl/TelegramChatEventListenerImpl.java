package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramChatService;
import com.edatasite.workforce.gwt.core.client.ui.TelegramConstants;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Farrukh on 18-Jun-17.
 */
public class TelegramChatEventListenerImpl implements BusinessEventListener, TelegramConstants {
    public static WfmType<EdsObject> TYPE = new WfmType<>(EventTypes.telegramChatEventListener);

    @Autowired
    private TelegramChatService telegramChatService;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (SEND_CASE_CREATE.equals(event.getEventType())) {
            telegramChatService.sendCaseCreateMessage(event.getEntityID(), event.getEventType(), event.getSourceID());
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    @Override
    public void onAddEvent(EdsBusinessEvent event) {

    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {

    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {

    }
}
