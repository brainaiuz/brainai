package com.edatasite.workforce.gwt.core.server.app.dto;

import java.util.List;

public class ShortenedLinkResponse {

    private String originalURL;
    private String path;
    private String idString;
    private String id;
    private String shortURL;
    private String secureShortURL;
    private boolean cloaking;
    private List<String> tags;
    private String createdAt;
    private boolean skipQS;
    private boolean archived;
    private long DomainId;
    private long OwnerId;
    private boolean hasPassword;
    private String source;
    private boolean success;
    private boolean duplicate;

    public String getOriginalURL() {
        return originalURL;
    }

    public void setOriginalURL(String originalURL) {
        this.originalURL = originalURL;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getIdString() {
        return idString;
    }

    public void setIdString(String idString) {
        this.idString = idString;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShortURL() {
        return shortURL;
    }

    public void setShortURL(String shortURL) {
        this.shortURL = shortURL;
    }

    public String getSecureShortURL() {
        return secureShortURL;
    }

    public void setSecureShortURL(String secureShortURL) {
        this.secureShortURL = secureShortURL;
    }

    public boolean isCloaking() {
        return cloaking;
    }

    public void setCloaking(boolean cloaking) {
        this.cloaking = cloaking;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isSkipQS() {
        return skipQS;
    }

    public void setSkipQS(boolean skipQS) {
        this.skipQS = skipQS;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public long getDomainId() {
        return DomainId;
    }

    public void setDomainId(long domainId) {
        DomainId = domainId;
    }

    public long getOwnerId() {
        return OwnerId;
    }

    public void setOwnerId(long ownerId) {
        OwnerId = ownerId;
    }

    public boolean isHasPassword() {
        return hasPassword;
    }

    public void setHasPassword(boolean hasPassword) {
        this.hasPassword = hasPassword;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isDuplicate() {
        return duplicate;
    }

    public void setDuplicate(boolean duplicate) {
        this.duplicate = duplicate;
    }
}