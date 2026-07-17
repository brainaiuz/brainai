package com.edatasite.workforce.gwt.contact.client.rpc;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryList;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.MergeItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.Relational;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.crm.client.rpc.CrmSubItem;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportField;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 09-Jul-2009
 * Time: 18:22:40
 * To change this template use File | Settings | File Templates.
 */
public class CrmAccountItem extends Relational implements IsSerializable, ListingCustomFields, CrmConstants {


    //till here///

    public static final String OWNER = "OWNER";
    public static final String PARENT_ACCOUNT_NAME = "PARENT_ACCOUNT_NAME";
    public static final String ACCOUNT_NAME = "ACCOUNT_NAME";
    public static final String ACCOUNT_NUMBER = "ACCOUNT_NUMBER";
    public static final String PHONE = "PHONE";
    public static final String FAX = "FAX";
    public static final String WEBSITE = "WEBSITE";
    public static final String EMAIL = "EMAIL";
    public static final String ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public static final String STREET = "STREET";
    public static final String CITY = "CITY";
    public static final String COUNTRY = "COUNTRY";
    public static final String STATE = "STATE";
    public static final String POST_CODE = "POST_CODE";
    public static final String STREET2 = "STREET2";
    public static final String CITY2 = "CITY2";
    public static final String COUNTRY2 = "COUNTRY2";
    public static final String STATE2 = "STATE2";
    public static final String POST_CODE2 = "POST_CODE2";
    public static final String INDUSTRY = "INDUSTRY";
    public static final String ORGANIZATION_TYPE = "ORGANIZATION_TYPE";
    public static final String ANNUAL_REVENUE = "ANNUAL_REVENUE";
    public static final String NUMBER_OF_EMPLOYEES = "NUMBER_OF_EMPLOYEES";
    public static final String OWNERSHIP = "OWNERSHIPType";
    public static final String RATING = "RATING";
    public static final String CURRENCY = "CURRENCY";
    public static final String VAT_NUMBER = "VAT_NUMBER";
    public static final String TRN_NUMBER = "TRN_NUMBER";
    public static final String REGISTRATION_NUMBER = "REGISTRATION_NUMBER";
    public static final String PAYMENT_METHOD = "PAYMENT_METHOD";
    public static final String BLOCKED = "BLOCKED";
    public static final String LAST_MODIFIED = "LAST_MODIFIED";
    public static final String CREATION_DATE = "CREATION_DATE";
    public static final String STATUS = "STATUS";
    public static final String CONTACT_NAME = "CONTACT_NAME";
    public static final String ADDRESS = "ADDRESS";
    public static final String MAILING_ADDRESS = "MAILING_ADDRESS";
    public static final String BILLING_ADDRESS = "BILLING_ADDRESS";
    public static final String MAILING_ADDRESS2 = "MAILING_ADDRESS2";
    public static final String BILLING_ADDRESS2 = "BILLING_ADDRESS2";
    public static final String CLIENT_BALANCE = "clientbalance";
    public static final String SUPPLIER_BALANCE = "supplierbalance";
    public static final String CREDIT_LIMIT = "creditlimit";
    public static final String TERMS = "terms";
    public static final String CONTACT_EMAIL = "CONTACT_EMAIL";
    public static final String BANK_ACCOUNT = "BANK_ACCOUNT";
    public static final String TAX = "TAX";
    public static final String IN_TARGET = "IN_TARGET";
    public static final String INVOICE_EXPIRE_DATE = "INVOICE_EXPIRE_DATE";
    public static final String INVOICE_PAID_STATUS = "INVOICE_PAID_STATUS";
    public static final String SALES_TYPE = "SALES_TYPE";
    public static final String CR_NUMBER = "CR_NUMBER";

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

    private String note;
    private Integer noteId;

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

    private SelectItem[] ratings;
    private Integer ratingId;
    private String rating;

    private SelectItem[] organizationTypes;
    private Integer organizationTypeID;
    private String organizationType;
    private String organizationTypeCode;
    private String otherOrganizationType;

    private SelectItem[] annualRevenues;
    private Integer annualRevenueID;
    private String annualRevenue;
    private String annualRevenueCode;

    private SelectItem[] numberOfEmployees;
    private Integer numberOfEmployeeID;
    private String numberOfEmployee;

    private SelectItem[] currencies;
    private Integer currencyId;
    private String currency;

    private SelectItem[] bankAccounts;
    private Integer bankAccountId;
    private String bankAccount;

    private SelectItem[] paymentMethods;
    private Integer paymentMethodId;
    private String paymentMethod;
    private SelectItem campaign;
    private Integer campaignId;
    private String campaignName;
    private AccountItem accountsReceivablePayable;

    private SelectItem[] vatCategories;
    private SelectItem vatCategory;


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
    private ArrayList<CrmAccountItem> children;

    private Date createdDate;
    private Date lastUpdatedDate;

    private ArrayList<CompanyCustomFieldItem> customFields;
    private HashMap<String, Object> customFieldsMap;
    private ArrayList<CompanyCustomFieldItem> customFieldsForFiltering;

    private BigDecimal creditLimit;
    private BigDecimal quoteCreditLimit;

    private SelectItem subsidiary;
    private TaxItem vat;

    private boolean isDeleted = false;
    private FileItem[] attachments;
    private boolean fromSignUp;
    private boolean fromSaasu;
    private boolean fromMobile;
    private boolean keyClient = false;
    private boolean fromQuickbooks;
    private boolean blocked;
    private boolean reverseChargeApplicable;
    private Boolean inTarget;
    private String targetId;
    private HashMap<String, Boolean> accountTypesDisabled;

    private SelectItem[] appliedPriceLavel;
    private SelectItem[] appliedDiscounts;
    private SelectItem[] clientTypes;
    private SelectItem clientType;

    private ArrayList<HistoryListItem> notes;

    private Date saasuLastUpdatedDate;
    private String saasuLastUpdatedUid;

    private Integer magentoEntityId;
    private Date magentoLastSyncDate;
    private boolean showContactAddress;
    private boolean hasContacts;
    private boolean createGlAccount;
    private SelectItem warehouse;
    private SelectItem department;

    private Integer companyId;

    private SelectItem[] taxTreatments;
    private String trn;
    private SelectItem taxTreatment;
    private SelectItem[] gccCountries;
    private SelectItem[] gccStates;
    private SelectItem placeOfSupplyCountry;
    private SelectItem placeOfSupplyState;

    private Integer taxTreatmentId;
    private Integer placeOfSupplyCountryId;
    private Integer placeOfSupplyStateId;
    private ArrayList<CrmSubItem> items;
    private ArrayList<CompanyCustomFieldItem> itemCustomFields;
    private ColumnConfigs[] customItemColumns;
    private LinkedHashMap<String, FormProperty> formProperty;
    private Date conversionDate;
    private ArrayList<SelectItem> telegramChats;
    private DateNonConvertable invoiceExpireDate;
    private String invoicePaidStatus;
    private String salesType;
    private Integer salesTypeId;
    private SelectItem[] salesTypes;
    private String passportNumber;
    private String crNumber;

    private String amazonLink;
    private String shortLink;
    private String saleType;

