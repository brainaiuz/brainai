package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dilshod Madrahimov on 4/4/15 2:26 PM
 */
public class ContactTO implements IsSerializable {

    Integer id;
    String name;
    String firstName;
    String lastName;
    String title;
    CrmAccountTO crmAccount; //Company Name
    String primaryPhone;
    String primaryEmail;
    AddressTO primaryAddress;
    String jobTitle;
    UserTO owner;
    String department;
    AttachmentTO image;
    Long dateOfBirth;
    UserTO reportsTo;//supervisor
    SelectItemTO campaign;
    Boolean isEmailOptOut;
    List<SelectItemTO> categories;
    SelectItemTO mailingList;
    SelectItemTO supervisor;
    List<AddressTO> addresses;
    List<ContactParamTO> emails;
    List<ContactParamTO> phones;
    List<ContactParamTO> imAddresses;
    List<ContactParamTO> webAddresses;
    List<ContactParamTO> relationships;
    SelectItemTO company;
    SelectItemTO leadStatus;
    SelectItemTO LeadSource;
    SelectItemTO rating;

    Integer contactType;
    String relationType;
    Integer relationId;
    Boolean checkForDuplicates = Boolean.FALSE;

    List<Object> custom_fields;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    public String getPrimaryEmail() {
        return primaryEmail;
    }

    public void setPrimaryEmail(String primaryEmail) {
        this.primaryEmail = primaryEmail;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }


    public UserTO getOwner() {
        return owner;
    }

    public void setOwner(UserTO owner) {
        this.owner = owner;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public AttachmentTO getImage() {
        return image;
    }

    public void setImage(AttachmentTO image) {
        this.image = image;
    }

    public Long getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Long dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public UserTO getReportsTo() {
        return reportsTo;
    }

    public void setReportsTo(UserTO reportsTo) {
        this.reportsTo = reportsTo;
    }

    public SelectItemTO getCampaign() {
        return campaign;
    }

    public void setCampaign(SelectItemTO campaign) {
        this.campaign = campaign;
    }

    public Boolean getIsEmailOptOut() {
        return isEmailOptOut;
    }

    public void setIsEmailOptOut(Boolean isEmailOptOut) {
        this.isEmailOptOut = isEmailOptOut;
    }

    public List<SelectItemTO> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<SelectItemTO> categories) {
        this.categories = categories;
    }

    public SelectItemTO getMailingList() {
        return mailingList;
    }

    public void setMailingList(SelectItemTO mailingList) {
        this.mailingList = mailingList;
    }

    public SelectItemTO getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(SelectItemTO supervisor) {
        this.supervisor = supervisor;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public Integer getRelationId() {
        return relationId;
    }

    public void setRelationId(Integer relationId) {
        this.relationId = relationId;
    }

    public List<AddressTO> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressTO> addresses) {
        this.addresses = addresses;
    }

    public CrmAccountTO getCrmAccount() {
        return crmAccount;
    }

    public void setCrmAccount(CrmAccountTO crmAccount) {
        this.crmAccount = crmAccount;
    }

    public List<ContactParamTO> getEmails() {
        return emails;
    }

    public void setEmails(List<ContactParamTO> emails) {
        this.emails = emails;
    }

    public List<ContactParamTO> getPhones() {
        return phones;
    }

    public void setPhones(List<ContactParamTO> phones) {
        this.phones = phones;
    }

    public List<ContactParamTO> getImAddresses() {
        return imAddresses;
    }

    public void setImAddresses(List<ContactParamTO> imAddresses) {
        this.imAddresses = imAddresses;
    }

    public List<ContactParamTO> getWebAddresses() {
        return webAddresses;
    }

    public void setWebAddresses(List<ContactParamTO> webAddresses) {
        this.webAddresses = webAddresses;
    }

    public List<ContactParamTO> getRelationships() {
        return relationships;
    }

    public void setRelationships(List<ContactParamTO> relationships) {
        this.relationships = relationships;
    }

    public Integer getContactType() {
        return contactType;
    }

