package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by Sherzod on 7/7/2015.
 */
public class BatchPaymentAddEditData implements IsSerializable {
    private CurrencyItem baseCurrency;
    private SelectItem[] paymentMethods;
    private BankTransferNumberData numberData;
    private ReceivePaymentData data;
    private boolean isEnableDepartment;

    public BatchPaymentAddEditData() {
    }

    public CurrencyItem getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(CurrencyItem baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public SelectItem[] getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(SelectItem[] paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public BankTransferNumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(BankTransferNumberData numberData) {
        this.numberData = numberData;
    }

    public ReceivePaymentData getData() {
        return data;
    }

    public void setData(ReceivePaymentData data) {
        this.data = data;
    }

    public boolean isEnableDepartment() {
        return isEnableDepartment;
    }

    public void setEnableDepartment(boolean isEnableDepartment) {
        this.isEnableDepartment = isEnableDepartment;
    }
}
