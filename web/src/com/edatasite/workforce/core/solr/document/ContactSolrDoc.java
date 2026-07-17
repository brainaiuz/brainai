package com.edatasite.workforce.core.solr.document;

import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.solr.core.mapping.Indexed;
import org.springframework.data.solr.core.mapping.SolrDocument;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author: Dilsh0d Tadjiev on 06.08.2020 18:21.
 */

@SolrDocument(collection = "contactCore")
public class ContactSolrDoc extends BaseSolrDoc {

    @Id
    @Indexed(name = "oid", type = "string", required = true)
    private String oid;

    @Field("companyId")
    private Integer companyId;

    @Field("contactId")
    private Integer contactId;

    @Field("contactName")
    private String contactName;

    @Field("contactNameComposite")
    private String contactNameComposite;

    @Field("leadNameComposite")
    private String leadNameComposite;

    @Field("countryId")
    private Integer countryId;

    @Field("countryName")
    private String countryName;

    @Field("countryCode")
    private String countryCode;

    @Field("countryIdCode")
    private String countryIdCode;

    @Field("countryIdCodeName")
    private String countryIdCodeName;

    @Field("stateId")
    private Integer stateId;

    @Field("stateName")
    private String stateName;

    @Field("stateIdName")
    private String stateIdName;

    @Field("street")
    private String street;

    @Field("street2")
    private String street2;

    @Field("city")
    private String city;

    @Field("postCode")
    private String postCode;

    @Field("longitude")
    private Double longitude;

    @Field("latitude")
    private Double latitude;

    @Field("accountId")
    private Integer accountId;

    @Field("accountOwnerId")
    @Indexed(name = "accountOwnerId", type = "pints", stored = false)
    private List<Integer> accountOwnerId = new ArrayList<>();

    @Field("accountName")
    private String accountName;

    @Field("accountNumber")
    private String accountNumber;

    @Field("accountIdName")
    private String accountIdName;

    @Field("accountType")
    @Indexed(name = "accountType", type = "string")
    private List<String> accountType = new ArrayList<>();

    @Field("accountIndustry")
    private String accountIndustry;

    @Field("accountIndustryId")
    private Integer accountIndustryId;

    @Field("clientContactId")
    private Integer clientContactId;

    @Field("accessEnabled")
    private Boolean accessEnabled;

    @Field("ownerId")
    private Integer ownerId;

    @Field("ownerName")
    private String ownerName;

    @Field("ownerIdName")
    @Indexed(name = "ownerIdName", type = "string", stored = false)
    private String ownerIdName;

    @Field("contactType")
    private Integer contactType;

    @Field("firstName")
    private String firstName;

    @Field("middleName")
    private String middleName;

    @Field("lastName")
    private String lastName;

    @Field("refIndNumber")
    private String refIndNumber;

    @Field("title")
    private String title;

    @Field("primaryEmail")
    private String primaryEmail;

    @Field("primaryPhone")
    private String primaryPhone;

    @Field("workPhone")
    private String workPhone;

    @Field("extension")
    private String extension;

    @Field("fax")
    private String fax;

    @Field("mobile")
    private String mobile;

    @Field("updateDate")
    private Date updateDate;

    @Field("categoryId")
    @Indexed(name = "categoryId", type = "pints")
    private List<Integer> categoryId = new ArrayList<>();

    @Field("categoryName")
    @Indexed(name = "categoryName", type = "strings")
    private List<String> categoryName = new ArrayList<>();

    @Field("categoryIdName")
    @Indexed(name = "categoryIdName", type = "strings", stored = false)
    private List<String> categoryIdName = new ArrayList<>();

    @Field("categoryNameSort")
    private String categoryNameSort;

    @Field("leadKanbanOrder")
    private Long leadKanbanOrder;

    @Field("mailListId")
    @Indexed(name = "mailListId", type = "pints")
    private List<Integer> mailListId = new ArrayList<>();

    @Field("mailListName")
    @Indexed(name = "mailListName", type = "strings")
    private List<String> mailListName = new ArrayList<>();

    @Field("mailListIdName")
    @Indexed(name = "mailListIdName", type = "strings", stored = false)
    private List<String> mailListIdName = new ArrayList<>();

    @Field("campaignId")
    private Integer campaignId;

    @Field("campaignName")
    private String campaignName;

    @Field("campaignIdName")
    @Indexed(name = "campaignIdName", type = "string", stored = false)
    private String campaignIdName;

    @Field("department")
    private String department;

