package com.edatasite.workforce.core.domain.emailfetching;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.domain.ObjectHistory;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.utils.EdsContextParams;
import jakarta.mail.internet.MimeUtility;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot Rahimov
 * Date: 25.10.11
 * Time: 16:55
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "emailattachments")
public class EdsEmailAttachment extends EdsObject implements ObjectHistory, Constants {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String emailId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emailtracker_id")
    private EdsEmailTracker emailTracker;

    @Column(name = "type")
    private String contentType;

    @Column(name = "filename")
    private String fileName;

    @Column(name = "description")
    private String description;

    @Column(name = "content_id")
    private String contentID;

    @Column(name = "filesize")
    private long filesize;

    @Transient
    private InputStream inputStream;

    private Date creationTime;

    private Date lastUpdateTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updaterid")
    private EdsUser updater;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorid")
    private EdsUser creator;

    public void setDescription(String description) {
        this.description = description != null && description.length() > 255 ? description.substring(0, 254) : description;
    }

    private FileResource asFileResource() {
        FileResource fileItem = new FileResource();
        fileItem.setUploadType(EdsContextParams.getUploadType());
        fileItem.setEmailID(getEmailId());
        if (getFileName() != null) {
            try {
                fileItem.setName(MimeUtility.decodeText(getFileName()));
            } catch (UnsupportedEncodingException e) {
                fileItem.setName(getFileName());
            }
        }
        fileItem.setEntityID(getEmailTracker().getObjectID());
        fileItem.setObjectId(getObjectID());
        fileItem.setBodyId(getObjectID()); // Hack for view image attachments in image viewer popup in case summary view
        fileItem.setContentType(getContentType());
        fileItem.setDescription(getDescription());
        fileItem.setContentLength(getFilesize());
        fileItem.setEncryptedLinkAttribute(EncryptionHelper.encryptURL(getObjectID().toString()));
        fileItem.setDownloadFromEmailServer(true);
        fileItem.setEmailAttachmentID(getObjectID());
        fileItem.setCreationDate(getCreationTime());
        fileItem.setModificationDate(getLastUpdateTime());
        return fileItem;
    }

    public static ArrayList<FileResource> asFileResourses(Collection<EdsEmailAttachment> trackerAttachments) {
        ArrayList<FileResource> list = new ArrayList<>();
        if (trackerAttachments != null && trackerAttachments.size() > 0) {
            for (EdsEmailAttachment attachment : trackerAttachments) {
                if (attachment != null) {
                    list.add(attachment.asFileResource());
                }
            }
        }
        return list;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public EdsEmailTracker getEmailTracker() {
        return emailTracker;
    }

    public void setEmailTracker(EdsEmailTracker emailTracker) {
        this.emailTracker = emailTracker;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDescription() {
        return description;
    }

    public String getContentID() {
        return contentID;
    }

    public void setContentID(String contentID) {
        this.contentID = contentID;
    }

    public long getFilesize() {
        return filesize;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    @Override
    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    @Override
    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public EdsUser getUpdater() {
        return updater;
    }

    @Override
    public void setUpdater(EdsUser updater) {
        this.updater = updater;
    }

    public EdsUser getCreator() {
        return creator;
    }

    @Override
    public void setCreator(EdsUser creator) {
        this.creator = creator;
    }
}
