package com.edatasite.workforce.gwt.core.client.rpc.workflow;

import com.google.gwt.user.client.rpc.IsSerializable;

public class WebhookRequestItem implements IsSerializable {
    private Integer id;
    private Integer workflowId;
    private String formId;
    private String uuid;
    private boolean itemTable;
    private boolean isPublic;

    public WebhookRequestItem() {
    }

    public WebhookRequestItem(Integer id, boolean isPublic) {
        this.id = id;
        this.isPublic = isPublic;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
}
