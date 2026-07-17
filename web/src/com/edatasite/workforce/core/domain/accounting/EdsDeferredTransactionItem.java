package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.enums.DeferredTransactionType;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Normurod Buriev.
 * Date: 6/16/2021 6:26 PM
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "deferred_transaction_item")
public class EdsDeferredTransactionItem extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Enumerated(EnumType.STRING)
    private DeferredTransactionType type;

    private Integer entityId;

    @Column(name = "service_id")
    private Integer serviceId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", insertable = false, updatable = false)
    private EdsItem service;

    @Temporal(TemporalType.DATE)
    private Date fromDate;
    @Temporal(TemporalType.DATE)
    private Date toDate;

    @Column(precision = 25, scale = 5)
    private BigDecimal dailyRate;

    @Column(name = "account_id")
    private Integer accountId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", insertable = false, updatable = false)
    private EdsAccount account;

    @Transient
    private int dayCount;
    @Transient
    private Date journalDate;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public DeferredTransactionType getType() {
        return type;
    }

    public void setType(DeferredTransactionType type) {
        this.type = type;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public EdsItem getService() {
        return service;
    }

    public void setService(EdsItem service) {
        this.service = service;
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

    public BigDecimal getDailyRate() {
        return dailyRate;
    }

    public void setDailyRate(BigDecimal dailyRate) {
        this.dailyRate = dailyRate;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public EdsAccount getAccount() {
        return account;
    }

    public void setAccount(EdsAccount account) {
        this.account = account;
    }

    public int getDayCount() {
        return dayCount;
    }

    public void setDayCount(int dayCount) {
        this.dayCount = dayCount;
    }

    public Date getJournalDate() {
        return journalDate;
    }

    public void setJournalDate(Date journalDate) {
        this.journalDate = journalDate;
    }
}
