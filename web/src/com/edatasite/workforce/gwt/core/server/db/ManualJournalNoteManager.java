package com.edatasite.workforce.gwt.core.server.db;


import com.edatasite.workforce.core.domain.accounting.EdsManualJournalNote;

import java.util.List;

/**
 * Created by Omonullo on 5/24/2017.
 */
public interface ManualJournalNoteManager extends Manager<EdsManualJournalNote> {
    List<EdsManualJournalNote> getManualJournalNoteByManualJournalId(Integer ObjectID);
    void deleteManualJournalNoteByManualJournalId(Integer objectID);
}
