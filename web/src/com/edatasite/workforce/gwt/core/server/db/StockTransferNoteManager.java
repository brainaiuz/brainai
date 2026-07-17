package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.accounting.EdsStockTransferNote;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;

import java.util.List;

public interface StockTransferNoteManager extends Manager<EdsStockTransferNote>{

    List<EdsStockTransferNote> getStockTransferNotes(Integer objectID);

    List<HistoryListItem> getStockTransferNotesAsHistoryListItem(Integer stockTransferId);

    void deleteStockTransferNotes(Integer stockTransferId);
}
