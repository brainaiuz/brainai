package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.enums.LeaveReasonType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Hurshid on 12/18/2018
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "leave_reason_relation")
public class EdsLeaveReasonRelation extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Enumerated(EnumType.STRING)
    private LeaveReasonType relatedType;

    private Integer relationId;

    @Column(name = "reason_code")
    private String reasonCode;

    @Column(name = "leave_days", columnDefinition = "Decimal(10,2)")
    private Double leaveDays;

    @Column(name = "apply_to_all", columnDefinition = "boolean DEFAULT false")
    private Boolean applyToAll;

    @Column(name = "effective_date")
    private Date effectiveDate;

    public EdsLeaveReasonRelation() {
    }

    public EdsLeaveReasonRelation(Integer objectID, LeaveReasonType relatedType, Integer relationId, String reasonCode) {
        this.objectID = objectID;
        this.relatedType = relatedType;
        this.relationId = relationId;
        this.reasonCode = reasonCode;
    }

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public LeaveReasonType getRelatedType() {
        return relatedType;
    }

    public void setRelatedType(LeaveReasonType relatedType) {
        this.relatedType = relatedType;
    }

    public Integer getRelationId() {
        return relationId;
    }

    public void setRelationId(Integer relationId) {
        this.relationId = relationId;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public Double getLeaveDays() {
        return leaveDays;
    }

    public void setLeaveDays(Double leaveDays) {
        this.leaveDays = leaveDays;
    }

    public Boolean getApplyToAll() {
        return applyToAll != null ? applyToAll : false;
    }

    public void setApplyToAll(Boolean applyToAll) {
        this.applyToAll = applyToAll;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EdsLeaveReasonRelation)) return false;
        if (!super.equals(o)) return false;

        EdsLeaveReasonRelation that = (EdsLeaveReasonRelation) o;

        if (getObjectID() != null ? !getObjectID().equals(that.getObjectID()) : that.getObjectID() != null)
            return false;
        if (getRelatedType() != that.getRelatedType()) return false;
        if (getRelationId() != null ? !getRelationId().equals(that.getRelationId()) : that.getRelationId() != null)
            return false;
        if (getReasonCode() != null ? !getReasonCode().equals(that.getReasonCode()) : that.getReasonCode() != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getObjectID() != null ? getObjectID().hashCode() : 0);
        result = 31 * result + (getRelatedType() != null ? getRelatedType().hashCode() : 0);
        result = 31 * result + (getRelationId() != null ? getRelationId().hashCode() : 0);
        result = 31 * result + (getReasonCode() != null ? getReasonCode().hashCode() : 0);
        return result;
    }
}
