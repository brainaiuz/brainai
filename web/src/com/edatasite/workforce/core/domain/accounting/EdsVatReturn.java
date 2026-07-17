package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.accounting.client.rpc.VatReturnItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.server.domain.ObjectHistory;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "vatreturn")
public class EdsVatReturn extends EdsObject implements ObjectHistory {

    public static final String VAT_RETURN_STATUS = "_VAT_RETURN_STATUS";
    public static final String FILED = "FILED";
    public static final String UNFILED = "UNFILED";
    public static final String OPEN = "OPEN";
    public static final String OPEN_SHORT = "O";
    public static final String FILED_SHORT = "F";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private EdsReference status;

    @Temporal(TemporalType.DATE)
    private Date fromDate;

    @Temporal(TemporalType.DATE)
    private Date toDate;

    @Temporal(TemporalType.DATE)
    private Date dueDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date filedOn;

    @Column(name = "period_key")
    private String periodKey;

    @Column(name = "payable_taxtotal", precision = 25, scale = 5)
    private BigDecimal payableTaxTotal;
    @Column(name = "reclaimable_taxtotal", precision = 25, scale = 5)
    private BigDecimal reclaimableTaxTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private EdsUser creator;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date")
    private Date creationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "modifier_id")
    private EdsUser modifier;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified_date")
    private Date modifiedDate;

    private boolean deleted;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        this.status = status;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public Date getFiledOn() {
        return filedOn;
    }

    public void setFiledOn(Date filedOn) {
        this.filedOn = filedOn;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public BigDecimal getPayableTaxTotal() {
        return payableTaxTotal;
    }

    public void setPayableTaxTotal(BigDecimal payableTaxTotal) {
        this.payableTaxTotal = payableTaxTotal;
    }

    public BigDecimal getReclaimableTaxTotal() {
        return reclaimableTaxTotal;
    }

    public void setReclaimableTaxTotal(BigDecimal reclaimableTaxTotal) {
        this.reclaimableTaxTotal = reclaimableTaxTotal;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public VatReturnItem getRPC() {
        VatReturnItem item = new VatReturnItem();
        item.setObjectID(objectID);

        if (getStatus() != null) {
            item.setStatus(getStatus().getAsSelectItem());
            item.getStatus().setCode(getStatus().getCode());
        }
        Optional.ofNullable(getFromDate()).ifPresent(date -> item.setFromDate(new DateNonConvertable(date)));
        Optional.ofNullable(getToDate()).ifPresent(date -> item.setToDate(new DateNonConvertable(date)));
        Optional.ofNullable(getFiledOn()).ifPresent(date -> item.setFiledOn(new DateNonConvertable(date)));
        Optional.ofNullable(getDueDate()).ifPresent(date -> item.setDue(new DateNonConvertable(date)));
        item.setPayableTaxTotal(getPayableTaxTotal());
        item.setReclaimableTaxTotal(getReclaimableTaxTotal());
        return item;
    }

    @Override
    public void setLastUpdateTime(Date value) {
        this.modifiedDate = value;
    }

    @Override
    public void setUpdater(EdsUser user) {
        this.modifier = user;
    }

    @Override
    public void setCreationTime(Date value) {
        this.creationDate = value;
    }

    @Override
    public void setCreator(EdsUser value) {
        this.creator = value;
    }

    public EdsUser getCreator() {
        return creator;
    }

    public String getPeriodKey() {
        return periodKey;
    }

    public void setPeriodKey(String periodKey) {
        this.periodKey = periodKey;
    }
}
