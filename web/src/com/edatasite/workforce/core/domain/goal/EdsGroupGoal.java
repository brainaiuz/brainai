package com.edatasite.workforce.core.domain.goal;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsTask;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsValidityPeriod;
import com.edatasite.workforce.core.domain.approving.EdsApprovable;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.documents.EdsAuditInfo;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.hrms.client.rpc.GroupGoalITem;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "groupgoal")
public class EdsGroupGoal extends EdsApprovable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeId")
    private EdsUser employee;

    @Embedded
    private EdsAuditInfo auditInfo;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "groupID", fetch = FetchType.LAZY)
    @org.hibernate.annotations.ForeignKey(name = "none")
    private List<EdsGoal> goals = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entityID", fetch = FetchType.LAZY)
    @Where(clause = "entityType = 'GROUP_GOAL'")
    @OrderBy(value = "approverOrder ASC")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private List<EdsApprover> approvers = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id")
    private EdsValidityPeriod validityPeriod;

    @Column(name = "fromDate")
    private Date fromDate;

    @Column(name = "toDate")
    private Date toDate;



    @Column(name = "deleted", columnDefinition = "boolean default false")
    private Boolean deleted = false;


    @Override
    public List<EdsApprover> getApprovers() {
        return approvers;
    }

    @Override
    public void setApprovers(List<EdsApprover> approvers) {
        this.approvers = approvers;
    }

    @Override
    public void setEntityStatus(EdsReference overallStatus) {
        setOverallStatus(overallStatus);
    }

    @Override
    public boolean isCurrentApproverApproved() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && EdsTask.IN_PROGRESS.equals(getCurrentApprover().getStatus().getCode());
    }

    @Override
    public boolean isCurrentApproverRejected() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && EdsTask.NOT_STARTED.equals(getCurrentApprover().getStatus().getCode());
    }

    @Override
    protected EdsReference getStatusByMarkedAction(Integer actionId) {
        if (!isOk(actionId)) {
            return null;
        }
        ReferenceManager referenceManager = StaticContextAccessor.getBean(ReferenceManager.class);
        if (actionId.equals(ApproverItem.MARK_AS_REJECTED)) {
            return referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.NOT_STARTED);
        } else if (actionId.equals(ApproverItem.MARK_AS_APPROVED)) {
            return referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.IN_PROGRESS);
        } else if (actionId.equals(ApproverItem.SEND_TO_CREATOR)) {
            return referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.NOT_STARTED);
        } else if (actionId.equals(ApproverItem.SEND_TO_DIRECTORS)) {
            return referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.NOT_STARTED);
        }
        return null;
    }

    @Override
    public void updateRejectedStatus() {
        if (getOverallStatus() != null && EdsTask.NOT_STARTED.equals(getOverallStatus().getCode())) {
            ReferenceManager referenceManager = StaticContextAccessor.getBean(ReferenceManager.class);
            setEntityStatus(referenceManager.findReference(EdsTask.TASK_STATUS, EdsTask.NOT_STARTED));
        }
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsUser getEmployee() {
        return employee;
    }

    public void setEmployee(EdsUser employee) {
        this.employee = employee;
    }

    public List<EdsGoal> getGoals() {
        return goals;
    }

    public void setGoals(List<EdsGoal> goals) {
        this.goals = goals;
    }

    public EdsAuditInfo getAuditInfo() {
        if (auditInfo == null) {
            auditInfo = new EdsAuditInfo();
        }
        return auditInfo;
    }

    public void setAuditInfo(EdsAuditInfo auditInfo) {
        this.auditInfo = auditInfo;
    }

    public EdsValidityPeriod getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(EdsValidityPeriod validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }


    public GroupGoalITem getRPC() {
        GroupGoalITem item = new GroupGoalITem();
        item.setObjectId(getObjectID());
        if (getEmployee() != null) {
            item.setEmployee(new SelectItem(getEmployee().getObjectID(), getEmployee().getName()));
        }
        if (getValidityPeriod() != null) {
            EdsValidityPeriod validityPeriod = getValidityPeriod();
            item.setValidityPeriod(validityPeriod.getDTO());
        }
        item.setFromDate(new DateNonConvertable(getFromDate()));
        item.setToDate(new DateNonConvertable(getToDate()));

        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
            item.setApprover(new SelectItem(getCurrentApprover().getExactEmployee().getObjectID(), getCurrentApprover().getExactEmployee().getName()));
        }
        if (getOverallStatus() != null) {
            item.setStatus(new SelectItem(getOverallStatus().getObjectID(), getOverallStatus().getName()));
        }
        return item;
    }
}
