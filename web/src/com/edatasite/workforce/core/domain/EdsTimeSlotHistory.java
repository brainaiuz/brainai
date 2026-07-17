package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsScope;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;

/**
 * User: ASUS
 * Date: 22.02.2016 18:25
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "timeslotHistory")
public class EdsTimeSlotHistory extends EdsHistory implements HrmsHistory {


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timeslotid")
    private EdsTimeSlot timeSlot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shiftSettingsId")
    private EdsShiftSettings shiftSettings;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updaterid")
    private EdsUser updater;

    @Column(name = "creationTime")
    private Date creationTime;

    @Column(name = "message")
    @Type(type = "text")
    private String message;

    public EdsTimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(EdsTimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public EdsUser getUpdater() {
        return updater;
    }

    @Override
    public Integer getEntityID() {
        return getTimeSlot() != null ? getTimeSlot().getObjectID() : null;
    }

    public void setUpdater(EdsUser updater) {
        this.updater = updater;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public EdsShiftSettings getShiftSettings() {
        return shiftSettings;
    }

    public void setShiftSettings(EdsShiftSettings shiftSettings) {
        this.shiftSettings = shiftSettings;
    }
}
