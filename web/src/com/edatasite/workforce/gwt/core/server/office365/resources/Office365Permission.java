package com.edatasite.workforce.gwt.core.server.office365.resources;

import com.edatasite.workforce.gwt.core.server.office365.resources.base.Office365BaseResource;

import java.util.ArrayList;

/**
 * Created by umakarimov on 9/30/15.
 */
public class Office365Permission extends Office365BaseResource {
    private static final String READ = "read";
    private static final String WRITE = "write";

    private String id;
    private String shareId;

    private ArrayList<String> roles;
    private Office365IdentitySet grantedTo;

    private Office365SharingLink link;
    private Office365SharingInvitation invitation;
    private Office365ItemReference inheritedFrom;

    /**
     * @param data
     * @see http://graph.microsoft.io/GraphDocuments/api-reference/v1.0/resources/permission.htm
     */
    public Office365Permission() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }

    public ArrayList<String> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<String> roles) {
        this.roles = roles;
    }

    public Office365IdentitySet getGrantedTo() {
        return grantedTo;
    }

    public void setGrantedTo(Office365IdentitySet grantedTo) {
        this.grantedTo = grantedTo;
    }

    public Office365SharingLink getLink() {
        return link;
    }

    public void setLink(Office365SharingLink link) {
        this.link = link;
    }

    public Office365SharingInvitation getInvitation() {
        return invitation;
    }

    public void setInvitation(Office365SharingInvitation invitation) {
        this.invitation = invitation;
    }

    public Office365ItemReference getInheritedFrom() {
        return inheritedFrom;
    }

    public void setInheritedFrom(Office365ItemReference inheritedFrom) {
        this.inheritedFrom = inheritedFrom;
    }
}
