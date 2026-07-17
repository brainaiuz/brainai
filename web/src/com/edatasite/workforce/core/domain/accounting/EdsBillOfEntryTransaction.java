package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: 25.05.2019
 * Time: 21:20:51
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "billofentry_ransaction")
public class EdsBillOfEntryTransaction extends EdsTransaction {

    @Column(name = "billofentry_id")
    private Integer billOfEntryId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billofentry_id", updatable = false, insertable = false)
    private EdsBillOfEntry billOfEntry;


    public void setBankAccount(EdsBankAccount bankAccount) {
        setCurrencyID(bankAccount.getAccount().getCurrency() != null ? bankAccount.getAccount().getCurrency().getObjectID() : null);
        setExchangeRate(bankAccount.getExchangeRate());
    }


    public Integer getBillOfEntryId() {
        return billOfEntryId;
    }

    public void setBillOfEntryId(Integer billOfEntryId) {
        this.billOfEntryId = billOfEntryId;
    }

    public EdsBillOfEntry getBillOfEntry() {
        return billOfEntry;
    }

    public void setBillOfEntry(EdsBillOfEntry billOfEntry) {
        this.billOfEntry = billOfEntry;
    }


    @Override
    public Integer getKeyId() {
            return billOfEntry.getObjectID();
    }

    @Override
    public String getKeyType() {
            return BANK_MONEY_TRANSFER_TRANSACTION;//Bank Transfer Money
    }
}
