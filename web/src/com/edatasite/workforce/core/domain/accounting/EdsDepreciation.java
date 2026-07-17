package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/11/11
 * Time: 12:46 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "depreciation")
public class EdsDepreciation extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixedassetid")
    private EdsFixedAsset fixedAsset;

    private Date month;
    private Date month_old;

    private Integer period;

    @Column(precision = 25, scale = 5)
    private BigDecimal amount;

    @Column(name = "deleted")
    private Boolean deleted = false;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsFixedAsset getFixedAsset() {
        return fixedAsset;
    }

    public void setFixedAsset(EdsFixedAsset fixedAsset) {
        this.fixedAsset = fixedAsset;
    }

    public Date getMonth() {
        return month;
    }

    public void setMonth(Date month) {
        this.month = month;
    }

    public Integer getPeriod() {
        return period;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Boolean isDeleted() {
        return deleted == null ? Boolean.FALSE : deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
