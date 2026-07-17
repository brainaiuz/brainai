package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.customfields.EdsManualJournalItemCustomFields;
import com.edatasite.workforce.gwt.core.server.db.accounting.ManualJournalItemCFManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

@Repository("manualJournalItemCFManager")
public class ManualJournalItemCFManagerImpl extends BaseManager<EdsManualJournalItemCustomFields> implements ManualJournalItemCFManager {

    public ManualJournalItemCFManagerImpl() {
        super(EdsManualJournalItemCustomFields.class);
    }
}