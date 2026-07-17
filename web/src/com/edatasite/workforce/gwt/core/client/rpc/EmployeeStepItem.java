package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Azazello on 7/11/15.
 */
public class EmployeeStepItem implements IsSerializable, ListingCustomFields {
    public static final String ACTION = "ACTION";
    public static final String TYPE = "TYPE";
    public static final String EMPLOYEE = "EMPLOYEE";
    public static final String EMPLOYEE_CODE = "EMPLOYEE_CODE";
    public static final String CANDIDATE_CODE = "CANDIDATE_CODE";
    public static final String EMPLOYEE_LOCATION = "EMPLOYEE_LOCATION";
    public static final String STATUS = "STATUS";
    public static final String ASSIGN_STATUS = "ASSIGN_STATUS";
    public static final String CREATION_DATE = "CREATION_DATE";
    public static final String UPDATED_DATE = "UPDATED_DATE";
    public static final String EMPLOYEE_TYPE = "EMPLOYEE_TYPE";
    public static final String CANDIDATE_TYPE = "CANDIDATE_TYPE";
    Integer objectID;
    Integer stepID;
    private String formID;
    String stepName;
    Integer statusID;
    String statusName;
    Integer employeeID;
    String employeeName;
    String employeeCode;
    String candidateCode;
    Integer typeID;
    String typeName;
    String typeCode;
    Integer locationID;
    String location;
    Integer creatorID;
    String creatorName;
    Date creationDate;
    Date updatedDate;
    SelectItem[] statuses;
    Integer currentUserID;
    String currentUserName;

    private Integer workflowID;
    private boolean workflowActionTimeBased = false;
    private String workflowActionStartTime;
    private Integer workflowActionStartTimeUnit;
    private String workflowActionStartTimeGranularity;
    private SelectItem[] onboardingSteps;

    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private ArrayList<SelectItem> expenses;
    private ArrayList<SelectItem> linkedCertificates;
    private SelectItem[] onboardingCustomFieldItems;
    private HashMap<String, Object> customFieldsMap;
    private boolean archived;
    private boolean canApprove = false;
    public static final ArrayList<String> defaultColumnNames = new ArrayList<>(Arrays.asList(
            TYPE,
            EMPLOYEE,
            EMPLOYEE_CODE,
            CANDIDATE_CODE,
            EMPLOYEE_LOCATION,
            STATUS,
            CREATION_DATE,
            UPDATED_DATE
    ));
    private ArrayList<ApproverItemMini> approvers;
    private Integer appoveStatusId;
    private Integer rejectStatusId;
    private boolean hasApprover = false;
    private String assignStatues;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getStepID() {
        return stepID;
    }

