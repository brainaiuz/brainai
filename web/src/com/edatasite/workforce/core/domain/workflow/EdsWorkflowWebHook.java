package com.edatasite.workforce.core.domain.workflow;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.customform.EdsModelCustom;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ItemTableEnum;
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
import javax.persistence.ManyToOne;
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
 * Date : 10.01.2022
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "workflow_web_hook")
public class EdsWorkflowWebHook extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String name;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow_id")
    private EdsWorkflowRule workflow;

    @Enumerated(EnumType.STRING)
    private WebHookMethod method;
    private String url;


    @Column(name = "date_format")
    private String dateFormat;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "web_hook_id")
    private List<EdsWebHookParameter> headers = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "body_id")
    private EdsWebHookBody body;

    @Column(name = "saveIntegrationId", columnDefinition = " boolean default false")
    private boolean saveIntegrationId = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id")
    private EdsModelCustom form;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "webHook")
    private List<EdsWebhookAttribute> responseAttributes = new ArrayList<>();

    @Column(name = "responseQuery")
    private String responseQuery;

    @Column(name = "responsedValue")
    private String responsedValue;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "webHook")
    private List<EdsWebHookResponse> webHookResponse;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_table_form")
    private ItemTableEnum itemTableForm;

    @Column(name = "item_table_uuid")
    private String itemTableUuid;

    @Column(name = "table_field_name")
    private String tableFieldName;

    public EdsWorkflowWebHook() {
    }

    public EdsWorkflowWebHook(Integer objectID, String name, String description, EdsWorkflowRule workflow, WebHookMethod method, String url, List<EdsWebHookParameter> headers, EdsWebHookBody body) {
        this.objectID = objectID;
        this.name = name;
        this.description = description;
        this.workflow = workflow;
        this.method = method;
        this.url = url;
        this.headers = headers;
        this.body = body;
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

    public EdsWorkflowRule getWorkflow() {
        return workflow;
    }

    public void setWorkflow(EdsWorkflowRule workflow) {
        this.workflow = workflow;
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

    public List<EdsWebHookParameter> getHeaders() {
        return headers;
    }

    public void setHeaders(List<EdsWebHookParameter> headers) {
        this.headers = headers;
    }

    public EdsWebHookBody getBody() {
        return body;
    }

    public void setBody(EdsWebHookBody body) {
        this.body = body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof EdsWorkflowWebHook))
            return false;
        if (!super.equals(o))
            return false;

        EdsWorkflowWebHook that = (EdsWorkflowWebHook) o;

        if (getObjectID() != null ? !getObjectID().equals(that.getObjectID()) : that.getObjectID() != null)
            return false;
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null)
            return false;
        if (getDescription() != null ? !getDescription().equals(that.getDescription()) : that.getDescription() != null)
            return false;
        if (getWorkflow() != null ? !getWorkflow().equals(that.getWorkflow()) : that.getWorkflow() != null)
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
        result = 31 * result + (getWorkflow() != null ? getWorkflow().hashCode() : 0);
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

    public WorkflowWebHookItem getRpc(boolean isFully, List<String> itemTableColumns, List<SelectItem> templates) {
        WorkflowWebHookItem result = new WorkflowWebHookItem();
        result.setId(getObjectID());
        result.setName(getName());
        result.setDescription(getDescription());
        result.setWorkflow(getWorkflow() != null ? getWorkflow().getRPC(null) : null);
        result.setForm(getForm() != null ? new SelectItem(getForm().getObjectID(), getForm().getFormID()) : null);
        result.setDateFormats(new EdsCompanySettings().getAllDateFormats());
        result.setDateFormat(getDateFormat());
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
            result.setTableFieldName(getTableFieldName());
        }
        if (getResponseAttributes() != null) {
            HashMap<String, String> map = new HashMap<>();
            for (EdsWebhookAttribute attribute : getResponseAttributes()) {
                map.put(attribute.getKey(), attribute.getValue());
            }
            result.setResponseAttributes(map);
        }
        result.setItemTableColumns(itemTableColumns);
        result.setTemplates(templates);
        return result;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public boolean isSaveIntegrationId() {
        return saveIntegrationId;
    }

    public void setSaveIntegrationId(boolean saveIntegrationId) {
        this.saveIntegrationId = saveIntegrationId;
    }

    public EdsModelCustom getForm() {
        return form;
    }

    public void setForm(EdsModelCustom form) {
        this.form = form;
    }

    public List<EdsWebhookAttribute> getResponseAttributes() {
        return responseAttributes;
    }

    public void setResponseAttributes(List<EdsWebhookAttribute> responseAttributes) {
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

    public List<EdsWebHookResponse> getWebHookResponse() {
        return webHookResponse;
    }

    public void setWebHookResponse(List<EdsWebHookResponse> webHookResponse) {
        this.webHookResponse = webHookResponse;
    }

    public ItemTableEnum getItemTableForm() {
        return itemTableForm;
    }

    public void setItemTableForm(ItemTableEnum itemTableForm) {
        this.itemTableForm = itemTableForm;
    }

    public String getItemTableUuid() {
        return itemTableUuid;
    }

    public void setItemTableUuid(String itemTableUuid) {
        this.itemTableUuid = itemTableUuid;
    }

    public String getTableFieldName() {
        return tableFieldName;
    }

    public void setTableFieldName(String tableFieldName) {
        this.tableFieldName = tableFieldName;
    }
}
