package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.approving.EdsApprovable;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.customfields.EdsBackupsEmployeeCustomFields;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.hrms.client.rpc.BackupsEmployeeObject;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Where;

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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "backups_employee")
public class EdsBackupsEmployee extends EdsApprovable {

    public static final String BACKUPS_EMPLOYEE_APPROVED = "BACKUPS_EMPLOYEE_APPROVED";
    public static final String BACKUPS_EMPLOYEE_REJECTED = "BACKUPS_EMPLOYEE_REJECTED";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "backup_employee_code")
    private String backupEmployeecode;

    private Integer intNumber;
    @Column(name = "customreason_id")
    private Integer customReasonId;

    @Column(name = "date")
    private Date date;


    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id")
    private EdsEmployee employee;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, mappedBy = "backupsEmployees")
    private List<EdsBackupEmployee> backupEmployees = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customfieldsid")
    private EdsBackupsEmployeeCustomFields customFields;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "creatorId")
    private EdsUser creator;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "updaterId")
    private EdsUser updater;

    @Column(name = "createdDate")
    private Date createdDate;

    @Column(name = "updatedDate")
    private Date updatedDate;

    @Column(name = "deleted")
    private Boolean deleted;

    private Date approvedDate;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entityID", fetch = FetchType.LAZY)
    @Where(clause = "entityType = 'BACKUPS_EMPLOYEE'")
    @OrderBy(value = "approverOrder ASC")
    @ForeignKey(name = "none")
    private List<EdsApprover> approvers = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "position_id")
    private EdsPosition position;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "deartment_id")
    private EdsDepartment department;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reason_Id")
    private EdsLeaveReason leaveReason;

    private String description;

    private String isNeedToSign;

    private String percentage;


    public BackupsEmployeeObject toRpc() {
        BackupsEmployeeObject object = new BackupsEmployeeObject();
        object.setId(getObjectID());
        object.setDate(new DateNonConvertable(getDate()));
        if (getEmployee() != null) {
            object.setSelectedEmployee(getEmployee().getAsSelectItem());
        }
        object.setDeleted(getDeleted());

        initApproverData(object);
        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
            object.setApproverEmployee(getCurrentApprover().getExactEmployee().getAsSelectItem());
        }
        if (getApprovedDate() != null) {
            object.setApprovedDate(new DateNonConvertable(getApprovedDate()));
        }
        if (getDepartment() != null) {
            object.setDepartment(getDepartment().getAsSelectItem());
        }
        if (getPosition() != null) {
            object.setPosition(getPosition().getAsSelectItem());
        }
        object.setCreatedDate(getCreatedDate() != null ? new DateNonConvertable(getCreatedDate()) : null);
        object.setUpdatedDate(getUpdatedDate() != null ? new DateNonConvertable(getUpdatedDate()) : null);
        object.setCreator(getCreator() != null ? getCreator().getAsSelectItem() : null);
        object.setUpdater(getUpdater() != null ? getUpdater().getAsSelectItem() : null);
        object.setCode(getBackupEmployeecode() != null ? getBackupEmployeecode() : "N/A");
        EdsReference reference = getOverallStatus();
        object.setStatus(reference != null ? reference.getName() : "N/A");
        object.setStatusCode(reference != null ? reference.getCode() : "N/A");

        object.setDescription(getDescription());
        object.setPercentage(getPercentage());
        object.setIsNeedSignature(getIsNeedToSign());
        object.setReasonsId(getLeaveReason() != null ? getLeaveReason().getObjectID() : null);
        object.setSelectedReason(getLeaveReason() != null ? getLeaveReason().getAsSelectItem() : null);
        object.setCustomReasonId(getCustomReasonId());
        return object;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public EdsEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(EdsEmployee employee) {
        this.employee = employee;
    }

    public EdsBackupsEmployeeCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsBackupsEmployeeCustomFields customFields) {
        this.customFields = customFields;
    }

    public EdsUser getCreator() {
        return creator;
    }

    public void setCreator(EdsUser creator) {
        this.creator = creator;
    }

    public EdsUser getUpdater() {
        return updater;
    }

    public void setUpdater(EdsUser updater) {
        this.updater = updater;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Boolean getDeleted() {
        return deleted != null && deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getBackupEmployeecode() {
        return backupEmployeecode;
    }

    public void setBackupEmployeecode(String backupEmployeecode) {
        this.backupEmployeecode = backupEmployeecode;
    }


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
        this.setOverallStatus(overallStatus);
    }

    @Override
    public boolean isCurrentApproverApproved() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && BACKUPS_EMPLOYEE_APPROVED.equals(getCurrentApprover().getStatus().getCode());
    }

    @Override
    public boolean isCurrentApproverRejected() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && BACKUPS_EMPLOYEE_REJECTED.equals(getCurrentApprover().getStatus().getCode());
    }

    @Override
    protected EdsReference getStatusByMarkedAction(Integer actionId) {
        if (!isOk(actionId)) {
            return null;
        }
        ReferenceManager referenceManager = StaticContextAccessor.getBean(ReferenceManager.class);
        if (actionId.equals(ApproverItem.MARK_AS_REJECTED)) {
            return referenceManager.getByCode(BACKUPS_EMPLOYEE_REJECTED);
        } else if (actionId.equals(ApproverItem.MARK_AS_APPROVED)) {
            return referenceManager.getByCode(BACKUPS_EMPLOYEE_APPROVED);
        } else if (actionId.equals(ApproverItem.SEND_TO_CREATOR)) {
            return referenceManager.getByCode(BACKUPS_EMPLOYEE_REJECTED);
        } else if (actionId.equals(ApproverItem.SEND_TO_DIRECTORS)) {
            return referenceManager.getByCode(BACKUPS_EMPLOYEE_REJECTED);
        }
        return null;
    }

    public String getStatus() {
        if (getOverallStatus() != null) {
            return getOverallStatus().getCode();
        }
        return null;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public Integer getCustomReasonId() {
        return customReasonId;
    }

    public void setCustomReasonId(Integer customReasonId) {
        this.customReasonId = customReasonId;
    }

    public List<EdsBackupEmployee> getBackupEmployees() {
        return backupEmployees;
    }

    public void setBackupEmployees(List<EdsBackupEmployee> backupEmployees) {
        this.backupEmployees = backupEmployees;
    }

    public EdsPosition getPosition() {
        return position;
    }

    public void setPosition(EdsPosition position) {
        this.position = position;
    }

    public EdsDepartment getDepartment() {
        return department;
    }

    public void setDepartment(EdsDepartment department) {
        this.department = department;
    }


    public EdsLeaveReason getLeaveReason() {
        return leaveReason;
    }

    public void setLeaveReason(EdsLeaveReason leaveReason) {
        this.leaveReason = leaveReason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsNeedToSign() {
        return isNeedToSign;
    }

    public void setIsNeedToSign(String isNeedToSign) {
        this.isNeedToSign = isNeedToSign;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
}