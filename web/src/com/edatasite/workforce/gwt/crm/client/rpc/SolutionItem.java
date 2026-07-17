package com.edatasite.workforce.gwt.crm.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 09-Jul-2009
 * Time: 18:22:40
 * To change this template use File | Settings | File Templates.
 */
public class SolutionItem implements IsSerializable {
    public static final String TITLE = "title";
    public static final String ASSIGNEE = "assignee";
    public static final String STATUS = "status";
    public static final String QUESTION = "question";
    public static final String ANSWER = "answer";

    private Integer objectId;
    private String title;

    private SelectItem[] assignees;
    private Integer assigneeId;
    private String assignee;

    private SelectItem[] statuses;
    private Integer statusId;
    private String status;

    private String question;
    private String answer;
    private String details;

    private SolutionCaseItem crmCase;
    private FileItem[] attachments;

    public SolutionCaseItem getCrmCase() {
        return crmCase;
    }

    public void setCrmCase(SolutionCaseItem crmCase) {
        this.crmCase = crmCase;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public SelectItem[] getStatuses() {
        return statuses;
    }

    public void setStatuses(SelectItem[] statuses) {
        this.statuses = statuses;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public SelectItem[] getAssignees() {
        return assignees;
    }

    public void setAssignees(SelectItem[] assignees) {
        this.assignees = assignees;
    }

    public Integer getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Integer assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public FileItem[] getAttachments() {
        return attachments;
    }
}