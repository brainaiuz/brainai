package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

public class PositionSolrItem implements IsSerializable {

    private Integer objectId;
    private String number;
    private String name;
    private SelectItem status;
    private String employeeCount;
    private Integer vacantCount;
    private SelectItem location;
    private SelectItem department;
    private ReferenceLocale departmentLocale;
    private SelectItem createdBy;
    private Date createdDate;
    private SelectItem modifiedBy;
    private Date modifiedDate;
    private SelectItem type;
    private ReferenceLocale typeLocale;
    private ReferenceLocale nameLocale;
    private ReferenceLocale statusLocale;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SelectItem getStatus() {
        return status;
    }

    public void setStatus(SelectItem status) {
        this.status = status;
    }

    public String getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(String employeeCount) {
        this.employeeCount = employeeCount;
    }

    public SelectItem getLocation() {
        return location;
    }

    public void setLocation(SelectItem location) {
        this.location = location;
    }

    public SelectItem getDepartment() {
        return department;
    }

    public void setDepartment(SelectItem department) {
        this.department = department;
    }

    public ReferenceLocale getDepartmentLocale() {
        return departmentLocale;
    }

    public void setDepartmentLocale(ReferenceLocale departmentLocale) {
        this.departmentLocale = departmentLocale;
    }

    public SelectItem getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(SelectItem createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public SelectItem getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(SelectItem modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public SelectItem getType() {
        return type;
    }

    public void setType(SelectItem type) {
        this.type = type;
    }

    public ReferenceLocale getTypeLocale() {
        return typeLocale;
    }

    public void setTypeLocale(ReferenceLocale typeLocale) {
        this.typeLocale = typeLocale;
    }

    public ReferenceLocale getNameLocale() {
        return nameLocale;
    }

    public void setNameLocale(ReferenceLocale nameLocale) {
        this.nameLocale = nameLocale;
    }

    public ReferenceLocale getStatusLocale() {
        return statusLocale;
    }

    public void setStatusLocale(ReferenceLocale statusLocale) {
        this.statusLocale = statusLocale;
    }

    public Integer getVacantCount() {
        return vacantCount;
    }

    public void setVacantCount(Integer vacantCount) {
        this.vacantCount = vacantCount;
    }
}
