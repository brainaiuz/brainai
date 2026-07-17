package com.edatasite.workforce.gwt.hrms.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

public class CertificateSolrItem implements IsSerializable {

    private Integer objectId;
    private String number;
    private SelectItem employee;
    private SelectItem type;
    private SelectItem currentApprover;
    private Date issuedDate;
    private SelectItem issuedBy;
    private Date createdDate;
    private SelectItem createdBy;
    private SelectItem status;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public SelectItem getEmployee() {
        return employee;
    }

    public void setEmployee(SelectItem employee) {
        this.employee = employee;
    }

    public SelectItem getType() {
        return type;
    }

    public void setType(SelectItem type) {
        this.type = type;
    }

    public SelectItem getCurrentApprover() {
        return currentApprover;
    }

    public void setCurrentApprover(SelectItem currentApprover) {
        this.currentApprover = currentApprover;
    }

    public Date getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(Date issuedDate) {
        this.issuedDate = issuedDate;
    }

    public SelectItem getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(SelectItem issuedBy) {
        this.issuedBy = issuedBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public SelectItem getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(SelectItem createdBy) {
        this.createdBy = createdBy;
    }

    public SelectItem getStatus() {
        return status;
    }

    public void setStatus(SelectItem status) {
        this.status = status;
    }
}