    public void setFromSignUp(boolean from) {
        this.fromSignUp = from;
    }

    public boolean isFromSignUp() {
        return this.fromSignUp;
    }

    public boolean isKeyClient() {
        return keyClient;
    }

    public void setKeyClient(boolean keyClient) {
        this.keyClient = keyClient;
    }

    public ArrayList<HistoryListItem> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<HistoryListItem> notes) {
        this.notes = notes;
    }

    public CrmAccountItem() {
    }

    public CrmAccountItem(String accountName) {
        this.accountName = accountName;
    }

    public CrmAccountItem getParent() {
        return parent;
    }

    public void setParent(CrmAccountItem parent) {
        this.parent = parent;
    }

    public ArrayList<CrmAccountItem> getChildren() {
        if (children == null) {
            children = new ArrayList<>();
        }
        return children;
    }

    public void setChildren(ArrayList<CrmAccountItem> children) {
        this.children = children;
    }

    public void addChild(CrmAccountItem child) {
        if (child != null) {
            getChildren().add(child);
        }
    }

    public ContactListItem getPrimaryContact() {
        return primaryContact;
    }

    public void setPrimaryContact(ContactListItem primaryContact) {
        this.primaryContact = primaryContact;
    }

    public String getPrimaryContactName() {
        return getPrimaryContact() != null ? getPrimaryContact().getName() : "N/A";
    }

    public String getPrimaryContactEmail() {
        return getPrimaryContact() != null ? getPrimaryContact().getPrimaryEmail() : "N/A";
    }

    public ArrayList<ContactListItem> getContacts() {
        if (contacts == null) {
            contacts = new ArrayList<>();
        }
        return contacts;
    }

    public void setContacts(ArrayList<ContactListItem> contacts) {
        this.contacts = contacts;
    }

    public void addContacts(ContactListItem... contacts) {
        if (contacts != null && contacts.length > 0) {
            for (ContactListItem contact : contacts) {
                if (!getContacts().contains(contact)) {
                    getContacts().add(contact);
                }
            }
        }
    }

    public HistoryList getAllHistory() {
        return allHistory;
    }

    public void setAllHistory(HistoryList allHistory) {
        this.allHistory = allHistory;
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

    public String getOwnerNames() {
        return ownerNames;
    }

    public void setOwnerNames(String ownerNames) {
        this.ownerNames = ownerNames;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public SelectItem[] getAnnualRevenues() {
        return annualRevenues;
    }

    public void setAnnualRevenues(SelectItem[] annualRevenues) {
        this.annualRevenues = annualRevenues;
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

    public SelectItem[] getNumberOfEmployees() {
        return numberOfEmployees;
    }

    public void setNumberOfEmployees(SelectItem[] numberOfEmployees) {
        this.numberOfEmployees = numberOfEmployees;
    }

    public SelectItem[] getRatings() {
        return ratings;
    }

    public void setRatings(SelectItem[] ratings) {
        this.ratings = ratings;
    }

    public Integer getRatingId() {
        return ratingId;
    }

    public void setRatingId(Integer ratingId) {
        this.ratingId = ratingId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(String organizationType) {
        this.organizationType = organizationType;
    }

    public Integer getOrganizationTypeID() {
        return organizationTypeID;
    }

    public void setOrganizationTypeID(Integer organizationTypeID) {
        this.organizationTypeID = organizationTypeID;
    }

    public SelectItem[] getOrganizationTypes() {
        return organizationTypes;
    }

    public void setOrganizationTypes(SelectItem[] organizationTypes) {
        this.organizationTypes = organizationTypes;
    }

    public String getOtherOrganizationType() {
        return otherOrganizationType;
    }

    public void setOtherOrganizationType(String otherOrganizationType) {
        this.otherOrganizationType = otherOrganizationType;
    }

    public String getOrganizationTypeCode() {
        return organizationTypeCode;
    }

    public void setOrganizationTypeCode(String organizationTypeCode) {
        this.organizationTypeCode = organizationTypeCode;
    }

    public String getAnnualRevenueCode() {
        return annualRevenueCode;
    }

    public void setAnnualRevenueCode(String annualRevenueCode) {
        this.annualRevenueCode = annualRevenueCode;
    }

    public String getOwnershipCode() {
        return ownershipCode;
    }

    public void setOwnershipCode(String ownershipCode) {
        this.ownershipCode = ownershipCode;
    }

    public String getOtherIndustry() {
        return otherIndustry;
    }

    public void setOtherIndustry(String otherIndustry) {
        this.otherIndustry = otherIndustry;
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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getNoteId() {
        return noteId;
    }

    public void setNoteId(Integer noteId) {
        this.noteId = noteId;
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

    public Integer getImportFileID() {
        return importFileID;
    }

    public void setImportFileID(Integer importFileID) {
        this.importFileID = importFileID;
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

    public SelectItem[] getTitle() {
        return title;
    }

    public SelectItem[] getCurrencies() {
        return currencies;
    }

    public void setCurrencies(SelectItem[] currencies) {
        this.currencies = currencies;
    }

    public String getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(String vatNumber) {
        this.vatNumber = vatNumber;
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

    public SelectItem[] getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(SelectItem[] paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public AccountItem getAccountsReceivablePayable() {
        return accountsReceivablePayable;
    }

    public void setAccountsReceivablePayable(AccountItem accountsReceivablePayable) {
        this.accountsReceivablePayable = accountsReceivablePayable;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public Integer getVatNumberId() {
        return vatNumberId;
    }

    public void setVatNumberId(Integer vatNumberId) {
        this.vatNumberId = vatNumberId;
    }

    public Integer getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Integer paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public SelectItem getCampaign() {
        return campaign;
    }

    public void setCampaign(SelectItem campaign) {
        this.campaign = campaign;
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

    public ImportFile importFile() {
        ImportFile importFile = createColumns(this);
        importFile.setFileID(getObjectId());
        importFile.setConversionDate(getConversionDate());
        return importFile;
    }

    private ImportFile createColumns(CrmAccountItem item) {
        ImportFile importFile = new ImportFile();
        if (item != null) {
            //Information
            importFile.addColumn(ImportField.CrmAccountField.FIELD_NAME, item.getNameId());
            importFile.addColumn(ImportField.CrmAccountField.FIELD_PARENT, item.getParentID());
            importFile.addColumn(ImportField.CrmAccountField.FIELD_NUMBER, item.getNumberId());
            importFile.addColumn(ImportField.CrmAccountField.FIELD_TYPE, item.getAccountTypeID());
            importFile.addColumn(ImportField.CrmAccountField.FIELD_INDUSTRY, item.getIndustryID());
            importFile.addColumn(ImportField.CrmAccountField.FIELD_EMAIL, item.getEmailId());
            importFile.addColumn(ImportField.CrmAccountField.FIELD_PHONE, item.getPhoneId());
            importFile.addColumn(ImportField.CrmAccountField.FIELD_FAX, item.getFaxId());
            importFile.addColumn(ImportField.CrmAccountField.FIELD_WEBSITE, item.getWebsiteId());
            //Address Information
            Address[] billAddresses = item.getBillAddresses(), mailAddresses = item.getMailAddresses();
            if (billAddresses != null) {
                for (Address addr : billAddresses) {
                    String nameID = importFile.getAsString(addr.getNameId());
                    String addressID = importFile.getAsString(addr.getAddressId());
                    String addressBID = importFile.getAsString(addr.getAddressBId());
                    String cityID = importFile.getAsString(addr.getCityId());
                    String countryID = importFile.getAsString(addr.getCountryId());
                    String stateID = importFile.getAsString(addr.getStateId());
                    String zipcodeID = importFile.getAsString(addr.getZipCodeId());
                    importFile.addExtraColumn(true, ImportField.CrmAccountField.FIELD_ADDRESSES, Address.BILLING_ADDRESS, nameID, addressID, addressBID, cityID, countryID, stateID, zipcodeID);
                }
            }
            if (mailAddresses != null) {
                for (Address addr : mailAddresses) {
                    String nameID = importFile.getAsString(addr.getNameId());
                    String addressID = importFile.getAsString(addr.getAddressId());
                    String addressBID = importFile.getAsString(addr.getAddressBId());
                    String cityID = importFile.getAsString(addr.getCityId());
                    String countryID = importFile.getAsString(addr.getCountryId());
                    String stateID = importFile.getAsString(addr.getStateId());
                    String zipcodeID = importFile.getAsString(addr.getZipCodeId());
                    importFile.addExtraColumn(true, ImportField.CrmAccountField.FIELD_ADDRESSES, Address.MAILING_ADDRESS, nameID, addressID, addressBID, cityID, countryID, stateID, zipcodeID);
                }
            }
            //Financial Information
            importFile.addColumn(ImportField.CrmAccountField.FIELD_CURRENCY, item.getCurrencyId());
            importFile.addColumn(ImportField.CrmAccountField.FIELD_VAT_NUMBER, item.getVatNumberId());
            importFile.addColumn(ImportField.CrmAccountField.FIELD_PAYMENT_METHOD, item.getPaymentMethodId());
            importFile.addColumn(ImportField.CrmAccountField.FIELD_REGISTRATION_NUMBER, item.getRegistrationNumberId());
            importFile.addColumn(ImportField.CrmAccountField.FIELD_CREDIT_LIMIT, item.getCreditLimitId());
            importFile.addColumn(ImportField.CrmAccountField.FIELD_CLIENT_TYPE, item.getClientTypeId());
            importFile.addColumn(ImportField.CrmAccountField.FIELD_BALANCE_DATE, item.getBalanceDateId());
            importFile.addColumn(ImportField.CrmAccountField.FIELD_BALANCE_AMOUNT, item.getBalanceAmountId());
            importFile.addColumn(ImportField.CrmAccountField.FIELD_TERMS, item.getTermsId());
            importFile.addColumn(ImportField.CrmAccountField.FIELD_NOTE, item.getNoteId());
            //supplier bank information
            importFile.addColumn(ImportField.CrmAccountField.FIELD_BANK_NAME, item.getBankNameID());
            importFile.addColumn(ImportField.CrmAccountField.FIELD_ACCOUNT_NAME, item.getAccountNameID());
            importFile.addColumn(ImportField.CrmAccountField.FIELD_ACCOUNT_NO, item.getAccountNoID());
            importFile.addColumn(ImportField.CrmAccountField.FIELD_SWIFT_CODE, item.getSwiftCodeID());
            importFile.addColumn(ImportField.CrmAccountField.FIELD_SORT_CODE, item.getSortCodeID());
            importFile.addColumn(ImportField.CrmAccountField.FIELD_IBAN_CODE, item.getIbanCodeID());
            importFile.addColumn(ImportField.CrmAccountField.FIELD_BRANCH, item.getBranchID());
            importFile.addColumn(ImportField.CrmAccountField.FIELD_BANK_ADDRESS, item.getBankAddressID());
            if (item.getCustomFields() != null && item.getCustomFields().size() > 0) {
                int s = ImportField.ContactField.FIELD_CUSTOM_FIELD_START_NUMBER;
                for (CompanyCustomFieldItem customField : item.getCustomFields()) {
                    if (customField != null && customField.getFieldStringValue() != null && !"".equals(customField.getFieldStringValue()) && customField.getFieldStringValue().matches(Constants.REGEX_INTEGER)) {
                        Integer columnID = Integer.parseInt(customField.getFieldStringValue());
                        importFile.addExtraColumn(false,
                                s++,
                                columnID,
                                customField.getDataType(),
                                customField.getColumnCode(),
                                customField.getCustomFieldSettingID() != null ? customField.getCustomFieldSettingID().toString() : "-1",
                                customField.getUiType(),
                                customField.getPredefinedValues() != null ? String.join("-:-", customField.getPredefinedValues()) : null);
                    } else {
                        importFile.addExtraColumn(false, s++, null);
                    }
                }
            }
        }
        return importFile;
    }

    public boolean isNew() {
        return getObjectId() == null;
    }

    public SelectItem asSelectItem() {
        return new SelectItem(getObjectId(), getName());
    }

    public CrmAccountItem clone() {
        CrmAccountItem crmAccountItem = new CrmAccountItem();
        crmAccountItem.setAccountTypes(getAccountTypes());
        crmAccountItem.setIndustries(getIndustries());
        crmAccountItem.setOwnerships(getOwnerships());
        crmAccountItem.setRatings(getRatings());
        crmAccountItem.setOrganizationTypes(getOrganizationTypes());
        crmAccountItem.setAnnualRevenues(getAnnualRevenues());
        crmAccountItem.setNumberOfEmployees(getNumberOfEmployees());
        crmAccountItem.setCurrencies(getCurrencies());
        crmAccountItem.setPaymentMethods(getPaymentMethods());
        return crmAccountItem;
    }

    public static final ArrayList<String> defaultColumnNames = new ArrayList<>(Arrays.asList(
            CrmAccountItem.ACCOUNT_NUMBER,
            CrmAccountItem.ACCOUNT_NAME,
            CrmAccountItem.PHONE,
            CrmAccountItem.EMAIL,
            CrmAccountItem.OWNER
    ));

    public static ArrayList<Integer> getIDsOnly(Set<CrmAccountItem> selectedItems) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (CrmAccountItem item : selectedItems) {
            ids.add(item.getObjectId());
        }
        return ids;
    }

    public HashMap<String, Boolean> getAccountTypesDisabled() {
        return accountTypesDisabled;
    }

    public void setAccountTypesDisabled(HashMap<String, Boolean> accountTypesDisabled) {
        this.accountTypesDisabled = accountTypesDisabled;
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

    public ArrayList<CompanyCustomFieldItem> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(ArrayList<CompanyCustomFieldItem> customFields) {
        this.customFields = customFields;
    }

    public void setCustomFieldsMap(HashMap<String, Object> customFieldsMap) {
        this.customFieldsMap = customFieldsMap;
    }

    public HashMap<String, Object> getCustomFieldsMap() {
        if (customFieldsMap == null) {
            customFieldsMap = new HashMap<>();
        }
        return customFieldsMap;
    }

    public void setCustomFieldsForFiltering(ArrayList<CompanyCustomFieldItem> customFieldsForFiltering) {
        this.customFieldsForFiltering = customFieldsForFiltering;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldsForFiltering() {
        return customFieldsForFiltering;
    }

    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return getCustomFieldsMap().get(columnCodeKey);
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {
        getCustomFieldsMap().put(columnCodeKey, cellValue);
    }

    public void setTypeChecked(String typeName, Integer typeID) {
        if ((typeID != null && !"".equals(typeID)) || (typeName != null && !"".equals(typeName)) && getAccountTypes() != null && getAccountTypes().length > 0) {
            for (SelectItem type : getAccountTypes()) {
                if (type != null && ((typeName != null && typeName.equalsIgnoreCase(type.getDescription())) || (typeID != null && typeID.equals(type.getId())))) {
                    type.setSelected(true);
                }
            }
        }
    }


    // CLIENTDATA

    private String quickbookCustomerID;
    private String quickbookEditSequence;

    private String saasuGUID;
    private Date creationTime;

    private String code;
    private String description;

    private Integer outGoingType;

    private Integer codeId;
    private Integer balanceDateId;
    private Integer balanceAmountId;
    private Integer creditLimitId;
    private Integer saddressId;
    private Integer scityId;
    private Integer szipCodeId;
    private Integer contactFirstNameId;
    private Integer contactLastNameId;
    private Integer contactEmailId;
    private Integer contactPhoneId;
    private Integer contactPositionId;
    private Integer clientTypeId;
    private Boolean invisible;

    private boolean importing = false;

    private boolean isOpeningBalanceEditable;
    private DateNonConvertable balanceDate;
    private Double balanceAmount;
    private DateNonConvertable supplierBalanceDate;
    private Double supplierBalanceAmount;
    private SelectItem termsItem;
    private String termName;
    private Integer termsId;

    private Double clientBalance;
    private Double supplierBalance;

    private Integer baseCurrencyID;
    private String baseCurrencyName;
    private String encryptedID;
    private String taxName;


    public String getCode() {
        return getNumber();
    }

    public void setCode(String code) {
        setNumber(code);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getOutGoingType() {
        return outGoingType;
    }

    public void setOutGoingType(Integer outGoingType) {
        this.outGoingType = outGoingType;
    }

    public Integer getBalanceDateId() {
        return balanceDateId;
    }

    public void setBalanceDateId(Integer balanceDateId) {
        this.balanceDateId = balanceDateId;
    }

    public Integer getBalanceAmountId() {
        return balanceAmountId;
    }

    public void setBalanceAmountId(Integer balanceAmountId) {
        this.balanceAmountId = balanceAmountId;
    }

    public Integer getCreditLimitId() {
        return creditLimitId;
    }

    public void setCreditLimitId(Integer creditLimitId) {
        this.creditLimitId = creditLimitId;
    }

    public Integer getClientTypeId() {
        return clientTypeId;
    }

    public void setClientTypeId(Integer clientTypeId) {
        this.clientTypeId = clientTypeId;
    }

    public SelectItem getTermsItem() {
        return termsItem;
    }

    public void setTermsItem(SelectItem termsItem) {
        this.termsItem = termsItem;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public Integer getTermsId() {
        return termsId;
    }

    public void setTermsId(Integer termsId) {
        this.termsId = termsId;
    }

    public Boolean getInvisible() {
        if (invisible == null) {
            invisible = false;
        }
        return invisible;
    }

    public void setInvisible(Boolean invisible) {
        this.invisible = invisible;
    }

    public Integer getCodeId() {
        return codeId;
    }

    public void setCodeId(Integer codeId) {
        this.codeId = codeId;
    }

    public Integer getSaddressId() {
        return saddressId;
    }

    public void setSaddressId(Integer saddressId) {
        this.saddressId = saddressId;
    }

    public Integer getScityId() {
        return scityId;
    }

    public void setScityId(Integer scityId) {
        this.scityId = scityId;
    }

    public Integer getSzipCodeId() {
        return szipCodeId;
    }

    public void setSzipCodeId(Integer szipCodeId) {
        this.szipCodeId = szipCodeId;
    }

    public Integer getContactFirstNameId() {
        return contactFirstNameId;
    }

    public void setContactFirstNameId(Integer contactFirstNameId) {
        this.contactFirstNameId = contactFirstNameId;
    }

    public Integer getContactLastNameId() {
        return contactLastNameId;
    }

    public void setContactLastNameId(Integer contactLastNameId) {
        this.contactLastNameId = contactLastNameId;
    }

    public Integer getContactEmailId() {
        return contactEmailId;
    }

    public void setContactEmailId(Integer contactEmailId) {
        this.contactEmailId = contactEmailId;
    }

    public Integer getContactPhoneId() {
        return contactPhoneId;
    }

    public void setContactPhoneId(Integer contactPhoneId) {
        this.contactPhoneId = contactPhoneId;
    }

    public Integer getContactPositionId() {
        return contactPositionId;
    }

    public void setContactPositionId(Integer contactPositionId) {
        this.contactPositionId = contactPositionId;
    }

    public String getQuickbookCustomerID() {
        return quickbookCustomerID;
    }

    public void setQuickbookCustomerID(String quickbookCustomerID) {
        this.quickbookCustomerID = quickbookCustomerID;
    }

    public String getQuickbookEditSequence() {
        return quickbookEditSequence;
    }

    public void setQuickbookEditSequence(String quickbookEditSequence) {
        this.quickbookEditSequence = quickbookEditSequence;
    }

    public String getSaasuGUID() {
        return saasuGUID;
    }

    public void setSaasuGUID(String saasuGUID) {
        this.saasuGUID = saasuGUID;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public boolean isImporting() {
        return importing;
    }

    public void setImporting(boolean importing) {
        this.importing = importing;
    }

    public boolean isOpeningBalanceEditable() {
        return isOpeningBalanceEditable;
    }

    public void setOpeningBalanceEditable(boolean openingBalanceEditable) {
        this.isOpeningBalanceEditable = openingBalanceEditable;
    }

    public DateNonConvertable getBalanceDate() {
        return balanceDate;
    }

    public void setBalanceDate(DateNonConvertable balanceDate) {
        this.balanceDate = balanceDate;
    }

    public Double getBalanceAmount() {
        return balanceAmount;
    }

    public void setBalanceAmount(Double balanceAmount) {
        this.balanceAmount = balanceAmount;
    }

    public DateNonConvertable getSupplierBalanceDate() {
        return supplierBalanceDate;
    }

    public void setSupplierBalanceDate(DateNonConvertable supplierBalanceDate) {
        this.supplierBalanceDate = supplierBalanceDate;
    }

    public Double getSupplierBalanceAmount() {
        return supplierBalanceAmount;
    }

    public void setSupplierBalanceAmount(Double supplierBalanceAmount) {
        this.supplierBalanceAmount = supplierBalanceAmount;
    }

    public Integer getBaseCurrencyID() {
        return baseCurrencyID;
    }

    public void setBaseCurrencyID(Integer baseCurrencyID) {
        this.baseCurrencyID = baseCurrencyID;
    }

    public String getBaseCurrencyName() {
        return baseCurrencyName;
    }

    public void setBaseCurrencyName(String baseCurrencyName) {
        this.baseCurrencyName = baseCurrencyName;
    }

    public String getEncryptedID() {
        return encryptedID;
    }

    public void setEncryptedID(String encryptedID) {
        this.encryptedID = encryptedID;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public static final ArrayList<String> defaultClientColumnNames = new ArrayList<>(Arrays.asList(
            ACCOUNT_NUMBER,
            ACCOUNT_NAME,
            CONTACT_NAME,
            EMAIL,
            CLIENT_BALANCE
    ));

    public static final ArrayList<String> defaultSupplierColumnNames = new ArrayList<>(Arrays.asList(
            ACCOUNT_NUMBER,
            ACCOUNT_NAME,
            CONTACT_NAME,
            EMAIL,
            SUPPLIER_BALANCE
    ));

    private String bankName;
    private String accountName;
    private String accountNo;
    private String swiftCode;
    private String sortCode;
    private String ibanCode;
    private String branch;
    private String bankAddress;
    private Integer bankNameID;
    private Integer accountNameID;
    private Integer accountNoID;
    private Integer swiftCodeID;
    private Integer sortCodeID;
    private Integer ibanCodeID;
    private Integer branchID;
    private Integer bankAddressID;
    public static final int FACET_FILTER_CONTENTS_SIZE = 8;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public String getIbanCode() {
        return ibanCode;
    }

    public void setIbanCode(String ibanCode) {
        this.ibanCode = ibanCode;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public Integer getBankNameID() {
        return bankNameID;
    }

    public void setBankNameID(Integer bankNameID) {
        this.bankNameID = bankNameID;
    }

    public Integer getAccountNameID() {
        return accountNameID;
    }

    public void setAccountNameID(Integer accountNameID) {
        this.accountNameID = accountNameID;
    }

    public Integer getAccountNoID() {
        return accountNoID;
    }

    public void setAccountNoID(Integer accountNoID) {
        this.accountNoID = accountNoID;
    }

    public Integer getSwiftCodeID() {
        return swiftCodeID;
    }

    public void setSwiftCodeID(Integer swiftCodeID) {
        this.swiftCodeID = swiftCodeID;
    }

    public Integer getSortCodeID() {
        return sortCodeID;
    }

    public void setSortCodeID(Integer sortCodeID) {
        this.sortCodeID = sortCodeID;
    }

    public Integer getIbanCodeID() {
        return ibanCodeID;
    }

    public void setIbanCodeID(Integer ibanCodeID) {
        this.ibanCodeID = ibanCodeID;
    }

    public Integer getBranchID() {
        return branchID;
    }

    public void setBranchID(Integer branchID) {
        this.branchID = branchID;
    }

    public Integer getBankAddressID() {
        return bankAddressID;
    }

    public void setBankAddressID(Integer bankAddressID) {
        this.bankAddressID = bankAddressID;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public String getIndustryCode() {
        return industryCode;
    }

    public void setIndustryCode(String industryCode) {
        this.industryCode = industryCode;
    }

    public Address getDefaultAddress(boolean isBilling) {
        if (isBilling) {
            if (billAddresses != null) {
                for (Address addr : billAddresses) {
                    if (addr.isPrimary()) {
                        return addr;
                    }
                }
            }
        } else {
            if (mailAddresses != null) {
                for (Address addr : mailAddresses) {
                    if (addr.isPrimary()) {
                        return addr;
                    }
                }
            }
        }
        return new Address();
    }

    public static ArrayList<MergeItem> getAsMergeItems(String field, HashMap<Integer, CrmAccountItem> crmAccountItems) {
        ArrayList<MergeItem> items = new ArrayList<>();
        if (crmAccountItems != null && crmAccountItems.size() > 0) {
            for (Map.Entry<Integer, CrmAccountItem> item : crmAccountItems.entrySet()) {
                if (item != null) {
                    items.add(item.getValue().getAsMergeItem(field));
                }
            }
        }
        return items;
    }

    public SelectItem[] getBankAccounts() {
        return bankAccounts;
    }

    public void setBankAccounts(SelectItem[] bankAccounts) {
        this.bankAccounts = bankAccounts;
    }

    public Integer getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(Integer bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    private MergeItem getAsMergeItem(String field) {
        MergeItem item = new MergeItem(getObjectId());
        if (field != null) {
            ArrayList<CompanyCustomFieldItem> companyCustomFieldItems = getCustomFields();
            if (companyCustomFieldItems != null && companyCustomFieldItems.size() > 0) {
                for (CompanyCustomFieldItem cfitem : getCustomFields()) {
                    String cfFieldName = cfitem.getFieldName() + "MAINITEM";
                    if (cfFieldName.equals(field)) {
                        if (CompanyCustomFieldItem.DATE.equals(cfitem.getDataType()) && cfitem.getFieldDateNonConvertedValue() != null) {
                            item.setName(DateUtils.getDateFormatShort(cfitem.getFieldDateNonConvertedValue().getNonConvertedDate()));
                        } else {
                            item.setName(cfitem.getFieldStringValue());
                        }
                        item.setId(cfitem.getObjectId());
                        item.setDescription(cfitem.getFieldName());
                        item.setCustomFieldItem(cfitem);
                    }
                }
            }
            switch (field) {
                case CrmAccountItem.OWNER:
                /*item.setId(getOwnerID());
                item.setName(getOwnerName());*/
                    item.setManyResults(true);
                    if (getSelectedOwners() != null && getSelectedOwners().size() > 0) {
                        for (SelectItem owner : getOwnerItems()) {
                            item.addChild(new MergeItem(getObjectId(), owner.getId(), owner.getName()));
                        }
                    }
                    break;
                case CrmAccountItem.PARENT_ACCOUNT_NAME:
                    if (getParent() != null) {
                        item.setId(getParent().getObjectId());
                        item.setName(getParent().getName());
                    }
                    break;
                case CrmAccountItem.ACCOUNT_NAME:
                    item.setId(null);
                    item.setName(getName());
                    break;
                case CrmAccountItem.ACCOUNT_NUMBER:
                    item.setId(null);
                    item.setName(getNumber());
                    break;
                case CrmAccountItem.PHONE:
                    item.setId(null);
                    item.setName(getPhone());
                    break;
                case CrmAccountItem.FAX:
                    item.setId(null);
                    item.setName(getFax());
                    break;
                case CrmAccountItem.WEBSITE:
                    item.setId(null);
                    item.setName(getWebsite());
                    break;
                case CrmAccountItem.EMAIL:
                    item.setId(null);
                    item.setName(getEmail());
                    break;
                case CrmAccountItem.ACCOUNT_TYPE:
                    item.setManyResults(true);
                    if (getAccountTypes() != null && getAccountTypes().length > 0) {
                        for (SelectItem type : getAccountTypes()) {
                            if (type != null && type.isSelected()) {
                                item.addChild(new MergeItem(getObjectId(), type.getId(), type.getName()));
                            }
                        }
                    }
                    break;
                case CrmAccountItem.INDUSTRY:
                    item.setId(getIndustryID());
                    item.setName(getIndustry());
                    break;
                case CrmAccountItem.ORGANIZATION_TYPE:
                    item.setId(getOrganizationTypeID());
                    item.setName(getOrganizationType());
                    break;
                case CrmAccountItem.ANNUAL_REVENUE:
                    item.setId(getAnnualRevenueID());
                    item.setName(getAnnualRevenue());
                    break;
                case CrmAccountItem.NUMBER_OF_EMPLOYEES:
                    item.setId(getNumberOfEmployeeID());
                    item.setName(getNumberOfEmployee());
                    break;
                case CrmAccountItem.OWNERSHIP:
                    item.setId(getOwnershipId());
                    item.setName(getOwnership());
                    break;
                case CrmAccountItem.RATING:
                    item.setId(getRatingId());
                    item.setName(getRating());
                    break;
                case CrmAccountItem.CURRENCY:
                    item.setId(getCurrencyId());
                    item.setName(getCurrency());
                    break;
                case CrmAccountItem.VAT_NUMBER:
                    item.setId(null);
                    item.setName(getVatNumber());
                    break;
                case CrmAccountItem.PAYMENT_METHOD:
                    item.setId(getPaymentMethodId());
                    item.setName(getPaymentMethod());
                    break;
                case CrmAccountItem.MAILING_ADDRESS:
                    item.setManyResults(true);
                    if (getMailAddresses() != null && getMailAddresses().length > 0) {
                        for (Address address : getMailAddresses()) {
                            if (address != null) {
                                String address_ = address.toString();
                                if (!"".equals(address_) && !"N/A".equals(address_)) {
                                    item.addChild(new MergeItem(getObjectId(), address.getObjectID(), address.toString()));
                                }
                            }
                        }
                    }
                    break;
                case CrmAccountItem.BILLING_ADDRESS:
                    item.setManyResults(true);
                    if (getBillAddresses() != null && getBillAddresses().length > 0) {
                        for (Address address : getBillAddresses()) {
                            if (address != null) {
                                String address_ = address.toString();
                                if (!"".equals(address_) && !"N/A".equals(address_)) {
                                    item.addChild(new MergeItem(getObjectId(), address.getObjectID(), address.toString()));
                                }
                            }
                        }
                    }
                    break;
            }
        }
        return item;

    }

    public void changeByMergeItem(String field, MergeItem item, boolean value) {
        if (item.getValue() == null || "N/A".equals(item.getValue())) {
            item.setValue(null);
        }
        if (field != null) {
            if (item.getCustomFieldItem() != null) {
                String cfFieldName = item.getCustomFieldItem().getFieldName() + "MAINITEM";
                if (cfFieldName.equals(field)) {
                    HashMap<String, CompanyCustomFieldItem> customFieldMap = CompanyCustomFieldItem.asMap(getCustomFields());
                    customFieldMap.put(item.getDescription(), item.getCustomFieldItem());
                    setCustomFields(new ArrayList<>(customFieldMap.values()));
                }
            }
            if ("MAINITEM".equals(field)) {
                setObjectId(item.getItemObjectID());
                setName(item.getValue());
            } else if (CrmAccountItem.OWNER.equals(field)) {
                /*setOwnerID(item.getValueID());
                setOwnerName(item.getValue());*/
                ArrayList<SelectItem> selectedOwners = new ArrayList<>();
                selectedOwners.add(new SelectItem(item.getValueID(), item.getValue()));
                setSelectedOwners(selectedOwners);
            } else if (CrmAccountItem.PARENT_ACCOUNT_NAME.equals(field)) {
                setParent(new CrmAccountItem());
                getParent().setObjectId(item.getValueID());
                getParent().setName(item.getValue());
            } else if (CrmAccountItem.ACCOUNT_NAME.equals(field)) {
                setName(item.getValue());
            } else if (CrmAccountItem.ACCOUNT_NUMBER.equals(field)) {
                setNumber(item.getValue());
            } else if (CrmAccountItem.PHONE.equals(field)) {
                setPhone(item.getValue());
            } else if (CrmAccountItem.FAX.equals(field)) {
                setFax(item.getValue());
            } else if (CrmAccountItem.WEBSITE.equals(field)) {
                setWebsite(item.getValue());
            } else if (CrmAccountItem.EMAIL.equals(field)) {
                setEmail(item.getValue());
            } else if (CrmAccountItem.ACCOUNT_TYPE.equals(field)) {
                if (getAccountTypes() != null && getAccountTypes().length > 0) {
                    for (SelectItem type : getAccountTypes()) {
                        if (type != null && type.getId() != null && type.getId().equals(item.getValueID())) {
                            // AccountType hammasida ham bulishi mumkin.
                            // masalan 2 ta accountda ham CUSTOMER bulishi mumkin... shunda bug chiqadi...
                            // shu uchun descriptiondan foydalanamiz...
                            if (type.getDescription() == null || !type.getDescription().matches("((\\d)*(,)?)*")) {
                                type.setDescription("");
                            }
                            //agar selected qilinsa(value == true) descriptionga shu accountni idsini set qilib qo'yamiz...(faqat 1 marta)
                            if (value) {
                                if (!type.getDescription().matches(REGEX_ACCOUNT_TYPE_DESCRIPTION(item.getItemObjectID().toString()))) {
                                    type.setDescription(type.getDescription() + ("".equals(type.getDescription())
                                            ? ""
                                            : ","));
                                    type.setDescription(type.getDescription() + (item.getItemObjectID().toString()));
                                }
                                type.setSelected(value);
                            } else if (!"".equals(type.getDescription())) {
                                //aks holda uni olib tashlash kerak...
                                String[] descriptions = type.getDescription().split(",");
                                StringBuilder newDescription = new StringBuilder();
                                if (descriptions != null && descriptions.length > 0) {
                                    boolean writeComma = false;
                                    for (String desc : descriptions) {
                                        if (!"".equals(desc)) {
                                            if (!item.getItemObjectID().toString().equals(desc)) {
                                                newDescription.append(writeComma ? "," : "").append(desc);
                                                writeComma = true;
                                            }
                                        }
                                    }
                                }
                                type.setDescription(newDescription.toString());
                                if (type.getDescription() == null || "".equals(type.getDescription())) {
                                    type.setSelected(value);
                                }
                            }
                        }
                    }
                }
            } else if (CrmAccountItem.INDUSTRY.equals(field)) {
                setIndustryID(item.getValueID());
                setIndustry(item.getValue());
            } else if (CrmAccountItem.ORGANIZATION_TYPE.equals(field)) {
                setOrganizationTypeID(item.getValueID());
                setOrganizationType(item.getValue());
            } else if (CrmAccountItem.ANNUAL_REVENUE.equals(field)) {
                setAnnualRevenueID(item.getValueID());
                setAnnualRevenue(item.getValue());
            } else if (CrmAccountItem.NUMBER_OF_EMPLOYEES.equals(field)) {
                setNumberOfEmployeeID(item.getValueID());
                setNumberOfEmployee(item.getValue());
            } else if (CrmAccountItem.OWNERSHIP.equals(field)) {
                setOwnershipId(item.getValueID());
                setOwnership(item.getValue());
            } else if (CrmAccountItem.RATING.equals(field)) {
                setRatingId(item.getValueID());
                setRating(item.getValue());
            } else if (CrmAccountItem.CURRENCY.equals(field)) {
                setCurrency(item.getValue());
                setCurrencyId(item.getValueID());
            } else if (CrmAccountItem.VAT_NUMBER.equals(field)) {
                setVatNumber(item.getValue());
            } else if (CrmAccountItem.REGISTRATION_NUMBER.equals(field)) {
                setRegistrationNumber(item.getValue());
            } else if (CrmAccountItem.PAYMENT_METHOD.equals(field)) {
                setPaymentMethodId(item.getValueID());
                setPaymentMethod(item.getValue());
            } else if (CrmAccountItem.MAILING_ADDRESS.equals(field)) {
                HashMap<Integer, Address> addresses = Address.asMap(getMailAddresses());
                if (value) {
                    addresses.put(item.getValueID(), new Address(item.getValueID()));
                } else
                    addresses.remove(item.getValueID());
                setMailAddresses(addresses.values().toArray(new Address[]{}));
            } else if (CrmAccountItem.BILLING_ADDRESS.equals(field)) {
                HashMap<Integer, Address> addresses = Address.asMap(getBillAddresses());
                if (value) {
                    addresses.put(item.getValueID(), new Address(item.getValueID()));
                } else
                    addresses.remove(item.getValueID());
                setBillAddresses(addresses.values().toArray(new Address[]{}));
            }
        }
    }

    private static String REGEX_ACCOUNT_TYPE_DESCRIPTION(String s) {
        return "((\\d*)?(,)?)*(" + s + ")((,)?(\\d*)?)*";
    }

    public boolean hasCustomerType() {
        boolean s = false;
        if (accountTypes != null && accountTypes.length > 0) {
            for (SelectItem accountType : accountTypes) {
                //s = accountType != null && accountType.getDescription() != null && accountType.getDescription().equalsIgnoreCase(CrmAccountItem.CUSTOMER) && accountType.isSelected();
                s = accountType != null && ((accountType.getDescription() != null && accountType.getDescription().equalsIgnoreCase(CrmAccountItem.CUSTOMER)) || (accountType.getName() != null && accountType.getName().equalsIgnoreCase(CrmAccountItem.CUSTOMER))) && accountType.isSelected();
                if (s) {
                    return s;
                }
            }
        }
        return s;
    }

    public boolean hasSupplierType() {
        boolean s = false;
        if (accountTypes != null && accountTypes.length > 0) {
            for (SelectItem accountType : accountTypes) {
                s = accountType != null && ((accountType.getDescription() != null && accountType.getDescription().equalsIgnoreCase(CrmAccountItem.SUPPLIER)) || (accountType.getName() != null && accountType.getName().equalsIgnoreCase(CrmAccountItem.SUPPLIER))) && accountType.isSelected();
                if (s) {
                    return s;
                }
            }
        }
        return s;
    }

    public Double getClientBalance() {
        return clientBalance != null ? clientBalance : 0d;
    }

    public void setClientBalance(Double clientBalance) {
        this.clientBalance = clientBalance;
    }

    public Double getSupplierBalance() {
        return supplierBalance != null ? supplierBalance : 0d;
    }

    public void setSupplierBalance(Double supplierBalance) {
        this.supplierBalance = supplierBalance;
    }

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getQuoteCreditLimit() {
        return quoteCreditLimit;
    }

    public void setQuoteCreditLimit(BigDecimal quoteCreditLimit) {
        this.quoteCreditLimit = quoteCreditLimit;
    }

    public SelectItem getSubsidiary() {
        return subsidiary;
    }

    public void setSubsidiary(SelectItem subsidiary) {
        this.subsidiary = subsidiary;
    }

    public TaxItem getVat() {
        return vat;
    }

    public void setVat(TaxItem vat) {
        this.vat = vat;
    }

    public Date getSaasuLastUpdatedDate() {
        return saasuLastUpdatedDate;
    }

    public void setSaasuLastUpdatedDate(Date saasuLastUpdatedDate) {
        this.saasuLastUpdatedDate = saasuLastUpdatedDate;
    }

    public String getSaasuLastUpdatedUid() {
        return saasuLastUpdatedUid;
    }

    public void setSaasuLastUpdatedUid(String saasuLastUpdatedUid) {
        this.saasuLastUpdatedUid = saasuLastUpdatedUid;
    }

    public Integer getMagentoEntityId() {
        return magentoEntityId;
    }

    public void setMagentoEntityId(Integer magentoEntityId) {
        this.magentoEntityId = magentoEntityId;
    }

    public Date getMagentoLastSyncDate() {
        return magentoLastSyncDate;
    }

    public void setMagentoLastSyncDate(Date magentoLastSyncDate) {
        this.magentoLastSyncDate = magentoLastSyncDate;
    }

    public boolean isShowContactAddress() {
        return showContactAddress;
    }

    public void setShowContactAddress(boolean showContactAddress) {
        this.showContactAddress = showContactAddress;
    }

    public boolean hasContacts() {
        return hasContacts;
    }

    public void setHasContacts(boolean hasContacts) {
        this.hasContacts = hasContacts;
    }

    public boolean isCreateGlAccount() {
        return createGlAccount;
    }

    public void setCreateGlAccount(boolean createGlAccount) {
        this.createGlAccount = createGlAccount;
    }

    public boolean isFromSaasu() {
        return fromSaasu;
    }

    public void setFromSaasu(boolean fromSaasu) {
        this.fromSaasu = fromSaasu;
    }

    public boolean isFromQuickbooks() {
        return fromQuickbooks;
    }

    public void setFromQuickbooks(boolean fromQuickbooks) {
        this.fromQuickbooks = fromQuickbooks;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isReverseChargeApplicable() {
        return reverseChargeApplicable;
    }

    public void setReverseChargeApplicable(boolean reverseChargeApplicable) {
        this.reverseChargeApplicable = reverseChargeApplicable;
    }

    public SelectItem[] getAppliedPriceLavel() {
        return appliedPriceLavel;
    }

    public void setAppliedPriceLavel(SelectItem[] appliedPriceLavel) {
        this.appliedPriceLavel = appliedPriceLavel;
    }

    public SelectItem[] getAppliedDiscounts() {
        return appliedDiscounts;
    }

    public void setAppliedDiscounts(SelectItem[] appliedDiscounts) {
        this.appliedDiscounts = appliedDiscounts;
    }

    public SelectItem[] getClientTypes() {
        return clientTypes;
    }

    public void setClientTypes(SelectItem[] clientTypes) {
        this.clientTypes = clientTypes;
    }

    public SelectItem getClientType() {
        return clientType;
    }

    public void setClientType(SelectItem clientType) {
        this.clientType = clientType;
    }

    public SelectItem[] getVatCategories() {
        return vatCategories;
    }

    public void setVatCategories(SelectItem[] vatCategories) {
        this.vatCategories = vatCategories;
    }

    public SelectItem getVatCategory() {
        return vatCategory;
    }

    public void setVatCategory(SelectItem vatCategory) {
        this.vatCategory = vatCategory;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public void addAddress(boolean isBilling, Address address) {
        if (isBilling) {
            if (getBillAddresses() == null) {
                setBillAddresses(new Address[]{});
            }
        } else {
            if (getMailAddresses() == null) {
                setMailAddresses(new Address[]{});
            }
        }
        Arrays.fill(isBilling ? getBillAddresses() : getMailAddresses(), address);
    }

    @Override
    public Integer getRelationID() {
        return getObjectId();
    }

    @Override
    public String getRelationType() {
        return RelationItem.TYPE_CRM_ACCOUNT;
    }

    @Override
    public String getRelationName() {
        return getName();
    }

    public boolean isFromMobile() {
        return fromMobile;
    }

    public void setFromMobile(boolean fromMobile) {
        this.fromMobile = fromMobile;
    }

    public Integer getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(Integer creatorID) {
        this.creatorID = creatorID;
    }

    public boolean isInTarget() {
        return inTarget != null && inTarget;
    }

    public void setInTarget(Boolean inTarget) {
        this.inTarget = inTarget;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public void setAccountTypeID(Integer accountTypeID) {
        this.accountTypeID = accountTypeID;
    }

    public Integer getAccountTypeID() {
        return accountTypeID;
    }

    public Integer getParentID() {
        return parentID;
    }

    public void setParentID(Integer parentID) {
        this.parentID = parentID;
    }

    public boolean hasAccountType(String type) {
        if (this.getAccountTypes() == null
                || this.getAccountTypes().length <= 0) {
            return false;
        }
        for (SelectItem selectItem : this.getAccountTypes()) {
            if (selectItem == null) {
                continue;
            }
            if (type.equals(selectItem.getCode())) {
                return true;
            }
        }
        return false;

    }

    public SelectItem getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(SelectItem warehouse) {
        this.warehouse = warehouse;
    }

    public SelectItem getDepartment() {
        return department;
    }

    public void setDepartment(SelectItem department) {
        this.department = department;
    }

    public SelectItem[] getTaxTreatments() {
        return taxTreatments;
    }

    public void setTaxTreatments(SelectItem[] taxTreatments) {
        this.taxTreatments = taxTreatments;
    }

    public SelectItem getTaxTreatment() {
        return taxTreatment;
    }

    public void setTaxTreatment(SelectItem taxTreatment) {
        this.taxTreatment = taxTreatment;
    }

    public SelectItem[] getGccCountries() {
        return gccCountries;
    }

    public void setGccCountries(SelectItem[] gccCountries) {
        this.gccCountries = gccCountries;
    }

    public SelectItem[] getGccStates() {
        return gccStates;
    }

    public void setGccStates(SelectItem[] gccStates) {
        this.gccStates = gccStates;
    }

    public SelectItem getPlaceOfSupplyCountry() {
        return placeOfSupplyCountry;
    }

    public void setPlaceOfSupplyCountry(SelectItem placeOfSupplyCountry) {
        this.placeOfSupplyCountry = placeOfSupplyCountry;
    }

    public SelectItem getPlaceOfSupplyState() {
        return placeOfSupplyState;
    }

    public void setPlaceOfSupplyState(SelectItem placeOfSupplyState) {
        this.placeOfSupplyState = placeOfSupplyState;
    }

    public String getTrn() {
        return trn;
    }

    public void setTrn(String trn) {
        this.trn = trn;
    }

    public Integer getTaxTreatmentId() {
        return taxTreatmentId;
    }

    public void setTaxTreatmentId(Integer taxTreatmentId) {
        this.taxTreatmentId = taxTreatmentId;
    }

    public Integer getPlaceOfSupplyCountryId() {
        return placeOfSupplyCountryId;
    }

    public void setPlaceOfSupplyCountryId(Integer placeOfSupplyCountryId) {
        this.placeOfSupplyCountryId = placeOfSupplyCountryId;
    }

    public Integer getPlaceOfSupplyStateId() {
        return placeOfSupplyStateId;
    }

    public void setPlaceOfSupplyStateId(Integer placeOfSupplyStateId) {
        this.placeOfSupplyStateId = placeOfSupplyStateId;
    }

    public ArrayList<CrmSubItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<CrmSubItem> items) {
        this.items = items;
    }

    public ArrayList<CompanyCustomFieldItem> getItemCustomFields() {
        return itemCustomFields;
    }

    public void setItemCustomFields(ArrayList<CompanyCustomFieldItem> itemCustomFields) {
        this.itemCustomFields = itemCustomFields;
    }

    public ColumnConfigs[] getCustomItemColumns() {
        return customItemColumns;
    }

    public void setCustomItemColumns(ColumnConfigs[] customItemColumns) {
        this.customItemColumns = customItemColumns;
    }

    public LinkedHashMap<String, FormProperty> getFormProperty() {
        return this.formProperty;
    }

    public void setFormProperty(final LinkedHashMap<String, FormProperty> formProperty) {
        this.formProperty = formProperty;
    }

    public Date getConversionDate() {
        return this.conversionDate;
    }

    public void setConversionDate(final Date conversionDate) {
        this.conversionDate = conversionDate;
    }

    public ArrayList<SelectItem> getTelegramChats() {
        return telegramChats;
    }

    public void setTelegramChats(ArrayList<SelectItem> telegramChats) {
        this.telegramChats = telegramChats;
    }

    public DateNonConvertable getInvoiceExpireDate() {
        return invoiceExpireDate;
    }

    public void setInvoiceExpireDate(DateNonConvertable invoiceExpireDate) {
        this.invoiceExpireDate = invoiceExpireDate;
    }

    public String getInvoicePaidStatus() {
        return invoicePaidStatus;
    }

    public void setInvoicePaidStatus(String invoicePaidStatus) {
        this.invoicePaidStatus = invoicePaidStatus;
    }

    public HashMap<Integer, ArrayList<String>> getItemsParam(CrmAccountItem item) {
        if (item.getTelegramChats() != null && !item.getTelegramChats().isEmpty()) {
            HashMap<Integer, ArrayList<String>> itemParam = new HashMap<>();
            for (SelectItem chat : item.getTelegramChats()) {
                ArrayList<String> list = new ArrayList<>();
                list.add(chat.getEntityId().toString());
                itemParam.put(chat.getId(), list);
            }
            return itemParam;
        }
        return null;
    }

    public SelectItem[] getSalesTypes() {
        return salesTypes;
    }

    public void setSalesTypes(SelectItem[] salesTypes) {
        this.salesTypes = salesTypes;
    }

    public Integer getSalesTypeId() {
        return salesTypeId;
    }

    public void setSalesTypeId(Integer salesTypeId) {
        this.salesTypeId = salesTypeId;
    }

    public String getSalesType() {
        return salesType;
    }

    public void setSalesType(String salesType) {
        this.salesType = salesType;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public String getCrNumber() {
        return crNumber;
    }

    public void setCrNumber(String crNumber) {
        this.crNumber = crNumber;
    }

    public String getAmazonLink() {
        return amazonLink;
    }

    public void setAmazonLink(String amazonLink) {
        this.amazonLink = amazonLink;
    }

    public String getShortLink() {
        return shortLink;
    }

    public void setShortLink(String shortLink) {
        this.shortLink = shortLink;
    }

    public String getSaleType() {
        return saleType;
    }

    public void setSaleType(String saleType) {
        this.saleType = saleType;
    }
}
