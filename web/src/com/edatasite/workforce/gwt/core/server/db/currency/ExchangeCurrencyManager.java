package com.edatasite.workforce.gwt.core.server.db.currency;

import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.currency.EdsExchangeCurrency;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Shohruh
 * Date: 08.01.16
 * Time: 15:08:42
 * To change this template use File | Settings | File Templates.
 */
public interface ExchangeCurrencyManager extends Manager<EdsExchangeCurrency>{
    String USD = "USD";
    String EUR = "EUR";
    String GBP = "GBP";
    List<EdsCurrency> getCurrencyList();
    EdsExchangeCurrency getCurrency(EdsCurrency currency);
    EdsExchangeCurrency getCurrencyById(Integer currencyId);
    List<EdsCurrency> getAvailableCurrencies();
}
