package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCFCommitBoxNote;
import com.edatasite.workforce.core.domain.EdsCompanyCustomFieldsSettings;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CFCommitBoxNoteManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CFCommitBoxNoteManagerImpl extends BaseManager<EdsCFCommitBoxNote> implements CFCommitBoxNoteManager {
    public CFCommitBoxNoteManagerImpl() {
        super(EdsCFCommitBoxNote.class);
    }

    @Override
    public EdsCFCommitBoxNote getCommitNoteById(Integer objectId) {
        return (EdsCFCommitBoxNote) findSingle("select note from EdsCFCommitBoxNote note where note.objectID = ?", objectId);
    }

    @Override
    public List<EdsCFCommitBoxNote> getCFCommitBoxAllNotes(EdsCompanyCustomFieldsSettings customField, Integer formItemId) {
        return (List<EdsCFCommitBoxNote>) find("SELECT notes FROM EdsCFCommitBoxNote notes where notes.customFields = ? and notes.formItemId is not null and notes.formItemId = ? order by notes.date asc", customField, formItemId);
    }

    @Override
    public void deleteCommitFromBox(EdsCFCommitBoxNote note) {
        this.update("UPDATE  EdsCFCommitBoxNote cfcn  SET cfcn.customFields= null where cfcn.objectID = ?", note.getObjectID());
        this.update("DELETE FROM EdsCFCommitBoxNote cfcn WHERE cfcn.objectID = ?", note.getObjectID());
    }

    @Override
    public void deleteNotesByCustomField(EdsCompanyCustomFieldsSettings setting) {
        this.update("DELETE FROM EdsCFCommitBoxNote cfcn WHERE cfcn.customFields = ?", setting);
    }

    @Override
    public List<EdsCFCommitBoxNote> getUserCommits(List<Integer> newNoteIds) {

        return (List<EdsCFCommitBoxNote>) find("SELECT notes FROM EdsCFCommitBoxNote notes where notes.objectID in ( " + ServerUtils.getAsCommoDelimited(newNoteIds, "0", ",") + " )");
    }
}
