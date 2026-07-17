package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsPaymentMethod;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.approving.EdsApprovable;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "multiCashAdvance")
public class EdsMultiCashAdvance extends EdsApprovable {


    public static final String APPROVED = "APPROVED";
    public static final String REJECTED = "REJECTED";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private EdsPayrollCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private EdsEmployee creator;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payroll_batch_id")
    private EdsPayrollBatch payrollBatch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeid")
    private EdsEmployee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departmentid")
    private EdsDepartment department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locationid")
    private EdsLocation location;

    private Date creationDate;

    private Date approvedDate;

    private Date requestDate;

    private Date lastUpdateTime;

    private Boolean deleted = false;

    private String number;
    private Integer intNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paymentMethodId")
    private EdsPaymentMethod paymentMethod;

    @Column(name = "totalAmount", precision = 14, scale = 4)
    private BigDecimal totalAmount;

    @Column(precision = 14, scale = 4)
    private BigDecimal totalInBase;

    @Column(name = "type")
    private String type;

    @Column(name = "amountType")
    private String amountType;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entityID", fetch = FetchType.LAZY)
    @Where(clause = "entityType = 'CASH_ADVANCE' AND (deleted = 'false' or deleted is null) ")
    @OrderBy(value = "approverOrder ASC")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private List<EdsApprover> approvers = new ArrayList<>();


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsPayrollCategory getCategory() {
        return category;
    }

    public void setCategory(EdsPayrollCategory category) {
        if (!ServerUtils.equalsEdsObject(this.category, category)) {
            addChange(CustomFormConstants.PAYROLL.CATEGORY);
        }
        this.category = category;
    }

    public EdsReference getStatus() {
        return getOverallStatus();
    }

