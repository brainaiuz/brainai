package com.edatasite.workforce.gwt.core.server.db.currency;

import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.currency.EdsExchangeCurrencyRate;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Shohruh
 * Date: 22.01.16
 * Time: 14:07:06
 * To change this template use File | Settings | File Templates.
 */
public interface ExchangeCurrencyRateManager extends Manager<EdsExchangeCurrencyRate>{
    void registerExchangeRateHistory(EdsCurrency baseCurrency, EdsCurrency currency, BigDecimal exRate, Date date, Date updateDate);

    EdsExchangeCurrencyRate getExchangeRateByDate(Integer baseCurrency, Integer currency, Date date);

    EdsExchangeCurrencyRate getExchangeRateHistoryByDate(Integer baseCurrency, Integer currency, Date date);

    void deleteExchangeRateHistory(Integer baseCurrency, Integer currency, Date date);
}