    @Field("dateOfBirth")
    private Date dateOfBirth;

    @Field("reportsTo")
    private String reportsTo;

    @Field("reportsToId")
    private Integer reportsToId;

    @Field("emailAllowed")
    private Boolean emailAllowed;

    @Field("isPrimaryContact")
    private Boolean isPrimaryContact;

    @Field("googleId")
    private String googleId;

    @Field("isFavourited")
    private Boolean isFavourited;

    @Field("assignee")
    private String assignee;

    @Field("assigneeId")
    private Integer assigneeId;

    @Field("assigneeIdName")
    @Indexed(name = "assigneeIdName", type = "string", stored = false)
    private String assigneeIdName;

    @Field("backupAssignee")
    private String backupAssignee;

    @Field("backupAssigneeId")
    private Integer backupAssigneeId;

    @Field("backupAssigneeIdName")
    @Indexed(name = "backupAssigneeIdName", type = "string", stored = false)
    private String backupAssigneeIdName;

    @Field("rating")
    private String rating;

    @Field("ratingId")
    private Integer ratingId;

    @Field("ratingCode")
    private String ratingCode;

    @Field("ratingIdCode")
    @Indexed(name = "ratingIdCode", type = "string", stored = false)
    private String ratingIdCode;

    @Field("status")
    private String status;

    @Field("statusId")
    private Integer statusId;

    @Field("statusCode")
    private String statusCode;

    @Field("statusIdCode")
    @Indexed(name = "statusIdCode", type = "string", stored = false)
    private String statusIdCode;

    @Field("statusIdCodeName")
    @Indexed(name = "statusIdCodeName", type = "string", stored = false)
    private String statusIdCodeName;

    @Field("statusSorder")
    private Integer statusSorder;

    @Field("leadSource")
    private String leadSource;

    @Field("leadSourceOther")
    private String leadSourceOther;

    @Field("leadSourceId")
    private Integer leadSourceId;

    @Field("leadSourceCode")
    private String leadSourceCode;

    @Field("leadSourceIdCode")
    @Indexed(name = "leadSourceIdCode", type = "string", stored = false)
    private String leadSourceIdCode;

    @Field("leadSourceIdCodeName")
    @Indexed(name = "leadSourceIdCodeName", type = "string", stored = false)
    private String leadSourceIdCodeName;

    @Field("creationDate")
    private Date creationDate;

    @Field("jobTitle")
    private String jobTitle;

    @Field("website")
    private String website;

    @Field("vacancyId")
    @Indexed(name = "vacancyId", type = "pints")
    private List<Integer> vacancyId = new ArrayList<>();

    @Field("vacancyName")
    @Indexed(name = "vacancyName", type = "strings")
    private List<String> vacancyName = new ArrayList<>();

    @Field("vacancyIdName")
    @Indexed(name = "vacancyIdName", type = "strings", stored = false)
    private List<String> vacancyIdName = new ArrayList<>();

    @Field("preferredLocation")
    private String preferredLocation;

    @Field("preferredLocationId")
    private Integer preferredLocationId;

    @Field("preferredLocationIdName")
    private String preferredLocationIdName;

    @Field("candidateDepartment")
    private String candidateDepartment;

    @Field("candidateDepartmentId")
    private Integer candidateDepartmentId;

    @Field("candidateDepartmentIdName")
    private String candidateDepartmentIdName;

    @Field("candidatePosition")
    private String candidatePosition;

    @Field("candidatePositionId")
    private Integer candidatePositionId;

    @Field("candidatePositionIdName")
    private String candidatePositionIdName;


    @Field("number")
    private String number;

    @Field("workExperience")
    private Integer workExperience;

    @Field("workExperienceMonthYear")
    private Integer workExperienceMonthYear;

    @Field("currentEmployer")
    private String currentEmployer;

    @Field("expectedSalary")
    private Double expectedSalary;

    @Field("isShortList")
    private Boolean isShortList;

    @Field("candidateSkills")
    private String candidateSkills;

    @Field("candidateProjectId")
    @Indexed(name = "candidateProjectId", type = "pint")
    private Integer candidateProjectId;

    @Field("candidateProject")
    @Indexed(name = "candidateProject", type = "string")
    private String candidateProject;

    @Field("candidateProjectIdName")
    @Indexed(name = "candidateProjectIdName", type = "string", stored = false)
    private String candidateProjectIdName;

    @Field("creatorId")
    @Indexed(name = "creatorId", type = "pint")
    private Integer creatorId;

