package com.edatasite.workforce.gwt.accounting.server.app.itemBatches;

import com.edatasite.workforce.core.domain.EdsAdjustmentItem;
import com.edatasite.workforce.core.domain.accounting.EdsBaseInvoiceItem;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsProductWarehouseLocation;
import com.edatasite.workforce.core.domain.accounting.EdsShippingDataItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductTrackBatchItem;

import java.util.ArrayList;

public interface ItemBatchServiceLocal {

    void createForOpeningBalance(EdsProductWarehouseLocation location);

    void createForGoodsReceived(Integer grnId, EdsShippingDataItem shippingDataItem);

    void assignForGoodsDelivered(Integer gdnId, EdsShippingDataItem shippingDataItem);

    void createBatchForPurchaseInvoice(Integer piId, NewInvoiceItem invoiceItem, Integer lineItemId);

    void createBatchForConvertedPurchaseInvoice(Integer piId, NewInvoiceItem invoiceItem, Integer lineItemId);

    void createBatchForSaleInvoice(Integer siId, NewInvoiceItem invoiceItem, Integer lineItemId);

    void createBatchForConvertedSaleInvoice(Integer siId, NewInvoiceItem invoiceItem, Integer lineItemId);

    void createBatchForStockAdjustment(Integer adjustmentId, EdsAdjustmentItem invoiceItem);

    void assignBatchForStockAdjustment(Integer adjustmentId, EdsAdjustmentItem invoiceItem);

    void assignBatchForStockTransferOut(Integer adjustmentId, EdsAdjustmentItem invoiceItem, String transferStatus);

    void createBatchForStockTransferIn(Integer adjustmentId, EdsAdjustmentItem invoiceItem, String transferStatus);

    void createBatchCreditNote(Integer noteId, EdsBaseInvoiceItem invoiceItem);

    void createBatchDebitNote(Integer noteId, EdsBaseInvoiceItem invoiceItem);

    ArrayList<ProductTrackBatchItem> getBatchItems(Integer lineItemId, Integer itemID, Integer entityId, String entityType);

    ArrayList<ProductTrackBatchItem> getBatchItemsOfGrnOrGdn(Integer quoteItemId, Integer itemID, Integer entityId, String entityType);

    void updateStockTransferBatchItemsStatus(Integer objectId);

    void deleteBatchSerialsForInvoice(EdsInvoice invoice);

    void deleteBatchSerialsForAdjustment(Integer adjustmentId);

    void deleteBatchSerialsForTransfer(Integer transferId);

}
