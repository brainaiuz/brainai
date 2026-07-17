package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.customfields.EdsBenefitRequestCustomFields;
import com.edatasite.workforce.gwt.availability.client.rpc.BenefitRequestItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Djuraev on 8/6/15.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "benefitRequest")
public class EdsBenefitRequest extends EdsObject {

    public static final String _BENEFIT_REQUEST_STATUSES = "_BENEFIT_REQUEST_STATUSES";
    public static final String WAITING_FOR_APPROVAL = "BR_WAITING_FOR_APPROVAL";
    public static final String APPROVED = "BR_APPROVED";
    public static final String REJECTED = "BR_REJECTED";

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester")
    private EdsEmployee requester;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver")
    private EdsEmployee approver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorid")
    private EdsUser creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "benefit_id")
    private EdsBenefit benefit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status")
    private EdsReference status;

    @Column(name = "requestedQuantity", nullable = false, columnDefinition = "Decimal(10,2) default 0.00")
    private Double requestedQuantity = 0.0;

    @Column(name = "date")
    private Date date;
    private Date createdDate;
    private Date lastUpdateTime;

    @Column(name = "deleted", columnDefinition = " boolean DEFAULT false")
    private Boolean deleted = false;

    @Type(type = "text")
    @Column(name = "description")
    private String description;

    @Type(type = "text")
    @Column(name = "rejectionReason")
    private String rejectionReason;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "benefitrequestcustomfieldsid")
    private EdsBenefitRequestCustomFields customFields;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsEmployee getRequester() {
        return requester;
    }

    public void setRequester(EdsEmployee requester) {
        this.requester = requester;
    }

    public EdsEmployee getApprover() {
        return approver;
    }

    public void setApprover(EdsEmployee approver) {
        this.approver = approver;
    }

    public EdsUser getCreator() {
        return creator;
    }

    public void setCreator(EdsUser creator) {
        this.creator = creator;
    }

    public EdsBenefit getBenefit() {
        return benefit;
    }

    public void setBenefit(EdsBenefit benefit) {
        this.benefit = benefit;
    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        this.status = status;
    }

    public Double getRequestedQuantity() {
        return requestedQuantity;
    }

    public void setRequestedQuantity(Double requestedQuantity) {
        this.requestedQuantity = requestedQuantity;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
    public EdsBenefitRequestCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsBenefitRequestCustomFields customFields) {
        this.customFields = customFields;
    }

    public BenefitRequestItem toRequestItem(boolean withEmployeeCode) {
        BenefitRequestItem item = new BenefitRequestItem();
        item.setObjectID(getObjectID());
        if (getApprover() != null) {
            item.setApproverID(getApprover().getObjectID());
            item.setApprover(getApprover().getName());
        }
        if (getRequester() != null) {
            item.setRequesterID(getRequester().getObjectID());
            item.setRequester(getRequester().getName());
            if (withEmployeeCode) {
                String code = getRequester().getProfile().getEmployeeCode();
                item.setRequester(getRequester() != null ? (code != null && !"".equals(code) ? code + "-" : "") + getRequester().getName() : null);
            }
        }

        item.setDate(new DateNonConvertable(getDate()));
        item.setDescription(getDescription());
        item.setRejectionReason(getRejectionReason());
        item.setStatus(getStatus() != null ? new SelectItem(getStatus().getObjectID(), getStatus().getName(), getStatus().getCode(), getStatus().getDescription(), "") : new SelectItem());
        if (getBenefit() != null) {
            item.setBenefitID(getBenefit().getObjectID());
            item.setBenefitName(getBenefit().getName());
            if (getBenefit().getQtytype() != null) {
                item.setQuantityType(getBenefit().getQtytype().getAsSelectItem());
            }
            if (getBenefit().getCurrency() != null) {
                SelectItem currency = new SelectItem();
                currency.setId(getBenefit().getCurrency().getObjectID());
                currency.setName(getBenefit().getCurrency().getName());
                currency.setCode(getBenefit().getCurrency().getSymbol());
                item.setBenefitCurrency(currency);
            }
        }
        item.setRequestedQuantity(getRequestedQuantity());
        return item;
    }

}
