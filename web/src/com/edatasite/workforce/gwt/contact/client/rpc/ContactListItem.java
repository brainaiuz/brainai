package com.edatasite.workforce.gwt.contact.client.rpc;

import com.edatasite.workforce.gwt.contactcategory.client.rpc.ContactCategoryListItem;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.reference.AddressReference;
import com.edatasite.workforce.gwt.core.client.reference.WebAddressReference;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomTableRpc;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryList;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.Key;
import com.edatasite.workforce.gwt.core.client.rpc.MergeItem;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.Relational;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.SpokenLanguageItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.rpc.TreeSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.VacancyItem;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityItem;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.PermissionHolder;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * User: unni
 * Date: Jul 10, 2009
 * Time: 10:48:56 AM
 */
public class ContactListItem extends Relational implements IsSerializable, ListingCustomFields, Key {

    public static final String CONTACT_ID = "contactId";
    public static final String CONTACT_NAME = "contactName";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String OWNER = "owner";
    public static final String GENDER = "gender";
    public static final String WEBSITE = "website";
    public static final String PHONE = "phone";
    public static final String EMAIL = "email";
    public static final String TITLE = "title";
    public static final String COUNTRY = "country";
    public static final String TYPE = "contactType";
    public static final String DEPARTMENT = "contactDepartment";
    public static final String POSITION = "contactPosition";
    public static final String CRM_ACCOUNT = "contactAccount";
    public static final String DATE_OF_BIRTH = "contactDateOfBirth";
    public static final String REPORTS_TO = "contactReportsTo";
    public static final String CAMPAIGN = "contactCampaign";
    public static final String EMAIL_ALLOWED = "contactEmailAllowed";
    public static final String CATEGORIES = "contactCategories";
    public static final String EXTENSIONS = "contactExtensions";
    public static final String LEAD_ASSIGNEE = "assignee";
    public static final String LEAD_BACKUP_ASSIGNEE = "backupAssignee";
    public static final String LEAD_STATUS = "status";
    public static final String LEAD_RATING = "rating";
    public static final String JOB_TITLE = "jobTitle";
    public static final String STREET = "street";
    public static final String STREET2 = "street2";
    public static final String CITY = "city";
    public static final String POST_CODE = "postCode";
    public static final String STATE = "state";
    public static final String LEAD_SOURCE = "leadSource";
    public static final String CANDIDATE_SKILLS = "candidateSkills";
    public static final String FAX = "fax";
    public static final String MOBILE = "mobile";
    public static final String LAST_MODIFIED = "updateDate";
    public static final String CREATION_DATE = "creationDate";
    public static final String CREATOR = "creator";
    public static final String UPDATER = "updater";
    public static final String ASSETS = "assets";
    public static final String MAILING_LIST = "mailingList";
    public static final String PROJECT = "project";
    public static final String INDUSTRY = "industry";
    public static final String VACANCIES = "vacancies";
    public static final String VACANCY_NAME = "vacancy";

    public static final Integer CRM_CONTACT = CrmConstants.TYPE_CRM_CONTACT;
    public static final Integer CLIENT_CONTACT = CrmConstants.TYPE_CLIENT_CONTACT;
    public static final Integer SUPPLIER_CONTACT = CrmConstants.TYPE_SUPPLIER_CONTACT;
    public static final Integer EMPLOYEE_CONTACT = CrmConstants.TYPE_EMPLOYEE_CONTACT;
    public static final Integer LEAD_CONTACT = CrmConstants.TYPE_LEAD_CONTACT;
    public static final Integer STUDENT_CONTACT = CrmConstants.TYPE_STUDENT_CONTACT;
    public static final Integer CANDIDATE = CrmConstants.TYPE_CANDIDATE;
    //candidate statuses
    public static final String _CANDIDATE_STATUS = "_CANDIDATE_STATUS";
    public static final String C_S_AVAILABLE = "CANDIDATE_STATUS_AVAILABLE";               //Available
    public static final String C_S_NEW = "CANDIDATE_STATUS_NEW";                           //New status
    public static final String C_S_NOT_AVAILABLE = "CANDIDATE_STATUS_NOT_AVAILABLE";       //Not available
    public static final String C_S_MATCHED = "CANDIDATE_STATUS_MATCHED";                   //Matched
    public static final String C_S_SHORTLIST = "CANDIDATE_STATUS_SHORTLIST";               //Shortlist
    public static final String C_S_INTERVIEW = "CANDIDATE_STATUS_INTERVIEW";               //Interview
    public static final String C_S_ON_HOLD = "CANDIDATE_STATUS_ON_HOLD";                   //On hold
    public static final String C_S_REJECTED = "CANDIDATE_STATUS_REJECTED";                 //Rejected
    public static final String C_S_OFFER_MADE = "CANDIDATE_STATUS_OFFER_MADE";             //Offer made
    public static final String C_S_PLACED = "CANDIDATE_STATUS_PLACED";                     //Placed
    public static final String C_S_OFFER_DECLINED = "CANDIDATE_STATUS_OFFER_DECLINED";     //Offer declined
    public static final String C_S_OFFER_WITHDRAWN = "CANDIDATE_STATUS_OFFER_WITHDRAWN";   //Offer withdrawn
    public static final String C_S_HIRED = "CANDIDATE_STATUS_HIRED";                       //Hired
    public static final String C_S_UNQUALIFIED = "CANDIDATE_STATUS_UNQUALIFIED";           //Unqualified

    public static final String REQUEST_FROM_CONTACT_SYNC = "REQUEST_FROM_CONTACT_SYNC";
    public static final String REQUEST_FROM_PM_EMPLOYEE_EDIT = "REQUEST_FROM_PM_EMPLOYEE_EDIT";

    // Mobile device sync statuses
    public static final String UPDATED = "UPDATED";
    public static final String DELETED = "DELETED";
    public static final String SYNCED = "SYNCED";
    public static final String NEW = "NEW";
    private static final String HAS_TOKEN = "hasToken";
    private static final String HAS_OFFICE_TOKEN = "hasOfficeToken";
    private static final String CREATED_DATE = "createdDate";
    private static final String UPDATED_DATE = "updatedDate";
    private static final String MIDDLE_NAME_ID = "middleNameId";
    private static final String WEB_FORM_ID = "webFormID";
    private static final String REF_IND_NUMBER = "refIndNumber";
    private static final String OTHER_NAME = "otherName";
    private static final String JOB_FUNCTION = "jobFunction";
    private static final String JOB_FUNCTION_ID = "jobFunctionId";
    private static final String MIDDLE_NAME = "middleName";
    private static final String LAST_NAME_ID = "lastNameId";
    private static final String TITLE_ID = "titleId";
    private static final String HOME_WEB_SITE_ID = "homeWebSiteId";
    private static final String FIRST_NAME_ID = "firstNameId";
    private static final String JOB_TITLE_ID = "jobTitleId";
    private static final String GOOGLE_ID = "googleId";
    private static final String OBJECT_ID = "objectId";
    private static final String OWNER_ID = "ownerId";
    private static final String EMAIL_OPT_OUT = "emailOptOut";
    private static final String REPORTS_TO1 = "reportsTo";
    private static final String ADDRESS_3 = "address3";
    private static final String ADDRESS_2_B = "address2b";
    private static final String ADDRESS_2_ID = "address2Id";
    private static final String HOME_ADDRESS_ID = "homeAddressId";
    private static final String EMAIL_OPT_OUT_ID = "emailOptOutId";
    private static final String REPORTS_TO_ID = "reportsToId";
    private static final String BIRTH_DATE_ID = "birthDateId";
    private static final String DEVICE_ID = "deviceID";
    private static final String DEVICE_CONTACT_ID = "deviceContactID";
    public static final String STATUS = "status";
    private static final String COMPANY_PHOTO_ID = "companyPhotoId";
    private static final String PHOTO_ID = "photoId";
    private static final String COMPANY_PHOTO_URL = "companyPhotoUrl";
    private static final String LEAD_ASSIGNEE_ID = "leadAssigneeID";
    private static final String LEAD_ASSIGNEE1 = "leadAssignee";
    private static final String LEAD_BACKUP_ASSIGNEE_ID = "leadBackupAssigneeID";
    private static final String LEAD_BACKUP_ASSIGNEE1 = "leadBackupAssignee";
    private static final String CATEGORY_NAMES = "categoryNames";
    private static final String BACKGROUND_INFORMATION = "backgroundInformation";
    private static final String OTHER_NAME_ID = "otherNameId";
    private static final String CREATED_FROM = "createdFrom";
    private static final String INDEX = "index";
    private static final String DISCLAIMER = "disclaimer";
    private static final String WORK_EXPERIENCE_MONTH_OR_YEAR = "workExperienceMonthOrYear";
    private static final String CURRENT_EMPLOYER = "currentEmployer";
    private static final String EXPECTED_SALARY = "expectedSalary";
    private static final String START_SALARY = "expectedSalary";
    private static final String SKILLS = "skills";
    private static final String DEPARTMENT1 = "department";
    private static final String CAMPAIGN_ID = "campaignId";
    private static final String CAMPAIGN1 = "campaign";
    private static final String NOTE = "note";
    private static final String ANTIBOT = "antibot";
    private static final String ENTITY_ID = "entityID";
    private static final String PM_DEPARTMENT_ID = "pmDepartmentID";
    private static final String DEPT_START_DATE = "deptStartDate";
    private static final String WAGE_RATE = "wageRate";
    private static final String CLIENT_CHARGE_RATE = "clientChargeRate";
    private static final String JOB_TITLE1 = "jobTitle";
    private static final String OTHER_LEAD_SOURCE = "otherLeadSource";
    private static final String LEAD_SOURCE1 = "leadSource";
    private static final String LEAD_SOURCE_ID = "leadSourceID";
    private static final String LEAD_STATUS_ID = "leadStatusID";
    private static final String LEAD_RATING_ID = "leadRatingID";
    private static final String LEAD_RATING1 = "leadRating";
    private static final String SAASU_UID = "saasuUID";
    private static final String DEPARTMENT_ID = "departmentID";
    private static final String WORK_EXPERIENCE = "workExperience";
    private static final String CURRENT_EMPLOYER_ID = "currentEmployerID";
    private static final String EXPECTED_SALARY_ID = "expectedSalaryID";
    private static final String LOCATION_ID = "locationID";
    private static final String SKILLS_ID = "skillsID";
    private static final String VACANCY_ID = "vacancyID";
    private static final String CREATED_DATE_ID = "createdDateID";
    private static final String URL = "url";
    private static final String POSITION1 = "position";
    private static final String MAILING_LISTS = "mailingLists";
    private static final String EXAM_STATUS_ID = "examStatusId";
    private static final String EXAM_STATUS = "examStatus";
    private static final String CONTACT_IMAGE_URL = "contactImageUrl";
    private static final String CONTACT_IMAGE_ID = "contactImageID";
    private static final String WORK_EMAIL = "workEmail";
    private static final String ENTITY_CONTACT_ID = "entityContactID";
    private static final String HOME_WEB_SITE = "homeWebSite";
    private static final String WORK_WEB_SITE = "workWebSite";
    private static final String HOME_PAGE = "contacthomePage";
    private static final String FTP = "ftp";
    private static final String BLOG = "blog";
    private static final String PROFILE_WEB_SITE = "profileWebSite";
    private static final String OTHER_WEB_SITE = "otherWebSite";
    private static final String LINKEDIN_WEB_SITE = "linkedinWebSite";
    private static final String FACEBOOK_WEB_SITE = "facebookWebSite";
    private static final String TWITTER_WEB_SITE = "twitterWebSite";
    private static final String INSTAGRAM_WEB_SITE = "instagramWebSite";
    private static final String HOME_EMAIL = "homeEmail";
    private static final String OTHER_EMAIL = "otherEmail";
    private static final String EXTENSION = "extension";
    private static final String HOME_PHONE = "homePhone";
    private static final String OTHER_PHONE = "otherPhone";
    private static final String HOME_FAX = "homeFax";
    private static final String WORK_PHONE = "workPhone";
    private static final String WORK_FAX = "workFax";
    private static final String MOBILE1 = "mobile";
    private static final String PAGER = "pager";
    private static final String WHATS_APP = "whatsApp";
    private static final String TELEGRAM = "telegram";
    private static final String VIBER = "viber";
    private static final String SKYPE = "skype";
    private static final String QQ = "QQ";
    private static final String MSN = "MSN";
    private static final String ICQ = "ICQ";
    private static final String JABBER = "jabber";
    private static final String G_TALK = "gTalk";
    private static final String AIM = "AIM";
    private static final String YAHOO = "yahoo";
    private static final String WORK_PHONE_ID = "workPhoneId";
    private static final String HOME_PHONE_ID = "homePhoneId";
    private static final String OTHER_PHONE_ID = "otherPhoneId";
    private static final String HOME_EMAIL_ID = "homeEmailId";
    private static final String MOBILE_ID = "mobileId";
    private static final String EXTENSION_ID = "extensionId";
    private static final String HOME_FAX_ID = "homeFaxId";
    private static final String SHOW_ACCOUNT_ADDRESS = "showAccountAddress";
    private static final String EMAIL_TRACKER_ID = "emailTrackerId";
    private static final String PDF_LIMIT = "pdfLimit";
    private static final String EXCEL_LIMIT = "excelLimit";
    public static final String CREATED_BY = "created_by";
    public static final String UPDATED_BY = "updated_by";

    private HashSet<Integer> trackerIDSet;

    private String martialStatus;
    private SelectItem[] martialStatusList;
    private Integer martialStatusId;

