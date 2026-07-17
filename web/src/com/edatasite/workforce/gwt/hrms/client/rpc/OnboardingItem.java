package com.edatasite.workforce.gwt.hrms.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.EmployeeStepItem;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: User
 * Date: 8/25/12
 * Time: 12:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class OnboardingItem implements IsSerializable, ListingCustomFields {

    public static final String ONBOARDING_PERIOD_ACTION = Constants.LISTING_ACTION.COLUMN_CODE;
    public static final String ONBOARDING_STEP_ACTION = Constants.LISTING_ACTION.COLUMN_CODE;
    public static final String ONBOARDING_STEP_NAME = "stepName";
    public static final String ONBOARDING_STEP_DESCRIPTION = "stepDescription";
    public static final String ONBOARDING_PERIOD_NAME = "periodName";
    public static final String ONBOARDING_PERIOD_PARENT_STEP = "parentStep";
    public static final String ONBOARDING_PERIOD_RELIATIVE_START = "periodRelativeStart";
    public static final String ONBOARDING_PERIOD_DURATION = "periodDuration";
    public static final String ONBOARDING_PERIOD_ACTIVE = "periodActive";
    public static final String ONBOARDING_PERIOD_DESCRIPTION = "periodDescription";

    public static final String EMPLOYEE_NAME = "employeeName";

    private String stepName;
    private Integer stepId;
    private String stepDescription;
    private String periodName;
    private Integer periodId;
    private String periodDescription;
    private Integer periodRelativeStart;
    private Integer duration;
    private SelectItem[] periods;
    private Boolean showInEmployeeProfile;
    private Boolean periodActive;
    private Boolean isBeforeHireDate;
    private Boolean stepStatus;
    private boolean createForm;
    private Integer employeeId;
    private LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> roles;
    private String selectedRoles;
    private ArrayList<Integer> rolesForSave;
    private ArrayList<Integer> employeesForSave;
    private ArrayList<ReferenceItem> statusItems;
    private Integer parentID;
    private String parentName;
    private SelectItem[] parentSteps;
    private String formID;
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private String viewName;
    private EmployeeStepItem assignedEmployee;
    private HashMap<String, Object> customFieldsMap;


    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Boolean getStepStatus() {
        return stepStatus;
    }

    public void setStepStatus(Boolean stepStatus) {
        this.stepStatus = stepStatus;
    }

    public Integer getStepId() {
        return stepId;
    }

    public void setStepId(Integer stepId) {
        this.stepId = stepId;
    }

    public ArrayList<Integer> getRolesForSave() {
        return rolesForSave;
    }

    public void setRolesForSave(ArrayList<Integer> rolesForSave) {
        this.rolesForSave = rolesForSave;
    }

    public ArrayList<Integer> getEmployeesForSave() {
        return employeesForSave;
    }

    public void setEmployeesForSave(ArrayList<Integer> employeesForSave) {
        this.employeesForSave = employeesForSave;
    }

    public Integer getPeriodId() {
        return periodId;
    }

    public void setPeriodId(Integer periodId) {
        this.periodId = periodId;
    }

    public Boolean getBeforeHireDate() {
        return isBeforeHireDate;
    }

    public void setBeforeHireDate(Boolean beforeHireDate) {
        isBeforeHireDate = beforeHireDate;
    }

    public Boolean getPeriodActive() {
        return periodActive;
    }

    public void setPeriodActive(Boolean periodActive) {
        this.periodActive = periodActive;
    }

    public LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getRoles() {
        return roles;
    }

    public void setRoles(LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> roles) {
        this.roles = roles;
    }

    public String getSelectedRoles() {
        return selectedRoles;
    }

    public void setSelectedRoles(String selectedRoles) {
        this.selectedRoles = selectedRoles;
    }

    public Boolean getShowInEmployeeProfile() {
        return showInEmployeeProfile;
    }

    public void setShowInEmployeeProfile(Boolean showInEmployeeProfile) {
        this.showInEmployeeProfile = showInEmployeeProfile;
    }


    public SelectItem[] getPeriods() {
        return periods;
    }

    public void setPeriods(SelectItem[] periods) {
        this.periods = periods;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getStepDescription() {
        return stepDescription;
    }

    public void setStepDescription(String stepDescription) {
        this.stepDescription = stepDescription;
    }

    public String getPeriodName() {
        return periodName;
    }

    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    public String getPeriodDescription() {
        return periodDescription;
    }

    public void setPeriodDescription(String periodDescription) {
        this.periodDescription = periodDescription;
    }

    public Integer getPeriodRelativeStart() {
        return periodRelativeStart;
    }

    public void setPeriodRelativeStart(Integer periodRelativeStart) {
        this.periodRelativeStart = periodRelativeStart;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public boolean isCreateForm() {
        return createForm;
    }

    public void setCreateForm(boolean createForm) {
        this.createForm = createForm;
    }

    public ArrayList<ReferenceItem> getStatusItems() {
        return statusItems;
    }

    public void setStatusItems(ArrayList<ReferenceItem> statusItems) {
        this.statusItems = statusItems;
    }

    public void setParentID(Integer parentID) {
        this.parentID = parentID;
    }

    public Integer getParentID() {
        return parentID;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public SelectItem[] getParentSteps() {
        return parentSteps;
    }

    public void setParentSteps(SelectItem[] parentSteps) {
        this.parentSteps = parentSteps;
    }

    public String getFormID() {
        return formID;
    }

    public void setFormID(String formID) {
        this.formID = formID;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }

    public EmployeeStepItem getAssignedEmployee() {
        return assignedEmployee;
    }

    public void setAssignedEmployee(EmployeeStepItem assignedEmployee) {
        this.assignedEmployee = assignedEmployee;
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
        if (customFieldsMap == null) {
            customFieldsMap = new HashMap<>();
        }
        return customFieldsMap;
    }

    public void setCustomFieldsMap(HashMap<String, Object> customFieldsMap) {
        this.customFieldsMap = customFieldsMap;
    }
}
