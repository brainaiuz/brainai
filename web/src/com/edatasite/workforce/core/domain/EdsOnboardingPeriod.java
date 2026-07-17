package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.hrms.client.rpc.OnboardingItem;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * User: User
 * Date: 8/24/12
 * Time: 4:30 PM
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "onboardingperiod")
public class EdsOnboardingPeriod extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    @Type(type = "text")
    private String description;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "isBeforeHireDate")
    private Boolean isBeforeHireDate;

    @Column(name = "relativestart")
    private Integer relativeStart;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Boolean getBeforeHireDate() {
        return isBeforeHireDate != null ? isBeforeHireDate : Boolean.FALSE;
    }

    public void setBeforeHireDate(Boolean beforeHireDate) {
        isBeforeHireDate = beforeHireDate;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRelativeStart() {
        return relativeStart;
    }

    public void setRelativeStart(Integer relativeStart) {
        this.relativeStart = relativeStart;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public OnboardingItem getRPC() {
        OnboardingItem item = new OnboardingItem();
        item.setPeriodId(getObjectID());
        item.setPeriodName(getName());
        item.setPeriodDescription(getDescription());
        item.setPeriodActive(getActive());
        item.setBeforeHireDate(getBeforeHireDate());
        item.setPeriodRelativeStart(getRelativeStart());
        item.setDuration(getDuration());
        return item;
    }
}
