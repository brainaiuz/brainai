package com.edatasite.workforce.gwt.core.server.app.social.facebook.model;

/**
 * Created by Anvar Akramov on 10/5/17.
 */
public class Currency {

    private int currencyOffset;

    private String userCurrency;

    private float usdExchange;

    private float usdExchangeInverse;

    public int getCurrencyOffset() {
        return currencyOffset;
    }

    public String getUserCurrency() {
        return userCurrency;
    }

    public float getUsdExchange() {
        return usdExchange;
    }

    public float getUsdExchangeInverse() {
        return usdExchangeInverse;
    }

}