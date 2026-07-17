package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.accounting.EdsTransaction;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "payrollPaymentTransaction")
public class EdsPayrollPaymentTransaction  extends EdsTransaction {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payroll_payment_id")
    private EdsPayrollPaymentItem paymentItem;

    public EdsPayrollPaymentItem getPaymentItem() {
        return paymentItem;
    }

    public void setPaymentItem(EdsPayrollPaymentItem paymentItem) {
        this.paymentItem = paymentItem;

        setCurrencyID(paymentItem.getCurrency() != null ? paymentItem.getCurrency().getObjectID() : null);
        setExchangeRate(paymentItem.getExchangeRate());
    }

    public Integer getKeyId() {
        return getPaymentItem().getObjectID();
    }

    public String getKeyType() {
        return PAYROLL_PAYMENT_TRANSACTION;
    }
}
