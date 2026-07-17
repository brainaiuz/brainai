package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.ui.communication.AsteriskSettings;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: 7/4/2020
 * Time: 7:37 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "asterisk_settings")
public class EdsAsteriskSettings extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "number")
    private String number;

    @Column(name = "asterisk_host")
    private String asteriskHost;

    @Column(name = "asterisk_port")
    private String asteriskPort;

    @Column(name = "deleted", columnDefinition = " boolean default false")
    private boolean deleted = false;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAsteriskHost() {
        return asteriskHost;
    }

    public void setAsteriskHost(String asteriskHost) {
        this.asteriskHost = asteriskHost;
    }

    public String getAsteriskPort() {
        return asteriskPort;
    }

    public void setAsteriskPort(String asteriskPort) {
        this.asteriskPort = asteriskPort;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public EdsAsteriskSettings fromRPC(AsteriskSettings item) {
        setNumber(item.getAsteriskNumber());
        setAsteriskHost(item.getAsteriskHost());
        setAsteriskPort(item.getAsteriskPort());
        return this;
    }

    public AsteriskSettings getRPC() {
        AsteriskSettings item = new AsteriskSettings();
        item.setId(getObjectID());
        item.setAsteriskNumber(getNumber());
        item.setAsteriskHost(getAsteriskHost());
        item.setAsteriskPort(getAsteriskPort());
        return item;
    }
}
