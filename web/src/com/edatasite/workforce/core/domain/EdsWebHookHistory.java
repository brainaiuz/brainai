package com.edatasite.workforce.core.domain;


import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "webHookHistory")
public class EdsWebHookHistory extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;
    @Column(name = "web_hook_id")
    private Integer webHookId;
    private String name;
    private String description;
    @Column(name = "workflow_id")
    private Integer workflowId;

    private String method;
    private String url;

    @Column(name = "date_format")
    private String dateFormat;
    @Column(columnDefinition = "text")
    private String shortText;
    private String bodyType;
    private String bodyFormat;

    @Column(name = "saveIntegrationId", columnDefinition = " boolean default false")
    private boolean saveIntegrationId = false;


    @Column(name = "responseQuery")
    private String responseQuery;

    @Column(name = "responsedValue")
    private String responsedValue;



    @Column(name = "table_field_name")
    private String tableFieldName;

    private Integer userId;
    private Date date;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getWebHookId() {
        return webHookId;
    }

    public void setWebHookId(Integer webHookId) {
        this.webHookId = webHookId;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Integer workflowId) {
        this.workflowId = workflowId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getShortText() {
        return shortText;
    }

    public void setShortText(String shortText) {
        this.shortText = shortText;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public String getBodyFormat() {
        return bodyFormat;
    }

    public void setBodyFormat(String bodyFormat) {
        this.bodyFormat = bodyFormat;
    }

    public boolean isSaveIntegrationId() {
        return saveIntegrationId;
    }

    public void setSaveIntegrationId(boolean saveIntegrationId) {
        this.saveIntegrationId = saveIntegrationId;
    }



    public String getResponseQuery() {
        return responseQuery;
    }

    public void setResponseQuery(String responseQuery) {
        this.responseQuery = responseQuery;
    }

    public String getResponsedValue() {
        return responsedValue;
    }

    public void setResponsedValue(String responsedValue) {
        this.responsedValue = responsedValue;
    }

    public String getTableFieldName() {
        return tableFieldName;
    }

    public void setTableFieldName(String tableFieldName) {
        this.tableFieldName = tableFieldName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
