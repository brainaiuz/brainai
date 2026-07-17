package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsExpensePayment;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 25.04.2009
 * Time: 16:32:15
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "expensePaymentTransaction")
public class EdsExpensePaymentTransaction extends EdsPaymentTransaction {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expensePaymentId")
    private EdsExpensePayment expensePayment;

    public EdsExpensePayment getExpensePayment() {
        return expensePayment;
    }

    public void setExpensePayment(EdsExpensePayment expensePayment) {
        this.expensePayment = expensePayment;

        setCurrencyID(expensePayment.getCurrencyID());
        setExchangeRate(expensePayment.getExchangeRate());
    }

    public Integer getKeyId() {
        return getExpensePayment().getObjectID();
    }

    public String getKeyType() {
        return EXPENSEPAYMENT_TRANSACTION;
    }
}
