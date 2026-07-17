package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "vatadjustment_transaction")
public class EdsVatAdjustmentTransaction extends EdsTransaction {

    @Column(name = "vatadjustment_id")
    private Integer vatAdjustmentId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vatadjustment_id", updatable = false, insertable = false)
    private EdsVatAdjustment vatAdjustment;

    public Integer getVatAdjustmentId() {
        return vatAdjustmentId;
    }

    public void setVatAdjustmentId(Integer vatAdjustmentId) {
        this.vatAdjustmentId = vatAdjustmentId;
    }

    public EdsVatAdjustment getVatAdjustment() {
        return vatAdjustment;
    }

    public void setVatAdjustment(EdsVatAdjustment vatAdjustment) {
        this.vatAdjustment = vatAdjustment;
    }

    @Override
    public Integer getKeyId() {
        return vatAdjustmentId;
    }
}
