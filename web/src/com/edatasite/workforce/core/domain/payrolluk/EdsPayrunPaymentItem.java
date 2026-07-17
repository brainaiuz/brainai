package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrunPaymentItem;
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

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "payrunpaymentitem")
public class EdsPayrunPaymentItem extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne
    @JoinColumn(name = "employeeID")
    private EdsEmployee employee;

    @Column(name = "paidFromAccountId")
    private Integer paidFromAccountID;

    @Column(name = "paidToAccountId")
    private Integer paidToAccountID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payslipTableItem_id")
    private EdsPayslipTableItem payslipTableItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payrun_payment_id")
    private EdsPayrunPayment payrunPayment;

    @Column(name = "deleted")
    private Boolean deleted = false;

    private Date dueDate;

    private Date paymentDate;

    @Column(name = "bankAccount")
    private String bankAccount;

    @Column(name = "reference")
    private String reference;

    @Column(name = "details")
    private String details;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currencyid")
    @ForeignKey(name = "none")
    private EdsCurrency currency;

    @Column(precision = 25, scale = 15)
    private BigDecimal exchangeRate;

    @Column(name = "payment_amount")
    private BigDecimal paymentAmount;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public EdsEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(EdsEmployee employee) {
        this.employee = employee;
    }

    public Integer getPaidFromAccountID() {
        return paidFromAccountID;
    }

    public void setPaidFromAccountID(Integer paidFromAccountID) {
        this.paidFromAccountID = paidFromAccountID;
    }

    public Integer getPaidToAccountID() {
        return paidToAccountID;
    }

    public void setPaidToAccountID(Integer paidToAccountID) {
        this.paidToAccountID = paidToAccountID;
    }

    public EdsPayslipTableItem getPayslipTableItem() {
        return payslipTableItem;
    }

    public void setPayslipTableItem(EdsPayslipTableItem payslipTableItem) {
        this.payslipTableItem = payslipTableItem;
    }

    public EdsPayrunPayment getPayrunPayment() {
        return payrunPayment;
    }

    public void setPayrunPayment(EdsPayrunPayment payrunPayment) {
        this.payrunPayment = payrunPayment;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
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

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public PayrunPaymentItem toRPC() {
        PayrunPaymentItem item = new PayrunPaymentItem();
        item.setObjectID(getObjectID());
        if (getEmployee() != null) {
            item.setEmployeeID(getEmployee().getObjectID());
            item.setEmployee(getEmployee().getFullName());
        }
        if (getPayslipTableItem() != null) {
            item.setSinglePayrunID(getPayslipTableItem().getObjectID());
        }
        item.setPaidFromAccountID(getPaidFromAccountID());
        item.setPaidToAccountID(getPaidToAccountID());

        item.setPaymentDate(new DateNonConvertable(getPaymentDate()));
        item.setReference(getReference());
        item.setBankAccount(getBankAccount());
        item.setDetails(getDetails());
        item.setPaymentAmount(getPaymentAmount());

        return item;
    }
}
