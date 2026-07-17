package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 30.08.2010
 * Time: 17:14:34
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "bankTransferTransaction")
public class EdsBankTransferTransaction extends EdsTransaction {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banktransferid")
    private EdsBankTransfer bankTransfer;

    public EdsBankTransfer getBankTransfer() {
        return bankTransfer;
    }

    public void setBankTransfer(EdsBankTransfer bankTransfer) {
        this.bankTransfer = bankTransfer;

        setCurrencyID(bankTransfer.getCurrency() != null ? bankTransfer.getCurrency().getObjectID() : null);
        setExchangeRate(bankTransfer.getExchangeRate());
    }

    @Override
    public Integer getKeyId() {
        return bankTransfer != null ? bankTransfer.getObjectID() : null;
    }

    @Override
    public String getKeyType() {
        return BANK_TRANSFER_TRANSACTION;
    }
}
