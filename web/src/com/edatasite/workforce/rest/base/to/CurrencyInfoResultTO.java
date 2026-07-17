package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

import java.util.ArrayList;

/**
 * Created by Abdurakhmonov Farrukh on 03/16/2018.
 */
public class CurrencyInfoResultTO extends ResponseData {
    private CurrencyInfoTO company_currency;
    private ArrayList<CurrencyInfoTO> currencies;

    public CurrencyInfoTO getCompany_currency() {
        return company_currency;
    }

    public void setCompany_currency(CurrencyInfoTO company_currency) {
        this.company_currency = company_currency;
    }

    public ArrayList<CurrencyInfoTO> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(ArrayList<CurrencyInfoTO> currencies) {
        this.currencies = currencies;
    }
}
