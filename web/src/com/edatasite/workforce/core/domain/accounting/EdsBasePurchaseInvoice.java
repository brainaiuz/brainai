package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsCustomCrmAccount;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by Sherzod on 6/19/2015.
 */
@MappedSuperclass
public class EdsBasePurchaseInvoice extends EdsInvoice {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private EdsCrmAccount supplier;

    private Integer clientID;
    private Integer clientMailAddressID;

    //Custom Fields
    private Date cancelDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderterms")
    private EdsInvoiceTerms invoiceTerms;

    public EdsCrmAccount getSupplier() {
        return supplier;
    }

    public void setSupplier(EdsCrmAccount supplier) {
        this.supplier = supplier;
    }

    public EdsCrmAccount getClientOrSupplier() {
        return supplier;
    }

    public Integer getClientID() {
        return clientID;
    }

    public void setClientID(Integer clientID) {
        this.clientID = clientID;
    }

    public Integer getClientMailAddressID() {
        return clientMailAddressID;
    }

    public void setClientMailAddressID(Integer clientMailAddressID) {
        this.clientMailAddressID = clientMailAddressID;
    }

    public Date getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(Date cancelDate) {
        this.cancelDate = cancelDate;
    }

    public EdsInvoiceTerms getInvoiceTerms() {
        return invoiceTerms;
    }

    public void setInvoiceTerms(EdsInvoiceTerms invoiceTerms) {
        this.invoiceTerms = invoiceTerms;
    }

    @Override
    public void setEntityStatus(EdsReference overallStatus) {

    }

    @Override
    public List<EdsApprover> getApprovers() {
        return null;
    }

    @Override
    public void setApprovers(List<EdsApprover> approvers) {

    }

    @Override
    public boolean isCurrentApproverApproved() {
        return false;
    }

    @Override
    public boolean isCurrentApproverRejected() {
        return false;
    }

    @Override
    protected EdsReference getStatusByMarkedAction(Integer integer) {
        return null;
    }
}
