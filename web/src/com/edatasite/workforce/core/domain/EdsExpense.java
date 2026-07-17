package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseOrder;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.customfields.EdsExpenseItemCustomFields;
import com.edatasite.workforce.gwt.accounting.client.rpc.BillableExpenseItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.expenses.client.rpc.ExpenseListItem;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 18.10.2008
 * Time: 16:37:39
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "expense")
public class EdsExpense extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "reportId")
    private EdsExpenseReport report;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    private EdsExpenseCategory category;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeId")
    private EdsEmployee employee;

    @Column(name = "glCode")
    private String GLCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId")
    private EdsAccount account;

    @Column(name = "description")
    @Type(type = "text")
    private String description;

    @Column(name = "date")
    private Date date;

    @Column(name = "units", precision = 25, scale = 5)
    private BigDecimal units;

    @Column(name = "costPerUnit", precision = 25, scale = 5)
    private BigDecimal costPerUnit;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "currencyId")
    @ForeignKey(name = "none")
    private EdsCurrency currency;

    @Column(name = "exchageRate", precision = 25, scale = 15)
    private BigDecimal exchageRate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taxId")
    private EdsVat tax;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doubletax_id")
    private EdsVat doubleTax;

    private BigDecimal taxAmount;

    private BigDecimal doubleTaxAmount;

    @Column(name = "subtotal", precision = 25, scale = 5)
    private BigDecimal subtotal;

    @Column(name = "basesubtotal", precision = 25, scale = 5)
    private BigDecimal baseSubtotal;

    @Column(name = "markupAmount", precision = 25, scale = 5)
    private BigDecimal markupAmount;

    @Column(name = "markupTaxAmount", precision = 25, scale = 5)
    private BigDecimal markupTaxAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "markuptaxId")
    private EdsVat markupTax;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "markupAccountId")
    private EdsAccount markupAccount;

    private Integer cashOrCardType;

    @Column(name = "isDeleted")
    private Boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private EdsCrmAccount client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private EdsDepartment department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private EdsProject project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private EdsInvoice invoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchaseOrderID")
    private EdsPurchaseOrder purchaseOrder;

    @Column(columnDefinition = "boolean default false")
    private Boolean isProjectBase = Boolean.FALSE;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private EdsExpenseItemCustomFields customFields;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsExpenseReport getReport() {
        return report;
    }

    public void setReport(EdsExpenseReport report) {
        this.report = report;
    }

    public EdsExpenseCategory getCategory() {
        return category;
    }

    public void setCategory(EdsExpenseCategory category) {
        this.category = category;
    }

    public EdsEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(EdsEmployee employee) {
        this.employee = employee;
    }

    public String getGLCode() {
        return GLCode;
    }

    public void setGLCode(String GLCode) {
        this.GLCode = GLCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getUnits() {
        return units;
    }

    public void setUnits(BigDecimal units) {
        this.units = units;
    }

    public BigDecimal getCostPerUnit() {
        return costPerUnit;
    }

    public void setCostPerUnit(BigDecimal costPerUnit) {
        this.costPerUnit = costPerUnit;
    }

    public EdsCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(EdsCurrency currency) {
        this.currency = currency;
    }

    public BigDecimal getExchageRate() {
        return exchageRate;
    }

    public void setExchageRate(BigDecimal exchageRate) {
        this.exchageRate = exchageRate;
    }

    public EdsVat getTax() {
        return tax;
    }

    public void setTax(EdsVat tax) {
        this.tax = tax;
    }

    public EdsVat getDoubleTax() {
        return doubleTax;
    }

    public void setDoubleTax(EdsVat doubleTax) {
        this.doubleTax = doubleTax;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getDoubleTaxAmount() {
        return doubleTaxAmount;
    }

    public void setDoubleTaxAmount(BigDecimal doubleTaxAmount) {
        this.doubleTaxAmount = doubleTaxAmount;
    }

    public EdsProject getProject() {
        return project;
    }

    public void setProject(EdsProject project) {
        this.project = project;
    }

    public ExpenseListItem createExpenseListItem() {
        final ExpenseListItem expenseListItem = new ExpenseListItem();

        expenseListItem.setId(getObjectID());
        if (getCategory() != null) {
            expenseListItem.setCategoryId(getCategory().getObjectID());
            expenseListItem.setCategoryName(getCategory().getName());
        }
        if (getAccount() != null) {
            expenseListItem.setAccountId(getAccount().getObjectID());
            expenseListItem.setAccountName(getAccount().getName());
            expenseListItem.setAccountCode(getAccount().getAccountCode());
        }
        expenseListItem.setDescription(getDescription());
        expenseListItem.setCostPerUnit(getCostPerUnit());
        expenseListItem.setUnits(getUnits());
        expenseListItem.setExchageRate(getExchageRate());
        if (getTax() != null) {
            expenseListItem.setTax(getTax().createTaxItem());
        }
        if (getDoubleTax() != null) {
            expenseListItem.setDoubleTax(getDoubleTax().createTaxItem());
        }
        expenseListItem.setTaxAmountInBase(getTaxAmountInBase(false));
        expenseListItem.setTaxAmountInTc(getTax() != null ? getTaxAmount() : AccountingConstants.ZERO);
        expenseListItem.setDoubleTaxAmountInBase(getTaxAmountInBase(true));
        if (getDate() != null) {
            expenseListItem.setIncurredDate(new Date(getDate().getTime()));
        }
        expenseListItem.setGlCode(getGLCode());
        expenseListItem.setSubtotal(getSubtotal());
        expenseListItem.setBaseSubtotal(getBaseSubtotal());
        expenseListItem.setMarkupAmount(getMarkupAmount());
        expenseListItem.setCashOrCardType(getCashOrCardType());
        if (getCurrency() != null) {
            expenseListItem.setCurrencyId(this.getCurrency().getObjectID());
            expenseListItem.setCurrencyName(this.getCurrency().getName());
        }
        expenseListItem.setProjectBase(getIsProjectBase());
        if (getClient() != null) {
            expenseListItem.setClientId(getClient().getObjectID());
            expenseListItem.setClientName(getClient().getName());
        }
        if (getDepartment() != null) {
            expenseListItem.setDepartment(getDepartment().getAsSelectItem());
        }
        if (getProject() != null) {
            expenseListItem.setProject(getProject().getAsSelectItem());
        }
        if (isAllocatedToPO()) {
            expenseListItem.setPurchaseOrder(new SelectItem(getPurchaseOrder().getObjectID(), getPurchaseOrder().getNumber()));
        }
        final EdsInvoice invoice = this.getInvoice();
        
        if (invoice != null && !invoice.isDeleted()) {
            expenseListItem.setSaleInvoiceId(invoice.getObjectID());
        }
        return expenseListItem;
    }

    public BillableExpenseItem createBillableExpenseItem(boolean...forEdit) {
        BillableExpenseItem beItem = new BillableExpenseItem();
        beItem.setObjectID(getObjectID());
        beItem.setType(BillableExpenseItem.EXPENSE);
        beItem.setDescription(getDescription());
        if (getReport() != null) {
            beItem.setInvoiceId(getReport().getObjectID());
        }
        if (client != null) {
            beItem.setClient(client.getAsSelectItem());
        }
        if (account != null) {
            beItem.setAccount(account.getAsSelectItem());
        }

        beItem.setAmountInCurrency(getSubtotal());
        beItem.setAmountInBase(getBaseSubtotal());
        beItem.setMarkupAmount(getMarkupAmount());
        if (getMarkupTax() != null) {
            beItem.setMarkupTax(getMarkupTax().createTaxItem());
        }

        if (report != null) {
            beItem.setNumber(report.getNumber());
            beItem.setCurrencyID(report.getCurrency() != null ? report.getCurrency().getObjectID() : null);
            beItem.setCurrencyName(report.getCurrency() != null ? report.getCurrency().getName() : null);

            beItem.setMarkupAmountInBase(getMarkupAmount() != null ? getMarkupAmount().divide(report.getExchangeRate(), ServerUtils.getSystemCalculationScale(), BigDecimal.ROUND_HALF_UP) : null);
            beItem.setDate(report.getStartDate());
        }

        if (forEdit != null && forEdit.length > 0) {

            if (getMarkupAccount() != null) {
                beItem.setMarkupAccount(getMarkupAccount().getAsSelectItem());
            }

            beItem.setMarkupTaxAmount(getMarkupTaxAmount());
            beItem.setMarkupTaxAmountInBase(getMarkupTaxAmount() != null ? getMarkupTaxAmount().divide(report.getExchangeRate(), ServerUtils.getSystemCalculationScale(), BigDecimal.ROUND_HALF_UP) : null);
            beItem.setSelected(forEdit[0]);
        }

        return beItem;
    }

    public BigDecimal getNetAmountInBase() {
        return units.multiply(costPerUnit).divide(getReport().getExchangeRate(), ServerUtils.getSystemCalculationScale(), BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getTaxAmountInBase(boolean forDoubleTax) {

        if (forDoubleTax) {

            if (getDoubleTax() != null) {
                return getDoubleTaxAmount().divide(getReport().getExchangeRate(), ServerUtils.getSystemCalculationScale(), BigDecimal.ROUND_HALF_UP);
            }
        } else {

            if (getTax() != null) {
                return getTaxAmount().divide(getReport().getExchangeRate(), ServerUtils.getSystemCalculationScale(), BigDecimal.ROUND_HALF_UP);
            }
        }
        return AccountingConstants.ZERO;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getBaseSubtotal() {
        return (baseSubtotal != null && baseSubtotal.compareTo(BigDecimal.ZERO) != 0) ? baseSubtotal : subtotal.divide(exchageRate != null ? exchageRate : BigDecimal.ONE, 10, BigDecimal.ROUND_HALF_UP);
    }

    public void setBaseSubtotal(BigDecimal baseSubtotal) {
        this.baseSubtotal = baseSubtotal;
    }

    public BigDecimal getMarkupAmount() {
        return markupAmount;
    }

    public void setMarkupAmount(BigDecimal markupAmount) {
        this.markupAmount = markupAmount;
    }

    public EdsVat getMarkupTax() {
        return markupTax;
    }

    public void setMarkupTax(EdsVat markupTax) {
        this.markupTax = markupTax;
    }

    public BigDecimal getMarkupTaxAmount() {
        return markupTaxAmount;
    }

    public void setMarkupTaxAmount(BigDecimal markupTaxAmount) {
        this.markupTaxAmount = markupTaxAmount;
    }

    public EdsAccount getMarkupAccount() {
        return markupAccount;
    }

    public void setMarkupAccount(EdsAccount markupAccount) {
        this.markupAccount = markupAccount;
    }

    public Boolean isAllocatedToPO() {
        return getPurchaseOrder() != null;
    }

    public Integer getCashOrCardType() {
        return cashOrCardType;
    }

    public void setCashOrCardType(Integer cashOrCardType) {
        this.cashOrCardType = cashOrCardType;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public EdsAccount getAccount() {
        return account;
    }

    public void setAccount(EdsAccount account) {
        this.account = account;
    }

    public EdsCrmAccount getClient() {
        return client;
    }

    public void setClient(EdsCrmAccount client) {
        this.client = client;
    }

    public EdsDepartment getDepartment() {
        return department;
    }

    public void setDepartment(EdsDepartment department) {
        this.department = department;
    }

    public EdsInvoice getInvoice() {
        return invoice;
    }

    public void setInvoice(EdsInvoice invoice) {
        this.invoice = invoice;
    }

    public EdsPurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(EdsPurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public Boolean getIsProjectBase() {
        return isProjectBase;
    }

    public void setProjectBase(Boolean projectBase) {
        isProjectBase = projectBase;
    }

    public EdsExpenseItemCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsExpenseItemCustomFields customFields) {
        this.customFields = customFields;
    }
}
