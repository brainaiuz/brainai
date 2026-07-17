package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.accounting.EdsStockAdjustmentNote;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;

import java.util.List;

public interface StockAdjustmentNoteManager extends Manager<EdsStockAdjustmentNote> {

    List<EdsStockAdjustmentNote> getStockAdjustmentNotes(Integer objectID);

    List<HistoryListItem> getStockAdjustmentNotesAsHistoryListItem(Integer stockAdjustmentId);

    void deleteStockAdjustmentNotes(Integer stockAdjustmentId);
}
