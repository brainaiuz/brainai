package com.workforcetrack.mobile.rpc.crmAccount;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.workforcetrack.mobile.rpc.base.WebServiceUtils;
import com.workforcetrack.mobile.rpc.client.MAdressData;
import com.workforcetrack.mobile.rpc.client.MSelectItem;
import com.workforcetrack.mobile.rpc.contact.MContactListItem;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: Dilsh0d Madrahimov
 * Date: 09.11.16
 * Time: 17:41
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "crmAccountListItem")
public class MCrmAccountListItem {
    private Integer objectID;

    private String ownerName;
    private Integer ownerID;
    private List<MSelectItem> owners;

    private String name;
    private String number;

    private List<MSelectItem> accountTypes;

    private List<MSelectItem> ownerships;
    private Integer ownershipID;
    private String ownership;
    private String ownershipCode;

    private String email;
    private String phone;
    private String fax;
    private String website;

    private List<MSelectItem> ratings;
    private Integer ratingID;
    private String rating;

    private List<MAdressData> billAddresses;
    private List<MAdressData> mailAddresses;

    private List<MSelectItem> countries;
    private List<MSelectItem> states;

    private String note;

    private Integer entityID;

    private List<MSelectItem> title;

    private List<MSelectItem> organizationTypes;
    private Integer organizationTypeID;
    private String organizationType;
    private String otherOrganizationType;

    private List<MSelectItem> industries;
    private Integer industryID;
    private String industry;
    private String otherIndustry;
    private String industryCode;

    private String annualRevenue;
    private Integer annualRevenueID;
    private List<MSelectItem> annualRevenues;
    private String annualRevenueCode;

    private String numberOfEmployee;
    private Integer numberOfEmployeeID;
    private List<MSelectItem> numberOfEmployees;

    private List<MSelectItem> currencies;
    private Integer currencyID;
    private String currency;

    private List<MSelectItem> paymentMethods;
    private Integer paymentMethodID;
    private String paymentMethod;

    private String vatNumber;
    private Integer contactID;
    private String contactFirstName;
    private String contactLastName;
    private String contactEmail;
    private String contactPhone;

    private List<MContactListItem> contacts;
    private MCrmAccountListItem parent;
    private List<MCrmAccountListItem> children;

    private Date createdDate;
    private Date lastUpdatedDate;
    private Boolean doNotShow;
    private Boolean isDeleted;
    private String organizationTypeCode;


    public MCrmAccountListItem() {
    }

    public MCrmAccountListItem(Integer objectID, String name) {
        this.objectID = objectID;
        this.name = name;
    }

    public static MCrmAccountListItem convertToMobile(CrmAccountItem crmAccountItem, boolean isShortData) {
        if (crmAccountItem == null) {
            crmAccountItem = new CrmAccountItem();
        }
        MCrmAccountListItem mCrmAccountListItem = new MCrmAccountListItem();
        mCrmAccountListItem.setObjectID(crmAccountItem.getObjectId());
        mCrmAccountListItem.setName(crmAccountItem.getName());
        mCrmAccountListItem.setNumber(crmAccountItem.getNumber());
        if (crmAccountItem.getParent() != null) {
            mCrmAccountListItem.setParent(new MCrmAccountListItem(crmAccountItem.getParent().getObjectId(), crmAccountItem.getParent().getName()));
        }
        mCrmAccountListItem.setIndustryID(crmAccountItem.getIndustryID());
        mCrmAccountListItem.setIndustry(crmAccountItem.getIndustry());
        mCrmAccountListItem.setIndustryCode(crmAccountItem.getIndustryCode());

        mCrmAccountListItem.setEmail(crmAccountItem.getEmail());
        mCrmAccountListItem.setPhone(crmAccountItem.getPhone());
        mCrmAccountListItem.setFax(crmAccountItem.getFax());
        mCrmAccountListItem.setWebsite(crmAccountItem.getWebsite());

        MAdressData billingAddress = new MAdressData(crmAccountItem.getBillAddresses());
        List<MAdressData> billingAddressList = new ArrayList<>();
        billingAddressList.add(billingAddress);
        mCrmAccountListItem.setBillAddresses(billingAddressList);

        MAdressData mailingAddress = new MAdressData(crmAccountItem.getMailAddresses());
        List<MAdressData> mailingAddressList = new ArrayList<>();
        mailingAddressList.add(mailingAddress);
        mCrmAccountListItem.setMailAddresses(mailingAddressList);

        mCrmAccountListItem.setCurrencyID(crmAccountItem.getCurrencyId());
        mCrmAccountListItem.setCurrency(crmAccountItem.getCurrency());

        mCrmAccountListItem.setVatNumber(crmAccountItem.getVatNumber());

        mCrmAccountListItem.setPaymentMethodID(crmAccountItem.getPaymentMethodId());
        mCrmAccountListItem.setPaymentMethod(crmAccountItem.getPaymentMethod());

        mCrmAccountListItem.setLastUpdatedDate(crmAccountItem.getLastUpdatedDate());
        mCrmAccountListItem.setCreatedDate(crmAccountItem.getCreatedDate());

        if (!isShortData) {
            mCrmAccountListItem.setOwners(WebServiceUtils.getAsMSelectItemList(crmAccountItem.getOwnerItems()));
            mCrmAccountListItem.setIndustries(WebServiceUtils.getAsMSelectItemList(crmAccountItem.getIndustries()));
            mCrmAccountListItem.setAccountTypes(WebServiceUtils.getAsMSelectItemList(crmAccountItem.getAccountTypes()));
        }

        return mCrmAccountListItem;
    }

