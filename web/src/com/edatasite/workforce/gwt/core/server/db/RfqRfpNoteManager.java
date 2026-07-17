package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.accounting.EdsRfqRfpNote;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;

import java.util.List;

/**
 * Created by Shohruh on 03-Feb-16.
 */
public interface RfqRfpNoteManager extends Manager<EdsRfqRfpNote> {
    List<EdsRfqRfpNote> getRfpNotes(Integer objectID);

    List<EdsRfqRfpNote> getRfqNotes(Integer objectID);

    List<HistoryListItem> getRfpNotesAsHistoryListItem(Integer rfpId);

    List<HistoryListItem> getRfqNotesAsHistoryListItem(Integer rfqId);

    void deleteRfqRfpNotes(Integer rfqRfpId, boolean isRfq);
}
