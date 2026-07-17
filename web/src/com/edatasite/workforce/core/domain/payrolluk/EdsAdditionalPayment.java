package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.approving.EdsApprovable;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.customfields.EdsAdditionalPaymentCustomFields;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrAdditionalPaymentPresenter;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.payroll.client.rpc.AdditionalPayment;
import org.apache.solr.common.SolrInputDocument;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Type;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

/**
 * Created by Shohruh on 27 Oct 2016.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "additionalPayment")
public class EdsAdditionalPayment extends EdsApprovable {

    public static final String APPROVED = "PAYMENT_APPROVED";
    public static final String REJECTED = "PAYMENT_REJECTED";

    public static final String ADDITIONAL_PAYMENT_TYPE = "ADDITIONAL_PAYMENT";
    public static final String BY_COMMISION_TYPE = "BY_COMMISION";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "reference", columnDefinition = "TEXT")
    private String reference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payroll_batch_id")
    private EdsPayrollBatch payrollBatch;

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "addpayment_id")
    @Where(clause = "deleted = 'false' or deleted is null")
    private List<EdsPaymentDeduction> items = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entityID", fetch = FetchType.LAZY)
    @Where(clause = "entityType = 'ADDITIONAL_PAYMENT'")
    @OrderBy(value = "approverOrder ASC")
    @ForeignKey(name = "none")
    private List<EdsApprover> approvers = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private EdsEmployee creator;

    @Column(name = "employeeIds")
    @Type(type = "text")
    private String employeeIds;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departmentid")
    private EdsDepartment department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locationid")
    private EdsLocation location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supervisorid")
    private EdsEmployee supervisor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    @ForeignKey(name = "none")
    private EdsCurrency currency;

    @Column(name = "defaultpaymentDate")
    private Long defaultDate;

    @Column(name = "paymentType")
    private String paymentType;

    @Column(name = "fixed_amount")
    private BigDecimal fixedAmount;

    @Column(name = "percentage")
    private BigDecimal percentage;

    @Column(name = "defaultPayrollCategoryId")
    private Integer defaultPayrollCategoryId;

    @Column(name = "allowanceCategoriyIds")
    private String allowanceCategoriyIds;

    @Column(name = "isBasicAllowance")
    private Boolean isBasicAndAllowance = false;
    private BigDecimal total;

    private String type;

    private Boolean showInPaySlip = false;
    private Boolean deleted = false;

    private Date creationDate;
    private Date approvedDate;
    private Integer monthID;
    private Integer pdfTemplateID;
    private Integer year;
    private String month;

    private Date lastUpdateTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updater")
    private EdsUser updater;

    @Column(columnDefinition = "varchar(20) default 'Payment'")
    private String categoryType;

    @OneToMany(mappedBy = "additionalPayment", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<EdsAdditionalPaymentItemTable> itemTables = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "additionalPayment")
    @Where(clause = "(deleted = 'false' or deleted is null)")
    private Set<EdsPayrollPayment> payments = new HashSet<>();

    private Integer leaveRequestId;

    @Column(name = "backupsEmployeeId")
    private Integer backupsEmployeeId;

    @Column(name = "calculationDetails")
    @Type(type = "text")
    private String calculationDetails;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private EdsAdditionalPaymentCustomFields customFields;

    @Column(name = "from_id")
    private Integer fromId;

    @Column(name = "from_type")
    private String fromType;
    private String entityType;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public Set<EdsAdditionalPaymentItemTable> getItemTables() {
        return itemTables;
    }

    public void setItemTables(Set<EdsAdditionalPaymentItemTable> itemTables) {
        this.itemTables = itemTables;
    }

    public EdsAdditionalPaymentCustomFields getCustomFields() {
        return this.customFields;
    }

    public void setCustomFields(final EdsAdditionalPaymentCustomFields customFields) {
        this.customFields = customFields;
    }

    public AdditionalPayment getRPC() {
        return getRPC(true);
    }

    public AdditionalPayment getRPC(boolean withItems) {
        AdditionalPayment item = new AdditionalPayment();
        item.setObjectID(getObjectID());
        item.setReference(getReference());
        if (getEmployeeIds() != null) {
            item.setEmployeeIds(getEmployeeIds());
        }
        if (getDepartment() != null) {
            item.setDepartment(getDepartment().getAsSelectItem());
        }
        if (getLocation() != null) {
            item.setLocation(getLocation().getAsSelectItem());
        }
        if (getSupervisor() != null) {
            item.setSupervisor(getSupervisor().getAsSelectItem());
        }
        initApproverData(item);
        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
            item.setApprover(getCurrentApprover().getExactEmployee().getAsSelectItem());
        }
        item.setDate(new DateNonConvertable(getCreationDate()));
        if (getApprovedDate() != null) {
            item.setApprovedDate(new DateNonConvertable(getApprovedDate()));
        }
        if (getPayrollBatch() != null) {
            item.setPayrollBatch(getPayrollBatch().asSelectItem());
        } else {
            item.setPayrollBatch(new SelectItem(0, "All Employees"));
        }
        if (withItems && getItems() != null) {
            List<PaymentDeductionObject> items = new ArrayList<>();
            for (EdsPaymentDeduction pd : getItems()) {
                items.add(pd.getRPC());
            }
            item.setItems(items);
        }
        if (getCurrency() != null) {
            item.setCurrency(getCurrency().createCurrencyItem());
        }
        if (getOverallStatus() != null) {
            item.setStatus(getOverallStatus().getName());
            item.setStatusCode(getOverallStatus().getCode());
        }
        if (getBasicAndAllowance() != null) {
            item.setBasicPlusAllowance(getBasicAndAllowance());
        }
        item.setType(getType());
        item.setEntityType(getEntityType());
        item.setShowInPayslip(getShowInPaySlip());
        item.setMonthID(getMonthID());
        item.setYear(getYear());
        item.setMonth(getMonth());
        item.setTotal(getTotal());
        item.setDefaultDate(getDefaultDate() != null ? new DateNonConvertable(new Date(getDefaultDate())) : null);
        item.setPaymentType(getPaymentType());
        item.setFixedAmount(getFixedAmount());
        item.setCategoryType(getCategoryType());
        item.setPercentage(getPercentage());
        item.setLeaveRequestId(getLeaveRequestId());
        item.setBackupsEmployeeId(getBackupsEmployeeId());
        item.setDefaultPayrollCategoryId(getDefaultPayrollCategoryId());
        item.setDeleted(getDeleted());
        if (getDepartment() != null) {
            SelectItem departmentItem = new SelectItem();
            departmentItem.setId(getDepartment().getObjectID());
            departmentItem.setName(getDepartment().getName());
            item.setDepartment(departmentItem);
        }
        if (getLocation() != null) {
            SelectItem locationItem = new SelectItem();
            locationItem.setId(getLocation().getObjectID());
            locationItem.setName(getLocation().getName());
            item.setLocation(locationItem);
        }
        if (getSupervisor() != null) {
            SelectItem supervisorItem = new SelectItem();
            supervisorItem.setId(getSupervisor().getObjectID());
            supervisorItem.setName(getSupervisor().getFullName());
            item.setSupervisor(supervisorItem);
        }
        if (getCreator() != null) {
            SelectItem creator = new SelectItem();
            creator.setId(getCreator().getObjectID());
            creator.setName(getCreator().getFullName());
            item.setCreator(creator);
        }
        return item;
    }

    public SolrInputDocument indexToSolr(Integer companyID) {
        SolrInputDocument doc = new SolrInputDocument();
        String compositID = companyID + "_" + getObjectID();

        doc.addField(SolrAdditionalPaymentPresenter.FIELD_COMPANY_ID, companyID);
        doc.addField(SolrAdditionalPaymentPresenter.FIELD_COMPOSITE_ID, compositID);
        doc.addField(SolrAdditionalPaymentPresenter.FIELD_ADDITIONAL_PAYMENT_ID, getObjectID());
        doc.addField(SolrAdditionalPaymentPresenter.FIELD_REFERENCE, getReference());
        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
            doc.addField(SolrAdditionalPaymentPresenter.FIELD_APPROVER_ID, getCurrentApprover().getExactEmployee().getObjectID());
            doc.addField(SolrAdditionalPaymentPresenter.FIELD_APPROVER_NAME, getCurrentApprover().getExactEmployee().getFullName());
            doc.addField(SolrAdditionalPaymentPresenter.FIELD_APPROVER_ID_NAME, getCurrentApprover().getExactEmployee().getObjectID() + SolrAdditionalPaymentPresenter.SPLIT + getCurrentApprover().getExactEmployee().getFullName());
        }
        if (getCreator() != null) {
            doc.addField(SolrAdditionalPaymentPresenter.FIELD_CREATOR_ID, getCreator().getObjectID());
            doc.addField(SolrAdditionalPaymentPresenter.FIELD_CREATOR_NAME, getCreator().getName());
            doc.addField(SolrAdditionalPaymentPresenter.FIELD_CREATOR_ID_NAME, getCreator().getObjectID() + SolrAdditionalPaymentPresenter.SPLIT + getCreator().getName());
        }
        if (getUpdater() != null) {
            doc.addField(SolrAdditionalPaymentPresenter.FIELD_UPDATER_ID, getUpdater().getObjectID());
            doc.addField(SolrAdditionalPaymentPresenter.FIELD_UPDATER_NAME, getUpdater().getName());
            doc.addField(SolrAdditionalPaymentPresenter.FIELD_UPDATER_ID_NAME, getUpdater().getObjectID() + SolrAdditionalPaymentPresenter.SPLIT + getUpdater().getName());
        }
        if (getPayrollBatch() != null) {
            doc.addField(SolrAdditionalPaymentPresenter.FIELD_PAYROLL_BATCH_ID, getPayrollBatch().getObjectID());
            doc.addField(SolrAdditionalPaymentPresenter.FIELD_PAYROLL_BATCH_NAME, getPayrollBatch().getName());
        }
        if (getDepartment() != null) {
            doc.addField(SolrAdditionalPaymentPresenter.FIELD_PAYROLL_DEPARTMENT_ID, getDepartment().getObjectID());
            doc.addField(SolrAdditionalPaymentPresenter.FIELD_PAYROLL_DEPARTMENT_NAME, getDepartment().getName());
        }
        String categoryLookUpName = "";
        if (getPayrollBatch() != null) {
            categoryLookUpName = getPayrollBatch().getName();
        } else if (getEmployeeIds() != null) {
            categoryLookUpName = "Multi Employee";
        } else if (getDepartment() != null) {
            categoryLookUpName = getDepartment().getName();
        } else if (getLocation() != null) {
            categoryLookUpName = getLocation().getName();
        } else if (getSupervisor() != null) {
            categoryLookUpName = getSupervisor().getFullName();
        }

        if (categoryLookUpName.isEmpty()) {
            categoryLookUpName = "All Employees";
        }

        doc.addField(SolrAdditionalPaymentPresenter.FIELD_CATEGORY_LOOKUP_ID, categoryLookUpName.hashCode());
        doc.addField(SolrAdditionalPaymentPresenter.FIELD_CATEGORY_LOOKUP_NAME, categoryLookUpName);
        doc.addField(SolrAdditionalPaymentPresenter.FIELD_CATEGORY_LOOKUP_ID_NAME, categoryLookUpName.hashCode() + SolrAdditionalPaymentPresenter.SPLIT + categoryLookUpName);


        doc.addField(SolrAdditionalPaymentPresenter.FIELD_CREATION_DATE, getCreationDate());
        if (getApprovedDate() != null) {
            doc.addField(SolrAdditionalPaymentPresenter.FIELD_APPROVED_DATE, getApprovedDate());
        }
        if (getMonthID() != null) {
            doc.addField(SolrAdditionalPaymentPresenter.FIELD_MONTH_NAME, getMonth());
            doc.addField(SolrAdditionalPaymentPresenter.FIELD_MONTH_ID, getMonthID());
            doc.addField(SolrAdditionalPaymentPresenter.FIELD_MONTH_ID_NAME, getMonthID() + SolrAdditionalPaymentPresenter.SPLIT + getMonth() + SolrAdditionalPaymentPresenter.SPLIT + getYear());
        }
        if (getYear() != null) {
            doc.addField(SolrAdditionalPaymentPresenter.FIELD_YEAR, getYear());
            doc.addField(SolrAdditionalPaymentPresenter.FIELD_YEAR_ID_NAME, getYear() + SolrAdditionalPaymentPresenter.SPLIT + getYear());
        }

        doc.addField(SolrAdditionalPaymentPresenter.FIELD_TOTAL_AMOUNT, getTotal() != null ? getTotal().doubleValue() : 0d);
        doc.addField(SolrAdditionalPaymentPresenter.FIELD_TYPE, getType());
        String entityType = getEntityType();
        if (entityType != null) {
            doc.addField(SolrAdditionalPaymentPresenter.FIELD_ENTITY_TYPE, entityType);
        }
        if (getOverallStatus() != null) {
            doc.addField(SolrAdditionalPaymentPresenter.FIELD_STATUS_ID, getOverallStatus().getObjectID());
            doc.addField(SolrAdditionalPaymentPresenter.FIELD_STATUS_NAME, getOverallStatus().getName());
            doc.addField(SolrAdditionalPaymentPresenter.FIELD_STATUS_CODE, getOverallStatus().getCode());
            doc.addField(SolrAdditionalPaymentPresenter.FIELD_STATUS_ID_NAME, getOverallStatus().getObjectID() + SolrAdditionalPaymentPresenter.SPLIT + getOverallStatus().getName());
        }
        doc.addField(SolrAdditionalPaymentPresenter.FIELD_LAST_UPDATE, getLastUpdateTime());
        doc.addField(SolrAdditionalPaymentPresenter.FIELD_PAYMENT_CATEGORY, getCategoryType());
        doc.addField(SolrAdditionalPaymentPresenter.FIELD_PAYMENT_TYPE, getPaymentType());
        CustomFieldsUtils.setInSolrCustomFields(doc, getCustomFields());
        return doc;
    }


    @Override
    public boolean isCurrentApproverApproved() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && APPROVED.equals(getCurrentApprover().getStatus().getCode());
    }

    public Integer getFromId() {
        return fromId;
    }

    public void setFromId(Integer fromId) {
        this.fromId = fromId;
    }

    public String getFromType() {
        return fromType;
    }

    public void setFromType(String fromType) {
        this.fromType = fromType;
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

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public EdsPayrollBatch getPayrollBatch() {
        return payrollBatch;
    }

    public void setPayrollBatch(EdsPayrollBatch payrollBatch) {
        this.payrollBatch = payrollBatch;
    }

    public List<EdsPaymentDeduction> getItems() {
        return items;
    }

    public void setItems(List<EdsPaymentDeduction> items) {
        this.items = items;
    }

    @Override
    public List<EdsApprover> getApprovers() {
        return approvers;
    }

    @Override
    public void setApprovers(List<EdsApprover> approvers) {
        this.approvers = approvers;
    }

    public EdsEmployee getCreator() {
        return creator;
    }

    public void setCreator(EdsEmployee creator) {
        this.creator = creator;
    }

    @Override
    public void setEntityStatus(EdsReference overallStatus) {
        setOverallStatus(overallStatus);
    }

    public EdsCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(EdsCurrency currency) {
        this.currency = currency;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getType() {
        return type;
    }

    public Integer getLeaveRequestId() {
        return this.leaveRequestId;
    }

    public void setLeaveRequestId(final Integer leaveRequestId) {
        this.leaveRequestId = leaveRequestId;
    }

    public Integer getBackupsEmployeeId() {
        return backupsEmployeeId;
    }

    public void setBackupsEmployeeId(Integer backupsEmployeeId) {
        this.backupsEmployeeId = backupsEmployeeId;
    }

    public String getCalculationDetails() {
        return this.calculationDetails;
    }

    public void setCalculationDetails(final String calculationDetails) {
        this.calculationDetails = calculationDetails;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityType() {
        if (entityType != null) {
            return entityType;
        }
        if (getEmployeeIds() != null && !getEmployeeIds().isEmpty()) {
            return "employee";
        }
        if (getDepartment() != null) {
            return "department";
        }
        if (getLocation() != null) {
            return "location";
        }
        if (getSupervisor() != null) {
            return "supervisor";
        }
        if (getPayrollBatch() != null) {
            return "group";
        }
        return null;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getShowInPaySlip() {
        return showInPaySlip;
    }

    public void setShowInPaySlip(Boolean showInPaySlip) {
        this.showInPaySlip = showInPaySlip;
    }

    public Boolean getDeleted() {
        return deleted != null ? deleted : Boolean.FALSE;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
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

    public Integer getMonthID() {
        return monthID;
    }

    public void setMonthID(Integer monthID) {
        this.monthID = monthID;
    }

    public Integer getPdfTemplateID() {
        return pdfTemplateID;
    }

    public void setPdfTemplateID(Integer pdfTemplateID) {
        this.pdfTemplateID = pdfTemplateID;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public EdsUser getUpdater() {
        return updater;
    }

    public void setUpdater(EdsUser updater) {
        this.updater = updater;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    public String getEmployeeIds() {
        return employeeIds;
    }

    public void setEmployeeIds(String employeeIds) {
        this.employeeIds = employeeIds;
    }

    public Long getDefaultDate() {
        return defaultDate;
    }

    public void setDefaultDate(Long defaultDate) {
        this.defaultDate = defaultDate;
    }

    public EdsDepartment getDepartment() {
        return department;
    }

    public void setDepartment(EdsDepartment department) {
        this.department = department;
    }

    public EdsLocation getLocation() {
        return location;
    }

    public void setLocation(EdsLocation location) {
        this.location = location;
    }

    public EdsEmployee getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(EdsEmployee supervisor) {
        this.supervisor = supervisor;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public BigDecimal getFixedAmount() {
        return this.fixedAmount;
    }

    public void setFixedAmount(final BigDecimal fixedAmount) {
        this.fixedAmount = fixedAmount;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public Integer getDefaultPayrollCategoryId() {
        return defaultPayrollCategoryId;
    }

    public void setDefaultPayrollCategoryId(Integer defaultPayrollCategoryId) {
        this.defaultPayrollCategoryId = defaultPayrollCategoryId;
    }

    public String getAllowanceCategoriyIds() {
        return allowanceCategoriyIds;
    }

    public void setAllowanceCategoriyIds(String allowanceCategoriyIds) {
        this.allowanceCategoriyIds = allowanceCategoriyIds;
    }

    public Boolean getBasicAndAllowance() {
        return isBasicAndAllowance;
    }

    public void setBasicAndAllowance(Boolean basicAndAllowance) {
        isBasicAndAllowance = basicAndAllowance;
    }

    public Set<EdsPayrollPayment> getPayments() {
        return payments;
    }

    public void setPayments(Set<EdsPayrollPayment> payments) {
        this.payments = payments;
    }
}
