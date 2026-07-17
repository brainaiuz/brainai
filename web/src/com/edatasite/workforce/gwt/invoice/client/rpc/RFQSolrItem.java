package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RFQSolrItem implements IsSerializable {

    private Integer objectID;
    private String rfqNumber;
    private SelectItem client;
    private SelectItem country;
    private SelectItem relatedProject;
    private List<SelectItem> multiProjects = new ArrayList<>();
    private ReferenceItem status;
    private SelectItem creator;
    private SelectItem currentApprover;
    private List<Integer> itemIds = new ArrayList<>();
    private Date dueDate;
    private Date rfqDate;
    private Date creationDate;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getRfqNumber() {
        return rfqNumber;
    }

    public void setRfqNumber(String rfqNumber) {
        this.rfqNumber = rfqNumber;
    }

    public SelectItem getClient() {
        return client;
    }

    public void setClient(SelectItem client) {
        this.client = client;
    }

    public SelectItem getCountry() {
        return country;
    }

    public void setCountry(SelectItem country) {
        this.country = country;
    }

    public SelectItem getRelatedProject() {
        return relatedProject;
    }

    public void setRelatedProject(SelectItem relatedProject) {
        this.relatedProject = relatedProject;
    }

    public List<SelectItem> getMultiProjects() {
        return multiProjects;
    }

    public void setMultiProjects(List<SelectItem> multiProjects) {
        this.multiProjects = multiProjects;
    }

    public ReferenceItem getStatus() {
        return status;
    }

    public void setStatus(ReferenceItem status) {
        this.status = status;
    }

    public SelectItem getCreator() {
        return creator;
    }

    public void setCreator(SelectItem creator) {
        this.creator = creator;
    }

    public SelectItem getCurrentApprover() {
        return currentApprover;
    }

    public void setCurrentApprover(SelectItem currentApprover) {
        this.currentApprover = currentApprover;
    }

    public List<Integer> getItemIds() {
        return itemIds;
    }

    public void setItemIds(List<Integer> itemIds) {
        this.itemIds = itemIds;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getRfqDate() {
        return rfqDate;
    }

    public void setRfqDate(Date rfqDate) {
        this.rfqDate = rfqDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
