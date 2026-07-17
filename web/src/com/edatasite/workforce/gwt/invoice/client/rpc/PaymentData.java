package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

public class PaymentData implements IsSerializable {

    //Validations
    public static final Integer REFERENCE_EXIST = -1;
    public static final Integer OVER_PAID = -2;

    private Integer objectID;
    private Integer underPaymentID;
    private Integer invoiceID;
    private Integer expenseId;
    private Integer manualJournalID;
    private Integer bankTransferID;
    private BigDecimal paymentAmount;
    private BigDecimal paymentAmountInInvoiceCurrency;
    private BigDecimal basePaymentAmount;
    private DateNonConvertable date;
    private Integer paymentTypeID;
    private String referenceNumber;
    private String reference;
    private boolean validateReference = true;
    private BigDecimal total;
    private BigDecimal remainingAmount;
    private BigDecimal totalInInvoiceCurrency;
    private BigDecimal totalDueAmount;
    private BigDecimal totalDiscount;
    private BigDecimal baseTotal;
    private BigDecimal exchangeRate;
    private BigDecimal entityExchangeRate;//invoice,manual or opening balance entity exchange rate
    private BigDecimal appliedAmount;
    private BigDecimal refundAmount;
    private boolean closePrepayment;
    private BigDecimal closedAmount;

    private String invoiceNumber;
    private String number;
    private Integer intNumber;
    private boolean isOpeningBalance;
    private boolean isManualJournal;
    private boolean isPrepayment;
    private boolean isBankTransafer;
    private boolean expensePayment;
    private DateNonConvertable invoiceDate;
    private DateNonConvertable invoiceDueDate;
    private String invoiceProjectNumber;
    private String invoiceProjectName;
    private ArrayList<String> lineItemProject;
    private ArrayList<String> lineItemParentProject;
    private HashMap<String, BigDecimal> lineItemNameAndQty = new HashMap<>();

    private SelectItem paymentAccount;
    private SelectItem currency;

    //PrePayment fields
    private Integer relatedObjectID;
    private Integer prepaymentID;
    private BigDecimal baseAmount;
    private SelectItem crmAccount;
    private SelectItem project;
    private SelectItem department;
    private SelectItem bankCheckItem;
    private SelectItem saleQuoteItem;
    private SelectItem saleInvoiceItem;
    private SelectItem purchaseOrderItem;
    private SelectItem rentalOrderItem;
    private AccountItem receivablePayable;
    private AccountItem bankFee;
    private String note;
    private String type;
    private String bankFeeType;
    private String paymentStatus;
    private BigDecimal bankFeeValue;

    private boolean crmAccountCredit;

    private String gatewayReturnedURL;

    private boolean postDatedTransaction;

    private boolean isCustomer = true;
    private boolean isPaymentDiffCurrency = false;
    private boolean fullPaid = false;

    private Integer batchPaymentID;
    private Integer paymentRefundID;

    private SelectItem accountItem;
    private String poNumber;
    private SelectItem underPaymentAccount;
    private BigDecimal underPaymentAmount;
    private BigDecimal underPaymentAmountInInvoiceCurrency;

    private SelectItem overPaymentAccount;
    private BigDecimal overPaymentAmount;

    private BigDecimal underPaymentTaxRate;
    private BigDecimal underPaymentTaxAmount;

    private FileResource[] attachments;
    private FileItem[] attachedFiles;
    private HistoryListItem[] historyList;
    /*invoice item data*/
    private Integer itemId;
    private String itemName;
    private BigDecimal itemQty;
    private BigDecimal itemUnitPrice;
    private BigDecimal itemNetAmount;
    private BigDecimal itemTotalAmount;
    private TaxItem taxItem;
    private BigDecimal itemTaxAmount;
    private Integer taxCalculationType;
    private ArrayList<CompanyCustomFieldItem> customFields;

    private BigDecimal foreignAccExRate;


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(Integer invoiceID) {
        this.invoiceID = invoiceID;
    }

    public Integer getExpenseId() {
        return this.expenseId;
    }

    public void setExpenseId(final Integer expenseId) {
        this.expenseId = expenseId;
    }

    public Integer getManualJournalID() {
        return manualJournalID;
    }

