package com.edatasite.workforce.core.domain.workflow;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowPush;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Azazello on 10/15/15.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "workflowPush")
public class EdsWorkflowPush extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "subject")
    private String subject;

    @Column(name = "recipient")
    private String recipient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow")
    private EdsWorkflowRule workflow;

    @Column(name = "deleted", columnDefinition = " boolean default false")
    private boolean deleted = false;

    @ManyToMany(cascade = {CascadeType.PERSIST})
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "workflowPush_permissions",
            joinColumns = {@JoinColumn(name = "workflowPush_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private Set<EdsRole> selectedRoles = new HashSet<>();

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public EdsWorkflowRule getWorkflow() {
        return workflow;
    }

    public void setWorkflow(EdsWorkflowRule workflow) {
        this.workflow = workflow;
    }

    public Set<EdsRole> getSelectedRoles() {
        return selectedRoles;
    }

    public void setSelectedRoles(Set<EdsRole> allowedRoles) {
        this.selectedRoles = allowedRoles;
    }

    public WorkflowPush getRPC(WorkflowPush item) {
        item = item == null ? new WorkflowPush() : item;
        item.setObjectID(getObjectID());
        item.setSubject(getSubject());
        item.setRecipient(getRecipient());
        item.setWorkflowID(getWorkflow() != null ? getWorkflow().getObjectID() : null);
        item.setWorkflow(getWorkflow() != null ? getWorkflow().getRPC(null) : null);
        if (getSelectedRoles() != null && getSelectedRoles().size() > 0) {
            item.setSelectedRoles(getSelectedRoles().stream().map(x -> new SelectItem(x.getObjectID(), x.getName())).collect(Collectors.toCollection(ArrayList::new)));
        }
        return item;
    }
}
