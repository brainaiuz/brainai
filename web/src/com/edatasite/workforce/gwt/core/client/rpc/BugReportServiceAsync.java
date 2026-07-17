package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created by IntelliJ IDEA.
 * User: Unni
 * Date: Dec 11, 2008
 * Time: 12:11:13 PM
 * To change this template use File | Settings | File Templates.
 */
public interface BugReportServiceAsync {

    void sendBugReport(BugReportItem bugReportItem, AsyncCallback<Void> async);

    void sendBugReportNew(BugReportItem bugReportItem, AsyncCallback<Void> async);

    void addNote(HistoryListItem item, AsyncCallback<Integer> async);

    void deleteNote(Integer id, AsyncCallback<Void> callback);

    void deleteNoteComment(Integer noteCommentId, AsyncCallback<Void> callback);

    void noteList(AsyncCallback<ListResult<HistoryListItem>> async);

    void getNoteRelatedList(int i, AsyncCallback<SelectItem[]> async);

    void isEmployee(AsyncCallback<Boolean> callback);
}
