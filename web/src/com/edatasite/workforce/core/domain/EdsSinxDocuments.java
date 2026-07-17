package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 17.11.2008
 * Time: 16:36:20
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "googledocuments")
public class EdsSinxDocuments extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "officeid", length = 1000)
    private String officeID;

    @Column(name = "googleid")
    private String googleID;

    @Column(name = "token")
    private String token;

    @Column(name = "officetoken", length = 1000)
    private String officeToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private EdsUser user;

    @Column(nullable = false, columnDefinition = "int4 default 0")
    private Integer attempts = 0;

    private Boolean active = true;

    @Column(name = "reason", length = 1000)
    private String reason;

    public Integer getObjectID() {
        return objectID;
    }

    public String getGoogleID() {
        return googleID;
    }

    public void setGoogleID(String googleID) {
        this.googleID = googleID;
    }

    public String getOfficeID() {
        return officeID;
    }

    public void setOfficeID(String officeID) {
        this.officeID = officeID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public EdsUser getUser() {
        return user;
    }

    public void setUser(EdsUser user) {
        this.user = user;
    }

    public Integer getAttempts() {
        if(attempts == null) {
            setAttempts(0);
        }
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Boolean getActive() {
        return active == null || active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getOfficeToken() {
        return officeToken;
    }

    public void setOfficeToken(String officeToken) {
        this.officeToken = officeToken;
    }
}
