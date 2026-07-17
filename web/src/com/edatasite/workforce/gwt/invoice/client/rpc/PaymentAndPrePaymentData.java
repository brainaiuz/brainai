package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 11/3/11
 * Time: 5:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class PaymentAndPrePaymentData implements IsSerializable{

    private PaymentItem[] payments;
    private PaymentData[] credits;

    public PaymentAndPrePaymentData() {
    }

    public PaymentItem[] getPayments() {
        return payments;
    }

    public void setPayments(PaymentItem[] payments) {
        this.payments = payments;
    }

    public PaymentData[] getCredits() {
        return credits;
    }

    public void setCredits(PaymentData[] credits) {
        this.credits = credits;
    }
}
