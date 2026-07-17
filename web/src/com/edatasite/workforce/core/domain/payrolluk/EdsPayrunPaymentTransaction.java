package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.accounting.EdsTransaction;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "payrunPaymentTransaction")
public class EdsPayrunPaymentTransaction extends EdsTransaction {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payrun_payment_id")
    private EdsPayrunPaymentItem paymentItem;

    public EdsPayrunPaymentItem getPaymentItem() {
        return paymentItem;
    }

    public void setPaymentItem(EdsPayrunPaymentItem paymentItem) {
        this.paymentItem = paymentItem;

        setCurrencyID(paymentItem.getCurrency() != null ? paymentItem.getCurrency().getObjectID() : null);
        setExchangeRate(paymentItem.getExchangeRate());
    }

    public Integer getKeyId() {
        return getPaymentItem().getObjectID();
    }

    public String getKeyType() {
        return SINGLE_PAYRUN_PAYMENT_TRANSACTION;
    }
}
