package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsScope;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * User: ASUS
 * Date: 26.02.2016 11:57
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "holidayHistory")
public class EdsHolidayHistory extends EdsHistory implements HrmsHistory {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "holidayid")
    private EdsHoliday holiday;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updaterid")
    private EdsUser updater;

    @Column(name = "creationTime")
    private Date creationTime;

    @Column(name = "message")
    @Type(type = "text")
    private String message;

    public EdsHoliday getHoliday() {
        return holiday;
    }

    public void setHoliday(EdsHoliday holiday) {
        this.holiday = holiday;
    }

    public EdsUser getUpdater() {
        return updater;
    }

    @Override
    public Integer getEntityID() {
        return getHoliday() != null ? getHoliday().getObjectID() : null;
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
}