    public void setStepID(Integer stepID) {
        this.stepID = stepID;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public Integer getStatusID() {
        return statusID;
    }

    public void setStatusID(Integer statusID) {
        this.statusID = statusID;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String employeeCode) {
        this.employeeCode = employeeCode;
    }

    public String getCandidateCode() {
        return candidateCode;
    }

    public void setCandidateCode(String candidateCode) {
        this.candidateCode = candidateCode;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getLocationID() {
        return locationID;
    }

    public void setLocationID(Integer locationID) {
        this.locationID = locationID;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Integer getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(Integer creatorID) {
        this.creatorID = creatorID;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public SelectItem[] getStatuses() {
        return statuses;
    }

    public void setStatuses(SelectItem[] statuses) {
        this.statuses = statuses;
    }

    public Integer getCurrentUserID() {
        return currentUserID;
    }

    public void setCurrentUserID(Integer currentUserID) {
        this.currentUserID = currentUserID;
    }

    public String getCurrentUserName() {
        return currentUserName;
    }

    public void setCurrentUserName(String currentUserName) {
        this.currentUserName = currentUserName;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
        if (customFieldItems != null && customFieldItems.size() > 0) {
            for (CompanyCustomFieldItem customField : customFieldItems) {
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

    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public SelectItem[] getOnboardingCustomFieldItems() {
        return onboardingCustomFieldItems;
    }

    public void setOnboardingCustomFieldItems(SelectItem[] onboardingCustomFieldItems) {
        this.onboardingCustomFieldItems = onboardingCustomFieldItems;
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

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public boolean isArchived() {
        return archived;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public ArrayList<SelectItem> getExpenses() {
        if (expenses == null) {
            expenses = new ArrayList<>();
        }
        return expenses;
    }

    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return getCustomFieldsMap().get(columnCodeKey);
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {
        getCustomFieldsMap().put(columnCodeKey, cellValue);
    }

    public void setApprovers(ArrayList<ApproverItemMini> approvers) {
        this.approvers = approvers;
    }

    public ArrayList<ApproverItemMini> getApprovers() {
        return approvers;
    }

    public boolean isCanApprove() {
        return canApprove;
    }

    public void setCanApprove(boolean canApprove) {
        this.canApprove = canApprove;
    }

    public void setAppoveStatusId(Integer appoveStatusId) {
        this.appoveStatusId = appoveStatusId;
    }

    public Integer getAppoveStatusId() {
        return appoveStatusId;
    }

    public void setRejectStatusId(Integer rejectStatusId) {
        this.rejectStatusId = rejectStatusId;
    }

    public Integer getRejectStatusId() {
        return rejectStatusId;
    }

    public void setHasApprover(boolean hasApprover) {
        this.hasApprover = hasApprover;
    }

    public boolean isHasApprover() {
        return hasApprover;
    }

    public void setAssignStatues(String assignStatues) {
        this.assignStatues = assignStatues;
    }

    public String getAssignStatues() {
        return assignStatues;
    }

    public String getFormID() {
        return formID;
    }

    public void setFormID(String formID) {
        this.formID = formID;
    }

    public Integer getWorkflowID() {
        return workflowID;
    }

    public void setWorkflowID(Integer workflowID) {
        this.workflowID = workflowID;
    }

    public boolean isWorkflowActionTimeBased() {
        return workflowActionTimeBased;
    }

    public void setWorkflowActionTimeBased(boolean workflowActionTimeBased) {
        this.workflowActionTimeBased = workflowActionTimeBased;
    }

    public String getWorkflowActionStartTime() {
        return workflowActionStartTime;
    }

    public void setWorkflowActionStartTime(String workflowActionStartTime) {
        this.workflowActionStartTime = workflowActionStartTime;
    }

    public Integer getWorkflowActionStartTimeUnit() {
        return workflowActionStartTimeUnit;
    }

    public void setWorkflowActionStartTimeUnit(Integer workflowActionStartTimeUnit) {
        this.workflowActionStartTimeUnit = workflowActionStartTimeUnit;
    }

    public String getWorkflowActionStartTimeGranularity() {
        return workflowActionStartTimeGranularity;
    }

    public void setWorkflowActionStartTimeGranularity(String workflowActionStartTimeGranularity) {
        this.workflowActionStartTimeGranularity = workflowActionStartTimeGranularity;
    }

    public SelectItem[] getOnboardingSteps() {
        return onboardingSteps;
    }

    public void setOnboardingSteps(SelectItem[] onboardingSteps) {
        this.onboardingSteps = onboardingSteps;
    }

    public ArrayList<SelectItem> getLinkedCertificates() {
        if (linkedCertificates == null) linkedCertificates = new ArrayList<>();
        return linkedCertificates;
    }

    public void setLinkedCertificates(ArrayList<SelectItem> linkedCertificates) {
        this.linkedCertificates = linkedCertificates;
    }

    public static ArrayList<Integer> getIDsOnly(HashSet<EmployeeStepItem> selectedItems) {
        ArrayList<Integer> result = new ArrayList<>();
        if (selectedItems != null && selectedItems.size() > 0) {
            for (EmployeeStepItem item : selectedItems) {
                if (item.getObjectID() != null) {
                    result.add(item.getObjectID());
                }
            }
        }
        return result;
    }
}
