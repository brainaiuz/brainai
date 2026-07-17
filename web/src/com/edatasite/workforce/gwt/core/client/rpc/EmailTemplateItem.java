package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * User: muratov
 * Date: Mar 19, 2010
 * Time: 4:45:39 PM
 */
public class EmailTemplateItem implements IsSerializable {

    public static final String TEMPLATE_NAME = "templateName";
    public static final String TEMPLATE_SUBJECT = "templateSubject";
    public static final String TEMPLATE_CATEGORY = "templateCategory";
    public static final String TEMPLATE_IS_DEFAULT = "templateIsDefault2";
    public static final String TEMPLATE_ONLY_MINE = "templateOnlyMine";
    public static final String TEMPLATE_MODULE = "templateModule";
    private Integer objectId;
    private String name;
    private String subject;
    private boolean isDefault = false;
    private boolean isOnlyMine = false;
    private boolean sendSummaryPdf = false;
    private Integer categoryId;
    private String categoryName;
    private Integer moduleID;
    private String module;
    private SelectItem[] modules;
    private String fromEmail;
    private String messageHTML;
    private String testEmail;
    private Integer fromUserID;
    private String fromUserName;
    private SelectItem[] fromUsers;
    private String toEmail;
    private String replyTo;
    private Integer companyId;
    private String isCompanyEmailTemplate;
    private String localeCode;
    private Boolean showInMessageCenter;
    private FileItem[] attachments;
    private ArrayList<FileResource> fileResources;
    private boolean test = false;
    private Integer pdfTemplateId;
    private SelectItem[] pdfTemplates;
    private SelectItem[] langugages;
    private SelectItem language;

    private String cc;
    private String bcc;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public String getMessageHTML() {
        return messageHTML;
    }

    public void setMessageHTML(String messageHTML) {
        this.messageHTML = messageHTML;
    }

    public String getTestEmail() {
        return testEmail;
    }

    public void setTestEmail(String testEmail) {
        this.testEmail = testEmail;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getFromUserID() {
        return fromUserID;
    }

    public void setFromUserID(Integer fromUserID) {
        this.fromUserID = fromUserID;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public SelectItem[] getFromUsers() {
        return fromUsers;
    }

    public void setFromUsers(SelectItem[] fromUsers) {
        this.fromUsers = fromUsers;
    }

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanyEmailTemplate() {
        return isCompanyEmailTemplate;
    }

    public void setCompanyEmailTemplate(String companyEmailTemplate) {
        isCompanyEmailTemplate = companyEmailTemplate;
    }

    public FileItem[] getAttachments() {
        return attachments;
    }

    public void setAttachments(FileItem[] attachments) {
        this.attachments = attachments;
    }

    public ArrayList<FileResource> getAttachmentsAsFileResources() {
        return fileResources;
    }

    public ArrayList<FileResource> getFileResources() {
        return fileResources;
    }

    public void setFileResources(ArrayList<FileResource> fileResources) {
        this.fileResources = fileResources;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public boolean isOnlyMine() {
        return isOnlyMine;
    }

    public void setOnlyMine(boolean isOnlyMine) {
        this.isOnlyMine = isOnlyMine;
    }

    public boolean isSendSummaryPdf() {
        return sendSummaryPdf;
    }

    public void setSendSummaryPdf(boolean sendSummaryPdf) {
        this.sendSummaryPdf = sendSummaryPdf;
    }

    public Integer getModuleID() {
        return moduleID;
    }

    public void setModuleID(Integer moduleID) {
        this.moduleID = moduleID;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public SelectItem[] getModules() {
        return modules;
    }

    public void setModules(SelectItem[] modules) {
        this.modules = modules;
    }

    public String getLocaleCode() {
        return localeCode;
    }

    public void setLocaleCode(String localeCode) {
        this.localeCode = localeCode;
    }

    public void showInMessageCenter(Boolean showInMessageCenter) {
        this.showInMessageCenter = showInMessageCenter;
    }

    public Boolean showInMessageCenter() {
        return showInMessageCenter;
    }

    public boolean isTest() {
        return test;
    }

    public void setTest(boolean test) {
        this.test = test;
    }

    public Integer getPdfTemplateId() {
        return pdfTemplateId;
    }

    public void setPdfTemplateId(Integer pdfTemplateId) {
        this.pdfTemplateId = pdfTemplateId;
    }

    public SelectItem[] getPdfTemplates() {
        return pdfTemplates;
    }

    public void setPdfTemplates(SelectItem[] pdfTemplates) {
        this.pdfTemplates = pdfTemplates;
    }

    public SelectItem[] getLangugages() {
        return langugages;
    }

    public void setLangugages(SelectItem[] langugages) {
        this.langugages = langugages;
    }

    public SelectItem getLanguage() {
        return language;
    }

    public void setLanguage(SelectItem language) {
        this.language = language;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }
}
