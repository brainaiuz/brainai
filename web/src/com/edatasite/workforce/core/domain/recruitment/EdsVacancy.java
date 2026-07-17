package com.edatasite.workforce.core.domain.recruitment;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsJobFamily;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsPosition;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsReferenceLocale;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.approving.EdsApprovable;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.customfields.EdsVacancyCustomFields;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrLeaveRequestConst;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrVacancyRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.solr.common.SolrInputDocument;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

/**
 * User: hayot
 * Date: 6/21/12
 * Time: 5:22 PM
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "vacancy")
public class EdsVacancy extends EdsApprovable {

    //vacancy status
    //parent
    public static final String VACANCY_STATUSES = VacancyItem.VACANCY_STATUSES;            //Vacancy statuses
    //children
    public static final String VS_OPEN = VacancyItem.VS_OPEN;                              //Open
    public static final String VS_IN_PROGRESS = VacancyItem.VS_IN_PROGRESS;                //In progress
    public static final String VS_ON_HOLD = VacancyItem.VS_ON_HOLD;                        //On hold
    public static final String VS_PARTIALLY_FILLED = VacancyItem.VS_PARTIALLY_FILLED;      //Partially filled
    public static final String VS_FILLED = VacancyItem.VS_FILLED;                          //Filled
    public static final String VS_CANCELLED = VacancyItem.VS_CANCELLED;                    //Cancelled
    public static final String DECLINED = VacancyItem.DECLINED;                            //Declined

    //approval status
    //parent
    public static final String VACANCY_APPROVAL_STATUS = VacancyItem.VACANCY_APPROVAL_STATUS;                                     //Status
    //children
    public static final String VACANCY_APPROVAL_STATUS_REJECTED = VacancyItem.VACANCY_APPROVAL_STATUS_REJECTED;                   //Rejected
    public static final String VACANCY_APPROVAL_STATUS_STATUS_SUBMITTED = VacancyItem.VACANCY_APPROVAL_STATUS_STATUS_SUBMITTED;   //Submited
    public static final String VACANCY_APPROVAL_STATUS_APPROVED = VacancyItem.VACANCY_APPROVAL_STATUS_APPROVED;                   //Approved
    public static final String VACANCY_APPROVAL_STATUS_STATUS_DRAFT = VacancyItem.VACANCY_APPROVAL_STATUS_STATUS_DRAFT;           //Draft

    //vacancy degrees
    //parent
    public static final String VACANCY_DEGREES = VacancyItem.VACANCY_DEGREES;              //Vacancy degrees
    //children
    public static final String VD_BSC = VacancyItem.VD_BSC;                                //VD_DSC
    public static final String VD_BA = VacancyItem.VD_BA;                                  //VD_BA
    public static final String VD_MA = VacancyItem.VD_MA;                                  //VD_MA
    public static final String VD_MSc = VacancyItem.VD_MSc;                                //VD_MSc

    public static final String VACANCY_TYPE = "_VACANCY_TYPE";
    public static final String VACANCY_RELIGION = "_VACANCY_RELIGION";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id")
    private EdsUser manager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private EdsPosition position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private EdsLocation location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private EdsDepartment department;

    @Column(name = "fullPartTime")
    private Integer fullPartTime;

    @Column(name = "jobFamily")
    private Integer jobFamily;

    private String jobTitle;
    @Type(type = "text")
    @Column(name = "job_title_localization")
    private String jobTitleLocalize;

    @Type(type = "text")
    private String description;

    @Type(type = "text")
    @Column(name = "description_localization")
    private String descriptionLocalize;

    private Date startDate;

    private Date endDate;

    @Column(name = "lastModifiedDate")
    private Date lastUpdatedTime;
    //   private Date lastUpdatedTime;

    @Column(name = "creationTime")
    private Date creationTime;

    @Column(name = "lastModifiedBy")
    private String modifiedBy;

    @Column(name = "createdBy")
    private String createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private EdsReference status;

    private Integer vacantPlaces = 0;

    @Type(type = "text")
    private String responsibility;

    @Type(type = "text")
    @Column(name = "responsibility_localization")
    private String responsibilityLocalize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requireddegree_id")
    private EdsReference requiredDegree;

    @Column(name = "vacancynumber")
    private String vacancyNumber;

    @Column(name = "intNumber")
    private Integer intNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "backupManager_id")
    private EdsUser backupManager;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vacancycustomfieldsid", unique = true)
    private EdsVacancyCustomFields vacancyCustomFields;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private EdsProject project;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "countryId")
//    @org.hibernate.annotations.ForeignKey(name = "none")
//    private EdsCountry country;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "embassyId")
//    private EdsEmbassy embassy;

    @Column(name = "gender")
    private String gender;

    @Column(name = "proposedSalary")
    private String proposedSalary;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currencyId", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private EdsCurrency currency;

    @Type(type = "text")
    @Column(name = "jobrequirements")
    private String jobrequirements;

    @Type(type = "text")
    @Column(name = "job_requirements_localization")
    private String jobRequirementsLocalize;

    private Date contractFrom;

    private Date contractTo;

    @Column(name = "vacancyTypeId")
    private Integer vacancyTypeId;

    @Column(name = "vacancyTypeName")
    private String vacancyTypeName;


//    @Column(name = "religionId")
//    private Integer religionId;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entityID", fetch = FetchType.LAZY)
    @Where(clause = "stepEmployeeType = 'VACANCY'")
    @OrderBy(value = "approverOrder ASC")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private List<EdsApprover> approvers = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vacancy", fetch = FetchType.LAZY)
    @OrderBy(value = "creationDate DESC")
    private List<EdsVacancyNote> vacancies = new ArrayList<>();

    @OneToMany(mappedBy = "vacancy", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<EdsVacancyItemTable> itemTables = new HashSet<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "localeId")
    private EdsReferenceLocale locale;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vacancy", fetch = FetchType.LAZY)
    private List<EdsVacancyQuestion> customQuestions = new ArrayList<>();


    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Set<EdsVacancyItemTable> getItemTables() {
        return itemTables;
    }

    public void setItemTables(Set<EdsVacancyItemTable> itemTables) {
        this.itemTables = itemTables;
    }

    public void addItemTable(EdsVacancyItemTable itemTable) {
        itemTables.add(itemTable);
    }

    public void setCustomQuestions(ArrayList<EdsVacancyQuestion> customQuestions) {
        this.customQuestions = customQuestions;
    }

    public void addCustomQuestion(EdsVacancyQuestion customQuestion) {
        customQuestions.add(customQuestion);
    }

    public List<EdsVacancyQuestion> getCustomQuestions() {
        return customQuestions;
    }


    @Override
    public String getName() {
        if (getLocale() != null) {
            String lang = ServerUtils.getUserLocale().getLanguage();
            if (StringUtils.isNotBlank(getLocale().getLocaleByCode(lang))) {
                return getLocale().getLocaleByCode(lang);
            }
        }
        return getJobTitle();
    }

    public Boolean getDeleted() {
        return deleted != null ? deleted : Boolean.FALSE;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public EdsUser getManager() {
        return manager;
    }

    public void setManager(EdsUser manager) {
        if (!ServerUtils.equalsEdsObject(this.manager, manager)) {
            addChange(CustomFormConstants.VACANCY.MANAGER);
        }
        this.manager = manager;
    }

    public EdsPosition getPosition() {
        return position;
    }

    public void setPosition(EdsPosition position) {
        if (!ServerUtils.equalsEdsObject(this.position, position)) {
            addChange(CustomFormConstants.VACANCY.POSITION);
        }
        this.position = position;
    }

    public EdsLocation getLocation() {
        return location;
    }

    public void setLocation(EdsLocation location) {
        if (!ServerUtils.equalsEdsObject(this.location, location)) {
            addChange(CustomFormConstants.VACANCY.LOCATION);
        }
        this.location = location;
    }

    public Integer getFullPartTime() {
        return fullPartTime;
    }

    public void setFullPartTime(Integer fullPartTime) {
        if (!ServerUtils.equalsInteger(this.fullPartTime, fullPartTime)) {
            addChange(CustomFormConstants.VACANCY.JOB_TYPE);
        }
        this.fullPartTime = fullPartTime;
    }

    public Integer getJobFamily() {
        return jobFamily;
    }

    public void setJobFamily(Integer jobFamily) {
        if (!ServerUtils.equalsInteger(this.jobFamily, jobFamily)) {
            addChange(CustomFormConstants.VACANCY.JOB_FAMILY);
        }
        this.jobFamily = jobFamily;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        if (!ServerUtils.equalsString(this.jobTitle, jobTitle)) {
            addChange(CustomFormConstants.VACANCY.JOB_TITLE);
        }
        this.jobTitle = jobTitle;
    }

    public String getJobTitleLocalize() {
        return jobTitleLocalize;
    }

    public void setJobTitleLocalize(String jobTitleLocalize) {
        this.jobTitleLocalize = jobTitleLocalize;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (!ServerUtils.equalsString(this.description, description)) {
            addChange(CustomFormConstants.VACANCY.DESCRIPTION);
        }
        this.description = description;
    }

    public String getDescriptionLocalize() {
        return descriptionLocalize;
    }

    public void setDescriptionLocalize(String descriptionLocalize) {
        this.descriptionLocalize = descriptionLocalize;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        if (!ServerUtils.equalsDate(this.startDate, startDate)) {
            addChange(CustomFormConstants.VACANCY.START_DATE);
        }
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        if (!ServerUtils.equalsDate(this.endDate, endDate)) {
            addChange(CustomFormConstants.VACANCY.END_DATE);
        }
        this.endDate = endDate;
    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        if (!ServerUtils.equalsEdsObject(this.status, status)) {
            addChange(CustomFormConstants.VACANCY.STATUS);
        }
        this.status = status;
    }

    public Integer getVacantPlaces() {
        return vacantPlaces;
    }

    public void setVacantPlaces(Integer vacantPlaces) {
        if (!ServerUtils.equalsInteger(this.vacantPlaces, vacantPlaces)) {
            addChange(CustomFormConstants.VACANCY.VACANCY_PLACE_COUNT);
        }
        this.vacantPlaces = vacantPlaces;
    }

    public String getResponsibility() {
        return responsibility;
    }

    public void setResponsibility(String responsibility) {
        if (!ServerUtils.equalsString(this.responsibility, responsibility)) {
            addChange(CustomFormConstants.VACANCY.RESPONSIBILITIES);
        }
        this.responsibility = responsibility;
    }

    public String getResponsibilityLocalize() {
        return responsibilityLocalize;
    }

    public void setResponsibilityLocalize(String responsibilityLocalize) {
        this.responsibilityLocalize = responsibilityLocalize;
    }

    public EdsReference getRequiredDegree() {
        return requiredDegree;
    }

    public void setRequiredDegree(EdsReference requiredDegree) {
        if (!ServerUtils.equalsEdsObject(this.requiredDegree, requiredDegree)) {
            addChange(CustomFormConstants.VACANCY.REQUIRED_DEGREE);
        }
        this.requiredDegree = requiredDegree;
    }

    public String getVacancyNumber() {
        return vacancyNumber;
    }

    public void setVacancyNumber(String vacancyNumber) {
        if (!ServerUtils.equalsString(this.vacancyNumber, vacancyNumber)) {
            addChange(CustomFormConstants.VACANCY.VACANCY_NUMBER);
        }
        this.vacancyNumber = vacancyNumber;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    @Override
    public SelectItem getAsSelectItem() {
        return new SelectItem(getObjectID(), !getJobTitle().equals("") ? getJobTitle() : getPosition().getName(), getDescription());
    }

    //    public EdsUser getBackupManager() {
//        return backupManager;
//    }
//    public void setBackupManager(EdsUser backupManager) {
//        this.backupManager = backupManager;
//    }
    public EdsVacancyCustomFields getVacancyCustomFields() {
        return vacancyCustomFields;
    }

    public void setVacancyCustomFields(EdsVacancyCustomFields vacancyCustomFields) {
        this.vacancyCustomFields = vacancyCustomFields;
    }

    public void setProposedSalary(String proposedSalary) {
        if (!ServerUtils.equalsString(this.proposedSalary, proposedSalary)) {
            addChange(CustomFormConstants.VACANCY.PROPOSED_SALARY);
        }
        this.proposedSalary = proposedSalary;
    }

    public String getProposedSalary() {
        return proposedSalary;
    }

    public void setGender(String gender) {
        if (!ServerUtils.equalsString(this.gender, gender)) {
            addChange(CustomFormConstants.VACANCY.GENDER);
        }
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public EdsProject getProject() {
        return project;
    }

    public void setProject(EdsProject project) {
        if (!ServerUtils.equalsEdsObject(this.project, project)) {
            addChange(CustomFormConstants.VACANCY.PROJECT);
        }
        this.project = project;
    }

    public String getJobrequirements() {
        return jobrequirements;
    }

    public void setJobrequirements(String jobrequirements) {
        if (!ServerUtils.equalsString(this.jobrequirements, jobrequirements)) {
            addChange(CustomFormConstants.VACANCY.JOB_REQUIREMENT);
        }
        this.jobrequirements = jobrequirements;
    }

    public String getJobRequirementsLocalize() {
        return jobRequirementsLocalize;
    }

    public void setJobRequirementsLocalize(String jobRequirementsLocalize) {
        this.jobRequirementsLocalize = jobRequirementsLocalize;
    }

    public Date getContractFrom() {
        return contractFrom;
    }

    public void setContractFrom(Date contractFrom) {
        if (!ServerUtils.equalsDate(this.contractTo, contractTo)) {
            addChange(CustomFormConstants.VACANCY.CONTRACT_PERIOD);
        }
        this.contractFrom = contractFrom;
    }

    public Date getContractTo() {
        return contractTo;
    }

    public void setContractTo(Date contractTo) {
        if (!ServerUtils.equalsDate(this.contractTo, contractTo)) {
            addChange(CustomFormConstants.VACANCY.CONTRACT_PERIOD);
        }
        this.contractTo = contractTo;
    }

    public Integer getVacancyTypeId() {
        return vacancyTypeId;
    }

    public void setVacancyTypeId(Integer vacancyTypeId) {
        this.vacancyTypeId = vacancyTypeId;
    }

    public String getVacancyTypeName() {
        return vacancyTypeName;
    }

    public void setVacancyTypeName(String vacancyTypeName) {
        this.vacancyTypeName = vacancyTypeName;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public EdsCurrency getCurrency() {
        if (!ServerUtils.equalsEdsObject(this.currency, currency)) {
            addChange(CustomFormConstants.VACANCY.CURRENCY);
        }
        return this.currency;
    }

    public void setCurrency(final EdsCurrency currency) {
        this.currency = currency;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(Date lastUpdatedTime) {
        this.lastUpdatedTime = lastUpdatedTime;
    }

    public VacancyItem getRPC() {
        VacancyItem vacancy = new VacancyItem();

        vacancy.setContractFrom(getContractFrom());
        vacancy.setContractTo(getContractTo());
        vacancy.setCreatedDate(getCreationTime());
        vacancy.setModifiedDate(getLastUpdatedTime());
        vacancy.setModifiedBy(getModifiedBy());
        vacancy.setCreatedBy(getCreatedBy());
        vacancy.setDescription(getDescription());
        vacancy.setEndDate(getEndDate());
        vacancy.setGender(getGender());
        vacancy.setJobRequirements(getJobrequirements());
        vacancy.setJobTitle(getJobTitle());
        vacancy.setLocation(getLocation() != null ? getLocation().getAsSelectItem() : null);
        vacancy.setLocationItem(getLocation() != null ? getLocation().getRPC() : null);
        vacancy.setManager(getManager() != null ? getManager().getAsSelectItem() : null);
        vacancy.setNumberData(new NumberData(getVacancyNumber(), getIntNumber()));
        vacancy.setObjectID(getObjectID());
        vacancy.setPositionItem(getPosition() != null ? getPosition().getRPC() : null);
        vacancy.setProposedSalary(getProposedSalary());
        vacancy.setRequiredDegree(getRequiredDegree() != null ? getRequiredDegree().getRPC() : null);
        vacancy.setResponsibility(getResponsibility());
        vacancy.setStartDate(getStartDate());
        vacancy.setVacantPlaces(getVacantPlaces());


        if (getOverallStatus() != null) {
            vacancy.setApprovalStatusCode(getOverallStatus().getCode());
        }

        if (getCreationTime() != null) {
            vacancy.setCreatedDate(getCreationTime());
        }

        if (getLastUpdatedTime() != null) {
            vacancy.setModifiedDate(getLastUpdatedTime());
        }

        if (getStatus() != null) {
            ReferenceItem referenceItem = getStatus().getRPC();
            if (getStatus().getLocale() != null) {
                referenceItem.setName(getStatus().getLocale().getLocaleByCode(ServerUtils.getUserLocale().getLanguage()));
            }
            vacancy.setStatus(referenceItem);
        }

        if (getProject() != null) {
            vacancy.setProjectId(getProject().getObjectID());
            vacancy.setProjectName(getProject().getName());
        }

        if (getVacancyTypeId() != null) {
            vacancy.setVacancyType(getVacancyTypeId());
        }
        if (getVacancyTypeName() != null) {
            vacancy.setVacancyTypeName(getVacancyTypeName());
        }
        if (getDepartment() != null) {
            vacancy.setDepartment(getDepartment().getAsSelectItem());
        }
        initApproverData(vacancy);

        return vacancy;
    }

    public VacancySolrItem getSolrRPC() {
        VacancySolrItem vacancy = new VacancySolrItem();

        vacancy.setObjectID(getObjectID());
        vacancy.setVacancyNumber(getVacancyNumber());
        vacancy.setJobFamilyId(getJobFamily());
        vacancy.setFullPartTime(getFullPartTime());
        vacancy.setJobTitle(getJobTitle());

        String en = getName();
        String ru = getName();
        String ar = getName();
        String uz = getName();
        if (getLocale() != null) {
            en = getLocale().getEnglish() != null ? getLocale().getEnglish() : getName();
            ru = getLocale().getRussian() != null ? getLocale().getRussian() : getName();
            ar = getLocale().getArabic() != null ? getLocale().getArabic() : getName();
            uz = getLocale().getUzbek() != null ? getLocale().getUzbek() : getName();
        }

        vacancy.setNameEn(en);
        vacancy.setNameRu(ru);
        vacancy.setNameAr(ar);
        vacancy.setNameUz(uz);

        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
            vacancy.setApprover(getCurrentApprover().getExactEmployee().getAsSelectItem());
        }

        if (getStatus() != null) {
            EdsReference status = getStatus();
            vacancy.setVacancyStatus(status.getRPC());
        }

        if (getRequiredDegree() != null) {
            EdsReference rdegree = getRequiredDegree();
            vacancy.setRdegreeStatus(rdegree.getRPC());
        }

        if (getProject() != null) {
            EdsProject project = getProject();
            vacancy.setProject(project.getAsSelectItem());
        }

        vacancy.setGender(getGender());
        vacancy.setProposedSalary(getProposedSalary());
        vacancy.setJobRequirements(getJobrequirements());
        vacancy.setContractFrom(getContractFrom());
        vacancy.setContractTo(getContractTo());
        vacancy.setVacancyType(new SelectItem(getVacancyTypeId(), getVacancyTypeName()));

        if (getManager() != null) {
            EdsUser manager = getManager();
            vacancy.setManager(manager.getAsSelectItem());
        }

        if (getPosition() != null) {
            EdsPosition position = getPosition();
            vacancy.setPosition(position.getAsSelectItem());
        }

        if (getLocation() != null) {
            EdsLocation location = getLocation();

            String locationName = ((location.getCountry() != null ? (location.getCountry().getName() + ",") : "") +
                    (location.getState() != null ? (location.getState().getName() + ",") : "") +
                    location.getCity());
            vacancy.setLocation(new SelectItem(location.getObjectID(), locationName));
        }

        if (getCurrency() != null) {
            EdsCurrency currency = getCurrency();
            vacancy.setCurrency(currency.getAsSelectItem());
        }

        vacancy.setStartDate(getStartDate());
        vacancy.setEndDate(getEndDate());
        vacancy.setCreatedDate(getCreationTime());
        vacancy.setLastUpdateDate(getLastUpdatedTime());
        vacancy.setCreatedBy(getCreatedBy());
        vacancy.setModifiedBy(getModifiedBy());

        return vacancy;
    }

    public SolrInputDocument indexToSolr(EdsReference jobType, EdsJobFamily jobFamily, Integer companyID) {
        SolrInputDocument doc = new SolrInputDocument();
        String compositID = companyID + "_" + getObjectID();
        doc.addField(SolrVacancyRepresenter.COMPOSITE_ID, compositID);
        doc.addField(SolrVacancyRepresenter.COMPANY_ID, companyID);
        doc.addField(SolrVacancyRepresenter.FIELD_VACANCY_ID, getObjectID());
        doc.addField(SolrVacancyRepresenter.FIELD_VACANCY_NUMBER, getVacancyNumber());
        doc.addField(SolrVacancyRepresenter.FIELD_JOB_TITLE, getJobTitle());
        doc.addField(SolrVacancyRepresenter.FIELD_START_DATE, getStartDate());
        doc.addField(SolrVacancyRepresenter.FIELD_END_DATE, getEndDate());
        doc.addField(SolrVacancyRepresenter.FIELD_GENDER, getGender());
        doc.addField(SolrVacancyRepresenter.FIELD_PROPOSED_SALARY, getProposedSalary());
        doc.addField(SolrVacancyRepresenter.FIELD_JOB_REQUIREMENTS, getJobrequirements());
        doc.addField(SolrVacancyRepresenter.FIELD_CONTRACT_FROM, getContractFrom());
        doc.addField(SolrVacancyRepresenter.FIELD_CONTRACT_TO, getContractTo());
        doc.addField(SolrVacancyRepresenter.FIELD_VACANCY_TYPE, getVacancyTypeId());
        doc.addField(SolrVacancyRepresenter.FIELD_VACANCY_TYPE_NAME, getVacancyTypeName());
        doc.addField(SolrVacancyRepresenter.FIELD_LAST_UPDATE_DATE, getLastUpdatedTime());
        doc.addField(SolrVacancyRepresenter.FIELD_CREATED_DATE, getCreationTime());
        doc.addField(SolrVacancyRepresenter.FIELD_MODIFIED_BY, getModifiedBy());
        doc.addField(SolrVacancyRepresenter.FIELD_CREATED_BY, getCreatedBy());

        if (getProject() != null) {
            doc.addField(SolrVacancyRepresenter.FIELD_PROJECT_ID, getProject().getObjectID());
            doc.addField(SolrVacancyRepresenter.FIELD_PROJECT_NAME, getProject().getName());
            doc.addField(SolrVacancyRepresenter.FIELD_PROJECT_ID_NAME, getProject().getObjectID() + SolrVacancyRepresenter.SPLIT + getProject().getName());
        }

        if (getCurrency() != null) {
            doc.addField(SolrVacancyRepresenter.FIELD_CURRENCY_ID, getCurrency().getObjectID());
            doc.addField(SolrVacancyRepresenter.FIELD_CURRENCY_NAME, getCurrency().getName());
            doc.addField(SolrVacancyRepresenter.FIELD_CURRENCY_ID_NAME, getCurrency().getObjectID() + SolrVacancyRepresenter.SPLIT + getCurrency().getName());
        }

        if (jobType != null) {
            doc.addField(SolrVacancyRepresenter.FIELD_JOB_TYPE_ID, jobType.getObjectID());
            doc.addField(SolrVacancyRepresenter.FIELD_JOB_TYPE_NAME, jobType.getName());
            doc.addField(SolrVacancyRepresenter.FIELD_JOB_TYPE_NAME_ID, jobType.getObjectID() + SolrVacancyRepresenter.SPLIT + jobType.getName());
        }

        if (jobFamily != null) {
            doc.addField(SolrVacancyRepresenter.FIELD_JOB_FAMILY_ID, jobFamily.getObjectID());
            doc.addField(SolrVacancyRepresenter.FIELD_JOB_FAMILY_NAME, jobFamily.getName());
            doc.addField(SolrVacancyRepresenter.FIELD_JOB_FAMILY_NAME_ID, jobFamily.getObjectID() + SolrVacancyRepresenter.SPLIT + jobFamily.getName());
        }

        if (getManager() != null) {
            doc.addField(SolrVacancyRepresenter.FIELD_MANAGER_ID, getManager().getObjectID());
            doc.addField(SolrVacancyRepresenter.FIELD_MANAGER_NAME, getManager().getName());
            doc.addField(SolrVacancyRepresenter.FIELD_MANAGER_ID_NAME, getManager().getObjectID() + SolrVacancyRepresenter.SPLIT + getManager().getName());
        }


        if (getLocation() != null) {
            String locationName = (getLocation() != null && getLocation().getCode() != null) ? getLocation().getCode() + "->" + getLocation().getName() : getLocation().getName();
            doc.addField(SolrVacancyRepresenter.FIELD_LOCATION_ID, getLocation().getObjectID());
            doc.addField(SolrVacancyRepresenter.FIELD_LOCATION_NAME, locationName);
            doc.addField(SolrVacancyRepresenter.FIELD_LOCATION_ID_NAME, getLocation().getObjectID() + SolrVacancyRepresenter.SPLIT + locationName);
        }

        if (getPosition() != null) {
            doc.addField(SolrVacancyRepresenter.FIELD_POSITION_ID, getPosition().getObjectID());
            doc.addField(SolrVacancyRepresenter.FIELD_POSITION_NAME, getPosition().getName());
            doc.addField(SolrVacancyRepresenter.FIELD_POSITION_ID_NAME, getPosition().getObjectID() + SolrVacancyRepresenter.SPLIT + getPosition().getName());
        }

        if (getDepartment() != null) {
            doc.addField(SolrVacancyRepresenter.FIELD_DEPARTMENT_ID, getDepartment().getObjectID());
            doc.addField(SolrVacancyRepresenter.FIELD_DEPARTMENT_NAME, getDepartment().getName());
            doc.addField(SolrVacancyRepresenter.FIELD_DEPARTMENT_ID_NAME, getDepartment().getObjectID() + SolrVacancyRepresenter.SPLIT + getDepartment().getName());
        }

        if (getStatus() != null) {
            doc.addField(SolrVacancyRepresenter.FIELD_VACANCY_STATUS_ID, getStatus().getObjectID());
            doc.addField(SolrVacancyRepresenter.FIELD_VACANCY_STATUS, getStatus().getName());
            doc.addField(SolrVacancyRepresenter.FIELD_VACANCY_STATUS_ID_CODE, getStatus().getObjectID() + SolrVacancyRepresenter.SPLIT
                    + getStatus().getCode());
            doc.addField(SolrVacancyRepresenter.FIELD_VACANCY_STATUS_ID_CODE_NAME, getStatus().getObjectID() + SolrVacancyRepresenter.SPLIT
                    + getStatus().getCode() + SolrVacancyRepresenter.SPLIT + getStatus().getName());
            doc.addField(SolrVacancyRepresenter.FIELD_VACANCY_STATUS_CODE, getStatus().getCode());
            doc.addField(SolrVacancyRepresenter.FIELD_VACANCY_STATUS_SORDER, getStatus().getSorder());
        }

        if (getOverallStatus() != null) {
            doc.addField(SolrVacancyRepresenter.FIELD_APPROVAL_STATUS_ID, getOverallStatus().getObjectID());
            doc.addField(SolrVacancyRepresenter.FIELD_APPROVAL_STATUS_NAME, getOverallStatus().getCode());
            doc.addField(SolrVacancyRepresenter.FIELD_APPROVAL_STATUS_ID_NAME, getOverallStatus().getObjectID() + SolrVacancyRepresenter.SPLIT + getOverallStatus().getName());
        }

        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
            doc.addField(SolrVacancyRepresenter.FIELD_APPROVER_ID, getCurrentApprover().getExactEmployee().getObjectID());
            doc.addField(SolrVacancyRepresenter.FIELD_APPROVER_NAME, getCurrentApprover().getExactEmployee().getName());
            doc.addField(SolrVacancyRepresenter.FIELD_APPROVER_ID_NAME, getCurrentApprover().getExactEmployee().getObjectID() + SolrLeaveRequestConst.SPLIT + getCurrentApprover().getExactEmployee().getName());
        }

        if (getRequiredDegree() != null) {
            doc.addField(SolrVacancyRepresenter.FIELD_RDEGREE_STATUS_ID, getRequiredDegree().getObjectID());
            doc.addField(SolrVacancyRepresenter.FIELD_RDEGREE_STATUS, getRequiredDegree().getName());
            doc.addField(SolrVacancyRepresenter.FIELD_RDEGREE_STATUS_ID_CODE, getRequiredDegree().getObjectID() + SolrVacancyRepresenter.SPLIT
                    + getRequiredDegree().getCode());
            doc.addField(SolrVacancyRepresenter.FIELD_RDEGREE_STATUS_ID_CODE_NAME, getRequiredDegree().getObjectID() + SolrVacancyRepresenter.SPLIT
                    + getRequiredDegree().getCode() + SolrVacancyRepresenter.SPLIT + getRequiredDegree().getName());
            doc.addField(SolrVacancyRepresenter.FIELD_RDEGREE_STATUS_CODE, getRequiredDegree().getCode());
            doc.addField(SolrVacancyRepresenter.FIELD_RDEGREE_STATUS_SORDER, getRequiredDegree().getSorder());
        }

        String en = getName();
        String ru = getName();
        String ar = getName();
        String uz = getName();
        if (getLocale() != null) {
            en = getLocale().getEnglish() != null ? getLocale().getEnglish() : getName();
            ru = getLocale().getRussian() != null ? getLocale().getRussian() : getName();
            ar = getLocale().getArabic() != null ? getLocale().getArabic() : getName();
            uz = getLocale().getUzbek() != null ? getLocale().getUzbek() : getName();
        }
        doc.addField(SolrVacancyRepresenter.FIELD_NAME_EN, en);
        doc.addField(SolrVacancyRepresenter.FIELD_NAME_RU, ru);
        doc.addField(SolrVacancyRepresenter.FIELD_NAME_AR, ar);
        doc.addField(SolrVacancyRepresenter.FIELD_NAME_UZ, uz);

        CustomFieldsUtils.setInSolrCustomFields(doc, getVacancyCustomFields());

        return doc;
    }

    public EdsDepartment getDepartment() {
        return department;
    }

    public void setDepartment(EdsDepartment department) {
        if (!ServerUtils.equalsEdsObject(this.department, department)) {
            addChange(CustomFormConstants.VACANCY.DEPARTMENT);
        }
        this.department = department;
    }

    @Override
    public List<EdsApprover> getApprovers() {
        return approvers;
    }

    @Override
    public void setApprovers(List<EdsApprover> approvers) {
        this.approvers = approvers;
    }

    @Override
    public void setEntityStatus(EdsReference status) {
        setOverallStatus(status);
    }

    @Override
    public boolean isCurrentApproverApproved() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && VacancyItem.VACANCY_APPROVAL_STATUS_APPROVED.equals(getCurrentApprover().getStatus().getCode());
    }

    @Override
    public boolean isCurrentApproverRejected() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && VacancyItem.VACANCY_APPROVAL_STATUS_REJECTED.equals(getCurrentApprover().getStatus().getCode());
    }

    @Override
    protected EdsReference getStatusByMarkedAction(Integer actionID) {
        if (!isOk(actionID)) {
            return null;
        }
        ReferenceManager referenceManager = StaticContextAccessor.getBean(ReferenceManager.class);
        if (actionID.equals(ApproverItem.MARK_AS_REJECTED)) {
            return referenceManager.findReference(Constants.VACANCY_APPROVAL_STATUS, Constants.VACANCY_APPROVAL_STATUS_REJECTED);
        } else if (actionID.equals(ApproverItem.MARK_AS_APPROVED)) {
            return referenceManager.findReference(Constants.VACANCY_APPROVAL_STATUS, Constants.VACANCY_APPROVAL_STATUS_APPROVED);
        } else if (actionID.equals(ApproverItem.SEND_TO_CREATOR)) {
            return referenceManager.findReference(Constants.VACANCY_APPROVAL_STATUS, Constants.VACANCY_APPROVAL_STATUS_DRAFT);
        } else if (actionID.equals(ApproverItem.SEND_TO_DIRECTORS)) {
            return referenceManager.findReference(Constants.VACANCY_APPROVAL_STATUS, Constants.VACANCY_APPROVAL_STATUS_DRAFT);
        }
        return null;
    }

    @Override
    public void updateRejectedStatus() {
        if (getOverallStatus() != null && VacancyItem.VACANCY_APPROVAL_STATUS_REJECTED.equals(getOverallStatus().getCode())) {
            ReferenceManager referenceManager = StaticContextAccessor.getBean(ReferenceManager.class);
            setEntityStatus(referenceManager.findReference(Constants.VACANCY_APPROVAL_STATUS, Constants.VACANCY_APPROVAL_STATUS_SUBMITTED));
        }
    }

    @Override
    public void jumpToPreviousApprover() {
        super.jumpToPreviousApprover();
    }

    @Override
    public Object getRealValue(String fieldID) {
        if (fieldID == null) {
            return null;
        } else if (fieldID.equals(CustomFormConstants.VACANCY.DESCRIPTION)) {
            return getDescription();
        } else if (fieldID.equals(CustomFormConstants.VACANCY.GENDER)) {
            return getGender();
        } else if (fieldID.equals(CustomFormConstants.VACANCY.STATUS)) {
            return getStatus();
        } else if (fieldID.equals(CustomFormConstants.VACANCY.JOB_REQUIREMENT)) {
            return getJobrequirements();
        } else if (fieldID.equals(CustomFormConstants.VACANCY.PROJECT)) {
            return getProject();
        } else if (fieldID.equals(CustomFormConstants.VACANCY.JOB_TITLE)) {
            return getJobTitle();
        } else if (fieldID.equals(CustomFormConstants.VACANCY.LOCATION)) {
            return getLocation();
        } else if (fieldID.equals(CustomFormConstants.VACANCY.VACANCY_TYPE)) {
            return getVacancyTypeName();
        } else if (fieldID.equals(CustomFormConstants.VACANCY.REQUIRED_DEGREE)) {
            return getRequiredDegree();
        } else if (fieldID.equals(CustomFormConstants.VACANCY.RESPONSIBILITIES)) {
            return getResponsibility();
        } else if (fieldID.equals(CustomFormConstants.VACANCY.PROPOSED_SALARY)) {
            return getProposedSalary();
        } else if (fieldID.equals(CustomFormConstants.VACANCY.VACANCY_NUMBER)) {
            return getVacancyNumber();
        } else if (fieldID.equals(CustomFormConstants.VACANCY.CREATED_DATE)) {
            return getCreationTime();
        } else if (fieldID.equals(CustomFormConstants.VACANCY.MODIFIED_DATE)) {
            return getLastUpdatedTime();
        }
        return super.getRealValue(fieldID);
    }

    public EdsReferenceLocale getLocale() {
        return locale;
    }

    public void setLocale(EdsReferenceLocale locale) {
        this.locale = locale;
    }
}
