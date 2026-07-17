package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.accounting.EdsCustomerPrepaymentNote;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.CustomerPrepaymentNoteManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("customerPrepaymentNoteManager")
public class CustomerPrepaymentNoteManagerImpl extends BaseManager<EdsCustomerPrepaymentNote> implements CustomerPrepaymentNoteManager {
    public CustomerPrepaymentNoteManagerImpl() {
        super(EdsCustomerPrepaymentNote.class);
    }

    @Override
    public List<EdsCustomerPrepaymentNote> getPrepaymentNotesByPaymentId(Integer objectID) {
        return find("select pn from EdsCustomerPrepaymentNote pn where pn.payment.objectID=? order by pn.date", objectID);
    }

    @Override
    public List<HistoryListItem> getPaymentNotesAsHistoryListItem(Integer paymentId) {
        return mapToHistoryListItem(getPrepaymentNotesByPaymentId(paymentId));
    }

    private List<HistoryListItem> mapToHistoryListItem(List<EdsCustomerPrepaymentNote> records) {
        List<HistoryListItem> recordItems = new ArrayList<>();
        for (EdsCustomerPrepaymentNote r : records) {
            HistoryListItem recordItem = new HistoryListItem();
            recordItem.setObjectID(r.getObjectID());
            if (r.isSuperUser()) {
                recordItem.setEmployee(Constants.defaultSupportName);
            } else {
                recordItem.setEmployee(r.getCommentator().getFullName());
            }
            recordItem.setComment(r.getComment());
            recordItem.setEventDate(r.getDate());
            recordItems.add(recordItem);
        }
        return recordItems;
    }

    @Override
    public void deletePaymentNotes(Integer paymentId) {
        update("delete from EdsCustomerPrepaymentNote where payment.objectID = ?", paymentId);
    }
}
