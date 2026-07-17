package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.HashMap;

/**
 * User: Abdulaziz
 * Date: Nov 3, 2009
 * Time: 4:39:04 PM
 */
public class SearchResultItem implements IsSerializable {
    private String section;
    private String entityID;
    private Integer bodyId;
    private String dateCreated;
    private String entityType;
    private String name;
    private String description;
    private String size;
    private String titleLink;
    private String plainLink;
    private String title;
    private Boolean isFromAmazon;
    private Boolean internal = false;
    private HashMap<String, String> highlits;

    public String getEntityID() {
        return entityID;
    }

    public void setEntityID(String entityID) {
        this.entityID = entityID;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }


    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, String> getHighlits() {
        if (highlits == null) {
            highlits = new HashMap<>();
        }
        return highlits;
    }

    public void setHighlits(HashMap<String, String> highlits) {
        this.highlits = highlits;
    }

    public String getTitleLink() {
        return titleLink;
    }

    public void setTitleLink(String titleLink) {
        this.titleLink = titleLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getInternal() {
        return internal;
    }

    public void setInternal(Boolean internal) {
        this.internal = internal;
    }

    public String getPlainLink() {
        return plainLink;
    }

    public void setPlainLink(String plainLink) {
        this.plainLink = plainLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Integer getBodyId() {
        return bodyId;
    }

    public void setBodyId(Integer bodyId) {
        this.bodyId = bodyId;
    }

    public Boolean isFromAmazon() {
        return isFromAmazon;
    }

    public void setFromAmazon(Boolean fromAmazon) {
        isFromAmazon = fromAmazon;
    }
}
