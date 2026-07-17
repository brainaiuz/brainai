package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.accounting.EdsTransaction;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "additionalPaymentTransaction")
public class EdsAdditionalPaymentTransaction extends EdsTransaction {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "additionalPaymentid")
    private EdsPaymentDeduction paymentDeduction;

    public EdsPaymentDeduction getAdditionalPayment() {
        return this.paymentDeduction;
    }

    public void setAdditionalPayment(final EdsPaymentDeduction paymentDeduction) {
        this.paymentDeduction = paymentDeduction;
    }

    @Override
    public Integer getKeyId() {
        return getAdditionalPayment().getObjectID();
    }

    @Override
    public String getKeyType() {
        return ADDITIONAL_PAYMENT_TRANSACTION;
    }

}
