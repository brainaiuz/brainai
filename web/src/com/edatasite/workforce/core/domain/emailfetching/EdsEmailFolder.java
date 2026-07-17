package com.edatasite.workforce.core.domain.emailfetching;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.settings.EdsEmailSetting;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.EmailFolder;
import com.edatasite.workforce.gwt.messagecenter.client.enumtype.MCFolderType;
import jakarta.mail.Folder;
import jakarta.mail.FolderNotFoundException;
import jakarta.mail.MessagingException;
import jakarta.mail.Store;

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
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot Rahimov
 * Date: 25.10.11
 * Time: 16:55
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "email_folder")
public class EdsEmailFolder extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    private EdsEmailFolder parent;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "emailSetting")
    private EdsEmailSetting emailSetting;

    @Column(name = "name", length = 1000)
    private String name;

    @Column(name = "fullName")
    private String fullName;

    @Column(name = "url", length = 1000)
    private String url;

    @Enumerated(EnumType.STRING)
    private MCFolderType type = MCFolderType.INBOX;

    @Column(name = "lastuid")
    private Long lastUID;

//    @Column(name = "nextuid")
//    private Long nextUID;

    private Date lastFetchedDate;

    private Date creationTime;

    private Date lastUpdateTime;

    @Column(name = "fetchable", columnDefinition = " boolean DEFAULT false")
    private boolean fetchable = false;

    @Column(name = "deleted", columnDefinition = " boolean DEFAULT false")
    private boolean deleted = false;


    @Column(name = "fetchRejectReason", length = 1000)
    private String fetchRejectReason;

    public Long getNextUID() {
        return validLastUID() ? lastUID + 1 : 1;
    }

    public Long getLastUID() {
        return validLastUID() ? lastUID : 1;
    }

    private boolean validLastUID() {
        return lastUID != null && lastUID > 0;
    }

    public EmailFolder getRPC() {
        EmailFolder folder = new EmailFolder();
        folder.setObjectID(getObjectID());
        folder.setName(getName());
        folder.setType(getType());
        if (getParent() != null) {
            folder.setParentID(getParent().getObjectID());
        }
        folder.setFetchable(isFetchable());
        return folder;
    }

    public static Folder findFolder(Store store, String folderName, boolean isGmail) {
        Folder folder = null;
        Folder freakFolder = null;
        try {
            Folder[] folders = store.getDefaultFolder().list("*");
            fs:
            for (Folder folder1 : folders) {
                folder = folder1;
                if (folder.getName() != null && folder.getName().toLowerCase().contains(folderName.toLowerCase())) {
                    if (!isGmail || (folder.getParent() != null && folder.getParent().getName().contains("Gmail"))) {
                        break fs;
                    }
                    freakFolder = folder;
                }
                folder = null;
            }
        } catch (MessagingException e) {
            e.printStackTrace();
            if (e instanceof FolderNotFoundException) {
                folder = null;
            }
        }
        return folder == null ? freakFolder : folder;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public EdsEmailFolder getParent() {
        return parent;
    }

    public void setParent(EdsEmailFolder parent) {
        this.parent = parent;
    }

    public EdsEmailSetting getEmailSetting() {
        return emailSetting;
    }

    public void setEmailSetting(EdsEmailSetting emailSetting) {
        this.emailSetting = emailSetting;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public MCFolderType getType() {
        return type;
    }

    public void setType(MCFolderType type) {
        this.type = type;
    }

    public void setLastUID(Long lastUID) {
        this.lastUID = lastUID;
    }

    public Date getLastFetchedDate() {
        return lastFetchedDate;
    }

    public void setLastFetchedDate(Date lastFetchedDate) {
        this.lastFetchedDate = lastFetchedDate;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public boolean isFetchable() {
        return fetchable;
    }

    public void setFetchable(boolean fetchable) {
        this.fetchable = fetchable;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getFetchRejectReason() {
        return fetchRejectReason;
    }

    public void setFetchRejectReason(String fetchRejectReason) {
        this.fetchRejectReason = fetchRejectReason;
    }
}
