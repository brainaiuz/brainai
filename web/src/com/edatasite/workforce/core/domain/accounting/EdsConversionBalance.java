package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsUser;

import javax.persistence.CascadeType;
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
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 02.06.2009
 * Time: 13:21:43
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "conversionBalance")
public class EdsConversionBalance extends EdsObject {    //looks to EdsTransaction

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    //The id of the transaction in company
    private Integer journalId;

    private String name;

    private Date journalDate;
    private Date postedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postedBy")
    private EdsUser postedBy;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "conversionBalanceId")
    private Set<EdsConversionBalanceItem> conversionBalanceItems = new HashSet<>();


    public void addTransactionItem(EdsConversionBalanceItem conversionBalanceItem) {
        conversionBalanceItem.setConversionBalance(this);
        conversionBalanceItems.add(conversionBalanceItem);
    }

    @Column(precision = 25, scale = 5)
    private BigDecimal totalDebit;
    @Column(precision = 25, scale = 5)
    private BigDecimal totalCredit;

//    public EdsInvoice getInvoice() {
//        return invoice;
//    }
//
//    public void setInvoice(EdsInvoice invoice) {
//        this.invoice = invoice;
//    }
//
//    public EdsInvoicePayment getInvoicePayment() {
//        return invoicePayment;
//    }
//
//    public void setInvoicePayment(EdsInvoicePayment invoicePayment) {
//        this.invoicePayment = invoicePayment;
//    }

    public Date getJournalDate() {
        return journalDate;
    }

    public void setJournalDate(Date journalDate) {
        this.journalDate = journalDate;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EdsUser getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(EdsUser postedBy) {
        this.postedBy = postedBy;
    }

    public Integer getJournalId() {
        return journalId;
    }

    public void setJournalId(Integer journalId) {
        this.journalId = journalId;
    }

    public Set<EdsConversionBalanceItem> getConversionBalanceItems() {
        return conversionBalanceItems;
    }

    public void setConversionBalanceItems(Set<EdsConversionBalanceItem> conversionBalanceItems) {
        this.conversionBalanceItems = conversionBalanceItems;
    }

    public BigDecimal getTotalDebit() {
        return totalDebit;
    }

    public void setTotalDebit(BigDecimal totalDebit) {
        this.totalDebit = totalDebit;
    }

    public BigDecimal getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(BigDecimal totalCredit) {
        this.totalCredit = totalCredit;
    }
}
