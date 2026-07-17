package com.edatasite.workforce.core.domain.settings;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.gwt.profile.client.ui.EmailTemplateConstants;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.Locale;

/**
 * User: Admin
 * Date: 15.03.2010
 * Time: 19:04:37
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "emailtemplate")
public class EdsEmailTemplate extends EdsObject implements EmailTemplateConstants {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "locale")
    private Locale locale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoryId")
    private EdsReference category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id")
    private EdsReference module;

    private Boolean isDefault = false;

    @Column(name = "subject")
    private String subject;

    @Column(name = "fromEmail")
    private String fromEmail;

    @Column(name = "fromUserName")
    private String fromUserName;

    @Column(name = "messagehtml")
    @Type(type = "text")
    private String messageHTML;

    @Column(name = "activityText")
    @Type(type = "text")
    private String activityText;

    @Column(name = "sendEmail")
    private String sendEmail;

    @Column(name = "isCompanyEmailTemplate")
    private String isCompanyEmailTemplate = COMPANY_EMAIL_TEMPLATE;

    private Boolean deleted = false;

    @Column(name = "fromUserId")
    private Integer fromUser;

    @Column(name = "toEmailQuery")
    private String toEmailQuery;

    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "emailTemplate")
    private List<EdsWfpNotification> notifications;

    @Column(name = "replyTo")
    private String replyTo;

    @Column(name = "website_id")
    private Integer websiteID;

    @Column(name = "user_id")
    private Integer userID;

    @Column(name = "external_guid", unique = true)
    private String externalGUID;

    @Column(name = "showinmessagecenter")
    private Boolean showInMessageCenter;

    private Boolean sendSummaryPdf;

    @Column(name = "pdf_template_id")
    private Integer pdfTemplateId;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public EdsReference getModule() {
        return module;
    }

    public void setModule(EdsReference module) {
        this.module = module;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }


    public Boolean isDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getSendEmail() {
        return sendEmail;
    }

    public void setSendEmail(String sendEmail) {
        this.sendEmail = sendEmail;
    }

    public String getCompanyEmailTemplate() {
        return isCompanyEmailTemplate;
    }

    public void setCompanyEmailTemplate(String companyEmailTemplate) {
        isCompanyEmailTemplate = companyEmailTemplate;
    }

    public EdsReference getTemplateCategory() {
        return category;
    }

    public void setTemplateCategory(EdsReference templateCategory) {
        this.category = templateCategory;
    }

    public String getMessageHTML() {
        return messageHTML;
    }

    public void setMessageHTML(String messageHTML) {
        this.messageHTML = messageHTML;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getFromUser() {
        return fromUser;
    }

    public void setFromUser(Integer fromUser) {
        this.fromUser = fromUser;
    }

    public String getToEmailQuery() {
        return toEmailQuery;
    }

    public void setToEmailQuery(String toEmailQuery) {
        this.toEmailQuery = toEmailQuery;
    }

    public String getActivityText() {
        return activityText;
    }

    public void setActivityText(String activityText) {
        this.activityText = activityText;
    }

    public List<EdsWfpNotification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<EdsWfpNotification> notifications) {
        this.notifications = notifications;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public Integer getWebsiteID() {
        return websiteID;
    }

    public void setWebsiteID(Integer websiteID) {
        this.websiteID = websiteID;
    }

    public String getExternalGUID() {
        return externalGUID;
    }

    public void setExternalGUID(String externalGUID) {
        this.externalGUID = externalGUID;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public void showInMessageCenter(Boolean showInMessageCenter) {
        this.showInMessageCenter = showInMessageCenter;
    }

    public Boolean showInMessageCenter() {
        return showInMessageCenter;
    }

    public Boolean getSendSummaryPdf() {
        return sendSummaryPdf != null ? sendSummaryPdf : false;
    }

    public void setSendSummaryPdf(Boolean sendSummaryPdf) {
        this.sendSummaryPdf = sendSummaryPdf;
    }

    public Integer getPdfTemplateId() {
        return pdfTemplateId;
    }

    public void setPdfTemplateId(Integer pdfTemplateId) {
        this.pdfTemplateId = pdfTemplateId;
    }
}
