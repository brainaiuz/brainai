package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

public class CustomFormSolrRPC implements IsSerializable {

    private Integer objectId;
    private String formId;
    private Integer itemId;
    private String formName;
    private SelectItem creator;
    private Date createdDate;
    private Date updatedDate;
    private SelectItem updater;
    private SelectItem status;
    private SelectItem currentApprover;
    private String customFieldKey;
    private String customFieldValue;
    private String docType;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public SelectItem getCreator() {
        return creator;
    }

    public void setCreator(SelectItem creator) {
        this.creator = creator;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public SelectItem getUpdater() {
        return updater;
    }

    public void setUpdater(SelectItem updater) {
        this.updater = updater;
    }

    public SelectItem getStatus() {
        return status;
    }

    public void setStatus(SelectItem status) {
        this.status = status;
    }

    public SelectItem getCurrentApprover() {
        return currentApprover;
    }

    public void setCurrentApprover(SelectItem currentApprover) {
        this.currentApprover = currentApprover;
    }

    public String getCustomFieldKey() {
        return customFieldKey;
    }

    public void setCustomFieldKey(String customFieldKey) {
        this.customFieldKey = customFieldKey;
    }

    public String getCustomFieldValue() {
        return customFieldValue;
    }

    public void setCustomFieldValue(String customFieldValue) {
        this.customFieldValue = customFieldValue;
    }

    public String getDocType() {
        return docType;
    }

    public void setDocType(String docType) {
        this.docType = docType;
    }
}
