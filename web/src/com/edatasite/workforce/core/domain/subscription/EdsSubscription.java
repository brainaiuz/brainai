package com.edatasite.workforce.core.domain.subscription;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.enums.BreakType;
import com.edatasite.workforce.core.domain.enums.SubscriptionLimitFrequency;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * User : Akhror
 * Date : 03.11.2023
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "subscription")
public class EdsSubscription extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private EdsEmployee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id")
    private EdsCrmAccount vendor;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    private int limit;

    @Column(name = "limit_frequency")
    @Enumerated(EnumType.STRING)
    private SubscriptionLimitFrequency limitFrequency = SubscriptionLimitFrequency.DAILY;

    @Column(name = "break_duration")
    private int breakDuration;

    @Column(name = "break_type")
    @Enumerated(EnumType.STRING)
    private BreakType breakType = BreakType.SECONDS;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(EdsEmployee employee) {
        this.employee = employee;
    }

    public EdsCrmAccount getVendor() {
        return vendor;
    }

    public void setVendor(EdsCrmAccount vendor) {
        this.vendor = vendor;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public SubscriptionLimitFrequency getLimitFrequency() {
        return limitFrequency;
    }

    public void setLimitFrequency(SubscriptionLimitFrequency limitFrequency) {
        this.limitFrequency = limitFrequency;
    }

    public int getBreakDuration() {
        return breakDuration;
    }

    public void setBreakDuration(int breakDuration) {
        this.breakDuration = breakDuration;
    }

    public BreakType getBreakType() {
        return breakType;
    }

    public void setBreakType(BreakType breakType) {
        this.breakType = breakType;
    }
}
