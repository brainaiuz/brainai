package com.edatasite.workforce.core.solr.document;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.Date;

@SolrDocument(collection = "positionCore")
public class PositionSolrDoc extends BaseSolrDoc {
    @Id
    @Indexed(name = "oid", type = "string", required = true)
    private String oid;

    @Field("companyId")
    @Indexed(name = "companyId", type = "pint", required = true)
    private Integer companyId;

    @Field("positionId")
    @Indexed(name = "positionId", type = "pint", required = true)
    private Integer positionId;

    @Field("number")
    private String number;

    @Field("name")
    private String name;

    @Field("statusId")
    private Integer statusId;

    @Field("statusName")
    private String statusName;

    @Field("statusCode")
    private String statusCode;

    @Field("statusIdName")
    @Indexed(name = "statusIdName", type = "string", stored = false)
    private String statusIdName;

    @Field("employeeCount")
    private String employeeCount;

    @Field("locationId")
    private Integer locationId;

    @Field("locationName")
    private String locationName;

    @Field("locationIdName")
    @Indexed(name = "locationIdName", type = "string", stored = false)
    private String locationIdName;

    @Field("departmentId")
    private Integer departmentId;

    @Field("departmentName")
    private String departmentName;

    @Field("departmentIdName")
    @Indexed(name = "departmentIdName", type = "string", stored = false)
    private String departmentIdName;

    @Field("departmentNameUz")
    private String departmentNameUz;

    @Field("departmentNameRu")
    private String departmentNameRu;

    @Field("departmentNameEn")
    private String departmentNameEn;

    @Field("departmentNameAr")
    private String departmentNameAr;

    @Field("vacantCount")
    private Integer vacantCount;

    @Field("createdById")
    private Integer createdById;

    @Field("createdByName")
    private String createdByName;

    @Field("createdByIdName")
    @Indexed(name = "createdByIdName", type = "string", stored = false)
    private String createdByIdName;

    @Field("createdDate")
    private Date createdDate;

    @Field("modifiedById")
    private Integer modifiedById;

    @Field("modifiedByName")
    private String modifiedByName;

    @Field("modifiedByIdName")
    @Indexed(name = "modifiedByIdName", type = "string", stored = false)
    private String modifiedByIdName;

    @Field("modifiedDate")
    private Date modifiedDate;

    @Field("typeId")
    private Integer typeId;

    @Field("typeName")
    private String typeName;

    @Field("typeNameUz")
    private String typeNameUz;
    @Field("typeNameRu")
    private String typeNameRu;
    @Field("typeNameEn")
    private String typeNameEn;
    @Field("typeNameAr")
    private String typeNameAr;

    @Field("typeCode")
    private String typeCode;

    @Field("typeIdName")
    @Indexed(name = "typeIdName", type = "string", stored = false)
    private String typeIdName;

    @Field("nameUz")
    private String nameUz;
    @Field("nameRu")
    private String nameRu;
    @Field("nameEn")
    private String nameEn;
    @Field("nameAr")
    private String nameAr;

    @Field("statusUz")
    private String statusUz;
    @Field("statusRu")
    private String statusRu;
    @Field("statusEn")
    private String statusEn;
    @Field("statusAr")
    private String statusAr;

    public PositionSolrDoc(String oid, Integer companyId, Integer positionId, String number, String name, Integer statusId, String statusName, String statusCode, String statusIdName, String employeeCount, Integer locationId, String locationName, String locationIdName, Integer departmentId, String departmentName, String departmentIdName, String departmentNameUz, String departmentNameRu, String departmentNameEn, String departmentNameAr, Integer vacantCount, Integer createdById, String createdByName, String createdByIdName, Date createdDate, Integer modifiedById, String modifiedByName, String modifiedByIdName, Date modifiedDate, Integer typeId, String typeName, String typeNameUz, String typeNameRu, String typeNameEn, String typeNameAr, String typeCode, String typeIdName, String nameUz, String nameRu, String nameEn, String nameAr, String statusUz, String statusRu, String statusEn, String statusAr) {
        this.oid = oid;
        this.companyId = companyId;
        this.positionId = positionId;
        this.number = number;
        this.name = name;
        this.statusId = statusId;
        this.statusName = statusName;
        this.statusCode = statusCode;
        this.statusIdName = statusIdName;
        this.employeeCount = employeeCount;
        this.locationId = locationId;
        this.locationName = locationName;
        this.locationIdName = locationIdName;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.departmentIdName = departmentIdName;
        this.departmentNameUz = departmentNameUz;
        this.departmentNameRu = departmentNameRu;
        this.departmentNameEn = departmentNameEn;
        this.departmentNameAr = departmentNameAr;
        this.vacantCount = vacantCount;
        this.createdById = createdById;
        this.createdByName = createdByName;
        this.createdByIdName = createdByIdName;
        this.createdDate = createdDate;
        this.modifiedById = modifiedById;
        this.modifiedByName = modifiedByName;
        this.modifiedByIdName = modifiedByIdName;
        this.modifiedDate = modifiedDate;
        this.typeId = typeId;
        this.typeName = typeName;
        this.typeNameUz = typeNameUz;
        this.typeNameRu = typeNameRu;
        this.typeNameEn = typeNameEn;
        this.typeNameAr = typeNameAr;
        this.typeCode = typeCode;
        this.typeIdName = typeIdName;
        this.nameUz = nameUz;
        this.nameRu = nameRu;
        this.nameEn = nameEn;
        this.nameAr = nameAr;
        this.statusUz = statusUz;
        this.statusRu = statusRu;
        this.statusEn = statusEn;
        this.statusAr = statusAr;
    }

