package com.edatasite.workforce.gwt.expenses.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.HasApprovers;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.core.client.ui.view.PdfTemplateItemList;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

public class ExpenseReportsListItem extends HasApprovers implements IsSerializable, ListingCustomFields {

    public static final String ACTION = "action";

    private Integer id;
    private String title;
    private String description;
    private DateNonConvertable startDate;
    private String projectName;
    private String projectStatusCode;
    private SelectItem project;
    private String opportunityName;
    private SelectItem opportunity;
    private SelectItem approverSelectItem;
    private SelectItem purchaseOrder;
    private String purchaseOrderNumber;
    private CurrencyItem baseCurrency;
    private CurrencyItem expenseCurrency;
    private BigDecimal exchangeRate;
    private String reporterName;
    private Integer reporterId;
    private String status;
    private String statusColor;
    private String statusCode;
    private Integer statusID;
    private BigDecimal taxTotal;
    private BigDecimal baseTotal;
    private BigDecimal total;
    private BigDecimal paidTotal;
    private BigDecimal dueTotal;
    private ExpensePaymentData[] paymentItems;
    private HistoryListItem[] noteItems;
    private ExpenseListItem[] items;
    private boolean isApprover;
    private boolean approveProcessEnabled;
    private Integer employeeId;
    private String purpose;
    private String place;
    private boolean isDoubleApproverEnabled;
    private FileItem[] attachments;
    private boolean isCandidate;
    private PdfTemplateItemList pdfTemplateList;
    private Integer pdfTemplateId;
    private SelectItem paymentAccount;
    private AccountItem payableAccount;

    //private NumberData number;
    private BankTransferNumberData expenseNumberData;
    private String expenseNumber;
    private Integer intNumber;

    private String solrNumbering;

    private Integer clientId;
    private String clientName;

    private Integer journalId;
    private boolean reSubmit;

    private ArrayList<CompanyCustomFieldItem> customFieldItems; //expense report custom fields
    private ArrayList<CompanyCustomFieldItem> itemCustomFields; //expense report items custom fields
    private ArrayList<CompanyCustomFieldItem> systemCustomFields; //expense report system custom fields
    private HashMap<String, Object> customFields;//it is declared as Map, but defined as HashMap, don`t worry :-)

    private Boolean isOldExpense;

    private ArrayList<Integer> deletedItemIds;

    private Boolean isJoinOpportunityToExpenseClaim;

    private SelectItem fixedAsset;
    private SelectItem supplier;

    private Integer taxCalculationType;

    private DateNonConvertable periodStartDate;
    private DateNonConvertable periodEndDate;
    private Integer[] projectIds;
    private boolean isProjectBase;
    private Boolean isAllocatedToPO;
    private BigDecimal totalAllocated;
    private boolean fromOldMobile;
    private boolean isCompanyExpense = false;
    private boolean categoriesSelected = false;

    private ColumnConfigs[] customItemColumns;
    private boolean hasAccess = false;

    private SelectItem taxTreatment;
    private SelectItem placeOfSupply;
    private boolean reversechargeApplicable;
    private DateNonConvertable createdDate;
    private DateNonConvertable updatedDate;

    private SelectItem saleOrderClient;

    private SelectItem saleOrder;