    private HashMap<String, String> valueMap = null;
    private HashMap<String, ArrayList<String>> stringListMap = new HashMap<>();
    private boolean hasOfficeToken;
    private ArrayList<SpokenLanguageItem> spokingLanguages;
    private LinkedHashMap<Integer, String> immCodes;
    private SelectItem[] templates;
    private Integer selectedSubStageId;
    private boolean draggable;
    private boolean allowEdit;
    private Date eventStartDate;
    private Date eventEndDate;
    private SelectItem departmentItem;
    private SelectItem timeSlotItem;
    private SelectItem positionItem;
    private SelectItem creator;
    private String passportNumber;

    private ArrayList<String> getStringList(String key) {
        stringListMap.computeIfAbsent(key, k -> new ArrayList<>());
        return stringListMap.get(key);
    }

    private void addStringList(String key, ArrayList<String> value) {
        stringListMap.put(key, value);
    }

    protected HashMap<String, String> getInstance() {
        return valueMap = valueMap == null ? new HashMap<>() : valueMap;
    }

    private SelectItem[] titleList;

    //    private String contactImageUrl;
//    private Integer entityContactID;
    //new addings...
    private TreeSelectItem[] categories;
    private ContactCategoryListItem[] categoryListItems;
    private ArrayList<SelectItem> selectedCategories;

//    private String assets;

    private SelectItem[] emails;
    private SelectItem[] phoneNumbers;
    private SelectItem[] address;
    private SelectItem[] imAddress;
    private SelectItem[] webSites;
    private SelectItem[] countries;
    private SelectItem[] cities;
    private SelectItem[] states;
    private SelectItem[] postCods;

    private String primaryEmail;  //need for getting contact's single email address
    private Address primaryAddress;  //need for getting contact's single address
    private String primaryPhone;  //need for getting contact's single phone


    private DateNonConvertable birthDate;

    private ArrayList<Address> addresses = new ArrayList<>();

    private HistoryList history;

    private String gender;

    private FileItem[] attachments;

    private HistoryList allHistory;
    private Integer contactType = CRM_CONTACT;

    // this field is for all relations. beware on using this field
    private SelectItem[] relationships;
    private ArrayList<SelectItem> relationshipList;
    //this field is only for selected relations.
    private ArrayList<SelectItem> selectedRelationships;
    private ArrayList<SelectItem> selectedContactImAddress;
    private ArrayList<SelectItem> telegramChats;
    //not null only on Summary view...
    private PermissionHolder permissionForEntireUser;

    private Integer categoryID;//for import only...
    private boolean categoryFromFile;//for import only...
    private boolean campaignFromFile;//for import only...
    private boolean assigneeFromFile;//for import only...
    private boolean ownerFromFile;//for import only...

    private boolean checkForDuplicates = false;
    private boolean isCandaidateNewFromApi = false;

    private int editablePermission;
    private SelectItem[] pmDepartmentItems;
    private SelectItem[] departments;

    private String candidateLocation;

    //leadInformation
    private SelectItem[] leadAssignees;
    private SelectItem[] leadSources;

    private SelectItem[] leadStatuses;
    private ReferenceItem leadStatus;
    private String nationality;

    private SelectItem[] leadRatings;

    //Contact
    private SelectItem[] contactImAddress;

    //Company Information
    private CrmAccountItem crmAccount;
    private boolean isClientContact;
    private boolean isCallModal;

    private ArrayList<CompanyCustomFieldItem> customFields;
    private HashMap<String, Object> customFieldsMap;
    private boolean addingFromWebForms = false;

    private Integer clientContactId;
    private boolean isAccessEnabled;
    private boolean isActive;
    private boolean primaryContact;
    private String accessStatus;

    private long syncID;

    private ArrayList<Integer> subscriptionIDs;
    private ArrayList<HistoryListItem> notes;

    //CANDIDATE FIELDS BEGIN
    private NumberData numberData;
    private SelectItem candidateStatus;
    private SelectItem[] locations;
    private SelectItem preferredLocation;
    private ArrayList<SelectItem> vacancies;
    private ArrayList<VacancyItem> vacancyItems;
    private String vacancy;
    //    private Integer examStatusId;
    private boolean hasPlacement;
    private Integer placementId;
    //CANDIDATE FIELDS END
    private SelectItem[] supervisors;
    private boolean isFromAPI = false;
    private SelectItem projectItem;
    private Integer profileImageID;
    private ArrayList<PaymentDeductionObject> allowanceCategories;
    private boolean nameNotUnique;
    private LinkedHashMap<String, FormProperty> formProperty;
    private OpportunityListItem opportunity;

    private boolean isFromOpportunityQuickAdd = false;
    private String accountIndustry;
    private EmployeeListItem employee;
    private ColumnConfigs[] customItemColumns;
    private HashMap<String, ArrayList<CustomTableRpc>> customTableItems = new HashMap<>();
    private String currency;
    private SelectItem productCategory;
    private SelectItem productBrand;
    private TaxItem taxItem;
    private BigDecimal taxAmount;
    private BigDecimal net;
    private BigDecimal subTotal;
    private TreeSelectItem[] productCategories;
    private SelectItem[] productBrands;
    private OpportunityItem[] opportunityItem;
    private BigDecimal exchangeRate;
    private Integer baseCurrencyID;
    private String baseCurrencyName;
    private Map<String, List<CustomTableRpc>> candidateCustomTableItems = new HashMap<>();
    private String candidateZoomLink;

    public OpportunityItem[] getItems() {
        return opportunityItem;
    }

    public void setItems(OpportunityItem[] items) {
        this.opportunityItem = items;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public SelectItem getProjectItem() {
        return projectItem;
    }

    public void setProjectItem(SelectItem projectItem) {
        this.projectItem = projectItem;
    }

    public ArrayList<PaymentDeductionObject> getAllowanceCategories() {
        if (allowanceCategories == null) {
            allowanceCategories = new ArrayList<>();
        }
        return allowanceCategories;
    }

    public void setAllowanceCategories(ArrayList<PaymentDeductionObject> allowanceCategories) {
        this.allowanceCategories = allowanceCategories;
    }

    public ArrayList<HistoryListItem> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<HistoryListItem> notes) {
        this.notes = notes;
    }

    //FOR DEVICE SYNC
//    private String deviceID;
//    private String deviceContactID;
//    private String status;
//    private String examStatus;

    public String getDeviceID() {
        return getString(DEVICE_ID);
    }

    public void setDeviceID(String deviceID) {
        addString(DEVICE_ID, deviceID);
    }

    public String getDeviceContactID() {
        return getString(DEVICE_CONTACT_ID);
    }

    public void setDeviceContactID(String deviceContactID) {
        addString(DEVICE_CONTACT_ID, deviceContactID);
    }

    public String getStatus() {
        return getString(STATUS);
    }

    public void setStatus(String status) {
        addString(STATUS, status);
    }

    public SelectItem getDepartmentItem() {
        return departmentItem;
    }

    public void setDepartmentItem(SelectItem departmentItem) {
        this.departmentItem = departmentItem;
    }

    public SelectItem getPositionItem() {
        return positionItem;
    }

    public void setPositionItem(SelectItem positionItem) {
        this.positionItem = positionItem;
    }

    public String getAccessStatus() {
        return accessStatus;
    }

    public void setAccessStatus(String access) {
        this.accessStatus = access;
    }

    public Integer getCompanyPhotoId() {
        return getInteger(COMPANY_PHOTO_ID);
    }

    public void setCompanyPhotoId(Integer companyPhotoId) {
        addInteger(COMPANY_PHOTO_ID, companyPhotoId);
    }

    public Integer getPhotoId() {
        return getInteger(PHOTO_ID);
    }

    public void setPhotoId(Integer photoId) {
        addInteger(PHOTO_ID, photoId);
    }

    public SelectItem[] getLeadAssignees() {
        return leadAssignees;
    }

    public String getCompanyPhotoUrl() {
        return getString(COMPANY_PHOTO_URL);
    }

    public void setCompanyPhotoUrl(String companyPhotoUrl) {
        addString(COMPANY_PHOTO_URL, companyPhotoUrl);
    }

    public void setLeadAssignees(SelectItem[] leadAssignees) {
        this.leadAssignees = leadAssignees;
    }

    public Integer getLeadAssigneeID() {
        return getInteger(LEAD_ASSIGNEE_ID);
    }

    public void setLeadAssigneeID(Integer leadAssigneeID) {
        addInteger(LEAD_ASSIGNEE_ID, leadAssigneeID);
    }

    public String getLeadAssignee() {
        return getString(LEAD_ASSIGNEE1);
    }

    public void setLeadAssignee(String leadAssignee) {
        addString(LEAD_ASSIGNEE1, leadAssignee);
    }

    public Integer getLeadBackupAssigneeID() {
        return getInteger(LEAD_BACKUP_ASSIGNEE_ID);
    }

    public void setLeadBackupAssigneeID(Integer leadBackupAssigneeID) {
        addInteger(LEAD_BACKUP_ASSIGNEE_ID, leadBackupAssigneeID);
    }

    public String getLeadBackupAssignee() {
        return getString(LEAD_BACKUP_ASSIGNEE1);
    }

    public void setLeadBackupAssignee(String leadBackupAssignee) {
        addString(LEAD_BACKUP_ASSIGNEE1, leadBackupAssignee);
    }

    public SelectItem[] getLeadSources() {
        return leadSources;
    }

    public void setLeadSources(SelectItem[] leadSources) {
        this.leadSources = leadSources;
    }

    public SelectItem[] getCandidateSources() {
        return getLeadSources();
    }

    public void setCandidateSources(SelectItem[] sources) {
        setLeadSources(sources);
    }

    public SelectItem[] getCandidateStatuses() {
        return getLeadStatuses();
    }

    public void setCandidateStatuses(SelectItem[] statuses) {
        setLeadStatuses(statuses);
    }

    public Integer getLeadSourceID() {
        return getInteger(LEAD_SOURCE_ID);
    }

    public void setLeadSourceID(Integer leadSourceID) {
        addInteger(LEAD_SOURCE_ID, leadSourceID);
    }

    public Integer getLeadStatusID() {
        return getInteger(LEAD_STATUS_ID);
    }

    public void setLeadStatusID(Integer leadStatusID) {
        addInteger(LEAD_STATUS_ID, leadStatusID);
    }

    public String getLeadSource() {
        return getString(LEAD_SOURCE1);
    }

    public void setLeadSource(String leadSource) {
        addString(LEAD_SOURCE1, leadSource);
    }

    public String getOtherLeadSource() {
        return getString(OTHER_LEAD_SOURCE);
    }

    public void setOtherLeadSource(String otherLeadSource) {
        addString(OTHER_LEAD_SOURCE, otherLeadSource);
    }

    public SelectItem[] getLeadStatuses() {
        return leadStatuses;
    }

    public void setLeadStatuses(SelectItem[] leadStatuses) {
        this.leadStatuses = leadStatuses;
    }

    public SelectItem[] getContactImAddress() {
        return contactImAddress;
    }

    public void setContactImAddress(SelectItem[] contactImAddress) {
        this.contactImAddress = contactImAddress;
    }

    public ReferenceItem getLeadStatus(boolean... notNull) {
        return leadStatus == null && notNull != null && notNull.length > 0 && notNull[0] ? new ReferenceItem() : leadStatus;
    }

    public void setLeadStatus(ReferenceItem leadStatus) {
        this.leadStatus = leadStatus;
    }

    public void setLeadStatus(SelectItem leadStatus) {
        this.leadStatus = leadStatus == null ? null : (leadStatus instanceof ReferenceItem ? ((ReferenceItem) leadStatus) : new ReferenceItem(leadStatus.getId(), leadStatus.getName()));
    }

    public SelectItem[] getLeadRatings() {
        return leadRatings;
    }

    public void setLeadRatings(SelectItem[] leadRatings) {
        this.leadRatings = leadRatings;
    }

    public Integer getLeadRatingID() {
        return getInteger(LEAD_RATING_ID);
    }

    public void setLeadRatingID(Integer leadRatingID) {
        addInteger(LEAD_RATING_ID, leadRatingID);
    }

    public String getLeadRating() {
        return getString(LEAD_RATING1);
    }

    public void setLeadRating(String leadRating) {
        addString(LEAD_RATING1, leadRating);
    }

    public Integer getJobTitleId() {
        return getInteger(JOB_TITLE_ID);
    }

    public void setJobTitleId(Integer jobTitleId) {
        addInteger(JOB_TITLE_ID, jobTitleId);
    }

    public Integer getDepartmentID() {
        return getInteger(DEPARTMENT_ID);
    }

    public void setDepartmentID(Integer departmentID) {
        addInteger(DEPARTMENT_ID, departmentID);
    }

    public SelectItem[] getDepartments() {
        return departments;
    }

    public void setDepartments(SelectItem[] departments) {
        this.departments = departments;
    }

    public String getAssets() {
        return getString(ASSETS);
    }

    public void setAssets(String assets) {
        addString(ASSETS, assets);
    }

    public TreeSelectItem[] getCategories() {
        return categories;
    }

    public void setCategories(TreeSelectItem[] categories) {
        this.categories = categories;
    }

    public ArrayList<SelectItem> getSelectedCategories() {
        if (selectedCategories == null) {
            selectedCategories = new ArrayList<>();
        }
        return selectedCategories;
    }

    public void setSelectedCategories(ArrayList<SelectItem> selectedCategories) {
        this.selectedCategories = selectedCategories;
    }

    public void addSelectedCategories(SelectItem... categories) {
        if (categories != null) {
            for (SelectItem category : categories) {
                if (category != null && !getSelectedCategories().contains(category)) {
                    getSelectedCategories().add(category);
                }
            }
        }
    }

    public String getContactImageUrl() {
        return getString(CONTACT_IMAGE_URL);
    }

    public void setContactImageUrl(String contactImageUrl) {
        addString(CONTACT_IMAGE_URL, contactImageUrl);
    }

    public Integer getContactImageID() {
        return getInteger(CONTACT_IMAGE_ID);
    }

