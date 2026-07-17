package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class VacancyItem extends HasApprovers implements IsSerializable, ListingCustomFields {
    public static final String DECLINED = "DECLINED";                         //Declined
    public static final String PROJECT = "PROJECT";
    public static final String VACANCY_APPROVAL_STATUS = "VACANCY_APPROVAL_STATUS";                                     //Approval Status
    public static final String VACANCY_APPROVAL_STATUS_APPROVED = "VACANCY_APPROVAL_STATUS_APPROVED";                   //Approved
    public static final String VACANCY_APPROVAL_STATUS_REJECTED = "VACANCY_APPROVAL_STATUS_REJECTED";                   //Rejected
    public static final String VACANCY_APPROVAL_STATUS_STATUS_DRAFT = "VACANCY_APPROVAL_STATUS_STATUS_DRAFT";           //Draft
    public static final String VACANCY_APPROVAL_STATUS_STATUS_SUBMITTED = "VACANCY_APPROVAL_STATUS_STATUS_SUBMITTED";   //Submited
    public static final String VACANCY_APPROVER = "VACANCY_APPROVER";
    public static final String VACANCY_CONTRACT_FROM = "VACANCY_CONTRACT_FROM";
    public static final String VACANCY_CONTRACT_TO = "VACANCY_CONTRACT_TO";
    public static final String VACANCY_CREATED_BY = "createdBy";
    public static final String VACANCY_CREATED_DATE = "createdDate";
    public static final String VACANCY_CURRENCY = "VACANCY_CURRENCY";
    public static final String VACANCY_DEGREES = "VACANCY_DEGREES";           //Vacancy degrees
    public static final String _VACANCY_TYPE = "_VACANCY_TYPE";
    public static final String VACANCY_DEPARTMENT = "VACANCY_DEPARTMENT";
    public static final String VACANCY_END_DATE = "VACANCY_END_DATE";
    public static final String VACANCY_GENDER = "VACANCY_GENDER";
    public static final String VACANCY_ID = "VACANCY_ID";
    public static final String VACANCY_JOB_FAMILY = "VACANCY_JOB_FAMILY";
    public static final String VACANCY_JOB_REQUIREMENT = "VACANCY_JOB_REQUIREMENT";
    public static final String VACANCY_JOB_TITLE = "VACANCY_JOB_TITLE";
    public static final String VACANCY_JOB_TYPE = "VACANCY_JOB_TYPE";
    public static final String VACANCY_LOCATION = "VACANCY_LOCATION";
    public static final String VACANCY_MANAGER = "VACANCY_MANAGER";
    public static final String VACANCY_MODIFIED_BY = "modifiedBy";
    public static final String VACANCY_MODIFIED_DATE = "modifiedDate";
    public static final String VACANCY_POSITION = "VACANCY_POSITION";
    public static final String VACANCY_PROPOSED_SALARY = "VACANCY_PROPOSED_SALARY";
    public static final String VACANCY_REQUIRED_DEGREE = "VACANCY_REQUIRED_DEGREE";
    public static final String VACANCY_START_DATE = "VACANCY_START_DATE";
    public static final String VACANCY_STATUS = "VACANCY_STATUS";
    public static final String VACANCY_STATUSES = "VACANCY_STATUSES";         //Vacancy statuses
    public static final String VACANCY_TYPE = "VACANCY_TYPE";
    public static final String VACANCY_TYPE_NAME = "VACANCY_TYPE_NAME";
    public static final String VD_BA = "VD_BA";                               //VD_BA
    public static final String VD_BSC = "VD_BSC";                             //VD_DSC
    public static final String VD_MA = "VD_MA";                               //VD_MA
    public static final String VD_MSc = "VD_MSc";                             //VD_MSc
    public static final String VS_CANCELLED = "VS_CANCELLED";                 //Cancelled
    public static final String VS_FILLED = "VS_FILLED";                       //Filled
    public static final String VS_IN_PROGRESS = "VS_IN_PROGRESS";             //In progress
    public static final String VS_ON_HOLD = "VS_ON_HOLD";                     //On hold
    public static final String VS_OPEN = "VS_OPEN";                           //Open
    public static final String VS_PARTIALLY_FILLED = "VS_PARTIALLY_FILLED";   //On hold
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private ArrayList<HistoryListItem> vacancyNotes;
    private ArrayList<RelationItem> relations;
    private ArrayList<SelectItem> matchedCandidates;
    private ArrayList<SpokenLanguageItem> spokenLanguages;
    private boolean isRelationChanged = false;
    private Date contractFrom,contractTo,endDate;
    private Date createdDate;
    private Date modifiedDate;
    private Date startDate;
    private FileItem[] attachments;
    private FileResource[] convertedFileResources;
    private HashMap<String, ArrayList<CustomTableRpc>> customTableItems = new HashMap<>();
    private HashMap<String, Object> customFieldValues;
    private HashMap<String, String> descriptionLocalize;
    private HashMap<String, String> jobRequirementLocalize;
    private HashMap<String, String> jobTitleLocalize;
    private HashMap<String, String> responsibilitiesLocalize;
    private Integer objectID;
    private Integer projectId,vacancyType;
    private Integer selectedTemplateId;
    private Integer vacantPlaces;
    private LocationItem locationItem;
    private NumberData numberData;
    private PositionItem positionItem;
    private ReferenceItem requiredDegree;
    private ReferenceItem status;
    private ReferenceLocale referenceLocale;
    private SelectItem creatorDepatment;
    private SelectItem creatorLocation;
    private SelectItem department;
    private SelectItem jobfamily,jobType;
    private SelectItem location;
    private SelectItem manager,currency;
    private SelectItem[] jobFamilies,departmentItems;
    private SelectItem[] locations;
    private SelectItem[] managers;
    private Integer managerID;
    private SelectItem[] positions;
    private SelectItem[] requiredDegrees;
    private SelectItem[] statuses;
    private SelectItem[] templates;
    private SelectItem[] timeTypes;
    private String approvalStatusCode,contractPeriod;
    private String createdBy;
    private String description;
    private String gender;
    private String jobRequirements;
    private String jobTitle;
    private String modifiedBy;
    private String projectName;
    private String proposedSalary;
    private String responsibility;
    private String vacancyTypeName,relationName;
    private SelectItem approver;
    private VacancyQuestionTableItem[] questionTableItem;
    public HashMap<String, ArrayList<CustomTableRpc>> getCustomTableItems() {
        return customTableItems;
    }

    public void setCustomTableItems(HashMap<String, ArrayList<CustomTableRpc>> customTableItems) {
        this.customTableItems = customTableItems;
    }
    public SelectItem[] getDepartmentItems() {
        return departmentItems;
    }
    public void setDepartmentItems(final SelectItem[] departmentItems) {
        this.departmentItems = departmentItems;
    }
    public Integer getObjectID() {
        return objectID;
    }
    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }
    public NumberData getNumberData() {
        return numberData;
    }
    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
    }
    public SelectItem getManager() {
        return manager;
    }

    public void setManager(SelectItem manager) {
        this.manager = manager;
    }

    public PositionItem getPositionItem() {
        return positionItem;
    }

    public void setPositionItem(PositionItem positionItem) {
        this.positionItem = positionItem;
    }

    public LocationItem getLocationItem() {
        return locationItem;
    }

    public void setLocationItem(LocationItem locationItem) {
        this.locationItem = locationItem;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public ReferenceItem getStatus() {
        return status;
    }

    public void setStatus(ReferenceItem status) {
        this.status = status;
    }

    public Integer getVacantPlaces() {
        return vacantPlaces;
    }

    public void setVacantPlaces(Integer vacantPlaces) {
        this.vacantPlaces = vacantPlaces;
    }

    public String getResponsibility() {
        return responsibility;
    }

    public void setResponsibility(String responsibility) {
        this.responsibility = responsibility;
    }

    public String getContractPeriod() {
        return contractPeriod;
    }

    public void setContractPeriod(String contractPeriod) {
        this.contractPeriod = contractPeriod;
    }

    public ReferenceItem getRequiredDegree() {
        return requiredDegree;
    }

    public void setRequiredDegree(ReferenceItem requiredDegree) {
        this.requiredDegree = requiredDegree;
    }
    public SelectItem[] getManagers() {
        return managers;
    }

    public void setManagers(SelectItem[] managers) {
        this.managers = managers;
    }

    public SelectItem[] getPositions() {
        return positions;
    }

    public void setPositions(SelectItem[] positions) {
        this.positions = positions;
    }

    public SelectItem[] getLocations() {
        return locations;
    }

    public void setLocations(SelectItem[] selectItems) {
        this.locations = selectItems;
    }

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public SelectItem[] getRequiredDegrees() {
        return requiredDegrees;
    }

    public void setRequiredDegrees(SelectItem[] requiredDegrees) {
        this.requiredDegrees = requiredDegrees;
    }

    public ArrayList<HistoryListItem> getVacancyNotes() {
        return vacancyNotes;
    }

    public void setVacancyNotes(ArrayList<HistoryListItem> vacancyNotes) {
        this.vacancyNotes = vacancyNotes;
    }

    public ArrayList<SelectItem> getMatchedCandidates() {
        return matchedCandidates;
    }

    public void setMatchedCandidates(ArrayList<SelectItem> matchedCandidates) {
        this.matchedCandidates = matchedCandidates;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }
    public HashMap<String, Object> getCustomFieldValuesItems() {
        return customFieldValues;
    }

    public void setCustomFieldValuesItems(HashMap<String, Object> customFieldValues) {
        this.customFieldValues = customFieldValues;
    }

    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return customFieldValues.get(columnCodeKey);
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {
        customFieldValues.put(columnCodeKey, cellValue);
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

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
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

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Integer getVacancyType() {
        return vacancyType;
    }

    public void setVacancyType(Integer vacancyType) {
        this.vacancyType = vacancyType;
    }
    public String getVacancyTypeName() {
        return vacancyTypeName;
    }

    public void setVacancyTypeName(String vacancyTypeName) {
        this.vacancyTypeName = vacancyTypeName;
    }
    public SelectItem[] getStatuses() {
        return statuses;
    }

    public void setStatuses(SelectItem[] statuses) {
        this.statuses = statuses;
    }

    public SelectItem[] getTimeTypes() {
        return timeTypes;
    }

    public void setTimeTypes(SelectItem[] timeTypes) {
        this.timeTypes = timeTypes;
    }

    public SelectItem[] getJobFamilies() {
        return jobFamilies;
    }

    public void setJobFamilies(SelectItem[] jobFamilies) {
        this.jobFamilies = jobFamilies;
    }

    public SelectItem getJobfamily() {
        return jobfamily;
    }

    public void setJobfamily(SelectItem jobfamily) {
        this.jobfamily = jobfamily;
    }

    public SelectItem getJobType() {
        return jobType;
    }

    public void setJobType(SelectItem jobType) {
        this.jobType = jobType;
    }

    public SelectItem[] getTemplates() {
        return templates;
    }

    public void setTemplates(SelectItem[] templates) {
        this.templates = templates;
    }

    public Integer getSelectedTemplateId() {
        return selectedTemplateId;
    }

    public void setSelectedTemplateId(Integer selectedTemplateId) {
        this.selectedTemplateId = selectedTemplateId;
    }

    public SelectItem getDepartment() {
        return department;
    }

    public void setDepartment(SelectItem department) {
        this.department = department;
    }

    public String getApprovalStatusCode() {
        return approvalStatusCode;
    }

    public void setApprovalStatusCode(String approvalStatusCode) {
        this.approvalStatusCode = approvalStatusCode;
    }

    public HashMap<String, String> getJobTitleLocalize() {
        return jobTitleLocalize;
    }

    public void setJobTitleLocalize(HashMap<String, String> jobTitleLocalize) {
        this.jobTitleLocalize = jobTitleLocalize;
    }

    public HashMap<String, String> getDescriptionLocalize() {
        return descriptionLocalize;
    }

    public void setDescriptionLocalize(HashMap<String, String> descriptionLocalize) {
        this.descriptionLocalize = descriptionLocalize;
    }

    public HashMap<String, String> getJobRequirementLocalize() {
        return jobRequirementLocalize;
    }

    public void setJobRequirementLocalize(HashMap<String, String> jobRequirementLocalize) {
        this.jobRequirementLocalize = jobRequirementLocalize;
    }

    public HashMap<String, String> getResponsibilitiesLocalize() {
        return responsibilitiesLocalize;
    }

    public void setResponsibilitiesLocalize(HashMap<String, String> responsibilitiesLocalize) {
        this.responsibilitiesLocalize = responsibilitiesLocalize;
    }

    public Integer getCurrentApproverEmployeeID() {
        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
            return getCurrentApprover().getExactEmployee().getId();
        }
        return null;
    }

    public String getCurrentApproverEmployeeName() {
        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
            return getCurrentApprover().getExactEmployee().getName();
        }
        return null;
    }

    public ArrayList<SpokenLanguageItem> getSpokenLanguages() {
        return spokenLanguages;
    }

    public void setSpokenLanguages(ArrayList<SpokenLanguageItem> spokenLanguages) {
        this.spokenLanguages = spokenLanguages;
    }

    public SelectItem getLocation() {
        return location;
    }

    public void setLocation(SelectItem location) {
        this.location = location;
    }

    public SelectItem getCreatorLocation() {
        return creatorLocation;
    }

    public void setCreatorLocation(SelectItem creatorLocation) {
        this.creatorLocation = creatorLocation;
    }

    public SelectItem getCreatorDepatment() {
        return creatorDepatment;
    }

    public void setCreatorDepatment(SelectItem creatorDepatment) {
        this.creatorDepatment = creatorDepatment;
    }

    public ArrayList<RelationItem> getRelations() {
        return relations;
    }

    public void setRelations(ArrayList<RelationItem> relations) {
        this.relations = relations;
        this.isRelationChanged = true;
    }
    public String getRelationName() {
        return relationName;
    }

    public void setRelationName(String relationName) {
        this.relationName = relationName;
    }

    public FileResource[] getConvertedFileResources() {
        return convertedFileResources;
    }

    public void setConvertedFileResources(FileResource[] convertedFileResources) {
        this.convertedFileResources = convertedFileResources;
    }
    public boolean isRelationChanged() {
        return isRelationChanged;
    }

    public void setRelationChanged(boolean relationChanged) {
        isRelationChanged = relationChanged;
    }

    public ReferenceLocale getReferenceLocale() {
        return referenceLocale;
    }

    public void setReferenceLocale(ReferenceLocale referenceLocale) {
        this.referenceLocale = referenceLocale;
    }
    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    public SelectItem getCurrency() {
        return this.currency;
    }

    public void setCurrency(final SelectItem currency) {
        this.currency = currency;
    }

    public Integer getManagerID() {
        return managerID;
    }

    public void setManagerID(Integer managerID) {
        this.managerID = managerID;
    }

    public SelectItem getApprover() {
        return approver;
    }

    public void setApprover(SelectItem approver) {
        this.approver = approver;
    }

    public void setVacancyQuestionItems(VacancyQuestionTableItem[] questionItems) {
        this.questionTableItem = questionItems;
    }

    public VacancyQuestionTableItem[] getVacancyQiestionItems() {
        if (questionTableItem == null) {
            return questionTableItem = new VacancyQuestionTableItem[0];
        }
        return questionTableItem;
    }
}
