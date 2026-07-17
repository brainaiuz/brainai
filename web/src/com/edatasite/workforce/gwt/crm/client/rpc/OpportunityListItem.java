package com.edatasite.workforce.gwt.crm.client.rpc;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomTableRpc;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryList;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.Key;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.Relational;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TreeSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.CustomTableRpc;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryList;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.Key;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.Relational;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TreeSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.itemtablesettings.ColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.edatasite.workforce.gwt.documents.client.rest.resource.AuditInfoResource;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * User: unni
 * Date: Jul 10, 2009
 * Time: 11:26:03 AM
 */
public class OpportunityListItem extends Relational implements IsSerializable, ListingCustomFields, Key {

    public static final String ACTION = "action";
    public static final String NUMBER = "number";
    public static final String ASSIGNEE_NAME = "assigneeName";
    public static final String BACKUP_ASSIGNEE_NAME = "backupAssigneeName";
    public static final String OPPORTUNITY_NAME = "opportunityName";
    public static final String OPPORTUNITY_CONTACT_NAME = "OPPORTUNITY_CONTACT_NAME";
    public static final String OPPORTUNITY_CONTACT_PHONE = "OPPORTUNITY_CONTACT_PHONE";
    public static final String OPPORTUNITY_CONTACT_EMAIL = "OPPORTUNITY_CONTACT_EMAIL";
    public static final String OPPORTUNITY_LEAD_SOURCE = "OPPORTUNITY_LEAD_SOURCE";
    public static final String AMOUNT = "amount";
    public static final String CURRENCY = "currency";
    public static final String STAGE = "stage";
    public static final String CLOSING_DATE = "closingDate";
    public static final String CREATED_DATE = "createdDate";
    public static final String CREATOR_NAME = "creatorName";
    public static final String UPDATED_DATE = "updatedDate";
    public static final String ACCOUNT_NAME = "accountName";
    public static final String COUNTRY_NAME = "countryName";
    public static final String PROBABILITY = "probability";
    public static final String ISCONVERTEDTOPROJECT = "ISCONVERTEDTOPROJECT";
    public static final String STRING_VALUE = "string_value";
    public static final String NUMBER_VALUE = "double_value";
    public static final String DATE_VALUE = "date_value";
    public static final String CAMPAIGN = "campaign";
    public static final String OPPORTUNITY_ATTACHMENT = "hasAttachments";

    public static final String OBJECT_ID = "objectId";
    public static final String OWNER = "owner";
    public static final ArrayList<String> defaultColumnNames = new ArrayList<>(Arrays.asList(
            NUMBER,
            OPPORTUNITY_NAME,
            AMOUNT,
            STAGE,
            CLOSING_DATE,
            ACCOUNT_NAME,
            ASSIGNEE_NAME
    ));


    private Integer objectId;
    private String opportunityName;
    private String formType;
    private String fromName;
    private LinkedHashMap<String, FormProperty> formProperty;

    private Double amount;
    private Double amountInBaseCurrency;
    private Integer currencyId;
    private String currency;
    private SelectItem[] currencies;
    private Date closingDate;
    private CrmAccountItem crmAccountItem;

    private String owner;
    private Integer ownerID;

    private String imageUrl;

    private String assignee;
    private SelectItem[] assignees;
    private Integer assigneeId;
    private Integer backupAssigneeID;
    private String backupAssignee;

    private String contact;
    private String contactPrimaryEmail;
    private boolean contactEmailOptOut;
    private String contactPrimaryPhone;
    private ContactListItem contactItem;
    private Integer contactId;

    private SelectItem stage;
    private SelectItem project;
    private SelectItem rejectionReason;
    private SelectItem[] stages;
    private ColumnConfigs[] stageHistoryColConf;
    private String stageCode;

    private Float probability;

    private String type;
    private SelectItem[] types;
    private Integer typeId;

    private String nextStep;
    private Double expectedRevenue;

    private String leadSource;
    private SelectItem[] leadSources;
    private Integer leadSourceId;

    private String campaign;
    private Integer campaignId;

    private HistoryList history;

    private OpportunityItem[] items;

    private HashMap<String, ArrayList<CustomTableRpc>> customTableItems = new HashMap<>();

    private boolean isConvertedToProject;
    private boolean draggable = true;
    private boolean allowEdit;

    private NumberData numberData;
    private boolean requireContractUpload;
    private boolean isFromQuickAdd = false;

    private FileItem[] attachments;

    private ArrayList<CompanyCustomFieldItem> customFields;
    private HashMap<String, Object> customFieldsMap;
    private boolean copyLeadDetails = false;

