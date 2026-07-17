package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 9/4/12
 * Time: 4:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class TrialBalanceFilterData implements IsSerializable{
    private DateNonConvertable financialYearEnd;
    private Date conversationDate;
    private CurrencyItem baseCurrency;
    private CurrencyItem[] currencies;

    public TrialBalanceFilterData() {
    }

    public DateNonConvertable getFinancialYearEnd() {
        return financialYearEnd;
    }

    public void setFinancialYearEnd(DateNonConvertable financialYearEnd) {
        this.financialYearEnd = financialYearEnd;
    }

    public Date getConversationDate() {
        return conversationDate;
    }

    public void setConversationDate(Date conversationDate) {
        this.conversationDate = conversationDate;
    }

    public CurrencyItem getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(CurrencyItem baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public CurrencyItem[] getCurrencies() {
        return currencies;
    }

    public void setCurrencies(CurrencyItem[] currencies) {
        this.currencies = currencies;
    }
}
