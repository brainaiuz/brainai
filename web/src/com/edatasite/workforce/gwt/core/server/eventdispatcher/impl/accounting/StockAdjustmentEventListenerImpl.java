package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting;

import com.edatasite.workforce.core.domain.EdsStockAdjustment;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.gwt.core.server.db.StockAdjustmentManager;
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
public class StockAdjustmentEventListenerImpl extends CustomBusinessEventListenerAdapter {
    public static WfmType<EdsStockAdjustment> TYPE = new WfmType<>(EventTypes.stockAdjustmentEventListener);

    public static String STOCK_ADJUSTMENT_SUBMITTED = "STOCK_ADJUSTMENT_SUBMITTED";
    public static String STOCK_ADJUSTMENT_APPROVED = "STOCK_ADJUSTMENT_APPROVED";
    public static String STOCK_ADJUSTMENT_DECLINED = "STOCK_ADJUSTMENT_DECLINED";

    @Autowired
    private UserManager userManager;
    @Autowired
    private MyUpdateManager myUpdateManager;
    @Autowired
    private StockAdjustmentManager stockAdjustmentManager;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {

        if (EdsMyUpdate.ADD.equalsIgnoreCase(event.getEventType())) {
            onAddEvent(event);
        } else if (EdsMyUpdate.EDIT.equalsIgnoreCase(event.getEventType())) {
            onEditEvent(event);
        } else if (EdsMyUpdate.DELETE.equalsIgnoreCase(event.getEventType())) {
            onDeleteEvent(event);
        } else if (STOCK_ADJUSTMENT_SUBMITTED.equals(event.getEventType())) {
            onSendToApprover(event);
        } else if (STOCK_ADJUSTMENT_APPROVED.equals(event.getEventType())) {
            onApprove(event);
        } else if (STOCK_ADJUSTMENT_DECLINED.equals(event.getEventType())) {
            onDecline(event);
        }
    }

    public void onSendToApprover(EdsBusinessEvent event) {

        EdsUser creator = userManager.get(event.getSourceID());
        EdsStockAdjustment adjustment = stockAdjustmentManager.get(event.getEntityID());
        EdsUser receiver = null;
        if (isOk(adjustment.getCurrentApprover()) && isOk(adjustment.getCurrentApprover().getExactEmployee())) {
            receiver = adjustment.getCurrentApprover().getExactEmployee();
        }
        if (receiver != null) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerStockAdjustmentSendToApprover(adjustment.getObjectID(), creator, receiver.getObjectID(), event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setStatus(EventStatus.COMPLETED.name());
            } catch (Exception ex) {
                event.setStatus(EventStatus.FAILED.name());
            }
        }
    }

    public void onApprove(EdsBusinessEvent event) {

        EdsUser receiver = userManager.get(event.getSourceID());
        EdsStockAdjustment adjustment = stockAdjustmentManager.get(event.getEntityID());

        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerStockAdjustmentApproveUpdate(adjustment.getObjectID(), receiver, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    public void onDecline(EdsBusinessEvent event) {

        EdsUser receiver = userManager.get(event.getSourceID());
        EdsStockAdjustment adjustment = stockAdjustmentManager.get(event.getEntityID());

        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerStockAdjustmentDeclineUpdate(adjustment.getObjectID(), receiver, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setStatus(EventStatus.COMPLETED.name());
        } catch (Exception ex) {
            event.setStatus(EventStatus.FAILED.name());
        }
    }

    @Override
    public void onAddEvent(EdsBusinessEvent event) {

        EdsStockAdjustment adjustment = stockAdjustmentManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerStockAdjustmentAddUpdate(adjustment, creator, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setMyUpdatesItemAdd(true);
        } catch (Exception ex) {
            event.setMyUpdatesItemAdd(false);
            event.setStatus(EventStatus.FAILED.name());
        }
        if (event.isMyUpdatesItemAdd()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    @Override
    public void onEditEvent(EdsBusinessEvent event) {

        EdsStockAdjustment adjustment = stockAdjustmentManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerStockAdjustmentEditUpdate(adjustment, creator, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setMyUpdatesItemAdd(true);
        } catch (Exception ex) {
            event.setMyUpdatesItemAdd(false);
            event.setStatus(EventStatus.FAILED.name());
        }
        if (event.isMyUpdatesItemAdd()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {
        EdsStockAdjustment adjustment = stockAdjustmentManager.get(event.getEntityID());
        EdsUser creator = userManager.get(event.getSourceID());
        try {
            EdsMyUpdate myUpdate = myUpdateManager.registerStockAdjustmentDelete(adjustment, creator, event.getTime());
            myUpdate.setSuperUser(event.isSuperUser());
            event.setMyUpdatesItemAdd(true);
        } catch (Exception ex) {
            event.setMyUpdatesItemAdd(false);
            event.setStatus(EventStatus.FAILED.name());
        }
        if (event.isMyUpdatesItemAdd()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }
}