    public ExpenseReportsListItem() {
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DateNonConvertable getStartDate() {
        return startDate;
    }

    public void setStartDate(DateNonConvertable startDate) {
        this.startDate = startDate;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public SelectItem getProject() {
        return project;
    }

    public void setProject(SelectItem project) {
        this.project = project;
    }

    public String getOpportunityName() {
        return opportunityName;
    }

    public void setOpportunityName(String opportunityName) {
        this.opportunityName = opportunityName;
    }

    public SelectItem getOpportunity() {
        return opportunity;
    }

    public void setOpportunity(SelectItem opportunity) {
        this.opportunity = opportunity;
    }

    public BigDecimal getTaxTotal() {
        return taxTotal;
    }

    public void setTaxTotal(BigDecimal taxTotal) {
        this.taxTotal = taxTotal;
    }

    public BigDecimal getBaseTotal() {
        return baseTotal;
    }

    public void setBaseTotal(BigDecimal baseTotal) {
        this.baseTotal = baseTotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getPaidTotal() {
        return paidTotal;
    }

    public void setPaidTotal(BigDecimal paidTotal) {
        this.paidTotal = paidTotal;
    }

    public ExpensePaymentData[] getPaymentItems() {
        return paymentItems;
    }

    public void setPaymentItems(ExpensePaymentData[] paymentItems) {
        this.paymentItems = paymentItems;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getReporterId() {
        return reporterId;
    }

    public void setReporterId(Integer reporterId) {
        this.reporterId = reporterId;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public CurrencyItem getExpenseCurrency() {
        return expenseCurrency;
    }

    public void setExpenseCurrency(CurrencyItem expenseCurrency) {
        this.expenseCurrency = expenseCurrency;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public CurrencyItem getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(CurrencyItem baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public SelectItem getApproverSelectItem() {
        return approverSelectItem;
    }

    public void setApproverSelectItem(SelectItem approverSelectItem) {
        this.approverSelectItem = approverSelectItem;
    }

    public SelectItem getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(SelectItem purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getStatusID() {
        return statusID;
    }

    public void setStatusID(Integer statusID) {
        this.statusID = statusID;
    }

    public HistoryListItem[] getNoteItems() {
        return noteItems;
    }

    public void setNoteItems(HistoryListItem[] noteItems) {
        this.noteItems = noteItems;
    }

    public ExpenseListItem[] getItems() {
        return items;
    }

    public void setItems(ExpenseListItem[] items) {
        this.items = items;
    }

    public boolean isApprover() {
        return isApprover;
    }

    public void setApprover(boolean approver) {
        isApprover = approver;
    }

    public boolean isApproveProcessEnabled() {
        return this.approveProcessEnabled;
    }

    public void setApproveProcessEnabled(final boolean approveProcessEnabled) {
        this.approveProcessEnabled = approveProcessEnabled;
    }

    public boolean isPreferredApprover(Integer userID) {
        return approverSelectItem != null && userID.equals(approverSelectItem.getId());
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

    public boolean isDoubleApproverEnabled() {
        return isDoubleApproverEnabled;
    }

    public void setDoubleApproverEnabled(boolean doubleApproverEnabled) {
        isDoubleApproverEnabled = doubleApproverEnabled;
    }

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public SelectItem getPaymentAccount() {
        return paymentAccount;
    }

    public void setPaymentAccount(SelectItem paymentAccount) {
        this.paymentAccount = paymentAccount;
    }

    public AccountItem getPayableAccount() {
        return payableAccount;
    }

    public void setPayableAccount(AccountItem payableAccount) {
        this.payableAccount = payableAccount;
    }

    public boolean isReSubmit() {
        return reSubmit;
    }

    public void setReSubmit(boolean reSubmit) {
        this.reSubmit = reSubmit;
    }

    public Boolean isOldExpense() {
        return isOldExpense != null ? isOldExpense : false;
    }

    public void setOldExpense(Boolean oldExpense) {
        isOldExpense = oldExpense;
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

    public ArrayList<CompanyCustomFieldItem> getCustomFieldItems() {
        return customFieldItems;
    }

    public void setCustomFieldItems(ArrayList<CompanyCustomFieldItem> customFieldItems) {
        this.customFieldItems = customFieldItems;
    }

    public ArrayList<CompanyCustomFieldItem> getItemCustomFields() {
        return itemCustomFields;
    }

    public void setItemCustomFields(ArrayList<CompanyCustomFieldItem> itemCustomFields) {
        this.itemCustomFields = itemCustomFields;
    }

    /*public NumberData getNumber() {
        return number;
    }

    public void setNumber(NumberData number) {
        this.number = number;
    }*/

    public String getSolrNumbering() {
        return solrNumbering;
    }

    public void setSolrNumbering(String solrNumbering) {
        this.solrNumbering = solrNumbering;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusColor() {
        return statusColor;
    }

    public void setStatusColor(String statusColor) {
        this.statusColor = statusColor;
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

    public ArrayList<CompanyCustomFieldItem> getSystemCustomFields() {
        return systemCustomFields;
    }

    public void setSystemCustomFields(ArrayList<CompanyCustomFieldItem> systemCustomFields) {
        this.systemCustomFields = systemCustomFields;
    }

    public Boolean isJoinOpportunityToExpenseClaim() {
        return isJoinOpportunityToExpenseClaim;
    }

    public void setJoinOpportunityToExpenseClaim(Boolean joinOpportunityToExpenseClaim) {
        isJoinOpportunityToExpenseClaim = joinOpportunityToExpenseClaim;
    }

    public SelectItem getFixedAsset() {
        return fixedAsset;
    }

    public void setFixedAsset(SelectItem fixedAsset) {
        this.fixedAsset = fixedAsset;
    }

    public Integer getTaxCalculationType() {
        return taxCalculationType;
    }

    public void setTaxCalculationType(Integer taxCalculationType) {
        this.taxCalculationType = taxCalculationType;
    }

    public String getProjectStatusCode() {
        return projectStatusCode;
    }

    public void setProjectStatusCode(String projectStatusCode) {
        this.projectStatusCode = projectStatusCode;
    }

    public BigDecimal getDueTotal() {
        return dueTotal;
    }

    public void setDueTotal(BigDecimal dueTotal) {
        this.dueTotal = dueTotal;
    }

    public boolean isCandidate() {
        return isCandidate;
    }

    public void setCandidate(boolean isCandidate) {
        this.isCandidate = isCandidate;
    }

    public String getPurchaseOrderNumber() {
        return purchaseOrderNumber;
    }

    public void setPurchaseOrderNumber(String purchaseOrderNumber) {
        this.purchaseOrderNumber = purchaseOrderNumber;
    }

    public SelectItem getSupplier() {
        return supplier;
    }

    public void setSupplier(SelectItem supplier) {
        this.supplier = supplier;
    }

    public Integer getCurrentApproverEmployeeID() {
        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
            return getCurrentApprover().getExactEmployee().getId();
        }
        return null;
    }

    public String getCurrentApproverEmployeeName() {
        if (getCurrentApprover() != null && getCurrentApprover().getExactEmployee() != null) {
            return getCurrentApprover().getExactEmployee().getName();
        }
        return null;
    }

    public String getOverallStatusName() {
        return getOverallStatus() != null ? getOverallStatus().getName() : null;
    }

    public String getOverallStatusCode() {
        return getOverallStatus() != null ? getOverallStatus().getCode() : null;
    }

    public DateNonConvertable getPeriodStartDate() {
        return periodStartDate;
    }

    public void setPeriodStartDate(DateNonConvertable periodStartDate) {
        this.periodStartDate = periodStartDate;
    }

    public DateNonConvertable getPeriodEndDate() {
        return periodEndDate;
    }

    public void setPeriodEndDate(DateNonConvertable periodEndDate) {
        this.periodEndDate = periodEndDate;
    }

    public Integer[] getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(Integer[] projectIds) {
        this.projectIds = projectIds;
    }

    public boolean isProjectBase() {
        return isProjectBase;
    }

    public void setProjectBase(boolean projectBase) {
        isProjectBase = projectBase;
    }

    public Boolean isAllocatedToPO() {
        return isAllocatedToPO == null ? false : isAllocatedToPO;
    }

    public void setAllocatedToPO(Boolean allocatedToPO) {
        isAllocatedToPO = allocatedToPO;
    }

    public BigDecimal getTotalAllocated() {
        return totalAllocated;
    }

    public void setTotalAllocated(BigDecimal totalAllocated) {
        this.totalAllocated = totalAllocated;
    }

    public PdfTemplateItemList getPdfTemplateList() {
        return pdfTemplateList;
    }

    public void setPdfTemplateList(PdfTemplateItemList pdfTemplateList) {
        this.pdfTemplateList = pdfTemplateList;
    }

    public Integer getPdfTemplateId() {
        return pdfTemplateId;
    }

    public void setPdfTemplateId(Integer pdfTemplateId) {
        this.pdfTemplateId = pdfTemplateId;
    }

    public boolean isFromOldMobile() {
        return fromOldMobile;
    }

    public void setFromOldMobile(boolean fromOldMobile) {
        this.fromOldMobile = fromOldMobile;
    }

    public boolean isCompanyExpense() {
        return isCompanyExpense;
    }

    public void setCompanyExpense(boolean companyExpense) {
        isCompanyExpense = companyExpense;
    }

    public void setCategoriesSelected(boolean categoriesSelected) {
        this.categoriesSelected = categoriesSelected;
    }

    public boolean isCategoriesSelected() {
        return categoriesSelected;
    }

    public ColumnConfigs[] getCustomItemColumns() {
        return customItemColumns;
    }

    public void setCustomItemColumns(ColumnConfigs[] customItemColumns) {
        this.customItemColumns = customItemColumns;
    }

    public boolean hasAccess() {
        return hasAccess;
    }

    public void setAccess(boolean hasAccess) {
        this.hasAccess = hasAccess;
    }

    public BankTransferNumberData getExpenseNumberData() {
        return expenseNumberData;
    }

    public void setExpenseNumberData(BankTransferNumberData expenseNumberData) {
        this.expenseNumberData = expenseNumberData;
    }

    public String getExpenseNumber() {
        return expenseNumber;
    }

    public void setExpenseNumber(String expenseNumber) {
        this.expenseNumber = expenseNumber;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
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

    public Integer getJournalId() {
        return this.journalId;
    }

    public void setJournalId(final Integer journalId) {
        this.journalId = journalId;
    }

    public DateNonConvertable getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(DateNonConvertable createdDate) {
        this.createdDate = createdDate;
    }

    public DateNonConvertable getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(DateNonConvertable updatedDate) {
        this.updatedDate = updatedDate;
    }

    public SelectItem getSaleOrderClient() {
        return saleOrderClient;
    }

    public void setSaleOrderClient(SelectItem saleOrderClient) {
        this.saleOrderClient = saleOrderClient;
    }

    public SelectItem getSaleOrder() {
        return saleOrder;
    }

    public void setSaleOrder(SelectItem saleOrder) {
        this.saleOrder = saleOrder;
    }
}
