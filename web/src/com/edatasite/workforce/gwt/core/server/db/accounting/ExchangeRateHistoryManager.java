package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.accounting.EdsExchangeRateHistory;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 2/11/13
 * Time: 8:23 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ExchangeRateHistoryManager extends Manager<EdsExchangeRateHistory> {

    void registerExchangeRateHistory(BigDecimal exRate, EdsCurrency currency);

    void registerExpenseReportExRateHistory(EdsCurrency currency, BigDecimal expenseReportExRate);

    BigDecimal getExRateByCurrency(String currencyName);
}
