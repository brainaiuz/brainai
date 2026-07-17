package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 23.07.2010
 * Time: 21:54:31
 * To change this template use File | Settings | File Templates.
 */
public class AllocateCreditData implements IsSerializable {
    private Integer creditNoteID;
    private AllocateCreditItem[] invoices;

    public AllocateCreditData() {
    }

    public Integer getCreditNoteID() {
        return creditNoteID;
    }

    public void setCreditNoteID(Integer creditNoteID) {
        this.creditNoteID = creditNoteID;
    }

    public AllocateCreditItem[] getInvoices() {
        return invoices;
    }

    public void setInvoices(AllocateCreditItem[] invoices) {
        this.invoices = invoices;
    }
}
