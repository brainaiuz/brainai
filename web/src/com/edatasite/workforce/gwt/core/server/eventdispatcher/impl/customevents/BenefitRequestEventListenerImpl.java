package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents;

import com.edatasite.workforce.core.domain.EdsBenefitRequest;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.notification.NotificationMsgManager;
import com.edatasite.workforce.gwt.core.server.enums.NotificationTypeEnum;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.hrms.client.rpc.ActionOnEntityEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Djuraev on 8/19/15.
 */
@Transactional
public class BenefitRequestEventListenerImpl extends CustomBusinessEventListenerAdapter {

    public static WfmType<EdsBenefitRequest> TYPE = new WfmType<>(EventTypes.benefitRequestEventListener);
    public static final String BR_SUBMITTED = "BR_SUBMITTED";
    public static final String BR_APPROVED = "BR_APPROVED";
    public static final String BR_REJECTED = "BR_REJECTED";

    @Autowired
    private MessageManager messageManager;
    @Autowired
    private NotificationMsgManager notificationMsgManager;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        messageManager.sendBenefitRequestNotification(event.getEventType(), event.getEntityID(), event.getSourceID());
        if (BR_SUBMITTED.equals(event.getEventType())) {
            notificationMsgManager.createBenefitRequestNotification(ActionOnEntityEnum.WAIT_APPROVAL, event.getEntityID());
        } else if (BR_APPROVED.equals(event.getEventType())) {
            notificationMsgManager.updateClickedNotificationEvent(event.getEntityID(), NotificationTypeEnum.BenefitRequest, ActionOnEntityEnum.WAIT_APPROVAL);
            notificationMsgManager.createBenefitRequestNotification(ActionOnEntityEnum.APPROVED, event.getEntityID());
        } else if (BR_REJECTED.equals(event.getEventType())) {
            notificationMsgManager.updateClickedNotificationEvent(event.getEntityID(), NotificationTypeEnum.BenefitRequest, ActionOnEntityEnum.WAIT_APPROVAL);
            notificationMsgManager.createBenefitRequestNotification(ActionOnEntityEnum.REJECTED, event.getEntityID());
        }
        event.setStatus(EventStatus.COMPLETED.name());
    }
}
