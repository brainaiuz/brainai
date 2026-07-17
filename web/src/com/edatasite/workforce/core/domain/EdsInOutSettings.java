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
 * User: admin
 * Date: Jan 27, 2010
 * Time: 9:34:49 AM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "inOutSettings")
public class EdsInOutSettings extends EdsObject {
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

    private Boolean trackNoHoursEmployee = false;
    private Boolean trackLunch = true;
    private Boolean trackManualCheckInOut = false;

    public Boolean isTrackNoHoursEmployee() {
        return trackNoHoursEmployee;
    }

    public void setTrackNoHoursEmployee(Boolean trackNoHoursEmployee) {
        this.trackNoHoursEmployee = trackNoHoursEmployee;
    }

    public Boolean isTrackLunch() {
        return trackLunch;
    }

    public void setTrackLunch(Boolean trackLunch) {
        this.trackLunch = trackLunch;
    }

    public Boolean isTrackManualCheckInOut() {
        return trackManualCheckInOut;
    }

    public void setTrackManualCheckInOut(Boolean trackManualCheckInOut) {
        this.trackManualCheckInOut = trackManualCheckInOut;
    }

}
