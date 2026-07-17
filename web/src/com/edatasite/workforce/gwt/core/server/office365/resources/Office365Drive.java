package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseResource;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by umakarimov on 9/30/15.
 */
public class Office365Drive extends Office365BaseResource {
    public static final String PERSONAL = "personal";
    public static final String BUSINESS = "business";

    private String id;
    private String driveType;
    private Office365DriveQuota quota;
    private Office365IdentitySet owner;

    /**
     * @param data
     * @see http://graph.microsoft.io/GraphDocuments/api-reference/v1.0/resources/drive.htm
     */
    public Office365Drive() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDriveType() {
        return driveType;
    }

    public void setDriveType(String driveType) {
        this.driveType = driveType;
    }

    public Office365DriveQuota getQuota() {
        return quota;
    }

    public void setQuota(Office365DriveQuota quota) {
        this.quota = quota;
    }

    public Office365IdentitySet getOwner() {
        return owner;
    }

    public void setOwner(Office365IdentitySet owner) {
        this.owner = owner;
    }

    @JsonIgnore
    public boolean isPersonal() {
        return PERSONAL.equalsIgnoreCase(this.driveType);
    }

    @JsonIgnore
    public boolean isForBusiness() {
        return BUSINESS.equalsIgnoreCase(this.driveType);
    }
}
