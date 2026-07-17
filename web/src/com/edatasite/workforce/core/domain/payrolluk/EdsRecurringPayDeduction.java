package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.approving.EdsApprovable;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.ui.view.recurring.PayType;
import com.edatasite.workforce.gwt.core.client.ui.view.recurring.RecurringPayDeductItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import org.hibernate.annotations.Where;
import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "recurringPayDeduction")
public class EdsRecurringPayDeduction extends EdsApprovable {

    public static final String APPROVED = "APPROVED";
    public static final String REJECTED = "REJECTED";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String reference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private EdsEmployee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private EdsPayrollCategory category;

    @Column(name = "paymentAmount", precision = 14, scale = 4)
    private BigDecimal paymentAmount;

    @Column(name = "percentage", precision = 14, scale = 4)
    private BigDecimal percentage;

    @Column(name = "totalLimit", precision = 14, scale = 4)
    private BigDecimal totalLimit;

    @Column(name = "type")
    private Integer type;

    @Enumerated(EnumType.STRING)
    private PayType payType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private EdsCurrency currency;

    @Column(precision = 25, scale = 15)
    private BigDecimal exchangeRate;

    private Date creationDate;

    private Date lastUpdateTime;

    private Date fromDate;

    private Date toDate;

    private Date approvedDate;

    @Transient
    private BigDecimal remainingAmount;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paymentDeductionid")
    private EdsPaymentDeduction paymentDeduction;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entityID", fetch = FetchType.LAZY)
    @Where(clause = "entityType = 'CASH_ADVANCE' AND (deleted = 'false' or deleted is null) ")
    @OrderBy(value = "approverOrder ASC")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private List<EdsApprover> approvers = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinTable(schema = EdsScope.PRIVATE_SCHEMA,
            name = "RecurringPayDeductionCategories",
            joinColumns = {@JoinColumn(name = "rpdId")},
            inverseJoinColumns = {@JoinColumn(name = "categoryId")})
    @Where(clause = "(deleted = 'false' or deleted is null)")
    private List<EdsPayrollCategory> linkedCategories = new ArrayList<>();

    private Boolean fromAllAllowances;

    private Boolean deleted = false;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public RecurringPayDeductItem toRpc(boolean forList) {
        RecurringPayDeductItem item = new RecurringPayDeductItem();
        item.setObjectID(getObjectID());

        item.setEmployee(getEmployee().getAsSelectItem());
        if (getEmployee().getProfile() != null) {
            item.setEmployeeCode(getEmployee().getProfile().getEmployeeCode());
        }
        item.setEmployeeName(getEmployee().getFullName());
        item.setCategoryItem(getCategory().getAsSelectItem());

        if (getStatus() != null) {
            SelectItem status = new SelectItem();
            status.setId(getStatus().getObjectID());
            status.setName(getStatus().getName());
            status.setCode(getStatus().getCode());
            item.setStatus(status);
        }
        item.setPayType(getPayType());
        item.setType(getType());

        item.setPaymentAmount(getPaymentAmount());
        item.setPercentage(getPercentage());
        item.setTotalLimit(getTotalLimit());

        if (getFromDate() != null) {
            item.setFromDate(new DateNonConvertable(getFromDate()));
        }
        if (getToDate() != null) {
            item.setToDate(new DateNonConvertable(getToDate()));
        }

        item.setFromAllAllowances(isFromAllAllowances());
        if (!forList && !CollectionUtils.isEmpty(getLinkedCategories())) {
            getLinkedCategories().forEach(c -> item.getLinkedCategories().add(c.createPaymentDeductionSelectItem()));
        }

        return item;
    }

    public EdsReference getStatus() {
        return getOverallStatus();
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public EdsEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(EdsEmployee employee) {
        this.employee = employee;
    }

    public EdsPayrollCategory getCategory() {
        return category;
    }

    public void setCategory(EdsPayrollCategory category) {
        this.category = category;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public BigDecimal getTotalLimit() {
        return totalLimit;
    }

    public void setTotalLimit(BigDecimal totalLimit) {
        this.totalLimit = totalLimit;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public PayType getPayType() {
        return payType;
    }

    public void setPayType(PayType payType) {
        this.payType = payType;
    }

    public EdsCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(EdsCurrency currency) {
        this.currency = currency;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
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

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public BigDecimal getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public EdsPaymentDeduction getPaymentDeduction() {
        return paymentDeduction;
    }

    public void setPaymentDeduction(EdsPaymentDeduction paymentDeduction) {
        this.paymentDeduction = paymentDeduction;
    }

    @Override
    public List<EdsApprover> getApprovers() {
        return approvers;
    }

    @Override
    public void setApprovers(List<EdsApprover> approvers) {
        this.approvers = approvers;
    }

    public List<EdsPayrollCategory> getLinkedCategories() {
        return linkedCategories;
    }

    public void setLinkedCategories(List<EdsPayrollCategory> linkedCategories) {
        this.linkedCategories = linkedCategories;
    }

    public Boolean isFromAllAllowances() {
        return fromAllAllowances != null && fromAllAllowances;
    }

    public void setFromAllAllowances(Boolean fromAllAllowances) {
        this.fromAllAllowances = fromAllAllowances;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public void setEntityStatus(EdsReference status) {
        if (!ServerUtils.equalsReference(getOverallStatus(), status)) {
            addChange(CustomFormConstants.STATUS);
        }
        setOverallStatus(status);
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
}
