package com.edatasite.workforce.gwt.core.client.rpc.workflow;

import com.edatasite.workforce.gwt.core.client.enums.WorkflowExecutionCriteriaEnum;
import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.Relational;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Hayot on 2/28/14.
 */
public class WorkflowRule extends Relational implements Serializable {
    public static final String TYPE = "type";
    public static final String EXECUTION_DATE = "execution_date";
    public static final String CREATION_DATE = "creation_date";
    public static final String EXECUTION_CRITERIA = "execution_criteria";
    public static final String STATUS = "status";
    public static final String RULE_NAME = "rule_name";

    public static final String WORKFLOW_ALERT = "workflow_alert";
    public static final String WORKFLOW_EVENT = "workflow_event";
    public static final String WORKFLOW_TASK = "workflow_task";
    public static final String WORKFLOW_SMS_ALERT = "workflow_sms_alert";
    public static final String WORKFLOW_ONBOARDING_STEP = "workflow_onboarding_step";

    public static final String _WORKFLOW_MODULE = "_WORKFLOW_MODULE";
    public static final String _WORKFLOW_MODULE_LEAD = "_WORKFLOW_MODULE_LEAD";
    public static final String _WORKFLOW_MODULE_CANDIDATE = "_WORKFLOW_MODULE_CANDIDATE";
    public static final String _WORKFLOW_MODULE_CONTACT = "_WORKFLOW_MODULE_CONTACT";
    public static final String _WORKFLOW_MODULE_CASE = "_WORKFLOW_MODULE_CASE";
    public static final String _WORKFLOW_MODULE_ACTIVITY = "_WORKFLOW_MODULE_ACTIVITY";
    public static final String _WORKFLOW_MODULE_LOGACALL = "_WORKFLOW_MODULE_LOGACALL";
    public static final String _WORKFLOW_MODULE_SCHEDULED_COURSE = "_WORKFLOW_MODULE_SCHEDULED_COURSE";
    public static final String _WORKFLOW_MODULE_CS_STUDENT = "_WORKFLOW_MODULE_CS_STUDENT";
    public static final String _WORKFLOW_MODULE_HRMS_EMPLOYEE = "_WORKFLOW_MODULE_HRMS_EMPLOYEE";
    public static final String _WORKFLOW_MODULE_SALE_INVOICE = "_WORKFLOW_MODULE_SALE_INVOICE";
    public static final String _WORKFLOW_MODULE_SALEQUOTE = "_WORKFLOW_MODULE_SALEQUOTE";
    public static final String _WORKFLOW_MODULE_REQUEST_FOR_PURCHASE = "_WORKFLOW_MODULE_REQUEST_FOR_PURCHASE";
    public static final String _WORKFLOW_MODULE_PURCHASEORDER = "_WORKFLOW_MODULE_PURCHASEORDER";
    public static final String _WORKFLOW_MODULE_PAYRUN = "_WORKFLOW_MODULE_PAYRUN";
    public static final String _WORKFLOW_MODULE_CASH_ADVANCE = "_WORKFLOW_MODULE_CASH_ADVANCE";
    public static final String _WORKFLOW_MODULE_SICK_REQUEST = "_WORKFLOW_MODULE_LEAVE_REQUEST";
    public static final String _WORKFLOW_MODULE_OPPORTUNITY = "_WORKFLOW_MODULE_OPPORTUNITY";
    public static final String _WORKFLOW_MODULE_EXPENSE_CLAIM = "_WORKFLOW_MODULE_EXPENSE_CLAIM";
    public static final String _WORKFLOW_MODULE_ADDITIONAL_PAYMENT = "_WORKFLOW_MODULE_ADDITIONAL_PAYMENT";
    public static final String _WORKFLOW_MODULE_CERTIFICATE = "_WORKFLOW_MODULE_CERTIFICATE";
    public static final String _WORKFLOW_MODULE_PROJECT = "_WORKFLOW_MODULE_PROJECT";
    public static final String _WORKFLOW_MODULE_PRODUCT = "_WORKFLOW_MODULE_PRODUCT";
    public static final String _WORKFLOW_MODULE_ACCOUNT = "_WORKFLOW_MODULE_ACCOUNT";
    public static final String _WORKFLOW_MODULE_MANUAL_JOURNAL = "_WORKFLOW_MODULE_MANUAL_JOURNAL";
    public static final String _WORKFLOW_MODULE_GDN = "_WORKFLOW_MODULE_GDN";
    public static final String _WORKFLOW_MODULE_PICKLIST = "_WORKFLOW_MODULE_PICKLIST";
    public static final String _WORKFLOW_MODULE_TASK = "_WORKFLOW_MODULE_TASK";
    public static final String _WORKFLOW_MODULE_REQUEST_FOR_QUOTE = "_WORKFLOW_MODULE_REQUEST_FOR_QUOTE";
    public static final String _WORKFLOW_MODULE_PURCHASE_INVOICE = "_WORKFLOW_MODULE_PURCHASE_INVOICE";
    public static final String _WORKFLOW_MODULE_GROUP_GOAL = "_WORKFLOW_MODULE_GROUP_GOAL";
    public static final String _WORKFLOW_MODULE_SALEORDER = "_WORKFLOW_MODULE_SALEORDER";
    public static final String _WORKFLOW_MODULE_STOCK_TRANSFER = "_WORKFLOW_MODULE_STOCK_TRANSFER";
    public static final String _WORKFLOW_MODULE_STOCK_ADJUSTMENT = "_WORKFLOW_MODULE_STOCK_ADJUSTMENT";
    public static final String _WORKFLOW_MODULE_VACANCY = "_WORKFLOW_MODULE_VACANCY";
    public static final String _WORKFLOW_MODULE_DEPARTMENT = "_WORKFLOW_MODULE_DEPARTMENT";
    public static final String _WORKFLOW_MODULE_POSITION = "_WORKFLOW_MODULE_POSITION";
    public static final String _WORKFLOW_MODULE_RENTAL_ORDER = "_WORKFLOW_MODULE_RENTAL_ORDER";
    public static final String _WORKFLOW_MODULE_RENTAL_PRODUCT = "_WORKFLOW_MODULE_RENTAL_PRODUCT";
    public static final String _WORKFLOW_MODULE_RECEIVE_PAYMENT = "_WORKFLOW_MODULE_RECEIVE_PAYMENT";
    public static final String _WORKFLOW_MODULE_PAY_INVOICE = "_WORKFLOW_MODULE_PAY_INVOICE";
    public static final String _WORKFLOW_MODULE_INCIDENT = "_WORKFLOW_MODULE_INCIDENT";
    public static final String _WORKFLOW_MODULE_PLACEMENT = "_WORKFLOW_MODULE_PLACEMENT";
    public static final String _WORKFLOW_MODULE_SHIFT = "_WORKFLOW_MODULE_SHIFT";
    public static final String _WORKFLOW_MODULE_ROTATION = "_WORKFLOW_MODULE_ROTATION";
    public static final String _WORKFLOW_MODULE_GROUP_PLACEMENT = "_WORKFLOW_MODULE_GROUP_PLACEMENT";
    public static final String _WORKFLOW_MODULE_PRODUCT_CATEGORY = "_WORKFLOW_MODULE_PRODUCT_CATEGORY";
    public static final String _WORKFLOW_MODULE_OVERTIME = "_WORKFLOW_MODULE_OVERTIME";
    public static final String _WORKFLOW_MODULE_CREDIT_NOTE = "_WORKFLOW_MODULE_CREDIT_NOTE";
    public static final String _WORKFLOW_MODULE_DEBIT_NOTE = "_WORKFLOW_MODULE_DEBIT_NOTE";
    public static final String _WORKFLOW_MODULE_BACKUP_EMPLOYEE = "_WORKFLOW_MODULE_BACKUP_EMPLOYEE";

