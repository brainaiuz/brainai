package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.customfields.EdsCustomFormNote;
import com.edatasite.workforce.gwt.core.server.db.CustomFormNoteManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("customFormNoteManager")
public class CustomFormNoteManagerImpl extends BaseManager<EdsCustomFormNote> implements CustomFormNoteManager {

    public CustomFormNoteManagerImpl() {
        super(EdsCustomFormNote.class);
    }

    @Override
    public List<EdsCustomFormNote> getCustomFormNotes(Integer ObjectID) {
        return find("select cfn from EdsCustomFormNote cfn where cfn.costomFormItem.objectID=? order by cfn.date desc", ObjectID);
    }

    @Override
    public void deleteCustomFormNotes(Integer ObjectID) {
        update("delete from EdsCustomFormNote where costomFormItem.objectID = ?", ObjectID);
    }
}