    @Override
    public void setEntityStatus(EdsReference status) {
        if (!ServerUtils.equalsReference(getOverallStatus(), status)) {
            addChange(CustomFormConstants.STATUS);
        }
        setOverallStatus(status);
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public Boolean getDeleted() {
        return deleted == null ? Boolean.FALSE : deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        if (!ServerUtils.equalsBigDecimal(this.totalAmount, totalAmount)) {
            addChange(CustomFormConstants.PAYROLL.REQUESTED_AMOUNT);
        }
        this.totalAmount = totalAmount;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public EdsPaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(EdsPaymentMethod paymentMethod) {
        if (!ServerUtils.equalsEdsObject(this.paymentMethod, paymentMethod)) {
            addChange(CustomFormConstants.PAYROLL.PAYMENT_METHOD);
        }
        this.paymentMethod = paymentMethod;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        if (!ServerUtils.equalsDate(this.requestDate, requestDate)) {
            addChange(CustomFormConstants.PAYROLL.DATE);
        }
        this.requestDate = requestDate;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmountType() {
        return this.amountType;
    }

    public void setAmountType(final String amountType) {
        this.amountType = amountType;
    }

    @Override
    public List<EdsApprover> getApprovers() {
        return approvers;
    }

    @Override
    public void setApprovers(List<EdsApprover> approvers) {
        this.approvers = approvers;
    }

    public BigDecimal getTotalInBase() {
        return totalInBase;
    }

    public void setTotalInBase(BigDecimal totalInBase) {
        this.totalInBase = totalInBase;
    }

    public EdsEmployee getCreator() {
        return this.creator;
    }

    public void setCreator(final EdsEmployee creator) {
        this.creator = creator;
    }

    public EdsPayrollBatch getPayrollBatch() {
        return this.payrollBatch;
    }

    public void setPayrollBatch(final EdsPayrollBatch payrollBatch) {
        this.payrollBatch = payrollBatch;
    }

    public EdsEmployee getEmployee() {
        return this.employee;
    }

    public void setEmployee(final EdsEmployee employee) {
        this.employee = employee;
    }

    public EdsDepartment getDepartment() {
        return this.department;
    }

    public void setDepartment(final EdsDepartment department) {
        this.department = department;
    }

    public EdsLocation getLocation() {
        return this.location;
    }

    public void setLocation(final EdsLocation location) {
        this.location = location;
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

//    public CashAdvanceItem getRPC() {
//        CashAdvanceItem item = new CashAdvanceItem();
//        item.setObjectID(getObjectID());
//        initApproverData(item);
//        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
//            if (getCurrentApprover().getExactEmployee().isEmployee()) {
//                EdsEmployee edsEmployee = getCurrentApprover().getExactEmployee().getEmployee();
//                if (edsEmployee != null && edsEmployee.getProfile() != null && edsEmployee.getProfile().getEmployeeCode() != null) {
//                    item.setApprover(new SelectItem(edsEmployee.getObjectID(), edsEmployee.getProfile().getEmployeeCode() + " - " + edsEmployee.getFullName()));
//                } else {
//                    item.setApprover(getCurrentApprover().getExactEmployee().getAsSelectItem());
//                }
//            } else {
//                item.setApprover(getCurrentApprover().getExactEmployee().getAsSelectItem());
//            }
//        }
//
//        if (getRequestDate() != null) {
//            item.setDate(new DateNonConvertable(getRequestDate()));
//        }
//        if (getApprovedDate() != null) {
//            item.setApprovedDate(new DateNonConvertable(getApprovedDate()));
//        }
//        if (getCategory() != null) {
//            item.setCategoryItem(getCategory().createPaymentDeductionSelectItem());
//        }
//        item.setPercent(getPercent());
//        item.setPaymentAmount(getPaymentAmount());
//        item.setTotalAmount(getTotalAmount());
//        item.setTotalInBaseAmount(getTotalInBase());
//        item.setType(getType());
//        if (getPaymentMethod() != null) {
//            item.setPaymentMethod(getPaymentMethod().getAsSelectItem());
//        }
//        item.setCurrency(getCurrency() != null ? getCurrency().createCurrencyItem() : null);
//        item.setExchangeRate(getExchangeRate());
//        item.setNumber(getNumber());
//        if (getStatus() != null) {
//            SelectItem status = new SelectItem();
//            status.setId(getStatus().getObjectID());
//            status.setName(getStatus().getName());
//            status.setCode(getStatus().getCode());
//            item.setStatus(status);
//        }
//        if (getOverallStatus() != null) {
//            item.setOverallStatus(getOverallStatus().getRPC());
//        }
//        return item;
//    }

//    public SolrInputDocument indexToSolr(Integer companyID) {
//        SolrInputDocument doc = new SolrInputDocument();
//        String compositID = companyID + "_" + getObjectID();
//
//        doc.addField(SolrCashAdvanceRepresenter.FIELD_COMPANY_ID, companyID);
//        doc.addField(SolrCashAdvanceRepresenter.FIELD_COMPOSITE_ID, compositID);
//        doc.addField(SolrCashAdvanceRepresenter.FIELD_CASH_ADVANCE_ID, getObjectID());
////        if (getEmployee() != null && getEmployee().getProfile() != null) {
////            doc.addField(SolrCashAdvanceRepresenter.FIELD_EMPLOYEE_ID, getEmployee().getObjectID());
////            doc.addField(SolrCashAdvanceRepresenter.FIELD_EMPLOYEE_NAME, getEmployee().getFullName());
////            doc.addField(SolrCashAdvanceRepresenter.FIELD_EMPLOYEE_CODE, getEmployee().getProfile().getEmployeeCode());
////            doc.addField(SolrCashAdvanceRepresenter.FIELD_EMPLOYEE_ID_NAME, getEmployee().getObjectID() + SolrCashAdvanceRepresenter.SPLIT + getEmployee().getFullName());
////            if (getEmployee().getDriverNumber() != null) {
////                doc.addField(SolrCashAdvanceRepresenter.FIELD_DRIVER_ID, getEmployee().getDriverNumber().toString());
////            }
////        }
////        if (getEmployee().getPayrollBatches() != null) {
////            for (EdsPayrollBatch batch : getEmployee().getPayrollBatches()) {
////                doc.addField(SolrCashAdvanceRepresenter.FIELD_PAYROLL_BATCH_ID, batch.getObjectID());
////            }
////        }
//        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
//            doc.addField(SolrCashAdvanceRepresenter.FIELD_APPROVER_ID, getCurrentApprover().getExactEmployee().getObjectID());
//            doc.addField(SolrCashAdvanceRepresenter.FIELD_APPROVER_NAME, getCurrentApprover().getExactEmployee().getFullName());
//            doc.addField(SolrCashAdvanceRepresenter.FIELD_APPROVER_ID_NAME, getCurrentApprover().getExactEmployee().getObjectID() + SolrCashAdvanceRepresenter.SPLIT + getCurrentApprover().getExactEmployee().getFullName());
//        }
//        doc.addField(SolrCashAdvanceRepresenter.FIELD_REQUEST_DATE, getRequestDate());
//        if (getApprovedDate() != null) {
//            doc.addField(SolrCashAdvanceRepresenter.FIELD_APPROVED_DATE, getApprovedDate());
//        }
//        doc.addField(SolrCashAdvanceRepresenter.FIELD_PERCENT, getPercent());
//        doc.addField(SolrCashAdvanceRepresenter.FIELD_PAYMENT_AMOUNT, getPaymentAmount() != null ? getPaymentAmount().doubleValue() : 0d);
//        doc.addField(SolrCashAdvanceRepresenter.FIELD_TOTAL_AMOUNT, getTotalAmount() != null ? getTotalAmount().doubleValue() : 0d);
//        doc.addField(SolrCashAdvanceRepresenter.FIELD_TYPE, getType());
//        if (getPaymentMethod() != null) {
//            doc.addField(SolrCashAdvanceRepresenter.FIELD_PAYMENT_METHOD_ID, getPaymentMethod().getObjectID());
//            doc.addField(SolrCashAdvanceRepresenter.FIELD_PAYMENT_METHOD_NAME, getPaymentMethod().getName());
//            doc.addField(SolrCashAdvanceRepresenter.FIELD_PAYMENT_METHOD_CODE, getPaymentMethod().getCode());
//        }
//        if (getStatus() != null) {
//            doc.addField(SolrCashAdvanceRepresenter.FIELD_STATUS_ID, getStatus().getObjectID());
//            doc.addField(SolrCashAdvanceRepresenter.FIELD_STATUS_NAME, getStatus().getName());
//            doc.addField(SolrCashAdvanceRepresenter.FIELD_STATUS_CODE, getStatus().getCode());
//            doc.addField(SolrCashAdvanceRepresenter.FIELD_STATUS_ID_NAME, getStatus().getObjectID() + SolrCashAdvanceRepresenter.SPLIT + getStatus().getName());
//        }
//        doc.addField(SolrCashAdvanceRepresenter.FIELD_NUMBER, getNumber());
//        doc.addField(SolrCashAdvanceRepresenter.FIELD_LAST_UPDATE, getLastUpdateTime());
//        doc.addField(SolrCashAdvanceRepresenter.FIELD_REMAINING_AMOUNT, getRemainingAmount() != null ? getRemainingAmount().doubleValue() : 0d);
//        if (getCurrency() != null) {
//            doc.addField(SolrCashAdvanceRepresenter.FIELD_CURRENCY_ID, getCurrency().getObjectID());
//            doc.addField(SolrCashAdvanceRepresenter.FIELD_CURRENCY_NAME, getCurrency().getName());
//        }
//        List<EdsApprover> approvers = getApprovers();
//        approvers.sort(Comparator.comparing(EdsApprover::getApproverOrder));
//
//        for (EdsApprover edsApprover : approvers) {
//            doc.addField(SolrCashAdvanceRepresenter.DYNAMIC_FIELD_APPROVER_ID + edsApprover.getApproverOrder(), edsApprover.getObjectID());
//            doc.addField(SolrCashAdvanceRepresenter.DYNAMIC_FIELD_APPROVER_NAME + edsApprover.getApproverOrder(), edsApprover.getName());
//            doc.addField(SolrCashAdvanceRepresenter.DYNAMIC_FIELD_APPROVER_ID_NAME + edsApprover.getApproverOrder(), edsApprover.getObjectID() + SolrCashAdvanceRepresenter.SPLIT + edsApprover.getName());
//            doc.addField(SolrCashAdvanceRepresenter.DYNAMIC_FIELD_APPROVER_STATUS_ID + edsApprover.getApproverOrder(), edsApprover.getStatus() != null ? edsApprover.getStatus().getObjectID() : null);
//            doc.addField(SolrCashAdvanceRepresenter.DYNAMIC_FIELD_APPROVER_STATUS_CODE + edsApprover.getApproverOrder(), edsApprover.getStatus() != null ? edsApprover.getStatus().getCode() : "");
//            doc.addField(SolrCashAdvanceRepresenter.DYNAMIC_FIELD_APPROVER_EXACT_EMPLOYEE_ID + edsApprover.getApproverOrder(), edsApprover.getExactEmployee() != null ? edsApprover.getExactEmployee().getObjectID() : null);
//            doc.addField(SolrCashAdvanceRepresenter.DYNAMIC_FIELD_APPROVER_EXACT_EMPLOYEE_NAME + edsApprover.getApproverOrder(), edsApprover.getExactEmployee() != null ? edsApprover.getExactEmployee().getName() : "");
//        }
//        if (getPrevApprover() != null) {
//            doc.addField(SolrCashAdvanceRepresenter.DYNAMIC_FIELD_PREVIOUS_APPROVER_ID, getPrevApprover().getObjectID());
//            doc.addField(SolrCashAdvanceRepresenter.DYNAMIC_FIELD_PREVIOUS_APPROVER_NAME, getPrevApprover().getName());
//            doc.addField(SolrCashAdvanceRepresenter.DYNAMIC_FIELD_PREVIOUS_APPROVER_ID_NAME, getPrevApprover().getObjectID() + SolrCashAdvanceRepresenter.SPLIT + getPrevApprover().getName());
//            doc.addField(SolrCashAdvanceRepresenter.DYNAMIC_FIELD_PREVIOUS_APPROVER_STATUS_ID, getPrevApprover().getStatus() != null ? getPrevApprover().getStatus().getObjectID() : null);
//            doc.addField(SolrCashAdvanceRepresenter.DYNAMIC_FIELD_PREVIOUS_APPROVER_STATUS_CODE, getPrevApprover().getStatus() != null ? getPrevApprover().getStatus().getCode() : "");
//            doc.addField(SolrCashAdvanceRepresenter.DYNAMIC_FIELD_PREVIOUS_APPROVER_EXACT_EMPLOYEE_ID, getPrevApprover().getExactEmployee() != null ? getCurrentApprover().getExactEmployee().getObjectID() : null);
//            doc.addField(SolrCashAdvanceRepresenter.DYNAMIC_FIELD_PREVIOUS_APPROVER_EXACT_EMPLOYEE_NAME, getPrevApprover().getExactEmployee() != null ? getCurrentApprover().getExactEmployee().getName() : "");
//        }
//        if (getCurrentApprover() != null) {
//            doc.addField(SolrCashAdvanceRepresenter.DYNAMIC_FIELD_CURRENT_APPROVER_ID, getCurrentApprover().getObjectID());
//            doc.addField(SolrCashAdvanceRepresenter.DYNAMIC_FIELD_CURRENT_APPROVER_NAME, getCurrentApprover().getName());
//            doc.addField(SolrCashAdvanceRepresenter.DYNAMIC_FIELD_CURRENT_APPROVER_ID_NAME, getCurrentApprover().getObjectID() + SolrCashAdvanceRepresenter.SPLIT + getCurrentApprover().getName());
//            doc.addField(SolrCashAdvanceRepresenter.DYNAMIC_FIELD_CURRENT_APPROVER_STATUS_ID, getCurrentApprover().getStatus() != null ? getCurrentApprover().getStatus().getObjectID() : null);
//            doc.addField(SolrCashAdvanceRepresenter.DYNAMIC_FIELD_CURRENT_APPROVER_STATUS_CODE, getCurrentApprover().getStatus() != null ? getCurrentApprover().getStatus().getCode() : "");
//            doc.addField(SolrCashAdvanceRepresenter.DYNAMIC_FIELD_CURRENT_APPROVER_EXACT_EMPLOYEE_ID, getCurrentApprover().getExactEmployee() != null ? getCurrentApprover().getExactEmployee().getObjectID() : null);
//            doc.addField(SolrCashAdvanceRepresenter.DYNAMIC_FIELD_CURRENT_APPROVER_EXACT_EMPLOYEE_NAME, getCurrentApprover().getExactEmployee() != null ? getCurrentApprover().getExactEmployee().getName() : "");
//        }
//        if (getOverallStatus() != null) {
//            doc.addField(SolrCashAdvanceRepresenter.DYNAMIC_FIELD_OVERALL_STATUS_ID, getOverallStatus().getObjectID());
//            doc.addField(SolrCashAdvanceRepresenter.DYNAMIC_FIELD_OVERALL_STATUS_NAME, getOverallStatus().getName());
//            doc.addField(SolrCashAdvanceRepresenter.DYNAMIC_FIELD_OVERALL_STATUS_CODE, getOverallStatus().getCode());
//        }
//        return doc;
//    }

    @Override
    protected String getStringValueByFieldID(String realFieldID) {
        return super.getStringValueByFieldID(realFieldID);
    }

    @Override
    public Object getRealValue(String fieldID) {
        if (fieldID == null) {
            return null;
        } else if (fieldID.equals(CustomFormConstants.PAYROLL.DATE)) {
            return getRequestDate();
        } else if (fieldID.equals(CustomFormConstants.PAYROLL.STATUS)) {
            return getStatus();
        } else if (fieldID.equals(CustomFormConstants.PAYROLL.CATEGORY)) {
            return getCategory();
        } else if (fieldID.equals(CustomFormConstants.PAYROLL.REQUESTED_AMOUNT)) {
            return getTotalAmount();
        } else if (fieldID.equals(CustomFormConstants.PAYROLL.PAYMENT_METHOD)) {
            return getPaymentMethod();
        }/* else if (fieldID.equals(CustomFormConstants.PAYROLL.REQUESTER)) {
            return getEmployee();
        }*/
        return super.getRealValue(fieldID);
    }

    @Override
    public void jumpToPreviousApprover() {
        EdsApprover prevPrevApprover = null;
        EdsApprover prevApprover = null;
        for (EdsApprover approver : getApprovers()) {
            if (isOk(prevPrevApprover)) {
                prevApprover = approver;
            } else {
                prevPrevApprover = approver;
            }
            if (getCurrentApprover().getObjectID().equals(approver.getObjectID())) {
                int currentIndex = getApprovers().indexOf(prevApprover);
                if (currentIndex > 0) {
                    EdsApprover prev = getApprovers().get(currentIndex - 1);
                    if (prev != null) {
                        setCurrentApprover(prev);
                    }
                } else {
                    setCurrentApprover(prevApprover);
                }
                if (currentIndex >= 2) {
                    EdsApprover prevPrev = getApprovers().get(currentIndex - 2);
                    if (prevPrev != null) {
                        setPrevApprover(prevPrev);
                    }
                } else {
                    setPrevApprover(null);
                }
                break;
            }
        }
    }
}
