package com.edatasite.workforce.gwt.core.client.rpc.workflow;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.HashMap;
import java.util.List;

public class WorkflowWebHookListItem implements IsSerializable {

    private Integer id;
    private String name;
    private String description;
    private WorkflowRule workflow;
    private SelectItem form;
    private Integer workflowId;
    private String formId;
    private String uuid;
    private boolean itemTable;
    private List<String> itemTableColumns;
    private HashMap<String, String> responseAttributes;

    private HashMap<String, String> queryAtributes;

    private boolean isPublic;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public WorkflowRule getWorkflow() {
        return workflow;
    }

    public void setWorkflow(WorkflowRule workflow) {
        this.workflow = workflow;
    }

    public SelectItem getForm() {
        return form;
    }

    public void setForm(SelectItem form) {
        this.form = form;
    }

    public Integer getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(Integer workflowId) {
        this.workflowId = workflowId;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public HashMap<String, String> getResponseAttributes() {
        if (responseAttributes == null) {
            responseAttributes = new HashMap<>();
        }
        return responseAttributes;
    }

    public void setResponseAttributes(HashMap<String, String> responseAttributes) {
        this.responseAttributes = responseAttributes;
    }

    public void addResponseAttribute(String key, String value) {
        if (responseAttributes == null) {
            responseAttributes = new HashMap<>();
        }
        responseAttributes.put(key, value);
    }

    public HashMap<String, String> getQueryAtributes() {
        return queryAtributes;
    }

    public void addQueryAtributes(String key, String value) {
        if (queryAtributes == null) {
            queryAtributes = new HashMap<>();
        }
        queryAtributes.put(key, value);
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean isItemTable() {
        return itemTable;
    }

    public void setItemTable(boolean itemTable) {
        this.itemTable = itemTable;
    }

    public List<String> getItemTableColumns() {
        return itemTableColumns;
    }

    public void setItemTableColumns(List<String> itemTableColumns) {
        this.itemTableColumns = itemTableColumns;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}
