package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
public abstract class EdsUploadSettings extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "accessKey")
    @Type(type = "text")
    private String accessKey;

    @Column(name = "fileLink")
    @Type(type = "text")
    private String fileLink;

    @ManyToOne
    @JoinColumn(name = "uploadId")
    @ForeignKey(name = "none")
    private EdsUpload upload;

    @Column(name = "expireDate")
    private Date expireDate;

    @Transient
    private String fileType;

    public EdsUploadSettings() {
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public EdsUpload getUpload() {
        return upload;
    }

    public void setUpload(EdsUpload upload) {
        this.upload = upload;
    }

    public String getFileLink() {
        return fileLink;
    }

    public void setFileLink(String fileLink) {
        this.fileLink = fileLink;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
