package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsNoteComment;
import com.edatasite.workforce.gwt.core.server.db.NoteCommentManager;
import org.springframework.stereotype.Repository;

/**
 * Created by IntelliJ IDEA.
 * User: Employee
 * Date: Nov 4, 2009
 * Time: 12:06:52 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("noteCommentManager")
public class NoteCommentManagerImpl extends BaseManager<EdsNoteComment> implements NoteCommentManager {
    public NoteCommentManagerImpl() {
        super(EdsNoteComment.class);
    }

    @Override
    public void deleteNoteComments(Integer noteID) {
        update("delete from EdsNoteComment where note.objectID=?", noteID);
    }

    @Override
    public Integer getCommentCountByNoteID(Integer noteID) {
        Long result = (Long) findSingle("select count(c.objectID) from EdsNoteComment c where c.note.objectID=?", noteID);
        return result.intValue();
    }
}
