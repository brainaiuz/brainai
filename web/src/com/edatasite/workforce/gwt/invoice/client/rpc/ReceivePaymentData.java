package com.edatasite.workforce.gwt.invoice.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.core.client.ui.view.PdfTemplateItemList;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 4/29/11
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReceivePaymentData implements IsSerializable {
    private Integer objectID;
    private boolean batchPayment;
    private String number;
    private Integer intNumber;
    private BankTransferNumberData numberData;
    private SelectItem crmAccount;
    private SelectItem account;
    private SelectItem project;
    private String accountNumber;
    private BigDecimal totalAmount;
    private BigDecimal totalRefundAmount;
    private String reference;
    private DateNonConvertable date;
    private SelectItem paymentMethod;//Cash, Credit Card, Debit Card, ...
    private String paymentTarget;//Invoice or Manual Journal
    private String type;//RECEIVABLE OR PAYABLE
    private CurrencyItem currency;
    private CurrencyItem baseCurrency;
    private BigDecimal exRate;
    private PaymentData[] payments;
    private PaymentData overPayment;
    private PaymentData[] lessPayments;

    private PaymentData bankCheckTotalData;
    private boolean validateReferences;
    private int totalCount;
    private CurrencyItem[] enabledCurrencies;
    private String description;
    private SelectItem department;
    private boolean isEnableDepartment;
    private Integer pdfTemplateID;
    private PdfTemplateItemList pdfTemplateList;
    private FileItem[] attachments;
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private ArrayList<CompanyCustomFieldItem> systemCustomFields;
    private HashMap<String, Object> customFields;
    private String layoutHtml;
    private Integer crmAccountBillAddressId;
    private Address crmAccountAddress;
    private boolean enabledLineItemProject;
    private FileResource[] paymentAttachments;
    private boolean includeSubAccountTransaction;
    private SelectItem accountsReceivablePayable;
    private SelectItem bankAccount;
    private boolean reversed;
    private Integer journalID;
    private boolean hasMultiTransaction;
    private SelectItem closeAccount;
    private BigDecimal closeAmount;
    private String paymentStatus;
    private CurrencyItem bankAccountCurrency;
    private Integer rentalOrderId;

    private BigDecimal foreignAccExRate;


    public CurrencyItem getBankAccountCurrency() {
        return bankAccountCurrency;
    }

    public void setBankAccountCurrency(CurrencyItem bankAccountCurrency) {
        this.bankAccountCurrency = bankAccountCurrency;
    }

    public ReceivePaymentData() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public boolean isBatchPayment() {
        return batchPayment;
    }

    public void setBatchPayment(boolean batchPayment) {
        this.batchPayment = batchPayment;
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

    public BankTransferNumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(BankTransferNumberData numberData) {
        this.numberData = numberData;
    }

    public SelectItem getCrmAccount() {
        return crmAccount;
    }

    public void setCrmAccount(SelectItem crmAccount) {
        this.crmAccount = crmAccount;
    }

    public SelectItem getAccount() {
        return account;
    }

    public void setAccount(SelectItem account) {
        this.account = account;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public DateNonConvertable getDate() {
        return date;
    }

    public void setDate(DateNonConvertable date) {
        this.date = date;
    }

    public SelectItem getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(SelectItem paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentTarget() {
        return paymentTarget;
    }

    public void setPaymentTarget(String paymentTarget) {
        this.paymentTarget = paymentTarget;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PaymentData[] getPayments() {
        return payments;
    }

    public void setPayments(PaymentData[] payments) {
        this.payments = payments;
    }

    public PaymentData getOverPayment() {
        return overPayment;
    }

    public void setOverPayment(PaymentData overPayment) {
        this.overPayment = overPayment;
    }

    public PaymentData[] getLessPayments() {
        return lessPayments;
    }

    public void setLessPayments(PaymentData[] lessPayments) {
        this.lessPayments = lessPayments;
    }

    public PaymentData getBankCheckTotalData() {
        return bankCheckTotalData;
    }

    public void setBankCheckTotalData(PaymentData bankCheckTotalData) {
        this.bankCheckTotalData = bankCheckTotalData;
    }

    public boolean isValidateReferences() {
        return validateReferences;
    }

    public void setValidateReferences(boolean validateReferences) {
        this.validateReferences = validateReferences;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public CurrencyItem[] getEnabledCurrencies() {
        return enabledCurrencies;
    }

    public void setEnabledCurrencies(CurrencyItem[] enabledCurrencies) {
        this.enabledCurrencies = enabledCurrencies;
    }

    public CurrencyItem getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyItem currency) {
        this.currency = currency;
    }

    public CurrencyItem getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(CurrencyItem baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public BigDecimal getExRate() {
        return exRate;
    }

    public void setExRate(BigDecimal exRate) {
        this.exRate = exRate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SelectItem getDepartment() {
        return department;
    }

    public void setDepartment(SelectItem department) {
        this.department = department;
    }

    public boolean isEnableDepartment() {
        return isEnableDepartment;
    }

    public void setEnableDepartment(boolean isEnableDepartment) {
        this.isEnableDepartment = isEnableDepartment;
    }

    public Integer getPdfTemplateID() {
        return pdfTemplateID;
    }

    public void setPdfTemplateID(Integer pdfTemplateID) {
        this.pdfTemplateID = pdfTemplateID;
    }

    public PdfTemplateItemList getPdfTemplateList() {
        return pdfTemplateList;
    }

    public void setPdfTemplateList(PdfTemplateItemList pdfTemplateList) {
        this.pdfTemplateList = pdfTemplateList;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }

    public HashMap<String, Object> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(HashMap<String, Object> customFields) {
        this.customFields = customFields;
    }

    public String getLayoutHtml() {
        return layoutHtml;
    }

    public void setLayoutHtml(String layoutHtml) {
        this.layoutHtml = layoutHtml;
    }

    public Integer getCrmAccountBillAddressId() {
        return crmAccountBillAddressId;
    }

    public void setCrmAccountBillAddressId(Integer crmAccountAddress) {
        this.crmAccountBillAddressId = crmAccountAddress;
    }

    public Address getCrmAccountAddress() {
        return crmAccountAddress;
    }

    public void setCrmAccountAddress(Address crmAccountAddress) {
        this.crmAccountAddress = crmAccountAddress;
    }

    public boolean isEnabledLineItemProject() {
        return enabledLineItemProject;
    }

    public void setEnabledLineItemProject(boolean enabledLineItemProject) {
        this.enabledLineItemProject = enabledLineItemProject;
    }

    public SelectItem getProject() {
        return project;
    }

    public void setProject(SelectItem project) {
        this.project = project;
    }

    public FileResource[] getPaymentAttachments() {
        return paymentAttachments;
    }

    public void setPaymentAttachments(FileResource[] paymentAttachments) {
        this.paymentAttachments = paymentAttachments;
    }

    public boolean isIncludeSubAccountTransaction() {
        return includeSubAccountTransaction;
    }

    public void setIncludeSubAccountTransaction(boolean includeSubAccountTransaction) {
        this.includeSubAccountTransaction = includeSubAccountTransaction;
    }

    public SelectItem getAccountsReceivablePayable() {
        return accountsReceivablePayable;
    }

    public void setAccountsReceivablePayable(SelectItem accountsReceivablePayable) {
        this.accountsReceivablePayable = accountsReceivablePayable;
    }

    public SelectItem getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(SelectItem bankAccount) {
        this.bankAccount = bankAccount;
    }

    public boolean isReversed() {
        return reversed;
    }

    public void setReversed(boolean reversed) {
        this.reversed = reversed;
    }

    public Integer getJournalID() {
        return this.journalID;
    }

    public void setJournalID(final Integer journalID) {
        this.journalID = journalID;
    }

    public boolean isHasMultiTransaction() {
        return hasMultiTransaction;
    }

    public void setHasMultiTransaction(boolean hasMultiTransaction) {
        this.hasMultiTransaction = hasMultiTransaction;
    }

    public BigDecimal getTotalRefundAmount() {
        return this.totalRefundAmount;
    }

    public void setTotalRefundAmount(final BigDecimal totalRefundAmount) {
        this.totalRefundAmount = totalRefundAmount;
    }

    public ArrayList<CompanyCustomFieldItem> getSystemCustomFields() {
        return systemCustomFields;
    }

    public void setSystemCustomFields(ArrayList<CompanyCustomFieldItem> systemCustomFields) {
        this.systemCustomFields = systemCustomFields;
    }

    public SelectItem getCloseAccount() {
        return this.closeAccount;
    }

    public void setCloseAccount(final SelectItem closeAccount) {
        this.closeAccount = closeAccount;
    }

    public BigDecimal getCloseAmount() {
        return this.closeAmount;
    }

    public void setCloseAmount(final BigDecimal closeAmount) {
        this.closeAmount = closeAmount;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public BigDecimal getForeignAccExRate() {
        return foreignAccExRate;
    }

    public void setForeignAccExRate(BigDecimal foreignAccExRate) {
        this.foreignAccExRate = foreignAccExRate;
    }

    public void setRentalOrderId(Integer rentalOrderId) {
        this.rentalOrderId = rentalOrderId;
    }

    public Integer getRentalOrderId() {
        return this.rentalOrderId;
    }
}
