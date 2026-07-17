package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseResource;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

/**
 * Created by umakarimov on 9/30/15.
 */
public class Office365DriveItem extends Office365BaseResource {
    @JsonProperty("@content.downloadUrl")
    private String downloadUrl;
    @JsonProperty("@content.sourceUrl")
    private String sourceUrl;

    private String id;
    private String name;
    private String cTag;
    private String eTag;
    private String description;

    private Long size;
    private Object content;
    private Office365Deleted deleted;

    private Date createdDateTime;
    private Date lastModifiedDateTime;
    private Office365IdentitySet createdBy;
    private Office365IdentitySet lastModifiedBy;

    private Office365ItemReference parentReference;
    private Office365FileSystemInfo fileSystemInfo;

    private Office365Audio audio;
    private Office365Photo photo;
    private Office365Image image;
    private Office365Video video;
    private Office365File file;
    private Office365Folder folder;
    private Office365Location location;
    private Office365SpecialFolder specialFolder;

    private String webUrl;
    private Office365SearchResult searchResult;

    /**
     * @param data
     * @see http://graph.microsoft.io/GraphDocuments/api-reference/v1.0/resources/driveitem.htm
     */
    public Office365DriveItem() {
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getcTag() {
        return cTag;
    }

    public void setcTag(String cTag) {
        this.cTag = cTag;
    }

    public String geteTag() {
        return eTag;
    }

    public void seteTag(String eTag) {
        this.eTag = eTag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public Office365Deleted getDeleted() {
        return deleted;
    }

    public void setDeleted(Office365Deleted deleted) {
        this.deleted = deleted;
    }

    public Date getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(Date createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public Date getLastModifiedDateTime() {
        return lastModifiedDateTime;
    }

    public void setLastModifiedDateTime(Date lastModifiedDateTime) {
        this.lastModifiedDateTime = lastModifiedDateTime;
    }

    public Office365IdentitySet getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Office365IdentitySet createdBy) {
        this.createdBy = createdBy;
    }

    public Office365IdentitySet getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Office365IdentitySet lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Office365ItemReference getParentReference() {
        return parentReference;
    }

    public void setParentReference(Office365ItemReference parentReference) {
        this.parentReference = parentReference;
    }

    public Office365FileSystemInfo getFileSystemInfo() {
        return fileSystemInfo;
    }

    public void setFileSystemInfo(Office365FileSystemInfo fileSystemInfo) {
        this.fileSystemInfo = fileSystemInfo;
    }

    public Office365Audio getAudio() {
        return audio;
    }

    public void setAudio(Office365Audio audio) {
        this.audio = audio;
    }

    public Office365Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Office365Photo photo) {
        this.photo = photo;
    }

    public Office365Image getImage() {
        return image;
    }

    public void setImage(Office365Image image) {
        this.image = image;
    }

    public Office365Video getVideo() {
        return video;
    }

    public void setVideo(Office365Video video) {
        this.video = video;
    }

    public Office365File getFile() {
        return file;
    }

    public void setFile(Office365File file) {
        this.file = file;
    }

    public Office365Folder getFolder() {
        return folder;
    }

    public void setFolder(Office365Folder folder) {
        this.folder = folder;
    }

    public Office365Location getLocation() {
        return location;
    }

    public void setLocation(Office365Location location) {
        this.location = location;
    }

    public Office365SpecialFolder getSpecialFolder() {
        return specialFolder;
    }

    public void setSpecialFolder(Office365SpecialFolder specialFolder) {
        this.specialFolder = specialFolder;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public Office365SearchResult getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(Office365SearchResult searchResult) {
        this.searchResult = searchResult;
    }
}
