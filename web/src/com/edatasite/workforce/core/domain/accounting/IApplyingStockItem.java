package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.workforce.core.domain.EdsItem;

import java.math.BigDecimal;

public interface IApplyingStockItem {

    EdsItem getItem();

    EdsWarehouse getWarehouse();

    BigDecimal getApplyingQuantity();

    Integer getTransactionItemId();

    BigDecimal getTransactionValue();

    void setTransactionValue(BigDecimal transactionValue);
}
