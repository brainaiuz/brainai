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
 * Date: 08.04.2010
 * Time: 21:20:51
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "bankTransaction")
public class EdsBankTransaction extends EdsTransaction {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bankmoneytransferid")
    private EdsBankMoneyTransfer bankMoneyTransfer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bankaccountid")
    private EdsBankAccount bankAccount;

    public EdsBankMoneyTransfer getBankMoneyTransfer() {
        return bankMoneyTransfer;
    }

    public void setBankMoneyTransfer(EdsBankMoneyTransfer bankMoneyTransfer) {
        this.bankMoneyTransfer = bankMoneyTransfer;

        //In this section no needed currency & exchange rate to set
    }

    public EdsBankAccount getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(EdsBankAccount bankAccount) {
        this.bankAccount = bankAccount;
        setCurrencyID(bankAccount.getAccount().getCurrency() != null ? bankAccount.getAccount().getCurrency().getObjectID() : null);
        setExchangeRate(bankAccount.getExchangeRate());
    }

    //    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "fromAccountId")
//    private EdsAccount fromAccount;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "toAccountId")
//    private EdsAccount toAccount;

//    public EdsAccount getFromAccount() {
//        return fromAccount;
//    }
//
//    public void setFromAccount(EdsAccount fromAccount) {
//        this.fromAccount = fromAccount;
//    }
//
//    public EdsAccount getToAccount() {
//        return toAccount;
//    }
//
//    public void setToAccount(EdsAccount toAccount) {
//        this.toAccount = toAccount;
//    }

    @Override
    public Integer getKeyId() {
        if (bankAccount != null) {
            return bankAccount.getObjectID();
        } else {
            return bankMoneyTransfer.getObjectID();
        }
    }

    @Override
    public String getKeyType() {
        if (bankAccount != null) {
            return BANK_OPENING_BALANCE_TRANSACTION;//Bank Opening Balance
        } else {
            return BANK_MONEY_TRANSFER_TRANSACTION;//Bank Transfer Money
        }
    }
}
