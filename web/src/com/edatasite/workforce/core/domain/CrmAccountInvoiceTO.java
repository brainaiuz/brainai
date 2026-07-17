package com.edatasite.workforce.core.domain;

import java.util.Date;

public class CrmAccountInvoiceTO {
    private Integer crmAccountId;
    private Integer crmContactId;
    private Date invoiceExpireDate;
    private Integer invoiceId;

    public CrmAccountInvoiceTO() {
    }

    public CrmAccountInvoiceTO(Integer crmAccountId, Integer crmContactId, Date invoiceExpireDate, Integer invoiceId) {
        this.crmAccountId = crmAccountId;
        this.crmContactId = crmContactId;
        this.invoiceExpireDate = invoiceExpireDate;
        this.invoiceId = invoiceId;
    }

    public Integer getCrmAccountId() {
        return crmAccountId;
    }

    public Integer getCrmContactId() {
        return crmContactId;
    }

    public Date getInvoiceExpireDate() {
        return invoiceExpireDate;
    }

    public Integer getInvoiceId() {
        return invoiceId;
    }
}
