package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/27/11
 * Time: 4:16 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "fixedAssetTransaction")
public class EdsFixedAssetTransaction extends EdsTransaction {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixedassetid")
    private EdsFixedAsset fixedAsset;

    public EdsFixedAsset getFixedAsset() {
        return fixedAsset;
    }

    public void setFixedAsset(EdsFixedAsset fixedAsset) {
        this.fixedAsset = fixedAsset;

        if (fixedAsset.getPurchaseInvoice() != null) {
            EdsInvoice pInv = fixedAsset.getPurchaseInvoice();
            setCurrencyID(pInv.getCurrency() != null ? pInv.getCurrency().getObjectID() : null);
            setExchangeRate(pInv.getExchangeRate());
        }
    }

    public Integer getKeyId() {
        return getFixedAsset().getObjectID();
    }

    public String getKeyType() {
        return FIXED_ASSET_TRANSACTION;
    }
}
