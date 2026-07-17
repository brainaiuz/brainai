package com.edatasite.workforce.gwt.hrms.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: Abdulaziz
 * Date: 13.05.2009
 * Time: 11:59:07
 */
public class PerformanceNoteItem implements IsSerializable {

    public static final String ACTION = "ACTION";
    public static final String NAME = "NAME";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String RELATED_TO = "RELATED_TO";
    public static final String PERIOD = "PERIOD";
    public static final String STATUS = "STATUS";
    public static final String PRIORITY = "PRIORITY";
    public static final String REPORTED_BY = "REPORTED_BY";
    public static final String RESOLVER = "RESOLVER";

    private FileItem[] attachments;
    private Integer objectID;
    private Integer currentUserID;
    private String description;
    private DateNonConvertable endDate;
    private boolean isIncident;
    private boolean isPublic;
    private String name;
    private Integer relatedToID;
    private String relatedToName;
    private Integer reportedByID;
    private String reportedByName;
    private Integer resolverID;
    private String resolverName;
    private DateNonConvertable startDate;
    private Integer statusID;
    private String statusName;
    private String statusCode;
    private Integer priorityID;
    private String priorityName;
    private String priorityCode;
    private SelectItem[] statuses;
    private SelectItem[] relatedToEmployees;
    private SelectItem[] reportedByItems;
    private SelectItem[] priorities;

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getCurrentUserID() {
        return currentUserID;
    }

    public void setCurrentUserID(Integer currentUserID) {
        this.currentUserID = currentUserID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DateNonConvertable getEndDate() {
        return endDate;
    }

    public void setEndDate(DateNonConvertable endDate) {
        this.endDate = endDate;
    }

    public boolean isIncident() {
        return isIncident;
    }

    public void setIncident(boolean incident) {
        isIncident = incident;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRelatedToID() {
        return relatedToID;
    }

    public void setRelatedToID(Integer relatedToID) {
        this.relatedToID = relatedToID;
    }

    public String getRelatedToName() {
        return relatedToName;
    }

    public void setRelatedToName(String relatedToName) {
        this.relatedToName = relatedToName;
    }

    public Integer getReportedByID() {
        return reportedByID;
    }

    public void setReportedByID(Integer reportedByID) {
        this.reportedByID = reportedByID;
    }

    public String getReportedByName() {
        return reportedByName;
    }

    public void setReportedByName(String reportedByName) {
        this.reportedByName = reportedByName;
    }

    public Integer getResolverID() {
        return resolverID;
    }

    public void setResolverID(Integer resolverID) {
        this.resolverID = resolverID;
    }

    public String getResolverName() {
        return resolverName;
    }

    public void setResolverName(String resolverName) {
        this.resolverName = resolverName;
    }

    public DateNonConvertable getStartDate() {
        return startDate;
    }

    public void setStartDate(DateNonConvertable startDate) {
        this.startDate = startDate;
    }

    public Integer getStatusID() {
        return statusID;
    }

    public void setStatusID(Integer statusID) {
        this.statusID = statusID;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public SelectItem[] getStatuses() {
        return statuses;
    }

    public void setStatuses(SelectItem[] statuses) {
        this.statuses = statuses;
    }

    public SelectItem[] getRelatedToEmployees() {
        return relatedToEmployees;
    }

    public void setRelatedToEmployees(SelectItem[] relatedToEmployees) {
        this.relatedToEmployees = relatedToEmployees;
    }

    public SelectItem[] getReportedByItems() {
        return reportedByItems;
    }

    public void setReportedByItems(SelectItem[] reportedByItems) {
        this.reportedByItems = reportedByItems;
    }

    public Integer getPriorityID() {
        return priorityID;
    }

    public void setPriorityID(Integer priorityID) {
        this.priorityID = priorityID;
    }

    public SelectItem[] getPriorities() {
        return priorities;
    }

    public void setPriorities(SelectItem[] priorities) {
        this.priorities = priorities;
    }

    public String getPriorityName() {
        return priorityName;
    }

    public void setPriorityName(String priorityName) {
        this.priorityName = priorityName;
    }

    public String getPriorityCode() {
        return priorityCode;
    }

    public void setPriorityCode(String priorityCode) {
        this.priorityCode = priorityCode;
    }
}