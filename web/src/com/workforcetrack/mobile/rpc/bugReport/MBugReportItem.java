package com.workforcetrack.mobile.rpc.bugReport;

import com.edatasite.workforce.gwt.core.client.rpc.BugReportItem;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/1/11
 * Time: 2:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class MBugReportItem {

    private String reportText;
    private String reportSection;
    private Integer reportedBy;
    private String userAgent;
    //private Integer priorityID;

    //private String bugStatus;
    //private Date creationDate;

    public MBugReportItem() {
    }

    public MBugReportItem(BugReportItem bugReportItem) {
        if (bugReportItem != null) {
            this.reportText = bugReportItem.getReportText();
            this.reportSection = bugReportItem.getReportSection();
            this.reportedBy = bugReportItem.getReportedBy();
            this.userAgent = bugReportItem.getUserAgent();
        }
    }

    public static boolean convert(BugReportItem bugReportItem, MBugReportItem mBugReportItem, boolean fromBugReportItem) {
        if (bugReportItem == null || mBugReportItem == null)
            return false;

        try {
            if (fromBugReportItem) {
                mBugReportItem.setReportText(bugReportItem.getReportText());
                mBugReportItem.setReportedBy(bugReportItem.getReportedBy());
                mBugReportItem.setReportSection(bugReportItem.getReportSection());
                mBugReportItem.setUserAgent(bugReportItem.getUserAgent());
            } else {
                bugReportItem.setReportText(mBugReportItem.getReportText());
                bugReportItem.setReportedBy(mBugReportItem.getReportedBy());
                bugReportItem.setReportSection(mBugReportItem.getReportSection());
                bugReportItem.setUserAgent(mBugReportItem.getUserAgent());
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getReportText() {
        return reportText;
    }

    public void setReportText(String reportText) {
        this.reportText = reportText;
    }

    public String getReportSection() {
        return reportSection;
    }

    public void setReportSection(String reportSection) {
        this.reportSection = reportSection;
    }

    public Integer getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(Integer reportedBy) {
        this.reportedBy = reportedBy;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }
}
