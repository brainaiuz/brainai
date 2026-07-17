package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "shift_settings")
public class EdsShiftSettings extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private Boolean deleted = false;
    private String description;
    private String name;

    @Column(name = "short_name")
    private String shortName;

    @Column(name = "color")
    private String hexColor;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "locale_id")
    private EdsReferenceLocale locale;

    @Column(name = "interval")
    private String interval;

    @Column(name = "end_time")
    private Integer endTime;
    @Column(name = "start_time")
    private Integer startTime;

    @Column(name = "lunch_end")
    private Integer lunchEnd;
    @Column(name = "lunch_start")
    private Integer lunchStart;

    @Column(name = "coffee_end")
    private Integer coffeeEnd;
    @Column(name = "coffee_start")
    private Integer coffeeStart;

    @Column(name = "included_days")
    private String includedDays;

    public Integer getEndTime() {
        return endTime;
    }

    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    public Integer getStartTime() {
        return startTime;
    }

    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    public Integer getLunchEnd() {
        return lunchEnd;
    }

    public void setLunchEnd(Integer lunchEnd) {
        this.lunchEnd = lunchEnd;
    }

    public Integer getLunchStart() {
        return lunchStart;
    }

    public void setLunchStart(Integer lunchStart) {
        this.lunchStart = lunchStart;
    }

    public Integer getCoffeeEnd() {
        return coffeeEnd;
    }

    public void setCoffeeEnd(Integer coffeeEnd) {
        this.coffeeEnd = coffeeEnd;
    }

    public Integer getCoffeeStart() {
        return coffeeStart;
    }

    public void setCoffeeStart(Integer coffeeStart) {
        this.coffeeStart = coffeeStart;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getHexColor() {
        return hexColor;
    }

    public void setHexColor(String hexColor) {
        this.hexColor = hexColor;
    }

    public EdsReferenceLocale getLocale() {
        return locale;
    }

    public void setLocale(EdsReferenceLocale locale) {
        this.locale = locale;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getIncludedDays() {
        return includedDays;
    }

    public void setIncludedDays(String includedDays) {
        this.includedDays = includedDays;
    }
}
