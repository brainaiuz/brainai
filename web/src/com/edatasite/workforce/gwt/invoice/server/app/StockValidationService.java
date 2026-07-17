package com.edatasite.workforce.gwt.invoice.server.app;


import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsProductKitItems;
import com.edatasite.workforce.core.domain.EdsStockAdjustment;
import com.edatasite.workforce.core.domain.EdsStockTransfer;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsGoodsDeliveredTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsShippingData;
import com.edatasite.workforce.core.domain.accounting.EdsTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsWarehouse;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteManager;
import com.edatasite.workforce.gwt.core.server.db.ShippingDataManager;
import com.edatasite.workforce.gwt.core.server.db.StockAdjustmentManager;
import com.edatasite.workforce.gwt.core.server.db.StockTransferManager;
import com.edatasite.workforce.gwt.core.server.db.TransactionManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ItemStockManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.PickListManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.WarehouseManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.invoice.client.rpc.QuantityItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.ProcessType;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.StockOutFlow;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.StockTransactionType;
import com.google.common.collect.Table;
import com.google.common.collect.Tables;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
@Service("stockValidationService")
public class StockValidationService implements AccountingConstants {

    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private ItemManager itemManager;
    @Autowired
    private WarehouseManager warehouseManager;
    @Autowired
    private TransactionManager transactionManager;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private StockAdjustmentManager stockAdjustmentManager;
    @Autowired
    private ItemStockManager itemStockManager;
    @Autowired
    private QuoteManager quoteManager;
    @Autowired
    private StockTransferManager stockTransferManager;
    @Autowired
    private PickListManager pickListManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private ShippingDataManager shippingDataManager;

    private static final Integer NO_WAREHOUSE = 0;

