package com.edatasite.workforce.core.domain.approving;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsApprovableHistory;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.workflow.EdsWorkflowRule;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

/**
 * Created by Hayot on 1/31/2016.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "approvers")
public class EdsApprover extends EdsObject {

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Column(name = "is_default")
    private Boolean is_default = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status")
    private EdsReference status;

    @Column(name="entity_id")
    private Integer entityID;

    @Column(name="onApprovedAction")
    private Integer onApprovedAction;

    @Column(name = "onRejectedAction")
    private Integer onRejectedAction;

    @Column(name = "entityType")
    private String entityType;

    @Column(name = "stepEmployeeType")
    private String stepEmployeeType;

    @Column(name = "is_location")
    private Boolean isLocation = false;

    @Column(name = "approver_order")
    private Integer approverOrder;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exactApprover")
    private EdsUser exactEmployee;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workflow")
    private EdsWorkflowRule onApprovedWorkflow;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rejected_workflow")
    private EdsWorkflowRule onRejectedWorkflow;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH}, fetch = FetchType.LAZY, mappedBy = "approver", orphanRemoval = true)
    private Set<EdsApproverRoles> approverRoles = new HashSet<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH}, fetch = FetchType.LAZY, mappedBy = "approver", orphanRemoval = true)
    private Set<EdsApproverEmployees> approverEmployees = new HashSet<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH}, fetch = FetchType.LAZY, mappedBy = "approver", orphanRemoval = true)
    private Set<EdsDynamicApprover> dynamicQueries = new HashSet<>();

    private Integer approveStatusID;
    private Integer rejectStatusID;
    private Integer startStatusID;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "approverId")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private Set<EdsApprovableHistory> approverHistory;

    @Column(name = "backup", columnDefinition = " boolean default false")
    private Boolean backup = false;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsWorkflowRule getOnApprovedWorkflow() {
        return onApprovedWorkflow;
    }

    public void setOnApprovedWorkflow(EdsWorkflowRule workflow) {
        this.onApprovedWorkflow = workflow;
    }

    public EdsWorkflowRule getOnRejectedWorkflow() {
        return onRejectedWorkflow;
    }

    public void setOnRejectedWorkflow(EdsWorkflowRule onRejectedWorkflow) {
        this.onRejectedWorkflow = onRejectedWorkflow;
    }

    public Boolean getDeleted() {
        return deleted != null ? deleted : Boolean.FALSE;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getIs_default() {
        return is_default != null && is_default;
    }

    public void setIs_default(Boolean is_default) {
        this.is_default = is_default;
    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        this.status = status;
        setHistoryForApproval();
    }

    public Boolean getBackup() {
        return this.backup;
    }

    public void setBackup(final Boolean backup) {
        this.backup = backup;
    }

    @Transactional
    public void setHistoryForApproval() {
        EdsApprovableHistory edsTimeAction = new EdsApprovableHistory();
        EdsUser user = (EdsUser) SecurityContext.getInstance().getUser();
        edsTimeAction.setRealApprover(user);
        edsTimeAction.setApproveDate(new Date());
        edsTimeAction.setStatus(this.status);
        edsTimeAction.setEntityType(this.getClass().getSimpleName());
        this.getApproverHistory().add(edsTimeAction);
    }

    public Set<EdsApprovableHistory> getApproverHistory() {
        if (approverHistory == null) {
            approverHistory = new HashSet<>();
        }
        return approverHistory;
    }

    public void setApproverHistory(Set<EdsApprovableHistory> approverHistory) {
        this.approverHistory = approverHistory;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getStepEmployeeType() {
        return stepEmployeeType;
    }

    public void setStepEmployeeType(String stepEmployeeType) {
        this.stepEmployeeType = stepEmployeeType;
    }

    public Integer getApproverOrder() {
        return approverOrder;
    }

    public void setApproverOrder(Integer approverOrder) {
        this.approverOrder = approverOrder;
    }

    public EdsUser getExactEmployee() {
        return exactEmployee;
    }

    public void setExactEmployee(EdsUser exactApprover) {
        this.exactEmployee = exactApprover;
    }

    public Integer getOnApprovedAction() {
        return onApprovedAction;
    }

    public void setOnApprovedAction(Integer onApprovedAction) {
        this.onApprovedAction = onApprovedAction;
    }

    public Integer getOnRejectedAction() {
        return onRejectedAction;
    }

    public void setOnRejectedAction(Integer onRejectedAction) {
        this.onRejectedAction = onRejectedAction;
    }

    public Set<EdsApproverRoles> getApproverRoles() {
        return approverRoles;
    }

    public void setApproverRoles(Set<EdsApproverRoles> approverRoles) {
        this.approverRoles = approverRoles;
    }

    public Set<EdsApproverEmployees> getApproverEmployees() {
        return approverEmployees;
    }

    public void setApproverEmployees(Set<EdsApproverEmployees> approverEmployees) {
        this.approverEmployees = approverEmployees;
    }

    public ApproverItem getRPC(ApproverItem ... items){
        ApproverItem item = items!= null && items.length > 0 && items[0] != null ? items[0] : new ApproverItem();

        if(getOnApprovedWorkflow() != null){
            item.setOnApproveWorkflowRule(getOnApprovedWorkflow().getRPC(null));
        }
        if(getOnRejectedWorkflow() != null){
            item.setOnRejectWorkflowRule(getOnRejectedWorkflow().getRPC(null));
        }
        if(getExactEmployee() != null){
            item.setExactEmployee(getExactEmployee().getAsSelectItem());
        }
        if (getApproverRoles() != null && getApproverRoles().size() > 0) {
            for (EdsApproverRoles role : getApproverRoles()) {
                item.getRoles().add(role.getAsSelectItem());
            }
        }
        if (getApproverEmployees() != null && getApproverEmployees().size() > 0) {
            for (EdsApproverEmployees employee : getApproverEmployees()) {
                item.getEmployees().add(employee.getAsSelectItem());
            }
        }
        if (getDynamicQueries() != null && getDynamicQueries().size() > 0) {
            for (EdsDynamicApprover query : getDynamicQueries()) {
                item.getDynamicQueries().add(query.getAsSelectItem());
            }
        }
        if (isOk(getStatus())) {
            item.setStatus(getStatus().getRPC());
        }
        item.setName(getName());
        item.setEntityID(getEntityID());
        item.setEntityType(getEntityType());
        item.setEntityType(getStepEmployeeType());
        item.setAppproveStatusId(getApproveStatusID());
        item.setRejectStatusId(getRejectStatusID());
        item.setStartStatusId(getStartStatusID());
        boolean isDefault = getIs_default() != null ? getIs_default() : false;
        item.setIs_default(isDefault);
        item.setApproverOrder(getApproverOrder());
        item.setObjectID(getObjectID());
        item.setOnApprovedAction(getOnApprovedAction());
        item.setOnRejectedAction(getOnRejectedAction());
        item.setLocation(isLocation());
        return item;
    }

    public ApproverItemMini getRPCMini(ApproverItemMini... items) {
        ApproverItemMini item = items!= null && items.length > 0 && items[0] != null ? items[0] : new ApproverItem();
        if(getExactEmployee() != null){
            item.setExactEmployee(getExactEmployee().getAsSelectItem());
        }
        if(isOk(getStatus())){
            item.setStatus(getStatus().getRPC());
        }
        item.setApproverOrder(getApproverOrder());
        item.setObjectID(getObjectID());
        return item;
    }

    public void setApproveStatusID(Integer approveStatusID) {
        this.approveStatusID = approveStatusID;
    }

    public Integer getApproveStatusID() {
        return approveStatusID;
    }

    public void setRejectStatusID(Integer rejectStatusID) {
        this.rejectStatusID = rejectStatusID;
    }

    public Integer getRejectStatusID() {
        return rejectStatusID;
    }

    public void setStartStatusID(Integer startStatusID) {
        this.startStatusID = startStatusID;
    }

    public Integer getStartStatusID() {
        return startStatusID;
    }

    public Set<EdsDynamicApprover> getDynamicQueries() {
        return dynamicQueries;
    }

    public void setDynamicQueries(Set<EdsDynamicApprover> dynamicQueries) {
        this.dynamicQueries = dynamicQueries;
    }

    public Boolean isLocation() {
        return isLocation;
    }

    public void setLocation(Boolean location) {
        isLocation = location;
    }
}
