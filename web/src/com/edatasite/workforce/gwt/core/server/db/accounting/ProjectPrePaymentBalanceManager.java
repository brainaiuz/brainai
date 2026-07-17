package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsProjectPrepaymentBalance;
import com.edatasite.workforce.gwt.core.server.db.Manager;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 11/3/11
 * Time: 8:41 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ProjectPrePaymentBalanceManager extends Manager<EdsProjectPrepaymentBalance> {
    EdsProjectPrepaymentBalance getCustomerPrePaymentBalance(Integer customerID);
}
