package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsCurrency;
import org.hibernate.annotations.ForeignKey;

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
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/23/13
 * Time: 5:03 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "bankMoneyTransfer")
public class EdsBankMoneyTransfer extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fromaccountid")
    private EdsAccount fromAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "toaccountid")
    private EdsAccount toAccount;

    private String reference;

    @Column(precision = 25, scale = 5)
    private BigDecimal amount;

    @Column(precision = 25, scale = 15)
    private BigDecimal exchangeRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amountcurrency")
    @ForeignKey(name = "none")
    private EdsCurrency amountCurrency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency")
    @ForeignKey(name = "none")
    private EdsCurrency currency;

    private Boolean deleted = false;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public EdsAccount getFromAccount() {
        return fromAccount;
    }

    public void setFromAccount(EdsAccount fromAccount) {
        this.fromAccount = fromAccount;
    }

    public EdsAccount getToAccount() {
        return toAccount;
    }

    public void setToAccount(EdsAccount toAccount) {
        this.toAccount = toAccount;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public EdsCurrency getAmountCurrency() {
        return amountCurrency;
    }

    public void setAmountCurrency(EdsCurrency amountCurrency) {
        this.amountCurrency = amountCurrency;
    }

    public EdsCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(EdsCurrency currency) {
        this.currency = currency;
    }

    public Boolean isDeleted() {
        return deleted != null ? deleted : false;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
