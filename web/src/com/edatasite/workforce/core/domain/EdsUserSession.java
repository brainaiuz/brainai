package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: iskan
 * Date: Dec 24, 2007
 * Time: 5:55:03 PM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "myUserSession")
public class EdsUserSession extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")                                               
    private Integer objectID;

    private String sessionID;
    private Boolean openIDUser;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    @Column(name = "userId")
    private Integer userId;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    private EdsUser user;

    private Date lastAccessTime;

    private Date signiinDate;

    @Column(length = 1000)
    private String userAgent;

    private String IPAddress;

    @Column(name = "shadow")
    private Boolean shadow = false;

    private Boolean expired = false;

    private Boolean superUser = false;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public EdsUser getUser() {
        return user;
    }

    public void setUser(EdsUser user) {
        this.user = user;
    }

    public Date getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Date lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public void setIPAddress(String IPAddress) {
        this.IPAddress = IPAddress;
    }

    public Boolean isShadow() {
        return shadow;
    }

    public void setShadow(Boolean shadow) {
        this.shadow = shadow;
    }

    public Boolean isExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

    public Date getSigniinDate() {
        return signiinDate;
    }

    public void setSigniinDate(Date signiinDate) {
        this.signiinDate = signiinDate;
    }

    public Boolean isSuperUser() {
        if (superUser != null) {
            return superUser;
        }
        return false;
    }

    public void setSuperUser(Boolean superUser) {
        this.superUser = superUser;
    }

    public void setOpenIDUser(Boolean openIDUser) {
        this.openIDUser = openIDUser;
    }

    public boolean isOpenIDUser() {
        return openIDUser;
    }
}
