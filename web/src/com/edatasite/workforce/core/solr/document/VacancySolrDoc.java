package com.edatasite.workforce.core.solr.document;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.Date;

/**
 * @author: Dilsh0d Tadjiev on 15.08.2020 23:33.
 */
@SolrDocument(collection = "vacancyCore")
public class VacancySolrDoc extends BaseSolrDoc {

    @Id
    @Indexed(name = "oid", type = "string", required = true)
    private String oid;

    @Field("companyId")
    @Indexed(name = "companyId", type = "pint", required = true)
    private Integer companyId;

    @Field("vacancyId")
    @Indexed(name = "vacancyId", type = "pint", required = true)
    private Integer vacancyId;

    @Field("vacancyNumber")
    private String vacancyNumber;

    @Field("jobFamilyId")
    private Integer jobFamilyId;

    @Field("jobFamilyName")
    private String jobFamilyName;

    @Field("jobFamilyNameId")
    private String jobFamilyNameId;

    @Field("approverId")
    private Integer approverId;

    @Field("approverName")
    private String approverName;

    @Field("approverIdName")
    private String approverIdName;

    @Field("jobTypeId")
    @Indexed(name = "jobTypeId", type = "pint", stored = false)
    private Integer jobTypeId;

    @Field("jobTypeName")
    private String jobTypeName;

    @Field("jobTypeNameId")
    @Indexed(name = "jobTypeNameId", type = "string", stored = false)
    private String jobTypeNameId;

    @Field("jobTitle")
    private String jobTitle;

    @Field("nameEn")
    private String nameEn;

    @Field("nameRu")
    private String nameRu;

    @Field("nameAr")
    private String nameAr;

    @Field("nameUz")
    private String nameUz;

    @Field("vacancyStatusId")
    private Integer vacancyStatusId;

    @Field("vacancyStatus")
    private String vacancyStatus;

    @Field("vacancyStatusIdCode")
    @Indexed(name = "vacancyStatusIdCode", type = "string", stored = false)
    private String vacancyStatusIdCode;

    @Field("vacancyStatusIdCodeName")
    @Indexed(name = "vacancyStatusIdCodeName", type = "string", stored = false)
    private String vacancyStatusIdCodeName;

    @Field("vacancyStatusCode")
    private String vacancyStatusCode;

    @Field("vacancyStatusSorder")
    @Indexed(name = "vacancyStatusSorder", type = "pint", stored = false)
    private Integer vacancyStatusSorder;

    @Field("rdegreeStatus")
    private String rdegreeStatus;

    @Field("rdegreeStatusId")
    @Indexed(name = "rdegreeStatusId", type = "pint", stored = false)
    private Integer rdegreeStatusId;

    @Field("rdegreeStatusIdCode")
    @Indexed(name = "rdegreeStatusIdCode", type = "string", stored = false)
    private String rdegreeStatusIdCode;

    @Field("rdegreeStatusIdCodeName")
    @Indexed(name = "rdegreeStatusIdCodeName", type = "string", stored = false)
    private String rdegreeStatusIdCodeName;

    @Field("rdegreeStatusCode")
    private String rdegreeStatusCode;

    @Field("rdegreeStatusSorder")
    @Indexed(name = "rdegreeStatusSorder", type = "pint", stored = false)
    private Integer rdegreeStatusSorder;

    @Field("projectId")
    @Indexed(name = "projectId", type = "pint", stored = false)
    private Integer projectId;

    @Field("projectName")
    private String projectName;

    @Field("projectIdName")
    @Indexed(name = "projectIdName", type = "string", stored = false)
    private String projectIdName;

    @Field("countryId")
    @Indexed(name = "countryId", type = "pint", stored = false)
    private Integer countryId;

    @Field("countryName")
    private String countryName;

    @Field("countryIdName")
    private String countryIdName;

    @Field("embassyId")
    @Indexed(name = "embassyId", type = "pint", stored = false)
    private Integer embassyId;

