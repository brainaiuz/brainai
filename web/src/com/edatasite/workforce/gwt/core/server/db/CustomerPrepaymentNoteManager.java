package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.accounting.EdsCustomerPrepaymentNote;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;

import java.util.List;

public interface CustomerPrepaymentNoteManager extends Manager<EdsCustomerPrepaymentNote> {

    List<EdsCustomerPrepaymentNote> getPrepaymentNotesByPaymentId(Integer paymentId);

    List<HistoryListItem> getPaymentNotesAsHistoryListItem(Integer rfqId);

    void deletePaymentNotes(Integer paymentId);

}
