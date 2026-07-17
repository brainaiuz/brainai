package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.core.domain.EdsPaymentMethod;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.workflow.EdsTraceable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrGroupPayrunRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.payroll.client.rpc.PayslipTable;
import org.apache.solr.common.SolrInputDocument;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 07.03.14
 * Time: 9:43
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "payslipTable")
public class EdsPayslipTable extends EdsTraceable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "preparer_id")
    private EdsEmployee preparer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id")
    private EdsEmployee approver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver2Id")
    private EdsEmployee approver2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updater_id")
    private EdsUser updator;

    @ManyToOne(fetch = FetchType.LAZY)
    private EdsReference status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status2Id")
    private EdsReference status2;

    @Column(name = "frequency")
    private Integer frequency; //0-week, 1-month, 2- year (see Frequency)

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "payslipTable")
    @Where(clause = "(deleted = 'false' or deleted is null)")
    private List<EdsPayslipTableItem> payslipTableItems = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "payslipTable")
    @Where(clause = "(deleted = 'false' or deleted is null)")
    private Set<EdsPayrunPayment> payments = new HashSet<>();

    @Column(name = "totalAmount", precision = 14, scale = 4)
    private BigDecimal totalAmount;

    @Column(precision = 14, scale = 4)
    private BigDecimal totalInBase;

    @Column(name = "lastUpdateTime")
    private Date lastUpdateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    @ForeignKey(name = "none")
    private EdsCurrency currency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paymentMethodId")
    private EdsPaymentMethod paymentMethod;

    @Column(precision = 25, scale = 15)
    private BigDecimal exchangeRate;

    private Integer monthID;
    private Integer departmentID;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "payrollbatchid")
    private EdsPayrollBatch payrollBatch;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "projectId")
    private EdsProject project;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "locationId")
    private EdsLocation location;

    private Integer totalItems;
    private Integer successItems;
    private Integer failedItems;

    private Integer year;
    private String month;
    private Boolean deleted = false;
    private Boolean fromTaxi;

    private Date creationDate;
    private Date approvedDate;
    private Date processDate;

    private BigDecimal basicSalary;
    private BigDecimal allowance;
    private BigDecimal pension;
    private BigDecimal deduction;
    private BigDecimal tax;

    private BigDecimal employerContribution;
    private BigDecimal expense;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public EdsEmployee getPreparer() {
        return preparer;
    }

    public void setPreparer(EdsEmployee preparer) {
        this.preparer = preparer;
    }

    public EdsEmployee getApprover() {
        return approver;
    }

    public void setApprover(EdsEmployee approver) {
        this.approver = approver;
    }

    public EdsEmployee getApprover2() {
        return approver2;
    }

    public void setApprover2(EdsEmployee approver2) {
        this.approver2 = approver2;
    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        if (!ServerUtils.equalsReference(this.status, status)) {
            addChange(CustomFormConstants.STATUS);
        }
        this.status = status;
    }

    public EdsReference getStatus2() {
        return status2;
    }

    public void setStatus2(EdsReference status2) {
        this.status2 = status2;
    }

    public Integer getMonthID() {
        return monthID;
    }

    public void setMonthID(Integer monthID) {
        this.monthID = monthID;
    }

    public Integer getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(Integer departmentID) {
        this.departmentID = departmentID;
    }

    public EdsPayrollBatch getPayrollBatch() {
        return payrollBatch;
    }

    public void setPayrollBatch(EdsPayrollBatch payrollBatch) {
        this.payrollBatch = payrollBatch;
    }

    public Integer getTotalItems() {
        return totalItems != null ? totalItems : 0;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public Integer getSuccessItems() {
        return successItems != null ? successItems : 0;
    }

    public void setSuccessItems(Integer successItems) {
        this.successItems = successItems;
    }

    public Integer getFailedItems() {
        return failedItems != null ? failedItems : 0;
    }

    public void setFailedItems(Integer failedItems) {
        this.failedItems = failedItems;
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

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public List<EdsPayslipTableItem> getPayslipTableItems() {
        return payslipTableItems;
    }

    public void setPayslipTableItems(List<EdsPayslipTableItem> payslipTableItems) {
        this.payslipTableItems = payslipTableItems;
    }

    public Set<EdsPayrunPayment> getPayments() {
        return payments;
    }

    public void setPayments(Set<EdsPayrunPayment> payments) {
        this.payments = payments;
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

    public Date getProcessDate() {
        return processDate;
    }

    public void setProcessDate(Date processDate) {
        this.processDate = processDate;
    }

    public Boolean isDeleted() {
        return deleted != null ? deleted : Boolean.FALSE;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean isFromTaxi() {
        return fromTaxi != null ? fromTaxi : Boolean.FALSE;
    }

    public void setFromTaxi(Boolean fromTaxi) {
        this.fromTaxi = fromTaxi;
    }

    public BigDecimal getTotalInBase() {
        return totalInBase != null ? totalInBase : totalAmount;
    }

    public void setTotalInBase(BigDecimal totalInBase) {
        this.totalInBase = totalInBase;
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

    public EdsPaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(EdsPaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public EdsProject getProject() {
        return project;
    }

    public void setProject(EdsProject project) {
        this.project = project;
    }

    public EdsLocation getLocation() {
        return this.location;
    }

    public void setLocation(final EdsLocation location) {
        this.location = location;
    }

    public EdsUser getUpdator() {
        return updator;
    }

    public void setUpdator(EdsUser updator) {
        this.updator = updator;
    }

    public SolrInputDocument indexToSolr(Integer companyID) {
        SolrInputDocument doc = new SolrInputDocument();
        String compositID = companyID + "_" + getObjectID();

        doc.addField(SolrGroupPayrunRepresenter.FIELD_COMPANY_ID, companyID);
        doc.addField(SolrGroupPayrunRepresenter.FIELD_COMPOSITE_ID, compositID);
        doc.addField(SolrGroupPayrunRepresenter.FIELD_GROUP_PAYRUN_ID, getObjectID());
        if (getPreparer() != null) {
            doc.addField(SolrGroupPayrunRepresenter.FIELD_PREPARER_ID, getPreparer().getObjectID());
            doc.addField(SolrGroupPayrunRepresenter.FIELD_PREPARER_NAME, getPreparer().getName());
            doc.addField(SolrGroupPayrunRepresenter.FIELD_PREPARER_ID_NAME, getPreparer().getObjectID() + SolrGroupPayrunRepresenter.SPLIT + getPreparer().getFullName());
        }
        if (getMonthID() != null) {
            doc.addField(SolrGroupPayrunRepresenter.FIELD_MONTH_ID, getMonthID());
            doc.addField(SolrGroupPayrunRepresenter.FIELD_MONTH_NAME, getMonth());
            doc.addField(SolrGroupPayrunRepresenter.FIELD_MONTH_ID_NAME, getMonthID() + SolrGroupPayrunRepresenter.SPLIT + getMonth() + SolrGroupPayrunRepresenter.SPLIT + getYear());
        }
        if (getYear() != null) {
            doc.addField(SolrGroupPayrunRepresenter.FIELD_YEAR, getYear());
            doc.addField(SolrGroupPayrunRepresenter.FIELD_YEAR_ID_NAME, getYear() + SolrGroupPayrunRepresenter.SPLIT + getYear());
        }
        if (getApprover() != null) {
            doc.addField(SolrGroupPayrunRepresenter.FIELD_APPROVER_ID, getApprover().getObjectID());
            doc.addField(SolrGroupPayrunRepresenter.FIELD_APPROVER_NAME, getApprover().getName());
            doc.addField(SolrGroupPayrunRepresenter.FIELD_APPROVER_ID_NAME, getApprover().getObjectID() + SolrGroupPayrunRepresenter.SPLIT + getApprover().getName());
        }
        if (getStatus() != null) {
            doc.addField(SolrGroupPayrunRepresenter.FIELD_STATUS_ID, getStatus().getObjectID());
            doc.addField(SolrGroupPayrunRepresenter.FIELD_STATUS_NAME, getStatus().getName());
            doc.addField(SolrGroupPayrunRepresenter.FIELD_STATUS_CODE, getStatus().getCode());
            doc.addField(SolrGroupPayrunRepresenter.FIELD_STATUS_ID_NAME, getStatus().getObjectID() + SolrGroupPayrunRepresenter.SPLIT + getStatus().getName());
        }

        doc.addField(SolrGroupPayrunRepresenter.FIELD_TOTAL, getTotalAmount() != null ? getTotalAmount().doubleValue() : 0d);
        doc.addField(SolrGroupPayrunRepresenter.FIELD_TOTAL_IN_BASE, getTotalInBase() != null ? getTotalInBase().doubleValue() : 0d);

        if (getCurrency() != null) {
            doc.addField(SolrGroupPayrunRepresenter.FIELD_FOREIGN_CURRENCY_NAME, getCurrency().getName());
        } else {
            doc.addField(SolrGroupPayrunRepresenter.FIELD_FOREIGN_CURRENCY_NAME, null);
        }
        if (payrollBatch != null) {
            doc.addField(SolrGroupPayrunRepresenter.FIELD_PAYROLL_BATCH_ID, payrollBatch.getObjectID());
            doc.addField(SolrGroupPayrunRepresenter.FIELD_PAYROLL_BATCH_NAME, payrollBatch.getName());
        }
        if (getProject() != null) {
            doc.addField(SolrGroupPayrunRepresenter.FIELD_PROJECT_ID, getProject().getObjectID());
            doc.addField(SolrGroupPayrunRepresenter.FIELD_PROJECT_NAME, getProject().getName());
        }
        if (getLocation() != null) {
            doc.addField(SolrGroupPayrunRepresenter.FIELD_LOCATION_ID, getLocation().getObjectID());
            doc.addField(SolrGroupPayrunRepresenter.FIELD_LOCATION_NAME, getLocation().getName());
        }
        doc.addField(SolrGroupPayrunRepresenter.FIELD_CREATION_DATE, getCreationDate());
        doc.addField(SolrGroupPayrunRepresenter.FIELD_APPROVED_DATE, getApprovedDate());
        doc.addField(SolrGroupPayrunRepresenter.FIELD_LAST_UPDATE, getLastUpdateTime());
        doc.addField(SolrGroupPayrunRepresenter.FIELD_BASIC_SALARY, getBasicSalary() != null ? getBasicSalary().doubleValue() : 0d);
        doc.addField(SolrGroupPayrunRepresenter.FIELD_ALLOWANCE, getAllowance() != null ? getAllowance().doubleValue() : 0d);
        doc.addField(SolrGroupPayrunRepresenter.FIELD_PENSION, getPension() != null ? getPension().doubleValue() : 0d);
        doc.addField(SolrGroupPayrunRepresenter.FIELD_DEDUCTION, getDeduction() != null ? getDeduction().doubleValue() : 0d);
        doc.addField(SolrGroupPayrunRepresenter.FIELD_EXPENSE, getExpense() != null ? getExpense().doubleValue() : 0d);
        if (getPaymentMethod() != null) {
            doc.addField(SolrGroupPayrunRepresenter.FIELD_PAYMENT_METHOD_NAME, getPaymentMethod().getName());
        }
        if (getProcessDate() != null) {
            doc.addField(SolrGroupPayrunRepresenter.FIELD_PROCESS_DATE, getProcessDate());
        } else if (getCreationDate() != null) {
            DateUtil.getMonthLastDate(new Date(getCreationDate().getYear() - 100, getCreationDate().getMonth(), 1));
        }
        return doc;
    }

    public BigDecimal getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(BigDecimal basicSalary) {
        this.basicSalary = basicSalary;
    }

    public BigDecimal getAllowance() {
        return allowance;
    }

    public void setAllowance(BigDecimal allowance) {
        this.allowance = allowance;
    }

    public BigDecimal getPension() {
        return pension;
    }

    public void setPension(BigDecimal pension) {
        this.pension = pension;
    }

    public BigDecimal getDeduction() {
        return deduction;
    }

    public void setDeduction(BigDecimal deduction) {
        this.deduction = deduction;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getEmployerContribution() {
        return employerContribution;
    }

    public void setEmployerContribution(BigDecimal employerContribution) {
        this.employerContribution = employerContribution;
    }

    public BigDecimal getExpense() {
        return expense;
    }

    public void setExpense(BigDecimal expense) {
        this.expense = expense;
    }

    public PayslipTable getRPC() {
        PayslipTable rpc = new PayslipTable();
        rpc.setId(getObjectID());
        rpc.setCreationDate(getCreationDate());
        Optional.ofNullable(getPreparer())
                .map(p -> new SelectItem(p.getObjectID(), p.getName()))
                .ifPresent(rpc::setPreparer);
        Optional.ofNullable(getStatus())
                .map(EdsReference::getRPC)
                .ifPresent(rpc::setStatus);
        Optional.ofNullable(getApprover())
                .map(a -> new SelectItem(a.getObjectID(), a.getName()))
                .ifPresent(rpc::setApprover);
        rpc.setLastUpdateTime(getLastUpdateTime());
        rpc.setApprovedDate(getApprovedDate());
        rpc.setProcessDate(getProcessDate());
        rpc.setMonth(new SelectItem(getMonthID(), getMonth()));
        rpc.setYear(getYear());
        rpc.setTotalAmount(getTotalAmount());
        rpc.setTotalInBase(getTotalInBase());
        Optional.ofNullable(getPayrollBatch())
                .map(pb -> new SelectItem(pb.getObjectID(), pb.getName()))
                .ifPresent(rpc::setPayrollBatch);
        Optional.ofNullable(getPaymentMethod())
                .map(EdsPaymentMethod::getRPC)
                .ifPresent(rpc::setPaymentMethod);
        Optional.ofNullable(getCurrency())
                .map(c -> new SelectItem(c.getObjectID(), c.getName()))
                .ifPresent(rpc::setCurrency);
        Optional.ofNullable(getProject())
                .map(p -> new SelectItem(p.getObjectID(), p.getName()))
                .ifPresent(rpc::setProject);
        Optional.ofNullable(getLocation())
                .map(EdsLocation::getRPC)
                .ifPresent(rpc::setLocation);
        rpc.setDeleted(isDeleted());
        rpc.setFrequency(getFrequency());
        rpc.setPension(getPension());
        rpc.setDeduction(getDeduction());
        rpc.setExpense(getExpense());
        return rpc;
    }
}