    public void setManualJournalID(Integer manualJournalID) {
        this.manualJournalID = manualJournalID;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public BigDecimal getBasePaymentAmount() {
        return basePaymentAmount;
    }

    public void setBasePaymentAmount(BigDecimal basePaymentAmount) {
        this.basePaymentAmount = basePaymentAmount;
    }

    public DateNonConvertable getDate() {
        return date;
    }

    public void setDate(DateNonConvertable date) {
        this.date = date;
    }

    public SelectItem getPaymentAccount() {
        return paymentAccount;
    }

    public void setPaymentAccount(SelectItem paymentAccount) {
        this.paymentAccount = paymentAccount;
    }

    public SelectItem getCurrency() {
        return currency;
    }

    public void setCurrency(SelectItem currency) {
        this.currency = currency;
    }

    public Integer getPaymentTypeID() {
        return paymentTypeID;
    }

    public void setPaymentTypeID(Integer paymentTypeID) {
        this.paymentTypeID = paymentTypeID;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public boolean isValidateReference() {
        return validateReference;
    }

    public void setValidateReference(boolean validateReference) {
        this.validateReference = validateReference;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getTotalInInvoiceCurrency() {
        return totalInInvoiceCurrency;
    }

    public void setTotalInInvoiceCurrency(BigDecimal totalInInvoiceCurrency) {
        this.totalInInvoiceCurrency = totalInInvoiceCurrency;
    }

    public BigDecimal getTotalDueAmount() {
        return totalDueAmount;
    }

    public void setTotalDueAmount(BigDecimal totalDueAmount) {
        this.totalDueAmount = totalDueAmount;
    }

    public BigDecimal getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(BigDecimal totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getEntityExchangeRate() {
        return entityExchangeRate;
    }

    public void setEntityExchangeRate(BigDecimal entityExchangeRate) {
        this.entityExchangeRate = entityExchangeRate;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public boolean isOpeningBalance() {
        return isOpeningBalance;
    }

    public void setOpeningBalance(boolean openingBalance) {
        isOpeningBalance = openingBalance;
    }

    public boolean isManualJournal() {
        return isManualJournal;
    }

    public void setManualJournal(boolean manualJournal) {
        isManualJournal = manualJournal;
    }

    public boolean isPrepayment() {
        return isPrepayment;
    }

    public void setPrepayment(boolean isSupplierCredit) {
        this.isPrepayment = isSupplierCredit;
    }

    public DateNonConvertable getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(DateNonConvertable invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public DateNonConvertable getInvoiceDueDate() {
        return invoiceDueDate;
    }

    public void setInvoiceDueDate(DateNonConvertable invoiceDueDate) {
        this.invoiceDueDate = invoiceDueDate;
    }

    public String getInvoiceProjectNumber() {
        return invoiceProjectNumber;
    }

    public void setInvoiceProjectNumber(String invoiceProjectNumber) {
        this.invoiceProjectNumber = invoiceProjectNumber;
    }

    public String getInvoiceProjectName() {
        return invoiceProjectName;
    }

    public void setInvoiceProjectName(String invoiceProjectName) {
        this.invoiceProjectName = invoiceProjectName;
    }

    public ArrayList<String> getLineItemProject() {
        return lineItemProject;
    }

    public void setLineItemProject(ArrayList<String> lineItemProject) {
        this.lineItemProject = lineItemProject;
    }

    public ArrayList<String> getLineItemParentProject() {
        return lineItemParentProject;
    }

    public void setLineItemParentProject(ArrayList<String> lineItemParentProject) {
        this.lineItemParentProject = lineItemParentProject;
    }

    public HashMap<String, BigDecimal> getLineItemNameAndQty() {
        return lineItemNameAndQty;
    }

    public void setLineItemNameAndQty(HashMap<String, BigDecimal> lineItemNameAndQty) {
        this.lineItemNameAndQty = lineItemNameAndQty;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getRelatedObjectID() {
        return relatedObjectID;
    }

    public void setRelatedObjectID(Integer relatedObjectID) {
        this.relatedObjectID = relatedObjectID;
    }

    public Integer getPrepaymentID() {
        return this.prepaymentID;
    }

    public void setPrepaymentID(final Integer prepaymentID) {
        this.prepaymentID = prepaymentID;
    }

    public BigDecimal getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(BigDecimal baseAmount) {
        this.baseAmount = baseAmount;
    }

    public SelectItem getCrmAccount() {
        return crmAccount;
    }

    public void setCrmAccount(SelectItem crmAccount) {
        this.crmAccount = crmAccount;
    }

    public SelectItem getSaleQuoteItem() {
        return saleQuoteItem;
    }

    public void setSaleQuoteItem(SelectItem saleQuoteItem) {
        this.saleQuoteItem = saleQuoteItem;
    }

    public SelectItem getSaleInvoiceItem() {
        return saleInvoiceItem;
    }

    public void setSaleInvoiceItem(SelectItem saleInvoiceItem) {
        this.saleInvoiceItem = saleInvoiceItem;
    }

    public SelectItem getPurchaseOrderItem() {
        return purchaseOrderItem;
    }

    public void setPurchaseOrderItem(SelectItem purchaseOrderItem) {
        this.purchaseOrderItem = purchaseOrderItem;
    }

    public SelectItem getRentalOrderItem() {
        return rentalOrderItem;
    }

    public void setRentalOrderItem(SelectItem rentalOrderItem) {
        this.rentalOrderItem = rentalOrderItem;
    }

    public SelectItem getBankCheckItem() {
        return bankCheckItem;
    }

    public void setBankCheckItem(SelectItem bankCheckItem) {
        this.bankCheckItem = bankCheckItem;
    }

    public AccountItem getReceivablePayable() {
        return receivablePayable;
    }

    public void setReceivablePayable(AccountItem receivablePayable) {
        this.receivablePayable = receivablePayable;
    }

    public AccountItem getBankFee() {
        return bankFee;
    }

    public void setBankFee(AccountItem bankFee) {
        this.bankFee = bankFee;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBankFeeType() {
        return bankFeeType;
    }

    public void setBankFeeType(String bankFeeType) {
        this.bankFeeType = bankFeeType;
    }

    public BigDecimal getBankFeeValue() {
        return bankFeeValue;
    }

    public void setBankFeeValue(BigDecimal bankFeeValue) {
        this.bankFeeValue = bankFeeValue;
    }

    public boolean isPostDatedTransaction() {
        return postDatedTransaction;
    }

    public void setPostDatedTransaction(boolean postDatedTransaction) {
        this.postDatedTransaction = postDatedTransaction;
    }

    public boolean isCrmAccountCredit() {
        return crmAccountCredit;
    }

    public void setCrmAccountCredit(boolean crmAccountCredit) {
        this.crmAccountCredit = crmAccountCredit;
    }

    public String getGatewayReturnedURL() {
        return gatewayReturnedURL;
    }

    public void setGatewayReturnedURL(String gatewayReturnedURL) {
        this.gatewayReturnedURL = gatewayReturnedURL;
    }

    public boolean isCustomer() {
        return isCustomer;
    }

    public void setCustomer(boolean isCustomer) {
        this.isCustomer = isCustomer;
    }

    public Integer getBatchPaymentID() {
        return batchPaymentID;
    }

    public void setBatchPaymentID(Integer batchPaymentID) {
        this.batchPaymentID = batchPaymentID;
    }

    public Integer getPaymentRefundID() {
        return this.paymentRefundID;
    }

    public void setPaymentRefundID(final Integer paymentRefundID) {
        this.paymentRefundID = paymentRefundID;
    }

    public SelectItem getAccountItem() {
        return accountItem;
    }

    public void setAccountItem(SelectItem accountItem) {
        this.accountItem = accountItem;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public BigDecimal getBaseTotal() {
        return baseTotal;
    }

    public void setBaseTotal(BigDecimal baseTotal) {
        this.baseTotal = baseTotal;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Integer getBankTransferID() {
        return bankTransferID;
    }

    public void setBankTransferID(Integer bankTransferID) {
        this.bankTransferID = bankTransferID;
    }

    public boolean isBankTransafer() {
        return isBankTransafer;
    }

    public void setBankTransafer(boolean bankTransafer) {
        isBankTransafer = bankTransafer;
    }

    public boolean isPaymentDiffCurrency() {
        return isPaymentDiffCurrency;
    }

    public void setPaymentDiffCurrency(boolean paymentDiffCurrency) {
        isPaymentDiffCurrency = paymentDiffCurrency;
    }

    public boolean isFullPaid() {
        return fullPaid;
    }

    public void setFullPaid(boolean fullPaid) {
        this.fullPaid = fullPaid;
    }

    public BigDecimal getPaymentAmountInInvoiceCurrency() {
        return paymentAmountInInvoiceCurrency;
    }

    public void setPaymentAmountInInvoiceCurrency(BigDecimal paymentAmountInInvoiceCurrency) {
        this.paymentAmountInInvoiceCurrency = paymentAmountInInvoiceCurrency;
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

    public String getKeyForMap() {
        String key = String.valueOf(getInvoiceID());

        if (getAccountItem() != null) {
            key += "_" + getAccountItem().getId();
        }
        if (getCrmAccount() != null && getCrmAccount().getId() != null) {
            key += "_" + getCrmAccount().getId();
        }

        return key;
    }

    public FileResource[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileResource[] attachments) {
        this.attachments = attachments;
    }

    public SelectItem getProject() {
        return project;
    }

    public void setProject(SelectItem project) {
        this.project = project;
    }

    public SelectItem getDepartment() {
        return department;
    }

    public void setDepartment(SelectItem department) {
        this.department = department;
    }

    public BigDecimal getAppliedAmount() {
        return appliedAmount;
    }

    public void setAppliedAmount(BigDecimal appliedAmount) {
        this.appliedAmount = appliedAmount;
    }

    public SelectItem getUnderPaymentAccount() {
        return underPaymentAccount;
    }

    public void setUnderPaymentAccount(SelectItem underPaymentAccount) {
        this.underPaymentAccount = underPaymentAccount;
    }

    public BigDecimal getUnderPaymentAmount() {
        return underPaymentAmount;
    }

    public void setUnderPaymentAmount(BigDecimal underPaymentAmount) {
        this.underPaymentAmount = underPaymentAmount;
    }

    public BigDecimal getUnderPaymentAmountInInvoiceCurrency() {
        return underPaymentAmountInInvoiceCurrency;
    }

    public void setUnderPaymentAmountInInvoiceCurrency(BigDecimal underPaymentAmountInInvoiceCurrency) {
        this.underPaymentAmountInInvoiceCurrency = underPaymentAmountInInvoiceCurrency;
    }

    public SelectItem getOverPaymentAccount() {
        return overPaymentAccount;
    }

    public void setOverPaymentAccount(SelectItem overPaymentAccount) {
        this.overPaymentAccount = overPaymentAccount;
    }

    public BigDecimal getOverPaymentAmount() {
        return overPaymentAmount;
    }

    public void setOverPaymentAmount(BigDecimal overPaymentAmount) {
        this.overPaymentAmount = overPaymentAmount;
    }

    public BigDecimal getUnderPaymentTaxRate() {
        return underPaymentTaxRate;
    }

    public void setUnderPaymentTaxRate(BigDecimal underPaymentTaxRate) {
        this.underPaymentTaxRate = underPaymentTaxRate;
    }

    public BigDecimal getUnderPaymentTaxAmount() {
        return underPaymentTaxAmount;
    }

    public void setUnderPaymentTaxAmount(BigDecimal underPaymentTaxAmount) {
        this.underPaymentTaxAmount = underPaymentTaxAmount;
    }

    public Integer getUnderPaymentID() {
        return underPaymentID;
    }

    public void setUnderPaymentID(Integer underPaymentID) {
        this.underPaymentID = underPaymentID;
    }

    public FileItem[] getAttachedFiles() {
        return attachedFiles;
    }

    public void setAttachedFiles(FileItem[] attachedFiles) {
        this.attachedFiles = attachedFiles;
    }

    public HistoryListItem[] getHistoryList() {
        return historyList;
    }

    public void setHistoryList(HistoryListItem[] historyList) {
        this.historyList = historyList;
    }

    public boolean isExpensePayment() {
        return expensePayment;
    }

    public void setExpensePayment(boolean expensePayment) {
        this.expensePayment = expensePayment;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public BigDecimal getItemQty() {
        return itemQty;
    }

    public void setItemQty(BigDecimal itemQty) {
        this.itemQty = itemQty;
    }

    public BigDecimal getItemUnitPrice() {
        return itemUnitPrice;
    }

    public void setItemUnitPrice(BigDecimal itemUnitPrice) {
        this.itemUnitPrice = itemUnitPrice;
    }

    public BigDecimal getItemNetAmount() {
        return itemNetAmount;
    }

    public void setItemNetAmount(BigDecimal itemNetAmount) {
        this.itemNetAmount = itemNetAmount;
    }

    public BigDecimal getItemTotalAmount() {
        return itemTotalAmount;
    }

    public void setItemTotalAmount(BigDecimal itemTotalAmount) {
        this.itemTotalAmount = itemTotalAmount;
    }

    public TaxItem getTaxItem() {
        return taxItem;
    }

    public void setTaxItem(TaxItem taxItem) {
        this.taxItem = taxItem;
    }

    public BigDecimal getItemTaxAmount() {
        return itemTaxAmount;
    }

    public void setItemTaxAmount(BigDecimal itemTaxAmount) {
        this.itemTaxAmount = itemTaxAmount;
    }

    public Integer getTaxCalculationType() {
        return taxCalculationType;
    }

    public void setTaxCalculationType(Integer taxCalculationType) {
        this.taxCalculationType = taxCalculationType;
    }

    public BigDecimal getRemainingAmount() {
        return this.remainingAmount;
    }

    public void setRemainingAmount(final BigDecimal remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public BigDecimal getRefundAmount() {
        return this.refundAmount;
    }

    public void setRefundAmount(final BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }


    public ArrayList<CompanyCustomFieldItem> getCustomFields() {
        return this.customFields;
    }

    public void setCustomFields(final ArrayList<CompanyCustomFieldItem> customFields) {
        this.customFields = customFields;
    }

    public boolean isClosePrepayment() {
        return this.closePrepayment;
    }

    public void setClosePrepayment(final boolean closePrepayment) {
        this.closePrepayment = closePrepayment;
    }

    public BigDecimal getClosedAmount() {
        return this.closedAmount;
    }

    public void setClosedAmount(final BigDecimal closedAmount) {
        this.closedAmount = closedAmount;
    }

    public BigDecimal getForeignAccExRate() {
        return foreignAccExRate;
    }

    public void setForeignAccExRate(BigDecimal foreignAccExRate) {
        this.foreignAccExRate = foreignAccExRate;
    }
}