    @Field("embassyName")
    private String embassyName;

    @Field("embassyIdName")
    private String embassyIdName;

    @Field("gender")
    private String gender;

    @Field("proposedSalary")
    private String proposedSalary;

    @Field("jobRequirements")
    private String jobRequirements;

    @Field("contractFrom")
    private Date contractFrom;

    @Field("contractTo")
    private Date contractTo;

    @Field("vacancyType")
    private Integer vacancyType;

    @Field("vacancyTypeName")
    private String vacancyTypeName;

    @Field("vacancyReligion")
    private Integer vacancyReligion;

    @Field("managerId")
    @Indexed(name = "managerId", type = "pint", stored = false)
    private Integer managerId;

    @Field("managerName")
    private String managerName;

    @Field("managerIdName")
    @Indexed(name = "managerIdName", type = "string", stored = false)
    private String managerIdName;

    @Field("backupManagerId")
    @Indexed(name = "backupManagerId", type = "pint", stored = false)
    private Integer backupManagerId;

    @Field("backupManagerName")
    private String backupManagerName;

    @Field("backupManagerIdName")
    @Indexed(name = "backupManagerIdName", type = "string", stored = false)
    private String backupManagerIdName;

    @Field("positionId")
    @Indexed(name = "positionId", type = "pint", stored = false)
    private Integer positionId;

    @Field("positionName")
    private String positionName;

    @Field("positionIdName")
    @Indexed(name = "positionIdName", type = "string", stored = false)
    private String positionIdName;

    @Field("locationId")
    @Indexed(name = "locationId", type = "pint", stored = false)
    private Integer locationId;

    @Field("locationName")
    private String locationName;

    @Field("locationIdName")
    @Indexed(name = "locationIdName", type = "string", stored = false)
    private String locationIdName;

    @Field("startDate")
    private Date startDate;

    @Field("endDate")
    private Date endDate;

    @Field("createdDate")
    private Date createdDate;

    @Field("createdBy")
    private String createdBy;

    @Field("lastUpdateDate")
    private Date lastUpdateDate;

    @Field("modifiedBy")
    private String modifiedBy;

    @Field("departmentId")
    @Indexed(name = "departmentId", type = "integer", stored = false)
    private Integer departmentId;

    @Field("departmentName")
    private String departmentName;

    @Field("departmentIdName")
    @Indexed(name = "departmentIdName", type = "string", stored = false)
    private String departmentIdName;

    @Field("currencyId")
    @Indexed(name = "currencyId", type = "pint", stored = false)
    private Integer currencyId;

    @Field("currencyName")
    private String currencyName;

    @Field("currencyIdName")
    private String currencyIdName;

    @Field("approvalStatusId")
    @Indexed(name = "approvalStatusId", type = "integer", stored = false)
    private Integer approvalStatusId;

    @Field("approvalStatusName")
    private String approvalStatusName;

    @Field("approvalStatusIdName")
    @Indexed(name = "approvalStatusIdName", type = "string", stored = false)
    private String approvalStatusIdName;

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

    public Integer getVacancyId() {
        return vacancyId;
    }

    public void setVacancyId(Integer vacancyId) {
        this.vacancyId = vacancyId;
    }

    public String getVacancyNumber() {
        return vacancyNumber;
    }

    public void setVacancyNumber(String vacancyNumber) {
        this.vacancyNumber = vacancyNumber;
    }

    public Integer getJobFamilyId() {
        return jobFamilyId;
    }

    public void setJobFamilyId(Integer jobFamilyId) {
        this.jobFamilyId = jobFamilyId;
    }

    public String getJobFamilyName() {
        return jobFamilyName;
    }

    public void setJobFamilyName(String jobFamilyName) {
        this.jobFamilyName = jobFamilyName;
    }

    public String getJobFamilyNameId() {
        return jobFamilyNameId;
    }

    public void setJobFamilyNameId(String jobFamilyNameId) {
        this.jobFamilyNameId = jobFamilyNameId;
    }

