/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/5/17 8:29:44                                                                                            *
 **********************************************************************************************************************/

package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.customfields.EdsReportDataCustomFields;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.server.domain.ObjectHistory;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 30-May-2009
 * Time: 14:37:36
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "attachment")
public class EdsAttachment extends EdsUpload implements ObjectHistory {

    public static final String _ATTACHMENT_TYPE = "_ATTACHMENT_TYPE";
    public static final String BUG_REPORT_ATTACHMENT = "BUG_REPORT_ATTACHMENT";
    public static final String PUR_INVOICE_ATTACHMENTS = "PUR_INVOICE_ATTACHMENTS";
    public static final String COMPANY_ATTACHMENTS = "COMPANY_ATTACHMENTS";
    public static final String EMPLOYMENT_ATTACHMENTS = "EMPLOYMENT_ATTACHMENTS";
    public static final String EMPLOYEE_ATTACHMENTS = "EMPLOYEE_ATTACHMENTS";
    public static final String _CONTACT_ATTACHMENT = "_CONTACT_ATTACHMENT";
    public static final String LEADS_ATTACHMENT = "LEADS_ATTACHMENT";
    public static final String EMAILS_ATTACHMENT = "EMAILS_ATTACHMENT";
    public static final String CLIENT_ATTACHMENT = "CLIENT_ATTACHMENT";

    public static final String PERSONAL_GOALS_ATTACHMENT = "PERSONAL_GOALS_ATTACHMENT";
    public static final String DEPARTMENT_GOALS_ATTACHMENT = "DEPARTMENT_GOALS_ATTACHMENT";
    public static final String PROJECT_GOALS_ATTACHMENT = "PROJECT_GOALS_ATTACHMENT";
    public static final String BUSINESS_GOALS_ATTACHMENT = "BUSINESS_GOALS_ATTACHMENT";
    public static final String COMPANY_GOALS_ATTACHMENT = "COMPANY_GOALS_ATTACHMENT";

    public static final String NETWORK_LOGO_ATTACHMENT = "NETWORK_LOGO_ATTACHMENT";
    public static final String NETWORK_CONTACT_NEWS_ATTACHMENT = "NETWORK_CONTACT_NEWS_ATTACHMENT";
    public static final String MESSAGE_CENTER_MAIL_ATTACHMENT = "MESSAGE_CENTER_MAIL_ATTACHMENT";
    public static final String CONTACT_COMPANY_IMAGE_LOGO = "CONTACT_COMPANY_IMAGE_LOGO";

    public static final String _EMAIL_TYPES = "EMAIL_TYPES";
    public static final String _INBOX = "INBOX";
    public static final String _OUTBOX = "OUTBOX";
    // Message Center Attachment Type
    public static final String _MESSAGE_CENTER_EMAIL_ATTACHMENT_TYPES = "MESSAGE_CENTER_EMAIL_ATTACHMENT_TYPES";
    public static final String MESSAGE_CENTER_CORPORATE_INBOX = "MESSAGE_CENTER_CORPORATE_INBOX";
    public static final String MESSAGE_CENTER_CORPORATE_SENT = "MESSAGE_CENTER_CORPORATE_SENT";
    public static final String MESSAGE_CENTER_USER_INBOX = "MESSAGE_CENTER_USER_INBOX";
    public static final String MESSAGE_CENTER_USER_SENT = "MESSAGE_CENTER_USER_SENT";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updaterid")
    private EdsUser updater;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorid")
    private EdsUser creator;

    private Date creationTime;

    private String description;
    private Date lastUpdateTime;
    private Long size;

    @Column(name = "isDeleted")
    private Boolean deleted = false;

    private String deletedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryType")
    private EdsReference categoryType;

    @Column(name = "attachmentId")
    private Integer attachmentId;

    @Column(name = "emailID")
    private Integer emailID;

    @Column(name = "caseAttachmentCopied", columnDefinition = "boolean default false")
    private boolean caseAttachmentCopied = false;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "attachment_fields",
            joinColumns = {@JoinColumn(name = "attachmentId")},
            inverseJoinColumns = {@JoinColumn(name = "customFieldid")}
    )
    private Set<EdsReportDataCustomFields> reportDataCustomFields = new HashSet<>();


    public boolean isCaseAttachmentCopied() {
        return caseAttachmentCopied;
    }

    public void setCaseAttachmentCopied(boolean caseAttachmentCopied) {
        this.caseAttachmentCopied = caseAttachmentCopied;
    }

    public EdsReference getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(EdsReference categoryType) {
        this.categoryType = categoryType;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public EdsUser getUpdater() {
        return updater;
    }

    public void setUpdater(EdsUser updater) {
        this.updater = updater;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public void setCreationTime(Date value) {
        creationTime = value;
    }

    public void setCreator(EdsUser value) {
        creator = value;
    }

    public EdsUser getCreator() {
        return creator;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Integer getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(Integer attachmentId) {
        this.attachmentId = attachmentId;
    }

    public Integer getEmailID() {
        return emailID;
    }

    public void setEmailID(Integer emailID) {
        this.emailID = emailID;
    }

    public Set<EdsReportDataCustomFields> getReportDataCustomFields() {
        return reportDataCustomFields;
    }

    public void setReportDataCustomFields(Set<EdsReportDataCustomFields> reportDataCustomFields) {
        this.reportDataCustomFields = reportDataCustomFields;
    }

    public FileItem getRPC() {
        FileItem fileItem = new FileItem();
        fileItem.setEmailID(getEmailID());
        fileItem.setFileName(getOriginalName());
        fileItem.setAttachmentId(getAttachmentId());
        fileItem.setUploadType(getCategoryType() != null ? getCategoryType().getName() : "");
        fileItem.setId(getObjectID());
        fileItem.setAddedBy(getCreator() != null ? getCreator().getName() : "");
        fileItem.setContentType(getContentType());
        fileItem.setDescription(getDescription());
        fileItem.setSize(getSize());
        return fileItem;
    }
}