    private AuditInfoResource auditInfoResource;
    private Integer opportunityNameID;
    private Integer nextStepID;
    private Integer amountID;
    private Integer closingDateID;
    private Integer probabilityID;
    private Integer expectedRevenueID;
    private Integer noteID;
    private String creatorName;
    private Date createdDate;
    private Date updatedDate;
    private boolean isConvertedLead = false;
    private Integer creatorID;
    private Integer baseCurrencyID;
    private String baseCurrencyName;
    private BigDecimal exchangeRate;
    private String note;

    private String countryName;

    private Integer fromContactID;
    private Integer rfqId;
    private Integer selectedSubStageId;

    private ArrayList<RelationItem> convertedRelations;

    private ArrayList<HistoryListItem> notes;
    private SelectItem[] templates;
    private TreeSelectItem[] productCategories;
    private SelectItem[] productBrands;
    private Integer taxCalculationType;
    private BigDecimal subTotal;
    private BigDecimal discountTotal;
    private BigDecimal taxTotal;
    private BigDecimal total;
    private BigDecimal totalInBase;
    private BigDecimal quantityTotal;
    private boolean hasAttachments;
    private boolean amountWidgetDisable;
    private boolean closeDateDisable;
    private boolean leadSourceDisable;
    ArrayList<ApproverItemMini> approvers = new ArrayList<>();
    ApproverItemMini currentApprover;
    ApproverItemMini prevApprover;
    ReferenceItem overallStatus;
    private String statusCode;
    private Boolean isApprover;
    private DateNonConvertable approvedDate;
    private SelectItem updater;
    private SelectItem approverEmployee;

