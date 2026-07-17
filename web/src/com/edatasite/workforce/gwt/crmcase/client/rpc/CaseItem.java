package com.edatasite.workforce.gwt.crmcase.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryList;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.Key;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.Relational;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingCustomFields;
import com.edatasite.workforce.gwt.documents.client.rest.resource.AuditInfoResource;
import com.edatasite.workforce.gwt.task.client.rpc.TaskListItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 09-Jul-2009
 * Time: 18:22:40
 * To change this template use File | Settings | File Templates.
 */
public class CaseItem extends Relational implements IsSerializable, ListingCustomFields, Key {
    public static final String CASE_ID = "caseID";
    public static final String CREATED_DATE = "createdDate";
    public static final String LAST_UPDATED_DATE = "lastUpdatedDate";
    public static final String ASSIGNED_TO = "caseAssigneeName";
    public static final String RESOLVER = "resolverName";
    public static final String ASSIGNED_TO_DEPARTMENT = "assignedToDepartment";
    public static final String STATUS = "status";
    public static final String SUBJECT = "subject";
    public static final String PRIORITY = "priority";
    public static final String ORIGIN = "origin";
    public static final String CASE_TYPE = "type";
    public static final String REPORTED_BY = "reportedBy";
    public static final String REPORTED_BY_COMPANY_NAME = "reportedByCompanyName";
    public static final String CASE_REASON = "caseReason";
    public static final String OTHER_REASON = "Other Reason";
    public static final String CASE_ATTACHMENT = "hasAttachments";
    public static final String BILLABLE = "billable";
    public static final String IN_TRASH = "IN_TRASH";
    public static final String BRAND = "Brand";
    public static final String PRODUCT_CATEGORY = "Product category";
    public static final String PRODUCT = "Product";

    public static final String CASE_STATUS_REPLIED = "REPLIED";
    public static final String CASE_STATUS_CLOSED = "CS_CLOSED";
    public static final String CASE_STATUS_RESOLVED = "RESOLVED";
    public static final String INTERNAL_STATUS = "INTERNAL_STATUS";
    public static final String INTERNAL_UPDATED_DATE = "INTERNAL_UPDATED_DATE";
    public static final String TASK_NUMBER = "taskNumber";
    public static final String TASK_STATUS = "taskStatus";

    public static final ArrayList<String> shownColumns = new ArrayList<>(Arrays.asList(
            CASE_ID,
            SUBJECT,
            REPORTED_BY,
            PRIORITY,
            ASSIGNED_TO,
            STATUS
    ));

    private Integer objectId;
    private String caseNumber;
    private String fromName;
    private String subject;
    private Integer filterID;

    private String caseOrigin;
    private Integer caseOriginId;
    private SelectItem[] caseOrigins;
    private String caseOriginCode;

    private String priority;
    private Integer priorityId;
    private Integer prioritySorder;
    private SelectItem[] priorities;
    private String priorityCode;
    private String priorityColor;

    private SelectItem[] types;
    private String type;
    private Integer typeId;
    private String typeCode;

    private String crmContact;
    private Integer crmContactID;
    private SelectItem[] contacts;

    private Integer accountId;
    private String accountName;
    private String accountNumber;
    private SelectItem[] accounts;

    private String lead;
    private Integer leadId;
    private SelectItem[] leads;

    private String potentialName;
    private Integer potentialId;

    private ReferenceItem status;
    private SelectItem[] statusItems;
    private String statusCode;

    private Integer caseAssigneeId;
    private String caseAssigneeName;
    private String caseAssigneeImageUrl;

    private String department;
    private Integer departmentID;

    private String resolverName;
    private Integer resolverId;

    private String caseReason;
    private Integer caseReasonId;
    private SelectItem[] caseReasons;
    private String caseReasonCode;

    private String otherReason;

    private String ccEmails;

    //when other is selected for reportedBy

    private String firstName;
    private String lastName;
    private String company;
    private String email;
    private String reportEmail;
    private String phone;
    private String fax;

    private String internalComment;
    private String description;
    private HistoryList history;
    private Integer entityID;
    private Date createdDate;
    private Date closedDate;
    private Date lastUpdatedDate;
    private boolean timerIsStarted = false;

    private boolean hasAttachments;
    private boolean inTrash;
    private Integer webFormID;
    private String reportedBy;
    private String reportedByName;
    private String reportedByCompanyName;
    private String statusChangedNote;
    private String lastNote;
    private String replyTo;
    private String emailID;
    private Integer trackerID;
    private ArrayList<RelationItem> convertedRelations;
    private ArrayList<Email> caseEmails;
    private LinkedHashMap<String, FormProperty> formProperty;

