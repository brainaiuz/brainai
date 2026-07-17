package com.edatasite.shared.massmailler;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.shared.mail.Upload;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.utils.EdsContextParams;

import java.io.Serializable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 30-Jan-2010
 * Time: 16:58:56
 */
public class MassMailerBody implements Serializable {

    private String fullName;
    private String from;
    private String replyTo;
    private String cc;
    private String bcc;
    private String subject;
    private String preheader;
    private String message;
    private boolean isHtml;
    private List<Upload> files;
    private List<Integer> fileHeaderIds;
    //Mass Mail Settings
    private String host;
    private String port;
    private String login;
    private String password;
    private String bouncerEmail;
    private String abuseEmail;
    private String unsubscribeText;
    private String unsubscribeHTML;
    private String tolerateText;
    private String tolerateHTML;
    private boolean smtpAuth;

    static {

    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPreheader() {
        return preheader;
    }

    public void setPreheader(String preheader) {
        this.preheader = preheader;
    }

    public String getUnsubscribeText() {
        return unsubscribeText != null ? unsubscribeText : "\n\n\n\nClick here to unsubscribe  http://{unsubscribeurl}/unsubscribe{encrypted}\n\n";
    }

    public void setUnsubscribeText(String unsubscribeText) {
        this.unsubscribeText = unsubscribeText;
    }

    public String getUnsubscribeHTML() {
        return unsubscribeHTML != null ? unsubscribeHTML : "<br><br><br><hr/><a href=\"http://{unsubscribeurl}/unsubscribe{encrypted}\" style=\"font-size:12px\"> Click here </a>to unsubscribe<br><br>";
    }

    public void setUnsubscribeHTML(String unsubscribeHTML) {
        this.unsubscribeHTML = unsubscribeHTML;
    }

    public String getTolerateText() {
        return tolerateText != null ? tolerateText : "At {productname}, we do not tolerate spam. If you suspect that any of our clients are spamming, please contact us at {abuseemail}";
    }

    public void setTolerateText(String tolerateText) {
        this.tolerateText = tolerateText;
    }

    public String getTolerateHTML() {
        return tolerateHTML != null ? tolerateHTML : "At {productname}, we do not tolerate spam. If you suspect that any of our clients are spamming, please contact us at <a href=\"mailto:{abuseemail}\">{abuseemail}</a>";
    }

    public void setTolerateHTML(String tolerateHTML) {
        this.tolerateHTML = tolerateHTML;
    }

    public boolean isHtml() {
        return isHtml;
    }

    public void setHtml(boolean html) {
        isHtml = html;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBouncerEmail() {
        return bouncerEmail;
    }

    public void setBouncerEmail(String bouncerEmail) {
        this.bouncerEmail = bouncerEmail;
    }

    public List<Upload> getFiles() {
        return files;
    }

    public void setFiles(List<Upload> files) {
        this.files = files;
    }

    public List<Integer> getFileHeaderIds() {
        return fileHeaderIds;
    }

    public void setFileHeaderIds(List<Integer> fileHeaderIds) {
        this.fileHeaderIds = fileHeaderIds;
    }

    public String retriveMessage(MassMailerCrmEntityBody crmEntityBody, String encrypted) {
        String msg = "";
        if (isHtml() && getPreheader() != null) {
            msg += "<span style=\"display: none !important; visibility: hidden; opacity: 0; color: transparent; height: 0; width: 0;\">" + getPreheader() + " - </span>";
        }
        msg += getMessage();
        if (crmEntityBody.getEntityID() != null) {
            msg = msg.replace("eid=n", "eid=" + EncryptionHelper.encryptURL(crmEntityBody.getEntityID().toString()));
        }
        if (crmEntityBody.getMsgId() != null) {
            msg = msg.replace("mid=n", "mid=" + EncryptionHelper.encryptURL(crmEntityBody.getMsgId().toString()));
        }
        msg = msg.replaceAll("\\$?\\{\\$?title\\}", getEmptyIfNull(crmEntityBody.getSenderTitle()));
        msg = msg.replaceAll("\\$?\\{\\$?Title\\}", getEmptyIfNull(crmEntityBody.getSenderTitle()));
        msg = msg.replaceAll("\\$?\\{\\$?firstname\\}", getEmptyIfNull(crmEntityBody.getSenderFirstName()));
        msg = msg.replaceAll("\\$?\\{\\$?First Name\\}", getEmptyIfNull(crmEntityBody.getSenderFirstName()));
        msg = msg.replaceAll("\\$?\\{\\$?lastname\\}", getEmptyIfNull(crmEntityBody.getSenderSurname()));
        msg = msg.replaceAll("\\$?\\{\\$?Last Name\\}", getEmptyIfNull(crmEntityBody.getSenderSurname()));
        msg = msg.replaceAll("\\$?\\{\\$?companyname\\}", getEmptyIfNull(crmEntityBody.getSenderCompanyName()));
        msg = msg.replaceAll("\\$?\\{\\$?Company Name\\}", getEmptyIfNull(crmEntityBody.getSenderCompanyName()));
        msg = msg.replaceAll("\\$?\\{\\$?email\\}", getEmptyIfNull(crmEntityBody.getSenderEmail()));
        msg = msg.replaceAll("\\$?\\{\\$?E-mail\\}", getEmptyIfNull(crmEntityBody.getSenderEmail()));
        msg = msg.replaceAll("\\$?\\{\\$?recipienttitle\\}", getEmptyIfNull(crmEntityBody.getRecepientTitle()));
        msg = msg.replaceAll("\\$?\\{\\$?recipientfirstname\\}", getEmptyIfNull(crmEntityBody.getRecipientFirstName()));
        msg = msg.replaceAll("\\$?\\{\\$?recipientlastname\\}", getEmptyIfNull(crmEntityBody.getRecipientLastName()));
        msg = msg.replaceAll("\\$?\\{\\$?recipientfullname\\}", getEmptyIfNull(crmEntityBody.getRecipientFirstName()) + getEmptyIfNull(crmEntityBody.getRecipientLastName()));
        msg = msg.replaceAll("\\$?\\{\\$?recipientcompanyname\\}", getEmptyIfNull(crmEntityBody.getRecipientCompanyName()));
        msg = msg.replaceAll("\\$?\\{\\$?recipientemail\\}", getEmptyIfNull(crmEntityBody.getRecipientEmail()));
        String recepientPhone = crmEntityBody.getRecipientPhone() == null ? "" : crmEntityBody.getRecipientPhone().replace("|", "");
        String recepientMobile = crmEntityBody.getRecipientMobile() == null ? "" : crmEntityBody.getRecipientMobile().replace("|", "");
        msg = msg.replaceAll("\\$?\\{\\$?recipientphone\\}", getEmptyIfNull(recepientPhone));
        msg = msg.replaceAll("\\$?\\{\\$?recipientmobile\\}", getEmptyIfNull(recepientMobile));

        msg = msg.replaceAll("\\$?\\{\\$?sendertitle\\}", getEmptyIfNull(crmEntityBody.getSenderTitle()));
        msg = msg.replaceAll("\\$?\\{\\$?senderfirstname\\}", getEmptyIfNull(crmEntityBody.getSenderFirstName()));
        msg = msg.replaceAll("\\$?\\{\\$?senderlastname\\}", getEmptyIfNull(crmEntityBody.getSenderSurname()));
        msg = msg.replaceAll("\\$?\\{\\$?sendercompanyname\\}", getEmptyIfNull(crmEntityBody.getSenderCompanyName()));
        msg = msg.replaceAll("\\$?\\{\\$?senderemail\\}", getEmptyIfNull(crmEntityBody.getSenderEmail()));
        String senderPhone = crmEntityBody.getSenderPhoneNumber() == null ? "" : crmEntityBody.getSenderPhoneNumber().replace("|", "");
        msg = msg.replaceAll("\\$?\\{\\$?senderphone\\}", getEmptyIfNull(senderPhone));
        msg = msg.replaceAll("\\$?\\{\\$?\\w*\\}", "");  //keraksiz ili ishlatilmagan keylarni o'chirish kerak... LOCHIN so'ragan
        msg = addFooter(msg, encrypted);
        return msg;
    }

    private String addFooter(String message, String encrypted) {
        message += getRetrivedFooterMessage(encrypted);
        return message;
    }

    private String getRetrivedFooterMessage(String encrypted) {
        String footer = isHtml() ? getUnsubscribeHTML() : getUnsubscribeText();
        if (SecurityContext.getCompanyID() == null || SecurityContext.getCompanyID() != 34830) {
            footer += isHtml() ? getTolerateHTML() : getTolerateText();
        }
        if (isHtml()) {
            footer += "<img src = \"http://{unsubscribeurl}/track{encrypted}\" height=1 width=1 />";
        }
        footer = footer.replaceAll("\\$?\\{\\$?unsubscribeurl\\}", getEmptyIfNull(EdsContextParams.getHostname()));
        footer = footer.replaceAll("\\$?\\{\\$?productname\\}", getEmptyIfNull(EdsContextParams.getProductName()));
        footer = footer.replaceAll("\\$?\\{\\$?abuseemail\\}", getEmptyIfNull(getAbuseEmail()));
        footer = footer.replaceAll("\\$?\\{\\$?encrypted\\}", getEmptyIfNull(encrypted));
        footer = footer.replaceAll("\\$?\\{\\$?\\w*\\}", "");  //keraksiz ili ishlatilmagan keylarni o'chirish kerak... LOCHIN so'ragan
        return footer;
    }

    private String getEmptyIfNull(String title) {
        return title == null || "null".equals(title) ? "" : title;
    }

    public String retriveSubject(MassMailerCrmEntityBody crmEntityBody) {
        String msgSubject = getSubject();
        msgSubject = msgSubject.replaceAll("\\$?\\{\\$?sendertitle\\}", getEmptyIfNull(crmEntityBody.getSenderTitle()));
        msgSubject = msgSubject.replaceAll("\\$?\\{\\$?senderfirstname\\}", getEmptyIfNull(crmEntityBody.getSenderFirstName()));
        msgSubject = msgSubject.replaceAll("\\$?\\{\\$?senderlastname\\}", getEmptyIfNull(crmEntityBody.getSenderSurname()));
        msgSubject = msgSubject.replaceAll("\\$?\\{\\$?sendercompanyname\\}", getEmptyIfNull(crmEntityBody.getSenderCompanyName()));
        msgSubject = msgSubject.replaceAll("\\$?\\{\\$?senderemail\\}", getEmptyIfNull(crmEntityBody.getSenderEmail()));
        String senderPhone = crmEntityBody.getSenderPhoneNumber() == null ? "" : crmEntityBody.getSenderPhoneNumber().replace("|", "");
        msgSubject = msgSubject.replaceAll("\\$?\\{\\$?senderphone\\}", getEmptyIfNull(senderPhone));
        msgSubject = msgSubject.replaceAll("\\$?\\{\\$?Title\\}", crmEntityBody.getSenderTitle());
        msgSubject = msgSubject.replaceAll("\\$?\\{\\$?First Name\\}", crmEntityBody.getSenderFirstName());
        msgSubject = msgSubject.replaceAll("\\$?\\{\\$?Last Name\\}", crmEntityBody.getSenderSurname());
        msgSubject = msgSubject.replaceAll("\\$?\\{\\$?Company Name\\}", crmEntityBody.getSenderCompanyName());
        msgSubject = msgSubject.replaceAll("\\$?\\{\\$?E-mail\\}", crmEntityBody.getSenderEmail());
        msgSubject = msgSubject.replaceAll("\\$?\\{\\$?recipientfirstname\\}", crmEntityBody.getRecipientFirstName());
        msgSubject = msgSubject.replaceAll("\\$?\\{\\$?recipientlastname\\}", crmEntityBody.getRecipientLastName());
        msgSubject = msgSubject.replaceAll("\\$?\\{\\$?recipientfullname\\}", getEmptyIfNull(crmEntityBody.getRecipientFirstName()) + " " + getEmptyIfNull(crmEntityBody.getRecipientLastName()));
        msgSubject = msgSubject.replaceAll("\\$?\\{\\$?recipientcompanyname\\}", crmEntityBody.getRecipientCompanyName());
        msgSubject = msgSubject.replaceAll("\\$?\\{\\$?recipientemail\\}", crmEntityBody.getRecipientEmail());
        String recepientPhone = crmEntityBody.getRecipientPhone() == null ? "" : crmEntityBody.getRecipientPhone().replace("|", "");
        msgSubject = msgSubject.replaceAll("\\$?\\{\\$?recipientphone\\}", recepientPhone);
        String recepientMobile = crmEntityBody.getRecipientMobile() == null ? "" : crmEntityBody.getRecipientMobile().replace("|", "");
        msgSubject = msgSubject.replaceAll("\\$?\\{\\$?recipientphone\\}", recepientMobile);
        return msgSubject;
    }

    public String getAbuseEmail() {
        return abuseEmail != null ? abuseEmail : "abuse@kpi.com";
    }

    public void setAbuseEmail(String abuseEmail) {
        this.abuseEmail = abuseEmail;
    }

    public boolean isSmtpAuth() {
        return smtpAuth;
    }

    public void setSmtpAuth(boolean smtpAuth) {
        this.smtpAuth = smtpAuth;
    }
}
