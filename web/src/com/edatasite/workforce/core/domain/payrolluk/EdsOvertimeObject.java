package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.approving.EdsApprovable;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.customfields.EdsOvertimeCustomFields;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.payroll.client.rpc.OvertimeObject;
import com.edatasite.workforce.gwt.payroll.client.rpc.OvertimeObjectData;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "overtime_object")
public class EdsOvertimeObject extends EdsApprovable {
    public static final String APPROVED = "OVERTIME_APPROVED";
    public static final String REJECTED = "OVERTIME_REJECTED";
    public static final String OVERTIME_EMPLOYEE_TYPE = "employee";
    public static final String OVERTIME_DEPARTMENT_TYPE = "department";
    public static final String OVERTIME_GROUP_EMPLOYEE_TYPE = "group";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "overtimeCode")
    private String overtimeCode;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selectedDepartmentId")
    private EdsDepartment selectedDepartment;

    @Column(name = "date")
    private Date date;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "categoryId")
    private EdsPayrollCategory category;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "overtime_object_id")
    private Set<EdsOvertimeObjectData> lineItems;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "selectedEmployeeId")
    private EdsEmployee selectedEmployee;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "payrollBatchId")
    private EdsPayrollBatch payrollBatch;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "creatorId")
    private EdsUser creator;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "updaterId")
    private EdsUser updater;

    @Column(name = "createdDate")
    private Date createdDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customfieldsid")
    private EdsOvertimeCustomFields customFields;

    @Column(name = "updatedDate")
    private Date updatedDate;

    @Column(name = "deleted")
    private Boolean deleted;

    private Date approvedDate;

    @Column(name = "type")
    private String overtimeType;

    @Column(name = "defaultHours")
    private BigDecimal defaultHours;

    @Column(name = "intNumber")
    private Integer intNumber;

    @Column(name = "apply_for_sub_department")
    private Boolean applyForSubDepartment;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entityID", fetch = FetchType.LAZY)
    @Where(clause = "entityType = 'OVERTIME'")
    @OrderBy(value = "approverOrder ASC")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private List<EdsApprover> approvers = new ArrayList<>();

    public OvertimeObject toRpc(boolean withItems) {
        OvertimeObject object = new OvertimeObject();
        object.setId(getObjectID());
        object.setDate(new DateNonConvertable(getDate()));
        object.setApplyForSubDepartment(getApplyForSubDepartment());
        object.setCategory(getCategory() != null ? getCategory().getAsSelectItem() : null);
        if (getSelectedEmployee() != null) {
            object.setSelectedEmployee(getSelectedEmployee().getAsSelectItem());
        } else if (getSelectedDepartment() != null) {
            object.setSelectedDepartment(getSelectedDepartment().getAsSelectItem());
        }
        if (getPayrollBatch() != null) {
            object.setPayrollBatch(getPayrollBatch().getAsSelectItem());
        }
        if (withItems) {
            List<OvertimeObjectData> dataList = new ArrayList<>();
            for (EdsOvertimeObjectData data : getLineItems()) {
                dataList.add(data.toRpc(false));
            }
            object.setItems(dataList);
        }
        initApproverData(object);
        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
            object.setApproverEmployee(getCurrentApprover().getExactEmployee().getAsSelectItem());
        }
        if (getApprovedDate() != null) {
            object.setApprovedDate(new DateNonConvertable(getApprovedDate()));
        }
        object.setOvertimeType(getOvertimeType() != null ? getOvertimeType() : (getSelectedEmployee() != null ? OVERTIME_EMPLOYEE_TYPE : OVERTIME_DEPARTMENT_TYPE));
        object.setCreatedDate(getCreatedDate() != null ? new DateNonConvertable(getCreatedDate()) : null);
        object.setUpdatedDate(getUpdatedDate() != null ? new DateNonConvertable(getUpdatedDate()) : null);
        object.setCreator(getCreator() != null ? getCreator().getAsSelectItem() : null);
        object.setUpdater(getUpdater() != null ? getUpdater().getAsSelectItem() : null);
        EdsReference reference = getOverallStatus();
        object.setStatus(reference != null ? reference.getName() : "N/A");
        object.setStatusCode(reference != null ? reference.getCode() : "N/A");
        object.setDefaultHours(getDefaultHours());
        object.setIntNumber(getIntNumber());
        object.setCode(getOvertimeCode());
        return object;
    }


    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsDepartment getSelectedDepartment() {
        return selectedDepartment;
    }

    public void setSelectedDepartment(EdsDepartment selectedDepartment) {
        this.selectedDepartment = selectedDepartment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public EdsPayrollCategory getCategory() {
        return category;
    }

    public void setCategory(EdsPayrollCategory category) {
        this.category = category;
    }

    public Set<EdsOvertimeObjectData> getLineItems() {
        return lineItems;
    }

    public void setLineItems(Set<EdsOvertimeObjectData> lineItems) {
        this.lineItems = lineItems;
    }

    public EdsEmployee getSelectedEmployee() {
        return selectedEmployee;
    }

    public void setSelectedEmployee(EdsEmployee selectedEmployee) {
        this.selectedEmployee = selectedEmployee;
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

    public String getStatus() {
        if (getOverallStatus() != null) {
            return getOverallStatus().getCode();
        }
        return null;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public EdsOvertimeCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsOvertimeCustomFields customFields) {
        this.customFields = customFields;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getOvertimeType() {
        return overtimeType;
    }

    public void setOvertimeType(String overtimeType) {
        this.overtimeType = overtimeType;
    }

    public EdsPayrollBatch getPayrollBatch() {
        return payrollBatch;
    }

    public void setPayrollBatch(EdsPayrollBatch payrollBatch) {
        this.payrollBatch = payrollBatch;
    }

    public BigDecimal getDefaultHours() {
        return defaultHours;
    }

    public void setDefaultHours(BigDecimal defaultHour) {
        this.defaultHours = defaultHour;
    }

    public String getOvertimeCode() {
        return overtimeCode;
    }

    public void setOvertimeCode(String overtimeCode) {
        this.overtimeCode = overtimeCode;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public Boolean getApplyForSubDepartment() {
        return applyForSubDepartment;
    }

    public void setApplyForSubDepartment(Boolean applyForSubDepartment) {
        this.applyForSubDepartment = applyForSubDepartment;
    }
}
