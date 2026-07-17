package com.edatasite.workforce.core.domain.workflow;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowInvoice;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Azazello on 10/6/16.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "workflowInvoice")
public class EdsWorkflowInvoice extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "workflowID")
    private Integer workflowID;

    @Column(name = "isworkflowactionTimeBased", columnDefinition = "boolean default false")
    private boolean workflowActionTimeBased = false;
    private String workflowActionStartTime;
    private Integer workflowActionStartTimeUnit;
    private String workflowActionStartTimeGranularity;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "invoiceFields")
    private Set<EdsWorkflowInvoiceField> invoiceFields = new HashSet<>();

    @Override
    public Integer getObjectID() {
        return objectID;
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

    public Set<EdsWorkflowInvoiceField> getInvoiceFields() {
        if(invoiceFields == null){
            invoiceFields = new HashSet<>();
        }
        return invoiceFields;
    }

    public WorkflowInvoice getRPC(WorkflowInvoice item) {
        item = item != null ? item : new WorkflowInvoice();
        item.setObjectID(getObjectID());
        item.setWorkflowID(getWorkflowID());
        for(EdsWorkflowInvoiceField field : getInvoiceFields()){
            item.getInvoiceFields().add(field.getRPC());
        }
        return item;
    }
}