    public ArrayList<HistoryListItem> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<HistoryListItem> notes) {
        this.notes = notes;
    }

    public Double getExpectedRevenue() {
        return expectedRevenue;
    }

    public void setExpectedRevenue(Double expectedRevenue) {
        this.expectedRevenue = expectedRevenue;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getOpportunityName() {
        return opportunityName;
    }

    public void setOpportunityName(String opportunityName) {
        this.opportunityName = opportunityName;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount == null ? Double.parseDouble("0") : amount;
    }

    public Double getAmountInBaseCurrency() {
        return amountInBaseCurrency;
    }

    public void setAmountInBaseCurrency(Double amountInBaseCurrency) {
        this.amountInBaseCurrency = amountInBaseCurrency;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public SelectItem[] getCurrencies() {
        return currencies;
    }

    public void setCurrencies(SelectItem[] currencies) {
        this.currencies = currencies;
    }

    public Date getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(Date closingDate) {
        this.closingDate = closingDate;
    }

    public CrmAccountItem getCrmAccountItem() {
        if (crmAccountItem == null) {
            crmAccountItem = new CrmAccountItem();
        }
        return crmAccountItem;
    }

    public SelectItem getApproverEmployee() {
        return approverEmployee;
    }

    public void setApproverEmployee(SelectItem approverEmployee) {
        this.approverEmployee = approverEmployee;
    }

    public SelectItem getUpdater() {
        return updater;
    }

    public void setUpdater(SelectItem updater) {
        this.updater = updater;
    }

    public DateNonConvertable getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(DateNonConvertable approvedDate) {
        this.approvedDate = approvedDate;
    }

    public Boolean getApprover() {
        return isApprover;
    }

    public void setApprover(Boolean approver) {
        isApprover = approver;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public void setCrmAccountItem(CrmAccountItem crmAccountItem) {
        this.crmAccountItem = crmAccountItem;
    }

    public String getAccount() {
        return getCrmAccountItem().getName();
    }

    public void setAccount(String account) {
        getCrmAccountItem().setName(account);
    }

    public String getAccountNumber() {
        return getCrmAccountItem().getNumber();
    }

    public void setAccountNumber(String accountNumber) {
        getCrmAccountItem().setNumber(accountNumber);
    }

    public Integer getAccountId() {
        return getCrmAccountItem().getObjectId();
    }

    public void setAccountId(Integer accountId) {
        getCrmAccountItem().setObjectId(accountId);
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactPrimaryEmail() {
        return contactPrimaryEmail;
    }

    public void setContactPrimaryEmail(String contactPrimaryEmail) {
        this.contactPrimaryEmail = contactPrimaryEmail;
    }

    public boolean isContactEmailOptOut() {
        return contactEmailOptOut;
    }

    public void setContactEmailOptOut(boolean contactEmailOptOut) {
        this.contactEmailOptOut = contactEmailOptOut;
    }

    public String getContactPrimaryPhone() {
        return contactPrimaryPhone;
    }

    public void setContactPrimaryPhone(String contactPrimaryPhone) {
        this.contactPrimaryPhone = contactPrimaryPhone;
    }

    public Integer getContactId() {
        return contactId;
    }

    public void setContactId(Integer contactId) {
        this.contactId = contactId;
    }

    public SelectItem getStage() {
        return stage;
    }

    public void setStage(SelectItem stage) {
        this.stage = stage;
    }

    public String getStageName() {
        return stage != null ? stage.getName() : null;
    }

    public void setStageName(String stageName) {
        if (this.stage == null) {
            stage = new ReferenceItem();
        }
        stage.setName(stageName);
    }

    public SelectItem[] getStages() {
        return stages;
    }

    public void setStages(SelectItem[] stages) {
        this.stages = stages;
    }

    public Integer getStageId() {
        return stage == null ? null : stage.getId();
    }

    public void setStageId(Integer stageId) {
        if (this.stage == null) {
            stage = new ReferenceItem();
        }
        this.stage.setId(stageId);
    }

    public Float getProbability() {
        return probability;
    }

    public void setProbability(Float probability) {
        this.probability = probability;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public SelectItem[] getTypes() {
        return types;
    }

    public void setTypes(SelectItem[] types) {
        this.types = types;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getNextStep() {
        return nextStep;
    }

    public void setNextStep(String nextStep) {
        this.nextStep = nextStep;
    }

    public String getLeadSource() {
        return leadSource;
    }

    public void setLeadSource(String leadSource) {
        this.leadSource = leadSource;
    }

    public SelectItem[] getLeadSources() {
        return leadSources;
    }

    public void setLeadSources(SelectItem[] leadSources) {
        this.leadSources = leadSources;
    }

    public Integer getLeadSourceId() {
        return leadSourceId;
    }

    public void setLeadSourceId(Integer leadSourceId) {
        this.leadSourceId = leadSourceId;
    }

    public String getCampaign() {
        return campaign;
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    public Integer getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(Integer campaignId) {
        this.campaignId = campaignId;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Integer getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(Integer ownerID) {
        this.ownerID = ownerID;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public SelectItem[] getAssignees() {
        return assignees;
    }

    public void setAssignees(SelectItem[] assignees) {
        this.assignees = assignees;
    }

    public Integer getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(Integer assigneeId) {
        this.assigneeId = assigneeId;
    }

    public Integer getBackupAssigneeID() {
        return backupAssigneeID;
    }

    public void setBackupAssigneeID(Integer backupAssigneeID) {
        this.backupAssigneeID = backupAssigneeID;
    }

    public String getBackupAssignee() {
        return backupAssignee;
    }

    public void setBackupAssignee(String backupAssignee) {
        this.backupAssignee = backupAssignee;
    }

    public HistoryList getHistory() {
        return history;
    }

    public void setHistory(HistoryList history) {
        this.history = history;
    }

    public void setCustomFields(ArrayList<CompanyCustomFieldItem> customFields) {
        this.customFields = customFields;
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFields() {
        return customFields;
    }

    public OpportunityItem[] getItems() {
        return items;
    }

    public void setItems(OpportunityItem[] items) {
        this.items = items;
    }

    public boolean isConvertedToProject() {
        return isConvertedToProject;
    }

    public void setConvertedToProject(boolean convertedToProject) {
        isConvertedToProject = convertedToProject;
    }

    public NumberData getNumberData() {
        return numberData;
    }

    public void setNumberData(NumberData numberData) {
        this.numberData = numberData;
    }

    public boolean getRequireContractUpload() {
        return requireContractUpload;
    }

    public void setRequireContractUpload(boolean requireContractUpload) {
        this.requireContractUpload = requireContractUpload;
    }

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public HashMap<String, Object> getCustomFieldsMap() {
        return customFieldsMap;
    }

    public void setCustomFieldsMap(HashMap<String, Object> customFieldsMap) {
        this.customFieldsMap = customFieldsMap;
    }

    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return getCustomFieldsMap().get(columnCodeKey);
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {
        getCustomFieldsMap().put(columnCodeKey, cellValue);
    }

    public boolean isCopyLeadDetails() {
        return copyLeadDetails;
    }

    public void setCopyLeadDetails(boolean copyLeadDetails) {
        this.copyLeadDetails = copyLeadDetails;
    }

    public AuditInfoResource getAuditInfoResource() {
        return auditInfoResource;
    }

    public void setAuditInfoResource(AuditInfoResource auditInfoResource) {
        this.auditInfoResource = auditInfoResource;
    }

    public String getStageCode() {
        return stageCode;
    }

    public void setStageCode(String stageCode) {
        this.stageCode = stageCode;
    }

    public Integer getOpportunityNameID() {
        return opportunityNameID;
    }

    public void setOpportunityNameID(Integer opportunityNameID) {
        this.opportunityNameID = opportunityNameID;
    }

    public Integer getNextStepID() {
        return nextStepID;
    }

    public void setNextStepID(Integer nextStepID) {
        this.nextStepID = nextStepID;
    }

    public Integer getAmountID() {
        return amountID;
    }

    public void setAmountID(Integer amountID) {
        this.amountID = amountID;
    }

    public Integer getClosingDateID() {
        return closingDateID;
    }

    public void setClosingDateID(Integer closingDateID) {
        this.closingDateID = closingDateID;
    }

    public Integer getProbabilityID() {
        return probabilityID;
    }

    public void setProbabilityID(Integer probabilityID) {
        this.probabilityID = probabilityID;
    }

    public Integer getExpectedRevenueID() {
        return expectedRevenueID;
    }

    public void setExpectedRevenueID(Integer expectedRevenueID) {
        this.expectedRevenueID = expectedRevenueID;
    }

    public Integer getNoteID() {
        return noteID;
    }

    public void setNoteID(Integer noteID) {
        this.noteID = noteID;
    }

    @Override
    public Integer getRelationID() {
        return getObjectId();
    }

    @Override
    public String getRelationType() {
        return RelationItem.TYPE_OPPORTUNITY;
    }

    @Override
    public String getRelationName() {
        return getOpportunityName();
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getCreatorID() {
        return creatorID;
    }

    public void setCreatorID(Integer creatorID) {
        this.creatorID = creatorID;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public boolean isConvertedLead() {
        return isConvertedLead;
    }

    public void setConvertedLead(boolean isConvertedLead) {
        this.isConvertedLead = isConvertedLead;
    }

    public Integer getBaseCurrencyID() {
        return baseCurrencyID;
    }

    public void setBaseCurrencyID(Integer baseCurrencyID) {
        this.baseCurrencyID = baseCurrencyID;
    }

    public String getBaseCurrencyName() {
        return baseCurrencyName;
    }

    public void setBaseCurrencyName(String baseCurrencyName) {
        this.baseCurrencyName = baseCurrencyName;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getFromContactID() {
        return fromContactID;
    }

    public void setFromContactID(Integer fromContactID) {
        this.fromContactID = fromContactID;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public Integer getRFQId() {
        return rfqId;
    }

    public void setRfqId(Integer rfqId) {
        this.rfqId = rfqId;
    }

    public String getOpportunityImageUrl() {
        return imageUrl;
    }

    public void setOpportunityImageUrl(String contactImageUrl) {
        this.imageUrl = contactImageUrl;
    }

    @Override
    public String getKey() {
        return "" + getObjectId();
    }

    public HashMap<String, ArrayList<CustomTableRpc>> getCustomTableItems() {
        return this.customTableItems;
    }

    public void setCustomTableItems(final HashMap<String, ArrayList<CustomTableRpc>> customTableItems) {
        this.customTableItems = customTableItems;
    }

    public SelectItem getRejectionReason() {
        return this.rejectionReason;
    }

    public void setRejectionReason(final SelectItem rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public Integer getSelectedSubStageId() {
        return this.selectedSubStageId;
    }

    public void setSelectedSubStageId(final Integer selectedSubStageId) {
        this.selectedSubStageId = selectedSubStageId;
    }

    public String getFormType() {
        return this.formType;
    }

    public void setFormType(final String formType) {
        this.formType = formType;
    }

    public ArrayList<RelationItem> getConvertedRelations() {
        return this.convertedRelations;
    }

    public void setConvertedRelations(final ArrayList<RelationItem> convertedRelations) {
        this.convertedRelations = convertedRelations;
    }

    public SelectItem[] getTemplates() {
        return templates;
    }

    public void setTemplates(SelectItem[] templates) {
        this.templates = templates;
    }

    public TreeSelectItem[] getProductCategories() {
        return productCategories;
    }

    public void setProductCategories(TreeSelectItem[] productCategories) {
        this.productCategories = productCategories;
    }

    public String getFromName() {
        return this.fromName;
    }

    public void setFromName(final String fromName) {
        this.fromName = fromName;
    }

    public LinkedHashMap<String, FormProperty> getFormProperty() {
        return this.formProperty;
    }

    public void setFormProperty(final LinkedHashMap<String, FormProperty> formProperty) {
        this.formProperty = formProperty;
    }

    public void setTaxCalculationType(Integer taxCalculationType) {
        this.taxCalculationType = taxCalculationType;
    }

    public Integer getTaxCalculationType() {
        return taxCalculationType;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getDiscountTotal() {
        return discountTotal;
    }

    public void setDiscountTotal(BigDecimal discountTotal) {
        this.discountTotal = discountTotal;
    }

    public BigDecimal getTaxTotal() {
        return taxTotal;
    }

    public void setTaxTotal(BigDecimal taxTotal) {
        this.taxTotal = taxTotal;
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

    public SelectItem[] getProductBrands() {
        return productBrands;
    }

    public void setProductBrands(SelectItem[] productBrands) {
        this.productBrands = productBrands;
    }

    public BigDecimal getQuantityTotal() {
        return quantityTotal;
    }

    public void setQuantityTotal(BigDecimal quantityTotal) {
        this.quantityTotal = quantityTotal;
    }

    public boolean isDraggable() {
        return this.draggable;
    }

    public void setDraggable(final boolean draggable) {
        this.draggable = draggable;
    }

    public ColumnConfigs[] getStageHistoryColConf() {
        return this.stageHistoryColConf;
    }

    public void setStageHistoryColConf(final ColumnConfigs[] stageHistoryColConf) {
        this.stageHistoryColConf = stageHistoryColConf;
    }

    public boolean isAllowEdit() {
        return this.allowEdit;
    }

    public void setAllowEdit(final boolean allowEdit) {
        this.allowEdit = allowEdit;
    }

    public SelectItem getProject() {
        return this.project;
    }

    public void setProject(final SelectItem project) {
        this.project = project;
    }

    public boolean hasAttachments() {
        return hasAttachments;
    }

    public void setHasAttachments(boolean hasAttachments) {
        this.hasAttachments = hasAttachments;
    }

    public boolean isAmountWidgetDisable() {
        return this.amountWidgetDisable;
    }

    public void setAmountWidgetDisable(final boolean amountWidgetDisable) {
        this.amountWidgetDisable = amountWidgetDisable;
    }

    public boolean isCloseDateDisable() {
        return this.closeDateDisable;
    }

    public void setCloseDateDisable(final boolean closeDateDisable) {
        this.closeDateDisable = closeDateDisable;
    }

    public boolean isLeadSourceDisable() {
        return this.leadSourceDisable;
    }

    public void setLeadSourceDisable(final boolean leadSourceDisable) {
        this.leadSourceDisable = leadSourceDisable;
    }

    public ContactListItem getContactItem() {
        return this.contactItem;
    }

    public void setContactItem(final ContactListItem contactItem) {
        this.contactItem = contactItem;
    }

    public boolean isFromQuickAdd() {
        return isFromQuickAdd;
    }

    public void setFromQuickAdd(boolean fromQuickAdd) {
        isFromQuickAdd = fromQuickAdd;
    }

    public ArrayList<ApproverItemMini> getApprovers() {
        if (approvers == null) {
            approvers = new ArrayList<>();
        }
        return approvers;
    }

    public void setApprovers(ArrayList<ApproverItemMini> approvers) {
        this.approvers = approvers;
    }

    public void addApprover(ApproverItemMini item) {
        getApprovers().add(item);
    }

    public ApproverItemMini getFirstApprover() {
        return getApprovers().size() > 0 ? getApprovers().get(0) : null;
    }

    public ApproverItemMini getLastApprover() {
        return getApprovers().size() > 0 ? getApprovers().get(getApprovers().size() - 1) : null;
    }

    public ApproverItemMini getCurrentApprover() {
        return currentApprover;
    }

    public ReferenceItem getCurrentStatus() {
        if (getCurrentApprover() != null) {
            return getCurrentApprover().getStatus();
        }
        return null;
    }

    public void setCurrentApprover(ApproverItemMini currentApprover) {
        this.currentApprover = currentApprover;
    }

    public ApproverItemMini getPrevApprover() {
        return prevApprover;
    }

    public void setPrevApprover(ApproverItemMini prevApprover) {
        this.prevApprover = prevApprover;
    }

    public ReferenceItem getOverallStatus() {
        return overallStatus;
    }

    public void setOverallStatus(ReferenceItem overallStatus) {
        this.overallStatus = overallStatus;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (ApproverItemMini approver : approvers) {
            s.append(approver.toString()).append("\n");
        }
        return s.toString();
    }
}