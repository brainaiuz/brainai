package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsPaymentBase;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.core.server.domain.ObjectHistory;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;


@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "paymentrefund")
public class EdsPaymentRefund extends EdsPaymentBase implements ObjectHistory {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crmAccountID")
    private EdsCrmAccount crmAccount; //Client or Supplier

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bankaccountid")
    private EdsBankAccount bankAccount;


    private String number;

    private Integer numberInt;

    @Type(type = "text")
    private String description;

    private String type;//RECEIVABLE, PAYABLE
    private String paymentTarget;

    @Column(precision = 25, scale = 5)
    private BigDecimal total;

    @Column(precision = 25, scale = 5)
    private BigDecimal totalInBase;

    private Date creationDate;

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }


    public String getNumber() {
        return this.number;
    }

    public void setNumber(final String number) {
        this.number = number;
    }

    public Integer getNumberInt() {
        return this.numberInt;
    }

    public void setNumberInt(final Integer numberInt) {
        this.numberInt = numberInt;
    }

    public EdsCrmAccount getCrmAccount() {
        return this.crmAccount;
    }

    public void setCrmAccount(final EdsCrmAccount crmAccount) {
        this.crmAccount = crmAccount;
    }

    public String getType() {
        return this.type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getPaymentTarget() {
        return this.paymentTarget;
    }

    public void setPaymentTarget(final String paymentTarget) {
        this.paymentTarget = paymentTarget;
    }

    public EdsBankAccount getBankAccount() {
        return this.bankAccount;
    }

    public void setBankAccount(final EdsBankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public BigDecimal getTotal() {
        return this.total;
    }

    public void setTotal(final BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getTotalInBase() {
        return this.totalInBase;
    }

    public void setTotalInBase(final BigDecimal totalInBase) {
        this.totalInBase = totalInBase;
    }

    @Override
    public void setLastUpdateTime(Date value) {

    }

    @Override
    public void setUpdater(EdsUser user) {

    }

    @Override
    public void setCreationTime(Date value) {
        this.creationDate = value;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(final Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public void setCreator(EdsUser value) {

    }
}
