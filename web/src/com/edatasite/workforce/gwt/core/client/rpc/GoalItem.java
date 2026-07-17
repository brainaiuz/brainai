package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: Oct 24, 2009
 * Time: 1:50:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class GoalItem implements ListingCustomFields, IsSerializable {

    public static final String ACTION = "action";
    public static final String GOAL_LIST_TITLE = "goalListTitle";
    public static final String GOAL_LIST_DESCRIPTION = "goalListDescription";
    public static final String GOAL_LIST_FROM_DATE = "goalListFromDate";
    public static final String GOAL_LIST_TO_DATE = "goalListToDate";
    public static final String GOAL_LIST_RESOVER = "goalListResolver";
    public static final String GOAL_LIST_ASSIGN = "goalListAssign";
    public static final String GOAL_LIST_WEIGHT = "goalListWeight";
    public static final String GOAL_LIST_SCORE = "goalListScore";
    public static final String GOAL_LIST_STRATEGIC = "goalListStrategic";
    public static final String GOAL_LIST_PROJECT = "goalListProject";
    public static final String GOAL_LIST_DEPARTMENT = "goalListDepartment";
    public static final String GOAL_LIST_LOCATION = "goalListLocation";
    public static final String GOAL_LIST_VALIDITY_PERIOD = "goalListValidityPeriod";
    public static final String GOAL_NUMBER = "goalNumber";
    public static final String GOAL_STATUS = "statuss";

    public static final String COMPANY_GOAL_LIST_TITLE = "companyGoalListTitle";
    public static final String COMPANY_GOAL_LIST_DESCRIPTION = "companyGoalListDescription";
    public static final String COMPANY_GOAL_LIST_OUTCOME = "companyGoalListOutcome";
    public static final String COMPANY_GOAL_LIST_FROM_DATE = "companyGoalListFromDate";
    public static final String COMPANY_GOAL_LIST_TO_DATE = "companyGoalListToDate";
    public static final String COMPANY_GOAL_LIST_STATUS = "companyGoalListStatus";
    public static final String EMPLOYEE_GOAL_LIST_GOAL_CATEGORY = "employeeGoalListCompanyGoalCategory";
    public static final String EMPLOYEE_GOAL_LIST_STATUS = "employeeGoalListStatus";
    public static final String EMPLOYEE_GOAL_LIST_TITLE = "employeeGoalListTitle";
    public static final String EMPLOYEE_GOAL_LIST_DESCRIPTION = "employeeGoalListDescription";
    public static final String EMPLOYEE_GOAL_LIST_WEIGHT = "employeeGoalListWeight";
    public static final String EMPLOYEE_GOAL_LIST_ACTIONSTEPS = "employeeGoalListActionSteps";
    //  public static final String EMPLOYEE_GOAL_LIST_CODE = "employeeGoalListCode";
    public static final String EMPLOYEE_GOAL_LIST_RESOLVER = "employeeGoalListResolver";
    public static final String PROJECT_GOAL = "projectgoal";

    private DateNonConvertable projectStartDate;
    private DateNonConvertable projectEndDate;
    private Integer objectId;
    private String companyGoal;
    private SelectItem[] companyGoals;
    private Integer companyGoalId;
    private String title;
    private String description;
    private String outcome;
    private String actionSteps;
    private DateNonConvertable fromDate;
    private DateNonConvertable toDate;
    private SelectItem[] statuss;
    private Integer statusId;
    private String status;
    private SelectItem[] goalCategorys;
    private Integer goalCategoryId;
    private int departmentGoalWeight;
    private String goalCategory;
    private Double progress;
    private SelectItem[] weights;
    private Integer weightId;
    private double weight = 0;
    private String weightString;
    private SelectItem[] resolvers;
    private Integer resolverId;
    private String resolver;
    private SelectItem[] projects;
    private SelectItem[] projectGoals;
    private Integer projectId;
    private String project;
    private GoalAssigneeItem[] goalAssigneeItem;
    private Integer personalGoalId;
    private Integer departmentGoalId;
    private Integer projectGoalId;
    private Integer selectedProjectGoalId;
    private String projectGoalTitle;
    private Integer businGoalId;
    private String goalAssignedTo;
    private Integer assigneeId;
    private String creatorName;
    private Integer creatorId;
    private SelectItem[] departments;
    private SelectItem[] locations;
    private String location;
    private Integer locationId;
    private Integer departmentId;
    private Integer avialableWeight;
    private String department;
    private FileItem[] attachments;
    private FileResource[] goalAttachments;
    private ArrayList<HistoryListItem> notes;
    private SelectItem[] scores;
    private SelectItem score;
    private ValidityPeriodItem[] validityPeriodItems;
    private ValidityPeriodItem validityPeriodItem;
    private SelectItem measurementUnit;
    private Integer selectedEmployeeID;
    private Integer targetGoal;
    private Integer actualGoal;
    private DepartmentGoalChartSettingsItem chartSettings;
    private ArrayList<CompanyCustomFieldItem> customFields;
    private HashMap<String, Object> customFieldsMap;
    private NumberData goalNumber;
    private Double givenScore = 0.d;
    private ArrayList<RelationItem> relations;
    private boolean isRelationChanged = false;

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public FileResource[] getGoalAttachments() {
        return goalAttachments;
    }

    public void setGoalAttachments(FileResource[] goalAttachments) {
        this.goalAttachments = goalAttachments;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getCompanyGoal() {
        return companyGoal;
    }

    public void setCompanyGoal(String companyGoal) {
        this.companyGoal = companyGoal;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public String getActionSteps() {
        return actionSteps;
    }

    public void setActionSteps(String actionSteps) {
        this.actionSteps = actionSteps;
    }

    public DateNonConvertable getFromDate() {
        return fromDate;
    }

    public void setFromDate(DateNonConvertable fromDate) {
        this.fromDate = fromDate;
    }

    public DateNonConvertable getToDate() {
        return toDate;
    }

    public void setToDate(DateNonConvertable toDate) {
        this.toDate = toDate;
    }

    public SelectItem[] getStatuss() {
        return statuss;
    }

    public void setStatuss(SelectItem[] statuss) {
        this.statuss = statuss;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SelectItem[] getGoalCategorys() {
        return goalCategorys;
    }

    public void setGoalCategorys(SelectItem[] goalCategorys) {
        this.goalCategorys = goalCategorys;
    }

    public Integer getGoalCategoryId() {
        return goalCategoryId;
    }

    public void setGoalCategoryId(Integer goalCategoryId) {
        this.goalCategoryId = goalCategoryId;
    }

    public String getGoalCategory() {
        return goalCategory;
    }

    public void setGoalCategory(String goalCategory) {
        this.goalCategory = goalCategory;
    }

    public Double getProgress() {
        return progress;
    }

    public void setProgress(Double progress) {
        this.progress = progress;
    }

    public SelectItem[] getWeights() {
        return weights;
    }

    public void setWeights(SelectItem[] weights) {
        this.weights = weights;
    }

    public Integer getWeightId() {
        return weightId;
    }

    public void setWeightId(Integer weightId) {
        this.weightId = weightId;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getWeightString() {
        return weightString;
    }

    public void setWeightString(String weightString) {
        this.weightString = weightString;
    }

    public SelectItem[] getResolvers() {
        return resolvers;
    }

    public void setResolvers(SelectItem[] resolvers) {
        this.resolvers = resolvers;
    }

    public Integer getResolverId() {
        return resolverId;
    }

    public void setResolverId(Integer resolverId) {
        this.resolverId = resolverId;
    }

    public String getResolver() {
        return resolver;
    }

    public void setResolver(String resolver) {
        this.resolver = resolver;
    }

    public SelectItem[] getCompanyGoals() {
        return companyGoals;
    }

    public void setCompanyGoals(SelectItem[] companyGoals) {
        this.companyGoals = companyGoals;
    }

    public Integer getCompanyGoalId() {
        return companyGoalId;
    }

    public void setCompanyGoalId(Integer companyGoalId) {
        this.companyGoalId = companyGoalId;
    }

    public GoalAssigneeItem[] getGoalAssigneeItem() {
        return goalAssigneeItem;
    }

    public void setGoalAssigneeItem(GoalAssigneeItem[] goalAssigneeItem) {
        this.goalAssigneeItem = goalAssigneeItem;
    }

    public Integer getPersonalGoalId() {
        return personalGoalId;
    }

    public void setPersonalGoalId(Integer personalGoalId) {
        this.personalGoalId = personalGoalId;
    }

    public Integer getDepartmentGoalId() {
        return departmentGoalId;
    }

    public void setDepartmentGoalId(Integer departmentGoalId) {
        this.departmentGoalId = departmentGoalId;
    }

    public Integer getProjectGoalId() {
        return projectGoalId;
    }

    public void setProjectGoalId(Integer projectGoalId) {
        this.projectGoalId = projectGoalId;
    }

    public SelectItem[] getProjects() {
        return projects;
    }

    public void setProjects(SelectItem[] projects) {
        this.projects = projects;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getGoalAssignedTo() {
        return goalAssignedTo;
    }

    public void setGoalAssignedTo(String goalAssignedTo) {
        this.goalAssignedTo = goalAssignedTo;
    }

    public Integer getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Integer assigneeId) {
        this.assigneeId = assigneeId;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    public SelectItem[] getDepartments() {
        return departments;
    }

    public void setDepartments(SelectItem[] departments) {
        this.departments = departments;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getBusinGoalId() {
        return businGoalId;
    }

    public void setBusinGoalId(Integer businGoalId) {
        this.businGoalId = businGoalId;
    }

    public ArrayList<HistoryListItem> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<HistoryListItem> notes) {
        this.notes = notes;
    }

    public SelectItem[] getScores() {
        return scores;
    }

    public void setScores(SelectItem[] scores) {
        this.scores = scores;
    }

    public SelectItem getScore() {
        return score;
    }

    public void setScore(SelectItem score) {
        this.score = score;
    }

    public void setCustomFields(ArrayList<CompanyCustomFieldItem> customFields) {
        this.customFields = customFields;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFields() {
        return customFields;
    }

    public ValidityPeriodItem getValidityPeriodItem() {
        /*if (validityPeriodItem == null && getValidityPeriodItems() != null) {
            for (ValidityPeriodItem periodItem : getValidityPeriodItems()) {
                if (periodItem.isDefault()) {
                    validityPeriodItem = periodItem;
                    break;
                }

            }
        }*/
        return validityPeriodItem;
    }

    public void setValidityPeriodItem(ValidityPeriodItem validityPeriodItem) {
        this.validityPeriodItem = validityPeriodItem;
    }

    public ValidityPeriodItem[] getValidityPeriodItems() {
        return validityPeriodItems;
    }

    public void setValidityPeriodItems(ValidityPeriodItem[] validityPeriodItems) {
        this.validityPeriodItems = validityPeriodItems;
    }

    public SelectItem getMeasurementUnit() {
        return measurementUnit;
    }

    public void setMeasurementUnit(SelectItem measurementUnit) {
        this.measurementUnit = measurementUnit;
    }

    public Double getGivenScore() {
        return givenScore;
    }

    public void setGivenScore(Double givenScore) {
        this.givenScore = givenScore;
    }

    @Override
    public String toString() {
        return "GoalItem{" +
                "objectId=" + objectId +
                ", companyGoal='" + companyGoal + '\'' +
                ", companyGoalId=" + companyGoalId +
                ", companyGoals=" + (companyGoals == null ? null : Arrays.asList(companyGoals)) +
                '}';
    }

    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return getCustomFieldsMap().get(columnCodeKey);
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {
        getCustomFieldsMap().put(columnCodeKey, cellValue);
    }

    public HashMap<String, Object> getCustomFieldsMap() {
        return customFieldsMap;
    }

    public void setCustomFieldsMap(HashMap<String, Object> customFieldsMap) {
        this.customFieldsMap = customFieldsMap;
    }

    public void setRelations(ArrayList<RelationItem> relations) {
        this.relations = relations;
        isRelationChanged = true;
    }

    public ArrayList<RelationItem> getRelations() {
        if (relations == null) {
            relations = new ArrayList<>();
        }
        return relations;
    }

    public boolean isRelationChanged() {
        return isRelationChanged;
    }

    public void setRelationChanged(boolean relationChanged) {
        isRelationChanged = relationChanged;
    }

    public Integer getSelectedEmployeeID() {
        return selectedEmployeeID;
    }

    public void setSelectedEmployeeID(Integer selectedEmployeeID) {
        this.selectedEmployeeID = selectedEmployeeID;
    }

    public NumberData getGoalNumber() {
        return goalNumber;
    }

    public void setGoalNumber(NumberData goalNumber) {
        this.goalNumber = goalNumber;
    }


    public Integer getSelectedProjectGoalId() {
        return selectedProjectGoalId;
    }

    public void setSelectedProjectGoalId(Integer selectedProjectGoalId) {
        this.selectedProjectGoalId = selectedProjectGoalId;
    }

    public String getProjectGoalTitle() {
        return projectGoalTitle;
    }

    public void setProjectGoalTitle(String projectGoalTitle) {
        this.projectGoalTitle = projectGoalTitle;
    }

    public SelectItem[] getProjectGoals() {
        return projectGoals;
    }

    public void setProjectGoals(SelectItem[] projectGoals) {
        this.projectGoals = projectGoals;
    }


    public DateNonConvertable getProjectStartDate() {
        return projectStartDate;
    }

    public void setProjectStartDate(DateNonConvertable startDate) {
        this.projectStartDate = startDate;
    }

    public DateNonConvertable getProjectEndDate() {
        return projectEndDate;
    }

    public void setProjectEndDate(DateNonConvertable endDate) {
        this.projectEndDate = endDate;
    }

    public Integer getActualGoal() {
        return actualGoal;
    }

    public Integer getTargetGoal() {
        return targetGoal;
    }

    public void setTargetGoal(Integer targetGoal) {
        this.targetGoal = targetGoal;
    }

    public DepartmentGoalChartSettingsItem getChartSettings() {
        return chartSettings;
    }

    public void setChartSettings(DepartmentGoalChartSettingsItem chartSettings) {
        this.chartSettings = chartSettings;
    }

    public void setActualGoal(Integer actualGoal) {
        this.actualGoal = actualGoal;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getAvialableWeight() {
        return avialableWeight;
    }

    public void setAvialableWeight(Integer avialableWeight) {
        this.avialableWeight = avialableWeight;
    }

    public int getDepartmentGoalWeight() {
        return departmentGoalWeight;
    }

    public void setDepartmentGoalWeight(int departmentGoalWeight) {
        this.departmentGoalWeight = departmentGoalWeight;
    }

    public SelectItem[] getLocations() {
        return locations;
    }

    public void setLocations(SelectItem[] locations) {
        this.locations = locations;
    }
}
