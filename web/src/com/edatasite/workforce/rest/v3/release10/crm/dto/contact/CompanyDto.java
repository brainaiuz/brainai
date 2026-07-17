package com.edatasite.workforce.rest.v3.release10.crm.dto.contact;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryList;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CompanyDto {
    private Integer id;
    private Integer objectId;
    private String objectKey;
    private String ownerName;
    private Integer ownerID;
    private Integer creatorID;
    private SelectItem[] ownerItems;
    private ArrayList<SelectItem> selectedOwners;
    private String ownerNames;
    private String name;
    private Integer nameId;
    private String prefix;
    private Integer intNumber;
    private String number;
    private Integer numberId;

    private SelectItem[] accountTypes;
    private Integer accountTypeID;

    private String email;
    private Integer emailId;
    private String phone;
    private Integer phoneId;
    private String fax;
    private Integer faxId;
    private String website;
    private Integer websiteId;

    private Address[] billAddresses;
    private Address[] mailAddresses;

    private SelectItem[] countrys;
    private SelectItem[] states;
    private HistoryList history;

    private Integer entityID;

    private HistoryList allHistory;

    private Integer importFileID;

    private SelectItem[] title;
    private Integer titleID;

    private SelectItem[] industries;
    private Integer industryID;
    private String industry;
    private String otherIndustry;
    private String industryCode;

    private SelectItem[] ownerships;
    private Integer ownershipId;
    private String ownership;
    private String ownershipCode;

    private String vatNumber;
    private Integer vatNumberId;
    private String registrationNumber;
    private Integer registrationNumberId;
    private Integer logoId;
    private String logoUrl;

    private ContactListItem primaryContact;
    private ArrayList<ContactListItem> contacts;
    private CrmAccountItem parent;
    private Integer parentID;

    private Date createdDate;
    private Date lastUpdatedDate;

    private ArrayList<CompanyCustomFieldItem> customFields;
    private HashMap<String, Object> customFieldsMap;
    private ArrayList<CompanyCustomFieldItem> customFieldsForFiltering;
    private SelectItem subsidiary;

    private FileItem[] attachments;

    private SelectItem department;

    private Integer companyId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getObjectKey() {
        return objectKey;
    }

    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Integer getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(Integer ownerID) {
        this.ownerID = ownerID;
    }

    public Integer getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(Integer creatorID) {
        this.creatorID = creatorID;
    }

    public SelectItem[] getOwnerItems() {
        return ownerItems;
    }

    public void setOwnerItems(SelectItem[] ownerItems) {
        this.ownerItems = ownerItems;
    }

    public ArrayList<SelectItem> getSelectedOwners() {
        return selectedOwners;
    }

    public void setSelectedOwners(ArrayList<SelectItem> selectedOwners) {
        this.selectedOwners = selectedOwners;
    }

    public String getOwnerNames() {
        return ownerNames;
    }

    public void setOwnerNames(String ownerNames) {
        this.ownerNames = ownerNames;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNameId() {
        return nameId;
    }

    public void setNameId(Integer nameId) {
        this.nameId = nameId;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getNumberId() {
        return numberId;
    }

    public void setNumberId(Integer numberId) {
        this.numberId = numberId;
    }

    public SelectItem[] getAccountTypes() {
        return accountTypes;
    }

    public void setAccountTypes(SelectItem[] accountTypes) {
        this.accountTypes = accountTypes;
    }

    public Integer getAccountTypeID() {
        return accountTypeID;
    }

    public void setAccountTypeID(Integer accountTypeID) {
        this.accountTypeID = accountTypeID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getEmailId() {
        return emailId;
    }

    public void setEmailId(Integer emailId) {
        this.emailId = emailId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(Integer phoneId) {
        this.phoneId = phoneId;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Integer getFaxId() {
        return faxId;
    }

    public void setFaxId(Integer faxId) {
        this.faxId = faxId;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Integer getWebsiteId() {
        return websiteId;
    }

    public void setWebsiteId(Integer websiteId) {
        this.websiteId = websiteId;
    }

    public Address[] getBillAddresses() {
        return billAddresses;
    }

    public void setBillAddresses(Address[] billAddresses) {
        this.billAddresses = billAddresses;
    }

    public Address[] getMailAddresses() {
        return mailAddresses;
    }

    public void setMailAddresses(Address[] mailAddresses) {
        this.mailAddresses = mailAddresses;
    }

    public SelectItem[] getCountrys() {
        return countrys;
    }

    public void setCountrys(SelectItem[] countrys) {
        this.countrys = countrys;
    }

    public SelectItem[] getStates() {
        return states;
    }

    public void setStates(SelectItem[] states) {
        this.states = states;
    }

    public HistoryList getHistory() {
        return history;
    }

    public void setHistory(HistoryList history) {
        this.history = history;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public HistoryList getAllHistory() {
        return allHistory;
    }

    public void setAllHistory(HistoryList allHistory) {
        this.allHistory = allHistory;
    }

    public Integer getImportFileID() {
        return importFileID;
    }

    public void setImportFileID(Integer importFileID) {
        this.importFileID = importFileID;
    }

    public SelectItem[] getTitle() {
        return title;
    }

    public void setTitle(SelectItem[] title) {
        this.title = title;
    }

    public Integer getTitleID() {
        return titleID;
    }

    public void setTitleID(Integer titleID) {
        this.titleID = titleID;
    }

    public SelectItem[] getIndustries() {
        return industries;
    }

    public void setIndustries(SelectItem[] industries) {
        this.industries = industries;
    }

    public Integer getIndustryID() {
        return industryID;
    }

    public void setIndustryID(Integer industryID) {
        this.industryID = industryID;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getOtherIndustry() {
        return otherIndustry;
    }

    public void setOtherIndustry(String otherIndustry) {
        this.otherIndustry = otherIndustry;
    }

    public String getIndustryCode() {
        return industryCode;
    }

    public void setIndustryCode(String industryCode) {
        this.industryCode = industryCode;
    }

    public SelectItem[] getOwnerships() {
        return ownerships;
    }

    public void setOwnerships(SelectItem[] ownerships) {
        this.ownerships = ownerships;
    }

    public Integer getOwnershipId() {
        return ownershipId;
    }

    public void setOwnershipId(Integer ownershipId) {
        this.ownershipId = ownershipId;
    }

    public String getOwnership() {
        return ownership;
    }

    public void setOwnership(String ownership) {
        this.ownership = ownership;
    }

    public String getOwnershipCode() {
        return ownershipCode;
    }

    public void setOwnershipCode(String ownershipCode) {
        this.ownershipCode = ownershipCode;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public Integer getVatNumberId() {
        return vatNumberId;
    }

    public void setVatNumberId(Integer vatNumberId) {
        this.vatNumberId = vatNumberId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Integer getRegistrationNumberId() {
        return registrationNumberId;
    }

    public void setRegistrationNumberId(Integer registrationNumberId) {
        this.registrationNumberId = registrationNumberId;
    }

    public Integer getLogoId() {
        return logoId;
    }

    public void setLogoId(Integer logoId) {
        this.logoId = logoId;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public ContactListItem getPrimaryContact() {
        return primaryContact;
    }

    public void setPrimaryContact(ContactListItem primaryContact) {
        this.primaryContact = primaryContact;
    }

    public ArrayList<ContactListItem> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<ContactListItem> contacts) {
        this.contacts = contacts;
    }

    public CrmAccountItem getParent() {
        return parent;
    }

    public void setParent(CrmAccountItem parent) {
        this.parent = parent;
    }

    public Integer getParentID() {
        return parentID;
    }

    public void setParentID(Integer parentID) {
        this.parentID = parentID;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(ArrayList<CompanyCustomFieldItem> customFields) {
        this.customFields = customFields;
    }

    public HashMap<String, Object> getCustomFieldsMap() {
        return customFieldsMap;
    }

    public void setCustomFieldsMap(HashMap<String, Object> customFieldsMap) {
        this.customFieldsMap = customFieldsMap;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldsForFiltering() {
        return customFieldsForFiltering;
    }

    public void setCustomFieldsForFiltering(ArrayList<CompanyCustomFieldItem> customFieldsForFiltering) {
        this.customFieldsForFiltering = customFieldsForFiltering;
    }

    public SelectItem getSubsidiary() {
        return subsidiary;
    }

    public void setSubsidiary(SelectItem subsidiary) {
        this.subsidiary = subsidiary;
    }

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public SelectItem getDepartment() {
        return department;
    }

    public void setDepartment(SelectItem department) {
        this.department = department;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }
}
