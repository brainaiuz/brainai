package com.workforcetrack.mobile.services;

import com.edatasite.workforce.gwt.core.client.rpc.BugReportItem;
import com.edatasite.workforce.gwt.core.client.rpc.BugReportService;
import com.workforcetrack.mobile.rpc.bugReport.MBugReportItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/1/11
 * Time: 2:51 PM
 */
@Service("bugReportWebService")
public class BugReportWebServiceImpl implements BugReportWebService {

    @Autowired
    @Qualifier("bugReportService")
    BugReportService bugReportService;

    @Override
    public Boolean sendBugReport(MBugReportItem mBugReportItem) {
        if (mBugReportItem == null)
            return false;

        BugReportItem bugReportItem = new BugReportItem();
        if (MBugReportItem.convert(bugReportItem, mBugReportItem, false)) {
            bugReportService.sendBugReport(bugReportItem);
            return true;
        }
        return false;
    }


}
