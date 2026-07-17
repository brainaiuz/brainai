package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsExchangeRate;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: dilsh0d
 * Date: 25/10/12
 * Time: 14:30
 * To change this template use File | Settings | File Templates.
 */
public interface ExchangeRateManager extends Manager<EdsExchangeRate> {
    List<Object[]> getCurrencyExchangeRateByPeriod(Date date, String currencyIds);

    Map<String, BigDecimal> getExchangeRatesByCurrency(Integer currencyId, Date startDate, Date endDate);
}
