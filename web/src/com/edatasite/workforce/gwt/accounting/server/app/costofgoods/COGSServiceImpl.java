package com.edatasite.workforce.gwt.accounting.server.app.costofgoods;

import com.edatasite.workforce.core.domain.EdsAdjustmentItem;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.accounting.*;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.accounting.ItemStockManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.StockCalcManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.WarehouseManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.TransactionCustomEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FIFOItemMQ;
import com.edatasite.workforce.gwt.core.server.rabbitmq.data.FifoItem;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class COGSServiceImpl implements COGSService {
    protected static final Logger log = LoggerFactory.getLogger(COGSServiceImpl.class);

    @Autowired
    protected TransactionManager transactionManager;
    @Autowired
    protected ItemManager itemManager;
    @Autowired
    protected ItemStockManager itemStockManager;
    @Autowired
    protected TransactionItemManager transactionItemManager;
    @Autowired
    protected WarehouseManager warehouseManager;
    @Autowired
    protected StockCalcManager stockCalcManager;
    @Autowired
    protected FinancialSettingsManager financialSettingsManager;
    @Autowired
    private StockAdjustmentItemManager stockAdjustmentItemManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private GenericSettingsManager genericSettingsManager;

    @Override
    @Transactional
    public BigDecimal createCogsTransaction(FifoItem fifoItem, boolean fromProductGroup) {
        synchronized (this) {
            EdsTransaction transaction = transactionManager.get(fifoItem.getTransactionId());
            EdsItem product = itemManager.getItem(fifoItem.getProductId());
            BigDecimal itemQty = fifoItem.getQuantity();

            //clear old transaction items related to the given inventory
            transactionManager.deleteCOGSTransactionItems(transaction.getObjectID(), product.getObjectID(), fifoItem.getTransactionItemId());
            itemStockManager.deleteItemStocksByTransaction(transaction.getObjectID(), Constants.TC_OUT, product.getObjectID(), fifoItem.getWarehouserId(), fifoItem.getTransactionItemId());
            BigDecimal COGS = getCOGSValue(product, itemQty, transaction, fifoItem.getWarehouserId(), fifoItem.getTransactionItemId());

            if (transaction instanceof EdsStockTransferTransaction) {
                createStockTransferOutTransactionItem(COGS, product, transaction, fifoItem.getTransactionItemId(), fromProductGroup);
            } else {
                createTransactionItems(COGS, product, transaction, Constants.TC_OUT, fifoItem.getTransactionItemId());
            }

            return COGS;
        }
    }

    @Override
    @Transactional
    public BigDecimal createCogsTransaction(FIFOItemMQ fifoItem, Integer transactionId) {
        EdsTransaction transaction = transactionManager.get(transactionId);
        EdsItem product = itemManager.get(fifoItem.getProductId());
        BigDecimal itemQty = fifoItem.getQuantity();

        //clear old transaction items related to the given inventory
        transactionManager.deleteCOGSTransactionItems(transaction.getObjectID(), product.getObjectID(), fifoItem.getTransactionItemId());
        itemStockManager.deleteItemStocksByTransaction(transaction.getObjectID(), Constants.TC_OUT, product.getObjectID(), fifoItem.getWarehouserId(), fifoItem.getTransactionItemId());
        BigDecimal COGS = getCOGSValue(product, itemQty, transaction, fifoItem.getWarehouserId(), fifoItem.getTransactionItemId());

        if (transaction instanceof EdsStockTransferTransaction) {
            createStockTransferOutTransactionItem(COGS, product, transaction, fifoItem.getTransactionItemId(), false);
        } else {
            createTransactionItems(COGS, product, transaction, Constants.TC_OUT, fifoItem.getTransactionItemId());
        }

        return COGS;
    }

    @Override
    @Transactional
    public BigDecimal createCogsTransaction(FifoItem fifoItem) {
        return createCogsTransaction(fifoItem, false);
    }

    @Override
    @Transactional
    public BigDecimal applyPurchasesToItemStock(IApplyingStockItem applyingItem, EdsTransaction transaction, EdsItem item, BigDecimal productKitItemQty) {

        if (transaction.getStockProductIdentifierList().containsKey(item.getObjectID())) {
            transaction.getStockProductIdentifierList().put(item.getObjectID(), transaction.getStockProductIdentifierList().get(item.getObjectID()) + 1);
        } else {
            transaction.getStockProductIdentifierList().put(item.getObjectID(), 1);
        }
        EdsItemStock itemStock = new EdsItemStock();
        itemStock.setProductIdentifier(transaction.getStockProductIdentifier(item.getObjectID()));
        itemStock.setWarehouse(applyingItem.getWarehouse() != null ? applyingItem.getWarehouse() : warehouseManager.getDefaultWarehouse());
        itemStock.setQuantity(applyingItem.getApplyingQuantity());

        if (productKitItemQty != null) {
            itemStock.setQuantity(itemStock.getQuantity().multiply(productKitItemQty));
        }
        BigDecimal transValue = applyingItem.getTransactionValue();
        BigDecimal transQty = itemStock.getQuantity() != null && itemStock.getQuantity().compareTo(BigDecimal.ZERO) > 0 ? itemStock.getQuantity() : BigDecimal.ONE;

        itemStock.setPrice(transQty.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : transValue.divide(transQty, ServerUtils.getSystemPriceScale(), RoundingMode.HALF_EVEN));
        itemStock.setDate(transaction.getJournalDate());
        itemStock.setTranDate(transaction.getJournalDate());
        itemStock.setTranValue(transValue);
        itemStock.setTranCode(Constants.TC_IN);
        itemStock.setItemId(item.getObjectID());
        itemStock.setTransaction(transaction);
        itemStock.setTransactionItemId(applyingItem.getTransactionItemId());

        if (itemStock.getObjectID() == null) {
            itemStock.setOrder(itemStockManager.getNextOrder(item.getObjectID()));
        }
        itemStockManager.createOrUpdate(itemStock);
        return itemStock.getTranValue();
    }

    @Override
    @Transactional
    public void applyReturnedItemsToStock(EdsBaseInvoiceItem invoiceItem, EdsTransaction transaction) {
        EdsItem item = invoiceItem.getItem();
        if (!(item != null && item.getType() != null && (item.enableIT() != null && item.enableIT() || financialSettingsManager.getFinancialSettings().enableIT()))) {
            return;
        }

        BigDecimal returnedQty = (invoiceItem instanceof EdsQuoteItem) ? ((EdsQuoteItem) invoiceItem).getShip() : invoiceItem.getQty();
        Integer warehouseId = invoiceItem.getWarehouse() != null ? invoiceItem.getWarehouse().getObjectID() : null;
        HashBasedTable<Integer, Integer, BigDecimal> returnedItems = COGSService.mapRequestedItems(item, returnedQty, warehouseId, HashBasedTable.create());
        createItemStocks(returnedItems, transaction, invoiceItem.getObjectID(), null);
    }

    @Override
    @Transactional
    public void applyAdjustedItemsToStock(EdsAdjustmentItem adjustmentItem, EdsTransaction transaction) {
        EdsItem item = adjustmentItem.getItem();
        if (!(item != null && item.getType() != null && (item.enableIT() != null && item.enableIT() || financialSettingsManager.getFinancialSettings().enableIT()))) {
            return;
        }
        if (adjustmentItem.getNewQty() == null || adjustmentItem.getNewQty().compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }
        Integer warehouseId = adjustmentItem.getWarehouse() != null ? adjustmentItem.getWarehouse().getObjectID() : null;
        HashBasedTable<Integer, Integer, BigDecimal> adjustedItems = COGSService.mapRequestedItems(item, adjustmentItem.getNewQty(), warehouseId, HashBasedTable.create());
        createItemStocks(adjustedItems, transaction, adjustmentItem.getObjectID(), adjustmentItem.getPrice());
    }

    @Override
    @Transactional
    public void applyStockTransferToItemStock(EdsItem item, BigDecimal COGS, BigDecimal itemQty, EdsTransaction transaction, Integer warehouseId, Integer transactionItemId) {
        applyStockTransferToItemStock(item, COGS, itemQty, transaction, warehouseId, transactionItemId, false);
    }

    @Override
    @Transactional
    public void applyStockTransferToItemStock(EdsItem item, BigDecimal COGS, BigDecimal itemQty, EdsTransaction transaction, Integer warehouseId, Integer transactionItemId, boolean runPostProcessor) {
        applyStockTransferToItemStock(item, COGS, itemQty, transaction, warehouseId, transactionItemId, false, false);
    }

    @Override
    @Transactional
    public void applyStockTransferToItemStock(EdsItem item, BigDecimal COGS, BigDecimal itemQty, EdsTransaction transaction, Integer warehouseId, Integer transactionItemId, boolean runPostProcessor, boolean fromProductGroup) {
        do_not_extra_action_for_the_same_COGS_amount:
        {
            BigDecimal oldCOGS = transactionManager.getCOGSAmountOfStockTransferItem(transaction.getObjectID(), item.getObjectID(), transactionItemId);
            if (oldCOGS != null && COGS.compareTo(oldCOGS) == 0) {
                return;
            }
        }

        //clear old transaction items related to the given inventory
        transactionManager.deleteStockTransferTransactionItems(transaction.getObjectID(), item.getObjectID(), transactionItemId);
        itemStockManager.deleteItemStocksByTransaction(transaction.getObjectID(), Constants.TC_IN, item.getObjectID(), warehouseId, transactionItemId);

        EdsItemStock itemStock = new EdsItemStock();
        EdsWarehouse warehouse = warehouseManager.get(warehouseId);

        if (transaction.getStockProductIdentifierList().containsKey(item.getObjectID())) {
            transaction.getStockProductIdentifierList().put(item.getObjectID(), transaction.getStockProductIdentifierList().get(item.getObjectID()) + 1);
        } else {
            transaction.getStockProductIdentifierList().put(item.getObjectID(), 1);
        }
        BigDecimal unitPrice = BigDecimal.ZERO;

        if (BigDecimal.ZERO.compareTo(itemQty) != 0) {
            unitPrice = COGS.divide(itemQty, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
        }

        itemStock.setQuantity(itemQty);
        itemStock.setPrice(unitPrice);
        itemStock.setDate(transaction.getJournalDate());
        itemStock.setTranDate(transaction.getJournalDate());
        itemStock.setTranValue(itemQty.multiply(unitPrice));
        itemStock.setTranCode(Constants.TC_IN);
        itemStock.setOrder(itemStockManager.getNextOrder(item.getObjectID()));
        itemStock.setItemId(item.getObjectID());
        itemStock.setWarehouse(warehouse);
        itemStock.setProductIdentifier(transaction.getStockProductIdentifier(item.getObjectID()));
        itemStock.setTransaction(transaction);
        itemStock.setTransactionItemId(transactionItemId);
        itemStockManager.createOrUpdate(itemStock);


        EdsTransactionItem transactionItem = new EdsTransactionItem();
        transactionItem.setTransaction(transaction);
        transactionItem.setDebit(COGS);
        transactionItem.setInventoryId(item.getObjectID());

        EdsAdjustmentItem toItem = stockAdjustmentItemManager.get(transactionItemId);
        transactionItem.setAccount(fromProductGroup ? item.getAssetAccount() : toItem.getAdjustment().getToAccount());
        transactionItem.setItemId(toItem.getObjectID());
        transactionItem.setStockAdjustmentItem(toItem);
        transactionItemManager.create(transactionItem);

        if (runPostProcessor) {
            baseEventPostProcessor.registerEvent(TransactionCustomEventListenerImpl.TYPE, TransactionCustomEventListenerImpl.EVENT_STOCK_TRANSFER_IN_TRANSACTION, toItem, transaction.getPostedBy() != null ? transaction.getPostedBy() : null);
        }
    }

    /**
     * Increase stock by received items
     *
     * @param items
     * @param transaction
     * @param transactionItemId = it could be invoice item id, adjustment item id etc.
     */
    private void createItemStocks(HashBasedTable<Integer, Integer, BigDecimal> items, EdsTransaction transaction, Integer transactionItemId, BigDecimal unitPrice) {
        for (Table.Cell<Integer, Integer, BigDecimal> cell : items.cellSet()) {
            EdsItem product = itemManager.get(cell.getRowKey());
            EdsWarehouse warehouse = cell.getColumnKey() != null ? warehouseManager.get(cell.getColumnKey()) : warehouseManager.getDefaultWarehouse();
            BigDecimal qty = cell.getValue();
            BigDecimal price = BigDecimal.ZERO;

            if (unitPrice != null) {
                price = unitPrice;
            } else {
                EdsItemStock firstInBeforeTransaction = stockCalcManager.getFirstInStockBeforeTransaction(product.getObjectID(), warehouse.getObjectID(), transaction);

                if (firstInBeforeTransaction != null) {
                    price = firstInBeforeTransaction.getPrice();
                } else {
                    price = product.getUnitPrice();
                }
            }

            BigDecimal transValue = qty.multiply(price != null ? price : BigDecimal.ZERO);

            EdsItemStock itemStock = new EdsItemStock();
            itemStock.setWarehouse(warehouse);
            itemStock.setQuantity(qty);
            itemStock.setPrice(price);
            itemStock.setDate(transaction.getJournalDate());
            itemStock.setTranDate(transaction.getJournalDate());
            itemStock.setTranValue(transValue);
            itemStock.setTranCode(Constants.TC_IN);
            itemStock.setItemId(product.getObjectID());
            itemStock.setTransaction(transaction);
            itemStock.setOrder(itemStockManager.getNextOrder(product.getObjectID()));
            itemStock.setTransactionItemId(transactionItemId);
            itemStockManager.createOrUpdate(itemStock);
            createTransactionItems(transValue, product, transaction, Constants.TC_IN, transactionItemId);
        }
    }

    /**
     * transaction type = {TC_IN, TC_OUT}
     *
     * @param fifoResult
     * @param product
     * @param transaction
     * @param transactionType
     * @param transactionItemId - it could be invoice item id, adjustment item id etc.
     */
    protected void createTransactionItems(BigDecimal COGS, EdsItem product, EdsTransaction transaction, String transactionType, Integer transactionItemId) {
        EdsProject project = null;
        if (transaction instanceof EdsStockAdjustmentTransaction) {
            EdsAdjustmentItem adjustmentItem = stockAdjustmentItemManager.get(transactionItemId);
            project = adjustmentItem.getProject();
        }

        EdsTransactionItem transactionItem = new EdsTransactionItem();
        transactionItem.setAccount(product.getAssetAccount());
        transactionItem.setInventoryId(product.getObjectID());
        transactionItem.setItemId(transactionItemId);
        transactionItem.setCogsItem(true);

        if (Constants.TC_OUT.equals(transactionType)) {
            transactionItem.setCredit(COGS);
        } else {
            transactionItem.setDebit(COGS);
        }
        transactionItem.setTransaction(transaction);
        transactionItem.setProject(project);
        transactionItemManager.create(transactionItem);

        /*
         * DO NOT WRITE COGS(expense) transaction item, If there is an Assimbly Transaction
         */
        if (Constants.TC_OUT.equals(transactionType) &&
                transaction instanceof EdsInventoryTransaction &&
                Constants.TT_BUILD_ASSEMBLY.equals(((EdsInventoryTransaction) transaction).getTransactionType())) {
            baseEventPostProcessor.registerEvent(TransactionCustomEventListenerImpl.TYPE, TransactionCustomEventListenerImpl.EVENT_CORRECTION_ASSEMBLY_BUILD_TRANSACTION, (EdsTransaction) transaction, transactionManager.getUser());
            return;
        }
        transactionItem = new EdsTransactionItem();
        if (transaction instanceof EdsStockAdjustmentTransaction || Constants.ADJUSTMENT_TRANSACTION.equals(transaction.getKeyType())) {
            EdsAccount adjustmentAccount = ((EdsStockAdjustmentTransaction) transaction).getAdjustment().getAccount();
            transactionItem.setAccount(adjustmentAccount);
        } else {
            transactionItem.setAccount(product.getCogsAccount());
        }
        transactionItem.setInventoryId(product.getObjectID());
        transactionItem.setItemId(transactionItemId);
        transactionItem.setCogsItem(true);
        transactionItem.setProject(project);

        if (Constants.TC_OUT.equals(transactionType)) {
            transactionItem.setDebit(COGS);
        } else {
            transactionItem.setCredit(COGS);
        }
        transactionItem.setTransaction(transaction);
        transactionItemManager.create(transactionItem);
    }

    protected void createStockTransferOutTransactionItem(BigDecimal COGS, EdsItem product, EdsTransaction transaction, Integer transactionItemId, boolean fromProductGroup) {
        EdsAdjustmentItem adjustmentItem = stockAdjustmentItemManager.get(transactionItemId);
        if (adjustmentItem == null) {
            return;
        }
        EdsTransactionItem transactionItem = new EdsTransactionItem();
        transactionItem.setAccount(fromProductGroup ? product.getAssetAccount() : adjustmentItem.getAdjustment().getFromAccount());
        transactionItem.setCredit(COGS);
        transactionItem.setCogsItem(true);
        transactionItem.setInventoryId(product.getObjectID());
        transactionItem.setItemId(adjustmentItem.getObjectID());
        transactionItem.setTransaction(transaction);
        transactionItemManager.create(transactionItem);
    }
}
