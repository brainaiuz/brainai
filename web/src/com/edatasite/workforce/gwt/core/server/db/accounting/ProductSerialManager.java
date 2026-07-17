package com.edatasite.workforce.gwt.core.server.db.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsProductSerial;
import com.edatasite.workforce.core.domain.accounting.EdsShippingData;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;
import com.edatasite.workforce.gwt.invoice.client.rpc.ProductSerialItem;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/8/12
 * Time: 5:05 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ProductSerialManager extends Manager<EdsProductSerial> {
    List<EdsProductSerial> getProductSerials(ListingFilterParameter filterParametrs);

    List<Integer> getProductSerialsByPurchaseOrderItems(List<Integer> orderItemsDeleted);

    List<Integer> getProductSerialsBySalesInvoiceItems(List<Integer> invoiceItemsDeleted);

    ProductSerialItem[] getOrderItemSerialsAsSelectItem(Integer orderItemID);

    ProductSerialItem[] getInvoiceItemSerialsAsSelectItem(Integer invoiceItemID);

    List<ProductSerialItem> getInvoiceItemBatchSerials(Integer lineItemID, Integer productId, boolean fromGdn);

    List<Integer> getProductSerialsByGDN(List<Integer> invoiceItemsDeleted);

    void removeSalesInvoiceFromProductSerials(Integer salesInvoiceID);

    ArrayList<ProductSerialItem> getProductSerialsByItemID(Integer itemID);

    ArrayList<ProductSerialItem> getProductSerialsWithQtyByItemID(Integer itemID);

    Integer getProductSerialsQty(Integer itemID, String serialNumber, Date expirationDate);

    List<EdsProductSerial> getProductSerialsByCount(Integer itemID, EdsProductSerial edsProductSerial, Integer qty);

    ProductSerialItem getFirstProductSerialByIds(Integer orderItemId, Integer itemId, String type);

    List<ProductSerialItem> getProductSerialByIds(Integer orderItemId, Integer itemId, String type);

    void removePurchaseOrderProductSerials(Integer purchaseOrderId);

    void removeGrnSerialNumbers(Integer grnId);

    Map<Integer, ProductSerialItem> getFirstSerialsByInvoiceItemIds(Set<Integer> invoiceItemIds, Set<Integer> itemIds, String purchaseOrder);

    Map<Integer, List<ProductSerialItem>> getBatchSerialsByInvoiceItemIds(Set<Integer> invoiceItemIds, Set<Integer> itemIds, Set<EdsShippingData> edsShippingData);
}
