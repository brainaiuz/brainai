
package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Iskandar
 * Date: 25-Jul-2007
 * Time: 14:01:05
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "upload")
public class EdsUpload extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String localPath;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    private String contentType;

    @Type(type = "text")
    private String originalName;

    @Transient
    private InputStream inputStream;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "typeid")
    private EdsReference type;

    @Transient
    private String fileType;

    private Long size;

    @Transient
    private String folderName;

    @Transient
    private String driveFolderId;

    @Transient
    private String driveFolderName;

    //This field need for images Amazon static folder
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId")
    private EdsUpload parent;

    //This field need for image sizes to Amazon static folder's images
    private String imageSize;

    private Long duration;

    private String width;
    private String height;

    @Transient
    private boolean downloadable = true;

    /**
     * Version field for optimistic locking. Renamed to avoid conflict with file
     * body version.
     */
//    @SuppressWarnings("unused")
//    @Version
//    @Column(nullable = true)
//    private Integer dbVersion;
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public boolean canAccess(EdsUser user) {
        return true;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public EdsReference getType() {
        return type;
    }

    public void setType(EdsReference type) {
        this.type = type;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public EdsUpload getParent() {
        return parent;
    }

    public void setParent(EdsUpload parent) {
        this.parent = parent;
    }

    public String getImageSize() {
        return imageSize;
    }

    public void setImageSize(String imageSize) {
        this.imageSize = imageSize;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getLocalPath() {
        return localPath;
    }

    public String getDriveFolderId() {
        return driveFolderId;
    }

    public void setDriveFolderId(String driveFolderId) {
        this.driveFolderId = driveFolderId;
    }

    public String getDriveFolderName() {
        return driveFolderName;
    }

    public void setDriveFolderName(String driveFolderName) {
        this.driveFolderName = driveFolderName;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public boolean isDownloadable() {
        return downloadable;
    }

    public void setDownloadable(boolean downloadable) {
        this.downloadable = downloadable;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
