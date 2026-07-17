package com.edatasite.workforce.gwt.backend.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Unni
 * Date: Dec 11, 2008
 * Time: 2:39:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class BugListItem implements IsSerializable {
    public static final String BUG = "description";
    public static final String USER = "creatorName";
    public static final String CREATED_FROM = "createdFrom";
    public static final String UPDATE_TIME = "updateTime";
    public static final String PRIORITY = "priority";
    public static final String STATUS = "status";
    public static final String COMPANY = "company";
    public static final String CREATION_TIME = "creationTime";
    public static final String ASSIGNEE = "assignee";
    public static final String COMMENT = "comment";
    public static final String EMAIL = "email";
    public static final String BROWSER = "browser";
    public static final String LABEL = "label";
    public static final String BUG_ID = "id";

    private String bug;
    private String priority;
    private String status;
    private String creationTime;
    private String updateTime;
    private String updaterName;
    private String user;
    private String company;
    private String createdFrom;
    private String bugId;
    private String assignee;
    private String comment;
    private String email;
    private FileItem[] attachments;
    private BugListItem[] bugHistory;
    private BugComment[] bugCommentsHistr;
    private String browser;
    private String label;

    private String subject;

    public String getBugId() {
        return bugId;
    }

    public void setBugId(String bugId) {
        this.bugId = bugId;
    }

    public String getBug() {
        return bug;
    }

    public void setBug(String bug) {
        this.bug = bug;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdaterName() {
        return updaterName;
    }

    public void setUpdaterName(String updaterName) {
        this.updaterName = updaterName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCreatedFrom() {
        return createdFrom;
    }

    public void setCreatedFrom(String createdFrom) {
        this.createdFrom = createdFrom;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public BugListItem[] getBugHistory() {
        return bugHistory;
    }

    public void setBugHistory(BugListItem[] bugHistory) {
        this.bugHistory = bugHistory;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public BugComment[] getBugCommentsHistr() {
        return bugCommentsHistr;
    }

    public void setBugCommentsHistr(BugComment[] bugCommentsHistr) {
        this.bugCommentsHistr = bugCommentsHistr;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

}
