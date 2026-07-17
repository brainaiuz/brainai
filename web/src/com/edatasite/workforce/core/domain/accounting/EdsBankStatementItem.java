package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.accounting.client.rpc.BankStatementItemListItem;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * User: Anvarbek
 * Date: May 13, 2010
 * Time: 3:38:25 PM
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "bankStatementItem")
public class EdsBankStatementItem extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private Date transactionDate;

    @Type(type = "text")
    private String description;

    @Type(type = "text")
    private String reference;

    @Column(precision = 25, scale = 5)
    private BigDecimal debit;
    @Column(precision = 25, scale = 5)
    private BigDecimal credit;
    @Column(precision = 25, scale = 5)
    private BigDecimal balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bankstatementid")
    private EdsBankStatement bankStatement;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transactionid")
    private EdsTransaction transaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private EdsAccount account;

    private Boolean reconciled;

    private Boolean deleted = false;

    private Boolean uploadedFileDeleted = Boolean.FALSE;


    public EdsBankStatementItem() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EdsBankStatement getBankStatement() {
        return bankStatement;
    }

    public void setBankStatement(EdsBankStatement bankStatement) {
        this.bankStatement = bankStatement;
    }

    public Boolean isReconciled() {
        return reconciled == null ? false : reconciled;
    }

    public void setReconciled(Boolean reconciled) {
        this.reconciled = reconciled;
    }

    public Boolean getUploadedFileDeleted() {
        return uploadedFileDeleted;
    }

    public void setUploadedFileDeleted(Boolean uploadedFileDeleted) {
        this.uploadedFileDeleted = uploadedFileDeleted;
    }

    public EdsTransaction getTransaction() {
        return transaction;
    }

    public void setTransaction(EdsTransaction transaction) {
        this.transaction = transaction;
    }

    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public EdsAccount getAccount() {
        return account;
    }

    public void setAccount(EdsAccount account) {
        this.account = account;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public BankStatementItemListItem getRPC() {
        BankStatementItemListItem item = new BankStatementItemListItem();
        item.setObjectID(getObjectID());
        item.setDate(getTransactionDate());
        item.setSpent(getCredit());
        item.setReceived(getDebit());
        item.setBalance(getBalance());
        item.setReconsiled(isReconciled());
        return item;
    }
}
