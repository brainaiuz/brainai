package com.edatasite.workforce.gwt.accounting.server.app.itemserials;

import com.edatasite.workforce.core.domain.EdsAdjustmentItem;
import com.edatasite.workforce.core.domain.accounting.EdsBaseInvoiceItem;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsProductWarehouseLocation;
import com.edatasite.workforce.core.domain.accounting.EdsShippingDataItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.ItemSerialEntityType;

import java.util.ArrayList;
import java.util.List;

public interface ItemSerialServiceLocal {

    ArrayList<String> getSerials(Integer entityId, ItemSerialEntityType entityType);

    void createForOpeningBalance(EdsProductWarehouseLocation location, Integer transactionId);

    void createForStockAdjustment(EdsAdjustmentItem adjustmentItem, Integer transactionId);

    void createForGoodsReceived(EdsShippingDataItem shippingDataItem, Integer transactionId);

    void createForPurchaseInvoice(EdsBaseInvoiceItem invoiceItem, Integer transactionId);

    void assignForStockAdjustment(EdsAdjustmentItem adjustmentItem, Integer transactionId);

    void assignForGoodsDelivered(EdsShippingDataItem shippingDataItem, Integer transactionId);

    void assignForSalesInvoice(EdsBaseInvoiceItem invoiceItem, Integer transactionId);

    void assignForCreditNote(EdsBaseInvoiceItem invoiceItem, Integer transactionId);

    void assignForDebitNote(EdsBaseInvoiceItem invoiceItem, Integer transactionId);

    void voidForInvoice(EdsInvoice invoice, Integer transactionId);

    void deleteSerials(Integer itemID);

    void deleteForInvoice(EdsInvoice invoice);

    void deleteForInvoice(EdsInvoice invoice, List<Integer> entityIds);

    void deleteSerialRelation(Integer entityId, String entityType);
}