    private ArrayList<CompanyCustomFieldItem> customFields;
    private HashMap<String, Object> customFieldsMap;
    private boolean addingFromWebForms = false;
    private boolean isAnonim = false;

    private AuditInfoResource auditInfoResource;

    private ArrayList<HistoryListItem> notes;

    private FileItem[] attachments;

    private SelectItem[] internalStatusItems;
    private List<TaskListItem> tasks;
    private Integer internalStatusId;
    private Integer internalStatusSorder;
    private String internalStatusName;
    private Date internalUpdatedDate;
    private Long kanbanOrder;
    private Email lastEmail;

    private Integer brandId;
    private Integer productCategoryId;
    private Integer productId;
    private SelectItem brand;
    private SelectItem productCategory;
    private SelectItem product;

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public ArrayList<HistoryListItem> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<HistoryListItem> notes) {
        this.notes = notes;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getInternalComment() {
        return internalComment;
    }

    public void setInternalComment(String internalComment) {
        this.internalComment = internalComment;
    }

    public SelectItem[] getContacts() {
        return contacts;
    }

    public void setContacts(SelectItem[] contacts) {
        this.contacts = contacts;
    }

    public SelectItem[] getAccounts() {
        return accounts;
    }

    public void setAccounts(SelectItem[] accounts) {
        this.accounts = accounts;
    }

    public SelectItem[] getStatusItems() {
        return statusItems;
    }

    public void setStatusItems(SelectItem[] statusItems) {
        this.statusItems = statusItems;
    }

    public SelectItem[] getCaseOrigins() {
        return caseOrigins;
    }

    public void setCaseOrigins(SelectItem[] caseOrigins) {
        this.caseOrigins = caseOrigins;
    }

    public SelectItem[] getPriorities() {
        return priorities;
    }

    public void setPriorities(SelectItem[] priorities) {
        this.priorities = priorities;
    }

    public SelectItem[] getTypes() {
        return types;
    }

    public void setTypes(SelectItem[] types) {
        this.types = types;
    }

    public SelectItem[] getCaseReasons() {
        return caseReasons;
    }

    public void setCaseReasons(SelectItem[] caseReasons) {
        this.caseReasons = caseReasons;
    }

    public Integer getCaseReasonId() {
        return caseReasonId;
    }

    public void setCaseReasonId(Integer caseReasonId) {
        this.caseReasonId = caseReasonId;
    }

    public Integer getPotentialId() {
        return potentialId;
    }

    public void setPotentialId(Integer potentialId) {
        this.potentialId = potentialId;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getCrmContactID() {
        return crmContactID;
    }

    public void setCrmContactID(Integer crmContactID) {
        this.crmContactID = crmContactID;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCrmContact() {
        return crmContact;
    }

    public void setCrmContact(String crmContact) {
        this.crmContact = crmContact;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getPotentialName() {
        return potentialName;
    }

    public void setPotentialName(String potentialName) {
        this.potentialName = potentialName;
    }

    public ReferenceItem getStatus() {
        if (status == null) {
            status = new ReferenceItem();
        }
        return status;
    }

    public void setStatus(ReferenceItem status) {
        this.status = status;
    }

    public void setStatus(SelectItem status) {
        if (status != null && status instanceof SelectItem && !(status instanceof ReferenceItem)) {
            setStatus(new ReferenceItem(status.getId(), status.getName(), status.getDescription()));
        } else {
            this.status = (ReferenceItem) status;
        }
    }

    public String getCaseReason() {
        return caseReason;
    }

    public void setCaseReason(String caseReason) {
        this.caseReason = caseReason;
    }

    public Integer getPriorityId() {
        return priorityId;
    }

    public void setPriorityId(Integer priorityId) {
        this.priorityId = priorityId;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getCaseOriginId() {
        return caseOriginId;
    }

    public void setCaseOriginId(Integer caseOriginId) {
        this.caseOriginId = caseOriginId;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getSubject() {
        return addCaseNumberToSubject(subject);
    }

    private String addCaseNumberToSubject(String subject) {
        if (subject == null || "".equals(subject.trim())) {
            return getCaseNumber();
        }
        if (getCaseNumber() != null && !"".equals(getCaseNumber())) {
            return subject.matches(".*\\[" + getCaseNumber() + "\\].*") ? subject : subject + "[" + getCaseNumber() + "]";
        }
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCaseOrigin() {
        return caseOrigin;
    }

    public void setCaseOrigin(String caseOrigin) {
        this.caseOrigin = caseOrigin;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLead() {
        return lead;
    }

    public void setLead(String lead) {
        this.lead = lead;
    }

    public Integer getLeadId() {
        return leadId;
    }

    public void setLeadId(Integer leadId) {
        this.leadId = leadId;
    }

    public SelectItem[] getLeads() {
        return leads;
    }

    public void setLeads(SelectItem[] leads) {
        this.leads = leads;
    }

    public String getCaseAssigneeName() {
        return caseAssigneeName;
    }

    public void setCaseAssigneeName(String caseAssigneeName) {
        this.caseAssigneeName = caseAssigneeName;
    }

    public Integer getCaseAssigneeId() {
        return caseAssigneeId;
    }

    public void setCaseAssigneeId(Integer caseAssigneeId) {
        this.caseAssigneeId = caseAssigneeId;
    }

    public String getCaseAssigneeImageUrl() {
        return caseAssigneeImageUrl;
    }

    public void setCaseAssigneeImageUrl(String caseAssigneeImageUrl) {
        this.caseAssigneeImageUrl = caseAssigneeImageUrl;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public Integer getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(Integer departmentID) {
        this.departmentID = departmentID;
    }

    public String getResolverName() {
        return resolverName;
    }

    public void setResolverName(String resolverName) {
        this.resolverName = resolverName;
    }

    public Integer getResolverId() {
        return resolverId;
    }

    public void setResolverId(Integer resolverId) {
        this.resolverId = resolverId;
    }

    public HistoryList getHistory() {
        return history;
    }

    public void setHistory(HistoryList history) {
        this.history = history;
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }

    public Date getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(Date closedDate) {
        this.closedDate = closedDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public boolean isTimerIsStarted() {
        return timerIsStarted;
    }

    public void setTimerIsStarted(boolean timerIsStarted) {
        this.timerIsStarted = timerIsStarted;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getOtherReason() {
        return otherReason;
    }

    public void setOtherReason(String otherReason) {
        this.otherReason = otherReason;
    }

    public boolean hasAttachments() {
        return hasAttachments;
    }

    public void setHasAttachments(boolean hasAttachments) {
        this.hasAttachments = hasAttachments;
    }

    public String getReportedBy() {
        if (getLeadId() != null) {
            return getLead();
        }
        if (getAccountId() != null) {
            return getAccountName();
        }
        if (getCrmContactID() != null) {
            return getCrmContact();
        }
        return reportedBy != null ? reportedBy : "";
    }

    public static ArrayList<Integer> getIDsOnly(HashSet<CaseItem> selectedItems) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (CaseItem item : selectedItems) {
            ids.add(item.getObjectId());
        }
        return ids;
    }

    public void setInTrash(boolean inTrash) {
        this.inTrash = inTrash;
    }

    public boolean isInTrash() {
        return inTrash;
    }

    public String getCcEmails() {
        return ccEmails;
    }

    public void setCcEmails(String ccEmails) {
        this.ccEmails = ccEmails;
    }

    public String getStatusChangedNote() {
        return statusChangedNote;
    }

    public void setStatusChangedNote(String statusChangedNote) {
        this.statusChangedNote = statusChangedNote;
    }

    public String getLastNote() {
        return lastNote;
    }

    public void setLastNote(String lastNote) {
        this.lastNote = lastNote;
    }

    public Integer getWebFormID() {
        return webFormID;
    }

    public void setWebFormID(Integer webFormID) {
        this.webFormID = webFormID;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    public String getReportEmail() {
        return this.reportEmail;
    }

    public void setReportEmail(final String reportEmail) {
        this.reportEmail = reportEmail;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public Integer getTrackerID() {
        return trackerID;
    }

    public void setTrackerID(Integer trackerID) {
        this.trackerID = trackerID;
    }

    public Integer getFilterID() {
        return filterID;
    }

    public void setFilterID(Integer filterID) {
        this.filterID = filterID;
    }

    public HashMap<String, Object> getCustomFieldsMap() {
        return customFieldsMap;
    }

    public void setCustomFieldsMap(HashMap<String, Object> customFieldsMap) {
        this.customFieldsMap = customFieldsMap;
    }

    public boolean isAddingFromWebForms() {
        return addingFromWebForms;
    }

    public void setAddingFromWebForms(boolean addingFromWebForms) {
        this.addingFromWebForms = addingFromWebForms;
    }

    @Override
    public Object getCustomFieldsValue(String columnCodeKey) {
        return getCustomFieldsMap().get(columnCodeKey);
    }

    @Override
    public void setCustomFieldsValue(String columnCodeKey, Object cellValue) {
        getCustomFieldsMap().put(columnCodeKey, cellValue);
    }

    public ArrayList<CompanyCustomFieldItem> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(ArrayList<CompanyCustomFieldItem> customFields) {
        this.customFields = customFields;
    }

    public AuditInfoResource getAuditInfoResource() {
        return auditInfoResource;
    }

    public void setAuditInfoResource(AuditInfoResource auditInfoResource) {
        this.auditInfoResource = auditInfoResource;
    }

    public String getPriorityCode() {
        return priorityCode;
    }

    public void setPriorityCode(String priorityCode) {
        this.priorityCode = priorityCode;
    }

    public String getPriorityColor() {
        return priorityColor;
    }

    public void setPriorityColor(String priorityColor) {
        this.priorityColor = priorityColor;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getCaseReasonCode() {
        return caseReasonCode;
    }

    public void setCaseReasonCode(String caseReasonCode) {
        this.caseReasonCode = caseReasonCode;
    }

    public String getCaseOriginCode() {
        return caseOriginCode;
    }

    public void setCaseOriginCode(String caseOriginCode) {
        this.caseOriginCode = caseOriginCode;
    }

    @Override
    public String getKey() {
        return "" + getObjectId();
    }

    @Override
    public Integer getRelationID() {
        return getObjectId();
    }

    @Override
    public String getRelationType() {
        return RelationItem.TYPE_CASE;
    }

    @Override
    public String getRelationName() {
        return getSubject();
    }

    public String getReportedByName() {
        return reportedByName;
    }

    public void setReportedByName(String reportedByName) {
        this.reportedByName = reportedByName;
    }

    public SelectItem[] getInternalStatusItems() {
        return internalStatusItems;
    }

    public void setInternalStatusItems(SelectItem[] internalStatusItems) {
        this.internalStatusItems = internalStatusItems;
    }

    public Integer getInternalStatusId() {
        return internalStatusId;
    }

    public void setInternalStatusId(Integer internalStatusId) {
        this.internalStatusId = internalStatusId;
    }

    public String getInternalStatusName() {
        return internalStatusName;
    }

    public void setInternalStatusName(String internalStatusName) {
        this.internalStatusName = internalStatusName;
    }

    public Date getInternalUpdatedDate() {
        return internalUpdatedDate;
    }

    public void setInternalUpdatedDate(Date internalUpdatedDate) {
        this.internalUpdatedDate = internalUpdatedDate;
    }

    public Long getKanbanOrder() {
        return kanbanOrder;
    }

    public void setKanbanOrder(Long kanbanOrder) {
        this.kanbanOrder = kanbanOrder;
    }

    public void setLastEmail(Email lastEmail) {
        this.lastEmail = lastEmail;
    }

    public Email getLastEmail() {
        return lastEmail;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public Integer getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Integer productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public void setProduct(SelectItem product) {
        this.product = product;
    }

    public SelectItem getProduct() {
        return product;
    }

    public void setProductCategory(SelectItem productCategory) {
        this.productCategory = productCategory;
    }

    public SelectItem getProductCategory() {
        return productCategory;
    }

    public void setBrand(SelectItem brand) {
        this.brand = brand;
    }

    public SelectItem getBrand() {
        return brand;
    }

    public ArrayList<RelationItem> getConvertedRelations() {
        return this.convertedRelations;
    }

    public void setConvertedRelations(final ArrayList<RelationItem> convertedRelations) {
        this.convertedRelations = convertedRelations;
    }

    public ArrayList<Email> getCaseEmails() {
        return caseEmails;
    }

    public void setCaseEmails(ArrayList<Email> caseEmails) {
        this.caseEmails = caseEmails;
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

    public String getReportedByCompanyName() {
        return reportedByCompanyName;
    }

    public void setReportedByCompanyName(String reportedByCompanyName) {
        this.reportedByCompanyName = reportedByCompanyName;
    }

    public List<TaskListItem> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskListItem> tasks) {
        this.tasks = tasks;
    }

    public boolean isAnonim() {
        return isAnonim;
    }

    public void setAnonim(boolean anonim) {
        isAnonim = anonim;
    }

    public Integer getPrioritySorder() {
        return prioritySorder;
    }

    public void setPrioritySorder(Integer prioritySorder) {
        this.prioritySorder = prioritySorder;
    }

    public Integer getInternalStatusSorder() {
        return internalStatusSorder;
    }

    public void setInternalStatusSorder(Integer internalStatusSorder) {
        this.internalStatusSorder = internalStatusSorder;
    }
}
