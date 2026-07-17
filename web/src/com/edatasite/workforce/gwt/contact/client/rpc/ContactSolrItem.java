package com.edatasite.workforce.gwt.contact.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContactSolrItem implements IsSerializable {

    private Integer objectId;
    private String contactName;
    private SelectItem country;
    private SelectItem state;
    private String street;
    private String street2;
    private String city;
    private String postCode;
    private Double longitude;
    private Double latitude;
    private List<Integer> accountOwnerId = new ArrayList<>();
    private SelectItem account;
    private List<String> accountType = new ArrayList<>();
    private SelectItem accountIndustry;
    private Integer clientContactId;
    private Boolean accessEnabled;
    private SelectItem owner;
    private Integer contactType;
    private String firstName;
    private String middleName;
    private String lastName;
    private String refIndNumber;
    private String title;
    private String primaryEmail;
    private String primaryPhone;
    private String workPhone;
    private String extension;
    private String fax;
    private String mobile;
    private Date updateDate;
    private List<SelectItem> category = new ArrayList<>();
    private String categoryNameSort;
    private Long leadKanbanOrder;
    private List<SelectItem> mailList = new ArrayList<>();
    private SelectItem campaign;
    private String department;
    private Date dateOfBirth;
    private String reportsTo;
    private Integer reportsToId;
    private Boolean emailAllowed;
    private Boolean isPrimaryContact;
    private String googleId;
    private Boolean favourite;
    private SelectItem assignee;
    private SelectItem backupAssignee;
    private SelectItem rating;
    private ReferenceItem status;
    private String leadSourceOther;
    private SelectItem leadSource;
    private Date creationDate;
    private String jobTitle;
    private String website;
    private List<SelectItem> vacancy = new ArrayList<>();
    private SelectItem preferredLocation;
    private SelectItem candidateDepartment;
    private SelectItem candidatePosition;
    private String number;
    private Integer workExperience;
    private Integer workExperienceMonthYear;
    private String currentEmployer;
    private Double expectedSalary;
    private Boolean isShortList;
    private String candidateSkills;
    private SelectItem candidateProject;
    private SelectItem creator;
    private SelectItem candidateStatus;
    private SelectItem candidateCreatedBy;
    private SelectItem updater;
    private boolean candidate;
    private boolean lead;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public SelectItem getCountry() {
        return country;
    }

    public void setCountry(SelectItem country) {
        this.country = country;
    }

    public SelectItem getState() {
        return state;
    }

    public void setState(SelectItem state) {
        this.state = state;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public List<Integer> getAccountOwnerId() {
        return accountOwnerId;
    }

    public void setAccountOwnerId(List<Integer> accountOwnerId) {
        this.accountOwnerId = accountOwnerId;
    }

    public SelectItem getAccount() {
        return account;
    }

    public void setAccount(SelectItem account) {
        this.account = account;
    }

    public List<String> getAccountType() {
        return accountType;
    }

    public void setAccountType(List<String> accountType) {
        this.accountType = accountType;
    }

    public SelectItem getAccountIndustry() {
        return accountIndustry;
    }

    public void setAccountIndustry(SelectItem accountIndustry) {
        this.accountIndustry = accountIndustry;
    }

    public Integer getClientContactId() {
        return clientContactId;
    }

    public void setClientContactId(Integer clientContactId) {
        this.clientContactId = clientContactId;
    }

    public Boolean getAccessEnabled() {
        return accessEnabled;
    }

    public void setAccessEnabled(Boolean accessEnabled) {
        this.accessEnabled = accessEnabled;
    }

    public SelectItem getOwner() {
        return owner;
    }

    public void setOwner(SelectItem owner) {
        this.owner = owner;
    }

    public Integer getContactType() {
        return contactType;
    }

    public void setContactType(Integer contactType) {
        this.contactType = contactType;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRefIndNumber() {
        return refIndNumber;
    }

    public void setRefIndNumber(String refIndNumber) {
        this.refIndNumber = refIndNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrimaryEmail() {
        return primaryEmail;
    }

    public void setPrimaryEmail(String primaryEmail) {
        this.primaryEmail = primaryEmail;
    }

    public String getPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public List<SelectItem> getCategory() {
        return category;
    }

    public void setCategory(List<SelectItem> category) {
        this.category = category;
    }

    public String getCategoryNameSort() {
        return categoryNameSort;
    }

    public void setCategoryNameSort(String categoryNameSort) {
        this.categoryNameSort = categoryNameSort;
    }

    public Long getLeadKanbanOrder() {
        return leadKanbanOrder;
    }

    public void setLeadKanbanOrder(Long leadKanbanOrder) {
        this.leadKanbanOrder = leadKanbanOrder;
    }

    public List<SelectItem> getMailList() {
        return mailList;
    }

    public void setMailList(List<SelectItem> mailList) {
        this.mailList = mailList;
    }

    public SelectItem getCampaign() {
        return campaign;
    }

    public void setCampaign(SelectItem campaign) {
        this.campaign = campaign;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getReportsTo() {
        return reportsTo;
    }

    public void setReportsTo(String reportsTo) {
        this.reportsTo = reportsTo;
    }

    public Integer getReportsToId() {
        return reportsToId;
    }

    public void setReportsToId(Integer reportsToId) {
        this.reportsToId = reportsToId;
    }

    public Boolean getEmailAllowed() {
        return emailAllowed;
    }

    public void setEmailAllowed(Boolean emailAllowed) {
        this.emailAllowed = emailAllowed;
    }

    public Boolean getPrimaryContact() {
        return isPrimaryContact;
    }

    public void setPrimaryContact(Boolean primaryContact) {
        isPrimaryContact = primaryContact;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public SelectItem getAssignee() {
        return assignee;
    }

    public void setAssignee(SelectItem assignee) {
        this.assignee = assignee;
    }

    public SelectItem getBackupAssignee() {
        return backupAssignee;
    }

    public void setBackupAssignee(SelectItem backupAssignee) {
        this.backupAssignee = backupAssignee;
    }

    public SelectItem getRating() {
        return rating;
    }

    public void setRating(SelectItem rating) {
        this.rating = rating;
    }

    public ReferenceItem getStatus() {
        return status;
    }

    public void setStatus(ReferenceItem status) {
        this.status = status;
    }

    public String getLeadSourceOther() {
        return leadSourceOther;
    }

    public void setLeadSourceOther(String leadSourceOther) {
        this.leadSourceOther = leadSourceOther;
    }

    public SelectItem getLeadSource() {
        return leadSource;
    }

    public void setLeadSource(SelectItem leadSource) {
        this.leadSource = leadSource;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public List<SelectItem> getVacancy() {
        return vacancy;
    }

    public void setVacancy(List<SelectItem> vacancy) {
        this.vacancy = vacancy;
    }

    public SelectItem getPreferredLocation() {
        return preferredLocation;
    }

    public void setPreferredLocation(SelectItem preferredLocation) {
        this.preferredLocation = preferredLocation;
    }

    public SelectItem getCandidateDepartment() {
        return candidateDepartment;
    }

    public void setCandidateDepartment(SelectItem candidateDepartment) {
        this.candidateDepartment = candidateDepartment;
    }

    public SelectItem getCandidatePosition() {
        return candidatePosition;
    }

    public void setCandidatePosition(SelectItem candidatePosition) {
        this.candidatePosition = candidatePosition;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(Integer workExperience) {
        this.workExperience = workExperience;
    }

    public Integer getWorkExperienceMonthYear() {
        return workExperienceMonthYear;
    }

    public void setWorkExperienceMonthYear(Integer workExperienceMonthYear) {
        this.workExperienceMonthYear = workExperienceMonthYear;
    }

    public String getCurrentEmployer() {
        return currentEmployer;
    }

    public void setCurrentEmployer(String currentEmployer) {
        this.currentEmployer = currentEmployer;
    }

    public Double getExpectedSalary() {
        return expectedSalary;
    }

    public void setExpectedSalary(Double expectedSalary) {
        this.expectedSalary = expectedSalary;
    }

    public Boolean getShortList() {
        return isShortList;
    }

    public void setShortList(Boolean shortList) {
        isShortList = shortList;
    }

    public String getCandidateSkills() {
        return candidateSkills;
    }

    public void setCandidateSkills(String candidateSkills) {
        this.candidateSkills = candidateSkills;
    }

    public SelectItem getCandidateProject() {
        return candidateProject;
    }

    public void setCandidateProject(SelectItem candidateProject) {
        this.candidateProject = candidateProject;
    }

    public SelectItem getCreator() {
        return creator;
    }

    public void setCreator(SelectItem creator) {
        this.creator = creator;
    }

    public SelectItem getCandidateStatus() {
        return candidateStatus;
    }

    public void setCandidateStatus(SelectItem candidateStatus) {
        this.candidateStatus = candidateStatus;
    }

    public SelectItem getCandidateCreatedBy() {
        return candidateCreatedBy;
    }

    public void setCandidateCreatedBy(SelectItem candidateCreatedBy) {
        this.candidateCreatedBy = candidateCreatedBy;
    }

    public SelectItem getUpdater() {
        return updater;
    }

    public void setUpdater(SelectItem updater) {
        this.updater = updater;
    }

    public Boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(Boolean favourite) {
        this.favourite = favourite;
    }

    public boolean isCandidate() {
        return candidate;
    }

    public void setCandidate(boolean candidate) {
        this.candidate = candidate;
    }

    public boolean isLead() {
        return lead;
    }

    public void setLead(boolean lead) {
        this.lead = lead;
    }
}
