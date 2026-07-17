package com.edatasite.workforce.core.domain.settings;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.emailfetching.EdsEmailFolder;
import com.edatasite.workforce.gwt.core.client.rpc.EmailAccountItem;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.messagecenter.client.enumtype.MCFolderType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Apr 30, 2010
 * Time: 7:17:18 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "autoResponse", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class EdsEmailSetting extends EdsObject {

    public final static Integer TLS_PORT = 587;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(nullable = false, columnDefinition = "int4 default 0")
    private Integer attempts = 0;

    @Column(nullable = false, columnDefinition = "int4 default 3")
    private Integer attemptLimit = 3;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "emailHost")
    private String emailHost;

    @Column(name = "emailHostSmtp")
    private String emailHostSMTP;

    @Column(name = "emailPort")
    private Integer emailPort;

    @Column(name = "emailProtocol")
    private String emailProtocol;

    @Column(name = "emailPortSMTP")
    private Integer emailPortSMTP;

    @Column(name = "emailSmtpProtocol")
    private String emailSmtpProtocol;

    @Column(name = "fromName")
    private String fromName;

    @Column(name = "userName")
    private String userName;

    @Column(name = "errorOfInactivation")
    @Type(type = "text")
    private String errorOfInactivation;

    @Column(name = "active", columnDefinition = " boolean default false")
    private boolean active = false;

    @Column(name = "deleted", columnDefinition = " boolean default false")
    private boolean deleted = false;

    @Column(name = "defaultEmail", columnDefinition = " boolean default false")
    private boolean defaultEmail = false;

    @Column(name = "companyEmail", columnDefinition = " boolean default false ")
    private boolean companyEmail = false;

    @Column(name = "smtpAuth", columnDefinition = " boolean default true ")
    private boolean smtpAuth = true;

    @Column(name = "saveCopyToSentFolder", columnDefinition = " boolean default false ")
    private boolean saveCopyToSentFolder = false;

    @Column(name = "startDate")
    private Date startDate;

    @Column(name = "lastFetchingStarted")
    private Date lastFetchingStarted;

    @Column(name = "lastFetchingEnd")
    private Date lastFetchingEnd;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "provider", columnDefinition = "varchar(32) default 'DEFAULT'")
    @Enumerated(value = EnumType.STRING)
    private Provider provider = Provider.DEFAULT;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private EdsUser user;

    @OneToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "emailSetting")
    @Where(clause = "deleted = 'false' or deleted is null")
    @OrderBy("fullName")
    private List<EdsEmailFolder> folders = new ArrayList<>();

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

    public String getEmailSmtpProtocol() {
        return isSecure(getEmailPortSMTP()) ? "smtps" : "smtp";
    }

    public void setEmailSmtpProtocol(String emailSmtpProtocol) {
        this.emailSmtpProtocol = emailSmtpProtocol;
    }

    public Integer getEmailPort() {
        return emailPort;
    }

    public void setEmailPort(Integer emailPort) {
        this.emailPort = emailPort;
    }

    public Integer getEmailPortSMTP() {
        return emailPortSMTP;
    }

    public void setEmailPortSMTP(Integer emailPortSMTP) {
        this.emailPortSMTP = emailPortSMTP;
    }

    public String getEmailHost() {
        return emailHost;
    }

    public void setEmailHost(String emailHost) {
        this.emailHost = emailHost;
    }

    public String getEmailHostSMTP() {
        return emailHostSMTP;
    }

    public void setEmailHostSMTP(String emailHostSMTP) {
        this.emailHostSMTP = emailHostSMTP;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        if (active) {
            setAttempts(0);
        }
        this.active = active;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts == null ? 0 : attempts;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public EdsUser getUser() {
        return user;
    }

    public void setUser(EdsUser user) {
        this.user = user;
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

    public void setErrorOfInactivation(String errorOfInactivation) {
        this.errorOfInactivation = errorOfInactivation;
    }

    public String getErrorOfInactivation() {
        return errorOfInactivation;
    }

    public boolean isCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(boolean companyEmail) {
        this.companyEmail = companyEmail;
    }

    public boolean isDefaultEmail() {
        return defaultEmail;
    }

    public void setDefaultEmail(boolean defaultEmail) {
        this.defaultEmail = defaultEmail;
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

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public List<EdsEmailFolder> getFolders() {
        if (folders == null) {
            folders = new ArrayList<>();
        }
        return folders;
    }

    public void setFolders(List<EdsEmailFolder> folders) {
        this.folders = folders;
    }

    public Integer getAttemptLimit() {
        return attemptLimit;
    }

    public void setAttemptLimit(Integer attemptLimit) {
        this.attemptLimit = attemptLimit;
    }

    public String getSessionKey() {
        return SecurityContext.getCompanyID() + "_" + getObjectID() + "_" + getUserName();
    }

    public Date getLastFetchingStarted() {
        return lastFetchingStarted;
    }

    public void setLastFetchingStarted(Date lastFetchingStarted) {
        this.lastFetchingStarted = lastFetchingStarted;
    }

    public Date getLastFetchingEnd() {
        return lastFetchingEnd;
    }

    public void setLastFetchingEnd(Date lastFetchingEnd) {
        this.lastFetchingEnd = lastFetchingEnd;
    }

    public EmailAccountItem getRPC(EmailAccountItem item) {
        if (item == null) {
            item = new EmailAccountItem();
        }
        item.setObjectID(getObjectID());
        item.setFromName(getFromName());
        item.setEmail(getEmail());
        item.setUserName(getUserName());

        if (getProvider().equals(Provider.DEFAULT)) {
            item.setPassword(EncryptionHelper.decrypt(getPassword()));
            item.setImapHost(getEmailHost());
            item.setImapPort(getEmailPort());
            item.setSmtpHost(getEmailHostSMTP());
            item.setSmtpPort(getEmailPortSMTP());
            item.setSmtpAuth(isSmtpAuth());
        } else {
            item.setRefreshToken(getRefreshToken());
        }
        item.setProvider(getProvider().name());

        item.setActive(isActive());
        item.setCompanyEmail(isCompanyEmail());
        item.setDefaultEmail(isDefaultEmail());
        item.setStartDate(getStartDate());
        item.setSaveCopyToSentFolder(isSaveCopyToSentFolder());
        item.setErrorOfInactivation(getErrorOfInactivation());
        if (getUser() != null) {
            item.setOwnerID(getUser().getObjectID());
            item.setOwner(getUser().getFullName());
        }
        return item;
    }

    public EdsEmailFolder getFolder(MCFolderType type) {
        for (EdsEmailFolder f : getFolders()) {
            if (type.equals(f.getType())) {
                return f;
            }
        }
        return null;
    }

    public static boolean isSecure(Integer port) {
        return port != null && (port == 465 || port == 993);
    }

    public boolean isDefaultProvider() {
        return Provider.DEFAULT.equals(getProvider());
    }

    public enum Provider {
        GOOGLE, OFFICE365, DEFAULT
    }
}
