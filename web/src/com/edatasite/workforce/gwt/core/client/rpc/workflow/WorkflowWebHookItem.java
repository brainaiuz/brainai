package com.edatasite.workforce.gwt.core.client.rpc.workflow;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.List;
import java.util.Map;

public class WorkflowWebHookItem extends WorkflowWebHookListItem implements IsSerializable {
    private String method;
    private String requestUrl;

    private Map<String, String> headers;

    private String bodyType;
    private Map<String, String> formDataParams;
    private String rawDataFormat;
    private String rawDataText;
    private String dateFormat;
    private SelectItem[] dateFormats;
    private boolean saveIntegrationId;

    private String queryResponse;
    private String responsedValue;
    private String tableFieldName;

    private List<SelectItem> templates;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public Map<String, String> getFormDataParams() {
        return formDataParams;
    }

    public void setFormDataParams(Map<String, String> formDataParams) {
        this.formDataParams = formDataParams;
    }

    public String getRawDataFormat() {
        return rawDataFormat;
    }

    public void setRawDataFormat(String rawDataFormat) {
        this.rawDataFormat = rawDataFormat;
    }

    public String getRawDataText() {
        return rawDataText;
    }

    public void setRawDataText(String rawDataText) {
        this.rawDataText = rawDataText;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public SelectItem[] getDateFormats() {
        return dateFormats;
    }

    public void setDateFormats(SelectItem[] dateFormats) {
        this.dateFormats = dateFormats;
    }

    public boolean isSaveIntegrationId() {
        return saveIntegrationId;
    }

    public void setSaveIntegrationId(boolean saveIntegrationId) {
        this.saveIntegrationId = saveIntegrationId;
    }

    public String getQueryResponse() {
        return queryResponse;
    }

    public void setQueryResponse(String queryResponse) {
        this.queryResponse = queryResponse;
    }

    public String getTableFieldName() {
        return tableFieldName;
    }

    public void setTableFieldName(String tableFieldName) {
        this.tableFieldName = tableFieldName;
    }

    public String getResponsedValue() {
        return responsedValue;
    }

    public void setResponsedValue(String responsedValue) {
        this.responsedValue = responsedValue;
    }

    public List<SelectItem> getTemplates() {
        return templates;
    }

    public void setTemplates(List<SelectItem> templates) {
        this.templates = templates;
    }
}
