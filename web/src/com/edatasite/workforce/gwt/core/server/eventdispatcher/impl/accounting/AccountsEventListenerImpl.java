package com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.businessevent.EdsBusinessEvent;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.CustomBusinessEventListenerAdapter;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EventTypes;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.WfmType;
import com.edatasite.workforce.gwt.core.server.rabbitmq.enums.EventStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 10/31/11
 * Time: 4:45 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
public class AccountsEventListenerImpl extends CustomBusinessEventListenerAdapter {

    public static WfmType<EdsAccount> TYPE = new WfmType<>(EventTypes.accountsEventListener);

    public static String EVENT_ACCOUNT_CALCULATE_ACCOUNT_BALANCE = "EVENT_ACCOUNT_CALCULATE_ACCOUNT_BALANCE";

    @Autowired
    private AccountingManager accountingManager;

    @Override
    public void onCustomEvent(EdsBusinessEvent event) {
        if (EVENT_ACCOUNT_CALCULATE_ACCOUNT_BALANCE.equalsIgnoreCase(event.getEventType())) {
            onCalculateAccountBalance(event);
        }
    }

    public void onCalculateAccountBalance(EdsBusinessEvent event) {
        accountingManager.recalculateAccountBalances();
        event.setStatus(EventStatus.COMPLETED.name());
    }
}
