package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 5/11/11
 * Time: 3:36 PM
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "goodsReceivedTransaction")
public class EdsGoodsReceivedTransaction extends EdsTransaction {

    private Boolean isExpenseAllocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchaseorder_id")
    private EdsPurchaseOrder purchaseOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shippingDataId")
    private EdsShippingData shippingData;

    public EdsPurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(EdsPurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
        this.setCurrencyID(purchaseOrder.getCurrency() != null ? purchaseOrder.getCurrency().getObjectID() : null);
        this.setExchangeRate(purchaseOrder.getExchangeRate());
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

    public Boolean getExpenseAllocation() {
        return isExpenseAllocation;
    }

    public void setExpenseAllocation(Boolean expenseAllocation) {
        isExpenseAllocation = expenseAllocation;
    }

    public Integer getKeyId() {
        if (this.getPurchaseOrder() != null) {
            return this.getPurchaseOrder().getObjectID();
        }
        return this.getShippingData().getObjectID();
    }

    public String getKeyType() {
        return GOODS_RECEIVED_TRANSACTION;
    }
}
