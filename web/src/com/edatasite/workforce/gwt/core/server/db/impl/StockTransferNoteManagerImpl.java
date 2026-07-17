package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.accounting.EdsStockTransferNote;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.StockTransferNoteManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("stockTransferNoteManager")
public class StockTransferNoteManagerImpl extends BaseManager<EdsStockTransferNote> implements StockTransferNoteManager {

    public StockTransferNoteManagerImpl() {
        super(EdsStockTransferNote.class);
    }

    @Override
    public List<EdsStockTransferNote> getStockTransferNotes(Integer objectID) {
        return find("select stn from EdsStockTransferNote stn where stn.stockTransfer.objectID=? order by stn.date", objectID);
    }

    @Override
    public List<HistoryListItem> getStockTransferNotesAsHistoryListItem(Integer stockTransferId) {
        return mapToHistoryListItem(getStockTransferNotes(stockTransferId));
    }

    private List<HistoryListItem> mapToHistoryListItem(List<EdsStockTransferNote> records) {
        List<HistoryListItem> recordItems = new ArrayList<>();
        for (EdsStockTransferNote r : records) {
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
    public void deleteStockTransferNotes(Integer stockTransferId) {
        update("delete from EdsStockTransferNote where rfq.stockTransfer = ?", stockTransferId);
    }
}
