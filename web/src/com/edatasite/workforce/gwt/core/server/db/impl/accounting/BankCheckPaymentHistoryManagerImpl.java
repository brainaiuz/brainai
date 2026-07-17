package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsBankCheckPaymentHistory;
import com.edatasite.workforce.gwt.core.server.db.accounting.BankCheckPaymentHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/25/12
 * Time: 2:39 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("bankCheckPaymentHistoryManager")
public class BankCheckPaymentHistoryManagerImpl extends BaseManager<EdsBankCheckPaymentHistory> implements BankCheckPaymentHistoryManager {
    public BankCheckPaymentHistoryManagerImpl() {
        super(EdsBankCheckPaymentHistory.class);
    }

    @Override
    public List<EdsBankCheckPaymentHistory> getBankCheckPaymentHistoryList(Integer paymentID) {
        return find("select cph from EdsBankCheckPaymentHistory cph where cph.invoicePayment.objectID = ?", paymentID);
    }
}