    public void setContactType(Integer contactType) {
        this.contactType = contactType;
    }

    public SelectItemTO getCompany() {
        return company;
    }

    public void setCompany(SelectItemTO company) {
        this.company = company;
    }

    public AddressTO getPrimaryAddress() {
        return primaryAddress;
    }

    public void setPrimaryAddress(AddressTO primaryAddress) {
        this.primaryAddress = primaryAddress;
    }

    public SelectItemTO getLeadStatus() {
        return leadStatus;
    }

    public void setLeadStatus(SelectItemTO leadStatus) {
        this.leadStatus = leadStatus;
    }

    public SelectItemTO getLeadSource() {
        return LeadSource;
    }

    public void setLeadSource(SelectItemTO leadSource) {
        LeadSource = leadSource;
    }

    public SelectItemTO getRating() {
        return rating;
    }

    public void setRating(SelectItemTO rating) {
        this.rating = rating;
    }

    public Boolean isCheckForDuplicates() {
        return checkForDuplicates;
    }

    public void setCheckForDuplicates(Boolean checkForDuplicates) {
        this.checkForDuplicates = checkForDuplicates;
    }

    public List<Object> getCustom_fields() {
        return custom_fields;
    }

    public void setCustom_fields(List<Object> custom_fields) {
        this.custom_fields = custom_fields;
    }

    public ContactTO() {
    }

    public ContactTO(ContactListItem item) {
        this.id = item.getObjectId();
        this.name = item.getContactName();
        this.firstName = item.getFirstName();
        this.lastName = item.getLastName();
        this.jobTitle = item.getJobTitle();
        this.primaryPhone = !"N/A".equals(item.getPrimaryPhone()) ? item.getPrimaryPhone() : null;
        this.primaryEmail = !"N/A".equals(item.getPrimaryEmail()) ? item.getPrimaryEmail() : null;
        if (item.getContactImageID() != null) {
            this.image = convertToAttachmentTo(item);
        }
        this.company = new SelectItemTO(item.getCrmAccount().getObjectId(), item.getCrmAccount().getName());
        this.primaryAddress = new AddressTO(item.getPrimaryAddress());
    }

    private AttachmentTO convertToAttachmentTo(ContactListItem item) {
        AttachmentTO attachmentTO = new AttachmentTO();
        attachmentTO.setId(item.getContactImageID());
        attachmentTO.setDownloadLink(item.getContactImageUrl());
        return attachmentTO;
    }

    public ContactTO(ContactListItem item, boolean briefly) {
        this(item);
        this.title = item.getTitle();
        this.owner = new UserTO(item.getOwnerId(), item.getOwner());
        this.department = item.getDepartment();
        this.dateOfBirth = WrapUtils.dateToLong(item.getBirthDate() != null ? item.getBirthDate().getDate() : null);
        this.reportsTo = item.getReportsToId() == null ? null : new UserTO(item.getReportsToId(), item.getReportsTo());
        this.campaign = item.getCampaignId() == null ? null : new SelectItemTO(item.getCampaignId(), item.getCampaign());
        this.isEmailOptOut = item.isEmailOptOut();
        if (item.getSelectedCategories() != null && !item.getSelectedCategories().isEmpty()) {
            ArrayList<SelectItemTO> categoryList = new ArrayList<>();
            for (SelectItem category : item.getSelectedCategories()) {
                categoryList.add(new SelectItemTO(category));
            }
            this.categories = categoryList;
        }

        if (!item.getAddresses().isEmpty()) {
            List<AddressTO> addressList = new ArrayList<>();
            for (Address addressItem : item.getAddresses()) {
                addressList.add(new AddressTO(addressItem));
            }
            this.addresses = addressList;
        }
        if (item.getCrmAccount() != null && item.getCrmAccount().getObjectId() != null) {
            CrmAccountTO crmAccountTO = new CrmAccountTO(item.getCrmAccount().getObjectId(), item.getCrmAccount().getName());
            if (item.getCrmAccount().getAccountTypes() != null && item.getCrmAccount().getAccountTypes().length > 0) {
                crmAccountTO.setAccountTypes(WrapUtils.wrapCheckListItemTOs(item.getCrmAccount().getAccountTypes()));
            }
            crmAccountTO.setIndustry(new SelectItemTO(item.getCrmAccount().getIndustryID(), item.getCrmAccount().getIndustry(), item.getCrmAccount().getIndustryCode(), ""));

            this.crmAccount = crmAccountTO;
        }

    }


