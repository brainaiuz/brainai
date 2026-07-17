package com.workforcetrack.mobile.services;

import com.workforcetrack.mobile.rpc.bugReport.MBugReportItem;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/1/11
 * Time: 2:31 PM
 * To change this template use File | Settings | File Templates.
 */
public interface BugReportWebService {

    Boolean sendBugReport(MBugReportItem mBugReportItem);
}
