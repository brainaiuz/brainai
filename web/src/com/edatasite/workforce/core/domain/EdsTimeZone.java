package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: izaynutdinov
 * Date: 28.04.2007
 * Time: 16:38:33
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "timeZone")
public class EdsTimeZone extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    @Column(name = "name")
    private String name;

    @Column(name = "zoneID")
    private String zoneID;

    @Column(name = "microsoftZoneID")
    private String microsoftZoneID;

    @Column(name = "timeZoneOffset")
    private Integer timeZoneOffset;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getZoneID() {
        return zoneID;
    }

    public void setZoneID(String zoneID) {
        this.zoneID = zoneID;
    }

    public String getMicrosoftZoneID() {
        return microsoftZoneID;
    }

    public void setMicrosoftZoneID(String microsoftZoneID) {
        this.microsoftZoneID = microsoftZoneID;
    }

    public Integer getTimeZoneOffset() {
        return timeZoneOffset;
    }

    public void setTimeZoneOffset(Integer timeZoneOffset) {
        this.timeZoneOffset = timeZoneOffset;
    }
}
