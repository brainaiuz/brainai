package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.accounting.EdsExchangeRateHistory;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ExchangeRateHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 2/11/13
 * Time: 8:23 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("exchangeRateHistoryManager")
public class ExchangeRateHistoryManagerImpl extends BaseManager<EdsExchangeRateHistory> implements ExchangeRateHistoryManager {

    @Autowired
    FinancialSettingsManager financialSettingsManager;

    public ExchangeRateHistoryManagerImpl() {
        super(EdsExchangeRateHistory.class);
    }

    public void registerExchangeRateHistory(BigDecimal exRate, EdsCurrency currency) {
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        if (fs != null && fs.isExRateHistoryEnabled()) {
            Integer exRateCalcScale = fs.getExchangeRateScale();
            EdsExchangeRateHistory exRateHistory = (EdsExchangeRateHistory) findSingle("select erh from EdsExchangeRateHistory erh where erh.currency = ? order by erh.date desc", currency);
            if (exRateHistory == null || exRateHistory.getExchangeRate().setScale(exRateCalcScale, RoundingMode.HALF_UP).compareTo(exRate.setScale(exRateCalcScale, RoundingMode.HALF_UP)) != 0) {
                createExchangeRateHistory(exRate, currency);
            }
        }
    }

    public void registerExpenseReportExRateHistory(EdsCurrency currency, BigDecimal expenseReportExRate) {
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        if (fs != null && fs.isExRateHistoryEnabled()) {
            Integer exRateCalcScale = fs.getExchangeRateScale();
            BigDecimal savedExRate = getExRateByCurrency(currency.getName());
            if (savedExRate == null || expenseReportExRate.setScale(exRateCalcScale, RoundingMode.HALF_UP).compareTo(BigDecimal.ONE.divide(savedExRate, exRateCalcScale, RoundingMode.HALF_UP)) != 0) {
                createExchangeRateHistory(expenseReportExRate, currency);
            }
        }
    }

    private void createExchangeRateHistory(BigDecimal exRate, EdsCurrency currency) {
        EdsExchangeRateHistory exRateHistory;
        exRateHistory = new EdsExchangeRateHistory();
        exRateHistory.setDate(new Date());
        exRateHistory.setCurrency(currency);
        exRateHistory.setExchangeRate(exRate);
        create(exRateHistory);
    }

    @Override
    public BigDecimal getExRateByCurrency(String currencyName) {
        return (BigDecimal) findSingle("select erh.exchangeRate from EdsExchangeRateHistory erh where erh.currency.name = ? order by erh.date desc", currencyName);
    }
}
