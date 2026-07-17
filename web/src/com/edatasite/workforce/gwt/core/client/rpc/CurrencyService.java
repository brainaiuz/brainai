package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyLayerItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.Date;

/**
 * Created by Shohruh on 26 Mar 2016.
 */
public interface CurrencyService extends RemoteService{

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

    Double getExchangeRateInSumm(int currencyId);

    CurrencyItem getCompanyBaseCurrency();

    CurrencyItem[] getAccountCurrencyWithBase(Integer accountCurrencyId);

    class App {
        public static CurrencyServiceAsync get() {
            ServiceDefTarget target = GWT.create(CoreGenericService.class);
            target.setServiceEntryPoint(Utils.getHostNameURL() + "rpc/currency");
            return (CurrencyServiceAsync) target;
        }
    }
}
