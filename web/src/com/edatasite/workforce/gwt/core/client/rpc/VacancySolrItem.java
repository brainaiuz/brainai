package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

public class VacancySolrItem implements IsSerializable {

    private Integer objectID;
    private String vacancyNumber;
    private SelectItem jobFamily;
    private SelectItem approver;
    private SelectItem jobType;
    private String jobTitle;
    private Integer jobFamilyId;
    private Integer fullPartTime;
    private String nameEn;
    private String nameRu;
    private String nameAr;
    private String nameUz;
    private ReferenceItem vacancyStatus;
    private ReferenceItem rdegreeStatus;
    private SelectItem project;
    private SelectItem country;
    private SelectItem embassy;
    private String gender;
    private String proposedSalary;
    private String jobRequirements;
    private Date contractFrom;
    private Date contractTo;
    private SelectItem vacancyType;
    private Integer vacancyReligion;
    private SelectItem manager;
    private SelectItem backupManager;
    private SelectItem position;
    private SelectItem location;
    private Date startDate;
    private Date endDate;
    private Date createdDate;
    private String createdBy;
    private Date lastUpdateDate;
    private String modifiedBy;
    private SelectItem department;
    private SelectItem currency;
    private SelectItem approvalStatus;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getVacancyNumber() {
        return vacancyNumber;
    }

    public void setVacancyNumber(String vacancyNumber) {
        this.vacancyNumber = vacancyNumber;
    }

    public SelectItem getJobFamily() {
        return jobFamily;
    }

    public void setJobFamily(SelectItem jobFamily) {
        this.jobFamily = jobFamily;
    }

    public SelectItem getApprover() {
        return approver;
    }

    public void setApprover(SelectItem approver) {
        this.approver = approver;
    }

    public SelectItem getJobType() {
        return jobType;
    }

    public void setJobType(SelectItem jobType) {
        this.jobType = jobType;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Integer getJobFamilyId() {
        return jobFamilyId;
    }

    public void setJobFamilyId(Integer jobFamilyId) {
        this.jobFamilyId = jobFamilyId;
    }

    public Integer getFullPartTime() {
        return fullPartTime;
    }

    public void setFullPartTime(Integer fullPartTime) {
        this.fullPartTime = fullPartTime;
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

    public ReferenceItem getVacancyStatus() {
        return vacancyStatus;
    }

    public void setVacancyStatus(ReferenceItem vacancyStatus) {
        this.vacancyStatus = vacancyStatus;
    }

    public ReferenceItem getRdegreeStatus() {
        return rdegreeStatus;
    }

    public void setRdegreeStatus(ReferenceItem rdegreeStatus) {
        this.rdegreeStatus = rdegreeStatus;
    }

    public SelectItem getProject() {
        return project;
    }

    public void setProject(SelectItem project) {
        this.project = project;
    }

    public SelectItem getCountry() {
        return country;
    }

    public void setCountry(SelectItem country) {
        this.country = country;
    }

    public SelectItem getEmbassy() {
        return embassy;
    }

    public void setEmbassy(SelectItem embassy) {
        this.embassy = embassy;
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

    public SelectItem getVacancyType() {
        return vacancyType;
    }

    public void setVacancyType(SelectItem vacancyType) {
        this.vacancyType = vacancyType;
    }

    public Integer getVacancyReligion() {
        return vacancyReligion;
    }

    public void setVacancyReligion(Integer vacancyReligion) {
        this.vacancyReligion = vacancyReligion;
    }

    public SelectItem getManager() {
        return manager;
    }

    public void setManager(SelectItem manager) {
        this.manager = manager;
    }

    public SelectItem getBackupManager() {
        return backupManager;
    }

    public void setBackupManager(SelectItem backupManager) {
        this.backupManager = backupManager;
    }

    public SelectItem getPosition() {
        return position;
    }

    public void setPosition(SelectItem position) {
        this.position = position;
    }

    public SelectItem getLocation() {
        return location;
    }

    public void setLocation(SelectItem location) {
        this.location = location;
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

    public SelectItem getDepartment() {
        return department;
    }

    public void setDepartment(SelectItem department) {
        this.department = department;
    }

    public SelectItem getCurrency() {
        return currency;
    }

    public void setCurrency(SelectItem currency) {
        this.currency = currency;
    }

    public SelectItem getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(SelectItem approvalStatus) {
        this.approvalStatus = approvalStatus;
    }
}
