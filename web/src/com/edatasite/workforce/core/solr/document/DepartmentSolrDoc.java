package com.edatasite.workforce.core.solr.document;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.Date;

@SolrDocument(collection = "departmentCore")
public class DepartmentSolrDoc extends BaseSolrDoc{
    @Id
    @Indexed(name = "oid", type = "string", required = true)
    private String oid;

    @Field("companyId")
    @Indexed(name = "companyId", type = "pint", required = true)
    private Integer companyId;

    @Field("departmentId")
    @Indexed(name = "departmentId", type = "pint", required = true)
    private Integer departmentId;

    @Field("number")
    private String number;

    @Field("name")
    private String name;

    @Field("nameUz")
    private String nameUz;
    @Field("nameRu")
    private String nameRu;
    @Field("nameEn")
    private String nameEn;
    @Field("nameAr")
    private String nameAr;

    @Field("startDate")
    private Date startDate;

    @Field("locationId")
    private Integer locationId;

    @Field("locationName")
    private String locationName;

    @Field("locationIdName")
    @Indexed(name = "locationIdName", type = "string", stored = false)
    private String locationIdName;

    @Field("parentDepartmentId")
    private Integer parentDepartmentId;

    @Field("parentDepartmentName")
    private String parentDepartmentName;

    @Field("parentDepartmentIdName")
    @Indexed(name = "parentDepartmentIdName", type = "string", stored = false)
    private String parentDepartmentIdName;

    @Field("parentDepartmentNameEn")
    private String parentDepartmentNameEn;

    @Field("parentDepartmentNameRu")
    private String parentDepartmentNameRu;

    @Field("parentDepartmentNameUz")
    private String parentDepartmentNameUz;

    @Field("parentDepartmentNameAr")
    private String parentDepartmentNameAr;

    @Field("leaderId")
    private Integer leaderId;

    @Field("leaderName")
    private String leaderName;

    @Field("leaderIdName")
    @Indexed(name = "leaderIdName", type = "string", stored = false)
    private String leaderIdName;

    @Field("leaderIsVacant")
    private Boolean leaderIsVacant;

    @Field("headCount")
    private String headCount;

    @Field("encryptedId")
    private String encryptedId;

    @Field("statusName")
    private Boolean statusName;

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


    public DepartmentSolrDoc() {
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

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
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

    public Integer getParentDepartmentId() {
        return parentDepartmentId;
    }

    public void setParentDepartmentId(Integer parentDepartmentId) {
        this.parentDepartmentId = parentDepartmentId;
    }

    public String getParentDepartmentName() {
        return parentDepartmentName;
    }

    public void setParentDepartmentName(String parentDepartmentName) {
        this.parentDepartmentName = parentDepartmentName;
    }

    public String getParentDepartmentIdName() {
        return parentDepartmentIdName;
    }

    public void setParentDepartmentIdName(String parentDepartmentIdName) {
        this.parentDepartmentIdName = parentDepartmentIdName;
    }

    public String getParentDepartmentNameEn() {
        return parentDepartmentNameEn;
    }

    public void setParentDepartmentNameEn(String parentDepartmentNameEn) {
        this.parentDepartmentNameEn = parentDepartmentNameEn;
    }

    public String getParentDepartmentNameRu() {
        return parentDepartmentNameRu;
    }

    public void setParentDepartmentNameRu(String parentDepartmentNameRu) {
        this.parentDepartmentNameRu = parentDepartmentNameRu;
    }

    public String getParentDepartmentNameUz() {
        return parentDepartmentNameUz;
    }

    public void setParentDepartmentNameUz(String parentDepartmentNameUz) {
        this.parentDepartmentNameUz = parentDepartmentNameUz;
    }

    public String getParentDepartmentNameAr() {
        return parentDepartmentNameAr;
    }

    public void setParentDepartmentNameAr(String parentDepartmentNameAr) {
        this.parentDepartmentNameAr = parentDepartmentNameAr;
    }

    public Integer getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(Integer leaderId) {
        this.leaderId = leaderId;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getLeaderIdName() {
        return leaderIdName;
    }

    public void setLeaderIdName(String leaderIdName) {
        this.leaderIdName = leaderIdName;
    }

    public Boolean getLeaderIsVacant() {
        return leaderIsVacant != null && leaderIsVacant;
    }

    public void setLeaderIsVacant(Boolean leaderIsVacant) {
        this.leaderIsVacant = leaderIsVacant;
    }

    public String getHeadCount() {
        return headCount;
    }

    public void setHeadCount(String headCount) {
        this.headCount = headCount;
    }

    public String getEncryptedId() {
        return encryptedId;
    }

    public void setEncryptedId(String encryptedId) {
        this.encryptedId = encryptedId;
    }

    public Boolean getStatusName() {
        return statusName != null && statusName;
    }

    public void setStatusName(Boolean statusName) {
        this.statusName = statusName;
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
}