    public CrmAccountItem convertFromMobile(CrmAccountItem crmAccountItem, ContactListItem contactListItem) {
        if (crmAccountItem == null) {
            crmAccountItem = new CrmAccountItem();
        }
        crmAccountItem.setObjectId(this.objectID);
        crmAccountItem.setName(this.name);

        //crmAccountItem.setOwnerID(this.ownerID);
        //crmAccountItem.setOwnerName(this.ownerName);

        crmAccountItem.setNumber(this.number);

        crmAccountItem.setEmail(this.email);
        crmAccountItem.setPhone(this.phone);
        crmAccountItem.setWebsite(this.website);
        crmAccountItem.setVatNumber(this.vatNumber);
        crmAccountItem.setCurrency(this.currency);
        crmAccountItem.setCurrencyId(this.currencyID);

        if (this.accountTypes != null && this.accountTypes.size() > 0) {
            SelectItem[] items = new SelectItem[this.accountTypes.size()];
            int i = 0;
            for (MSelectItem accountType : this.accountTypes) {
                items[i] = new SelectItem(accountType.getObjectID(), accountType.getName(), accountType.getDescription(), true);
                i++;
            }
            crmAccountItem.setAccountTypes(items);
        }

        if (this.billAddresses != null && !this.billAddresses.isEmpty()) {
            Address billingAddress = new Address();
            billingAddress = this.billAddresses.get(0).convertToAD(billingAddress);
            List<Address> list = new ArrayList<>();
            list.add(billingAddress);
            crmAccountItem.setBillAddresses(list.toArray(new Address[0]));
        }

        if (this.mailAddresses != null && !this.mailAddresses.isEmpty()) {
            Address mailingAddress = new Address();
            mailingAddress = this.mailAddresses.get(0).convertToAD(mailingAddress);
            List<Address> list = new ArrayList<>();
            list.add(mailingAddress);
            crmAccountItem.setMailAddresses(list.toArray(new Address[0]));
        }

        if (contactListItem == null) {
            contactListItem = new ContactListItem();
        }

        contactListItem.getHomeEmail().add(this.contactEmail);
        contactListItem.setPrimaryEmail(this.contactEmail);
        contactListItem.getHomePhone().add(this.contactPhone);
        contactListItem.setPrimaryPhone(this.contactPhone);
        contactListItem.setFirstName(this.contactFirstName);
        contactListItem.setLastName(this.contactLastName);
        contactListItem.setPrimaryContact(true);
        contactListItem.setCrmAccount(crmAccountItem);
        ArrayList<ContactListItem> contacts = new ArrayList<>();
        contacts.add(contactListItem);
        crmAccountItem.setContacts(contacts);

        return crmAccountItem;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
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

    public List<MSelectItem> getOwners() {
        return owners;
    }

    public void setOwners(List<MSelectItem> owners) {
        this.owners = owners;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<MSelectItem> getAccountTypes() {
        return accountTypes;
    }

    public void setAccountTypes(List<MSelectItem> accountTypes) {
        this.accountTypes = accountTypes;
    }

    public List<MSelectItem> getOwnerships() {
        return ownerships;
    }

    public void setOwnerships(List<MSelectItem> ownerships) {
        this.ownerships = ownerships;
    }

    public Integer getOwnershipID() {
        return ownershipID;
    }

    public void setOwnershipID(Integer ownershipID) {
        this.ownershipID = ownershipID;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public List<MSelectItem> getRatings() {
        return ratings;
    }

    public void setRatings(List<MSelectItem> ratings) {
        this.ratings = ratings;
    }

    public Integer getRatingID() {
        return ratingID;
    }

    public void setRatingID(Integer ratingID) {
        this.ratingID = ratingID;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public List<MAdressData> getBillAddresses() {
        return billAddresses;
    }

    public void setBillAddresses(List<MAdressData> billAddresses) {
        this.billAddresses = billAddresses;
    }

    public List<MAdressData> getMailAddresses() {
        return mailAddresses;
    }

    public void setMailAddresses(List<MAdressData> mailAddresses) {
        this.mailAddresses = mailAddresses;
    }

    public List<MSelectItem> getCountries() {
        return countries;
    }

    public void setCountries(List<MSelectItem> countries) {
        this.countries = countries;
    }

    public List<MSelectItem> getStates() {
        return states;
    }

    public void setStates(List<MSelectItem> states) {
        this.states = states;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public List<MSelectItem> getTitle() {
        return title;
    }

    public void setTitle(List<MSelectItem> title) {
        this.title = title;
    }

    public List<MSelectItem> getOrganizationTypes() {
        return organizationTypes;
    }

    public void setOrganizationTypes(List<MSelectItem> organizationTypes) {
        this.organizationTypes = organizationTypes;
    }

    public Integer getOrganizationTypeID() {
        return organizationTypeID;
    }

    public void setOrganizationTypeID(Integer organizationTypeID) {
        this.organizationTypeID = organizationTypeID;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public String getOtherOrganizationType() {
        return otherOrganizationType;
    }

    public void setOtherOrganizationType(String otherOrganizationType) {
        this.otherOrganizationType = otherOrganizationType;
    }

    public List<MSelectItem> getIndustries() {
        return industries;
    }

    public void setIndustries(List<MSelectItem> industries) {
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

    public String getAnnualRevenue() {
        return annualRevenue;
    }

    public void setAnnualRevenue(String annualRevenue) {
        this.annualRevenue = annualRevenue;
    }

    public Integer getAnnualRevenueID() {
        return annualRevenueID;
    }

    public void setAnnualRevenueID(Integer annualRevenueID) {
        this.annualRevenueID = annualRevenueID;
    }

    public List<MSelectItem> getAnnualRevenues() {
        return annualRevenues;
    }

    public void setAnnualRevenues(List<MSelectItem> annualRevenues) {
        this.annualRevenues = annualRevenues;
    }

    public String getAnnualRevenueCode() {
        return annualRevenueCode;
    }

    public void setAnnualRevenueCode(String annualRevenueCode) {
        this.annualRevenueCode = annualRevenueCode;
    }

    public String getNumberOfEmployee() {
        return numberOfEmployee;
    }

    public void setNumberOfEmployee(String numberOfEmployee) {
        this.numberOfEmployee = numberOfEmployee;
    }

    public Integer getNumberOfEmployeeID() {
        return numberOfEmployeeID;
    }

    public void setNumberOfEmployeeID(Integer numberOfEmployeeID) {
        this.numberOfEmployeeID = numberOfEmployeeID;
    }

    public List<MSelectItem> getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public void setNumberOfEmployees(List<MSelectItem> numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    public List<MSelectItem> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<MSelectItem> currencies) {
        this.currencies = currencies;
    }

    public Integer getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(Integer currencyID) {
        this.currencyID = currencyID;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<MSelectItem> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(List<MSelectItem> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public Integer getPaymentMethodID() {
        return paymentMethodID;
    }

    public void setPaymentMethodID(Integer paymentMethodID) {
        this.paymentMethodID = paymentMethodID;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
    }

    public List<MContactListItem> getContacts() {
        return contacts;
    }

    public void setContacts(List<MContactListItem> contacts) {
        this.contacts = contacts;
    }

    public MCrmAccountListItem getParent() {
        return parent;
    }

    public void setParent(MCrmAccountListItem parent) {
        this.parent = parent;
    }

    public List<MCrmAccountListItem> getChildren() {
        return children;
    }

    public void setChildren(List<MCrmAccountListItem> children) {
        this.children = children;
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

    public Boolean getDoNotShow() {
        return doNotShow;
    }

    public void setDoNotShow(Boolean doNotShow) {
        this.doNotShow = doNotShow;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public String getOrganizationTypeCode() {
        return organizationTypeCode;
    }

    public void setOrganizationTypeCode(String organizationTypeCode) {
        this.organizationTypeCode = organizationTypeCode;
    }

    public Integer getContactID() {
        return contactID;
    }

    public void setContactID(Integer contactID) {
        this.contactID = contactID;
    }

    public String getContactFirstName() {
        return contactFirstName;
    }

    public void setContactFirstName(String contactFirstName) {
        this.contactFirstName = contactFirstName;
    }

    public String getContactLastName() {
        return contactLastName;
    }

    public void setContactLastName(String contactLastName) {
        this.contactLastName = contactLastName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
}
