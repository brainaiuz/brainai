package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsNoteComment;

/**
 * Created by IntelliJ IDEA.
 * User: Employee
 * Date: Nov 4, 2009
 * Time: 12:05:06 PM
 * To change this template use File | Settings | File Templates.
 */
public interface NoteCommentManager extends Manager<EdsNoteComment> {
    void deleteNoteComments(Integer noteID);

    Integer getCommentCountByNoteID(Integer objectID);
}
