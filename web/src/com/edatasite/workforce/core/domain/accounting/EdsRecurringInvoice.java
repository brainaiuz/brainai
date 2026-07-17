package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsUpload;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.pdf.EdsCompanyPdfTemplate;
import com.edatasite.workforce.core.domain.settings.EdsEmailTemplate;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 05.05.2010
 * Time: 20:35:12
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "recurringinvoice")
public class EdsRecurringInvoice extends EdsBaseSaleInvoice {
    //For Send To Client Message
    @Type(type = "text")
    private String emailSubject;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senderId")
    private EdsUser sender;

    private Boolean sendCopyToMe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emailTemplateId")
    private EdsEmailTemplate emailTemplate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pdfTemplateId")
    private EdsCompanyPdfTemplate pdfTemplate;

    private String fromEmail;

    @Column(name = "cc")
    private String cc;


    @Column(name = "bcc")
    private String bcc;

    @Column(name = "toEmails")
    @Type(type = "text")
    private String toEmails;

    @ManyToMany(fetch = FetchType.LAZY)
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "recurring_upload",
            joinColumns = {@JoinColumn(name = "recurring_id")},
            inverseJoinColumns = {@JoinColumn(name = "fileids_id")}
    )
    List<EdsUpload> fileIDs = new ArrayList<>();

    @Column(name = "replyTo")
    private String replyTo;

    @Column(name = "invoicetype")
    private Integer invoiceType;

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public EdsUser getSender() {
        return sender;
    }

    public void setSender(EdsUser sender) {
        this.sender = sender;
    }

    public Boolean getSendCopyToMe() {
        return sendCopyToMe;
    }

    public void setSendCopyToMe(Boolean sendCopyToMe) {
        this.sendCopyToMe = sendCopyToMe;
    }

    public EdsEmailTemplate getEmailTemplate() {
        return emailTemplate;
    }

    public void setEmailTemplate(EdsEmailTemplate emailTemplate) {
        this.emailTemplate = emailTemplate;
    }

    public EdsCompanyPdfTemplate getPdfTemplate() {
        return pdfTemplate;
    }

    public void setPdfTemplate(EdsCompanyPdfTemplate pdfTemplate) {
        this.pdfTemplate = pdfTemplate;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
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

    public String getToEmails() {
        return toEmails;
    }

    public void setToEmails(String toEmails) {
        this.toEmails = toEmails;
    }

    public List<EdsUpload> getFileIDs() {
        return fileIDs;
    }

    public void setFileIDs(List<EdsUpload> fileIDs) {
        this.fileIDs = fileIDs;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public Integer getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(Integer invoiceType) {
        this.invoiceType = invoiceType;
    }
}
