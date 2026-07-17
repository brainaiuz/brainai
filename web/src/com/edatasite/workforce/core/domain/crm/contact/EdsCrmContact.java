package com.edatasite.workforce.core.domain.crm.contact;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.shared.db.ObjectIdentifier;
import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.crm.EdsCampaign;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsCrmContactItem;
import com.edatasite.workforce.core.domain.crm.EdsCrmContactItemParams;
import com.edatasite.workforce.core.domain.customfields.EdsCrmCustomFields;
import com.edatasite.workforce.core.domain.customform.EdsModelField;
import com.edatasite.workforce.core.domain.documents.EdsAuditInfo;
import com.edatasite.workforce.core.domain.emailfetching.EdsEmailTracker;
import com.edatasite.workforce.core.domain.payrolluk.EdsPaymentDeduction;
import com.edatasite.workforce.core.domain.recruitment.EdsCandidateItemTable;
import com.edatasite.workforce.core.domain.recruitment.EdsVacancy;
import com.edatasite.workforce.core.domain.webforms.EdsWebForm;
import com.edatasite.workforce.core.domain.workflow.EdsTraceable;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactSolrItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.domain.ObjectKanbanItem;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants;
import com.edatasite.workforce.gwt.webforms.client.forms.LeadField;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.*;

