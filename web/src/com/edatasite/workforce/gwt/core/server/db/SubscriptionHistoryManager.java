package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsSubscriptionHistory;
import com.edatasite.workforce.core.domain.EdsUsagePlan;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: kMan
 * Date: Apr 3, 2009
 * Time: 2:40:34 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SubscriptionHistoryManager extends Manager<EdsSubscriptionHistory> {

    EdsSubscriptionHistory getLastSubscriptionHistory(EdsUsagePlan usagePlan);

    List<EdsSubscriptionHistory> getAllSubscriptionHistoryList(EdsReference freeTrial, EdsReference expired);
}
