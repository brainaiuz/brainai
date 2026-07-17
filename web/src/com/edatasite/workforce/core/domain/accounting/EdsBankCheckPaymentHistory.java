package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

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

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 7/25/12
 * Time: 2:36 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "bankcheckhistory")
public class EdsBankCheckPaymentHistory extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoicepaymentid")
    private EdsInvoicePayment invoicePayment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bankcheckitemid")
    private EdsBankCheckItem bankCheckItem;

    @Column(precision = 25, scale = 5)
    private BigDecimal amount;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsInvoicePayment getInvoicePayment() {
        return invoicePayment;
    }

    public void setInvoicePayment(EdsInvoicePayment invoicePayment) {
        this.invoicePayment = invoicePayment;
    }

    public EdsBankCheckItem getBankCheckItem() {
        return bankCheckItem;
    }

    public void setBankCheckItem(EdsBankCheckItem bankCheckItem) {
        this.bankCheckItem = bankCheckItem;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