    public static final String _WORKFLOW_MODULE_COMPANY_SETTINGS = "_WORKFLOW_MODULE_COMPANY_SETTINGS";
    public static final String _WORKFLOW_MODULE_EMPLOYEE_DOCUMENTS = "_WORKFLOW_MODULE_EMPLOYEE_DOCUMENTS";
    public static final String _WORKFLOW_MODULE_BUILD_ASSEMBLY = "_WORKFLOW_MODULE_BUILD_ASSEMBLY";

    private Integer objectID;
    private String creator;
    private String name;
    private String module;
    private boolean active;
    private String description;
    private WorkflowExecutionCriteriaEnum executionCriteria;
    private String executionCriteriaUpdateField;
    private String ruleCriteria;
    private String pattern;
    private HashMap<Integer, WorkflowCondition> conditions = new HashMap<>();
    private RecurrenceJobItem recurrenceJobItem;

    private SelectItem[] modules;
    private SelectItem[] onboardingSteps;
    private Integer recurrenceID;

    private String activitiesType;
    private String activitiesRuleName;
    private Date executionDate;
    private Integer entityId;
    private boolean callLog = false;
    private String entityName;
    private String entityLink;
    private boolean dynamicCondition = false;
    private String dynamicConditionQuery;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExecutionCriteriaUpdateField() {
        return executionCriteriaUpdateField;
    }

