package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsFixedAsset;
import com.edatasite.workforce.core.domain.accounting.EdsInvoicePayment;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseOrder;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.approving.EdsApprovable;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.EdsOpportunity;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.customfields.EdsInvoiceCustomFields;
import com.edatasite.workforce.core.domain.pdf.EdsCompanyPdfTemplate;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrExpenseReportRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.domain.ObjectHistory;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseReportsListItem;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseReportsSolrItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.common.SolrInputDocument;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
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
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static com.edatasite.workforce.core.domain.accounting.EdsInvoicePayment.REVERSED;
import static com.edatasite.workforce.gwt.core.server.app.Utils.isOk;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "expenseReport")
public class EdsExpenseReport extends EdsApprovable implements AccountingConstants, ObjectHistory {

    public static final String EXPENSE_APPROVED = "EXPENSE_APPROVED";
    public static final String EXPENSE_DECLINED = "EXPENSE_DECLINED";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "number")
    private String number;

    @Column(name = "intNumber")
    private Integer intNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporterId")
    private EdsEmployee reporter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidateId")
    private EdsCrmContact candidate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId")
    private EdsProject project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opportunityId")
    private EdsOpportunity opportunity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "baseCurrencyId")
    @ForeignKey(name = "none")
    private EdsCurrency baseCurrency;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchaseOrderID")
    private EdsPurchaseOrder purchaseOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "saleOrderID")
    private EdsSaleQuote saleOrder;

    @Column(name = "startDate")
    private Date startDate;

    @Column(name = "title")
    @Type(type = "text")
    private String title;

    @Column(name = "description")
    @Type(type = "text")
    private String description;

    @Column(name = "basetotal", precision = 25, scale = 5)
    private BigDecimal baseTotal;

    @Column(name = "total", precision = 25, scale = 5)
    private BigDecimal total;

    @Column(name = "taxtotal", precision = 25, scale = 5)
    private BigDecimal taxTotal;

    @Column(name = "isDeleted")
    private Boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorid")
    private EdsUser creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pdftemplateid")
    private EdsCompanyPdfTemplate pdfTemplate;

    @Column(name = "creationTime")
    private Date creationTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updaterid")
    private EdsUser updater;

    @Column(name = "lastUpdateTime")
    private Date lastUpdateTime;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportId")
    @OrderBy("objectID")
    @Where(clause = "(isDeleted = 'false' or isDeleted is null)")
    private List<EdsExpense> expenses = new LinkedList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "expenseReportId")
    @Where(clause = "(deleted = 'false' or deleted is null)")
    private List<EdsExpensePayment> payments = new LinkedList<>();

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "expenseid")
    @Where(clause = "(deleted = 'false' or deleted is null)")
    private List<EdsInvoicePayment> prePayments = new LinkedList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private EdsInvoiceCustomFields customFields;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplierid")
    private EdsCrmAccount supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payableaccount_id")
    private EdsAccount payableAccount;

    @Type(type = "text")
    private String purpose;

    @Type(type = "text")
    private String place;

    private Integer emailTemplateID;

    private Integer payslipID;

    private Integer payslipTableItemID;

    private Integer paymentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountID")
    private EdsAccount account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currencyID")
    @ForeignKey(name = "none")
    private EdsCurrency currency;

    @Column(name = "exchageRate", precision = 25, scale = 10)
    private BigDecimal exchangeRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixedassetid")
    private EdsFixedAsset fixedAsset;

    private Integer taxCalculationType;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "entityID", fetch = FetchType.LAZY)
    @Where(clause = "entityType = 'EXPENSE_CLAIM' AND (deleted = 'false' or deleted is null) ")
    @OrderBy(value = "approverOrder ASC")
    @org.hibernate.annotations.ForeignKey(name = "none")
    private List<EdsApprover> approvers = new ArrayList<>();

    private Date periodStartDate;
    private Date periodEndDate;

    @Column(name = "isCompanyExpense", columnDefinition = "boolean default false")
    private boolean isCompanyExpense = false;

    @Column(name = "tax_treatment")
    private Integer taxtreatmentId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tax_treatment", insertable = false, updatable = false)
    private EdsReference taxTreatment;

    @Column(name = "placeofsupply_gcc_countryid")
    private Integer placeofsupplyGCCCountryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "placeofsupply_gcc_countryid", insertable = false, updatable = false)
    @ForeignKey(name = "none")
    @NotFound(action = NotFoundAction.IGNORE)
    private EdsCountry placeOfSupplyGCCCountry;

    @Column(name = "placeofsupply_id")
    private Integer placeofsupplyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "placeofsupply_id", insertable = false, updatable = false)
    @ForeignKey(name = "none")
    @NotFound(action = NotFoundAction.IGNORE)
    private EdsRegion placeOfSupply;

    @Column(name = "reversecharge_applicable", columnDefinition = "boolean default false")
    private boolean reverseChargeApplicable;

    @Column(name = "vat_return_id")
    private Integer vatReturnId;

    @Transient
    private String rejectionNote;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EdsEmployee getReporter() {
        return reporter;
    }

    public void setReporter(EdsEmployee reporter) {
        this.reporter = reporter;
    }

    public EdsCrmContact getCandidate() {
        return candidate;
    }

    public void setCandidate(EdsCrmContact candidate) {
        this.candidate = candidate;
    }

    public EdsProject getProject() {
        return project;
    }

    public void setProject(EdsProject project) {
        this.project = project;
    }

    public EdsOpportunity getOpportunity() {
        return opportunity;
    }

    public void setOpportunity(EdsOpportunity opportunity) {
        this.opportunity = opportunity;
    }

    public EdsCurrency getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(EdsCurrency baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public EdsReference getStatus() {
        return getOverallStatus();
    }

    @Override
    public void setEntityStatus(EdsReference status) {
        setOverallStatus(status);
    }

    public EdsPurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(EdsPurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public EdsUser getCreator() {
        return creator;
    }

    public void setCreator(EdsUser creator) {
        this.creator = creator;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public EdsUser getUpdater() {
        return updater;
    }

    public void setUpdater(EdsUser updater) {
        this.updater = updater;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
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

    public EdsCompanyPdfTemplate getPdfTemplate() {
        return pdfTemplate;
    }

    public void setPdfTemplate(EdsCompanyPdfTemplate pdfTemplate) {
        this.pdfTemplate = pdfTemplate;
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
    public boolean isCurrentApproverApproved() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && EXPENSE_APPROVED.equals(getCurrentApprover().getStatus().getCode());
    }

    @Override
    public boolean isCurrentApproverRejected() {
        return isOk(getCurrentApprover()) && isOk(getCurrentApprover().getStatus()) && EXPENSE_DECLINED.equals(getCurrentApprover().getStatus().getCode());
    }

    @Override
    protected EdsReference getStatusByMarkedAction(Integer actionID) {
        if (!isOk(actionID)) {
            return null;
        }
        ReferenceManager referenceManager = StaticContextAccessor.getBean(ReferenceManager.class);
        if (actionID.equals(ApproverItem.MARK_AS_REJECTED)) {
            return referenceManager.findReference(Constants.EXPENSE_STATUS, EXPENSE_DECLINED);
        } else if (actionID.equals(ApproverItem.MARK_AS_APPROVED)) {
            return referenceManager.findReference(Constants.EXPENSE_STATUS, EXPENSE_APPROVED);
        } else if (actionID.equals(ApproverItem.SEND_TO_CREATOR)) {
            return referenceManager.findReference(Constants.EXPENSE_STATUS, EXPENSE_DECLINED);
        } else if (actionID.equals(ApproverItem.SEND_TO_DIRECTORS)) {
            return referenceManager.findReference(Constants.EXPENSE_STATUS, EXPENSE_DECLINED);
        }
        return null;
    }

    @Override
    public void updateRejectedStatus() {
        if (getStatus() != null && EXPENSE_DECLINED.equals(getStatus().getCode())) {
            ReferenceManager referenceManager = StaticContextAccessor.getBean(ReferenceManager.class);
            setEntityStatus(referenceManager.findReference(Constants.EXPENSE_STATUS, Constants.EXPENSE_SUBMITTED));
        }
    }

    public ExpenseReportsListItem createExpenseReportListItem() {

        ExpenseReportsListItem item = new ExpenseReportsListItem();

        item.setId(getObjectID());
        item.setTitle(getTitle());
        item.setDescription(getDescription());
        if (getAccount() != null) {
            item.setPaymentAccount(getAccount().getAsSelectItem());
        }
        if (reporter != null) {
            item.setReporterId(reporter.getObjectID());
            item.setReporterName(reporter.getName());
        }
        if (candidate != null) {
            item.setCandidate(true);
            item.setReporterId(candidate.getObjectID());
            item.setReporterName(candidate.getName());
        }
        initApproverData(item);

        if (project != null) {
            if (StringUtils.isNotBlank(project.getNumber())) {
                item.setProject(new SelectItem(project.getObjectID(), project.getNumber() + " -> " + project.getName()));
                item.setProjectName(project.getNumber() + " -> " + project.getName());
            } else {
                item.setProject(new SelectItem(project.getObjectID(), project.getName()));
                item.setProjectName(project.getName());
            }
            item.setProjectStatusCode(project.getStatus().getCode());
        }
        if (opportunity != null) {
            if (StringUtils.isNotBlank(opportunity.getNumber())) {
                item.setOpportunity(new SelectItem(opportunity.getObjectID(), opportunity.getNumber() + " -> " + opportunity.getName()));
                item.setOpportunityName(opportunity.getNumber() + " -> " + opportunity.getName());
            } else {
                item.setOpportunity(new SelectItem(opportunity.getObjectID(), opportunity.getName()));
                item.setOpportunityName(opportunity.getName());
            }
        }
        if (purchaseOrder != null) {
            item.setPurchaseOrder(new SelectItem(purchaseOrder.getObjectID(), purchaseOrder.getNumber()));
        }
        if (payableAccount != null) {
            item.setPayableAccount(payableAccount.createAccountItem());
        }
        if (baseCurrency != null) {
            Integer id = baseCurrency.getObjectID();
            String name = baseCurrency.getName();
            String symbol = baseCurrency.getSymbol();
            item.setBaseCurrency(new CurrencyItem(id, name, symbol));
        }
        if (currency != null) {
            item.setExpenseCurrency(currency.createCurrencyItem());
        }
        item.setExchangeRate(exchangeRate);
        if (getStatus() != null) {
            item.setStatusCode(getStatus().getCode());
            item.setStatusID(getStatus().getObjectID());
            item.setStatusColor(getStatus().getColor());
        }

        item.setStartDate(startDate == null ? null : new DateNonConvertable(startDate));
        item.setPeriodStartDate(periodStartDate == null ? null : new DateNonConvertable(periodStartDate));
        item.setPeriodEndDate(periodEndDate == null ? null : new DateNonConvertable(periodEndDate));

        item.setTaxTotal(getTaxTotal());
        item.setTotal(getTotal());

        item.setCompanyExpense(isCompanyExpense());

        return item;
    }

    public ExpenseReportsSolrItem getSolrRPC() {
        ExpenseReportsSolrItem item = new ExpenseReportsSolrItem();

        item.setObjectID(getObjectID());
        item.setTitle(getTitle() == null ? "" : getTitle());
        item.setStartDate(getStartDate());

        if (getNumber() != null && !"".equals(getNumber())) {
            item.setNumbering(getNumber());
        }

        if (getProject() != null) {
            SelectItem relatedProject = new SelectItem(getProject().getObjectID(), getProject().getName());
            relatedProject.setCode(getProject().getNumber());
            item.setRelatedProject(relatedProject);
        }

        if (!getProjects().isEmpty()) {
            getProjects().forEach(edsProject -> {
                SelectItem multiProject = new SelectItem(edsProject.getObjectID(), edsProject.getName());
                multiProject.setCode(edsProject.getNumber());
                item.getMultiProject().add(multiProject);
            });
        }

        if (getReporter() != null) {
            item.setReporter(getReporter().getAsSelectItem());
        }

        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
            item.setApprover(getCurrentApprover().getExactEmployee().getAsSelectItem());
        }

        if (getStatus() != null) {
            SelectItem status = new SelectItem(getStatus().getObjectID(), getStatus().getName());
            status.setCode(getStatus().getCode());
            item.setStatus(status);
        }

        if (getTotal() != null) {
            item.setOrginalAmount(getTotal().doubleValue());
        }

        if (getCurrency() != null) {
            item.setCurrency(getCurrency().getAsSelectItem());
        }

        if (getSupplier() != null) {
            item.setSupplier(getSupplier().getAsSelectItem());
            if (!getSupplier().getOwners().isEmpty()) {
                getSupplier().getOwners().forEach(o -> item.getSupplierOwnerIds().add(o.getObjectID()));
            }
        }

        List<EdsExpensePayment> expensePayments = getPayments();
        BigDecimal paidAmount = AccountingConstants.ZERO;
        for (EdsExpensePayment payment : expensePayments) {
            if (!payment.isDeleted()) {
                paidAmount = paidAmount.add(payment.getAmountInEntityCurrency() != null ? payment.getAmountInEntityCurrency() : payment.getAmount());
            }
        }

        item.setPaidAmount(paidAmount.doubleValue());
        item.setDueAmount(getTotal() != null ? getTotal().subtract(paidAmount).doubleValue() : 0d);
        item.setTaxAmount(getTaxTotal() != null ? getTaxTotal().doubleValue() : 0d);
        item.setCompanyExpense(isCompanyExpense());

        if (getPrevApprover() != null) {
            item.setPreviousApprover(getPrevApprover().getAsSelectItem());
            item.setPreviousApproverStatus(getPrevApprover().getStatus() != null ? getPrevApprover().getStatus().getAsSelectItem() : null);
            item.setPreviousApproverExactEmployee(getPrevApprover().getExactEmployee() != null ? getPrevApprover().getExactEmployee().getAsSelectItem() : null);
        }

        if (getCurrentApprover() != null) {
            item.setCurrentApprover(getCurrentApprover().getExactEmployee().getAsSelectItem());
            item.setCurrentApproverStatus(getCurrentApprover().getStatus() != null ? getCurrentApprover().getStatus().getAsSelectItem() : null);
            item.setCurrentApproverExactEmployee(getCurrentApprover().getExactEmployee() != null ? getCurrentApprover().getExactEmployee().getAsSelectItem() : null);
        }

        if (getOverallStatus() != null) {
            item.setOverallStatus(getOverallStatus().getAsSelectItem());
        }

        return item;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getTotal() {
        if (total != null) {
            return total;
        }
        BigDecimal total = AccountingConstants.ZERO;
        for (EdsExpense item : expenses) {

            total = total.add(item.getSubtotal());
        }

        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getTaxTotal() {
        if (taxTotal != null) {
            return taxTotal;
        }
        BigDecimal taxTotal = AccountingConstants.ZERO;
        for (EdsExpense item : expenses) {
            if (item.getTaxAmount() != null) {
                taxTotal = taxTotal.add(item.getTaxAmount());
            }
            if (item.getDoubleTaxAmount() != null) {
                taxTotal = taxTotal.add(item.getDoubleTaxAmount());
            }
        }
        return taxTotal;
    }

    public void setTaxTotal(BigDecimal taxTotal) {
        this.taxTotal = taxTotal;
    }

    public BigDecimal getBaseTotal() {
        if (baseTotal != null) {
            return baseTotal;
        }
        BigDecimal baseTotal = AccountingConstants.ZERO;
        for (EdsExpense item : expenses) {
            baseTotal = baseTotal.add(item.getBaseSubtotal());
        }
        return baseTotal;
    }

    public void setBaseTotal(BigDecimal baseTotal) {
        this.baseTotal = baseTotal;
    }

    public BigDecimal getPaidTotal(Date startDate, Date endDate) {
        BigDecimal paidTotal = AccountingConstants.ZERO;
        for (EdsExpensePayment item : getPayments()) {
            if ((item.getPaymentDate().after(startDate) || item.getPaymentDate().getTime() == startDate.getTime())
                    && (item.getPaymentDate().before(endDate) || item.getPaymentDate().getTime() == endDate.getTime())) {
                paidTotal = paidTotal.add(item.getAmount());
            }
        }
        for (EdsInvoicePayment item : getPrePayments()) {
            if ((item.getPaymentDate().after(startDate) || item.getPaymentDate().getTime() == startDate.getTime())
                    && (item.getPaymentDate().before(endDate) || item.getPaymentDate().getTime() == endDate.getTime())) {
                paidTotal = paidTotal.add(item.getAmount());
            }
        }
        return paidTotal;
    }

    public BigDecimal getPaidTotal(boolean inBase) {
        BigDecimal paidTotal = AccountingConstants.ZERO;
        for (EdsExpensePayment item : getPayments()) {
            BigDecimal exRate = item.getExchangeRate() != null && item.getExchangeRate().compareTo(BigDecimal.ZERO) != 0 ? item.getExchangeRate() : BigDecimal.ONE;
            if (inBase)
                paidTotal = paidTotal.add(item.getAmount().divide(exRate, ServerUtils.getSystemCalculationScale(), BigDecimal.ROUND_HALF_UP));
            else paidTotal = paidTotal.add(item.getAmount());
        }
        for (EdsInvoicePayment item : getPrePayments()) {
            BigDecimal exRate = item.getExchangeRate() != null && item.getExchangeRate().compareTo(BigDecimal.ZERO) != 0 ? item.getExchangeRate() : BigDecimal.ONE;
            if (inBase)
                paidTotal = paidTotal.add(item.getAmount().divide(exRate, ServerUtils.getSystemCalculationScale(), BigDecimal.ROUND_HALF_UP));
            else paidTotal = paidTotal.add(item.getAmount());
        }
        return paidTotal;
    }

    public BigDecimal getPaidTotalByPayslip(Integer singlePayrunId, boolean inBase) {
        BigDecimal paidTotal = AccountingConstants.ZERO;
        for (EdsExpensePayment item : getPayments()) {
            if (item.getPayslipTableItem() != null && item.getPayslipTableItem().getObjectID() == singlePayrunId) {
                BigDecimal exRate = item.getExchangeRate() != null && item.getExchangeRate().compareTo(BigDecimal.ZERO) != 0 ? item.getExchangeRate() : BigDecimal.ONE;
                if (inBase)
                    paidTotal = paidTotal.add(item.getAmount().divide(exRate, ServerUtils.getSystemCalculationScale(), BigDecimal.ROUND_HALF_UP));
                else paidTotal = paidTotal.add(item.getAmount());
            }
        }
        return paidTotal;
    }

    public BigDecimal getPaymentShare(Date startDate, Date endDate) {
        return getPaidTotal(startDate, endDate).divide(getBaseTotal(), 10, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getCashTotal() {
        BigDecimal baseTotal = AccountingConstants.ZERO;
        for (EdsExpense item : expenses) {
            if (item.getCashOrCardType() == null || CASH_TYPE.equals(item.getCashOrCardType()))
                baseTotal = baseTotal.add(item.getBaseSubtotal());
        }
        return baseTotal;
    }

    public BigDecimal getCreditCardTotal() {
        BigDecimal baseTotal = AccountingConstants.ZERO;
        for (EdsExpense item : expenses) {
            if (MASTER_CARD_TYPE.equals(item.getCashOrCardType()))
                baseTotal = baseTotal.add(item.getBaseSubtotal());
        }
        return baseTotal;
    }

    public Set<EdsProject> getProjects() {
        Set<EdsProject> projects = new HashSet<>();
        if (getExpenses() != null && !getExpenses().isEmpty()) {
            for (EdsExpense item : getExpenses()) {
                if (item.getProject() != null) {
                    projects.add(item.getProject());
                }
            }
        }
        return projects;
    }

    public Boolean getDeleted() {
        return isDeleted == null ? Boolean.FALSE : isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public List<EdsExpense> getExpenses() {
        return expenses;
    }

    public void setExpenses(List<EdsExpense> expenses) {
        this.expenses = expenses;
    }

    public List<EdsExpensePayment> getPayments() {
        return payments;
    }

    public void setPayments(List<EdsExpensePayment> payments) {
        this.payments = payments;
    }

    public EdsInvoiceCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsInvoiceCustomFields customFields) {
        this.customFields = customFields;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Integer getEmailTemplateID() {
        return emailTemplateID;
    }

    public void setEmailTemplateID(Integer emailTemplateID) {
        this.emailTemplateID = emailTemplateID;
    }

    public Integer getPayslipID() {
        return payslipID;
    }

    public void setPayslipID(Integer payslipID) {
        this.payslipID = payslipID;
    }

    public Integer getPayslipTableItemID() {
        return payslipTableItemID;
    }

    public void setPayslipTableItemID(Integer payslipTableItemID) {
        this.payslipTableItemID = payslipTableItemID;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public EdsAccount getAccount() {
        return account;
    }

    public void setAccount(EdsAccount account) {
        this.account = account;
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

    public String getRejectionNote() {
        return rejectionNote;
    }

    public void setRejectionNote(String rejectionNote) {
        this.rejectionNote = rejectionNote;
    }

    public EdsFixedAsset getFixedAsset() {
        return fixedAsset;
    }

    public void setFixedAsset(EdsFixedAsset fixedAsset) {
        this.fixedAsset = fixedAsset;
    }

    public Integer getTaxCalculationType() {
        return taxCalculationType;
    }

    public void setTaxCalculationType(Integer taxCalculationType) {
        this.taxCalculationType = taxCalculationType;
    }

    public EdsCrmAccount getSupplier() {
        return supplier;
    }

    public void setSupplier(EdsCrmAccount supplier) {
        this.supplier = supplier;
    }

    public Date getPeriodStartDate() {
        return periodStartDate;
    }

    public void setPeriodStartDate(Date periodStartDate) {
        this.periodStartDate = periodStartDate;
    }

    public Date getPeriodEndDate() {
        return periodEndDate;
    }

    public void setPeriodEndDate(Date periodEndDate) {
        this.periodEndDate = periodEndDate;
    }

    public boolean isCompanyExpense() {
        return isCompanyExpense;
    }

    public void setCompanyExpense(boolean companyExpense) {
        isCompanyExpense = companyExpense;
    }

    public Integer getVatReturnId() {
        return vatReturnId;
    }

    public void setVatReturnId(Integer vatReturnId) {
        this.vatReturnId = vatReturnId;
    }

    public Integer getTaxtreatmentId() {
        return taxtreatmentId;
    }

    public void setTaxtreatmentId(Integer taxtreatmentId) {
        this.taxtreatmentId = taxtreatmentId;
    }

    public EdsReference getTaxTreatment() {
        return taxTreatment;
    }

    public void setTaxTreatment(EdsReference taxTreatment) {
        this.taxTreatment = taxTreatment;
    }

    public Integer getPlaceofsupplyGCCCountryId() {
        return placeofsupplyGCCCountryId;
    }

    public void setPlaceofsupplyGCCCountryId(Integer placeofsupplyGCCCountryId) {
        this.placeofsupplyGCCCountryId = placeofsupplyGCCCountryId;
    }

    public EdsCountry getPlaceOfSupplyGCCCountry() {
        return placeOfSupplyGCCCountry;
    }

    public void setPlaceOfSupplyGCCCountry(EdsCountry placeOfSupplyGCCCountry) {
        this.placeOfSupplyGCCCountry = placeOfSupplyGCCCountry;
    }

    public Integer getPlaceofsupplyId() {
        return placeofsupplyId;
    }

    public void setPlaceofsupplyId(Integer placeofsupplyId) {
        this.placeofsupplyId = placeofsupplyId;
    }

    public EdsRegion getPlaceOfSupply() {
        return placeOfSupply;
    }

    public void setPlaceOfSupply(EdsRegion placeOfSupply) {
        this.placeOfSupply = placeOfSupply;
    }

    public boolean isReverseChargeApplicable() {
        return reverseChargeApplicable;
    }

    public void setReverseChargeApplicable(boolean reverseChargeApplicable) {
        this.reverseChargeApplicable = reverseChargeApplicable;
    }

    public EdsAccount getPayableAccount() {
        return payableAccount;
    }

    public void setPayableAccount(EdsAccount payableAccount) {
        this.payableAccount = payableAccount;
    }

    public SolrInputDocument wrapToSolrDocument() {
        SolrInputDocument doc = new SolrInputDocument();
        doc.addField(SolrExpenseReportRepresenter.FIELD_COMPANY_ID, SecurityContext.getCompanyID());
        doc.addField(SolrExpenseReportRepresenter.FIELD_COMPOSITE_ID, SecurityContext.getCompanyID() + "_" + getObjectID());
        doc.addField(SolrExpenseReportRepresenter.FIELD_REPORT_ID, getObjectID());

        doc.addField(SolrExpenseReportRepresenter.FIELD_TITLE, getTitle() == null ? "" : getTitle());
        doc.addField(SolrExpenseReportRepresenter.FIELD_START_DATE, getStartDate());

        if (getNumber() != null && !"".equals(getNumber())) {
            doc.addField(SolrExpenseReportRepresenter.FIELD_NUMBERING, getNumber());
        }

        if (getProject() != null) {
            doc.addField(SolrExpenseReportRepresenter.FIELD_RELATED_PROJECT_ID, getProject().getObjectID());
            doc.addField(SolrExpenseReportRepresenter.FIELD_RELATED_PROJECT_NAME, getProject().getName());
            doc.addField(SolrExpenseReportRepresenter.FIELD_RELATED_PROJECT_NUMBER, getProject().getNumber());
            doc.addField(SolrExpenseReportRepresenter.FIELD_RELATED_PROJECT_NUMBER_NAME, project.getNumber() + SolrExpenseReportRepresenter.ARROW + project.getName());
            doc.addField(SolrExpenseReportRepresenter.FIELD_RELATED_PROJECT_ID_NAME, getProject().getObjectID() + SolrExpenseReportRepresenter.SPLIT + getProject().getNumber() + " - " + getProject().getName());
        }

        for (EdsProject project : getProjects()) {
            doc.addField(SolrExpenseReportRepresenter.FIELD_MULTI_PROJECT_ID, project.getObjectID());
            doc.addField(SolrExpenseReportRepresenter.FIELD_MULTI_PROJECT_NAME, project.getName());
            doc.addField(SolrExpenseReportRepresenter.FIELD_MULTI_PROJECT_NUMBER, project.getNumber());
            doc.addField(SolrExpenseReportRepresenter.FIELD_MULTI_PROJECT_ID_NAME, project.getObjectID() + SolrExpenseReportRepresenter.SPLIT + project.getName());
            doc.addField(SolrExpenseReportRepresenter.FIELD_MULTI_PROJECT_NUMBER_NAME, project.getNumber() + SolrExpenseReportRepresenter.ARROW + project.getName());
        }

        if (getReporter() != null) {
            doc.addField(SolrExpenseReportRepresenter.FIELD_REPORTER_ID, getReporter().getObjectID());
            doc.addField(SolrExpenseReportRepresenter.FIELD_REPORTER_NAME, getReporter().getName());
            doc.addField(SolrExpenseReportRepresenter.FIELD_REPORTER_ID_NAME, getReporter().getObjectID() + SolrExpenseReportRepresenter.SPLIT + getReporter().getName());
        }

        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
            doc.addField(SolrExpenseReportRepresenter.FIELD_APPROVER_ID, getCurrentApprover().getExactEmployee().getObjectID());
            doc.addField(SolrExpenseReportRepresenter.FIELD_APPROVER_NAME, getCurrentApprover().getExactEmployee().getFullName());
            doc.addField(SolrExpenseReportRepresenter.FIELD_APPROVER_ID_NAME, getCurrentApprover().getExactEmployee().getObjectID() + SolrExpenseReportRepresenter.SPLIT + getCurrentApprover().getExactEmployee().getFullName());
        }

        if (getStatus() != null) {
            doc.addField(SolrExpenseReportRepresenter.FIELD_STATUS_ID, getStatus().getObjectID());
            doc.addField(SolrExpenseReportRepresenter.FIELD_STATUS_CODE, getStatus().getCode());
            doc.addField(SolrExpenseReportRepresenter.FIELD_STATUS_NAME, getStatus().getName());
            doc.addField(SolrExpenseReportRepresenter.FIELD_STATUS_ID_NAME, getStatus().getObjectID() + SolrExpenseReportRepresenter.SPLIT + getStatus().getName());
        }

        if (getFixedAsset() != null) {
            doc.addField(SolrExpenseReportRepresenter.FIELD_FIXED_ASSET_ID, getFixedAsset().getObjectID());
            doc.addField(SolrExpenseReportRepresenter.FIELD_FIXED_ASSET_NAME, getFixedAsset().getName());
            doc.addField(SolrExpenseReportRepresenter.FIELD_FIXED_ASSET_ID_NAME, getFixedAsset().getObjectID() + getFixedAsset().getName());
        }

        if (getTotal() != null) {
            doc.addField(SolrExpenseReportRepresenter.FIELD_ORIGINAL_AMOUNT, getTotal().doubleValue());
        }
        if (getCurrency() != null) {
            doc.addField(SolrExpenseReportRepresenter.FIELD_CURRENCY_ID, getCurrency().getObjectID());
            doc.addField(SolrExpenseReportRepresenter.FIELD_CURRENCY_NAME, getCurrency().getName());
            doc.addField(SolrExpenseReportRepresenter.FIELD_CURRENCY_ID_NAME, getCurrency().getObjectID() + SolrExpenseReportRepresenter.SPLIT + getCurrency().getName());
        }

        if (getSupplier() != null) {
            doc.addField(SolrExpenseReportRepresenter.FIELD_SUPPLIER_ID, getSupplier().getObjectID());
            doc.addField(SolrExpenseReportRepresenter.FIELD_SUPPLIER_NAME, getSupplier().getName());
            doc.addField(SolrExpenseReportRepresenter.FIELD_SUPPLIER_ID_NAME, getSupplier().getObjectID() + SolrExpenseReportRepresenter.SPLIT + getSupplier().getName());
        }

        List<EdsExpensePayment> expensePayments = getPayments();
        BigDecimal paidAmount = AccountingConstants.ZERO;
        for (EdsExpensePayment payment : expensePayments) {
            if (!payment.isDeleted()) {
                paidAmount = paidAmount.add(payment.getAmountInEntityCurrency() != null ? payment.getAmountInEntityCurrency() : payment.getAmount());
            }
        }

        List<EdsInvoicePayment> prePayments = getPrePayments();
        for (EdsInvoicePayment payment : prePayments) {
            if (!payment.isDeleted() && (payment.getStatus() == null || (payment.getStatus() != null && !payment.getStatus().getCode().equals(REVERSED)))) {
                paidAmount = paidAmount.add(payment.getAmountInInvoiceCurrency() != null ? payment.getAmountInInvoiceCurrency() : payment.getAmount());
            }
        }

        doc.addField(SolrExpenseReportRepresenter.FIELD_PAID_AMOUNT, paidAmount.doubleValue());
        doc.addField(SolrExpenseReportRepresenter.FIELD_DUE_AMOUNT, getTotal() != null ? getTotal().subtract(paidAmount).doubleValue() : 0d);
        doc.addField(SolrExpenseReportRepresenter.FIELD_TAX_AMOUNT, getTaxTotal() != null ? getTaxTotal().doubleValue() : 0d);

        doc.addField(SolrExpenseReportRepresenter.FIELD_IS_COMPANY_EXPENSE, isCompanyExpense());

        CustomFieldsUtils.setInSolrCustomFields(doc, getCustomFields());
        List<EdsApprover> approvers = getApprovers();
        approvers.sort(Comparator.comparing(EdsApprover::getApproverOrder));

        for (EdsApprover edsApprover : approvers) {
            doc.addField(SolrExpenseReportRepresenter.DYNAMIC_FIELD_APPROVER_ID + edsApprover.getApproverOrder(), edsApprover.getObjectID());
            doc.addField(SolrExpenseReportRepresenter.DYNAMIC_FIELD_APPROVER_NAME + edsApprover.getApproverOrder(), edsApprover.getName());
            doc.addField(SolrExpenseReportRepresenter.DYNAMIC_FIELD_APPROVER_ID_NAME + edsApprover.getApproverOrder(), edsApprover.getObjectID() + SolrExpenseReportRepresenter.SPLIT + edsApprover.getName());
            doc.addField(SolrExpenseReportRepresenter.DYNAMIC_FIELD_APPROVER_STATUS_ID + edsApprover.getApproverOrder(), edsApprover.getStatus() != null ? edsApprover.getStatus().getObjectID() : null);
            doc.addField(SolrExpenseReportRepresenter.DYNAMIC_FIELD_APPROVER_STATUS_CODE + edsApprover.getApproverOrder(), edsApprover.getStatus() != null ? edsApprover.getStatus().getCode() : "");
            doc.addField(SolrExpenseReportRepresenter.DYNAMIC_FIELD_APPROVER_EXACT_EMPLOYEE_ID + edsApprover.getApproverOrder(), edsApprover.getExactEmployee() != null ? edsApprover.getExactEmployee().getObjectID() : null);
            doc.addField(SolrExpenseReportRepresenter.DYNAMIC_FIELD_APPROVER_EXACT_EMPLOYEE_NAME + edsApprover.getApproverOrder(), edsApprover.getExactEmployee() != null ? edsApprover.getExactEmployee().getName() : "");
        }
        if (getPrevApprover() != null) {
            doc.addField(SolrExpenseReportRepresenter.DYNAMIC_FIELD_PREVIOUS_APPROVER_ID, getPrevApprover().getObjectID());
            doc.addField(SolrExpenseReportRepresenter.DYNAMIC_FIELD_PREVIOUS_APPROVER_NAME, getPrevApprover().getName());
            doc.addField(SolrExpenseReportRepresenter.DYNAMIC_FIELD_PREVIOUS_APPROVER_ID_NAME, getPrevApprover().getObjectID() + SolrExpenseReportRepresenter.SPLIT + getPrevApprover().getName());
            doc.addField(SolrExpenseReportRepresenter.DYNAMIC_FIELD_PREVIOUS_APPROVER_STATUS_ID, getPrevApprover().getStatus() != null ? getPrevApprover().getStatus().getObjectID() : null);
            doc.addField(SolrExpenseReportRepresenter.DYNAMIC_FIELD_PREVIOUS_APPROVER_STATUS_CODE, getPrevApprover().getStatus() != null ? getPrevApprover().getStatus().getCode() : "");
            doc.addField(SolrExpenseReportRepresenter.DYNAMIC_FIELD_PREVIOUS_APPROVER_EXACT_EMPLOYEE_ID, getPrevApprover().getExactEmployee() != null ? getCurrentApprover().getExactEmployee().getObjectID() : null);
            doc.addField(SolrExpenseReportRepresenter.DYNAMIC_FIELD_PREVIOUS_APPROVER_EXACT_EMPLOYEE_NAME, getPrevApprover().getExactEmployee() != null ? getCurrentApprover().getExactEmployee().getName() : "");
        }
        if (getCurrentApprover() != null) {
            doc.addField(SolrExpenseReportRepresenter.DYNAMIC_FIELD_CURRENT_APPROVER_ID, getCurrentApprover().getObjectID());
            doc.addField(SolrExpenseReportRepresenter.DYNAMIC_FIELD_CURRENT_APPROVER_NAME, getCurrentApprover().getName());
            doc.addField(SolrExpenseReportRepresenter.DYNAMIC_FIELD_CURRENT_APPROVER_ID_NAME, getCurrentApprover().getObjectID() + SolrExpenseReportRepresenter.SPLIT + getCurrentApprover().getName());
            doc.addField(SolrExpenseReportRepresenter.DYNAMIC_FIELD_CURRENT_APPROVER_STATUS_ID, getCurrentApprover().getStatus() != null ? getCurrentApprover().getStatus().getObjectID() : null);
            doc.addField(SolrExpenseReportRepresenter.DYNAMIC_FIELD_CURRENT_APPROVER_STATUS_CODE, getCurrentApprover().getStatus() != null ? getCurrentApprover().getStatus().getCode() : "");
            doc.addField(SolrExpenseReportRepresenter.DYNAMIC_FIELD_CURRENT_APPROVER_EXACT_EMPLOYEE_ID, getCurrentApprover().getExactEmployee() != null ? getCurrentApprover().getExactEmployee().getObjectID() : null);
            doc.addField(SolrExpenseReportRepresenter.DYNAMIC_FIELD_CURRENT_APPROVER_EXACT_EMPLOYEE_NAME, getCurrentApprover().getExactEmployee() != null ? getCurrentApprover().getExactEmployee().getName() : "");
        }
        if (getOverallStatus() != null) {
            doc.addField(SolrExpenseReportRepresenter.DYNAMIC_FIELD_OVERALL_STATUS_ID, getOverallStatus().getObjectID());
            doc.addField(SolrExpenseReportRepresenter.DYNAMIC_FIELD_OVERALL_STATUS_NAME, getOverallStatus().getName());
            doc.addField(SolrExpenseReportRepresenter.DYNAMIC_FIELD_OVERALL_STATUS_CODE, getOverallStatus().getCode());
        }

        return doc;
    }

    @Override
    public Object getRealValue(String fieldID) {
        if (fieldID == null) {
            return null;
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.EXPENSE_CLAIM.DATE)) {
            return getStartDate();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.EXPENSE_CLAIM.REPORTER)) {
            return getReporter();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.EXPENSE_CLAIM.REPORT_TITLE)) {
            return getTitle();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.EXPENSE_CLAIM.FIXED_ASSET)) {
            return getFixedAsset();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.EXPENSE_CLAIM.DESCRIPTION)) {
            return getDescription();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.EXPENSE_CLAIM.NUMBER)) {
            return getNumber();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.EXPENSE_CLAIM.PROJECT)) {
            return getProject();
        } else if (fieldID.equals(CustomFormConstants.ACCOUNTING.EXPENSE_CLAIM.TOTAL)) {
            return getTotal();
        } else if (fieldID.equals(CustomFormConstants.STATUS)) {
            return getStatus();
        } else if (fieldID.equals(CustomFormConstants.NEXT_APPROVER_STATUS)) {
            return getNextApprover() != null ? getNextApprover().getStatus() : "";
        }
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

    public EdsSaleQuote getSaleOrder() {
        return saleOrder;
    }

    public void setSaleOrder(EdsSaleQuote saleOrder) {
        this.saleOrder = saleOrder;
    }
    public List<EdsInvoicePayment> getPrePayments() {
        return prePayments;
    }

    public void setPrePayments(List<EdsInvoicePayment> prePayments) {
        this.prePayments = prePayments;
    }
}
