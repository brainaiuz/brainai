package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.accounting.EdsInvoiceQuoteNote;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 24.01.2011
 * Time: 14:21:36
 * To change this template use File | Settings | File Templates.
 */
public interface InvoiceQuoteNoteManager extends Manager<EdsInvoiceQuoteNote>{
    List<EdsInvoiceQuoteNote> getInvoiceNotes(Integer objectID);
    List<EdsInvoiceQuoteNote> getQuoteNotes(Integer objectID);

    void deleteInvoiceQuoteNotes(Integer invoiceQuoteID, boolean isInvoice);
}
