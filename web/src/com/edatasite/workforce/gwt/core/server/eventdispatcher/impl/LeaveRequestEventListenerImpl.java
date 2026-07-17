package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl;

import com.edatasite.workforce.core.domain.EdsSickRequest;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.gwt.availability.server.app.AvailabilityServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.websocket.WebSocketServerObject;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.server.db.SickRequestManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.db.notification.NotificationMsgManager;
import com.edatasite.workforce.gwt.core.server.enums.NotificationTypeEnum;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BusinessEventListener;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import com.edatasite.workforce.gwt.core.server.rabbitmq.service.RabbitMQService;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.hrms.client.rpc.ActionOnEntityEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

/**
 * User: Ilhombek
 * Date: 10/28/11
 * Time: 1:19 PM
 */
@Transactional
public class LeaveRequestEventListenerImpl implements BusinessEventListener {
    public static WfmType<EdsSickRequest> TYPE = new WfmType<>(EventTypes.sickRequestEventListener);

    @Autowired
    private MyUpdateManager myUpdateManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private NotificationMsgManager notificationMsgManager;
    @Autowired
    private SickRequestManager sickRequestManager;
    @Autowired
    private AvailabilityServiceLocal availabilityServiceLocal;
    @Autowired
    private RabbitMQService rabbitMQService;

    public static final String LEAVE_REQUEST_APPROVED = "LEAVE_REQUEST_APPROVED";
    public static final String LEAVE_REQUEST_DENIED = "LEAVE_REQUEST_DENIED";
    public static final String LEAVE_REQUEST_REINDEX = "LEAVE_REQUEST_REINDEX";


    @Override
    public void onAddEvent(EdsBusinessEvent event) {
        EdsSickRequest edsSickRequest = sickRequestManager.get(event.getEntityID());
        notificationMsgManager.updateClickedNotificationEvent(edsSickRequest.getObjectID(), NotificationTypeEnum.LeaveRequests, ActionOnEntityEnum.WAIT_APPROVAL);
        notificationMsgManager.createLeaveRequestNotification(ActionOnEntityEnum.WAIT_APPROVAL, edsSickRequest);
    }

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        SecurityContext.getInstance().setStaticUserID(event.getSourceID());
        if (LEAVE_REQUEST_APPROVED.equals(event.getEventType())) {
            onApproved(event);
        }
        if (LEAVE_REQUEST_DENIED.equals(event.getEventType())) {
            onDenied(event);
        }
        if (LEAVE_REQUEST_REINDEX.equals(event.getEventType())) {
            indexLeave(event);
        }
    }

    private void indexLeave(EdsBusinessEvent event) {
        ListingFilterParameter fp = new ListingFilterParameter();
        if (LayoutRPC.LEAVE_REASON_FORM.equals(event.getEntityType())) {
            Integer reasonId = event.getSourceID();
            fp.setReasonID(reasonId);
//            availabilityServiceLocal.indexLeaveRequests(fp); //TODO review later
        }
    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {
        EdsUser deleter = userManager.get(event.getSourceID());
        String requestData = event.getCustomStringField();
        if (!event.isMyUpdatesItemDelete()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerLeaveRequestDeleteUpdate(event.getEntityID(), requestData, deleter, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemDelete(true);
            } catch (Exception ex) {
                event.setMyUpdatesItemDelete(false);
                event.setStatus(EventStatus.FAILED.name());
            }
            if (event.isMyUpdatesItemDelete()) {
                event.setStatus(EventStatus.COMPLETED.name());
            }
            notificationMsgManager.deleteNotificationEvent(event.getEntityID(), NotificationTypeEnum.LeaveRequests, ActionOnEntityEnum.WAIT_APPROVAL);
        }
    }

    private void onApproved(EdsBusinessEvent event) {
        EdsSickRequest edsSickRequest = sickRequestManager.get(event.getEntityID());
        notificationMsgManager.updateClickedNotificationEvent(edsSickRequest.getObjectID(), NotificationTypeEnum.LeaveRequests, ActionOnEntityEnum.WAIT_APPROVAL);
        notificationMsgManager.createLeaveRequestNotification(ActionOnEntityEnum.APPROVED, edsSickRequest);
        if (isOk(edsSickRequest.getOverallStatus()) && !EdsSickRequest.APPROVED.equals(edsSickRequest.getOverallStatus().getCode())) {
            notificationMsgManager.createLeaveRequestNotification(ActionOnEntityEnum.WAIT_APPROVAL, edsSickRequest);
        }
        WebSocketServerObject pushMessage = new WebSocketServerObject();
        pushMessage.setEventType(WfmUiEventType.ON_LEAVER_REQUEST_APPROVED);
//        WebSocketServletImpl.sendMessage(pushMessage);
        try {
            Integer userId = ((EdsUser)SecurityContext.getInstance().getUser()).getObjectID();
            pushMessage.setUserId(userId);
            rabbitMQService.sendWebPushNotification(pushMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onDenied(EdsBusinessEvent event) {
        EdsSickRequest edsSickRequest = sickRequestManager.get(event.getEntityID());
        notificationMsgManager.updateClickedNotificationEvent(edsSickRequest.getObjectID(), NotificationTypeEnum.LeaveRequests, ActionOnEntityEnum.WAIT_APPROVAL);
        notificationMsgManager.createLeaveRequestNotification(ActionOnEntityEnum.DENIED, edsSickRequest);
        if (isOk(edsSickRequest.getOverallStatus()) && !EdsSickRequest.DENIED.equals(edsSickRequest.getOverallStatus().getCode())) {
            notificationMsgManager.createLeaveRequestNotification(ActionOnEntityEnum.WAIT_APPROVAL, edsSickRequest);
        }
        WebSocketServerObject pushMessage = new WebSocketServerObject();
        pushMessage.setEventType(WfmUiEventType.ON_LEAVER_REQUEST_APPROVED);
//        WebSocketServletImpl.sendMessage(pushMessage);
        try {
            Integer userId = ((EdsUser)SecurityContext.getInstance().getUser()).getObjectID();
            pushMessage.setUserId(userId);
            rabbitMQService.sendWebPushNotification(pushMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {
        EdsSickRequest edsSickRequest = sickRequestManager.get(event.getEntityID());
        notificationMsgManager.updateClickedNotificationEvent(edsSickRequest.getObjectID(), NotificationTypeEnum.LeaveRequests, ActionOnEntityEnum.WAIT_APPROVAL);
        if (isOk(edsSickRequest.getOverallStatus()) && EdsSickRequest.DENIED.equals(edsSickRequest.getOverallStatus().getCode())) {
            notificationMsgManager.createLeaveRequestNotification(ActionOnEntityEnum.DENIED, edsSickRequest);
        } else {
            notificationMsgManager.createLeaveRequestNotification(ActionOnEntityEnum.APPROVED, edsSickRequest);
        }
    }
}
