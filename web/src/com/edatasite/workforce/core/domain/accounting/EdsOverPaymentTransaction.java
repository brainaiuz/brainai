package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;

/**
 * User: Dilsh0d Madrahimov
 * Date: 7/31/17
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "overPaymentTransaction")
public class EdsOverPaymentTransaction extends EdsPaymentTransaction {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "overPaymentId")
    private EdsOverPayment overPayment;

    public EdsOverPayment getOverPayment() {
        return overPayment;
    }

    public void setOverPayment(EdsOverPayment overPayment) {
        this.overPayment = overPayment;
    }

    public Integer getKeyId() {
        return getOverPayment().getObjectID();
    }

    public String getKeyType() {
        return OVER_PAYMENT_TRANSACTION;
    }
}