    public PositionSolrDoc() {
    }

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

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
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

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusIdName() {
        return statusIdName;
    }

    public void setStatusIdName(String statusIdName) {
        this.statusIdName = statusIdName;
    }

    public String getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(String employeeCount) {
        this.employeeCount = employeeCount;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationIdName() {
        return locationIdName;
    }

    public void setLocationIdName(String locationIdName) {
        this.locationIdName = locationIdName;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDepartmentIdName() {
        return departmentIdName;
    }

    public void setDepartmentIdName(String departmentIdName) {
        this.departmentIdName = departmentIdName;
    }

    public String getDepartmentNameUz() {
        return departmentNameUz;
    }

    public void setDepartmentNameUz(String departmentNameUz) {
        this.departmentNameUz = departmentNameUz;
    }

    public String getDepartmentNameRu() {
        return departmentNameRu;
    }

    public void setDepartmentNameRu(String departmentNameRu) {
        this.departmentNameRu = departmentNameRu;
    }

    public String getDepartmentNameEn() {
        return departmentNameEn;
    }

    public void setDepartmentNameEn(String departmentNameEn) {
        this.departmentNameEn = departmentNameEn;
    }

    public String getDepartmentNameAr() {
        return departmentNameAr;
    }

    public void setDepartmentNameAr(String departmentNameAr) {
        this.departmentNameAr = departmentNameAr;
    }

    public Integer getVacantCount() {
        return vacantCount;
    }

    public void setVacantCount(Integer vacantCount) {
        this.vacantCount = vacantCount;
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getModifiedById() {
        return modifiedById;
    }

    public void setModifiedById(Integer modifiedById) {
        this.modifiedById = modifiedById;
    }

    public String getModifiedByName() {
        return modifiedByName;
    }

    public void setModifiedByName(String modifiedByName) {
        this.modifiedByName = modifiedByName;
    }

    public String getModifiedByIdName() {
        return modifiedByIdName;
    }

    public void setModifiedByIdName(String modifiedByIdName) {
        this.modifiedByIdName = modifiedByIdName;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
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

    public String getTypeNameUz() {
        return typeNameUz;
    }

    public void setTypeNameUz(String typeNameUz) {
        this.typeNameUz = typeNameUz;
    }

    public String getTypeNameRu() {
        return typeNameRu;
    }

    public void setTypeNameRu(String typeNameRu) {
        this.typeNameRu = typeNameRu;
    }

    public String getTypeNameEn() {
        return typeNameEn;
    }

    public void setTypeNameEn(String typeNameEn) {
        this.typeNameEn = typeNameEn;
    }

    public String getTypeNameAr() {
        return typeNameAr;
    }

    public void setTypeNameAr(String typeNameAr) {
        this.typeNameAr = typeNameAr;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getTypeIdName() {
        return typeIdName;
    }

    public void setTypeIdName(String typeIdName) {
        this.typeIdName = typeIdName;
    }

    public String getNameUz() {
        return nameUz;
    }

    public void setNameUz(String nameUz) {
        this.nameUz = nameUz;
    }

    public String getNameRu() {
        return nameRu;
    }

    public void setNameRu(String nameRu) {
        this.nameRu = nameRu;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getStatusUz() {
        return statusUz;
    }

    public void setStatusUz(String statusUz) {
        this.statusUz = statusUz;
    }

    public String getStatusRu() {
        return statusRu;
    }

    public void setStatusRu(String statusRu) {
        this.statusRu = statusRu;
    }

    public String getStatusEn() {
        return statusEn;
    }

    public void setStatusEn(String statusEn) {
        this.statusEn = statusEn;
    }

    public String getStatusAr() {
        return statusAr;
    }

    public void setStatusAr(String statusAr) {
        this.statusAr = statusAr;
    }
}