    public void setContactImageID(Integer contactImageID) {
        addInteger(CONTACT_IMAGE_ID, contactImageID);
    }

    public HistoryList getAllHistory() {
        return allHistory;
    }

    public void setAllHistory(HistoryList allHistory) {
        this.allHistory = allHistory;
    }

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public String getNote() {
        return getString(NOTE);
    }

    public void setNote(String note) {
        addString(NOTE, note);
    }

    public HistoryList getHistory() {
        return history;
    }

    public void setHistory(HistoryList history) {
        this.history = history;
    }

    public Integer getObjectId() {
        return getInteger(OBJECT_ID);
    }

    public void setObjectId(Integer objectId) {
        addInteger(OBJECT_ID, objectId);
    }

    public String getGoogleId() {
        return getString(GOOGLE_ID);
    }

    public void setGoogleId(String googleId) {
        addString(GOOGLE_ID, googleId);
    }

    public String getContactName() {
        return getString(CONTACT_NAME);
    }

    public void setContactName(String contactName) {
        addString(CONTACT_NAME, contactName);
    }

    public String getOwner() {
        return getString(OWNER);
    }

    public void setOwner(String owner) {
        addString(OWNER, owner);
    }

    public void setCreator(String creator) {
        addString(CREATOR, creator);
    }

    public String getCreator() {
        return getString(CREATOR);
    }

    public String getUpdater() {
        return getString(UPDATER);
    }

    public void setUpdater(String updater) {
        addString(UPDATER, updater);
    }

    public String getOtherName() {
        return getString(OTHER_NAME);
    }

    public void setOtherName(String otherName) {
        addString(OTHER_NAME, otherName);
    }

    public ArrayList<String> getHomeWebSite() {
        return getStringList(HOME_WEB_SITE);
    }

    public void setHomeWebSite(ArrayList<String> homeWebSite) {
        addStringList(HOME_WEB_SITE, homeWebSite);
    }

    public ArrayList<String> getWorkWebSite() {
        return getStringList(WORK_WEB_SITE);
    }

    public void setWorkWebSite(ArrayList<String> workWebSite) {
        addStringList(WORK_WEB_SITE, workWebSite);
    }

    public ArrayList<String> getHomePage() {
        return getStringList(HOME_PAGE);
    }

    public void setHomePage(ArrayList<String> homePage) {
        addStringList(HOME_PAGE, homePage);
    }

    public ArrayList<String> getFtp() {
        return getStringList(FTP);
    }

    public void setFtp(ArrayList<String> ftp) {
        addStringList(FTP, ftp);
    }

    public ArrayList<String> getBlog() {
        return getStringList(BLOG);
    }

    public void setBlog(ArrayList<String> blog) {
        addStringList(BLOG, blog);
    }

    public ArrayList<String> getProfileWebSite() {
        return getStringList(PROFILE_WEB_SITE);
    }

    public void setProfileWebSite(ArrayList<String> profileWebSite) {
        addStringList(PROFILE_WEB_SITE, profileWebSite);
    }

    public ArrayList<String> getOtherWebSite() {
        return getStringList(OTHER_WEB_SITE);
    }

    public void setOtherWebSite(ArrayList<String> otherWebSite) {
        addStringList(OTHER_WEB_SITE, otherWebSite);
    }

    public ArrayList<String> getLinkedinWebSite() {
        return getStringList(LINKEDIN_WEB_SITE);
    }

    public void setLinkedinWebSite(ArrayList<String> linkedinWebSite) {
        addStringList(LINKEDIN_WEB_SITE, linkedinWebSite);
    }

    public ArrayList<String> getFacebookWebSite() {
        return getStringList(FACEBOOK_WEB_SITE);
    }

    public void setFacebookWebSite(ArrayList<String> facebook) {
        addStringList(FACEBOOK_WEB_SITE, facebook);
    }

    public ArrayList<String> getTwitterWebSite() {
        return getStringList(TWITTER_WEB_SITE);
    }

    public void setTwitterWebSite(ArrayList<String> twitterWebSite) {
        addStringList(TWITTER_WEB_SITE, twitterWebSite);
    }

    public ArrayList<String> getInstagramWebSite() {
        return getStringList(INSTAGRAM_WEB_SITE);
    }

    public void setInstagramWebSite(ArrayList<String> instagramWebSite) {
        addStringList(INSTAGRAM_WEB_SITE, instagramWebSite);
    }

    public ArrayList<String> getHomeEmail() {
        return getStringList(HOME_EMAIL);
    }

    public void setHomeEmail(ArrayList<String> homeEmail) {
        addStringList(HOME_EMAIL, homeEmail);
    }

    public void setHomeEmail(String homeEmail) {
        getHomeEmail().add(homeEmail);
    }

    public ArrayList<String> getWorkEmail() {
        return getStringList(WORK_EMAIL);
    }

    public void setWorkEmail(ArrayList<String> workEmail) {
        addStringList(WORK_EMAIL, workEmail);
    }

    public static HashMap<Integer, HashMap<Integer, ArrayList<String>>> getAllItemParamsAsMap(ContactListItem item) {
        HashMap<Integer, HashMap<Integer, ArrayList<String>>> itemParams = new HashMap<>();
        HashMap<Integer, ArrayList<String>> itemParam = new HashMap<>();
        if (item != null) {
            //Emails... Constants.CONTACT_EMAILS...
            itemParam.put(Constants.G_HOME, item.getHomeEmail());
            itemParam.put(Constants.G_WORK, item.getWorkEmail());
            itemParam.put(Constants.G_OTHER, item.getOtherEmail());
            itemParams.put(Constants.CONTACT_EMAILS, itemParam);//emails set
            //Phones... Constants.CONTACT_PHONES
            itemParam = new HashMap<>();
            itemParam.put(Constants.G_HOME, item.getHomePhone());
            itemParam.put(Constants.G_WORK, item.getWorkPhone());
            itemParam.put(Constants.G_HOME_FAX, item.getHomeFax());
            itemParam.put(Constants.G_WORK_FAX, item.getWorkFax());
            itemParam.put(Constants.G_MOBILE, item.getMobile());
            itemParam.put(Constants.G_PAGER, item.getPager());
            itemParam.put(Constants.G_OTHER, item.getOtherPhone());
            itemParam.put(Constants.G_EXTENSION, item.getExtension());
            itemParam.put(Constants.G_FAX, item.getFax());
            itemParam.put(Constants.G_WHATS_APP, item.getWhatsApp());
            itemParam.put(Constants.G_TELEGRAM, item.getTelegram());
            itemParam.put(Constants.G_VIBER, item.getViber());

            itemParams.put(Constants.CONTACT_PHONES, itemParam);
            //WebAddresses... Constants.CONTACT_WEBSITES
            itemParam = new HashMap<>();
            itemParam.put(Constants.G_HOME, item.getHomeWebSite());
            itemParam.put(Constants.G_WORK, item.getWorkWebSite());
            itemParam.put(Constants.G_HOME_PAGE, item.getHomePage());
            itemParam.put(Constants.G_FTP, item.getFtp());
            itemParam.put(Constants.G_BLOG, item.getBlog());
            itemParam.put(Constants.G_PROFILE, item.getProfileWebSite());
            itemParam.put(Constants.G_OTHER, item.getOtherWebSite());
            itemParam.put(Constants.G_LINKEDIN, item.getLinkedinWebSite());
            itemParam.put(Constants.G_FACEBOOK, item.getFacebookWebSite());
            itemParam.put(Constants.G_TWITTER, item.getTwitterWebSite());
            itemParam.put(Constants.G_INSTAGRAM, item.getInstagramWebSite());
            itemParams.put(Constants.CONTACT_WEBSITES, itemParam);
            //Contact relations .... Constants.CONTACT_RELATIONSHIPS
            itemParam = new HashMap<>();
            for (SelectItem relationship : item.getSelectedRelationships()) {
                if (itemParam.containsKey(relationship.getId())) {
                    itemParam.get(relationship.getId()).add(relationship.getDescription());
                } else {
                    ArrayList<String> list = new ArrayList<>();
                    list.add(relationship.getDescription());
                    itemParam.put(relationship.getId(), list);
                }
            }
            itemParams.put(Constants.CONTACT_RELATIONSHIPS, itemParam);
            //Contact relations .... Constants.CONTACT_IMADDRESSES
            itemParam = new LinkedHashMap<>();
            for (SelectItem imAddress : item.getSelectedContactImAddress()) {
                if (itemParam.containsKey(imAddress.getId())) {
                    itemParam.get(imAddress.getId()).add(imAddress.getDescription());
                } else {
                    ArrayList<String> list = new ArrayList<>();
                    list.add(imAddress.getDescription());
                    itemParam.put(imAddress.getId(), list);
                }
            }
            itemParams.put(Constants.CONTACT_IMADDRESSES, itemParam);
            if (item.getTelegramChats() != null && !item.getTelegramChats().isEmpty()) {
                //Contact telegram chats .... Constants.CONTACT_TELEGRAMS
                itemParam = new LinkedHashMap<>();
                for (SelectItem chat : item.getTelegramChats()) {
                    ArrayList<String> list = new ArrayList<>();
                    list.add(chat.getEntityId() != null ? chat.getEntityId().toString() : "");
                    itemParam.put(chat.getId(), list);
                }
                itemParams.put(Constants.CONTACT_TELEGRAMS, itemParam);
            }
        }
        return itemParams;
    }

    public ArrayList<String> getOtherEmail() {
        return getStringList(OTHER_EMAIL);
    }

    public void setOtherEmail(ArrayList<String> otherEmail) {
        addStringList(OTHER_EMAIL, otherEmail);
    }

    public void setOtherEmail(String otherEmail) {
        getOtherEmail().add(otherEmail);
    }


    public Integer getOwnerId() {
        return getInteger(OWNER_ID);
    }

    public void setOwnerId(Integer ownerId) {
        addInteger(OWNER_ID, ownerId);
    }

    public String getFirstName() {
        return getString(FIRST_NAME);
    }

    public void setFirstName(String firstName) {
        addString(FIRST_NAME, firstName);
    }

    public String getLastName() {
        return getString(LAST_NAME);
    }

    public void setLastName(String lastName) {
        addString(LAST_NAME, lastName);
    }

    public CrmAccountItem getCrmAccount() {
        if (crmAccount == null) {
            crmAccount = new CrmAccountItem();
        }
        return crmAccount;
    }

    public void setCrmAccount(CrmAccountItem crmAccount) {
        this.crmAccount = crmAccount;
    }

    public String getTitle() {
        return getString(TITLE);
    }

    public void setTitle(String title) {
        addString(TITLE, title);
    }

    public SelectItem[] getTitleList() {
        return titleList;
    }

    public void setTitleList(SelectItem[] titleList) {
        this.titleList = titleList;
    }

    public String getDepartment() {
        return getString(DEPARTMENT1);
    }

    public void setDepartment(String department) {
        addString(DEPARTMENT1, department);
    }

    public Integer getCampaignId() {
        return getInteger(CAMPAIGN_ID);
    }

    public void setCampaignId(Integer campaignId) {
        addInteger(CAMPAIGN_ID, campaignId);
    }

    public String getCampaign() {
        return getString(CAMPAIGN1);
    }

    public void setCampaign(String campaign) {
        addString(CAMPAIGN1, campaign);
    }

    public void setCampaignSI(SelectItem campaign) {
        if (campaign != null) {
            this.setCampaignId(campaign.getId());
            if (this.getCampaignId() != null && this.getCampaignId() > 0) {
                this.setCampaign(campaign.getName());
            }
        } else {
            this.setCampaignId(null);
            this.setCampaign(null);
        }
    }

    public ArrayList<String> getWorkPhone() {
        return getStringList(WORK_PHONE);
    }

    public void setWorkPhone(ArrayList<String> workPhone) {
        addStringList(WORK_PHONE, workPhone);
    }

    public void setWorkPhone(String workPhone) {
        getWorkPhone().add(workPhone);
    }

    public ArrayList<String> getHomePhone() {
        return getStringList(HOME_PHONE);
    }

    public void setHomePhone(ArrayList<String> homePhone) {
        addStringList(HOME_PHONE, homePhone);
    }

    public void setHomePhone(String homePhone) {
        getHomePhone().add(homePhone);
    }

    public ArrayList<String> getOtherPhone() {
        return getStringList(OTHER_PHONE);
    }

    public void setOtherPhone(ArrayList<String> otherPhone) {
        addStringList(OTHER_PHONE, otherPhone);
    }

    public void setOtherPhone(String otherPhone) {
        getOtherPhone().add(otherPhone);
    }

    public ArrayList<String> getHomeFax() {
        return getStringList(HOME_FAX);
    }

    public void setHomeFax(ArrayList<String> homeFax) {
        addStringList(HOME_FAX, homeFax);
    }

    public ArrayList<String> getFax() {
        return getStringList(FAX);
    }

    public void setFax(ArrayList<String> fax) {
        addStringList(FAX, fax);
    }

    public void setFax(String fax) {
        getFax().add(fax);
    }

    public void setHomeFax(String homeFax) {
        getHomeFax().add(homeFax);
    }

    public ArrayList<String> getWorkFax() {
        return getStringList(WORK_FAX);
    }

    public void setWorkFax(ArrayList<String> workFax) {
        addStringList(WORK_FAX, workFax);
    }

    public void setWorkFax(String workFax) {
        getWorkFax().add(workFax);
    }

    public ArrayList<String> getMobile() {
        return getStringList(MOBILE1);
    }

    public void setMobile(ArrayList<String> mobile) {
        addStringList(MOBILE1, mobile);
    }

    public void setMobile(String mobile) {
        getMobile().add(mobile);
    }

    public ArrayList<String> getPager() {
        return getStringList(PAGER);
    }

