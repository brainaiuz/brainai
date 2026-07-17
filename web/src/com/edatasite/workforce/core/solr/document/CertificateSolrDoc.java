package com.edatasite.workforce.core.solr.document;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.Date;

@SolrDocument(collection = "certificateCore")
public class CertificateSolrDoc extends BaseSolrDoc {

    @Id
    @Indexed(name = "oid", type = "string", required = true)
    private String oid;

    @Field("companyId")
    @Indexed(name = "companyId", type = "pint", required = true)
    private Integer companyId;

    @Field("certificateId")
    private Integer certificateId;

    @Field("number")
    private String number;

    @Field("employeeId")
    private Integer employeeId;

    @Field("employeeName")
    private String employeeName;

    @Field("employeeCode")
    private String employeeCode;

    @Field("employeeIdName")
    @Indexed(name = "employeeIdName", type = "string", stored = false)
    private String employeeIdName;

    @Field("typeId")
    @Indexed(name = "typeId", type = "pint", stored = false)
    private Integer typeId;

    @Field("typeName")
    private String typeName;

    @Field("typeIdName")
    @Indexed(name = "typeIdName", type = "string", stored = false)
    private String typeIdName;

    @Field("currentApproverId")
    @Indexed(name = "currentApproverId", type = "pint", stored = false)
    private Integer currentApproverId;

    @Field("currentApproverName")
    private String currentApproverName;

    @Field("currentApproverIdName")
    @Indexed(name = "currentApproverIdName", type = "string", stored = false)
    private String currentApproverIdName;

    @Field("issuedDate")
    private Date issuedDate;

    @Field("issuedById")
    @Indexed(name = "issuedById", type = "pint", stored = false)
    private Integer issuedById;

    @Field("issuedByName")
    private String issuedByName;

    @Field("issuedByIdName")
    @Indexed(name = "issuedByIdName", type = "string", stored = false)
    private String issuedByIdName;

    @Field("createdDate")
    private Date createdDate;

    @Field("createdById")
    @Indexed(name = "createdById", type = "pint", stored = false)
    private Integer createdById;

    @Field("createdByName")
    private String createdByName;

    @Field("createdByIdName")
    @Indexed(name = "createdByIdName", type = "string", stored = false)
    private String createdByIdName;

    @Field("statusId")
    @Indexed(name = "statusId", type = "pint", stored = false)
    private Integer statusId;

    @Field("statusName")
    private String statusName;

    @Field("statusIdName")
    @Indexed(name = "statusIdName", type = "string", stored = false)
    private String statusIdName;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getCertificateId() {
        return certificateId;
    }

    public void setCertificateId(Integer certificateId) {
        this.certificateId = certificateId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getEmployeeIdName() {
        return employeeIdName;
    }

    public void setEmployeeIdName(String employeeIdName) {
        this.employeeIdName = employeeIdName;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeIdName() {
        return typeIdName;
    }

    public void setTypeIdName(String typeIdName) {
        this.typeIdName = typeIdName;
    }

    public Integer getCurrentApproverId() {
        return currentApproverId;
    }

    public void setCurrentApproverId(Integer currentApproverId) {
        this.currentApproverId = currentApproverId;
    }

    public String getCurrentApproverName() {
        return currentApproverName;
    }

    public void setCurrentApproverName(String currentApproverName) {
        this.currentApproverName = currentApproverName;
    }

    public String getCurrentApproverIdName() {
        return currentApproverIdName;
    }

    public void setCurrentApproverIdName(String currentApproverIdName) {
        this.currentApproverIdName = currentApproverIdName;
    }

    public Date getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(Date issuedDate) {
        this.issuedDate = issuedDate;
    }

    public Integer getIssuedById() {
        return issuedById;
    }

    public void setIssuedById(Integer issuedById) {
        this.issuedById = issuedById;
    }

    public String getIssuedByName() {
        return issuedByName;
    }

    public void setIssuedByName(String issuedByName) {
        this.issuedByName = issuedByName;
    }

    public String getIssuedByIdName() {
        return issuedByIdName;
    }

    public void setIssuedByIdName(String issuedByIdName) {
        this.issuedByIdName = issuedByIdName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Integer createdById) {
        this.createdById = createdById;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getCreatedByIdName() {
        return createdByIdName;
    }

    public void setCreatedByIdName(String createdByIdName) {
        this.createdByIdName = createdByIdName;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusIdName() {
        return statusIdName;
    }

    public void setStatusIdName(String statusIdName) {
        this.statusIdName = statusIdName;
    }
}
