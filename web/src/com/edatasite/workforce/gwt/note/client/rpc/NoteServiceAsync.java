package com.edatasite.workforce.gwt.note.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 31-Aug-2009
 * Time: 14:32:22
 * To change this template use File | Settings | File Templates.
 */
public interface NoteServiceAsync {

    Request noteList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<HistoryListItem>> callback);

    void deleteNote(Integer id, AsyncCallback callback);

    void getNoteDates(ListingFilterParameter fp, AsyncCallback<SelectItem[]> callback);

    void getNoteUsers(ListingFilterParameter fp, AsyncCallback<SelectItem[]> callback);

    void getNoteRelation(ListingFilterParameter fp, AsyncCallback<SelectItem[]> callback);

    void getNote(Integer objectId, AsyncCallback<HistoryListItem> callback);

    void getEmployees(AsyncCallback<HashMap<WfmTreeItem, LinkedList<WfmTreeItem>>> callback);

//    void getNotecomments(Integer objectId, AsyncCallback<NewsComment[]> callback);

//    void saveNoteComment(NewsComment data, AsyncCallback<NewsComment> callback);
}