/**
 * User: Sherali
 * Date: 09-Jul-2009
 * Time: 15:25:41
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "crmContact")
public class EdsCrmContact extends EdsTraceable implements WebFormInterface, HasTemplateKeyValues, ObjectKanbanItem, ObjectIdentifier {

    public static final Integer CRM_CONTACT = CrmConstants.TYPE_CRM_CONTACT;
    public static final Integer CLIENT_CONTACT = CrmConstants.TYPE_CLIENT_CONTACT;
    public static final Integer SUPPLIER_CONTACT = CrmConstants.TYPE_SUPPLIER_CONTACT;
    public static final Integer EMPLOYEE_CONTACT = CrmConstants.TYPE_EMPLOYEE_CONTACT;
    public static final Integer LEAD_CONTACT = CrmConstants.TYPE_LEAD_CONTACT;
    public static final Integer STUDENT_CONTACT = CrmConstants.TYPE_STUDENT_CONTACT;
    public static final Integer CANDIDATE = CrmConstants.TYPE_CANDIDATE;
    public static final String CONTACT_RELATION = "CONTACT_RELATIONS";

    public static final String _IM_ADDRESSES = "_IM_ADDRESSES";

    public static final String _LEAD_STATUS = "_LEAD_STATUS";
    public static final String _LEAD_STATUS_NOT_CONTACTED = "_LEAD_STATUS_NOT_CONTACTED";
    public static final String ATTEMPTED_TO_CONTACT = "ATTEMPTED_TO_CONTACT";
    public static final String _LEAD_SOURCE = "_LEAD_SOURCE";
    public static final String LEAD_SOURCE_OTHER = CrmConstants.LEAD_SOURCE_OTHER;
    public static final String _LEAD_RATING = "_LEAD_RATING";
    public static final String _CANDIDATE_SOURCE = "_CANDIDATE_SOURCE";
    //candidate status
    public static final String _CANDIDATE_STATUS = ContactListItem._CANDIDATE_STATUS;
    public static final String CANDIDATE_STATUS_SHORTLIST = ContactListItem.C_S_SHORTLIST;
    public static final String CANDIDATE_STATUS_INTERVIEW = ContactListItem.C_S_INTERVIEW;
    public static final String CANDIDATE_STATUS_OFFER_MADE = ContactListItem.C_S_OFFER_MADE;
    public static final String CANDIDATE_STATUS_HIRED = ContactListItem.C_S_HIRED;
    public static final String CANDIDATE_STATUS_REJECTED = ContactListItem.C_S_REJECTED;
    public static final String CANDIDATE_STATUS_PLACED = ContactListItem.C_S_PLACED;
    public static final String CANDIDATE_STATUS_MATCHED = ContactListItem.C_S_MATCHED;
    public static final String CANDIDATE_STATUS_UNQUALIFIED = ContactListItem.C_S_UNQUALIFIED;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "objectKey", unique = true, updatable = false)
    private String objectKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner")
    private EdsUser owner;

    @Column(name = "firstName")
    private String firstName;

    @Column(name = "primaryEmail")
    private String primaryEmail;

    @Column(name = "primaryPhone")
    private String primaryPhone;

    @Column(name = "gender")
    private String gender;

    @Column(name = "lastName")
    private String lastName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crmAccount")
    private EdsCrmAccount crmAccount;

    @Column(name = "title")
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "titleID")
    private EdsReference titleRef;

    @Column(name = "department")
    private String department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign")
    private EdsCampaign campaign;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photoId")
    private EdsUpload photo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "companyPhotoId")
    private EdsUpload companyPhoto;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "contactid")
    private List<EdsCrmContactItemParams> itemParams = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "contactID")
    @Where(clause = " entityType = '" + EdsAddress.ENTITY_TYPE_CONTACT + "' and deleted = 'false'")
    private List<EdsAddress> addresses = new ArrayList<>();

    @Column(name = "dateOfBirth")
    private Date dateOfBirth;

    @Column(name = "reportsTo")
    private String reportsTo;

    @Column(name = "reportsToId")
    private Integer reportsToId;

    @Column(name = "emailOptOut")
    private Boolean emailOptOut = false;

    @Column(name = "bouncedcount")
    private Integer bouncedcount = 0;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "contactType")
    private Integer contactType = CRM_CONTACT;

    @Column(name = "clientSupplierContact_id")
    private Integer entityContactID;

    private Boolean accessEnabled;

    @Column(name = "selected_reference")
    private Integer selectedReference;

    private Boolean primaryContact;

    @Column(name = "middleName")
    private String middleName;

    @Column(name = "otherName")
    private String otherName;

    @Column(name = "jobTitles")
    private String jobTitles;

    @Column(name = "entity_id")
    private Integer entityID;

    @Column(name = "googleId", length = 500)
    private String googleId;

    @Column(name = "importfile_id")
    private Integer importFileID;

    @Column(columnDefinition = " int8 default 0")
    private long syncID;

    private String jobFunction;

    private String position;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private EdsCrmContactDetails crmContactDetails;

    @Column(name = "assets")
    private String assets;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "crmcontact_contactcategory",
            joinColumns = {@JoinColumn(name = "crmcontact_id")},
            inverseJoinColumns = {@JoinColumn(name = "categories_id")}
    )
    private Set<EdsContactCategory> categories = new HashSet<>();

    //LEAD DETAILS
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignee")
    private EdsEmployee leadAssignee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "backupAssignee")
    private EdsEmployee leadBackupAssignee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source")
    private EdsReference leadSource;

    @Column(name = "otherSource")
    private String otherLeadSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status")
    private EdsReference leadStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rating")
    private EdsReference leadRating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "webform_id")
    private EdsWebForm leadWebForm;

    //temporary
    @Column(name = "lead_id")
    private Integer leadID;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private EdsCrmCustomFields customFields;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private EdsCrmCustomFields oldCustomFields;

    /**
     * The subCases in this case. A List so we can keep order.
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "historicalParent", fetch = FetchType.LAZY)
    @OrderBy("objectID DESC")
    private List<EdsCrmContact> histories = new ArrayList<>();

    // allowances
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "candidate", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @Where(clause = "(deleted = 'false' or deleted is null) and isRecurring is not null")
    private List<EdsPaymentDeduction> allowances = new ArrayList<>();

    /**
     * The parent case of this one.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private EdsCrmContact historicalParent;

    @Column(name = "historical")
    private Boolean historical = false;

    /**
     * The audit information.
     */
    @Embedded
    private EdsAuditInfo auditInfo;

    private transient boolean propertiesChanged = false;

    private Date convertedDate;

    @Column(name = "convertedfrom", columnDefinition = "int4 default 0")
    private Integer convertedFrom = 0;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "contactID")
    private List<EdsDeviceCrmContact> deviceCrmContacts = new ArrayList<>();

    @Transient
    private List<EdsCrmContactItemParams> itemParamsTransient;

    @Transient
    private List<Integer> cateogryIDs;

    @Transient
    private Map<Integer, EdsAddress> addressesTransient;

    @Transient
    private String note;

    @Transient
    private CompanyCustomFieldItem[] transientCustomFields;

    @Column(name = "saasu_guid")
    private String saasuGUID;

    //Candidate fields
    @Column(name = "number")
    private String number;

    @Column(name = "number_integer")
    private Integer numberInteger;

    @Column(name = "work_experience", length = 2)
    private Integer workExperience;

    @Column(name = "workExperienceMonthOrYear", length = 2)
    private Integer workExperienceMonthOrYear;// 1 -- Month; 2 -- Years; DEFAULT -- null;

    @Column(name = "currentEmploye")
    private String currentEmployer;

    @Column(name = "expected_salary")
    private Double expectedSalary;

    @Column(name = "skills")
    @Type(type = "text")
    private String skills;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preferred_location")
    private EdsLocation prefferedLocation;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @Where(clause = "deleted = 'false'")
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "candidate_vacancies",
            joinColumns = {@JoinColumn(name = "candidate_id")},
            inverseJoinColumns = {@JoinColumn(name = "vacancy_id")}
    )
    private Set<EdsVacancy> vacancies = new HashSet<>();

    @Column(name = "isShortList")
    private Boolean isShortList = false; //for candidate
    @Column(name = "ref_ind_number")
    private String refIndNumber;
    @Column(columnDefinition = " boolean DEFAULT false")
    private boolean showAccountAddress = false;


    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "crmcontact_corpemailtracker",
            joinColumns = {@JoinColumn(name = "crmcontact_id")},
            inverseJoinColumns = {@JoinColumn(name = "tracker_id")}
    )
    private Set<EdsEmailTracker> trackerSet = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorid")
    private EdsUser creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidateProjectID")
    private EdsProject candidateProject;

    @Column(name = "kanban_order")
    private Long kanbanorder;

    @Column(name = "is_favourited")
    private Boolean favourited;

    @OneToMany(mappedBy = "crmContact", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<EdsCrmContactCustomItemTable> itemTables = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "martialStatusId")
    private EdsReference martialStatus;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "crmContact")
    private List<EdsCrmContactItem> crmContactItems = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY, mappedBy = "candidate")
    private List<EdsCandidateStatusHistory> statusHistories = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "candidateId")
    private Set<EdsCandidateChanges> candidateHistory = new HashSet<>();

    @OneToMany(mappedBy = "candidate", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<EdsCandidateItemTable> candidateItemTables = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "positionid")
    private EdsPosition candidatePosition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departmentId")
    private EdsDepartment candidateDepartment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timedlotid")
    private EdsTimeSlot timeSlot;

    @Column(name = "start_salary")
    private Double startSalary;

    @Column(name = "passportNumber")
    private String passportNumber;


    public List<EdsCrmContactItem> getCrmContactItems() {
        return crmContactItems;
    }

    public EdsReference getMartialStatus() {
        return martialStatus;
    }

    public void setMartialStatus(EdsReference martialStatus) {
        if (is(EdsCrmContact.CANDIDATE)) {
            if (!ServerUtils.equalsEdsObject(this.martialStatus, martialStatus)) {
                addHistoryChange("Martial Status", this.martialStatus != null ? this.martialStatus.getName() : null, martialStatus != null ? martialStatus.getName() : null);
            }
        }
        this.martialStatus = martialStatus;
    }

    public void setCrmContactItems(List<EdsCrmContactItem> crmContactItems) {
        this.crmContactItems = crmContactItems;
    }

    public Set<EdsCrmContactCustomItemTable> getItemTables() {
        return this.itemTables;
    }

    public void setItemTables(final Set<EdsCrmContactCustomItemTable> itemTables) {
        this.itemTables = itemTables;
    }

    public void addItemTable(EdsCrmContactCustomItemTable itemTable) {
        itemTables.add(itemTable);
    }

    public EdsUser getCreator() {
        return creator != null ? creator : owner;
    }

    public void setCreator(EdsUser creator) {
        this.creator = creator;
    }

    public void addTracker(EdsEmailTracker emailTracker) {
        if (trackerSet == null) {
            trackerSet = new HashSet<>();
        }
        trackerSet.add(emailTracker);
    }

    public Integer getConvertedFrom() {
        return convertedFrom;
    }

    public void setConvertedFrom(Integer convertedFrom) {
        this.convertedFrom = convertedFrom;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        if (is(EdsCrmContact.CANDIDATE)) {
            if (!ServerUtils.equalsString(this.number, number)) {
                addHistoryChange("Number", this.number, number);
            }
        }
        this.number = number;
    }

    public Integer getNumberInteger() {
        return numberInteger;
    }

    public void setNumberInteger(Integer numberInteger) {
        this.numberInteger = numberInteger;
    }

    public Integer getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(Integer workExperience) {
        this.workExperience = workExperience;
    }

    public Integer getWorkExperienceMonthOrYear() {
        return workExperienceMonthOrYear;
    }

    public void setWorkExperienceMonthOrYear(Integer workExperienceMonthOrYear) {
        this.workExperienceMonthOrYear = workExperienceMonthOrYear;
    }

    public String getCurrentEmployer() {
        return currentEmployer;
    }

    public void setCurrentEmployer(String currentEmployer) {
        if (is(EdsCrmContact.CANDIDATE)) {
            if (!ServerUtils.equalsString(this.currentEmployer, currentEmployer)) {
                addHistoryChange("Current Employee", this.currentEmployer, currentEmployer);
            }
        }
        this.currentEmployer = currentEmployer;
    }

    public Double getExpectedSalary() {
        return expectedSalary;
    }

    public void setExpectedSalary(Double expectedSalary) {
        if (is(EdsCrmContact.CANDIDATE)) {
            if (!ServerUtils.equalsDouble(this.expectedSalary, expectedSalary)) {
                addHistoryChange("Expected Salary", this.expectedSalary, expectedSalary);
            }
        }
        this.expectedSalary = expectedSalary;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        if (is(EdsCrmContact.CANDIDATE)) {
            if (!ServerUtils.equalsString(this.skills, skills)) {
                addHistoryChange("Skills", this.skills, skills);
            }
        }
        this.skills = skills;
    }

    public EdsLocation getPrefferedLocation() {
        return prefferedLocation;
    }

    public void setPrefferedLocation(EdsLocation prefferedLocation) {
        if (is(EdsCrmContact.CANDIDATE)) {
            if (!ServerUtils.equalsEdsObject(this.prefferedLocation, prefferedLocation)) {
                addHistoryChange("Preffered Location", this.prefferedLocation != null ? this.prefferedLocation.getName() : "", prefferedLocation != null ? prefferedLocation.getName() : "");
            }
        }
        this.prefferedLocation = prefferedLocation;
    }

    public Set<EdsVacancy> getVacancies() {
        return vacancies;
    }

    public void setVacancies(Set<EdsVacancy> vacancies) {
        this.vacancies = vacancies;
    }

    public Boolean getShortList() {
        return isShortList != null ? isShortList : Boolean.FALSE;
    }

    public void setShortList(Boolean shortList) {
        isShortList = shortList;
    }

    public Date getConvertedDate() {
        return convertedDate;
    }

    public void setConvertedDate(Date convertedDate) {
        this.convertedDate = convertedDate;
    }

    public EdsCrmCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsCrmCustomFields customFields) {
        this.customFields = customFields;
    }

    public EdsCrmCustomFields getOldCustomFields() {
        return oldCustomFields;
    }

    public void setOldCustomFields(EdsCrmCustomFields oldCustomFields) {
        this.oldCustomFields = oldCustomFields;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entity) {
        this.entityID = entity;
    }

    public String getName() {
        return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "");
    }

    public String getFullName() {
        return (firstName != null ? firstName : "") + " " + (middleName != null ? middleName : "") + " " + (lastName != null ? lastName : "");
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsUser getOwner() {
        return owner;
    }

    public boolean isShowAccountAddress() {
        return showAccountAddress;
    }

    public void setShowAccountAddress(boolean showAccountAddress) {
        this.showAccountAddress = showAccountAddress;
    }

    public void setOwner(EdsUser owner) {
        EdsUser oldOwner = this.owner;
        this.owner = owner;
        if (!ServerUtils.equalsEdsObject(oldOwner, owner)) {
            addChange(CustomFormConstants.LEAD_OWNER);
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (!ServerUtils.equalsString(this.firstName, firstName)) {
            addChange(CustomFormConstants.FIRST_NAME);
        }
        if (is(EdsCrmContact.CANDIDATE)) {
            if (!ServerUtils.equalsString(this.firstName, firstName)) {
                addHistoryChange("First Name", this.firstName, firstName);
            }
        }
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (!ServerUtils.equalsString(this.lastName, lastName)) {
            addChange(CustomFormConstants.LAST_NAME);
        }
        if (is(EdsCrmContact.CANDIDATE)) {
            if (!ServerUtils.equalsString(this.lastName, lastName)) {
                addHistoryChange("Last Name", this.lastName, lastName);
            }
        }
        this.lastName = lastName;
    }

    public EdsCrmAccount getCrmAccount() {
        return crmAccount;
    }

    public void setCrmAccount(EdsCrmAccount crmAccount) {
        if (!ServerUtils.equalsEdsObject(this.crmAccount, crmAccount)) {
            addChange(CustomFormConstants.CRM_ACCOUNT_NAME);
        }
        this.crmAccount = crmAccount;
        if (crmAccount != null && crmAccount.getEntityID() != null) {
            setEntityID(crmAccount.getEntityID());
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (is(EdsCrmContact.CANDIDATE)) {
            if (!ServerUtils.equalsString(this.title, title)) {
                addHistoryChange("Title", this.title, title);
            }
        }
        this.title = title;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }


    public EdsCampaign getCampaign() {
        return campaign;
    }

    public void setCampaign(EdsCampaign campaign) {
        if (!ServerUtils.equalsEdsObject(this.campaign, campaign)) {
            addChange(CustomFormConstants.CRM_CAMPAIGN_NAME);
        }
        this.campaign = campaign;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        if (!ServerUtils.equalsDate(this.dateOfBirth, dateOfBirth)) {
            addChange(CustomFormConstants.BIRTH_DAY);
        }
        if (is(EdsCrmContact.CANDIDATE)) {
            if (!ServerUtils.equalsDate(this.dateOfBirth, dateOfBirth)) {
                addHistoryChange("Date Of Birth", this.dateOfBirth, dateOfBirth);
            }
        }
        this.dateOfBirth = dateOfBirth;
    }

    public String getReportsTo() {
        return reportsTo;
    }

    public void setReportsTo(String reportsTo) {
        if (is(EdsCrmContact.CANDIDATE)) {
            if (!ServerUtils.equalsString(this.reportsTo, reportsTo)) {
                addHistoryChange("Reports To", this.reportsTo, reportsTo);
            }
        }
        this.reportsTo = reportsTo;
    }

    public Integer getReportsToId() {
        return reportsToId;
    }

    public void setReportsToId(Integer reportsToId) {
        this.reportsToId = reportsToId;
    }

    public Boolean getEmailOptOut() {
        return emailOptOut;
    }

    public void setEmailOptOut(Boolean emailOptOut) {
        this.emailOptOut = emailOptOut;
    }

    public Integer getBouncedcount() {
        return bouncedcount;
    }

    public void setBouncedcount(Integer bouncedcount) {
        this.bouncedcount = bouncedcount;
    }

    public Boolean isDeleted() {
        return deleted != null ? deleted : Boolean.FALSE;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        if (is(EdsCrmContact.CANDIDATE)) {
            if (!ServerUtils.equalsString(this.middleName, middleName)) {
                addHistoryChange("Middle Name", this.middleName, middleName);
            }
        }
        this.middleName = middleName;
    }

    public String getJobTitles() {
        return jobTitles;
    }

    public void setJobTitles(String jobTitles) {
        if (!ServerUtils.equalsString(this.jobTitles, jobTitles)) {
            addChange(CustomFormConstants.JOB_TITLE);
        }
        this.jobTitles = jobTitles;
    }

    public String getJobFunction() {
        return jobFunction;
    }

    public void setJobFunction(String jobFunction) {
        this.jobFunction = jobFunction;
    }

    public EdsCrmContactDetails getCrmContactDetails() {
        return crmContactDetails;
    }

    public void setCrmContactDetails(EdsCrmContactDetails crmContactDetails) {
        this.crmContactDetails = crmContactDetails;
    }

    public String getBackgroundInformation() {
        if (this.crmContactDetails != null) {
            return this.crmContactDetails.getBackgroundInformation();
        }
        return null;
    }

    public void setBackgroundInformation(String backgroundInformation) {
        if (this.crmContactDetails == null) {
            setCrmContactDetails(new EdsCrmContactDetails());
        }
        this.crmContactDetails.setBackgroundInformation(backgroundInformation);
    }


    public String getDisclaimer() {
        if (this.crmContactDetails != null) {
            return this.crmContactDetails.getDisclaimer();
        }
        return null;
    }

    public void setDisclaimer(String disclaimer) {
        if (this.crmContactDetails == null) {
            setCrmContactDetails(new EdsCrmContactDetails());
        }
        this.crmContactDetails.setDisclaimer(disclaimer);
    }

    public String getAssets() {
        return assets;
    }

    public void setAssets(String assets) {
        this.assets = assets;
    }

    public Integer getContactType() {
        return contactType;
    }

    public void setContactType(Integer contactType) {
        if (contactType == null) {
            contactType = CRM_CONTACT;
        }
        if (this.contactType != null && ((LEAD_CONTACT.equals(this.contactType) && !LEAD_CONTACT.equals(contactType)) || (CANDIDATE.equals(this.contactType) && !CANDIDATE.equals(contactType)))) {
            setConvertedDate(new Date());
            setConvertedFrom(this.contactType);
        }
        this.contactType = contactType;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public Integer getEntityContactID() {
        return entityContactID;
    }

    public void setEntityContactID(Integer entityContactID) {
        this.entityContactID = entityContactID;
    }

    public Boolean isAccessEnabled() {
        return accessEnabled != null ? accessEnabled : false;
    }

    public void setAccessEnabled(Boolean accessEnabled) {
        this.accessEnabled = accessEnabled;
    }

    public Boolean getPrimaryContact() {
        return primaryContact == null ? false : primaryContact;
    }

    public void setPrimaryContact(Boolean primaryContact) {
        this.primaryContact = primaryContact;
    }

    public boolean isPrimaryContact() {
        return primaryContact != null ? primaryContact : false;
    }

    public String getOtherName() {
        return otherName;
    }

    public void setOtherName(String otherName) {
        this.otherName = otherName;
    }

    public List<EdsCrmContactItemParams> getItemParams() {
        return itemParams;
    }

    public void setItemParams(List<EdsCrmContactItemParams> itemParams) {
        this.itemParams = itemParams;
    }

    public Integer getImportFileID() {
        return importFileID;
    }

    public void setImportFileID(Integer importFileID) {
        this.importFileID = importFileID;
    }

    public long getSyncID() {
        return syncID;
    }

    public void setSyncID(long syncID) {
        this.syncID = syncID;
    }

    public EdsUpload getCompanyPhoto() {
        return companyPhoto;
    }

    public void setCompanyPhoto(EdsUpload companyPhoto) {
        this.companyPhoto = companyPhoto;
    }

    public Set<EdsContactCategory> getCategories() {
        if (categories == null) {
            categories = new HashSet<>();
        }
        return categories;
    }

    public void setCategories(Set<EdsContactCategory> categories) {
        this.categories = categories;
    }

    public void addCategories(EdsContactCategory edsContactCategory) {
        if (edsContactCategory != null) {
            getCategories().add(edsContactCategory);
        }
    }

    public void addCategories(List<EdsContactCategory> edsContactCategories) {
        if (edsContactCategories != null && edsContactCategories.size() > 0) {
            for (EdsContactCategory edsContactCategory : edsContactCategories) {
                addCategories(edsContactCategory);
            }
        }
    }

    public void setItemParamsTransient(List<EdsCrmContactItemParams> itemParamsTransient) {
        this.itemParamsTransient = itemParamsTransient;
    }

    public void setAddressesTransient(Map<Integer, EdsAddress> addressesTransient) {
        this.addressesTransient = addressesTransient;
    }

    public List<EdsCrmContactItemParams> getItemParamsTransient() {
        return itemParamsTransient;
    }

    public Map<Integer, EdsAddress> getAddressesTransient() {
        return addressesTransient;
    }

    public EdsUpload getPhoto() {
        return photo;
    }

    public void setPhoto(EdsUpload photo) {
        this.photo = photo;
    }

    public String getGooglenote() {
        if (this.crmContactDetails != null) {
            try {
                return this.crmContactDetails.getGooglenote();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public void setGooglenote(String googlenote) {
        if (this.crmContactDetails == null) {
            setCrmContactDetails(new EdsCrmContactDetails());
        }
        this.crmContactDetails.setGooglenote(googlenote);
    }

    public String getPrimaryEmail() {
        if (primaryEmail == null) {
            setPrimaryEmail(getPrimaryEmailFromAll());
        }
        return primaryEmail;
    }

    public void setPrimaryEmail(String primaryEmail) {
        if (!ServerUtils.equalsString(this.primaryEmail, primaryEmail)) {
            addChange(CustomFormConstants.EMAIL);
        }
        if (is(EdsCrmContact.CANDIDATE)) {
            if (!ServerUtils.equalsString(this.primaryEmail, primaryEmail)) {
                addHistoryChange("Primary Email", this.primaryEmail, primaryEmail);
            }
        }
        this.primaryEmail = primaryEmail;
    }

    public String getPrimaryPhone() {
        return primaryPhone;
    }

    public void setPrimaryPhone(String primaryPhone) {
        if (!ServerUtils.equalsString(this.primaryPhone, primaryPhone)) {
            addChange(CustomFormConstants.PHONE);
        }
        if (is(EdsCrmContact.CANDIDATE)) {
            if (!ServerUtils.equalsString(this.primaryPhone, primaryPhone)) {
                addHistoryChange("Primary Phone", this.primaryPhone, primaryPhone);
            }
        }
        this.primaryPhone = primaryPhone;
    }

    public EdsEmployee getLeadAssignee() {
        return leadAssignee;
    }

    public void setLeadAssignee(EdsEmployee leadAssignee) {
        if (!ServerUtils.equalsEdsObject(this.leadAssignee, leadAssignee)) {
            addChange(CustomFormConstants.ASSIGNEE);
        }
        this.leadAssignee = leadAssignee;
    }

    public EdsEmployee getLeadBackupAssignee() {
        return leadBackupAssignee;
    }

    public void setLeadBackupAssignee(EdsEmployee leadBackupAssignee) {
        if (!ServerUtils.equalsEdsObject(this.leadBackupAssignee, leadBackupAssignee)) {
            addChange(CustomFormConstants.BACKUP_ASSIGNEE);
        }
        this.leadBackupAssignee = leadBackupAssignee;
    }

    public EdsReference getLeadSource() {
        return leadSource;
    }

    public void setLeadSource(EdsReference leadSource) {
        if (!ServerUtils.equalsEdsObject(this.leadSource, leadSource)) {
            addChange(CustomFormConstants.LEAD_SOURCE);
        }
        this.leadSource = leadSource;
    }

    public EdsReference getCandidateSource() {
        return getLeadSource();
    }

    public void setCandidateSource(EdsReference source) {
        if (is(EdsCrmContact.CANDIDATE)) {
            if (!ServerUtils.equalsReference(this.leadSource, source)) {
                addHistoryChange("Source", this.leadSource, source);
            }
        }
        setLeadSource(source);
    }

    public EdsReference getCandidateStatus() {
        return getLeadStatus();
    }

    public void setCandidateStatus(EdsReference status) {
        setLeadStatus(status);
    }

    public String getOtherLeadSource() {
        return otherLeadSource;
    }

    public void setOtherLeadSource(String otherLeadSource) {
        this.otherLeadSource = otherLeadSource;
    }

    public EdsReference getLeadStatus() {
        return leadStatus;
    }

    public void setLeadStatus(EdsReference leadStatus) {
        if (!ServerUtils.equalsEdsObject(this.leadStatus, leadStatus)) {
            addChange(CustomFormConstants.STATUS);
            propertiesChanged = true;
        }
        if (is(EdsCrmContact.CANDIDATE)) {
            if (!ServerUtils.equalsReference(this.leadStatus, leadStatus)) {
                addHistoryChange("Status", this.leadSource, leadStatus);
            }
        }
        this.leadStatus = leadStatus;
    }

    public EdsReference getLeadRating() {
        return leadRating;
    }

    public void setLeadRating(EdsReference leadRating) {
        if (!ServerUtils.equalsEdsObject(this.leadRating, leadRating)) {
            addChange(CustomFormConstants.RATING);
        }
        this.leadRating = leadRating;
    }

    public EdsWebForm getLeadWebForm() {
        return leadWebForm;
    }

    public void setLeadWebForm(EdsWebForm leadWebForm) {
        this.leadWebForm = leadWebForm;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<EdsDeviceCrmContact> getDeviceCrmContacts() {
        return deviceCrmContacts;
    }

    public void setDeviceCrmContacts(List<EdsDeviceCrmContact> deviceCrmContacts) {
        this.deviceCrmContacts = deviceCrmContacts;
    }

    public EdsDeviceCrmContact getDeviceCrmContact(String objectID, boolean isDeviceID) {
        EdsDeviceCrmContact deviceContact = null;
        if (getDeviceCrmContacts() != null && getDeviceCrmContacts().size() > 0) {
            for (EdsDeviceCrmContact deviceCrmContact : getDeviceCrmContacts()) {
                if (deviceCrmContact != null && objectID != null && !"".equals(objectID) &&
                        (isDeviceID ? (deviceCrmContact.getDeviceID() != null && objectID.equals(deviceCrmContact.getDeviceID()))
                                : (deviceCrmContact.getDeviceContactID() != null && objectID.equals(deviceCrmContact.getDeviceContactID())))) {
                    deviceContact = deviceCrmContact;
                    break;
                }
            }
        }

        return deviceContact;
    }

    public void setDeviceCrmContactsStatusIfExist(String status) {
        if (getDeviceCrmContacts() != null && getDeviceCrmContacts().size() > 0) {
            for (EdsDeviceCrmContact deviceCrmContact : getDeviceCrmContacts()) {
                if (deviceCrmContact != null) {
                    deviceCrmContact.setStatus(status);
                }
            }
        }
    }

    private String getPrimary(List... lists) {
        if (lists != null) {
            for (List<String> list : lists) {
                if (list != null && list.size() > 0) {
                    return list.get(0);
                }
            }
        }
        return null;
    }

    public List<EdsCrmContactItemParams> getItemParams(int param) {
        if (itemParams == null || itemParams.size() == 0) {
            return itemParams;
        }
        List<EdsCrmContactItemParams> paramList = new ArrayList<>();
        if (itemParams != null && itemParams.size() > 0) {
            for (EdsCrmContactItemParams itemParam : itemParams) {
                if (itemParam.getParam().equals(param)) {
                    paramList.add(itemParam);
                }
            }
        }
        return paramList;
    }

    public List<EdsAddress> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<EdsAddress> addresses) {
        this.addresses = addresses;
    }

    public HashMap<Integer, ArrayList<String>> getParams(int type) {
        HashMap<Integer, ArrayList<String>> params = new LinkedHashMap<>();
        for (EdsCrmContactItemParams itemParam : getItemParams(type)) {
            if (itemParam.getRelation() != null && !"".equals(itemParam.getRelation())) {
                if (params.containsKey(itemParam.getRelation())) {
                    params.get(itemParam.getRelation()).add(itemParam.getValue());
                } else {
                    params.put(itemParam.getRelation(), new ArrayList<>());
                    params.get(itemParam.getRelation()).add(itemParam.getValue());
                }
            }
        }
        return params;
    }

    public String getPrimaryEmailFromAll() {
        List<String> homeEmails = new ArrayList<>();
        List<String> workEmails = new ArrayList<>();
        List<String> otherEmails = new ArrayList<>();
        if (itemParams != null && itemParams.size() > 0) {
            for (EdsCrmContactItemParams itemParam : getItemParams(EdsCrmContactItemParams.EMAIL)) {
                if (itemParam != null && itemParam.getRelation() != null && !"".equals(itemParam.getValue())) {
                    if (EdsCrmContactItemParams.HOME == itemParam.getRelation()) {
                        homeEmails.add(itemParam.getValue());
                    } else if (EdsCrmContactItemParams.WORK == itemParam.getRelation()) {
                        workEmails.add(itemParam.getValue());
                    } else if (EdsCrmContactItemParams.OTHER == itemParam.getRelation()) {
                        otherEmails.add(itemParam.getValue());
                    }
                }
            }
        }
        return getPrimary(homeEmails, workEmails, otherEmails);
    }

    public Address getPrimaryAddressFromAll() {
        List<EdsAddress> homeAddresses = new ArrayList<>();
        List<EdsAddress> workAddresses = new ArrayList<>();
        List<EdsAddress> otherAddresses = new ArrayList<>();
        EdsAddress foundPrimary = null;
        if (getAddresses() != null && !getAddresses().isEmpty()) {
            for (EdsAddress address : getAddresses()) {
                if (address.isPrimary()) {
                    foundPrimary = address;
                    return foundPrimary.getRPC();
                }
                if (address.getRelationType() != null) {
                    if (EdsCrmContactItemParams.HOME == address.getRelationType()) {
                        homeAddresses.add(address);
                    } else if (EdsCrmContactItemParams.WORK == address.getRelationType()) {
                        workAddresses.add(address);
                    } else if (EdsCrmContactItemParams.OTHER == address.getRelationType()) {
                        otherAddresses.add(address);
                    }
                }
            }
        }
        return workAddresses.size() > 0 ? workAddresses.get(0).getRPC() : (homeAddresses.size() > 0 ? homeAddresses.get(0).getRPC() : (otherAddresses.size() > 0 ? otherAddresses.get(0).getRPC() : (null)));
    }

    public EdsAddress getEdsPrimaryAddressFromAll() {
        List<EdsAddress> homeAddresses = new ArrayList<>();
        List<EdsAddress> workAddresses = new ArrayList<>();
        List<EdsAddress> otherAddresses = new ArrayList<>();
        if (getAddresses() != null && getAddresses().size() > 0) {
            for (EdsAddress address : getAddresses()) {
                if (address.isPrimary()) {
                    return address;
                }
                if (address != null && address.getRelationType() != null) {
                    if (EdsCrmContactItemParams.HOME == address.getRelationType()) {
                        homeAddresses.add(address);
                    } else if (EdsCrmContactItemParams.WORK == address.getRelationType()) {
                        workAddresses.add(address);
                    } else if (EdsCrmContactItemParams.OTHER == address.getRelationType()) {
                        otherAddresses.add(address);
                    }
                }
            }
        }
        return workAddresses.size() > 0 ? workAddresses.get(0) : (homeAddresses.size() > 0 ? homeAddresses.get(0) : (otherAddresses.size() > 0 ? otherAddresses.get(0) : (null)));
    }

    public String getPrimaryPhoneFromAll() {
        List<String> homePhones = new ArrayList<>();
        List<String> workPhones = new ArrayList<>();
        List<String> mobilePhones = new ArrayList<>();
        List<String> homeFaxes = new ArrayList<>();
        List<String> workFaxes = new ArrayList<>();
        List<String> pagers = new ArrayList<>();
        List<String> otherPhones = new ArrayList<>();
        List<String> faxes = new ArrayList<>();
        List<String> whatApps = new ArrayList<>();
        List<String> telegram = new ArrayList<>();
        List<String> vibers = new ArrayList<>();
        if (itemParams != null && itemParams.size() > 0) {
            for (EdsCrmContactItemParams itemParam : getItemParams(EdsCrmContactItemParams.PHONE)) {
                if (itemParam != null && itemParam.getRelation() != null && !"".equals(itemParam.getValue())) {
                    if (EdsCrmContactItemParams.HOME == itemParam.getRelation()) {
                        homePhones.add(itemParam.getValue());
                    } else if (EdsCrmContactItemParams.WORK == itemParam.getRelation()) {
                        workPhones.add(itemParam.getValue());
                    } else if (EdsCrmContactItemParams.MOBILE == itemParam.getRelation()) {
                        mobilePhones.add(itemParam.getValue());
                    } else if (EdsCrmContactItemParams.HOME_FAX == itemParam.getRelation()) {
                        homeFaxes.add(itemParam.getValue());
                    } else if (EdsCrmContactItemParams.WORK_FAX == itemParam.getRelation()) {
                        workFaxes.add(itemParam.getValue());
                    } else if (EdsCrmContactItemParams.PAGER == itemParam.getRelation()) {
                        pagers.add(itemParam.getValue());
                    } else if (EdsCrmContactItemParams.OTHER == itemParam.getRelation()) {
                        otherPhones.add(itemParam.getValue());
                    } else if (Constants.G_FAX == itemParam.getRelation()) {
                        faxes.add(itemParam.getValue());
                    } else if (Constants.G_WHATS_APP == itemParam.getRelation()) {
                        whatApps.add(itemParam.getValue());
                    } else if (Constants.G_TELEGRAM == itemParam.getRelation()) {
                        telegram.add(itemParam.getValue());
                    } else if (Constants.G_VIBER == itemParam.getRelation()) {
                        vibers.add(itemParam.getValue());
                    }
                }
            }
        }
        return getPrimary(homePhones, workPhones, mobilePhones, homeFaxes, workFaxes, pagers, otherPhones, faxes, whatApps, telegram, vibers);

    }

    public String getPrimaryTelegram() {
        List<String> telegram = new ArrayList<>();
        for (EdsCrmContactItemParams itemParam : getItemParams(EdsCrmContactItemParams.G_TELEGRAM)) {
            if (Constants.G_TELEGRAM == itemParam.getRelation()) {
                telegram.add(itemParam.getValue());
            }
        }
        return getPrimary(telegram);
    }

    public String getMobileNumberOrFirst() {
        if (itemParams != null && itemParams.size() > 0) {
            for (EdsCrmContactItemParams itemParam : getItemParams(EdsCrmContactItemParams.PHONE)) {
                if (itemParam != null && itemParam.getRelation() != null && !"".equals(itemParam.getValue())) {
                    if (EdsCrmContactItemParams.MOBILE == itemParam.getRelation()) {
                        return itemParam.getValue();
                    }
                }
            }
            for (EdsCrmContactItemParams itemParam : getItemParams(EdsCrmContactItemParams.PHONE)) {
                if (itemParam != null && itemParam.getRelation() != null && !"".equals(itemParam.getValue())) {
                    return itemParam.getValue();
                }
            }
        }
        return null;
    }

    public String getRelatedPhone(int relation) {
        if (itemParams != null && itemParams.size() > 0) {
            for (EdsCrmContactItemParams itemParam : getItemParams(EdsCrmContactItemParams.PHONE)) {
                if (itemParam != null && itemParam.getRelation() != null && !"".equals(itemParam.getValue())) {
                    if (relation == itemParam.getRelation()) {
                        return itemParam.getValue();
                    }
                }
            }
        }
        return null;
    }

    public ContactListItem getRPC(ListingFilterParameter fp, ContactListItem... items) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        ContactListItem item = new ContactListItem();
        if ((items != null && items.length > 0 && items[0] != null)) {
            item = items[0];
        }
        item.setObjectId(getObjectID());
        item.setGoogleId(getGoogleId());
        item.setContactName(getName());
        item.setFirstName(getFirstName());
        item.setLastName(getLastName());
        item.setOtherName(getOtherName());
        item.setContactType(getContactType());
        item.setEntityContactID(getEntityContactID());
        item.setJobTitle(getJobTitles());
        item.setJobFunction(getJobFunction());
        item.setRefIndNumber(getRefIndNumber());
        item.setTitle(getTitle());
        item.setGender(getGender());
        if (getMartialStatus() != null) {
            item.setMartialStatusId(getMartialStatus().getObjectID());
            item.setMartialStatus(getMartialStatus().getName());
        }
        if (!fp.isHRMS()) {
            HashSet<Integer> trackerIdSet = new HashSet<>();
            for (EdsEmailTracker emailTracker : trackerSet) {
                trackerIdSet.add(emailTracker.getObjectID());
            }
            item.setTrackerIDSet(trackerIdSet);
        }
        if (getTitleRef() != null) {
            item.setTitleId(getTitleRef().getObjectID());
        }
        item.setPrimaryContact(isPrimaryContact());
        item.setShowAccountAddress(isShowAccountAddress());
        item.setDepartment(getDepartment());
        item.setCreatedDate(getAuditInfo().getCreationDate());
        item.setUpdatedDate(getAuditInfo().getModificationDate());
        item.setReportsTo(getReportsTo());
        item.setReportsToId(getReportsToId());
        item.setMiddleName(getMiddleName());
        if (getPhoto() != null) {
            item.setPhotoId(getPhoto().getObjectID());
        }
        if (getCompanyPhoto() != null) {
            item.setCompanyPhotoId(getCompanyPhoto().getObjectID());
        }
        if (getDateOfBirth() != null) {
            item.setBirthDate(new DateNonConvertable(getDateOfBirth()));
        }
        if (getHistorical() != null && getHistorical() && isDeleted() && getAuditInfo() != null && getAuditInfo().getModifiedBy() != null) {
            item.setOwner(getAuditInfo().getModifiedBy().getFullName());
            item.setOwnerId(getAuditInfo().getModifiedBy().getObjectID());
        }
        item.setEmailOptOut(getEmailOptOut() != null ? getEmailOptOut() : false);
        item.setPrimaryEmail(getPrimaryEmail());
        item.setPrimaryPhone(getPrimaryPhone());
        item.setPrimaryAddress(getPrimaryAddressFromAll());
        if (getEntityID() != null) {
            item.setEntityID(getEntityID());
        }
        if (!fp.isBriefly()) {
            if (getCrmAccount() != null && !fp.isHRMS()) {
                item.setCrmAccount(getCrmAccount().getRPC(new CrmAccountItem(), fp.isBriefly()));
            }
            if (!fp.isForCSVonly()) {
                item.setNote(getGooglenote());
                item.setBackgroundInformation(getBackgroundInformation());
                if (getOwner() != null) {
                    item.setOwner(getOwner().getName());
                    item.setOwnerId(getOwner().getObjectID());
                }
                if (getCampaign() != null) {
                    item.setCampaignId(getCampaign().getObjectID());
                    item.setCampaign(getCampaign().getName());
                }
                if (is(EdsCrmContact.CANDIDATE)) {
                    item.setNumberData(new NumberData(getNumber(), getNumberInteger()));
                    item.setCandidateSource(getCandidateSource() != null ? getCandidateSource().getRPC() : null);
                    item.setCandidateStatus(getCandidateStatus() != null ? getCandidateStatus().getRPC() : null);
                    item.setWorkExperience(getWorkExperience());
                    item.setWorkExperienceMonthOrYear(getWorkExperienceMonthOrYear());
                    item.setCurrentEmployer(getCurrentEmployer());
                    item.setExpectedSalary(getExpectedSalary());
                    item.setStartSalary(getStartSalary());
                    item.setSkills(getSkills());
                    item.setPreferredLocation(getPrefferedLocation() != null ? getPrefferedLocation().getAsSelectItem() : null);
                    if (getVacancies().size() > 0 && item.getVacancies() != null && item.getVacancies().size() > 0) {
                        Map<Integer, SelectItem> vacancyMap = SelectItem.asMap(item.getVacancies().toArray(new SelectItem[]{}));
                        for (EdsVacancy vacancy : getVacancies()) {
                            if (vacancyMap.containsKey(vacancy.getObjectID())) {
                                vacancyMap.get(vacancy.getObjectID()).setSelected(true);
                            }
                        }
                        item.setVacancies(new ArrayList<SelectItem>(vacancyMap.values()));
                    }
                    if (getCandidateProject() != null) {
                        item.setProjectItem(new SelectItem(getCandidateProject().getObjectID(), getCandidateProject().getName()));
                    }
                    if (getAllowances() != null) {
                        for (EdsPaymentDeduction allowance : getAllowances()) {
                            item.getAllowanceCategories().add(allowance.getRPC());
                        }
                    }
                } else if (is(EdsCrmContact.LEAD_CONTACT)) {
                    if (getLeadAssignee() != null) {
                        item.setLeadAssigneeID(getLeadAssignee().getObjectID());
                        item.setLeadAssignee(getLeadAssignee().getName());
                    }
                    if (getLeadBackupAssignee() != null) {
                        item.setLeadBackupAssigneeID(getLeadBackupAssignee().getObjectID());
                        item.setLeadBackupAssignee(getLeadBackupAssignee().getName());
                    }
                    if (getLeadSource() != null) {
                        item.setLeadSourceID(getLeadSource().getObjectID());
                        item.setLeadSource(getLeadSource().getName());
                    }
                    if (getOtherLeadSource() != null) {
                        item.setOtherLeadSource(getOtherLeadSource());
                    }
                    if (getLeadStatus() != null) {
                        item.setLeadStatus(getLeadStatus().getRPC());
                        item.setLeadStatusID(getLeadStatus().getObjectID());
                    }
                    if (getLeadRating() != null) {
                        item.setLeadRatingID(getLeadRating().getObjectID());
                        item.setLeadRating(getLeadRating().getLocalizedName());
                    }
                }
                if (getCategories() != null && getCategories().size() > 0) {
                    for (EdsContactCategory category : getCategories()) {
                        item.addSelectedCategories(category.getAsSelectItem());
                    }
                }
                //RelationShips
                item.setSelectedRelationships(getParams(EdsCrmContactItemParams.CONTACT_RELATIONSHIPS));
            }
            //Emails
            Map<Integer, ArrayList<String>> emails = getParams(EdsCrmContactItemParams.EMAIL);
            if (emails.size() > 0) {
                if (emails.containsKey(EdsCrmContactItemParams.HOME)) {
                    item.setHomeEmail(emails.get(EdsCrmContactItemParams.HOME));
                }
                if (emails.containsKey(EdsCrmContactItemParams.WORK)) {
                    item.setWorkEmail(emails.get(EdsCrmContactItemParams.WORK));
                }
                if (emails.containsKey(EdsCrmContactItemParams.OTHER)) {
                    item.setOtherEmail(emails.get(EdsCrmContactItemParams.OTHER));
                }
            }
            //IM_ADDRESSES
            item.setSelectedContactImAddress(getParams(EdsCrmContactItemParams.CONTACT_IMADDRESSES));

            //Websites
            Map<Integer, ArrayList<String>> webSites = getParams(EdsCrmContactItemParams.WEBSITE);
            if (webSites.size() > 0) {
                if (webSites.containsKey(EdsCrmContactItemParams.HOME)) {
                    item.setHomeWebSite(webSites.get(EdsCrmContactItemParams.HOME));
                }
                if (webSites.containsKey(EdsCrmContactItemParams.WORK)) {
                    item.setWorkWebSite(webSites.get(EdsCrmContactItemParams.WORK));
                }
                if (webSites.containsKey(EdsCrmContactItemParams.HOME_PAGE)) {
                    item.setHomePage(webSites.get(EdsCrmContactItemParams.HOME_PAGE));
                }
                if (webSites.containsKey(EdsCrmContactItemParams.FTP)) {
                    item.setFtp(webSites.get(EdsCrmContactItemParams.FTP));
                }
                if (webSites.containsKey(EdsCrmContactItemParams.BLOG)) {
                    item.setBlog(webSites.get(EdsCrmContactItemParams.BLOG));
                }
                if (webSites.containsKey(EdsCrmContactItemParams.PROFILE)) {
                    item.setProfileWebSite(webSites.get(EdsCrmContactItemParams.PROFILE));
                }
                if (webSites.containsKey(EdsCrmContactItemParams.OTHER)) {
                    item.setOtherWebSite(webSites.get(EdsCrmContactItemParams.OTHER));
                }
                if (webSites.containsKey(EdsCrmContactItemParams.LINKEDIN)) {
                    item.setLinkedinWebSite(webSites.get(EdsCrmContactItemParams.LINKEDIN));
                }
                if (webSites.containsKey(EdsCrmContactItemParams.FACEBOOK)) {
                    item.setFacebookWebSite(webSites.get(EdsCrmContactItemParams.FACEBOOK));
                }
                if (webSites.containsKey(EdsCrmContactItemParams.TWITTER)) {
                    item.setTwitterWebSite(webSites.get(EdsCrmContactItemParams.TWITTER));
                }
                if (webSites.containsKey(EdsCrmContactItemParams.INSTAGRAM)) {
                    item.setInstagramWebSite(webSites.get(EdsCrmContactItemParams.INSTAGRAM));
                }
            }

            //Phones
            Map<Integer, ArrayList<String>> phones = getParams(EdsCrmContactItemParams.PHONE);
            if (phones.size() > 0) {
                if (phones.containsKey(EdsCrmContactItemParams.HOME)) {
                    item.setHomePhone(phones.get(EdsCrmContactItemParams.HOME));
                }
                if (phones.containsKey(EdsCrmContactItemParams.WORK)) {
                    item.setWorkPhone(phones.get(EdsCrmContactItemParams.WORK));
                }
                if (phones.containsKey(EdsCrmContactItemParams.MOBILE)) {
                    item.setMobile(phones.get(EdsCrmContactItemParams.MOBILE));
                }
                if (phones.containsKey(EdsCrmContactItemParams.HOME_FAX)) {
                    item.setHomeFax(phones.get(EdsCrmContactItemParams.HOME_FAX));
                }
                if (phones.containsKey(EdsCrmContactItemParams.WORK_FAX)) {
                    item.setWorkFax(phones.get(EdsCrmContactItemParams.WORK_FAX));
                }
                if (phones.containsKey(EdsCrmContactItemParams.PAGER)) {
                    item.setPager(phones.get(EdsCrmContactItemParams.PAGER));
                }
                if (phones.containsKey(EdsCrmContactItemParams.OTHER)) {
                    item.setOtherPhone(phones.get(EdsCrmContactItemParams.OTHER));
                }
                if (phones.containsKey(EdsCrmContactItemParams.EXTENSION)) {
                    item.setExtension(phones.get(EdsCrmContactItemParams.EXTENSION));
                }
                if (phones.containsKey(Constants.G_FAX)) {
                    item.setFax(phones.get(Constants.G_FAX));
                }
                if (phones.containsKey(Constants.G_WHATS_APP)) {
                    item.setWhatsApp(phones.get(Constants.G_WHATS_APP));
                }
                if (phones.containsKey(Constants.G_TELEGRAM)) {
                    item.setTelegram(phones.get(Constants.G_TELEGRAM));
                }
                if (phones.containsKey(Constants.G_VIBER)) {
                    item.setViber(phones.get(Constants.G_VIBER));
                }
            }

            //addresses
            if (getAddresses() != null && getAddresses().size() > 0) {
                item.getAddresses().clear();
                for (EdsAddress address : getAddresses()) {
                    item.getAddresses().add(address.getRPC());
                }
            }
        }
        return item;
    }

    public ContactSolrItem getSolrRPC() {
        ContactSolrItem item = new ContactSolrItem();

        item.setObjectId(getObjectID());
        item.setContactName(getName());
        item.setContactType(getContactType());
        item.setPrimaryContact(getPrimaryContact());
        item.setFirstName(getFirstName());
        item.setMiddleName(getMiddleName());
        item.setLastName(getLastName());
        item.setRefIndNumber(getRefIndNumber());
        item.setTitle(getTitle());
        item.setJobTitle(getJobTitles());
        item.setExtension(EdsCrmContactItemParams.getFirstItemParamValue(getItemParams(EdsCrmContactItemParams.PHONE), false, EdsCrmContactItemParams.EXTENSION));
        item.setPrimaryEmail(getPrimaryEmail());
        item.setPrimaryPhone(getPrimaryPhone());
        item.setFax(EdsCrmContactItemParams.getFirstItemParamValue(getItemParams(EdsCrmContactItemParams.PHONE), false, EdsCrmContactItemParams.HOME_FAX, EdsCrmContactItemParams.WORK_FAX));
        item.setMobile(EdsCrmContactItemParams.getFirstItemParamValue(getItemParams(EdsCrmContactItemParams.PHONE), false, EdsCrmContactItemParams.MOBILE));
        item.setWorkPhone(EdsCrmContactItemParams.getFirstItemParamValue(getItemParams(EdsCrmContactItemParams.PHONE), false, EdsCrmContactItemParams.WORK));
        item.setWebsite(EdsCrmContactItemParams.getFirstItemParamValue(getItemParams(EdsCrmContactItemParams.WEBSITE), true));
        item.setDepartment(getDepartment());
        item.setUpdateDate(getAuditInfo().getModificationDate());
        item.setCreationDate(getAuditInfo().getCreationDate());
        item.setDateOfBirth(getDateOfBirth());
        item.setReportsTo(getReportsTo());
        item.setReportsToId(getReportsToId());
        item.setEmailAllowed(getEmailOptOut());
        item.setGoogleId(getGoogleId());
        item.setLeadKanbanOrder(getKanbanorder());
        item.setFavourite(getFavourited());

        if (getCreator() != null) {
            item.setCreator(getCreator().getAsSelectItem());
        }

        if (getAuditInfo() != null && getAuditInfo().getModifiedBy() != null) {
            item.setUpdater(getAuditInfo().getModifiedBy().getAsSelectItem());
        }

        StringBuilder categoryNames = new StringBuilder();
        if (getCategories() != null && !getCategories().isEmpty()) {
            for (EdsContactCategory category : getCategories()) {
                item.getCategory().add(category.getAsSelectItem());
                if ("".equals(categoryNames.toString())) {
                    categoryNames.append(category.getName() != null ? category.getName() : "");
                }
            }
            item.setCategoryNameSort(categoryNames.toString());
        }
        if (getOwner() != null) {
            item.setOwner(getOwner().getAsSelectItem());
        }
        if (getCrmAccount() != null) {
            SelectItem selectItem = new SelectItem(getCrmAccount().getObjectID(), getCrmAccount().getName());
            selectItem.setNumber(getCrmAccount().getNumber());
            item.setAccount(selectItem);
            if (getCrmAccount().getIndustry() != null) {
                item.setAccountIndustry(getCrmAccount().getIndustry().getAsSelectItem());
            }
            if (getCrmAccount().getOwners() != null) {
                getCrmAccount().getOwners().forEach(owner -> item.getAccountOwnerId().add(owner.getObjectID()));
            }
            if (getCrmAccount().getAccountTypes() != null) {
                getCrmAccount().getAccountTypes().forEach(edsReference -> item.getAccountType().add(edsReference.getCode()));
            }
        }
        if (Boolean.TRUE.equals(isAccessEnabled())) {
            item.setAccessEnabled(isAccessEnabled());
        } else {
            item.setAccessEnabled(Boolean.FALSE);
        }
        if (is(EdsCrmContact.CANDIDATE)) {
            item.setCandidate(Boolean.TRUE);
            if (getLeadStatus() != null) {
                item.setStatus(getLeadStatus().getRPC());
            }
            if (getLeadSource() != null) {
                item.setLeadSource(getLeadSource().getRPC());
            }
            item.setNumber(getNumber());
            item.setWorkExperience(getWorkExperience());
            item.setWorkExperienceMonthYear(getWorkExperienceMonthOrYear());
            item.setCurrentEmployer(getCurrentEmployer());
            item.setExpectedSalary(getExpectedSalary());
            item.setShortList(getShortList());
            item.setCandidateSkills(getSkills());
            if (getCandidateProject() != null) {
                item.setCandidateProject(getCandidateProject().getAsSelectItem());
            }
            if (getPrefferedLocation() != null) {
                item.setPreferredLocation(getPrefferedLocation().getAsSelectItem());
            }
            if (getCandidateDepartment() != null) {
                item.setCandidateDepartment(getCandidateDepartment().getAsSelectItem());
            }
            if (getCandidatePosition() != null) {
                item.setCandidatePosition(getCandidatePosition().getAsSelectItem());
            }
            if (getVacancies() != null && !getVacancies().isEmpty()) {
                item.setVacancy(getVacancies().stream().map(vacancy -> vacancy.getAsSelectItem()).toList());
            }
            if (getLeadStatus() != null) {
                item.setCandidateStatus(getLeadStatus().getAsSelectItem());
            }
        } else if (is(EdsCrmContact.LEAD_CONTACT)) {
            item.setLead(Boolean.TRUE);
            if (getLeadAssignee() != null) {
                item.setAssignee(getLeadAssignee().getAsSelectItem());
            }
            if (getLeadBackupAssignee() != null) {
                item.setBackupAssignee(getLeadBackupAssignee().getAsSelectItem());
            }
            if (getLeadRating() != null) {
                SelectItem rating = new SelectItem(getLeadRating().getObjectID(), getLeadRating().getName());
                rating.setCode(getLeadRating().getCode());
                item.setRating(rating);
            }
            if (getLeadSource() != null) {
                SelectItem leadSource = new SelectItem(getLeadSource().getObjectID(), getLeadSource().getName());
                leadSource.setCode(getLeadSource().getCode());
                item.setLeadSource(leadSource);
            }
            item.setLeadSourceOther(getOtherLeadSource());
            if (getLeadStatus() != null) {
                item.setStatus(getLeadStatus().getRPC());
            }
        }
        if (getCampaign() != null) {
            item.setCampaign(getCampaign().getAsSelectItem());
        }
        Address primaryAddress = getPrimaryAddressFromAll();
        if (primaryAddress != null) {
            if (primaryAddress.getCountryId() != null) {
                SelectItem country = new SelectItem(primaryAddress.getCountryId(), primaryAddress.getCountry());
                country.setCode(primaryAddress.getCountryCode());
                item.setCountry(country);
            }
            if (primaryAddress.getStateId() != null) {
                item.setState(new SelectItem(primaryAddress.getStateId(), primaryAddress.getState()));
            }
            item.setCity(primaryAddress.getCity());
            item.setStreet(primaryAddress.getAddress());
            item.setStreet2(primaryAddress.getAddressb());
            item.setPostCode(primaryAddress.getZipCode());
            item.setLongitude(primaryAddress.getLongitude());
            item.setLatitude(primaryAddress.getLatitude());
        }

        return item;
    }

    public static EdsCrmContact getPreferedItem(List<EdsCrmContact> contacts, String... emails) {
        EdsCrmContact contact = null;
        String email = null;
        if (emails != null && emails.length > 0 && emails[0] != null && !"".equals(emails[0])) {
            email = emails[0];
        }
        if (contacts != null && contacts.size() > 0) {
            contact = contacts.get(0);
            if (contacts.size() > 1 && email != null && !"".equals(email)) {
                for (EdsCrmContact crmContact : contacts) {
                    if (crmContact != null && email.equals(crmContact.getPrimaryEmail())) {
                        contact = crmContact;
                    }
                }
            }
        }
        return contact;
    }

    public static void removeContactItemParams(final EdsCrmContact contact, int paramType) {
        if (contact != null) {
            if (paramType != -1) {
                if (contact.getItemParams() != null && contact.getItemParams().size() > 0) {
                    List<EdsCrmContactItemParams> contactParams = contact.getItemParams(paramType);
                    contact.getItemParams().removeAll(contactParams);
                }
            }
        }
    }

    /**
     * please do not use this method.
     * It's not optimized
     *
     * @param edsContactCategory
     */
    @Deprecated
    public void removeCategory(EdsContactCategory edsContactCategory) {
        if (edsContactCategory != null) {
            edsContactCategory.getContacts().remove(this);
            getCategories().remove(edsContactCategory);
        }
    }

    public boolean is(Integer employeeContact) {
        return getContactType() != null && getContactType().equals(employeeContact);
    }

    public List<Integer> getCateogryIDs() {
        if (cateogryIDs == null) {
            cateogryIDs = new ArrayList<>();
        }
        return cateogryIDs;
    }

    public void setCateogryIDs(List<Integer> cateogryIDs) {
        this.cateogryIDs = cateogryIDs;
    }

    public void addCategoryID(Integer contactCategoryID) {
        if (!getCateogryIDs().contains(contactCategoryID)) {
            getCateogryIDs().add(contactCategoryID);
        }
    }

    public String getFieldAsString(Integer savingField) {
        if (savingField.equals(LeadField.FIELD_ASSIGNEE)) {
            if (getLeadAssignee() != null) {
                return getLeadAssignee().getFullName();
            }
        }
        if (savingField.equals(LeadField.FIELD_BACKUP_ASSIGNEE)) {
            if (getLeadBackupAssignee() != null) {
                return getLeadBackupAssignee().getFullName();
            }
        }
        if (savingField.equals(LeadField.FIELD_COMPANYNAME)) {
            if (getCrmAccount() != null && getCrmAccount().getName() != null && !"".equals(getCrmAccount().getName())) {
                return getCrmAccount().getName();
            }
        }
        if (savingField.equals(LeadField.FIELD_FIRSTNAME)) {
            if (isNotEmpty(getFirstName())) {
                return getFirstName();
            }
        }
        if (savingField.equals(LeadField.FIELD_LASTNAME)) {
            if (isNotEmpty(getLastName())) {
                return getLastName();
            }
        }
        if (savingField.equals(LeadField.FIELD_JOBTITLE)) {
            if (isNotEmpty(getJobTitles())) {
                return getJobTitles();
            }
        }
        if (savingField.equals(LeadField.FIELD_STREET)) {
            if (getPrimaryAddressFromAll() != null && isNotEmpty(getPrimaryAddressFromAll().getAddress())) {
                return getPrimaryAddressFromAll().getAddress();
            }
        }
        if (savingField.equals(LeadField.FIELD_CITYTOWN)) {
            if (getPrimaryAddressFromAll() != null && isNotEmpty(getPrimaryAddressFromAll().getCity())) {
                return getPrimaryAddressFromAll().getCity();
            }
        }
        if (savingField.equals(LeadField.FIELD_POSTCODE)) {
            if (getPrimaryAddressFromAll() != null && isNotEmpty(getPrimaryAddressFromAll().getZipCode())) {
                return getPrimaryAddressFromAll().getZipCode();
            }
        }
        if (savingField.equals(LeadField.FIELD_COUNTRY)) {
            if (getPrimaryAddressFromAll() != null && isNotEmpty(getPrimaryAddressFromAll().getCountry())) {
                return getPrimaryAddressFromAll().getCountry();
            }
        }
        if (savingField.equals(LeadField.FIELD_LEADSOURCE)) {
            if (getLeadSource() != null) {
                if (LEAD_SOURCE_OTHER.equals(getLeadSource().getCode()) && getOtherLeadSource() != null) {
                    return getLeadSource().getName() + "/" + getOtherLeadSource();
                }
                return getLeadSource().getName();
            }
        }
        if (savingField.equals(LeadField.FIELD_CAMPAIGNSOURCE)) {
            if (getCampaign() != null) {
                return getCampaign().getName();
            }
        }
        if (savingField.equals(LeadField.FIELD_LEADSTATUS)) {
            if (getLeadStatus() != null) {
                return getLeadStatus().getName();
            }
        }
        if (savingField.equals(LeadField.FIELD_INDUSTRY)) {
            if (getCrmAccount() != null && getCrmAccount().getIndustry() != null) {
                return getCrmAccount().getIndustry().getName();
            }
        }
        if (savingField.equals(LeadField.FIELD_EMAIL1)) {
            return getNotEmptyFirst(getItemParams(EdsCrmContactItemParams.EMAIL));
        }
        if (savingField.equals(LeadField.FIELD_EMAIL2)) {
            String email = EdsCrmContactItemParams.getFirstItemParamValue(getItemParams(EdsCrmContactItemParams.EMAIL), false, EdsCrmContactItemParams.WORK);
            if (email != null && !"".equals(email)) {
                return email;
            }
        }
        if (savingField.equals(LeadField.FIELD_EMAIL3)) {
            String email = EdsCrmContactItemParams.getFirstItemParamValue(getItemParams(EdsCrmContactItemParams.EMAIL), false, EdsCrmContactItemParams.OTHER);
            if (email != null && !"".equals(email)) {
                return email;
            }
        }
        if (savingField.equals(LeadField.FIELD_PHONE1)) {
            String tel = getNotEmptyFirst(getItemParams(EdsCrmContactItemParams.PHONE)).replace("|", "");
            if (tel != null && !"".equals(tel) && !"N/A".equals(tel) && !tel.contains("+")) {
                tel = "+" + tel;
            }
            return tel;
        }
        if (savingField.equals(LeadField.FIELD_PHONE2)) {
            String tel = EdsCrmContactItemParams.getFirstItemParamValue(getItemParams(EdsCrmContactItemParams.PHONE), false, EdsCrmContactItemParams.WORK);
            if (tel != null && !"".equals(tel)) {
                tel = tel.replace("|", "");
            }
            if (tel != null && !"".equals(tel) && !"N/A".equals(tel) && !tel.contains("+")) {
                tel = "+" + tel;
            }
            return tel;
        }
        if (savingField.equals(LeadField.FIELD_PHONE3)) {
            String tel = EdsCrmContactItemParams.getFirstItemParamValue(getItemParams(EdsCrmContactItemParams.PHONE), false, EdsCrmContactItemParams.OTHER);
            if (tel != null && !"".equals(tel)) {
                tel = tel.replace("|", "");
            }
            if (tel != null && !"".equals(tel) && !"N/A".equals(tel) && !tel.contains("+")) {
                tel = "+" + tel;
            }
            return tel;
        }
        if (savingField.equals(LeadField.FIELD_FAX)) {
            String tel = getNotEmptyFirst(getItemParams(EdsCrmContactItemParams.PHONE), EdsCrmContactItemParams.HOME_FAX).replace("|", "");
            if (tel != null && !"".equals(tel)) {
                tel = tel.replace("|", "");
            }
            if (tel != null && !"".equals(tel) && !"N/A".equals(tel) && !tel.contains("+")) {
                tel = "+" + tel;
            }
            return tel;
        }
        if (savingField.equals(LeadField.FIELD_MOBILE)) {
            String tel = getNotEmptyFirst(getItemParams(EdsCrmContactItemParams.PHONE), EdsCrmContactItemParams.MOBILE).replace("|", "");
            if (tel != null && !"".equals(tel)) {
                tel = tel.replace("|", "");
            }
            if (tel != null && !"".equals(tel) && !"N/A".equals(tel) && !tel.contains("+")) {
                tel = "+" + tel;
            }
            return tel;
        }
        if (savingField.equals(LeadField.FIELD_WEBSITE)) {
            return getNotEmptyFirst(getItemParams(EdsCrmContactItemParams.WEBSITE));
        }
        if (savingField.equals(LeadField.FIELD_EMAILOPTOUT)) {
            if (getEmailOptOut() != null && getEmailOptOut()) {
                return "Yes";
            } else {
                return "No";
            }
        }
        if (savingField.equals(LeadField.FIELD_RATING)) {
            if (getLeadRating() != null) {
                return getLeadRating().getName();
            }
        }
        if (savingField.equals(LeadField.FIELD_NOTE)) {
            if (getNote() != null) {
                return getNote();
            }
        }
        if (savingField.equals(LeadField.FIELD_STATE)) {
            if (getPrimaryAddressFromAll() != null && isNotEmpty(getPrimaryAddressFromAll().getState())) {
                return getPrimaryAddressFromAll().getState();
            }
        }
        return "N/A";
    }

    private String getNotEmptyFirst(List<EdsCrmContactItemParams> itemParams, int... relation) {
        String value = EdsCrmContactItemParams.getFirstItemParamValue(itemParams, true, relation);
        if (value == null || "".equals(value.trim())) {
            value = "N/A";
        }
        return value;
    }

    private boolean isNotEmpty(String value) {
        return value != null && !"".equals(value);
    }

    public Integer getClientID() {
        return crmAccount != null ? crmAccount.getClientID() : null;
    }

    public CompanyCustomFieldItem[] getTransientCustomFields() {
        return transientCustomFields;
    }

    public void setTransientCustomFields(CompanyCustomFieldItem[] transientCustomFields) {
        this.transientCustomFields = transientCustomFields;
    }

    public EdsReference getTitleRef() {
        return titleRef;
    }


    public void setTitleRef(EdsReference titleRef) {
        this.titleRef = titleRef;
    }

    public List<EdsCrmContact> getHistories() {
        return histories;
    }

    public void setHistories(List<EdsCrmContact> histories) {
        this.histories = histories;
    }

    public List<EdsPaymentDeduction> getAllowances() {
        return allowances;
    }

    public void setAllowances(List<EdsPaymentDeduction> allowances) {
        this.allowances = allowances;
    }

    public EdsCrmContact getHistoricalParent() {
        return historicalParent;
    }

    public void setHistoricalParent(EdsCrmContact historicalParent) {
        this.historicalParent = historicalParent;
    }

    public Boolean getHistorical() {
        return historical;
    }

    public void setHistorical(Boolean historical) {
        this.historical = historical;
    }

    public EdsAuditInfo getAuditInfo() {
        if (auditInfo == null) {
            auditInfo = new EdsAuditInfo();
        }
        return auditInfo;
    }

    public void setAuditInfo(EdsAuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    public boolean isPropertiesChanged() {
        return propertiesChanged;
    }

    public void setPropertiesChanged(boolean propertiesChanged) {
        this.propertiesChanged = propertiesChanged;
    }

    public String getSaasuGUID() {
        return saasuGUID;
    }

    public void setSaasuGUID(String saasuGUID) {
        this.saasuGUID = saasuGUID;
    }

    public String getRefIndNumber() {
        return refIndNumber;
    }

    public void setRefIndNumber(String refIndNumber) {
        this.refIndNumber = refIndNumber;
    }

    public ClientContact getAsClientContact() {
        ClientContact contactItem = new ClientContact();
        contactItem.setObjectID(getObjectID());
        contactItem.setFirstName(getFirstName());
        contactItem.setLastName(getLastName());
        contactItem.setEmail(getPrimaryEmail());
        contactItem.setPosition(getJobTitles());
        contactItem.setPhone(getPrimaryPhone());
        contactItem.setActive(isAccessEnabled());
        contactItem.setPrimaryContact(getPrimaryContact());
        contactItem.setRefIndNumber(getRefIndNumber());
        return contactItem;
    }

    public String getFieldLabelByCode(String fieldID) {
        if ("NUMBER".equals(fieldID)) {
            return "Number";
        } else if ("OWNER".equals(fieldID)) {
            return "Owner";
        } else if ("FIRST_NAME".equals(fieldID)) {
            return "First Name";
        } else if ("LAST_NAME".equals(fieldID)) {
            return "Last Name";
        } else if ("BIRTH_DAY".equals(fieldID)) {
            return "Birth Day";
        } else if ("CREATED_DATE".equals(fieldID)) {
            return "Created Date";
        } else if ("LEAD_SOURCE".equals(fieldID)) {
            return "Source";
        } else if ("VACANCIES".equals(fieldID)) {
            return "Vacancies";
        } else if ("WORK_EXPERIENCE".equals(fieldID)) {
            return "Work Experience";
        } else if ("CURRENT_EMPLOYER".equals(fieldID)) {
            return "Current Employer";
        } else if ("EXPECTED_SALARY".equals(fieldID)) {
            return "Expected Salary";
        } else if ("STATUS".equals(fieldID)) {
            return "Status";
        } else if ("LOCATION".equals(fieldID)) {
            return "Location";
        } else if ("SKILLS".equals(fieldID)) {
            return "Skills";
        } else if ("EMAIL".equals(fieldID)) {
            return "Email(s)";
        } else if ("PHONE".equals(fieldID)) {
            return "Phone(s)";
        } else if ("IM_ADDRESS".equals(fieldID)) {
            return "IM-Address";
        } else if ("WEB_ADDRESS".equals(fieldID)) {
            return "Web-Address";
        } else if ("CRM_NOTE".equals(fieldID)) {
            return "Note";
        } else if ("ADDRESS".equals(fieldID)) {
            return "Addresss";
        } else if ("ATTACHMENTS".equals(fieldID)) {
            return "Attachments";
        }
        return "";
    }

    public String getFieldValueByCode(String fieldID) {
        if ("NUMBER".equals(fieldID)) {
            return getNumber();
        } else if ("OWNER".equals(fieldID)) {
            return getOwner() != null ? getOwner().getFullName() : "N/A";
        } else if ("FIRST_NAME".equals(fieldID)) {
            return getFirstName();
        } else if ("LAST_NAME".equals(fieldID)) {
            return getLastName();
        } else if ("BIRTH_DAY".equals(fieldID)) {
            return getDateOfBirth() != null ? ServerUtils.dateFormat(getDateOfBirth(), "yyyy-MM-dd") : "N/A";
        } else if ("CREATED_DATE".equals(fieldID)) {
            return getAuditInfo() != null && getAuditInfo().getCreationDate() != null ? ServerUtils.dateFormat(getAuditInfo().getCreationDate(), "yyyy-MM-dd") : "N/A";
        } else if ("LEAD_SOURCE".equals(fieldID)) {
            return getCandidateSource() != null ? getCandidateSource().getName() : "N/A";
        } else if ("VACANCIES".equals(fieldID)) {
            if (getVacancies() == null || getVacancies().size() == 0) {
                return "N/A";
            }
            StringBuilder vacancies = new StringBuilder();
            boolean appendComma = false;
            for (EdsVacancy vacancy : getVacancies()) {
                if (appendComma) {
                    vacancies.append(",");
                }
                vacancies.append(vacancy.getJobTitle());
                appendComma = true;
            }
            return vacancies.toString();
        } else if ("WORK_EXPERIENCE".equals(fieldID)) {
            String workExperienceT = getWorkExperience() != null ? getWorkExperience().toString() : "N/A";
            if (getWorkExperienceMonthOrYear() != null) {
                if (getWorkExperience() != 0 && getWorkExperience() > 0) {
                    if (getWorkExperienceMonthOrYear() != null && getWorkExperienceMonthOrYear() == 1) {
                        workExperienceT = getWorkExperience().toString() + " " + "Month";
                    } else if (getWorkExperienceMonthOrYear() != null && getWorkExperienceMonthOrYear() == 2) {
                        if (getWorkExperience() == 1) {
                            workExperienceT = getWorkExperience().toString() + " " + "Year";//localization
                        } else {
                            workExperienceT = getWorkExperience().toString() + " " + "Years";//localization
                        }
                    }
                }
            }
            return workExperienceT;
        } else if ("CURRENT_EMPLOYER".equals(fieldID)) {
            return getCurrentEmployer();
        } else if ("EXPECTED_SALARY".equals(fieldID)) {
            return getExpectedSalary() != null ? getExpectedSalary().toString() : "N/A";
        } else if ("STATUS".equals(fieldID)) {
            return getCandidateStatus() != null ? getCandidateStatus().getName() : "N/A";
        } else if ("LOCATION".equals(fieldID)) {
            return getPrefferedLocation() != null ? getPrefferedLocation().getAsSelectItem().getName() : "N/A";
        } else if ("SKILLS".equals(fieldID)) {
            return getSkills();
        } else if ("EMAIL".equals(fieldID)) {
            return getNotEmptyFirst(getItemParams(EdsCrmContactItemParams.EMAIL));
        } else if ("PHONE".equals(fieldID)) {
            return getNotEmptyFirst(getItemParams(EdsCrmContactItemParams.PHONE)).replace("|", "");
        } else if ("IM_ADDRESS".equals(fieldID)) {
            return getNotEmptyFirst(getItemParams(EdsCrmContactItemParams.IMADDRESS));
        } else if ("WEB_ADDRESS".equals(fieldID)) {
            return getNotEmptyFirst(getItemParams(EdsCrmContactItemParams.WEBSITE));
        } else if ("CRM_NOTE".equals(fieldID)) {
            return getNote();
        } else if ("ADDRESS".equals(fieldID)) {
            if (getPrimaryAddressFromAll() != null && isNotEmpty(getPrimaryAddressFromAll().getState())) {
                return getPrimaryAddressFromAll().toString();
            }
        }
        return null;
    }

    public List<String> getEmails() {
        ArrayList<String> emails = new ArrayList<>();
        if (getPrimaryEmail() != null) {
            emails.add(getPrimaryEmail());
        }
        List<EdsCrmContactItemParams> list = getItemParams(EdsCrmContactItemParams.EMAIL);
        if (list != null && list.size() > 0) {
            for (EdsCrmContactItemParams param : list) {
                if (param != null && param.getValue() != null && !emails.contains(param.getValue())) {
                    emails.add(param.getValue());
                }
            }
        }
        return emails;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    protected String getStringValueByFieldID(String fieldID) {
        if (fieldID != null) {
            if (CustomFormConstants.PHONE.equals(fieldID)) {
                return getPrimaryPhone() != null ? getPrimaryPhone().replace("|", "") : "";
            }
        }
        return super.getStringValueByFieldID(fieldID);
    }

    @Override
    public Object getRealValue(String fieldID) {
        if (fieldID == null) {
            return null;
        }
        if (fieldID.equals(CustomFormConstants.TITLE)) {
            return getTitle();
        } else if (fieldID.equals(CustomFormConstants.STATUS)) {
            return getLeadStatus();
        } else if (fieldID.equals(CustomFormConstants.LEAD_OWNER)) {
            return getOwner();
        } else if (fieldID.equals(CustomFormConstants.ASSIGNEE)) {
            return getLeadAssignee();
        } else if (fieldID.equals(CustomFormConstants.BACKUP_ASSIGNEE)) {
            return getLeadBackupAssignee();
        } else if (fieldID.equals(CustomFormConstants.FIRST_NAME)) {
            return getFirstName();
        } else if (fieldID.equals(CustomFormConstants.LAST_NAME)) {
            return getLastName();
        } else if (fieldID.equals(CustomFormConstants.JOB_TITLE)) {
            return getJobTitles();
        } else if (fieldID.equals(CustomFormConstants.BIRTH_DAY)) {
            return getDateOfBirth();
        } else if (fieldID.equals(CustomFormConstants.PHONE)) {
            return getPrimaryPhone();
        } else if (fieldID.equals(CustomFormConstants.EMAIL)) {
            return getPrimaryEmail();
        } else if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_NAME)) {
            return getCrmAccount() != null ? getCrmAccount().getName() : null;
        } else if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_TYPE)) {
            return getCrmAccount() != null ? getCrmAccount().getAccountTypes() : null;
        } else if (fieldID.equals(CustomFormConstants.LEAD_SOURCE)) {
            return getLeadSource();
        } else if (fieldID.equals(CustomFormConstants.CRM_CAMPAIGN_NAME)) {
            return getCampaign();
        } else if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_INDUSTRY)) {
            return getCrmAccount() != null ? getCrmAccount().getIndustry() : null;
        } else if (fieldID.equals(CustomFormConstants.RATING)) {
            return getLeadRating();
        } else if (fieldID.equals(CustomFormConstants.ADDRESS)) {
            return getAddresses();
        } else if ((fieldID.equals(CustomFormConstants.STREET_ADDRESS1) || fieldID.equals(CustomFormConstants.STREET_ADDRESS2) ||
                fieldID.equals(CustomFormConstants.CITY) || fieldID.equals(CustomFormConstants.STATE) ||
                fieldID.equals(CustomFormConstants.COUNTRY) || fieldID.equals(CustomFormConstants.POST_CODE)) && getEdsPrimaryAddressFromAll() != null) {
            EdsAddress address = getEdsPrimaryAddressFromAll();
            switch (fieldID) {
                case CustomFormConstants.STREET_ADDRESS1 -> {
                    return address.getAddress();
                }
                case CustomFormConstants.STREET_ADDRESS2 -> {
                    return address.getAddressb();
                }
                case CustomFormConstants.CITY -> {
                    return address.getCity();
                }
                case CustomFormConstants.STATE -> {
                    return address.getState();
                }
                case CustomFormConstants.COUNTRY -> {
                    return address.getCountry();
                }
                case CustomFormConstants.POST_CODE -> {
                    return address.getZipCode();
                }
            }
        } else if (fieldID.equals(CustomFormConstants.CREATED_DATE)) {
            return getAuditInfo().getCreationDate();
        } else if (fieldID.equals(CustomFormConstants.UPDATED_DATE)) {
            return getAuditInfo().getModificationDate();
        } else if (fieldID.equals(CustomFormConstants.OWNER)) {
            return getOwner();
        } else if (fieldID.equals(CustomFormConstants.CURRENT_EMPLOYER)) {
            return getCurrentEmployer();
        } else if (fieldID.equals(CustomFormConstants.EXPECTED_SALARY)) {
            return getExpectedSalary();
        } else if (fieldID.equals(CustomFormConstants.SKILLS)) {
            return getSkills();
        } else if (fieldID.equals(CustomFormConstants.CANDIDATE.LOCATION)) {
            return getPrefferedLocation();
        } else if (fieldID.equals(CustomFormConstants.CATEGORY)) {
            return getCategories();
        } else if (fieldID.equals(CustomFormConstants.CANDIDATE.CANDIDATE_PROJECT)) {
            return getCandidateProject();
        } else if (fieldID.contains("string_value") || fieldID.contains("double_value") || fieldID.contains("date_value")) {
            return getCustomFields() != null ? CustomFieldsUtils.getObjectValue(getCustomFields(), fieldID) : "";
        } else if (fieldID.contains(CustomFormConstants.CANDIDATE.VACANCIES)) {
            StringBuilder v = new StringBuilder();
            for (EdsVacancy vacancy : getVacancies()) {
                v.append("Vacancy number: ");
                v.append(vacancy.getVacancyNumber() != null ? vacancy.getVacancyNumber() + "\t" : "");
                v.append("Vacancy name: ");
                v.append(vacancy.getName() != null ? vacancy.getName() + "\t" : "");
                v.append("Position: ");
                v.append(vacancy.getPosition().getName() != null ? vacancy.getPosition().getName() + ".\t" : "");
            }
            return v;
        } else if (fieldID.contains(CustomFormConstants.NUMBER)) {
            return getNumber();
        } else if (fieldID.contains(CustomFormConstants.CANDIDATE.WORK_EXPERIENCE)) {
            return getWorkExperience() != null ? getWorkExperience() : "";
        } else if (fieldID.contains(CustomFormConstants.UPDATED_BY)) {
            return getCreator() != null ? getCreator().getFullName() : "";
        } else if (fieldID.contains(CustomFormConstants.CREATED_BY)) {
            return getCreator() != null ? getCreator().getFullName() : "";
        } else if (fieldID.contains(CustomFormConstants.CANDIDATE.ALLOWANCES)) {
            StringBuilder all = new StringBuilder();
            if (getAllowances() != null) {
                for (EdsPaymentDeduction allowance : getAllowances()) {
                    all.append("Allowances: ");
                    all.append(allowance.getCategory().getName() != null ? allowance.getCategory().getName() + ",\t" : "n/a");
                    all.append("Payment amount: ");
                    all.append(allowance.getPaymentAmount() != null ? allowance.getPaymentAmount() + "\n" : "n/a");
                }
            }
            return all;
        } else if (fieldID.contains(CustomFormConstants.WEB_ADDRESS)) {
            return getCrmAccount() != null ? getCrmAccount().getWebsite() : "";
        } else if (fieldID.contains(CustomFormConstants.MOBILE_NUMBER)) {
            return getRelatedPhone(EdsCrmContactItemParams.MOBILE) != null ? getRelatedPhone(EdsCrmContactItemParams.MOBILE) : "";
        } else if (fieldID.contains(CustomFormConstants.TELEGRAM)) {
            return getPrimaryTelegram() != null ? getPrimaryTelegram() : "";
        } else if (fieldID.contains(CustomFormConstants.CRM_NOTE)) {
            return getNote() != null ? getNote() : "";
        } else if (fieldID.contains("ACCESS_ENABLED")) {
            return isAccessEnabled() != null ? isAccessEnabled().toString() : "false";
        }
        return super.getRealValue(fieldID);
    }

    @Override
    protected boolean hasCustomConditionChecking(String fieldID) {
        if (fieldID != null) {
            switch (fieldID) {
                case CustomFormConstants.ADDRESS, CustomFormConstants.IM_ADDRESS, CustomFormConstants.WEB_ADDRESS, CustomFormConstants.CATEGORY -> {
                    return true;
                }
            }
        }
        return super.hasCustomConditionChecking(fieldID);
    }

    protected boolean checkConditionCustom(String fieldID, String operands, String value) {
        if (fieldID != null) {
            switch (fieldID) {
                case CustomFormConstants.ADDRESS -> {
                    if (getAddresses() != null && getAddresses().size() > 0) {
                        for (EdsAddress address : getAddresses()) {
                            return true;
                        }
                    }
                }
                case CustomFormConstants.IM_ADDRESS, CustomFormConstants.WEB_ADDRESS -> {
                    List<EdsCrmContactItemParams> params = getItemParams(fieldID.equals(CustomFormConstants.WEB_ADDRESS) ? EdsCrmContactItemParams.WEBSITE : EdsCrmContactItemParams.IMADDRESS);
                    if (params != null && params.size() > 0) {
                        for (EdsCrmContactItemParams param : params) {
                            String realValue = param.getValue();
                            if (checkStrings(realValue, operands, value)) {
                                return true;
                            }
                        }
                    }
                }
                case CustomFormConstants.CATEGORY -> {
                    if (getCategories().size() > 0) {
                        for (EdsContactCategory item : getCategories()) {
                            String realValue = item.getName();
                            if (value != null && value.contains("@")) {
                                String[] values = value.split("@");
                                if (values != null && values.length > 0) {
                                    String splitValue = "";
                                    if (values.length >= 2) {
                                        splitValue = values[1];
                                    }
                                    if (checkStrings(realValue, operands, splitValue)) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public String getDomainType() {
        if (is(LEAD_CONTACT)) {
            return RelationItem.TYPE_LEAD;
        } else if (is(EdsCrmContact.CANDIDATE)) {
            return RelationItem.TYPE_CANDIDATE;
        } else {
            return RelationItem.TYPE_CONTACT;
        }
    }

    @Override
    public Map<String, Object> getPropertyValueAsMap() {
        if (getLeadAssignee() != null) {
            addFieldIDValue(EmailTemplateConstants.CRM.ASSIGNEE, getLeadAssignee().getFullName());
        }
        addFieldIDValue(EmailTemplateConstants.ET_FIRST_NAME, StringUtils.capitalize(getFirstName()));
        addFieldIDValue(EmailTemplateConstants.ET_LAST_NAME, getLastName());
        addFieldIDValue(EmailTemplateConstants.ET_EMAIL, getPrimaryEmail());
        addFieldIDValue(EmailTemplateConstants.CRM.CRM_ACCOUNT, getCrmAccount() != null ? getCrmAccount().getName() : null);
        return fieldIDValue;
    }

    @Override
    public Date getCreationDate() {
        return getAuditInfo().getCreationDate();
    }

    @Override
    public Date getModificationDate() {
        return getAuditInfo().getModificationDate();
    }

    @Override
    public void setValueForField(EdsModelField field, Object value) {
        if (field != null && field.getField_ID() != null) {
            String fieldID = field.getField_ID();
            if (fieldID.equals(CustomFormConstants.TITLE)) {
                setTitle((String) value);
            } else if (fieldID.equals(CustomFormConstants.STATUS)) {
                setLeadStatus(value instanceof EdsReference ? (EdsReference) value : getLeadStatus());
            } else if (fieldID.equals(CustomFormConstants.LEAD_OWNER)) {
                if (value instanceof EdsUser) {
                    setOwner((EdsUser) value);
                }
            } else if (fieldID.equals(CustomFormConstants.ASSIGNEE)) {
                setLeadAssignee(value instanceof EdsEmployee ? (EdsEmployee) value : getLeadAssignee());
            } else if (fieldID.equals(CustomFormConstants.BACKUP_ASSIGNEE)) {
                setLeadBackupAssignee(value instanceof EdsEmployee ? (EdsEmployee) value : getLeadBackupAssignee());
            } else if (fieldID.equals(CustomFormConstants.FIRST_NAME)) {
                setFirstName((String) value);
            } else if (fieldID.equals(CustomFormConstants.LAST_NAME)) {
                setLastName((String) value);
            } else if (fieldID.equals(CustomFormConstants.BIRTH_DAY)) {
                setDateOfBirth((Date) value);
            } else if (fieldID.equals(CustomFormConstants.JOB_TITLE)) {
                setJobTitles((String) value);
            } else if (fieldID.equals(CustomFormConstants.PHONE)) {
                setPrimaryPhone((String) value);
            } else if (fieldID.equals(CustomFormConstants.EMAIL)) {
                setPrimaryEmail((String) value);
            } else if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_NAME)) {
                setCrmAccount(value instanceof EdsCrmAccount ? (EdsCrmAccount) value : getCrmAccount());
            } else if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_TYPE)) {
                if (getCrmAccount() != null && value != null && value instanceof EdsReference) {
                    getCrmAccount().getAccountTypes().add((EdsReference) value);
                }
            } else if (fieldID.equals(CustomFormConstants.STATUS)) {
                setLeadStatus(value instanceof EdsReference ? (EdsReference) value : getLeadStatus());
            } else if (fieldID.equals(CustomFormConstants.LEAD_SOURCE)) {
                setLeadSource(value instanceof EdsReference ? (EdsReference) value : getLeadSource());
            } else if (fieldID.equals(CustomFormConstants.CRM_CAMPAIGN_NAME)) {
                setCampaign(value instanceof EdsCampaign ? (EdsCampaign) value : null);
            } else if (fieldID.equals(CustomFormConstants.CURRENT_EMPLOYER)) {
                setCurrentEmployer((String) value);
            } else if (fieldID.equals(CustomFormConstants.EXPECTED_SALARY)) {
                setExpectedSalary((Double) value);
            } else if (fieldID.equals(CustomFormConstants.SKILLS)) {
                setSkills((String) value);
            } else if (fieldID.equals(CustomFormConstants.CANDIDATE.LOCATION)) {
                setPrefferedLocation(value instanceof EdsLocation ? (EdsLocation) value : getPrefferedLocation());
            } else if (fieldID.equals(CustomFormConstants.CRM_ACCOUNT_INDUSTRY)) {
                if (getCrmAccount() != null) {
                    getCrmAccount().setIndustry(value instanceof EdsReference ? (EdsReference) value : getCrmAccount().getIndustry());
                }
            } else if (fieldID.equals(CustomFormConstants.RATING)) {
                setLeadRating(value instanceof EdsReference ? (EdsReference) value : getLeadRating());
            } else if (fieldID.equals(CustomFormConstants.ADDRESS)) {
                EdsAddress address = getAddresses() != null && getAddresses().size() > 0 ? getAddresses().get(0) : null;
                if (address != null) {
                    address.setAddressb((String) value);
                }
            } else if ((fieldID.equals(CustomFormConstants.STREET_ADDRESS1) || fieldID.equals(CustomFormConstants.STREET_ADDRESS2) ||
                    fieldID.equals(CustomFormConstants.CITY) || fieldID.equals(CustomFormConstants.STATE) ||
                    fieldID.equals(CustomFormConstants.COUNTRY) || fieldID.equals(CustomFormConstants.POST_CODE)) && getEdsPrimaryAddressFromAll() != null) {
                EdsAddress address = getEdsPrimaryAddressFromAll();
                switch (fieldID) {
                    case CustomFormConstants.STREET_ADDRESS1 -> address.setAddress((String) value);
                    case CustomFormConstants.STREET_ADDRESS2 -> address.setAddressb((String) value);
                    case CustomFormConstants.CITY -> address.setCity((String) value);
                    case CustomFormConstants.STATE ->
                            address.setState(value instanceof EdsRegion ? (EdsRegion) value : address.getState());
                    case CustomFormConstants.COUNTRY ->
                            address.setCountry(value instanceof EdsCountry ? (EdsCountry) value : address.getCountry());
                    case CustomFormConstants.POST_CODE -> address.setZipCode((String) value);
                }
            } else if (field.isCustomField()) {
                Object ob = CustomFieldsUtils.getObjectValue(getCustomFields(), fieldID);
                if (ob != null) {
                    if (ob instanceof String) {
                        String text = (String) ob;
                        if (!text.equals(value)) {
                            addChange(fieldID);
                        }
                    } else if (ob instanceof Number) {
                        String text = String.valueOf(((Double) ob).intValue());
                        if (!text.equals(value)) {
                            addChange(fieldID);
                        }
                    } else if (ob instanceof Date) {
                        Date date = (Date) ob;
                        if (!date.equals(value)) {
                            addChange(fieldID);
                        }
                    }
                } else {
                    addChange(fieldID);
                }
                Map<String, Object> customFieldsMap = new HashMap<>();
                customFieldsMap.put(fieldID, value);
                CustomFieldsUtils.setDomenObjectFieldChange(getCustomFields(), customFieldsMap, fieldID);
            }
        }
        super.setValueForField(field, value);
    }

    private void checkForCrmAccountDetailsChanged(EdsCrmAccount oldCrmAccount, EdsCrmAccount crmAccount) {
        if (oldCrmAccount != null && crmAccount != null) {
            if (oldCrmAccount.getAccountTypes().size() > 0) {
                boolean typeHasChanged = false;
                for (EdsReference type : crmAccount.getAccountTypes()) {
                    if (!oldCrmAccount.getAccountTypes().contains(type)) {
                        addChange(CustomFormConstants.CRM_ACCOUNT_TYPE);
                        typeHasChanged = true;
                        break;
                    } else {
                        oldCrmAccount.getAccountTypes().remove(type);
                    }
                }
                if (!typeHasChanged && oldCrmAccount.getAccountTypes().size() > 0) {
                    addChange(CustomFormConstants.CRM_ACCOUNT_TYPE);
                }
            }
            if (!ServerUtils.equalsReference(oldCrmAccount.getIndustry(), crmAccount.getIndustry())) {
                addChange(CustomFormConstants.CRM_ACCOUNT_INDUSTRY);
            }
        }
    }

    public void addCustomFieldChanges(String changes) {
        if (changes != null && !"".equals(changes)) {
            for (String change : changes.split(",")) {
                if (!"".equals(change.trim())) {
                    addChange(change);
                }
            }
        }
    }

    public void setTrackerIdSet(Set<EdsEmailTracker> trackerId) {
        this.trackerSet = trackerId;
    }

    public Set<EdsEmailTracker> getTrackerIdSet() {
        return trackerSet;
    }

    public EdsProject getCandidateProject() {
        return candidateProject;
    }

    public void setCandidateProject(EdsProject candidateProject) {
        this.candidateProject = candidateProject;
    }

    public Long getKanbanorder() {
        return kanbanorder;
    }

    public void setKanbanorder(Long kanbanorder) {
        this.kanbanorder = kanbanorder;
    }

    public Boolean getFavourited() {
        return favourited != null ? favourited : false;
    }

    public void setFavourited(Boolean favourited) {
        this.favourited = favourited;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        if (is(EdsCrmContact.CANDIDATE)) {
            if (!ServerUtils.equalsString(this.gender, gender)) {
                addHistoryChange("Gender", this.gender, gender);
            }
        }
        this.gender = gender;
    }

    public Integer getSelectedReference() {
        return selectedReference;
    }

    public void setSelectedReference(Integer selectedReference) {
        this.selectedReference = selectedReference;
    }

    public List<EdsCandidateStatusHistory> getStatusHistories() {
        return statusHistories;
    }

    public void setStatusHistories(List<EdsCandidateStatusHistory> statusHistories) {
        this.statusHistories = statusHistories;
    }

    public Set<EdsCandidateChanges> getCandidateHistory() {
        return candidateHistory;
    }

    public void setCandidateHistory(Set<EdsCandidateChanges> candidateHistory) {
        this.candidateHistory = candidateHistory;
    }

    public Set<EdsCandidateItemTable> getCandidateItemTables() {
        return candidateItemTables;
    }

    public void setCandidateItemTables(Set<EdsCandidateItemTable> candidateItemTables) {
        this.candidateItemTables = candidateItemTables;
    }

    public String getHumanReadableContactType() {
        if (getContactType() != null) {
            return switch (this.contactType) {
                case CrmConstants.TYPE_CRM_CONTACT -> "Contact";
                case CrmConstants.TYPE_CLIENT_CONTACT -> "Customer Contact";
                case CrmConstants.TYPE_SUPPLIER_CONTACT -> "Supplier Contact";
                case CrmConstants.TYPE_LEAD_CONTACT -> "Lead";
                case CrmConstants.TYPE_EMPLOYEE_CONTACT -> "Employee";
                default -> "Contact";
            };
        }
        return "Contact";
    }

    public void addStatusHistory(EdsReference newStatus) {
        EdsCandidateStatusHistory statusHistory = new EdsCandidateStatusHistory();
        statusHistory.setModifiedDate(new Date());
        statusHistory.setModifier(((EdsUser) SecurityContext.getInstance().getUser()).getEmployee());
        statusHistory.setStatus(newStatus);
        statusHistory.setCandidate(this);
        getStatusHistories().add(statusHistory);
    }

    public void addHistoryChange(String field, Object oldValue, Object newValue) {
        EdsCandidateChanges candidateChanges = new EdsCandidateChanges();
        candidateChanges.setField(field);
        candidateChanges.setEntityID(getObjectID());
        candidateChanges.setEntityName(getName());
        if (oldValue instanceof String || oldValue instanceof Double) {
            oldValue = oldValue == null ? "" : oldValue;
            candidateChanges.setFromStringValue(String.valueOf(oldValue));
        } else if (oldValue instanceof Number) {
            candidateChanges.setFromNumberValue((BigDecimal) oldValue);
        } else if (oldValue instanceof Date) {
            candidateChanges.setFromDateValue((Date) oldValue);
        } else if (oldValue instanceof Boolean) {
            candidateChanges.setFromStringValue((Boolean) oldValue ? "Yes" : "No");
        } else if (oldValue instanceof EdsReference) {
            candidateChanges.setFromReferenceId(((EdsReference) oldValue).getObjectID());
        }
        if (newValue instanceof String || newValue instanceof Double) {
            newValue = newValue == null ? "" : newValue;
            candidateChanges.setToStringValue(String.valueOf(newValue));
        } else if (newValue instanceof Number) {
            candidateChanges.setToNumberValue((BigDecimal) newValue);
        } else if (newValue instanceof Date) {
            candidateChanges.setToDateValue((Date) newValue);
        } else if (newValue instanceof Boolean) {
            candidateChanges.setToStringValue((Boolean) newValue ? "Yes" : "No");
        } else if (newValue instanceof EdsReference) {
            candidateChanges.setToReferenceId(((EdsReference) newValue).getObjectID());
        }
        candidateChanges.setModificationDate(new Date());
        candidateChanges.setUpdater((EdsUser) SecurityContext.getInstance().getUser());
        candidateChanges.setSuperUser(ServerUtils.isSuperUser());
        getCandidateHistory().add(candidateChanges);
        addChange(field);
    }

    @Override
    public String getObjectKey() {
        return objectKey;
    }

    @Override
    public void setObjectKey(String objectKey) {
        this.objectKey = objectKey;
    }

    public EdsPosition getCandidatePosition() {
        return candidatePosition;
    }

    public void setCandidatePosition(EdsPosition candidatePosition) {
        if (is(EdsCrmContact.CANDIDATE)) {
            if (!ServerUtils.equalsEdsObject(this.candidatePosition, candidatePosition)) {
                addHistoryChange("Position", this.candidatePosition != null ? this.candidatePosition.getName() : null, candidatePosition != null ? candidatePosition.getName() : null);
            }
        }
        this.candidatePosition = candidatePosition;
    }

    public EdsDepartment getCandidateDepartment() {
        return candidateDepartment;
    }

    public void setCandidateDepartment(EdsDepartment candidateDepartment) {
        if (is(EdsCrmContact.CANDIDATE)) {
            if (!ServerUtils.equalsEdsObject(this.candidateDepartment, candidateDepartment)) {
                addHistoryChange("Department", this.candidateDepartment != null ? this.candidateDepartment.getName() : null, candidateDepartment != null ? candidateDepartment.getName() : null);
            }
        }
        this.candidateDepartment = candidateDepartment;
    }

    public EdsTimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(EdsTimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public Double getStartSalary() {
        return startSalary;
    }

    public void setStartSalary(Double startSalary) {
        this.startSalary = startSalary;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }
}
