package com.edatasite.workforce.gwt.issue.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.Relational;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Acer
 * Date: 07-Jan-2008
 * Time: 19:10:22
 */
public class IssueListItem extends Relational implements IsSerializable, ListingCustomFields {

    public static final String NUMBER = "number";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String RELATED_TO = "relatedTo";
    public static final String TIMESHEET = "isTimeSheetEnabled";
    public static final String ACTION = "action";
    public static final String PRIORITY = "priority";
    public static final String STATUS = "status";
    public static final String PERIOD = "period";
    public static final String RESOLVER = "resolver";
    public static final String REPORTED_BY = "REPORTED_BY";
    public static final String LAST_MODIFIED = "lastmodified";

    private Integer objectID;
    private String name;
    private String number;
    private String relatedTo;
    private String description;
    private String status;
    private String reportedByName;
    private String resolver;
    private String priority;
    private Date startDate;
    private Date endDate;
    private boolean isTimeSheetEnabled;
    private Integer issueCreatorID;
    private String relatedToName;
    private String urlLink;
    private boolean timerIsStarted = false;
    private boolean showTimer = false;
    private Integer projectID;

    private List<CompanyCustomFieldItem> customFields;
    private Map<String, Object> customFieldsMap;
    private List<CompanyCustomFieldItem> customFieldsForFiltering;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRelatedTo() {
        return relatedTo;
    }

    public void setRelatedTo(String relatedTo) {
        this.relatedTo = relatedTo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReportedByName() {
        return reportedByName;
    }

    public void setReportedByName(String reportedByName) {
        this.reportedByName = reportedByName;
    }

    public String getResolver() {
        return resolver;
    }

    public void setResolver(String resolver) {
        this.resolver = resolver;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isTimeSheetEnabled() {
        return isTimeSheetEnabled;
    }

    public void setTimeSheetEnabled(boolean timeSheetEnabled) {
        isTimeSheetEnabled = timeSheetEnabled;
    }

    public Integer getIssueCreatorID() {
        return issueCreatorID;
    }

    public void setIssueCreatorID(Integer issueCreatorID) {
        this.issueCreatorID = issueCreatorID;
    }

    public String getRelatedToName() {
        return relatedToName;
    }

    public void setRelatedToName(String relatedToName) {
        this.relatedToName = relatedToName;
    }

    public String getUrlLink() {
        return urlLink;
    }

    public void setUrlLink(String urlLink) {
        this.urlLink = urlLink;
    }

    public boolean isTimerIsStarted() {
        return timerIsStarted;
    }

    public void setTimerIsStarted(boolean timerIsStarted) {
        this.timerIsStarted = timerIsStarted;
    }

    public boolean isShowTimer() {
        return showTimer;
    }

    public void setShowTimer(boolean showTimer) {
        this.showTimer = showTimer;
    }

    public Integer getProjectID() {
        return projectID;
    }

    public void setProjectID(Integer projectID) {
        this.projectID = projectID;
    }

    public List<CompanyCustomFieldItem> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(List<CompanyCustomFieldItem> customFields) {
        this.customFields = customFields;
    }

    public Map<String, Object> getCustomFieldsMap() {
        if (customFieldsMap == null) {
            customFieldsMap = new HashMap<>();
        }
        return customFieldsMap;
    }

    public void setCustomFieldsMap(Map<String, Object> customFieldsMap) {
        this.customFieldsMap = customFieldsMap;
    }

    public List<CompanyCustomFieldItem> getCustomFieldsForFiltering() {
        return customFieldsForFiltering;
    }

    public void setCustomFieldsForFiltering(List<CompanyCustomFieldItem> customFieldsForFiltering) {
        this.customFieldsForFiltering = customFieldsForFiltering;
    }

    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return getCustomFieldsMap().get(columnCodeKey);
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {
        getCustomFieldsMap().put(columnCodeKey, cellValue);
    }

    @Override
    public Integer getRelationID() {
        return getObjectID();
    }

    @Override
    public String getRelationType() {
        return RelationItem.TYPE_ISSUE;
    }

    @Override
    public String getRelationName() {
        return getName();
    }
}