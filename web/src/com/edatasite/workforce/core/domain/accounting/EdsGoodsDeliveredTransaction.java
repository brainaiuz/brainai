package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Optional;

/**
 * Created by Normurod on 6/28/2017.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "goodsDeliveredTransaction")
public class EdsGoodsDeliveredTransaction extends EdsTransaction {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "saleorder_id")
    private EdsSaleQuote saleOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shippingDataId")
    private EdsShippingData shippingData;

    public EdsSaleQuote getSaleOrder() {
        return saleOrder;
    }

    @Deprecated
    public void setSaleOrder(EdsSaleQuote saleOrder) {
        this.saleOrder= saleOrder;

        setCurrencyID(saleOrder.getCurrency() != null ? saleOrder.getCurrency().getObjectID() : null);
        setExchangeRate(saleOrder.getExchangeRate());
    }

    public EdsShippingData getShippingData() {
        return shippingData;
    }

    public void setShippingData(EdsShippingData shippingData) {
        this.shippingData = shippingData;
        if (shippingData == null) {
            return;
        }
        Optional.ofNullable(shippingData.getCurrency()).ifPresent(edsCurrency -> this.setCurrencyID(edsCurrency.getObjectID()));
        Optional.ofNullable(shippingData.getQuote()).ifPresent(edsQuote -> this.setExchangeRate(edsQuote.getExchangeRate()));
    }

    public Integer getKeyId() {
        return saleOrder != null ? saleOrder.getObjectID() : (shippingData != null ? shippingData.getObjectID() : null);
    }

    public String getKeyType() {
        return GOODS_DELIVERED_TRANSACTION;
    }
}
