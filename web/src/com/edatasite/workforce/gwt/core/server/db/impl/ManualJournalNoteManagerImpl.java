package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.accounting.EdsManualJournalNote;
import com.edatasite.workforce.gwt.core.server.db.ManualJournalNoteManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Omonullo on 5/24/2017.
 */
@Repository("manualJournalNoteManager")
public class ManualJournalNoteManagerImpl extends BaseManager<EdsManualJournalNote> implements ManualJournalNoteManager {
    public ManualJournalNoteManagerImpl() {
        super(EdsManualJournalNote.class);
    }

    @Override
    public List<EdsManualJournalNote> getManualJournalNoteByManualJournalId(Integer ObjectID) {
        return find("select mjn from EdsManualJournalNote mjn where mjn.manualJournal.objectID=? order by mjn.date desc", ObjectID);
    }

    @Override
    public void deleteManualJournalNoteByManualJournalId(Integer objectID) {
        update("delete from EdsManualJournalNote where manualJournal.objectID = ?", objectID);
    }
}
