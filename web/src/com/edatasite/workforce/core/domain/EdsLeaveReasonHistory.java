package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.ReasonItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "leave_reason_history")
public class EdsLeaveReasonHistory extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "reason_code")
    private String reasonCode;

    @Column(name = "effective_date")
    private Date effectiveDate;

    @Column(name = "modified_date")
    private Date modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private EdsUser modifiedBy;

    @Column(name = "leave_days_old")
    private Double leaveDaysOld;

    @Column(name = "leave_days_new")
    private Double leaveDaysNew;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reasonId")
    private EdsLeaveReason reasonId;

    @Column(name = "min_leave_days")
    private Integer minLeaveDays;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Double getLeaveDaysOld() {
        return leaveDaysOld;
    }

    public void setLeaveDaysOld(Double leaveDays) {
        this.leaveDaysOld = leaveDays;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public EdsLeaveReason getReason() {
        return reasonId;
    }

    public void setReason(EdsLeaveReason reasonId) {
        this.reasonId = reasonId;
    }


    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public EdsUser getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(EdsUser modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Double getLeaveDaysNew() {
        return leaveDaysNew;
    }

    public void setLeaveDaysNew(Double leaveDaysNew) {
        this.leaveDaysNew = leaveDaysNew;
    }

    public Integer getMinLeaveDays() {
        return minLeaveDays;
    }

    public void setMinLeaveDays(Integer minLeaveDays) {
        this.minLeaveDays = minLeaveDays;
    }

    public ReasonItem toRPC() {
        ReasonItem item = new ReasonItem();
        item.setId(getObjectID());
        item.setCode(getReasonCode());
        item.setEffectiveFrom(new DateNonConvertable(getEffectiveDate()));
        item.setModifiedDate(new DateNonConvertable(getModifiedDate()));
        item.setModifiedBy(getModifiedBy() != null ? getModifiedBy().getFullName() : null);
        item.setLeaveDaysOld(getLeaveDaysOld());
        if (reasonId != null) {
            item.setName(getReason().getName());
        }
        item.setMinLeaveDays(getMinLeaveDays() != null ? getMinLeaveDays() : 0);
        return item;
    }
}
