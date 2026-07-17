package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsPaymentMethod;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.accounting.EdsUserBankAccount;
import com.edatasite.workforce.core.domain.customfields.EdsPayrollCustomFields;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrGroupPayrunRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrSinglePayrunRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.payroll.client.rpc.SinglePayrunItem;
import com.edatasite.workforce.gwt.payroll.client.rpc.SinglePayrunSolrItem;
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
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 07.03.14
 * Time: 9:45
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "payslipTableItem")
public class EdsPayslipTableItem extends EdsObject {
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
    private EdsReference status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status2Id")
    private EdsReference status2;

    @Column(name = "frequency")
    private Integer frequency; //0-week, 1-month, 2- year (see Frequency)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payslipTable_id")
    private EdsPayslipTable payslipTable;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "payslipTableItem")
    @Where(clause = "(deleted = 'false' or deleted is null)")
    private Set<EdsPayrunPaymentItem> paymentItems = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private EdsEmployee employee;

    @OneToOne(fetch = FetchType.LAZY)
    private P11 payslip;

    @Column(name = "description", length = 500)
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "projectId")
    private EdsProject project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approvalOrRejectionStatus")
    private EdsReference approvalOrRejectionStatus;

    @Column(name = "basicSalary", precision = 14, scale = 4)
    private BigDecimal basicSalary;

    @Column(name = "dailyRate", precision = 14, scale = 4)
    private BigDecimal dailyRate;

    @Column(name = "actualMonthPay", precision = 14, scale = 4)
    private BigDecimal actualMonthPay;

    @Column(name = "allowance", precision = 14, scale = 4)
    private BigDecimal allowance;

    @Column(name = "additionalPay", precision = 14, scale = 4)
    private BigDecimal additionalPay;

    @Column(name = "deduction", precision = 14, scale = 4)
    private BigDecimal deduction;

    @Column(name = "tax", precision = 14, scale = 4)
    private BigDecimal tax;

    @Column(name = "employerContribution", precision = 14, scale = 4)
    private BigDecimal employerContribution;

    @Column(name = "expense", precision = 14, scale = 4)
    private BigDecimal expense;

    @Column(name = "total", precision = 14, scale = 4)
    private BigDecimal total;

    @Column(precision = 14, scale = 4)
    private BigDecimal totalInBase;

    @Column(name = "used_Petrol", precision = 14, scale = 4)
    private BigDecimal usedPetrol;

    @Column(name = "commision", precision = 14, scale = 4)
    private BigDecimal commision;

    @Column(name = "collection", precision = 14, scale = 4)
    private BigDecimal collection;

    @Column(name = "grossPay", precision = 25, scale = 4)
    private BigDecimal grossPay;

    @Column(name = "lastUpdateTime")
    private Date lastUpdateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    @ForeignKey(name = "none")
    private EdsCurrency currency;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private EdsPayrollCustomFields customFields;

    @Column(precision = 25, scale = 15)
    private BigDecimal exchangeRate;

    private BigDecimal pensionRate;
    private BigDecimal nonLocalPensionRate;
    private Integer pensionValueType;
    private Integer companyPensionType;
    private BigDecimal pensionAmount;
    private BigDecimal companyPensionAmount;
    private BigDecimal companyPensionRate;
    private BigDecimal companyNonLocalPensionRate;

    private BigDecimal daysWorked;
    private Date fromDate;
    private Date toDate;
    private Date processDate;

    private Date creationDate;
    private Date approvedDate;
    private Integer monthID;
    private Integer pdfTemplateID;
    private Integer year;
    private String month;

    private Boolean approved;
    private Boolean transacted;
    private Boolean sendEmail;
    private Boolean fromEndOfService;
    @Column(name = "rejection_note")
    @Type(type = "text")
    private String rejectionNote;
    @Column(name = "payment_policy")
    @Type(type = "text")
    private String paymentPolicy;
    private Long driverID;
    private Boolean deleted;
    private Boolean transactionCreated;
    private BigDecimal monthlySalik;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paymentMethodId")
    private EdsPaymentMethod paymentMethod;


    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public EdsPayslipTable getPayslipTable() {
        return payslipTable;
    }

    public void setPayslipTable(EdsPayslipTable payslipTable) {
        this.payslipTable = payslipTable;
    }

    public Set<EdsPayrunPaymentItem> getPaymentItems() {
        return paymentItems;
    }

    public void setPaymentItems(Set<EdsPayrunPaymentItem> paymentItems) {
        this.paymentItems = paymentItems;
    }

    public EdsEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(EdsEmployee employee) {
        this.employee = employee;
    }

    public P11 getPayslip() {
        return payslip;
    }

    public void setPayslip(P11 payslip) {
        this.payslip = payslip;
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

    public Date getProcessDate() {
        return processDate;
    }

    public void setProcessDate(Date processDate) {
        this.processDate = processDate;
    }

    public BigDecimal getDaysWorked() {
        return daysWorked;
    }

    public void setDaysWorked(BigDecimal daysWorked) {
        this.daysWorked = daysWorked;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getBasicSalary() {
        return basicSalary;
    }

    public void setBasicSalary(BigDecimal basicSalary) {
        this.basicSalary = basicSalary;
    }

    public BigDecimal getActualMonthPay() {
        return actualMonthPay;
    }

    public void setActualMonthPay(BigDecimal actualMonthPay) {
        this.actualMonthPay = actualMonthPay;
    }

    public BigDecimal getAllowance() {
        return allowance;
    }

    public void setAllowance(BigDecimal allowance) {
        this.allowance = allowance;
    }

    public BigDecimal getAdditionalPay() {
        return additionalPay;
    }

    public void setAdditionalPay(BigDecimal additionalPay) {
        this.additionalPay = additionalPay;
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

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getTotalInBase() {
        return totalInBase;
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

    public EdsPayrollCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsPayrollCustomFields customFields) {
        this.customFields = customFields;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(BigDecimal dailyRate) {
        this.dailyRate = dailyRate;
    }

    public BigDecimal getExpense() {
        return expense;
    }

    public void setExpense(BigDecimal expense) {
        this.expense = expense;
    }

    public BigDecimal getUsedPetrol() {
        return usedPetrol;
    }

    public void setUsedPetrol(BigDecimal usedPetrol) {
        this.usedPetrol = usedPetrol;
    }

    public BigDecimal getCommision() {
        return commision;
    }

    public void setCommision(BigDecimal commision) {
        this.commision = commision;
    }

    public BigDecimal getCollection() {
        return collection;
    }

    public void setCollection(BigDecimal collection) {
        this.collection = collection;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public BigDecimal getPensionRate() {
        return pensionRate;
    }

    public void setPensionRate(BigDecimal pensionRate) {
        this.pensionRate = pensionRate;
    }

    public BigDecimal getNonLocalPensionRate() {
        return nonLocalPensionRate;
    }

    public void setNonLocalPensionRate(BigDecimal nonLocalPensionRate) {
        this.nonLocalPensionRate = nonLocalPensionRate;
    }

    public BigDecimal getPensionAmount() {
        return pensionAmount;
    }

    public BigDecimal getCompanyPensionAmount() {
        return companyPensionAmount;
    }

    public void setCompanyPensionAmount(BigDecimal companyPensionAmount) {
        this.companyPensionAmount = companyPensionAmount;
    }

    public Integer getPensionValueType() {
        return pensionValueType;
    }

    public void setPensionValueType(Integer pensionValueType) {
        this.pensionValueType = pensionValueType;
    }

    public void setPensionAmount(BigDecimal pensionAmount) {
        this.pensionAmount = pensionAmount;
    }

    public BigDecimal getCompanyPensionRate() {
        return companyPensionRate;
    }

    public void setCompanyPensionRate(BigDecimal companyPensionRate) {
        this.companyPensionRate = companyPensionRate;
    }

    public BigDecimal getCompanyNonLocalPensionRate() {
        return companyNonLocalPensionRate;
    }

    public void setCompanyNonLocalPensionRate(BigDecimal companyNonLocalPensionRate) {
        this.companyNonLocalPensionRate = companyNonLocalPensionRate;
    }

    public Integer getCompanyPensionType() {
        return companyPensionType;
    }

    public void setCompanyPensionType(Integer companyPensionType) {
        this.companyPensionType = companyPensionType;
    }

    public Boolean isApproved() {
        return status != null ? Constants.PAYRUN_STATUS_APPROVED.equals(status.getCode())
                || Constants.PAYRUN_STATUS_PARTIAL_PAID.equals(status.getCode())
                || Constants.PAYRUN_STATUS_PAID.equals(status.getCode())
                : Boolean.FALSE;
    }

    public Boolean getApproved() {
        return isApproved();
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public Boolean isTransacted() {
        return transacted != null ? transacted : Boolean.FALSE;
    }

    public Boolean getTransacted() {
        return transacted;
    }

    public void setTransacted(Boolean transacted) {
        this.transacted = transacted;
    }

    public Boolean isSendEmail() {
        return sendEmail != null ? sendEmail : Boolean.FALSE;
    }

    public void setSendEmail(Boolean sendEmail) {
        this.sendEmail = sendEmail;
    }

    public Boolean isFromEndOfService() {
        return fromEndOfService != null ? fromEndOfService : Boolean.FALSE;
    }

    public void setFromEndOfService(Boolean fromEndOfService) {
        this.fromEndOfService = fromEndOfService;
    }

    public String getRejectionNote() {
        return rejectionNote;
    }

    public void setRejectionNote(String rejectionNote) {
        this.rejectionNote = rejectionNote;
    }

    public String getPaymentPolicy() {
        return paymentPolicy;
    }

    public void setPaymentPolicy(String paymentPolicy) {
        this.paymentPolicy = paymentPolicy;
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
        this.status = status;
    }

    public EdsReference getStatus2() {
        return status2;
    }

    public void setStatus2(EdsReference status2) {
        this.status2 = status2;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
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

    public Integer getMonthID() {
        return monthID;
    }

    public Integer getPdfTemplateID() {
        return pdfTemplateID;
    }

    public void setPdfTemplateID(Integer pdfTemplateID) {
        this.pdfTemplateID = pdfTemplateID;
    }

    public void setMonthID(Integer monthID) {
        this.monthID = monthID;
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

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean isTransactionCreated() {
        return transactionCreated != null ? transactionCreated : Boolean.FALSE;
    }

    public void setTransactionCreated(Boolean transactionCreated) {
        this.transactionCreated = transactionCreated;
    }

    public EdsPaymentMethod getPaymentMethod() {
        return this.paymentMethod;
    }

    public void setPaymentMethod(final EdsPaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    public EdsProject getProject() {
        return project;
    }

    public void setProject(EdsProject project) {
        this.project = project;
    }


    public SinglePayrunItem getRPC(EdsUserBankAccount userBankAccount) {
        SinglePayrunItem item = new SinglePayrunItem();
        item.setObjectID(getObjectID());
        if (this.getEmployee() != null) {
            item.setEmployeeID(this.getEmployee().getObjectID());
            item.setEmployee(this.getEmployee().getFullName());
            if (this.getEmployee().getProfile() != null) {
                item.setEmployeeCode(this.getEmployee().getProfile().getEmployeeCode());
            }
            item.setDriverID(this.getEmployee().getDriverNumber());
            if (userBankAccount != null) {
                item.setBankCode(userBankAccount.getAgentID() != null ? userBankAccount.getAgentID() : "");
                item.setBankIBAN(userBankAccount.getIbanCode() != null ? userBankAccount.getIbanCode() : "");
                item.setBankName(userBankAccount.getBankName() != null ? userBankAccount.getBankName() : "");
                item.setAccountName(userBankAccount.getAccountName() != null ? userBankAccount.getAccountName() : "");
                item.setAccountNumber(userBankAccount.getAccountNumber() != null ? userBankAccount.getAccountNumber() : "");
            }

        }
        item.setFromDate(new DateNonConvertable(getFromDate()));
        item.setToDate(new DateNonConvertable(getToDate()));
        item.setProcessDate(getProcessDate() != null ? new DateNonConvertable(getProcessDate()) : new DateNonConvertable(getToDate()));
        item.setDaysWorked(getDaysWorked());
        item.setBasicSalary(getBasicSalary());
        item.setDailyRate(getDailyRate());
        item.setActualMonthPay(getActualMonthPay());
        item.setAllowance(getAllowance());
        item.setAdditionalPay(getAdditionalPay());
        item.setDeduction(getDeduction());
        item.setTax(getTax());
        item.setEmployerContribution(getEmployerContribution());
        item.setExpense(getExpense());
        item.setComission(getCommision());
        item.setMonthlyCollection(getCollection());
        item.setSpentFlueAmount(getUsedPetrol());
        item.setMonthlySalik(getMonthlySalik());
        item.setDescription(getDescription() != null ? getDescription() : "");
        item.setTotal(getTotal());
        item.setTotalInBase(getTotalInBase());
        item.setCurrency(getCurrency() != null ? getCurrency().createCurrencyItem() : null);
        item.setExchangeRate(getExchangeRate());
        item.setApproved(isApproved());
        item.setRejectionNote(getRejectionNote());
        item.setSendEmail(isSendEmail());
        item.setPensionRate(getPensionRate());
        item.setPensionValueType(getPensionValueType());
        item.setPensionAmount(getPensionAmount());
        item.setCompanyPensionAmount(getCompanyPensionAmount());
        item.setCompanyPensionType(getCompanyPensionType());
        item.setCompanyPensionRate(getCompanyPensionRate());
        item.setCompanyNonLocalPensionRate(getCompanyNonLocalPensionRate());
        item.setMonthID(getMonthID());
        item.setYear(getYear());
        item.setMonth(getMonth());
        item.setFrequency(getFrequency());
        if (getStatus() != null) {
            item.setStatusID(getStatus().getObjectID());
            item.setStatus(getStatus().getName());
            item.setStatusCode(getStatus().getCode());
        }
        item.setStatus2(getStatus2() != null ? getStatus2().getName() : "");
        item.setCreator(getPreparer() != null ? getPreparer().getAsSelectItem() : null);
        item.setApprover(getApprover() != null ? getApprover().getAsSelectItem() : null);
        item.setApprover2(getApprover2() != null ? getApprover2().getAsSelectItem() : null);
        item.setFromEndOfService(isFromEndOfService());
        item.setPaymentPolicy(getPaymentPolicy());
        item.setPdfTemplateID(getPdfTemplateID());
        item.setApprovalRejectionStatus(getApprovalOrRejectionStatus() != null ? getApprovalOrRejectionStatus().getCode() : null);
        item.setGross(getGrossPay());
        return item;
    }

    public SinglePayrunSolrItem getSolrRPC() {
        SinglePayrunSolrItem singlePayrunSolrItem = new SinglePayrunSolrItem();

        singlePayrunSolrItem.setObjectID(getObjectID());

        if (getPreparer() != null) {
            singlePayrunSolrItem.setPreparer(getPreparer().getAsSelectItem());
        }

        if (getEmployee() != null) {
            SelectItem employee = getEmployee().getAsSelectItem();
            if (getEmployee().getProfile() != null) {
                String code = getEmployee().getProfile().getEmployeeCode();
                employee.setCode(code);
            }
            singlePayrunSolrItem.setEmployee(employee);
        }

        if (getApprover() != null) {
            singlePayrunSolrItem.setApprover(getApprover().getAsSelectItem());
        }

        if (getApprover2() != null) {
            singlePayrunSolrItem.setApprover2(getApprover2().getAsSelectItem());
        }

        if (getStatus() != null) {
            singlePayrunSolrItem.setStatus(getStatus().getRPC());
        }

        if (getStatus2() != null) {
            singlePayrunSolrItem.setStatus2(getStatus2().getRPC());
        }

        singlePayrunSolrItem.setFrequency(getFrequency());

        if (getPayslipTable() != null) {
            EdsPayslipTable edsPayslipTable = getPayslipTable();
            singlePayrunSolrItem.setPayslipTableId(edsPayslipTable.getObjectID());
            if (edsPayslipTable.getPayrollBatch() != null) {
                singlePayrunSolrItem.setPayrollBatchId(edsPayslipTable.getPayrollBatch().getObjectID());
            }
        }

        singlePayrunSolrItem.setDescription(getDescription());
        singlePayrunSolrItem.setBasicSalary(getBasicSalary() != null ? getBasicSalary().toString() : "");
        singlePayrunSolrItem.setDailyRate(getDailyRate() != null ? getDailyRate().toString() : "");
        singlePayrunSolrItem.setActualMonthPay(getActualMonthPay() != null ? getActualMonthPay().toString() : "");
        singlePayrunSolrItem.setAllowance(getAllowance() != null ? getAllowance().toString() : "");
        singlePayrunSolrItem.setAdditionalPay(getAdditionalPay() != null ? getAdditionalPay().toString() : "");
        singlePayrunSolrItem.setDeduction(getDeduction() != null ? getDeduction().toString() : "");
        singlePayrunSolrItem.setExpense(getExpense() != null ? getExpense().toString() : "");
        singlePayrunSolrItem.setTotal(getTotal() != null ? getTotal().toString() : "");

        if (getCurrency() != null) {
            singlePayrunSolrItem.setCurrency(getCurrency().getAsSelectItem());
            singlePayrunSolrItem.setCurrencySymbol(getCurrency().getSymbol());
        }

        singlePayrunSolrItem.setUsedPertol(getUsedPetrol() != null ? getUsedPetrol().toString() : "");
        singlePayrunSolrItem.setComission(getCommision() != null ? getCommision().toString() : "");
        singlePayrunSolrItem.setCollection(getCollection() != null ? getCollection().toString() : "");
        singlePayrunSolrItem.setPensionRate(getPensionRate() != null ? getPensionRate().toString() : "");
        singlePayrunSolrItem.setNonLocalPensionRate(getNonLocalPensionRate() != null ? getNonLocalPensionRate().toString() : "");
        singlePayrunSolrItem.setPensionValueType(getPensionValueType() != null ? getPensionValueType().toString() : "");
        singlePayrunSolrItem.setCompanyPensionType(getCompanyPensionType());
        singlePayrunSolrItem.setPensionAmount(getPensionAmount() != null ? getPensionAmount().toString() : "");
        singlePayrunSolrItem.setCompanyPensionAmount(getCompanyPensionAmount() != null ? getCompanyPensionAmount().toString() : "");
        singlePayrunSolrItem.setCompanyPensionRate(getCompanyPensionRate() != null ? getCompanyPensionRate().toString() : "");
        singlePayrunSolrItem.setCompanyNonLocalPensionRate(getCompanyNonLocalPensionRate() != null ? getCompanyNonLocalPensionRate().toString() : "");
        singlePayrunSolrItem.setDaysWorked(getDaysWorked() != null ? getDaysWorked().longValue() : 0);
        singlePayrunSolrItem.setFromDate(getFromDate());
        singlePayrunSolrItem.setToDate(getToDate());
        singlePayrunSolrItem.setProcessDate(getProcessDate());
        singlePayrunSolrItem.setCreationDate(getCreationDate());
        singlePayrunSolrItem.setApprovedDate(getApprovedDate());
        singlePayrunSolrItem.setPdfTemplateId(getPdfTemplateID() != null ? getPdfTemplateID().toString() : "");
        singlePayrunSolrItem.setYear(getYear());
        if (getMonth() != null && getMonthID() != null) {
            singlePayrunSolrItem.setMonth(new SelectItem(getMonthID(), getMonth()));
        }
        singlePayrunSolrItem.setApproved(getApproved());
        singlePayrunSolrItem.setTransacted(getTransacted());
        singlePayrunSolrItem.setSendEmail(isSendEmail());
        singlePayrunSolrItem.setFromEndOfService(isFromEndOfService());
        singlePayrunSolrItem.setRejectionNote(getRejectionNote());
        singlePayrunSolrItem.setPaymentPolicy(getPaymentPolicy());
        singlePayrunSolrItem.setDriverId(getDriverID());
        singlePayrunSolrItem.setDeleted(isDeleted());
        singlePayrunSolrItem.setLastUpdate(getLastUpdateTime());

        return singlePayrunSolrItem;
    }

    public EdsReference getApprovalOrRejectionStatus() {
        return approvalOrRejectionStatus;
    }

    public void setApprovalOrRejectionStatus(EdsReference approvalOrRejectionStatus) {
        this.approvalOrRejectionStatus = approvalOrRejectionStatus;
    }

    public void setDriverID(Long driverID) {
        this.driverID = driverID;
    }

    public Long getDriverID() {
        return driverID;
    }

    public BigDecimal getMonthlySalik() {
        return monthlySalik;
    }

    public void setMonthlySalik(BigDecimal monthlySalik) {
        this.monthlySalik = monthlySalik;
    }

    public BigDecimal getGrossPay() {
        return grossPay;
    }

    public void setGrossPay(BigDecimal grossPay) {
        this.grossPay = grossPay;
    }

    public SolrInputDocument indexToSolr(Integer companyID, boolean isIntegerNumberEnabled) {
        SolrInputDocument doc = new SolrInputDocument();
        String compositID = companyID + "_" + getObjectID();

        doc.addField(SolrSinglePayrunRepresenter.FIELD_COMPANY_ID, companyID);
        doc.addField(SolrSinglePayrunRepresenter.FIELD_COMPOSITE_ID, compositID);
        doc.addField(SolrSinglePayrunRepresenter.FIELD_SINGLE_PAYRUN_ID, getObjectID());
        if (getEmployee() != null && getEmployee().getProfile() != null) {
            doc.addField(SolrSinglePayrunRepresenter.FIELD_EMPLOYEE_ID, getEmployee().getObjectID());
            doc.addField(SolrSinglePayrunRepresenter.FIELD_EMPLOYEE_NAME, getEmployee().getFullName());
            doc.addField(SolrSinglePayrunRepresenter.FIELD_EMPLOYEE_CODE, getEmployee().getProfile().getEmployeeCode());
            String code = getEmployee().getProfile().getEmployeeCode();
            if (code != null && !"".equals(code) && isIntegerNumberEnabled) {
                doc.addField(SolrSinglePayrunRepresenter.FIELD_EMPLOYEE_INTEGER_NUMBER, Long.parseLong(code.replaceAll("[\\D]", "")));
            }
            doc.addField(SolrSinglePayrunRepresenter.FIELD_EMPLOYEE_ID_NAME, getEmployee().getObjectID() + SolrSinglePayrunRepresenter.SPLIT + getEmployee().getFullName());
        }
        if (getPreparer() != null) {
            doc.addField(SolrSinglePayrunRepresenter.FIELD_PREPARER_ID, getPreparer().getObjectID());
            doc.addField(SolrSinglePayrunRepresenter.FIELD_PREPARER_NAME, getPreparer().getName());
        }
        if (getStatus() != null) {
            doc.addField(SolrSinglePayrunRepresenter.FIELD_STATUS_ID, getStatus().getObjectID());
            doc.addField(SolrSinglePayrunRepresenter.FIELD_STATUS_NAME, getStatus().getName());
            doc.addField(SolrSinglePayrunRepresenter.FIELD_STATUS_CODE, getStatus().getCode());
            doc.addField(SolrSinglePayrunRepresenter.FIELD_STATUS_ID_NAME, getStatus().getObjectID() + SolrSinglePayrunRepresenter.SPLIT + getStatus().getName());
        }
//        if (getStatus2() != null) {
//            doc.addField(SolrSinglePayrunRepresenter.FIELD_STATUS2_ID, getStatus2().getObjectID());
//            doc.addField(SolrSinglePayrunRepresenter.FIELD_STATUS2_CODE, getStatus2().getCode());
//            doc.addField(SolrSinglePayrunRepresenter.FIELD_STATUS2_NAME, getStatus2().getName());
//            doc.addField(SolrSinglePayrunRepresenter.FIELD_STATUS2_ID_NAME, getStatus2().getObjectID() + SolrSinglePayrunRepresenter.SPLIT + getStatus2().getName());
//        }
        if (getApprover() != null) {
            doc.addField(SolrSinglePayrunRepresenter.FIELD_APPROVER_ID, getApprover().getObjectID());
            doc.addField(SolrSinglePayrunRepresenter.FIELD_APPROVER_NAME, getApprover().getFullName());
            doc.addField(SolrSinglePayrunRepresenter.FIELD_APPROVER_ID_NAME, getApprover().getObjectID() + SolrSinglePayrunRepresenter.SPLIT + getApprover().getFullName());
        }
        if (getCurrency() != null) {
            doc.addField(SolrSinglePayrunRepresenter.FIELD_CURRENCY_ID, getCurrency().getObjectID());
            doc.addField(SolrSinglePayrunRepresenter.FIELD_CURRENCY_NAME, getCurrency().getName());
            doc.addField(SolrSinglePayrunRepresenter.FIELD_CURRENCY_ID_NAME, getCurrency().getObjectID() + SolrSinglePayrunRepresenter.SPLIT + getCurrency().getName());
        } else {
            if (getPayslipTable() != null && getPayslipTable().getCurrency() != null) {
                EdsCurrency currency = getPayslipTable().getCurrency();
                doc.addField(SolrSinglePayrunRepresenter.FIELD_CURRENCY_ID, currency.getObjectID());
                doc.addField(SolrSinglePayrunRepresenter.FIELD_CURRENCY_NAME, currency.getName());
                doc.addField(SolrSinglePayrunRepresenter.FIELD_CURRENCY_ID_NAME, currency.getObjectID() + SolrSinglePayrunRepresenter.SPLIT + currency.getName());
            }
        }
//        if (getApprover2() != null) {
//            doc.addField(SolrSinglePayrunRepresenter.FIELD_APPROVER2_ID, getApprover2().getObjectID());
//            doc.addField(SolrSinglePayrunRepresenter.FIELD_APPROVER2_NAME, getApprover2().getName());
//            doc.addField(SolrSinglePayrunRepresenter.FIELD_APPROVER2_ID_NAME, getApprover2().getObjectID() + SolrSinglePayrunRepresenter.SPLIT + getApprover2().getName());
//        }
        if (getMonthID() != null) {
            doc.addField(SolrSinglePayrunRepresenter.FIELD_MONTH_ID, getMonthID());
            doc.addField(SolrSinglePayrunRepresenter.FIELD_MONTH_NAME, getMonth());
            doc.addField(SolrSinglePayrunRepresenter.FIELD_MONTH_ID_NAME, getMonthID() + SolrSinglePayrunRepresenter.SPLIT + getMonth() + SolrGroupPayrunRepresenter.SPLIT + getYear());
        }
        if (getYear() != null) {
            doc.addField(SolrSinglePayrunRepresenter.FIELD_YEAR, getYear());
        }
        if (getFrequency() != null) {
            doc.addField(SolrSinglePayrunRepresenter.FIELD_FREQUENCY, getFrequency());
        }
        if (getTotal() != null) {
            doc.addField(SolrSinglePayrunRepresenter.FIELD_TOTAL, getTotal().doubleValue());
        }
        if (getPayslipTable() != null) {
            doc.addField(SolrSinglePayrunRepresenter.FIELD_PAYSLIP_TABLE_ID, getPayslipTable().getObjectID());
            if (getPayslipTable().getPayrollBatch() != null) {
                doc.addField(SolrSinglePayrunRepresenter.FIELD_PAYROLL_BATCH_ID, getPayslipTable().getPayrollBatch().getObjectID());
            }
        }
        if (getEmployee().getPayrollBatches() != null) {
            for (EdsPayrollBatch batch : getEmployee().getPayrollBatches()) {
                doc.addField(SolrSinglePayrunRepresenter.FIELD_PAYROLL_BATCH_ID, batch.getObjectID());
            }
        }
        if (getFromDate() != null) {
            doc.addField(SolrSinglePayrunRepresenter.FIELD_FROM_DATE, getFromDate());
        }
        if (getToDate() != null) {
            doc.addField(SolrSinglePayrunRepresenter.FIELD_TO_DATE, getToDate());
        }
        if (getProcessDate() != null) {
            doc.addField(SolrSinglePayrunRepresenter.FIELD_PROCESS_DATE, getProcessDate());
        } else if (getToDate() != null) {
            doc.addField(SolrSinglePayrunRepresenter.FIELD_PROCESS_DATE, getToDate());
        }
        if (getCreationDate() != null) {
            doc.addField(SolrSinglePayrunRepresenter.FIELD_CREATION_DATE, getCreationDate());
        }
        doc.addField(SolrSinglePayrunRepresenter.FIELD_APPROVED, isApproved());
        if (isApproved()) {
            doc.addField(SolrSinglePayrunRepresenter.FIELD_APPROVED_DATE, getApprovedDate());
        }
        doc.addField(SolrSinglePayrunRepresenter.FIELD_PAYMENT_METHOD, getPaymentMethod() != null && getPaymentMethod().getName() != null ? getPaymentMethod().getName() : getEmployee().getPayMethod() != null ? getEmployee().getPayMethod().getName() : "");
//        doc.addField(SolrSinglePayrunRepresenter.FIELD_DESCRIPTION, getDescription());
//        doc.addField(SolrSinglePayrunRepresenter.FIELD_BASIC_SALARY, getBasicSalary());
//        doc.addField(SolrSinglePayrunRepresenter.FIELD_DAILY_RATE, getDailyRate());
//        doc.addField(SolrSinglePayrunRepresenter.FIELD_ACTUAL_MONTH_PAY, getActualMonthPay());
//        doc.addField(SolrSinglePayrunRepresenter.FIELD_ALLOWANCE, getAllowance());
//        doc.addField(SolrSinglePayrunRepresenter.FIELD_ADDITIONAL_PAY, getAdditionalPay());
//        doc.addField(SolrSinglePayrunRepresenter.FIELD_DEDUCTION, getDeduction());
//        doc.addField(SolrSinglePayrunRepresenter.FIELD_EXPENSE, getExpense());
//        doc.addField(SolrSinglePayrunRepresenter.FIELD_USED_PETROL, getUsedPetrol());
//        doc.addField(SolrSinglePayrunRepresenter.FIELD_COMMISION, getCommision());
//        doc.addField(SolrSinglePayrunRepresenter.FIELD_PENSION_RATE, getPensionRate());

//        doc.addField(SolrSinglePayrunRepresenter.FIELD_DAYS_WORKED, getDaysWorked());
        doc.addField(SolrSinglePayrunRepresenter.FIELD_PDF_TEMPLATE_ID, getPdfTemplateID());
//        doc.addField(SolrSinglePayrunRepresenter.FIELD_SEND_EMAIL, isSendEmail());
//        doc.addField(SolrSinglePayrunRepresenter.FIELD_FROM_END_OF_SERVICE, isFromEndOfService());
        doc.addField(SolrSinglePayrunRepresenter.FIELD_DRIVER_ID, getDriverID());

        CustomFieldsUtils.setInSolrCustomFields(doc, getCustomFields());

        return doc;
    }
}
