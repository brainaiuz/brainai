package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting;

import com.edatasite.workforce.core.domain.EdsExpensePayment;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.core.domain.myupdates.EdsMyUpdate;
import com.edatasite.workforce.gwt.core.server.db.ExpensePaymentManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.edatasite.workforce.gwt.core.server.db.myupdate.MyUpdateTypeManager.EXPENSE_PAYMENT;

/**
 * Created by User on 13.05.2016.
 */
@Transactional
public class ExpensePaymentEventListenerImpl extends CustomBusinessEventListenerAdapter {

    public static WfmType<EdsExpensePayment> TYPE = new WfmType<>(EventTypes.expensePaymentEventListener);

    @Autowired
    private ExpensePaymentManager expensePaymentManager;

    @Autowired
    UserManager userManager;
    @Autowired
    MyUpdateManager myUpdateManager;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EdsMyUpdate.ADD.equalsIgnoreCase(event.getEventType())) {
            onAddEvent(event);
        } else if (EdsMyUpdate.DELETE.equals(event.getEventType())) {
            onDeleteEvent(event);
        }
    }

    public void onAddEvent(EdsBusinessEvent event) {

        EdsUser creator = userManager.get(event.getSourceID());
        EdsExpensePayment expensePayment = expensePaymentManager.get(event.getEntityID());

        if (!event.isMyUpdatesItemAdd()) {
            try {
                EdsMyUpdate myUpdate = myUpdateManager.registerExpensePaymentAdd(expensePayment, creator, event.getTime());
                myUpdate.setSuperUser(event.isSuperUser());
                event.setMyUpdatesItemAdd(true);
            } catch (Exception ex) {
                event.setMyUpdatesItemAdd(false);
                event.setStatus(EventStatus.FAILED.name());
            }
        }
        if (event.isMyUpdatesItemAdd()) {
            event.setStatus(EventStatus.COMPLETED.name());
        }
    }

    @Override
    public void onDeleteEvent(EdsBusinessEvent event) {
        List<EdsMyUpdate> updates = myUpdateManager.getUpdates(event.getEntityID(), EXPENSE_PAYMENT, EdsMyUpdate.ADD);
        if(updates != null && updates.size() > 0) {
            for(EdsMyUpdate update : updates) {
                myUpdateManager.delete(update);
            }
        }
    }
}
