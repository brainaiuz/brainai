package com.edatasite.workforce.gwt.core.client.rpc.accounting;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Jul 17, 2009
 * Time: 8:45:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class TransactionsReport implements IsSerializable {
    private String baseCurrency;
    private double currentCash;
    private double moneyWeOwe;
    private double moneyOwedToUs;

    public double getCurrentCash() {
        return currentCash;
    }

    public void setCurrentCash(double currentCash) {
        this.currentCash = currentCash;
    }

    public double getMoneyWeOwe() {
        return moneyWeOwe;
    }

    public void setMoneyWeOwe(double moneyWeOwe) {
        this.moneyWeOwe = moneyWeOwe;
    }

    public double getMoneyOwedToUs() {
        return moneyOwedToUs;
    }

    public void setMoneyOwedToUs(double moneyOwedToUs) {
        this.moneyOwedToUs = moneyOwedToUs;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }
}
