package com.edatasite.workforce.core.domain.webhook;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.workflow.WebHookMethod;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowWebHookItem;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * User : Akhror
 * Date : 04.03.2025
 */
@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "web_hook")
public class EdsPublicWebHook extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private WebHookMethod method;
    private String url;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "web_hook_id")
    private List<EdsPublicWebHookParameter> headers = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "body_id")
    private EdsPublicWebHookBody body;

    @Column(name = "saveIntegrationId", columnDefinition = " boolean default false")
    private boolean saveIntegrationId = false;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "webHook")
    private List<EdsPublicWebhookAttribute> responseAttributes = new ArrayList<>();

    @Column(name = "responseQuery")
    private String responseQuery;

    @Column(name = "responsedValue")
    private String responsedValue;


    public EdsPublicWebHook() {
    }

    @Override
    public Integer getObjectID() {
        return objectID;
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

    public WebHookMethod getMethod() {
        return method;
    }

    public void setMethod(WebHookMethod method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<EdsPublicWebHookParameter> getHeaders() {
        return headers;
    }

    public void setHeaders(List<EdsPublicWebHookParameter> headers) {
        this.headers = headers;
    }

    public EdsPublicWebHookBody getBody() {
        return body;
    }

    public void setBody(EdsPublicWebHookBody body) {
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof EdsPublicWebHook that))
            return false;
        if (!super.equals(o))
            return false;

        if (getObjectID() != null ? !getObjectID().equals(that.getObjectID()) : that.getObjectID() != null)
            return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null)
            return false;
        if (getDescription() != null ? !getDescription().equals(that.getDescription()) : that.getDescription() != null)
            return false;
        if (getMethod() != that.getMethod())
            return false;
        if (getUrl() != null ? !getUrl().equals(that.getUrl()) : that.getUrl() != null)
            return false;
        if (getHeaders() != null ? !getHeaders().equals(that.getHeaders()) : that.getHeaders() != null)
            return false;
        if (getBody() != null ? !getBody().equals(that.getBody()) : that.getBody() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getObjectID() != null ? getObjectID().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getMethod() != null ? getMethod().hashCode() : 0);
        result = 31 * result + (getUrl() != null ? getUrl().hashCode() : 0);
        result = 31 * result + (getHeaders() != null ? getHeaders().hashCode() : 0);
        result = 31 * result + (getBody() != null ? getBody().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getObjectID() + " " + getName() + " " + getDescription();
    }

    public WorkflowWebHookItem getRpc(boolean isFully) {
        WorkflowWebHookItem result = new WorkflowWebHookItem();
        result.setId(getObjectID());
        result.setName(getName());
        result.setDescription(getDescription());
        result.setSaveIntegrationId(isSaveIntegrationId());
        result.setQueryResponse(getResponseQuery());
        result.setResponsedValue(getResponsedValue());
        if (isFully) {
            result.setMethod(getMethod().name());
            result.setRequestUrl(getUrl());

            if (getHeaders() != null && !getHeaders().isEmpty()) {
                Map<String, String> headers = new LinkedHashMap<>();
                getHeaders().forEach(h -> headers.put(h.getName(), h.getValue()));
                result.setHeaders(headers);
            }

            result.setBodyType(getBody().getType().name());
            if (getBody().getFormDataParams() != null && !getBody().getFormDataParams().isEmpty()) {
                Map<String, String> params = new LinkedHashMap<>();
                getBody().getFormDataParams().forEach(h -> params.put(h.getName(), h.getValue()));
                result.setFormDataParams(params);
            }
            result.setRawDataText(getBody().getRawText());
            result.setRawDataFormat(getBody().getFormat());
        }
        if (getResponseAttributes() != null) {
            HashMap<String, String> map = new HashMap<>();
            for (EdsPublicWebhookAttribute attribute : getResponseAttributes()) {
                map.put(attribute.getKey(), attribute.getValue());
            }
            result.setResponseAttributes(map);
        }
        return result;
    }

    public boolean isSaveIntegrationId() {
        return saveIntegrationId;
    }

    public void setSaveIntegrationId(boolean saveIntegrationId) {
        this.saveIntegrationId = saveIntegrationId;
    }


    public List<EdsPublicWebhookAttribute> getResponseAttributes() {
        return responseAttributes;
    }

    public void setResponseAttributes(List<EdsPublicWebhookAttribute> responseAttributes) {
        this.responseAttributes = responseAttributes;
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
}