    @Field("creatorName")
    @Indexed(name = "creatorName", type = "string")
    private String creatorName;

    @Field("creatorIdName")
    @Indexed(name = "creatorIdName", type = "string", stored = false)
    private String creatorIdName;

    @Field("candidateStatusId")
    @Indexed(name = "candidateStatusId", type = "pint")
    private Integer candidateStatusId;

    @Field("candidateStatus")
    @Indexed(name = "candidateStatus", type = "string")
    private String candidateStatus;

    @Field("candidateStatusIdName")
    @Indexed(name = "candidateStatusIdName", type = "string", stored = false)
    private String candidateStatusIdName;

    @Field("candidateCreatedById")
    @Indexed(name = "candidateCreatedById", type = "pint")
    private Integer candidateCreatedById;

    @Field("candidateCreatedBy")
    @Indexed(name = "candidateCreatedBy", type = "string")
    private String candidateCreatedBy;

    @Field("candidateCreatedByIdName")
    @Indexed(name = "candidateCreatedByIdName", type = "string", stored = false)
    private String candidateCreatedByIdName;

    @Field("updaterId")
    @Indexed(name = "updaterId", type = "pint")
    private Integer updaterId;

    @Field("updaterName")
    @Indexed(name = "updaterName", type = "string")
    private String updaterName;

    @Field("updaterIdName")
    @Indexed(name = "updaterIdName", type = "string", stored = false)
    private String updaterIdName;

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

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNameComposite() {
        return contactNameComposite;
    }

    public void setContactNameComposite(String contactNameComposite) {
        this.contactNameComposite = contactNameComposite;
    }

    public String getLeadNameComposite() {
        return leadNameComposite;
    }

