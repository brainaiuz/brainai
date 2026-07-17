package com.edatasite.workforce.gwt.profile.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * User: Abror Abdukadirov
 * Date: 07.12.2018 14:15
 */
public class SettingsPdfTemplateListItem implements IsSerializable {
    public static final String NAME = "NAME";
    public static final String CATEGORY = "CATEGORY";
    public static final String IS_DEFAULT = "IS_DEFAULT";
    public static final String MODIFIED_BY = "MODIFIED_BY";
    public static final String MODIFIED_DATE = "MODIFIED_DATE";
    public static final String CREATION_DATE = "CREATION_DATE";

    private Integer objectId;
    private String name;
    private String category;
    private String pdfType;
    private boolean isDefault;
    private String modifiedBy;
    private Date modifiedDate;
    private Date creationDate;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPdfType() {
        return pdfType;
    }

    public void setPdfType(String pdfType) {
        this.pdfType = pdfType;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
}
