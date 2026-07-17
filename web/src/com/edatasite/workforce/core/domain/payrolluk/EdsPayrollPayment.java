package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollPayment;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayrollPaymentItem;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "payrollpayment")
public class EdsPayrollPayment extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private Date paymentDate;

    @Column(name = "bankAccountId")
    private Integer bankAccountID;

    @Column(name = "paidToAccountId")
    private Integer paidToAccountID;

    @Column(name = "reference")
    private String reference;

    @Column(name = "details")
    private String details;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    @ForeignKey(name = "none")
    private EdsCurrency currency;

    @Column(precision = 25, scale = 15)
    private BigDecimal exchangeRate;

    @Column(precision = 14, scale = 4)
    private BigDecimal amount;

    @Column(precision = 14, scale = 4)
    private BigDecimal amountInBase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "addpayment_id")
    private EdsAdditionalPayment additionalPayment;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "payrollPayment")
    @Where(clause = "(deleted = 'false' or deleted is null)")
    private Set<EdsPayrollPaymentItem> items = new HashSet<>();

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Integer getBankAccountID() {
        return bankAccountID;
    }

    public void setBankAccountID(Integer bankAccountID) {
        this.bankAccountID = bankAccountID;
    }

    public Integer getPaidToAccountID() {
        return paidToAccountID;
    }

    public void setPaidToAccountID(Integer paidToAccountID) {
        this.paidToAccountID = paidToAccountID;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal total) {
        this.amount = total;
    }

    public BigDecimal getAmountInBase() {
        return amountInBase;
    }

    public void setAmountInBase(BigDecimal totalInBase) {
        this.amountInBase = totalInBase;
    }

    public EdsAdditionalPayment getAdditionalPayment() {
        return additionalPayment;
    }

    public void setAdditionalPayment(EdsAdditionalPayment additionalPayment) {
        this.additionalPayment = additionalPayment;
    }

    public Set<EdsPayrollPaymentItem> getItems() {
        return items;
    }

    public void setItems(Set<EdsPayrollPaymentItem> items) {
        this.items = items;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public PayrollPayment toSimpleRPC() {
        PayrollPayment payment = new PayrollPayment();
        payment.setObjectID(getObjectID());
        payment.setAdditionalPaymentID(getAdditionalPayment().getObjectID());
        payment.setPaymentDate(new DateNonConvertable(getPaymentDate()));
        payment.setPaidFromAccountID(getBankAccountID());
        payment.setPaidToAccountID(getPaidToAccountID());
        payment.setDetails(getDetails());
        if (getCurrency() != null) {
            payment.setCurrency(getCurrency().createCurrencyItem());
        }
        payment.setExchangeRate(getExchangeRate());
        payment.setAmount(getAmount());
        payment.setAmountInBase(getAmountInBase());

        return payment;
    }

    public PayrollPayment toRPC() {
        PayrollPayment payment = toSimpleRPC();

        ArrayList<PayrollPaymentItem> items = new ArrayList<>();
        for (EdsPayrollPaymentItem paymentItem : getItems()) {
            items.add(paymentItem.toRPC());
        }
        payment.setItems(items);

        return payment;
    }
}
