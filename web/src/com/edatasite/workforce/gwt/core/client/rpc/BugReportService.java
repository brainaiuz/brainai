package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Created by IntelliJ IDEA.
 * User: Unni
 * Date: Dec 11, 2008
 * Time: 12:10:55 PM
 * To change this template use File | Settings | File Templates.
 */
public interface BugReportService extends RemoteService {
    void sendBugReport(BugReportItem bugReportItem);

    void sendBugReportNew(BugReportItem bugReportItem);

    Integer addNote(HistoryListItem item);

    void deleteNote(Integer id);

    void deleteNoteComment(Integer noteCommentId);

    ListResult<HistoryListItem> noteList();

    SelectItem[] getNoteRelatedList(int i);

    Boolean isEmployee();

    class App {
        public static BugReportServiceAsync get() {
            ServiceDefTarget target = GWT.create(CoreGenericService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/bugreport");
            return (BugReportServiceAsync) target;
        }
    }
}
