/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/5/7 6:57:46                                                                                             *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.Email;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;
import java.util.LinkedHashSet;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Mar 19, 2010
 * Time: 4:45:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmailAccountItem implements IsSerializable {

    public final static String OWNER = "OWNER";
    public final static String EMAIL = "EMAIL";
    public final static String STATUS = "STATUS";
    public final static String ERROR = "ERROR";
    public final static String COMPANY_EMAIL = "COMPANY_EMAIL";
    //Email Filters
    public static final String DELIMITR = ";;";
    public static final String ROW_DELIMITR = ":;:;";

    public static final int ERROR_CREDENTIAL = 1;
    public static final int ERROR_COULDNOTCONNECT = 2;
    public static final int ERROR_CONNECTIONTIMEDOUT = 3;
    public static final int ERROR_UNKNOWNHOSTEXCEPTION = 4;
    public static final int ERROR_CONNECTIONREFUSED = 5;
    public static final int ERROR_CANTSENDEMAIL = 6;
    public static final int ERROR_SMTP_SERVER = 7;
    public static final int SETUP_CORPORATE_EMAIL_NOT_SETUP_USER_EMAIL = 14;
    public static final int SETUP_USER_EMAIL_NOT_SETUP_CORPORATE_EMAIL = 15;
    public static final int ERROR_DUBLICATE_EMAIL = 16;
    public static final int ERROR_FREE_TRIAL = 17;
    public static final int ERROR_CREDENTIAL_CHECK_BROWSER = 18;

    private Integer objectID;
    private Integer ownerID;
    private String owner;
    private String email;
    private String fromName;

    private String userName;
    private String password;
    private String imapHost;
    private Integer imapPort;
    private String smtpHost;
    private Integer smtpPort;
    private boolean smtpAuth;

    private String refreshToken;
    private String provider;

    private Date startDate;
    private boolean active;
    private boolean defaultEmail;
    private boolean companyEmail;
    private boolean saveCopyToSentFolder;
    private String errorOfInactivation;
    private Long unreadCount = 0L;
    private LinkedHashSet<Email> lastEmails;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(Integer ownerID) {
        this.ownerID = ownerID;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getImapHost() {
        return imapHost;
    }

    public void setImapHost(String imapHost) {
        this.imapHost = imapHost;
    }

    public Integer getImapPort() {
        return imapPort;
    }

    public void setImapPort(Integer imapPort) {
        this.imapPort = imapPort;
    }

    public Integer getSmtpPort() {
        return smtpPort;
    }

    public void setSmtpPort(Integer smtpPort) {
        this.smtpPort = smtpPort;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getSmtpHost() {
        return smtpHost;
    }

    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    public boolean isDefaultEmail() {
        return defaultEmail;
    }

    public void setDefaultEmail(boolean defaultEmail) {
        this.defaultEmail = defaultEmail;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public boolean isSmtpAuth() {
        return smtpAuth;
    }

    public void setSmtpAuth(boolean smtpAuth) {
        this.smtpAuth = smtpAuth;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public boolean isCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(boolean companyEmail) {
        this.companyEmail = companyEmail;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public boolean isSaveCopyToSentFolder() {
        return saveCopyToSentFolder;
    }

    public void setSaveCopyToSentFolder(boolean saveCopyToSentFolder) {
        this.saveCopyToSentFolder = saveCopyToSentFolder;
    }

    public String getErrorOfInactivation() {
        return errorOfInactivation;
    }

    public void setErrorOfInactivation(String errorOfInactivation) {
        this.errorOfInactivation = errorOfInactivation;
    }

    public Long getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(Long unreadCount) {
        this.unreadCount = unreadCount;
    }

    public LinkedHashSet<Email> getLastEmails() {
        if(lastEmails == null){
            lastEmails = new LinkedHashSet<>();
        }
        return lastEmails;
    }

    public void setLastEmails(LinkedHashSet<Email> lastEmails) {
        this.lastEmails = lastEmails;
    }
}
