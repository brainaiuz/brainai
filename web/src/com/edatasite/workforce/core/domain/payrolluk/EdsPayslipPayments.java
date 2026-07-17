package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
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
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 11.06.14
 * Time: 21:01
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "payslip_payments")
public class EdsPayslipPayments extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "payment_deduction_id")
    private Integer paymentDeductionID;

    @Column(name = "payslip_id")
    private Integer payslipID;

    @Column(name = "payslip_item_id")
    private Integer payslipItemID;

    @Column(name = "payment_total")
    private BigDecimal paymentTotal;

    private String remarks;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId")
    private EdsAccount account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private EdsUser user;

    private Date paymentDate;

    private String reference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currencyid")
    @ForeignKey(name = "none")
    private EdsCurrency currency;

    @Column(precision = 25, scale = 15)
    private BigDecimal exchangeRate;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cashadvanceid")
    private EdsCashAdvance cashAdvance;

    private Date creationDate;

    private Date lasUpdated;
    @Column(name = "forwardedPayment", columnDefinition = "boolean default false")
    private boolean forwardedPayment = false;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getPaymentDeductionID() {
        return paymentDeductionID;
    }

    public void setPaymentDeductionID(Integer paymentDeductionID) {
        this.paymentDeductionID = paymentDeductionID;
    }

    public Integer getPayslipID() {
        return payslipID;
    }

    public void setPayslipID(Integer payslipID) {
        this.payslipID = payslipID;
    }

    public Integer getPayslipItemID() {
        return payslipItemID;
    }

    public void setPayslipItemID(Integer payslipItemID) {
        this.payslipItemID = payslipItemID;
    }

    public BigDecimal getPaymentTotal() {
        return paymentTotal;
    }

    public void setPaymentTotal(BigDecimal paymentTotal) {
        this.paymentTotal = paymentTotal;
    }

    public EdsAccount getAccount() {
        return account;
    }

    public void setAccount(EdsAccount account) {
        this.account = account;
    }

    public EdsUser getUser() {
        return user;
    }

    public void setUser(EdsUser user) {
        this.user = user;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public EdsCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(EdsCurrency currency) {
        this.currency = currency;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public EdsCashAdvance getCashAdvance() {
        return cashAdvance;
    }

    public void setCashAdvance(EdsCashAdvance cashAdvance) {
        this.cashAdvance = cashAdvance;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLasUpdated() {
        return lasUpdated;
    }

    public void setLasUpdated(Date lasUpdated) {
        this.lasUpdated = lasUpdated;
    }

    public void setForwardedPayment(boolean forwardedPayment) {
        this.forwardedPayment = forwardedPayment;
    }

    public boolean getForwardedPayment() {
        return forwardedPayment;
    }
}
