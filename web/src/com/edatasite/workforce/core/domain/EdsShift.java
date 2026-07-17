package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.approving.EdsApprovable;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.customfields.EdsShiftCustomFields;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.hrms.client.rpc.ShiftItem;
import org.hibernate.annotations.Where;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants.BRIGADA_ID;
import static com.edatasite.workforce.gwt.core.client.ui.lookup.LookUpConstants.EMPLOYEE_ID;
import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "shift")
public class EdsShift extends EdsApprovable {

    public static final String APPROVED = "SHIFT_APPROVED";
    public static final String REJECTED = "SHIFT_REJECTED";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statusId")
    private EdsReference status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departmentId")
    private EdsDepartment department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorId")
    private EdsUser creator;

    @Column(name = "date")
    private Date date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver")
    private EdsEmployee approver;

    @Column(name = "period")
    private Date period;

    @Column(name = "shift_name")
    private String shiftName;

    @Column(name = "indexed")
    private Boolean indexed;

    @Column(name = "startDate")
    private Date startDate;

    @Column(name = "endDate")
    private Date endDate;

    @Column(name = "submittedDate")
    private Date submittedDate;

    @Column(name = "approvalDate")
    private Date approvalDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updaterId")
    private EdsUser updater;

    @Column(name = "createdDate")
    private Date createdDate;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "updatedDate")
    private Date updatedDate;

    private Date approvedDate;

    private Integer intNumber;

    private String manager;

    private String backupManager;

    @Column(name = "shift_code")
    private String shiftCode;

    private Integer lookupType;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entityID", fetch = FetchType.LAZY)
    @Where(clause = "entityType = 'SHIFT'")
    @OrderBy(value = "approverOrder ASC")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private List<EdsApprover> approvers = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customfieldsid")
    private EdsShiftCustomFields customFields;

    @ManyToMany(fetch = FetchType.LAZY)
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "shift_owners",
            joinColumns = {@JoinColumn(name = "shift_id")},
            inverseJoinColumns = {@JoinColumn(name = "owner_id")})
    @Where(clause = "(deleted = 'false' or deleted is null)")
    private List<EdsUser> owners = new ArrayList<>();

    private String ownersId;

    @Column(name = "period_type")
    private String periodType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brigadaId")
    private EdsBrigada brigada;


    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getManager() {
        return this.manager;
    }

    public void setManager(final String manager) {
        this.manager = manager;
    }

    public String getBackupManager() {
        return this.backupManager;
    }

    public void setBackupManager(final String backupManager) {
        this.backupManager = backupManager;
    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        this.status = status;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }

    public Boolean getIndexed() {
        return indexed;
    }

    public void setIndexed(Boolean indexed) {
        this.indexed = indexed;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
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

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(Date submittedDate) {
        this.submittedDate = submittedDate;
    }

    public Date getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(Date approvalDate) {
        this.approvalDate = approvalDate;
    }

    public EdsEmployee getApprover() {
        return approver;
    }

    public void setApprover(EdsEmployee approver) {
        this.approver = approver;
    }

    public Date getPeriod() {
        return period;
    }

    public void setPeriod(Date period) {
        this.period = period;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public EdsShiftCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsShiftCustomFields customFields) {
        this.customFields = customFields;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public EdsDepartment getDepartment() {
        return department;
    }

    public void setDepartment(EdsDepartment department) {
        this.department = department;
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
        setOverallStatus(overallStatus);
    }

    @Override
    public boolean isCurrentApproverApproved() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && APPROVED.equals(getCurrentApprover().getStatus().getCode());

    }


    @Override
    public boolean isCurrentApproverRejected() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && REJECTED.equals(getCurrentApprover().getStatus().getCode());
    }

    @Override
    protected EdsReference getStatusByMarkedAction(Integer actionID) {
        if (!isOk(actionID)) {
            return null;
        }
        ReferenceManager referenceManager = StaticContextAccessor.getBean(ReferenceManager.class);
        if (actionID.equals(ApproverItem.MARK_AS_REJECTED)) {
            return referenceManager.getByCode(REJECTED);
        } else if (actionID.equals(ApproverItem.MARK_AS_APPROVED)) {
            return referenceManager.getByCode(APPROVED);
        } else if (actionID.equals(ApproverItem.SEND_TO_CREATOR)) {
            return referenceManager.getByCode(REJECTED);
        } else if (actionID.equals(ApproverItem.SEND_TO_DIRECTORS)) {
            return referenceManager.getByCode(REJECTED);
        }
        return null;
    }

    public ShiftItem toRpc() {
        ShiftItem shift = new ShiftItem();
        shift.setId(getObjectID());
        shift.setShiftName(getShiftName());
        shift.setPeriod(getPeriod());
        shift.setEndDate(getEndDate() != null ? new DateNonConvertable(getEndDate()) : null);
        shift.setDepartment(getDepartment() != null ? getDepartment().getAsSelectItem() : null);
        shift.setStatusName(getStatus() != null ? getStatus().getName() : null);
        shift.setCreator(getCreator() != null ? new SelectItem(getCreator().getObjectID(), getCreator().getName()) : null);
        shift.setUpdater(getUpdater() != null ? new SelectItem(getUpdater().getObjectID(), getUpdater().getName()) : null);
        shift.setCreatedDate(new DateNonConvertable(getCreatedDate()));
        shift.setUpdatedDate(new DateNonConvertable(getUpdatedDate()));
        shift.setSubmittedDate(new DateNonConvertable(getUpdatedDate()));
        shift.setApprovalDate(new DateNonConvertable(getApprovalDate()));
        initApproverData(shift);
        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
            shift.setApproverEmployee(getCurrentApprover().getExactEmployee().getAsSelectItem());
        }
        if (getApprovedDate() != null) {
            shift.setApprovedDate(new DateNonConvertable(getApprovedDate()));
        }
        EdsReference reference = getOverallStatus();
        shift.setStatus(reference != null ? reference.getName() : "N/A");
        shift.setStatusCode(reference != null ? reference.getCode() : "N/A");
        shift.setCode(getShiftCode() != null ? getShiftCode() : null);
        shift.setLookUpType(getLookupType() != null ? getLookupType() : null);
        shift.setPeriodType(getPeriodType());

        return shift;
    }

    public EdsUser getCreator() {
        return creator;
    }

    public void setCreator(EdsUser creator) {
        this.creator = creator;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Boolean getDeleted() {
        return deleted;
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

    public String getShiftCode() {
        return shiftCode;
    }

    public void setShiftCode(String shiftCode) {
        this.shiftCode = shiftCode;
    }

    public Integer getLookupType() {
        return lookupType;
    }

    public void setLookupType(Integer lookUpType) {
        this.lookupType = lookUpType;
    }

    public String getOwnersId() {
        return ownersId;
    }

    public void setOwnersId(String ownersId) {
        this.ownersId = ownersId;
    }

    public String getPeriodType() {
        return periodType;
    }

    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }

    public List<EdsUser> getOwners() {
        return owners;
    }

    public void setOwners(List<EdsUser> owners) {
        this.owners = owners;
    }

    public EdsBrigada getBrigada() {
        return brigada;
    }

    public void setBrigada(EdsBrigada brigada) {
        this.brigada = brigada;
    }

    @Override
    public Object getRealValue(String fieldId) {
        if (fieldId == null) {
            return null;
        } else if (fieldId.equals(CustomFormConstants.TYPE)) {
            return lookupType == null || lookupType.equals(BRIGADA_ID) ? "Shift" : lookupType.equals(EMPLOYEE_ID) ? "Duty" : "Overtime";
        } else if (fieldId.equals(CustomFormConstants.PROJECT.BACKUP_MANAGER)) {
            return backupManager;
        } else if (fieldId.equals(CustomFormConstants.MANAGER)) {
            return manager;
        } else if (fieldId.equals(CustomFormConstants.OWNER)) {
            return owners;
        }
        return super.getRealValue(fieldId);
    }
}
