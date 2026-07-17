package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyLayerItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.Date;

/**
 * Created by Shohruh on 26 Mar 2016.
 */
public interface CurrencyServiceAsync {

    void getCurrencies(AsyncCallback<CurrencyItem[]> callback);

    void getCurrencies(boolean showUsed, AsyncCallback<CurrencyItem[]> callback);

    void getCurrencies(boolean showUsed, boolean withoutBaseCurrency, AsyncCallback<CurrencyItem[]> callback);

    void getEmployeeCurrencies(Integer employeeId, boolean ifBaseAll, AsyncCallback<CurrencyItem[]> callback);

    void createOrUpdateCurrency(CurrencyListItem item, AsyncCallback<Void> callback);

    void createCurrency(Integer currencyID, AsyncCallback<Void> callback);

    void deleteCurrency(Integer currencyID, AsyncCallback<Void> callback);

    void getCurrencyRateList(DateNonConvertable date, AsyncCallback<ListResult<CurrencyListItem>> callback);

    void getCurrencyRateByDate(Integer currencyId, DateNonConvertable date, AsyncCallback<CurrencyListItem> callback);

    void getExchangeRateDouble(String from, String to, Date date, int attempt, AsyncCallback<CurrencyLayerItem> callback);

    void getExchangeRateInSumm(int currencyId, AsyncCallback<Double> callback);

    void getBaseCurrency(AsyncCallback<CurrencyItem> async);

    void getCompanyBaseCurrency(AsyncCallback<CurrencyItem> async);

    void getCurrency(Integer currencyID, AsyncCallback<CurrencyItem> async);

    void getAccountCurrencyWithBase(Integer accountCurrencyId, AsyncCallback<CurrencyItem[]> callback);

}
