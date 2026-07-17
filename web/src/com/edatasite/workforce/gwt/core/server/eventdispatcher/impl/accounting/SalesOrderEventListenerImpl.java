package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.MagentoService;
import com.edatasite.workforce.gwt.core.server.db.QuoteManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
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
 * Created by IntelliJ IDEA.
 * User: Atabek Boboyev
 * Date: 03.07.12
 * Time: 17:05
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class SalesOrderEventListenerImpl extends CustomBusinessEventListenerAdapter implements AccountingConstants {
    public static WfmType<EdsSaleQuote> TYPE = new WfmType<>(EventTypes.salesOrderEventListener);

    public static String EVENT_PICKLIST_SALE_ORDER = "EVENT_PICKLIST_SALE_ORDER";
    public static String EVENT_SEND_SALE_ORDER = "EVENT_SEND_SALE_ORDER";
    public static String EVENT_STATUS_CLOSED_SALE_ORDER = "EVENT_STATUS_CLOSED_SALE_ORDER";
    public static String EVENT_SALE_ORDER_MANAGER_APPROVE = "EVENT_SALE_ORDER_MANAGER_APPROVE";
    public static String EVENT_SALE_ORDER_MANAGER_REJECT = "EVENT_SALE_ORDER_MANAGER_REJECT";
    public static String EVENT_SALE_ORDER_SUBMITTED_TO_MANAGER = "EVENT_SALE_ORDER_SUBMITTED_TO_MANAGER";
    public static String EVENT_SALE_ORDER_CONVERT_FROM_SQ = "EVENT_SALE_ORDER_CONVERT_FROM_SQ";

    @Autowired
    private UserManager userManager;
    @Autowired
    private QuoteManager quoteManager;
    @Autowired
    private MagentoService magentoService;
    @Autowired
    private MyUpdateManager myUpdateManager;
    @Autowired
    private NotificationMsgManager notificationMsgManager;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EdsMyUpdate.ADD.equalsIgnoreCase(event.getEventType())) {
            onAddEvent(event);
        } else if (EdsMyUpdate.EDIT.equalsIgnoreCase(event.getEventType())) {
            onEditEvent(event);
        } else if (EdsMyUpdate.DELETE.equalsIgnoreCase(event.getEventType())) {
            onDeleteEvent(event);
        } else if (EVENT_PICKLIST_SALE_ORDER.equalsIgnoreCase(event.getEventType())) {
            onPicklistEvent(event);
        } else if (EVENT_SEND_SALE_ORDER.equalsIgnoreCase(event.getEventType())) {
            onOrderSendEvent(event);
        } else if (EVENT_STATUS_CLOSED_SALE_ORDER.equalsIgnoreCase(event.getEventType())) {
            onClosedEvent(event);
        } else if (EVENT_SALE_ORDER_MANAGER_APPROVE.equalsIgnoreCase(event.getEventType())) {
            onManagerApproveEvent(event);
        } else if (EVENT_SALE_ORDER_MANAGER_REJECT.equalsIgnoreCase(event.getEventType())) {
            onManagerRejectEvent(event);
        } else if (EVENT_SALE_ORDER_SUBMITTED_TO_MANAGER.equalsIgnoreCase(event.getEventType())) {
            onSubmittedToManager(event);
        } else if (EVENT_SALE_ORDER_CONVERT_FROM_SQ.equalsIgnoreCase(event.getEventType())) {
            convertFromSQ(event);
        }
    }

    @Override
    public void onAddEvent(EdsBusinessEvent event) {

        EdsUser creator = userManager.get(event.getSourceID());
        EdsSaleQuote quote = (EdsSaleQuote) quoteManager.get(event.getEntityID());

        if (!event.isMyUpdatesItemAdd()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerSaleOrderAddUpdate(quote, creator, event.getTime());
                if (myUpdate != null) {
                    myUpdate.setSuperUser(event.isSuperUser());
                }
                event.setMyUpdatesItemAdd(true);
            } catch (Exception ex) {
                event.setMyUpdatesItemAdd(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemAdd()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }

        if (quote.getOverallStatus() != null && Constants.SUBMITTED_TO_MANAGER.equals(quote.getOverallStatus().getCode())) {
            notificationMsgManager.updateClickedNotificationEvent(quote.getObjectID(), NotificationTypeEnum.SalesOrder, ActionOnEntityEnum.WAIT_APPROVAL);
            notificationMsgManager.createSalesOrderNotification(quote, creator);
        }

    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {

        EdsUser creator = userManager.get(event.getSourceID());
        EdsSaleQuote quote = (EdsSaleQuote) quoteManager.get(event.getEntityID());

        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerSaleOrderEditUpdate(quote, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemEdit(true);
            } catch (Exception ex) {
                event.setMyUpdatesItemEdit(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemEdit()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
        if (quote.getOverallStatus() != null && Constants.SUBMITTED_TO_MANAGER.equals(quote.getOverallStatus().getCode())) {
            notificationMsgManager.updateClickedNotificationEvent(quote.getObjectID(), NotificationTypeEnum.SalesOrder, ActionOnEntityEnum.WAIT_APPROVAL);
            notificationMsgManager.createSalesOrderNotification(quote, creator);
        }
    }

    public void onPicklistEvent(EdsBusinessEvent event) {

        EdsUser creator = userManager.get(event.getSourceID());
        EdsSaleQuote quote = (EdsSaleQuote) quoteManager.get(event.getEntityID());

        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerSaleOrderPickListUpdate(quote, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setStatus(EventStatus.COMPLETED.name());
            } catch (Exception ex) {
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemEdit()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    public void onClosedEvent(EdsBusinessEvent event) {

        EdsUser creator = userManager.get(event.getSourceID());
        EdsSaleQuote quote = (EdsSaleQuote) quoteManager.get(event.getEntityID());

        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerSaleOrderClosed(quote, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setStatus(EventStatus.COMPLETED.name());
            } catch (Exception ex) {
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemEdit()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {

        EdsUser creator = userManager.get(event.getSourceID());
        EdsSaleQuote salesOrder = (EdsSaleQuote) quoteManager.get(event.getEntityID());

        if (!event.isMyUpdatesItemEdit()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerSaleOrderDeleteUpdate(salesOrder, event.getCustomStringField(), creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemEdit(true);
            } catch (Exception ex) {
                event.setMyUpdatesItemEdit(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemEdit()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    public void onSubmittedToManager(EdsBusinessEvent event) {
        EdsUser user = userManager.get(event.getSourceID());
        EdsSaleQuote quote = (EdsSaleQuote) quoteManager.get(event.getEntityID());
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerSaleOrderSubmittedToManager(quote, user, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    public void convertFromSQ(EdsBusinessEvent event) {
        EdsUser user = userManager.get(event.getSourceID());
        EdsSaleQuote quote = (EdsSaleQuote) quoteManager.get(event.getEntityID());
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerSaleOrderConvertFromSQ(quote, user, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    private void onManagerApproveEvent(EdsBusinessEvent event) {
        EdsUser manager = userManager.get(event.getSourceID());
        EdsSaleQuote quote = (EdsSaleQuote) quoteManager.get(event.getEntityID());
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerSaleOrderManagerApproveUpdate(quote, manager, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }

    }

    private void onManagerRejectEvent(EdsBusinessEvent event) {
        EdsUser manager = userManager.get(event.getSourceID());
        EdsSaleQuote quote = (EdsSaleQuote) quoteManager.get(event.getEntityID());
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerSaleOrderManagerRejectUpdate(quote, manager, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    public void onOrderSendEvent(EdsBusinessEvent event) {
        EdsSaleQuote order = (EdsSaleQuote) quoteManager.get(event.getEntityID());
        magentoService.emailOrderToCustomer(order.getObjectID(), order.getClientOrSupplier());
    }

}
