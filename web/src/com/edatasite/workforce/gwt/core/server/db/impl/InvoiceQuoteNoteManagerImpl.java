package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.accounting.EdsInvoiceQuoteNote;
import com.edatasite.workforce.gwt.core.server.db.InvoiceQuoteNoteManager;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 24.01.2011
 * Time: 14:22:38
 * To change this template use File | Settings | File Templates.
 */
@Repository("invoiceQuoteNoteManager")
public class InvoiceQuoteNoteManagerImpl extends BaseManager<EdsInvoiceQuoteNote> implements InvoiceQuoteNoteManager {

    public InvoiceQuoteNoteManagerImpl() {
        super(EdsInvoiceQuoteNote.class);
    }

    @Override
    public List<EdsInvoiceQuoteNote> getInvoiceNotes(Integer objectID) {
        if (objectID == null) {
            return Collections.emptyList();
        }
        return find("select ih from EdsInvoiceQuoteNote ih where ih.invoice.objectID=? order by ih.date", objectID);
    }

    @Override
    public List<EdsInvoiceQuoteNote> getQuoteNotes(Integer objectID) {
        return find("select ih from EdsInvoiceQuoteNote ih where ih.quote.objectID=? order by ih.date", objectID);
    }

    @Override
    public void deleteInvoiceQuoteNotes(Integer invoiceQuoteID, boolean isInvoice) {
        if (isInvoice) {
            update("delete from EdsInvoiceQuoteNote where invoice.objectID = ?", invoiceQuoteID);
        } else {
            update("delete from EdsInvoiceQuoteNote where quote.objectID = ?", invoiceQuoteID);
        }
    }

}
