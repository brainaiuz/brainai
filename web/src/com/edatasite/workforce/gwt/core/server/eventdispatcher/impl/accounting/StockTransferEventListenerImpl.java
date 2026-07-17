package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting;

import com.edatasite.workforce.core.domain.EdsStockTransfer;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.gwt.core.server.db.StockTransferManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

@Transactional
public class StockTransferEventListenerImpl extends CustomBusinessEventListenerAdapter {
    public static WfmType<EdsStockTransfer> TYPE = new WfmType<>(EventTypes.stockTransferEventListener);

    public static String STOCK_TRANSFER_SUBMITTED = "STOCK_TRANSFER_SUBMITTED";
    public static String STOCK_TRANSFER_APPROVED = "STOCK_TRANSFER_APPROVED";
    public static String STOCK_TRANSFER_DECLINED = "STOCK_TRANSFER_DECLINED";
    public static String STOCK_TRANSFER_TRANSFERRED = "STOCK_TRANSFER_TRANSFERRED";

    @Autowired
    private UserManager userManager;
    @Autowired
    private MyUpdateManager myUpdateManager;
    @Autowired
    private StockTransferManager stockTransferManager;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {

        if (EdsMyUpdate.ADD.equalsIgnoreCase(event.getEventType())) {
            onAddEvent(event);
        } else if (EdsMyUpdate.EDIT.equalsIgnoreCase(event.getEventType())) {
            onEditEvent(event);
        } else if (EdsMyUpdate.DELETE.equalsIgnoreCase(event.getEventType())) {
            onDeleteEvent(event);
        }else if (STOCK_TRANSFER_SUBMITTED.equals(event.getEventType())) {
            onSendToApprover(event);
        } else if (STOCK_TRANSFER_APPROVED.equals(event.getEventType())) {
            onApprove(event);
        } else if (STOCK_TRANSFER_DECLINED.equals(event.getEventType())) {
            onDecline(event);
        } else if (STOCK_TRANSFER_TRANSFERRED.equals(event.getEventType())) {
            onTransfer(event);
        }
    }

    public void onSendToApprover(EdsBusinessEvent event) {

        EdsUser creator = userManager.get(event.getSourceID());
        EdsStockTransfer transfer = stockTransferManager.get(event.getEntityID());
        EdsUser receiver = null;
        if (isOk(transfer.getCurrentApprover()) && isOk(transfer.getCurrentApprover().getExactEmployee())) {
            receiver = transfer.getCurrentApprover().getExactEmployee();
        }
        if (receiver != null) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerStockTransferSendToApprover(transfer.getObjectID(), creator, receiver.getObjectID(), event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setStatus(EventStatus.COMPLETED.name());
            } catch (Exception ex) {
                event.setStatus(EventStatus.FAILED.name());
            }
        }
    }

    public void onApprove(EdsBusinessEvent event) {

        EdsUser receiver = userManager.get(event.getSourceID());
        EdsStockTransfer transfer = stockTransferManager.get(event.getEntityID());

        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerStockTransferApproveUpdate(transfer.getObjectID(), receiver, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    public void onDecline(EdsBusinessEvent event) {

        EdsUser receiver = userManager.get(event.getSourceID());
        EdsStockTransfer transfer = stockTransferManager.get(event.getEntityID());

        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerStockTransferDeclineUpdate(transfer.getObjectID(), receiver, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    public void onTransfer(EdsBusinessEvent event) {

        EdsUser receiver = userManager.get(event.getSourceID());
        EdsStockTransfer transfer = stockTransferManager.get(event.getEntityID());

        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerStockTransferTransferredUpdate(transfer.getObjectID(), receiver, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    @Override
    public void onAddEvent(EdsBusinessEvent event) {

        EdsStockTransfer transfer = stockTransferManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerStockTransferAddUpdate(transfer, creator, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setMyUpdatesItemAdd(true);
        } catch (Exception ex) {
            event.setMyUpdatesItemAdd(false);
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {

        EdsStockTransfer transfer = stockTransferManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerStockTransferEditUpdate(transfer, creator, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setMyUpdatesItemAdd(true);
        } catch (Exception ex) {
            event.setMyUpdatesItemAdd(false);
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {
        EdsStockTransfer transfer = stockTransferManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerStockTransferDelete(transfer, creator, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setMyUpdatesItemAdd(true);
        } catch (Exception ex) {
            event.setMyUpdatesItemAdd(false);
            event.setStatus(EventStatus.FAILED.name());
        }
    }
}