    public void setExecutionCriteriaUpdateField(String executionCriteriaUpdateField) {
        this.executionCriteriaUpdateField = executionCriteriaUpdateField;
    }

    public WorkflowExecutionCriteriaEnum getExecutionCriteria() {
        return executionCriteria;
    }

    public void setExecutionCriteria(WorkflowExecutionCriteriaEnum executionCriteria) {
        this.executionCriteria = executionCriteria;
    }

    public String getRuleCriteria() {
        return ruleCriteria;
    }

    public void setRuleCriteria(String ruleCriteria) {
        this.ruleCriteria = ruleCriteria;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public SelectItem[] getModules() {
        return modules;
    }

    public void setModules(SelectItem[] modules) {
        this.modules = modules;
    }

    public HashMap<Integer, WorkflowCondition> getConditions() {
        if(conditions == null){
            conditions = new HashMap<>();
        }
        return conditions;
    }

    public void setConditions(HashMap<Integer, WorkflowCondition> conditions) {
        this.conditions = conditions;
    }

    public void addCondition(WorkflowCondition condition) {
        if (condition != null) {
            this.conditions.put(condition.getConditionID(), condition);
        }
    }

    public RecurrenceJobItem getRecurrenceJobItem() {
        return recurrenceJobItem;
    }

    public void setRecurrenceJobItem(RecurrenceJobItem recurrenceJobItem) {
        this.recurrenceJobItem = recurrenceJobItem;
    }

    public static ArrayList<Integer> getIDsOnly(HashSet<WorkflowRule> selectedItems) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (WorkflowRule item : selectedItems) {
            ids.add(item.getObjectID());
        }
        return ids;
    }

    @Override
    public Integer getRelationID() {
        return getObjectID();
    }

    @Override
    public String getRelationType() {
        return RelationItem.TYPE_WORKFLOW;
    }

    @Override
    public String getRelationName() {
        return getName();
    }

    public Integer getRecurrenceID() {
        return recurrenceID;
    }

    public void setRecurrenceID(Integer recurrenceID) {
        this.recurrenceID = recurrenceID;
    }

    public String getActivitiesType() {
        return activitiesType;
    }

    public void setActivitiesType(String activitiesType) {
        this.activitiesType = activitiesType;
    }

    public String getActivitiesRuleName() {
        return activitiesRuleName;
    }

    public void setActivitiesRuleName(String activitiesRuleName) {
        this.activitiesRuleName = activitiesRuleName;
    }

    public Date getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(Date executionDate) {
        this.executionDate = executionDate;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public boolean isCallLog() {
        return callLog;
    }

    public void setCallLog(boolean callLog) {
        this.callLog = callLog;
    }

    public SelectItem[] getOnboardingSteps() {
        return onboardingSteps;
    }

    public void setOnboardingSteps(SelectItem[] onboardingSteps) {
        this.onboardingSteps = onboardingSteps;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityLink() {
        return entityLink;
    }

    public void setEntityLink(String entityLink) {
        this.entityLink = entityLink;
    }

    public boolean isDynamicCondition() {
        return dynamicCondition;
    }

    public void setDynamicCondition(boolean dynamicCondition) {
        this.dynamicCondition = dynamicCondition;
    }

    public String getDynamicConditionQuery() {
        return dynamicConditionQuery;
    }

    public void setDynamicConditionQuery(String dynamicConditionQuery) {
        this.dynamicConditionQuery = dynamicConditionQuery;
    }
}
