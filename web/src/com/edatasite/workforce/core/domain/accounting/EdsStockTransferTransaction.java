package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsStockTransfer;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.*;


@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "stock_transfer_transaction")
public class EdsStockTransferTransaction extends EdsTransaction {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stockTransferId")
    @ForeignKey(name = "none")
    private EdsStockTransfer stockTransfer;

    public EdsStockTransfer getStockTransfer() {
        return stockTransfer;
    }

    public void setStockTransfer(EdsStockTransfer stockTransfer) {
        this.stockTransfer = stockTransfer;
    }

    public Integer getKeyId() {
        return getStockTransfer().getObjectID();
    }

    public String getKeyType() {
        return STOCK_TRANSFER_TRANSACTION;
    }

}
