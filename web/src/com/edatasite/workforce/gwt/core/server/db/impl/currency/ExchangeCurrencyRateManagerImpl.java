package com.edatasite.workforce.gwt.core.server.db.impl.currency;

import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.currency.EdsExchangeCurrencyRate;
import com.edatasite.workforce.gwt.core.server.db.currency.ExchangeCurrencyRateManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Shohruh
 * Date: 22.01.16
 * Time: 14:05:56
 * To change this template use File | Settings | File Templates.
 */
@Repository("exchangeCurrencyRateManager")
public class ExchangeCurrencyRateManagerImpl extends BaseManager<EdsExchangeCurrencyRate> implements ExchangeCurrencyRateManager {
    public ExchangeCurrencyRateManagerImpl() {
        super(EdsExchangeCurrencyRate.class);
    }

    @Override
    public void registerExchangeRateHistory(EdsCurrency baseCurrency, EdsCurrency currency, BigDecimal exRate, Date date, Date updateDate) {
        EdsExchangeCurrencyRate exchangeCurrencyRate = getExchangeRateByDate(baseCurrency.getObjectID(), currency.getObjectID(), date);
        if (exchangeCurrencyRate == null) {
            exchangeCurrencyRate = new EdsExchangeCurrencyRate();
        }
        exchangeCurrencyRate.setBaseCurrency(baseCurrency);
        exchangeCurrencyRate.setCurrency(currency);
        exchangeCurrencyRate.setExchangeRate(exRate);
        exchangeCurrencyRate.setDate(date);
        exchangeCurrencyRate.setUpdateTime(updateDate);
        exchangeCurrencyRate.setUpdater(getUser());
        createOrUpdate(exchangeCurrencyRate);
    }

    public EdsExchangeCurrencyRate getExchangeRateByDate(Integer baseCurrency, Integer currency, Date date) {
        return (EdsExchangeCurrencyRate) findSingle("select ecr from EdsExchangeCurrencyRate ecr where ecr.baseCurrency.objectID = ? and ecr.currency.objectID = ? and date(ecr.date) = ? order by ecr.date desc", baseCurrency, currency, date);
    }

    @Override
    public EdsExchangeCurrencyRate getExchangeRateHistoryByDate(Integer baseCurrency, Integer currency, Date date) {
        return (EdsExchangeCurrencyRate) findSingle("select ecr from EdsExchangeCurrencyRate ecr where ecr.baseCurrency.objectID = ? and ecr.currency.objectID = ? and ecr.date <= ? order by ecr.date desc", baseCurrency, currency, date);
    }

    @Override
    public void deleteExchangeRateHistory(Integer baseCurrency, Integer currency, Date date) {
        EdsExchangeCurrencyRate rate = getExchangeRateByDate(baseCurrency, currency, date);
        if (rate != null) {
            delete(rate);
        }
    }
}
