package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyLayerItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;

import java.util.Date;

/**
 * Created by Dilshod Madrahimov on 26 Mar 2016.
 */
public interface CurrencyServiceLocal {

    CurrencyItem[] getCurrencies();

    CurrencyItem[] getCurrencies(boolean showUsed);

    CurrencyItem[] getCurrencies(boolean showUsed, boolean withoutBaseCurrency);

    CurrencyItem getBaseCurrency();

    CurrencyItem[] getEmployeeCurrencies(Integer employeeId, boolean ifBaseAll);

    void createOrUpdateCurrency(CurrencyListItem item);

    void createCurrency(Integer currencyID);

    void deleteCurrency(Integer currencyID);

    CurrencyItem getCurrency(Integer currencyID);

    ListResult<CurrencyListItem> getCurrencyRateList(DateNonConvertable date);

    CurrencyListItem getCurrencyRateByDate(Integer currencyId, DateNonConvertable date);

    CurrencyLayerItem getExchangeRateDouble(String from, String to, Date date, int attempt);

    CurrencyItem getCompanyBaseCurrency();


}
