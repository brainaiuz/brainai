package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsBankCheckPaymentHistory;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/25/12
 * Time: 2:38 PM
 * To change this template use File | Settings | File Templates.
 */
public interface BankCheckPaymentHistoryManager extends Manager<EdsBankCheckPaymentHistory> {
    List<EdsBankCheckPaymentHistory> getBankCheckPaymentHistoryList(Integer paymentID);
}
