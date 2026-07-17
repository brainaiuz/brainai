package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.BankAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.core.client.ui.view.PdfTemplateItemList;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: May 11, 2009
 * Time: 5:07:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class NewManualTransaction extends HasApprovers implements IsSerializable, ListingCustomFields {

    public static final String DRAFT = "DRAFT";
    public static final String POST = "POST";
    public static final String REVERSED = "REVERSED";
    public static final String APPROVED = "APPROVED";

    //Validations
    public static final Integer REFERENCE_EXIST = -1;

    private Integer objectId;
    private String narration;
    private boolean showOnCashReports;
    private boolean validateReference = true;
    private String reference;
    private DateNonConvertable date;
    private String status;
    private NewManualTransactionItem[] items;

    private CurrencyItem baseCurrency;
    private CurrencyItem currency;
    private BigDecimal exchangeRate;

    private BigDecimal debitTotal;
    private BigDecimal creditTotal;
    private BigDecimal debitTaxTotal;
    private BigDecimal creditTaxTotal;

    private BigDecimal requiredTotal;
    private BigDecimal subtotal;
    private BigDecimal taxTotal;
    private BigDecimal taxForeignTotal;
    private BigDecimal total;

    //Used in spend receive money
    private NewManualTransactionItem[] vatTransactionItems;
    //0 = No Tax, 1 = Tax Inclusive, 2 = Tax Exclusive
    private Integer taxCalculationType;
    //0 = RECEIVE_MONEY, 1 = SPEND_MONEY
    private Integer transferType;
    //0 = SPEND_RECEIVE_FORM, 1 = CREATE_TRANSACTION_FORM
    private Integer formType;

    private BankAccountItem bankAccountItem;

    private FileItem[] attachments;
    private FileResource[] attachmentResources;

    private NumberData numberData;

    private Integer intNumber;

    private boolean isMemorizedTransaction;
    private boolean currencyAdjustment;
    private String layoutHtml;
    private ArrayList<CompanyCustomFieldItem> customFieldItems;
    private HashMap<String, Object> customFields;

    private boolean enabledPostDatedTransaction;
    private boolean enabledDepartmentRelation;
    private boolean enabledProjectInLineItem;
    private boolean postDatedTransaction;
    private String number;
    private String checkNumber;
    private Integer projectId;
    private Integer roleId;
    private SelectItem project;
    private SelectItem role;
    private SelectItem cashAccount;
    private BankTransferNumberData transferNumberData;
    private Integer pdfTemplateID;
    private PdfTemplateItemList pdfTemplateList;

    private boolean recurringTemplate;
    private RecurrenceJobItem recurrenceJobItem;

    private SelectItem account;
    private String transactionType;

    private HistoryListItem[] historyListItems;

    private Date ncDate;
    private String projectName;
    private String currencyName;
    private String accountName;
    private boolean isUsed;
    private String creator;
    private SelectItem creatorItem;
    private String crmAccountItemName;
    private SelectItem approver;
    private Integer journalID;
    private ColumnConfigs[] customItemColumns;
    private ArrayList<CompanyCustomFieldItem> itemCustomFields;
    private Integer recurringTemplateId;
    private SelectItem[] taxTreatments;
    private SelectItem taxTreatment;
    private SelectItem placeOfSupply;
    private boolean reversechargeApplicable;
    private boolean forceValidNumberGenerate;

    private SelectItem defaultDepartment;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public SelectItem getDefaultDepartment() {
        return defaultDepartment;
    }

    public void setDefaultDepartment(SelectItem defaultDepartment) {
        this.defaultDepartment = defaultDepartment;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public boolean isShowOnCashReports() {
        return showOnCashReports;
    }

    public void setShowOnCashReports(boolean showOnCashReports) {
        this.showOnCashReports = showOnCashReports;
    }

    public boolean isValidateReference() {
        return validateReference;
    }

    public void setValidateReference(boolean validateReference) {
        this.validateReference = validateReference;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public NewManualTransactionItem[] getItems() {
        return items;
    }

    public void setItems(NewManualTransactionItem[] items) {
        this.items = items;
    }

    public CurrencyItem getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(CurrencyItem baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public CurrencyItem getCurrency() {
        return currency;
    }

    public void setCurrency(CurrencyItem currency) {
        this.currency = currency;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getDebitTotal() {
        return debitTotal;
    }

    public void setDebitTotal(BigDecimal debitTotal) {
        this.debitTotal = debitTotal;
    }

    public BigDecimal getCreditTotal() {
        return creditTotal;
    }

    public void setCreditTotal(BigDecimal creditTotal) {
        this.creditTotal = creditTotal;
    }

    public BigDecimal getDebitTaxTotal() {
        return debitTaxTotal;
    }

    public void setDebitTaxTotal(BigDecimal debitTaxTotal) {
        this.debitTaxTotal = debitTaxTotal;
    }

    public BigDecimal getCreditTaxTotal() {
        return creditTaxTotal;
    }

    public void setCreditTaxTotal(BigDecimal creditTaxTotal) {
        this.creditTaxTotal = creditTaxTotal;
    }

    public BigDecimal getRequiredTotal() {
        return requiredTotal;
    }

    public void setRequiredTotal(BigDecimal requiredTotal) {
        this.requiredTotal = requiredTotal;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTaxTotal() {
        return taxTotal;
    }

    public void setTaxTotal(BigDecimal taxTotal) {
        this.taxTotal = taxTotal;
    }

    public BigDecimal getTaxForeignTotal() {
        return taxForeignTotal;
    }

    public void setTaxForeignTotal(BigDecimal taxForeignTotal) {
        this.taxForeignTotal = taxForeignTotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public NewManualTransactionItem[] getVatTransactionItems() {
        return vatTransactionItems;
    }

    public void setVatTransactionItems(NewManualTransactionItem[] vatTransactionItems) {
        this.vatTransactionItems = vatTransactionItems;
    }

    public Integer getTaxCalculationType() {
        return taxCalculationType;
    }

    public void setTaxCalculationType(Integer taxCalculationType) {
        this.taxCalculationType = taxCalculationType;
    }

    public Integer getTransferType() {
        return transferType;
    }

    public void setTransferType(Integer transferType) {
        this.transferType = transferType;
    }

    public Integer getFormType() {
        return formType;
    }

    public void setFormType(Integer formType) {
        this.formType = formType;
    }

    public BankAccountItem getBankAccountItem() {
        return bankAccountItem;
    }

    public void setBankAccountItem(BankAccountItem bankAccountItem) {
        this.bankAccountItem = bankAccountItem;
    }

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public boolean isMemorizedTransaction() {
        return isMemorizedTransaction;
    }

    public void setMemorizedTransaction(boolean memorizedTransaction) {
        isMemorizedTransaction = memorizedTransaction;
    }

    public boolean isCurrencyAdjustment() {
        return currencyAdjustment;
    }

    public void setCurrencyAdjustment(boolean currencyAdjustment) {
        this.currencyAdjustment = currencyAdjustment;
    }

    public NumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public String getLayoutHtml() {
        return layoutHtml;
    }

    public void setLayoutHtml(String layoutHtml) {
        this.layoutHtml = layoutHtml;
    }

    public boolean isEnabledPostDatedTransaction() {
        return enabledPostDatedTransaction;
    }

    public void setEnabledPostDatedTransaction(boolean enabledPostDatedTransaction) {
        this.enabledPostDatedTransaction = enabledPostDatedTransaction;
    }

    public boolean isPostDatedTransaction() {
        return postDatedTransaction;
    }

    public void setPostDatedTransaction(boolean postDatedTransaction) {
        this.postDatedTransaction = postDatedTransaction;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public SelectItem getProject() {
        return project;
    }

    public void setProject(SelectItem project) {
        this.project = project;
    }

    public SelectItem getRole() {
        return role;
    }

    public void setRole(SelectItem role) {
        this.role = role;
    }

    public FileResource[] getAttachmentResources() {
        return attachmentResources;
    }

    public void setAttachmentResources(FileResource[] attachmentResources) {
        this.attachmentResources = attachmentResources;
    }

    public SelectItem getCashAccount() {
        return cashAccount;
    }

    public void setCashAccount(SelectItem cashAccount) {
        this.cashAccount = cashAccount;
    }

    public BankTransferNumberData getTransferNumberData() {
        return transferNumberData;
    }

    public void setTransferNumberData(BankTransferNumberData transferNumberData) {
        this.transferNumberData = transferNumberData;
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

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }

    public boolean isRecurringTemplate() {
        return recurringTemplate;
    }

    public void setRecurringTemplate(boolean recurringTemplate) {
        this.recurringTemplate = recurringTemplate;
    }

    public RecurrenceJobItem getRecurrenceJobItem() {
        return recurrenceJobItem;
    }

    public void setRecurrenceJobItem(RecurrenceJobItem recurrenceJobItem) {
        this.recurrenceJobItem = recurrenceJobItem;
    }

    public boolean isEnabledDepartmentRelation() {
        return enabledDepartmentRelation;
    }

    public void setEnabledDepartmentRelation(boolean enabledDepartmentRelation) {
        this.enabledDepartmentRelation = enabledDepartmentRelation;
    }

    public boolean isEnabledProjectInLineItem() {
        return enabledProjectInLineItem;
    }

    public void setEnabledProjectInLineItem(boolean enabledProjectInLineItem) {
        this.enabledProjectInLineItem = enabledProjectInLineItem;
    }

    public SelectItem getAccount() {
        return account;
    }

    public void setAccount(SelectItem account) {
        this.account = account;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public void setNcDate(Date ncDate) {
        if (ncDate != null) {
            date = new DateNonConvertable(ncDate);
        }
    }

    public void setProjectName(String projectName) {
        if (projectName != null && !projectName.isEmpty()) {
            project = new SelectItem(null, projectName);
        }
    }

    public void setCurrencyName(String currencyName) {
        if (currencyName != null && !currencyName.isEmpty()) {
            currency = new CurrencyItem(null, currencyName, null);
        }
    }

    public void setAccountName(String accountName) {
        if (accountName != null && !accountName.isEmpty()) {
            account = new SelectItem(null, accountName);
        }
    }

    public HistoryListItem[] getHistoryListItems() {
        return historyListItems;
    }

    public void setHistoryListItems(HistoryListItem[] historyListItems) {
        this.historyListItems = historyListItems;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }

    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return getCustomFields() != null ? getCustomFields().get(columnCodeKey) : null;
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {
        getCustomFields().put(columnCodeKey, cellValue);
    }

    public void setCustomFields(HashMap<String, Object> customFields) {
        this.customFields = customFields;
    }

    public HashMap<String, Object> getCustomFields() {
        if (customFields == null) {
            customFields = new HashMap<>();
        }
        return customFields;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public SelectItem getCreatorItem() {
        return this.creatorItem;
    }

    public void setCreatorItem(final SelectItem creatorItem) {
        this.creatorItem = creatorItem;
    }

    public String getCrmAccountItemName() {
        return crmAccountItemName;
    }

    public void setCrmAccountItemName(String crmAccountItemName) {
        this.crmAccountItemName = crmAccountItemName;
    }

    public SelectItem getApprover() {
        return approver;
    }

    public void setApprover(SelectItem approver) {
        this.approver = approver;
    }

    public Integer getJournalID() {
        return this.journalID;
    }

    public void setJournalID(final Integer journalID) {
        this.journalID = journalID;
    }

    public ColumnConfigs[] getCustomItemColumns() {
        return customItemColumns;
    }

    public void setCustomItemColumns(ColumnConfigs[] customItemColumns) {
        this.customItemColumns = customItemColumns;
    }

    public ArrayList<CompanyCustomFieldItem> getItemCustomFields() {
        return itemCustomFields;
    }

    public void setItemCustomFields(ArrayList<CompanyCustomFieldItem> itemCustomFields) {
        this.itemCustomFields = itemCustomFields;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public SelectItem[] getTaxTreatments() {
        return taxTreatments;
    }

    public void setTaxTreatments(SelectItem[] taxTreatments) {
        this.taxTreatments = taxTreatments;
    }

    public SelectItem getTaxTreatment() {
        return taxTreatment;
    }

    public void setTaxTreatment(SelectItem taxTreatment) {
        this.taxTreatment = taxTreatment;
    }

    public SelectItem getPlaceOfSupply() {
        return placeOfSupply;
    }

    public void setPlaceOfSupply(SelectItem placeOfSupply) {
        this.placeOfSupply = placeOfSupply;
    }

    public boolean isReversechargeApplicable() {
        return reversechargeApplicable;
    }

    public void setReversechargeApplicable(boolean reversechargeApplicable) {
        this.reversechargeApplicable = reversechargeApplicable;
    }

    public Integer getRecurringTemplateId() {
        return recurringTemplateId;
    }

    public void setRecurringTemplateId(Integer recurringTemplateId) {
        this.recurringTemplateId = recurringTemplateId;
    }

    public boolean isForceValidNumberGenerate() {
        return forceValidNumberGenerate;
    }

    public void setForceValidNumberGenerate(boolean forceValidNumberGenerate) {
        this.forceValidNumberGenerate = forceValidNumberGenerate;
    }
}
