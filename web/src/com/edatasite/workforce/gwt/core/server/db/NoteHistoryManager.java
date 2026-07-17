package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsNoteHistory;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 09-Jul-2009
 * Time: 17:22:16
 * To change this template use File | Settings | File Templates.
 */
public interface NoteHistoryManager extends Manager<EdsNoteHistory> {

    List<EdsNoteHistory> getNoteList(ListingFilterParameter fp);

    Map<Integer, String> getLastNotesAsMap(Integer relationType, List<Integer> ids);

    Integer getListCount(ListingFilterParameter fp);

    void updateNotesWithAccountID(Integer objectID, List<Integer> otherAccountIDs);

    void updateNotesWithContactID(Integer contactID, List<Integer> otherContactIDs);
}