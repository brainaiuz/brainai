package com.edatasite.workforce.gwt.core.server.db.accounting.vatreturn;

import com.edatasite.workforce.core.domain.accounting.EdsVatReturn;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface UKVatReturnManager extends Manager<EdsVatReturn> {
    BigDecimal generateAccrualVATData(Date fromDate, Date toDate, int accountCode);

    BigDecimal getInvoicePaymentVATData(Date from, Date to, String type, boolean total);

    BigDecimal getSpendReceiveVATData(Date from, Date to, boolean receive, boolean total);

    BigDecimal getExpenseVATData(Date from, Date to, boolean total);

    List<Object[]> getInvoicePaymentTransactions(Date from, Date to, String type, boolean total);

    List<Object[]> getSpendReceiveTransactions(Date from, Date to, boolean receive, boolean total);
}
