package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.accounting.EdsStockAdjustmentNote;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.StockAdjustmentNoteManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository("stockAdjustmentNoteManager")
public class StockAdjustmentNoteManagerImpl extends BaseManager<EdsStockAdjustmentNote> implements StockAdjustmentNoteManager {

    public StockAdjustmentNoteManagerImpl() {
        super(EdsStockAdjustmentNote.class);
    }

    @Override
    public List<EdsStockAdjustmentNote> getStockAdjustmentNotes(Integer objectID) {
        return find("select stn from EdsStockAdjustmentNote stn where stn.stockAdjustment.objectID=? order by stn.date", objectID);
    }

    @Override
    public List<HistoryListItem> getStockAdjustmentNotesAsHistoryListItem(Integer stockAdjustmentId) {
        return mapToHistoryListItem(getStockAdjustmentNotes(stockAdjustmentId));
    }

    private List<HistoryListItem> mapToHistoryListItem(List<EdsStockAdjustmentNote> records) {
        List<HistoryListItem> recordItems = new ArrayList<>();
        for (EdsStockAdjustmentNote r : records) {
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
    public void deleteStockAdjustmentNotes(Integer stockAdjustmentId) {
        update("delete from EdsStockAdjustmentNote where rfq.stockAdjustment = ?", stockAdjustmentId);
    }
}
