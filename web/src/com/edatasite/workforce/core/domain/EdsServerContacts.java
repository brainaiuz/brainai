
package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 17.11.2008
 * Time: 16:38:14
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "googlecontacts")
public class EdsServerContacts extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "googleid")
    private String googleID;

    @Column(name = "token")
    @Type(type = "text")
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private EdsUser user;

    private Integer attempts = 0;

    private Boolean active = true;

    private Boolean isOfficeContact = false;

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
        if (attempts == null) {
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

    public Boolean getIsOfficeContact() {
        return isOfficeContact;
    }

    public void setIsOfficeContact(Boolean isOfficeContact) {
        this.isOfficeContact = isOfficeContact;
    }
}