    public ContactListItem wrap(ContactTO contactTO) {
        ContactListItem item = new ContactListItem();
        item.setObjectId(contactTO.getId());
        item.setContactName(contactTO.getName());
        item.setFirstName(contactTO.getFirstName());
        item.setLastName(contactTO.getLastName());
        item.setTitle(contactTO.getTitle());
        item.setPrimaryPhone(contactTO.getPrimaryPhone());
        item.setPrimaryEmail(contactTO.getPrimaryEmail());
        item.setJobTitle(contactTO.getJobTitle());
        item.setDepartment(contactTO.getDepartment());
        item.setBirthDate(new DateNonConvertable(WrapUtils.longToDate(contactTO.getDateOfBirth())));
        item.setEmailOptOut(contactTO.getIsEmailOptOut() == null ? false : contactTO.getIsEmailOptOut());
        item.setCheckForDuplicates(contactTO.isCheckForDuplicates());

        if (contactTO.getImage() != null) {
            item.setContactImageID(contactTO.getImage().getId());
        }
        item.getAddresses().addAll(AddressTO.getAddresses(contactTO.getAddresses()));

        if (contactTO.getLeadSource() != null) {
            item.setLeadSourceID(contactTO.getLeadSource().getId());
            item.setLeadSource(contactTO.getLeadSource().getName());
        }
        if (contactTO.getLeadStatus() != null) {
            item.setLeadStatus(WrapUtils.wrapSelectItem(contactTO.getLeadStatus()));
        }
        if (contactTO.getRating() != null) {
            item.setLeadRatingID(contactTO.getRating().getId());
            item.setLeadRating(contactTO.getRating().getName());
        }

        if (contactTO.getCampaign() != null) {
            item.setCampaignId(contactTO.getCampaign().getId());
            item.setCampaign(contactTO.getCampaign().getName());
        }
        if (contactTO.getOwner() != null) {
            item.setOwnerId(contactTO.getOwner().getId());
            item.setOwner(contactTO.getOwner().getName());
        }
        if (contactTO.getReportsTo() != null) {
            item.setReportsToId(contactTO.getReportsTo().getId());//Supervisor
            item.setReportsTo(contactTO.getReportsTo().getName());//Supervisor
        }
        if (contactTO.getCrmAccount() != null && contactTO.getCrmAccount().getId() != null) {
            CrmAccountItem crmAccountItem = new CrmAccountItem();
            crmAccountItem.setObjectId(contactTO.getCrmAccount().getId());
            crmAccountItem.setName(contactTO.getCrmAccount().getName());
            item.setCrmAccount(crmAccountItem);

            if (contactTO.getCrmAccount().getIndustry() != null) {
                item.getCrmAccount().setIndustryID(contactTO.getCrmAccount().getIndustry().getId());
                item.getCrmAccount().setIndustry(contactTO.getCrmAccount().getIndustry().getName());
            }
            List<CheckListItemTO> accountTypeTOs = contactTO.getCrmAccount().getAccountTypes();
            if (accountTypeTOs != null && !accountTypeTOs.isEmpty()) {
                List<SelectItem> accountTypes = new ArrayList<>(accountTypeTOs.size());
                for (CheckListItemTO accountType : accountTypeTOs) {
                    accountTypes.add(new SelectItem(accountType.getId(), accountType.getName(), accountType.getDescription(), accountType.getSelected()));
                }
                item.getCrmAccount().setAccountTypes(accountTypes.toArray(new SelectItem[0]));
            }
        }
        item.setNameNotUnique(!contactTO.isCheckForDuplicates());
        return item;
    }
}