    public void setPager(ArrayList<String> pager) {
        addStringList(PAGER, pager);
    }

    public void setPager(String pager) {
        getPager().add(pager);
    }

    public ArrayList<String> getWhatsApp() {
        return getStringList(WHATS_APP);
    }

    public void setWhatsApp(ArrayList<String> whatsApp) {
        addStringList(WHATS_APP, whatsApp);
    }

    public void setWhatsApp(String whatsApp) {
        getWhatsApp().add(whatsApp);
    }


    public ArrayList<String> getTelegram() {
        return getStringList(TELEGRAM);
    }

    public void setTelegram(ArrayList<String> telegram) {
        addStringList(TELEGRAM, telegram);
    }

    public void setTelegram(String telegram) {
        getTelegram().add(telegram);
    }


    public ArrayList<String> getViber() {
        return getStringList(VIBER);
    }

    public void setViber(ArrayList<String> viber) {
        addStringList(VIBER, viber);
    }

    public void setViber(String viber) {
        getViber().add(viber);
    }

    public DateNonConvertable getBirthDate() {
        return birthDate;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public SelectItem[] getMartialStatusList() {
        return martialStatusList;
    }

    public void setMartialStatusList(SelectItem[] martialStatusList) {
        this.martialStatusList = martialStatusList;
    }

    public Integer getMartialStatusId() {
        return martialStatusId;
    }

    public void setMartialStatusId(Integer martialStatusId) {
        this.martialStatusId = martialStatusId;
    }

    public String getMartialStatus() {
        return martialStatus;
    }

    public void setMartialStatus(String martialStatus) {
        this.martialStatus = martialStatus;

    }

    public void setBirthDate(DateNonConvertable birthDate) {
        this.birthDate = birthDate;
    }

    public String getReportsTo() {
        return getString(REPORTS_TO1);
    }

    public void setReportsTo(String reportsTo) {
        addString(REPORTS_TO1, reportsTo);
    }

    public boolean isEmailOptOut() {
        return getBool(EMAIL_OPT_OUT);
    }

    public void setEmailOptOut(boolean emailOptOut) {
        addBool(EMAIL_OPT_OUT, emailOptOut);
    }

    public SelectItem[] getEmails() {
        return emails;
    }

    public void setEmails(SelectItem[] emails) {
        this.emails = emails;
    }

    public SelectItem[] getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(SelectItem[] phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public SelectItem[] getAddress() {
        return address;
    }

    public void setAddress(SelectItem[] address) {
        this.address = address;
    }

    public SelectItem[] getWebSites() {
        return webSites;
    }

    public void setWebSites(SelectItem[] webSites) {
        this.webSites = webSites;
    }

    public SelectItem[] getImAddress() {
        return imAddress;
    }

    public void setImAddress(SelectItem[] imAddress) {
        this.imAddress = imAddress;
    }

    public SelectItem[] getCountries() {
        return countries;
    }

    public void setCountries(SelectItem[] countries) {
        this.countries = countries;
    }

    public SelectItem[] getCities() {
        return cities;
    }

    public void setCities(SelectItem[] cities) {
        this.cities = cities;
    }

    public SelectItem[] getPostCods() {
        return postCods;
    }

    public void setPostCods(SelectItem[] postCods) {
        this.postCods = postCods;
    }

    public String getAddress2b() {
        return getString(ADDRESS_2_B);
    }

    public void setAddress2b(String address2b) {
        addString(ADDRESS_2_B, address2b);
    }

    public String getAddress3() {
        return getString(ADDRESS_3);
    }

    public void setAddress3(String address3) {
        addString(ADDRESS_3, address3);
    }

    public SelectItem[] getStates() {
        return states;
    }

    public void setStates(SelectItem[] states) {
        this.states = states;
    }

    public Integer getFirstNameId() {
        return getInteger(FIRST_NAME_ID);
    }

    public void setFirstNameId(Integer firstNameId) {
        addInteger(FIRST_NAME_ID, firstNameId);
    }

    public Integer getLastNameId() {
        return getInteger(LAST_NAME_ID);
    }

    public void setLastNameId(Integer lastNameId) {
        addInteger(LAST_NAME_ID, lastNameId);
    }

    public Integer getTitleId() {
        return getInteger(TITLE_ID);
    }

    public void setTitleId(Integer titleId) {
        addInteger(TITLE_ID, titleId);
    }

    public Integer getHomeWebSiteId() {
        return getInteger(HOME_WEB_SITE_ID);
    }

    public void setHomeWebSiteId(Integer homeWebSiteId) {
        addInteger(HOME_WEB_SITE_ID, homeWebSiteId);
    }

    public Integer getWorkPhoneId() {
        return getInteger(WORK_PHONE_ID);
    }

    public void setWorkPhoneId(Integer workPhoneId) {
        addInteger(WORK_PHONE_ID, workPhoneId);
    }

    public Integer getHomePhoneId() {
        return getInteger(HOME_PHONE_ID);
    }

    public void setHomePhoneId(Integer homePhoneId) {
        addInteger(HOME_PHONE_ID, homePhoneId);
    }

    public Integer getOtherPhoneId() {
        return getInteger(OTHER_PHONE_ID);
    }

    public void setOtherPhoneId(Integer otherPhoneId) {
        addInteger(OTHER_PHONE_ID, otherPhoneId);
    }

    public ArrayList<String> getExtension() {
        return getStringList(EXTENSION);
    }

    public void setExtension(ArrayList<String> extension) {
        addStringList(EXTENSION, extension);
    }

    public void setExtension(String extension) {
        getExtension().add(extension);
    }

    public Integer getExtensionId() {
        return getInteger(EXTENSION_ID);
    }

    public void setExtensionId(Integer extensionId) {
        addInteger(EXTENSION_ID, extensionId);
    }

    public Integer getHomeFaxId() {
        return getInteger(HOME_FAX_ID);
    }

    public void setHomeFaxId(Integer homeFaxId) {
        addInteger(HOME_FAX_ID, homeFaxId);
    }

    public Integer getMobileId() {
        return getInteger(MOBILE_ID);
    }

    public void setMobileId(Integer mobileId) {
        addInteger(MOBILE_ID, mobileId);
    }

    public Integer getHomeEmailId() {
        return getInteger(HOME_EMAIL_ID);
    }

    public void setHomeEmailId(Integer homeEmailId) {
        addInteger(HOME_EMAIL_ID, homeEmailId);
    }

    public String getPrimaryEmail() {
        return primaryEmail;
    }

    public void setPrimaryEmail(String primaryEmail) {
        this.primaryEmail = primaryEmail;
    }

    public Integer getBirthDateId() {
        return getInteger(BIRTH_DATE_ID);
    }

    public void setBirthDateId(Integer birthDateId) {
        addInteger(BIRTH_DATE_ID, birthDateId);
    }

    public Integer getReportsToId() {
        return getInteger(REPORTS_TO_ID);
    }

    public void setReportsToId(Integer reportsToId) {
        addInteger(REPORTS_TO_ID, reportsToId);
    }

    public Integer getEmailOptOutId() {
        return getInteger(EMAIL_OPT_OUT_ID);
    }

    public void setEmailOptOutId(Integer emailOptOutId) {
        addInteger(EMAIL_OPT_OUT_ID, emailOptOutId);
    }

    public Integer getHomeAddressId() {
        return getInteger(HOME_ADDRESS_ID);
    }

    public void setHomeAddressId(Integer homeAddressId) {
        addInteger(HOME_ADDRESS_ID, homeAddressId);
    }

    public Integer getAddress2Id() {
        return getInteger(ADDRESS_2_ID);
    }

    public void setAddress2Id(Integer address2Id) {
        addInteger(ADDRESS_2_ID, address2Id);
    }

    public String getJobTitle() {
        return getString(JOB_TITLE1);
    }

    public void setJobTitle(String jobTitle) {
        addString(JOB_TITLE1, jobTitle);
    }

    public String getMiddleName() {
        return getString(MIDDLE_NAME);
    }

    public void setMiddleName(String middleName) {
        addString(MIDDLE_NAME, middleName);
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getJobFunction() {
        return getString(JOB_FUNCTION);
    }

    public void setJobFunction(String jobFunction) {
        addString(JOB_FUNCTION, jobFunction);
    }

    public Integer getJobFunctionId() {
        return getInteger(JOB_FUNCTION_ID);
    }

    public void setJobFunctionId(Integer jobFunctionId) {
        addInteger(JOB_FUNCTION_ID, jobFunctionId);
    }

    public Integer getEntityID() {
        return getInteger(ENTITY_ID);
    }

    public void setEntityID(Integer entityID) {
        addInteger(ENTITY_ID, entityID);
    }

    public int getEditablePermission() {
        return editablePermission;
    }

    public void setEditablePermission(int editablePermission) {
        this.editablePermission = editablePermission;
    }

    public Integer getPmDepartmentID() {
        return getInteger(PM_DEPARTMENT_ID);
    }

    public void setPmDepartmentID(Integer pmDepartmentID) {
        addInteger(PM_DEPARTMENT_ID, pmDepartmentID);
    }

    public SelectItem[] getPmDepartmentItems() {
        return pmDepartmentItems;
    }

    public void setPmDepartmentItems(SelectItem[] pmDepartmentItems) {
        this.pmDepartmentItems = pmDepartmentItems;
    }

    public Double getWageRate() {
        return getDouble(WAGE_RATE);
    }

    public void setWageRate(Double wageRate) {
        addDouble(WAGE_RATE, wageRate);
    }

    public Double getClientChargeRate() {
        return getDouble(CLIENT_CHARGE_RATE);
    }

    public void setClientChargeRate(Double clientChargeRate) {
        addDouble(CLIENT_CHARGE_RATE, clientChargeRate);
    }

    public String getCreatedFrom() {
        return getString(CREATED_FROM);
    }

    public void setCreatedFrom(String createdFrom) {
        addString(CREATED_FROM, createdFrom);
    }

    public Integer getContactType() {
        return contactType;
    }

    public void setContactType(Integer contactType) {
        this.contactType = contactType;
    }

    public ArrayList<String> getgTalk() {
        return getStringList(G_TALK);
    }

    public void setgTalk(ArrayList<String> gTalk) {
        addStringList(G_TALK, gTalk);
    }

    public ArrayList<String> getAIM() {
        return getStringList(AIM);
    }

    public void setAIM(ArrayList<String> AIM) {
        addStringList(ContactListItem.AIM, AIM);
    }

    public ArrayList<String> getYahoo() {
        return getStringList(YAHOO);
    }

    public void setYahoo(ArrayList<String> yahoo) {
        addStringList(YAHOO, yahoo);
    }

    public ArrayList<String> getSkype() {
        return getStringList(SKYPE);
    }

    public void setSkype(ArrayList<String> skype) {
        addStringList(SKYPE, skype);
    }

    public ArrayList<String> getQQ() {
        return getStringList(QQ);
    }

    public void setQQ(ArrayList<String> QQ) {
        addStringList(ContactListItem.QQ, QQ);
    }

    public ArrayList<String> getMSN() {
        return getStringList(MSN);
    }

    public void setMSN(ArrayList<String> MSN) {
        addStringList(ContactListItem.MSN, MSN);
    }

    public ArrayList<String> getICQ() {
        return getStringList(ICQ);
    }

    public void setICQ(ArrayList<String> ICQ) {
        addStringList(ContactListItem.ICQ, ICQ);
    }

    public ArrayList<String> getJabber() {
        return getStringList(JABBER);
    }

    public void setJabber(ArrayList<String> jabber) {
        addStringList(JABBER, jabber);
    }

    public ArrayList<Address> getAddresses() {
        return getAddresses(false);
    }

    public ArrayList<Address> getAddresses(boolean addOneItemIfNothingFound) {
        if (addresses.isEmpty() && addOneItemIfNothingFound) {
            addresses.add(new Address());
        }
        return addresses;
    }

    public static ArrayList<Address> getAddresses(ArrayList<Address> addresses, int relation) {
        ArrayList<Address> newAddresses = new ArrayList<>();
        if (addresses != null && addresses.size() > 0) {
            for (Address address : addresses) {
                if (address != null && address.getRelationType() != null && address.getRelationType().equals(relation)) {
                    newAddresses.add(address);
                }
            }
            return newAddresses;
        }
        return null;
    }

    public static Address getFirstAddress(ArrayList<Address> addresses, int relation, boolean ifNotExistReturnFirst) {
        if (addresses != null && addresses.size() > 0) {
            for (Address address : addresses) {
                if (address != null && address.getRelationType() != null && address.getRelationType().equals(relation)) {
                    return address;
                }
            }
            return ifNotExistReturnFirst ? addresses.get(0) : null;
        }
        return null;
    }

    public void setAddresses(ArrayList<Address> addresses) {
        this.addresses = addresses;
    }

    public boolean isSupplierContact() {
        if (contactType != null) {
            return contactType.equals(SUPPLIER_CONTACT);
        }
        return false;
    }

    public boolean isClientContact() {
        return isClientContact;
    }

    public void setClientContact(boolean isClientContact) {
        this.isClientContact = isClientContact;
    }

    public boolean isCallModal() {
        return this.isCallModal;
    }

    public void setCallModal(final boolean callModal) {
        this.isCallModal = callModal;
    }

    public boolean isCrmContact() {
        if (contactType != null) {
            return contactType.equals(CRM_CONTACT);
        }
        return false;
    }


    public boolean isEmployeeContact() {
        if (contactType != null) {
            return contactType.equals(EMPLOYEE_CONTACT);
        }
        return false;
    }

    public boolean isStudentContact() {
        if (contactType != null) {
            return contactType.equals(STUDENT_CONTACT);
        }
        return false;
    }

    public boolean isLeadContact() {
        if (contactType != null) {
            return contactType.equals(LEAD_CONTACT);
        }
        return false;
    }

    public boolean isCandidate() {
        if (contactType != null) {
            return contactType.equals(CANDIDATE);
        }
        return false;
    }

    public Integer getIndex() {
        return getInteger(INDEX);
    }

    public void setIndex(Integer index) {
        addInteger(INDEX, index);
    }

    public Address getPrimaryAddress() {
        return getPrimaryAddress(false);
    }

    public Address getPrimaryAddress(boolean doNotReturnNull) {
        if (primaryAddress == null && doNotReturnNull) {
            return new Address();
        }
        return primaryAddress;
    }

    public void setPrimaryAddress(Address primaryAddress) {
        this.primaryAddress = primaryAddress;
    }

    public String getPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(String primaryPhone) {
        this.primaryPhone = primaryPhone;
    }

    public Date getUpdatedDate() {
        return getDate(UPDATED_DATE);
    }

    public void setUpdatedDate(Date updatedDate) {
        addDate(UPDATED_DATE, updatedDate, true);
    }

    public Date getDeptStartDate() {
        return getDate(DEPT_START_DATE);
    }

    public void setDeptStartDate(Date deptStartDate) {
        addDate(DEPT_START_DATE, deptStartDate, true);
    }

    public Date getCreatedDate() {
        return getDate(CREATED_DATE);
    }

    public void setCreatedDate(Date createdDate) {
        addDate(CREATED_DATE, createdDate, true);
    }

    public boolean isHasToken() {
        return getBool(HAS_TOKEN);
    }

    public void setHasToken(boolean hasToken) {
        addBool(HAS_TOKEN, hasToken);
    }

    public Integer getMiddleNameId() {
        return getInteger(MIDDLE_NAME_ID);
    }

    public void setMiddleNameId(Integer middleNameId) {
        addInteger(MIDDLE_NAME_ID, middleNameId);
    }

    public Integer getOtherNameId() {
        return getInteger(OTHER_NAME_ID);
    }

    public void setOtherNameId(Integer otherNameId) {
        addInteger(OTHER_NAME_ID, otherNameId);
    }

    public boolean getShowAccountAddress() {
        return getBool(SHOW_ACCOUNT_ADDRESS);
    }

    public void setShowAccountAddress(boolean showAccountAddress) {
        addBool(SHOW_ACCOUNT_ADDRESS, showAccountAddress);
    }

    public SelectItem[] getRelationships() {
        return relationships;
    }

    public void setRelationships(SelectItem[] relationships) {
        this.relationships = relationships;
    }

    public ArrayList<SelectItem> getRelationshipList() {
        if (relationshipList == null) {
            relationshipList = new ArrayList<>();
        }
        return relationshipList;
    }

    public void setRelationshipList(ArrayList<SelectItem> relationshipList) {
        this.relationshipList = relationshipList;
    }

    public ArrayList<SelectItem> getSelectedRelationships() {
        if (selectedRelationships == null) {
            selectedRelationships = new ArrayList<>();
        }
        return selectedRelationships;
    }

    public void setSelectedRelationships(ArrayList<SelectItem> selectedRelationships) {
        this.selectedRelationships = selectedRelationships;
    }

    public ArrayList<SelectItem> getSelectedContactImAddress() {
        if (selectedContactImAddress == null) {
            selectedContactImAddress = new ArrayList<>();
        }
        return selectedContactImAddress;
    }

    public void setSelectedContactImAddress(ArrayList<SelectItem> selectedContactImAddress) {
        this.selectedContactImAddress = selectedContactImAddress;
    }

    public void setSelectedContactImAddress(HashMap<Integer, ArrayList<String>> imAddresses) {
        if (imAddresses != null && imAddresses.size() > 0) {
            setSelectedContactImAddress((ArrayList<SelectItem>) null);
            for (HashMap.Entry<Integer, ArrayList<String>> entry : imAddresses.entrySet()) {
                Integer id = entry.getKey();
                for (String value : entry.getValue()) {
                    addSelectedImAddresses(new SelectItem(id, "", value));
                }
            }
        }
    }

    public void addSelectedImAddresses(SelectItem... imAddresses) {
        if (imAddresses != null) {
            for (SelectItem imAddress : imAddresses) {
                getSelectedContactImAddress().add(imAddress);
            }
        }
    }

    public PermissionHolder getPermissionForEntireUser() {
        return permissionForEntireUser;
    }

    public void setPermissionForEntireUser(PermissionHolder permissionForEntireUser) {
        this.permissionForEntireUser = permissionForEntireUser;
    }

    public void setSelectedRelationships(HashMap<Integer, ArrayList<String>> relationShips) {
        if (relationShips != null && relationShips.size() > 0) {
            setSelectedRelationships((ArrayList<SelectItem>) null);
            for (HashMap.Entry<Integer, ArrayList<String>> entry : relationShips.entrySet()) {
                Integer ID = entry.getKey();
                for (String value : entry.getValue()) {
                    addSelectedRelationships(new SelectItem(ID, "", value));
                }
            }
        }
    }

    public void addSelectedRelationships(SelectItem... relationships) {
        if (relationships != null) {
            for (SelectItem relation : relationships) {
                getSelectedRelationships().add(relation);
            }
        }
    }

    public String getName() {
        if (getContactName() != null) {
            return getContactName();
        } else {
            if (getFirstName() == null && getLastName() == null && "".equals(getFirstName()) && "".equals(getLastName())) {
                setContactName("");
                return getContactName();
            } else {
                setContactName("" + (getTitle() != null ? getTitle() : "") + " " + (getFirstName() != null ? getFirstName() : "") + " " + (getLastName() != null ? getLastName() : ""));
                return getContactName();
            }
        }
    }

    public String getNameWithTitle() {
        return (getTitle() != null ? getTitle() + " " : "") + (getFirstName() != null ? getFirstName() + " " : "") + (getLastName() != null ? getLastName() : "") + (getMiddleName() != null ? " " + getMiddleName() + " " : "");
    }

    public void setEmails(HashMap<Integer, ArrayList<String>> emails) {
        if (emails != null) {
            this.setHomeEmail(emails.get(Constants.G_HOME));
            this.setWorkEmail(emails.get(Constants.G_WORK));
            this.setOtherEmail(emails.get(Constants.G_OTHER));
        }
    }

    public void setPhones(Map<Integer, ArrayList<String>> phones) {
        if (phones != null) {
            this.setHomePhone(phones.get(Constants.G_HOME));
            this.setWorkPhone(phones.get(Constants.G_WORK));
            this.setMobile(phones.get(Constants.G_MOBILE));
            this.setHomeFax(phones.get(Constants.G_HOME_FAX));
            this.setWorkFax(phones.get(Constants.G_WORK_FAX));
            this.setPager(phones.get(Constants.G_PAGER));
            this.setOtherPhone(phones.get(Constants.G_OTHER));
            this.setExtension(phones.get(Constants.G_EXTENSION));
            this.setFax(phones.get(Constants.G_FAX));
            this.setWhatsApp(phones.get(Constants.G_WHATS_APP));
            this.setTelegram(phones.get(Constants.G_TELEGRAM));
            this.setViber(phones.get(Constants.G_VIBER));
        }
    }

    public void setImAddresses(HashMap<Integer, ArrayList<String>> imAddresses) {
        if (imAddresses != null) {
            this.setgTalk(imAddresses.get(Constants.G_GOOGLE_TALK));
            this.setAIM(imAddresses.get(Constants.G_AIM));
            this.setYahoo(imAddresses.get(Constants.G_YAHOO));
            this.setSkype(imAddresses.get(Constants.G_SKYPE));
            this.setQQ(imAddresses.get(Constants.G_QQ));
            this.setMSN(imAddresses.get(Constants.G_MSN));
            this.setICQ(imAddresses.get(Constants.G_ICQ));
            this.setJabber(imAddresses.get(Constants.G_JABBER));
        }
    }

    public void setWebAddresses(HashMap<Integer, ArrayList<String>> webAddresses) {
        if (webAddresses != null) {
            this.setHomeWebSite(webAddresses.get(Constants.G_HOME));
            this.setWorkWebSite(webAddresses.get(Constants.G_WORK));
            this.setHomePage(webAddresses.get(Constants.G_HOME_PAGE));
            this.setFtp(webAddresses.get(Constants.G_FTP));
            this.setBlog(webAddresses.get(Constants.G_BLOG));
            this.setProfileWebSite(webAddresses.get(Constants.G_PROFILE));
            this.setOtherWebSite(webAddresses.get(Constants.G_OTHER));
            this.setLinkedinWebSite(webAddresses.get(Constants.G_LINKEDIN));
            this.setFacebookWebSite(webAddresses.get(Constants.G_FACEBOOK));
            this.setTwitterWebSite(webAddresses.get(Constants.G_TWITTER));
            this.setInstagramWebSite(webAddresses.get(Constants.G_INSTAGRAM));
        }
    }

    public void setEmails(ArrayList<String>... emails) {
        this.setHomeEmail(emails != null && emails.length > 0 ? emails[0] : new ArrayList<>());
        this.setWorkEmail(emails != null && emails.length > 0 ? emails[0] : new ArrayList<>());
        this.setOtherEmail(emails != null && emails.length > 0 ? emails[0] : new ArrayList<>());
    }

    public void setPhones(ArrayList<String>... phones) {
        this.setHomePhone(phones != null && phones.length > 0 ? phones[0] : new ArrayList<>());
        this.setWorkPhone(phones != null && phones.length > 0 ? phones[0] : new ArrayList<>());
        this.setMobile(phones != null && phones.length > 0 ? phones[0] : new ArrayList<>());
        this.setFax(phones != null && phones.length > 0 ? phones[0] : new ArrayList<>());
        this.setWhatsApp(phones != null && phones.length > 0 ? phones[0] : new ArrayList<>());
        this.setTelegram(phones != null && phones.length > 0 ? phones[0] : new ArrayList<>());
        this.setViber(phones != null && phones.length > 0 ? phones[0] : new ArrayList<>());
    }

    public void setImAddresses(ArrayList<String>... imAddresses) {
        this.setgTalk(imAddresses != null && imAddresses.length > 0 ? imAddresses[0] : new ArrayList<>());
        this.setAIM(imAddresses != null && imAddresses.length > 0 ? imAddresses[0] : new ArrayList<>());
        this.setYahoo(imAddresses != null && imAddresses.length > 0 ? imAddresses[0] : new ArrayList<>());
        this.setSkype(imAddresses != null && imAddresses.length > 0 ? imAddresses[0] : new ArrayList<>());
        this.setQQ(imAddresses != null && imAddresses.length > 0 ? imAddresses[0] : new ArrayList<>());
        this.setMSN(imAddresses != null && imAddresses.length > 0 ? imAddresses[0] : new ArrayList<>());
        this.setICQ(imAddresses != null && imAddresses.length > 0 ? imAddresses[0] : new ArrayList<>());
        this.setJabber(imAddresses != null && imAddresses.length > 0 ? imAddresses[0] : new ArrayList<>());
    }

    public void setWebAddresses(ArrayList<String>... webAddresses) {
        this.setHomeWebSite(webAddresses != null && webAddresses.length > 0 ? webAddresses[0] : new ArrayList<>());
        this.setWorkWebSite(webAddresses != null && webAddresses.length > 0 ? webAddresses[0] : new ArrayList<>());
        this.setHomePage(webAddresses != null && webAddresses.length > 0 ? webAddresses[0] : new ArrayList<>());
        this.setFtp(webAddresses != null && webAddresses.length > 0 ? webAddresses[0] : new ArrayList<>());
        this.setBlog(webAddresses != null && webAddresses.length > 0 ? webAddresses[0] : new ArrayList<>());
        this.setProfileWebSite(webAddresses != null && webAddresses.length > 0 ? webAddresses[0] : new ArrayList<>());
        this.setOtherWebSite(webAddresses != null && webAddresses.length > 0 ? webAddresses[0] : new ArrayList<>());
        this.setLinkedinWebSite(webAddresses != null && webAddresses.length > 0 ? webAddresses[0] : new ArrayList<>());
        this.setFacebookWebSite(webAddresses != null && webAddresses.length > 0 ? webAddresses[0] : new ArrayList<>());
        this.setTwitterWebSite(webAddresses != null && webAddresses.length > 0 ? webAddresses[0] : new ArrayList<>());
        this.setInstagramWebSite(webAddresses != null && webAddresses.length > 0 ? webAddresses[0] : new ArrayList<>());
    }

    public void setEntityContactID(Integer entityContactID) {
        addInteger(ENTITY_CONTACT_ID, entityContactID);
    }

    public Integer getEntityContactID() {
        return getInteger(ENTITY_CONTACT_ID);
    }

    public void setWorkEmail(String workEmail) {
        getWorkEmail().add(workEmail);
    }

    public static HashMap<Integer, ArrayList<String>> getItemParamsAsMap(ContactListItem item, int param) {
        HashMap<Integer, ArrayList<String>> itemParam = new HashMap<>();
        if (item != null) {
            switch (param) {
                case Constants.CONTACT_EMAILS:
                    itemParam.put(Constants.G_HOME, item.getHomeEmail());
                    itemParam.put(Constants.G_WORK, item.getWorkEmail());
                    itemParam.put(Constants.G_OTHER, item.getOtherEmail());
                    break;
                case Constants.CONTACT_PHONES:
                    itemParam.put(Constants.G_HOME, item.getHomePhone());
                    itemParam.put(Constants.G_WORK, item.getWorkPhone());
                    itemParam.put(Constants.G_HOME_FAX, item.getHomeFax());
                    itemParam.put(Constants.G_WORK_FAX, item.getWorkFax());
                    itemParam.put(Constants.G_MOBILE, item.getMobile());
                    itemParam.put(Constants.G_PAGER, item.getPager());
                    itemParam.put(Constants.G_OTHER, item.getOtherPhone());
                    itemParam.put(Constants.G_EXTENSION, item.getExtension());
                    itemParam.put(Constants.G_FAX, item.getFax());
                    itemParam.put(Constants.G_WHATS_APP, item.getWhatsApp());
                    itemParam.put(Constants.G_TELEGRAM, item.getTelegram());
                    itemParam.put(Constants.G_VIBER, item.getViber());
                    break;
                case Constants.CONTACT_IMADDRESSES:
                    itemParam.put(Constants.G_GOOGLE_TALK, item.getgTalk());
                    itemParam.put(Constants.G_AIM, item.getAIM());
                    itemParam.put(Constants.G_YAHOO, item.getYahoo());
                    itemParam.put(Constants.G_SKYPE, item.getSkype());
                    itemParam.put(Constants.G_QQ, item.getQQ());
                    itemParam.put(Constants.G_MSN, item.getMSN());
                    itemParam.put(Constants.G_ICQ, item.getICQ());
                    itemParam.put(Constants.G_JABBER, item.getJabber());
                    break;
                case Constants.CONTACT_WEBSITES:
                    itemParam.put(Constants.G_HOME, item.getHomeWebSite());
                    itemParam.put(Constants.G_WORK, item.getWorkWebSite());
                    itemParam.put(Constants.G_HOME_PAGE, item.getHomePage());
                    itemParam.put(Constants.G_FTP, item.getFtp());
                    itemParam.put(Constants.G_BLOG, item.getBlog());
                    itemParam.put(Constants.G_PROFILE, item.getProfileWebSite());
                    itemParam.put(Constants.G_OTHER, item.getOtherWebSite());
                    itemParam.put(Constants.G_LINKEDIN, item.getLinkedinWebSite());
                    itemParam.put(Constants.G_FACEBOOK, item.getFacebookWebSite());
                    itemParam.put(Constants.G_TWITTER, item.getTwitterWebSite());
                    itemParam.put(Constants.G_INSTAGRAM, item.getInstagramWebSite());
                    break;
            }
        }
        return itemParam;
    }

    public boolean isItemParamsEmpty(int param) {
        HashMap<Integer, ArrayList<String>> itemParams = getItemParamsAsMap(this, param);
        boolean empty = true;
        if (itemParams != null && itemParams.size() > 0) {
            outerLoop:
            for (ArrayList<String> values : itemParams.values()) {
                if (values == null || values.size() == 0) {
                    continue;
                }
                for (String value : values) {
                    if (value != null && !"".equals(value.trim())) {
                        empty = false;
                        break outerLoop;
                    }
                }
            }
        }
        return empty;
    }

    public HashSet<String> getAllPhones() {
        HashSet<String> allPhones = new HashSet<>();
        allPhones.addAll(getHomePhone());
        allPhones.addAll(getWorkPhone());
        allPhones.addAll(getMobile());
        allPhones.addAll(getHomeFax());
        allPhones.addAll(getWorkFax());
        allPhones.addAll(getPager());
        allPhones.addAll(getOtherPhone());
        allPhones.addAll(getWhatsApp());
        allPhones.addAll(getTelegram());
        allPhones.addAll(getViber());
        allPhones.addAll(getFax());
        allPhones.addAll(getExtension());
        return allPhones;
    }

    public void removeAllPhones() {
        getHomePhone().clear();
        getWorkPhone().clear();
        getMobile().clear();
        getHomeFax().clear();
        getWorkFax().clear();
        getPager().clear();
        getOtherPhone().clear();
        getWhatsApp().clear();
        getTelegram().clear();
        getViber().clear();
        getFax().clear();
        getExtension().clear();
    }

    public HashMap<String, ArrayList<String>> getAllPhonesAsMap() {
        HashMap<String, ArrayList<String>> allPhones = new HashMap<>();
        allPhones.put("HOME", getHomePhone());
        allPhones.put("WORK", getWorkPhone());
        allPhones.put("MOBILE", getMobile());
        allPhones.put("FAX", getFax());
        allPhones.put("TELEGRAM", getTelegram());
        allPhones.put("VIBER", getViber());
        allPhones.put("WHATSAPP", getWhatsApp());

        return allPhones;
    }

    public void addParam(Integer param, Integer relation, String value, String... code) {
        if (param != null && relation != null && value != null && !"".equals(value.trim())) {
            if (param.equals(Constants.CONTACT_PHONES)) {
                addPhone(relation, value);
            } else if (param.equals(Constants.CONTACT_EMAILS)) {
                addEmail(relation, value);
            } else if (param.equals(Constants.CONTACT_WEBSITES)) {
                addWebSite(relation, value);
            } else if (param.equals(Constants.CONTACT_IMADDRESSES)) {
                addImAddress(relation, value);
            }


        }
    }

    public void addWebSite(int relation, String value) {
        if (value != null && !"".equals(value)) {
            if (relation == WebAddressReference.HOME.getId()) {
                getHomeWebSite().add(value);
            } else {
                if (relation == WebAddressReference.WORK.getId()) {
                    getWorkWebSite().add(value);
                } else {
                    if (relation == WebAddressReference.HOMEPAGE.getId()) {
                        getHomePage().add(value);
                    } else {
                        if (relation == WebAddressReference.FTP.getId()) {
                            getFtp().add(value);
                        } else {
                            if (relation == WebAddressReference.BLOG.getId()) {
                                getBlog().add(value);
                            } else {
                                if (relation == WebAddressReference.PROFILE.getId()) {
                                    getProfileWebSite().add(value);
                                } else {
                                    if (relation == WebAddressReference.OTHER.getId()) {
                                        getOtherWebSite().add(value);
                                    } else {
                                        if (relation == WebAddressReference.LINKEDIN.getId()) {
                                            getLinkedinWebSite().add(value);
                                        } else {
                                            if (relation == WebAddressReference.FACEBOOK.getId()) {
                                                getFacebookWebSite().add(value);
                                            } else {
                                                if (relation == WebAddressReference.TWITTER.getId()) {
                                                    getTwitterWebSite().add(value);
                                                } else {
                                                    if (relation == WebAddressReference.INSTAGRAM.getId()) {
                                                        getInstagramWebSite().add(value);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void addImAddress(Integer relation, String value) {
        if (immCodes != null && immCodes.size() > 0)
            if (immCodes.get(relation).equals(Constants.GTALK)) {
                getgTalk().add(value);
            } else if (immCodes.get(relation).equals(Constants.AIM)) {
                getAIM().add(value);
            } else if (immCodes.get(relation).equals(Constants.YAHOO)) {
                getYahoo().add(value);
            } else if (immCodes.get(relation).equals(Constants.SKYPE)) {
                getSkype().add(value);
            } else if (immCodes.get(relation).equals(Constants.QQ)) {
                getQQ().add(value);
            } else if (immCodes.get(relation).equals(Constants.MSN)) {
                getMSN().add(value);
            } else if (immCodes.get(relation).equals(Constants.JABBER)) {
                getJabber().add(value);
            }
    }


    public void addEmail(Integer relation, String value) {
        if (relation == AddressReference.HOME.getId()) {
            getHomeEmail().add(value);
        } else {
            if (relation == AddressReference.WORK.getId()) {
                getWorkEmail().add(value);
            } else {
                if (relation == AddressReference.OTHER.getId()) {
                    getOtherEmail().add(value);
                }
            }
        }
    }

    public void addPhone(Integer relation, String value) {
        switch (relation) {
            case Constants.G_WORK:
                getWorkPhone().add(value);
                break;
            case Constants.G_HOME:
                getHomePhone().add(value);
                break;
            case Constants.G_MOBILE:
                getMobile().add(value);
                break;
            case Constants.G_FAX:
                getFax().add(value);
                break;
            case Constants.G_WHATS_APP:
                getWhatsApp().add(value);
                break;
            case Constants.G_TELEGRAM:
                getTelegram().add(value);
                break;
            case Constants.G_VIBER:
                getViber().add(value);
                break;
            case Constants.G_HOME_FAX:
                getHomeFax().add(value);
                break;
            case Constants.G_WORK_FAX:
                getWorkFax().add(value);
                break;
            case Constants.G_PAGER:
                getPager().add(value);
                break;
            case Constants.G_OTHER:
                getOtherPhone().add(value);
                break;
            case Constants.G_EXTENSION:
                getExtension().add(value);
                break;
        }
        /*if (relation == PhoneReference.HOME.getId()) {
            getHomePhone().add(value);
        } else {
            if (relation == PhoneReference.WORK.getId()) {
                getWorkPhone().add(value);
            } else {
                if (relation == PhoneReference.MOBILE.getId()) {
                    getMobile().add(value);
                } else {
                    if (relation == PhoneReference.HOMEFAX.getId()) {
                        getHomeFax().add(value);
                    } else {
                        if (relation == PhoneReference.WORKFAX.getId()) {
                            getWorkFax().add(value);
                        } else {
                            if (relation == PhoneReference.PAGER.getId()) {
                                getPager().add(value);
                            } else {
                                if (relation == PhoneReference.OTHER.getId()) {
                                    getOtherPhone().add(value);
                                } else {
                                    if (relation == PhoneReference.EXTENSION.getId()) {
                                        getExtension().add(value);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }*/
    }

    public Integer getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(Integer categoryID) {
        this.categoryID = categoryID;
    }

    public boolean isCategoryFromFile() {
        return categoryFromFile;
    }

    public void setCategoryFromFile(boolean categoryFromFile) {
        this.categoryFromFile = categoryFromFile;
    }

    public boolean isCampaignFromFile() {
        return campaignFromFile;
    }

    public void setCampaignFromFile(boolean campaignFromFile) {
        this.campaignFromFile = campaignFromFile;
    }

    public boolean isAssigneeFromFile() {
        return assigneeFromFile;
    }

    public void setAssigneeFromFile(boolean assigneeFromFile) {
        this.assigneeFromFile = assigneeFromFile;
    }

    public boolean isOwnerFromFile() {
        return ownerFromFile;
    }

    public void setOwnerFromFile(boolean ownerFromFile) {
        this.ownerFromFile = ownerFromFile;
    }

    public boolean isCheckForDuplicates() {
        return checkForDuplicates;
    }

    public void setCheckForDuplicates(boolean checkForDuplicates) {
        this.checkForDuplicates = checkForDuplicates;
    }

    public String getCategoryNames() {
        return getString(CATEGORY_NAMES);
    }

    public void setCategoryNames(String categoryNames) {
        addString(CATEGORY_NAMES, categoryNames);
    }

    public String getBackgroundInformation() {
        return getString(BACKGROUND_INFORMATION);
    }

    public void setBackgroundInformation(String backgroundInformation) {
        addString(BACKGROUND_INFORMATION, backgroundInformation);
    }

    public boolean isAccessEnabled() {
        return isAccessEnabled;
    }

    public void setAccessEnabled(boolean accessEnabled) {
        isAccessEnabled = accessEnabled;
    }

    public Integer getClientContactId() {
        return clientContactId;
    }

    public void setClientContactId(Integer clientContactId) {
        this.clientContactId = clientContactId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isPrimaryContact() {
        return primaryContact;
    }

    public void setPrimaryContact(boolean primaryContact) {
        this.primaryContact = primaryContact;
    }

    public String getDisclaimer() {
        return getString(DISCLAIMER);
    }

    public void setDisclaimer(String disclaimer) {
        addString(DISCLAIMER, disclaimer);
    }

    public long getSyncID() {
        return syncID;
    }

    public void setSyncID(long syncID) {
        this.syncID = syncID;
    }

    public static boolean hasTokenAndGoogleIDAndOwner(ArrayList<ContactListItem> items, Integer ownerID) {
        if (items != null && items.size() > 0) {
            for (ContactListItem item : items) {
                if ((item.getOwnerId() != null && item.getOwnerId().equals(ownerID)) && (item.isHasToken() || item.isHasOfficeToken()) && item.getGoogleId() != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public static SelectItem[] asSelectItems(ContactListItem[] contactListItems) {
        if (contactListItems == null || contactListItems.length == 0) {
            return new SelectItem[0];
        } else {
            SelectItem[] result = new SelectItem[contactListItems.length];
            int i = 0;
            for (ContactListItem contactListItem : contactListItems) {
                if (contactListItem != null) {
                    result[i++] = contactListItem.asSelectItem();
                }
            }
            return result;
        }
    }

    private SelectItem asSelectItem() {
        SelectItem item = new SelectItem();
        item.setName(getEmptyIfNull(getFirstName()) + " " + getEmptyIfNull(getLastName()));
        item.setId(getObjectId());
        item.setDescription(getPrimaryEmail());
        return item;
    }

    private String getEmptyIfNull(String value) {
        return value == null ? "" : value;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(ArrayList<CompanyCustomFieldItem> customFields) {
        this.customFields = customFields;
        if (customFields != null && customFields.size() > 0) {
            for (CompanyCustomFieldItem customField : customFields) {
                if ((customField.getFieldStringValue() != null && !"".equals(customField.getFieldStringValue())) || (customField.getFieldDateNonConvertedValue() != null)) {
                    if (customField.getColumnCode() != null) {
                        Object value = null;
                        if ((customField.getDataType().equals(CompanyCustomFieldItem.TEXT) || customField.getDataType().equals(CompanyCustomFieldItem.NUMBER)) && customField.getFieldStringValue() != null) {
                            try {
                                value = customField.getDataType().equals(CompanyCustomFieldItem.TEXT) ? customField.getFieldStringValue() : Double.valueOf(customField.getFieldStringValue());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            if (customField.getDataType().equals(CompanyCustomFieldItem.DATE) && customField.getFieldDateNonConvertedValue() != null) {
                                value = customField.getFieldDateNonConvertedValue().getNonConvertedDate();
                            }
                        }
                        if (value != null) {
                            getCustomFieldsMap().put(customField.getColumnCode(), value);
                        }
                    }
                }
            }
        }
    }

    public HashMap<String, Object> getCustomFieldsMap() {
        if (customFieldsMap == null) {
            customFieldsMap = new HashMap<>();
        }
        return customFieldsMap;
    }

    public void setCustomFieldsMap(HashMap<String, Object> customFieldsMap) {
        this.customFieldsMap = customFieldsMap;
    }

    public boolean isAddingFromWebForms() {
        return addingFromWebForms;
    }

    public void setAddingFromWebForms(boolean addingFromWebForms) {
        this.addingFromWebForms = addingFromWebForms;
    }

    public Object getCustomFieldsValue(String columnCodeKey) {
        return getCustomFieldsMap().get(columnCodeKey);
    }


    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {
        getCustomFieldsMap().put(columnCodeKey, cellValue);
    }

    public static final ArrayList<String> allContactColumnNames = new ArrayList<>(Arrays.asList(
            ContactListItem.CONTACT_NAME,
            ContactListItem.CRM_ACCOUNT,
            ContactListItem.PHONE,
            ContactListItem.EMAIL,
            ContactListItem.TITLE,
            ContactListItem.COUNTRY,
            ContactListItem.OWNER,
            ContactListItem.DEPARTMENT,
            ContactListItem.POSITION,
            ContactListItem.DATE_OF_BIRTH,
            ContactListItem.REPORTS_TO,
            ContactListItem.CAMPAIGN,
            ContactListItem.EMAIL_ALLOWED,
            ContactListItem.CATEGORIES
    ));

    public static final ArrayList<String> defaultContactColumnNames = new ArrayList<>(Arrays.asList(
            ContactListItem.CONTACT_NAME,
            ContactListItem.EMAIL,
            ContactListItem.PHONE,
            ContactListItem.CRM_ACCOUNT,
            ContactListItem.CATEGORIES
    ));

    public static final ArrayList<String> defaultCandidateColumnNames = new ArrayList<>(Arrays.asList(
            ContactListItem.CONTACT_NAME,
            ContactListItem.LEAD_STATUS,
            ContactListItem.EMAIL,
            ContactListItem.PHONE,
            ContactListItem.LEAD_SOURCE
    ));

    public static final ArrayList<String> defaultLeadColumnNames = new ArrayList<>(Arrays.asList(
            ContactListItem.CONTACT_NAME,
            ContactListItem.CRM_ACCOUNT,
            ContactListItem.EMAIL,
            ContactListItem.PHONE,
            ContactListItem.LEAD_STATUS,
            ContactListItem.LEAD_SOURCE,
            ContactListItem.LEAD_ASSIGNEE
    ));

    public void setItemParams(HashMap<Integer, HashMap<Integer, ArrayList<String>>> params) {
        if (params != null && params.size() > 0) {
            setEmails(params.get(Constants.CONTACT_EMAILS));
            setPhones(params.get(Constants.CONTACT_PHONES));
            setImAddresses(params.get(Constants.CONTACT_IMADDRESSES));
            setWebAddresses(params.get(Constants.CONTACT_WEBSITES));
        }
    }

    public ArrayList<Integer> getSubscriptionIDs() {
        return subscriptionIDs;
    }

    public void setSubscriptionIDs(ArrayList<Integer> subscriptionIDs) {
        this.subscriptionIDs = subscriptionIDs;
    }

    public void setContactCategoryListItems(ContactCategoryListItem[] contactCategoryListItems) {
        this.categoryListItems = contactCategoryListItems;
    }

    public ContactCategoryListItem[] getContactCategoryListItem() {
        return categoryListItems;
    }

    public String getSaasuUID() {
        return getString(SAASU_UID);
    }

    public void setSaasuUID(String saasuUID) {
        addString(SAASU_UID, saasuUID);
    }

    //Candidate Fields


    public NumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
    }

    public SelectItem getCandidateSource() {
        return getLeadSourceID() != null || getLeadSource() != null ? new SelectItem(getLeadSourceID(), getLeadSource()) : null;
    }

    public void setCandidateSource(SelectItem source) {
        setLeadSourceID(source == null ? null : source.getId());
        setLeadSource(source == null ? null : source.getName());
    }

    public ReferenceItem getCandidateStatus() {
        return getLeadStatus();
    }

    public void setCandidateStatus(ReferenceItem candidateStatus) {
        setLeadStatus(candidateStatus);
    }

    public void setCandidateStatus(SelectItem candidateStatus) {
        setLeadStatus(candidateStatus);
    }

    public Integer getWorkExperience() {
        return getInteger(WORK_EXPERIENCE);
    }

    public void setWorkExperience(Integer workExperience) {
        addInteger(WORK_EXPERIENCE, workExperience);
    }

    public Integer getWorkExperienceMonthOrYear() {
        return getInteger(WORK_EXPERIENCE_MONTH_OR_YEAR);
    }

    public void setWorkExperienceMonthOrYear(Integer workExperienceMonthOrYear) {
        addInteger(WORK_EXPERIENCE_MONTH_OR_YEAR, workExperienceMonthOrYear);
    }

    public String getCurrentEmployer() {
        return getString(CURRENT_EMPLOYER);
    }

    public void setCurrentEmployer(String currentEmployer) {
        addString(CURRENT_EMPLOYER, currentEmployer);
    }

    public Double getExpectedSalary() {
        return getDouble(EXPECTED_SALARY);
    }

    public void setExpectedSalary(Double expectedSalary) {
        addDouble(EXPECTED_SALARY, expectedSalary);
    }

    public Double getStartSalary() {
        return getDouble(START_SALARY);
    }

    public void setStartSalary(Double startSalary) {
        addDouble(START_SALARY, startSalary);
    }






    public String getSkills() {
        return getString(SKILLS);
    }

    public void setSkills(String skills) {
        addString(SKILLS, skills);
    }

    public SelectItem[] getLocations() {
        return locations;
    }

    public void setLocations(SelectItem[] locations) {
        this.locations = locations;
    }

    public SelectItem getPreferredLocation() {
        return preferredLocation;
    }

    public void setPreferredLocation(SelectItem preferredLocation) {
        this.preferredLocation = preferredLocation;
    }

    public ArrayList<SelectItem> getVacancies() {
        return vacancies;
    }

    public void setVacancies(ArrayList<SelectItem> vacancies) {
        this.vacancies = vacancies;
    }

    public String getVacancy() {
        return vacancy;
    }

    public void setVacancy(String vacancy) {
        this.vacancy = vacancy;
    }

    public boolean isHasPlacement() {
        return hasPlacement;
    }

    public void setHasPlacement(boolean hasPlacement) {
        this.hasPlacement = hasPlacement;
    }

    public Integer getCurrentEmployerID() {
        return getInteger(CURRENT_EMPLOYER_ID);
    }

    public void setCurrentEmployerID(Integer currentEmployerID) {
        addInteger(CURRENT_EMPLOYER_ID, currentEmployerID);
    }

    public Integer getExpectedSalaryID() {
        return getInteger(EXPECTED_SALARY_ID);
    }

    public void setExpectedSalaryID(Integer expectedSalaryID) {
        addInteger(EXPECTED_SALARY_ID, expectedSalaryID);
    }

    public Integer getLocationID() {
        return getInteger(LOCATION_ID);
    }

    public void setLocationID(Integer locationID) {
        addInteger(LOCATION_ID, locationID);
    }

    public Integer getSkillsID() {
        return getInteger(SKILLS_ID);
    }

    public void setSkillsID(Integer skillsID) {
        addInteger(SKILLS_ID, skillsID);
    }

    public Integer getVacancyID() {
        return getInteger(VACANCY_ID);
    }

    public void setVacancyID(Integer vacancyID) {
        addInteger(VACANCY_ID, vacancyID);
    }

    public Integer getCreatedDateID() {
        return getInteger(CREATED_DATE_ID);
    }

    public void setCreatedDateID(Integer createdDateID) {
        addInteger(CREATED_DATE_ID, createdDateID);
    }

    public Integer getWebFormID() {
        return getInteger(WEB_FORM_ID);
    }

    public void setWebFormID(Integer webFormID) {
        addInteger(WEB_FORM_ID, webFormID);
    }

    public String getAntibot() {
        return getString(ANTIBOT);
    }

    public void setAntibot(String antibot) {
        addString(ANTIBOT, antibot);
    }

    @Override
    public String getKey() {
        return "" + getObjectId();
    }

    public static String getRelationTypeByContactType(Integer contactType) {
        if (contactType != null) {
            if (LEAD_CONTACT.equals(contactType)) {
                return RelationItem.TYPE_LEAD;
            } else if (CANDIDATE.equals(contactType)) {
                return RelationItem.TYPE_CANDIDATE;
            }
        }
        return RelationItem.TYPE_CONTACT;
    }

    public boolean isOtherLeadSourceIsSelected() {
        return Constants.OTHER.equals(getLeadSource()) && getOtherLeadSource() != null && !"".equals(getOtherLeadSource());
    }

    public String getExamStatus() {
        return getString(EXAM_STATUS);
    }

    public void setExamStatus(String examStatus) {
        addString(EXAM_STATUS, examStatus);
    }

    public Integer getExamStatusId() {
        return getInteger(EXAM_STATUS_ID);
    }

    public void setExamStatusId(Integer examStatusId) {
        addInteger(EXAM_STATUS_ID, examStatusId);
    }

    public String getRefIndNumber() {
        return getString(REF_IND_NUMBER);
    }

    public void setRefIndNumber(String refIndNumber) {
        addString(REF_IND_NUMBER, refIndNumber);
    }

    @Override
    public Integer getRelationID() {
        return getObjectId();
    }

    @Override
    public String getRelationType() {
        return isLeadContact() ? RelationItem.TYPE_LEAD : isCandidate() ? RelationItem.TYPE_CANDIDATE : RelationItem.TYPE_CONTACT;
    }

    @Override
    public String getRelationName() {
        return getName();
    }

    public String getPosition() {
        return getString(POSITION1);
    }

    public void setPosition(String position) {
        addString(POSITION1, position);
    }

    public String getUrl() {
        return getString(URL);
    }

    public void setUrl(String url) {
        addString(URL, url);
    }

    public SelectItem[] getSupervisors() {
        return supervisors;
    }

    public void setSupervisors(SelectItem[] supervisors) {
        this.supervisors = supervisors;
    }

    public boolean isFromAPI() {
        return isFromAPI;
    }

    public void setFromAPI(boolean isFromAPI) {
        this.isFromAPI = isFromAPI;
    }

    public String getMailingLists() {
        return getString(MAILING_LISTS);
    }

    public void setMailingLists(String mailingLists) {
        addString(MAILING_LISTS, mailingLists);
    }

    public String getPdfLimit() {
        return getString(PDF_LIMIT);
    }

    public void setPdfLimit(String pdfLimit) {
        addString(PDF_LIMIT, pdfLimit);
    }

    public String getExcelLimit() {
        return getString(EXCEL_LIMIT);
    }

    public void setExcelLimit(String excelLimit) {
        addString(EXCEL_LIMIT, excelLimit);
    }

    public HashSet<Integer> getTrackerIDSet() {
        return trackerIDSet;
    }

    public void setTrackerIDSet(HashSet<Integer> trackerID) {
        this.trackerIDSet = trackerID;
    }

    public void addTrackerId(Integer trackerId) {
        if (trackerIDSet == null) {
            trackerIDSet = new HashSet<>();
        }
        trackerIDSet.add(trackerId);
    }

    public static ArrayList<Integer> getIDsOnly(HashSet<ContactListItem> selectedItems) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (ContactListItem item : selectedItems) {
            ids.add(item.getObjectId());
        }
        return ids;
    }

    private SelectItem[] selectedMailingLists;

    public SelectItem[] getSelectedMailingLists() {
        return selectedMailingLists;
    }

    public void setSelectedMailingLists(SelectItem[] selectedMailingLists) {
        this.selectedMailingLists = selectedMailingLists;
    }

    public Long getKanbanorder() {
        return getLong(KANBAN_ORDER);
    }

    public void setKanbanorder(Long kanbanorder) {
        addLong(KANBAN_ORDER, kanbanorder);
    }

    public static ArrayList<MergeItem> getAsMergeContactItems(String field, HashMap<Integer, ContactListItem> contactItems) {
        ArrayList<MergeItem> items = new ArrayList<>();
        if (contactItems != null && contactItems.size() > 0) {
            for (HashMap.Entry<Integer, ContactListItem> item : contactItems.entrySet()) {
                if (item != null) {
                    items.add(item.getValue().getAsMergeItem(field));
                }
            }
        }
        return items;
    }

    private MergeItem getAsMergeItem(String field) {
        MergeItem item = new MergeItem(getObjectId());
        if (field != null) {
            ArrayList<CompanyCustomFieldItem> customFields = getCustomFields();
            if (customFields != null && customFields.size() > 0) {
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
            if (ContactListItem.CONTACT_NAME.equals(field)) {
                item.setId(null);
                item.setName(getName());
            } else if (ContactListItem.OWNER.equals(field)) {
                item.setId(getOwnerId());
                item.setName(getOwner());
            } else if (ContactListItem.FIRST_NAME.equals(field)) {
                item.setId(null);
                item.setName(getFirstName());
            } else if (ContactListItem.LAST_NAME.equals(field)) {
                item.setId(null);
                item.setName(getLastName());
            } else if (ContactListItem.EMAIL.equals(field)) {
                item.setId(null);
                item.setName(getPrimaryEmail());
            } else if (ContactListItem.PHONE.equals(field)) {
                item.setId(null);
                item.setName(getPrimaryPhone());
            } else if (ContactListItem.JOB_TITLE.equals(field)) {
                item.setId(null);
                item.setName(getJobTitle() != null && "".equals(getJobTitle()) ? null : getJobTitle());
            } else if (CrmAccountItem.ACCOUNT_NAME.equals(field)) {
                if (getCrmAccount() != null) {
                    item.setId(getCrmAccount().getObjectId());
                    item.setName(getCrmAccount().getName());
                }
            } else if (CrmAccountItem.ACCOUNT_TYPE.equals(field)) {
                item.setManyResults(true);
                if (getCrmAccount() != null && getCrmAccount().getAccountTypes() != null) {
                    getCrmAccount().getAccountTypes();
                    for (SelectItem type : getCrmAccount().getAccountTypes()) {
                        if (type != null && type.isSelected()) {
                            item.addChild(new MergeItem(getCrmAccount().getObjectId(), type.getId(), type.getName()));
                        }
                    }
                }
            } else if (CrmAccountItem.INDUSTRY.equals(field)) {
                if (getCrmAccount() != null && getCrmAccount().getIndustryID() != null) {
                    item.setId(getCrmAccount().getIndustryID());
                    item.setName(getCrmAccount().getIndustry());
                }
            } else if (ContactListItem.DEPARTMENT.equals(field)) {
                item.setId(null);
                item.setName(getDepartment() != null && "".equals(getDepartment()) ? null : getDepartment());
            } else if (ContactListItem.CAMPAIGN.equals(field)) {
                item.setId(getCampaignId());
                item.setName(getCampaign());
            } else if (CrmAccountItem.ADDRESS.equals(field)) {
                item.setManyResults(true);
                if (getAddresses() != null && getAddresses().size() > 0) {
                    for (Address address : getAddresses()) {
                        if (address != null) {
                            String address_ = address.toString();
                            if (!"".equals(address_) && !"N/A".equals(address_)) {
                                item.addChild(new MergeItem(getObjectId(), address.getObjectID(), address.toString()));
                            }
                        }
                    }
                }
            } else if (ContactListItem.CATEGORIES.equals(field)) {
                item.setManyResults(true);
                if (getSelectedCategories() != null && getSelectedCategories().size() > 0) {
                    for (SelectItem type : getSelectedCategories()) {
                        if (type != null) {
                            item.addChild(new MergeItem(getObjectId(), type.getId(), type.getName()));
                        }
                    }
                }
            } else if (CustomFormConstants.RELATIONSHIP.equals(field)) {
                item.setManyResults(true);
                if (getSelectedRelationships() != null && getSelectedRelationships().size() > 0) {
                    for (SelectItem type : getSelectedRelationships()) {
                        if (type != null) {
                            item.addChild(new MergeItem(getObjectId(), type.getId(), type.getDescription()));
                        }
                    }
                }
            } else if (ContactListItem.MAILING_LIST.equals(field)) {
                item.setManyResults(true);
                if (getSelectedMailingLists() != null) {
                    getSelectedMailingLists();
                    for (SelectItem type : getSelectedMailingLists()) {
                        if (type != null) {
                            item.addChild(new MergeItem(getObjectId(), type.getId(), type.getName()));
                        }
                    }
                }
            } else if (CustomFormConstants.REPORTS_TO.equals(field)) {
                item.setId(getReportsToId());
                item.setName(getReportsTo());
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
                setContactName(item.getValue());
            } else if (ContactListItem.OWNER.equals(field)) {
                setOwnerId(item.getValueID());
                setOwner(item.getValue());
            } else if (ContactListItem.FIRST_NAME.equals(field)) {
                setFirstName(item.getName());
            } else if (ContactListItem.LAST_NAME.equals(field)) {
                setLastName(item.getName());
            } else if (ContactListItem.EMAIL.equals(field)) {
                getWorkEmail().clear();
                addParam(Constants.CONTACT_EMAILS, 2, item.getValue());
                setPrimaryEmail(item.getValue());
            } else if (ContactListItem.PHONE.equals(field)) {
                getWorkPhone().clear();
                addParam(Constants.CONTACT_PHONES, 2, item.getValue());
                setPrimaryPhone(item.getValue());
            } else if (ContactListItem.JOB_TITLE.equals(field)) {
                setJobTitle(item.getValue());
            } else if (CrmAccountItem.ACCOUNT_NAME.equals(field)) {
                if (getCrmAccount() == null) {
                    setCrmAccount(new CrmAccountItem());
                } else {
                    getCrmAccount().setObjectId(item.getValueID());
                    getCrmAccount().setName(item.getValue());
                }
            } else if (CrmAccountItem.ACCOUNT_TYPE.equals(field)) {
                if (getCrmAccount() != null && getCrmAccount().getAccountTypes() != null) {
                    getCrmAccount().getAccountTypes();
                    for (SelectItem type : getCrmAccount().getAccountTypes()) {
                        if (type != null && type.getId() != null && type.getId().equals(item.getValueID())) {
                            if (type.getDescription() == null || !type.getDescription().matches("((\\d)*(,)?)*")) {
                                type.setDescription("");
                            }
                            if (value) {
                                if (!type.getDescription().matches(REGEX_ACCOUNT_TYPE_DESCRIPTION(item.getItemObjectID().toString()))) {
                                    type.setDescription(type.getDescription() + ("".equals(type.getDescription())
                                            ? ""
                                            : ","));
                                    type.setDescription(type.getDescription() + (item.getItemObjectID().toString()));
                                }
                                type.setSelected(value);
                            } else if (!"".equals(type.getDescription())) {
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
                if (getCrmAccount() == null) {
                    setCrmAccount(new CrmAccountItem());
                } else {
                    getCrmAccount().setIndustryID(item.getValueID());
                    getCrmAccount().setIndustry(item.getValue());
                }
            } else if (ContactListItem.DEPARTMENT.equals(field)) {
                setDepartment(item.getValue());
            } else if (ContactListItem.CAMPAIGN.equals(field)) {
                setCampaignId(item.getValueID());
                setCampaign(item.getValue());
            } else if (CrmAccountItem.ADDRESS.equals(field)) {
                HashMap<Integer, Address> addresses = Address.asMap(getAddresses().toArray(new Address[]{}));
                if (value) {
                    addresses.put(item.getValueID(), new Address(item.getValueID()));
                } else
                    addresses.remove(item.getValueID());
                setAddresses(new ArrayList<>(addresses.values()));
            } else if (ContactListItem.CATEGORIES.equals(field)) {
                LinkedHashMap<Integer, SelectItem> categories = SelectItem.asMap(getSelectedCategories().toArray(new SelectItem[]{}));
                if (value) {
                    categories.put(item.getValueID(), new SelectItem(item.getValueID()));
                } else
                    categories.remove(item.getValueID());
                setSelectedCategories(new ArrayList<>(categories.values()));
            } else if (CustomFormConstants.RELATIONSHIP.equals(field)) {
                LinkedHashMap<Integer, SelectItem> relationship = SelectItem.asMap(getSelectedRelationships().toArray(new SelectItem[]{}));
                if (value) {
                    relationship.put(item.getValueID(), new SelectItem(item.getValueID()));
                } else
                    relationship.remove(item.getValueID());
                setSelectedRelationships(new ArrayList<>(relationship.values()));
            } else if (ContactListItem.MAILING_LIST.equals(field)) {
                LinkedHashMap<Integer, SelectItem> mailing_list = SelectItem.asMap(getSelectedMailingLists());
                if (value) {
                    mailing_list.put(item.getValueID(), new SelectItem(item.getValueID()));
                } else
                    mailing_list.remove(item.getValueID());
                setSelectedMailingLists(mailing_list.values().toArray(new SelectItem[]{}));
            } else if (CustomFormConstants.REPORTS_TO.equals(field)) {
                setReportsToId(item.getValueID());
                setReportsTo(item.getValue());
            }
        }
    }

    private static String REGEX_ACCOUNT_TYPE_DESCRIPTION(String s) {
        return "((\\d*)?(,)?)*(" + s + ")((,)?(\\d*)?)*";
    }

    public void setHasOfficeToken(boolean hasOfficeToken) {
        addBool(HAS_OFFICE_TOKEN, hasOfficeToken);
    }

    public boolean isHasOfficeToken() {
        return getBool(HAS_OFFICE_TOKEN);
    }

    public void setSpokingLanguages(ArrayList<SpokenLanguageItem> spokenLanguages) {
        this.spokingLanguages = spokenLanguages;
    }

    public ArrayList<SpokenLanguageItem> getSpokingLanguages() {
        return spokingLanguages;
    }

    public boolean isNameNotUnique() {
        return nameNotUnique;
    }

    public void setNameNotUnique(boolean nameNotUnique) {
        this.nameNotUnique = nameNotUnique;
    }

    public LinkedHashMap<Integer, String> getImmCodes() {
        return immCodes;
    }

    public void setImmCodes(LinkedHashMap<Integer, String> immCodes) {
        this.immCodes = immCodes;
    }

    public LinkedHashMap<String, FormProperty> getFormProperty() {
        return this.formProperty;
    }

    public void setFormProperty(final LinkedHashMap<String, FormProperty> formProperty) {
        this.formProperty = formProperty;
    }

    public OpportunityListItem getOpportunity() {
        return opportunity;
    }

    public void setOpportunity(OpportunityListItem opportunity) {
        this.opportunity = opportunity;
    }

    public String getAccountIndustry() {
        return accountIndustry;
    }

    public void setAccountIndustry(String accountIndustry) {
        this.accountIndustry = accountIndustry;
    }

    public EmployeeListItem getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeListItem employee) {
        this.employee = employee;
    }

    public ColumnConfigs[] getCustomItemColumns() {
        return customItemColumns;
    }

    public void setCustomItemColumns(ColumnConfigs[] customItemColumns) {
        this.customItemColumns = customItemColumns;
    }

    public HashMap<String, ArrayList<CustomTableRpc>> getCustomTableItems() {
        return customTableItems;
    }

    public void setCustomTableItems(HashMap<String, ArrayList<CustomTableRpc>> customTableItems) {
        this.customTableItems = customTableItems;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public SelectItem getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(SelectItem productCategory) {
        this.productCategory = productCategory;
    }

    public SelectItem getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(SelectItem productBrand) {
        this.productBrand = productBrand;
    }

    public TaxItem getTaxItem() {
        return taxItem;
    }

    public void setTaxItem(TaxItem taxItem) {
        this.taxItem = taxItem;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getNet() {
        return net;
    }

    public void setNet(BigDecimal net) {
        this.net = net;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public TreeSelectItem[] getProductCategories() {
        return productCategories;
    }

    public void setProductCategories(TreeSelectItem[] productCategories) {
        this.productCategories = productCategories;
    }

    public SelectItem[] getProductBrands() {
        return productBrands;
    }

    public void setProductBrands(SelectItem[] productBrands) {
        this.productBrands = productBrands;
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

    public ArrayList<SelectItem> getTelegramChats() {
        return telegramChats;
    }

    public void setTelegramChats(ArrayList<SelectItem> telegramChats) {
        this.telegramChats = telegramChats;
    }

    public SelectItem[] getTemplates() {
        return this.templates;
    }

    public void setTemplates(final SelectItem[] templates) {
        this.templates = templates;
    }

    public Integer getSelectedSubStageId() {
        return selectedSubStageId;
    }

    public void setSelectedSubStageId(Integer selectedSubStageId) {
        this.selectedSubStageId = selectedSubStageId;
    }

    public Integer getPlacementId() {
        return placementId;
    }

    public void setPlacementId(Integer placementId) {
        this.placementId = placementId;
    }

    public Map<String, List<CustomTableRpc>> getCandidateCustomTableItems() {
        return candidateCustomTableItems;
    }

    public boolean isDraggable() {
        return draggable;
    }

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
    }

    public boolean isAllowEdit() {
        return allowEdit;
    }

    public void setAllowEdit(boolean allowEdit) {
        this.allowEdit = allowEdit;
    }

    public void setCandidateCustomTableItems(Map<String, List<CustomTableRpc>> candidateCustomTableItems) {
        this.candidateCustomTableItems = candidateCustomTableItems;
    }

    public ArrayList<VacancyItem> getVacancyItems() {
        return vacancyItems;
    }

    public void setVacancyItems(ArrayList<VacancyItem> vacancyItems) {
        this.vacancyItems = vacancyItems;
    }

    public Date getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(Date eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public Date getEventEndDate() {
        return eventEndDate;
    }

    public void setEventEndDate(Date eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public String getCandidateZoomLink() {
        return candidateZoomLink;
    }

    public void setCandidateZoomLink(String candidateZoomLink) {
        this.candidateZoomLink = candidateZoomLink;
    }

    public String getFullName() {
        return ((getFirstName() != null ? getFirstName() : "") + " " + (getMiddleName() != null ? getMiddleName() : "") + " " + (getLastName() != null ? getLastName() : "")).trim();
    }

    public boolean isFromOpportunityQuickAdd() {
        return isFromOpportunityQuickAdd;
    }

    public void setFromOpportunityQuickAdd(boolean fromOpportunityQuickAdd) {
        isFromOpportunityQuickAdd = fromOpportunityQuickAdd;
    }

    public String getCandidateLocation() {
        return candidateLocation;
    }

    public void setCandidateLocation(String location) {
        this.candidateLocation = location;
    }


    public boolean isCandaidateNewFromApi() {
        return isCandaidateNewFromApi;
    }

    public void setCandaidateNewFromApi(boolean candaidateNewFromApi) {
        isCandaidateNewFromApi = candaidateNewFromApi;
    }


    public SelectItem getTimeSlotItem() {
        return timeSlotItem;
    }

    public void setTimeSlotItem(SelectItem timeSlotItem) {
        this.timeSlotItem = timeSlotItem;
    }

    public void setCreator(SelectItem creator) {
        this.creator = creator;
    }

    public SelectItem getCreatorAsSelectItem() {
        return creator;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }
}
