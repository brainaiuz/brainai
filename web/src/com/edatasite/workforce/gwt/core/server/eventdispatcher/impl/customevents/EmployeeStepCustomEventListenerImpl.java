package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents;

import com.edatasite.workforce.core.domain.EdsStepEmployee;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.gwt.core.server.db.StepEmployeeManager;
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
 * User: Abror Abdukadirov
 * Date: 14.06.2017 19:08
 */
@Transactional
public class EmployeeStepCustomEventListenerImpl extends CustomBusinessEventListenerAdapter {

    public static WfmType<EdsStepEmployee> TYPE = new WfmType<>(EventTypes.employeeStepEventListener);

    public static final String STATUS_SUBMITTED = "STATUS_SUBMITTED";
    public static final String STATUS_REJECTED = "STATUS_REJECTED";
    public static final String STATUS_APPROVED = "STATUS_APPROVED";

    @Autowired
    private StepEmployeeManager stepEmployeeManager;
    @Autowired
    private NotificationMsgManager notificationMsgManager;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (STATUS_SUBMITTED.equals(event.getEventType())) {
            onSubmitEvent(event);
        } else if (STATUS_REJECTED.equals(event.getEventType())) {
            onRejectEvent(event);
        } else if (STATUS_APPROVED.equals(event.getEventType())) {
            onApproveEvent(event);
        }
    }

    private void onSubmitEvent(EdsBusinessEvent event) {
        EdsStepEmployee edsStepEmployee = stepEmployeeManager.get(event.getEntityID());
        notificationMsgManager.updateClickedNotificationEvent(edsStepEmployee.getObjectID(), NotificationTypeEnum.OnboardingStep, ActionOnEntityEnum.WAIT_APPROVAL);
        notificationMsgManager.createStepEmployeeNotification(ActionOnEntityEnum.WAIT_APPROVAL, edsStepEmployee);
        event.setStatus(EventStatus.COMPLETED.name());
    }

    private void onRejectEvent(EdsBusinessEvent event) {
        EdsStepEmployee edsStepEmployee = stepEmployeeManager.get(event.getEntityID());
        notificationMsgManager.updateClickedNotificationEvent(edsStepEmployee.getObjectID(), NotificationTypeEnum.OnboardingStep, ActionOnEntityEnum.WAIT_APPROVAL);
        notificationMsgManager.createStepEmployeeNotification(ActionOnEntityEnum.REJECTED, edsStepEmployee);
        event.setStatus(EventStatus.COMPLETED.name());
    }

    private void onApproveEvent(EdsBusinessEvent event) {
        EdsStepEmployee edsStepEmployee = stepEmployeeManager.get(event.getEntityID());
        notificationMsgManager.updateClickedNotificationEvent(edsStepEmployee.getObjectID(), NotificationTypeEnum.OnboardingStep, ActionOnEntityEnum.WAIT_APPROVAL);
        notificationMsgManager.createStepEmployeeNotification(ActionOnEntityEnum.APPROVED, edsStepEmployee);
        event.setStatus(EventStatus.COMPLETED.name());
    }
}
