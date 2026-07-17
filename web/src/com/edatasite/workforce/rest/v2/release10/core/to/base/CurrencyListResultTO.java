package com.edatasite.workforce.rest.v2.release10.core.to.base;

import java.util.ArrayList;

public class CurrencyListResultTO extends ResponseData {

    private ArrayList<CurrencyListTO> currencies;

    public CurrencyListResultTO() {
    }


    public CurrencyListResultTO(ArrayList<CurrencyListTO> currencies) {
        this.currencies = currencies;
    }

    public ArrayList<CurrencyListTO> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(ArrayList<CurrencyListTO> currencies) {
        this.currencies = currencies;
    }
}
