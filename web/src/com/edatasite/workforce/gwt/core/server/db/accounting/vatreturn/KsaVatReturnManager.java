package com.edatasite.workforce.gwt.core.server.db.accounting.vatreturn;

import com.edatasite.workforce.core.domain.accounting.EdsVatReturn;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.Date;
import java.util.List;

public interface KsaVatReturnManager extends Manager<EdsVatReturn> {
    List<Object[]> getInvoiceTaxableTransactions(Date toDate, Integer returnId, String taxRateKey, String transactionType, boolean domestic);

    List<Object[]> getExpenseTaxableTransactions(Date toDate, Integer returnId, String taxRateKey);

    List<Object[]> getReverseChargeApplicableTransactions(Date toDate, Integer returnId);
}