    public Integer getJobTypeId() {
        return jobTypeId;
    }

    public void setJobTypeId(Integer jobTypeId) {
        this.jobTypeId = jobTypeId;
    }

    public String getJobTypeName() {
        return jobTypeName;
    }

    public void setJobTypeName(String jobTypeName) {
        this.jobTypeName = jobTypeName;
    }

    public String getJobTypeNameId() {
        return jobTypeNameId;
    }

    public void setJobTypeNameId(String jobTypeNameId) {
        this.jobTypeNameId = jobTypeNameId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Integer getVacancyStatusId() {
        return vacancyStatusId;
    }

    public void setVacancyStatusId(Integer vacancyStatusId) {
        this.vacancyStatusId = vacancyStatusId;
    }

    public String getVacancyStatus() {
        return vacancyStatus;
    }

    public void setVacancyStatus(String vacancyStatus) {
        this.vacancyStatus = vacancyStatus;
    }

    public String getVacancyStatusIdCode() {
        return vacancyStatusIdCode;
    }

    public void setVacancyStatusIdCode(String vacancyStatusIdCode) {
        this.vacancyStatusIdCode = vacancyStatusIdCode;
    }

    public String getVacancyStatusIdCodeName() {
        return vacancyStatusIdCodeName;
    }

    public void setVacancyStatusIdCodeName(String vacancyStatusIdCodeName) {
        this.vacancyStatusIdCodeName = vacancyStatusIdCodeName;
    }

    public String getVacancyStatusCode() {
        return vacancyStatusCode;
    }

    public void setVacancyStatusCode(String vacancyStatusCode) {
        this.vacancyStatusCode = vacancyStatusCode;
    }

    public Integer getVacancyStatusSorder() {
        return vacancyStatusSorder;
    }

    public void setVacancyStatusSorder(Integer vacancyStatusSorder) {
        this.vacancyStatusSorder = vacancyStatusSorder;
    }

    public String getRdegreeStatus() {
        return rdegreeStatus;
    }

    public void setRdegreeStatus(String rdegreeStatus) {
        this.rdegreeStatus = rdegreeStatus;
    }

    public Integer getRdegreeStatusId() {
        return rdegreeStatusId;
    }

    public void setRdegreeStatusId(Integer rdegreeStatusId) {
        this.rdegreeStatusId = rdegreeStatusId;
    }

    public String getRdegreeStatusIdCode() {
        return rdegreeStatusIdCode;
    }

    public void setRdegreeStatusIdCode(String rdegreeStatusIdCode) {
        this.rdegreeStatusIdCode = rdegreeStatusIdCode;
    }

    public String getRdegreeStatusIdCodeName() {
        return rdegreeStatusIdCodeName;
    }

    public void setRdegreeStatusIdCodeName(String rdegreeStatusIdCodeName) {
        this.rdegreeStatusIdCodeName = rdegreeStatusIdCodeName;
    }

    public String getRdegreeStatusCode() {
        return rdegreeStatusCode;
    }

    public void setRdegreeStatusCode(String rdegreeStatusCode) {
        this.rdegreeStatusCode = rdegreeStatusCode;
    }

    public Integer getRdegreeStatusSorder() {
        return rdegreeStatusSorder;
    }

    public void setRdegreeStatusSorder(Integer rdegreeStatusSorder) {
        this.rdegreeStatusSorder = rdegreeStatusSorder;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectIdName() {
        return projectIdName;
    }

    public void setProjectIdName(String projectIdName) {
        this.projectIdName = projectIdName;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryIdName() {
        return countryIdName;
    }

    public void setCountryIdName(String countryIdName) {
        this.countryIdName = countryIdName;
    }

    public Integer getEmbassyId() {
        return embassyId;
    }

    public void setEmbassyId(Integer embassyId) {
        this.embassyId = embassyId;
    }

    public String getEmbassyName() {
        return embassyName;
    }

    public void setEmbassyName(String embassyName) {
        this.embassyName = embassyName;
    }

    public String getEmbassyIdName() {
        return embassyIdName;
    }

    public void setEmbassyIdName(String embassyIdName) {
        this.embassyIdName = embassyIdName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProposedSalary() {
        return proposedSalary;
    }

    public void setProposedSalary(String proposedSalary) {
        this.proposedSalary = proposedSalary;
    }

    public String getJobRequirements() {
        return jobRequirements;
    }

    public void setJobRequirements(String jobRequirements) {
        this.jobRequirements = jobRequirements;
    }

    public Date getContractFrom() {
        return contractFrom;
    }

    public void setContractFrom(Date contractFrom) {
        this.contractFrom = contractFrom;
    }

    public Date getContractTo() {
        return contractTo;
    }

    public void setContractTo(Date contractTo) {
        this.contractTo = contractTo;
    }

    public Integer getVacancyType() {
        return vacancyType;
    }

    public void setVacancyType(Integer vacancyType) {
        this.vacancyType = vacancyType;
    }

    public String getVacancyTypeName() {
        return vacancyTypeName;
    }

    public void setVacancyTypeName(String vacancyTypeName) {
        this.vacancyTypeName = vacancyTypeName;
    }

    public Integer getVacancyReligion() {
        return vacancyReligion;
    }

    public void setVacancyReligion(Integer vacancyReligion) {
        this.vacancyReligion = vacancyReligion;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerIdName() {
        return managerIdName;
    }

    public void setManagerIdName(String managerIdName) {
        this.managerIdName = managerIdName;
    }

    public Integer getBackupManagerId() {
        return backupManagerId;
    }

    public void setBackupManagerId(Integer backupManagerId) {
        this.backupManagerId = backupManagerId;
    }

    public String getBackupManagerName() {
        return backupManagerName;
    }

    public void setBackupManagerName(String backupManagerName) {
        this.backupManagerName = backupManagerName;
    }

    public String getBackupManagerIdName() {
        return backupManagerIdName;
    }

    public void setBackupManagerIdName(String backupManagerIdName) {
        this.backupManagerIdName = backupManagerIdName;
    }

    public Integer getPositionId() {
        return positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public String getPositionIdName() {
        return positionIdName;
    }

    public void setPositionIdName(String positionIdName) {
        this.positionIdName = positionIdName;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
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

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencyIdName() {
        return currencyIdName;
    }

    public void setCurrencyIdName(String currencyIdName) {
        this.currencyIdName = currencyIdName;
    }

    public Integer getApprovalStatusId() {
        return approvalStatusId;
    }

    public void setApprovalStatusId(Integer approvalStatusId) {
        this.approvalStatusId = approvalStatusId;
    }

    public String getApprovalStatusName() {
        return approvalStatusName;
    }

    public void setApprovalStatusName(String approvalStatusName) {
        this.approvalStatusName = approvalStatusName;
    }

    public String getApprovalStatusIdName() {
        return approvalStatusIdName;
    }

    public void setApprovalStatusIdName(String approvalStatusIdName) {
        this.approvalStatusIdName = approvalStatusIdName;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameRu() {
        return nameRu;
    }

    public void setNameRu(String nameRu) {
        this.nameRu = nameRu;
    }

    public String getNameAr() {
        return nameAr;
    }

    public void setNameAr(String nameAr) {
        this.nameAr = nameAr;
    }

    public String getNameUz() {
        return nameUz;
    }

    public void setNameUz(String nameUz) {
        this.nameUz = nameUz;
    }

    public Integer getApproverId() {
        return approverId;
    }

    public void setApproverId(Integer approverId) {
        this.approverId = approverId;
    }

    public String getApproverName() {
        return approverName;
    }

    public void setApproverName(String approverName) {
        this.approverName = approverName;
    }

    public String getApproverIdName() {
        return approverIdName;
    }

    public void setApproverIdName(String approverIdName) {
        this.approverIdName = approverIdName;
    }
}
