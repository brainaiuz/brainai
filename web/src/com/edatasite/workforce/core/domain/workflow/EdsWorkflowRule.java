package com.edatasite.workforce.core.domain.workflow;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.enums.WorkflowExecutionCriteriaEnum;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowRule;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Hayot on 2/27/14.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "workflowrule")
public class EdsWorkflowRule extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator")
    private EdsUser creator;

    private String name;

    private String module;

    @Type(type = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    private WorkflowExecutionCriteriaEnum executionCriteria;

    private String executionCriteriaUpdateField;

    @Type(type = "text")
    private String ruleCriteria;

    @Column(name = "deleted", columnDefinition = "boolean default false")
    private boolean deleted = false;

    private String pattern;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "conditions")
    private Set<EdsWorkflowCondition> conditions = new HashSet<>();

    @Column(name = "recurrenceID")
    private Integer recurrenceID;

    @Column(name="showInList", columnDefinition = " boolean default true")
    private boolean showInList = true;

    @Column(name="active", columnDefinition = " boolean default true")
    private boolean active = true;

    @Column(name = "dynamicCondition", columnDefinition = " boolean default false")
    private boolean dynamicCondition = false;

    @Type(type = "text")
    @Column(columnDefinition = "text")
    private String dynamicConditionQuery;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsUser getCreator() {
        return creator;
    }

    public void setCreator(EdsUser creator) {
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

    public WorkflowExecutionCriteriaEnum getExecutionCriteria() {
        return executionCriteria;
    }

    public void setExecutionCriteria(WorkflowExecutionCriteriaEnum executionCriteria) {
        this.executionCriteria = executionCriteria;
    }

    public String getExecutionCriteriaUpdateField() {
        return executionCriteriaUpdateField;
    }

    public void setExecutionCriteriaUpdateField(String executionCriteriaUpdateField) {
        this.executionCriteriaUpdateField = executionCriteriaUpdateField;
    }

    public String getRuleCriteria() {
        return ruleCriteria;
    }

    public void setRuleCriteria(String ruleCriteria) {
        this.ruleCriteria = ruleCriteria;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Set<EdsWorkflowCondition> getConditions() {
        return conditions;
    }

    public void setConditions(Set<EdsWorkflowCondition> conditions) {
        this.conditions = conditions;
    }

    public Integer getRecurrenceID() {
        return recurrenceID;
    }

    public void setRecurrenceID(Integer recurrenceID) {
        this.recurrenceID = recurrenceID;
    }

    public boolean isShowInList() {
        return showInList;
    }

    public void setShowInList(boolean showInList) {
        this.showInList = showInList;
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

    public void setDynamicConditionQuery(String conditionQuery) {
        this.dynamicConditionQuery = conditionQuery;
    }

    public WorkflowRule getRPC(WorkflowRule item) {
        if (item == null) {
            item = new WorkflowRule();
        }
        item.setObjectID(getObjectID());
        item.setCreator(getCreator() != null ? getCreator().getFullName() : null);
        item.setName(getName());
        item.setDescription(getDescription());
        item.setModule(getModule());
        item.setActive(isActive());
        item.setExecutionCriteria(getExecutionCriteria());
        item.setExecutionCriteriaUpdateField(getExecutionCriteriaUpdateField());
        item.setRuleCriteria(getRuleCriteria());
        item.setPattern(getPattern());
        item.setDynamicCondition(isDynamicCondition());
        item.setDynamicConditionQuery(getDynamicConditionQuery());
        for (EdsWorkflowCondition condition : getConditions()) {
            item.addCondition(condition.getRPC(null));
        }
        return item;
    }

    public String getModuleAsString() {
        if (WorkflowRule._WORKFLOW_MODULE_LEAD.equals(module)) {
            return "Lead";
        } else if (WorkflowRule._WORKFLOW_MODULE_CANDIDATE.equals(module)) {
            return "Candidate";
        } else if (WorkflowRule._WORKFLOW_MODULE_CASE.equals(module)) {
            return "Case";
        } else if (WorkflowRule._WORKFLOW_MODULE_CONTACT.equals(module)) {
            return "Contact";
        } else if (WorkflowRule._WORKFLOW_MODULE_ACTIVITY.equals(module)) {
            return "Event";
        } else if (WorkflowRule._WORKFLOW_MODULE_LOGACALL.equals(module)) {
            return "Log a Call";
        } else if (WorkflowRule._WORKFLOW_MODULE_SCHEDULED_COURSE.equals(module)) {
            return "Course Schedule";
        } else if (WorkflowRule._WORKFLOW_MODULE_CS_STUDENT.equals(module)) {
            return "Course Schedule Student";
        } else if (WorkflowRule._WORKFLOW_MODULE_HRMS_EMPLOYEE.equals(module)) {
            return "Employee";
        } else if (WorkflowRule._WORKFLOW_MODULE_SALE_INVOICE.equals(module)) {
            return "Sales Invoice";
        } else if (WorkflowRule._WORKFLOW_MODULE_SALEQUOTE.equals(module)) {
            return "Sales Quote";
        } else if (WorkflowRule._WORKFLOW_MODULE_REQUEST_FOR_PURCHASE.equals(module)) {
            return "Request For Purchase";
        } else if (WorkflowRule._WORKFLOW_MODULE_PURCHASEORDER.equals(module)) {
            return "Purchase Order";
        } else if (WorkflowRule._WORKFLOW_MODULE_PAYRUN.equals(module)) {
            return "Payrun";
        } else if (WorkflowRule._WORKFLOW_MODULE_CASH_ADVANCE.equals(module)) {
            return "Cash Advance";
        } else if (WorkflowRule._WORKFLOW_MODULE_SICK_REQUEST.equals(module)) {
            return "Leave Request";
        } else if (WorkflowRule._WORKFLOW_MODULE_OPPORTUNITY.equals(module)) {
            return "Opportunity";
        } else if (WorkflowRule._WORKFLOW_MODULE_EXPENSE_CLAIM.equals(module)) {
            return "Expense Claim";
        } else if (WorkflowRule._WORKFLOW_MODULE_ADDITIONAL_PAYMENT.equals(module)) {
            return "Additional Payment";
        } else if (WorkflowRule._WORKFLOW_MODULE_REQUEST_FOR_QUOTE.equals(module)) {
            return "Request For Quote";
        } else if (WorkflowRule._WORKFLOW_MODULE_STOCK_TRANSFER.equals(module)) {
            return "Stock Transfer";
        } else if (WorkflowRule._WORKFLOW_MODULE_STOCK_ADJUSTMENT.equals(module)) {
            return "Stock Adjustment";
        } else if (WorkflowRule._WORKFLOW_MODULE_PLACEMENT.equals(module)) {
            return "Placement";
        }
        return module;
    }
}
