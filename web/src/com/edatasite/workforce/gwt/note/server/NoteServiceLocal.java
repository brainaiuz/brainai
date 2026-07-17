package com.edatasite.workforce.gwt.note.server;

import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.NewsComment;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.rest.base.to.CommentTO;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.base.to.NoteTO;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 12.10.2010
 * Time: 20:30:55
 * To change this template use File | Settings | File Templates.
 */
public interface NoteServiceLocal {
    void deleteNote(Integer id);

    Integer saveNote(HistoryListItem item);

    HistoryListItem getNote(Integer id);

    ListResultTO<NoteTO> getNoteListForAPI(ListingFilterParameter filter);

    NoteTO getNoteForAPI(Integer objectId);

    ListResultTO<CommentTO> getCommentListForAPI(ListingFilterParameter filter);

    Integer saveNoteComment(NewsComment item);

    CommentTO getCommentForAPI(Integer id);

    void deleteNoteComment(Integer id);

    ListResult<HistoryListItem> noteList(ListingFilterParameter filterParametrs);

}