    public void setLeadNameComposite(String leadNameComposite) {
        this.leadNameComposite = leadNameComposite;
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

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryIdCode() {
        return countryIdCode;
    }

    public void setCountryIdCode(String countryIdCode) {
        this.countryIdCode = countryIdCode;
    }

    public String getCountryIdCodeName() {
        return countryIdCodeName;
    }

    public void setCountryIdCodeName(String countryIdCodeName) {
        this.countryIdCodeName = countryIdCodeName;
    }

    public Integer getStateId() {
        return stateId;
    }

    public void setStateId(Integer stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateIdName() {
        return stateIdName;
    }

    public void setStateIdName(String stateIdName) {
        this.stateIdName = stateIdName;
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

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public List<Integer> getAccountOwnerId() {
        return accountOwnerId;
    }

    public void setAccountOwnerId(List<Integer> accountOwnerId) {
        this.accountOwnerId = accountOwnerId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountIdName() {
        return accountIdName;
    }

    public void setAccountIdName(String accountIdName) {
        this.accountIdName = accountIdName;
    }

    public List<String> getAccountType() {
        return accountType;
    }

    public void setAccountType(List<String> accountType) {
        this.accountType = accountType;
    }

    public String getAccountIndustry() {
        return accountIndustry;
    }

    public void setAccountIndustry(String accountIndustry) {
        this.accountIndustry = accountIndustry;
    }

    public Integer getAccountIndustryId() {
        return accountIndustryId;
    }

    public void setAccountIndustryId(Integer accountIndustryId) {
        this.accountIndustryId = accountIndustryId;
    }

    public Integer getClientContactId() {
        return clientContactId;
    }

    public void setClientContactId(Integer clientContactId) {
        this.clientContactId = clientContactId;
    }

    public Boolean getAccessEnabled() {
        return accessEnabled != null ? accessEnabled : false;
    }

    public void setAccessEnabled(Boolean accessEnabled) {
        this.accessEnabled = accessEnabled;
    }

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getOwnerIdName() {
        return ownerIdName;
    }

    public void setOwnerIdName(String ownerIdName) {
        this.ownerIdName = ownerIdName;
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

    public List<Integer> getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(List<Integer> categoryId) {
        this.categoryId = categoryId;
    }

    public List<String> getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(List<String> categoryName) {
        this.categoryName = categoryName;
    }

    public List<String> getCategoryIdName() {
        return categoryIdName;
    }

    public void setCategoryIdName(List<String> categoryIdName) {
        this.categoryIdName = categoryIdName;
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

    public List<Integer> getMailListId() {
        return mailListId;
    }

    public void setMailListId(List<Integer> mailListId) {
        this.mailListId = mailListId;
    }

    public List<String> getMailListName() {
        return mailListName;
    }

    public void setMailListName(List<String> mailListName) {
        this.mailListName = mailListName;
    }

    public List<String> getMailListIdName() {
        return mailListIdName;
    }

    public void setMailListIdName(List<String> mailListIdName) {
        this.mailListIdName = mailListIdName;
    }

    public Integer getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getCampaignIdName() {
        return campaignIdName;
    }

    public void setCampaignIdName(String campaignIdName) {
        this.campaignIdName = campaignIdName;
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
        return emailAllowed != null ? emailAllowed : false;
    }

    public void setEmailAllowed(Boolean emailAllowed) {
        this.emailAllowed = emailAllowed;
    }

    public Boolean getPrimaryContact() {
        return isPrimaryContact != null ? isPrimaryContact : false;
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

    public Boolean getFavourited() {
        return isFavourited != null ? isFavourited : false;
    }

    public void setFavourited(Boolean favourited) {
        isFavourited = favourited;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public Integer getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Integer assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getAssigneeIdName() {
        return assigneeIdName;
    }

    public void setAssigneeIdName(String assigneeIdName) {
        this.assigneeIdName = assigneeIdName;
    }

    public String getBackupAssignee() {
        return backupAssignee;
    }

    public void setBackupAssignee(String backupAssignee) {
        this.backupAssignee = backupAssignee;
    }

    public Integer getBackupAssigneeId() {
        return backupAssigneeId;
    }

    public void setBackupAssigneeId(Integer backupAssigneeId) {
        this.backupAssigneeId = backupAssigneeId;
    }

    public String getBackupAssigneeIdName() {
        return backupAssigneeIdName;
    }

    public void setBackupAssigneeIdName(String backupAssigneeIdName) {
        this.backupAssigneeIdName = backupAssigneeIdName;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Integer getRatingId() {
        return ratingId;
    }

    public void setRatingId(Integer ratingId) {
        this.ratingId = ratingId;
    }

    public String getRatingCode() {
        return ratingCode;
    }

    public void setRatingCode(String ratingCode) {
        this.ratingCode = ratingCode;
    }

    public String getRatingIdCode() {
        return ratingIdCode;
    }

    public void setRatingIdCode(String ratingIdCode) {
        this.ratingIdCode = ratingIdCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusIdCode() {
        return statusIdCode;
    }

    public void setStatusIdCode(String statusIdCode) {
        this.statusIdCode = statusIdCode;
    }

    public String getStatusIdCodeName() {
        return statusIdCodeName;
    }

    public void setStatusIdCodeName(String statusIdCodeName) {
        this.statusIdCodeName = statusIdCodeName;
    }

    public Integer getStatusSorder() {
        return statusSorder;
    }

    public void setStatusSorder(Integer statusSorder) {
        this.statusSorder = statusSorder;
    }

    public String getLeadSource() {
        return leadSource;
    }

    public void setLeadSource(String leadSource) {
        this.leadSource = leadSource;
    }

    public String getLeadSourceOther() {
        return leadSourceOther;
    }

    public void setLeadSourceOther(String leadSourceOther) {
        this.leadSourceOther = leadSourceOther;
    }

    public Integer getLeadSourceId() {
        return leadSourceId;
    }

    public void setLeadSourceId(Integer leadSourceId) {
        this.leadSourceId = leadSourceId;
    }

    public String getLeadSourceCode() {
        return leadSourceCode;
    }

    public void setLeadSourceCode(String leadSourceCode) {
        this.leadSourceCode = leadSourceCode;
    }

    public String getLeadSourceIdCode() {
        return leadSourceIdCode;
    }

    public void setLeadSourceIdCode(String leadSourceIdCode) {
        this.leadSourceIdCode = leadSourceIdCode;
    }

    public String getLeadSourceIdCodeName() {
        return leadSourceIdCodeName;
    }

    public void setLeadSourceIdCodeName(String leadSourceIdCodeName) {
        this.leadSourceIdCodeName = leadSourceIdCodeName;
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

    public List<Integer> getVacancyId() {
        return vacancyId;
    }

    public void setVacancyId(List<Integer> vacancyId) {
        this.vacancyId = vacancyId;
    }

    public List<String> getVacancyName() {
        return vacancyName;
    }

    public void setVacancyName(List<String> vacancyName) {
        this.vacancyName = vacancyName;
    }

    public List<String> getVacancyIdName() {
        return vacancyIdName;
    }

    public void setVacancyIdName(List<String> vacancyIdName) {
        this.vacancyIdName = vacancyIdName;
    }

    public String getPreferredLocation() {
        return preferredLocation;
    }

    public void setPreferredLocation(String preferredLocation) {
        this.preferredLocation = preferredLocation;
    }

    public Integer getPreferredLocationId() {
        return preferredLocationId;
    }

    public void setPreferredLocationId(Integer preferredLocationId) {
        this.preferredLocationId = preferredLocationId;
    }

    public String getPreferredLocationIdName() {
        return preferredLocationIdName;
    }

    public void setPreferredLocationIdName(String preferredLocationIdName) {
        this.preferredLocationIdName = preferredLocationIdName;
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
        return isShortList != null ? isShortList : false;
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

    public Integer getCandidateProjectId() {
        return candidateProjectId;
    }

    public void setCandidateProjectId(Integer candidateProjectId) {
        this.candidateProjectId = candidateProjectId;
    }

    public String getCandidateProject() {
        return candidateProject;
    }

    public void setCandidateProject(String candidateProject) {
        this.candidateProject = candidateProject;
    }

    public String getCandidateProjectIdName() {
        return candidateProjectIdName;
    }

    public void setCandidateProjectIdName(String candidateProjectIdName) {
        this.candidateProjectIdName = candidateProjectIdName;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getCreatorIdName() {
        return creatorIdName;
    }

    public void setCreatorIdName(String creatorIdName) {
        this.creatorIdName = creatorIdName;
    }

    public Integer getCandidateStatusId() {
        return candidateStatusId;
    }

    public void setCandidateStatusId(Integer candidateStatusId) {
        this.candidateStatusId = candidateStatusId;
    }

    public String getCandidateStatus() {
        return candidateStatus;
    }

    public void setCandidateStatus(String candidateStatus) {
        this.candidateStatus = candidateStatus;
    }

    public String getCandidateStatusIdName() {
        return candidateStatusIdName;
    }

    public void setCandidateStatusIdName(String candidateStatusIdName) {
        this.candidateStatusIdName = candidateStatusIdName;
    }

    public Integer getCandidateCreatedById() {
        return candidateCreatedById;
    }

    public void setCandidateCreatedById(Integer candidateCreatedById) {
        this.candidateCreatedById = candidateCreatedById;
    }

    public String getCandidateCreatedBy() {
        return candidateCreatedBy;
    }

    public void setCandidateCreatedBy(String candidateCreatedBy) {
        this.candidateCreatedBy = candidateCreatedBy;
    }

    public String getCandidateCreatedByIdName() {
        return candidateCreatedByIdName;
    }

    public void setCandidateCreatedByIdName(String candidateCreatedByIdName) {
        this.candidateCreatedByIdName = candidateCreatedByIdName;
    }

    public Integer getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(Integer updaterId) {
        this.updaterId = updaterId;
    }

    public String getUpdaterName() {
        return updaterName;
    }

    public void setUpdaterName(String updaterName) {
        this.updaterName = updaterName;
    }

    public String getUpdaterIdName() {
        return updaterIdName;
    }

    public void setUpdaterIdName(String updaterIdName) {
        this.updaterIdName = updaterIdName;
    }

    public String getCandidateDepartment() {
        return candidateDepartment;
    }

    public void setCandidateDepartment(String candidateDepartment) {
        this.candidateDepartment = candidateDepartment;
    }

    public Integer getCandidateDepartmentId() {
        return candidateDepartmentId;
    }

    public void setCandidateDepartmentId(Integer candidateDepartmentId) {
        this.candidateDepartmentId = candidateDepartmentId;
    }

    public String getCandidateDepartmentIdName() {
        return candidateDepartmentIdName;
    }

    public void setCandidateDepartmentIdName(String candidateDepartmentIdName) {
        this.candidateDepartmentIdName = candidateDepartmentIdName;
    }

    public String getCandidatePosition() {
        return candidatePosition;
    }

    public void setCandidatePosition(String candidatePosition) {
        this.candidatePosition = candidatePosition;
    }

    public Integer getCandidatePositionId() {
        return candidatePositionId;
    }

    public void setCandidatePositionId(Integer candidatePositionId) {
        this.candidatePositionId = candidatePositionId;
    }

    public String getCandidatePositionIdName() {
        return candidatePositionIdName;
    }

    public void setCandidatePositionIdName(String candidatePositionIdName) {
        this.candidatePositionIdName = candidatePositionIdName;
    }
}