    public SelectItem[] validateStockAvailability(QuantityItem[] qItems, Integer entityID, StockOutFlow outFlow, DateNonConvertable tillDate) {

        if (qItems == null || qItems.length == 0) {
            return new SelectItem[]{};
        }

        //requested products qty by given warehouse
        Table<Integer, Integer, BigDecimal> requestedItems = generateRequestedProducts(qItems);

        //we'll use the transactionId/s if we're modifying existing delivery process
        List<Integer> transactionIds = new ArrayList<>();

        if (entityID != null) {
            EdsTransaction transaction = null;
            switch (outFlow) {
                case FROM_SALE_INVOICE, FROM_DEBIT_NOTE -> {
                    EdsInvoice invoice = invoiceManager.get(entityID);
                    if (invoice != null) {
                        transaction = transactionManager.getTransactionByInvoice(invoice);

                        if (transaction != null) {
                            transactionIds.add(transaction.getObjectID());
                        }
                    }
                }
                case FROM_GOODS_DELIVERY_NOTES -> {
                    EdsShippingData shippingData = shippingDataManager.get(entityID);
                    if (shippingData != null) {
                        List<EdsGoodsDeliveredTransaction> gdnTransactions = transactionManager.getGoodsDeliverdTransactionByShippingData(shippingData);
                        gdnTransactions.forEach(gdn -> transactionIds.add(gdn.getObjectID()));
                    }
                }
                case FROM_STOCK_ADJUSTMENT -> {
                    EdsStockAdjustment adjustment = stockAdjustmentManager.get(entityID);
                    if (adjustment != null) {
                        transaction = transactionManager.getTransactionByStockAdjustment(adjustment);

                        if (transaction != null) {
                            transactionIds.add(transaction.getObjectID());
                        }
                    }
                }
                case FROM_STOCK_TRANSFER -> {
                    EdsStockTransfer transfer = stockTransferManager.get(entityID);
                    List<Integer> adjustmentIds = transfer.getItems().stream().filter(sa -> !sa.isDeleted()).map(EdsStockAdjustment::getObjectID).collect(Collectors.toList());
                    if (adjustmentIds != null && !adjustmentIds.isEmpty()) {
                        transactionIds.addAll(transactionManager.getTransactionIdsByAdjustments(adjustmentIds));
                    }
                    EdsTransaction tr = transactionManager.getTransactionByStockTransfer(transfer);
                    if (tr != null) {
                        transactionIds.add(tr.getObjectID());
                    }
                }
            }
        }

        List<SelectItem> errors = new ArrayList<>();
        int countRow = 0;
        //main logic for stock validation
        for (Integer productId : requestedItems.rowKeySet()) {
            //iterate by warehouse to check have available quantity of product
            for (Integer warehouseId : requestedItems.row(productId).keySet()) {

                BigDecimal requestedQty = requestedItems.get(productId, warehouseId).setScale(ServerUtils.getCalculationScale(), RoundingMode.HALF_UP);
                BigDecimal availableQty;
                if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.DISABLE_DATE_BASED_STOCK_VALIDATION)) {
                    availableQty = itemStockManager.getAvailableStock(productId, NO_WAREHOUSE.equals(warehouseId) ? null : warehouseId, transactionIds)
                            .setScale(ServerUtils.getCalculationScale(), RoundingMode.HALF_UP);
                } else {
                    availableQty = itemStockManager.getAvailableStock(productId, NO_WAREHOUSE.equals(warehouseId) ? null : warehouseId, transactionIds, tillDate)
                            .setScale(ServerUtils.getCalculationScale(), RoundingMode.HALF_UP);
                }

                SelectItem bookingItem = quoteManager.getBookingQty(productId, warehouseId, entityID);

                BigDecimal bookingQty = BigDecimal.valueOf(bookingItem.getTotalAmount());

                availableQty = availableQty.subtract(bookingQty);

                if (requestedQty.compareTo(availableQty) > 0) {
                    EdsItem product = itemManager.get(productId);
                    errors.add(new SelectItem(product.getObjectID(), product.getName(), bookingItem.getDescription(), countRow));
                }
            }
            countRow++;
        }

        return errors.toArray(new SelectItem[]{});
    }

    public SelectItem validateStockInconsistency(StockTransactionType transactionType, Integer entityID, QuantityItem[] qItems, Integer[] assemblyTransactionIds, ProcessType processType) {
        if (entityID == null && !StockTransactionType.ASSEMBLY.equals(transactionType)) {
            return null;
        }
        List<Integer> transactionIds = new ArrayList<>();
        switch (transactionType) {
            case PURCHASE_INVOICE, CREDIT_NOTE -> {
                EdsInvoice invoice = invoiceManager.get(entityID);
                if (!CollectionUtils.isEmpty(invoice.getConvertedShippingData())) {
                    return null;
                }
                Optional.ofNullable(transactionManager.getTransactionByInvoice(invoice)).ifPresent(t -> transactionIds.add(t.getObjectID()));
            }
            case GRN -> {
                EdsShippingData grn = shippingDataManager.get(entityID);
                transactionManager.getTransactionsByShippingData(grn).forEach(t -> transactionIds.add(t.getObjectID()));
            }
            case ADJUSTMENT -> {
                EdsStockAdjustment adjustment = stockAdjustmentManager.get(entityID);
                Optional.ofNullable(transactionManager.getTransactionByStockAdjustment(adjustment)).ifPresent(t -> transactionIds.add(t.getObjectID()));
            }
            case TRANSFER -> {
                EdsStockTransfer transfer = stockTransferManager.get(entityID);
                Optional.ofNullable(transactionManager.getTransactionByStockTransfer(transfer)).ifPresent(t -> transactionIds.add(t.getObjectID()));
            }
            case ASSEMBLY -> {
                if (assemblyTransactionIds != null && assemblyTransactionIds.length > 0)
                    transactionIds.addAll(Arrays.asList(assemblyTransactionIds));
            }
        }
        Table<Integer, Integer, BigDecimal> requestedItems = null;
        if (ProcessType.DELETE.equals(processType)) {
            qItems = itemStockManager.getInStocksByTransactions(transactionIds).toArray(new QuantityItem[]{});
            requestedItems = generateRequestedProducts(qItems);
        } else {
            requestedItems = generateRequestedProducts(qItems);
        }

        for (Integer productId : requestedItems.rowKeySet()) {

            //iterate by warehouse to check have available quantity of product
            for (Integer warehouseId : requestedItems.row(productId).keySet()) {

                BigDecimal requestedQty = requestedItems.get(productId, warehouseId).setScale(ServerUtils.getCalculationScale(), RoundingMode.HALF_UP);
                BigDecimal availableQty = itemStockManager.getAvailableStock(productId, NO_WAREHOUSE.equals(warehouseId) ? null : warehouseId, transactionIds)
                        .setScale(ServerUtils.getCalculationScale(), RoundingMode.HALF_UP);
                if ((ProcessType.DELETE.equals(processType) && requestedQty.compareTo(BigDecimal.ZERO) > 0) || (!ProcessType.DELETE.equals(processType) && requestedQty.compareTo(BigDecimal.ZERO) < 0)) {
                    continue;
                }
                BigDecimal onHand = ProcessType.DELETE.equals(processType) || requestedQty.compareTo(BigDecimal.ZERO) < 0 ? availableQty : availableQty.add(requestedQty);
                if (onHand.compareTo(BigDecimal.ZERO) < 0) {
                    EdsItem product = itemManager.get(productId);
                    return product.getAsProductSelectItem();
                }
            }
        }
        return null;
    }

    public BigDecimal countItemsInStock(QuantityItem qItem) {
        return itemStockManager.getAvailableStock(qItem.getId(), qItem.getWarehouseID(), null);
    }

    /**
     * Groupping all requested product with their warehouse to validate from stock
     *
     * @param qItems
     * @return
     */
    private Table<Integer, Integer, BigDecimal> generateRequestedProducts(QuantityItem[] qItems) {
        //contains all requested products to delivery with given warehouse and quantities
        Table<Integer, Integer, BigDecimal> requestedItems = Tables.newCustomTable(new LinkedHashMap<>(), LinkedHashMap::new);

        List<Integer> Ids = Arrays.stream(qItems).filter(quantityItem -> quantityItem.getQuantity().compareTo(BigDecimal.ZERO) != 0).map(QuantityItem::getId).toList();

        if (Ids.isEmpty()) {
            return requestedItems;
        }

        //retrieve list of product data from db by their Ids and map them
        Map<Integer, EdsItem> mapProducts = itemManager.getItemsByIds(StringUtils.join(Ids, ",")).stream().collect(Collectors.toMap(EdsItem::getObjectID, p -> p));

        //get default warehouse in the case of given warehouse is null
        EdsWarehouse defaultWarehouse = warehouseManager.getDefaultWarehouse();

        //if system uses multi warehouse
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        boolean hasMultiWarehouse = financialSettings.getEnableMultiWarehouse();
        for (QuantityItem qItem : qItems) {

            if (qItem.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
                continue;
            }
            EdsItem product = mapProducts.get(qItem.getId());

            if (product == null) {
                continue;
            }
            Integer warehouseId = !hasMultiWarehouse ? defaultWarehouse.getObjectID() : qItem.getWarehouseID() != null ? qItem.getWarehouseID() : NO_WAREHOUSE;

            if (INVENTORY_ITEM.equals(product.getType()) || ASSEMBLY_ITEM.equals(product.getType())) {

                if (requestedItems.get(qItem.getId(), warehouseId) != null) {
                    requestedItems.put(qItem.getId(), warehouseId, requestedItems.get(qItem.getId(), warehouseId).add(qItem.getQuantity()));
                } else {
                    requestedItems.put(qItem.getId(), warehouseId, qItem.getQuantity());
                }

            } else if (PRODUCT_KIT.equals(product.getType())) {
                initProductKit(product, qItem.getQuantity(), warehouseId, requestedItems);
            }
        }
        return requestedItems;
    }

    /**
     *
     * @param productKit
     * @param qty
     * @param warehouseId
     * @param requestedItems
     */
    private void initProductKit(EdsItem productKit, BigDecimal qty, Integer warehouseId, Table<Integer, Integer, BigDecimal> requestedItems) {

        for (EdsProductKitItems kitItem : productKit.getProductKitItems()) {
            EdsItem product = kitItem.getItem();
            BigDecimal kitItemQty = kitItem.getQuantity().multiply(qty).setScale(ServerUtils.getCalculationScale(), RoundingMode.HALF_UP);

            if (INVENTORY_ITEM.equals(product.getType()) || ASSEMBLY_ITEM.equals(product.getType())) {

                if (requestedItems.get(product.getObjectID(), warehouseId) != null) {
                    requestedItems.put(product.getObjectID(), warehouseId, requestedItems.get(product.getObjectID(), warehouseId).add(kitItemQty));
                } else {
                    requestedItems.put(product.getObjectID(), warehouseId, kitItem.getQuantity().multiply(qty));
                }

            } else if (PRODUCT_KIT.equals(product.getType())) {
                initProductKit(product, kitItemQty, warehouseId, requestedItems);
            }
        }
    }
}
