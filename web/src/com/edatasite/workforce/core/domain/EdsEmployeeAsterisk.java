package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: 07.04.2020
 * Time: 17:04:48
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "employee_asterisk")
public class EdsEmployeeAsterisk extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "userid")
    private Integer userId;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", updatable = false, insertable = false)
    private EdsUser user;

    @Column(name = "asterisk_settings_id")
    private Integer asteriskSettingsId;
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "asterisk_settings_id", updatable = false, insertable = false)
    private EdsAsteriskSettings asteriskSettings;

    @Column(name = "deleted")
    private Boolean deleted = false;

    private String username;
    private String password;
    private Boolean active;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsUser getUser() {
        return user;
    }

    public void setUser(EdsUser user) {
        this.user = user;
    }

    public EdsAsteriskSettings getAsteriskSettings() {
        return asteriskSettings;
    }

    public void setAsteriskSettings(EdsAsteriskSettings asteriskSettings) {
        this.asteriskSettings = asteriskSettings;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getAsteriskSettingsId() {
        return asteriskSettingsId;
    }

    public void setAsteriskSettingsId(Integer asteriskSettingsId) {
        this.asteriskSettingsId = asteriskSettingsId;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
