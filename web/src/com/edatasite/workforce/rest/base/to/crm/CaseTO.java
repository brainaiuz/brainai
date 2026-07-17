package com.edatasite.workforce.rest.base.to.crm;

import com.edatasite.workforce.rest.base.to.NoteTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.IdNameTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.SelectItemTO;
import com.edatasite.workforce.rest.v3.release10.core.to.crm.LeadInformationDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Anvar Akramov on 25/03/2020 7:14 PM
 */
public class CaseTO extends ResponseData {
    private Integer id;
    private String case_number;
    private String subject;
    private String description;
    private IdNameTO type;//One of Problem/Demo Request/Feature Request/Question/Request etc

    private IdNameTO contact;
    private IdNameTO lead;
    private IdNameTO company;
    //when other is selected for reportedBy
    private LeadInformationDTO other;

    private SelectItemTO origin;
    private SelectItemTO reason;
    private String other_reason;

    private SelectItemTO priority;
    private SelectItemTO status;
    private IdNameTO opportunity;

    private IdNameTO assignee;
    private IdNameTO department;

    private IdNameTO resolver;
    private IdNameTO internal_status;

    private Date created_date;
    private Date closed_date;
    private Date internal_updated_date;

    private String notes;

    private ArrayList<Object> custom_fields;
    private List<NoteTO> case_notes;

/*
    private Integer filterID;
    private String potentialName;
    private Integer potentialId;
    private String caseAssigneeImageUrl;
    private String ccEmails;
    private String internalComment;
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
    private String statusChangedNote;
    private String lastNote;
    private String replyTo;
    private String emailID;
    private Integer trackerID;

    private ArrayList<CompanyCustomFieldItem> customFields;
    private HashMap<String, Object> customFieldsMap;
    private boolean addingFromWebForms = false;

    private AuditInfoResource auditInfoResource;

    private FileItem[] attachments;

    private Email lastEmail;

    private Integer brandId;
    private Integer productCategoryId;
    private Integer productId;
    private Integer problemId;
    private SelectItem brand;
    private SelectItem productCategory;
    private SelectItem product;
    private SelectItem problem;*/

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCase_number() {
        return case_number;
    }

    public void setCase_number(String case_number) {
        this.case_number = case_number;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IdNameTO getType() {
        return type;
    }

    public void setType(IdNameTO type) {
        this.type = type;
    }

    public IdNameTO getContact() {
        return contact;
    }

    public void setContact(IdNameTO contact) {
        this.contact = contact;
    }

    public IdNameTO getLead() {
        return lead;
    }

    public void setLead(IdNameTO lead) {
        this.lead = lead;
    }

    public IdNameTO getCompany() {
        return company;
    }

    public void setCompany(IdNameTO company) {
        this.company = company;
    }

    public LeadInformationDTO getOther() {
        return other;
    }

    public void setOther(LeadInformationDTO other) {
        this.other = other;
    }

    public SelectItemTO getOrigin() {
        return origin;
    }

    public void setOrigin(SelectItemTO origin) {
        this.origin = origin;
    }

    public SelectItemTO getReason() {
        return reason;
    }

    public void setReason(SelectItemTO reason) {
        this.reason = reason;
    }

    public String getOther_reason() {
        return other_reason;
    }

    public void setOther_reason(String other_reason) {
        this.other_reason = other_reason;
    }

    public SelectItemTO getPriority() {
        return priority;
    }

    public void setPriority(SelectItemTO priority) {
        this.priority = priority;
    }

    public SelectItemTO getStatus() {
        return status;
    }

    public void setStatus(SelectItemTO status) {
        this.status = status;
    }

    public IdNameTO getOpportunity() {
        return opportunity;
    }

    public void setOpportunity(IdNameTO opportunity) {
        this.opportunity = opportunity;
    }

    public IdNameTO getAssignee() {
        return assignee;
    }

    public void setAssignee(IdNameTO assignee) {
        this.assignee = assignee;
    }

    public IdNameTO getDepartment() {
        return department;
    }

    public void setDepartment(IdNameTO department) {
        this.department = department;
    }

    public IdNameTO getResolver() {
        return resolver;
    }

    public void setResolver(IdNameTO resolver) {
        this.resolver = resolver;
    }

    public IdNameTO getInternal_status() {
        return internal_status;
    }

    public void setInternal_status(IdNameTO internal_status) {
        this.internal_status = internal_status;
    }

    public Date getCreated_date() {
        return created_date;
    }

    public void setCreated_date(Date created_date) {
        this.created_date = created_date;
    }

    public Date getClosed_date() {
        return closed_date;
    }

    public void setClosed_date(Date closed_date) {
        this.closed_date = closed_date;
    }

    public Date getInternal_updated_date() {
        return internal_updated_date;
    }

    public void setInternal_updated_date(Date internal_updated_date) {
        this.internal_updated_date = internal_updated_date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public ArrayList<Object> getCustom_fields() {
        return custom_fields;
    }

    public void setCustom_fields(ArrayList<Object> custom_fields) {
        this.custom_fields = custom_fields;
    }

    public List<NoteTO> getCase_notes() {
        return case_notes;
    }

    public void setCase_notes(List<NoteTO> case_notes) {
        this.case_notes = case_notes;
    }
}
