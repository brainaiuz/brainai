package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.customfields.EdsCustomFormNote;

import java.util.List;

public interface CustomFormNoteManager extends Manager<EdsCustomFormNote>{

    List<EdsCustomFormNote> getCustomFormNotes(Integer ObjectID);

    void deleteCustomFormNotes(Integer ObjectID);
}
