package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;

/**
 * User: Ilhombek
 * Date: 9/25/12
 * Time: 11:39 AM
 */
public interface BugReportServiceLocal {

    Integer addNote(HistoryListItem item);

}