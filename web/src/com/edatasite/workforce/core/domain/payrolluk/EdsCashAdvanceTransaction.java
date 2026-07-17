package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.accounting.EdsTransaction;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 11/19/14
 * Time: 3:43 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "cashAdvanceTransaction")
public class EdsCashAdvanceTransaction extends EdsTransaction {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cashadvance_id")
    private EdsCashAdvance cashAdvance;


    public EdsCashAdvance getCashAdvance() {
        return cashAdvance;
    }

    public void setCashAdvance(EdsCashAdvance cashAdvance) {
        this.cashAdvance = cashAdvance;
        setCurrencyID(cashAdvance.getCurrency() != null ? cashAdvance.getCurrency().getObjectID() : null);
        setExchangeRate(cashAdvance.getExchangeRate());
    }

    public Integer getKeyId() {
        return getCashAdvance().getObjectID();
    }

    public String getKeyType() {
        return CASH_ADVANCE_TRANSACTION;
    }

}
