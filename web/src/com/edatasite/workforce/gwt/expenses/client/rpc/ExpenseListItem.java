package com.edatasite.workforce.gwt.expenses.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 21.10.2008
 * Time: 11:54:42
 * To change this template use File | Settings | File Templates.
 */
public class ExpenseListItem implements Serializable {

    private Integer id;
    private Integer categoryId;
    private Integer accountId;
    private String categoryName;
    private String accountName;
    private String accountCode;
    private String description;
    private BigDecimal units;
    private BigDecimal costPerUnit;
    private Integer currencyId;
    private String currencyName;
    private BigDecimal exchageRate;
    private Date incurredDate;
    private String glCode;
    private TaxItem tax;
    private TaxItem doubleTax;
    private BigDecimal taxAmountInBase;//Tax Amount In Base Currency
    private BigDecimal taxAmountInTc;//Tax Amount In Transaction Currency
    private BigDecimal expenseAmountInCurency;//Tax Amount In Base Currency
    private BigDecimal doubleTaxAmountInBase;//Double Tax Amount In Base Currency
    private BigDecimal subtotal;
    private BigDecimal baseSubtotal;
    private BigDecimal markupAmount;
    private BigDecimal markupTaxAmount;
    private TaxItem markupTax;
    private SelectItem markupAccount;
    private Boolean isAllocatedToPO;
    private Integer cashOrCardType;
    private FileResource[] attachments;
    private String expenseReportNumber;

    private Integer clientId;
    private String clientName;

    private SelectItem department;
    private SelectItem project;
    private SelectItem purchaseOrder;
    private boolean isProjectBase;
    private Integer[] projectBasedEntryIds;
    private Date date;
    private Date expenseDate;

    private String type;
    private Integer bankTransferType;
    private Integer saleInvoiceId;
    private Integer reportId;

    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private SelectItem reportReporter;
    private BigDecimal billExpTotal;

    public ExpenseListItem() {
    }

    public FileResource[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileResource[] attachments) {
        this.attachments = attachments;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public BigDecimal getExchageRate() {
        return exchageRate;
    }

    public void setExchageRate(BigDecimal exchageRate) {
        this.exchageRate = exchageRate;
    }

    public Date getIncurredDate() {
        return incurredDate;
    }

    public void setIncurredDate(Date incurredDate) {
        this.incurredDate = incurredDate;
    }

    public String getGlCode() {
        return glCode;
    }

    public void setGlCode(String glCode) {
        this.glCode = glCode;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getBaseSubtotal() {
        return baseSubtotal;
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

    public BigDecimal getMarkupTaxAmount() {
        return markupTaxAmount;
    }

    public void setMarkupTaxAmount(BigDecimal markupTaxAmount) {
        this.markupTaxAmount = markupTaxAmount;
    }

    public TaxItem getMarkupTax() {
        return markupTax;
    }

    public void setMarkupTax(TaxItem markupTax) {
        this.markupTax = markupTax;
    }

    public SelectItem getMarkupAccount() {
        return markupAccount;
    }

    public void setMarkupAccount(SelectItem markupAccount) {
        this.markupAccount = markupAccount;
    }

    public Boolean isAllocatedToPO() {
        return isAllocatedToPO != null ? isAllocatedToPO : false;
    }

    public void setAllocatedToPO(Boolean allocatedToPO) {
        isAllocatedToPO = allocatedToPO;
    }

    public Integer getCashOrCardType() {
        return cashOrCardType;
    }

    public void setCashOrCardType(Integer cashOrCardType) {
        this.cashOrCardType = cashOrCardType;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public TaxItem getTax() {
        return tax;
    }

    public void setTax(TaxItem tax) {
        this.tax = tax;
    }

    public TaxItem getDoubleTax() {
        return doubleTax;
    }

    public void setDoubleTax(TaxItem doubleTax) {
        this.doubleTax = doubleTax;
    }

    public BigDecimal getTaxAmountInBase() {
        return taxAmountInBase;
    }

    public void setTaxAmountInBase(BigDecimal taxAmountInBase) {
        this.taxAmountInBase = taxAmountInBase;
    }

    public BigDecimal getTaxAmountInTc() {
        return taxAmountInTc;
    }

    public void setTaxAmountInTc(BigDecimal taxAmountInTc) {
        this.taxAmountInTc = taxAmountInTc;
    }

    public BigDecimal getDoubleTaxAmountInBase() {
        return doubleTaxAmountInBase;
    }

    public void setDoubleTaxAmountInBase(BigDecimal doubleTaxAmountInBase) {
        this.doubleTaxAmountInBase = doubleTaxAmountInBase;
    }

    public BigDecimal getBillExpTotal() {
        return billExpTotal;
    }

    public void setBillExpTotal(BigDecimal billExpTotal) {
        this.billExpTotal = billExpTotal;
    }

    public Date getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(Date expenseDate) {
        this.expenseDate = expenseDate;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getExpenseReportNumber() {
        return expenseReportNumber;
    }

    public void setExpenseReportNumber(String expenseReportNumber) {
        this.expenseReportNumber = expenseReportNumber;
    }

    public SelectItem getDepartment() {
        return department;
    }

    public void setDepartment(SelectItem department) {
        this.department = department;
    }

    public SelectItem getProject() {
        return project;
    }

    public void setProject(SelectItem project) {
        this.project = project;
    }

    public SelectItem getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(SelectItem purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public boolean isProjectBase() {
        return isProjectBase;
    }

    public void setProjectBase(boolean projectBase) {
        isProjectBase = projectBase;
    }

    public Date getDateSort() {
        return getDate() != null ? getDate() : new Date();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer[] getProjectBasedEntryIds() {
        return projectBasedEntryIds;
    }

    public void setProjectBasedEntryIds(Integer[] projectBasedEntryIds) {
        this.projectBasedEntryIds = projectBasedEntryIds;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSaleInvoiceId() {
        return saleInvoiceId;
    }

    public void setSaleInvoiceId(Integer saleInvoiceId) {
        this.saleInvoiceId = saleInvoiceId;
    }

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public CompanyCustomFieldItem getCustomFieldByCode(String columnCode) {

        if (customFieldItems != null && !customFieldItems.isEmpty()) {
            for (CompanyCustomFieldItem fieldItem : customFieldItems) {

                if (columnCode.equals(fieldItem.getColumnCode())) {
                    return fieldItem;
                }
            }
        }

        return null;
    }

    public CompanyCustomFieldItem getCustomFieldByAlias(String aliasName) {

        if (customFieldItems != null && !customFieldItems.isEmpty()) {
            for (CompanyCustomFieldItem fieldItem : customFieldItems) {

                if (aliasName.equals(fieldItem.getAliasName())) {
                    return fieldItem;
                }
            }
        }

        return null;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }

    public void setReportReporter(SelectItem reportReporter) {
        this.reportReporter = reportReporter;
    }

    public SelectItem getReportReporter() {
        return reportReporter;
    }

    public BigDecimal getExpenseAmountInCurency() {
        return expenseAmountInCurency;
    }

    public void setExpenseAmountInCurency(BigDecimal expenseAmountInCurency) {
        this.expenseAmountInCurency = expenseAmountInCurency;
    }

    public Integer getBankTransferType() {
        return bankTransferType;
    }

    public void setBankTransferType(Integer bankTransferType) {
        this.bankTransferType = bankTransferType;
    }
}
