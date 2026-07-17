package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsStockAdjustment;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 11/23/11
 * Time: 4:50 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "adjustment_transaction")
public class EdsStockAdjustmentTransaction extends EdsTransaction {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adjustment_id")
    private EdsStockAdjustment adjustment;

    public EdsStockAdjustment getAdjustment() {
        return adjustment;
    }

    public void setAdjustment(EdsStockAdjustment adjustment) {
        this.adjustment = adjustment;
    }

    public Integer getKeyId() {
        return getAdjustment().getObjectID();
    }

    public String getKeyType() {
        return ADJUSTMENT_TRANSACTION;
    }
}
