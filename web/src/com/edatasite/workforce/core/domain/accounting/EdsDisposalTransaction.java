package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "disposalTransaction")
public class EdsDisposalTransaction extends EdsTransaction {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fixedassetid")
    private EdsFixedAsset fixedAsset;

    public EdsFixedAsset getFixedAsset() {
        return fixedAsset;
    }

    public void setFixedAsset(EdsFixedAsset fixedAsset) {
        this.fixedAsset = fixedAsset;

        if (fixedAsset.getSalesInvoice() != null) {
            EdsInvoice sInv = fixedAsset.getSalesInvoice();
            setCurrencyID(sInv.getCurrency() != null ? sInv.getCurrency().getObjectID() : null);
            setExchangeRate(sInv.getExchangeRate());
        }
    }

    public Integer getKeyId() {
        return getFixedAsset().getObjectID();
    }

    public String getKeyType() {
        return DISPOSAL_TRANSACTION;
    }
}
