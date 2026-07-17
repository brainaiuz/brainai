package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.accounting.EdsTransaction;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by Omonullo on 5/27/2017.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "cashAdvancePaymentTrasaction")
public class EdsCashAdvancePayTransaction extends EdsTransaction {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cashadvancepaymentid")
    private EdsPayslipPayments cashAdvancePayment;

    public EdsPayslipPayments getCashAdvancePayment() {
        return cashAdvancePayment;
    }

    public void setCashAdvancePayment(EdsPayslipPayments cashAdvancePayment) {

        this.cashAdvancePayment = cashAdvancePayment;
        if (cashAdvancePayment != null) {
            if (cashAdvancePayment.getCurrency() != null) {
                setCurrencyID(cashAdvancePayment.getCurrency().getObjectID());
            }
            setExchangeRate(cashAdvancePayment.getExchangeRate());
        }
    }

    @Override
    public Integer getKeyId() {
        return getCashAdvancePayment().getObjectID();
    }

    @Override
    public String getKeyType() {
        return CASH_ADVANCE_PAYMENT_TRANSACTION;
    }

}
