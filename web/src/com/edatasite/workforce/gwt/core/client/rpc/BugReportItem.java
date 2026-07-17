package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 03-Jul-2009
 * Time: 11:00:26
 * To change this template use File | Settings | File Templates.
 */
public class BugReportItem implements IsSerializable {

    private String reportText;
    private String reportSection;
    private Integer reportedBy;
    private FileItem[] attachments;
    private String userAgent;
    private Integer priorityID;
    private Integer typeId;
    private String priority;

    private String bugStatus;
    private Date creationDate;
    private String subjectText;
    private boolean isAnonim;

    public Integer getPriorityID() {
        return priorityID;
    }

    public void setPriorityID(Integer priorityID) {
        this.priorityID = priorityID;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
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

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getBugStatus() {
        return bugStatus;
    }

    public void setBugStatus(String bugStatus) {
        this.bugStatus = bugStatus;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setSubjectText(String subjectText) {
        this.subjectText = subjectText;
    }

    public String getSubjectText() {
        return subjectText;
    }

    public boolean getIsAnonim() {
        return isAnonim;
    }

    public void setAnonim(boolean isAnonim) {
        this.isAnonim = isAnonim;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }
}
