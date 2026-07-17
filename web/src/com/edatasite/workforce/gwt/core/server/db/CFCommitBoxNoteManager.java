package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsCFCommitBoxNote;
import com.edatasite.workforce.core.domain.EdsCompanyCustomFieldsSettings;

import java.util.List;

public interface CFCommitBoxNoteManager extends Manager<EdsCFCommitBoxNote> {

    EdsCFCommitBoxNote getCommitNoteById(Integer objectId);

    List<EdsCFCommitBoxNote> getCFCommitBoxAllNotes(EdsCompanyCustomFieldsSettings customField, Integer formItemId);

    List<EdsCFCommitBoxNote> getUserCommits(List<Integer> newNoteIds);

    void deleteCommitFromBox(EdsCFCommitBoxNote note);

    void deleteNotesByCustomField(EdsCompanyCustomFieldsSettings setting);
}
