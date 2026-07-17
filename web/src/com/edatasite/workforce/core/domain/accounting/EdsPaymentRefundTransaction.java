package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "paymentRefundTransaction")
public class EdsPaymentRefundTransaction extends EdsPaymentTransaction {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paymentRefundId")
    private EdsPaymentRefund paymentRefund;

    public EdsPaymentRefund getPaymentRefund() {
        return paymentRefund;
    }

    public void setPaymentRefund(EdsPaymentRefund paymentRefund) {
        this.paymentRefund = paymentRefund;

        setCurrencyID(paymentRefund.getCurrencyID());
        setExchangeRate(paymentRefund.getExchangeRate());
    }

    public Integer getKeyId() {
        return getPaymentRefund() != null ? getPaymentRefund().getObjectID() : null;
    }

    public String getKeyType() {
        return PAYMENTREFUND_CLOSED_TRANSACTION;
    }
}
