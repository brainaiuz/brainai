package com.workforcetrack.mobile.rpc.crm;

import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;
import com.workforcetrack.mobile.rpc.attachment.MFileResource;
import com.workforcetrack.mobile.rpc.base.WebServiceUtils;
import com.workforcetrack.mobile.rpc.calendar.MAppointment;
import com.workforcetrack.mobile.rpc.client.MSelectItem;
import com.workforcetrack.mobile.rpc.messageCenter.MRelationItem;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sancho
 * Date: 01.10.11
 * Time: 13:51
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MCaseItem {

    private Integer objectID;
    private String caseNumber;
    private String subject;

    private String firstName;
    private String lastName;
    private String company;
    private String email;
    private String phone;
    private String fax;

    private String description;
    private String internalComment;
    private Date createdDate;

    private Date lastUpdatedDate;
    private String type;
    private String lead;

    //DROP_DOWN Fields
    private List<MSelectItem> types;
    private List<MSelectItem> resolvers;
    private List<MSelectItem> origins;
    private List<MSelectItem> statuses;
    private List<MSelectItem> priorities;
    private List<MSelectItem> reasons;
    private List<MSelectItem> assignees;


    private Integer caseOriginID;
    private Integer resolverID;
    private Integer statusID;
    private Integer priorityID;
    private Integer reasonID;
    private Integer assigneeID;

    //FOR OUTLOOK
    private String caseOrigin;
    private String reportedBy;
    private String reportedByName;
    private String resolver;
    private String assignee;
    private String status;
    private String priority;
    private String reason;

    private Integer typeID;
    private Boolean billable;
    private List<MHistoryListItem> notes;
    private List<MFileResource> attachments;
    private List<MRelationItem> relations;
    private List<MAppointment> activities;
    private List<MAuditInfo> statusHistories;
    private MEmailItem caseEmail;
    private Integer reportedByID;


    public MCaseItem() {

    }

    public static MCaseItem convertToExcel(CaseItem caseItem, boolean isForList) {
        MCaseItem resultItem = new MCaseItem();
        if (caseItem == null) {
            return resultItem;
        }
        resultItem.setObjectID(caseItem.getObjectId());
        resultItem.setCaseNumber(caseItem.getCaseNumber());
        resultItem.setAssignee(caseItem.getCaseAssigneeName());
        resultItem.setSubject(caseItem.getSubject());
        resultItem.setDescription(WebServiceUtils.removeHtmlTags(caseItem.getDescription()));
        resultItem.setCaseOrigin(caseItem.getCaseOrigin());
        resultItem.setStatus(caseItem.getStatus().getId() != null ? caseItem.getStatus().getName() : null);
        resultItem.setType(caseItem.getType());
        resultItem.setReportedBy(caseItem.getReportedBy());
        resultItem.setLead(caseItem.getLead());
        resultItem.setPriority(caseItem.getPriority());
        resultItem.setReason(caseItem.getCaseReason());
        resultItem.setResolver(caseItem.getResolverName());


        if (caseItem.getLeadId() != null && caseItem.getLead() != null) {
            resultItem.setReportedBy(CrmConstants.CRM_LEAD);
            resultItem.setReportedByName(caseItem.getLead());
        } else if (caseItem.getAccountId() != null && caseItem.getAccountName() != null) {
            resultItem.setReportedBy(CrmConstants.CRM_ACCOUNT);
            resultItem.setReportedByName(caseItem.getAccountName());
        } else if (caseItem.getCrmContactID() != null && caseItem.getCrmContact() != null) {
            resultItem.setReportedBy(CrmConstants.CRM_CONTACT);
            resultItem.setReportedByName(caseItem.getCrmContact());
        } else {
            resultItem.setReportedBy("Other");
            resultItem.setLastName(caseItem.getLastName());
            resultItem.setFirstName(caseItem.getFirstName());
        }

        //DROP _ DOWN
        if (!isForList) {
            resultItem.setReasons(WebServiceUtils.getAsMSelectItemList(caseItem.getCaseReasons()));
            resultItem.setTypes(WebServiceUtils.getAsMSelectItemList(caseItem.getTypes()));
            resultItem.setPriorities(WebServiceUtils.getAsMSelectItemList(caseItem.getPriorities()));
            resultItem.setOrigins(WebServiceUtils.getAsMSelectItemList(caseItem.getCaseOrigins()));
            resultItem.setStatuses(WebServiceUtils.getAsMSelectItemList(caseItem.getStatusItems()));
        }

        return resultItem;
    }

    public static MCaseItem convertToMobile(CaseItem caseItem, boolean isForEdit) {
        MCaseItem resultItem = new MCaseItem();
        if (caseItem == null) {
            return resultItem;
        }
        resultItem.setObjectID(caseItem.getObjectId());
        resultItem.setCaseNumber(caseItem.getCaseNumber());
        resultItem.setAssigneeID(caseItem.getCaseAssigneeId());
        resultItem.setAssignee(caseItem.getCaseAssigneeName());
        resultItem.setSubject(caseItem.getSubject());
        resultItem.setDescription(WebServiceUtils.removeHtmlTags(caseItem.getDescription()));
        resultItem.setCaseOrigin(caseItem.getCaseOrigin());
        resultItem.setCaseOriginID(caseItem.getCaseOriginId());
        if (caseItem.getStatus() != null) {
            resultItem.setStatus(caseItem.getStatus().getName());
            resultItem.setStatusID(caseItem.getStatus().getId());
        }

        resultItem.setTypeID(caseItem.getTypeId());
        resultItem.setType(caseItem.getType());
        resultItem.setPriorityID(caseItem.getPriorityId());
        resultItem.setPriority(caseItem.getPriority());
        resultItem.setReason(caseItem.getCaseReason());
        resultItem.setReasonID(caseItem.getCaseReasonId());
        resultItem.setResolver(caseItem.getResolverName());
        resultItem.setResolverID(caseItem.getResolverId());

        if (caseItem.getLeadId() != null && caseItem.getLead() != null) {
            resultItem.setReportedBy(CrmConstants.CRM_LEAD);
            resultItem.setReportedByName(caseItem.getLead());
            resultItem.setReportedByID(caseItem.getLeadId());
        } else if (caseItem.getAccountId() != null && caseItem.getAccountName() != null) {
            resultItem.setReportedBy(CrmConstants.CRM_ACCOUNT);
            resultItem.setReportedByName(caseItem.getAccountName());
            resultItem.setReportedByID(caseItem.getAccountId());
        } else if (caseItem.getCrmContactID() != null && caseItem.getCrmContact() != null) {
            resultItem.setReportedBy(CrmConstants.CRM_CONTACT);
            resultItem.setReportedByName(caseItem.getCrmContact());
            resultItem.setReportedByID(caseItem.getCrmContactID());
        } else {
            resultItem.setReportedBy(caseItem.getReportedBy());
            resultItem.setReportedByName(caseItem.getReportedByName());
            resultItem.setLastName(caseItem.getLastName());
            resultItem.setFirstName(caseItem.getFirstName());
            resultItem.setCompany(caseItem.getCompany());
            resultItem.setEmail(caseItem.getEmail());
            resultItem.setFax(caseItem.getFax());
            resultItem.setPhone(caseItem.getPhone());
        }

        resultItem.setRelations(MRelationItem.convertToMobile(caseItem.getRelations()));

        //DROP _ DOWN
        if (isForEdit) {
            resultItem.setReasons(WebServiceUtils.getAsMSelectItemList(caseItem.getCaseReasons()));
            resultItem.setTypes(WebServiceUtils.getAsMSelectItemList(caseItem.getTypes()));
            resultItem.setPriorities(WebServiceUtils.getAsMSelectItemList(caseItem.getPriorities()));
            resultItem.setOrigins(WebServiceUtils.getAsMSelectItemList(caseItem.getCaseOrigins()));
            resultItem.setStatuses(WebServiceUtils.getAsMSelectItemList(caseItem.getStatusItems()));
        }

        return resultItem;
    }

    public MCaseItem(CaseItem caseItem) {
        this.objectID = caseItem.getObjectId();
        this.caseNumber = caseItem.getCaseNumber();
        this.subject = caseItem.getSubject();
        this.firstName = caseItem.getFirstName();
        this.lastName = caseItem.getLastName();
        this.company = caseItem.getCompany();
        this.email = caseItem.getEmail();
        this.phone = caseItem.getPhone();
        this.fax = caseItem.getFax();
        this.description = caseItem.getDescription();
        this.internalComment = caseItem.getInternalComment();
        this.createdDate = caseItem.getCreatedDate();
        this.lastUpdatedDate = caseItem.getLastUpdatedDate();
        this.assignee = caseItem.getCaseAssigneeName();
        this.status = caseItem.getStatus().getId() != null ? caseItem.getStatus().getName() : null;
        this.priority = caseItem.getPriority();
        this.resolver = caseItem.getResolverName();
        this.caseOrigin = caseItem.getCaseOrigin();
        this.reportedBy = caseItem.getReportedBy();
        this.reason = caseItem.getCaseReason();
    }

    public CaseItem convertToCaseItem(CaseItem item) {
        if (item == null) {
            item = new CaseItem();
        }
        item.setObjectId(this.objectID);
        item.setCaseNumber(this.caseNumber);
        item.setSubject(this.subject);
        item.setFirstName(this.firstName);
        item.setLastName(this.lastName);
        item.setCompany(this.company);
        item.setEmail(this.email);
        item.setPhone(this.phone);
        item.setFax(this.fax);
        item.setDescription(this.description);
        item.setInternalComment(this.internalComment);
        if (getAttachments() != null && getAttachments().size() > 0) {
            List<FileItem> attachments = new ArrayList<>();
            for (MFileResource fileResource : getAttachments()) {
                FileItem fileItem = new FileItem();
                fileItem.setId(fileResource.getObjectID());
                fileItem.setFileName(fileResource.getName());
                attachments.add(fileItem);
            }
            item.setAttachments(attachments.toArray(new FileItem[]{}));
        }

        if (getNotes() != null && getNotes().size() > 0) {
            item.setNotes(new ArrayList<>());
            for (MHistoryListItem note : getNotes()) {
                item.getNotes().add(note.convertFromMobile(null));
            }
        }

        item.setRelations(MRelationItem.convertFromToMobile(getRelations()));

        return item;
    }

    public CaseItem convertFromExcel(CaseItem caseItem) {
        if (caseItem == null) {
            caseItem = new CaseItem();
        }
        caseItem.setObjectId(getObjectID());
        caseItem.setSubject(getSubject());
        caseItem.setDescription(getDescription());
        caseItem.setCaseOrigin(getCaseOrigin());
        caseItem.setCaseOriginId(getCaseOriginID());
        caseItem.setCaseAssigneeId(getAssigneeID());
        caseItem.setStatus(getStatusID() != null ? new SelectItem(getStatusID()) : null);
        caseItem.setPriorityId(getPriorityID());
        caseItem.setCaseReasonId(getReasonID());
        caseItem.setResolverId(getResolverID());


        return caseItem;
    }

    public CaseItem convertFromMobile(CaseItem item) {
        if (item == null) {
            item = new CaseItem();
        }
        item.setObjectId(getObjectID());
        item.setCaseNumber(getCaseNumber());
        item.setCaseAssigneeId(getAssigneeID());
        item.setCaseAssigneeName(getAssignee());
        item.setSubject(getSubject());
        item.setDescription(getDescription());
        item.setCaseOrigin(getCaseOrigin());
        item.setCaseOriginId(getCaseOriginID());
        item.setStatus(new SelectItem(getStatusID(), getStatus()));

        item.setTypeId(getTypeID());
        item.setType(getType());
        item.setPriorityId(getPriorityID());
        item.setPriority(getPriority());
        item.setCaseReason(getReason());
        item.setCaseReasonId(getReasonID());
        item.setResolverName(getResolver());
        item.setResolverId(getResolverID());

        item.setLeadId(null);
        item.setAccountId(null);
        item.setCrmContactID(null);
        item.setLead(null);
        item.setAccountName(null);
        item.setCrmContact(null);

        item.setCompany(null);
        item.setLastName(null);
        item.setFirstName(null);
        item.setPhone(null);
        item.setFax(null);
        item.setEmail(null);

        if (getReportedByID() != null && getReportedBy() != null) {
            if (getReportedBy().equalsIgnoreCase(CrmConstants.CRM_LEAD)) {
                item.setLead(getReportedByName());
                item.setLeadId(getReportedByID());
            } else if (getReportedBy().equalsIgnoreCase(CrmConstants.CRM_ACCOUNT)) {
                item.setAccountId(getReportedByID());
                item.setAccountName(getReportedByName());
            } else if (getReportedBy().equalsIgnoreCase(CrmConstants.CRM_CONTACT)) {
                item.setCrmContactID(getReportedByID());
                item.setCrmContact(getReportedByName());
            } else if (getReportedBy().equalsIgnoreCase("Other")) {
                item.setLastName(getLastName());
                item.setFirstName(getFirstName());
                item.setCompany(getCompany());
                item.setPhone(getPhone());
                item.setFax(getFax());
                item.setEmail(getEmail());
            }
        }

        if (getAttachments() != null && getAttachments().size() > 0) {
            List<FileItem> attachments = new ArrayList<>();
            for (MFileResource fileResource : getAttachments()) {
                FileItem fileItem = new FileItem();
                fileItem.setId(fileResource.getObjectID());
                fileItem.setFileName(fileResource.getName());
                attachments.add(fileItem);
            }
            item.setAttachments(attachments.toArray(new FileItem[]{}));
        }

        if (getNotes() != null && getNotes().size() > 0) {
            item.setNotes(new ArrayList<>());
            for (MHistoryListItem note : getNotes()) {
                item.getNotes().add(note.convertFromMobile(null));
            }
        }

        item.setRelations(MRelationItem.convertFromToMobile(getRelations()));

        return item;
    }


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInternalComment() {
        return internalComment;
    }

    public void setInternalComment(String internalComment) {
        this.internalComment = internalComment;
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

    public String getCaseOrigin() {
        return caseOrigin;
    }

    public void setCaseOrigin(String caseOrigin) {
        this.caseOrigin = caseOrigin;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    public String getResolver() {
        return resolver;
    }

    public void setResolver(String resolver) {
        this.resolver = resolver;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLead() {
        return lead;
    }

    public void setLead(String lead) {
        this.lead = lead;
    }

    public List<MSelectItem> getTypes() {
        return types;
    }

    public void setTypes(List<MSelectItem> types) {
        this.types = types;
    }

    public List<MSelectItem> getResolvers() {
        return resolvers;
    }

    public void setResolvers(List<MSelectItem> resolvers) {
        this.resolvers = resolvers;
    }

    public List<MSelectItem> getOrigins() {
        return origins;
    }

    public void setOrigins(List<MSelectItem> origins) {
        this.origins = origins;
    }

    public List<MSelectItem> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<MSelectItem> statuses) {
        this.statuses = statuses;
    }

    public List<MSelectItem> getPriorities() {
        return priorities;
    }

    public void setPriorities(List<MSelectItem> priorities) {
        this.priorities = priorities;
    }

    public List<MSelectItem> getReasons() {
        return reasons;
    }

    public void setReasons(List<MSelectItem> reasons) {
        this.reasons = reasons;
    }

    public List<MSelectItem> getAssignees() {
        return assignees;
    }

    public void setAssignees(List<MSelectItem> assignees) {
        this.assignees = assignees;
    }

    public Integer getCaseOriginID() {
        return caseOriginID;
    }

    public void setCaseOriginID(Integer caseOriginID) {
        this.caseOriginID = caseOriginID;
    }

    public Integer getResolverID() {
        return resolverID;
    }

    public void setResolverID(Integer resolverID) {
        this.resolverID = resolverID;
    }

    public Integer getStatusID() {
        return statusID;
    }

    public void setStatusID(Integer statusID) {
        this.statusID = statusID;
    }

    public Integer getPriorityID() {
        return priorityID;
    }

    public void setPriorityID(Integer priorityID) {
        this.priorityID = priorityID;
    }

    public Integer getReasonID() {
        return reasonID;
    }

    public void setReasonID(Integer reasonID) {
        this.reasonID = reasonID;
    }

    public Integer getAssigneeID() {
        return assigneeID;
    }

    public void setAssigneeID(Integer assigneeID) {
        this.assigneeID = assigneeID;
    }

    public String getReportedByName() {
        return reportedByName;
    }

    public void setReportedByName(String reportedByName) {
        this.reportedByName = reportedByName;
    }

    public List<MHistoryListItem> getNotes() {
        return notes;
    }

    public void setNotes(List<MHistoryListItem> notes) {
        this.notes = notes;
    }

    public Boolean getBillable() {
        return billable;
    }

    public void setBillable(Boolean billable) {
        this.billable = billable;
    }

    public List<MFileResource> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<MFileResource> attachments) {
        this.attachments = attachments;
    }

    public List<MRelationItem> getRelations() {
        return relations;
    }

    public void setRelations(List<MRelationItem> relations) {
        this.relations = relations;
    }

    public Integer getTypeID() {
        return typeID;
    }

    public void setTypeID(Integer typeID) {
        this.typeID = typeID;
    }

    public List<MAppointment> getActivities() {
        return activities;
    }

    public void setActivities(List<MAppointment> activities) {
        this.activities = activities;
    }

    public List<MAuditInfo> getStatusHistories() {
        return statusHistories;
    }

    public void setStatusHistories(List<MAuditInfo> statusHistories) {
        this.statusHistories = statusHistories;
    }

    public MEmailItem getCaseEmail() {
        return caseEmail;
    }

    public void setCaseEmail(MEmailItem caseEmail) {
        this.caseEmail = caseEmail;
    }

    public Integer getReportedByID() {
        return reportedByID;
    }

    public void setReportedByID(Integer reportedByID) {
        this.reportedByID = reportedByID;
    }
}
