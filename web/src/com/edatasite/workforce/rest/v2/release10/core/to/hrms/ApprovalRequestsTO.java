package com.edatasite.workforce.rest.v2.release10.core.to.hrms;

/**
 * Created by Farrukh Abdurakhmonov on 11/01/2018.
 */
public class ApprovalRequestsTO extends ApprovalOtherRequestTypeTO {
    private Integer id;
    private String title;
    private String approver;
    private String requester;
    private String description;
    private Object status;

    public ApprovalRequestsTO() {
    }

    public ApprovalRequestsTO(String type) {
        super(type);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getRequester() {
        return requester;
    }

    public void setRequester(String requester) {
        this.requester = requester;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }
}
